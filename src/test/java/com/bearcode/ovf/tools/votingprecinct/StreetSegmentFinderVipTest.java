/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipSource;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.service.VipService;

/**
 * Extended {@link StreetSegmentFinderCheck} test for {@link StreetSegmentFinderVip}.
 * 
 * @author IanBrown
 * 
 * @since Jul 5, 2012
 * @version Oct 9, 2012
 */
public final class StreetSegmentFinderVipTest extends StreetSegmentFinderCheck<StreetSegmentFinderVip> {

	/**
	 * the abbreviation of the invalid state.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	private final static String INVALID_STATE = "VA";

	/**
	 * the name of the invalid state.
	 * 
	 * @author IanBrown
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private static final String INVALID_STATE_NAME = "Virginia";

	/**
	 * the name of a valid county.
	 * 
	 * @author IanBrown
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	private static final String VALID_COUNTY = "Valid County";

	/**
	 * the abbreviation of the valid state.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	private final static String VALID_STATE = "VT";

	/**
	 * the name of the valid state.
	 * 
	 * @author IanBrown
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private static final String VALID_STATE_NAME = "Vermont";

	/**
	 * the latest source of data.
	 * 
	 * @author IanBrown
	 * @since Jul 5, 2012
	 * @version Jul 5, 2012
	 */
	private VipSource latestSource;

	/**
	 * are restricted addresses required?
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	private boolean restrictedAddressesRequired;

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private StateService stateService;

	/**
	 * the street segment.
	 * 
	 * @author IanBrown
	 * @since Jul 5, 2012
	 * @version Jul 5, 2012
	 */
	private VipStreetSegment streetSegment;

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jul 5, 2012
	 * @version Jul 5, 2012
	 */
	private VipService vipService;

