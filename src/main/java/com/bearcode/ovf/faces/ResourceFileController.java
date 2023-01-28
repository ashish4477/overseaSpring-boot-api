package com.bearcode.ovf.faces;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.service.FacesService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletConfigAware;

import static com.google.common.net.HttpHeaders.*;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 8, 2007
 * Time: 3:41:14 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping(value = {"/flash/**/*.jpg", "**/*.xml", "**/*.swf", "**/*.css", "**/*.js", "**/*.htc", "/img/**/*.gif", "/img/**/*.jpg", "/img/**/*.ico", "/img/**/*.png", "/img/**/*.svg", "/scripts/**/*.png", "/scripts/**/*.gif", "/css/**/*.png","**/*.woff","**/*.woff2"} )
public class ResourceFileController implements ServletConfigAware {

    private static final String MULTIPART_CONTENT_RANGE_REGEX = "^bytes=\\d*-\\d*(,\\d*-\\d*)*$";
    private static final String MULTIPART_CONTENT_RANGE = "bytes %d-%d/%d";
    private static final String MULTIPART_CONTENT_RANGE_FULL = "bytes */%d";

    private static final MediaType MULTIPART_CONTENT_TYPE = MediaType.parseMediaType( "multipart/byteranges; boundary=MULTIPART_BYTERANGES" );

    private static final String MULTIPART_BOUNDARY_HEADER = 
            "\n" +
            "--MULTIPART_BYTERANGES\n" +
            "Content-Type: %s\n" +
            "Content-Range: bytes %d-%d/%d\n";
            
    private static final String MULTIPART_BOUNDARY_END = 
            "\n" +
            "--MULTIPART_BYTERANGES--\n";

    private static final long EXPIRE_TIME = 604800000L; // ..ms = 1 week.
    private static final String WEB_INF = "/WEB-INF/";

    private String basePath;

    @Autowired
    private FacesService facesService;

    private final MimetypesFileTypeMap mimetypes = new MimetypesFileTypeMap() {{
        addMimeTypes("image/vnd.microsoft.icon ico ICO");
        addMimeTypes("image/png png PNG");
        addMimeTypes("image/svg+xml svg SVG");
        addMimeTypes("text/xml xml XML");
        addMimeTypes("text/css css CSS");
        addMimeTypes("text/javascript js JS");
        addMimeTypes("text/x-component htc HTC");
        addMimeTypes("application/x-shockwave-flash swf SWF");
    }};

    public void setFacesService( FacesService facesService ) {
        this.facesService = facesService;
    }

