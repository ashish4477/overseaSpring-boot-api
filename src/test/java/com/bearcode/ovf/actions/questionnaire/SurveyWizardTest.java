/**
 *
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.QuestionnaireArbiter;
import com.bearcode.ovf.validators.AnswerValidator;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;

/**
 * Extended {@link BaseControllerCheck} for {@link SurveyWizard}.
 *
 * @author IanBrown
 *
 * @since Jun 19, 2012
 * @version Sep 4, 2012
 */
public final class SurveyWizardTest extends BaseControllerCheck<SurveyWizard> {

	/**
	 * the mailing list service.
	 *
	 * @author IanBrown
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private MailingListService mailingListService;

	/**
	 * the question field service.
	 *
	 * @author IanBrown
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private QuestionFieldService questionFieldService;

	/**
	 * the questionnaire arbiter.
	 *
	 * @author IanBrown
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private QuestionnaireArbiter questionnaireArbiter;

	/**
	 * the questionnaire service.
	 *
	 * @author IanBrown
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#buildUrl(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, int[])}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	// TODO fails on Jenkins.
	public final void testBuildUrl() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final int page1 = 1, page2 = 2;
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		final QuestionnairePage currentPage = createCurrentPage(wizardResults, page1);
		replayAll();
		final WizardContext form = createWizardContext(null, wizardResults, null, currentPage);

		final String actualUrl = SurveyWizard.buildUrl(form, page1);

		assertTrue("The URL contains the flow type", actualUrl.contains(flowType.toString().toLowerCase()));
		assertTrue("The URL contains the first page number", actualUrl.contains(Integer.toString(page1)));
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.WizardContext#createFlowPageUrl(java.lang.String, int)}.
	 * This method was moved from SurveyWizard into WizardContext. (LG)
     *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testCreateFlowPageUrl() {
		final String flow = "Flow";
		final int page = 82;
        final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
        final WizardContext form = createWizardContext(null, wizardResults, null, null);
		replayAll();

		final String actualFlowPageUrl = form.createFlowPageUrl(flow, page);

		assertTrue("The flow page URL contains the flow string", actualFlowPageUrl.contains(flow.toLowerCase()));
		assertTrue("The flow page URL contains the page", actualFlowPageUrl.contains(Integer.toString(page)));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#createWizard(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.model.questionnaire.FlowType)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testCreateWizard() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FlowType flowType = FlowType.FWAB;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createUser();
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.getPhoneType()).andReturn( "" ).anyTimes();
		EasyMock.expect(user.isOptIn()).andReturn( false ).anyTimes();
		EasyMock.expect(user.getAlternatePhoneType()).andReturn( "" ).anyTimes();
		EasyMock.expect(user.getVoterClassificationType()).andReturn(null).anyTimes();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig((String) EasyMock.anyObject())).andReturn(faceConfig);
		final String urlPath = "/url/path/";
		EasyMock.expect(faceConfig.getUrlPath()).andReturn(urlPath);
		final Map<String, String> presetPdfAnswersFields = new HashMap<String, String>();
		EasyMock.expect(faceConfig.getPresetPdfAnswersFields()).andReturn(presetPdfAnswersFields);
		final int pageCount = 3;
		EasyMock.expect(getQuestionnaireService().countPages(PageType.OVERSEAS)).andReturn(pageCount);
		questionnaireService.saveWizardResults( (WizardResults) EasyMock.anyObject() );
		EasyMock.expect( user.getEodRegionId() ).andReturn( "1" ).anyTimes();
		replayAll();

		final WizardContext actualWizardContext = getBaseController().createWizard(request, flowType);

		assertNotNull("A wizard context is returned", actualWizardContext);
		final WizardResults wizardResults = actualWizardContext.getWizardResults();
		assertWizardResults(user, wizardResults);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#doCancel(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem canceling the survey.
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testDoCancel() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final OverseasUser user = createUser();
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		wizardResults.setReportable(false);
		saveWizardResults(wizardResults, false);
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		replayAll();
		final WizardContext form = createWizardContext(request, wizardResults, null, null);

		final String actualCancel = getBaseController().doCancel(request, form);

		final String expectedCancel = "redirect:"
				+ (String) ReflectionTestUtils.getField(getBaseController(), "CANCEL_REDIRECT_URL_LOGGED_IN");
		assertEquals("The cancel is a redirect", expectedCancel, actualCancel);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#doCancel(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where the user is not logged in.
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem canceling the survey.
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testDoCancel_notLoggedIn() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final OverseasUser user = null;
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		wizardResults.setReportable(false);
		saveWizardResults(wizardResults, false);
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		replayAll();
		final WizardContext form = createWizardContext(request, wizardResults, null, null);

		final String actualCancel = getBaseController().doCancel(request, form);

		final String expectedCancel = "redirect:"
				+ (String) ReflectionTestUtils.getField(getBaseController(), "CANCEL_REDIRECT_URL_NON_LOGGED_IN");
		assertEquals("The cancel is a redirect", expectedCancel, actualCancel);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#doFinish(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem finishing the wizard.
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testDoFinish() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		//getMailingListService().saveToMailingListIfHasSignup(wizardResults);
		replayAll();
		final WizardContext wizardContext = createWizardContext(request, wizardResults, null, null);

		final String actualFinish = getBaseController().doFinish(request, wizardContext);

		assertTrue("Finish goes to home", actualFinish.toUpperCase().contains("HOME"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#doFinish(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where the form is complete.
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem finishing the wizard.
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testDoFinish_isCompleted() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		//getMailingListService().saveToMailingListIfHasSignup(wizardResults);
		final QuestionnairePage currentPage = createCurrentPage(wizardResults, 2);
		replayAll();
		final WizardContext wizardContext = createWizardContext(request, wizardResults, null, currentPage);
		wizardContext.setPageCount(2);

		final String actualFinish = getBaseController().doFinish(request, wizardContext);

		assertTrue("Finish goes to download", actualFinish.toUpperCase().contains("DOWNLOAD"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#getContinuePage(javax.servlet.http.HttpServletRequest)}.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testGetContinuePage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int startPage = 989;
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", Integer.toString(startPage));
		replayAll();

		final int actualContinuePage = SurveyWizard.getContinuePage(request);

		assertEquals("The continue page is the start page", startPage, actualContinuePage);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#getContinuePage(javax.servlet.http.HttpServletRequest)} for the
	 * case where there is no start page.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testGetContinuePage_noStartPage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

		final int actualContinuePage = SurveyWizard.getContinuePage(request);

		assertEquals("No continue page", -1, actualContinuePage);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#getContinueUrl(javax.servlet.http.HttpServletRequest)}.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testGetContinueUrl() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int startPage = 12980;
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", Integer.toString(startPage));
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).anyTimes();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		EasyMock.expect(wizardResults.getUser()).andReturn(null);
		replayAll();
		createWizardContext(request, wizardResults, flowType, null);

		final String actualContinueUrl = SurveyWizard.getContinueUrl(request);

		assertTrue("The continue URL contains the start page", actualContinueUrl.contains(Integer.toString(startPage)));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#getContinueUrl(javax.servlet.http.HttpServletRequest)} for the
	 * case where there is no start page.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	// TODO fails on Jenkins.
	public final void testGetContinueUrl_noStartPage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

		final String actualContinueUrl = SurveyWizard.getContinueUrl(request);

		assertNull("There is no continue URL", actualContinueUrl);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#getFlowType(java.lang.String)}.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testGetFlowType() {
		final FlowType flowType = FlowType.RAVA;
		final String flow = flowType.name();
		replayAll();

		final FlowType actualFlowType = getBaseController().getFlowType(flow);

		assertSame("The flow type is correct", flowType, actualFlowType);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#getWizardContext(javax.servlet.http.HttpServletRequest, java.lang.String)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	@Test
	public final void testGetWizardContext() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		final String flow = flowType.name();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig((String) EasyMock.anyObject())).andReturn(faceConfig);
		final String urlPath = "url/path/";
		EasyMock.expect(faceConfig.getUrlPath()).andReturn(urlPath);
		final Map<String, String> presetPdfAnswersFields = new HashMap<String, String>();
		EasyMock.expect(faceConfig.getPresetPdfAnswersFields()).andReturn(presetPdfAnswersFields);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		EasyMock.expect(getQuestionnaireService().countPages(PageType.DOMESTIC_ABSENTEE)).andReturn(2);
		getQuestionnaireService().saveWizardResults((WizardResults) EasyMock.anyObject());
		replayAll();

		final WizardContext actualWizardContext = getBaseController().getWizardContext(request, flow);

		assertNotNull("A wizard context is returned", actualWizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#handleFlowPageGetMethod(javax.servlet.http.HttpServletRequest, java.lang.String, int, String, String, com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.common.OverseasUser, org.springframework.ui.ModelMap)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem handling the get.
	 * @since Jun 19, 2012
	 * @version Aug 15, 2012
	 */
	@Test
	public final void testHandleFlowPageGetMethod() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final String flow = flowType.name();
		final int pageNumber = 3;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		final OverseasUser user = createUser();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(getQuestionnaireService().findPageByNumber(1, PageType.DOMESTIC_REGISTRATION)).andReturn(page);
		EasyMock.expect(page.getNumber()).andReturn(pageNumber).atLeastOnce();
		getQuestionnaireArbiter().adjustPage(EasyMock.same(page), (WizardContext) EasyMock.anyObject());
		final Map<Long, Answer> answersAsMap = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answersAsMap).atLeastOnce();
		getQuestionnaireArbiter().checkAnswer(page, answersAsMap);
		final String pageTitle = "Page Title";
		EasyMock.expect(page.getTitle()).andReturn(pageTitle).atLeastOnce();
		wizardResults.setCurrentPageTitle(pageTitle);
		EasyMock.expect(page.isEmpty()).andReturn(false).atLeastOnce();
		final Collection<FieldDependency> fieldDependencies = new ArrayList<FieldDependency>();
		EasyMock.expect(getQuestionFieldService().findFieldDependencies()).andReturn(fieldDependencies);
		getQuestionnaireArbiter().applyFieldDependencies(page, fieldDependencies, answersAsMap);
		addAttributeToModelMap(model, EasyMock.eq("formUrl"), EasyMock.anyObject());
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		replayAll();
		final WizardContext wizardContext = createWizardContext(request, wizardResults, flowType, null);

		final String actualResponse = getBaseController().handleFlowPageGetMethod(request, flow, pageNumber, null, null,
				wizardContext, user, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#handleFlowPagePostMethod(javax.servlet.http.HttpServletRequest, java.lang.String, int, com.bearcode.ovf.actions.questionnaire.forms.WizardContext, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem posting.
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testHandleFlowPagePostMethod() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final String flow = flowType.name();
		final int pageNumber = 3;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final OverseasUser user = createUser();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		replayAll();
		final WizardContext wizardContext = createWizardContext(request, wizardResults, flowType, null);

		final String actualResponse = getBaseController().handleFlowPagePostMethod(request, flow, pageNumber, wizardContext,
				errors, model);

		assertTrue("A redirect is returned", actualResponse.startsWith("redirect:"));
		assertTrue("The page number is specified in the redirect", actualResponse.contains("/" + pageNumber + ".htm"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#handleFlowStartPage(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String, String, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Oct 19, 2012
	 */
	@Test
	public final void testHandleFlowStartPage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FlowType flowType = FlowType.RAVA;
		final String flow = flowType.name();
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		final String voterTypeStr = voterType.name();
		final String voterClassificationType = VoterClassificationType.CITIZEN_NEVER_RESIDED.name();
		final String votingRegionState = "ST";
		final String votingRegionName = "Voting Region";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		wizardResults.setVoterType(voterTypeStr);
		wizardResults.setVoterClassificationType(voterClassificationType);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(votingRegionState)).andReturn(votingState);
		EasyMock.expect(votingState.getAbbr()).andReturn(votingRegionState).atLeastOnce();
		EasyMock.expect(votingAddress.getState()).andReturn(null);
		votingAddress.setState(votingRegionState);
		votingAddress.setStreet1(null);
		votingAddress.setStreet2(null);
		votingAddress.setCounty(null);
		votingAddress.setCity(null);
		votingAddress.setZip(null);
		wizardResults.setVotingRegion(null);
		//EasyMock.expect(wizardResults.getVotingRegion()).andReturn(null);
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		//EasyMock.expect(getStateService().findRegion(votingState, votingRegionName)).andReturn(votingRegion);
		//wizardResults.setVotingRegion(votingRegion);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createUser();
		addOverseasUserToAuthentication(authentication, user);
        final FaceConfig faceConfig = createMock("faceConfig", FaceConfig.class);
        EasyMock.expect(getFacesService().findConfig("localhost")).andReturn(faceConfig).anyTimes();
        EasyMock.expect(faceConfig.isUseCaptcha()).andReturn(true).anyTimes();
		replayAll();
		final WizardContext wizardContext = createWizardContext(request, wizardResults, flowType, null);

		final String actualResponse = getBaseController().handleFlowStartPage(request, flow, voterTypeStr, voterClassificationType, votingRegionState,
				votingRegionName, wizardContext);

		assertTrue("A redirect is returned", actualResponse.startsWith("redirect:"));
		assertTrue("The first page number is specified in the redirect", actualResponse.contains("/1.htm"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * with an answer target.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testInitBinder_answwerTarget() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		final Object target = createMock("Target", Answer.class);
		EasyMock.expect(binder.getTarget()).andReturn(target);
		binder.setValidator((AnswerValidator) EasyMock.anyObject());
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * with no target.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testInitBinder_noTarget() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		EasyMock.expect(binder.getTarget()).andReturn(null);
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * with an object target.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testInitBinder_objectTarget() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		final Object target = createMock("Target", Object.class);
		EasyMock.expect(binder.getTarget()).andReturn(target);
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#saveWizardContext(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where there is no logged in user.
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem saving the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSaveWizardContext_noLoggedInUser() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final OverseasUser user = createUser();
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		replayAll();
		final WizardContext wizardContext = createWizardContext(null, wizardResults, null, null);

		getBaseController().saveWizardContext(wizardContext);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#saveWizardContext(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where there is user for the results.
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem saving the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSaveWizardContext_noResultsUser() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final OverseasUser user = createUser();
		EasyMock.expect(wizardResults.getUser()).andReturn(null).atLeastOnce();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		replayAll();
		final WizardContext wizardContext = createWizardContext(null, wizardResults, null, null);

		getBaseController().saveWizardContext(wizardContext);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#saveWizardContext(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where there is no user.
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem saving the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testSaveWizardContext_noUser() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardResults.getUser()).andReturn(null).atLeastOnce();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		wizardResults.setReportable(false);
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect(wizardResults.createTemporary()).andReturn(temporary);
//		final Collection<Answer> anonymizedAnswers = new ArrayList<Answer>();
//		EasyMock.expect(wizardResults.anonymize()).andReturn(anonymizedAnswers);
		getQuestionnaireService().saveWizardResults(wizardResults);
		wizardResults.copyFromTemporary(temporary);
		replayAll();
		final WizardContext wizardContext = createWizardContext(null, wizardResults, null, null);

		final OverseasUser actualUser = getBaseController().saveWizardContext(wizardContext);

		assertNull("No user is returned", actualUser);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#saveWizardContext(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where there is a user.
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem saving the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testSaveWizardContext_user() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final OverseasUser user = createUser();
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		wizardResults.setReportable(false);
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect(wizardResults.createTemporary()).andReturn(temporary);
		getQuestionnaireService().saveWizardResults(wizardResults);
		wizardResults.copyFromTemporary(temporary);
		replayAll();
		final WizardContext wizardContext = createWizardContext(null, wizardResults, null, null);

		final OverseasUser actualUser = getBaseController().saveWizardContext(wizardContext);

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#setApplicationContext(org.springframework.context.ApplicationContext)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testSetApplicationContext() {
		final ApplicationContext applicationContext = createMock("ApplicationContext", ApplicationContext.class);
		replayAll();

		getBaseController().setApplicationContext(applicationContext);

		assertSame("The application context is set", applicationContext,
				ReflectionTestUtils.getField(getBaseController(), "applicationContext"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#setContinuePage(javax.servlet.http.HttpServletRequest, int)}.
	 *
	 * @author IanBrown
	 *
	 * @since Jun 19, 2012
	 * @version Jun 21, 2012
	 */
	@Test
	public final void testSetContinuePage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int pageNumber = 5;
		replayAll();

		SurveyWizard.setContinuePage(request, pageNumber);

		assertEquals("The page is set", Integer.toString(pageNumber),
				request.getSession().getAttribute(SurveyWizard.class.getName() + ".startPage"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#showPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem showing the page.
	 * @since Jun 19, 2012
	 * @version Sep 4, 2012
	 */
	@Test
	public final void testShowPage() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final OverseasUser user = createUser();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		replayAll();
		final WizardContext wizardContext = createWizardContext(request, wizardResults, null, null);

		final String actualResponse = getBaseController().showPage(request, model, wizardContext);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyWizard#validateAnswers(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.actions.questionnaire.forms.WizardContext, org.springframework.validation.BindingResult)}
	 * .
	 *
	 * @author IanBrown
	 *
	 * @throws Exception
	 *             if there is a problem validating the answers.
	 * @since Jun 19, 2012
	 * @version Aug 15, 2012
	 */
	@Test
	public final void testValidateAnswers() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final OverseasUser user = createUser();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final BindingResult binder = createMock("Binder", BindingResult.class);
		replayAll();
		final WizardContext formObject = createWizardContext(request, wizardResults, null, null);

		final BindingResult actualBindingResult = getBaseController().validateAnswers(request, formObject, binder);

		assertSame("The binder is returned", binder, actualBindingResult);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final SurveyWizard createBaseController() {
		final SurveyWizard surveyWizard = new SurveyWizard();
		ReflectionTestUtils.setField(surveyWizard, "questionnaireService", getQuestionnaireService());
		ReflectionTestUtils.setField(surveyWizard, "mailingListService", getMailingListService());
		ReflectionTestUtils.setField(surveyWizard, "arbiter", getQuestionnaireArbiter());
		ReflectionTestUtils.setField(surveyWizard, "questionFieldService", getQuestionFieldService());
		return surveyWizard;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/wizard/WizardPage.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return null;
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
		setQuestionnaireService(createMock("QuestionnaireService", QuestionnaireService.class));
		setMailingListService(createMock("MailingListService", MailingListService.class));
		setQuestionnaireArbiter(createMock("QuestionnaireArbiter", QuestionnaireArbiter.class));
		setQuestionFieldService(createMock("QuestionFieldService", QuestionFieldService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setQuestionFieldService(null);
		setQuestionnaireArbiter(null);
		setMailingListService(null);
		setQuestionnaireService(null);
	}

	/**
	 * Custom assertion to ensure that the actual address matches the expected one.
	 *
	 * @author IanBrown
	 * @param prefix
	 *            the prefix.
	 * @param expectedAddress
	 *            the expected address.
	 * @param actualAddress
	 *            the actual address.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void assertAddress(final String prefix, final UserAddress expectedAddress, final WizardResultAddress actualAddress) {
		if (expectedAddress == null) {
			assertNull("The " + prefix + " address is not set", actualAddress);

		} else {
			assertSame("The " + prefix + " type is set", expectedAddress.getType(), actualAddress.getType());
			assertEquals("The " + prefix + " street line 1 is set", expectedAddress.getStreet1(), actualAddress.getStreet1());
			assertEquals("The " + prefix + " street line 2 is set", expectedAddress.getStreet2(), actualAddress.getStreet2());
			assertEquals("The " + prefix + " city is set", expectedAddress.getCity(), actualAddress.getCity());
			assertEquals("The " + prefix + " state is set", expectedAddress.getState(), actualAddress.getState());
			assertEquals("The " + prefix + " ZIP is set", expectedAddress.getZip(), actualAddress.getZip());
			assertEquals("The " + prefix + " ZIP+4 is set", expectedAddress.getZip4(), actualAddress.getZip4());
			assertEquals("The " + prefix + " country is set", expectedAddress.getCountry(), actualAddress.getCountry());
			assertEquals("The " + prefix + " description is set", expectedAddress.getDescription(), actualAddress.getDescription());
			assertEquals("The " + prefix + " county is set", expectedAddress.getCounty(), actualAddress.getCounty());
		}
	}

	/**
	 * Custom assert to ensure that the actual name matches the expected one.
	 *
	 * @author IanBrown
	 * @param prefix
	 *            the name prefix.
	 * @param expectedName
	 *            the expected name.
	 * @param actualName
	 *            the actual name.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void assertName(final String prefix, final Person expectedName, final WizardResultPerson actualName) {
		if (expectedName == null) {
			assertNull("There is no " + prefix + " name", actualName);

		} else {
			assertEquals("The " + prefix + " title is set", expectedName.getTitle(), actualName.getTitle());
			assertEquals("The " + prefix + " first name is set", expectedName.getFirstName(), actualName.getFirstName());
			assertEquals("The " + prefix + " initial is set", expectedName.getInitial(), actualName.getInitial());
			assertEquals("The " + prefix + " last name is set", expectedName.getLastName(), actualName.getLastName());
			assertEquals("The " + prefix + " suffix is set", expectedName.getSuffix(), actualName.getSuffix());
		}
	}

	/**
	 * Custom assertion to ensure that the user information is copied to the wizard results.
	 *
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void assertWizardResults(final OverseasUser user, final WizardResults wizardResults) {
		assertSame("The user is set", user, wizardResults.getUser());
		assertEquals("The user name is set", user.getUsername(), wizardResults.getUsername());
		assertName("current", user.getName(), wizardResults.getName());
		assertName("previous", user.getPreviousName(), wizardResults.getPreviousName());
		assertEquals("The phone number is set", user.getPhone(), wizardResults.getPhone());
		assertEquals("The alternate email is set", user.getAlternateEmail(), wizardResults.getAlternateEmail());
		assertEquals("The alternate phone is set", user.getAlternatePhone(), wizardResults.getAlternatePhone());
		assertSame("The voting region is set", user.getEodRegionId(), wizardResults.getEodRegionId());
		assertEquals("The voter type is set", user.getVoterType().name(), wizardResults.getVoterType());
		assertEquals("The voter history is set", user.getVoterHistory().name(), wizardResults.getVoterHistory());
		assertEquals("The ballot preference is set", user.getBallotPref(), wizardResults.getBallotPref());
		assertEquals("The birth date is set", user.getBirthDate(), wizardResults.getBirthDate());
		assertEquals("The birth year is set", user.getBirthYear(), wizardResults.getBirthYear());
		assertEquals("The birth month is set", user.getBirthMonth(), wizardResults.getBirthMonth());
		assertEquals("The race is set", user.getRace(), wizardResults.getRace());
		assertEquals("The ethnicity is set", user.getEthnicity(), wizardResults.getEthnicity());
		assertEquals("The gender is set", user.getGender(), wizardResults.getGender());
		assertEquals("The party is set", user.getParty(), wizardResults.getParty());
		assertAddress("voting", user.getVotingAddress(), wizardResults.getVotingAddress());
		assertAddress("current", user.getCurrentAddress(), wizardResults.getCurrentAddress());
		assertAddress("forwarding", user.getForwardingAddress(), wizardResults.getForwardingAddress());
		assertAddress("previous", user.getPreviousAddress(), wizardResults.getPreviousAddress());
	}

	/**
	 * Creates an address with the specified prefix.
	 *
	 * @author IanBrown
	 * @param prefix
	 *            the prefix.
	 * @param type
	 *            the type of address.
	 * @return the address.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private UserAddress createAddress(final String prefix, final AddressType type) {
		final UserAddress address = createMock(prefix + "Address", UserAddress.class);
		EasyMock.expect(address.getType()).andReturn(type).anyTimes();
		EasyMock.expect(address.getStreet1()).andReturn("1 " + prefix + " Street").anyTimes();
		EasyMock.expect(address.getStreet2()).andReturn("Unit " + prefix).anyTimes();
		EasyMock.expect(address.getCity()).andReturn(prefix + " City").anyTimes();
		EasyMock.expect(address.getState()).andReturn(prefix.toUpperCase().substring(0, 2)).anyTimes();
		final String prefixHash = Integer.toString(prefix.hashCode());
		EasyMock.expect(address.getZip()).andReturn(prefixHash.substring(0, 5)).anyTimes();
		EasyMock.expect(address.getZip4()).andReturn(prefixHash.substring(prefixHash.length() - 4)).anyTimes();
		EasyMock.expect(address.getCountry()).andReturn("Country of " + prefix).anyTimes();
		EasyMock.expect(address.getDescription()).andReturn(prefix).anyTimes();
		EasyMock.expect(address.getCounty()).andReturn(prefix + " County").anyTimes();
		return address;
	}

	/**
	 * Creates a current page for the wizard results.
	 *
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @param pageNumber
	 *            the page number.
	 * @return the current page.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private QuestionnairePage createCurrentPage(final WizardResults wizardResults, final int pageNumber) {
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		EasyMock.expect(currentPage.getNumber()).andReturn(pageNumber).anyTimes();
		return currentPage;
	}

	/**
	 * Creates a name with the specified name prefix.
	 *
	 * @author IanBrown
	 * @param prefix
	 *            the name prefix.
	 * @return the name.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private Person createName(final String prefix) {
		final Person name = createMock(prefix + "Name", Person.class);
		EasyMock.expect(name.getTitle()).andReturn(prefix.substring(0, 2)).anyTimes();
		EasyMock.expect(name.getFirstName()).andReturn(prefix + "first").anyTimes();
		EasyMock.expect(name.getInitial()).andReturn(prefix.substring(0, 1)).anyTimes();
		EasyMock.expect(name.getLastName()).andReturn(prefix + "last").anyTimes();
		EasyMock.expect(name.getSuffix()).andReturn(prefix.substring(prefix.length() - 2)).anyTimes();
		return name;
	}

	/**
	 * Creates a state with the specified name.
	 *
	 * @author IanBrown
	 * @param name
	 *            the name of the state.
	 * @return the state.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private State createState(final String name) {
		final State state = createMock(name.replaceAll(" ", ""), State.class);
		EasyMock.expect(state.getName()).andReturn(name).anyTimes();
		EasyMock.expect(state.getAbbr())
				.andReturn(name.substring(0, 1) + name.substring(name.indexOf(" ") + 1, name.indexOf(" ") + 2)).anyTimes();
		return state;
	}

	/**
	 * Creates a user for testing.
	 *
	 * @author IanBrown
	 * @return the user.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private OverseasUser createUser() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(user.getUsername()).andReturn("user@somewhere").anyTimes();
		EasyMock.expect(user.getName()).andReturn(createName("current")).anyTimes();
		EasyMock.expect(user.getPreviousName()).andReturn(createName("previous")).anyTimes();
		EasyMock.expect(user.getPhone()).andReturn("1-234-567-8901").anyTimes();
		EasyMock.expect(user.getAlternateEmail()).andReturn("user@somewhere.else").anyTimes();
		EasyMock.expect(user.getAlternatePhone()).andReturn("1-987-654-3210").anyTimes();
		//EasyMock.expect(user.getVotingRegion()).andReturn(createVotingRegion()).anyTimes();
		EasyMock.expect(user.getVoterType()).andReturn(VoterType.DOMESTIC_VOTER).anyTimes();
		EasyMock.expect(user.getVoterHistory()).andReturn(VoterHistory.FIRST_TIME_VOTER).anyTimes();
		EasyMock.expect(user.getBallotPref()).andReturn("Ballot Pref").anyTimes();
		EasyMock.expect(user.getBirthDate()).andReturn(20).anyTimes();
		EasyMock.expect(user.getBirthYear()).andReturn(2012).anyTimes();
		EasyMock.expect(user.getBirthMonth()).andReturn(6).anyTimes();
		EasyMock.expect(user.getRace()).andReturn("Race").anyTimes();
		EasyMock.expect(user.getEthnicity()).andReturn("Ethnicity").anyTimes();
		EasyMock.expect(user.getGender()).andReturn("Gender").anyTimes();
		EasyMock.expect(user.getParty()).andReturn("Party").anyTimes();
		EasyMock.expect(user.getVotingAddress()).andReturn(createAddress("Voting", AddressType.STREET)).anyTimes();
		EasyMock.expect(user.getCurrentAddress()).andReturn(createAddress("Current", AddressType.MILITARY)).anyTimes();
		EasyMock.expect(user.getForwardingAddress()).andReturn(createAddress("Forwarding", AddressType.OVERSEAS)).anyTimes();
		EasyMock.expect(user.getPreviousAddress()).andReturn(createAddress("Previous", AddressType.RURAL_ROUTE)).anyTimes();
		return user;
	}

	/**
	 * Creates a voting region.
	 *
	 * @author IanBrown
	 * @return the voting region.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private VotingRegion createVotingRegion() {
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(votingRegion.getName()).andReturn("Voting Region").anyTimes();
		EasyMock.expect(votingRegion.getState()).andReturn(createState("Voting State")).anyTimes();
		return votingRegion;
	}

	/**
	 * Creates a wizard context using the specified wizard results.
	 *
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param wizardResults
	 *            the wizard results.
	 * @param flowType
	 *            the flow type.
	 * @param currentPage
	 *            the current page.
	 * @return the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 20, 2012
	 */
	private WizardContext createWizardContext(final HttpServletRequest request, final WizardResults wizardResults,
			final FlowType flowType, final QuestionnairePage currentPage) {
		final WizardContext wizardContext = new WizardContext(wizardResults);
        wizardContext.setWizardUrlTemplate( SurveyWizard.BUILD_URL_TEMPLATE );
		if (flowType != null) {
			request.getSession().setAttribute(
					(String) ReflectionTestUtils.getField(SessionContextStorage.create(request), "ACTIVE_FLOW_KEY"), flowType);
			final String flowKey = flowKey(flowType);
			request.getSession().setAttribute(flowKey, wizardContext);
		}
		if (currentPage != null) {
			wizardContext.setCurrentPage(currentPage);
		}
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
	 * @version Jun 20, 2012
	 */
	private String flowKey(final FlowType flowType) {
		return String.format("%s.%s", WizardContext.class.getName(), flowType.name());
	}

	/**
	 * Gets the mailing list service.
	 *
	 * @author IanBrown
	 * @return the mailing list service.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private MailingListService getMailingListService() {
		return mailingListService;
	}

	/**
	 * Gets the question field service.
	 *
	 * @author IanBrown
	 * @return the question field service.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

	/**
	 * Gets the questionnaire arbiter.
	 *
	 * @author IanBrown
	 * @return the questionnaire arbiter.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private QuestionnaireArbiter getQuestionnaireArbiter() {
		return questionnaireArbiter;
	}

	/**
	 * Gets the questionnaire service.
	 *
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Sets up the mocks to save the wizard results.
	 *
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @param anonymize
	 *            <code>true</code> to anonymize the results, <code>false</code> otherwise.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void saveWizardResults(final WizardResults wizardResults, final boolean anonymize) {
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect(wizardResults.createTemporary()).andReturn(temporary);
		if (anonymize) {
			final Answer anonymizedAnswer = createMock("AnonymizedAnswer", Answer.class);
			final Collection<Answer> anonymizedAnswers = Arrays.asList(anonymizedAnswer);
			EasyMock.expect(wizardResults.anonymize()).andReturn(anonymizedAnswers);
		}
		getQuestionnaireService().saveWizardResults(wizardResults);
		wizardResults.copyFromTemporary(temporary);
	}

	/**
	 * Sets the mailing list service.
	 *
	 * @author IanBrown
	 * @param mailingListService
	 *            the mailing list service to set.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void setMailingListService(final MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	/**
	 * Sets the question field service.
	 *
	 * @author IanBrown
	 * @param questionFieldService
	 *            the question field service to set.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void setQuestionFieldService(final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

	/**
	 * Sets the questionnaire arbiter.
	 *
	 * @author IanBrown
	 * @param questionnaireArbiter
	 *            the questionnaire arbiter to set.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void setQuestionnaireArbiter(final QuestionnaireArbiter questionnaireArbiter) {
		this.questionnaireArbiter = questionnaireArbiter;
	}

	/**
	 * Sets the questionnaire service.
	 *
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since Jun 20, 2012
	 * @version Jun 20, 2012
	 */
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}
}
