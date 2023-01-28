/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.tools.ForStateOrVotingRegionCheck;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Abstract extended {@link ForStateOrVotingRegionCheck} test for implementations of {@link CandidateFinder}.
 * 
 * @author IanBrown
 * 
 * @param <F>
 *            the type of candidate finder to test.
 * @since Jul 2, 2012
 * @version Oct 10, 2012
 */
public abstract class CandidateFinderCheck<F extends CandidateFinder> extends ForStateOrVotingRegionCheck<F> {

	/**
	 * the candidate finder valet.
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private CandidateFinderValet valet;

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findCandidateBio(long)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 16, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindCandidateBio() throws Exception {
		final VipCandidateBio candidateBio = setUpForCandidateBio();
		replayAll();
		final long candidateVipId = candidateBio.getCandidate().getVipId();

		final VipCandidateBio actualCandidateBio = getForStateOrVotingRegion().findCandidateBio(candidateVipId);

		assertSame("The candidate bio is returned", candidateBio, actualCandidateBio);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findCandidateBio(long)} for the case where there is
	 * no such candidate.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 16, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindCandidateBio_noSuchCandidate() throws Exception {
		final long candidateVipId = 5422l;
		setUpForMissingCandidateBio(candidateVipId);
		replayAll();

		final VipCandidateBio actualCandidateBio = getForStateOrVotingRegion().findCandidateBio(candidateVipId);

		assertNull("There is no candidate bio", actualCandidateBio);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is data for a county contest.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jul 3, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_countyContestForAddress() throws Exception {
		final String zip = selectValidZip();
		final String zip4 = selectValidZip4();
		final Map<String, VipElectoralDistrict> electoralDistricts = new HashMap<String, VipElectoralDistrict>();
		final ValidAddress validAddress = createValidAddress(zip, zip4, electoralDistricts);
		final List<VipContest> contests = setUpDataForCountyContest(zip, zip4, electoralDistricts);
		completeSetUp(zip, zip4, electoralDistricts);
		replayAll();

		final List<VipContest> actualContests = getForStateOrVotingRegion().findContests(validAddress);

		assertEquals("The expected contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is data for a federal contest.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jul 3, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_federalContestForAddress() throws Exception {
		final String zip = selectValidZip();
		final String zip4 = selectValidZip4();
		final Map<String, VipElectoralDistrict> electoralDistricts = new HashMap<String, VipElectoralDistrict>();
		final ValidAddress validAddress = createValidAddress(zip, zip4, electoralDistricts);
		final List<VipContest> contests = setUpDataForFederalContest(zip, zip4, electoralDistricts);
		completeSetUp(zip, zip4, electoralDistricts);
		replayAll();

		final List<VipContest> actualContests = getForStateOrVotingRegion().findContests(validAddress);

		assertEquals("The expected contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is data for a local contest.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jul 2, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_localContestForAddress() throws Exception {
		final String zip = selectValidZip();
		final String zip4 = selectValidZip4();
		final Map<String, VipElectoralDistrict> electoralDistricts = new HashMap<String, VipElectoralDistrict>();
		final ValidAddress validAddress = createValidAddress(zip, zip4, electoralDistricts);
		final List<VipContest> contests = setUpDataForLocalContest(electoralDistricts);
		completeSetUp(zip, zip4, electoralDistricts);
		replayAll();

		final List<VipContest> actualContests = getForStateOrVotingRegion().findContests(validAddress);

		assertEquals("The expected contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is no data available.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jul 2, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_noData() throws Exception {
		final String zip = selectValidZip();
		final String zip4 = selectValidZip4();
		final Map<String, VipElectoralDistrict> electoralDistricts = new HashMap<String, VipElectoralDistrict>();
		final ValidAddress validAddress = createValidAddress(zip, zip4, electoralDistricts);
		setUpDataForNoContests(zip, zip4, electoralDistricts);
		completeSetUp(zip, zip4, electoralDistricts);
		replayAll();

		final List<VipContest> actualContests = getForStateOrVotingRegion().findContests(validAddress);

		assertTrue("No contests are returned", actualContests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is data, but not for the address.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jul 2, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_noDataForAddress() throws Exception {
		final String zip = selectValidZip();
		final String zip4 = selectValidZip4();
		final Map<String, VipElectoralDistrict> electoralDistricts = new HashMap<String, VipElectoralDistrict>();
		final ValidAddress validAddress = createValidAddress(zip, zip4, electoralDistricts);
		final List<VipContest> contests = setUpDataForOtherAddress(electoralDistricts);
		completeSetUp(zip, zip4, electoralDistricts);
		replayAll();

		final List<VipContest> actualContests = getForStateOrVotingRegion().findContests(validAddress);

		assertEquals("The expected contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findContests(com.bearcode.ovf.tools.votingprecinct.model.ValidAddress)}
	 * for the case where there is data for a state contest.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jul 3, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindContests_stateContestForAddress() throws Exception {
		final String zip = selectValidZip();
		final String zip4 = selectValidZip4();
		final Map<String, VipElectoralDistrict> electoralDistricts = new HashMap<String, VipElectoralDistrict>();
		final ValidAddress validAddress = createValidAddress(zip, zip4, electoralDistricts);
		final List<VipContest> contests = setUpDataForStateContest(electoralDistricts);
		completeSetUp(zip, zip4, electoralDistricts);
		replayAll();

		final List<VipContest> actualContests = getForStateOrVotingRegion().findContests(validAddress);

		assertEquals("The expected contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findReferendumDetail(long)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem finding the referendum detail.
	 * @since Aug 17, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindReferendumDetail() throws Exception {
		if (!areReferendumsSupported()) {
			return;
		}

		final VipReferendumDetail referendumDetail = setUpForReferendumDetail();
		replayAll();
		final long referendumVipId = referendumDetail.getReferendum().getVipId();

		final VipReferendumDetail actualReferendumDetail = getForStateOrVotingRegion().findReferendumDetail(referendumVipId);

		assertSame("The referendum detail is returned", referendumDetail, actualReferendumDetail);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinder#findReferendumDetail(long)} for the case where there
	 * is no such referendum.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem finding the referendum detail.
	 * @since Aug 17, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testFindReferendumDetail_noSuchReferendum() throws Exception {
		if (!areReferendumsSupported()) {
			return;
		}

		final long referendumVipId = 65127l;
		setUpForMissingReferendumDetail(referendumVipId);
		replayAll();

		final VipReferendumDetail actualReferendumDetail = getForStateOrVotingRegion().findReferendumDetail(referendumVipId);

		assertNull("There is no referendum detail", actualReferendumDetail);
		verifyAll();
	}

	/**
	 * Are referendums supported by the specific candidate finder?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if they are supported, <code>false</code> if they are not.
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	protected abstract boolean areReferendumsSupported();

	/**
	 * Completes the set up for testing for the specified ZIP, ZIP+4, and electoral districts.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the ZIP code.
	 * @param zip4
	 *            the ZIP+4.
	 * @param electoralDistricts
	 *            the map of electoral districts by name.
	 * @throws IOException
	 *             if there is a problem completing the set up.
	 * @since Sep 11, 2012
	 * @version Sep 12, 2012
	 */
	protected abstract void completeSetUp(String zip, String zip4, Map<String, VipElectoralDistrict> electoralDistricts)
			throws IOException;

