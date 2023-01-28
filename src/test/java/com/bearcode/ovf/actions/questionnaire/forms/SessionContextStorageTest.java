/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire.forms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;

/**
 * Extended {@link ContextStorageCheck} test for {@link SessionContextStorage}.
 * 
 * @author IanBrown
 * 
 * @since Jun 19, 2012
 * @version Jun 19, 2012
 */
public final class SessionContextStorageTest extends ContextStorageCheck<SessionContextStorage> {

	/**
	 * the request.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private MockHttpServletRequest request;

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
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage#create(javax.servlet.http.HttpServletRequest)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testCreateHttpServletRequest() {
		final SessionContextStorage actualContextStorage = SessionContextStorage.create(getRequest());

		assertNotNull("A session context storage is created", actualContextStorage);
		assertSame("The request is set", getRequest(), ReflectionTestUtils.getField(actualContextStorage, "request"));
		assertNull("No wizard context is set", ReflectionTestUtils.getField(actualContextStorage, "wizardContext"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage#create(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testCreateHttpServletRequestWizardContext() {
		final FlowType flowType = FlowType.RAVA;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = createWizardContext(wizardResults, flowType);
		replayAll();

		final SessionContextStorage actualContextStorage = SessionContextStorage.create(getRequest(), wizardContext);

		assertNotNull("A session context storage is created", actualContextStorage);
		assertSame("The request is set", getRequest(), ReflectionTestUtils.getField(actualContextStorage, "request"));
		assertSame("The wizard context is set", wizardContext, ReflectionTestUtils.getField(actualContextStorage, "wizardContext"));
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final void addWizardContext(final SessionContextStorage contextStorage, final WizardContext wizardContext) {
		ReflectionTestUtils.setField(contextStorage, "wizardContext", wizardContext);
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertActivated(final FlowType flowType) {
		final HttpSession session = getRequest().getSession();

		assertSame("The flow type is activated", flowType,
				session.getAttribute((String) ReflectionTestUtils.getField(getContextStorage(), "ACTIVE_FLOW_KEY")));
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertDelete() {
		final HttpSession session = getRequest().getSession();
		final WizardContext wizardContext = (WizardContext) ReflectionTestUtils.getField(getContextStorage(), "wizardContext");
		final WizardResults wizardResults = wizardContext.getWizardResults();
		final FlowType flowType = wizardResults.getFlowType();
		final String flowKey = flowKey(flowType);

		assertNull("There is no wizard context", session.getAttribute(flowKey));
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertSaved(final FlowType flowType, final WizardContext wizardContext) {
		assertActivated(flowType);

		final HttpSession session = getRequest().getSession();
		final String flowKey = flowKey(flowType);
		assertSame("There wizard context is saved", wizardContext, session.getAttribute(flowKey));
	}

	/** {@inheritDoc} */
	@Override
	protected final SessionContextStorage createContextStorage() {
		final SessionContextStorage sessionContextStorage = SessionContextStorage.create(getRequest());
		return sessionContextStorage;
	}

	/** {@inheritDoc} */
	@Override
	protected final WizardContext createWizardContext(final WizardResults wizardResults, final FlowType flowType) {
		final HttpSession session = getRequest().getSession();
		session.setAttribute((String) ReflectionTestUtils.getField(SessionContextStorage.create(request), "ACTIVE_FLOW_KEY"),
				flowType);
		final String flowKey = flowKey(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		session.setAttribute(flowKey, wizardContext);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).anyTimes();
		return wizardContext;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForActivate_activated(final FlowType flowType) {
		final MockHttpSession session = (MockHttpSession) getRequest().getSession();

		session.setAttribute((String) ReflectionTestUtils.getField(getContextStorage(), "ACTIVE_FLOW_KEY"), flowType);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForActivate_notActivated(final FlowType flowType) {
		// Nothing to do in this case.
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForContextStorage() {
		setSecurityContext(createMock("SecurityContext", SecurityContext.class));
		setRequest(new MockHttpServletRequest());
		setOriginalSecurityContext(SecurityContextHolder.getContext());
		SecurityContextHolder.setContext(getSecurityContext());
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForReset(final WizardResults wizardResults) {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).anyTimes();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForReset_clears(final WizardResults wizardResults) {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(wizardResults.getUser()).andReturn(null).anyTimes();
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForContextStorage() {
		SecurityContextHolder.setContext(getOriginalSecurityContext());
		setSecurityContext(null);
		setOriginalSecurityContext(null);
		setRequest(null);
	}

	/**
	 * Adds authentication to the security context.
	 * 
	 * @author IanBrown
	 * @return the authentication.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private Authentication addAuthenticationToSecurityContext() {
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
	private OverseasUser addOverseasUserToAuthentication(final Authentication authentication, final OverseasUser user) {
		EasyMock.expect(authentication.getPrincipal()).andReturn(user).anyTimes();
		return user;
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
	 * Gets the request.
	 * 
	 * @author IanBrown
	 * @return the request.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private MockHttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Gets the security context.
	 * 
	 * @author IanBrown
	 * @return the security context.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private SecurityContext getSecurityContext() {
		return securityContext;
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
	 * Sets the request.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setRequest(final MockHttpServletRequest request) {
		this.request = request;
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
