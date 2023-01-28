package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.eod.MailVotingType;
import com.bearcode.ovf.model.eod.StateVotingLaws;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import com.bearcode.ovf.webservices.localelections.model.VotingMethod;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 31.12.13
 * Time: 0:48
 *
 * @author Leonid Ginzburg
 */
@Controller
public class StateVotingLawsController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private LocalElectionsService localElectionsService;

    public StateVotingLawsController() {
        setContentBlock( "/WEB-INF/pages/blocks/EodStateVotingLaws.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "State Voting Laws Requirements" );
    }

    @ModelAttribute("votingLaws")
    protected StateVotingLaws getVotingLaws( @RequestParam(value = "stateCode", required = false, defaultValue = "") String stateAbbr ) {
            return localOfficialService.findVotingLawsByStateAbbr(stateAbbr);
    }

    @RequestMapping( value = "GetVotingLaws.htm", method = {RequestMethod.POST, RequestMethod.GET} )
    public ResponseEntity<String> getVotingLaws( HttpServletRequest request, ModelMap model,
                                @ModelAttribute("votingLaws") StateVotingLaws votingLaws ) {
        String output;
        Gson gson = new Gson();
        if (votingLaws != null) {
            output = gson.toJson(votingLaws);
        } else {
            Map<String, String> message = new HashMap<String, String>();
            message.put("error", "Requested state not found");
            output = gson.toJson( message );
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>( output, headers, HttpStatus.CREATED );
    }

    @RequestMapping( value = "GetStates.htm", method = RequestMethod.GET )
    public ResponseEntity<String> getStates( HttpServletRequest request, ModelMap model) {
        //Collection<State> states = stateService.findAllStates();
        Collection<StateVotingLaws> stateLaws = localOfficialService.findAllStateVotingLaws();
        Collection<Map<String,Object>> states = new ArrayList<Map<String, Object>>();
        for( StateVotingLaws laws : stateLaws ) {
            Map<String,Object> stateInfo = new HashMap<String, Object>();
            stateInfo.put( "abbr", laws.getState().getAbbr() );
            stateInfo.put( "fipsCode", laws.getState().getFipsCode() );
            stateInfo.put( "id", laws.getState().getId() );
            stateInfo.put( "name", laws.getState().getName() );
            stateInfo.put( "noExcuse", laws.isNoExcuseAbsenteeVoting() );
            stateInfo.put( "allMail", (laws.getAllMailVoting() == MailVotingType.ALL_ELECTIONS) );
            states.add( stateInfo );
        }
        String output;
        Map<String, Object> message = new HashMap<String, Object>();
        Gson gson = new Gson();
        message.put( "states", states );
        long count = localOfficialService.countStateWithNoExcuse();
        message.put( "countStatesWithNoExcuse", count );
        output = gson.toJson( message );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>( output, headers, HttpStatus.CREATED );
    }

    @RequestMapping( value = "/state-elections/state-voting-laws-requirements.htm", method = {RequestMethod.GET} )
    public String stateVotingLaws( HttpServletRequest request, ModelMap model ) {

        //model.addAttribute( "stateVoterInformationList", localElectionsService.getAllStateVoterInformation() );
        //return buildModelAndView( request, model );
        return "redirect:https://www.usvotefoundation.org/state-voting-methods-and-options";
    }


    @RequestMapping(value = {"GetVotingLaws.htm", "GetStates.htm", "/state-elections/state-voting-laws-requirements.htm"}, method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }

}
