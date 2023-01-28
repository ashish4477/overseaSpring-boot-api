/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.common;

import static com.bearcode.ovf.model.common.CommonTestHelper.assertRegion;
import static com.bearcode.ovf.model.common.CommonTestHelper.values;
import static com.bearcode.ovf.model.common.CommonTestHelper.createCounty;
import static com.bearcode.ovf.model.common.CommonTestHelper.createMunicipality;
import static com.bearcode.ovf.model.common.CommonTestHelper.createState;
import static com.bearcode.ovf.model.common.CommonTestHelper.createVotingRegion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for {@link VotingRegion}.
 * <p>
 * The pre-LDV form of the voting region contained only the following fields:
 * <ul>
 * <li>id</li>
 * <li>state</li>
 * <li>name</li>
 * </ul>
 * @author Ian
 * @since 01/16/2015
 * @version 01/16/2015
 */
public final class VotingRegionTest {

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#getFullName()} where the name and the state are specified.
	 */
	@Test
	public final void testGetFullName_nameAndState() {
		final String name = "New York City";
		final String stateAbbr = "NY";
		final State state = createState(values(new Object[][] {{"Abbr", stateAbbr}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}}));
		
		final String actualFullName = votingRegion.getFullName();
		
		assertEquals("The full name is the name, state abbreviation", name + ", " + state.getAbbr(), actualFullName);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#getFullName()} where just a name is specified.
	 */
	@Test
	public final void testGetFullName_nameOnly() {
		final String name = "Hillsborough Village";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}}));
		
		final String actualFullName = votingRegion.getFullName();
		
		assertEquals("The full name is the name", name + ", ", actualFullName);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#getFullName()} where nothing is specified.
	 */
	@Test
	public final void testGetFullName_noValues() {
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {}));
		
		final String actualFullName = votingRegion.getFullName();
		
		assertEquals("There is an empty full name", ", ", actualFullName);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#getFullName()} where just a state is specified.
	 */
	@Test
	public final void testGetFullName_stateOnly() {
		final String stateAbbr = "VT";
		final State state = createState(values(new Object[][] {{"Abbr", stateAbbr}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"State", state}}));
		
		final String actualFullName = votingRegion.getFullName();
		
		assertEquals("The full name is the state abbreviation after a comma", ", " + stateAbbr, actualFullName);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#getType()} where the name contains an identifying value.
	 */
	@Test
	public final void testGetType_identifyingValue() {
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "Hills Township"}}));
		
		final String actualType = votingRegion.getType();
		
		assertEquals("There is a type", "town", actualType);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#getType()} where the name doesn't contain any identifying value.
	 */
	@Test
	public final void testGetType_noIdentifyingValue() {
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "A Name"}}));
		
		final String actualType = votingRegion.getType();
		
		assertEquals("There is a type", "region", actualType);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#getType()} where nothing is specified.
	 */
	@Test
	public final void testGetType_noValues() {
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {}));
		
		final String actualType = votingRegion.getType();
		
		assertEquals("There is a type", "region", actualType);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#makeConsistent()} when region has a county without a state.
	 */
	@Test
	public final void testMakeConsistent_countyWithoutState() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final County county = createCounty(values(new Object[][] {{"Type", "County"}, {"Name", "Without State"}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "County without State"}, {"State", state}, {"County", county}}));
		
		votingRegion.makeConsistent();
		
		assertEquals("The voting region has the county", county, votingRegion.getCounty());
		assertEquals("The county now has the state", state, county.getState());
		assertNull("The voting region still has no municipality", votingRegion.getMunicipality());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#makeConsistent()} when region has a county with the correct state.
	 */
	@Test
	public final void testMakeConsistent_countyWithState() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final County county = createCounty(values(new Object[][] {{"Type", "County"}, {"Name", "With State"}, {"State", state}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "County with State"}, {"State", state}, {"County", county}}));
		
		votingRegion.makeConsistent();
		
		assertEquals("The voting region has the county", county, votingRegion.getCounty());
		assertEquals("The county has the state", state, county.getState());
		assertNull("The voting region still has no municipality", votingRegion.getMunicipality());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#makeConsistent()} when region has a municipality without a state.
	 */
	@Test
	public final void testMakeConsistent_municipalityWithoutState() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final Municipality municipality = createMunicipality(values(new Object[][] {{"Type", "City"}, {"Name", "Without State"}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "City without State"}, {"State", state}, {"Municipality", municipality}}));
		
		votingRegion.makeConsistent();
		
		assertNull("The voting region still has no county", votingRegion.getCounty());
		assertEquals("The voting region has the municipality", municipality, votingRegion.getMunicipality());
		assertEquals("The municipality now has the state", state, municipality.getState());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#makeConsistent()} when region has a municipality with the correct state.
	 */
	@Test
	public final void testMakeConsistent_municipalityWithState() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final Municipality municipality = createMunicipality(values(new Object[][] {{"Type", "Town"}, {"Name", "With State"}, {"State", state}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "Town with State"}, {"State", state}, {"Municipality", municipality}}));
		
		votingRegion.makeConsistent();
		
		assertNull("The voting region still has no county", votingRegion.getCounty());
		assertEquals("The voting region has the municipality", municipality, votingRegion.getMunicipality());
		assertEquals("The municipality has the state", state, municipality.getState());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#makeConsistent()} when region has no county or municipality.
	 */
	@Test
	public final void testMakeConsistent_noCountyOrMunicipality() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "No County or Municipality"}, {"State", state}}));
		
		votingRegion.makeConsistent();
		
		assertNull("The voting region still has no county", votingRegion.getCounty());
		assertNull("The voting region still has no municipality", votingRegion.getMunicipality());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where the municipality is changed.
	 */
	@Test
	public final void testUpdateFrom_changeMunicipality() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final County county = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality municipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}, {"County", county}}));
		final String name = municipalityName + " " + municipalityType + ", " + countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", county}, {"Municipality", municipality}}));
		final County updatedCounty = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final Municipality updatedMunicipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", "Township"}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", updatedCounty}, {"Municipality", updatedMunicipality}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is a county", actualVotingRegion.getCounty());
		assertNotNull("There is a municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
		assertSame("The county has not changed", county, actualVotingRegion.getCounty());
		assertNotSame("The municipality has changed", municipality, actualVotingRegion.getMunicipality());
		assertSame("The municipality county has not changed", county, actualVotingRegion.getMunicipality().getCounty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a municipality region becomes a county.
	 */
	@Test
	public final void testUpdateFrom_changeMunicipalityToCounty() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final County county = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality municipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}, {"County", county}}));
		final String name = municipalityName + " " + municipalityType + ", " + countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", county}, {"Municipality", municipality}}));
		final County updatedCounty = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", updatedCounty}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is a county", actualVotingRegion.getCounty());
		assertNull("There is no municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
		assertSame("The county has not changed", county, actualVotingRegion.getCounty());
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a county region's county is changed.
	 */
	@Test
	public final void testUpdateFrom_countyChanged() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final County county = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String name = countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", county}}));
		final County updatedCounty = createCounty(values(new Object[][] {{"Name", "New " + countyName}, {"Type", "Parish"}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", updatedCounty}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is a county", actualVotingRegion.getCounty());
		assertNull("There is no municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
		assertNotSame("The county has changed", county, actualVotingRegion.getCounty());
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a county region is changed to a municipality region.
	 */
	@Test
	public final void testUpdateFrom_countyToMunicipality() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final County county = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String name = countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Region Type", "County"}, {"Name", name}, {"State", state}, {"County", county}}));
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality updatedMunicipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Region Type", "Municipality"}, {"Name", name}, {"State", state}, {"County", county}, {"Municipality", updatedMunicipality}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is a county", actualVotingRegion.getCounty());
		assertNotNull("There is a municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
		assertSame("The county has not changed", county, actualVotingRegion.getCounty());
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a county region becomes a state region.
	 */
	@Test
	public final void testUpdateFrom_countyToState() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final County county = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String name = countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", county}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNull("There is no county", actualVotingRegion.getCounty());
		assertNull("There is no municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a county is added to a municipality.
	 */
	@Test
	public final void testUpdateFrom_municipalityAddCounty() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality municipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}}));
		final String name = municipalityName + " " + municipalityType + ", " + countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Region Type", "Municipality"}, {"Name", name}, {"State", state}, {"Municipality", municipality}}));
		final County updatedCounty = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final Municipality updatedMunicipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Region Type", "Municipality"}, {"Name", name}, {"State", state}, {"County", updatedCounty}, {"Municipality", updatedMunicipality}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is a county", actualVotingRegion.getCounty());
		assertNotNull("There is a municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
		assertNotSame("The municipality has changed", municipality, actualVotingRegion.getMunicipality());
		assertNotNull("The municipality has a county", actualVotingRegion.getMunicipality().getCounty());
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a municipality's county has changed.
	 */
	@Test
	public final void testUpdateFrom_municipalityChangeCounty() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final County county = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality municipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}, {"County", county}}));
		final String name = municipalityName + " " + municipalityType + ", " + countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", county}, {"Municipality", municipality}}));
		final County updatedCounty = createCounty(values(new Object[][] {{"Name", "New " + countyName}, {"Type", "County"}}));
		final Municipality updatedMunicipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", updatedCounty}, {"Municipality", updatedMunicipality}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is a county", actualVotingRegion.getCounty());
		assertNotNull("There is a municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
		assertNotSame("The county has changed", county, actualVotingRegion.getCounty());
		assertNotSame("The municipality has changed", municipality, actualVotingRegion.getMunicipality());
		assertNotSame("The municipality county has changed", county, actualVotingRegion.getMunicipality().getCounty());
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a municipality region is changed to a state region.
	 */
	@Test
	public final void testUpdateFrom_municipalityToState() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final String countyName = "Windsor";
		final County county = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality municipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}, {"County", county}}));
		final String name = municipalityName + " " + municipalityType + ", " + countyName + " County";
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}, {"County", county}, {"Municipality", municipality}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNull("There is no county", actualVotingRegion.getCounty());
		assertNull("There is no municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a state region is changed to a county region.
	 */
	@Test
	public final void testUpdateFrom_stateToCounty() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}}));
		final String countyName = "Windsor";
		final County updatedCounty = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}, {"County", updatedCounty}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is a county", actualVotingRegion.getCounty());
		assertNull("There is no municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a state region is changed to a municipality with a county region.
	 */
	@Test
	public final void testUpdateFrom_stateToCountyMunicipality() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}}));
		final String countyName = "Windsor";
		final County updatedCounty = createCounty(values(new Object[][] {{"Name", countyName}, {"Type", "County"}}));
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality updatedMunicipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}, {"County", updatedCounty}, {"Municipality", updatedMunicipality}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNotNull("There is is a county", actualVotingRegion.getCounty());
		assertNotNull("There is a municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a state region is changed to a municipality w/o county region.
	 */
	@Test
	public final void testUpdateFrom_stateToMunicipality() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}}));
		final String municipalityName = "Hanover";
		final String municipalityType = "Town";
		final Municipality updatedMunicipality = createMunicipality(values(new Object[][] {{"Name", municipalityName}, {"Type", municipalityType}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}, {"Municipality", updatedMunicipality}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNull("There is no county", actualVotingRegion.getCounty());
		assertNotNull("There is a municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#updateFrom(VotingRegion)} for the case where a state region is unchanged.
	 */
	@Test
	public final void testUpdateFrom_stateUnchanged() {
		final State state = createState(values(new Object[][] {{"Abbr", "NH"}, {"Name", "New Hampshire"}}));
		final VotingRegion votingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}}));
		final VotingRegion updatedVotingRegion = createVotingRegion(values(new Object[][] {{"Name", "All Regions"}, {"State", state}}));
		
		final VotingRegion actualVotingRegion = votingRegion.updateFrom(updatedVotingRegion);
		
		assertSame("The returned region is the original", votingRegion, actualVotingRegion);
		assertNull("There is no county", actualVotingRegion.getCounty());
		assertNull("There is no municipality", actualVotingRegion.getMunicipality());
		assertRegion(updatedVotingRegion, actualVotingRegion);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#valueEquals(com.bearcode.ovf.model.common.VotingRegion)} where the names are not equal.
	 */
	@Test
	public final void testValueEquals_differentNames() {
		final VotingRegion votingRegion1 = createVotingRegion(values(new Object[][] {{"Name", "Town of Hanover"}}));
		final VotingRegion votingRegion2 = createVotingRegion(values(new Object[][] {{"Name", "New York City"}}));
		
		final boolean actualValueEquals = votingRegion1.valueEquals(votingRegion2);
		
		assertFalse("The voting region values are not equal", actualValueEquals);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#valueEquals(com.bearcode.ovf.model.common.VotingRegion)} where the states are different.
	 */
	@Test
	public final void testValueEquals_differentStates() {
		final String name = "Town of Hanover";
		final State state1 = createState(values(new Object[][] {{"Abbr", "NH"}}));
		final VotingRegion votingRegion1 = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state1}}));
		final State state2 = createState(values(new Object[][] {{"Abbr", "VT"}}));
		final VotingRegion votingRegion2 = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state2}}));
		
		final boolean actualValueEquals = votingRegion1.valueEquals(votingRegion2);
		
		assertFalse("The voting region values are not equal", actualValueEquals);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#valueEquals(com.bearcode.ovf.model.common.VotingRegion)} where the name is specified for the first region.
	 */
	@Test
	public final void testValueEquals_nameNoName() {
		final VotingRegion votingRegion1 = createVotingRegion(values(new Object[][] {{"Name", "Town of Hanover"}}));
		final VotingRegion votingRegion2 = createVotingRegion(values(new Object[][] {}));
		
		final boolean actualValueEquals = votingRegion1.valueEquals(votingRegion2);
		
		assertFalse("The voting region values are not equal", actualValueEquals);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#valueEquals(com.bearcode.ovf.model.common.VotingRegion)} where no values are specified in either region.
	 */
	@Test
	public final void testValueEquals_noValues() {
		final VotingRegion votingRegion1 = createVotingRegion(values(new Object[][] {}));
		final VotingRegion votingRegion2 = createVotingRegion(values(new Object[][] {}));
		
		final boolean actualValueEquals = votingRegion1.valueEquals(votingRegion2);
		
		assertTrue("The voting region values are equal", actualValueEquals);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#valueEquals(com.bearcode.ovf.model.common.VotingRegion)} where the names are equal.
	 */
	@Test
	public final void testValueEquals_sameNames() {
		final String name = "Town of Hanover";
		final VotingRegion votingRegion1 = createVotingRegion(values(new Object[][] {{"Name", name}}));
		final VotingRegion votingRegion2 = createVotingRegion(values(new Object[][] {{"Name", name}}));
		
		final boolean actualValueEquals = votingRegion1.valueEquals(votingRegion2);
		
		assertTrue("The voting region values are equal", actualValueEquals);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#valueEquals(com.bearcode.ovf.model.common.VotingRegion)} where the states are the same.
	 */
	@Test
	public final void testValueEquals_sameStates() {
		final String name = "Town of Hanover";
		final State state1 = createState(values(new Object[][] {{"Abbr", "NH"}}));
		final VotingRegion votingRegion1 = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state1}}));
		final State state2 = createState(values(new Object[][] {{"Abbr", "NH"}}));
		final VotingRegion votingRegion2 = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state2}}));
		
		final boolean actualValueEquals = votingRegion1.valueEquals(votingRegion2);
		
		assertTrue("The voting region values are equal", actualValueEquals);
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.model.common.VotingRegion#valueEquals(com.bearcode.ovf.model.common.VotingRegion)} where only the first region has a state.
	 */
	@Test
	public final void testValueEquals_stateNoState() {
		final String name = "Town of Hanover";
		final State state1 = createState(values(new Object[][] {{"Abbr", "NH"}}));
		final VotingRegion votingRegion1 = createVotingRegion(values(new Object[][] {{"Name", name}, {"State", state1}}));
		final VotingRegion votingRegion2 = createVotingRegion(values(new Object[][] {{"Name", name}}));
		
		final boolean actualValueEquals = votingRegion1.valueEquals(votingRegion2);
		
		assertFalse("The voting region values are not equal", actualValueEquals);
	}
}
