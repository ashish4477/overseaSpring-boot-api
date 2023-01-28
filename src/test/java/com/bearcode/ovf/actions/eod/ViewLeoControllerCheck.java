/**
 * 
 */
package com.bearcode.ovf.actions.eod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.bearcode.ovf.webservices.grecaptcha.GReCaptchaService;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.service.LocalOfficialService;

/**
 * Abstract extended {@link BaseControllerCheck} for implementations of {@link ViewLeoController}.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of view leo controller to test.
 * @since Jul 25, 2012
 * @version Jul 25, 2012
 */
public abstract class ViewLeoControllerCheck<C extends BaseEodController> extends BaseControllerCheck<C> {

	/**
	 * the default expected section CSS.
	 * 
	 * @author IanBrown
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	protected static final String DEFAULT_EXPECTED_SECTION_CSS = "/css/eod.css";

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * the captcha service.
	 * 
	 * @author IanBrown
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	private GReCaptchaService captchaService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where everything is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "Captcha";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = 87;
		addAttributeToModelMap(model, EasyMock.eq("use_captcha_count"), EasyMock.eq(useCount - 1));
		// final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		// EasyMock.expect(getLocalOfficialService().findForRegion(regionId)).andReturn(leo);
		// addAttributeToModelMap(model, "leo", leo);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		// EasyMock.expect(leo.getRegion()).andReturn(region).anyTimes();
		addAttributeToModelMap(model, "selectedRegion", region);
		EasyMock.expect(model.get("selectedRegion")).andReturn(region).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		addAttributeToModelMap(model, "selectedState", state);
		EasyMock.expect(model.get("selectedState")).andReturn(state).anyTimes();
		// final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		// EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid);
		// addAttributeToModelMap(model, "svid", svid);
		addAttributeToModelMap(model, "content", getBaseController().getSuccessContentBlock());
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the captcha input is no good.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_badCaptchaInput() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "Captcha";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha( EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(false);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the captcha input is good, but there is no use count and the region ID is non zero.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_badUseCountNonZeroRegionID() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		// final Long stateId = 0l;
		// final Long regionId = 123768l;
		final String captchaInput = "Captcha";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha( EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(true);
		addAttributeToModelMap(model, EasyMock.eq("use_captcha_count"), EasyMock.anyObject());
		// final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		// EasyMock.expect(getLocalOfficialService().findForRegion(regionId)).andReturn(leo);
		// addAttributeToModelMap(model, "leo", leo);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		// EasyMock.expect(leo.getRegion()).andReturn(region).atLeastOnce();
		addAttributeToModelMap(model, "selectedRegion", region);
		EasyMock.expect(model.get("selectedRegion")).andReturn(region).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		addAttributeToModelMap(model, "selectedState", state);
		EasyMock.expect(model.get("selectedState")).andReturn(state).anyTimes();
		// final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		// EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid);
		// addAttributeToModelMap(model, "svid", svid);
		addAttributeToModelMap(model, "content", getBaseController().getSuccessContentBlock());
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the captcha input is good, but there is no use count and the region ID is null.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_badUseCountNoRegionId() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "Captcha";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha( EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(true);
		addAttributeToModelMap(model, "use_captcha_count", 5);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the captcha input is good, but there is no use count and the IDs are 0.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_badUseCountZeroIds() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "Captcha";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha( EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(true);
		addAttributeToModelMap(model, "use_captcha_count", 5);
		EasyMock.expect(model.get("selectedRegion")).andReturn(null).anyTimes();
		EasyMock.expect(model.get("selectedState")).andReturn(null).anyTimes();
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the IDs are non-zero.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_idsNonZero() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long regionId = 12366l;
		final String captchaInput = "";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(region.getId()).andReturn(regionId).anyTimes();
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(model.get("regions")).andReturn(regions).anyTimes();
		// final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		// EasyMock.expect(getLocalOfficialService().findForRegion(regionId)).andReturn(localOfficial).anyTimes();
		// addAttributeToModelMap(model, "leo", localOfficial);
		// EasyMock.expect(localOfficial.getRegion()).andReturn(region).anyTimes();
		addAttributeToModelMap(model, "selectedRegion", region);
		EasyMock.expect(model.get("selectedRegion")).andReturn(region).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		addAttributeToModelMap(model, "selectedState", state);
		EasyMock.expect(model.get("selectedState")).andReturn(state).anyTimes();
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the IDs are non-zero, but the region cannot be found.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_idsNonZeroNoSuchRegion() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long regionId = 12366l;
		final String captchaInput = "";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(region.getId()).andReturn(regionId + 1l).anyTimes();
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(model.get("regions")).andReturn(regions).anyTimes();
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where all of the IDs are <code>null</code>, the use count is <code>null</code>, and the user is
	 * <code>null</code>, and the captcha input is blank.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testCheckCaptcha_nullsBlankInput() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		replayAll();

		getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the state ID is non 0, but the regions ID is zero.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_stateIdNonZeroRegionIdZero() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		final Collection<VotingRegion> regions = Collections.emptyList();
		EasyMock.expect(model.get("regions")).andReturn(regions).anyTimes();
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the state ID is not 0, and the region is null.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_stateIdNonZeroRegionNull() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		final Collection<VotingRegion> regions = null;
		EasyMock.expect(model.get("regions")).andReturn(regions);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the state ID is 0.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testCheckCaptcha_stateIdZero() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = null;
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#checkCaptcha(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, javax.servlet.http.HttpSession, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the captcha input is good and there is a use count, but the IDs are 0.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testCheckCaptcha_useCountZeroIds() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final String captchaInput = "Captcha";
		final MockHttpSession theSession = new MockHttpSession();
		final Integer useCount = 86;
		EasyMock.expect(model.get("selectedRegion")).andReturn(null).anyTimes();
		EasyMock.expect(model.get("selectedState")).andReturn(null).anyTimes();
		replayAll();

		final String actualCheck = getBaseController().checkCaptcha(request, model, captchaInput, theSession, user, useCount);

		assertEquals("The check return is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualCheck);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#getLocalOfficialService()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	@Test
	public final void testGetLocalOfficialService() {
		assertSame("The local official service is set", getLocalOfficialService(), getBaseController().getLocalOfficialService());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#getPageUrlAndOther(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testGetPageUrlAndOther() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "regionLabel", "region");
		addAttributeToModelMap(model, "isEod", getBaseController().isShowEod());
		addAttributeToModelMap(model, "isSvid", getBaseController().isShowSvid());
		replayAll();

		final String actualPageUrlAndOther = getBaseController().getPageUrlAndOther(request, model);

		System.err.println(actualPageUrlAndOther);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#getRegions(java.lang.Long)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testGetRegions() {
		final Long stateId = 37l;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(getStateService().findRegionsForState(stateId)).andReturn(regions);
		replayAll();

		final Collection<VotingRegion> actualRegions = getBaseController().getRegions(stateId);

		assertSame("The regions are returned", regions, actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#getSelectedState(java.lang.Long)} for a non-zero state
	 * ID.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testGetSelectedState_nonZeroStateId() {
		final Long stateId = 872365l;
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateService().findState(stateId)).andReturn(state);
		replayAll();

		final State actualState = getBaseController().getSelectedState(stateId);

		assertSame("The state is returned", state, actualState);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#getSelectedState(java.lang.Long)} for a zero state ID.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testGetSelectedState_zeroStateId() {
		final Long stateId = 0l;
		replayAll();

		final State actualState = getBaseController().getSelectedState(stateId);

		assertNull("No state is returned", actualState);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#getUseCountAttribute()} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testGetUseCountAttribute() {
		replayAll();

		final Integer actualUseCountAttribute = ((ViewLeoController)getBaseController()).getUseCountAttribute();

		assertEquals("The use count attribute is five", new Integer(9), actualUseCountAttribute);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#isRegionAjax()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testIsRegionAjax() {
		replayAll();

		final boolean actualRegionAjax = getBaseController().isRegionAjax();

		assertFalse("The region AJAX flag is not set", actualRegionAjax);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#isShowEod()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testIsShowEod() {
		replayAll();

		final boolean actualShowEod = getBaseController().isShowEod();

		final boolean expectedShowEod = isShowEod();
		assertSame("The show EOD flag is set", expectedShowEod, actualShowEod);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#isShowSvid()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testIsShowSvid() {
		replayAll();

		final boolean actualShowSvid = getBaseController().isShowSvid();

		final boolean expectedShowSvid = isShowSvid();
		assertSame("The show SVID flag is set", expectedShowSvid, actualShowSvid);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#setCaptchaService(GReCaptchaService)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	@Test
	public final void testSetCaptchaService() {
		assertSame("The captcha service is set", getCaptchaService(),
				ReflectionTestUtils.getField(getBaseController(), "reCaptchaService"));
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#setRegionAjax(boolean)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testSetRegionAjax() {
		replayAll();

		getBaseController().setRegionAjax(true);

		assertTrue("The region AJAX flag is set", getBaseController().isRegionAjax());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#setShowEod(boolean)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testSetShowEod() {
		replayAll();

		getBaseController().setShowEod(false);

		assertFalse("The show EOD flag is clear", getBaseController().isShowEod());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.ViewLeoController#setShowSvid(boolean)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testSetShowSvid() {
		replayAll();

		getBaseController().setShowSvid(false);

		assertFalse("The show SVID flag is clear", getBaseController().isShowSvid());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#showPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testShowEodPage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long regionId = 32186l;
		final Integer useCount = 1276;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final Collection<VotingRegion> regions = Arrays.asList(region);
		EasyMock.expect(model.get("regions")).andReturn(regions).anyTimes();
		EasyMock.expect(region.getId()).andReturn(regionId).anyTimes();
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(getLocalOfficialService().findForRegion(regionId)).andReturn(leo).anyTimes();
		addAttributeToModelMap(model, "leo", leo);
		EasyMock.expect(leo.getRegion()).andReturn(region).anyTimes();
		addAttributeToModelMap(model, "selectedRegion", region);
		replayAll();

		final String actualShowEodPage = getBaseController().showPage(request, model, user, useCount);

		assertEquals("The returned show EOD page is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualShowEodPage);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.ViewLeoController#showResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testShowEodResult() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long regionId = 90340l;
		final Integer useCount = 8281;
		addAttributeToModelMap(model, "use_captcha_count", useCount - 1);
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(getLocalOfficialService().findForRegion(regionId)).andReturn(leo).anyTimes();
		addAttributeToModelMap(model, "leo", leo);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(leo.getRegion()).andReturn(region).anyTimes();
		addAttributeToModelMap(model, "selectedRegion", region);
		EasyMock.expect(model.get("selectedRegion")).andReturn(region).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		addAttributeToModelMap(model, "svid", svid);
		EasyMock.expect(model.get("selectedState")).andReturn(state).anyTimes();
		addAttributeToModelMap(model, "content", getBaseController().getSuccessContentBlock());
		replayAll();

		final String actualShowEodResult = getBaseController().showResult(request, model, user, useCount);

		assertEquals("The returned show EOD result is correct", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualShowEodResult);
		verifyAll();

	}

	/** {@inheritDoc} */
	@Override
	protected final C createBaseController() {
		final C viewLeoController = createViewLeoController();
		viewLeoController.setCaptchaService(getCaptchaService());
		viewLeoController.setLocalOfficialService(getLocalOfficialService());
		return viewLeoController;
	}

