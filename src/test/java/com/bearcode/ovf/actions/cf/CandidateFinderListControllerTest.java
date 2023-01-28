/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.forms.cf.CandidateFinderForm;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import com.bearcode.ovf.webservices.votesmart.model.CandidateZip;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Extended {@link BaseControllerCheck} test for
 * {@link CandidateFinderListController}.
 * 
 * @author IanBrown
 * @author Leonid Ginzburg
 * 
 * @since Dec 20, 2011
 * @version Dec 21, 2011
 */
public final class CandidateFinderListControllerTest extends
        BaseControllerCheck<CandidateFinderListController> {

	/**
	 * the vote smart service.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private VoteSmartService voteSmartService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there are no candidates.
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
	public final void testBuildReferences_noCandidates() throws Exception {
		String candidateId = "";
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final List<CandidateZip> candidates = new ArrayList<CandidateZip>();
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
		replayAll();

		final String actualReferences = getBaseController()
				.showForm( request, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there are no candidates who are running or who have
	 * won.
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
	public final void testBuildReferences_noEligibleCandidates()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final CandidateZip candidate = createCandidate("not running", null);
		final List<CandidateZip> candidates = Arrays.asList(candidate);
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		addAttributeToModelMap( model, "senateList", null );
        addAttributeToModelMap( model, "representativeList", null );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
        replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals( "The main template is returned", ReflectionTestUtils.getField( getBaseController(), "mainTemplate" ),
                actualReferences );
        verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a running representative candidate and one
	 * who won.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testBuildReferences_runningAndWonRepresentativeCandidate()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final CandidateZip candidate = createCandidate("running", "5");
		final CandidateZip candidateWon = createCandidate("won", "5");
		final List<CandidateZip> candidates = Arrays.asList(candidate,
				candidateWon);
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		addAttributeToModelMap( model, "senateList", null );
        addAttributeToModelMap( model, "representativeList", Arrays
                .asList( candidate ) );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
		replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a running representative candidate.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testBuildReferences_runningRepresentativeCandidate()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final CandidateZip candidate = createCandidate("running", "5");
		final List<CandidateZip> candidates = Arrays.asList(candidate);
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		addAttributeToModelMap( model, "senateList", null );
        addAttributeToModelMap( model, "representativeList", Arrays.asList( candidate ) );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
		replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a running senate candidate.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testBuildReferences_runningSenateCandidate()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final CandidateZip candidate = createCandidate("running", "6");
		final List<CandidateZip> candidates = Arrays.asList(candidate);
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		addAttributeToModelMap( model, "senateList", Arrays.asList(candidate) );
        addAttributeToModelMap( model, "representativeList", null );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
		replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals( "The main template is returned", ReflectionTestUtils.getField( getBaseController(), "mainTemplate" ),
                actualReferences );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a running senate candidate and one who won.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testBuildReferences_runningWonSenateCandidate()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final CandidateZip candidate = createCandidate("running", "6");
		final CandidateZip candidateWon = createCandidate("won", "6");
		final List<CandidateZip> candidates = Arrays.asList(candidate,
				candidateWon);
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		addAttributeToModelMap( model, "senateList", Arrays.asList(candidate) );
        addAttributeToModelMap( model, "representativeList", null );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
		replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a representative candidate who won.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testBuildReferences_wonRepresentativeCandidate()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final CandidateZip candidate = createCandidate("won", "5");
		final List<CandidateZip> candidates = Arrays.asList(candidate);
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		addAttributeToModelMap( model, "senateList", null );
        addAttributeToModelMap( model, "representativeList", Arrays
                .asList( candidate ) );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
		replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a senate candidate that won.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testBuildReferences_wonSenateCandidate() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		final CandidateZip candidate = createCandidate("won", "6");
		final List<CandidateZip> candidates = Arrays.asList(candidate);
		addCandidates(request, candidates);
        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "address", request.getSession().getAttribute( "cf_address" ) );
		addAttributeToModelMap( model, "senateList", Arrays.asList(candidate) );
        addAttributeToModelMap( model, "representativeList", null );
		EasyMock.expect(getVoteSmartService().getPresidents()).andReturn(new ArrayList<CandidateZip>()).anyTimes();
		replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);
		verifyAll();
	}


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderListController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no address.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem showing the form.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testShowFormHttpServletRequestHttpServletResponseBindException_noAddress()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
        final ModelMap model = createModelMap( null, request, null, true, true );
		replayAll();

        final String actualReferences = getBaseController()
                .showForm( request, model );
        assertEquals("The redirect URL is set", "redirect:/CandidateFinder.htm",
                actualReferences);
		verifyAll();
	}

	/**
	 * Adds an address to the request.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the address.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private CandidateFinderForm addAddressToRequest(
			final MockHttpServletRequest request) {
		final CandidateFinderForm cfAddress = createMock("CandidateFinderForm",
				CandidateFinderForm.class);
		final MockHttpSession session = (MockHttpSession) request.getSession();
		session.setAttribute("cf_address", cfAddress);
		return cfAddress;
	}

	/**
	 * Adds the candidates.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param candidates
	 *            the candidates.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void addCandidates(final MockHttpServletRequest request,
			final List<CandidateZip> candidates) {
		final String zip5 = "ZIP05";
		final String zip4 = "ZIP4";
		final CandidateFinderForm cfAddress = addAddressToRequest(request);
		final Address address = createMock("Address", Address.class);
		EasyMock.expect(cfAddress.getAddress()).andReturn(address)
				.atLeastOnce();
		EasyMock.expect(address.getZip()).andReturn(zip5).anyTimes();
		EasyMock.expect(address.getZip4()).andReturn(zip4).anyTimes();
		EasyMock.expect(getVoteSmartService().getCandidatesByZip(zip5, zip4))
				.andReturn(candidates);
        EasyMock.expect(getVoteSmartService().getOfficialsByZip(zip5, zip4))
        				.andReturn(Collections.<CandidateZip>emptyList());
	}

	/**
	 * Custom assertion to ensure that the address is copied to the reference
	 * data.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param actualReferenceData
	 *            the actual reference data.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	private void assertAddress(final MockHttpServletRequest request,
			final Map actualReferenceData) {
		assertSame("The address is in the reference data", request.getSession()
				.getAttribute("cf_address"), actualReferenceData.get("address"));
	}

	private static int candidateId = 0;

	/**
	 * Creates a candidate with the specified election status, and optionally,
	 * office type.
	 * 
	 * @author IanBrown
	 * @param electionStatus
	 *            the election status.
	 * @param officeType
	 *            the office type (ignored if <code>null</code>).
	 * @return the candidate.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private CandidateZip createCandidate(final String electionStatus,
			final String officeType) {
		final CandidateZip candidate = createMock("Candidate",
				CandidateZip.class);
		EasyMock.expect(candidate.getElectionStatus())
				.andReturn( electionStatus ).atLeastOnce();
		if (officeType != null) {
			EasyMock.expect(candidate.getElectionOfficeId())
					.andReturn(officeType).atLeastOnce();
		}
		EasyMock.expect( candidate.getElectionStage() ).andReturn( "Primary" ).anyTimes();
		int candidateId = CandidateFinderListControllerTest.candidateId++;
		EasyMock.expect( candidate.getCandidateId() ).andReturn( String.valueOf( candidateId ) ).anyTimes();
		return candidate;
	}

	/**
	 * Gets the vote smart service.
	 * 
	 * @author IanBrown
	 * @return the vote smart service.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private VoteSmartService getVoteSmartService() {
		return voteSmartService;
	}

	/**
	 * Sets the vote smart service.
	 * 
	 * @author IanBrown
	 * @param voteSmartService
	 *            the vote smart service to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setVoteSmartService(final VoteSmartService voteSmartService) {
		this.voteSmartService = voteSmartService;
	}

    /** {@inheritDoc} */
    @Override
    protected CandidateFinderListController createBaseController() {
        final CandidateFinderListController candidateFinderListController = new CandidateFinderListController();
        ReflectionTestUtils.setField( candidateFinderListController, "voteSmartService", voteSmartService );
        return candidateFinderListController;
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/CandidateFinderList.jsp";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedPageTitle() {
        return "Candidates";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedSectionCss() {
        return "/css/candidate-finder.css";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedSectionName() {
        return "rava";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedSuccessContentBlock() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected void setUpForBaseController() {
        setVoteSmartService(createMock("VoteSmartService",
                VoteSmartService.class));
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDownForBaseController() {
        setVoteSmartService( null );
    }
}
