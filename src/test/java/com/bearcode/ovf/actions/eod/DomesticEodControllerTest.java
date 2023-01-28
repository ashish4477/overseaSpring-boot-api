/**
 * 
 */
package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.*;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import com.bearcode.ovf.webservices.grecaptcha.GReCaptchaService;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.IdentificationRequirementsList;
import com.bearcode.ovf.webservices.localelections.model.StateOfElection;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Extended {@link BaseControllerCheck} test for {@link DomesticEodController}.
 * 
 * @author IanBrown
 * 
 * @since Jan 17, 2012
 * @version Feb 6, 2012
 */
public final class DomesticEodControllerTest extends BaseControllerCheck<DomesticEodController> {

	/**
	 * the captcha service.
	 * 
	 * @author IanBrown
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	private GReCaptchaService captchaService;

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	private LocalOfficialService localOfficialService;

	private EodApiService eodApiService;

	private LocalElectionsService localElectionsService;

	private OvfPropertyService propertyService;
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the captcha input is bad.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 20, 2012
	 */
	@Test
	public final void testCheckCaptchaEod_badCaptcha() {
        prepareOvfProperties();
        final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final MockHttpSession session = new MockHttpSession();
		final Long stateId = 37l;
		final Long regionId = 92l;
		final String captchaInput = "Captcha Input";
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) )).andReturn( false );
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap( model, "isSvid", true );
		final EodRegion region = createMock("Region", EodRegion.class);
		final Collection<EodRegion> regions = Arrays.asList( region );
		EasyMock.expect(model.get("regions")).andReturn(regions);
		EasyMock.expect(region.getId()).andReturn( regionId );
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		addAttributeToModelMap( model, "leo", leo );
		EasyMock.expect(getEodApiService().getLocalOffice( String.valueOf( regionId ) )).andReturn( leo );
		//EasyMock.expect(leo.getRegion()).andReturn(region);
		addAttributeToModelMap(model, "selectedRegion", region);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaEod(request, model, request.getSession(), stateId, regionId,
				captchaInput, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the captcha input is good.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testCheckCaptchaEod_goodCaptcha() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, true);
		final Long stateId = 37l;
		final Long regionId = 92l;
		final String captchaInput = "Captcha Input";
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha( EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn( true );
		addAttributeToModelMap(model, DomesticEodController.USE_COUNT,
				DomesticEodController.number4Captcha );
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap( model, "isSvid", true );
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		EasyMock.expect( getEodApiService().getLocalOffice( String.valueOf( regionId ) ) ).andReturn( leo );
		addAttributeToModelMap( model, "leo", leo );

        final EodRegion region = new EodRegion();
        region.setId( regionId );
        region.setRegionName( "regionName" );

        addAttributeToModelMap( model, "selectedRegion", region );
		final State state = createMock("State", State.class);
		EasyMock.expect(state.getId()).andReturn(stateId).anyTimes();
		final StateOfElection stateOfElection = createMock("StateOfElection", StateOfElection.class);
		final StateVoterInformation stateVoterInformation = createNiceMock( "SVID", StateVoterInformation.class );
		EasyMock.expect( stateVoterInformation.getState() ).andReturn( stateOfElection ).anyTimes();
		EasyMock.expect( stateOfElection.getShortName() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( state.getAbbr() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( getStateService().findState( stateId ) ).andReturn( state );
		EasyMock.expect( getLocalElectionsService().findElectionsOfState( EasyMock.<String>anyObject() ) ).andReturn( Collections.<ElectionView>emptyList() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( stateVoterInformation );
		addAttributeToModelMap( model, "stateVoterInformation", stateVoterInformation );

        addAttributeToModelMap( model, DomesticEodController.USE_COUNT,
				DomesticEodController.number4Captcha - 1 );
		addAttributeToModelMap( model, EasyMock.eq("localElections"), EasyMock.anyObject() );
        List<EodRegion> regions = new ArrayList<EodRegion>();
        regions.add( region );
        addAttributeToModelMap( model, "regions", regions );
        EasyMock.expect( model.get( "regions" ) ).andReturn( regions ).anyTimes();
		replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaEod(request, model, request.getSession(), stateId, regionId,
				captchaInput, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a use count.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testCheckCaptchaEod_useCount() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, true);
		final MockHttpSession session = new MockHttpSession();
		final Long stateId = 37l;
		final Long regionId = 92l;
		final String captchaInput = "Captcha Input";
		final Integer useCount = 4;
		addAttributeToModelMap(model, "isEod", true);
		addAttributeToModelMap( model, "isSvid", true );
		addAttributeToModelMap(model, DomesticEodController.USE_COUNT, useCount - 1);
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		EasyMock.expect( getEodApiService().getLocalOffice( String.valueOf( regionId ) ) ).andReturn( leo );
		addAttributeToModelMap(model, "leo", leo);

        final EodRegion region = new EodRegion();
        region.setId( regionId );
        region.setRegionName( "regionName" );

		addAttributeToModelMap( model, "selectedRegion", region );
		final State state = createMock("State", State.class);
		EasyMock.expect(state.getId()).andReturn(stateId).anyTimes();
		//EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		final StateOfElection stateOfElection = createMock( "StateOfElection", StateOfElection.class );
		final StateVoterInformation stateVoterInformation = createNiceMock("SVID", StateVoterInformation.class);
		EasyMock.expect( stateVoterInformation.getState() ).andReturn( stateOfElection ).anyTimes();
		EasyMock.expect( stateOfElection.getShortName() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( state.getAbbr() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( getStateService().findState( stateId ) ).andReturn( state );
		EasyMock.expect( getLocalElectionsService().findElectionsOfState( EasyMock.<String>anyObject() ) ).andReturn( Collections.<ElectionView>emptyList() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( stateVoterInformation );
		addAttributeToModelMap( model, "stateVoterInformation", stateVoterInformation );

        List<EodRegion> regions = new ArrayList<EodRegion>();
        regions.add( region );
        addAttributeToModelMap( model, "regions", regions );
        EasyMock.expect( model.get( "regions" ) ).andReturn( regions ).anyTimes();
		replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaEod(request, model, request.getSession(), stateId, regionId,
				captchaInput, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testCheckCaptchaEod_user() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, true );
        final MockHttpSession session = new MockHttpSession();
		final Long stateId = 37l;
        final Long regionId = 92l;
		final String captchaInput = "Captcha Input";
        final Integer useCount = null;
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap( model, "isSvid", true);
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		EasyMock.expect( getEodApiService().getLocalOffice( String.valueOf( regionId ) ) ).andReturn( leo );
		addAttributeToModelMap(model, "leo", leo);

        final EodRegion region = new EodRegion();
        region.setId( regionId );
        region.setRegionName( "regionName" );

        addAttributeToModelMap( model, "selectedRegion", region );
		final State state = createMock("State", State.class);
		EasyMock.expect(state.getId()).andReturn(stateId).anyTimes();
        //EasyMock.expect(region.getState()).andReturn(state).anyTimes();
        final StateOfElection stateOfElection = createMock( "StateOfElection", StateOfElection.class);
		final StateVoterInformation stateVoterInformation = createNiceMock("SVID", StateVoterInformation.class );
        EasyMock.expect( stateVoterInformation.getState() ).andReturn( stateOfElection ).anyTimes();
		EasyMock.expect( stateOfElection.getShortName() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( state.getAbbr() ).andReturn( "AB" ).anyTimes();
        EasyMock.expect( getStateService().findState( stateId ) ).andReturn( state );
		EasyMock.expect( getLocalElectionsService().findElectionsOfState( EasyMock.<String>anyObject() ) ).andReturn( Collections.<ElectionView>emptyList() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( stateVoterInformation );
		addAttributeToModelMap( model, "stateVoterInformation", stateVoterInformation );

        List<EodRegion> regions = new ArrayList<EodRegion>();
        regions.add( region );
        addAttributeToModelMap( model, "regions", regions );
        EasyMock.expect( model.get( "regions" ) ).andReturn( regions ).anyTimes();
		replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaEod(request, model, request.getSession(), stateId, regionId,
				captchaInput, user, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaSvid(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a bad captcha.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 20, 2012
	 */
	@Test
	public final void testCheckCaptchaSvid_badCaptcha() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final MockHttpSession session = new MockHttpSession();
		final Long stateId = 1l;
		final Long regionId = 0l;
		final String captchaInput = "Captcha Input";
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn( false );
		addAttributeToModelMap(model, "isEod", false);
		addAttributeToModelMap(model, "isSvid", true);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaSvid(request, model, request.getSession(), stateId, regionId,"","",
				captchaInput, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaSvid(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a good captcha.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testCheckCaptchaSvid_goodCaptcha() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, true);
		final MockHttpSession session = new MockHttpSession();
		final Long stateId = 1l;
		final Long regionId = 0l;
		final String captchaInput = "Captcha Input";
		final Integer useCount = null;
		EasyMock.expect(getCaptchaService().verifyCaptcha( EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(true);
		addAttributeToModelMap(model, DomesticEodController.USE_COUNT,
				DomesticEodController.number4Captcha);
        addAttributeToModelMap( model, "isEod", false );
		addAttributeToModelMap( model, "isSvid", true );
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect( svid.getState() ).andReturn( new State() ).anyTimes();
		EasyMock.expect(getStateService().findState(stateId)).andReturn( new State() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( null );
		addAttributeToModelMap( model, EasyMock.eq( "localElections" ), EasyMock.anyObject() );
		addAttributeToModelMap( model, "stateVoterInformation", svid );

		addAttributeToModelMap( model, "useCaptcha", true );
		replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaSvid(request, model, request.getSession(), stateId, regionId,"","",
				captchaInput, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaSvid(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a use count.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testCheckCaptchaSvid_useCount() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, true);
		final MockHttpSession session = new MockHttpSession();
		final Long stateId = 1l;
		final Long regionId = 0l;
		final String captchaInput = "Captcha Input";
        final Integer useCount = 1;
        addAttributeToModelMap( model, "isEod", false );
		addAttributeToModelMap(model, "isSvid", true);
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect( svid.getState() ).andReturn( new State() ).anyTimes();
		EasyMock.expect(getStateService().findState(stateId)).andReturn( new State() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( null );
		addAttributeToModelMap( model, EasyMock.eq( "localElections" ), EasyMock.anyObject() );
        addAttributeToModelMap( model, "stateVoterInformation", svid );

        addAttributeToModelMap(model, "useCaptcha", true);
		replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaSvid(request, model, request.getSession(), stateId, regionId,"","",
				captchaInput, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#checkCaptchaSvid(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, javax.servlet.http.HttpSession, java.lang.Long, java.lang.Long, java.lang.String, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testCheckCaptchaSvid_user() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class );
        addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, true);
		final MockHttpSession session = new MockHttpSession();
		final Long stateId = 1l;
        final Long regionId = 0l;
		final String captchaInput = "Captcha Input";
		final Integer useCount = 1;
		addAttributeToModelMap(model, "isEod", false );
		addAttributeToModelMap( model, "isSvid", true );
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect( svid.getState() ).andReturn( new State() ).anyTimes();
		EasyMock.expect(getStateService().findState( stateId )).andReturn( new State() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( null );
		addAttributeToModelMap( model, EasyMock.eq( "localElections" ), EasyMock.anyObject() );
        addAttributeToModelMap( model, "stateVoterInformation", svid );

        replayAll();

		final String actualModelAndView = getBaseController().checkCaptchaSvid(request, model, request.getSession(), stateId, regionId,"","",
				captchaInput, user, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}


	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getDomestic()}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testGetDomestic() {
		final boolean actualDomestic = getBaseController().getDomestic();

		assertTrue("The controller handles domestic pages", actualDomestic);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getLocalOfficialService()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetLocalOfficialService() {
		final LocalOfficialService actualLocalOfficialService = getBaseController().getLocalOfficialService();

		assertSame( "The local official service is set", getLocalOfficialService(), actualLocalOfficialService );
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getPageUrl(javax.servlet.http.HttpServletRequest)}.
     *
     * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetPageUrl() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String servletPath = "Servlet Path";
		request.setServletPath(servletPath);

		final String actualPageUrl = getBaseController().getPageUrl( request );

		assertEquals( "The servlet path is returned", servletPath, actualPageUrl );
    }

    /**
     * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getRegionLabel(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetRegionLabel() {
		final String regionLabel = "Region Label";

		final String actualRegionLabel = getBaseController().getRegionLabel( regionLabel );

		assertEquals("The region label is returned", regionLabel, actualRegionLabel);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getRegions(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetRegions() {
		final Long stateId = 1l;
        State state = new State();
        state.setAbbr( "Abbr" );
        state.setId( stateId );
		final EodRegion region = createMock("Region", EodRegion.class);
		final List<EodRegion> regions = Arrays.asList(region);
        EasyMock.expect( getStateService().findState( stateId ) ).andReturn( state );
		EasyMock.expect(getEodApiService().getRegionsOfState( state.getAbbr() )).andReturn(regions);
		replayAll();

		final Collection<EodRegion> actualRegions = getBaseController().getRegions( stateId );

		assertSame("The regions are returned", regions, actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getSelectedState(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetSelectedState() {
		final Long stateId = 1l;
		final State state = createMock( "State", State.class );
		EasyMock.expect(getStateService().findState( stateId )).andReturn(state);
		replayAll();

		final State actualSelectedState = getBaseController().getSelectedState( stateId );

		assertSame("The state is returned", state, actualSelectedState );
        verifyAll();
    }

    /**
     * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getSelectedState(java.lang.Long)} for a state ID of
	 * zero.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetSelectedState_zeroStateId() {
		final State actualSelectedState = getBaseController().getSelectedState(0l);

        assertNull( "There is no state", actualSelectedState );
    }

    /**
     * Test method for {@link com.bearcode.ovf.actions.eod.DomesticEodController#getUseCountAttribute()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetUseCountAttribute() {
		final Integer actualUseCountAttribute = getBaseController().getUseCountAttribute();

		assertEquals( "The actual use count attribute is five", 9, actualUseCountAttribute.intValue() );
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticEod() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long stateId = 2l;
		final Long regionId = 77l;
		final Integer useCount = 1;
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap( model, "isSvid", true );
		final EodRegion region = createMock("Region", EodRegion.class);
		final Collection<EodRegion> regions = Arrays.asList( region );
		EasyMock.expect(model.get("regions")).andReturn(regions);
		EasyMock.expect(region.getId()).andReturn( regionId );
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		addAttributeToModelMap( model, "leo", leo );
		EasyMock.expect(getEodApiService().getLocalOffice( String.valueOf( regionId ) )).andReturn(leo);
		//EasyMock.expect(leo.getRegion()).andReturn(region);
		addAttributeToModelMap(model, "selectedRegion", region);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEod(request, model, stateId, regionId, user, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is no region ID.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticEod_noRegionId() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long stateId = 2l;
		final Long regionId = 0l;
		final Integer useCount = 1;
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap(model, "isSvid", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEod(request, model, stateId, regionId, user, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the state is 0 and there is no user, but the use count is positive.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticEod_NoStateOrUserPositiveUseCount() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final Long stateId = 0l;
		final Long regionId = 0l;
		final Integer useCount = 1;
		addAttributeToModelMap(model, "isEod", true);
		addAttributeToModelMap(model, "isSvid", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEod(request, model, stateId, regionId, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the state is 0 and there is no user or use count.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 20, 2012
	 */
	@Test
	public final void testShowDomesticEod_NoStateUserOrUseCount() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final Long stateId = 0l;
		final Long regionId = 0l;
		final Integer useCount = null;
		addAttributeToModelMap(model, "isEod", true);
		addAttributeToModelMap(model, "isSvid", true);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEod(request, model, stateId, regionId, null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where the region does not belong to the state.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticEod_regionFromAnotherState() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication( authentication, user );
		final ModelMap model = createModelMap(user, request, null, true, false);
        final Long stateId = 2l;
		final Long regionId = 77l;
		final Integer useCount = 1;
		addAttributeToModelMap(model, "isEod", true);
		addAttributeToModelMap(model, "isSvid", true);
		final EodRegion region = createMock("Region", EodRegion.class);
		final Collection<EodRegion> regions = Arrays.asList(region);
		EasyMock.expect(model.get("regions")).andReturn( regions );
		EasyMock.expect(region.getId()).andReturn( regionId + 1 );
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEod(request, model, stateId, regionId, user, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEod(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
     * for the case where the state is 0 and there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticEod_userNoState() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long stateId = 0l;
		final Long regionId = 0l;
		final Integer useCount = 1;
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap( model, "isSvid", true );
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEod(request, model, stateId, regionId, user, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEodResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where no region or state identifier is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticEodResult_noRegionIdOrStateId() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class );
        addOverseasUserToAuthentication( authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long stateId = 0l;
		final Long regionId = 0l;
		final Integer useCount = 1;
		addAttributeToModelMap(model, "isEod", true);
		addAttributeToModelMap(model, "isSvid", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEodResult( request, model, stateId, regionId, user,
                useCount );

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEodResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is no user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testShowDomesticEodResult_noUser() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, true);
		final Long stateId = 37l;
		final Long regionId = 92l;
		final Integer useCount = 4;
		addAttributeToModelMap(model, "isEod", true);
		addAttributeToModelMap( model, "isSvid", true );
		addAttributeToModelMap( model, DomesticEodController.USE_COUNT, useCount - 1 );
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		EasyMock.expect( getEodApiService().getLocalOffice( String.valueOf( regionId ) ) ).andReturn( leo );
		addAttributeToModelMap( model, "leo", leo );

        final EodRegion region = new EodRegion();
        region.setId( regionId );
        region.setRegionName( "regionName" );

        addAttributeToModelMap( model, "selectedRegion", region );
		final State state = createMock("State", State.class);
		EasyMock.expect(state.getId()).andReturn(stateId).anyTimes();
		//EasyMock.expect(region.getState()).andReturn( state ).anyTimes();
		final StateOfElection stateOfElection = createMock("StateOfElection", StateOfElection.class);
		final StateVoterInformation stateVoterInformation = createNiceMock("SVID", StateVoterInformation.class);
		EasyMock.expect( stateVoterInformation.getState() ).andReturn( stateOfElection ).anyTimes();
		EasyMock.expect( stateOfElection.getShortName() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( state.getAbbr() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( getStateService().findState( stateId ) ).andReturn( state );
		EasyMock.expect( getLocalElectionsService().findElectionsOfState( EasyMock.<String>anyObject() ) ).andReturn( Collections.<ElectionView>emptyList() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( stateVoterInformation );
		addAttributeToModelMap( model, "stateVoterInformation", stateVoterInformation );

        List<EodRegion> regions = new ArrayList<EodRegion>();
        regions.add( region );
        addAttributeToModelMap( model, "regions", regions );
        EasyMock.expect( model.get( "regions" ) ).andReturn( regions ).anyTimes();
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEodResult(request, model, stateId, regionId, null,
				useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEodResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is no user or use count.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 20, 2012
	 */
	@Test
	public final void testShowDomesticEodResult_noUserOrUseCount() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final Long stateId = 37l;
		final Long regionId = 92l;
		final Integer useCount = null;
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap( model, "isSvid", true );
		final EodRegion region = createMock("Region", EodRegion.class);
		final Collection<EodRegion> regions = Arrays.asList( region );
		EasyMock.expect(model.get("regions")).andReturn(regions);
		EasyMock.expect(region.getId()).andReturn( regionId );
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		addAttributeToModelMap( model, "leo", leo );
		EasyMock.expect(getEodApiService().getLocalOffice( String.valueOf( regionId ) )).andReturn(leo);
		//EasyMock.expect(leo.getRegion()).andReturn(region);
		addAttributeToModelMap(model, "selectedRegion", region);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEodResult(request, model, stateId, regionId, null,
				useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEodResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a user, state identifier, but no region identifier.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testShowDomesticEodResult_stateIdNoRegionId() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, true);
		final Long stateId = 2l;
		final Long regionId = 0l;
		final Integer useCount = 1;
		addAttributeToModelMap(model, "isEod", true);
		addAttributeToModelMap(model, "isSvid", true);
		final StateSpecificDirectory svid = createMock( "SVID", StateSpecificDirectory.class );
		EasyMock.expect( svid.getState() ).andReturn( new State() ).anyTimes();
		EasyMock.expect(getStateService().findState( stateId )).andReturn( new State() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( null );
		addAttributeToModelMap( model, EasyMock.eq( "localElections" ), EasyMock.anyObject() );
        addAttributeToModelMap( model, "stateVoterInformation", svid );

        replayAll();

		final String actualModelAndView = getBaseController().showDomesticEodResult(request, model, stateId, regionId, user,
				useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticEodResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testShowDomesticEodResult_user() {
        prepareOvfProperties();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, true);
		final Long stateId = 37l;
		final Long regionId = 92l;
		final Integer useCount = null;
		addAttributeToModelMap( model, "isEod", true );
		addAttributeToModelMap(model, "isSvid", true);
		final LocalOffice leo = createMock("LEO", LocalOffice.class);
		EasyMock.expect( leo.getId() ).andReturn( regionId ).anyTimes();
		EasyMock.expect( getEodApiService().getLocalOffice( String.valueOf( regionId ) ) ).andReturn( leo );
		addAttributeToModelMap(model, "leo", leo);

        final EodRegion region = new EodRegion();
        region.setId( regionId );
        region.setRegionName( "regionName" );

        addAttributeToModelMap( model, "selectedRegion", region );
		final State state = createMock("State", State.class);
		EasyMock.expect(state.getId()).andReturn(stateId).anyTimes();
		//EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		final StateOfElection stateOfElection = createMock("StateOfElection", StateOfElection.class);
		final StateVoterInformation stateVoterInformation = createNiceMock("SVID", StateVoterInformation.class);
		EasyMock.expect( stateVoterInformation.getState() ).andReturn( stateOfElection ).anyTimes();
		EasyMock.expect( stateOfElection.getShortName() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( state.getAbbr() ).andReturn( "AB" ).anyTimes();
		EasyMock.expect( getStateService().findState( stateId ) ).andReturn( state );
		EasyMock.expect( getLocalElectionsService().findElectionsOfState( EasyMock.<String>anyObject() ) ).andReturn( Collections.<ElectionView>emptyList() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( stateVoterInformation );
		addAttributeToModelMap( model, "stateVoterInformation", stateVoterInformation );

        List<EodRegion> regions = new ArrayList<EodRegion>();
        regions.add( region );
        addAttributeToModelMap( model, "regions", regions );
        EasyMock.expect( model.get( "regions" ) ).andReturn( regions ).anyTimes();
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticEodResult(request, model, stateId, regionId, user,
				useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticSvid(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticSvid() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class );
        addOverseasUserToAuthentication( authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long stateId = 2l;
		final Long regionId = 77l;
		final Integer useCount = 1;
		addAttributeToModelMap(model, "isEod", false);
		addAttributeToModelMap(model, "isSvid", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticSvid(request, model, stateId, regionId,"","", user, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticSvid(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * for the case where there is no user and the use count is 0.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 20, 2012
	 */
	@Test
	public final void testShowDomesticSvid_noUserZeroUseCount() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final Long stateId = 0l;
		final Long regionId = 0l;
		final Integer useCount = 0;
		addAttributeToModelMap(model, "isEod", false);
		addAttributeToModelMap(model, "isSvid", true);
		addAttributeToModelMap(model, "showCaptcha", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticSvid(request, model, stateId, regionId,"","", null, useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticSvidResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Feb 6, 2012
	 */
	@Test
	public final void testShowDomesticSvidResult() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, true);
		final Long stateId = 1l;
		final Long regionId = 0l;
		final Integer useCount = 1;
		addAttributeToModelMap(model, "isEod", false);
		addAttributeToModelMap( model, "isSvid", true );
		final StateSpecificDirectory svid = createMock( "SVID", StateSpecificDirectory.class );
		EasyMock.expect( svid.getState() ).andReturn( new State() ).anyTimes();
		EasyMock.expect(getStateService().findState( stateId ) ).andReturn( new State() );
		EasyMock.expect( getLocalElectionsService().findStateVoterInformation( EasyMock.<String>anyObject() ) ).andReturn( null );
		addAttributeToModelMap( model, EasyMock.eq( "localElections" ), EasyMock.anyObject() );
        addAttributeToModelMap( model, "stateVoterInformation", svid );

        replayAll();

		final String actualModelAndView = getBaseController().showDomesticSvidResult( request, model, stateId, regionId, "","",user,
                useCount );

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.DomesticEodController#showDomesticSvidResult(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long, java.lang.Long, com.bearcode.ovf.model.common.OverseasUser, java.lang.Integer)}
     * for the case where no state identifier is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testShowDomesticSvidResult_noStateId() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class );
        addOverseasUserToAuthentication( authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Long stateId = 0l;
		final Long regionId = 0l;
		final Integer useCount = 1;
		addAttributeToModelMap( model, "isEod", false );
		addAttributeToModelMap(model, "isSvid", true);
		replayAll();

		final String actualModelAndView = getBaseController().showDomesticSvidResult(request, model, stateId, regionId,"","", user,
				useCount);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}


    @Test
    public final void testCorrectionUri_localUrl() {
        EasyMock.expect( getPropertyService().getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_TEMPLATE ) )
                .andReturn( "/corrections.htm?leoId=LEO_ID" ).anyTimes();
        EasyMock.expect( getPropertyService().getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_PARAM ) )
                .andReturn( OvfPropertyNames.EOD_API_CORRECTION_URL_PARAM.getDefaultValue() ).anyTimes();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath( "/vote" );
        replayAll();

        final String correctionUrl = getBaseController().buildCorrectionsUri( 55L, request );

        assertTrue( "Correction URL should contain Servlet path", correctionUrl.contains( request.getContextPath() ) );
        verifyAll();
    }

    @Test
    public final void testCorrectionUri_localUrlWithWrongPath() {
        EasyMock.expect( getPropertyService().getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_TEMPLATE ) )
                .andReturn( "vote/corrections.htm?leoId=LEO_ID" ).anyTimes();
        EasyMock.expect( getPropertyService().getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_PARAM ) )
                .andReturn( OvfPropertyNames.EOD_API_CORRECTION_URL_PARAM.getDefaultValue() ).anyTimes();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath( "/vote" );
        replayAll();

        final String correctionUrl = getBaseController().buildCorrectionsUri( 55L, request );

        assertTrue( "Correction URL should contain Servlet path", correctionUrl.contains( request.getContextPath() ) );
        assertFalse( "Correction URL should contain Servlet path only once", correctionUrl.substring( request.getContextPath().length() ).contains( request.getContextPath().substring( 1 ) ) );
        verifyAll();
    }

    /** {@inheritDoc} */
	@Override
	protected final DomesticEodController createBaseController() {
		final DomesticEodController domesticEodController = new DomesticEodController();
		domesticEodController.setLocalOfficialService( getLocalOfficialService() );
		domesticEodController.setReCaptchaService( getCaptchaService() );
		domesticEodController.setLocalElectionsService( getLocalElectionsService() );
        domesticEodController.setEodApiService( getEodApiService() );
		//domesticEodController.setCorrectionsUri( "http://electionmanagerstage-demo2.us-east-1.elasticbeanstalk.com/corrections/legacy/LEO_ID/submit/" );
		//domesticEodController.setCorrectionsIdPattern( "LEO_ID" );
        ReflectionTestUtils.setField( domesticEodController, "propertyService", getPropertyService() );
		return domesticEodController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return DomesticEodController.DOMESTIC_EOD_START_JSP;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Information Directory";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return DomesticEodController.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return DomesticEodController.DEFAULT_SECTION_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return DomesticEodController.DOMESTIC_EOD_RESULT_JSP;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setLocalOfficialService( createMock( "LocalOfficialService", LocalOfficialService.class ) );
		setCaptchaService( createMock( "CaptchaService", GReCaptchaService.class ) );
		setLocalElectionsService( createMock( "localElectionsService", LocalElectionsService.class ) );
	    setEodApiService( createMock( "eodApiService", EodApiService.class ) );
        setPropertyService( createMock( "propertyService", OvfPropertyService.class ) );
    }

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setCaptchaService( null );
		setLocalOfficialService( null );
		setLocalElectionsService( null );
	}

	/**
	 * Gets the captcha service.
	 * 
	 * @author IanBrown
	 * @return the captcha service.
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	private GReCaptchaService getCaptchaService() {
		return captchaService;
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the captcha service.
	 * 
	 * @author IanBrown
	 * @param captchaService
	 *            the captcha service to set.
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	private void setCaptchaService(final GReCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

	public LocalElectionsService getLocalElectionsService() {
		return localElectionsService;
	}

	public void setLocalElectionsService( LocalElectionsService localElectionsService ) {
		this.localElectionsService = localElectionsService;
	}

	public EodApiService getEodApiService() {
		return eodApiService;
	}

	public void setEodApiService( EodApiService eodApiService ) {
		this.eodApiService = eodApiService;
	}

    public OvfPropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService( OvfPropertyService propertyService ) {
        this.propertyService = propertyService;
    }

    public void prepareOvfProperties() {
        EasyMock.expect( getPropertyService().getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_TEMPLATE ) )
                .andReturn( OvfPropertyNames.EOD_API_CORRECTION_URL_TEMPLATE.getDefaultValue() ).anyTimes();
        EasyMock.expect( getPropertyService().getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_PARAM ) )
                .andReturn( OvfPropertyNames.EOD_API_CORRECTION_URL_PARAM.getDefaultValue() ).anyTimes();
    }
}
