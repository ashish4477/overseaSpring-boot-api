package com.bearcode.ovf.actions.mail;

import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("/email/Notifications.htm")
public class AmazonNotifications {
    private final Logger logger = LoggerFactory.getLogger( AmazonNotifications.class );
    @Autowired
    private EmailService emailService;

    @Autowired
    private MailingListService mailingListService;


    @RequestMapping( method = RequestMethod.POST )
    @ResponseBody
    public void processNotification( HttpServletRequest request,
                                     @RequestHeader("x-amz-sns-message-type") String messageType ) {
        JsonParser jsonParser = new JsonParser();
        try {
            if ( "Notification".equalsIgnoreCase( messageType ) ) {
                JsonObject jsonObject = jsonParser.parse( new InputStreamReader( request.getInputStream() ) ).getAsJsonObject();

                JsonElement message = jsonObject.get( "Message" );

                if ( message != null ) {
                    JsonObject messageObject = jsonParser.parse( message.getAsString() ).getAsJsonObject();

                    JsonElement type = messageObject.get( "notificationType" );
                    if ( type != null ) {
                        String notificationType = type.getAsString();
                        if ( notificationType.equalsIgnoreCase( "Bounce" ) ) {
                            JsonObject bounce = messageObject.getAsJsonObject( "bounce" );
                            JsonArray recepients = bounce.getAsJsonArray( "bouncedRecipients" );
                            for ( JsonElement recepient : recepients ) {
                                String bouncedEmail = recepient.getAsJsonObject().get( "emailAddress" ).getAsString();
                                mailingListService.updateBounces( bouncedEmail);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error( "Amazon notification.", e );
            final Email email = Email.builder()
                    .template( EmailTemplates.XML_EXCEPTION )
                    .to( "lginzburg@bear-code.com" )
                    .model( "requestUrl", request.getRequestURL() )
                    .model("queryString", request.getQueryString())
                    .model("exception", e)
                    .model( "priority", RawEmail.Priority.LOWEST )
                    .build();
            try {
                emailService.queue( email );
            } catch (EmailException e1) {
                //fatal
            }
        }
    }

    @RequestMapping( method = { RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<String> sendMethodNotAllowed() {
        return sendMethodNotAllowed();
    }

}
