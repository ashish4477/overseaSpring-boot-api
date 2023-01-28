/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipContest;

/**
 * Extended {@link WhatsOnMyBallotControllerExam} integration test for {@link WhatsOnMyBallotListController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 14, 2012
 * @version May 10, 2013
 */
public final class WhatsOnMyBallotListControllerIntegration extends WhatsOnMyBallotControllerExam<WhatsOnMyBallotListController> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the voter's information has not been provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 14, 2012
	 * @version Aug 20, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetList_noForm() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WhatsOnMyBallotList.htm");

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is a redirect to the candidate finder", WhatsOnMyBallotListController.REDIRECT_CANDIDATE_FINDER,
				actualModelAndView.getViewName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for a voter who is not planning to come home.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 14, 2012
	 * @version Aug 20, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetList_overseasIndefinitely() throws Exception {
		final String votingRegionState = "VA";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WhatsOnMyBallotList.htm");
		final WhatsOnMyBallotForm whatsOnMyBallot = new WhatsOnMyBallotForm();
		whatsOnMyBallot.setVoterType(VoterType.OVERSEAS_VOTER);
		whatsOnMyBallot.setVotingState(getStateService().findByAbbreviation(votingRegionState));
		final UserAddress address = new UserAddress();
		address.setStreet1("2 E Guinevere Dr SE");
		address.setCity("Annandale");
		address.setState(votingRegionState);
		address.setZip("22003");
		whatsOnMyBallot.setAddress(address);
		whatsOnMyBallot.setPartisanParty(null);
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNull("There is no presidential contest", actualModel.get("president"));
		assertNull("There is no U.S. Senate contest", actualModel.get("senator"));
		assertNull("There is no U.S. House of Representatives contest", actualModel.get("representative"));
		final List<VipContest> actualOffices = (List<VipContest>) actualModel.get("offices");
		assertNull("There is no list of state and local office contests", actualOffices);
		final List<VipContest> actualReferendums = (List<VipContest>) actualModel.get("referendums");
		assertNull("There is no list of referendums", actualReferendums);
		final List<VipContest> actualCustomBallots = (List<VipContest>) actualModel.get("customBallots");
		assertNull("There is no list of custom ballots", actualCustomBallots);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for a voter who is planning to come home.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	@SuppressWarnings("unchecked")
	@Test
	@OVFDBUnitUseData
	public final void testHandleGetList_overseasTemporarily() throws Exception {
		final String votingRegionState = "VA";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/WhatsOnMyBallotList.htm");
		final WhatsOnMyBallotForm whatsOnMyBallot = new WhatsOnMyBallotForm();
		whatsOnMyBallot.setVoterType(VoterType.OVERSEAS_VOTER);
		whatsOnMyBallot.setVotingState(getStateService().findByAbbreviation(votingRegionState));
		final UserAddress address = new UserAddress();
		address.setStreet1("2 E Guinevere Dr SE");
		address.setCity("Annandale");
		address.setState(votingRegionState);
		address.setZip("22003");
		whatsOnMyBallot.setAddress(address);
		whatsOnMyBallot.setPartisanParty("Democrat");
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNull("There is no presidential contest", actualModel.get("president"));
		assertNull("There is no U.S. Senate contest", actualModel.get("senator"));
		assertNull("There is no U.S. House of Representatives contest", actualModel.get("representative"));
		final List<VipContest> actualContests = (List<VipContest>) actualModel.get("contests");
		int offices = 0;
		int referendums = 0;
		int customBallots = 0;
		for (final VipContest actualContest : actualContests) {
			final VipBallot ballot = actualContest.getBallot();
			if (ballot.getCandidates() != null && !ballot.getCandidates().isEmpty()) {
				++offices;
			} else if (ballot.getReferendum() != null) {
				++referendums;
			} else if (ballot.getCustomBallot() != null) {
				++customBallots;
			}
		}
		assertTrue("There is at least one state or local office contest", offices > 0);
		assertTrue("There is at least one referendum", referendums > 0);
		assertTrue("There is at least one custom ballot", customBallots > 0);
	}

	/** {@inheritDoc} */
	@Override
	protected final WhatsOnMyBallotListController createWhatsOnMyBallotController() {
		final WhatsOnMyBallotListController whatsOnMyBallotListController = applicationContext
				.getBean(WhatsOnMyBallotListController.class);
		return whatsOnMyBallotListController;
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
