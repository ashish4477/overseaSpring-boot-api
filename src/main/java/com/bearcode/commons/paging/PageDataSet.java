package com.bearcode.commons.paging;

import com.bearcode.commons.util.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Leonid G
 * Date: 30.08.2005
 * Time: 19:06:17
 * @author Leonid Ginzburg
 */
public class PageDataSet {

    private final static Logger log = LoggerFactory.getLogger(PageDataSet.class);

    /**
     * Default parameter's name of reguested page number
     */
    public static final String PAGE_PARAM_NAME = "page";
    /**
     * Default parameter's name of reguested size of page.
     */
    public static final String PAGE_SIZE_PARAM_NAME = "pageSize";

    private String pageParamName = PAGE_PARAM_NAME;
    private String pageSizeParamName = PAGE_SIZE_PARAM_NAME;

    /**
     * Default constructor
     */
    public PageDataSet() {
    }

    /**
     * Constructor with parameters
     * @param pageParamName
     * @param pageSizeParamName
     */
    public PageDataSet(String pageParamName, String pageSizeParamName) {
        this.pageParamName = pageParamName;
        this.pageSizeParamName = pageSizeParamName;
    }

    /**
     * Generate a Map with URLs to all pages in selection
     * @param request HTTP request
     * @param rows Number of total rows in selection
     * @param pageSize Number of rows for one page.
     * @return a Map with URLs. Key is page's number, value is an URL. For current selected page URL is empty.
     */
    public Map<String,String> generatePages( HttpServletRequest request, int rows, int pageSize ) {
        Map<String,String> pages = new LinkedHashMap<String,String>( );

        int currentPage = getCurrentPage( request );

        int numberOfPages = (int) Math.ceil( (double) rows / (double) pageSize );
        for ( int i = 0; i < numberOfPages; i++ ) {
            String url = generatePageLink( request, i );
            if ( i == currentPage ) {
                url = "";
            }
            pages.put( ""+(i+1), url );
        }
        return pages;
    }

    /**
     * Generate an URL to page with selected page number
     * @param request
     * @param pageNo
     * @return URL with all desired paremeters
     */
    public String generatePageLink( HttpServletRequest request, int pageNo ) {
        StringBuffer link = new StringBuffer( request.getRequestURI() );
        link.append( "?" ).append( pageParamName ).append( "=" ).append( pageNo );
        Set<Map.Entry> entries = request.getParameterMap().entrySet();
        for( Iterator entryIt = entries.iterator(); entryIt.hasNext(); ) {
            Map.Entry entry = (Map.Entry) entryIt.next();
            if ( !pageParamName.equals( entry.getKey() ) ) {
                Object value = entry.getValue();
                if ( value instanceof Object[] ) {
                    for( int k=0; k < ((Object[]) value).length; k++ ) {
                        link.append( "&" ).append( entry.getKey() ).append( "=" ).append( ((Object[]) value)[k] );
                    }
                }
                else {
                    link.append( "&" ).append( entry.getKey() ).append( "=" ).append( value );
                }
            }
        }
        return link.toString();
    }

    /**
     *
     * @param request HTTP request
     * @return  requested page number
     */
    public int getCurrentPage( HttpServletRequest request ) {
        MapUtils.getInteger( request.getParameterMap(), pageParamName, 0 );
        return 0;
    }

    /**
     *
     * @param request
     * @param defaultSize
     * @return reguested size of page
     */
    public int getCurrentPageSize( HttpServletRequest request, int defaultSize ) {
        return MapUtils.getInteger( request.getParameterMap(), pageSizeParamName, defaultSize );
    }


    public String getPageParamName() {
        return pageParamName;
    }

    public void setPageParamName(String pageParamName) {
        this.pageParamName = pageParamName;
    }

    public String getPageSizeParamName() {
        return pageSizeParamName;
    }

    public void setPageSizeParamName(String pageSizeParamName) {
        this.pageSizeParamName = pageSizeParamName;
    }
}
