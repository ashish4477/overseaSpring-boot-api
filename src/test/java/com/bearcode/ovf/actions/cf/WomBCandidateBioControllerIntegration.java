/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipSource;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.*;

/**
 * Extended {@link WhatsOnMyBallotControllerExam} integration test for {@link WomBCandidateBioController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 16, 2012
 * @version Aug 24, 2012
 */
public final class WomBCandidateBioControllerIntegration extends
		WhatsOnMyBallotControllerExam<WomBCandidateBioController> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WomBCandidateBioController#handleGetCandidateBio(javax.servlet.http.HttpServletRequest, long, org.springframework.ui.ModelMap, WhatsOnMyBallotForm)}
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
	public final void testHandleGetCandidateBio() throws Exception {
		final String votingRegionState = "VA";
		final long candidateVipId = 90001l;
		final VipSource source = getVipService().findLatestSource();
		final VipCandidateBio candidateBio = getVipService().findCandidateBioBySourceAndVipId(source, 90001l);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WomBCandidateBio.htm");
		request.setParameter("candidateVipId", Long.toString(candidateVipId));
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
		assertSame("The candidate bio is in the model", candidateBio, actualModel.get("candidateBio"));
	}

	/** {@inheritDoc} */
	@Override
	protected final WomBCandidateBioController createWhatsOnMyBallotController() {
		final WomBCandidateBioController WomBCandidateBioController = applicationContext
				.getBean(WomBCandidateBioController.class);
		return WomBCandidateBioController;
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
