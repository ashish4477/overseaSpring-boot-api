package com.bearcode.ovf.webservices;

import com.bearcode.ovf.model.common.Address;
import com.google.gson.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;

/**
 * Date: 17.12.13
 * Time: 21:49
 *
 * @author Leonid Ginzburg
 */
@Component
public class SmartyStreetService {
    private final Logger logger = LoggerFactory.getLogger(SmartyStreetService.class);

    private String serviceUrl = "https://api.smartystreets.com/street-address"; 
    private String authId ="35927c6d-03e0-74ca-268d-d551ae41ba45";
    private String authToken = "1S8aPFOxicE4ZgVI62zT";

    private static final String URL_PATTERN = "%s?auth-id=%s&auth-token=%s";
    public static final String[] EMPTY = new String[] {"", "", ""};
    private static final String ALL_DISTRICT = "1";

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    // This function is used for Candidate Finder and Candidate Register
    public String[] findDistrict( String address, String city, String state, String zip ) {
        Hashtable<String, String> addressParams = new Hashtable<String, String>();
        Object[] request = new Object[1];
        request[0] = addressParams;

        addressParams.put( "street", address );
        addressParams.put( "city", city );
        addressParams.put( "state", state );
        addressParams.put( "zipcode", zip );

        JsonObject result = callMethod( request );
        if ( result != null  ) {
            JsonObject components = result.get( "components" ).getAsJsonObject();
            JsonObject meta = result.get( "metadata" ).getAsJsonObject();
            JsonObject analysis = result.get( "analysis" ).getAsJsonObject();

            String zipVerified =  components.get( "zipcode" ).getAsString().trim();
            String plus4 = components.get( "plus4_code" ).getAsString().trim();

            String district =  meta.get( "congressional_district" ).getAsString();

            String active =  analysis.get( "active" ).getAsString();

            if ( district.equalsIgnoreCase("AL") ) {
                district = ALL_DISTRICT;
            }
            if (district.startsWith("0")) {
                district = district.substring(1); // remove leading zero
            }
            return new String[]{district, zipVerified, plus4};
        }

        return EMPTY;
    }

    private JsonObject callMethod(Object[] request) {
        JsonObject result = null;

        String url = String.format( URL_PATTERN, serviceUrl, authId, authToken );
        // create a method instance.
        PostMethod method = new PostMethod( url );
        Gson gson = new Gson();

        // new instance of HttpClient.
        HttpClient client = new HttpClient();
        try {
            method.setRequestEntity(new StringRequestEntity( gson.toJson(request), "application/json", "UTF-8"  ));
            // execute the method.
            int statusCode = client.executeMethod( method );

            if ( statusCode == HttpStatus.SC_OK ) {
                try {
                    JsonParser parser = new JsonParser();
                    Reader reader = new InputStreamReader( method.getResponseBodyAsStream(), "UTF-8" );
                    JsonArray object = parser.parse( reader ).getAsJsonArray();
                    if ( object.size() >= 1 ) {
                        result = object.get(0).getAsJsonObject();
                    }
                } catch ( JsonSyntaxException e ) {

                    String responseBody = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
                    logger.error( String.format( "SmartyStreet: failed to parse result: \n %s ", responseBody ), e );
                }
            } else {
                logger.error( String.format( "SmartyStreet: failed to connect server; message: %s ", method.getStatusLine() ) );
            }
        } catch ( HttpException e ) {
            logger.error( "SmartyStreet: Fatal protocol violation: " + e.getMessage() );
        } catch ( IOException e ) {
            logger.error( "SmartyStreet: Fatal transport error: " + e.getMessage() );
        } finally {
            // release the connection.
            method.releaseConnection();
        }

        return result;
    }

    public boolean verifyAddress( Address address ) {
        Hashtable<String, String> addressParams = new Hashtable<String, String>();
        Object[] request = new Object[1];
        request[0] = addressParams;

        addressParams.put( "street", address.getStreet1() );
        if ( address.getStreet2().length() > 0 ) {
            addressParams.put( "street2", address.getStreet2() );
        }
        addressParams.put( "city", address.getCity() );
        addressParams.put( "state", address.getState() );
        addressParams.put( "zipcode", address.getZip() );

        JsonObject result = callMethod( request );

        return parseAddress( address, result );
    }

    private boolean parseAddress( Address address, JsonObject answer ) {
        if ( answer != null && address != null ) {
            address.setStreet1( answer.get("delivery_line_1").getAsString() );
            if ( answer.get("delivery_line_2") != null ) {
                address.setStreet2( answer.get( "delivery_line_2" ).getAsString() );
            }
            JsonObject components = answer.get( "components" ).getAsJsonObject();
            address.setCity( components.get( "city_name" ).getAsString() );
            address.setZip( components.get( "zipcode" ).getAsString() );
            address.setZip4( components.get( "plus4_code" ).getAsString() );
            return true;
        }
        return false;
    }
}
