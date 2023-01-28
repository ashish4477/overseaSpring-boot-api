/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;

/**
 * Helper class to provide methods for use in tests for the com.bearcode.ovf.model.common package.
 * @author Ian
 *
 */
public final class CommonTestHelper {

	/**
	 * Custom assertion to ensure that the address read in from the file matches the expected one.
	 * @param expectedAddress the expected address.
	 * @param actualAddress the actual address.
	 */
	public final static void assertAddress(final Address expectedAddress, final Address actualAddress) {
		assertEquals("Address to", expectedAddress.getAddressTo(), actualAddress.getAddressTo());
		assertEquals("Street 1", expectedAddress.getStreet1(), actualAddress.getStreet1());
		assertEquals("Street 2", expectedAddress.getStreet2(), actualAddress.getStreet2());
		assertEquals("City", expectedAddress.getCity(), actualAddress.getCity());
		assertEquals("State", expectedAddress.getState(), actualAddress.getState());
		assertEquals("ZIP", expectedAddress.getZip(), actualAddress.getZip());
		assertEquals("ZIP+4", expectedAddress.getZip4(), actualAddress.getZip4());
    }

	/**
	 * Custom assertion to ensure that the county read in from the file matches the expected one.
	 * @param expectedCounty the expected county.
	 * @param actualCounty the actual county.
	 */
	public final static void assertCounty(final County expectedCounty, final County actualCounty) {
	    assertEquals("The county is expected", expectedCounty != null, (actualCounty != null) && !actualCounty.getType().isEmpty());
	    if (expectedCounty != null) {
	    	assertEquals("County type", expectedCounty.getType(), actualCounty.getType());
	    	assertEquals("County name", expectedCounty.getName(), actualCounty.getName());
	    }
    }

	/**
	 * Custom assertion to ensure that the EOD read in from the file matches the expected one.
	 * @param expectedEod the expected EOD built locally.
	 * @param actualEod the actual EOD from the file.
	 */
	public final static void assertEod(final Collection<LocalOfficial> expectedEod,
            final Collection<LocalOfficial> actualEod) {
		assertEquals("The correct number of local officials were read in", expectedEod.size(), actualEod.size());
		
		for (final LocalOfficial actualLeo : actualEod) {
			final LocalOfficial expectedLeo = findMatchingLeo(actualLeo, expectedEod);
			assertLeo(expectedLeo, actualLeo);
		}
    }

	/**
	 * Custom assertion to ensure that the LEO read in from the file matches the expected one.
	 * @param expectedLeo the expected LEO.
	 * @param actualLeo the actual LEO.
	 */
	public final static void assertLeo(final LocalOfficial expectedLeo, final LocalOfficial actualLeo) {
		assertNotNull("There is an expected LEO for the actual LEO", expectedLeo);

		assertRegion(expectedLeo.getRegion(), actualLeo.getRegion());
	    assertAddress(expectedLeo.getPhysical(), actualLeo.getPhysical());
	    assertAddress(expectedLeo.getMailing(), actualLeo.getMailing());
	    assertEquals("Website", expectedLeo.getWebsite(), actualLeo.getWebsite());
	    assertEquals("Hours", expectedLeo.getHours(), actualLeo.getHours());
	    assertEquals("Further instruction", expectedLeo.getFurtherInstruction(), actualLeo.getFurtherInstruction());
	    // While the updated value is in the file, it will always be different than what is going to be produced here.
	    assertOfficers(expectedLeo.getOfficers(), actualLeo.getOfficers());
    }

	/**
	 * Private assertion to ensure that the municipality read in from the file matches the expected one.
	 * @param expectedMunicipality the expected municipality.
	 * @param actualMunicipality the actual municipality.
	 */
	public final static void assertMunicipality(final Municipality expectedMunicipality,
            final Municipality actualMunicipality) {
	    assertEquals("The municipality is expected", expectedMunicipality != null, (actualMunicipality != null) && !actualMunicipality.getType().isEmpty());
	    if (expectedMunicipality != null) {
	    	assertEquals("Municipality type", expectedMunicipality.getType(), actualMunicipality.getType());
	    	assertEquals("Municipality name", expectedMunicipality.getName(), actualMunicipality.getName());
	    }
    }

