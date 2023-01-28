/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.validators.WhatsOnMyBallotValidator;

/**
 * Extended {@link BaseControllerCheck} test for {@link WhatsOnMyBallotController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 13, 2012
 * @version May 10, 2013
 */
public final class WhatsOnMyBallotControllerTest extends BaseControllerCheck<WhatsOnMyBallotController> {

	/**
	 * the what's on my ballot? validator.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private WhatsOnMyBallotValidator whatsOnMyBallotValidator;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 21, 2012
	 * @version Aug 21, 2012
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the input state is not what's in the form.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 22, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetVotingAddress_differentState() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = "WI";
		final String votingRegionName = null;
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = this.addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final State oldVotingState = createMock("OldVotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(oldVotingState);
		final String oldVotingRegionState = "DS";
		EasyMock.expect(oldVotingState.getAbbr()).andReturn(oldVotingRegionState);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(votingRegionState)).andReturn(votingState);
		EasyMock.expect(votingState.getAbbr()).andReturn(votingRegionState).atLeastOnce();
		EasyMock.expect(getVotingPrecinctService().isReady(votingRegionState, null)).andReturn(true);
		whatsOnMyBallot.setVotingState(votingState);
		whatsOnMyBallot.setRegion(null);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		EasyMock.expect(address.getState()).andReturn(oldVotingRegionState);
		address.setStreet1(null);
		address.setStreet2(null);
		address.setCity(null);
		address.setState(votingRegionState);
		address.setZip(null);
		EasyMock.expect(address.getStreet1()).andReturn(null);
		addAttributeToModelMap(model, "whatsOnMyBallot", whatsOnMyBallot);
		final String zipCode = "12357";
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(getVotingPrecinctService().findZipCodes(votingState)).andReturn(zipCodes);
		addAttributeToModelMap(model, "zipCodes", zipCodes);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(getStateService().findRegionsForState(votingState)).andReturn(regions);
		addAttributeToModelMap(model, "regions", regions);
		replayAll();

		final String actualResponse = getBaseController().handleGetVotingAddress(request, votingRegionState, votingRegionName,
				model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is no state.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 22, 2012
	 * @version Aug 22, 2012
	 */
	@Test
	public final void testHandleGetVotingAddress_noState() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = null;
		final String votingRegionName = null;
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = this.addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(null);
		replayAll();

		final String actualResponse = getBaseController().handleGetVotingAddress(request, votingRegionState, votingRegionName,
				model, whatsOnMyBallot);

		assertEquals("The response is a redirection to the candidate finder", WhatsOnMyBallotController.REDIRECT_CANDIDATE_FINDER,
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is no user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 22, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetVotingAddress_noUser() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = "WI";
		final String votingRegionName = null;
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = this.addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		EasyMock.expect(votingState.getAbbr()).andReturn(votingRegionState).atLeastOnce();
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(votingRegionState, null)).andReturn(true);
		whatsOnMyBallot.setVotingState(votingState);
		whatsOnMyBallot.setRegion(null);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		EasyMock.expect(address.getState()).andReturn(votingRegionState);
		addAttributeToModelMap(model, "whatsOnMyBallot", whatsOnMyBallot);
		final String houseNumber = "10";
		final String streetName = "Street Name";
		final String street1 = houseNumber + " " + streetName;
		EasyMock.expect(address.getStreet1()).andReturn(street1);
		addAttributeToModelMap(model, "houseNumber", houseNumber);
		addAttributeToModelMap(model, "streetName", streetName);
		final String zipCode = "12357";
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(getVotingPrecinctService().findZipCodes(votingState)).andReturn(zipCodes);
		addAttributeToModelMap(model, "zipCodes", zipCodes);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(getStateService().findRegionsForState(votingState)).andReturn(regions);
		addAttributeToModelMap(model, "regions", regions);
		replayAll();

