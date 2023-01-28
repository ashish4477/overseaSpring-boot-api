package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.model.common.FaceConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: May 12, 2008
 * Time: 5:17:01 PM
 *
 * @author Leonid Ginzburg
 *         example : getDrupalContent.htm?uri=/candidate-information
 */
@Controller
public class CrossPlatformBridge extends BaseController {

    public CrossPlatformBridge() {
        setPageTitle( "Home" );
        setContentBlock( "/WEB-INF/pages/blocks/DrupalContent.jsp" );
        setSectionCss( "/css/drupal.css" );
        setSectionName( "drupal" );
    }

    @RequestMapping("/cms.htm")
    public String drupalBringe( HttpServletRequest request,
                                ModelMap model,
                                @RequestParam(value = "uri", required = false, defaultValue = "") String requestedPage ) throws Exception {
        String responseString = "";
        requestedPage = URLDecoder.decode( requestedPage, "utf-8" );
        if ( !requestedPage.equals( "" ) ) {

            FaceConfig config = getFaceConfig( request );
            String drupalUrl = config.getDrupalUrl();
            if ( drupalUrl.trim().length() == 0 ) {
                config = facesService.findDefaultConfig();
                drupalUrl = config.getDrupalUrl();
            }
            drupalUrl = fixUrlScheme( request, drupalUrl );

            responseString = callDrupalSite( drupalUrl, requestedPage );
          logger.debug(responseString);

            // keep in url only latin chars, -, _
            String pattern = "[^-_a-zA-Z0-9]";
            model.addAttribute( "uriBodyClass", requestedPage.replaceAll( pattern, "" ).toLowerCase() );
        }
        model.addAttribute( "reqContent", responseString );

        return buildModelAndView( request, model );
    }

    @RequestMapping("/eyvCms.htm")
    public String eyvDrupalBridge( HttpServletRequest request,
                                   ModelMap model,
                                   @RequestParam(value = "uri", required = false, defaultValue = "") String requestedPage ) throws Exception {
        drupalBringe( request, model, requestedPage );
        model.addAttribute( TITLE, "Express Your Vote" );
        String oldTemplate = this.mainTemplate;
        this.mainTemplate = "templates/EyvPopupTemplate";
        String viewName = buildModelAndView( request, model );
        this.mainTemplate = oldTemplate;
        return viewName;
    }

    public String fixUrlScheme( HttpServletRequest request, final String drupalUrl ) {
        String result = drupalUrl;
        if ( !drupalUrl.matches( "https?.*" ) ) {
            String scheme = request.getScheme();
//            String header = request.getHeader( "X-Forwarded-Proto" );
//            if ( header != null && header.equalsIgnoreCase( "https" ) ) {
                scheme = "https";
//            }
            result = scheme + "://" + drupalUrl;
        }
        return result;
    }

    private String callDrupalSite( String url, String requestedPage ) {
        String responseString = "";
        try {
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod( url + requestedPage );
            httpClient.executeMethod( method );
            responseString = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
            int start = responseString.indexOf( "<body>" );
            if ( start >= 0 ) {
                start += 6;
                int end = responseString.indexOf( "</body>" );
                if ( end > 0 ) {
                    responseString = responseString.substring( start, end );
                } else {
                    responseString = responseString.substring( start );
                }
            }
        } catch ( IOException e ) {
            logger.info( "Can't connect Drupal Site", e );
            // can't connect Drupal Site - ignore or not?
        }
        return responseString;
    }
}