	/**
	 * Creates a candidate finder of the type to test.
	 * 
	 * @author IanBrown
	 * @return the candidate finder.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	protected abstract F createCandidateFinder();

	/** {@inheritDoc} */
	@Override
	protected final F createForStateOrVotingRegion() {
		final F candidateFinder = createCandidateFinder();
		candidateFinder.setValet(getValet());
		return candidateFinder;
	}

	/**
	 * Creates a valid address.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the ZIP code for the address.
	 * @param zip4
	 *            the ZIP+4 for the address.
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the valid address.
	 * @since Jul 2, 2012
	 * @version Sep 11, 2012
	 */
	protected abstract ValidAddress createValidAddress(String zip, String zip4, Map<String, VipElectoralDistrict> electoralDistricts);

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	protected final CandidateFinderValet getValet() {
		return valet;
	}

	/**
	 * Choose a valid ZIP code.
	 * 
	 * @author IanBrown
	 * @return the valid ZIP code.
	 * @since Sep 11, 2012
	 * @version Oct 10, 2012
	 */
	protected abstract String selectValidZip();

	/**
	 * Choose a valid ZIP+4.
	 * 
	 * @author IanBrown
	 * @return the valid ZIP+4.
	 * @since Sep 11, 2012
	 * @version Oct 10, 2012
	 */
	protected abstract String selectValidZip4();