	/** {@inheritDoc} */
	@Override
	protected final boolean canHaveInvalidSetup() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean canRestrictedAddressesBeRequired() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createInvalidAddress() {
		final UserAddress address = new UserAddress();
		address.setStreet1("1 N 1ST ST");
		address.setCity("NOWHERE");
		address.setState(INVALID_STATE);
		address.setZip("54321");
		EasyMock.expect(
				getVipService().findStreetSegmentForAddress(latestSource, null, 1, null, "N", "1ST", "ST", null, "NOWHERE",
						INVALID_STATE, "54321")).andReturn(null).anyTimes();
		return address;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createNormalizedAddress() {
		final UserAddress address = new UserAddress();
		address.setStreet1("10 2ND ST");
		address.setCity("SOMEWHERE");
		address.setCounty("LOCALITY");
		address.setState(VALID_STATE);
		address.setZip("12345");
		EasyMock.expect(
				getVipService().findStreetSegmentForAddress(latestSource, null, 10, null, null, "2ND", "ST", null, "SOMEWHERE",
						VALID_STATE, "12345")).andReturn(streetSegment).anyTimes();
		return address;
	}

	/** {@inheritDoc} */
	@Override
	protected final StreetSegmentFinderVip createStreetSegmentFinder() {
		setVipService(createMock("VipService", VipService.class));
		setStateService(createMock("StateService", StateService.class));
		final StreetSegmentFinderVip streetSegmentFinder = new StreetSegmentFinderVip();
		streetSegmentFinder.setVipService(getVipService());
		streetSegmentFinder.setStateService(getStateService());
		latestSource = createMock("LatestSource", VipSource.class);
		setUpSourceForState(VALID_STATE, VALID_STATE_NAME, latestSource);
		final VipState vipState = createMock("VipState", VipState.class);
		EasyMock.expect(getVipService().findStateBySourceAndName(latestSource, VALID_STATE_NAME)).andReturn(vipState).anyTimes();
		setUpSourceForState(INVALID_STATE, INVALID_STATE_NAME, latestSource);
		streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress).anyTimes();
		EasyMock.expect(nonHouseAddress.toStreet()).andReturn("2ND ST").anyTimes();
		EasyMock.expect(nonHouseAddress.getCity()).andReturn("SOMEWHERE").anyTimes();
		EasyMock.expect(nonHouseAddress.getState()).andReturn(VALID_STATE).anyTimes();
		EasyMock.expect(nonHouseAddress.getZip()).andReturn("12345").anyTimes();
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(precinct).anyTimes();
		final VipLocality locality = createMock("Locality", VipLocality.class);
		EasyMock.expect(precinct.getLocality()).andReturn(locality).anyTimes();
		EasyMock.expect(locality.getType()).andReturn("County").anyTimes();
		EasyMock.expect(locality.getName()).andReturn(VALID_COUNTY.replace(" County", "")).anyTimes();
		final List<VipLocality> localities = Arrays.asList(locality);
		EasyMock.expect(getVipService().findLocalitiesByStateAndType(vipState, "County")).andReturn(localities).anyTimes();
		streetSegmentFinder.setVotingRegionType("County");
		return streetSegmentFinder;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createValidAddress() {
		final UserAddress address = new UserAddress();
		address.setStreet1("10 2ND ST");
		address.setCity("Somewhere");
		address.setState(VALID_STATE);
		address.setZip("12345");
		return address;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isSupportedStreetNames() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidCity() {
		final String city = "City";
		EasyMock.expect(getVipService().findStreetNamesBySourceStateAndCity(latestSource, VALID_STATE, city))
				.andReturn(new LinkedList<String>()).anyTimes();
		return city;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidState() {
		final String stateAbbreviation = INVALID_STATE;
		EasyMock.expect(getVipService().findZipCodesBySourceAndState(latestSource, stateAbbreviation))
				.andReturn(new LinkedList<String>()).anyTimes();
		return stateAbbreviation;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidVotingRegion() {
		final String votingRegionName = "Invalid County";
		EasyMock.expect(getVipService().findCitiesBySourceStateAndVotingRegion(latestSource, VALID_STATE, votingRegionName))
				.andReturn(new LinkedList<String>()).anyTimes();
		return votingRegionName;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidZipCode() {
		final String zipCode = "54321";
		EasyMock.expect(getVipService().findStreetSegmentsBySourceAndZip(latestSource, zipCode))
				.andReturn(new LinkedList<VipStreetSegment>()).anyTimes();
		EasyMock.expect(getVipService().findCitiesBySourceStateAndZip(latestSource, VALID_STATE, zipCode))
				.andReturn(new LinkedList<String>()).anyTimes();
		return zipCode;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidCity() {
		final String city = "City";
		final String streetName = "Street Name";
		final List<String> streetNames = Arrays.asList(streetName);
		EasyMock.expect(getVipService().findStreetNamesBySourceStateAndCity(latestSource, INVALID_STATE, city))
				.andReturn(new LinkedList<String>()).anyTimes();
		EasyMock.expect(getVipService().findStreetNamesBySourceStateAndCity(latestSource, VALID_STATE, city))
				.andReturn(streetNames).anyTimes();
		return city;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidState() {
		final String stateAbbreviation = VALID_STATE;
		final String zipCode = "12345";
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(getVipService().findZipCodesBySourceAndState(latestSource, stateAbbreviation)).andReturn(zipCodes)
				.anyTimes();
		return stateAbbreviation;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidVotingRegion() {
		final String votingRegionName = VALID_COUNTY;
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(getVipService().findCitiesBySourceStateAndVotingRegion(latestSource, VALID_STATE, votingRegionName))
				.andReturn(cities).anyTimes();
		EasyMock.expect(getVipService().findCitiesBySourceStateAndVotingRegion(latestSource, INVALID_STATE, votingRegionName))
				.andReturn(new LinkedList<String>()).anyTimes();
		return votingRegionName;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidZipCode() {
		final String zipCode = "12345";
		final List<VipStreetSegment> streetSegments = Arrays.asList(streetSegment);
		EasyMock.expect(getVipService().findStreetSegmentsBySourceAndZip(latestSource, zipCode)).andReturn(streetSegments)
				.anyTimes();
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(getVipService().findCitiesBySourceStateAndZip(latestSource, VALID_STATE, zipCode)).andReturn(cities)
				.anyTimes();
		EasyMock.expect(getVipService().findCitiesBySourceStateAndZip(latestSource, INVALID_STATE, zipCode))
				.andReturn(new LinkedList<String>()).anyTimes();
		return zipCode;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setRestrictedAddressesRequired(final boolean restrictedAddressesRequired) {
		this.restrictedAddressesRequired = restrictedAddressesRequired;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpInvalidStreetSegmentFinder() {
		// Nothing to do - the street segment finder is always valid as its data comes from the database.
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpNotReadyStreetSegmentFinder() {
		// Nothing to do - the street segment finder is always valid as its data comes from the database.
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForState() {
		getForStateOrVotingRegion().setStates(Arrays.asList(VALID_STATE));
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForVotingRegion() {
		final Map<String, Collection<String>> votingRegions = new HashMap<String, Collection<String>>();
		votingRegions.put(VALID_STATE, Arrays.asList(VALID_COUNTY));
		getForStateOrVotingRegion().setVotingRegions(votingRegions);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpSpecificStreetSegmentFinder() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpValidStreetSegmentFinder() {
		getForStateOrVotingRegion().setRestrictedAddressesRequired(isRestrictedAddressesRequired());
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownSpecificStreetSegmentFinder() {
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private StateService getStateService() {
		return stateService;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 5, 2012
	 * @version Jul 5, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Gets the restricted addresses required.
	 * 
	 * @author IanBrown
	 * @return the restricted addresses required.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	private boolean isRestrictedAddressesRequired() {
		return restrictedAddressesRequired;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}

	/**
	 * Sets up the source for the specified state.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @param stateName
	 *            the name of the state.
	 * @param source
	 *            the source.
	 * @since Sep 18, 2012
	 * @version Sep 26, 2012
	 */
	private void setUpSourceForState(final String stateAbbreviation, final String stateName, final VipSource source) {
		final State state = createMock(stateAbbreviation, State.class);
		EasyMock.expect(getStateService().findByAbbreviation(stateAbbreviation)).andReturn(state).anyTimes();
		EasyMock.expect(state.getName()).andReturn(stateName).anyTimes();
		EasyMock.expect(getVipService().findLatestSource(stateName)).andReturn(source).anyTimes();
	}

	/**
	 * Sets the VIP service.
	 * 
	 * @author IanBrown
	 * @param vipService
	 *            the VIP service to set.
	 * @since Jul 5, 2012
	 * @version Jul 5, 2012
	 */
	private void setVipService(final VipService vipService) {
		this.vipService = vipService;
	}
}
