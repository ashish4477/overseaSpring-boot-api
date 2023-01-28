package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.mail.MailTemplate;
import com.bearcode.ovf.model.mail.MailingList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author leonid.
 */
@Component
public class VelocitySintaxValidator implements Validator {
    @Autowired
    private VelocityEngine velocityEngine;

    @Override
    public boolean supports( Class<?> clazz ) {
        return MailingList.class.isAssignableFrom( clazz )
                || MailTemplate.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        String template = "";
        String field = "";
        String fromAddr = "";
        String replyToAddr = "";
        if ( target instanceof MailTemplate ) {
            template = ((MailTemplate)target).getBodyTemplate();
            field = "bodyTemplate";
            fromAddr = ((MailTemplate)target).getFrom();
            replyToAddr = ((MailTemplate)target).getReplyTo();
        }
        else if ( target instanceof MailingList ) {
            template = ((MailingList)target).getSignature();
            field = "signature";
            fromAddr = ((MailingList)target).getFrom();
            replyToAddr = ((MailingList)target).getReplyTo();
        }

        try {
            final StringWriter message = new StringWriter();
            final ToolManager toolManager = new ToolManager();
            final ToolContext toolContext = toolManager.createContext();
            final VelocityContext context = new VelocityContext( new HashMap(), toolContext );

            final String currentYear = String.valueOf( Calendar.getInstance().get(Calendar.YEAR));
            context.internalPut("year", currentYear);
            context.internalPut("electionYear", currentYear);

            velocityEngine.evaluate(context, message, "make test", template);

        } catch (ParseErrorException e) {
            errors.rejectValue( field, "", "Velocity syntax error: " + e.getMessage() );
        } catch ( MethodInvocationException e ) {
            errors.rejectValue( field, "", "Velocity error: Something threw an Exception, " + e.getMessage() );
        } catch (Exception e) {
            errors.rejectValue( field, "", "Velocity unexpected error: " + e.getMessage() );
        }

        Pattern emailPattern = Pattern.compile( OverseasUserValidator.USERNAME_PATTERN );
        if ( StringUtils.isNotBlank( fromAddr ) ) {
            if ( emailPattern.matcher( replyToAddr ).find() ) {
                if ( !emailPattern.matcher( replyToAddr ).matches() ) {
                    String wideRegex = "[\\w -]*\\<" + OverseasUserValidator.USERNAME_PATTERN +"\\>";
                    if ( !replyToAddr.matches( wideRegex ) ) {
                        errors.rejectValue( "from", "mva.username.not_valid" );
                    }
                }
            }
            else {
                errors.rejectValue( "from", "mva.username.not_valid" );
            }
        }
        if ( StringUtils.isNotBlank( replyToAddr ) && !emailPattern.matcher( replyToAddr ).matches() ) {
            errors.rejectValue( "replyTo", "mva.username.not_valid" );
        }

    }
}
