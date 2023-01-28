/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.editor.VotingRegionPropertyEditor;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.service.ZohoService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Extended {@link BaseControllerCheck} test for {@link GetVoterInformation}.
 * 
 * @author IanBrown
 * 
 * @since Jun 19, 2012
 * @version Sep 24, 2012
 */
public final class GetVoterInformationTest extends BaseControllerCheck<GetVoterInformation> {

	/**
	 * the overseas user validator.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private OverseasUserValidator overseasUserValidator;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the voting region property editor.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private VotingRegionPropertyEditor votingRegionPropertyEditor;

	/**
	 * Zoho service
	 *
	 * @author Shahriar Miraj
	 * @since Aug 11, 2022
	 * @version Aug 11, 2022
	 */
	private ZohoService zohoService;

	private EodApiService eodApiService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#getWizardContext(javax.servlet.http.HttpServletRequest)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testGetWizardContext() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpSession session = new MockHttpSession();
		request.setSession( session );
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication( authentication, user );
		final long userId = 12989l;
		EasyMock.expect(user.getId()).andReturn(userId).anyTimes();
		final FlowType flowType = FlowType.RAVA;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect( wizardResults.getUser() ).andReturn( user );
		final WizardContext wizardContext = createWizardContext(request, session, wizardResults, flowType);
		replayAll();

		final WizardContext actualWizardContext = getBaseController().getWizardContext( request );

