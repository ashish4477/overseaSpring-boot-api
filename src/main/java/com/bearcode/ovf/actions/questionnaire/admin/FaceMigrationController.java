package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.admin.forms.FaceMigrationContext;
import com.bearcode.ovf.tools.FaceMigrationDealer;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Date: 26.12.11
 * Time: 19:13
 *
 * @author Leonid Ginzburg
 */
@Controller
public class FaceMigrationController extends BaseController {

    @Autowired
    private FaceMigrationDealer migrationDealer;

    public FaceMigrationController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/FacesMigration.jsp" );
        setPageTitle( "Faces Migration" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("facesMigrationContext")
    public FaceMigrationContext getContext() {
        return new FaceMigrationContext();
    }

    @RequestMapping(value = "/admin/FacesMigration.htm", method = RequestMethod.GET)
    public String handleGetMigration( HttpServletRequest request,
                                      ModelMap model ) {
        return buildModelAndView( request, model );
    }


    @RequestMapping(value = "/admin/FacesMigration.htm", method = RequestMethod.POST)
    public String handlePostMigration( HttpServletRequest request,
                                       ModelMap model,
                                       @RequestParam("migrationZip") MultipartFile zipFile,
                                       @ModelAttribute("facesMigrationContext") FaceMigrationContext context ) {
        try {
            ZipInputStream zipIn = new ZipInputStream( zipFile.getInputStream() );
            ZipEntry entry = zipIn.getNextEntry();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len;
            while ( (len = zipIn.read( buff )) > 0 ) {
                out.write( buff, 0, len );
            }
            zipIn.close();

            Gson gson = new Gson();
            context = gson.fromJson( out.toString(), FaceMigrationContext.class );

            migrationDealer.doImport( context );
            if ( context.getMessages().isEmpty() ) {
                context.getMessages().add( "Migration has been successfully finished." );
            } else {
                context.getMessages().add( "Migration has been finished with some issues." );
            }
        } catch ( Exception e ) {
            logger.error( "Can't complete migration. ", e );
            context.getMessages().add( "Migration process was not finished." );
        }
        model.put( "facesMigrationContext", context );
        return buildModelAndView( request, model );
    }

    @RequestMapping(value = "/admin/GetFacesFile.do", method = RequestMethod.GET)
    public ResponseEntity<byte[]> doExport( @ModelAttribute("facesMigrationContext") FaceMigrationContext context ) throws Exception {

        migrationDealer.doExport( context );

        Gson gson = new Gson();
        String json = gson.toJson( context );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream( out );

        zip.putNextEntry( new ZipEntry( "faceMigrationContext.json" ) );
        zip.write( json.getBytes() );
        zip.closeEntry();

        zip.close();
        out.close();

        SimpleDateFormat df = new SimpleDateFormat( "yyyy_MM_dd" );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( new MediaType( "application", "force-download" ) );
        headers.add( "Content-Disposition", String.format( "attachment; filename=face_migration%s.zip", df.format( new Date() ) ) );
        headers.add( "Content-Transfer-Encoding", "binary" );

        return new ResponseEntity<byte[]>( out.toByteArray(), headers, HttpStatus.CREATED );
    }

}
