package com.bearcode.ovf.webservices.grecaptcha;

import com.bearcode.ovf.webservices.grecaptcha.model.ReCaptchaResponse;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringReader;

/**
 * @author leonid.
 */
@Component
public class GReCaptchaService {

    protected Logger logger = LoggerFactory.getLogger( GReCaptchaService.class );

    @Autowired
    private GReCaptchaConnector connector;

    private Gson gson;

    public GReCaptchaService() {
        gson = new GsonBuilder()
                .setFieldNamingPolicy( FieldNamingPolicy.LOWER_CASE_WITH_DASHES )
                .create();

    }

    public String getGoogleSiteKey() {
        return connector.getGoogleSiteKey();
    }

    public String getGoogleSiteKeyForSkimm() {
        return connector.getGoogleSiteKeyForSkimm();
    }

    public String getGoogleSiteKeyForVote411() {
        return connector.getGoogleSiteKeyForVote411();
    }

    public boolean verifyCaptcha( String captchaToken, String face ) {
        String response = connector.verifyCaptcha( captchaToken, face );
        try {
            if ( response != null ) {
                ReCaptchaResponse responseObj = gson.<ReCaptchaResponse>fromJson( new StringReader( response ), ReCaptchaResponse.class);
                return responseObj != null && responseObj.isSuccess();
            }
        } catch (JsonParseException e) {
            logger.warn( "Can't parse Google reCaptcha response", e );
        }
        return false;
    }
}
