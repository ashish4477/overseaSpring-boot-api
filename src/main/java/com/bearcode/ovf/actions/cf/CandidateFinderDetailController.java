package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import com.bearcode.ovf.webservices.votesmart.model.CandidateAdditionalBio;
import com.bearcode.ovf.webservices.votesmart.model.CandidateBio;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/CandidateFinderDetail.htm")
public class CandidateFinderDetailController extends BaseController {

    @Autowired
    private VoteSmartService voteSmartService;

    public CandidateFinderDetailController() {
        setPageTitle( "Candidate Details" );
        setContentBlock( "/WEB-INF/pages/blocks/CandidateFinderDetail.jsp" );
        setSectionCss( "/css/candidate-finder.css" );
        setSectionName( "rava" );
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String showForm( HttpServletRequest request, ModelMap model,
                            @RequestParam(value = "cid") Long cid ) throws Exception {
        CandidateBio cb = voteSmartService.getCandidateBio( cid.toString() );
        CandidateAdditionalBio cab = voteSmartService.getCandidateAdditionalBio( cid.toString() );
        model.addAttribute( "cb", cb );
        model.addAttribute( "cab", cab );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }
}
