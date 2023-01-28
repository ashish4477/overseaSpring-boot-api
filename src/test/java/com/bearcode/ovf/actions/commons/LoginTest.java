/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.model.common.OverseasUser;

/**
 * Extended {@link BaseControllerCheck} test for {@link Login}.
 * 
 * @author IanBrown
 * 
 * @since Dec 20, 2011
 * @version Jan 3, 2012
 */
public final class LoginTest extends BaseControllerCheck<Login> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Login#showPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is an external CSS.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testShowPage_externalCSS() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final String externalCss = "External CSS";
		request.getSession().setAttribute("externalCss", externalCss);
		final ModelMap model = createModelMap(null, request, externalCss, true, false);
		addAttributeToModelMap(model, "j_username", null);
		replayAll();

		final String actualShowPage = getBaseController().showPage(request, model);

		assertEquals("The page is correct", getBaseController().mainTemplate, actualShowPage);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Login#showPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testShowPage_noUser() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		addAttributeToModelMap(model, "j_username", null);
		replayAll();

		final String actualShowPage = getBaseController().showPage(request, model);

		assertEquals("The page is correct", getBaseController().mainTemplate, actualShowPage);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Login#showPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testShowPage_user() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "j_username", null);
		replayAll();

		final String actualShowPage = getBaseController().showPage(request, model);

		assertEquals("The page is correct", getBaseController().mainTemplate, actualShowPage);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final Login createBaseController() {
		final Login login = new Login();
		return login;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/Login.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "My Voter Account Login";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return "/css/login.css";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return "login";
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
	}

}
