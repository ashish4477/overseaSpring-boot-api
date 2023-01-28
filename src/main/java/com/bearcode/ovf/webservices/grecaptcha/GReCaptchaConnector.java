package com.bearcode.ovf.webservices.grecaptcha;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author leonid.
 */
@Component
public class GReCaptchaConnector {

    protected Logger logger = LoggerFactory.getLogger( GReCaptchaConnector.class );

    @Autowired
    private OvfPropertyService propertyService;

    @Value( "${reCaptcha.siteKey:6LehECgUAAAAAG_59yA3GjlPhPGqp1PhyiuiW0pu}" )
    private String googleSiteKey;
    @Value( "${reCaptcha.siteKeyForSkimm:6Le6Qq4ZAAAAAIXqPzB87Iyfvsauo46n7KcThAxy}" )
    private String googleSiteKeyForSkimm;
    @Value( "${reCaptcha.siteKeyForVote411:6LcI3s4ZAAAAACkMv7soCVg4Xdt3_BFqN7ld-vqz}" )
    private String googleSiteKeyForVote411;

    @Value( "${reCaptcha.secretKey:6LehECgUAAAAAEix-gSdmYDIqxdPNUMtNy-knifl}" )
    private String googleSecretKey;
    @Value( "${reCaptcha.secretKeyForSkimm:6Le6Qq4ZAAAAAFlvjT5y0ZDWHW9ekM4ByThgxxy2}" )
    private String googleSecretKeyForSkimm;
    @Value( "${reCaptcha.secretKeyForVote411:6LcI3s4ZAAAAACuahrb5c_K8F4SR0BqyxUrVsv_w}" )
    private String googleSecretKeyForVote411;

    @Value( "${reCaptcha.serviceUrl:https://www.google.com/recaptcha/api/siteverify}" )
    private String googleCaptchaUrl;

    private static final String SKIMM = "skimm";

    private static final String VOTE411 = "vote411";

    public String verifyCaptcha( String captchaToken, String face ) {

        PostMethod postMethod = new PostMethod( googleCaptchaUrl );

        postMethod.addParameter( "secret", getGoogleSecretKeyFromFace(face) );
        postMethod.addParameter( "response", captchaToken );

        HttpClient httpClient = new HttpClient();

        int statusCode = 0;

        try {
            statusCode = httpClient.executeMethod( postMethod );
            if ( statusCode == HttpStatus.SC_OK ) {
                return IOUtils.toString( postMethod.getResponseBodyAsStream(), Charsets.UTF_8 ) ;
            }
            logger.error( String.format( "Unexpected response from Google ReCaptcha service. HTTP status %d", statusCode ) );
        } catch (IOException e) {
            logger.error( "Can't connect Google ReCaptcha service", e );
        }
        return null;
    }


    public String getGoogleSiteKey() {
        return googleSiteKey;
        // return propertyService.getProperty( OvfPropertyNames.RE_CAPTCHA_SITE_KEY );
    }

    public String getGoogleSiteKeyForSkimm() {
        return googleSiteKeyForSkimm;
        // return propertyService.getProperty( OvfPropertyNames.RE_CAPTCHA_SITE_KEY_SKIMM );
    }

    public String getGoogleSiteKeyForVote411() {
        return googleSiteKeyForVote411;
        // return propertyService.getProperty( OvfPropertyNames.RE_CAPTCHA_SITE_KEY_VOTE411 );
    }

    public void setGoogleSiteKey( String googleSiteKey ) {
        this.googleSiteKey = googleSiteKey;
    }
    public void setGoogleSiteKeyForSkimm( String googleSiteKeyForSkimm ) {
        this.googleSiteKeyForSkimm = googleSiteKeyForSkimm;
    }
    public void setGoogleSiteKeyForVote411( String googleSiteKeyForVote411 ) {
        this.googleSiteKeyForVote411 = googleSiteKeyForVote411;
    }

    public String getGoogleSecretKey(){
        return googleSecretKey;
        // return propertyService.getProperty( OvfPropertyNames.RE_CAPTCHA_SECRET_KEY ); 
    }

    public String getGoogleSecretKeyForSkimm(){
        return googleSecretKeyForSkimm;
        // return propertyService.getProperty( OvfPropertyNames.RE_CAPTCHA_SECRET_KEY_SKIMM );
    }

    public String getGoogleSecretKeyForVote411(){
        return googleSecretKeyForVote411;
        // return propertyService.getProperty( OvfPropertyNames.RE_CAPTCHA_SECRET_KEY_VOTE411 );
    }

    public void setGoogleSecretKey( String googleSecretKey ) {
        this.googleSecretKey = googleSecretKey;
    }
    public void setGoogleSecretKeyForSkimm( String googleSecretKeyForSkimm ) {
        this.googleSecretKeyForSkimm = googleSecretKeyForSkimm;
    }
    public void setGoogleSecretKeyForVote411( String googleSecretKeyForVote411 ) {
        this.googleSecretKeyForVote411 = googleSecretKeyForVote411;
    }

    public String getGoogleSecretKeyFromFace(String face) {
        //return googleSecretKey;

        String key = "";
        if (face.contains(SKIMM)) {
            key = googleSecretKeyForSkimm;
        } else if (face.contains(VOTE411)) {
            key = googleSecretKeyForVote411;
        } else {
            key = googleSecretKey;
        }
        return key;
    }

    public String getGoogleCaptchaUrl() {
        return googleCaptchaUrl;
        // return propertyService.getProperty( OvfPropertyNames.RE_CAPTCHA_SERVICE_URL );
    }

    public void setGoogleCaptchaUrl( String googleCaptchaUrl ) {
        this.googleCaptchaUrl = googleCaptchaUrl;
    }
}