		assertSame( "The wizard context is returned", wizardContext, actualWizardContext );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#handleGetVoterInformation(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, String, String, WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testHandleGetVoterInformation() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String votingAddressState = "ST";
		final String votingAddressZip = "12354";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, user );
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).atLeastOnce();
		votingAddress.setState( votingAddressState );
		votingAddress.setZip( votingAddressZip );
		EasyMock.expect( wizardResults.getEodRegionId() ).andReturn( null ).anyTimes();
		replayAll();

		getBaseController().handleGetVoterInformation(request, model, votingAddressState, votingAddressZip, wizardContext);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#handleGetVoterInformation(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, String, String, WizardContext)}
	 * for the case where there is no logged in user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testHandleGetVoterInformation_noLoggedInUser() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap( null, request, null, true, false );
		final String votingAddressState = "ST";
		final String votingAddressZip = "12354";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, null );
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn( votingAddress ).atLeastOnce();
		votingAddress.setState( votingAddressState );
		votingAddress.setZip( votingAddressZip );
		final OverseasUser pretenceOfUser = createMock("PretenceOfUser", OverseasUser.class);
		wizardContext.setPretenceOfUser( pretenceOfUser );
		final UserAddress pretenceOfAddress = createMock("PretenhceOfAddress", UserAddress.class);
		EasyMock.expect(pretenceOfUser.getVotingAddress()).andReturn(pretenceOfAddress).atLeastOnce();
		pretenceOfAddress.setState( votingAddressState );
		pretenceOfAddress.setZip( votingAddressZip );
		EasyMock.expect( pretenceOfAddress.getState() ).andReturn( votingAddressState ).anyTimes();
        List<EodRegion> regions = new ArrayList<EodRegion>( );
        EasyMock.expect( getEodApiService().getRegionsOfState( votingAddressState ) ).andReturn( regions );
		EasyMock.expect( pretenceOfUser.getEodRegionId() ).andReturn( "1" ).anyTimes();
        EasyMock.expect( wizardResults.getEodRegionId() ).andReturn( null ).anyTimes();
        EodRegion region = new EodRegion();
        EasyMock.expect( getEodApiService().getRegion( "1" ) ).andReturn( region ).anyTimes();
        addAttributeToModelMap( model, "regions", regions );
        addAttributeToModelMap( model, "selectedRegion", region );
        replayAll();

		getBaseController().handleGetVoterInformation( request, model, votingAddressState, votingAddressZip, wizardContext );

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#handleGetVoterInformation(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, String, String, WizardContext)}
	 * for the case where no state or ZIP are provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testHandleGetVoterInformation_noStateOrZip() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final ModelMap model = createModelMap( user, request, null, true, false );
		final String votingAddressState = null;
		final String votingAddressZip = null;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, user );
        EasyMock.expect( wizardResults.getEodRegionId() ).andReturn( null ).anyTimes();
		replayAll();

		getBaseController().handleGetVoterInformation( request, model, votingAddressState, votingAddressZip, wizardContext );

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#handlePostVoterInformation(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Boolean, com.bearcode.ovf.model.common.OverseasUser, org.springframework.validation.BindingResult, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Sep 5, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testHandlePostVoterInformation() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		final FaceConfig faceConfig = createMock( "FaceConfig", FaceConfig.class );
		final ModelMap model = createModelMap( user, request, faceConfig, null, true, false );
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final WizardResults wizardResults = createMock( "WizardResults", WizardResults.class );
		final WizardContext form = new WizardContext(wizardResults);
        form.setWizardUrlTemplate( SurveyWizard.BUILD_URL_TEMPLATE );
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, user );
		getOverseasUserValidator().validateUser( EasyMock.same( user ), EasyMock.same( errors ), (Set<String>) EasyMock.anyObject() );
		EasyMock.expect(errors.hasErrors()).andReturn( false );
		wizardResults.populateFromUser( user );
		wizardResults.setReportable( false );
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect( wizardResults.createTemporary() ).andReturn( temporary );
		EasyMock.expect(wizardResults.getUser()).andReturn( user ).atLeastOnce();
		getQuestionnaireService().saveWizardResults( wizardResults );
		wizardResults.copyFromTemporary( temporary );
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(wizardResults.getFlowType()).andReturn( flowType ).atLeastOnce();
		request.getSession().setAttribute( String.format( "%s.currentFlow", WizardContext.class.getName() ), flowType );
		request.getSession().setAttribute( String.format( "%s.%s", WizardContext.class.getName(), flowType.name() ), form );
		EasyMock.expect( user.getEodRegionId() ).andReturn( "1" ).anyTimes();
        EodRegion region = new EodRegion();
        region.setRegionName( "regionName" );
        EasyMock.expect( getEodApiService().getRegion( "1" ) ).andReturn( region ).anyTimes();
        wizardResults.setVotingRegionName( region.getName() );
        UserAddress votingAddress = new UserAddress( AddressType.STREET );
        votingAddress.setState( "ST" );
        EasyMock.expect( user.getVotingAddress() ).andReturn( votingAddress );
        wizardResults.setVotingRegionState( "ST" );
		replayAll();

		final String actualResponse = getBaseController().handlePostVoterInformation(request, model, null, user, errors, form);

		assertEquals("The response is a redirect to the next RAVA page", "redirect:/w/rava/0.htm", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#handlePostVoterInformation(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Boolean, com.bearcode.ovf.model.common.OverseasUser, org.springframework.validation.BindingResult, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where there are errors.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Aug 2, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testHandlePostVoterInformation_errors() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final ModelMap model = createModelMap(user, request, faceConfig, null, true, false);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext form = new WizardContext(wizardResults);
		EasyMock.expect(form.getFlowType()).andReturn(null).anyTimes();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final String userValidationSkipFields = "Skip";
		EasyMock.expect(faceConfig.getUserValidationSkipFields()).andReturn(userValidationSkipFields).anyTimes();
		getOverseasUserValidator().validateUser( EasyMock.same( user ), EasyMock.same( errors ), (Set<String>) EasyMock.anyObject() );
		EasyMock.expect( errors.hasErrors() ).andReturn( true );
        EasyMock.expect( wizardResults.getEodRegionId() ).andReturn( null ).anyTimes();
		replayAll();

		getBaseController().handlePostVoterInformation(request, model, null, user, errors, form);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#handlePostVoterInformation(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Boolean, com.bearcode.ovf.model.common.OverseasUser, org.springframework.validation.BindingResult, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where the user is not logged in and saving users isn't supported.
	 * 
	 * @author IanBrown
	 * 
	 * @since Sep 5, 2012
	 * @version Sep 5, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testHandlePostVoterInformation_noSavedUsers() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		final FaceConfig faceConfig = createMock( "FaceConfig", FaceConfig.class );
		final ModelMap model = createModelMap( null, request, faceConfig, null, true, false );
		final BindingResult errors = createMock( "Errors", BindingResult.class );
		final WizardResults wizardResults = createMock( "WizardResults", WizardResults.class );
		final WizardContext form = new WizardContext(wizardResults);
        form.setWizardUrlTemplate( SurveyWizard.BUILD_URL_TEMPLATE );
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, user );
		getOverseasUserValidator().validateUser( EasyMock.same( user ), EasyMock.same( errors ), (Set<String>) EasyMock.anyObject() );
		EasyMock.expect(errors.hasErrors()).andReturn( false );
		wizardResults.populateFromUser( user );
		wizardResults.setReportable( false );
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect( wizardResults.createTemporary() ).andReturn( temporary );
		EasyMock.expect(wizardResults.getUser()).andReturn( user ).atLeastOnce();
		getQuestionnaireService().saveWizardResults( wizardResults );
		wizardResults.copyFromTemporary( temporary );
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(wizardResults.getFlowType()).andReturn( flowType ).atLeastOnce();
		request.getSession().setAttribute( String.format( "%s.currentFlow", WizardContext.class.getName() ), flowType );
		request.getSession().setAttribute( String.format( "%s.%s", WizardContext.class.getName(), flowType.name() ), form );
		EasyMock.expect( user.getEodRegionId() ).andReturn( "1" ).anyTimes();
        EodRegion region = new EodRegion();
        region.setRegionName( "regionName" );
        EasyMock.expect( getEodApiService().getRegion( "1" ) ).andReturn( region ).anyTimes();
        wizardResults.setVotingRegionName( region.getName() );
        UserAddress votingAddress = new UserAddress( AddressType.STREET );
        votingAddress.setState( "ST" );
        EasyMock.expect( user.getVotingAddress() ).andReturn( votingAddress );
        wizardResults.setVotingRegionState( "ST" );
		replayAll();

		final String actualResponse = getBaseController().handlePostVoterInformation(request, model, true, user, errors, form);

		assertEquals("The response is a redirect to the next RAVA page", "redirect:/w/rava/0.htm", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#handlePostVoterInformation(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Boolean, com.bearcode.ovf.model.common.OverseasUser, org.springframework.validation.BindingResult, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where the user is not logged in.
	 * 
	 * @author IanBrown
	 * 
	 * @since Sep 5, 2012
	 * @version Sep 5, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testHandlePostVoterInformation_notLoggedIn() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock( "User", OverseasUser.class );

		final FaceConfig faceConfig = createMock( "FaceConfig", FaceConfig.class );
		EasyMock.expect( faceConfig.isAutoCreateAccount() ).andReturn( false ).atLeastOnce();
		final ModelMap model = createModelMap( user, request, faceConfig, null, true, false );
		final BindingResult errors = createMock( "Errors", BindingResult.class );
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext form = new WizardContext(wizardResults);
		EasyMock.expect(form.getFlowType()).andReturn(null).anyTimes();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, null );
		getOverseasUserValidator().validateUser( EasyMock.same( user ), EasyMock.same( errors ), (Set<String>) EasyMock.anyObject() );
		EasyMock.expect(errors.hasErrors()).andReturn( false );
		wizardResults.populateFromUser( user );
		wizardResults.setReportable( false );
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect( wizardResults.createTemporary() ).andReturn( temporary );
		EasyMock.expect(wizardResults.getUser()).andReturn( user ).anyTimes();
		getQuestionnaireService().saveWizardResults( wizardResults );
		wizardResults.copyFromTemporary( temporary );
		final FlowType flowType = FlowType.RAVA;
		request.getSession().setAttribute( String.format( "%s.currentFlow", WizardContext.class.getName() ), flowType );
		request.getSession().setAttribute( String.format( "%s.%s", WizardContext.class.getName(), flowType.name() ), form );
		EasyMock.expect( user.getEodRegionId() ).andReturn( "1" ).anyTimes();
        EodRegion region = new EodRegion();
        region.setRegionName( "regionName" );
        EasyMock.expect( getEodApiService().getRegion( "1" ) ).andReturn( region ).anyTimes();
        wizardResults.setVotingRegionName( region.getName() );
        UserAddress votingAddress = new UserAddress( AddressType.STREET );
        votingAddress.setState( "ST" );
        EasyMock.expect( user.getVotingAddress() ).andReturn( votingAddress );
        wizardResults.setVotingRegionState( "ST" );
		replayAll();

		final String actualResponse = getBaseController().handlePostVoterInformation(request, model, null, user, errors, form);

		assertEquals("The response is a redirect to the password creation page", "redirect:/CreatePassword.htm", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testInitBinder() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		binder.registerCustomEditor(VotingRegion.class, getVotingRegionPropertyEditor());
		final OverseasUser target = createMock("Target", OverseasUser.class);
		EasyMock.expect(binder.getTarget()).andReturn(target);
		binder.setValidator( getOverseasUserValidator() );
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * for the case where there is no target.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testInitBinder_noTarget() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		binder.registerCustomEditor(VotingRegion.class, getVotingRegionPropertyEditor());
		EasyMock.expect(binder.getTarget()).andReturn(null);
		replayAll();

		getBaseController().initBinder( binder );

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * for the case where the target is not a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testInitBinder_targetNotUser() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		binder.registerCustomEditor(VotingRegion.class, getVotingRegionPropertyEditor());
		final Object target = createMock("Target", Object.class);
		EasyMock.expect(binder.getTarget()).andReturn(target);
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#modelDeception(HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Sep 24, 2012
	 */
	@Test
	public final void testModelDeception() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap( user, request, null, false, false );
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext form = new WizardContext(wizardResults);
		EasyMock.expect(model.get("wizardContext")).andReturn( form );
		replayAll();

		final String actualDeception = getBaseController().modelDeception(request, model);

		assertNull("Nothing is returned", actualDeception);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#modelDeception(HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no user or pretence of a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Sep 24, 2012
	 */
//	@Test
//	public final void testModelDeception_noUserOrPretence() {
//		final MockHttpServletRequest request = new MockHttpServletRequest();
//		final Authentication authentication = addAuthenticationToSecurityContext();
//		addOverseasUserToAuthentication( authentication, null );
//		final ModelMap model = createModelMap(null, request, null, false, false);
//		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
//		final WizardContext form = new WizardContext(wizardResults);
//		EasyMock.expect(model.get( "wizardContext" )).andReturn(form);
//		final String username = "user@name";
//		EasyMock.expect(wizardResults.getUsername()).andReturn( username ).anyTimes();
//		final WizardResultPerson name = createMock("Name", WizardResultPerson.class);
//		EasyMock.expect(wizardResults.getName()).andReturn(name);
//		final String title = "Title";
//		EasyMock.expect(name.getTitle()).andReturn(title);
//		final String firstName = "First";
//		EasyMock.expect(name.getFirstName()).andReturn(firstName);
//		final String initial = "I";
//		EasyMock.expect(name.getInitial()).andReturn(initial);
//		final String lastName = "Last";
//		EasyMock.expect(name.getLastName()).andReturn(lastName);
//		final String suffix = "Suffix";
//		EasyMock.expect(name.getSuffix()).andReturn(suffix);
//		EasyMock.expect(wizardResults.getPreviousName()).andReturn(null);
//		final String phone = "1234567890";
//		EasyMock.expect(wizardResults.getPhone()).andReturn(phone);
//		EasyMock.expect(wizardResults.getAlternateEmail()).andReturn("");
//		EasyMock.expect(wizardResults.getAlternatePhone()).andReturn("");
//		EasyMock.expect(wizardResults.getPhoneType()).andReturn("");
//		EasyMock.expect(wizardResults.getAlternatePhoneType()).andReturn("");
//		final String voterType = VoterType.CITIZEN_OVERSEAS_INDEFINITELY.getName();
//		EasyMock.expect(wizardResults.getVoterType()).andReturn(voterType);
//		final String voterHistory = VoterHistory.FIRST_TIME_VOTER.getName();
//		EasyMock.expect(wizardResults.getVoterHistory()).andReturn(voterHistory);
//		final String ballotPref = "Ballot Pref";
//		EasyMock.expect(wizardResults.getBallotPref()).andReturn(ballotPref);
//		final int birthYear = 1990;
//		EasyMock.expect(wizardResults.getBirthYear()).andReturn(birthYear);
//		final int birthMonth = 6;
//		EasyMock.expect(wizardResults.getBirthMonth()).andReturn(birthMonth);
//		final int birthDate = 19;
//		EasyMock.expect(wizardResults.getBirthDate()).andReturn(birthDate);
//		final String race = "Race";
//		EasyMock.expect(wizardResults.getRace()).andReturn(race);
//		final String ethnicity = "Ethnicity";
//		EasyMock.expect(wizardResults.getEthnicity()).andReturn(ethnicity);
//		final String gender = "M";
//		EasyMock.expect(wizardResults.getGender()).andReturn(gender);
//		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
//		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
//		final String addressTo = "Address To";
//		EasyMock.expect(votingAddress.getAddressTo()).andReturn(addressTo);
//		final String street1 = "Street 1";
//		EasyMock.expect(votingAddress.getStreet1()).andReturn(street1);
//		final String street2 = "Street 2";
//		EasyMock.expect(votingAddress.getStreet2()).andReturn(street2);
//		final String city = "City";
//		EasyMock.expect(votingAddress.getCity()).andReturn(city);
//		final String state = "ST";
//		EasyMock.expect(votingAddress.getState()).andReturn(state);
//		final String zip = "12345";
//		EasyMock.expect(votingAddress.getZip()).andReturn(zip);
//		final String zip4 = "6789";
//		EasyMock.expect(votingAddress.getZip4()).andReturn(zip4);
//		final String country = "Country";
//		EasyMock.expect( votingAddress.getCountry() ).andReturn( country );
//		final String description = "Description";
//		EasyMock.expect( votingAddress.getDescription() ).andReturn( description );
//		final String county = "County";
//		EasyMock.expect( votingAddress.getCounty() ).andReturn( county );
//		final AddressType type = AddressType.STREET;
//		EasyMock.expect(votingAddress.getType()).andReturn(type).atLeastOnce();
//		EasyMock.expect( wizardResults.getCurrentAddress() ).andReturn( null );
//		EasyMock.expect(wizardResults.getForwardingAddress()).andReturn(null);
//		EasyMock.expect(wizardResults.getPreviousAddress()).andReturn(null);
//		addAttributeToModelMap( model, EasyMock.eq( "user" ), EasyMock.anyObject() );
//        EasyMock.expect( wizardResults.getEodRegionId() ).andReturn( "1" ).anyTimes();
//		replayAll();
//
//		final String actualDeception = getBaseController().modelDeception(request, model);
//
//		assertNull("Nothing is returned", actualDeception);
//		verifyAll();
//	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#modelDeception(HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no user, but there is a pretence of a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testModelDeception_noUserPretence() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, false, false);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext form = new WizardContext(wizardResults);
		final OverseasUser pretenceOfUser = createMock("PretenceOfUser", OverseasUser.class);
		form.setPretenceOfUser( pretenceOfUser );
		addAttributeToModelMap( model, "user", pretenceOfUser );
		replayAll();

		final String actualDeception = getBaseController().modelDeception(request, model);

		assertNull( "Nothing is returned", actualDeception );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.GetVoterInformation#validateUserFields(com.bearcode.ovf.model.common.OverseasUser, org.springframework.validation.BindingResult, java.util.Set)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testValidateUserFields() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final String skipValue = "Skip";
		final Set<String> skip = new HashSet<String>(Arrays.asList(skipValue));
		getOverseasUserValidator().validateUser(user, errors, skip);
		replayAll();

		final BindingResult actualErrors = getBaseController().validateUserFields(user, errors, skip);

		assertSame("The errors are returned", errors, actualErrors);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final GetVoterInformation createBaseController() {
		final GetVoterInformation getVoterInformation = new GetVoterInformation();
		ReflectionTestUtils.setField(getVoterInformation, "overseasUserValidator", getOverseasUserValidator());
		ReflectionTestUtils.setField(getVoterInformation, "votingPrecinctService", getVotingPrecinctService());
		ReflectionTestUtils.setField(getVoterInformation, "questionnaireService", getQuestionnaireService());
		ReflectionTestUtils.setField(getVoterInformation, "votingRegionPropertyEditor", getVotingRegionPropertyEditor());
		ReflectionTestUtils.setField(getVoterInformation, "eodApiService", getEodApiService());
		ReflectionTestUtils.setField(getVoterInformation, "zohoService", getZohoService());
		return getVoterInformation;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/wizard/VoterInformation.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Voter Information";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return "/css/rava.css";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return "rava";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setOverseasUserValidator( createMock( "OverseasUserValidator", OverseasUserValidator.class ) );
		setVotingPrecinctService( createMock( "VotingPrecinctService", VotingPrecinctService.class ) );
		setQuestionnaireService( createMock( "QuestionnaireService", QuestionnaireService.class ) );
		setVotingRegionPropertyEditor( createMock( "VotingRegionPropertyEditor", VotingRegionPropertyEditor.class ) );
		setEodApiService( createMock( "EodApiService", EodApiService.class ) );
		setZohoService( createMock( "ZohoService", ZohoService.class ) );
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setVotingRegionPropertyEditor(null);
		setQuestionnaireService( null );
		setVotingPrecinctService( null );
		setOverseasUserValidator( null );
        setEodApiService( null );
		setZohoService( null );
	}

	/**
	 * Creates a wizard context wrapping the results.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param session
	 *            the session.
	 * @param wizardResults
	 *            the wizard results.
	 * @param flowType
	 *            the flow type.
	 * @return the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private WizardContext createWizardContext(final MockHttpServletRequest request, final MockHttpSession session,
			final WizardResults wizardResults, final FlowType flowType) {
		session.setAttribute((String) ReflectionTestUtils.getField(SessionContextStorage.create(request), "ACTIVE_FLOW_KEY"),
				flowType);
		final String flowKey = flowKey(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
        wizardContext.setWizardUrlTemplate( SurveyWizard.BUILD_URL_TEMPLATE );
		session.setAttribute(flowKey, wizardContext);
		return wizardContext;
	}

	/**
	 * Creates a flow key for the input flow type.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type.
	 * @return the flow key.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private String flowKey(final FlowType flowType) {
		return String.format("%s.%s", WizardContext.class.getName(), flowType.name());
	}

	/**
	 * Gets the overseas user validator.
	 * 
	 * @author IanBrown
	 * @return the overseas user validator.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private OverseasUserValidator getOverseasUserValidator() {
		return overseasUserValidator;
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Gets the voting region property editor.
	 * 
	 * @author IanBrown
	 * @return the voting region property editor.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private VotingRegionPropertyEditor getVotingRegionPropertyEditor() {
		return votingRegionPropertyEditor;
	}

	/**
	 * Sets the overseas user validator.
	 * 
	 * @author IanBrown
	 * @param overseasUserValidator
	 *            the overseas user validator to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setOverseasUserValidator(final OverseasUserValidator overseasUserValidator) {
		this.overseasUserValidator = overseasUserValidator;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
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
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Sets the voting region property editor.
	 * 
	 * @author IanBrown
	 * @param votingRegionPropertyEditor
	 *            the voting region property editor to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setVotingRegionPropertyEditor(final VotingRegionPropertyEditor votingRegionPropertyEditor) {
		this.votingRegionPropertyEditor = votingRegionPropertyEditor;
	}

	public EodApiService getEodApiService() {
		return eodApiService;
	}

	public void setEodApiService( EodApiService eodApiService ) {
		this.eodApiService = eodApiService;
	}

	public ZohoService getZohoService() {
		return zohoService;
	}

	public void setZohoService(ZohoService zohoService) {
		this.zohoService = zohoService;
	}
}
