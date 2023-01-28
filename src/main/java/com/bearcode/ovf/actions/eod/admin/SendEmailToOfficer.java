package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.Person;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.eod.StateVotingLaws;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/ajax/EmailToOfficer.htm")
public class SendEmailToOfficer {

    /** Logger */
    protected final Log logger = LogFactory.getLog( getClass() );

    private static final int TO_ALL = -1;

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FacesService facesService;

    public void setLocalOfficialService(LocalOfficialService localOfficialService) {
        this.localOfficialService = localOfficialService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public FacesService getFacesService() {
        return facesService;
    }

    public void setFacesService(FacesService facesService) {
        this.facesService = facesService;
    }

    @RequestMapping( method = RequestMethod.GET, params = "leoId" )
    @ResponseBody
    protected String sendOneLeoEmail(
            @RequestParam("leoId") Long leoId,
            @RequestParam( value = "recipient", required = false, defaultValue = "-1" ) Integer recipientIndex,
            HttpServletRequest request) {

        LocalOfficial leo = localOfficialService.findById( leoId );

        FaceConfig config = getFacesService().findConfig( request.getServerName() + request.getContextPath() );
        String templateName = getFacesService().getApprovedFileName( EmailTemplates.XML_OFFICER_SNAPSHOT, config.getRelativePrefix() );

        if ( doSend( leo, recipientIndex, templateName ) ) {
            return "Email was sent";
        }
        return "Email was not sent";
    }

    @RequestMapping( method = RequestMethod.GET, params = "stateId" )
    @ResponseBody
    protected String sendAllLeoEmail(
            @RequestParam("stateId") Long stateId,
            @RequestParam( value = "recipient", required = false, defaultValue = "-1" ) Integer recipientIndex,
            HttpServletRequest request) {

        FaceConfig config = getFacesService().findConfig( request.getServerName() + request.getContextPath() );
        String templateName = getFacesService().getApprovedFileName( EmailTemplates.XML_OFFICER_SNAPSHOT, config.getRelativePrefix() );

        Collection<LocalOfficial> leos = localOfficialService.findForState( stateId );
        int sentCounter = 0;
        for ( LocalOfficial leo : leos ) {
            if ( doSend( leo, recipientIndex, templateName ) ) {
                sentCounter++;
            }
        }
        return String.format( "%d emails sent successfully", sentCounter );
    }

    private boolean doSend( LocalOfficial leo, Integer recipientIndex, String templateName ) {
    	if (leo == null) {
            logger.error( "Send Snapshot Email to Official: Local official information was not found, wrong ID.");
    		return false;
    	}
        boolean result = false;
        if ( recipientIndex == TO_ALL ) {
            Set<String> stopDuplicateList = new HashSet<String>();
            for (int i = 0; i < leo.getOfficers().size(); i++ ) {
                result |= doActualSend( templateName, leo, i, stopDuplicateList );
            }
        } else if ( recipientIndex < leo.getOfficers().size() ) {
            result = doActualSend( templateName, leo, recipientIndex, null );
        } else {
            logger.warn("Send Snapshot Email to Official: Officer was not found, wrong index.");
        }

        return result;
    }

    private boolean doActualSend(String templateName, LocalOfficial leo, int index, Set<String> stopDuplicateList ) {

        Officer officer = leo.getOfficers().get( index );
        String greeting = officer.getOfficeName();
        Person toPerson = new Person();
        toPerson.setFirstName( officer.getFirstName() );
        toPerson.setLastName( officer.getLastName() );
        String toEmail = officer.getEmail();

        if (toEmail.isEmpty()) {
        	logger.error( "Send Snapshot Email to Official: Election official has no email.");
        	return false;
        }

        if ( stopDuplicateList != null ) {
            if ( stopDuplicateList.contains( toEmail ) ) {
                return true;  // email already sent
            }
            stopDuplicateList.add( toEmail );
        }
        try {
            final Email email = Email.builder()
            		.template( templateName )
            		.to( toEmail )
            		.model( "leo", leo )
            		.model("person", toPerson)
            		.model("greeting", greeting)
                    .model( "priority", RawEmail.Priority.LOW )
            		.build();
            emailService.queue(email);
            return true;
        } catch (Exception e) {
            logger.error("Email to Election Official was not sent.", e);
            return false;
        }
    }

}