package com.bearcode.ovf.tools;

import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.google.gson.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date: 15.09.12
 * Time: 1:23
 *
 * @author Leonid Ginzburg
 */
@Component
public class GetResponseConnector {
    private final Logger logger = LoggerFactory.getLogger( GetResponseConnector.class );

    private String url = "http://api2.getresponse.com/";

    private boolean connectAllowed = false;

    @Autowired
    protected ExceptionNotificationMailList notificationMailList;

    @Autowired
    private EmailService emailService;

    public boolean addContact( MailingList mailingList, MailingAddress mailingAddress ) {
        if ( !connectAllowed ) return false;

        Hashtable<String, Object> contactParams = new Hashtable<String, Object>();
        contactParams.put( "campaign", mailingList.getCampaignId() );
        contactParams.put( "name", mailingAddress.getFirstName() + " " + mailingAddress.getLastName() );
        contactParams.put( "email", mailingAddress.getEmail() );
        contactParams.put( "customs", mailingAddress.prepareGetResponseParams() );

        Object[] requestParams = {mailingList.getApiKey(), contactParams};

        // request object
        Hashtable<String, Object> request = new Hashtable<String, Object>();
        request.put( "method", "add_contact" );
        request.put( "params", requestParams );

        JsonObject result = null;
        try {
            result = callMethod( request );
        } catch (GetResponseException e) {
            logger.error(String.format( "GetResponse: Contact adding failed for email \'%s\' and campaign \'%s\'", mailingAddress.getEmail(), mailingList.getCampaignId() ) );
        }
        return result != null;
    }

    public Map<String, String> getDeletedContacts( String apiKey, Date fromDate ) {
        if ( !connectAllowed ) return Collections.emptyMap();

        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        Hashtable<String, String> operators = new Hashtable<String, String>();
        operators.put( "FROM", dateFormat.format( fromDate ) );

        Hashtable<String, Object> contactParams = new Hashtable<String, Object>();
        contactParams.put( "deleted_on", operators );

        Object[] requestParams = {apiKey, contactParams};

        // request object
        Hashtable<String, Object> request = new Hashtable<String, Object>();
        request.put( "method", "get_contacts_deleted" );
        request.put( "params", requestParams );

        JsonObject result = null;
        try {
            result = callMethod( request );
        } catch (GetResponseException e) {
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        if ( result != null ) {
            Set<Map.Entry<String, JsonElement>> elements = result.entrySet();

            for ( Map.Entry<String, JsonElement> element : elements ) {
                JsonObject contact = element.getValue().getAsJsonObject();
                String email = contact.get( "email" ).getAsString();
                String campaignId = contact.get( "campaign" ).getAsString();
                resultMap.put( email, campaignId );
            }
        }
        return resultMap;
    }

    public Map<String, String> getCampaigns( String apiKey ) {

        Object[] requestParams = {apiKey};

        // request object
        Hashtable<String, Object> request = new Hashtable<String, Object>();
        request.put( "method", "get_campaigns" );
        request.put( "params", requestParams );

        JsonObject result = null;
        try {
            result = callMethod( request );
        } catch (GetResponseException e) {
            sendNotificationEmail( e );
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        if ( result != null ) {
            Set<Map.Entry<String, JsonElement>> elements = result.entrySet();

            for ( Map.Entry<String, JsonElement> element : elements ) {
                JsonObject contact = element.getValue().getAsJsonObject();
                resultMap.put( element.getKey(), contact.get( "name" ).getAsString() );
            }
        }
        return resultMap;
    }

    private JsonObject callMethod( Hashtable<String, Object> request ) throws GetResponseException {
        JsonObject result = null;

        request.put( "id", "id_1" );  // just request id.. don't know why... it doesn't work without it
        request.put( "jsonrpc", "2.0" ); // another silly key. it doesn't work without it

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
                    JsonObject object = parser.parse( reader ).getAsJsonObject();
                    JsonElement error = object.get( "error" );
                    if ( error == null || error.isJsonNull() ) {
                        result = object.getAsJsonObject( "result" );
                    } else {
                        String msg = error.getAsJsonObject().get( "message" ).getAsString();
                        if ( !isContactAlreadyAdded( request.get( "method" ).toString(), msg ) ) {
                            String errorMsg = String.format( "GetResponse: \'%s\' failed with error message: %s ", request.get( "method" ).toString().toUpperCase(), msg );
                            logger.error( errorMsg );
                            throw new GetResponseException( errorMsg );
                        }
                    }
                } catch ( JsonSyntaxException e ) {
                    String responseBody = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
                    String errorMsg = String.format( "GetResponse: \'%s\' failed to parse result: \n %s ", request.get( "method" ).toString().toUpperCase(), responseBody );
                    logger.error( errorMsg, e );
                    throw new GetResponseException( errorMsg, e );
                }
            } else {
                String errorMsg = String.format( "GetResponse: \'%s\' failed to connect server; message: %s ", request.get( "method" ).toString().toUpperCase(), method.getStatusLine() );
                logger.error( errorMsg );
                throw new GetResponseException( errorMsg );
            }
        } catch ( HttpException e ) {
            String errorMsg = "GetResponse: Fatal protocol violation: " + e.getMessage();
            logger.error( errorMsg );
            throw new GetResponseException( errorMsg, e );
        } catch ( IOException e ) {
            String errorMsg = "GetResponse: Fatal transport error: " + e.getMessage();
            logger.error( errorMsg );
            throw new GetResponseException( errorMsg, e );
        } finally {
            // release the connection.
            method.releaseConnection();
        }
        return result;
    }

    private boolean isContactAlreadyAdded( String method, String msg ) {
        return  "ADD_CONTACT".equalsIgnoreCase( method ) && msg.contains( "already added" );
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public void setConnectAllowed( boolean connectAllowed ) {
        this.connectAllowed = connectAllowed;
    }

    private void sendNotificationEmail( Exception e ) {
        List<String> emails = notificationMailList.getMailList();
        for (String toEmail : emails ) {
            try {
                final Email email = Email.builder()
                        .template( EmailTemplates.XML_EXCEPTION)
                        .to(toEmail)
                        .model("requestUrl", "Internal call to " + url + "service. ")
                        .model("queryString", "")
                        .model("exception", e)
                        .build();
                emailService.queue(email);
            } catch (Exception ex) {
                logger.error("Exception message was not sent due to {}.", ex.getMessage());
                logger.error("Original exception : ", e);
            }
        }

    }
}
