package com.bearcode.ovf.tools;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leonid on 30.08.16.
 */
@Component
public class SendEmailHelper {

    @Autowired
    private FacesService facesService;

    @Autowired
    private EmailService emailService;

    public void sendNewAccountThank( final OverseasUser user, final FaceConfig config ) throws EmailException {
        final String userName = user.getUsername(); // userName is email
        if ( userName == null || userName.trim().isEmpty() ) {
            return;
        }

        final String templateName = facesService.getApprovedFileName( EmailTemplates.XML_NEW_ACCOUNT_THANK_YOU, config.getRelativePrefix() );
        final String firstName = user.getName().getFirstName();
        final Map model = new HashMap( );
        model.put( "firstName", firstName );
        model.put( "priority", RawEmail.Priority.HIGHEST );

        addEmailIntoQueue( userName, templateName, model );
    }

    private void addEmailIntoQueue( final String to, final String templateName, final Map model ) throws EmailException {
        final Email.Builder<?> builder = Email.builder()
                .to( to )
                .template( templateName );
        for ( Object key : model.keySet() ) {
            builder.model( key, model.get( key ) );
        }
        final Email email = builder.build();
        emailService.queue( email );
    }
}