	/**
	 * Custom assertion to ensure that the officer read in from the file matches the expected one.
	 * @param expectedOfficer the expected officer.
	 * @param actualOfficer the actual officer.
	 */
	public final static void assertOfficer(final Officer expectedOfficer, final Officer actualOfficer) {
	    assertEquals("Office name", expectedOfficer.getOfficeName(), actualOfficer.getOfficeName());
	    assertEquals("First name", expectedOfficer.getFirstName(), actualOfficer.getFirstName());
	    assertEquals("Last name", expectedOfficer.getLastName(), actualOfficer.getLastName());
	    assertEquals("Phone", expectedOfficer.getPhone(), actualOfficer.getPhone());
	    assertEquals("Fax", expectedOfficer.getFax(), actualOfficer.getFax());
	    assertEquals("Email", expectedOfficer.getEmail(), actualOfficer.getEmail());
    }

	/**
	 * Custom assertion to ensure that the officers read in from the file match the expected ones.
	 * @param expectedOfficers the expected officers.
	 * @param actualOfficers the actual officers.
	 */
	public final static void assertOfficers(final List<Officer> expectedOfficers, final List<Officer> actualOfficers) {
	    assertEquals("The correct number of officers were read in", expectedOfficers.size(), actualOfficers.size());
	    for (int idx = 0; idx < expectedOfficers.size(); ++idx) {
	    	final Officer expectedOfficer = expectedOfficers.get(idx);
	    	final Officer actualOfficer = actualOfficers.get(idx);
	    	assertOfficer(expectedOfficer, actualOfficer);
	    }
    }

	/**
	 * Custom assertion to ensure that the region read in from the file matches the expected one.
	 * @param expectedRegion the expected region.
	 * @param actualRegion the actual region.
	 */
	public final static void assertRegion(final VotingRegion expectedRegion, final VotingRegion actualRegion) {
		assertEquals("Region name", expectedRegion.getName(), actualRegion.getName());
		assertCounty(expectedRegion.getCounty(), actualRegion.getCounty());
		assertMunicipality(expectedRegion.getMunicipality(), actualRegion.getMunicipality());
    }

	/**
	 * Builds a new address.
	 * @param leoNumber the number of the local official within the state.
	 * @param officerNumber the optional number of officer within the region (0 means this belongs to the official).
	 * @param type the type of address.
	 * @param region the region of the address.
	 * @return the address.
	 */
	public final static Address buildAddress(final int leoNumber, final int officerNumber, final String type, final VotingRegion region) {
	    final Address address = new Address();
	    address.setAddressTo("LEO " + leoNumber + ((officerNumber == 0) ? "" : (" Officer " + officerNumber)));
	    address.setStreet1(leoNumber + " Street");
	    address.setStreet2((officerNumber == 0) ? "" : "Office " + officerNumber);
	    final Municipality municipality = region.getMunicipality();
		if (municipality == null) {
			final County county = region.getCounty();
			if (county == null) {
				address.setCity("State");
			} else {
				address.setCity(county.getName() + " " + county.getType());
			}
	    } else {
	    	address.setCity(municipality.getName() + " " + municipality.getType());
	    }
		address.setState(region.getState().getAbbr());
		address.setZip(String.format("%03d%02d", 1, leoNumber));
		address.setZip4(String.format("%04d", officerNumber));
	    return address;
    }

	/**
	 * Builds a new collection of local officials. The officials belong to the following types of regions:
	 * <ul>
	 * <li>the whole state,</li>
	 * <li>a single county within the state,</li>
	 * <li>a municipality without a county,</li>
	 * <li>a municipality within a county.</li>
	 * </ul>
	 * @param state the state for the local officials.
	 * @return the local officials.
	 */
	public final static Collection<LocalOfficial> buildEod(final State state) {
	    final Collection<LocalOfficial> eod = new ArrayList<LocalOfficial>();
	    eod.add(buildLeo(1, state, null, null, null, null));
	    eod.add(buildLeo(2, state, "County", "County", null, null));
	    eod.add(buildLeo(3, state, null, null, "City", "City"));
	    eod.add(buildLeo(4, state, "County", "Town", "Town", "Town"));  
	    return eod;
    }

