package com.bearcode.ovf.actions.mail.admin;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.validators.VelocitySintaxValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.*;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.mail.MailTemplate;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.tools.MailTemplateAccessService;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 30, 2008
 * Time: 7:51:35 PM
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditMailTemplate.htm")
public class EditMailTemplate extends BaseController {

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VelocitySintaxValidator velocitySintaxValidator;


    private MailTemplateAccessService templateAccessService;


    public void setTemplateAccessService(MailTemplateAccessService templateAccessService) {
        this.templateAccessService = templateAccessService;
    }

/*
		<property name="commandName" value="mail" />
*/

    public EditMailTemplate() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/MailEdit.jsp" );
        setPageTitle( "Edit Mail Template" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("mailTemplate")
    public MailTemplate getTemplate( @RequestParam(value = "templateId", required = false, defaultValue = "0") Long id ) {
        MailTemplate template = null;
        if ( id != null && id != 0 ) {
            template = mailingListService.findTemplate( id );
        }
        return template != null ? template : new MailTemplate();
    }

    @RequestMapping( method = {RequestMethod.GET, RequestMethod.POST})
    public String showForm(HttpServletRequest request, ModelMap model) throws Exception {
        return  buildModelAndView( request, model );
    }

    @RequestMapping( method = RequestMethod.POST, params = "save")
    protected String onSubmit(HttpServletRequest request, ModelMap model,
                              @ModelAttribute("mailTemplate") @Valid MailTemplate command,
                              BindingResult errors) throws Exception {
        ValidationUtils.invokeValidator( velocitySintaxValidator, command, errors );
        if ( !errors.hasErrors() ) {
            mailingListService.saveTemplate( command );
            return "redirect:/admin/MailTemplatesList.htm";
        }
        return buildModelAndView( request, model );
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof MailTemplate ) {
            binder.setValidator( validator );
        }
    }

    @RequestMapping( method = RequestMethod.POST, params = "test")
    public String testEmail( HttpServletRequest request, ModelMap model,
                             @ModelAttribute("mailTemplate") @Valid MailTemplate template,
                             @RequestParam(value = "sendToEmail", required = false, defaultValue = "") String sendToEmail,
                             BindingResult errors ) throws EmailException {
        ValidationUtils.invokeValidator( velocitySintaxValidator, template, errors );
        if ( !errors.hasErrors() ) {
            // send email to address or to admin
            Object ouser = model.get( "user" );
            if ( ouser instanceof OverseasUser ) {
                OverseasUser user = (OverseasUser) ouser;

                String[] toEmails = sendToEmail.split( "[;, ]" );
                if ( toEmails.length <= 0 ) {
                    toEmails = new String[] { user.getUsername() };
                }

                for ( String emailAddress : toEmails ) {
                    MailingAddress contact = new MailingAddress();
                    contact.setFirstName( user.getName().getFirstName() );
                    contact.setLastName( user.getName().getLastName() );

                    final Email email = Email.builder()
                            .templateBody( template.getBodyTemplate() )
                            .to( emailAddress )
                            .model( "contact", contact )
                            .model( "year", Calendar.getInstance().get( Calendar.YEAR ) )
                            .model( "priority", RawEmail.Priority.LOWEST )
                            .bcc( false )
                            .from( StringUtils.isEmpty( template.getFrom() ) ? "ovf-admin@bear-code.com" : template.getFrom() )
                            .replyTo( template.getReplyTo() )
                            .subject( StringUtils.isEmpty( template.getSubject() ) ? "Subject: test email template" : template.getSubject() )
                            .build();
                    emailService.queue( email );
                }
            }
        }

        return buildModelAndView( request, model );
    }

}
