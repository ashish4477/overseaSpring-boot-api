package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.*;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * Date: 28.10.13
 * Time: 18:07
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping(value = "/MyDates.htm")
public class DatesDeadlinesController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    LocalElectionsService localElectionsService;

    @Autowired
    EodApiService eodApiService;


    public DatesDeadlinesController() {
        setSectionCss( "/css/eod.css" );
        setSectionName( "mva dates" );
        setPageTitle( "My Voter Account" );
        setContentBlock( "/WEB-INF/pages/blocks/MyDates.jsp" );
    }

    @RequestMapping(method = RequestMethod.GET )
    public String showDatesAndDeadlines( HttpServletRequest request, ModelMap model,
                                         @ModelAttribute("user") OverseasUser user ) {

        String regionId = user.getEodRegionId();
        String stateName = user.getVotingAddress() != null ? user.getVotingAddress().getState() : null;

        if ( StringUtils.isNotEmpty( regionId ) ) {
            final LocalOffice leo = eodApiService.getLocalOffice( regionId, true );
            if ( leo != null ) {
                final State state = getStateService().findByAbbreviation( stateName!=null ? stateName : leo.getEodRegion().getStateAbbr() );

                model.addAttribute( "leo", leo );
                model.addAttribute( "selectedRegion", leo.getEodRegion() );
                model.addAttribute( "selectedState", state );

                final StateSpecificDirectory svid = getLocalOfficialService().findSvidForState( state );
                model.addAttribute( "svid", svid );
                StateVoterInformation stateVoterInformation = localElectionsService.findStateVoterInformation( stateName );
                model.addAttribute( "stateVoterInformation", stateVoterInformation );
                if ( stateVoterInformation != null ) {
                    model.addAttribute( "identificationRequirements", LocalElectionsService.getGroupedIdentificationRequirements(stateVoterInformation) );
                    model.addAttribute( "eligibilityRequirements", LocalElectionsService.getGroupedEligibilityRequirements(stateVoterInformation) );
                }
                List<ElectionView> localElections = localElectionsService.findElectionsOfState( stateName );
                model.addAttribute( "localElections", localElections );
            }
        }
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 7);
        return buildModelAndView( request, model );
    }

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }
}
