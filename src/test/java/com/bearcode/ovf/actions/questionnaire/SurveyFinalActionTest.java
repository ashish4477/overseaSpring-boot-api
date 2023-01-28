/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.PdfFormTrack;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.tools.pdf.CreatePdfExecutor;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import static org.junit.Assert.*;
import java.util.UUID;

/**
 * Extended {@link BaseControllerCheck} test for {@link SurveyFinalAction}.
 * 
 * @author IanBrown
 * 
 * @since Jun 19, 2012
 * @version Jun 30, 2012
 */
public final class SurveyFinalActionTest extends BaseControllerCheck<SurveyFinalAction> {

    private CreatePdfExecutor createPdfExecutor;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyFinalAction#buildReferences(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testBuildReferences() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		addAttributeToModelMap(model, "downloadExpiration", getBaseController().getDownloadExpiration());
		replayAll();

		final String actualReferences = getBaseController().buildReferences(request, model, wizardContext);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualReferences);
		verifyAll();
	}

/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyFinalAction#buildReferences(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext) for the case where the form is completed
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Aug 30, 2012
	 */
	@Test
	public final void testBuildReferences_formIsCompleted() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		addAttributeToModelMap(model, "downloadExpiration", getBaseController().getDownloadExpiration());
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final int currentPageNumber = 12;
		EasyMock.expect(currentPage.getNumber()).andReturn(currentPageNumber).atLeastOnce();
		wizardContext.setPageCount(currentPageNumber);
		addAttributeToModelMap(model, "formValid", true);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).atLeastOnce();
		addAttributeToModelMap(model, EasyMock.eq("wizardUrl"), EasyMock.anyObject());
		wizardResults.setUuid(EasyMock.anyObject(String.class));
        final PdfFormTrack track = new PdfFormTrack();
        final long id = 99l;
        track.setId( id );
        EasyMock.expect( getCreatePdfExecutor().getFormTrack( wizardContext)).andReturn( track );
		EasyMock.expect( wizardContext.getWizardResults().getUuid()).andReturn( null ).anyTimes();
        addAttributeToModelMap( model, "generationId", id );
        getCreatePdfExecutor().createPdfFormFile(id, wizardContext);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		final String actualReferences = getBaseController().buildReferences(request, model, wizardContext);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
		 		actualReferences);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyFinalAction#formBackingObject(javax.servlet.http.HttpServletRequest)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testFormBackingObject() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

		final WizardContext actualBackingObject = getBaseController().formBackingObject(request);

		assertNotNull("A wizard context is returned", actualBackingObject);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyFinalAction#formBackingObject(javax.servlet.http.HttpServletRequest)} for
	 * the case where there is an existing context.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testFormBackingObject_existingContext() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpSession session = (MockHttpSession) request.getSession();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = createWizardContext(request, session, wizardResults, flowType);
		EasyMock.expect(wizardResults.getUser()).andReturn(user);
		replayAll();

		final WizardContext actualBackingObject = getBaseController().formBackingObject(request);

		assertSame("The wizard context is returned", wizardContext, actualBackingObject);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.SurveyFinalAction#getDownloadExpiration()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testGetDownloadExpiration() {
		final int actualDownloadExpiration = getBaseController().getDownloadExpiration();

		assertTrue("There is a download expiration set", actualDownloadExpiration > 0);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.SurveyFinalAction#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testOnSubmit() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpSession session = (MockHttpSession) request.getSession();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = createWizardContext(request, session, wizardResults, flowType);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		addAttributeToModelMap(model, "expired", true);
		addAttributeToModelMap(model, "formValid", false);
		replayAll();

		getBaseController().onSubmit(request, model, wizardContext);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.SurveyFinalAction#setDownloadExpiration(int)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testSetDownloadExpiration() {
		final int downloadExpiration = 20901;

		getBaseController().setDownloadExpiration(downloadExpiration);

		assertEquals("The download expiration is set", downloadExpiration, getBaseController().getDownloadExpiration());
	}

	/** {@inheritDoc} */
	@Override
	protected final SurveyFinalAction createBaseController() {
		final SurveyFinalAction surveyFinalAction = new SurveyFinalAction();
        ReflectionTestUtils.setField(surveyFinalAction, "createPdfExecutor", getCreatePdfExecutor());
		return surveyFinalAction;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/wizard/DownloadLinkPage.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Form Download";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return "/css/rava.css";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return "rava Download";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
        setCreatePdfExecutor( createMock("CreatePdfExecutor", CreatePdfExecutor.class) );
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
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


    public CreatePdfExecutor getCreatePdfExecutor() {
        return createPdfExecutor;
    }

    public void setCreatePdfExecutor(CreatePdfExecutor createPdfExecutor) {
        this.createPdfExecutor = createPdfExecutor;
    }
}
