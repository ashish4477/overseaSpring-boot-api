/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.vip.VipBallotCandidate;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.service.SvrPropertiesService;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link BaseController} to prompt the user to select a party for
 * partisan elections.
 * 
 * @author Ian Brown
 * @since May 10, 2013
 * @version May 13, 2013
 */
@Controller
@RequestMapping("/WhatsOnMyBallotPartisanParty.htm")
public class WhatsOnMyBallotPartisanPartyController extends BaseController {

	/**
	 * the default content block for the page.
	 * 
	 * @author Ian Brown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/WhatsOnMyBallotPartisanParty.jsp";

	/**
	 * the default title for the page.
	 * 
	 * @author Ian Brown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	static final String DEFAULT_PAGE_TITLE = "What's on My Ballot? - Select Partisan Party";

	/**
	 * the path to the CSS file to format the content.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	static final String DEFAULT_SECTION_CSS = "/css/womb.css";

	/**
	 * the default section name.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	static final String DEFAULT_SECTION_NAME = "womb";

	/**
	 * redirects to the candidate finder.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	static final String REDIRECT_CANDIDATE_FINDER = "redirect:CandidateFinder.htm";

	/**
	 * redirect to the whats on my ballot request.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	static final String REDIRECT_WHATS_ON_MY_BALLOT = "redirect:WhatsOnMyBallot.htm";

	/**
	 * redirect to the whats on my ballot list display.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	static final String REDIRECT_WHATS_ON_MY_BALLOT_LIST = "redirect:WhatsOnMyBallotList.htm";

	/**
	 * the election service.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Autowired
	private ElectionService electionService;

	/**
	 * the SVR properties service.
	 */
	@Autowired
	private SvrPropertiesService svrPropertiesService;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;
	
	/**
	 * Constructs a what's on my ballot partisan party controller.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	public WhatsOnMyBallotPartisanPartyController() {
		setPageTitle(DEFAULT_PAGE_TITLE);
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setSectionCss(DEFAULT_SECTION_CSS);
		setSectionName(DEFAULT_SECTION_NAME);
	}

	/**
	 * Gets the election service.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	public ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the SVR properties service.
	 * @return the svrPropertiesService.
	 */
    public SvrPropertiesService getSvrPropertiesService() {
	    return svrPropertiesService;
    }

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	public VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Handles a request to retrieve the partisan party from the voter.
	 * 
	 * @param request
	 *            the request.
	 * @param model
	 *            the model map.
	 * @param whatsOnMyBallot
	 *            the What's on My Ballot? object.
	 * @return the view response.
	 * @throws Exception
	 *             if there is a problem getting the partisan party.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String handleGetPartisanParty(
	        final HttpServletRequest request,
	        final ModelMap model,
	        @ModelAttribute("whatsOnMyBallot") final WhatsOnMyBallotForm whatsOnMyBallot)
	        throws Exception {
		final State votingState = whatsOnMyBallot == null ? null
		        : whatsOnMyBallot.getVotingState();
		final String stateAbbreviation = votingState == null ? null
		        : votingState.getAbbr();
		final VotingRegion votingRegion = whatsOnMyBallot == null ? null
		        : whatsOnMyBallot.getRegion();
		final String votingRegionName = votingRegion == null ? null
		        : votingRegion.getName();
		if (votingState == null
		        || !getVotingPrecinctService().isReady(stateAbbreviation,
		                votingRegionName)
		        || !getElectionService().isReady(stateAbbreviation,
		                votingRegionName)) {
			return REDIRECT_CANDIDATE_FINDER;
		}

		if (whatsOnMyBallot != null) {
			final VoterType voterType = whatsOnMyBallot.getVoterType();
			final UserAddress address = whatsOnMyBallot.getAddress();

			if (votingState != null && voterType != null && address != null) {
				final ValidAddress validAddress = getVotingPrecinctService()
				        .validateAddress(address, votingState);

				if (validAddress != null) {
					final List<VipContest> contests = getElectionService()
					        .findContests(validAddress);
					final List<VipContest> partisanContests = getElectionService()
					        .findPartisanContests(contests);

					if ((partisanContests == null)
					        || partisanContests.isEmpty()) {
						return REDIRECT_WHATS_ON_MY_BALLOT_LIST;
					}

					final String noPartyName = singlelineProperty(stateAbbreviation, votingRegionName, PartisanPartyAddOn.NO_PARTY_PROPERTY, PartisanPartyAddOn.DEFAULT_NO_PARTY_NAME);
					final List<String> partisanParties = findPartisanPartyNames(partisanContests, noPartyName);
					model.addAttribute("partisanParties", partisanParties);
					final String response = buildModelAndView(request, model);
					return response;
				}
			}
		}

		return REDIRECT_WHATS_ON_MY_BALLOT;
	}

