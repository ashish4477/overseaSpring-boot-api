/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterHistory;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link BaseControllerCheck} test for {@link StateVotingAddress}.
 * 
 * @author IanBrown
 * 
 * @since Jul 30, 2012
 * @version Oct 24, 2012
 */
public final class StateVotingAddressTest extends BaseControllerCheck<StateVotingAddress> {

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handleGetStateVotingAddress(javax.servlet.http.HttpServletRequest, String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where no address information is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	@Test
	public final void testHandleGetStateVotingAddress_noAddressInformation() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createMock("Model", ModelMap.class);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		EasyMock.expect(wizardResults.getVotingRegionState()).andReturn(null);
		EasyMock.expect(wizardResults.getVotingRegionName()).andReturn(null);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(null);
		final FlowType flowType = FlowType.FWAB;
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).anyTimes();
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		replayAll();

		final String actualResponse = getBaseController().handleGetStateVotingAddress(request, null, null, model, wizardContext);

		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final String expectedResponse = "redirect:" + SurveyWizard.getContinueUrl(request);
		assertEquals("The continue URL was returned", expectedResponse, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handleGetStateVotingAddress(javax.servlet.http.HttpServletRequest, String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where no state is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2012
	 * @version Sep 19, 2012
	 */
	@Test
	public final void testHandleGetStateVotingAddress_noState() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createMock("Model", ModelMap.class);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		EasyMock.expect(wizardResults.getVotingRegionName()).andReturn(null);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		EasyMock.expect(votingAddress.getState()).andReturn(null);
		final FlowType flowType = FlowType.FWAB;
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		replayAll();

		final String actualResponse = getBaseController().handleGetStateVotingAddress(request, null, null, model, wizardContext);

		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final String expectedResponse = "redirect:" + SurveyWizard.getContinueUrl(request);
		assertEquals("The continue URL was returned", expectedResponse, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handleGetStateVotingAddress(javax.servlet.http.HttpServletRequest, String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where the state provided is not supported.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 31, 2012
	 * @version Sep 19, 2012
	 */
	@Test
	public final void testHandleGetStateVotingAddress_stateNotSupported() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createMock("Model", ModelMap.class);
		final String votingAddressState = "ST";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingRegionName()).andReturn(null);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		EasyMock.expect(votingAddress.getState()).andReturn(votingAddressState);
		EasyMock.expect(getVotingPrecinctService().areRestrictedAddressesRequired(votingAddressState, null)).andReturn(false);
		final FlowType flowType = FlowType.FWAB;
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		replayAll();

		final String actualResponse = getBaseController().handleGetStateVotingAddress(request, null, null, model, wizardContext);

		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final String expectedResponse = "redirect:" + SurveyWizard.getContinueUrl(request);
		assertEquals("The continue URL was returned", expectedResponse, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handlePostStateVotingAddress(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.OverseasUser, org.springframework.validation.BindingResult, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2012
	 * @version Aug 3, 2012
	 */
	@Test
	public final void testHandlePostStateVotingAddress() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final ModelMap model = createModelMap(user, request, faceConfig, null, true, false);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final UserAddress votingAddress = createMock("VotingAddress", UserAddress.class);
		EasyMock.expect(user.getVotingAddress()).andReturn(votingAddress);
		final String votingAddressState = "VS";
		EasyMock.expect(votingAddress.getState()).andReturn(votingAddressState);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(votingAddressState)).andReturn(votingState);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, votingState)).andReturn(validAddress);
		final String county = "County";
		EasyMock.expect(votingAddress.getCounty()).andReturn(county);
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(getStateService().findRegion(votingState, county)).andReturn(votingRegion);
		user.setVotingRegion(votingRegion);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment);
		getVotingPrecinctService().fixAddress(votingAddress, streetSegment);
		wizardResults.populateFromUser(user);
		wizardResults.setReportable(false);
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect(wizardResults.createTemporary()).andReturn(temporary);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		getQuestionnaireService().saveWizardResults(wizardResults);
		wizardResults.copyFromTemporary(temporary);
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		replayAll();

		final String actualResponse = getBaseController().handlePostStateVotingAddress(request, model, user, errors, wizardContext);

		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final String expectedResponse = "redirect:" + SurveyWizard.getContinueUrl(request);
		assertEquals("The continue URL was returned", expectedResponse, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handlePostStateVotingAddress(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.OverseasUser, org.springframework.validation.BindingResult, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where the address is invalid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2012
	 * @version Oct 24, 2012
	 */
	@Test
	public final void testHandlePostStateVotingAddress_invalidAddress() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final ModelMap model = createModelMap(user, request, faceConfig, null, true, false);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB).anyTimes();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(wizardResults.getVotingRegionName()).andReturn(null);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(user.getVotingAddress()).andReturn(votingAddress);
		final String votingAddressState = "VS";
		EasyMock.expect(votingAddress.getState()).andReturn(votingAddressState).atLeastOnce();
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(votingAddressState)).andReturn(votingState);
		final ValidAddress validAddress = null;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, votingState)).andReturn(validAddress);
		errors.rejectValue(EasyMock.eq("votingAddress"), (String) EasyMock.anyObject(), (String) EasyMock.anyObject());
		EasyMock.expect(getVotingPrecinctService().areRestrictedAddressesRequired(votingAddressState, null)).andReturn(true);
		final WizardResultAddress wizardVotingAddress = createMock("WizardVotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(wizardVotingAddress).atLeastOnce();
		EasyMock.expect(wizardVotingAddress.getState()).andReturn(votingAddressState);
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(votingAddressState)).andReturn(state);
		final String zip = "012345";
		final List<String> zips = Arrays.asList(zip);
		EasyMock.expect(getVotingPrecinctService().findZipCodes(state)).andReturn(zips);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(getStateService().findRegionsForState(state)).andReturn(regions);
		EasyMock.expect(wizardVotingAddress.getCounty()).andReturn(null);
		EasyMock.expect(wizardVotingAddress.getStreet1()).andReturn(null);
		replayAll();

		final String actualResponse = getBaseController().handlePostStateVotingAddress(request, model, user, errors, wizardContext);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#modelDeception(HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a logged in user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2012
	 * @version Sep 24, 2012
	 */
	@Test
	public final void testModelDeception_loggedIn() {
		final HttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createMock("Model", ModelMap.class);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(model.get("wizardContext")).andReturn(wizardContext);
		replayAll();

		final String actualModelDeception = getBaseController().modelDeception(request, model);

		assertNull("There is no model deception", actualModelDeception);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#modelDeception(HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no logged in user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2012
	 * @version Sep 24, 2012
	 */
	@Test
	public final void testModelDeception_notLoggedIn() {
		final HttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createMock("Model", ModelMap.class);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		EasyMock.expect(model.get("wizardContext")).andReturn(wizardContext);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final OverseasUser pretenceOfUser = createMock("PretenceOfUser", OverseasUser.class);
		wizardContext.setPretenceOfUser(pretenceOfUser);
		EasyMock.expect(model.addAttribute("user", pretenceOfUser)).andReturn(model);
		replayAll();

		final String actualModelDeception = getBaseController().modelDeception(request, model);

		assertNull("There is no model deception", actualModelDeception);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#modelDeception(HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no logged in user and no pretense of a user yet.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2012
	 * @version Jul 24, 2012
	 */
//	@Test
//	public final void testModelDeception_noUser() {
//		final MockHttpServletRequest request = new MockHttpServletRequest();
//		final ModelMap model = createMock("Model", ModelMap.class);
//		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
//		final WizardContext wizardContext = new WizardContext(wizardResults);
//		EasyMock.expect(model.get("wizardContext")).andReturn( wizardContext );
//		final Authentication authentication = addAuthenticationToSecurityContext();
//		addOverseasUserToAuthentication( authentication, null );
//		EasyMock.expect(wizardResults.getUsername()).andReturn("user@name");
//		EasyMock.expect(wizardResults.getName()).andReturn( createPerson( "name" ) );
//		EasyMock.expect(wizardResults.getPreviousName()).andReturn(createPerson("previousName"));
//		EasyMock.expect(wizardResults.getPhone()).andReturn("12345678");
//		EasyMock.expect(wizardResults.getAlternateEmail()).andReturn("alternate@email");
//		EasyMock.expect(wizardResults.getAlternatePhone()).andReturn("19876543");
//		EasyMock.expect(wizardResults.getPhoneType()).andReturn("");
//		EasyMock.expect(wizardResults.getAlternatePhoneType()).andReturn("");
//		//EasyMock.expect(wizardResults.getVotingRegion()).andReturn(createVotingRegion());
//		EasyMock.expect( wizardResults.getEodRegionId() ).andReturn( "1" ).anyTimes();
//		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.CITIZEN_OVERSEAS_INDEFINITELY.name());
//		EasyMock.expect(wizardResults.getVoterHistory()).andReturn(VoterHistory.FIRST_TIME_VOTER.name());
//		EasyMock.expect(wizardResults.getBallotPref()).andReturn("ballotPref");
//		EasyMock.expect(wizardResults.getBirthYear()).andReturn(1992);
//		EasyMock.expect(wizardResults.getBirthMonth()).andReturn(7);
//		EasyMock.expect(wizardResults.getBirthDate()).andReturn(30);
//		EasyMock.expect(wizardResults.getRace()).andReturn("Race");
//		EasyMock.expect(wizardResults.getEthnicity()).andReturn("Ethnicity");
//		EasyMock.expect(wizardResults.getGender()).andReturn("Male");
//		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(createAddress("voting"));
//		EasyMock.expect(wizardResults.getCurrentAddress()).andReturn(createAddress("current"));
//		EasyMock.expect(wizardResults.getForwardingAddress()).andReturn(null);
//		EasyMock.expect(wizardResults.getPreviousAddress()).andReturn(null);
//		EasyMock.expect(model.addAttribute(EasyMock.eq("user"), EasyMock.anyObject())).andReturn(model);
//		replayAll();
//
//		final String actualModelDeception = getBaseController().modelDeception(request, model);
//
//		assertNull("There is no model deception", actualModelDeception);
//		verifyAll();
//	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#wizardContext(javax.servlet.http.HttpServletRequest)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2012
	 * @version Aug 2, 2012
	 */
	@Test
	public final void testWizardContext() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final FlowType flowType = FlowType.RAVA;
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		EasyMock.expect(wizardResults.getUser()).andReturn(null);
		replayAll();

		final WizardContext actualWizardContext = getBaseController().wizardContext(request);

		assertSame("The wizard context is added", wizardContext, actualWizardContext);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final StateVotingAddress createBaseController() {
		final StateVotingAddress stateVotingAddress = new StateVotingAddress();
		stateVotingAddress.setVotingPrecinctService(getVotingPrecinctService());
		stateVotingAddress.setQuestionnaireService(getQuestionnaireService());
		return stateVotingAddress;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return StateVotingAddress.CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return StateVotingAddress.PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return StateVotingAddress.SECION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return StateVotingAddress.SECTION_NAME;
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
		setQuestionnaireService(createMock("QuestionnaireService", QuestionnaireService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setQuestionnaireService(null);
		setVotingPrecinctService(null);
	}

	/**
	 * Creates an address using the prefix.
	 * 
	 * @author IanBrown
	 * @param prefix
	 *            the prefix.
	 * @return the address.
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	private WizardResultAddress createAddress(final String prefix) {
		final WizardResultAddress address = createMock(prefix, WizardResultAddress.class);
		EasyMock.expect(address.getAddressTo()).andReturn("Address To " + prefix);
		EasyMock.expect(address.getStreet1()).andReturn(1 + " " + prefix + " Street");
		EasyMock.expect(address.getStreet2()).andReturn("");
		EasyMock.expect(address.getCity()).andReturn(prefix + " City");
		EasyMock.expect(address.getState()).andReturn("ST");
		EasyMock.expect(address.getZip()).andReturn(Integer.toString(prefix.hashCode() % 100000));
		EasyMock.expect(address.getZip4()).andReturn("");
		EasyMock.expect(address.getCountry()).andReturn("");
		EasyMock.expect(address.getDescription()).andReturn("");
		EasyMock.expect(address.getCounty()).andReturn("");
		EasyMock.expect(address.getType()).andReturn(AddressType.STREET).atLeastOnce();
		return address;
	}

	/**
	 * Creates a person with the specified prefix.
	 * 
	 * @author IanBrown
	 * @param prefix
	 *            the prefix to use.
	 * @return the person.
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	private WizardResultPerson createPerson(final String prefix) {
		final WizardResultPerson person = createMock(prefix, WizardResultPerson.class);
		EasyMock.expect(person.getTitle()).andReturn(prefix + "Title");
		EasyMock.expect(person.getFirstName()).andReturn(prefix + "First");
		EasyMock.expect(person.getInitial()).andReturn(prefix.substring(0, 1));
		EasyMock.expect(person.getLastName()).andReturn(prefix + "Last");
		EasyMock.expect(person.getSuffix()).andReturn(prefix.substring(prefix.length() - 1));
		return person;
	}

	/**
	 * Creates a voting region.
	 * 
	 * @author IanBrown
	 * @return the voting region.
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	private VotingRegion createVotingRegion() {
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		return votingRegion;
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
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
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
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
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
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
	private void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}
}
