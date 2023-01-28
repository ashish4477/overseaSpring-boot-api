/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test for {@link LoginSuccessHandler}.
 * 
 * @author IanBrown
 * 
 * @since Dec 20, 2011
 * @version Dec 20, 2011
 */
public final class LoginSuccessHandlerTest extends EasyMockSupport {

	/**
	 * the login success handler.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private LoginSuccessHandler loginSuccessHandler;

	/**
	 * the redirect strategy.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private RedirectStrategy redirectStrategy;

	/**
	 * Sets up to test the login success handler.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Before
	public final void setUpLoginSuccessHandler() {
		setRedirectStrategy(createMock("RedirectStrategy",
				RedirectStrategy.class));
		setLoginSuccessHandler(createLoginSuccessHandler());
		getLoginSuccessHandler().setRedirectStrategy(getRedirectStrategy());
	}

	/**
	 * Tears down the login success handler after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@After
	public final void tearDownLoginSuccessHandler() {
		setLoginSuccessHandler(null);
		setRedirectStrategy(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.LoginSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)}
	 * for the case where there the login came from an excluded page.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem performing the redirect.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testOnAuthenticationSuccess_excludedPage()
			throws IOException, ServletException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String loginCameFrom = "Login Came From";
		request.getSession().setAttribute(LoginSuccessHandler.LOGIN_CAME_FROM,
				loginCameFrom);
		getRedirectStrategy().sendRedirect(request, response, "/");
		getLoginSuccessHandler().setExcludePagePatterns(
				new String[] { loginCameFrom });
		replayAll();

		getLoginSuccessHandler().onAuthenticationSuccess(request, response,
				null);

		assertNull("The login came from attribute is cleared", request
				.getSession().getAttribute(LoginSuccessHandler.LOGIN_CAME_FROM));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.LoginSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)}
	 * for the case where there is no login came from set.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem performing the redirect.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testOnAuthenticationSuccess_noLoginCameFrom()
			throws IOException, ServletException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		getRedirectStrategy().sendRedirect(request, response, "/");
		replayAll();

		getLoginSuccessHandler().onAuthenticationSuccess(request, response,
				null);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.LoginSuccessHandler#setExcludePagePatterns(java.lang.String[])}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testSetExcludePagePatterns() {
		final String[] excludePagePatterns = { "Exclude Page" };

		getLoginSuccessHandler().setExcludePagePatterns(excludePagePatterns);

		assertSame("The exclude page patterns are set", excludePagePatterns,
				ReflectionTestUtils.getField(getLoginSuccessHandler(),
						"excludePagePatterns"));
	}

	/**
	 * Creates a login success handler.
	 * 
	 * @author IanBrown
	 * @return the login success handler.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private LoginSuccessHandler createLoginSuccessHandler() {
		return new LoginSuccessHandler();
	}

	/**
	 * Gets the login success handler.
	 * 
	 * @author IanBrown
	 * @return the login success handler.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private LoginSuccessHandler getLoginSuccessHandler() {
		return loginSuccessHandler;
	}

	/**
	 * Gets the redirect strategy.
	 * 
	 * @author IanBrown
	 * @return the redirect strategy.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	/**
	 * Sets the login success handler.
	 * 
	 * @author IanBrown
	 * @param loginSuccessHandler
	 *            the login success handler to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setLoginSuccessHandler(
			final LoginSuccessHandler loginSuccessHandler) {
		this.loginSuccessHandler = loginSuccessHandler;
	}

	/**
	 * Sets the redirect strategy.
	 * 
	 * @author IanBrown
	 * @param redirectStrategy
	 *            the redirect strategy to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

}
