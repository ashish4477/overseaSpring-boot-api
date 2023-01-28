/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.common;

import static com.bearcode.ovf.model.common.CommonTestHelper.createCounty;
import static com.bearcode.ovf.model.common.CommonTestHelper.createMunicipality;
import static com.bearcode.ovf.model.common.CommonTestHelper.createState;
import static com.bearcode.ovf.model.common.CommonTestHelper.values;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for {@link Municipality}.
 * @author Ian
 *
 */
public final class MunicipalityTest {

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when only one municipality has a county.
	 */
	@Test
	public final void testValueEquals_countyNoCounty() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the counties are different.
	 */
	@Test
	public final void testValueEquals_differentCounties() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county1 = createCounty(values(new Object[][] {{"State", state}, {"Name", "County1"}}));
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county1}}));
		final County county2 = createCounty(values(new Object[][] {{"State", state}, {"Name", "County2"}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county2}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the names are different.
	 */
	@Test
	public final void testValueEquals_differentNames() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final String type = "City";
		final String name1 = "Name1";
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}, {"Name", name1}}));
		final String name2 = "Name2";
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}, {"Name", name2}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the states are different.
	 */
	@Test
	public final void testValueEquals_differentStates() {
		final State state1 = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state1}}));
		final State state2 = createState(values(new Object[][] {{"Abbr", "A2"}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state2}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the types are different.
	 */
	@Test
	public final void testValueEquals_differentTypes() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final String type1 = "City";
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type1}}));
		final String type2 = "Town";
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type2}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when only one municipality has a name.
	 */
	@Test
	public final void testValueEquals_nameNoName() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final String type = "City";
		final String name = "Name";
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}, {"Name", name}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when no values are set.
	 */
	@Test
	public final void testValueEquals_noValues() {
		final Municipality municipality1 = createMunicipality(values(new Object[][] {}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertTrue("The municipalities are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the counties are the same.
	 */
	@Test
	public final void testValueEquals_sameCounty() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertTrue("The municipalities are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the names are the same.
	 */
	@Test
	public final void testValueEquals_sameName() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final String type = "City";
		final String name = "Name";
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}, {"Name", name}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}, {"Name", name}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertTrue("The municipalities are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the states are the same.
	 */
	@Test
	public final void testValueEquals_sameState() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertTrue("The municipalities are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when the types are the same.
	 */
	@Test
	public final void testValueEquals_sameType() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final String type = "City";
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertTrue("The municipalities are equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when only one municipality has a state.
	 */
	@Test
	public final void testValueEquals_stateNoState() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.Municipality#valueEquals(com.bearcode.ovf.model.common.Municipality)} when only one municipality has a type.
	 */
	@Test
	public final void testValueEquals_typeNoType() {
		final State state = createState(values(new Object[][] {{"Abbr", "AB"}}));
		final County county = createCounty(values(new Object[][] {{"State", state}, {"Name", "County"}}));
		final String type = "City";
		final Municipality municipality1 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}, {"Type", type}}));
		final Municipality municipality2 = createMunicipality(values(new Object[][] {{"State", state}, {"County", county}}));
		
		final boolean actualEquals = municipality1.valueEquals(municipality2);
		
		assertFalse("The municipalities are not equal", actualEquals);
	}
}
