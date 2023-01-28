/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;

/**
 * Extended {@link BaseControllerExam} integration test for {@link WhatsOnMyBallotController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 13, 2012
 * @version May 10, 2013
 */
public final class WhatsOnMyBallotControllerIntegration extends WhatsOnMyBallotControllerExam<WhatsOnMyBallotController> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 13, 2012
	 * @version Oct 16, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetVotingAddress() throws Exception {
		final String votingRegionState = "VA";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WhatsOnMyBallot.htm");
		request.setParameter("vrState", votingRegionState);
		final OverseasUser user = new OverseasUser();
		user.setVoterType(VoterType.DOMESTIC_VOTER);
		final UserAddress votingAddress = new UserAddress();
		votingAddress.setStreet1("9 Main Dr");
		votingAddress.setCity("Annandale");
		votingAddress.setState(votingRegionState);
		votingAddress.setZip("22003");
		user.setVotingAddress(votingAddress);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("There is a model", actualModel);
		final WhatsOnMyBallotForm actualForm = (WhatsOnMyBallotForm) actualModel.get("whatsOnMyBallot");
		assertNotNull("There is a what's on my ballot? form in the model", actualForm);
		final UserAddress actualAddress = actualForm.getAddress();
		assertNotNull("The form has an address", actualAddress);
		assertAddress("The address matches the user's voting address", votingAddress, actualAddress);
		assertSame("The form has the expected voter type", user.getVoterType(), actualForm.getVoterType());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is no user.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 13, 2012
	 * @version Oct 16, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetVotingAddress_noUser() throws Exception {
		final String votingRegionState = "VA";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WhatsOnMyBallot.htm");
		request.setParameter("vrState", votingRegionState);
		final OverseasUser user = null;
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("There is a model", actualModel);
		assertNull("There is no user in the model", actualModel.get("user"));
		final WhatsOnMyBallotForm actualForm = (WhatsOnMyBallotForm) actualModel.get("whatsOnMyBallot");
		assertNotNull("There is a what's on my ballot? form in the model", actualForm);
		final UserAddress actualAddress = actualForm.getAddress();
		assertNotNull("The form has an address", actualAddress);
		final UserAddress expectedAddress = new UserAddress();
		expectedAddress.setState(votingRegionState);
		assertAddress("The address just has the state set", expectedAddress, actualAddress);
		assertNull("The form does not have a voter type", actualForm.getVoterType());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the state is not supported.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 13, 2012
	 * @version Oct 16, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetVotingAddress_stateNotSupported() throws Exception {
		final String votingRegionState = "OK";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WhatsOnMyBallot.htm");
		request.setParameter("vrState", votingRegionState);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is a redirect to the candidate finder", WhatsOnMyBallotController.REDIRECT_CANDIDATE_FINDER,
				actualModelAndView.getViewName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handlePostVotingAddress(javax.servlet.http.HttpServletRequest, ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm, BindingResult)}
	 * for the case where the voting address is not valid.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 13, 2012
	 * @version May 10, 2013
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandlePostVotingAddress() throws Exception {
		final String votingRegionState = "VA";
		final State votingState = getStateService().findByAbbreviation(votingRegionState);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/WhatsOnMyBallot.htm");
		request.setParameter("voterType", VoterType.OVERSEAS_VOTER.name());
		request.setParameter("votingState.id", Long.toString(votingState.getId()));
		request.setParameter("votingState.abbr", votingState.getAbbr());
		request.setParameter("votingState.name", votingState.getName());
		request.setParameter("votingState.fipsCode", Integer.toString(votingState.getFipsCode()));
		request.setParameter("address.street1", "9 Main Dr");
		request.setParameter("address.city", "Annandale");
		request.setParameter("address.state", votingRegionState);
		request.setParameter("address.zip", "22003");
		final OverseasUser user = new OverseasUser();
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is a redirection to the what's on my ballot partisan party",
				WhatsOnMyBallotController.REDIRECT_WHATS_ON_MY_BALLOT_PARTISAN_PARTY, actualModelAndView.getViewName());
		assertNotNull("The form is added to the session", request.getSession().getAttribute("whatsOnMyBallot"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handlePostVotingAddress(javax.servlet.http.HttpServletRequest, ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm, BindingResult)}
	 * for the case where the voting address is not valid.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 13, 2012
	 * @version Aug 22, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandlePostVotingAddress_invalidAddress() throws Exception {
		final String votingRegionState = "VA";
		final State votingState = getStateService().findByAbbreviation(votingRegionState);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/WhatsOnMyBallot.htm");
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		request.setParameter("voterType", voterType.name());
		request.setParameter("votingState.id", Long.toString(votingState.getId()));
		request.setParameter("votingState.abbr", votingState.getAbbr());
		request.setParameter("votingState.name", votingState.getName());
		request.setParameter("votingState.fipsCode", Integer.toString(votingState.getFipsCode()));
		final String street1 = "1 Not A Valid Street";
		request.setParameter("address.street1", street1);
		final String city = "Invalid City";
		request.setParameter("address.city", city);
		request.setParameter("address.state", votingRegionState);
		final String zip = "12345";
		request.setParameter("address.zip", zip);
		final OverseasUser user = new OverseasUser();
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("There is a model", actualModel);
		final WhatsOnMyBallotForm actualForm = (WhatsOnMyBallotForm) actualModel.get("whatsOnMyBallot");
		assertNotNull("There is a what's on my ballot? form in the model", actualForm);
		final UserAddress actualAddress = actualForm.getAddress();
		assertNotNull("The form has an address", actualAddress);
		final UserAddress expectedAddress = new UserAddress();
		expectedAddress.setStreet1(street1);
		expectedAddress.setCity(city);
		expectedAddress.setState(votingRegionState);
		expectedAddress.setZip(zip);
		assertAddress("The address has the input values", expectedAddress, actualAddress);
		assertSame("The form has the expected voter type", voterType, actualForm.getVoterType());
		final BindingResult actualBindingResult = (BindingResult) actualModel
				.get("org.springframework.validation.BindingResult.whatsOnMyBallot");
		assertNotNull("There is a binding result for the form", actualBindingResult);
		assertTrue("There are errors in the form", actualBindingResult.hasErrors());
	}

	/** {@inheritDoc} */
	@Override
	protected final WhatsOnMyBallotController createWhatsOnMyBallotController() {
		final WhatsOnMyBallotController whatsOnMyBallotController = applicationContext.getBean(WhatsOnMyBallotController.class);
		return whatsOnMyBallotController;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForWhatsOnMyBallotController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForWhatsOnMyBallotController() {
	}

	/**
	 * Custom assertion to ensure that the address has the expected values.
	 * 
	 * @author IanBrown
	 * @param message
	 *            the message.
	 * @param expectedAddress
	 *            the expected address.
	 * @param actualAddress
	 *            the actual address.
	 * @since Aug 22, 2012
	 * @version Aug 22, 2012
	 */
	private void assertAddress(final String message, final UserAddress expectedAddress, final UserAddress actualAddress) {
		assertEquals(message + ": Street 1", expectedAddress.getStreet1(), actualAddress.getStreet1());
		assertEquals(message + ": Street 2", expectedAddress.getStreet2(), actualAddress.getStreet2());
		assertEquals(message + ": City", expectedAddress.getCity(), actualAddress.getCity());
		assertEquals(message + ": State", expectedAddress.getState(), actualAddress.getState());
		assertEquals(message + ": ZIP", expectedAddress.getZip(), actualAddress.getZip());
	}
}
