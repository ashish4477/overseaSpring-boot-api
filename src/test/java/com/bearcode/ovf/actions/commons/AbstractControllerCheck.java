/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bearcode.ovf.model.common.OverseasUser;

/**
 * Abstract test for implementations of {@link Controller}.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of overseas form controller (Spring3) to test.
 * @since Dec 19, 2011
 * @version Jun 19, 2012
 */
public abstract class AbstractControllerCheck<C> extends EasyMockSupport {

	/**
	 * the controller to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	private C controller;

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
	 * Sets up to test the controller.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	@Before
	public final void setUpController() {
		setSecurityContext(createMock("SecurityContext", SecurityContext.class));
		setUpForController();
		setController(createController());
		setOriginalSecurityContext(SecurityContextHolder.getContext());
		SecurityContextHolder.setContext(getSecurityContext());
	}

	/**
	 * Tears down the controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	@After
	public final void tearDownController() {
		setController(null);
		tearDownForController();
		SecurityContextHolder.setContext(getOriginalSecurityContext());
		setSecurityContext(null);
		setOriginalSecurityContext(null);
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
	 * Creates a controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the controller.
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	protected abstract C createController();

	/**
	 * Gets the controller.
	 * 
	 * @author IanBrown
	 * @return the controller.
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	protected final C getController() {
		return controller;
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
	 * Sets up to test the specific type of controller.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	protected abstract void setUpForController();

	/**
	 * Tears down the set up for testing the specific type of controller.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	protected abstract void tearDownForController();

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
	 * Sets the controller.
	 * 
	 * @author IanBrown
	 * @param controller
	 *            the controller to set.
	 * @since Dec 19, 2011
	 * @version Jun 19, 2012
	 */
	private void setController(final C controller) {
		this.controller = controller;
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
}
