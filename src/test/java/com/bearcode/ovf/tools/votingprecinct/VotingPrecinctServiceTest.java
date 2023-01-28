/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.SupportsStatesOrVotingRegionsCheck;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link SupportsStatesOrVotingRegionsCheck} test for {@link VotingPrecinctService}.
 * 
 * @author IanBrown
 * 
 * @since Jun 4, 2012
 * @version Oct 9, 2012
 */
public final class VotingPrecinctServiceTest extends SupportsStatesOrVotingRegionsCheck<VotingPrecinctService, StreetSegmentFinder> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#areRestrictedAddressesRequired(String, String)} for the
	 * case where no finder matches the requested state and voting region.
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testAreRestrictedAddressesRequired_noFinder() {
		final String stateAbbreviation = "ST";
		final String votingRegionName = "Voting Region Name";
		replayAll();

		final boolean actualRestrictedAddressesRequired = getSupportsStatesOrVotingRegions().areRestrictedAddressesRequired(
				stateAbbreviation, votingRegionName);

		assertFalse("Restricted addresses are not required", actualRestrictedAddressesRequired);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#areRestrictedAddressesRequired(String, String)} for the
	 * case where the finder does not require restrict addresses.
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testAreRestrictedAddressesRequired_notRequired() {
		final String stateAbbreviation = "ST";
		final String votingRegionName = "Voting Region Name";
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, votingRegionName)).andReturn(true).anyTimes();
		EasyMock.expect(streetSegmentFinder.areRestrictedAddressesRequired()).andReturn(false);
		replayAll();

		final boolean actualRestrictedAddressesRequired = getSupportsStatesOrVotingRegions().areRestrictedAddressesRequired(
				stateAbbreviation, votingRegionName);

		assertFalse("Restricted addresses are not required", actualRestrictedAddressesRequired);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#areRestrictedAddressesRequired(String, String)} for the
	 * case where the finder does require restrict addresses.
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testAreRestrictedAddressesRequired_required() {
		final String stateAbbreviation = "ST";
		final String votingRegionName = "Voting Region Name";
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, votingRegionName)).andReturn(true).anyTimes();
		EasyMock.expect(streetSegmentFinder.areRestrictedAddressesRequired()).andReturn(true);
		replayAll();

		final boolean actualRestrictedAddressesRequired = getSupportsStatesOrVotingRegions().areRestrictedAddressesRequired(
				stateAbbreviation, votingRegionName);

		assertTrue("Restricted addresses are required", actualRestrictedAddressesRequired);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findCitiesByVotingRegion(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 28, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByVotingRegion() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region County";
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(streetSegmentFinder.findCitiesByVotingRegion(stateAbbreviation, votingRegionName)).andReturn(cities);
		replayAll();

		final List<String> actualCities = getSupportsStatesOrVotingRegions().findCitiesByVotingRegion(stateAbbreviation,
				votingRegionName);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findCitiesByVotingRegion(String, String)} for a voting region that isn't ready.
	 * 
	 * @author IanBrown
	 * @since Jul 28, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByVotingRegion_notReady() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region County";
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(stateAbbreviation, votingRegionName)).andReturn(false);
		replayAll();

		final List<String> actualCities = getSupportsStatesOrVotingRegions().findCitiesByVotingRegion(stateAbbreviation,
				votingRegionName);

		assertNull("No cities are returned", actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findCitiesByVotingRegion(String, String)} for a voting region that isn't supported.
	 * 
	 * @author IanBrown
	 * @since Jul 28, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByVotingRegion_notSupported() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region County";
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, votingRegionName)).andReturn(false);
		replayAll();

		final List<String> actualCities = getSupportsStatesOrVotingRegions().findCitiesByVotingRegion(stateAbbreviation,
				votingRegionName);

		assertNull("No cities are returned", actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findCitiesByZip(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByZip() {
		final String stateAbbreviation = "SA";
		final String zip = "86420";
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(stateAbbreviation, null)).andReturn(true);
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(streetSegmentFinder.findCitiesByZip(stateAbbreviation, zip)).andReturn(cities);
		replayAll();

		final List<String> actualCities = getSupportsStatesOrVotingRegions().findCitiesByZip(stateAbbreviation, zip);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findStreetNamesByCity(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetNamesByCity() {
		final String stateAbbreviation = "SA";
		final String city = "City";
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(stateAbbreviation, null)).andReturn(true);
		final String streetName = "Street Name";
		final List<String> streetNames = Arrays.asList(streetName);
		EasyMock.expect(streetSegmentFinder.findStreetNamesByCity(stateAbbreviation, city)).andReturn(streetNames);
		replayAll();

		final List<String> actualStreetNames = getSupportsStatesOrVotingRegions().findStreetNamesByCity(stateAbbreviation, city);

		assertSame("The street names are returned", streetNames, actualStreetNames);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findStreetSegments(String, State)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegments() {
		final String zipCode = "12435";
		final State votingState = new State();
		final String stateAbbreviation = "MN";
		votingState.setAbbr(stateAbbreviation);
		final String stateName = "Minnesota";
		votingState.setName(stateName);
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(stateAbbreviation, null)).andReturn(true);
		final VipStreetSegment streetSegment = createMock(stateAbbreviation, VipStreetSegment.class);
		final List<VipStreetSegment> streetSegments = Arrays.asList(streetSegment);
		EasyMock.expect(streetSegmentFinder.findStreetSegments(votingState.getAbbr(), zipCode)).andReturn(streetSegments);
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getSupportsStatesOrVotingRegions().findStreetSegments(zipCode,
				votingState);

		assertSame("The street segments are returned", streetSegments, actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findStreetSegments(String, State)} for the
	 * case where the voting state is not supported.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegments_invalidVotingState() {
		final String zipCode = "12435";
		final State votingState = new State();
		votingState.setAbbr("UN");
		votingState.setName("Unknown");
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getSupportsStatesOrVotingRegions().findStreetSegments(zipCode,
				votingState);

		assertNull("No street segments are returned", actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findStreetSegments(String, State)} for the
	 * case where no voting state is provided.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegments_noVotingState() {
		final String zipCode = "12435";
		final State votingState = null;
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getSupportsStatesOrVotingRegions().findStreetSegments(zipCode,
				votingState);

		assertNull("No street segments are returned", actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findStreetSegments(String, State)} for the
	 * case where no ZIP code is provided.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegments_noZipCode() {
		final String zipCode = null;
		final State votingState = null;
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getSupportsStatesOrVotingRegions().findStreetSegments(zipCode,
				votingState);

		assertNull("No street segments are returned", actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findZipCodes(State)} for the case where no
	 * voting state is provided.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindZipCode_noVotingState() {
		final State votingState = null;
		replayAll();

		final List<String> actualStreetSegments = getSupportsStatesOrVotingRegions().findZipCodes(votingState);

		assertNull("No ZIP codes are returned", actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findZipCodes(State)}.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindZipCodes() {
		final State votingState = new State();
		final String stateAbbreviation = "MN";
		votingState.setAbbr(stateAbbreviation);
		votingState.setName("Minnesota");
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(stateAbbreviation, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(stateAbbreviation, null)).andReturn(true);
		final String zipCode = "12435";
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(streetSegmentFinder.findZipCodes(stateAbbreviation)).andReturn(zipCodes);
		replayAll();

		final List<String> actualZipCodes = getSupportsStatesOrVotingRegions().findZipCodes(votingState);

		assertSame("The ZIP codes are returned", zipCodes, actualZipCodes);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findZipCodes(State)} for the case where
	 * the voting state is not supported.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindZipCodes_invalidVotingState() {
		final State votingState = new State();
		votingState.setAbbr("UN");
		votingState.setName("Unknown");
		replayAll();

		final List<String> actualZipCodes = getSupportsStatesOrVotingRegions().findZipCodes(votingState);

		assertNull("No ZIP codes are returned", actualZipCodes);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#fixAddress(UserAddress, VipStreetSegment)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFixAddress() {
		final UserAddress address = createMock("Address", UserAddress.class);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress);
		final String state = "MN";
		EasyMock.expect(nonHouseAddress.getState()).andReturn(state);
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(state, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(state, null)).andReturn(true);
		streetSegmentFinder.fixAddress(address, streetSegment);
		replayAll();

		getSupportsStatesOrVotingRegions().fixAddress(address, streetSegment);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#fixAddress(UserAddress, VipStreetSegment)}
	 * for the case where the state isn't supported.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFixAddress_stateNotSupported() {
		final UserAddress address = createMock("Address", UserAddress.class);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress);
		final String state = "ST";
		EasyMock.expect(nonHouseAddress.getState()).andReturn(state);
		replayAll();

		getSupportsStatesOrVotingRegions().fixAddress(address, streetSegment);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, State)} for
	 * the case where the address does not provide a state.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressState_noState() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.STREET);
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, (State) null);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, State)} for
	 * the case where there are no street segment finders.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressState_noStreetSegmentFinders() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.STREET);
		final State votingState = new State();
		votingState.setAbbr("UN");
		votingState.setName("Unknown");
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, votingState);

		assertNotNull("The address is valid", actualValidAddress);
		assertSame("The validated address is set", address, actualValidAddress.getValidatedAddress());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, State)} for
	 * the case where the address is not valid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressState_notValid() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.STREET);
		final State votingState = new State();
		final String abbr = "MN";
		votingState.setAbbr(abbr);
		final String name = "Minnesota";
		votingState.setName(name);
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(abbr, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(abbr, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.findStreetSegment(address)).andReturn(null);
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, votingState);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, State)} for
	 * the case where the state is not one that requires validation.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressState_noValidation() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.STREET);
		final State votingState = new State();
		votingState.setAbbr("UN");
		votingState.setName("Unknown");
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported("UN", null)).andReturn(false);
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, votingState);

		assertNotNull("The address is valid", actualValidAddress);
		assertSame("The validated address is set", address, actualValidAddress.getValidatedAddress());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, State)} for
	 * the case where the address is valid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressState_valid() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.STREET);
		final State votingState = new State();
		final String abbr = "MN";
		votingState.setAbbr(abbr);
		final String name = "Minnesota";
		votingState.setName(name);
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(abbr, null)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(abbr, null)).andReturn(true);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(streetSegmentFinder.findStreetSegment(address)).andReturn(streetSegment);
		streetSegmentFinder.fixAddress(address, streetSegment);
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, votingState);

		assertNotNull("The address is valid", actualValidAddress);
		assertSame("The address is validated", address, actualValidAddress.getValidatedAddress());
		assertSame("The street segment is returned", streetSegment, actualValidAddress.getStreetSegment());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, State)} for
	 * the case where the address is not a valid type of address.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressState_wrongType() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.OVERSEAS);
		final State votingState = new State();
		votingState.setAbbr("UN");
		votingState.setName("Unknown");
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, votingState);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, VotingRegion)} for the case
	 * where the address is not valid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Sep 17, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressVotingRegion_notValid() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.STREET);
		final State votingState = new State();
		final String abbr = "FL";
		votingState.setAbbr(abbr);
		final String name = "Florida";
		votingState.setName(name);
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setState(votingState);
		final String votingRegionName = "Okaloosa County";
		votingRegion.setName(votingRegionName);
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(abbr, votingRegionName)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(abbr, votingRegionName)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.findStreetSegment(address)).andReturn(null);
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, votingRegion);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, VotingRegion)} for the case
	 * where the address is valid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Sep 17, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testValidateAddressUserAddressVotingRegion_valid() {
		final WizardResultAddress address = new WizardResultAddress(AddressType.STREET);
		final State votingState = new State();
		final String abbr = "FL";
		votingState.setAbbr(abbr);
		final String name = "Florida";
		votingState.setName(name);
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setState(votingState);
		final String votingRegionName = "Okaloosa County";
		votingRegion.setName(votingRegionName);
		final StreetSegmentFinder streetSegmentFinder = createForStateOrVotingRegion();
		final List<StreetSegmentFinder> streetSegmentFinders = Arrays.asList(streetSegmentFinder);
		getSupportsStatesOrVotingRegions().setForStateOrVotingRegions(streetSegmentFinders);
		EasyMock.expect(streetSegmentFinder.isSupported(abbr, votingRegionName)).andReturn(true);
		EasyMock.expect(streetSegmentFinder.isReady(abbr, votingRegionName)).andReturn(true);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(streetSegmentFinder.findStreetSegment(address)).andReturn(streetSegment);
		streetSegmentFinder.fixAddress(address, streetSegment);
		replayAll();

		final ValidAddress actualValidAddress = getSupportsStatesOrVotingRegions().validateAddress(address, votingRegion);

		assertNotNull("The address is valid", actualValidAddress);
		assertSame("The address is validated", address, actualValidAddress.getValidatedAddress());
		assertSame("The street segment is returned", streetSegment, actualValidAddress.getStreetSegment());
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final StreetSegmentFinder createForStateOrVotingRegion() {
		final StreetSegmentFinder streetSegmentFinder = createMock("StreetSegmentFinder", StreetSegmentFinder.class);
		return streetSegmentFinder;
	}

	/** {@inheritDoc} */
	@Override
	protected final VotingPrecinctService createSupportsStatesOrVotingRegions() {
		final VotingPrecinctService votingPrecinctService = new VotingPrecinctService();
		return votingPrecinctService;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpSpecificSupportsStatesOrVotingRegions() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownSpecificSupportsStatesOrVotingRegions() {
	}
}
