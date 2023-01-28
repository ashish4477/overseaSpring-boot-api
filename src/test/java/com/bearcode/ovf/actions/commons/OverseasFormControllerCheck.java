/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.utils.SecurityContextHelper;

/**
 * Abstract test for implementations of {@link OverseasFormController
 * @author IanBrown
 * 
 * @param <C> the type of overseas form controller to test.
 * @since Dec 9, 2011
 * @version Jul 25, 2012
 */
public abstract class OverseasFormControllerCheck<C extends OverseasFormController>
		extends EasyMockSupport {

	/**
	 * the security context.
	 * 
	 * @author IanBrown
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private SecurityContext securityContext;

	/**
	 * the overseas form controller to test.
	 * 
	 * @author IanBrown
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	private C overseasFormController;

	/**
	 * the original security context.
	 * 
	 * @author IanBrown
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private SecurityContext originalSecurityContext;

	/**
	 * Sets up to test the overseas form controller.
	 * 
	 * @author IanBrown
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Before
	public final void setUpOverseasFormController() {
		setUpForOverseasFormController();
		setSecurityContext(createMock("SecurityContext", SecurityContext.class));
		setOriginalSecurityContext(SecurityContextHolder.getContext());
		SecurityContextHolder.setContext(getSecurityContext());
		setOverseasFormController(createOverseasFormController());
	}

	/**
	 * Tears down the overseas form controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@After
	public final void tearDownOverseasFormController() {
		setOverseasFormController(null);
		SecurityContextHolder.setContext(getOriginalSecurityContext());
		setSecurityContext(null);
		setOriginalSecurityContext(null);
		tearDownForOverseasFormController();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#buildSuccessModelAndView(HttpServletRequest, Object, org.springframework.validation.BindException, java.util.Map)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem building the success model and view.
	 * @since Dec 19, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Test
	public final void testBuildSuccessModelAndViewRequestObjectExceptionMap()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Object object = createReferenceObject();
		final Errors errors = createMock("Errors", Errors.class);
		final StateService stateService = createMock("StateService",
				StateService.class);
		getOverseasFormController().setStateService(stateService);
		final FacesService facesService = createMock("FacesService",
				FacesService.class);
		getOverseasFormController().setFacesService(facesService);
		setUpBasicReferenceData(request, object, errors, stateService,
				facesService);
		setUpMinimalReferenceData(request, object, errors, stateService,
				facesService);
		final Object command = createCommand();
		final BindException bindException = createMock("BindException",
				BindException.class);
		final Map model = new HashMap();
		final String successContentBlock = "Success Content Block";
		getOverseasFormController().setSuccessContentBlock(successContentBlock);
		final String approvedFilename = "Approved Filename";
		EasyMock.expect(
				facesService.getApprovedFileName(successContentBlock,
						"Relative Prefix")).andReturn(approvedFilename)
				.atLeastOnce();
		replayAll();

		final ModelAndView actualModelAndView = getOverseasFormController()
				.buildSuccessModelAndView(request, command, bindException,
						model);

		assertNotNull("A model and view is returned", actualModelAndView);
		assertNull("There is no view", actualModelAndView.getView());
		assertNull("There is no view name", actualModelAndView.getViewName());
		final Map<String, Object> actualModel = actualModelAndView.getModel();
		assertNotNull("There is a model", actualModel);
		assertMinimalReferenceData(request, object, errors, stateService,
				facesService, actualModel);
		assertBasicReferenceData(request, object, errors, stateService,
				facesService, approvedFilename, actualModel);
		assertEquals("The command is set", command,
				actualModel.get(getOverseasFormController().getCommandName()));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#buildSuccessModelAndView(HttpServletRequest, Object, org.springframework.validation.BindException, String, Object)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem building the success model and view.
	 * @since Dec 19, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Test
	public final void testBuildSuccessModelAndViewRequestObjectExceptionStringMap()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Object object = createReferenceObject();
		final Errors errors = createMock("Errors", Errors.class);
		final StateService stateService = createMock("StateService",
				StateService.class);
		getOverseasFormController().setStateService(stateService);
		final FacesService facesService = createMock("FacesService",
				FacesService.class);
		getOverseasFormController().setFacesService(facesService);
		setUpBasicReferenceData(request, object, errors, stateService,
				facesService);
		setUpMinimalReferenceData(request, object, errors, stateService,
				facesService);
		final Object command = createCommand();
		final BindException bindException = createMock("BindException",
				BindException.class);
		final String modelName = "Model Name";
		final Map model = new HashMap();
		final String successContentBlock = "Success Content Block";
		getOverseasFormController().setSuccessContentBlock(successContentBlock);
		final String approvedFilename = "Approved Filename";
		EasyMock.expect(
				facesService.getApprovedFileName(successContentBlock,
						"Relative Prefix")).andReturn(approvedFilename)
				.atLeastOnce();
		replayAll();

		final ModelAndView actualModelAndView = getOverseasFormController()
				.buildSuccessModelAndView(request, command, bindException,
						modelName, model);

		assertNotNull("A model and view is returned", actualModelAndView);
		assertNull("There is no view", actualModelAndView.getView());
		assertNull("There is no view name", actualModelAndView.getViewName());
		final Map<String, Object> actualModel = actualModelAndView.getModel();
		assertNotNull("There is a model", actualModel);
		assertMinimalReferenceData(request, object, errors, stateService,
				facesService, actualModel);
		assertBasicReferenceData(request, object, errors, stateService,
				facesService, approvedFilename, actualModel);
		assertEquals("The command is set", command,
				actualModel.get(getOverseasFormController().getCommandName()));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#formBackingObject(HttpServletRequest)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem getting the form backing object.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testFormBackingObject() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();

		final Object actualFormBackingObject = getOverseasFormController()
				.formBackingObject(request);

		assertFormBackingObject(actualFormBackingObject);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getContentBlock()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetContentBlock() {
		final String actualContentBlock = getOverseasFormController()
				.getContentBlock();

		assertNull("No content block is set", actualContentBlock);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getFacesService()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetFacesService() {
		final FacesService actualFacesService = getOverseasFormController()
				.getFacesService();

		assertNull("No faces service is set", actualFacesService);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getPageTitle()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetPageTitle() {
		final String actualPageTitle = getOverseasFormController()
				.getPageTitle();

		assertNull("No page title is set", actualPageTitle);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getSectionCss()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetSectionCss() {
		final String actualSectionCss = getOverseasFormController()
				.getSectionCss();

		assertNull("No section CSS is set", actualSectionCss);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getSectionName()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetSectionName() {
		final String actualSectionName = getOverseasFormController()
				.getSectionName();

		assertNull("No section name is set", actualSectionName);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getStateService()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetStateService() {
		final StateService actualStateService = getOverseasFormController()
				.getStateService();

		assertNull("No state service is set", actualStateService);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getSuccessContentBlock()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetSuccessContentBlock() {
		final String actualSuccessContentBlock = getOverseasFormController()
				.getSuccessContentBlock();

		assertNull("No success content block is set", actualSuccessContentBlock);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#getUser()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testGetUser() {
		final OverseasUser actualUser = SecurityContextHelper.getUser();

		assertNull("No user is set", actualUser);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)}
	 * for an empty request.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testIsFormSubmission_emptyRequest() {
		final HttpServletRequest request = new MockHttpServletRequest();

		final boolean actualFormSubmission = getOverseasFormController()
				.isFormSubmission(request);

		assertFalse("The request is not a form submission",
				actualFormSubmission);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)}
	 * for a get request.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testIsFormSubmission_getRequest() {
		final String method = "get";
		final String requestURI = "uri://localhost/request";
		final HttpServletRequest request = new MockHttpServletRequest(method,
				requestURI);

		final boolean actualFormSubmission = getOverseasFormController()
				.isFormSubmission(request);

		assertFalse("The request is not a form submission",
				actualFormSubmission);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)}
	 * for a post request.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testIsFormSubmission_postRequest() {
		final String method = "post";
		final String requestURI = "uri://localhost/request";
		final HttpServletRequest request = new MockHttpServletRequest(method,
				requestURI);

		final boolean actualFormSubmission = getOverseasFormController()
				.isFormSubmission(request);

		final boolean expectedPostFormSubmission = getExpectedPostFormSubmission(false);
		assertEquals("The form submission state is correct",
				expectedPostFormSubmission, actualFormSubmission);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)}
	 * for a request that has a submission parameter.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testIsFormSubmission_submissionParameter() {
		final String method = "get";
		final String requestURI = "uri://localhost/request";
		final MockHttpServletRequest request = new MockHttpServletRequest(
				method, requestURI);
		request.addParameter("submission", "true");

		final boolean actualFormSubmission = getOverseasFormController()
				.isFormSubmission(request);

		final boolean expectedPostFormSubmission = getExpectedPostFormSubmission(true);
		assertEquals("The form submission state is correct",
				expectedPostFormSubmission, actualFormSubmission);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#isShowMetaKeywords()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testIsShowMetaKeywords() {
		final boolean actualShowMetaKeywords = getOverseasFormController()
				.isShowMetaKeywords();

		assertFalse("Meta keywords are not shown", actualShowMetaKeywords);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#referenceData(HttpServletRequest, Object, org.springframework.validation.Errors)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem with the reference data.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testReferenceData() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Object object = createReferenceObject();
		final Errors errors = createMock("Errors", Errors.class);
		final StateService stateService = createMock("StateService",
				StateService.class);
		getOverseasFormController().setStateService(stateService);
		final FacesService facesService = createMock("FacesService",
				FacesService.class);
		getOverseasFormController().setFacesService(facesService);
		setUpBasicReferenceData(request, object, errors, stateService,
				facesService);
		setUpMinimalReferenceData(request, object, errors, stateService,
				facesService);
		replayAll();

		final Map actualReferenceData = getOverseasFormController()
				.referenceData(request, object, errors);

		assertMinimalReferenceData(request, object, errors, stateService,
				facesService, actualReferenceData);
		assertBasicReferenceData(request, object, errors, stateService,
				facesService, null, actualReferenceData);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setContentBlock(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetContentBlock() {
		final String contentBlock = "Content Block";

		getOverseasFormController().setContentBlock(contentBlock);

		assertEquals("The content block is set", contentBlock,
				getOverseasFormController().getContentBlock());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setDeploymentEnv(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetDeploymentEnv() {
		final String deploymentEnv = "Deployment Env";

		getOverseasFormController().setDeploymentEnv(deploymentEnv);

		assertEquals("The deployment environment is set", deploymentEnv,
				getOverseasFormController().deploymentEnv);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setFacesService(com.bearcode.ovf.service.FacesService)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetFacesService() {
		final FacesService facesService = createMock("FacesService",
				FacesService.class);

		getOverseasFormController().setFacesService(facesService);

		assertSame("The faces service is set", facesService,
				getOverseasFormController().getFacesService());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setPageTitle(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetPageTitle() {
		final String pageTitle = "Page Title";

		getOverseasFormController().setPageTitle(pageTitle);

		assertEquals("The page title is set", pageTitle,
				getOverseasFormController().getPageTitle());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setSectionCss(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetSectionCss() {
		final String sectionCss = "Section CSS";

		getOverseasFormController().setSectionCss(sectionCss);

		assertEquals("The section CSS is set", sectionCss,
				getOverseasFormController().getSectionCss());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setSectionName(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetSectionName() {
		final String sectionName = "Section Name";

		getOverseasFormController().setSectionName(sectionName);

		assertEquals("The section name is set", sectionName,
				getOverseasFormController().getSectionName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setShowMetaKeywords(boolean)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetShowMetaKeywords() {
		getOverseasFormController().setShowMetaKeywords(true);

		assertTrue("Meta keywords are shown", getOverseasFormController()
				.isShowMetaKeywords());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setStateService(com.bearcode.ovf.service.StateService)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetStateService() {
		final StateService stateService = createMock("StateService",
				StateService.class);

		getOverseasFormController().setStateService(stateService);

		assertSame("The state service is set", stateService,
				getOverseasFormController().getStateService());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasFormController#setSuccessContentBlock(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	@Test
	public final void testSetSuccessContentBlock() {
		final String successContentBlock = "Success Content Block";

		getOverseasFormController().setSuccessContentBlock(successContentBlock);

		assertEquals("The success content block is set", successContentBlock,
				getOverseasFormController().getSuccessContentBlock());
	}

	/**
	 * Adds authentication to the security context.
	 * 
	 * @author IanBrown
	 * @return the authentication.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final Authentication addAuthenticationToSecurityContext() {
		final Authentication authentication = createMock("Authentication",
				Authentication.class);
		EasyMock.expect(getSecurityContext().getAuthentication())
				.andReturn(authentication).atLeastOnce();
		return authentication;
	}

	/**
	 * Adds a principal (overseas user) to the authentication.
	 * 
	 * @author IanBrown
	 * @param authentication
	 *            the authentication.
	 * @return the principal.
	 * @since Dec 16, 2011
	 * @version Dec 16, 2011
	 */
	protected final OverseasUser addPrincipalToAuthentication(
			final Authentication authentication) {
		final OverseasUser principal = createMock("Principal",
				OverseasUser.class);
		EasyMock.expect(authentication.getPrincipal()).andReturn(principal)
				.atLeastOnce();
		return principal;
	}

	/**
	 * Custom assertion to ensure that the form backing object is correct.
	 * 
	 * @author IanBrown
	 * @param actualFormBackingObject
	 *            the actual form backing object.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	protected abstract void assertFormBackingObject(
			final Object actualFormBackingObject);

	/**
	 * Custom assertion to ensure that the minimal reference data for the
	 * specific type of overseas form controller is correct.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param object
	 *            the reference object.
	 * @param errors
	 *            the errors.
	 * @param stateService
	 *            the state service.
	 * @param facesService
	 *            the faces service.
	 * @param actualReferenceData
	 *            the actual reference data.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@SuppressWarnings("rawtypes")
	protected abstract void assertMinimalReferenceData(
			MockHttpServletRequest request, Object object, Errors errors,
			StateService stateService, FacesService facesService,
			Map actualReferenceData);

	/**
	 * Creates a command appropriate to the controller.
	 * 
	 * @author IanBrown
	 * @return the command.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	protected abstract Object createCommand();

	/**
	 * Creates an overseas form controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the overseas form controller.
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	protected abstract C createOverseasFormController();

	/**
	 * Creates the object for reference data requests.
	 * 
	 * @author IanBrown
	 * @return the reference object.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	protected abstract Object createReferenceObject();

	/**
	 * Gets the expected value for the form submission result for a POST.
	 * 
	 * @author IanBrown
	 * @param submitted<code>true</code> if the form was submitted, <code>false</code> if it was not.
	 * @return <code>true</code> if the post is considered a form submission,
	 *         <code>false</code> if it is not.
	 * @since Dec 21, 2011
	 * @version Jul 25, 2012
	 */
	protected abstract boolean getExpectedPostFormSubmission(boolean submitted);

	/**
	 * Gets the overseas form controller.
	 * 
	 * @author IanBrown
	 * @return the overseas form controller.
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	protected final C getOverseasFormController() {
		return overseasFormController;
	}

	/**
	 * Gets the security context.
	 * 
	 * @author IanBrown
	 * @return the security context.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final SecurityContext getSecurityContext() {
		return securityContext;
	}

	/**
	 * Sets up the basic reference data.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param object
	 *            the reference object.
	 * @param errors
	 *            the errors.
	 * @param stateService
	 *            the state service.
	 * @param facesService
	 *            the faces service.
	 * @since Dec 19, 2011
	 * @version Dec 22, 2011
	 */
	protected final void setUpBasicReferenceData(
			final MockHttpServletRequest request, final Object object,
			final Errors errors, final StateService stateService,
			final FacesService facesService) {
		final Authentication authentication = addAuthenticationToSecurityContext();
		addPrincipalToAuthentication(authentication);
		final String contentBlock = "Content Block";
		getOverseasFormController().setContentBlock(contentBlock);
		getOverseasFormController().setPageTitle("Page Title");
		request.setParameter("css", "http://external.com/external.css");
		getOverseasFormController().setSectionCss("Section CSS");
		getOverseasFormController().setSectionName("Section Name");
		getOverseasFormController().setShowMetaKeywords(true);
		final State state = createMock("State", State.class);
		final Collection<State> allStates = Arrays.asList(state);
		EasyMock.expect(stateService.findAllStates()).andReturn(allStates)
				.atLeastOnce();
		request.setServerName("Server Name");
		request.setContextPath("Context Path");
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(
				facesService.findConfig(request.getServerName()
						+ request.getContextPath())).andReturn(faceConfig)
				.atLeastOnce();
		final String relativePath = "Relative Prefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePath)
				.atLeastOnce();
		EasyMock.expect(
				facesService.getApprovedFileName(contentBlock, relativePath))
				.andReturn("Approved Filename").atLeastOnce();
	}

	/**
	 * Sets up to test the specific type of overseas form controller.
	 * 
	 * @author IanBrown
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	protected abstract void setUpForOverseasFormController();

	/**
	 * Sets up the minimal reference data for the specific type of overseas form
	 * controller.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param object
	 *            the reference object.
	 * @param errors
	 *            the errors object.
	 * @param stateService
	 *            the state service.
	 * @param facesService
	 *            the faces service.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	protected abstract void setUpMinimalReferenceData(
			MockHttpServletRequest request, Object object, Errors errors,
			StateService stateService, FacesService facesService);

	/**
	 * Tears down the set up for testing the specific type of overseas form
	 * controller.
	 * 
	 * @author IanBrown
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	protected abstract void tearDownForOverseasFormController();

	/**
	 * Custom assertion to ensure that the basic reference data is correct.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param object
	 *            the object.
	 * @param errors
	 *            the errors.
	 * @param stateService
	 *            the state service.
	 * @param facesService
	 *            the faces service.
	 * @param expectedContent
	 *            expected content if it isn't the standard value.
	 * @param actualReferenceData
	 *            the actual reference data.
	 * @throws MalformedURLException
	 *             if the external CSS URL is incorrect.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@SuppressWarnings("rawtypes")
	private void assertBasicReferenceData(final MockHttpServletRequest request,
			final Object object, final Errors errors,
			final StateService stateService, final FacesService facesService,
			final String expectedContent, final Map actualReferenceData)
			throws MalformedURLException {
		assertSame("The user is correct", getSecurityContext()
				.getAuthentication().getPrincipal(),
				actualReferenceData.get("userDetails"));
		assertEquals("The title is correct", getOverseasFormController()
				.getPageTitle(),
				actualReferenceData.get(OverseasFormController.TITLE));
		assertEquals("The external CSS is correct",
				(new URL(request.getParameter("css"))).toString(),
				actualReferenceData.get(OverseasFormController.EXTERNAL_CSS));
		assertEquals("The section CSS is correct", getOverseasFormController()
				.getSectionCss(),
				actualReferenceData.get(OverseasFormController.SECTION_CSS));
		assertEquals("The section name is correct", getOverseasFormController()
				.getSectionName(),
				actualReferenceData.get(OverseasFormController.SECTION_NAME));
		assertEquals("The show meta keywords flag is correct",
				getOverseasFormController().isShowMetaKeywords(),
				actualReferenceData
						.get(OverseasFormController.SHOW_META_KEYWORDS));
		assertSame("The states are correct", stateService.findAllStates(),
				actualReferenceData.get("states"));
		final String expectedRelativePrefix = facesService.findConfig(
				request.getServerName() + request.getContextPath())
				.getRelativePrefix();
		assertEquals("The relative prefix is correct", expectedRelativePrefix,
				actualReferenceData
						.get(OverseasFormController.FACE_RELATIVE_PATH));
		assertEquals(
				"The content is correct",
				(expectedContent == null) ? facesService.getApprovedFileName(
						getOverseasFormController().getContentBlock(),
						expectedRelativePrefix) : expectedContent,
				actualReferenceData.get(OverseasFormController.CONTENT));
	}

	/**
	 * Gets the original security context.
	 * 
	 * @author IanBrown
	 * @return the original security context.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private final SecurityContext getOriginalSecurityContext() {
		return originalSecurityContext;
	}

	/**
	 * Sets the original security context.
	 * 
	 * @author IanBrown
	 * @param originalSecurityContext
	 *            the original security context.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private final void setOriginalSecurityContext(
			final SecurityContext originalSecurityContext) {
		this.originalSecurityContext = originalSecurityContext;
	}

	/**
	 * Sets the overseas form controller.
	 * 
	 * @author IanBrown
	 * @param overseasFormController
	 *            the overseas form controller to set.
	 * @since Dec 9, 2011
	 * @version Dec 9, 2011
	 */
	private void setOverseasFormController(final C overseasFormController) {
		this.overseasFormController = overseasFormController;
	}

	/**
	 * Sets the securityContext.
	 * 
	 * @author IanBrown
	 * @param securityContext
	 *            the securityContext to set.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private final void setSecurityContext(final SecurityContext securityContext) {
		this.securityContext = securityContext;
	}
}
