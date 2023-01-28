/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.model.common.Country;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.FaceBookApiService;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;
import org.apache.commons.httpclient.HttpClient;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Abstract test for implementations of {@link BaseController}.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of overseas form controller (Spring3) to test.
 * @since Dec 19, 2011
 * @version Aug 2, 2012
 */
public abstract class BaseControllerCheck<C extends BaseController> extends EasyMockSupport {

	/**
	 * the base controller to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jan 3, 2012
	 */
	private C baseController;

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private StateService stateService;

	/**
	 * the faces service.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private FacesService facesService;

	/**
	 * the facebook API service.
	 * 
	 * @author IanBrown
	 * @since Feb 8, 2012
	 * @version Feb 8, 2012
	 */
	private FaceBookApiService faceBookApiService;

	/**
	 * the original security context.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private SecurityContext originalSecurityContext;

	/**
	 * the security context.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private SecurityContext securityContext;

	/**
	 * Gets the facebook API service.
	 * 
	 * @author IanBrown
	 * @return the facebook API service.
	 * @since Mar 30, 2012
	 * @version Mar 30, 2012
	 */
	public final FaceBookApiService getFaceBookApiService() {
		return faceBookApiService;
	}

	/**
	 * Sets the facebook API service.
	 * 
	 * @author IanBrown
	 * @param faceBookApiService
	 *            the facebook API service.
	 * @since Mar 30, 2012
	 * @version Mar 30, 2012
	 */
	public final void setFaceBookApiService(final FaceBookApiService faceBookApiService) {
		this.faceBookApiService = faceBookApiService;
	}

	/**
	 * Sets up to test the base controller.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jan 3, 2012
	 */
	@Before
	public final void setUpBaseController() {
		setSecurityContext(createMock("SecurityContext", SecurityContext.class));
		setStateService(createMock("StateService", StateService.class));
		setFacesService(createMock("FacesService", FacesService.class));
		setFaceBookApiService(createNiceMock("FaceBookApiService", FaceBookApiService.class));
		setUpForBaseController();
		setBaseController(createBaseController());
		setOriginalSecurityContext(SecurityContextHolder.getContext());
		SecurityContextHolder.setContext(getSecurityContext());
		ReflectionTestUtils.setField(getBaseController(), "stateService", getStateService());
		ReflectionTestUtils.setField(getBaseController(), "facesService", getFacesService());
		ReflectionTestUtils.setField(getBaseController(), "faceBookApiService", getFaceBookApiService());
	}

	/**
	 * Tears down the base controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jan 3, 2012
	 */
	@After
	public final void tearDownBaseController() {
		setBaseController(null);
		tearDownForBaseController();
		setFacesService(null);
		setStateService(null);
		SecurityContextHolder.setContext(getOriginalSecurityContext());
		setSecurityContext(null);
		setOriginalSecurityContext(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildFullUrl(javax.servlet.http.HttpServletRequest, java.lang.String)}
	 * for a URI that does not start with a slash.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testBuildFullUrl_noSlash() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setLocalPort(80);
		final String serverName = "serverName";
		request.setServerName(serverName);
		final String contextPath = "/contextPath";
		request.setContextPath(contextPath);
		final String uri = "uri";
		replayAll();

		final String actualFullUrl = getBaseController().buildFullUrl(request, uri);

		assertEquals("The full URI is correct", "http://" + serverName + contextPath + "/" + uri, actualFullUrl);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildFullUrl(javax.servlet.http.HttpServletRequest, java.lang.String)}
	 * for port other than 80.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testBuildFullUrl_notPort80() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setLocalPort(81);
		final String serverName = "serverName";
		request.setServerName(serverName);
		final String contextPath = "/contextPath";
		request.setContextPath(contextPath);
		final String uri = "/uri";
		replayAll();

		final String actualFullUrl = getBaseController().buildFullUrl(request, uri);

