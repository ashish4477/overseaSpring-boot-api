package com.bearcode.ovf.tools;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Date: 26.09.14
 * Time: 15:07
 *
 * Help Home page to get secondary content (from Drupal site) using cache
 *
 * @author Leonid Ginzburg
 */
@Component
public class HomeSecondaryContentHelper {

    protected Logger logger = LoggerFactory.getLogger( HomeSecondaryContentHelper.class );

    @Cacheable(value = "getSecondaryContent", key = "#requestedPath"  )
    public String getSecondaryContent( String requestedPath, HttpClient httpClient, String environment ) {
        try {
            GetMethod method = new GetMethod( requestedPath );
            httpClient.executeMethod( method );
            String responseString = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
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
            return responseString;
        } catch (IOException e) {
            if ( !environment.equalsIgnoreCase( "staging" ) ) {
                logger.error( "Unable to get content from " + requestedPath, e );
            }
        }
        return "";
    }

}
