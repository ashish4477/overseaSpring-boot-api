package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.forms.UserAccountForm;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.utils.CipherAgentUtils;
import com.bearcode.ovf.validators.RemindPasswordValidator;
import com.google.common.collect.Sets;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 19, 2007
 * Time: 8:36:08 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/RemindPassword.htm")
public class RetrievePassword extends BaseController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private OverseasUserService userService;

    public RetrievePassword() {
        setPageTitle( "Remind Password" );
        mainTemplate = "templates/SecondTemplate";
        setContentBlock( "/WEB-INF/pages/blocks/RemindPassword.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/RemindPasswordSuccess.jsp" );
        setSectionName( "login" );
        setSectionCss( "/css/login.css" );
    }

    public void setEmailService( EmailService emailService ) {
        this.emailService = emailService;
    }

    public void setUserService( OverseasUserService userService ) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof UserAccountForm ) {
            binder.setValidator( new RemindPasswordValidator() );
        }
    }

    @ModelAttribute("userForm")
    public UserAccountForm getUserForm( @RequestParam(value = "username", required = false, defaultValue = "") String name ) {
        UserAccountForm refreshPass = new UserAccountForm();
        refreshPass.setUsername( name );
        return refreshPass;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showPage( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }


    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("userForm") @Valid UserAccountForm refreshPass,
                               BindingResult errors ) throws Exception {
        OverseasUser user = userService.findUserByName( refreshPass.getUsername() );
        if ( user != null && !errors.hasErrors() ) {
            String newPass = OverseasUser.generatePassword();
            try {
                FaceConfig config = this.getFaceConfig( request );

                sendEmail( user, newPass, config );
            } catch ( Exception e ) {
                logger.error( "Error while sending password remind email", e );
                errors.reject( "", "Can't send email, password is unchanged." );   //TODO message
            }
            return buildSuccessModelAndView( request, model );
        } else {
            errors.rejectValue( "username", "", "User with email " + refreshPass.getUsername() + " could not be found." );  //TODO message
        }
        return buildModelAndView( request, model );
    }

    protected void sendEmail( OverseasUser user, String newPass, FaceConfig config )
            throws EmailException {
        String token;
        try {
            token = CipherAgentUtils.createToken( config.getName(), String.format( "%s:%x", user.getUsername(), new Date().getTime() ));
        } catch (Exception e) {
            throw new EmailException( "Unable to encode authentication key" );
        }

        final Email email = Email.builder()
                .template( getFacesService().getApprovedFileName( EmailTemplates.XML_CHANGE_PASSWORD, config.getRelativePrefix() ) )
                .to( user.getUsername() )
                .model( "firstName", user.getName().getFirstName() )
                .model( "newPass", newPass )
                .model( "cipherCode", token )
                .model( "serverName", config.getUrlPath() )
                .model( "priority", RawEmail.Priority.HIGHEST )
                .bcc( false )
                .build();
        emailService.queue( email );
    }


    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }
}
