package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.tools.ExceptionNotificationMailList;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.google.gson.JsonSyntaxException;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 04.03.14
 * Time: 21:24
 *
 * @author Leonid Ginzburg
 */
@ControllerAdvice
public class ExceptionNotifier implements AttributesDictionary {
    protected Logger logger = LoggerFactory.getLogger(ExceptionNotifier.class);

    protected String mainTemplate = "templates/MainTemplate";

    @Autowired
    private EmailService emailService;

    @Autowired
    protected FacesService facesService;

    @Autowired
    protected ExceptionNotificationMailList notificationMailList;

    public EmailService getEmailService() {
        return emailService;
    }

    public FacesService getFacesService() {
        return facesService;
    }

    public void setMailList( String mailListStr ) {
        List<String> mailList = new LinkedList<String>();
        String[] shopList = mailListStr.split(",");
        for ( String elem : shopList ) {
            if ( elem.trim().matches(OverseasUserValidator.USERNAME_PATTERN) ) {
                mailList.add( elem.trim() );
            }
        }
        notificationMailList.setMailList( mailList );
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) {
        FaceConfig config = getFaceConfig(request);

        if ( e instanceof TypeMismatchException ) {
            if ( !request.getParameterMap().containsKey( "scriptattempt" ) ) {
                String fixedQuery = handleTypeMismatch( request, e );
                if ( fixedQuery != null ) {
                    return new ModelAndView( String.format( "redirect:%s?%s", request.getRequestURL(), fixedQuery ) );
                }
            }

        }
        else if ( e instanceof MultipartException ) {
            logger.error( "Exception: ", e );
        }
        else if ( e instanceof IllegalArgumentException ) {
            logger.error( "Exception: ", e );
        }
        else if ( e instanceof JsonSyntaxException ) {
            logger.error( "Exception: ", e );
        }
        else if ( e instanceof JDBCConnectionException ) {
            logger.error( "Exception: ", e );
        }
        else if ( e.getClass().getCanonicalName().equals( "org.apache.catalina.connector.ClientAbortException" )) {
            // can't use 'instanceof' because 'apache.catalina' belongs to apache web-server, so it's out of our libraries
            logger.error( "Exception: ", e );
        }
        else {
            //String templateName = getFacesService().getApprovedFileName(EmailTemplates.XML_EXCEPTION, config.getRelativePrefix());
            logger.warn( String.format( "Exception at requested URL: %s", request.getRequestURL() ), e );

            for (String toEmail : notificationMailList.getMailList() ) {
                try {
                    final Email email = Email.builder()
                            .template(EmailTemplates.XML_EXCEPTION)
                            .to( toEmail )
                            .model("requestUrl", request.getRequestURL() )
                            .model("queryString", request.getQueryString())
                            .model( "exception", e )
                            .model( "priority", RawEmail.Priority.LOWEST )
                            .build();
                    emailService.queue(email);
                } catch (Exception ex) {
                    logger.error("Exception message was not sent due to {}.", ex.getMessage());
                    logger.error("Original exception : ", e);
                }
            }

        }


        ModelAndView mav = new ModelAndView();
        mav.addObject( "exception", e)
                .addObject("errorCode", "500")
                .addObject( TITLE, "Error" )
                .addObject( FACE_RELATIVE_PATH, config.getRelativePrefix() )
                .setViewName(mainTemplate);
        String contentName = "/WEB-INF/pages/blocks/Errors.jsp";
        contentName = facesService.getApprovedFileName( contentName, config.getRelativePrefix() );
        mav.addObject( CONTENT, contentName );
        return mav;
    }

    private String handleTypeMismatch( HttpServletRequest request, Exception e ) {
        if ( e.getMessage().contains( "java.lang.Long" ) ) {
            StringBuilder queryString = new StringBuilder();
            for ( Object paramKey : request.getParameterMap().keySet() ) {
                if ( queryString.length() > 0 ) {
                    queryString.append( "&" );
                }
                String value = request.getParameter( (String) paramKey );
                if ( e.getMessage().contains( value.replace( " ", "" ) ) ) {
                    value = value.replaceAll( "\\D.*", "" );
                    if ( value.isEmpty() ) {
                        value = "1";
                    }
                }
                queryString.append( paramKey ).append( "=" ).append( value );
            }
            queryString.append( "&scriptattempt=true" );
            return queryString.toString();
        }
        return null;
    }

    protected FaceConfig getFaceConfig( HttpServletRequest request ) {
        return facesService.findConfig( request.getServerName() + request.getContextPath() );
    }

    public List<String> getExceptionMailList() {
        return notificationMailList.getMailList();
    }
}