	/**
	 * Creates a view leo controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the view leo controller.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	protected abstract C createViewLeoController();

	/**
	 * Gets the captcha service.
	 * 
	 * @author IanBrown
	 * @return the captcha service.
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	protected final GReCaptchaService getCaptchaService() {
		return captchaService;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/EodLocalStart.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Election Official Directory";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return "eod";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return "/WEB-INF/pages/blocks/EodDisplay.jsp";
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Dec 23, 2011
	 * @version Jul 25, 2012
	 */
	protected final LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Should the EOD be shown?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> to show the EOD, <code>false</code> otherwise.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	protected abstract boolean isShowEod();

	/**
	 * Should the SVID be shown by the controller under test?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the SVID should be shown, <code>false</code> otherwise.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	protected abstract boolean isShowSvid();

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setCaptchaService(createMock("CaptchaService", GReCaptchaService.class));
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
		setUpForViewLeoController();
	}

	/**
	 * Sets up to test the specific type of view leo controller.
	 * 
	 * @author IanBrown
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	protected abstract void setUpForViewLeoController();

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		tearDownForViewLeoController();
		setLocalOfficialService(null);
		setCaptchaService(null);
	}

	/**
	 * Taears down the set up for testing the specific type of view leo controller.
	 * 
	 * @author IanBrown
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	protected abstract void tearDownForViewLeoController();

	/**
	 * Sets the captcha service.
	 * 
	 * @author IanBrown
	 * @param captchaService
	 *            the captcha service to set.
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	private void setCaptchaService(final GReCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	/**
	 * Sets the localOfficialService.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the localOfficialService to set.
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}
}