	/**
	 * Sets up the data for a county contest for the address.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the ZIP code.
	 * @param zip4
	 *            the ZIP+4.
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the expected contests.
	 * @throws Exception
	 *             if there is a problem setting up the data.
	 * @since Jul 3, 2012
	 * @version Sep 11, 2012
	 */
	protected abstract List<VipContest> setUpDataForCountyContest(String zip, String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception;

	/**
	 * Sets up the data for a federal contest for the address.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the ZIP code.
	 * @param zip4
	 *            the ZIP+4.
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the expected contests.
	 * @throws Exception
	 *             if there is a problem setting up the data.
	 * @since Jul 3, 2012
	 * @version Sep 11, 2012
	 */
	protected abstract List<VipContest> setUpDataForFederalContest(String zip, String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception;

	/**
	 * Sets up the data for a local contest for the address.
	 * 
	 * @author IanBrown
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the expected contests.
	 * @throws Exception
	 *             if there is a problem setting up the data.
	 * @since Jul 3, 2012
	 * @version Aug 9, 2012
	 */
	protected abstract List<VipContest> setUpDataForLocalContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception;

	/**
	 * Sets up the "data" for the case where there are no contests.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the ZIP code.
	 * @param zip4
	 *            the ZIP+4 code.
	 * @param electoralDistricts
	 *            the map of electoral districts by name.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	protected abstract void setUpDataForNoContests(String zip, String zip4, Map<String, VipElectoralDistrict> electoralDistricts);

	/**
	 * Sets up data for a different address than the desired one.
	 * 
	 * @author IanBrown
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @throws Exception
	 *             if there is a problem setting up the data.
	 * @return the expected contests.
	 * @since Jul 2, 2012
	 * @version Aug 9, 2012
	 */
	protected abstract List<VipContest> setUpDataForOtherAddress(Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception;

	/**
	 * Sets up the data for a state contest for the address.
	 * 
	 * @author IanBrown
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the expected contests.
	 * @throws Exception
	 *             if there is a problem setting up the data.
	 * @since Jul 3, 2012
	 * @version Aug 9, 2012
	 */
	protected abstract List<VipContest> setUpDataForStateContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception;

	/**
	 * Sets up to have a candidate bio for the candidate VIP identifier.
	 * 
	 * @author IanBrown
	 * @return the VIP candidate bio.
	 * @throws Exception
	 *             if there is a problem setting up the candidate bio.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	protected abstract VipCandidateBio setUpForCandidateBio() throws Exception;

	/**
	 * Sets up to test the specific type of candidate finder.
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	protected abstract void setUpForCandidateFinder();

	/**
	 * Sets up to be missing the candidate for the candidate VIP identifier.
	 * 
	 * @author IanBrown
	 * @param candidateVipId
	 *            the candidate VIP identifier.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	protected abstract void setUpForMissingCandidateBio(long candidateVipId);

	/**
	 * Sets up to test for a missing referendum detail.
	 * 
	 * @author IanBrown
	 * @param referendumVipId
	 *            the referendum VIP identifier.
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	protected abstract void setUpForMissingReferendumDetail(long referendumVipId);

	/**
	 * Sets up to test retrieving a referendum detail.
	 * 
	 * @author IanBrown
	 * @return the referendum detail.
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	protected abstract VipReferendumDetail setUpForReferendumDetail();

	/** {@inheritDoc} */
	@Override
	protected final void setUpSpecificForStateOrVotingRegion() {
		setValet(createMock("Valet", CandidateFinderValet.class));
		setUpForCandidateFinder();
	}

	/**
	 * Tears down the set up for testing the specific type of candidate finder.
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	protected abstract void tearDownForCandidateFinder();

	/** {@inheritDoc} */
	@Override
	protected final void tearDownSpecificForStateOrVotingRegion() {
		tearDownForCandidateFinder();
		setValet(null);
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private void setValet(final CandidateFinderValet valet) {
		this.valet = valet;
	}
}
