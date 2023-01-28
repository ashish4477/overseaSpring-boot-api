/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.ForStateOrVotingRegionCheck;

/**
 * Abstract extended {@link ForStateOrVotingRegionCheck} test for implementations of {@link StreetSegmentFinder}.
 * 
 * @author IanBrown
 * 
 * @param <F>
 *            the type of street segment finder to test.
 * @since Jun 5, 2012
 * @version Oct 9, 2012
 */
public abstract class StreetSegmentFinderCheck<F extends StreetSegmentFinder> extends ForStateOrVotingRegionCheck<F> {

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#areRestrictedAddressesRequired()} for the
	 * case where they are not required.
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testAreRestrictedAddressesRequired_notRequired() {
		setRestrictedAddressesRequired(false);
		setUpValidStreetSegmentFinder();
		replayAll();

		final boolean actualRestrictedAddressesRequired = getForStateOrVotingRegion().areRestrictedAddressesRequired();

		assertFalse("Restricted addresses are not required", actualRestrictedAddressesRequired);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#areRestrictedAddressesRequired()} for the
	 * case where they are required.
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testAreRestrictedAddressesRequired_required() {
		if (!canRestrictedAddressesBeRequired()) {
			return;
		}

		setRestrictedAddressesRequired(true);
		setUpValidStreetSegmentFinder();
		replayAll();

		final boolean actualRestrictedAddressesRequired = getForStateOrVotingRegion().areRestrictedAddressesRequired();

		assertTrue("Restricted addresses are required", actualRestrictedAddressesRequired);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findCitiesByVotingRegion(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByVotingRegion() {
		final String stateAbbreviation = selectValidState();
		final String votingRegionName = selectValidVotingRegion();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualCities = getForStateOrVotingRegion().findCitiesByVotingRegion(stateAbbreviation, votingRegionName);

		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There are cities for the voting region", actualCities.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findCitiesByVotingRegion(String, String)}
	 * for an invalid state.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByVotingRegion_invalidState() {
		final String stateAbbreviation = selectInvalidState();
		final String votingRegionName = selectValidVotingRegion();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualCities = getForStateOrVotingRegion().findCitiesByVotingRegion(stateAbbreviation, votingRegionName);

		assertNotNull("A list of cities is returned", actualCities);
		assertTrue("There are no cities for the voting region", actualCities.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findCitiesByVotingRegion(String, String)}
	 * for an invalid voting region.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByVotingRegion_invalidVotingRegion() {
		final String stateAbbreviation = selectValidState();
		final String votingRegionName = selectInvalidVotingRegion();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualCities = getForStateOrVotingRegion().findCitiesByVotingRegion(stateAbbreviation, votingRegionName);

		assertNotNull("A list of cities is returned", actualCities);
		assertTrue("There are no cities for the voting region", actualCities.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findCitiesByZip(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByZip() {
		final String stateAbbreviation = selectValidState();
		final String zip = selectValidZipCode();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualCities = getForStateOrVotingRegion().findCitiesByZip(stateAbbreviation, zip);

		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There are cities for the ZIP code", actualCities.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findCitiesByZip(String, String)} for an
	 * invalid state.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByZip_invalidState() {
		final String stateAbbreviation = selectInvalidState();
		final String zip = selectValidZipCode();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualCities = getForStateOrVotingRegion().findCitiesByZip(stateAbbreviation, zip);

		assertNotNull("A list of cities is returned", actualCities);
		assertTrue("There are no cities for the ZIP code", actualCities.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findCitiesByZip(String, String)} for an
	 * invalid ZIP code.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesByZip_invalidZip() {
		final String stateAbbreviation = selectValidState();
		final String zip = selectInvalidZipCode();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualCities = getForStateOrVotingRegion().findCitiesByZip(stateAbbreviation, zip);

		assertNotNull("A list of cities is returned", actualCities);
		assertTrue("There are no cities for the ZIP code", actualCities.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetNamesByCity(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetNamesByCity() {
		if (!isSupportedStreetNames()) {
			return;
		}

		final String stateAbbreviation = selectValidState();
		final String city = selectValidCity();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualStreetNames = getForStateOrVotingRegion().findStreetNamesByCity(stateAbbreviation, city);

		assertNotNull("A list of street names is returned", actualStreetNames);
		assertFalse("There are treet names for the city", actualStreetNames.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetNamesByCity(String, String)} for
	 * an invalid city.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetNamesByCity_invalidCity() {
		if (!isSupportedStreetNames()) {
			return;
		}

		final String stateAbbreviation = selectValidState();
		final String city = selectInvalidCity();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualStreetNames = getForStateOrVotingRegion().findStreetNamesByCity(stateAbbreviation, city);

		assertNotNull("A list of street names is returned", actualStreetNames);
		assertTrue("There are no street names for the city", actualStreetNames.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetNamesByCity(String, String)} for
	 * an invalid state.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetNamesByCity_invalidState() {
		if (!isSupportedStreetNames()) {
			return;
		}

		final String stateAbbreviation = selectInvalidState();
		final String city = selectValidCity();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualStreetNames = getForStateOrVotingRegion().findStreetNamesByCity(stateAbbreviation, city);

		assertNotNull("A list of street names is returned", actualStreetNames);
		assertTrue("There are no street names for the city", actualStreetNames.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetSegment(UserAddress)} for the case
	 * where the address is not valid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 5, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegment_invalidAddress() {
		final UserAddress address = createInvalidAddress();
		setUpValidStreetSegmentFinder();
		replayAll();

		final VipStreetSegment actualStreetSegment = getForStateOrVotingRegion().findStreetSegment(address);

		assertNull("No street segment is returned", actualStreetSegment);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetSegment(UserAddress)} for the case
	 * where the finder is not set up to properly access its data.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 5, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegment_invalidSetup() {
		if (canHaveInvalidSetup()) {
			final UserAddress address = createValidAddress();
			setUpInvalidStreetSegmentFinder();
			replayAll();

			VipStreetSegment actualStreetSegment = getForStateOrVotingRegion().findStreetSegment(address);
			
			assertNull("No street segment can be found", actualStreetSegment);
			verifyAll();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetSegment(UserAddress)} for the case
	 * where the address is valid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 5, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegment_validAddress() {
		final UserAddress address = createValidAddress();
		createNormalizedAddress();
		setUpValidStreetSegmentFinder();
		replayAll();

		final VipStreetSegment actualStreetSegment = getForStateOrVotingRegion().findStreetSegment(address);

		assertNotNull("A street segment is returned", actualStreetSegment);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetSegments(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegments() {
		if (!isSupportedStreetNames()) {
			return;
		}

		final String stateAbbreviation = selectValidState();
		final String zipCode = selectValidZipCode();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getForStateOrVotingRegion().findStreetSegments(stateAbbreviation,
				zipCode);

		assertNotNull("A list of street segments is returned", actualStreetSegments);
		assertFalse("There are street segments for the ZIP code", actualStreetSegments.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findStreetSegments(String, String)} for an
	 * invalid ZIP.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindStreetSegments_invalidZip() {
		if (!isSupportedStreetNames()) {
			return;
		}

		final String stateAbbreviation = selectValidState();
		final String zipCode = selectInvalidZipCode();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getForStateOrVotingRegion().findStreetSegments(stateAbbreviation,
				zipCode);

		assertNotNull("A list of street segments is returned", actualStreetSegments);
		assertTrue("There are no street segments for the ZIP code", actualStreetSegments.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findZipCodes(String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindZipCodes() {
		final String stateAbbreviation = selectValidState();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualZipCodes = getForStateOrVotingRegion().findZipCodes(stateAbbreviation);

		assertNotNull("A list of ZIP codes is returned", actualZipCodes);
		assertFalse("There are ZIP codes for the state", actualZipCodes.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#findZipCodes(String)} for an invalid state.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindZipCodes_invalidState() {
		final String stateAbbreviation = selectInvalidState();
		setUpValidStreetSegmentFinder();
		replayAll();

		final List<String> actualZipCodes = getForStateOrVotingRegion().findZipCodes(stateAbbreviation);

		assertNotNull("A list of ZIP codes is returned", actualZipCodes);
		assertTrue("There are no ZIP codes for the state", actualZipCodes.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#fixAddress(UserAddress, com.bearcode.ovf.tools.votingprecinct.model.StreetSegment)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 5, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFixAddress() {
		final UserAddress normalizedAddress = createNormalizedAddress();
		final UserAddress address = createValidAddress();
		assertFalse("The valid address is not normalized",
				normalizedAddress.getSingleLineAddress().equals(address.getSingleLineAddress()));
		setUpValidStreetSegmentFinder();
		replayAll();
		final VipStreetSegment streetSegment = getForStateOrVotingRegion().findStreetSegment(address);

		getForStateOrVotingRegion().fixAddress(address, streetSegment);

		assertEquals("The address has changed", normalizedAddress.getSingleLineAddress(), address.getSingleLineAddress());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinder#fixAddress(UserAddress, com.bearcode.ovf.tools.votingprecinct.model.StreetSegment)}
	 * for the case where the address doesn't need fixing.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 5, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFixAddress_notNecessary() {
		final UserAddress originalAddress = createNormalizedAddress();
		final UserAddress address = WizardResultAddress.create(originalAddress);
		setUpValidStreetSegmentFinder();
		replayAll();
		final VipStreetSegment streetSegment = getForStateOrVotingRegion().findStreetSegment(address);

		getForStateOrVotingRegion().fixAddress(address, streetSegment);

		assertEquals("The address has not changed", originalAddress.getSingleLineAddress(), address.getSingleLineAddress());
		verifyAll();
	}

	/**
	 * Can this street segment finder have an invalid set up?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the set up can be invalid, <code>false</code> if it is always valid.
	 * @since Jul 5, 2012
	 * @version Jul 5, 2012
	 */
	protected abstract boolean canHaveInvalidSetup();

	/**
	 * Can restricted addresses be required by this type of street segment finder?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if restricted addresses can be required, <code>false</code> if they cannot.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	protected abstract boolean canRestrictedAddressesBeRequired();

	/** {@inheritDoc} */
	@Override
	protected final F createForStateOrVotingRegion() {
		final F streetSegmentFinder = createStreetSegmentFinder();
		streetSegmentFinder.setVotingRegionType("County");
		streetSegmentFinder.setNeedNormalization(true);
		return streetSegmentFinder;
	}

	/**
	 * Creates an invalid address.
	 * 
	 * @author IanBrown
	 * @return the invalid address.
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	protected abstract UserAddress createInvalidAddress();

	/**
	 * Creates an address that has been normalized to match the street segment values.
	 * 
	 * @author IanBrown
	 * @return the normalized address.
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	protected abstract UserAddress createNormalizedAddress();

	/**
	 * Creates a street segment finder of the type to test.
	 * 
	 * @author IanBrown
	 * @return the street segment finder.
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	protected abstract F createStreetSegmentFinder();

	/**
	 * Creates a valid address for the street segment finder.
	 * 
	 * @author IanBrown
	 * @return the valid address.
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	protected abstract UserAddress createValidAddress();

	/**
	 * Are street names supported by this street segment finder?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if street names are supported, <code>false</code> otherwise.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	protected abstract boolean isSupportedStreetNames();

	/**
	 * Selects the name of an invalid city.
	 * 
	 * @author IanBrown
	 * @return the city name.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	protected abstract String selectInvalidCity();

	/**
	 * Selects an invalid ZIP code.
	 * 
	 * @author IanBrown
	 * @return the ZIP code.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	protected abstract String selectInvalidZipCode();

	/**
	 * Selects the name of a valid city.
	 * 
	 * @author IanBrown
	 * @return the valid city.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	protected abstract String selectValidCity();

	/**
	 * Selects an valid ZIP code.
	 * 
	 * @author IanBrown
	 * @return the ZIP code.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	protected abstract String selectValidZipCode();

	/**
	 * Sets the restricted addresses required flag.
	 * 
	 * @author IanBrown
	 * @param restrictedAddressesRequired
	 *            <code>true</code> if restricted addresses should be required, <code>false</code> otherwise.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	protected abstract void setRestrictedAddressesRequired(boolean restrictedAddressesRequired);

	/**
	 * Sets up the street segment finder such that it won't be able to properly access its data.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	protected abstract void setUpInvalidStreetSegmentFinder();

	/**
	 * Sets up a street segment finder that is not ready.
	 * 
	 * @author IanBrown
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	protected abstract void setUpNotReadyStreetSegmentFinder();

	/** {@inheritDoc} */
	@Override
	protected final void setUpSpecificForStateOrVotingRegion() {
		setUpSpecificStreetSegmentFinder();
	}

	/**
	 * Sets up to test specific type of street segment finder.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void setUpSpecificStreetSegmentFinder();

	/**
	 * Sets up the street segment finder so that it will have valid data.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	protected abstract void setUpValidStreetSegmentFinder();

	/** {@inheritDoc} */
	@Override
	protected final void tearDownSpecificForStateOrVotingRegion() {
		tearDownSpecificStreetSegmentFinder();
	}

	/**
	 * Tears down the set up for testing the specific type of street segment finder.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract void tearDownSpecificStreetSegmentFinder();
}
