package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.model.mail.MailingLinkStatus;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by leonid on 27.03.15.
 */
@Controller
@RequestMapping("/admin/MatchLostMailingAddress.htm")
public class MatchLostMailingAddress extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private QuestionnaireService questionnaireService;

    public MatchLostMailingAddress() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/MatchingLostMailingAddress.jsp" );
        setPageTitle( "Matching Lost Mailing Address" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("addressCount")
    public Long lostAddressCount() {
        return mailingListService.countLostAddresses();
    }

    @ModelAttribute("mailingLists")
    public List<MailingList> getMailingLists() {
        return mailingListService.findAllMailingLists();
    }

    @ModelAttribute("facesOfLostAddress")
    public Map<String, Map<String,Long>> getFacesOfLostAddress( @RequestParam(value = "faceName", required = false) String faceUrl ) {
        return mailingListService.findFacesOfLostAddresses( faceUrl );
    }

    @RequestMapping( method = {RequestMethod.GET, RequestMethod.POST} )
    public String showCount( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @ModelAttribute("stateByState")
    public boolean getStateByState( @RequestParam(value = "byState", required = false) String byState ) {
        return byState != null && !byState.isEmpty();
    }

    @RequestMapping( method = RequestMethod.POST, params = "runMatching")
    public String doMatching( HttpServletRequest request, ModelMap model,
                              @RequestParam(value = "faceName", required = false) String faceUrl,
                              @RequestParam(value = "stateName", required = false) String stateName,
                              @RequestParam(value = "mailingListId", required = false) Long mailingListId ) {
        List<MailingAddress> addresses = mailingListService.findLostAddresses( faceUrl, stateName );
        if ( mailingListId != null ) {
            MailingList mailingList = mailingListService.findMailingList( mailingListId );
            if ( mailingList != null ) {
                List<MailingLink> createdLinks = new LinkedList<MailingLink>();
                for ( MailingAddress lostAddress : addresses ) {
                    MailingLink link = new MailingLink();
                    link.setMailingAddress( lostAddress );
                    link.setMailingList( mailingList );
                    link.setStatus( MailingLinkStatus.NEW );
                    createdLinks.add( link );
                }
                mailingListService.saveMailingLink( createdLinks );
                Map facesOfLost = mailingListService.findFacesOfLostAddresses( faceUrl );
                if ( (facesOfLost == null || facesOfLost.isEmpty()) && stateName != null ) {
                    return "redirect:/admin/MatchLostMailingAddress.htm";
                }
                model.addAttribute( "facesOfLostAddress", facesOfLost );
                model.addAttribute( "addressCount", mailingListService.countLostAddresses() );
            }
        }

        return buildModelAndView( request, model );
    }
}
