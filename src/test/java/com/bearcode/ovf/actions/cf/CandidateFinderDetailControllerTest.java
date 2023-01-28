/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import com.bearcode.ovf.webservices.votesmart.model.CandidateAdditionalBio;
import com.bearcode.ovf.webservices.votesmart.model.CandidateBio;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import static org.junit.Assert.assertEquals;

/**
 * Extended {@link com.bearcode.ovf.actions.commons.BaseController} test for
 * {@link CandidateFinderDetailController}.
 * 
 * @author IanBrown
 * @author Leonid Ginzburg
 *
 * @since Dec 20, 2011
 * @version Dec 21, 2011
 */
public final class CandidateFinderDetailControllerTest extends
        BaseControllerCheck<CandidateFinderDetailController> {

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
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderDetailController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Long)}
	 * .
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
	public final void testBuildReferences() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);

        final Long cid = 7932l;
        final CandidateBio candidateBio = createMock("CandidateBio",
                CandidateBio.class);
        EasyMock.expect(getVoteSmartService().getCandidateBio(cid.toString()))
                .andReturn( candidateBio ).anyTimes();
        final CandidateAdditionalBio candidateAdditionalBio = createMock(
                "CandidateAdditionalBio", CandidateAdditionalBio.class);
        EasyMock.expect(getVoteSmartService().getCandidateAdditionalBio(cid.toString()))
                .andReturn( candidateAdditionalBio ).anyTimes();

        final ModelMap model = createModelMap( null, request, null, true, true );
        addAttributeToModelMap( model, "cb", candidateBio );
        addAttributeToModelMap( model, "cab", candidateAdditionalBio );
		replayAll();

		final String actualReferences = getBaseController()
				.showForm( request, model, cid );

        assertEquals( "The main template is returned", ReflectionTestUtils.getField( getBaseController(), "mainTemplate" ),
                actualReferences );
		verifyAll();
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
    protected CandidateFinderDetailController createBaseController() {
        final CandidateFinderDetailController candidateFinderDetailController = new CandidateFinderDetailController();
        ReflectionTestUtils.setField( candidateFinderDetailController, "voteSmartService", voteSmartService );
        return candidateFinderDetailController;
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/CandidateFinderDetail.jsp";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedPageTitle() {
        return "Candidate Details";
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