	/**
	 * Handles a request to post the partisan party for displaying what's on the
	 * voter's ballot.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model map.
	 * @param whatsOnMyBallot
	 *            the form used to provide the information needed to display
	 *            what's on the voter's ballot.
	 * @param errors
	 *            the binding result.
	 * @return the resulting view.
	 * @throws Exception if there is an error reposting the get partisan party request.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String handlePostPartisanParty(
	        final HttpServletRequest request,
	        final ModelMap model,
	        @ModelAttribute("whatsOnMyBallot") @Valid final WhatsOnMyBallotForm whatsOnMyBallot,
	        final BindingResult errors) throws Exception {
		if (errors.hasErrors()) {
			return handleGetPartisanParty(request, model, whatsOnMyBallot);
		}
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);
		return REDIRECT_WHATS_ON_MY_BALLOT_LIST;
	}

	/**
	 * Sets the election service.
	 * 
	 * @author IanBrown
	 * @param electionService
	 *            the election service to set.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	public void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Sets the SVR properties service.
	 * @param svrPropertiesService the SVR properties service to set.
	 */
    public void setSvrPropertiesService(final SvrPropertiesService svrPropertiesService) {
	    this.svrPropertiesService = svrPropertiesService;
    }

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	public void setVotingPrecinctService(
	        final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Retrieves the what's on my ballot? form from the request session.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the what's on my ballot? form.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@ModelAttribute("whatsOnMyBallot")
	public WhatsOnMyBallotForm whatsOnMyBallot(final HttpServletRequest request) {
		return (WhatsOnMyBallotForm) request.getSession().getAttribute(
		        "whatsOnMyBallot");
	}

	/**
	 * Finds the list of unique party names for the partisan contests.
	 * 
	 * @author Ian Brown
	 * @param partisanContests
	 *            the partisan contests.
	 * @param noPartyName
	 *            the string displayed for no party selection.
	 * @return the list of unique party names.
	 * @since May 10, 2013
	 * @version Oct 1, 2013
	 */
	private List<String> findPartisanPartyNames(
	        final List<VipContest> partisanContests, final String noPartyName) {
		final List<String> partyNames = new LinkedList<String>();
		for (final VipContest partisanContest : partisanContests) {
			String partyName = partisanContest.getPartisanParty();
			if ((partyName != null) && !partyNames.contains(partyName)) {
				partyNames.add(partyName);
			} else if ("PRIMARY".equalsIgnoreCase(partisanContest.getType())) {
				for (final VipBallotCandidate candidate : partisanContest.getBallot().getCandidates()) {
					partyName = candidate.getCandidate().getParty();
					if ((partyName != null) && !partyName.isEmpty() && !PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY.equalsIgnoreCase(partyName) && !partyNames.contains(partyName)) {
						partyNames.add(partyName);
					}
				}
			}
		}
		partyNames.add(noPartyName);
		return partyNames;
	}

	/**
	 * Retrieves a single line property.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param propertyName
	 *          the name of the property.
	 * @param defaultLine
	 *          the default value.
	 * @return the single line value.
	 * @since Oct 23, 2012
	 * @version May 7, 2013
	 */
	private final String singlelineProperty(final String stateAbbreviation,
	    final String votingRegionName, final String propertyName,
	    final String defaultLine) {
		String line = getSvrPropertiesService().findProperty(stateAbbreviation,
		    votingRegionName, propertyName);
		if (line == null) {
			line = defaultLine;
		}

		return line;
	}

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
		return sendMethodNotAllowed();
    }
}
