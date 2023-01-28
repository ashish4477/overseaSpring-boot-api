/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.tools.candidate.ElectionService;

/**
 * Extended {@link BaseControllerCheck} for {@link WomBCandidateBioController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 16, 2012
 * @version Oct 12, 2012
 */
public final class WomBCandidateBioControllerTest extends BaseControllerCheck<WomBCandidateBioController> {

	/**
	 * the election service.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Aug 24, 2012
	 */
	private ElectionService electionService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WomBCandidateBioController#handleGetCandidateBio(javax.servlet.http.HttpServletRequest, long, org.springframework.ui.ModelMap, WhatsOnMyBallotForm)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 16, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetCandidateBio() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = "VS";
		final String votingRegionName = null;
		final long candidateVipId = 8928l;
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		EasyMock.expect(votingState.getAbbr()).andReturn(votingRegionState);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		EasyMock.expect(address.getCounty()).andReturn(null);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final VipCandidateBio candidateBio = createMock("CandidateBio", VipCandidateBio.class);
		EasyMock.expect(getElectionService().findCandidateBio(votingRegionState, votingRegionName, candidateVipId)).andReturn(
				candidateBio);
		addAttributeToModelMap(model, "candidateBio", candidateBio);
		replayAll();

		final String actualResponse = getBaseController().handleGetCandidateBio(request, candidateVipId, model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final WomBCandidateBioController createBaseController() {
		final WomBCandidateBioController WomBCandidateBioController = new WomBCandidateBioController();
		WomBCandidateBioController.setElectionService(getElectionService());
		return WomBCandidateBioController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return WomBCandidateBioController.DEFAULT_CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return WomBCandidateBioController.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return WomBCandidateBioController.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return WomBCandidateBioController.DEFAULT_SECTION_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setElectionService(createMock("ElectionService", ElectionService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setElectionService(null);
	}

	/**
	 * Gets the election service.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	private ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Sets the election service.
	 * 
	 * @author IanBrown
	 * @param electionService
	 *            the election service to set.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	private void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

}
