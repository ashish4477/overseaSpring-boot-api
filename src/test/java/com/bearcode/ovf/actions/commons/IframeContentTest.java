/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.Errors;

import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;

/**
 * Extended {@link OverseasFormControllerCheck} test for {@link IframeContent}.
 * 
 * @author IanBrown
 * 
 * @since Dec 20, 2011
 * @version Dec 21, 2011
 */
public final class IframeContentTest extends
		OverseasFormControllerCheck<IframeContent> {

	/**
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private static final String DEFAULT_STATE = "TEST_STATE_1";

	/**
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private static final String DEFAULT_VOTING_REGION = "TestCounty1";

	/**
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private static final String DEFAULT_LOGIN = "voter";

	/**
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private static final String DEFAULT_REDIRECT_URL = "http://195.53.103.187:8080/diaspora/index.action";

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)}
	 * for the case where the defaults are used.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences_defaults() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

		final Map actualReferences = getOverseasFormController()
				.buildReferences(request, null, null);

		assertIframeURL(DEFAULT_REDIRECT_URL, DEFAULT_LOGIN,
				DEFAULT_VOTING_REGION, DEFAULT_STATE, actualReferences);
		assertNull("There is no iframe header",
				actualReferences.get("iframeHeader"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)}
	 * for the case where a iframe header is set.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences_iframeHeader() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String iframeHeader = "Iframe Header";
		getOverseasFormController().setIframeHeader(iframeHeader);
		replayAll();

		final Map actualReferences = getOverseasFormController()
				.buildReferences(request, null, null);

		assertIframeURL(DEFAULT_REDIRECT_URL, DEFAULT_LOGIN,
				DEFAULT_VOTING_REGION, DEFAULT_STATE, actualReferences);
		assertEquals("The iframe header is set", iframeHeader,
				actualReferences.get("iframeHeader"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)}
	 * for the case where a login is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences_login() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String login = "User Login";
		request.setParameter("login", login);
		replayAll();

		final Map actualReferences = getOverseasFormController()
				.buildReferences(request, null, null);

		assertIframeURL(DEFAULT_REDIRECT_URL, login, DEFAULT_VOTING_REGION,
				DEFAULT_STATE, actualReferences);
		assertNull("There is no iframe header",
				actualReferences.get("iframeHeader"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)}
	 * for the case where the redirect URL is set.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences_redirectURL() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String redirectURL = "http://redirect/redirectURL";
		request.setParameter("redirectURL", redirectURL);
		replayAll();

		final Map actualReferences = getOverseasFormController()
				.buildReferences(request, null, null);

		assertIframeURL(redirectURL, DEFAULT_LOGIN, DEFAULT_VOTING_REGION,
				DEFAULT_STATE, actualReferences);
		assertNull("There is no iframe header",
				actualReferences.get("iframeHeader"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)}
	 * for the case where the state is set.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences_state() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String state = "User State";
		request.setParameter("state", state);
		replayAll();

		final Map actualReferences = getOverseasFormController()
				.buildReferences(request, null, null);

		assertIframeURL(DEFAULT_REDIRECT_URL, DEFAULT_LOGIN,
				DEFAULT_VOTING_REGION, state, actualReferences);
		assertNull("There is no iframe header",
				actualReferences.get("iframeHeader"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)}
	 * for the case where the voting region is set.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences_votingRegion() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegion = "User Voting Region";
		request.setParameter("voting_region", votingRegion);
		replayAll();

		final Map actualReferences = getOverseasFormController()
				.buildReferences(request, null, null);

		assertIframeURL(DEFAULT_REDIRECT_URL, DEFAULT_LOGIN, votingRegion,
				DEFAULT_STATE, actualReferences);
		assertNull("There is no iframe header",
				actualReferences.get("iframeHeader"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#setIframeHeader(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testSetIframeHeader() {
		final String iframeHeader = "Iframe Header";

		getOverseasFormController().setIframeHeader(iframeHeader);

		assertEquals("The Iframe header is set", iframeHeader,
				ReflectionTestUtils.getField(getOverseasFormController(),
						"iframeHeader"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.IframeContent#setIframeURL(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testSetIframeURL() {
		final String iframeURL = "Iframe URL";

		getOverseasFormController().setIframeURL(iframeURL);

		assertEquals("The Iframe URL is set", iframeURL,
				ReflectionTestUtils.getField(getOverseasFormController(),
						"iframeURL"));
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertFormBackingObject(
			final Object actualFormBackingObject) {
		assertTrue("The form backing object is a string buffer",
				actualFormBackingObject instanceof StringBuffer);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	@Override
	protected final void assertMinimalReferenceData(
			final MockHttpServletRequest request, final Object object,
			final Errors errors, final StateService stateService,
			final FacesService facesService, final Map actualReferenceData) {
		assertIframeURL(DEFAULT_REDIRECT_URL, DEFAULT_LOGIN,
				DEFAULT_VOTING_REGION, DEFAULT_STATE, actualReferenceData);
		assertNull("There is no iframe header",
				actualReferenceData.get("iframeHeader"));
	}

	/** {@inheritDoc} */
	@Override
	protected final Object createCommand() {
		return createMock("Command", Object.class);
	}

	/** {@inheritDoc} */
	@Override
	protected final IframeContent createOverseasFormController() {
		final IframeContent iframeContent = new IframeContent();
		return iframeContent;
	}

	/** {@inheritDoc} */
	@Override
	protected final Object createReferenceObject() {
		return createMock("ReferenceObject", Object.class);
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean getExpectedPostFormSubmission(boolean submitted) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForOverseasFormController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpMinimalReferenceData(
			final MockHttpServletRequest request, final Object object,
			final Errors errors, final StateService stateService,
			final FacesService facesService) {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForOverseasFormController() {
	}

	/**
	 * Custom assertion to ensure that the iframe URL is built correctly.
	 * 
	 * @author IanBrown
	 * @param expectedRedirectURL
	 *            the expected redirect URL.
	 * @param expectedLogin
	 *            the expected login.
	 * @param expectedVotingRegion
	 *            expected voting region.
	 * @param expectedState
	 *            the expected state.
	 * @param actualReferenceData
	 *            the actual reference data to check.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	private void assertIframeURL(final String expectedRedirectURL,
			final String expectedLogin, final String expectedVotingRegion,
			final String expectedState, final Map actualReferenceData) {
		final String expectedIframeURL = "/bogus?redirectURL="
				+ expectedRedirectURL
				+ "&login="
				+ expectedLogin
				+ "&voting_region="
				+ expectedVotingRegion
				+ "&state="
				+ expectedState;
		assertEquals("The iframe URL is the expected value", expectedIframeURL,
				actualReferenceData.get("iframeURL"));
	}

}
