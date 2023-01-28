/**
 * 
 */
package com.bearcode.ovf.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test for implementations of {@link SupportsStatesOrVotingRegions}.
 * 
 * @author IanBrown
 * 
 * @param <S>
 *            the type of object supporting states or voting regions to test.
 * @param <F>
 *            the type of for state or voting region object used.
 * @since Oct 9, 2012
 * @version Oct 9, 2012
 */
public abstract class SupportsStatesOrVotingRegionsCheck<S extends SupportsStatesOrVotingRegions<F>, F extends ForStateOrVotingRegion>
		extends EasyMockSupport {

	/**
	 * the supports states or voting regions object to test.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private S supportsStatesOrVotingRegions;

	/**
	 * Sets up to test the object supporting states or voting regions.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Before
	public final void setUpSupportsStatesOrVotingRegions() {
		setUpSpecificSupportsStatesOrVotingRegions();
		setSupportsStatesOrVotingRegions(createSupportsStatesOrVotingRegions());
	}

	/**
	 * Tears down the object supporting states or voting regions after testing.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@After
	public final void tearDownSupportsStatesOrVotingRegions() {
		setSupportsStatesOrVotingRegions(null);
		tearDownSpecificSupportsStatesOrVotingRegions();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#identifyForStateOrVotingRegion(java.lang.String, java.lang.String)}
	 * for a state when there are no objects available.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIdentifyForStateOrVotingRegion_noStateAvailable() {
		final String stateIdentification = "ST";
		final String votingRegionName = null;
		replayAll();

		final F actualForStateOrVotingRegion = getSupportsStatesOrVotingRegions().identifyForStateOrVotingRegion(
				stateIdentification, votingRegionName);

		assertNull("No object found for state", actualForStateOrVotingRegion);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#identifyForStateOrVotingRegion(java.lang.String, java.lang.String)}
	 * for a voting region when there are no objects available.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIdentifyForStateOrVotingRegion_noVotingRegionAvailable() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		replayAll();

		final F actualForStateOrVotingRegion = getSupportsStatesOrVotingRegions().identifyForStateOrVotingRegion(
				stateIdentification, votingRegionName);

		assertNull("No object found for voting region", actualForStateOrVotingRegion);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#identifyForStateOrVotingRegion(java.lang.String, java.lang.String)}
	 * for a state when there is an object ready for it.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testIdentifyForStateOrVotingRegion_state() {
		final String stateIdentification = "ST";
		final String votingRegionName = null;
		final F forStateOrVotingRegion = createForStateOrVotingRegion();
		EasyMock.expect(forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)).andReturn(true);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(Arrays.asList(forStateOrVotingRegion));
		replayAll();

		final F actualForStateOrVotingRegion = getSupportsStatesOrVotingRegions().identifyForStateOrVotingRegion(
				stateIdentification, votingRegionName);

		assertSame("Object found for state", forStateOrVotingRegion, actualForStateOrVotingRegion);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#identifyForStateOrVotingRegion(java.lang.String, java.lang.String)}
	 * for a state when there is no object ready for it.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testIdentifyForStateOrVotingRegion_stateObjectNotReady() {
		final String stateIdentification = "ST";
		final String votingRegionName = null;
		final F forStateOrVotingRegion = createForStateOrVotingRegion();
		EasyMock.expect(forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)).andReturn(false);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(Arrays.asList(forStateOrVotingRegion));
		replayAll();

		final F actualForStateOrVotingRegion = getSupportsStatesOrVotingRegions().identifyForStateOrVotingRegion(
				stateIdentification, votingRegionName);

		assertNull("No object found for state", actualForStateOrVotingRegion);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#identifyForStateOrVotingRegion(java.lang.String, java.lang.String)}
	 * for a voting region when there is an object ready for it.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testIdentifyForStateOrVotingRegion_votingRegion() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		final F forStateOrVotingRegion = createForStateOrVotingRegion();
		EasyMock.expect(forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)).andReturn(true);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(Arrays.asList(forStateOrVotingRegion));
		replayAll();

		final F actualForStateOrVotingRegion = getSupportsStatesOrVotingRegions().identifyForStateOrVotingRegion(
				stateIdentification, votingRegionName);

		assertSame("Object found for voting region", forStateOrVotingRegion, actualForStateOrVotingRegion);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#identifyForStateOrVotingRegion(java.lang.String, java.lang.String)}
	 * for a voting region when there is no object ready for it.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testIdentifyForStateOrVotingRegion_votingRegionObjectNotReady() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		final F forStateOrVotingRegion = createForStateOrVotingRegion();
		EasyMock.expect(forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)).andReturn(false);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(Arrays.asList(forStateOrVotingRegion));
		replayAll();

		final F actualForStateOrVotingRegion = getSupportsStatesOrVotingRegions().identifyForStateOrVotingRegion(
				stateIdentification, votingRegionName);

		assertNull("No object found for voting region", actualForStateOrVotingRegion);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#isReady(java.lang.String, java.lang.String)} for
	 * the case where there is an object supported, but not ready.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testIsReady() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		final F forStateOrVotingRegion = createForStateOrVotingRegion();
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(Arrays.asList(forStateOrVotingRegion));
		EasyMock.expect(forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)).andReturn(true);
		EasyMock.expect(forStateOrVotingRegion.isReady(stateIdentification, votingRegionName)).andReturn(true);
		replayAll();

		final boolean actualReady = getSupportsStatesOrVotingRegions().isReady(stateIdentification, votingRegionName);

		assertTrue("The voting region is ready", actualReady);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#isReady(java.lang.String, java.lang.String)} for
	 * the case where there is nothing ready.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIsReady_nothingReady() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		replayAll();

		final boolean actualReady = getSupportsStatesOrVotingRegions().isReady(stateIdentification, votingRegionName);

		assertFalse("The voting region is not ready", actualReady);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#isReady(java.lang.String, java.lang.String)} for
	 * the case where there is an object supported, but not ready.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testIsReady_supportedNotReady() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		final F forStateOrVotingRegion = createForStateOrVotingRegion();
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(Arrays.asList(forStateOrVotingRegion));
		EasyMock.expect(forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)).andReturn(true);
		EasyMock.expect(forStateOrVotingRegion.isReady(stateIdentification, votingRegionName)).andReturn(false);
		replayAll();

		final boolean actualReady = getSupportsStatesOrVotingRegions().isReady(stateIdentification, votingRegionName);

		assertFalse("The voting region is not ready", actualReady);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#isSupported(java.lang.String, java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testIsSupported() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		final F forStateOrVotingRegion = createForStateOrVotingRegion();
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(Arrays.asList(forStateOrVotingRegion));
		EasyMock.expect(forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)).andReturn(true);
		replayAll();

		final boolean actualSupported = getSupportsStatesOrVotingRegions().isSupported(stateIdentification, votingRegionName);

		assertTrue("The voting region is supported", actualSupported);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.SupportsStatesOrVotingRegions#isSupported(java.lang.String, java.lang.String)}
	 * for the case where there is nothing available.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testIsSupported_nothingAvailable() {
		final String stateIdentification = "ST";
		final String votingRegionName = "Voting Region Name";
		replayAll();

		final boolean actualSupported = getSupportsStatesOrVotingRegions().isSupported(stateIdentification, votingRegionName);

		assertFalse("The voting region is not supported", actualSupported);
		verifyAll();
	}

	/**
	 * Creates an object to perform operations for a state or voting region of the type used.
	 * 
	 * @author IanBrown
	 * @return the object to perform operations for a state or voting region.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract F createForStateOrVotingRegion();

	/**
	 * Creates an object the supports states or voting regions of the type to test.
	 * 
	 * @author IanBrown
	 * @return the object supporting states or voting regions.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract S createSupportsStatesOrVotingRegions();

	/**
	 * Gets the supports states or voting regions object.
	 * 
	 * @author IanBrown
	 * @return the supports states or voting regions object.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected final S getSupportsStatesOrVotingRegions() {
		return supportsStatesOrVotingRegions;
	}

	/**
	 * Sets up to test the specific type of object supporting states or voting regions.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void setUpSpecificSupportsStatesOrVotingRegions();

	/**
	 * Tears down the set up for testing the specific type of object supporting states or voting regions.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void tearDownSpecificSupportsStatesOrVotingRegions();

	/**
	 * Sets the supports states or voting regions object.
	 * 
	 * @author IanBrown
	 * @param supportsStatesOrVotingRegions
	 *            the supports states or voting regions object to set.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private void setSupportsStatesOrVotingRegions(final S supportsStatesOrVotingRegions) {
		this.supportsStatesOrVotingRegions = supportsStatesOrVotingRegions;
	}
}
