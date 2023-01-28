/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;

import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.SupportsStatesOrVotingRegionsCheck;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link SupportsStatesOrVotingRegionsCheck} test for {@link ElectionService}.
 * 
 * @author IanBrown
 * 
 * @since Jun 13, 2012
 * @version Oct 1, 2013
 */
public final class ElectionServiceTest extends SupportsStatesOrVotingRegionsCheck<ElectionService, CandidateFinder> {

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#contestOrder(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Oct 4, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testContestOrder() {
		final String state = "ST";
		final String votingRegionName = "VOTING REGION";
		final CandidateFinder candidateFinder = createMock("CandidateFinder", CandidateFinder.class);
		final List<CandidateFinder> candidateFinders = Arrays.asList(candidateFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(candidateFinders);
		EasyMock.expect(candidateFinder.getStates()).andReturn(null).anyTimes();
		final Map<String, Collection<String>> votingRegions = new HashMap<String, Collection<String>>();
		EasyMock.expect(candidateFinder.getVotingRegions()).andReturn(votingRegions).anyTimes();
		final Collection<String> stateVotingRegions = Arrays.asList(votingRegionName);
		votingRegions.put(state, stateVotingRegions);
		final String contestOffice = "OFFICE: STATE OFFICE";
		final String contestReferendum = "REFERENDUM: *";
		final List<String> contestOrder = Arrays.asList(contestOffice, contestReferendum);
		EasyMock.expect(candidateFinder.getContestOrder()).andReturn(contestOrder);
		replayAll();

		final List<String> actualContestOrder = getSupportsStatesOrVotingRegions().contestOrder(state, votingRegionName);

		assertSame("The contest order is returned", contestOrder, actualContestOrder);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findCandidateBio(String, String, long)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 16, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindCandidateBio() throws Exception {
		final String state = "ST";
		final String votingRegionName = null;
		final long candidateVipId = 654237l;
		final CandidateFinder candidateFinder = createMock("CandidateFinder", CandidateFinder.class);
		final List<CandidateFinder> candidateFinders = Arrays.asList(candidateFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(candidateFinders);
		final List<String> states = Arrays.asList(state);
		EasyMock.expect(candidateFinder.getStates()).andReturn(states);
		final VipCandidateBio candidateBio = createMock("CandidateBio", VipCandidateBio.class);
		EasyMock.expect(candidateFinder.findCandidateBio(candidateVipId)).andReturn(candidateBio);
		replayAll();

		final VipCandidateBio actualCandidateBio = getSupportsStatesOrVotingRegions().findCandidateBio(state, votingRegionName,
				candidateVipId);

		assertSame("The candidate bio is returned", candidateBio, actualCandidateBio);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.ElectionService#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jun 13, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests() throws Exception {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final CandidateFinder candidateFinder = createMock("CandidateFinder", CandidateFinder.class);
		final List<CandidateFinder> candidateFinders = Arrays.asList(candidateFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(candidateFinders);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment);
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress);
		final String state = "ST";
		EasyMock.expect(nonHouseAddress.getState()).andReturn(state);
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct);
		final VipLocality locality = createMock("Locality", VipLocality.class);
		EasyMock.expect(precinct.getLocality()).andReturn(locality);
		final String localityName = "Locality Name";
		EasyMock.expect(locality.getName()).andReturn(localityName);
		final List<String> states = Arrays.asList(state);
		EasyMock.expect(candidateFinder.getStates()).andReturn(states);
		final VipContest contest = createMock("Contest", VipContest.class);
		final List<VipContest> contests = Arrays.asList(contest);
		EasyMock.expect(candidateFinder.findContests(validAddress)).andReturn(contests);
		replayAll();

		final Collection<VipContest> actualContests = getSupportsStatesOrVotingRegions().findContests(validAddress);

		assertSame("The contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.ElectionService#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is no candidate finder for the state.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jun 13, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_noCandidateFinderForState() throws Exception {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final CandidateFinder candidateFinder = createMock("CandidateFinder", CandidateFinder.class);
		final List<CandidateFinder> candidateFinders = Arrays.asList(candidateFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(candidateFinders);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment);
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress);
		final String state = "ST";
		EasyMock.expect(nonHouseAddress.getState()).andReturn(state);
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct);
		final VipLocality locality = createMock("Locality", VipLocality.class);
		EasyMock.expect(precinct.getLocality()).andReturn(locality);
		final String localityName = "Locality Name";
		EasyMock.expect(locality.getName()).andReturn(localityName);
		final List<String> states = Arrays.asList("NO");
		EasyMock.expect(candidateFinder.getStates()).andReturn(states);
		EasyMock.expect(candidateFinder.getVotingRegions()).andReturn(null);
		replayAll();

		final Collection<VipContest> actualContests = getSupportsStatesOrVotingRegions().findContests(validAddress);

