/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;

/**
 * Abstract test for implementations of {@link CandidateFinderValet}.
 * 
 * @author IanBrown
 * 
 * @param <V>
 *            the type of candidate finder valet to test.
 * @since Jul 3, 2012
 * @version Aug 17, 2012
 */
public abstract class CandidateFinderValetCheck<V extends CandidateFinderValet> extends EasyMockSupport {

	/**
	 * the valet to test.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private V valet;

	/**
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@Before
	public final void setUpValet() {
		setUpForValet();
		setValet(createValet());
	}

	/**
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@After
	public final void tearDownValet() {
		setValet(null);
		tearDownForValet();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinderValet#acquireBallot()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@Test
	public final void testAcquireBallot() {
		final VipBallot actualBallot = getValet().acquireBallot();

		assertNotNull("A ballot is acquired", actualBallot);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinderValet#acquireCandidate()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@Test
	public final void testAcquireCandidate() {
		final VipCandidate actualCandidate = getValet().acquireCandidate();

		assertNotNull("A candidate is acquired", actualCandidate);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinderValet#acquireCandidateBio()}.
	 * 
	 * @author IanBrown
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	@Test
	public final void testAcquireCandidateBio() {
		final VipCandidateBio actualCandidateBio = getValet().acquireCandidateBio();

		assertNotNull("A candidate bio is acquired", actualCandidateBio);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinderValet#acquireContest()}.
	 */
	@Test
	public final void testAcquireContest() {
		final VipContest actualContest = getValet().acquireContest();

		assertNotNull("A contest is acquired", actualContest);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinderValet#acquireElectoralDistrict()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@Test
	public final void testAcquireElectoralDistrict() {
		final VipElectoralDistrict actualElectoralDistrict = getValet().acquireElectoralDistrict();

		assertNotNull("An electoral district is acquired", actualElectoralDistrict);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.CandidateFinderValet#acquireUserAddress()}.
	 * 
	 * @author IanBrown
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	@Test
	public final void testAcquireUserAddress() {
		final UserAddress actualUserAddress = getValet().acquireUserAddress();

		assertNotNull("A user address is acquired", actualUserAddress);
	}

	/**
	 * Creates a valet of the type to test.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	protected abstract V createValet();

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	protected final V getValet() {
		return valet;
	}

	/**
	 * Sets up to test the specific type of valet.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	protected abstract void setUpForValet();

	/**
	 * Tears down the set up for testing the specific type of valet.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	protected abstract void tearDownForValet();

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private void setValet(final V valet) {
		this.valet = valet;
	}

}
