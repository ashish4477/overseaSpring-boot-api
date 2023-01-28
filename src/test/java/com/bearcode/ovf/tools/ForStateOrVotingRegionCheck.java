/**
 * 
 */
package com.bearcode.ovf.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test for implementations of {@link ForStateOrVotingRegion}.
 * 
 * @author IanBrown
 * 
 * @param <F>
 *            the type of object for state or voting region to test.
 * @since Oct 9, 2012
 * @version Oct 9, 2012
 */
public abstract class ForStateOrVotingRegionCheck<F extends ForStateOrVotingRegion> extends EasyMockSupport {

	/**
	 * the object for state or voting region to test.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private F forStateOrVotingRegion;

	/**
	 * Sets up to test the object for state or voting region.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Before
	public final void setUpForStateOrVotingRegion() {
		setUpSpecificForStateOrVotingRegion();
		setForStateOrVotingRegion(createForStateOrVotingRegion());
	}

	/**
	 * Tears down after testing the for state or voting region object.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@After
	public final void tearDownForStateOrVotingRegion() {
		setForStateOrVotingRegion(null);
		tearDownSpecificForStateOrVotingRegion();
	}

	/**
	 * Test method for {@link ForStateOrVotingRegion#isReady(String, String)} for the case where there is just a state and it isn't
	 * ready.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIsReady_stateNotReady() {
		final String stateIdentification = selectInvalidState();
		final String votingRegionName = null;
		replayAll();

		final boolean actualReady = getForStateOrVotingRegion().isReady(stateIdentification, votingRegionName);

		assertFalse("The state is not ready", actualReady);
		verifyAll();
	}

	/**
	 * Test method for {@link ForStateOrVotingRegion#isReady(String, String)} for the case where there is just a state and it is
	 * ready.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIsReady_stateReady() {
		final String stateIdentification = selectValidState();
		final String votingRegionName = null;
		setUpReadyForState();
		replayAll();

		final boolean actualReady = getForStateOrVotingRegion().isReady(stateIdentification, votingRegionName);

		assertTrue("The state is ready", actualReady);
		verifyAll();
	}

	/**
	 * Test method for {@link ForStateOrVotingRegion#isReady(String, String)} for the case where there is just a voting region and
	 * it isn't ready.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIsReady_votingRegionNotReady() {
		final String stateIdentification = selectValidState();
		final String votingRegionName = selectInvalidVotingRegion();
		replayAll();

		final boolean actualReady = getForStateOrVotingRegion().isReady(stateIdentification, votingRegionName);

		assertFalse("The state is not ready", actualReady);
		verifyAll();
	}

	/**
	 * Test method for {@link ForStateOrVotingRegion#isReady(String, String)} for the case where there is a voting region and it is
	 * ready.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIsReady_votingRegionReady() {
		final String stateIdentification = selectValidState();
		final String votingRegionName = selectValidVotingRegion();
		setUpReadyForVotingRegion();
		replayAll();

		final boolean actualReady = getForStateOrVotingRegion().isReady(stateIdentification, votingRegionName);

		assertTrue("The state is ready", actualReady);
		verifyAll();
	}

	/**
	 * Test method for {@link ForStateOrVotingRegion#isReady(String, String)} for the case where there is a voting region and the
	 * state is ready.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIsReady_votingRegionStateReady() {
		final String stateIdentification = selectValidState();
		final String votingRegionName = selectValidVotingRegion();
		setUpReadyForState();
		replayAll();

		final boolean actualReady = getForStateOrVotingRegion().isReady(stateIdentification, votingRegionName);

		assertTrue("The state is ready", actualReady);
		verifyAll();
	}

	/**
	 * Creates an object for state or voting region of the type to test.
	 * 
	 * @author IanBrown
	 * @return the object for state or voting region object.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract F createForStateOrVotingRegion();

	/**
	 * Gets the for state or voting region object.
	 * 
	 * @author IanBrown
	 * @return the state or voting region object.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected final F getForStateOrVotingRegion() {
		return forStateOrVotingRegion;
	}

	/**
	 * Selects an invalid state abbreviation.
	 * 
	 * @author IanBrown
	 * @return the state abbreivation.
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract String selectInvalidState();

	/**
	 * Selects the name of an invalid voting region.
	 * 
	 * @author IanBrown
	 * @return the voting region name.
	 * @since Jul 30, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract String selectInvalidVotingRegion();

	/**
	 * Selects a valid state abbreviation.
	 * 
	 * @author IanBrown
	 * @return the state abbreviation.
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract String selectValidState();

	/**
	 * Selects a valid voting region.
	 * 
	 * @author IanBrown
	 * @return the name of the voting region.
	 * @since Jul 30, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract String selectValidVotingRegion();

	/**
	 * Sets things up so that the valid state is ready.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void setUpReadyForState();

	/**
	 * Sets up so that the valid voting region is ready.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void setUpReadyForVotingRegion();

	/**
	 * Sets up to test the specific type of for state or voting region object.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void setUpSpecificForStateOrVotingRegion();

	/**
	 * Tears down the set up for testing the specific type of for state or voting region object.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void tearDownSpecificForStateOrVotingRegion();

	/**
	 * Sets the state or voting region object.
	 * 
	 * @author IanBrown
	 * @param forStateOrVotingRegion
	 *            the state or voting region object to set.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private void setForStateOrVotingRegion(final F forStateOrVotingRegion) {
		this.forStateOrVotingRegion = forStateOrVotingRegion;
	}
}
