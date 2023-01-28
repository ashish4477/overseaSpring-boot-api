package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.cf.CandidateFinderForm;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import com.bearcode.ovf.webservices.votesmart.model.CandidateZip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/CandidateFinderList.htm")
public class CandidateFinderListController extends BaseController {

    private static final String OFFICE_REPRESENTATIVE_TYPE = "5";
    private static final String OFFICE_SENATE_TYPE = "6";

    @Autowired
    private VoteSmartService voteSmartService;

    public CandidateFinderListController() {
        setPageTitle( "Candidates" );
        setContentBlock( "/WEB-INF/pages/blocks/CandidateFinderList.jsp" );
        setSectionCss( "/css/candidate-finder.css" );
        setSectionName( "rava" );
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String showForm( HttpServletRequest request, ModelMap model ) throws Exception {
        Object cfaObject = request.getSession().getAttribute( "cf_address" );
        if ( cfaObject == null ) {
            return "redirect:/CandidateFinder.htm";
        }
        CandidateFinderForm cfAddress = (CandidateFinderForm) cfaObject;
        model.addAttribute( "address", cfAddress );

        List<CandidateZip> candidates = null;
        try {
            candidates = voteSmartService.getCandidatesByZip( cfAddress.getAddress().getZip(), cfAddress.getAddress().getZip4() );
        } catch (IllegalArgumentException e) {
            // nothing
        }
        String candidateId = "";
        if ( candidates != null ) {
            List<CandidateZip> senateList = new ArrayList<CandidateZip>();
            List<CandidateZip> representativeList = new ArrayList<CandidateZip>();

            for ( CandidateZip candidateZip : candidates ) {
                if ( candidateZip.getElectionStatus().equalsIgnoreCase( "running" ) ) {
                    if ( OFFICE_SENATE_TYPE.equals( candidateZip.getElectionOfficeId() ) ) {
                        senateList.add( candidateZip );
                    } else if ( OFFICE_REPRESENTATIVE_TYPE.equals( candidateZip.getElectionOfficeId() ) ) {
                        representativeList.add( candidateZip );
                    }
                }
            }

            boolean addSPrimaryWinners = senateList.size() == 0;
            boolean addRPrimaryWinners = representativeList.size() == 0;

            for ( CandidateZip candidateZip : candidates ) {
                candidateId = candidateZip.getCandidateId();
                if ( candidateZip.getElectionStatus().equalsIgnoreCase( "won" ) && candidateZip.getElectionStage().equalsIgnoreCase( "primary" )) {
                    // won primary, check general stage should running
                    if ( checkGeneralStageNotFinished( candidates, candidateZip.getCandidateId() ) ) {
                        continue;
                    }
                    if ( addSPrimaryWinners && OFFICE_SENATE_TYPE.equals( candidateZip.getElectionOfficeId() ) ) {
                        senateList.add( candidateZip );
                    } else if ( addRPrimaryWinners && OFFICE_REPRESENTATIVE_TYPE.equals( candidateZip.getElectionOfficeId() ) ) {
                        representativeList.add( candidateZip );
                    }
                }
            }

            if ( senateList.size() > 0 )
                model.addAttribute( "senateList", senateList );

            if ( representativeList.size() > 0 )
                model.addAttribute( "representativeList", representativeList );

            if ( senateList.size() == 0 || representativeList.size() == 0 ) {
                candidates = voteSmartService.getOfficialsByZip( cfAddress.getAddress().getZip(), cfAddress.getAddress().getZip4() );

                addSPrimaryWinners = senateList.size() == 0;
                addRPrimaryWinners = representativeList.size() == 0;

                for ( CandidateZip candidateZip : candidates ) {
                    if ( addSPrimaryWinners && OFFICE_SENATE_TYPE.equals( candidateZip.getElectionOfficeId() ) ) {
                        senateList.add( candidateZip );
                    } else if ( addRPrimaryWinners && OFFICE_REPRESENTATIVE_TYPE.equals( candidateZip.getElectionOfficeId() ) ) {
                        representativeList.add( candidateZip );
                    }
                }
                if ( addSPrimaryWinners && senateList.size() > 0 )
                    model.addAttribute( "senateIncumbentList", senateList );

                if ( addRPrimaryWinners && representativeList.size() > 0 )
                    model.addAttribute( "representativeIncumbentList", representativeList );

            }

            model.addAttribute( "presidentVicePresident", voteSmartService.getPresidents());
        }

        return buildModelAndView( request, model );
    }


    private boolean checkGeneralStageNotFinished( List<CandidateZip> candidates, String candidateId ) {
        for ( CandidateZip candidateZip : candidates ) {
            if ( candidateId.equals( candidateZip.getCandidateId() )
                    && candidateZip.getElectionStage().equalsIgnoreCase( "general" )
                    && !candidateZip.getElectionStatus().equalsIgnoreCase( "running" ) ) {
                return true;
            }
        }
        return false;
    }

    private void checkDuplicate( List<CandidateZip> candidatesList ) {
        List<String> candidateIds = new LinkedList<String>();
        for ( CandidateZip candidate : candidatesList ) {
            if ( candidate.getElectionStage().equalsIgnoreCase( "general" ) && candidate.getElectionStatus().equalsIgnoreCase( "won" ) ) {
                // won general - remove from
            }
        }
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }

}
