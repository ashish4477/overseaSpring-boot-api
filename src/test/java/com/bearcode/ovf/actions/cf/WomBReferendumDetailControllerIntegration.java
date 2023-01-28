/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipSource;

/**
 * Extended {@link WhatsOnMyBallotControllerExam} integration test for {@link WomBReferendumDetailController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 16, 2012
 * @version Aug 24, 2012
 */
public final class WomBReferendumDetailControllerIntegration extends
		WhatsOnMyBallotControllerExam<WomBReferendumDetailController> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WomBReferendumDetailController#handleGetReferendumDetail(javax.servlet.http.HttpServletRequest, long, org.springframework.ui.ModelMap, WhatsOnMyBallotForm)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 16, 2012
	 * @version Aug 24, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetReferendumDetail() throws Exception {
		final String votingRegionState = "VA";
		final VipSource source = getVipService().findLatestSource();
		final long referendumVipId = 90011l;
		final VipReferendumDetail referendumDetail = getVipService().findReferendumDetailBySourceAndVipId(source, referendumVipId);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WomBReferendumDetail.htm");
		request.setParameter("referendumVipId", Long.toString(referendumVipId));
		final WhatsOnMyBallotForm whatsOnMyBallot = new WhatsOnMyBallotForm();
		whatsOnMyBallot.setVoterType(VoterType.OVERSEAS_VOTER);
		whatsOnMyBallot.setVotingState(getStateService().findByAbbreviation(votingRegionState));
		final UserAddress address = new UserAddress();
		address.setStreet1("2 E Guinevere Dr SE");
		address.setCity("Annandale");
		address.setState(votingRegionState);
		address.setZip("22003");
		whatsOnMyBallot.setAddress(address);
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("There is a model and view", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertSame("The referendum detail is in the model", referendumDetail, actualModel.get("referendumDetail"));
	}

	/** {@inheritDoc} */
	@Override
	protected final WomBReferendumDetailController createWhatsOnMyBallotController() {
		final WomBReferendumDetailController whatsOnMyBallotDetailController = applicationContext
				.getBean(WomBReferendumDetailController.class);
		return whatsOnMyBallotDetailController;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForWhatsOnMyBallotController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForWhatsOnMyBallotController() {
	}
}
