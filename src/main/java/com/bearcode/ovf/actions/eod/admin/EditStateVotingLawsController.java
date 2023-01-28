package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.MailVotingType;
import com.bearcode.ovf.model.eod.SameDayRegistrationType;
import com.bearcode.ovf.model.eod.StateVotingLaws;
import com.bearcode.ovf.model.eod.VoterIdType;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 26.12.13
 * Time: 17:42
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditStateVotingLaws.htm")
public class EditStateVotingLawsController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private StateService stateService;

    public EditStateVotingLawsController() {
        setContentBlock("/WEB-INF/pages/blocks/admin/EodEditVotingLaws.jsp");
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "Edit State Voting Laws" );
    }

    @ModelAttribute("votingLaws")
    protected StateVotingLaws getVotingLaws( @RequestParam("stateId") Long stateId ) {
        State state = stateService.findState( stateId );
        if ( state != null ) {
            return localOfficialService.findVotingLaws( state );
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm( HttpServletRequest request, ModelMap model ) {
        model.addAttribute( "mailVotingTypes", MailVotingType.values() );
        model.addAttribute( "voterIdTypes", VoterIdType.values() );
        model.addAttribute( "sameDayRegistrations", SameDayRegistrationType.values() );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("votingLaws") StateVotingLaws votingLaws ) {
        localOfficialService.saveVotingLaws( votingLaws );
        return showForm( request, model );
    }

}
