package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.eod.AdditionalAddress;
import com.bearcode.ovf.model.eod.CorrectionsLeo;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.validators.OverseasUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 12, 2007
 * Time: 8:29:38 PM
 *
 * @author Leonid Ginzburg
 *         Not used yet. There is no correction for SVID.
 */
@Controller
@RequestMapping("/admin/EodCorrectionsEdit.htm")
public class EditCorrectionsController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private EmailService emailService;


    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public EditCorrectionsController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodCorrections.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/admin/EodEditSuccessPage.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "Edit Local Official Corrections" );
    }

    @ModelAttribute("correction")
    protected CorrectionsLeo formBackingObject( @RequestParam(value = "correctionsId", required = false, defaultValue = "0") Long correctionId ) {
        CorrectionsLeo correction = localOfficialService.findCorrectionsById( correctionId );
        if ( correction == null ) {
            correction = new CorrectionsLeo();
        }
        LocalOfficial leo = correction.getCorrectionFor();
        if ( leo != null ) {
            int requiredSize = Math.max( 3, correction.getOfficers().size() );
            while ( leo.getOfficers().size() < requiredSize ) {
                Officer officerToAdd = new Officer();
                officerToAdd.setOrderNumber( leo.getOfficers().size() +1 );
                leo.getOfficers().add( officerToAdd );
            }
            leo.sortAdditionalAddresses();
            for ( int i = leo.getAdditionalAddresses().size(); i < correction.getAdditionalAddresses().size(); i++ ) {
                leo.getAdditionalAddresses().add( new AdditionalAddress() );
            }
        }

        return correction;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected String showForm( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("correction") CorrectionsLeo correction ) {
        if ( correction.getCorrectionFor() == null ) {
            /* If there is no object, go to EOD page.*/
            return "redirect:/admin/EodCorrectionsList.htm";
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("correction") CorrectionsLeo correction, BindingResult errors ) throws Exception {
        OverseasUser admin = (OverseasUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        correction.setEditor( admin );
        correction.setUpdated( new Date() );
        if ( request.getParameterMap().containsKey( "accept" ) ) {
            localOfficialService.saveCorrections( correction );
            FaceConfig config = getFacesService().findConfig( request.getServerName() + request.getContextPath() );
            String templateName = getFacesService().getApprovedFileName(EmailTemplates.XML_CORRECTIONS_THANK_YOU, config.getRelativePrefix());
            sendThankYouEmail( correction, templateName );
            model.put( "messageCode", "eod.admin.corrections.save_success" );
        }
        if ( request.getParameterMap().containsKey( "decline" ) ) {
            localOfficialService.updateDeclinedCorrections( correction );
            model.put( "messageCode", "eod.admin.corrections.reject" );
        }
        return buildSuccessModelAndView( request, model );
    }

    private void sendThankYouEmail(CorrectionsLeo correction, String templateName) {
        String toEmail = correction.getSubmitterEmail();

        if ( toEmail.isEmpty() || !toEmail.matches(OverseasUserValidator.USERNAME_PATTERN) ) {
            return;  //no email to send to
        }
        String greeting = correction.getSubmitterName();

        try {
            final Email email = Email.builder()
            		.template( templateName )
            		.to( toEmail )
            		.model( "greeting", greeting )
                    .model( "priority", RawEmail.Priority.LOW )
            		.build();
            emailService.queue(email);
        } catch (Exception e) {
            logger.error("Corrections Thank You Email to Election Official was not sent.", e);
        }
    }
}
