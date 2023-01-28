/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;

/**
 * Extended {@link WhatsOnMyBallotControllerExam} integration test for
 * {@link WhatsOnMyBallotPartisanPartyController}.
 * 
 * @author Ian Brown
 * @since May 10, 2013
 * @version May 10, 2013
 */
public final class WhatsOnMyBallotPartisanPartyControllerIntegration extends
        WhatsOnMyBallotControllerExam<WhatsOnMyBallotPartisanPartyController> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotPartisanPartyController#handleGetPartisanParty(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * .
	 * 
	 * @author Ian Brown
	 * @throws Exception if there is a problem handling the request to get the partisan party.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetPartisanParty() throws Exception {
		final String votingRegionState = "VA";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WhatsOnMyBallotPartisanParty.htm");
		final WhatsOnMyBallotForm whatsOnMyBallot = new WhatsOnMyBallotForm();
		whatsOnMyBallot.setVoterType(VoterType.OVERSEAS_VOTER);
		whatsOnMyBallot.setVotingState(getStateService().findByAbbreviation(votingRegionState));
		final UserAddress address = new UserAddress();
		address.setStreet1("2 E Guinevere Dr SE");
		address.setCity("Annandale");
		address.setState(votingRegionState);
		address.setZip("22003");
		whatsOnMyBallot.setAddress(address);
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("There is a model", actualModel);
		final WhatsOnMyBallotForm actualForm = (WhatsOnMyBallotForm) actualModel.get("whatsOnMyBallot");
		assertNotNull("There is a what's on my ballot? form in the model", actualForm);
		assertNotNull("There is a list of partisan parties in the model", actualModel.get("partisanParties"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotPartisanPartyController#handlePostPartisanParty(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author Ian Brown
	 * @throws Exception if there is a problem posting the partisan party.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandlePostPartisanParty() throws Exception {
		final String votingRegionState = "VA";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/WhatsOnMyBallotPartisanParty.htm");
		request.setParameter("partisanParty", "Democrat");
		final WhatsOnMyBallotForm whatsOnMyBallot = new WhatsOnMyBallotForm();
		whatsOnMyBallot.setVoterType(VoterType.OVERSEAS_VOTER);
		whatsOnMyBallot.setVotingState(getStateService().findByAbbreviation(votingRegionState));
		final UserAddress address = new UserAddress();
		address.setStreet1("2 E Guinevere Dr SE");
		address.setCity("Annandale");
		address.setState(votingRegionState);
		address.setZip("22003");
		whatsOnMyBallot.setAddress(address);
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());
		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is a redirection to the what's on my ballot list",
				WhatsOnMyBallotController.REDIRECT_WHATS_ON_MY_BALLOT_LIST, actualModelAndView.getViewName());
		assertNotNull("The form is added to the session", request.getSession().getAttribute("whatsOnMyBallot"));
	}

	/** {@inheritDoc} */
	@Override
	protected final WhatsOnMyBallotPartisanPartyController createWhatsOnMyBallotController() {
		final WhatsOnMyBallotPartisanPartyController whatsOnMyBallotPartisanPartyController = applicationContext
		        .getBean(WhatsOnMyBallotPartisanPartyController.class);
		return whatsOnMyBallotPartisanPartyController;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForWhatsOnMyBallotController() {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForWhatsOnMyBallotController() {
		// TODO Auto-generated method stub

	}
}
