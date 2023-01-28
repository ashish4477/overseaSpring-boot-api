/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.questionnaire.StandardContest;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipBallotCandidate;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipCustomBallot;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendum;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link BaseControllerCheck} test for {@link WhatsOnMyBallotListController}.
 * 
 * @author IanBrown
 * 
 * @since Aug 14, 2012
 * @version Aug 1, 2013
 */
public final class WhatsOnMyBallotListControllerTest extends BaseControllerCheck<WhatsOnMyBallotListController> {

	/**
	 * the election service;
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private ElectionService electionService;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the voter is military.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Oct 5, 2012
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandleGetList_military() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String stateAbbreviation = "SI";
		final String votingRegionName = null;
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState).anyTimes();
		EasyMock.expect(votingState.getAbbr()).andReturn(stateAbbreviation).atLeastOnce();
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, null)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, null)).andReturn(true);
		final VoterType voterType = VoterType.DOMESTIC_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(validAddress);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment);
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct);
		final VipElectoralDistrict precinctElectoralDistrict = createMock("PrecinctElectoralDistrict", VipElectoralDistrict.class);
		final List<VipElectoralDistrict> precinctElectoralDistricts = Arrays.asList(precinctElectoralDistrict);
		EasyMock.expect(precinct.getElectoralDistricts()).andReturn(precinctElectoralDistricts);
		final VipPrecinctSplit precinctSplit = createMock("PrecinctSplit", VipPrecinctSplit.class);
		EasyMock.expect(streetSegment.getPrecinctSplit()).andReturn(precinctSplit);
		final VipElectoralDistrict precinctSplitElectoralDistrict = createMock("PrecinctSplitElectoralDistrict",
				VipElectoralDistrict.class);
		final List<VipElectoralDistrict> precinctSplitElectoralDistricts = Arrays.asList(precinctElectoralDistrict,
				precinctSplitElectoralDistrict);
		EasyMock.expect(precinctSplit.getElectoralDistricts()).andReturn(precinctSplitElectoralDistricts);
		addAttributeToModelMap(model, EasyMock.eq("electoralDistricts"), EasyMock.anyObject());
		EasyMock.expect(whatsOnMyBallot.getPartisanParty()).andReturn(null).anyTimes();
		final List<VipContest> contests = new LinkedList<VipContest>();
		long vipId = 1l;
		final VipContest president = createPresidentContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(president);
		addAttributeToModelMap(model, EasyMock.eq("president"), EasyMock.eq(Arrays.asList(president)));
		final VipContest senate = createSenateContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(senate);
		addAttributeToModelMap(model, "senator", senate);
		final VipContest representative = createRepresentativeContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(representative);
		addAttributeToModelMap(model, EasyMock.eq("representative"), EasyMock.eq(Arrays.asList(representative)));
		final List<VipContest> stateAndLocalOffices = createStateAndLocalOfficeContests(stateAbbreviation, votingRegionName, vipId, null, null);
		vipId += stateAndLocalOffices.size();
		contests.addAll(stateAndLocalOffices);
		final List<VipContest> referendums = createReferendumContests(stateAbbreviation, votingRegionName, vipId);
		vipId += referendums.size();
		contests.addAll(referendums);
		final List<VipContest> customBallots = createCustomBallotContests(vipId);
		vipId += customBallots.size();
		contests.addAll(customBallots);
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final List<String> contestOrder = Arrays.asList("OFFICE: .*STATE.*", "REFERENDUM: .*", "OFFICE: .*LOCAL.*", "CUSTOM: .*");
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, null)).andReturn(contestOrder);
		final List<VipContest> orderedContests = new LinkedList<VipContest>();
		orderedContests.add(stateAndLocalOffices.get(0));
		orderedContests.addAll(referendums);
		orderedContests.add(stateAndLocalOffices.get(1));
		orderedContests.addAll(customBallots);
		addAttributeToModelMap(model, EasyMock.eq("contests"), EasyMock.eq(contests));
		addAttributeToModelMap(model, EasyMock.eq("hasDetails"), EasyMock.anyObject());
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the form has no address.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetList_noAddress() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState).anyTimes();
		final String stateIdentification = "ST";
		EasyMock.expect(votingState.getAbbr()).andReturn(stateIdentification);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(stateIdentification, null)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateIdentification, null)).andReturn(true);
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(null);
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to what's on my ballot?",
				WhatsOnMyBallotListController.REDIRECT_WHATS_ON_MY_BALLOT, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is no form.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version Aug 20, 2012
	 */
	@Test
	public final void testHandleGetList_noForm() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = null;
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to the candidate finder", WhatsOnMyBallotListController.REDIRECT_CANDIDATE_FINDER,
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the address is not valid.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetList_notValid() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState).anyTimes();
		final String stateIdentification = "ST";
		EasyMock.expect(votingState.getAbbr()).andReturn(stateIdentification);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(stateIdentification, null)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateIdentification, null)).andReturn(true);
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(null);
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to what's on my ballot?",
				WhatsOnMyBallotListController.REDIRECT_WHATS_ON_MY_BALLOT, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the form has no voter type.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetList_noVoterType() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState).anyTimes();
		final String stateIdentification = "ST";
		EasyMock.expect(votingState.getAbbr()).andReturn(stateIdentification);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(stateIdentification, null)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateIdentification, null)).andReturn(true);
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(null);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(null);
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to what's on my ballot?",
				WhatsOnMyBallotListController.REDIRECT_WHATS_ON_MY_BALLOT, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the form has no voting state.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetList_noVotingState() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(null);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to the candidate finder", WhatsOnMyBallotListController.REDIRECT_CANDIDATE_FINDER,
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the voter is overseas indefinitely.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandleGetList_overseasIndefinitely() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState).anyTimes();
		final String stateAbbreviation = "ST";
		final String votingRegionName = null;
		EasyMock.expect(votingState.getAbbr()).andReturn(stateAbbreviation);
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, null)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, null)).andReturn(true);
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(validAddress);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment);
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct);
		final VipElectoralDistrict precinctElectoralDistrict = createMock("PrecinctElectoralDistrict", VipElectoralDistrict.class);
		final List<VipElectoralDistrict> precinctElectoralDistricts = Arrays.asList(precinctElectoralDistrict);
		EasyMock.expect(precinct.getElectoralDistricts()).andReturn(precinctElectoralDistricts);
		EasyMock.expect(streetSegment.getPrecinctSplit()).andReturn(null);
		addAttributeToModelMap(model, EasyMock.eq("electoralDistricts"), EasyMock.anyObject());
		EasyMock.expect(whatsOnMyBallot.getPartisanParty()).andReturn(null).anyTimes();
		final List<VipContest> contests = new LinkedList<VipContest>();
		long vipId = 1l;
		final VipContest president = createPresidentContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(president);
		addAttributeToModelMap(model, EasyMock.eq("president"), EasyMock.eq(Arrays.asList(president)));
		final VipContest senate = createSenateContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(senate);
		addAttributeToModelMap(model, "senator", senate);
		final VipContest representative = createRepresentativeContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(representative);
		addAttributeToModelMap(model, EasyMock.eq("representative"), EasyMock.eq(Arrays.asList(representative)));
		final List<VipContest> stateAndLocalOffices = createStateAndLocalOfficeContests(stateAbbreviation, votingRegionName, vipId, null, null);
		vipId += stateAndLocalOffices.size();
		contests.addAll(stateAndLocalOffices);
		final List<VipContest> referendums = createReferendumContests(stateAbbreviation, votingRegionName, vipId);
		vipId += referendums.size();
		contests.addAll(referendums);
		final List<VipContest> customBallots = createCustomBallotContests(vipId);
		vipId += customBallots.size();
		contests.addAll(customBallots);
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		addAttributeToModelMap(model, EasyMock.eq("hasDetails"), EasyMock.anyObject());
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the voter is overseas temporarily.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandleGetList_overseasTemporarily() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String stateAbbreviation = "SI";
		final String votingRegionName = null;
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState).anyTimes();
		EasyMock.expect(votingState.getAbbr()).andReturn(stateAbbreviation).atLeastOnce();
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, null)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, null)).andReturn(true);
		final VoterType voterType = VoterType.DOMESTIC_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(validAddress);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment);
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct);
		final VipElectoralDistrict precinctElectoralDistrict = createMock("PrecinctElectoralDistrict", VipElectoralDistrict.class);
		final List<VipElectoralDistrict> precinctElectoralDistricts = Arrays.asList(precinctElectoralDistrict);
		EasyMock.expect(precinct.getElectoralDistricts()).andReturn(precinctElectoralDistricts);
		final VipPrecinctSplit precinctSplit = createMock("PrecinctSplit", VipPrecinctSplit.class);
		EasyMock.expect(streetSegment.getPrecinctSplit()).andReturn(precinctSplit);
		final VipElectoralDistrict precinctSplitElectoralDistrict = createMock("PrecinctSplitElectoralDistrict",
				VipElectoralDistrict.class);
		final List<VipElectoralDistrict> precinctSplitElectoralDistricts = Arrays.asList(precinctElectoralDistrict,
				precinctSplitElectoralDistrict);
		EasyMock.expect(precinctSplit.getElectoralDistricts()).andReturn(precinctSplitElectoralDistricts);
		addAttributeToModelMap(model, EasyMock.eq("electoralDistricts"), EasyMock.anyObject());
		final String partisanParty = "Partisan Party";
		EasyMock.expect(whatsOnMyBallot.getPartisanParty()).andReturn(partisanParty).anyTimes();
		final List<VipContest> contests = new LinkedList<VipContest>();
		long vipId = 1l;
		final VipContest president = createPresidentContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(president);
		addAttributeToModelMap(model, EasyMock.eq("president"), EasyMock.eq(Arrays.asList(president)));
		final VipContest senate = createSenateContest(stateAbbreviation, votingRegionName, vipId, partisanParty);
		++vipId;
		contests.add(senate);
		addAttributeToModelMap(model, "senator", senate);
		final VipContest representative = createRepresentativeContest(stateAbbreviation, votingRegionName, vipId, null);
		++vipId;
		contests.add(representative);
		addAttributeToModelMap(model, EasyMock.eq("representative"), EasyMock.eq(Arrays.asList(representative)));
		final List<VipContest> stateAndLocalOffices = createStateAndLocalOfficeContests(stateAbbreviation, votingRegionName, vipId, null, null);
		vipId += stateAndLocalOffices.size();
		contests.addAll(stateAndLocalOffices);
		final List<VipContest> referendums = createReferendumContests(stateAbbreviation, votingRegionName, vipId);
		vipId += referendums.size();
		contests.addAll(referendums);
		final List<VipContest> customBallots = createCustomBallotContests(vipId);
		vipId += customBallots.size();
		contests.addAll(customBallots);
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, null)).andReturn(null);
		addAttributeToModelMap(model, EasyMock.eq("contests"), EasyMock.eq(contests));
		addAttributeToModelMap(model, EasyMock.eq("hasDetails"), EasyMock.anyObject());
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#handleGetList(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the state is not ready.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up or handling the list.
	 * @since Aug 14, 2012
	 * @version Oct 12, 2012
	 */
	@Test
	public final void testHandleGetList_stateNotReady() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState).anyTimes();
		final String stateIdentification = "ST";
		EasyMock.expect(votingState.getAbbr()).andReturn(stateIdentification).atLeastOnce();
		EasyMock.expect(whatsOnMyBallot.getRegion()).andReturn(null);
		EasyMock.expect(getVotingPrecinctService().isReady(stateIdentification, null)).andReturn(false);
		replayAll();

		final String actualResponse = getBaseController().handleGetList(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to the candidate finder", WhatsOnMyBallotListController.REDIRECT_CANDIDATE_FINDER,
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#whatsOnMyBallot(javax.servlet.http.HttpServletRequest)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	@Test
	public final void testWhatsOnMyBallot() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		request.getSession().setAttribute("whatsOnMyBallot", whatsOnMyBallot);
		replayAll();

		final WhatsOnMyBallotForm actualWhatsOnMyBallot = getBaseController().whatsOnMyBallot(request);

		assertSame("The what's on my ballot? form is returned", whatsOnMyBallot, actualWhatsOnMyBallot);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotListController#whatsOnMyBallot(javax.servlet.http.HttpServletRequest)} for
	 * the case where there is no what's on my ballot? form.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	@Test
	public final void testWhatsOnMyBallot_noForm() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

		final WhatsOnMyBallotForm actualWhatsOnMyBallot = getBaseController().whatsOnMyBallot(request);

		assertNull("No what's on my ballot? form is returned", actualWhatsOnMyBallot);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final WhatsOnMyBallotListController createBaseController() {
		final WhatsOnMyBallotListController whatsOnMyBallotListController = new WhatsOnMyBallotListController();
		whatsOnMyBallotListController.setVotingPrecinctService(getVotingPrecinctService());
		whatsOnMyBallotListController.setElectionService(getElectionService());
		return whatsOnMyBallotListController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return WhatsOnMyBallotListController.DEFAULT_CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return WhatsOnMyBallotListController.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return WhatsOnMyBallotListController.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return WhatsOnMyBallotListController.DEFAULT_SECTION_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setVotingPrecinctService(createMock("VotingPrecinctService", VotingPrecinctService.class));
		setElectionService(createMock("ElectionService", ElectionService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setElectionService(null);
		setVotingPrecinctService(null);
	}

	/**
	 * Creates a contest for the specified office.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param office
	 *            the office.
	 * @param vipId
	 *            the VIP identifier.
	 * @param partisanParty the partisan party for the contest.
	 * @return the contest.
	 * @throws Exception
	 *             if there is a problem retrieving the candidate bio.
	 * @since Aug 14, 2012
	 * @version Aug 1, 2013
	 */
	private VipContest createContestForOffice(final String stateAbbreviation, final String votingRegionName, final String office,
			final long vipId, String partisanParty) throws Exception {
		final String mockName = office.replace(' ', '_').replace('.', '_').replace('&', '_');
		final VipContest contest = createMock(mockName, VipContest.class);
		EasyMock.expect(contest.getType()).andReturn(office.toLowerCase()).anyTimes();
		EasyMock.expect(contest.getOffice()).andReturn(office).anyTimes();
		EasyMock.expect(contest.getVipId()).andReturn(vipId).anyTimes();
		EasyMock.expect(contest.isPartisan()).andReturn(partisanParty != null).anyTimes();
		EasyMock.expect(contest.getPartisanParty()).andReturn(partisanParty).anyTimes();
		final VipBallot ballot = createMock(mockName + "Ballot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot).anyTimes();
		final VipBallotCandidate ballotCandidate = createMock(mockName + "BallotCandidate", VipBallotCandidate.class);
		final List<VipBallotCandidate> candidates = Arrays.asList(ballotCandidate);
		EasyMock.expect(ballot.getCandidates()).andReturn(candidates).anyTimes();
		EasyMock.expect(ballot.getReferendum()).andReturn(null).anyTimes();
		EasyMock.expect(ballot.getCustomBallot()).andReturn(null).anyTimes();
		final VipCandidate candidate = createMock(mockName + "Candidate", VipCandidate.class);
		EasyMock.expect(ballotCandidate.getCandidate()).andReturn(candidate).anyTimes();
		final long candidateVipId = vipId * 1000l;
		EasyMock.expect(candidate.getVipId()).andReturn(candidateVipId).anyTimes();
		final VipCandidateBio candidateBio = createMock(mockName + "CandidateBio", VipCandidateBio.class);
		EasyMock.expect(getElectionService().findCandidateBio(stateAbbreviation, votingRegionName, candidateVipId))
				.andReturn(candidateBio).anyTimes();
		EasyMock.expect(candidateBio.isEmpty()).andReturn(vipId % 2 == 0).anyTimes();
		return contest;
	}

	/**
	 * Creates contests for custom ballots.
	 * 
	 * @author IanBrown
	 * @param vipId
	 *            the VIP identifier.
	 * @return the custom ballot contests.
	 * @since Aug 14, 2012
	 * @version May 13, 2013
	 */
	private List<VipContest> createCustomBallotContests(final long vipId) {
		final List<VipContest> customBallotContests = new LinkedList<VipContest>();
		final VipContest customBallotContest = createMock("CustomBallotContest", VipContest.class);
		EasyMock.expect(customBallotContest.getType()).andReturn("custom ballot").anyTimes();
		EasyMock.expect(customBallotContest.getOffice()).andReturn(null).anyTimes();
		EasyMock.expect(customBallotContest.getVipId()).andReturn(vipId).anyTimes();
		EasyMock.expect(customBallotContest.isPartisan()).andReturn(false).anyTimes();
		final VipBallot ballot = createMock("CustomBallotBallot", VipBallot.class);
		EasyMock.expect(customBallotContest.getBallot()).andReturn(ballot).anyTimes();
		EasyMock.expect(ballot.getCandidates()).andReturn(null).anyTimes();
		final VipCustomBallot customBallot = createMock("CustomBallot", VipCustomBallot.class);
		EasyMock.expect(ballot.getCustomBallot()).andReturn(customBallot).anyTimes();
		EasyMock.expect(ballot.getReferendum()).andReturn(null).anyTimes();
		final VipElectoralDistrict electoralDistrict = createMock("CustomBallotElectoralDistrict", VipElectoralDistrict.class);
		EasyMock.expect(customBallotContest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		EasyMock.expect(electoralDistrict.getType()).andReturn("Custom Ballot District").anyTimes();
		customBallotContests.add(customBallotContest);
		return customBallotContests;
	}

	/**
	 * Creates a contest for president.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param vipId
	 *            the VIP identifier.
	 * @param partisanParty the partisan party for the contest.
	 * @return the contest for president.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	private VipContest createPresidentContest(final String stateAbbreviation, final String votingRegionName, final long vipId, String partisanParty)
			throws Exception {
		return createContestForOffice(stateAbbreviation, votingRegionName, StandardContest.PRESIDENT.getOffice()[0], vipId, partisanParty);
	}

	/**
	 * Creates contests for referendums.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param vipId
	 *            the VIP identifier.
	 * @return the referendum contests.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	private List<VipContest> createReferendumContests(final String stateAbbreviation, final String votingRegionName,
			final long vipId) {
		final List<VipContest> referendumContests = new LinkedList<VipContest>();
		final VipContest referendumContest = createMock("ReferendumContest", VipContest.class);
		EasyMock.expect(referendumContest.getType()).andReturn("referendum").anyTimes();
		EasyMock.expect(referendumContest.getOffice()).andReturn(null).anyTimes();
		EasyMock.expect(referendumContest.getVipId()).andReturn(vipId).anyTimes();
		EasyMock.expect(referendumContest.isPartisan()).andReturn(false).anyTimes();
		final VipBallot ballot = createMock("ReferendumBallot", VipBallot.class);
		EasyMock.expect(referendumContest.getBallot()).andReturn(ballot).anyTimes();
		EasyMock.expect(ballot.getCandidates()).andReturn(null).anyTimes();
		EasyMock.expect(ballot.getCustomBallot()).andReturn(null).anyTimes();
		final VipReferendum referendum = createMock("Referendum", VipReferendum.class);
		EasyMock.expect(ballot.getReferendum()).andReturn(referendum).anyTimes();
		final VipElectoralDistrict electoralDistrict = createMock("ReferendumElectoralDistrict", VipElectoralDistrict.class);
		EasyMock.expect(referendumContest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		EasyMock.expect(electoralDistrict.getType()).andReturn("Referendum District").anyTimes();
		referendumContests.add(referendumContest);
		final long referendumVipId = vipId * 1000l;
		EasyMock.expect(referendum.getVipId()).andReturn(referendumVipId).anyTimes();
		final VipReferendumDetail referendumDetail = createMock("ReferendumDetail", VipReferendumDetail.class);
		EasyMock.expect(getElectionService().findReferendumDetail(stateAbbreviation, votingRegionName, referendumVipId))
				.andReturn(referendumDetail).anyTimes();
		EasyMock.expect(referendumDetail.isEmpty()).andReturn(vipId % 2 == 0).anyTimes();
		return referendumContests;
	}

	/**
	 * Creates a contest for the house of representatives.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param vipId
	 *            the VIP identifier.
	 * @param partisanParty the partisan party for the contest.
	 * @return the representative office.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	private VipContest createRepresentativeContest(final String stateAbbreviation, final String votingRegionName, final long vipId, String partisanParty)
			throws Exception {
		return createContestForOffice(stateAbbreviation, votingRegionName, StandardContest.REPRESENTATIVE.getOffice()[0], vipId, partisanParty);
	}

	/**
	 * Creates a contest for the senate.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param vipId
	 *            the VIP identifier.
	 * @param partisanParty the partisan party for the contest.
	 * @return the senate contest.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	private VipContest createSenateContest(final String stateAbbreviation, final String votingRegionName, final long vipId, String partisanParty)
			throws Exception {
		return createContestForOffice(stateAbbreviation, votingRegionName, StandardContest.SENATOR.getOffice()[0], vipId, partisanParty);
	}

	/**
	 * Creates contests for state and local officials.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param vipId
	 *            the VIP identifier.
	 * @param statePartisanParty the state partisan party.
	 * @param localPartisanParty the local partisan party.
	 * @return the state and local offices.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 14, 2012
	 * @version Oct 24, 2012
	 */
	private List<VipContest> createStateAndLocalOfficeContests(final String stateAbbreviation, final String votingRegionName,
			final long vipId, String statePartisanParty, String localPartisanParty) throws Exception {
		final List<VipContest> stateAndLocalContests = new LinkedList<VipContest>();
		stateAndLocalContests.add(createContestForOffice(stateAbbreviation, votingRegionName, "State Official", vipId, statePartisanParty));
		stateAndLocalContests.add(createContestForOffice(stateAbbreviation, votingRegionName, "Local Official", vipId + 1l, localPartisanParty));
		return stateAndLocalContests;
	}

	/**
	 * Gets the election service.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Sets the election service.
	 * 
	 * @author IanBrown
	 * @param electionService
	 *            the election service to set.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}
}
