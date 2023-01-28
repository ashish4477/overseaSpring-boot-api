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
 *            the type of component (Spring3) to test.
 * @since Aug 7, 2012
 * @version Aug 7, 2012
 */
public abstract class AbstractComponentCheck<C> extends EasyMockSupport {

	/**
	 * the component to test.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2011
	 * @version Aug 7, 2012
	 */
	private C component;

	/**
	 * the original security context.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private SecurityContext originalSecurityContext;

	/**
	 * the security context.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 201A
	 */
	private SecurityContext securityContext;

	/**
	 * Sets up to test the component.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2011
	 * @version Aug 7, 2012
	 */
	@Before
	public final void setUpComponent() {
		setSecurityContext(createMock("SecurityContext", SecurityContext.class));
		setUpForComponent();
		setComponent(createComponent());
		setOriginalSecurityContext(SecurityContextHolder.getContext());
		SecurityContextHolder.setContext(getSecurityContext());
	}

	/**
	 * Tears down the component after testing.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	@After
	public final void tearDownComponent() {
		setComponent(null);
		tearDownForComponent();
		SecurityContextHolder.setContext(getOriginalSecurityContext());
		setSecurityContext(null);
		setOriginalSecurityContext(null);
	}

	/**
	 * Adds authentication to the security context.
	 * 
	 * @author IanBrown
	 * @return the authentication.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
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
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	protected final OverseasUser addOverseasUserToAuthentication(final Authentication authentication, final OverseasUser user) {
		EasyMock.expect(authentication.getPrincipal()).andReturn(user).anyTimes();
		return user;
	}

	/**
	 * Creates a component of the type to test.
	 * 
	 * @author IanBrown
	 * @return the component.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	protected abstract C createComponent();

	/**
	 * Gets the component.
	 * 
	 * @author IanBrown
	 * @return the component.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	protected final C getComponent() {
		return component;
	}

	/**
	 * Gets the security context.
	 * 
	 * @author IanBrown
	 * @return the security context.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	protected final SecurityContext getSecurityContext() {
		return securityContext;
	}

	/**
	 * Sets up to test the specific type of component.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	protected abstract void setUpForComponent();

	/**
	 * Tears down the set up for testing the specific type of component.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	protected abstract void tearDownForComponent();

	/**
	 * Gets the original security context.
	 * 
	 * @author IanBrown
	 * @return the original security context.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private SecurityContext getOriginalSecurityContext() {
		return originalSecurityContext;
	}

	/**
	 * Sets the component.
	 * 
	 * @author IanBrown
	 * @param component
	 *            the component to set.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private void setComponent(final C component) {
		this.component = component;
	}

	/**
	 * Sets the original security context.
	 * 
	 * @author IanBrown
	 * @param originalSecurityContext
	 *            the original security context to set.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
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
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private void setSecurityContext(final SecurityContext securityContext) {
		this.securityContext = securityContext;
	}
}
