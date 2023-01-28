/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.common;

import static com.bearcode.ovf.model.common.CommonTestHelper.createCounty;
import static com.bearcode.ovf.model.common.CommonTestHelper.createState;
import static com.bearcode.ovf.model.common.CommonTestHelper.values;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for {@link County}.
 * @author Ian
 *
 */
public final class CountyTest {

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when the counties have different names.
	 */
	@Test
	public final void testValueEquals_differentNames() {
		final String name1 = "Name 1";
		final County county1 = createCounty(values(new Object[][] {{"Name", name1}}));
		final String name2 = "Name 2";
		final County county2 = createCounty(values(new Object[][] {{"Name", name2}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertFalse("The counties are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when the counties have different states.
	 */
	@Test
	public final void testValueEquals_differentStates() {
		final String name = "Name";
		final String type = "County";
		final State state1 = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county1 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}, {"State", state1}}));
		final State state2 = createState(values(new Object[][] {{"Abbr", "A2"}}));
		final County county2 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}, {"State", state2}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertFalse("The counties are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when the types are different.
	 */
	@Test
	public final void testValueEquals_differentTypes() {
		final String name = "Name";
		final String type1 = "County";
		final County county1 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type1}}));
		final String type2 = "Parish";
		final County county2 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type2}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertFalse("The counties are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when one county has a name and the other does not.
	 */
	@Test
	public final void testValueEquals_nameNoName() {
		final String name = "Name";
		final County county1 = createCounty(values(new Object[][] {{"Name", name}}));
		final County county2 = createCounty(values(new Object[][] {}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertFalse("The counties are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when no values are set.
	 */
	@Test
	public final void testValueEquals_noValues() {
		final County county1 = createCounty(values(new Object[][] {}));
		final County county2 = createCounty(values(new Object[][] {}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertTrue("The counties are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when the counties have the same name.
	 */
	@Test
	public final void testValueEquals_sameNames() {
		final String name = "Name";
		final County county1 = createCounty(values(new Object[][] {{"Name", name}}));
		final County county2 = createCounty(values(new Object[][] {{"Name", name}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertTrue("The counties are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when counties have the same state.
	 */
	@Test
	public final void testValueEquals_sameState() {
		final String name = "Name";
		final String type = "County";
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county1 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}, {"State", state}}));
		final County county2 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}, {"State", state}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertTrue("The counties are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when the types are the same.
	 */
	@Test
	public final void testValueEquals_sameTypes() {
		final String name = "Name";
		final String type = "County";
		final County county1 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}}));
		final County county2 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertTrue("The counties are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when only one county has a state.
	 */
	@Test
	public final void testValueEquals_stateNoState() {
		final String name = "Name";
		final String type = "County";
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county1 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}, {"State", state}}));
		final County county2 = createCounty(values(new Object[][] {{"Name", name}, {"Type", type}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertFalse("The counties are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.County#valueEquals(com.bearcode.ovf.model.common.County)} when only one county has a type.
	 */
	@Test
	public final void testValueEquals_typeNoType() {
		final String name = "Name";
		final County county1 = createCounty(values(new Object[][] {{"Name", name}, {"Type", "County"}}));
		final County county2 = createCounty(values(new Object[][] {{"Name", name}}));
		
		final boolean actualEquals = county1.valueEquals(county2);
		
		assertFalse("The counties are not equal", actualEquals);
	}
}