		assertEquals("The full URI is correct", "https://" + serverName + contextPath + uri, actualFullUrl);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildFullUrl(javax.servlet.http.HttpServletRequest, java.lang.String)}
	 * for port 80.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testBuildFullUrl_port80() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setLocalPort(80);
		final String serverName = "serverName";
		request.setServerName(serverName);
		final String contextPath = "/contextPath";
		request.setContextPath(contextPath);
		final String uri = "/uri";
		replayAll();

		final String actualFullUrl = getBaseController().buildFullUrl(request, uri);

		assertEquals("The full URI is correct", "http://" + serverName + contextPath + uri, actualFullUrl);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildModelAndView(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where an external CSS is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testBuildModelAndView_externalCss() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final String externalCss = "External CSS";
		request.getSession().setAttribute("externalCss", externalCss);
		final ModelMap model = createModelMap(null, request, externalCss, true, false);
		replayAll();

		final String actualModelAndView = getBaseController().buildModelAndView(request, model);

		assertEquals("The model and view is correct", getBaseController().mainTemplate, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildModelAndView(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where no user is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testBuildModelAndView_noUser() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		replayAll();

		final String actualModelAndView = getBaseController().buildModelAndView(request, model);

		assertEquals("The model and view is correct", getBaseController().mainTemplate, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildModelAndView(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where an overseas user is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testBuildModelAndView_user() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		replayAll();

		final String actualModelAndView = getBaseController().buildModelAndView(request, model);

		assertEquals("The model and view is correct", getBaseController().mainTemplate, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildSuccessModelAndView(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is an external CSS.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testBuildSuccessModelAndView_externalCss() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final String externalCss = "External CSS";
		request.getSession().setAttribute("externalCss", externalCss);
		final ModelMap model = createModelMap(null, request, externalCss, true, true);
		replayAll();

		final String actualSuccessModelAndView = getBaseController().buildSuccessModelAndView(request, model);

		assertEquals("The success model and view is correct", getBaseController().mainTemplate, actualSuccessModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildSuccessModelAndView(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testBuildSuccessModelAndView_noUser() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, true);
		replayAll();

		final String actualSuccessModelAndView = getBaseController().buildSuccessModelAndView(request, model);

		assertEquals("The success model and view is correct", getBaseController().mainTemplate, actualSuccessModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#buildSuccessModelAndView(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testBuildSuccessModelAndView_user() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, true);
		replayAll();

		final String actualSuccessModelAndView = getBaseController().buildSuccessModelAndView(request, model);

		assertEquals("The success model and view is correct", getBaseController().mainTemplate, actualSuccessModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getContentBlock()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testGetContentBlock() {
		final String actualContentBlock = getBaseController().getContentBlock();

		final String expectedContentBlock = getExpectedContentBlock();
		assertEquals("The content block is the default", expectedContentBlock, actualContentBlock);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getFaceConfig(javax.servlet.http.HttpServletRequest)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testGetFaceConfig() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String serverName = "ServerName";
		request.setServerName(serverName);
		final String contextPath = "/contextPath";
		request.setContextPath(contextPath);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig(serverName + contextPath)).andReturn(faceConfig);
		replayAll();

		final FaceConfig actualFaceConfig = getBaseController().getFaceConfig(request);

		assertSame("The face configuration is the one provided to the faces service", faceConfig, actualFaceConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getFacesService()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testGetFacesService() {
		final FacesService actualFacesService = getBaseController().getFacesService();

		assertSame("The faces service is set", getFacesService(), actualFacesService);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getHttpClient()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testGetHttpClient() {
		final HttpClient actualHttpClient = getBaseController().getHttpClient();

		assertNotNull("An HTTP client is returned", actualHttpClient);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getPageTitle()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testGetPageTitle() {
		final String actualPageTitle = getBaseController().getPageTitle();

		final String expectedPageTitle = getExpectedPageTitle();
		assertEquals("The page title is the default", expectedPageTitle, actualPageTitle);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getSectionCss()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetSectionCss() {
		final String actualSectionCss = getBaseController().getSectionCss();

		final String expectedSectionCss = getExpectedSectionCss();
		assertEquals("The section CSS is the default", expectedSectionCss, actualSectionCss);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getSectionName()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetSectionName() {
		final String actualSectionName = getBaseController().getSectionName();

		final String expectedSectionName = getExpectedSectionName();
		assertEquals("The section name is the default", expectedSectionName, actualSectionName);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getStateService()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetStateService() {
		final StateService actualStateService = getBaseController().getStateService();

		assertSame("The state service is set", getStateService(), actualStateService);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getSuccessContentBlock()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetSuccessContentBlock() {
		final String actualSuccessContentBlock = getBaseController().getSuccessContentBlock();

		final String expectedSuccessContentBlock = getExpectedSuccessContentBlock();
		assertEquals("The success content block is the default", expectedSuccessContentBlock, actualSuccessContentBlock);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getUser()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetUser() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = addOverseasUserToAuthentication(authentication);
		replayAll();

		final OverseasUser actualUser = getBaseController().getUser();

		assertSame("The overseas user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getUser()} for the case where there is no
	 * authentication.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetUser_noAuthentication() {
		EasyMock.expect(getSecurityContext().getAuthentication()).andReturn(null);
		replayAll();

		final OverseasUser actualUser = getBaseController().getUser();

		assertNull("There is no user", actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getUser()} for the case where there the user is not an
	 * overseas user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetUser_notOverseasUser() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final Object user = createMock("User", Object.class);
		EasyMock.expect(authentication.getPrincipal()).andReturn(user);
		replayAll();

		final OverseasUser actualUser = getBaseController().getUser();

		assertNull("There is no user", actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#getUser()} for the case where there is no user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetUser_noUser() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		EasyMock.expect(authentication.getPrincipal()).andReturn(null);
		replayAll();

		final OverseasUser actualUser = getBaseController().getUser();

		assertNull("There is no user", actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#isShowMetaKeywords()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testIsShowMetaKeywords() {
		final boolean actualShowMetaKeywords = getBaseController().isShowMetaKeywords();

		assertFalse("Meta keywords are not shown", actualShowMetaKeywords);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#prepareModel(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is an external CSS.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testPrepareModel_externalCss() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final String externalCss = "External CSS";
		request.getSession().setAttribute("externalCss", externalCss);
		final ModelMap model = createModelMap(null, request, externalCss, false, false);
		replayAll();

		getBaseController().prepareModel(request, model);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#prepareModel(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testPrepareModel_noUser() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, false, false);
		replayAll();

		getBaseController().prepareModel(request, model);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.BaseController#prepareModel(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testPrepareModel_user() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, false, false);
		replayAll();

		getBaseController().prepareModel(request, model);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#setContentBlock(java.lang.String)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetContentBlock() {
		final String contentBlock = "Content Block";

		getBaseController().setContentBlock(contentBlock);

		assertEquals("The content block is set", contentBlock, getBaseController().getContentBlock());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#setDeploymentEnv(java.lang.String)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetDeploymentEnv() {
		final String deploymentEnv = "Deployment Env";

		getBaseController().setDeploymentEnv(deploymentEnv);

		assertEquals("The deployment environment is set", deploymentEnv,
				ReflectionTestUtils.getField(getBaseController(), "deploymentEnv"));
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#setPageTitle(java.lang.String)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetPageTitle() {
		final String pageTitle = "Page Title";

		getBaseController().setPageTitle(pageTitle);

		assertEquals("The page title is set", pageTitle, getBaseController().getPageTitle());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#setSectionCss(java.lang.String)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetSectionCss() {
		final String sectionCss = "Section CSS";

		getBaseController().setSectionCss(sectionCss);

		assertEquals("The section CSS is set", sectionCss, getBaseController().getSectionCss());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#setSectionName(java.lang.String)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetSectionName() {
		final String sectionName = "Section Name";

		getBaseController().setSectionName(sectionName);

		assertEquals("The section name is set", sectionName, getBaseController().getSectionName());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#setShowMetaKeywords(boolean)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetShowMetaKeywords() {
		getBaseController().setShowMetaKeywords(true);

		assertTrue("Meta keywords are shown", getBaseController().isShowMetaKeywords());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.commons.BaseController#setSuccessContentBlock(java.lang.String)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetSuccessContentBlock() {
		final String successContentBlock = "Success Content Block";

		getBaseController().setSuccessContentBlock(successContentBlock);

		assertEquals("The success content block is set", successContentBlock, getBaseController().getSuccessContentBlock());
	}

	/**
	 * Adds an attribute to the model map.
	 * 
	 * @author IanBrown
	 * @param modelMap
	 *            the model map.
	 * @param attributeName
	 *            the name of the attribute.
	 * @param attributeValue
	 *            the value of the attribute.
	 * @since Dec 20, 2011
	 * @version Jan 3, 2012
	 */
	protected final void addAttributeToModelMap(final ModelMap modelMap, final String attributeName, final Object attributeValue) {
		EasyMock.expect(modelMap.addAttribute(attributeName, attributeValue)).andReturn(modelMap).anyTimes();
	}

	/**
	 * Adds authentication to the security context.
	 * 
	 * @author IanBrown
	 * @return the authentication.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected final Authentication addAuthenticationToSecurityContext() {
		final Authentication authentication = createMock("Authentication", Authentication.class);
		EasyMock.expect(getSecurityContext().getAuthentication()).andReturn(authentication).anyTimes();
		return authentication;
	}

	/**
	 * Adds the overseas user to the authentication.
	 * 
	 * @author IanBrown
	 * @param authentication
	 *            the authentication.
	 * @param user
	 *            the overseas user.
	 * @return the overseas user.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected final OverseasUser addOverseasUserToAuthentication(final Authentication authentication, final OverseasUser user) {
		EasyMock.expect(authentication.getPrincipal()).andReturn(user).anyTimes();
		return user;
	}

	/**
	 * Creates a base controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the base controller.
	 * @since Dec 19, 2011
	 * @version Jan 3, 2012
	 */
	protected abstract C createBaseController();

	/**
	 * Creates a model map with a face configuration.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @param request
	 *            the request.
	 * @param faceConfig
	 *            the face configuration.
	 * @param externalCss
	 *            the external CSS.
	 * @param expectContent
	 *            <code>true</code> if content is expected, <code>false</code> if it is not.
	 * @param expectSuccessContentBlock
	 *            <code>true</code> if the success content block is expected, <code>false</code> if the regular content block is
	 *            expected. Only checked if <code>expectContent</code> is set.
	 * @return the model map.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected final ModelMap createModelMap(final OverseasUser user, final HttpServletRequest request, final FaceConfig faceConfig,
			final String externalCss, final boolean expectContent, final boolean expectSuccessContentBlock) {
		final ModelMap modelMap = createNiceMock("ModelMap", ModelMap.class);
		final String relativePrefix = "RelativePrefix/";
		if (!expectContent) {
			EasyMock.expect(modelMap.get("content")).andReturn(null).anyTimes();
		} else {
			final String expectedContentBlock;
			if (!expectSuccessContentBlock) {
				expectedContentBlock = getExpectedContentBlock();
			} else {
				final String expectedSuccessContentBlock = getExpectedSuccessContentBlock();
				expectedContentBlock = expectedSuccessContentBlock == null ? getExpectedContentBlock()
						: expectedSuccessContentBlock;
			}

			if (expectedContentBlock == null) {
				EasyMock.expect(modelMap.get("content")).andReturn(null).anyTimes();

			} else {
				addAttributeToModelMap(modelMap, "content", expectedContentBlock, 0, 1);
				final String approvedFileName = "approved file name";
				EasyMock.expect(getFacesService().getApprovedFileName(expectedContentBlock, relativePrefix))
						.andReturn(approvedFileName).anyTimes();
				addAttributeToModelMap(modelMap, "content", approvedFileName);
			}
		}
		addAttributeToModelMap(modelMap, "userDetails", user);
		addAttributeToModelMap(modelMap, "externalCss", externalCss);
		final String expectedPageTitle = getExpectedPageTitle();
		if (expectedPageTitle != null) {
			addAttributeToModelMap(modelMap, "title", expectedPageTitle);
		}
		addAttributeToModelMap(modelMap, "sectionCss", getExpectedSectionCss());
		addAttributeToModelMap(modelMap, "sectionName", getExpectedSectionName());
		addAttributeToModelMap(modelMap, "showMetaKeywords", false);
		final Collection<State> states = new ArrayList<State>();
		EasyMock.expect(getStateService().findAllStates()).andReturn(states).anyTimes();
		addAttributeToModelMap(modelMap, "states", states);
		final Collection<Country> countries = new ArrayList<Country>();
		EasyMock.expect(getStateService().findAllCountries()).andReturn(countries).anyTimes();
		addAttributeToModelMap(modelMap, "countries", countries);
		EasyMock.expect(getFacesService().findConfig(request.getServerName() + request.getContextPath())).andReturn(faceConfig)
				.anyTimes();
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		EasyMock.expect(faceConfig.getPresetPdfAnswersFields()).andReturn(new HashMap<String, String>()).anyTimes();
		addAttributeToModelMap(modelMap, "relativePath", relativePrefix);
		EasyMock.expect(faceConfig.getUrlPath()).andReturn("face/vote").anyTimes();
		getBaseController();
		addAttributeToModelMap(modelMap, "facebookAppId", "bogus");
		addAttributeToModelMap(modelMap, "yearNumber", BaseController.YEAR_FORMAT.format(new Date()));
		return modelMap;
	}

	/**
	 * Creates a model map.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @param request
	 *            the request.
	 * @param externalCss
	 *            the external CSS.
	 * @param expectContent
	 *            <code>true</code> if content is expected, <code>false</code> if it is not.
	 * @param expectSuccessContentBlock
	 *            <code>true</code> if the success content block is expected, <code>false</code> if the regular content block is
	 *            expected. Only checked if <code>expectContent</code> is set.
	 * @return the model map.
	 * @since Dec 20, 2011
	 * @version Aug 2, 2012
	 */
	protected final ModelMap createModelMap(final OverseasUser user, final HttpServletRequest request, final String externalCss,
			final boolean expectContent, final boolean expectSuccessContentBlock) {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getUserValidationSkipFields()).andReturn("").anyTimes();
        EasyMock.expect(faceConfig.isUseCaptcha()).andReturn(true).anyTimes();
		EasyMock.expect( faceConfig.getName() ).andReturn( "faces/ovf" ).anyTimes();
		return createModelMap(user, request, faceConfig, externalCss, expectContent, expectSuccessContentBlock);
	}

	/**
	 * Gets the base controller.
	 * 
	 * @author IanBrown
	 * @return the base controller.
	 * @since Dec 19, 2011
	 * @version Jan 3, 2012
	 */
	protected final C getBaseController() {
		return baseController;
	}

	/**
	 * Gets the expected content block for the specific type of overseas form controller.
	 * 
	 * @author IanBrown
	 * @return the expected content block.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected abstract String getExpectedContentBlock();

	/**
	 * Gets the expected page title for the specific type of overseas form controller.
	 * 
	 * @author IanBrown
	 * @return the expected page title.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected abstract String getExpectedPageTitle();

	/**
	 * Gets the expected section CSS for the specific type of overseas form controller.
	 * 
	 * @author IanBrown
	 * @return the expected section CSS.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected abstract String getExpectedSectionCss();

	/**
	 * Gets the expected section name for the specific type of overseas form controller.
	 * 
	 * @author IanBrown
	 * @return the expected section name.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected abstract String getExpectedSectionName();

	/**
	 * Gets the expected success content block for the specific type of overseas form controller.
	 * 
	 * @author IanBrown
	 * @return the expected success content block.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected abstract String getExpectedSuccessContentBlock();

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	protected final FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the security context.
	 * 
	 * @author IanBrown
	 * @return the security context.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected final SecurityContext getSecurityContext() {
		return securityContext;
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	protected final StateService getStateService() {
		return stateService;
	}

	/**
	 * Sets up to test the specific type of base controller.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jan 3, 2012
	 */
	protected abstract void setUpForBaseController();

	/**
	 * Tears down the set up for testing the specific type of base controller.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jan 3, 2013
	 */
	protected abstract void tearDownForBaseController();

	/**
	 * Adds an attribute to the model map and handles the specified number of reads of that value.
	 * 
	 * @author IanBrown
	 * @param modelMap
	 *            the model map.
	 * @param attributeName
	 *            the name of the attribute.
	 * @param attributeValue
	 *            the value of the attribute.
	 * @param minTimes
	 *            the minimum times that the attribute will be read.
	 * @param maxTimes
	 *            the maximum times that the attribute will be read.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void addAttributeToModelMap(final ModelMap modelMap, final String attributeName, final Object attributeValue,
			final int minTimes, final int maxTimes) {
		addAttributeToModelMap(modelMap, attributeName, attributeValue);
		EasyMock.expect(modelMap.get(attributeName)).andReturn(attributeValue).times(minTimes, maxTimes);
	}

	/**
	 * Adds an overseas user to the authentication.
	 * 
	 * @author IanBrown
	 * @param authentication
	 *            the authentication.
	 * @return the overseas user.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private OverseasUser addOverseasUserToAuthentication(final Authentication authentication) {
		final OverseasUser user = createMock("User", OverseasUser.class);
		return addOverseasUserToAuthentication(authentication, user);
	}

	/**
	 * Gets the original security context.
	 * 
	 * @author IanBrown
	 * @return the original security context.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private SecurityContext getOriginalSecurityContext() {
		return originalSecurityContext;
	}

	/**
	 * Sets the base controller.
	 * 
	 * @author IanBrown
	 * @param baseController
	 *            the base controller to set.
	 * @since Dec 19, 2011
	 * @version Jan 3, 2012
	 */
	private void setBaseController(final C baseController) {
		this.baseController = baseController;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the original security context.
	 * 
	 * @author IanBrown
	 * @param originalSecurityContext
	 *            the original security context to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setOriginalSecurityContext(final SecurityContext originalSecurityContext) {
		this.originalSecurityContext = originalSecurityContext;
	}

	/**
	 * Sets the security context.
	 * 
	 * @author IanBrown
	 * @param securityContext
	 *            the security context to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setSecurityContext(final SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}

}