		assertNull("There are no contests", actualContests);
		verifyAll();
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.ElectionService#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is no candidate finder for the voting region.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Sep 17, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_noCandidateFinderForVotingRegion() throws Exception {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final CandidateFinder candidateFinder = createMock("CandidateFinder", CandidateFinder.class);
		final List<CandidateFinder> candidateFinders = Arrays.asList(candidateFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(candidateFinders);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment);
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress);
		final String state = "ST";
		EasyMock.expect(nonHouseAddress.getState()).andReturn(state);
		final List<String> states = Arrays.asList("NO");
		EasyMock.expect(candidateFinder.getStates()).andReturn(states);
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct);
		final VipLocality locality = createMock("Locality", VipLocality.class);
		EasyMock.expect(precinct.getLocality()).andReturn(locality);
		final String localityName = "Locality Name";
		EasyMock.expect(locality.getName()).andReturn(localityName);
		final Map<String, Collection<String>> votingRegions = new HashMap<String, Collection<String>>();
		EasyMock.expect(candidateFinder.getVotingRegions()).andReturn(votingRegions);
		final Collection<String> stateVotingRegions = Arrays.asList("Another Locality Name");
		votingRegions.put(state, stateVotingRegions);
		replayAll();

		final Collection<VipContest> actualContests = getSupportsStatesOrVotingRegions().findContests(validAddress);

		assertNull("There are no contests", actualContests);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.ElectionService#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is no list of candidate finders.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jun 13, 2012
	 * @version Oct 10
	 * , 2012
	 */
	@Test
	public final void testFindContests_noCandidateFinders() throws Exception {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		replayAll();

		final Collection<VipContest> actualContests = getSupportsStatesOrVotingRegions().findContests(validAddress);

		assertNull("There are no contests", actualContests);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.ElectionService#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is no street segment in the address.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jun 13, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_noStreetSegment() throws Exception {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final CandidateFinder candidateFinder = createMock("CandidateFinder", CandidateFinder.class);
		final List<CandidateFinder> candidateFinders = Arrays.asList(candidateFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(candidateFinders);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(null);
		replayAll();

		final Collection<VipContest> actualContests = getSupportsStatesOrVotingRegions().findContests(validAddress);

		assertNull("There are no contests", actualContests);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findPartisanContests(List)}.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testFindPartisanContests() {
		final VipContest nonPartisanContest = createMock("NonPartisanContest", VipContest.class);
		EasyMock.expect(nonPartisanContest.isPartisan()).andReturn(false).anyTimes();
		EasyMock.expect(nonPartisanContest.getType()).andReturn("General").anyTimes();
		final VipContest partisanContest = createMock("PartisanContest", VipContest.class);
		EasyMock.expect(partisanContest.isPartisan()).andReturn(true).anyTimes();
		final String partisanParty = "Partisan Party";
		EasyMock.expect(partisanContest.getPartisanParty()).andReturn(partisanParty).anyTimes();
		final List<VipContest> contests = Arrays.asList(nonPartisanContest, partisanContest);
		replayAll();
		
		final List<VipContest> actualPartisanContests = getSupportsStatesOrVotingRegions().findPartisanContests
				(contests);
		
		final List<VipContest> expectedPartisanContests = Arrays.asList(partisanContest);
		assertEquals("There are partisan contests", expectedPartisanContests, actualPartisanContests);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findPartisanContests(List)} for the case where there are no contests.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	@Test
	public final void testFindPartisanContests_noContests() {
		final List<VipContest> contests = new LinkedList<VipContest>();
		replayAll();
		
		final List<VipContest> actualPartisanContests = getSupportsStatesOrVotingRegions().findPartisanContests
				(contests);
		
		assertTrue("There are no partisan contests", actualPartisanContests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findPartisanContests(List)} for the case where there are no partisan contests.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testFindPartisanContests_noPartisanContests() {
		final VipContest nonPartisanContest = createMock("NonPartisanContest", VipContest.class);
		EasyMock.expect(nonPartisanContest.isPartisan()).andReturn(false).anyTimes();
		EasyMock.expect(nonPartisanContest.getType()).andReturn("General").anyTimes();
		final List<VipContest> contests = Arrays.asList(nonPartisanContest);
		replayAll();
		
		final List<VipContest> actualPartisanContests = getSupportsStatesOrVotingRegions().findPartisanContests
				(contests);
		
		assertTrue("There are no partisan contests", actualPartisanContests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findReferendumDetail(String, String, long)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 17, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindReferendumDetail() throws Exception {
		final String state = "ST";
		final String votingRegionName = null;
		final long referendumVipId = 526571l;
		final CandidateFinder candidateFinder = createMock("CandidateFinder", CandidateFinder.class);
		final List<CandidateFinder> candidateFinders = Arrays.asList(candidateFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(candidateFinders);
		final List<String> states = Arrays.asList(state);
		EasyMock.expect(candidateFinder.getStates()).andReturn(states);
		final VipReferendumDetail referendumDetail = createMock("ReferendumDetail", VipReferendumDetail.class);
		EasyMock.expect(candidateFinder.findReferendumDetail(referendumVipId)).andReturn(referendumDetail);
		replayAll();

		final VipReferendumDetail actualReferendumDetail = getSupportsStatesOrVotingRegions().findReferendumDetail(state,
				votingRegionName, referendumVipId);

		assertSame("The referendum detail is returned", referendumDetail, actualReferendumDetail);
		verifyAll();
	}

	/**
	 * Creates the election service.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since Jun 13, 2012
	 * @version Jun 13, 2012
	 */
	private ElectionService createElectionService() {
		return new ElectionService();
	}

	/** {@inheritDoc} */
	@Override
	protected final CandidateFinder createForStateOrVotingRegion() {
		return createMock("CandidateFinder", CandidateFinder.class);
	}

	/** {@inheritDoc} */
	@Override
	protected final ElectionService createSupportsStatesOrVotingRegions() {
		return createElectionService();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpSpecificSupportsStatesOrVotingRegions() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownSpecificSupportsStatesOrVotingRegions() {
	}
}
