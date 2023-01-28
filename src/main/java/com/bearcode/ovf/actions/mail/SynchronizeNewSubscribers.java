package com.bearcode.ovf.actions.mail;

import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.model.mail.MailingLinkStatus;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.tools.GetResponseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Date: 09.07.12
 * Time: 23:20
 *
 * @author Leonid Ginzburg
 */
@Component
@Deprecated
public class SynchronizeNewSubscribers {

    private final Logger logger = LoggerFactory.getLogger( SynchronizeNewSubscribers.class );

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private GetResponseConnector getResponseConnector;

    private int maxErrors = 5;

//    private String url = "http://api2.getresponse.com/";

    public void addNewContacts() {
        List<MailingLink> links = mailingListService.findNewMailingLinks();
        for ( MailingLink link : links ) {
            if ( getResponseConnector.addContact( link.getMailingList(), link.getMailingAddress() ) ) {
                link.setStatus( MailingLinkStatus.SUBSCRIBED );
                link.setLastUpdated( new Date() );
            } else {
                link.setErrorCount( link.getErrorCount() + 1 );
                if ( link.getErrorCount() == maxErrors ) {
                    link.setStatus( MailingLinkStatus.ERROR );
                }
            }
            mailingListService.updateMailingLink( link );
        }
    }

/*
    private boolean addContact( MailingList mailingList, MailingAddress mailingAddress ) {
        // new instance of HttpClient.
        HttpClient client = new HttpClient();

        Hashtable<String, String> contactParams = new Hashtable<String, String>();
        contactParams.put( "campaign", mailingList.getCampaignId() );
        contactParams.put( "name", mailingAddress.getFirstName() + " " + mailingAddress.getLastName() );
        contactParams.put( "email", mailingAddress.getEmail() );

        Object[] requestParams = {mailingList.getApiKey(), contactParams};

        // request object
        Hashtable<String, Object> request = new Hashtable<String, Object>();
        request.put( "method", "add_contact" );
        request.put( "params", requestParams );

        Gson gson = new Gson();

        // create a method instance.
        PostMethod method = new PostMethod( url );
        method.setRequestBody( gson.toJson( request ) );

        boolean responseStatus = false;

        try {
            // execute the method.
            int statusCode = client.executeMethod( method );

            if ( statusCode == HttpStatus.SC_OK ) {
                try {
                    JsonParser parser = new JsonParser();
                    JsonObject object = parser.parse( method.getResponseBodyAsString() ).getAsJsonObject();
                    JsonElement error = object.get( "error" );
                    if ( error.isJsonNull() ) {
                        responseStatus = true;
                    }
                    else {
                        String msg = error.getAsJsonObject().get( "message" ).getAsString();
                        logger.error( String.format( "GetResponse: Contact adding failed for email \'%s\' and campaign \'%s\'; message: %s ", mailingAddress.getEmail(), mailingList.getCampaignId(), msg ) );
                    }
                } catch ( JsonSyntaxException e ) {
                    logger.error( String.format( "GetResponse: failed to parse result: %s ", method.getResponseBodyAsString() ), e );
                }
            } else {
                logger.error( String.format( "GetResponse: Contact adding failed for email \'%s\' and campaign \'%s\'; message: %s ", mailingAddress.getEmail(), mailingList.getCampaignId(), method.getStatusLine() ) );
            }
        } catch ( HttpException e ) {
            logger.error( "GetResponse: Fatal protocol violation: " + e.getMessage() );
        } catch ( IOException e ) {
            logger.error( "GetResponse: Fatal transport error: " + e.getMessage() );
        } finally {
            // release the connection.
            method.releaseConnection();
        }
        return responseStatus;
    }
*/

    public void setMaxErrors( int maxErrors ) {
        this.maxErrors = maxErrors;
    }

    public int getMaxErrors() {
        return maxErrors;
    }
}
