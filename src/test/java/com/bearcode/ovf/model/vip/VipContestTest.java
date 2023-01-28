/**
 * Copyright 2013 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.vip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for {@link VipContest}.
 * 
 * @author IanBrown
 * 
 * @since Jul 30, 2013
 * @version Jul 30, 2013
 */
public final class VipContestTest {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.vip.VipContest#getPartisanParty()} for the
	 * case where the contest is partisan with no primary party, but all
	 * candidates share a party.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	@Test
	public final void testGetPartisanParty_candidatesShareParty() {
		final boolean partisan = true;
		final String primaryParty = null;
		final String partisanParty = "Partisan Party";
		final VipCandidate candidate1 = createVipCandidate(1, partisanParty);
		final VipCandidate candidate2 = createVipCandidate(2, partisanParty);
		final List<VipCandidate> candidates = Arrays.asList(candidate1,
		        candidate2);
		final VipContest vipContest = createVipContest(partisan, primaryParty,
		        candidates);

		final String actualPartisanParty = vipContest.getPartisanParty();

		assertEquals("The partisan party is taken from the candidates",
		        partisanParty, actualPartisanParty);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.vip.VipContest#getPartisanParty()} for the
	 * case where the contest is partisan with no primary party or candidates.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	@Test
	public final void testGetPartisanParty_noCandidates() {
		final boolean partisan = true;
		final String primaryParty = null;
		final List<VipCandidate> candidates = null;
		final VipContest vipContest = createVipContest(partisan, primaryParty,
		        candidates);

		final String actualPartisanParty = vipContest.getPartisanParty();

		assertNull("There is no partisan party", actualPartisanParty);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.vip.VipContest#getPartisanParty()} for the
	 * case where the contest is not partisan.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	@Test
	public final void testGetPartisanParty_nonPartisanContest() {
		final boolean partisan = false;
		final String primaryParty = null;
		final List<VipCandidate> candidates = null;
		final VipContest vipContest = createVipContest(partisan, primaryParty,
		        candidates);

		final String actualPartisanParty = vipContest.getPartisanParty();

		assertNull("There is no partisan party", actualPartisanParty);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.vip.VipContest#getPartisanParty()} for the
	 * case where the contest is partisan with a primary party.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	@Test
	public final void testGetPartisanParty_primaryParty() {
		final boolean partisan = true;
		final String primaryParty = "Primary Party";
		final List<VipCandidate> candidates = null;
		final VipContest vipContest = createVipContest(partisan, primaryParty,
		        candidates);

		final String actualPartisanParty = vipContest.getPartisanParty();

		assertEquals("The partisan party is the primary party", primaryParty,
		        actualPartisanParty);
	}

	/**
	 * Creates a VIP ballot for the candidates.
	 * 
	 * @author IanBrown
	 * 
	 * @param candidates
	 *            the candidates on the ballot.
	 * @return the VIP ballot.
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	private VipBallot createVipBallot(final List<VipCandidate> candidates) {
		final VipBallot vipBallot = new VipBallot();
		if (candidates != null) {
			for (final VipCandidate candidate : candidates) {
				vipBallot.addCandidate(candidate);
			}
		}
		return vipBallot;
	}

	/**
	 * Creates a VIP candidate.
	 * 
	 * @author IanBrown
	 * 
	 * @param candidateIndex
	 *            the index of the candidate (used to uniquely identify the
	 *            candidate).
	 * @param party
	 *            the party of the candidate.
	 * @return the VIP candidate.
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	private VipCandidate createVipCandidate(final int candidateIndex, final String party) {
		final VipCandidate vipCandidate = new VipCandidate();
		vipCandidate.setName("Candidate #" + candidateIndex);
		vipCandidate.setParty(party);
		return vipCandidate;
	}

	/**
	 * Creates a VIP contest with the specified values.
	 * 
	 * @author IanBrown
	 * 
	 * @param partisan
	 *            <code>true</code> for a partisan contest, <code>false</code>
	 *            otherwise.
	 * @param primaryParty
	 *            the name of the primary party or <code>null</code>.
	 * @param candidates
	 *            the candidates in the contest.
	 * @return the VIP contest.
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	private VipContest createVipContest(final boolean partisan,
	        final String primaryParty, final List<VipCandidate> candidates) {
		final VipContest vipContest = new VipContest();
		vipContest.setPartisan(partisan);
		vipContest.setPrimaryParty(primaryParty);
		vipContest.setBallot(createVipBallot(candidates));
		return vipContest;
	}

}
