package com.bearcode.ovf.webservices.sendgrid;

import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.webservices.sendgrid.model.*;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author leonid.
 */
@Component
public class SendGridService {
    @Autowired
    private SendGridApiConnector connector;

    @Autowired
    private SendGridLogger sendGridLogger;

    private Gson gson;

    public SendGridService() {
        gson = new GsonBuilder()
                .setFieldNamingPolicy( FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES )
                .setDateFormat( "MM/dd/yyyy" )
                .create();
    }

    public AddContactResponse sendNewContacts( List<MailingLink> links ) {
        if ( links.size() > 0 ) {
            sendGridLogger.info( String.format( "Send new contacts, number %d", links.size() ) );
        }
        List<SendGridContact> contacts = new ArrayList<SendGridContact>();
        for ( MailingLink link : links ) {
            contacts.add( new SendGridContact( link ) );
        }
        String body = gson.toJson( contacts );
        JsonElement jsonResponse = connector.callMethod( SendGridMethod.ADD_RECIPIENTS, null, body );
        if ( jsonResponse == null ) {
            sendGridLogger.info( "Send new contacts gets no response");
            return null;
        }
        AddContactResponse response = gson.fromJson( jsonResponse, AddContactResponse.class );

        return response;
    }

    public void restoreGlobalUnsubscribes( List<MailingLink> links ) {
        sendGridLogger.info( String.format( "Delete %d unsubscribes", links.size() ));
        for (  MailingLink link : links ) {
            Map<String, String> params = new HashMap<String, String>();
            params.put( "email", link.getMailingAddress().getEmail() );
            /*JsonElement jsonResponse =*/ connector.callMethod( SendGridMethod.DELETE_UNSUBSCRIBES, params, null );
        }
    }

    public void checkDeleteGroupUnsubscribes( List<MailingLink> links ) {
        sendGridLogger.info( String.format( "Check %d group unsubscribes", links.size() ) );
        for (  MailingLink link : links ) {
            Map<String, String> params = new HashMap<String, String>();
            params.put( "email", link.getMailingAddress().getEmail() );
            JsonElement jsonResponse = connector.callMethod( SendGridMethod.GET_SUPPRESSION_GROUPS, params, null );
            if ( jsonResponse != null ) {
                GetSuppressionGroupResponse response = gson.fromJson( jsonResponse, GetSuppressionGroupResponse.class );
                for ( SuppressionGroup suppressionGroup : response.getSuppressions() ) {
                    if ( suppressionGroup.isSuppressed() ) {
                        sendGridLogger.info( String.format( "Delete group unsubscribes for %s", link.getMailingAddress().getEmail() ));
                        params.put( "group_id", String.valueOf( suppressionGroup.getId() ) );
                        /*JsonElement jsonResponse2 =*/ connector.callMethod( SendGridMethod.DELETE_SUPPRESSIONS, params, null );
                    }
                }
            }
            else {
                sendGridLogger.info( "Check group unsubscribes gets no response" );
            }
        }

    }

    public List<String> getGlobalUnsubscribes( long startTime, int limit, int offset ) {
        Map<String, String> params = new HashMap<String, String>();
        if ( startTime != 0 ) {
            params.put( "start_time", String.valueOf( startTime ) );
        }
        params.put( "limit", String.valueOf( limit ) );
        params.put( "offset", String.valueOf( offset ) );
        JsonElement jsonResponse = connector.callMethod( SendGridMethod.GET_UNSUBSCRIBES, params, null );
        if ( jsonResponse == null ) {
            sendGridLogger.info( "Unsubscribes gets no response" );
            return null;
        }
        List<String> emails = new LinkedList<String>();
        if ( jsonResponse.isJsonArray() ) {
            sendGridLogger.info( String.format( "Unsubscribes gets %d records", jsonResponse.getAsJsonArray().size() ));
            for ( JsonElement jsonElement : jsonResponse.getAsJsonArray() ) {
                if ( jsonElement.isJsonObject() ) {
                    String email = jsonElement.getAsJsonObject().get( "email" ).getAsString();
                    emails.add( email );
                }
            }
        }
        return emails;
    }
}
