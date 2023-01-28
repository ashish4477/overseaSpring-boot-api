/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn;
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
import com.bearcode.ovf.service.SvrPropertiesService;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link BaseControllerCheck} test for
 * {@link WhatsOnMyBallotPartisanPartyController}.
 * 
 * @author Ian Brown
 * @since May 10, 2013
 * @version Aug 1, 2013
 */
public final class WhatsOnMyBallotPartisanPartyControllerTest extends
        BaseControllerCheck<WhatsOnMyBallotPartisanPartyController> {

	/**
	 * the election service to use.
	 * 
	 * @author Ian Brown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private ElectionService electionService;

	/**
	 * the SVR properties service.
	 * 
	 * @author Ian Brown
	 * @since May 13, 2013
	 * @version May 13, 2013
	 */
	private SvrPropertiesService svrPropertiesService;

	/**
	 * the voting precinct service to use.
	 * 
	 * @author Ian Brown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private VotingPrecinctService votingPrecinctService;
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotPartisanPartyController#handleGetPartisanParty(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where no address is provided.
	 * 
	 * @author Ian Brown
	 * @throws Exception if there is a problem getting the partisan party.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandleGetPartisanParty_noAddress() throws Exception {
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

		final String actualResponse = getBaseController().handleGetPartisanParty(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to what's on my ballot?",
				WhatsOnMyBallotPartisanPartyController.REDIRECT_WHATS_ON_MY_BALLOT, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotPartisanPartyController#handleGetPartisanParty(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is no form.
	 * 
	 * @author Ian Brown
	 * @throws Exception if there is a problem getting the partisan party.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandleGetPartisanParty_noForm() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		final WhatsOnMyBallotForm whatsOnMyBallot = null;
		replayAll();

		final String actualResponse = getBaseController().handleGetPartisanParty(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to the candidate finder", WhatsOnMyBallotPartisanPartyController.REDIRECT_CANDIDATE_FINDER,
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotPartisanPartyController#handleGetPartisanParty(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)} for the case where there are no partisan parties.
	 * 
	 * @author Ian Brown
	 * @throws Exception if there is a problem getting the partisan party.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandleGetPartisanParty_noPartisanParties() throws Exception {
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
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(validAddress);
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
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final List<VipContest> partisanContests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findPartisanContests(contests)).andReturn(partisanContests);
		replayAll();

		final String actualResponse = getBaseController().handleGetPartisanParty(request, model, whatsOnMyBallot);

		assertEquals("The response is a redirect to what's on my ballot? list",
				WhatsOnMyBallotPartisanPartyController.REDIRECT_WHATS_ON_MY_BALLOT_LIST, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotPartisanPartyController#handleGetPartisanParty(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}.
	 * 
	 * @author Ian Brown
	 * @throws Exception if there is a problem getting the partisan party.
	 * @since May 10, 2013
	 * @version May 13, 2013
	 */
	@Test
	public final void testHandleGetPartisanParty_partisanParties() throws Exception {
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
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(whatsOnMyBallot.getVoterType()).andReturn(voterType);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(validAddress);
		final String partisanParty1 = "Partisan 1";
		final String partisanParty2 = "Partisan 2";
		final List<VipContest> contests = new LinkedList<VipContest>();
		long vipId = 1l;
		final VipContest president = createPresidentContest(stateAbbreviation, votingRegionName, vipId, partisanParty1);
		++vipId;
		contests.add(president);
		addAttributeToModelMap(model, EasyMock.eq("president"), EasyMock.eq(Arrays.asList(president)));
		final VipContest senate = createSenateContest(stateAbbreviation, votingRegionName, vipId, partisanParty2);
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
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		EasyMock.expect(getSvrPropertiesService().findProperty(stateAbbreviation, votingRegionName, PartisanPartyAddOn.NO_PARTY_PROPERTY)).andReturn(null).anyTimes();
		final List<VipContest> partisanContests = Arrays.asList(president, senate);
		EasyMock.expect(getElectionService().findPartisanContests(contests)).andReturn(partisanContests);
		addAttributeToModelMap(model, EasyMock.eq("partisanParties"), EasyMock.eq(Arrays.asList(partisanParty1, partisanParty2, PartisanPartyAddOn.DEFAULT_NO_PARTY_NAME)));
		replayAll();

		final String actualResponse = getBaseController().handleGetPartisanParty(request, model, whatsOnMyBallot);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.WhatsOnMyBallotPartisanPartyController#handlePostPartisanParty(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author Ian Brown
	 * @throws Exception if there is a problem reposting the get partisan party request.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Test
	public final void testHandlePostPartisanParty() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false);
		replayAll();

		final String actualResponse = getBaseController().handlePostPartisanParty(request, model, whatsOnMyBallot, errors);

		assertEquals("The response is a redirect to the what's on my ballot? list",
				WhatsOnMyBallotController.REDIRECT_WHATS_ON_MY_BALLOT_LIST, actualResponse);
		assertSame("The form is added to the session", whatsOnMyBallot, request.getSession().getAttribute("whatsOnMyBallot"));
		verifyAll();
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
	 * @param partisanParty the partisan party for the office or <code>null</code> if the contest is not partisan.
	 * @return the contest.
	 * @throws Exception
	 *             if there is a problem retrieving the candidate bio.
	 * @since May 10, 2013
	 * @version Aug 1, 2013
	 */
	private VipContest createContestForOffice(final String stateAbbreviation, final String votingRegionName, final String office,
			final long vipId, final String partisanParty) throws Exception {
		final String mockName = office.replace(' ', '_').replace('.', '_').replace('&', '_');
		final VipContest contest = createMock(mockName, VipContest.class);
		EasyMock.expect(contest.getType()).andReturn(office.toLowerCase()).anyTimes();
		EasyMock.expect(contest.getOffice()).andReturn(office).anyTimes();
		EasyMock.expect(contest.getVipId()).andReturn(vipId).anyTimes();
		EasyMock.expect(contest.getPartisanParty()).andReturn(partisanParty).anyTimes();
		EasyMock.expect(contest.isPartisan()).andReturn(partisanParty != null).anyTimes();
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
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private VipContest createPresidentContest(final String stateAbbreviation, final String votingRegionName, final long vipId, final String partisanParty)
			throws Exception {
		return createContestForOffice(stateAbbreviation, votingRegionName, StandardContest.PRESIDENT.getOffice()[0], vipId, partisanParty);
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
	 * @param partisanParty the partisan party for the office.
	 * @return the representative office.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private VipContest createRepresentativeContest(final String stateAbbreviation, final String votingRegionName, final long vipId, final String partisanParty)
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
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private VipContest createSenateContest(final String stateAbbreviation, final String votingRegionName, final long vipId, final String partisanParty)
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
	 * @param statePartisanParty the partisan party for the state office.
	 * @param localPartisanParty the partisan party for the local office.
	 * @return the state and local offices.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private List<VipContest> createStateAndLocalOfficeContests(final String stateAbbreviation, final String votingRegionName,
			final long vipId, final String statePartisanParty, final String localPartisanParty) throws Exception {
		final List<VipContest> stateAndLocalContests = new LinkedList<VipContest>();
		stateAndLocalContests.add(createContestForOffice(stateAbbreviation, votingRegionName, "State Official", vipId, statePartisanParty));
		stateAndLocalContests.add(createContestForOffice(stateAbbreviation, votingRegionName, "Local Official", vipId + 1l, localPartisanParty));
		return stateAndLocalContests;
	}

	/**
	 * Gets the election service.
	 * 
	 * @author Ian Brown
	 * @return the election service.
	 * 
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the SVR properties service.
	 * @author Ian Brown
	 * @return the SVR properties service.
	 * @since May 13, 2013
	 * @version May 13, 2013
	 */
    private SvrPropertiesService getSvrPropertiesService() {
	    return svrPropertiesService;
    }

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author Ian Brown
	 * @return the votingPrecinct service.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Sets the election service.
	 * 
	 * @author Ian Brown
	 * @param electionService
	 *            the election service to set.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Sets the SVR properties service.
	 * @author Ian Brown
	 * @param svrPropertiesService the SVR properties service to set
	 * @since May 13, 2013
	 * @version May 13, 2013
	 */
    private void setSvrPropertiesService(final SvrPropertiesService svrPropertiesService) {
	    this.svrPropertiesService = svrPropertiesService;
    }

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author Ian Brown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private void setVotingPrecinctService(
	        final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/** @{@inheritDoc */
	@Override
	protected final WhatsOnMyBallotPartisanPartyController createBaseController() {
		final WhatsOnMyBallotPartisanPartyController whatsOnMyBallotPartisanPartyController = new WhatsOnMyBallotPartisanPartyController();
		whatsOnMyBallotPartisanPartyController
		        .setVotingPrecinctService(getVotingPrecinctService());
		whatsOnMyBallotPartisanPartyController
		        .setElectionService(getElectionService());
		whatsOnMyBallotPartisanPartyController.setSvrPropertiesService(getSvrPropertiesService());
		return whatsOnMyBallotPartisanPartyController;
	}

	/** @{inheritDoc */
	@Override
	protected final String getExpectedContentBlock() {
		return WhatsOnMyBallotPartisanPartyController.DEFAULT_CONTENT_BLOCK;
	}

	/** @{inheritDoc */
	@Override
	protected final String getExpectedPageTitle() {
		return WhatsOnMyBallotPartisanPartyController.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return WhatsOnMyBallotPartisanPartyController.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return WhatsOnMyBallotPartisanPartyController.DEFAULT_SECTION_NAME;
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
		setSvrPropertiesService(createMock("SvrPropertiesService", SvrPropertiesService.class));
	}

	/** @{inheritDoc */
	@Override
	protected final void tearDownForBaseController() {
		setSvrPropertiesService(null);
		setElectionService(null);
		setVotingPrecinctService(null);
	}
}
