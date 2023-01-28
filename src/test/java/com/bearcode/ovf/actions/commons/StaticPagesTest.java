/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.model.common.OverseasUser;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import static org.junit.Assert.assertEquals;

/**
 * Extended {@link BaseControllerCheck} test for {@link StaticPages}.
 * 
 * @author IanBrown
 * 
 * @since Jun 1, 2012
 * @version Jun 1, 2012
 */
public final class StaticPagesTest extends BaseControllerCheck<StaticPages> {

	/**
	 * the page title for the about MVA page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ABOUT_MVA_PAGE_TITLE = "About My Voter Account";

	/**
	 * the section CSS for the about MVA page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ABOUT_MVA_SECTION_CSS = "/css/home.css";

	/**
	 * the section name for the about MVA page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ABOUT_MVA_SECTION_NAME = "home";

	/**
	 * the content block for the about MVA page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ABOUT_MVA_CONTENT_BLOCK = "/WEB-INF/pages/blocks/StaticAboutMVA.jsp";

	/**
	 * the content block for the errors page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ERRORS_CONTENT_BLOCK = "/WEB-INF/pages/blocks/Errors.jsp";

	/**
	 * the page title for the errors page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ERRORS_PAGE_TITLE = "Error";

	/**
	 * the section CSS for the errors page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ERRORS_SECTION_CSS = "";

	/**
	 * the section name for the errors page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String ERRORS_SECTION_NAME = "";

	/**
	 * the content block for the FWAB start page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String FWAB_START_CONTENT_BLOCK = "/WEB-INF/pages/blocks/FwabStart.jsp";

	/**
	 * the page title for the FWAB start page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String FWAB_START_PAGE_TITLE = "Federal Write-in Absentee Ballot (FWAB)";

	/**
	 * the section CSS for the FWAB start page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String FWAB_START_SECTION_CSS = "/css/rava.css";

	/**
	 * the section name for the FWAB start page.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String FWAB_START_SECTION_NAME = "rava";

	/**
	 * the content block for the wizard login page.
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String WIZARD_LOGIN_CONTENT_BLOCK = "/WEB-INF/pages/blocks/RavaLogin.jsp";

	/**
	 * the page title for the wizard login page.
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String WIZARD_LOGIN_PAGE_TITLE = "Login";

	/**
	 * the section CSS for the wizard login page.
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String WIZARD_LOGIN_SECTION_CSS = "/css/rava.css";

	/**
	 * the section name for the wizard login page.
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private static final String WIZARD_LOGIN_SECTION_NAME = "rava";

	/**
	 * the expected content block.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private String expectedContentBlock;

	/**
	 * the expected page title.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private String expectedPageTitle;

	/**
	 * the expected section CSS.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private String expectedSectionCss;

	/**
	 * the expected section name.
	 * 
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private String expectedSectionName;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.StaticPages#aboutMvaPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	@Test
	public final void testAboutMvaPage() {
		setExpectedContentBlock(ABOUT_MVA_CONTENT_BLOCK);
		setExpectedPageTitle(ABOUT_MVA_PAGE_TITLE);
		setExpectedSectionCss(ABOUT_MVA_SECTION_CSS);
		setExpectedSectionName(ABOUT_MVA_SECTION_NAME);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		replayAll();

		final String actualResponse = getBaseController().aboutMvaPage(request, model);

		assertResponse(actualResponse);
		assertPage();
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.StaticPages#errorsPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	@Test
	public final void testErrorsPage() {
		setExpectedContentBlock(ERRORS_CONTENT_BLOCK);
		setExpectedPageTitle(ERRORS_PAGE_TITLE);
		setExpectedSectionCss(ERRORS_SECTION_CSS);
		setExpectedSectionName(ERRORS_SECTION_NAME);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		replayAll();

		final String actualResponse = getBaseController().errorsPage(request, model, "500");

		assertResponse(actualResponse);
		assertPage();
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.StaticPages#fwabStartPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	@Test
	public final void testFwabStartPage() {
		setExpectedContentBlock(FWAB_START_CONTENT_BLOCK);
		setExpectedPageTitle(FWAB_START_PAGE_TITLE);
		setExpectedSectionCss(FWAB_START_SECTION_CSS);
		setExpectedSectionName(FWAB_START_SECTION_NAME);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "queryString", null);
		addAttributeToModelMap(model, "showMetaKeywords", true);
		replayAll();

		final String actualResponse = getBaseController().fwabStartPage(request, model);

		assertResponse(actualResponse);
		assertPage();
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.StaticPages#monitor(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	@Test
	public final void testMonitor() {
		setExpectedPageTitle("Monitor");
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "queryString", null);
		addAttributeToModelMap(model, "showMetaKeywords", true);
		replayAll();

		final String actualResponse = getBaseController().monitor(request, model);
		
		assertEquals("The response is the monitor template", "templates/MonitorPage", actualResponse);
		assertPage();
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.StaticPages#wizardLoginPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	@Test
	public final void testWizardLoginPage() {
		setExpectedContentBlock(WIZARD_LOGIN_CONTENT_BLOCK);
		setExpectedPageTitle(WIZARD_LOGIN_PAGE_TITLE);
		setExpectedSectionCss(WIZARD_LOGIN_SECTION_CSS);
		setExpectedSectionName(WIZARD_LOGIN_SECTION_NAME);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		replayAll();

		final String actualResponse = getBaseController().wizardLoginPage(request, model);

		assertResponse(actualResponse);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final StaticPages createBaseController() {
		return new StaticPages();
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return expectedContentBlock;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return expectedPageTitle;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return expectedSectionCss;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return expectedSectionName;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setExpectedContentBlock(null);
		setExpectedPageTitle(null);
		setExpectedSectionCss(null);
		setExpectedSectionName(null);
	}

	/**
	 * Custom assertion to ensure that the response is what is expected.
	 * 
	 * @author IanBrown
	 * @param actualResponse
	 *            the actual response.
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private void assertResponse(final String actualResponse) {
		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
	}

	/**
	 * Custom assertion to check the page values.
	 * @author IanBrown
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private void assertPage() {
		assertEquals("The content block has been set", getExpectedContentBlock(), getBaseController().getContentBlock());
		assertEquals("The section name has been set", getExpectedSectionName(), getBaseController().getSectionName());
		assertEquals("The section CSS has been set", getExpectedSectionCss(), getBaseController().getSectionCss());
		assertEquals("The page title has been set", getExpectedPageTitle(), getBaseController().getPageTitle());
	}

	/**
	 * Sets the expected content block.
	 * 
	 * @author IanBrown
	 * @param expectedContentBlock
	 *            the expected content block to set.
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private void setExpectedContentBlock(final String expectedContentBlock) {
		this.expectedContentBlock = expectedContentBlock;
	}

	/**
	 * Sets the expected page title.
	 * 
	 * @author IanBrown
	 * @param expectedPageTitle
	 *            the expected page title to set.
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private void setExpectedPageTitle(final String expectedPageTitle) {
		this.expectedPageTitle = expectedPageTitle;
	}

	/**
	 * Sets the expected section CSS.
	 * 
	 * @author IanBrown
	 * @param expectedSectionCss
	 *            the expected section CSS to set.
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private void setExpectedSectionCss(final String expectedSectionCss) {
		this.expectedSectionCss = expectedSectionCss;
	}

	/**
	 * Sets the expected section name.
	 * 
	 * @author IanBrown
	 * @param expectedSectionName
	 *            the expected section name to set.
	 * @since Jun 1, 2012
	 * @version Jun 1, 2012
	 */
	private void setExpectedSectionName(final String expectedSectionName) {
		this.expectedSectionName = expectedSectionName;
	}
}
