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
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.tools.candidate.ElectionService;

/**
 * Extended {@link BaseControllerCheck} for {@link WomBReferendumDetailController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 16, 2012
 * @version Oct 12, 2012
 */
public final class WomBReferendumDetailControllerTest extends BaseControllerCheck<WomBReferendumDetailController> {

	/**
	 * the election service.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Aug 42, 2012
	 */
	private ElectionService electionService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WomBReferendumDetailController#handleGetReferendumDetail(javax.servlet.http.HttpServletRequest, long, org.springframework.ui.ModelMap, WhatsOnMyBallotForm)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 16, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetReferendumDetail() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String votingRegionState = "VS";
		final String votingRegionName = null;
		final long referendumVipId = 8928l;
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
		final VipReferendumDetail referendumDetail = createMock("ReferendumDetail", VipReferendumDetail.class);
		EasyMock.expect(getElectionService().findReferendumDetail(votingRegionState, votingRegionName, referendumVipId)).andReturn(
				referendumDetail);
		addAttributeToModelMap(model, "referendumDetail", referendumDetail);
		replayAll();

		final String actualResponse = getBaseController().handleGetReferendumDetail(request, referendumVipId, model,
				whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final WomBReferendumDetailController createBaseController() {
		final WomBReferendumDetailController whatsOnMyBallotDetailController = new WomBReferendumDetailController();
		whatsOnMyBallotDetailController.setElectionService(getElectionService());
		return whatsOnMyBallotDetailController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return WomBReferendumDetailController.DEFAULT_CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return WomBReferendumDetailController.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return WomBReferendumDetailController.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return WomBReferendumDetailController.DEFAULT_SECTION_NAME;
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
	 * @version Aug 24, 2012
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
	 * @version Aug 24, 2012
	 */
	private void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}
}
