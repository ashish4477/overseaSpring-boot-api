package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import com.bearcode.ovf.webservices.votesmart.model.Bill;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Shows voting history for a candidate
 * Handles /CandidateVoting.htm
 * Date: 09.10.13
 * Time: 18:26
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping(value = {"/CandidateVoting.htm", "/KeyVotes.htm"})
public class CandidateVoting extends BaseController {

    @Autowired
    private VoteSmartService voteSmartService;

    @Autowired
    private OverseasUserService userService;

    public CandidateVoting() {
        setSectionCss( "/css/rava.css" );
        setSectionName("mva");
        setPageTitle("My Voter Account");
        setContentBlock("/WEB-INF/pages/blocks/CandidateVoting.jsp");
    }

    @RequestMapping( method = {RequestMethod.GET, RequestMethod.POST} )
    public String showVotingHistory( HttpServletRequest request, ModelMap model,
                                     @RequestParam(value = "candidateId",required = false) String candidateId,
                                     @RequestParam(value = "categoryId",required = false) String categoryId,
                                     @RequestParam(value = "year", required = false, defaultValue = "") String year ) {

        if ( candidateId == null ) {
            return "redirect:MyReps.htm";
        }
        OverseasUser user = getUser();
        if ( user != null ) {
            //user membership history
            model.addAttribute( "membershipStat", userService.findUserMembershipStat( user ) );
        }
        CandidateBio candidate = voteSmartService.getCandidateBio( candidateId );
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        List<String> years = new LinkedList<String>();
        try {
            Date firstElectDate = format.parse( candidate.getOffice().getFirstElect() );
            Calendar firstElect = Calendar.getInstance();
            firstElect.setTime(firstElectDate);
            Calendar nowDate = Calendar.getInstance();

            for ( int yy = nowDate.get(Calendar.YEAR); yy >= firstElect.get(Calendar.YEAR); yy-- ) {
                years.add( String.valueOf( yy ) );
            }
        } catch (ParseException e) {
            logger.info(String.format("Impossible to parse FirstElected date for Candidate %s %s (id:%s)", candidate.getFirstName(), candidate.getLastName(), candidate.getId()));
        }
        model.addAttribute("years", years);
        model.addAttribute("votesCategories", voteSmartService.getVotesCategories());
        model.addAttribute("candidate",candidate);

        if ( categoryId != null && categoryId.length() > 0 ) {
            List<Bill> bills = voteSmartService.getVotesHistory( candidateId, categoryId, year );
            model.addAttribute( "bills", bills );
        }

        String target = buildModelAndView( request, model );
        model.addAttribute( SECTION_NAME, getSectionName() + " key-votes");
        return target;
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }
}
