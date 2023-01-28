package com.bearcode.ovf.webservices.sendgrid;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.webservices.sendgrid.model.SendGridMethod;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author leonid.
 */
@Component
public class SendGridApiConnector {
    private static Logger logger = LoggerFactory.getLogger( SendGridApiConnector.class );

    private String serviceUrl;

    private String authorizationToken;

    @Autowired
    private OvfPropertyService propertyService;

    @Autowired
    private SendGridLogger sendGridLogger;

    /**
     * Do RESTFul API call
     * @param method Api Method
     * @param parameters Path parameters will be integrated into calling URI
     * @param requestBody request body
     * @return JsonObject if there is no error
     */
    public JsonElement callMethod( SendGridMethod method,
                                  Map<String, String> parameters,
                                   String requestBody ) {
        JsonElement result = null;

        UriComponentsBuilder callingUrl = UriComponentsBuilder.fromHttpUrl( getServiceUrl() );

        callingUrl.path( method.getMethodUri( parameters ) );


        HttpMethodBase callingMethod = null;
        switch ( method.getRequestMethod() ) {
            case GET:
                if ( parameters != null && method.isQueryStringParams() ) {
                    for ( String paramName : parameters.keySet() ) {
                        callingUrl.queryParam( paramName, parameters.get( paramName ) );
                    }
                }
                callingMethod = new GetMethod( callingUrl.build().encode().toUriString() );
                break;
            case POST:
                callingMethod = new PostMethod( callingUrl.build().encode().toUriString() );
                try {
                    if ( StringUtils.isNotBlank( requestBody ) ) {
                        ((PostMethod)callingMethod).setRequestEntity( new StringRequestEntity( requestBody, "application/json", null ) );
                    }
                } catch (UnsupportedEncodingException e) {
                    getLogger().error( "Can't create SendGrid Request Body", e );
                    sendGridLogger.error( String.format( "Can't create SendGrid Request Body: %s", e.getMessage() ) );
                    return null;
                }
                break;
            case DELETE:
                callingMethod = new DeleteMethod( callingUrl.build().encode().toUriString() );
                break;
        }

        if ( callingMethod != null ) {
            callingMethod.addRequestHeader( "Authorization", "Bearer " + getAuthorizationToken() );
            callingMethod.addRequestHeader( "Accept", "application/json" );

            HttpClient httpClient = new HttpClient();

            int statusCode = 0;
            try {
                statusCode = httpClient.executeMethod( callingMethod );

                JsonParser parser = new JsonParser();
                if ( statusCode == HttpStatus.SC_OK
                        || statusCode == HttpStatus.SC_CREATED
                        || statusCode == HttpStatus.SC_NO_CONTENT ) {
                    if ( method.isExpectResponseBody() ) {
                        result = parser.parse( new InputStreamReader( callingMethod.getResponseBodyAsStream() ) );
                    }
                    sendGridLogger.info( String.format( "Send Grid answer OK, %d", statusCode) );
                } else {
                    String detail = null;
                    try {
                        if ( method.isExpectResponseBody() ) {
                            JsonObject error = parser.parse( new InputStreamReader( callingMethod.getResponseBodyAsStream() ) ).getAsJsonObject();
                            detail = error.get( "errors" ).getAsString();
                        } else {
                            detail = "No details";
                        }
                    } catch (Exception e) {
                        detail = "Incorrect or missing response ";
                    }
                    getLogger().error( String.format( "Get HTTP error from SendGrid API; HTTP status \"%d\"; Detail \"%s\", Requested: \"%s\"", statusCode, detail, callingMethod.getURI().toString() ) );
                    sendGridLogger.error( String.format( "Get HTTP error from SendGrid API; HTTP status \"%d\"; Detail \"%s\", Requested: \"%s\"", statusCode, detail, callingMethod.getURI().toString() ) );
                }
            } catch (IOException e) {
                sendGridLogger.error( String.format( "Connection error with SendGrid API: %s", e.getMessage() ) );
                getLogger().error( "Connection error with SendGrid API", e );
            } catch (JsonParseException e) {
                sendGridLogger.error( String.format( "Unable to get response from SendGrid API; HTTP status \"%d\": %s", statusCode, e.getMessage() ) );
                getLogger().error( String.format( "Unable to get response from SendGrid API; HTTP status \"%d\"", statusCode ), e );
            }
        }
        return result;
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getServiceUrl() {
        return propertyService.getProperty( OvfPropertyNames.SEND_GRID_API_SERVICE_URL );
    }

    public void setServiceUrl( String serviceUrl ) {
        this.serviceUrl = serviceUrl;
    }

    public String getAuthorizationToken() {
        return propertyService.getProperty( OvfPropertyNames.SEND_GRID_API_AUTH_TOKEN );
    }

    public void setAuthorizationToken( String authorizationToken ) {
        this.authorizationToken = authorizationToken;
    }
}
