/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * Extended {@link BaseController} implementation of {@link AllowedForRedirect} that provides state-level address handling.
 * 
 * @author IanBrown
 * 
 * @since Jul 30, 2012
 * @version Oct 24, 2012
 */
@Controller
@RequestMapping("/StateVotingAddress.htm")
public class StateVotingAddress extends BaseController implements AllowedForRedirect {

	/**
	 * the content block.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	static final String CONTENT_BLOCK = "/WEB-INF/pages/blocks/wizard/StateVotingAddress.jsp";

	/**
	 * the page title.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	static final String PAGE_TITLE = "State Voting Address";

	/**
	 * the section CSS file.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	static final String SECION_CSS = "/css/rava.css";

	/**
	 * the name of the section containing this page.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	static final String SECTION_NAME = "rava";

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Autowired
	private QuestionnaireService questionnaireService;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Constructs a state-level FWAB page.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	public StateVotingAddress() {
		setSectionName(SECTION_NAME);
		setSectionCss(SECION_CSS);
		setContentBlock(CONTENT_BLOCK);
		setPageTitle(PAGE_TITLE);
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Handles a GET request for the state-level voting address.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param votingRegionState
	 *            the abbreviation for the voting state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param model
	 *            the model.
	 * @param wizardContext
	 *            the wizard context.
	 * @return the resulting view.
	 * @since Jul 30, 2012
	 * @version Oct 24, 2012
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String handleGetStateVotingAddress(final HttpServletRequest request,
			@RequestParam(value = "vrState", required = false) final String votingRegionState, @RequestParam(value = "vrName",
					required = false) final String votingRegionName, final ModelMap model,
			@ModelAttribute("wizardContext") final WizardContext wizardContext) {
        if ( wizardContext == null ) {
            // wrong call to this page. should go home
            return "redirect:/home.htm";
        }
		if ( wizardContext.getFlowType() == FlowType.FWAB) {
			final WizardResults wizardResults = wizardContext.getWizardResults();
			final WizardResultAddress votingAddress = wizardResults.getVotingAddress();
			final String votingAddressState = votingAddress == null ? wizardResults.getVotingRegionState() : votingAddress.getState();
			final String votingState = votingAddressState == null || votingAddressState.trim().isEmpty() ? votingRegionState
					: votingAddressState;
			final String votingAddressRegion = wizardResults.getVotingRegionName() == null ? votingRegionName : wizardResults.getVotingRegionName();
			if (votingState != null
					&& getVotingPrecinctService().areRestrictedAddressesRequired(votingAddressState, votingAddressRegion)) {
				final String county = votingAddress.getCounty();
				if (county != null && !county.trim().isEmpty() && !county.toUpperCase().endsWith("COUNTY")) {
					votingAddress.setCounty(county + " County");
				} else if (votingRegionName != null && votingRegionName.endsWith("County")) {
					votingAddress.setCounty(votingRegionName);
				}
				final State state = getStateService().findByAbbreviation(votingState);
				final List<String> zipCodes = getVotingPrecinctService().findZipCodes(state);
				model.addAttribute("zipCodes", zipCodes);
				final Collection<VotingRegion> regions = getStateService().findRegionsForState(state);
				model.addAttribute("regions", regions);
				final String street1 = votingAddress == null ? null : votingAddress.getStreet1();
				if (street1 != null && !street1.trim().isEmpty()) {
					final int idx = street1.indexOf(" ");
                    try {
                        final int houseNumber = Integer.parseInt(street1.substring(0, idx));
                        final String streetName = street1.substring(idx + 1);
                        model.addAttribute("houseNumber", houseNumber);
                        model.addAttribute("streetName", streetName);
                    } catch (NumberFormatException e) {
                        // wrong address format.
                    }
                }
				return buildModelAndView(request, model);
			}

		}
		return "redirect:" + SurveyWizard.getContinueUrl(request);
	}

	/**
	 * Handles a POST request for the state-level voting address.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param user
	 *            the user.
	 * @param errors
	 *            if there were errors.
	 * @param wizardContext
	 *            the wizard context.
	 * @return the response string.
	 * @since Jul 30, 2012
	 * @version Aug 15, 2012
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String handlePostStateVotingAddress(final HttpServletRequest request, final ModelMap model,
			@ModelAttribute("user") final OverseasUser user, final BindingResult errors,
			@ModelAttribute("wizardContext") final WizardContext wizardContext) {
		final UserAddress votingAddress = user.getVotingAddress();
		final State votingState = getStateService().findByAbbreviation(votingAddress.getState());
		final ValidAddress validAddress = getVotingPrecinctService().validateAddress(votingAddress, votingState);
		if (validAddress == null) {
			errors.rejectValue("votingAddress", "", "Address is not an officially accepted location");
			return handleGetStateVotingAddress(request, null, null, model, wizardContext);
		}
		final VotingRegion votingRegion = getStateService().findRegion(votingState, votingAddress.getCounty());
		user.setVotingRegion(votingRegion);
		getVotingPrecinctService().fixAddress(votingAddress, validAddress.getStreetSegment());

		final WizardResults wizardResults = wizardContext.getWizardResults();
		wizardResults.populateFromUser(user); // copy result to WizardContext
		wizardContext.processSaveResults(questionnaireService);
		final String redirect = SurveyWizard.getContinueUrl(request);
		return "redirect:" + redirect;
	}

	/**
	 * Performs model deception to provide the "user" attribute in the case of an anonymous user filling the form.
	 * <p>
	 * Note that this method fills the user attribute in the model as a side-effect.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the servlet request.
	 * @param model
	 *            the model.
	 * @return the deception attribute - always <code>null</code> on output.
	 * @since Jul 30, 2012
	 * @version Sep 24, 2012
	 */
	@ModelAttribute("deception")
	public String modelDeception(final HttpServletRequest request, final ModelMap model) {
		final WizardContext modelContext = (WizardContext) model.get("wizardContext");
		final WizardContext wizardContext = modelContext == null ? wizardContext(request) : modelContext;
		if ( wizardContext != null && getUser() == null) {
			if (wizardContext.getPretenceOfUser() == null) {
				final OverseasUser user = new OverseasUser();
				user.populateFromWizardResults(wizardContext.getWizardResults());
				wizardContext.setPretenceOfUser(user);
			}
			model.addAttribute("user", wizardContext.getPretenceOfUser());
		}
		return null;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Adds the wizard context from the session into the model.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the wizard context.
	 * @since Jul 30, 2012
	 * @version Sep 21, 2012
	 */
	@ModelAttribute("wizardContext")
	public WizardContext wizardContext(final HttpServletRequest request) {
		final WizardContext wizardContext = SessionContextStorage.create(request).load();
		return wizardContext;
	}


    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
		return sendMethodNotAllowed();
    }
}
