/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendum;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipSource;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link CandidateFinderCheck} test for {@link CandidateFinderVip}.
 * 
 * @author IanBrown
 * 
 * @since Jul 6, 2012
 * @version Oct 10, 2012
 */
public final class CandidateFinderVipTest extends CandidateFinderCheck<CandidateFinderVip> {

	/**
	 * the valid state.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private static final String VALID_STATE = "OH";

	/**
	 * the valid state name.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private static final String VALID_STATE_NAME = "Ohio";

	/**
	 * the valid voting region.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private static final String VALID_VOTING_REGION = "Adams County";

	/**
	 * a valid ZIP code.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String VALID_ZIP = "87212";

	/**
	 * map of lists of contests to return for each known electoral district.
	 * 
	 * @author IanBrown
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	private Map<VipElectoralDistrict, List<VipContest>> contestsForElectoralDistricts;

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private StateService stateService;

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	private VipService vipService;

	/** {@inheritDoc} */
	@Override
	protected final boolean areReferendumsSupported() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final void completeSetUp(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		// Nothing more to do.
	}

	/** {@inheritDoc} */
	@Override
	protected final CandidateFinderVip createCandidateFinder() {
		final CandidateFinderVip candidateFinder = new CandidateFinderVip();
		candidateFinder.setVipService(getVipService());
		candidateFinder.setStateService(getStateService());
		return candidateFinder;
	}