	/**
	 * Builds a new local official belonging to the specified type of region.
	 * @param leoNumber the number of this local official within the state.
	 * @param state the state.
	 * @param countyType the type of county (<code>null</code> if no county).
	 * @param countyName the name of the county (<code>null</code> if no county).
	 * @param municipalityType the type of municipality (<code>null</code> if no municipality).
	 * @param municipalityName the name of the municipality (<code>null</code> if no municipality).
	 * @return the local official.
	 */
	public final static LocalOfficial buildLeo(final int leoNumber, final State state, final String countyType,
            final String countyName, final String municipalityType, final String municipalityName) {
	    final LocalOfficial leo = new LocalOfficial();
	    final VotingRegion region = buildRegion(state, countyType, countyName, municipalityType, municipalityName);
		leo.setRegion(region);
	    leo.setPhysical(buildAddress(leoNumber, 0, "Physicial", region));
	    leo.setMailing(buildAddress(leoNumber, 0, "Mailing", region));
	    leo.setGeneralEmail("general-email-" + leoNumber + "@" + state.getAbbr() + ".gov");
	    leo.setWebsite("leo-" + leoNumber + "." + state.getAbbr() + ".gov");
	    leo.setHours((7 + leoNumber) + "AM to " + (3 + leoNumber) + "PM");
	    leo.setFurtherInstruction("Go see " + leoNumber);
	    leo.setUpdated(new Date());
	    leo.setOfficers(buildOfficers(leoNumber, region));
		return leo;
    }

	/**
	 * Builds an officer for the local official with the specified number.
	 * @param leoNumber the number of this local official within the state.
	 * @param officerNumber the number of this officer within the region.
	 * @param region the region for the officer.
	 * @return the officer.
	 */
	public final static Officer buildOfficer(final int leoNumber, final int officerNumber, final VotingRegion region) {
	    final Officer officer = new Officer();
	    officer.setOfficeName(region.getFullName() + " #" + leoNumber + "." + officerNumber);
	    officer.setFirstName(leoNumber + "-" + officerNumber);
	    officer.setLastName(region.getFullName().replace(' ', '-').replace(',', '_'));
	    officer.setPhone(String.format("1-%3d-%03d-%04d", region.getState().getFipsCode() % 1000, leoNumber, officerNumber));
	    officer.setFax(String.format("1-%3d-%03d-1%03d", region.getState().getFipsCode() % 1000, leoNumber, officerNumber));
	    officer.setEmail("officer-" + leoNumber + "-" + officerNumber + "@" + region.getState().getAbbr() + ".gov");
	    return officer;
    }

	/**
	 * Builds the officers for the local official with the specified number. For simplicity, just builds one.
	 * @param leoNumber the number of this local official within the state.
	 * @param region the region for the officers.
	 * @return the list of officers.
	 */
	public final static List<Officer> buildOfficers(final int leoNumber, final VotingRegion region) {
	    final List<Officer> officers = new ArrayList<Officer>();
		officers.add(buildOfficer(leoNumber, 1, region));
	    return officers;
    }

