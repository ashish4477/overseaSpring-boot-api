package com.bearcode.ovf.webservices.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Base class for connect to RESTFul API server
 */
public abstract class BaseRestfulApiConnector {

    abstract public String getServiceUrl();
    abstract public String getAuthorizationToken();
    abstract public Logger getLogger();

    // return formatted JSON object from the API
    private boolean debugFormattedJSON;

    // always return the latest version of the data (ignore cache)
    private boolean debugNoCache;


    public boolean isDebugFormattedJSON() {
        return debugFormattedJSON;
    }

    public void setDebugFormattedJSON(boolean debugFormattedJSON) {
        this.debugFormattedJSON = debugFormattedJSON;
    }

    public boolean isDebugNoCache() {
        return debugNoCache;
    }

    public void setDebugNoCache(boolean debugNoCache) {
        this.debugNoCache = debugNoCache;
    }


    public JsonObject callMethod( String methodPath, String additionalParam, Map<String, String> parameters ) {
        return callMethod( methodPath + "/" + additionalParam, parameters );
    }
    /**
     * Do RESTFul API call
     * @param methodPath Method path
     * @param parameters Call parameters (will be added to url after '?')
     * @return JsonObject if there is no error
     */
    public JsonObject callMethod(String methodPath, Map<String, String> parameters ) {

    /*
     *  fix for
     *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
     *       sun.security.validator.ValidatorException:
     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
     *               unable to find valid certification path to requested target
     */
        /*try { //we do not need it any more. keep the code for a while
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier( allHostsValid );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/
    /*
     * end of the fix
     */

        JsonObject result = null;

        UriComponentsBuilder callingUrl = UriComponentsBuilder.fromHttpUrl( getServiceUrl() );

        callingUrl.path( "/" + methodPath );

        for ( String paramName : parameters.keySet() ) {
            callingUrl.queryParam( paramName, parameters.get( paramName ) );
        }

        GetMethod method = new GetMethod( callingUrl.build().encode().toUriString() );

        method.addRequestHeader( "Authorization", getAuthorizationToken() );
        if (this.debugFormattedJSON) {
            method.addRequestHeader( "Accept", "application/json; indent=4" );
        } else {
            method.addRequestHeader( "Accept", "application/json" );
        }

        if (this.debugNoCache) {
            method.addRequestHeader( "Cache-Control", "no-cache" );
        }

        HttpClient httpClient = new HttpClient();

        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod( method );

            JsonParser parser = new JsonParser();
            if ( statusCode == HttpStatus.SC_OK ) {
                result = parser.parse( new InputStreamReader( method.getResponseBodyAsStream() ) ).getAsJsonObject();
            } else if ( statusCode != HttpStatus.SC_NOT_FOUND) {
                // NOT_FOUND is not an error, it might be incorrect ID value
                String detail = null;
                try {
                    JsonObject error = parser.parse( new InputStreamReader( method.getResponseBodyAsStream() ) ).getAsJsonObject();
                    detail = error.get( "detail" ).getAsString();
                } catch (Exception e) {
                    detail = "No details";
                }
                getLogger().error( String.format( "Unable to get response from RESTfull API; HTTP status \"%d\"; Detail \"%s\", Requested: \"%s\"", statusCode, detail, method.getURI().toString() ) );
            }
        } catch (IOException e) {
            getLogger().error( "Connection error with localelection API", e );
        } catch (JsonParseException e) {
            getLogger().error( String.format( "Unable to get response from RESTfull API; HTTP status \"%d\"", statusCode ), e );
        }
        return result;
    }
}
