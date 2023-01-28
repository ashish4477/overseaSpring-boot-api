/**
 * 
 */
package com.bearcode.ovf.model.eod;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.bearcode.ovf.model.common.State;

/**
 * Extended {@link AbstractStateSpecificDirectoryCheck} test for {@link StateSpecificDirectory}.
 * 
 * @author IanBrown
 * 
 * @since Jan 12, 2012
 * @version Jan 12, 2012
 */
public final class StateSpecificDirectoryTest extends AbstractStateSpecificDirectoryCheck<StateSpecificDirectory> {

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.StateSpecificDirectory#getState()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetState() {
		final State actualState = getStateSpecificDirectory().getState();

		assertNull("There is no state set", actualState);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.StateSpecificDirectory#setState(com.bearcode.ovf.model.common.State)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetState() {
		final State state = createMock("State", State.class);

		getStateSpecificDirectory().setState(state);

		assertSame("The state is set", state, getStateSpecificDirectory().getState());
	}

	/** {@inheritDoc} */
	@Override
	protected final StateSpecificDirectory createStateSpecificDirectory() {
		return new StateSpecificDirectory();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForStateSpecificDirectory() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForStateSpecificDirectory() {
	}

}
