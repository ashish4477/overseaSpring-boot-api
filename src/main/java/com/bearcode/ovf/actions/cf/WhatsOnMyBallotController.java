/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.VoterTypePropertyEditor;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.validators.WhatsOnMyBallotValidator;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * Extended {@link BaseController} to support an extended candidate finder service backed by state and local data. The information
 * presented to the user will include their full ballot, adding state and local candidates, as well as referendums and custom
 * ballots to the Federal candidates list provided by the basic candidate finder.
 * 
 * @author IanBrown
 * 
 * @since Aug 13, 2012
 * @version May 10, 2013
 */
@Controller
@RequestMapping("/WhatsOnMyBallot.htm")
public class WhatsOnMyBallotController extends BaseController {

	/**
	 * the title for the page.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "What's on My Ballot?";

	/**
	 * the path to the JSP file to display the content.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/WhatsOnMyBallot.jsp";

	/**
	 * the path to the CSS file to format the content.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Oct 15, 2012
	 */
	static final String DEFAULT_SECTION_CSS = "/css/womb.css";

	/**
	 * the default section name.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Oct 16, 2012
	 */
	static final String DEFAULT_SECTION_NAME = "womb";

	/**
	 * redirects to the candidate finder.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	static final String REDIRECT_CANDIDATE_FINDER = "redirect:CandidateFinder.htm";

	/**
	 * redirect to the whats on my ballot list display.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	static final String REDIRECT_WHATS_ON_MY_BALLOT_LIST = "redirect:WhatsOnMyBallotList.htm";

	/**
	 * redirect to the whats on my ballot partisan party selection.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	static final String REDIRECT_WHATS_ON_MY_BALLOT_PARTISAN_PARTY = "redirect:WhatsOnMyBallotPartisanParty.htm";
	
	/**
	 * the validator.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	@Autowired
	private WhatsOnMyBallotValidator whatsOnMyBallotValidator;

	/**
	 * the property editor for {@link VoterType}.
	 * 
	 * @author IanBrown
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	@Autowired
	private VoterTypePropertyEditor voterTypePropertyEditor;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 21, 2012
	 * @version Aug 21, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Constructs a what's on my ballot controller.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	public WhatsOnMyBallotController() {
		setPageTitle(DEFAULT_PAGE_TITLE);
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setSectionCss(DEFAULT_SECTION_CSS);
		setSectionName(DEFAULT_SECTION_NAME);
	}

	/**
	 * Gets the voter type property editor.
	 * 
	 * @author IanBrown
	 * @return the voter type property editor.
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	public VoterTypePropertyEditor getVoterTypePropertyEditor() {
		return voterTypePropertyEditor;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 21, 2012
	 * @version Aug 21, 2012
	 */
	public VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Gets the whats on my ballot validator.
	 * 
	 * @author IanBrown
	 * @return the whats on my ballot validator.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	public WhatsOnMyBallotValidator getWhatsOnMyBallotValidator() {
		return whatsOnMyBallotValidator;
	}