	/**
	 * Builds a new voting region based on the parameters.
	 * @param state the state.
	 * @param countyType the type of county (<code>null</code> if no county).
	 * @param countyName the name of the county (<code>null</code> if no county).
	 * @param municipalityType the type of municipality (<code>null</code> if no municipality).
	 * @param municipalityName the name of the municipality (<code>null</code> if no municipality).
	 * @return the region.
	 */
	public final static VotingRegion buildRegion(final State state, final String countyType,
            final String countyName, final String municipalityType, final String municipalityName) {
	    final VotingRegion region = new VotingRegion();
	    region.setState(state);
	    County county = null;
	    String name = null;
	    if (countyType != null) {
	    	county = new County();
	    	county.setState(state);
	    	county.setName(countyName);
	    	county.setType(countyType);
			region.setCounty(county);
			name = countyName + " " + countyType;
	    }
		Municipality municipality = null;
		if (municipalityType != null) {
			municipality = new Municipality();
			municipality.setState(state);
			municipality.setCounty(county);
			municipality.setName(municipalityName);
			municipality.setType(municipalityType);
			region.setMunicipality(municipality);
			if (name == null) {
				name = municipalityName + " " + municipalityType;
			} else {
				name = municipalityName + " " + municipalityType + ", " + name;
			}
		}
		if (name == null) {
			name = "All Regions";
		}
		region.setName(name);
	    return region;
    }

	/**
	 * Builds a new state for testing.
	 * @return the state.
	 */
	public final static State buildState() {
		final State state = new State();
		state.setAbbr("SN");
		state.setName("State Name");
		state.setFipsCode(9999);
		return state;
    }

	/**
	 * Creates a county from the input values.
	 * @param values the values map.
	 * @return the county.
	 */
	public final static County createCounty(final Map<String, Object> values) {
		final County county = new County();
		county.setName((String) values.get("Name"));
		county.setState((State) values.get("State"));
		county.setType((String) values.get("Type"));
		return county;
	}	
	/**
	 * Creates a municipality from the input values.
	 * @param values the values map.
	 * @return the municipality.
	 */
	public final static Municipality createMunicipality(final Map<String, Object> values) {
		final Municipality municipality = new Municipality();
		municipality.setCounty((County) values.get("County"));
		municipality.setName((String) values.get("Name"));
		municipality.setState((State) values.get("State"));
		municipality.setType((String) values.get("Type"));
		return municipality;
	}

	/**
	 * Creates a state from the input values.
	 * @param values the values map.
	 * @return the state.
	 */
	public final static State createState(final Map<String, Object> values) {
		final State state = new State();
		state.setName((String) values.get("Name"));
		state.setAbbr((String) values.get("Abbr"));
		final Integer fipsCode = (Integer) values.get("FIPS Code");
		if (fipsCode != null) {
			state.setFipsCode(fipsCode);
		}
		return state;
	}

	/**
	 * Creates a voting region from the input values.
	 * @param values the values map.
	 * @return the voting region.
	 */
	public final static VotingRegion createVotingRegion(final Map<String, Object> values) {
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setName((String) values.get("Name"));
		votingRegion.setState((State) values.get("State"));
		votingRegion.setCounty((County) values.get("County"));
		votingRegion.setMunicipality((Municipality) values.get("Municipality"));
		return votingRegion;
	}
	
	/**
	 * Finds the matching (by general email) local official for the actual one.
	 * @param actualLeo the actual local official read in.
	 * @param expectedEod the expected local officials built locally.
	 * @return the expected LEO or <code>null</code> if no match can be found.
	 */
	public final static LocalOfficial findMatchingLeo(final LocalOfficial actualLeo,
            final Collection<LocalOfficial> expectedEod) {
	    for (final LocalOfficial expectedLeo : expectedEod) {
	    	if (expectedLeo.getGeneralEmail().equals(actualLeo.getGeneralEmail())) {
	    		return expectedLeo;
	    	}
	    }
	    return null;
    }
	
	/**
	 * Creates a values map from the input objects.
	 * @param objects the objects (pairs of string keys and object values).
	 * @return the values maps.
	 */
	public final static Map<String, Object> values(final Object[][] objects) {
		final Map<String, Object> valuesMap = new HashMap<String, Object>();
		for (final Object[] pair : objects) {
			valuesMap.put((String) pair[0], pair[1]);
		}
		return valuesMap;
	}
	
	/**
	 * Private constructor to ensure that the class is not instantiated.
	 */
	private CommonTestHelper() {
		throw new UnsupportedOperationException("This class should not be instantiated.");
	}
}