	/** {@inheritDoc} */
	@Override
	protected final ValidAddress createValidAddress(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment).anyTimes();
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct).anyTimes();
		final VipElectoralDistrict precinctElectoralDistrict = createMock("PrecinctElectoralDistrict", VipElectoralDistrict.class);
		final List<VipElectoralDistrict> precinctElectoralDistricts = Arrays.asList(precinctElectoralDistrict);
		EasyMock.expect(precinct.getElectoralDistricts()).andReturn(precinctElectoralDistricts).anyTimes();
		electoralDistricts.put("PrecinctElectoralDistrict", precinctElectoralDistrict);
		EasyMock.expect(precinctElectoralDistrict.getVipId()).andReturn(1l).anyTimes();
		final List<VipContest> precinctContests = new LinkedList<VipContest>();
		EasyMock.expect(getVipService().findContestsForElectoralDistrict(precinctElectoralDistrict)).andReturn(precinctContests)
				.anyTimes();
		getContestsForElectoralDistricts().put(precinctElectoralDistrict, precinctContests);
		final VipPrecinctSplit precinctSplit = createMock("PrecinctSplit", VipPrecinctSplit.class);
		EasyMock.expect(streetSegment.getPrecinctSplit()).andReturn(precinctSplit).anyTimes();
		final VipElectoralDistrict precinctSplitElectoralDistrict = createMock("PrecinctSplitElectoralDistrict",
				VipElectoralDistrict.class);
		final List<VipElectoralDistrict> precinctSplitElectoralDistricts = Arrays.asList(precinctSplitElectoralDistrict);
		EasyMock.expect(precinctSplit.getElectoralDistricts()).andReturn(precinctSplitElectoralDistricts).anyTimes();
		electoralDistricts.put("PrecinctSplitElectoralDistrict", precinctSplitElectoralDistrict);
		EasyMock.expect(precinctSplitElectoralDistrict.getVipId()).andReturn(2l).anyTimes();
		final List<VipContest> precinctSplitContests = new LinkedList<VipContest>();
		EasyMock.expect(getVipService().findContestsForElectoralDistrict(precinctSplitElectoralDistrict))
				.andReturn(precinctSplitContests).anyTimes();
		getContestsForElectoralDistricts().put(precinctSplitElectoralDistrict, precinctSplitContests);
		return validAddress;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidState() {
		return "NH";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidVotingRegion() {
		return "Invalid voting region";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidState() {
		return VALID_STATE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidVotingRegion() {
		return VALID_VOTING_REGION;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidZip() {
		return VALID_ZIP;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidZip4() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForCountyContest(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception {
		final VipElectoralDistrict countyElectoralDistrict = electoralDistricts.get("PrecinctElectoralDistrict");
		final VipContest countyContest = createMock("CountyContest", VipContest.class);
		getContestsForElectoralDistricts().get(countyElectoralDistrict).add(countyContest);
		return Arrays.asList(countyContest);
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForFederalContest(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception {
		final VipElectoralDistrict federalElectoralDistrict = electoralDistricts.get("PrecinctElectoralDistrict");
		final VipContest federalContest = createMock("FederalContest", VipContest.class);
		getContestsForElectoralDistricts().get(federalElectoralDistrict).add(federalContest);
		return Arrays.asList(federalContest);
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForLocalContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		final VipElectoralDistrict localElectoralDistrict = electoralDistricts.get("PrecinctSplitElectoralDistrict");
		final VipContest localContest = createMock("LocalContest", VipContest.class);
		getContestsForElectoralDistricts().get(localElectoralDistrict).add(localContest);
		return Arrays.asList(localContest);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpDataForNoContests(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		// Nothing to do.
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForOtherAddress(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		return new LinkedList<VipContest>();
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForStateContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		final VipElectoralDistrict stateElectoralDistrict = electoralDistricts.get("PrecinctElectoralDistrict");
		final VipContest stateContest = createMock("StateContest", VipContest.class);
		getContestsForElectoralDistricts().get(stateElectoralDistrict).add(stateContest);
		return Arrays.asList(stateContest);
	}

	/** {@inheritDoc} */
	@Override
	protected final VipCandidateBio setUpForCandidateBio() throws Exception {
		final long candidateVipId = 872l;
		final VipCandidateBio candidateBio = createMock("CandidateBio", VipCandidateBio.class);
		final VipCandidate candidate = createMock("Candidate", VipCandidate.class);
		EasyMock.expect(candidateBio.getCandidate()).andReturn(candidate).anyTimes();
		EasyMock.expect(candidate.getVipId()).andReturn(candidateVipId);
		final VipSource source = createMock("Source", VipSource.class);
		EasyMock.expect(getVipService().findLatestSource()).andReturn(source);
		EasyMock.expect(getVipService().findCandidateBioBySourceAndVipId(source, candidateVipId)).andReturn(candidateBio);
		return candidateBio;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForCandidateFinder() {
		setStateService(createMock("StateService", StateService.class));
		setVipService(createMock("VipService", VipService.class));
		setContestsForElectoralDistricts(new LinkedHashMap<VipElectoralDistrict, List<VipContest>>());
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForMissingCandidateBio(final long candidateVipId) {
		final VipSource source = createMock("Source", VipSource.class);
		EasyMock.expect(getVipService().findLatestSource()).andReturn(source);
		EasyMock.expect(getVipService().findCandidateBioBySourceAndVipId(source, candidateVipId)).andReturn(null);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForMissingReferendumDetail(final long referendumVipId) {
		final VipSource source = createMock("Source", VipSource.class);
		EasyMock.expect(getVipService().findLatestSource()).andReturn(source);
		EasyMock.expect(getVipService().findReferendumDetailBySourceAndVipId(source, referendumVipId)).andReturn(null);
	}

	/** {@inheritDoc} */
	@Override
	protected final VipReferendumDetail setUpForReferendumDetail() {
		final long referendumVipId = 872l;
		final VipReferendumDetail referendumDetail = createMock("ReferendumDetail", VipReferendumDetail.class);
		final VipReferendum referendum = createMock("Candidate", VipReferendum.class);
		EasyMock.expect(referendumDetail.getReferendum()).andReturn(referendum).anyTimes();
		EasyMock.expect(referendum.getVipId()).andReturn(referendumVipId);
		final VipSource source = createMock("Source", VipSource.class);
		EasyMock.expect(getVipService().findLatestSource()).andReturn(source);
		EasyMock.expect(getVipService().findReferendumDetailBySourceAndVipId(source, referendumVipId)).andReturn(referendumDetail);
		return referendumDetail;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForState() {
		getForStateOrVotingRegion().setStates(Arrays.asList(VALID_STATE));
		getForStateOrVotingRegion().setVotingRegionType("County");
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(VALID_STATE)).andReturn(state).anyTimes();
		EasyMock.expect(state.getName()).andReturn(VALID_STATE_NAME).anyTimes();
		final VipSource source = createMock("Source", VipSource.class);
		EasyMock.expect(getVipService().findLatestSource(VALID_STATE_NAME)).andReturn(source).anyTimes();
		final VipState vipState = createMock("VipState", VipState.class);
		EasyMock.expect(getVipService().findStateBySourceAndName(source, VALID_STATE_NAME)).andReturn(vipState).anyTimes();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForVotingRegion() {
		final Map<String, Collection<String>> votingRegions = new HashMap<String, Collection<String>>();
		votingRegions.put(VALID_STATE, Arrays.asList(VALID_VOTING_REGION));
		getForStateOrVotingRegion().setVotingRegions(votingRegions);
		getForStateOrVotingRegion().setVotingRegionType("County");
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateService().findByAbbreviation(VALID_STATE)).andReturn(state).anyTimes();
		EasyMock.expect(state.getName()).andReturn(VALID_STATE_NAME).anyTimes();
		final VipSource source = createMock("Source", VipSource.class);
		EasyMock.expect(getVipService().findLatestSource(VALID_STATE_NAME)).andReturn(source).anyTimes();
		final VipState vipState = createMock("VipState", VipState.class);
		EasyMock.expect(getVipService().findStateBySourceAndName(source, VALID_STATE_NAME)).andReturn(vipState).anyTimes();
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForCandidateFinder() {
		setContestsForElectoralDistricts(null);
		setVipService(null);
		setStateService(null);
	}

	/**
	 * Gets the contests for electoral districts.
	 * 
	 * @author IanBrown
	 * @return the contests for electoral districts.
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	private Map<VipElectoralDistrict, List<VipContest>> getContestsForElectoralDistricts() {
		return contestsForElectoralDistricts;
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private StateService getStateService() {
		return stateService;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Sets the contests for electoral districts.
	 * 
	 * @author IanBrown
	 * @param contestsForElectoralDistricts
	 *            the contests for electoral districts to set.
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	private void setContestsForElectoralDistricts(final Map<VipElectoralDistrict, List<VipContest>> contestsForElectoralDistricts) {
		this.contestsForElectoralDistricts = contestsForElectoralDistricts;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}

	/**
	 * Sets the VIP service.
	 * 
	 * @author IanBrown
	 * @param vipService
	 *            the VIP service to set.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	private void setVipService(final VipService vipService) {
		this.vipService = vipService;
	}
}