	/**
	 * Handles a request to get the voting address for displaying what's on the voter's ballot.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param votingRegionState
	 *            the abbreviation for the voting state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param model
	 *            the model map.
	 * @param whatsOnMyBallot
	 *            the form used to hold the information needed to display what's on the voter's ballot.
	 * @return the resulting view.
	 * @since Aug 13, 2012
	 * @version Oct 16, 2012
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String handleGetVotingAddress(final HttpServletRequest request,
			@RequestParam(value = "vrState", required = false) final String votingRegionState, @RequestParam(value = "vrName",
					required = false) final String votingRegionName, final ModelMap model,
			@ModelAttribute("whatsOnMyBallot") final WhatsOnMyBallotForm whatsOnMyBallot) {
		State votingState = whatsOnMyBallot.getVotingState();
		if (votingRegionState != null) {
			if (votingState == null || votingRegionState != votingState.getAbbr()) {
				votingState = getStateService().findByAbbreviation(votingRegionState);
			}
		}
		if (votingState == null) {
			return REDIRECT_CANDIDATE_FINDER;
		}

		VotingRegion votingRegion = whatsOnMyBallot.getRegion();
		if (votingRegionName != null) {
			if (votingState != null && (votingRegion == null || votingRegionName != votingRegion.getName())) {
				votingRegion = getStateService().findRegion(votingState, votingRegionName);
			}
		}

		final String stateAbbreviation = votingState.getAbbr();
		final String regionName = votingRegion == null ? null : votingRegion.getName();
		if (votingState != null && getVotingPrecinctService().isReady(stateAbbreviation, regionName)) {
			whatsOnMyBallot.setVotingState(votingState);
			whatsOnMyBallot.setRegion(votingRegion);
			final UserAddress ballotAddress = whatsOnMyBallot.getAddress();
			UserAddress workingAddress = ballotAddress;
			final OverseasUser user = getUser();
			model.addAttribute("user", user);
			if (workingAddress == null) {
				workingAddress = new UserAddress();
				if (user != null) {
					final VotingRegion userVotingRegion = user.getVotingRegion();
					final UserAddress votingAddress = user.getVotingAddress();
					if (votingAddress != null && votingAddress.getState().equals(stateAbbreviation) &&
							((votingRegion == null) || votingRegion.valueEquals(userVotingRegion))) {
						workingAddress.setStreet1(votingAddress.getStreet1());
						workingAddress.setStreet2(votingAddress.getStreet2());
						workingAddress.setCity(votingAddress.getCity());
						workingAddress.setState(votingAddress.getState());
						workingAddress.setZip(votingAddress.getZip());
					} else {
						workingAddress.setState(stateAbbreviation);
					}
				}
			}

			if (!stateAbbreviation.equals(workingAddress.getState())) {
				workingAddress.setState(stateAbbreviation);
				workingAddress.setStreet1(null);
				workingAddress.setStreet2(null);
				workingAddress.setCity(null);
				workingAddress.setZip(null);
			}

			if (ballotAddress == null) {
				whatsOnMyBallot.setAddress(workingAddress);
			}

			if (user != null && whatsOnMyBallot.getVoterType() == null) {
				whatsOnMyBallot.setVoterType(user.getVoterType());
			}

			model.addAttribute("whatsOnMyBallot", whatsOnMyBallot);
			final String street1 = workingAddress.getStreet1();
			if (street1 != null && !street1.trim().isEmpty()) {
				final int space = street1.indexOf(" ");
				final int dash = street1.indexOf("-");
				if (dash > 0 && space > dash) {
					model.addAttribute("houseNumber", street1.substring(0, dash).trim());
					model.addAttribute("streetName", street1.substring(dash).trim());
				} else if (space > 0) {
					model.addAttribute("houseNumber", street1.substring(0, space).trim());
					model.addAttribute("streetName", street1.substring(space + 1).trim());
				}
			}
			final List<String> zipCodes = getVotingPrecinctService().findZipCodes(votingState);
			model.addAttribute("zipCodes", zipCodes);
			final Collection<VotingRegion> regions = getStateService().findRegionsForState(votingState);
			model.addAttribute("regions", regions);
			model.addAttribute("modelAttr", "whatsOnMyBallot");
			final String response = buildModelAndView(request, model);
			return response;
		}

		return REDIRECT_CANDIDATE_FINDER;
	}

	/**
	 * Handles a request to post the voting address for displaying what's on the voter's ballot.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model map.
	 * @param whatsOnMyBallot
	 *            the form used to provide the information needed to display what's on the voter's ballot.
	 * @param errors
	 *            the binding result.
	 * @return the resulting view.
	 * @since Aug 13, 2012
	 * @version May 10, 2013
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String handlePostVotingAddress(final HttpServletRequest request, final ModelMap model,
			@ModelAttribute("whatsOnMyBallot") @Valid final WhatsOnMyBallotForm whatsOnMyBallot, final BindingResult errors) {
		if (whatsOnMyBallot.getVoterType() == null) {
			errors.rejectValue("voterType", "", "A voter type is required");
		}
		if (errors.hasErrors()) {
			return handleGetVotingAddress(request, null, null, model, whatsOnMyBallot);
		}
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);
		return REDIRECT_WHATS_ON_MY_BALLOT_PARTISAN_PARTY;
	}

	/**
	 * Sets the voter type property editor.
	 * 
	 * @author IanBrown
	 * @param voterTypePropertyEditor
	 *            the voter type property editor to set.
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	public void setVoterTypePropertyEditor(final VoterTypePropertyEditor voterTypePropertyEditor) {
		this.voterTypePropertyEditor = voterTypePropertyEditor;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Aug 21, 2012
	 * @version Aug 21, 2012
	 */
	public void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Sets the whats on my ballot validator.
	 * 
	 * @author IanBrown
	 * @param whatsOnMyBallotValidator
	 *            the whats on my ballot validator to set.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	public void setWhatsOnMyBallotValidator(final WhatsOnMyBallotValidator whatsOnMyBallotValidator) {
		this.whatsOnMyBallotValidator = whatsOnMyBallotValidator;
	}

	/**
	 * Supplies the form used to hold the information for the What's on my ballot? request.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the form.
	 * @since Aug 13, 2012
	 * @version Aug 24, 2012
	 */
	@ModelAttribute("whatsOnMyBallot")
	public WhatsOnMyBallotForm whatsOnMyBallot(final HttpServletRequest request) {
		WhatsOnMyBallotForm whatsOnMyBallotForm = (WhatsOnMyBallotForm) request.getSession().getAttribute("whatsOnMyBallot");
		if (whatsOnMyBallotForm == null) {
			whatsOnMyBallotForm = new WhatsOnMyBallotForm();
		}
		return whatsOnMyBallotForm;
	}

	/**
	 * Initializes the binder by supplying the validator.
	 * 
	 * @author IanBrown
	 * @param binder
	 *            the request data binder.
	 * @since Aug 13, 2012
	 * @version Aug 23, 2012
	 */
	@InitBinder
	protected void initBinder(final ServletRequestDataBinder binder) {
		final Object target = binder.getTarget();
		if (target instanceof WhatsOnMyBallotForm) {
			binder.setValidator(getWhatsOnMyBallotValidator());
			binder.registerCustomEditor(VoterType.class, getVoterTypePropertyEditor());
		}
	}

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
		return sendMethodNotAllowed();
    }
}
