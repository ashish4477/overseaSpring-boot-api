package com.bearcode.ovf.actions.mail;

import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.model.mail.MailingLinkStatus;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.tools.GetResponseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date: 10.07.12
 * Time: 18:05
 *
 * @author Leonid Ginzburg
 */
@Component
@Deprecated
public class SynchronizeUnsubscribers {

    private final Logger logger = LoggerFactory.getLogger( SynchronizeUnsubscribers.class );

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private GetResponseConnector getResponseConnector;

//    private String url = "http://api2.getresponse.com/";

    public void getDeletedContacts() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar fromDate = Calendar.getInstance();
        fromDate.add( Calendar.DAY_OF_MONTH, -2 );

        List<String> apiKeys = mailingListService.findMailingApiKeys();

        for ( String apiKey : apiKeys ) {
            Map<String,String> removedContacts = getResponseConnector.getDeletedContacts( apiKey, fromDate.getTime() );
            for ( Map.Entry<String, String> contact : removedContacts.entrySet() ) {

                removeFromSubscription( contact.getKey(), contact.getValue(), apiKey );
            }

/*
            // new instance of HttpClient.
            HttpClient client = new HttpClient();

            Hashtable<String, String> operators = new Hashtable<String, String>();
            operators.put( "FROM", dateFormat.format( fromDate.getTime() ) );


            Hashtable<String, Object> contactParams = new Hashtable<String, Object>();
            contactParams.put( "deleted_on", operators );

            Object[] requestParams = {apiKey, contactParams};

            // request object
            Hashtable<String, Object> request = new Hashtable<String, Object>();
            request.put( "method", "get_contacts_deleted" );
            request.put( "params", requestParams );

            Gson gson = new Gson();

            // create a method instance.
            PostMethod method = new PostMethod( url );
            method.setRequestBody( gson.toJson( request ) );

            try {
                // execute the method.
                int statusCode = client.executeMethod( method );

                if ( statusCode == HttpStatus.SC_OK ) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject object = parser.parse( method.getResponseBodyAsString() ).getAsJsonObject();
                        JsonElement error = object.get( "error" );
                        if ( error.isJsonNull() ) {
                            JsonObject result = object.getAsJsonObject( "result" );
                            Set<Map.Entry<String, JsonElement>> elements = result.entrySet();

                            for ( Map.Entry<String, JsonElement> element : elements ) {
                                JsonObject contact = element.getValue().getAsJsonObject();
                                String email = contact.get( "email" ).getAsString();
                                String campaignId = contact.get( "campaign" ).getAsString();

                                removeFromSubscription( email, campaignId, apiKey );
                            }
                        }
                        else {
                            String msg = error.getAsJsonObject().get( "message" ).getAsString();
                            logger.error( String.format( "GetResponse: failed to get deleted contacts; message: %s ", msg ) );
                        }
                    } catch ( JsonSyntaxException e ) {
                        logger.error( String.format( "GetResponse: failed to parse result: %s ", method.getResponseBodyAsString() ), e );
                    }
                } else {
                    logger.error( String.format( "GetResponse: failed to get deleted contacts; message: %s ", method.getStatusLine() ) );
                }

            } catch ( HttpException e ) {
                logger.error( "GetResponse: Fatal protocol violation: " + e.getMessage() );
            } catch ( IOException e ) {
                logger.error( "GetResponse: Fatal transport error: " + e.getMessage() );
            } finally {
                // release the connection.
                method.releaseConnection();
            }
*/
        }

    }

    private void removeFromSubscription( String email, String campaignId, String apiKey ) {
        MailingLink link = mailingListService.findLinkByCompaignAndEmail( apiKey, campaignId, email );
        if ( link != null ) {
            link.setStatus( MailingLinkStatus.UNSUBSCRIBED );
            link.setLastUpdated( new Date() );
            mailingListService.updateMailingLink( link );
        }
    }
}