		final String actualResponse = getBaseController().handleGetVotingAddress(request, votingRegionState, votingRegionName,
				model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the input state is all the information we've got.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 22, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetVotingAddress_state() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = "WI";
		final String votingRegionName = null;
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = this.addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(null);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(votingRegionState)).andReturn(votingState);
		EasyMock.expect(votingState.getAbbr()).andReturn(votingRegionState).atLeastOnce();
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(votingRegionState, null)).andReturn(true);
		whatsOnMyBallot.setVotingState(votingState);
		whatsOnMyBallot.setRegion(null);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(null);
		whatsOnMyBallot.setAddress((UserAddress) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new WhatsOnMyBallotForm() {

			@Override
			public final void setAddress(final UserAddress address) {
				assertEquals("The address state is set", votingRegionState, address.getState());
			}
		});
		addAttributeToModelMap(model, "whatsOnMyBallot", whatsOnMyBallot);
		final String zipCode = "12357";
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(getVotingPrecinctService().findZipCodes(votingState)).andReturn(zipCodes);
		addAttributeToModelMap(model, "zipCodes", zipCodes);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(getStateService().findRegionsForState(votingState)).andReturn(regions);
		addAttributeToModelMap(model, "regions", regions);
		replayAll();

		final String actualResponse = getBaseController().handleGetVotingAddress(request, votingRegionState, votingRegionName,
				model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the input state is not supported.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 22, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetVotingAddress_stateNotSupported() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = "WI";
		final String votingRegionName = null;
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = this.addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(null);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(votingRegionState)).andReturn(votingState);
		EasyMock.expect(votingState.getAbbr()).andReturn(votingRegionState);
		EasyMock.expect(getVotingPrecinctService().isReady(votingRegionState, null)).andReturn(false);
		replayAll();

		final String actualResponse = getBaseController().handleGetVotingAddress(request, votingRegionState, votingRegionName,
				model, whatsOnMyBallot);

		assertEquals("The response is a redirection to the candidate finder", WhatsOnMyBallotController.REDIRECT_CANDIDATE_FINDER,
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handleGetVotingAddress(javax.servlet.http.HttpServletRequest, java.lang.String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 22, 2012
	 * @version Oct 19, 2012
	 */
	@Test
	public final void testHandleGetVotingAddress_user() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = "WI";
		final String votingRegionName = null;
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final OverseasUser user = createMock("User", OverseasUser.class);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = this.addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		EasyMock.expect(votingState.getAbbr()).andReturn(votingRegionState).atLeastOnce();
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(votingRegionState, null)).andReturn(true);
		whatsOnMyBallot.setVotingState(votingState);
		whatsOnMyBallot.setRegion(null);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(null);
		final UserAddress votingAddress = createMock("VotingAddress", UserAddress.class);
		EasyMock.expect(user.getVotingAddress()).andReturn(votingAddress);
		final String houseNumber = "10";
		final String streetName = "Street Name";
		final String street1 = houseNumber + " " + streetName;
		EasyMock.expect(votingAddress.getStreet1()).andReturn(street1).atLeastOnce();
		final String street2 = "Street 2";
		EasyMock.expect(votingAddress.getStreet2()).andReturn(street2).atLeastOnce();
		final String city = "City";
		EasyMock.expect(votingAddress.getCity()).andReturn(city).atLeastOnce();
		EasyMock.expect(votingAddress.getState()).andReturn(votingRegionState).atLeastOnce();
		final String zipCode = "12357";
		EasyMock.expect(votingAddress.getZip()).andReturn(zipCode).atLeastOnce();
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(null);
		EasyMock.expect(user.getVotingRegion()).andReturn(null);
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(user.getVoterType()).andReturn(voterType);
		whatsOnMyBallot.setVoterType(voterType);
		whatsOnMyBallot.setAddress((UserAddress) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new WhatsOnMyBallotForm() {
			@Override
			public final void setAddress(final UserAddress address) {
				assertEquals("The street1 line is set", street1, address.getStreet1());
				assertEquals("The street2 line is set", street2, address.getStreet2());
				assertEquals("The city is set", city, address.getCity());
				assertEquals("The state is set", votingRegionState, address.getState());
				assertEquals("The ZIP code is set", zipCode, address.getZip());
			}
		});
		addAttributeToModelMap(model, "whatsOnMyBallot", whatsOnMyBallot);
		addAttributeToModelMap(model, "houseNumber", houseNumber);
		addAttributeToModelMap(model, "streetName", streetName);
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(getVotingPrecinctService().findZipCodes(votingState)).andReturn(zipCodes);
		addAttributeToModelMap(model, "zipCodes", zipCodes);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(getStateService().findRegionsForState(votingState)).andReturn(regions);
		addAttributeToModelMap(model, "regions", regions);
		replayAll();

		final String actualResponse = getBaseController().handleGetVotingAddress(request, votingRegionState, votingRegionName,
				model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#handlePostVotingAddress(javax.servlet.http.HttpServletRequest, ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm, BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandlePostVotingAddress() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		EasyMock.expect(errors.hasErrors()).andReturn(false);
		replayAll();

		final String actualResponse = getBaseController().handlePostVotingAddress(request, model, whatsOnMyBallot, errors);

		assertEquals("The response is a redirect to the what's on my ballot? partisan party",
				WhatsOnMyBallotController.REDIRECT_WHATS_ON_MY_BALLOT_PARTISAN_PARTY, actualResponse);
		assertSame("The form is added to the session", whatsOnMyBallot, request.getSession().getAttribute("whatsOnMyBallot"));
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#whatsOnMyBallot(HttpServletRequest)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 24, 2012
	 */
	@Test
	public final void testWhatsOnMyBallot() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

		final WhatsOnMyBallotForm actualWhatsOnMyBallot = getBaseController().whatsOnMyBallot(request);

		assertNotNull("A what's on my ballot? form is returned", actualWhatsOnMyBallot);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotController#whatsOnMyBallot(HttpServletRequest)} for the
	 * case where the request already has the form.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 24, 2012
	 * @version Aug 24, 2012
	 */
	@Test
	public final void testWhatsOnMyBallot_requestHasForm() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);
		replayAll();

		final WhatsOnMyBallotForm actualWhatsOnMyBallot = getBaseController().whatsOnMyBallot(request);

		assertSame("The what's on my ballot? form from the service is returned", whatsOnMyBallot, actualWhatsOnMyBallot);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final WhatsOnMyBallotController createBaseController() {
		final WhatsOnMyBallotController whatsOnMyBallotController = new WhatsOnMyBallotController();
		whatsOnMyBallotController.setVotingPrecinctService(getVotingPrecinctService());
		whatsOnMyBallotController.setWhatsOnMyBallotValidator(getWhatsOnMyBallotValidator());
		return whatsOnMyBallotController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return WhatsOnMyBallotController.DEFAULT_CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return WhatsOnMyBallotController.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return WhatsOnMyBallotController.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return WhatsOnMyBallotController.DEFAULT_SECTION_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setVotingPrecinctService(createMock("VotingPrecinctService", VotingPrecinctService.class));
		setWhatsOnMyBallotValidator(createMock("WhatsOnMyBallotValidator", WhatsOnMyBallotValidator.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setWhatsOnMyBallotValidator(null);
		setVotingPrecinctService(null);
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 21, 2012
	 * @version Aug 21, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Gets the what's on my ballot? validator.
	 * 
	 * @author IanBrown
	 * @return the what's on my ballot? validator.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private WhatsOnMyBallotValidator getWhatsOnMyBallotValidator() {
		return whatsOnMyBallotValidator;
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
	private void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Sets the what's on my ballot? validator.
	 * 
	 * @author IanBrown
	 * @param whatsOnMyBallotValidator
	 *            the what's on my ballot? validator to set.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private void setWhatsOnMyBallotValidator(final WhatsOnMyBallotValidator whatsOnMyBallotValidator) {
		this.whatsOnMyBallotValidator = whatsOnMyBallotValidator;
	}
}
