/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.common;

import static com.bearcode.ovf.model.common.CommonTestHelper.values;
import static com.bearcode.ovf.model.common.CommonTestHelper.createState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for {@link State}.
 * @author Ian
 *
 */
public final class StateTest {

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.State#valueEquals(com.bearcode.ovf.model.common.State)} where one state has an abbreviation, while the other does not.
	 */
	@Test
	public final void testValueEquals_abbrNoAbbr() {
		final String abbr = "AB";
		final State state1 = createState(values(new Object[][] {{"Abbr", abbr}}));
		final State state2 = createState(values(new Object[][] {}));
		
		final boolean actualEquals = state1.valueEquals(state2);
		
		assertFalse("The states are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.State#valueEquals(com.bearcode.ovf.model.common.State)} where the states do not have equal abbreviations.
	 */
	@Test
	public final void testValueEquals_differentAbbrs() {
		final String abbr1 = "AB";
		final State state1 = createState(values(new Object[][] {{"Abbr", abbr1}}));
		final String abbr2 = "A2";
		final State state2 = createState(values(new Object[][] {{"Abbr", abbr2}}));
		
		final boolean actualEquals = state1.valueEquals(state2);
		
		assertFalse("The states are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.State#valueEquals(com.bearcode.ovf.model.common.State)} where neither state has values.
	 */
	@Test
	public final void testValueEquals_noValues() {
		final State state1 = createState(values(new Object[][] {}));
		final State state2 = createState(values(new Object[][] {}));
		
		final boolean actualEquals = state1.valueEquals(state2);
		
		assertTrue("The states are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.State#valueEquals(com.bearcode.ovf.model.common.State)} where the abbreviations are the same.
	 */
	@Test
	public final void testValueEquals_sameAbbrs() {
		final String abbr = "AB";
		final State state1 = createState(values(new Object[][] {{"Abbr", abbr}}));
		final State state2 = createState(values(new Object[][] {{"Abbr", abbr}}));
		
		final boolean actualEquals = state1.valueEquals(state2);
		
		assertTrue("The states are equal", actualEquals);
	}
}