    @RequestMapping( method = RequestMethod.GET )
    public ResponseEntity<byte[]> handleRequestInternal( HttpServletRequest request, HttpEntity<byte[]> reqHttpEntity ) throws IOException {
        return produceResponse( request, reqHttpEntity, true );
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<byte[]> answerToHeadRequest( HttpServletRequest request, HttpEntity<byte[]> reqHttpEntity  ) throws IOException {
        return produceResponse( request,  reqHttpEntity, false );
    }

    private String createETag(String serverPath, String fileName, long length, long lastModified) {
        String eTag = String.format("%s/%s_%d_%d", serverPath, fileName, length, lastModified);
        return String.format("\"%s\"", DigestUtils.md5DigestAsHex( eTag.getBytes() ));
    }

    private ResponseEntity<byte[]> produceResponse( HttpServletRequest request, HttpEntity<byte[]> reqHttpEntity, boolean content ) throws IOException {

        final String requestPath = request.getServletPath();
        final String serverPath = request.getServerName() + request.getContextPath();
        final HttpHeaders requestHeaders = reqHttpEntity.getHeaders();

        File file = getResourceAsFile( requestPath, serverPath );
        if ( !file.exists() ) {
            return new ResponseEntity<byte[]>( HttpStatus.NOT_FOUND );
        }
        if( file.length() > Integer.MAX_VALUE ) {
            return new ResponseEntity<byte[]>( HttpStatus.REQUEST_ENTITY_TOO_LARGE );
        }

        HttpHeaders headers = new HttpHeaders();

        // Prepare some variables. The ETag is an unique identifier of the file.
        final String fileName = file.getName();
        final long length = file.length();
        final long lastModified = file.lastModified();
        final String eTag = this.createETag(serverPath, fileName, length, lastModified);

        // Validate request headers for caching ---------------------------------------------------

        // If-None-Match header should contain "*" or ETag. If so, then return 304.
        String ifNoneMatch = requestHeaders.getFirst( IF_NONE_MATCH );
        if ( ifNoneMatch != null && matches( ifNoneMatch, eTag ) ) {
            headers.setETag( eTag ); // Required in 304.
            return new ResponseEntity<byte[]>( headers, HttpStatus.NOT_MODIFIED );
        }

        // If-Modified-Since header should be greater than LastModified. If so, then return 304.
        // This header is ignored if any If-None-Match header is specified.
        long ifModifiedSince = requestHeaders.getIfModifiedSince();
        if ( ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
            headers.setETag( eTag ); // Required in 304.
            return new ResponseEntity<byte[]>( headers, HttpStatus.NOT_MODIFIED );
        }

        // Validate request headers for resume ----------------------------------------------------

        // If-Match header should contain "*" or ETag. If not, then return 412.
        String ifMatch = requestHeaders.getFirst( IF_MATCH );
        if ( ifMatch != null && !matches( ifMatch, eTag ) ) {
            return new ResponseEntity<byte[]>( HttpStatus.PRECONDITION_FAILED );
        }

        // If-Unmodified-Since header should be greater than LastModified. If not, then return 412.
        long ifUnmodifiedSince = requestHeaders.getFirstDate( IF_UNMODIFIED_SINCE );
        if ( ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified ) {
            return new ResponseEntity<byte[]>( HttpStatus.PRECONDITION_FAILED );
        }

        // Validate and process range -------------------------------------------------------------

        // Prepare some variables. The full Range represents the complete file.
        final Range full = new Range(0, length - 1, length);
        final List<Range> ranges = new ArrayList<Range>();

        // Validate and process Range and If-Range headers.
        String range = requestHeaders.getFirst( RANGE );
        if (range != null) {

            // Range header should match format "bytes=n-n,n-n,n-n...". If not, then return 416.
            if (!range.matches(MULTIPART_CONTENT_RANGE_REGEX)) {
                headers.set( CONTENT_RANGE, String.format(MULTIPART_CONTENT_RANGE_FULL, length) ); // Required in 416.
                return new ResponseEntity<byte[]>( headers, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE );
            }

            // If-Range header should either match ETag or be greater then LastModified. If not,
            // then return full file.
            String ifRange = requestHeaders.getFirst(IF_RANGE);
            if (ifRange != null && !ifRange.equals(eTag)) {
                try {
                    long ifRangeTime = requestHeaders.getFirstDate(IF_RANGE); // Throws IAE if invalid.
                    if (ifRangeTime != -1 && ifRangeTime + 1000 < lastModified) {
                        ranges.add(full);
                    }
                } catch (IllegalArgumentException ignore) {
                    ranges.add(full);
                }
            }

            // If any valid If-Range header, then process each part of byte range.
            if (ranges.isEmpty()) {
                for (String part : range.substring(6).split(",")) {
                    // Assuming a file with length of 100, the following examples returns bytes at:
                    // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
                    long start = sublong(part, 0, part.indexOf("-"));
                    long end = sublong(part, part.indexOf("-") + 1, part.length());

                    if (start == -1) {
                        start = length - end;
                        end = length - 1;
                    } else if (end == -1 || end > length - 1) {
                        end = length - 1;
                    }

                    // Check if Range is syntactically valid. If not, then return 416.
                    if (start > end) {
                        headers.set( CONTENT_RANGE, String.format(MULTIPART_CONTENT_RANGE_FULL, length) ); // Required in 416.
                        return new ResponseEntity<byte[]>( headers, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE );
                    }

                    // Add range.
                    ranges.add(new Range(start, end, length));
                }
            }
        }

        headers.set( ACCEPT_RANGES, "bytes" );
        headers.setLastModified( lastModified );
        headers.setExpires( System.currentTimeMillis() + EXPIRE_TIME );
        headers.setContentType( MediaType.parseMediaType( mimetypes.getContentType( requestPath ) ) );
        headers.setETag( eTag );

        byte[] bytes = null;
        if ( content ) {
            InputStream inputStream = new FileInputStream( file );
            bytes = IOUtils.toByteArray( inputStream );
            IOUtils.closeQuietly( inputStream );
        }

        if (ranges.isEmpty() || ranges.get(0) == full) {

            // Return full file.
            if (range != null) {
                headers.set( CONTENT_RANGE, String.format(MULTIPART_CONTENT_RANGE, full.start, full.end, full.total) );
            }

            if (content) {
                // send resource to the client
                return new ResponseEntity<byte[]>( bytes, headers, HttpStatus.OK );
            } else {
                return new ResponseEntity<byte[]>( headers, HttpStatus.OK );
            }

        } else if (ranges.size() == 1) {

            // Return single part of file.
            Range r = ranges.get(0);
            headers.set( CONTENT_RANGE, String.format(MULTIPART_CONTENT_RANGE, r.start, r.end, r.total));
            headers.setContentLength( r.length );
            if ( content ) {
                // Copy single part range.
                return new ResponseEntity<byte[]>(
                        Arrays.copyOfRange( bytes, Long.valueOf(r.start).intValue() , Long.valueOf(r.end).intValue() ),
                        headers, HttpStatus.PARTIAL_CONTENT );
            } else {
                return new ResponseEntity<byte[]>( headers, HttpStatus.PARTIAL_CONTENT );
            }

        } else {

            // Return multiple parts of file.
            headers.setContentType( MULTIPART_CONTENT_TYPE );

            if (content) {
                // Cast back to ServletOutputStream to get the easy println methods.
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                // Copy multi part range.
                for (Range r : ranges) {
                    // Add multipart boundary and header fields for every range.
                    out.write( String.format(MULTIPART_BOUNDARY_HEADER, 
                        mimetypes.getContentType( requestPath ),
                        r.start, r.end, r.total).getBytes() );

                    out.write( Arrays.copyOfRange( bytes, Long.valueOf(r.start).intValue() , Long.valueOf(r.end).intValue() ) );
                }

                // End with multipart boundary.
                out.write( MULTIPART_BOUNDARY_END.getBytes() );

                return new ResponseEntity<byte[]>( out.toByteArray(), headers, HttpStatus.PARTIAL_CONTENT );
            } else {
                return new ResponseEntity<byte[]>( headers, HttpStatus.PARTIAL_CONTENT );
            }
        }
    }

    private File getResourceAsFile(final String requestPath, final String serverPath) {
        final FaceConfig currentConfig = facesService.findConfig(serverPath);
        File resource = new File( basePath + WEB_INF + currentConfig.getRelativePrefix() + requestPath);
        if ( !resource.exists() ) {
            final FaceConfig defaultConfig = facesService.findDefaultConfig();
            resource = new File( basePath + WEB_INF + defaultConfig.getRelativePrefix() + requestPath);
        }
        return resource;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        final ServletContext servletContext = servletConfig.getServletContext();
        basePath = servletContext.getRealPath("");
        // Validate base path.
        if (this.basePath != null) {
            File path = new File(this.basePath);
            if ( !path.exists() || !path.isDirectory() || !path.canRead() ) {
                // path does not exist, is not a dir or can't read....
                // set basePath to NULL, subsequent request will return NOT_FOUND
                basePath = null;
            }
        }
    }

    /**
     * Returns true if the given match header matches the given value.
     * @param matchHeader The match header.
     * @param toMatch The value to be matched.
     * @return True if the given match header matches the given value.
     */
    private static boolean matches(String matchHeader, String toMatch) {
        String[] matchValues = matchHeader.split("\\s*,\\s*");
        Arrays.sort(matchValues);
        return Arrays.binarySearch(matchValues, toMatch) > -1
                || Arrays.binarySearch(matchValues, "*") > -1;
    }


    /**
     * Returns a substring of the given string value from the given begin index to the given end
     * index as a long. If the substring is empty, then -1 will be returned
     * @param value The string value to return a substring as long for.
     * @param beginIndex The begin index of the substring to be returned as long.
     * @param endIndex The end index of the substring to be returned as long.
     * @return A substring of the given string value as long or -1 if substring is empty.
     */
    private static long sublong(String value, int beginIndex, int endIndex) {
        String substring = value.substring(beginIndex, endIndex);
        return (substring.length() > 0) ? Long.parseLong(substring) : -1;
    }
    
    // Inner classes ------------------------------------------------------------------------------

    /**
     * This class represents a byte range.
     */
    protected class Range {
        long start;
        long end;
        long length;
        long total;

        /**
         * Construct a byte range.
         * @param start Start of the byte range.
         * @param end End of the byte range.
         * @param total Total length of the byte source.
         */
        public Range(long start, long end, long total) {
            this.start = start;
            this.end = end;
            this.length = end - start + 1;
            this.total = total;
        }
    }
}
