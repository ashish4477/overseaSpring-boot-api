/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.*;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.webservices.DistrictLookupService;
import com.bearcode.ovf.webservices.SmartyStreetService;
import org.easymock.EasyMock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Extended {@link StreetSegmentFinderCheck} test for {@link StreetSegmentFinderMelissa}.
 * 
 * @author IanBrown
 * 
 * @since Sep 4, 2012
 * @version Oct 9, 2012
 */
public final class StreetSegmentFinderMelissaTest extends StreetSegmentFinderCheck<StreetSegmentFinderMelissa> {

	/**
	 * the string to read from the city stream.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String CITY_STREAM_STRING;

	/**
	 * the name of the valid city.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String VALID_CITY = "City";

	/**
	 * the name of the city file.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String VALID_CITY_FILE;

	/**
	 * the name of the valid state.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String VALID_STATE = "ST";

	/**
	 * the name of a valid street.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String VALID_STREET;

	/**
	 * the name of the valid street.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String VALID_STREET_NAME = "Valid Street";

	/**
	 * the name of the valid voting region.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String VALID_VOTING_REGION = "Voting Region";

	/**
	 * the valid ZIP code.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final String VALID_ZIP = "13579";

	/**
	 * the state object or <code>null</code> if not initialized.
	 * 
	 * @author IanBrown
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	private VipState state;

	static {
		VALID_CITY_FILE = "com/bearcode/ovf/tools/votingprecinct/" + VALID_STATE + "_" + VALID_VOTING_REGION + ".csv";
		CITY_STREAM_STRING = VALID_CITY + "," + VALID_VOTING_REGION + "," + VALID_STATE + "," + VALID_ZIP;
		VALID_STREET = "1 " + VALID_STREET_NAME;
	}

	/**
	 * the district lookup service.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private DistrictLookupService districtLookupService;

    private SmartyStreetService smartyStreetService;

	/**
	 * the faces service to use.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private FacesService facesService;

	/**
	 * the valet to acquire resources.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private StreetSegmentFinderMelissaValet valet;

	/** {@inheritDoc} */
	@Override
	protected final boolean canHaveInvalidSetup() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean canRestrictedAddressesBeRequired() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createInvalidAddress() {
		final UserAddress invalidAddress = new UserAddress();
		invalidAddress.setCity(selectInvalidCity());
		invalidAddress.setCounty(selectInvalidVotingRegion());
		invalidAddress.setState(selectInvalidState());
		invalidAddress.setZip(selectInvalidZipCode());
		return invalidAddress;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createNormalizedAddress() {
		final UserAddress normalizedAddress = new UserAddress();
		final String street1 = VALID_STREET.toUpperCase();
		final String city = selectValidCity().toUpperCase();
		final String votingRegion = selectValidVotingRegion().toUpperCase();
		final String stateAbbr = selectValidState().toUpperCase();
		final String zip = selectValidZipCode();
		getForStateOrVotingRegion().setNeedNormalization(false);
		normalizedAddress.setStreet1(street1);
		normalizedAddress.setCity(city);
		normalizedAddress.setCounty(votingRegion);
		normalizedAddress.setState(stateAbbr);
		normalizedAddress.setZip(zip);
		final String[] district = new String[] { "normalizedDistrict", zip, "1234" };
		EasyMock.expect(getSmartyStreetService().findDistrict(street1, city, stateAbbr, zip)).andReturn(district).anyTimes();
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(getValet().acquireStreetSegment(street1, city, votingRegion, stateAbbr, zip, false))
				.andReturn(streetSegment).anyTimes();
		if (getState() == null) {
			final VipState state = createMock("State", VipState.class);
			EasyMock.expect(getValet().acquireState(stateAbbr)).andReturn(state).anyTimes();
			setState(state);
		}
		final VipLocality locality = createMock("Locality", VipLocality.class);
		EasyMock.expect(getValet().acquireLocality(getState(), "County", votingRegion)).andReturn(locality).anyTimes();
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(getValet().acquirePrecinct(locality, district[0])).andReturn(precinct).anyTimes();
		streetSegment.setPrecinct(precinct);
		EasyMock.expectLastCall().anyTimes();
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress).anyTimes();
		EasyMock.expect(nonHouseAddress.toStreet()).andReturn(VALID_STREET_NAME.toUpperCase()).anyTimes();
		EasyMock.expect(nonHouseAddress.getCity()).andReturn(city).anyTimes();
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(null).anyTimes();
		EasyMock.expect(nonHouseAddress.getState()).andReturn(stateAbbr).anyTimes();
		EasyMock.expect(nonHouseAddress.getZip()).andReturn(zip).anyTimes();
		return normalizedAddress;
	}

	/** {@inheritDoc} */
	@Override
	protected final StreetSegmentFinderMelissa createStreetSegmentFinder() {
		setValet(createMock("Valet", StreetSegmentFinderMelissaValet.class));
		setDistrictLookupService(createMock("DistrictLookupService", DistrictLookupService.class));
        setSmartyStreetService(createMock("SmartyStreetService", SmartyStreetService.class));
		setFacesService(createMock("FacesService", FacesService.class));
		final StreetSegmentFinderMelissa streetSegmentFinderMelissa = new StreetSegmentFinderMelissa();
		streetSegmentFinderMelissa.setValet(getValet());
		streetSegmentFinderMelissa.setDistrictLookupService(getDistrictLookupService());
        streetSegmentFinderMelissa.setSmartyStreetService(getSmartyStreetService());
		streetSegmentFinderMelissa.setFacesService(getFacesService());
		return streetSegmentFinderMelissa;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createValidAddress() {
		final UserAddress address = new UserAddress();
		final String street1 = VALID_STREET;
		final String city = selectValidCity();
		final String votingRegion = selectValidVotingRegion();
		final String stateAbbr = selectValidState();
		final String zip = selectValidZipCode();
		address.setStreet1(street1);
		address.setCity(city);
		address.setCounty(votingRegion);
		address.setState(stateAbbr);
		address.setZip(zip);
		final String[] district = new String[] { "district", zip, "1234" };
		EasyMock.expect(getSmartyStreetService().findDistrict(street1, city, stateAbbr, zip)).andReturn(district).anyTimes();
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(getValet().acquireStreetSegment(street1, city, votingRegion, stateAbbr, zip, false)).andReturn(
				streetSegment).anyTimes();
		if (getState() == null) {
			final VipState state = createMock("State", VipState.class);
			EasyMock.expect(getValet().acquireState(stateAbbr)).andReturn(state).anyTimes();
			setState(state);
		}
		final VipLocality locality = createMock("Locality", VipLocality.class);
		EasyMock.expect(getValet().acquireLocality(getState(), "County", votingRegion)).andReturn(locality).anyTimes();
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		EasyMock.expect(getValet().acquirePrecinct(locality, district[0])).andReturn(precinct).anyTimes();
		streetSegment.setPrecinct(precinct);
		EasyMock.expectLastCall().anyTimes();
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress).anyTimes();
		EasyMock.expect(nonHouseAddress.toStreet()).andReturn(VALID_STREET_NAME.toUpperCase()).anyTimes();
		EasyMock.expect(nonHouseAddress.getCity()).andReturn(VALID_CITY.toUpperCase()).anyTimes();
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(null).anyTimes();
		EasyMock.expect(nonHouseAddress.getState()).andReturn(VALID_STATE).anyTimes();
		EasyMock.expect(nonHouseAddress.getZip()).andReturn(VALID_ZIP).anyTimes();
		return address;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isSupportedStreetNames() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidCity() {
		return "Invalid City";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidState() {
		return "IS";
	}

	/** {@inheritDoc} */
	@Override
	protected String selectInvalidVotingRegion() {
		return "Invalid Voting Region";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidZipCode() {
		return "97531";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidCity() {
		return VALID_CITY;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidState() {
		return VALID_STATE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidVotingRegion() {
		return VALID_VOTING_REGION;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidZipCode() {
		return VALID_ZIP;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setRestrictedAddressesRequired(final boolean restrictedAddressesRequired) {
		if (restrictedAddressesRequired) {
			throw new UnsupportedOperationException("Restricted addresses cannot be required by the Melissa street segment finder");
		}
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpInvalidStreetSegmentFinder() {
		setState(null);
		getForStateOrVotingRegion().setCityFilePath(VALID_CITY_FILE);
		EasyMock.expect(getValet().acquireCityStream(getFacesService(), VALID_CITY_FILE)).andReturn(null).anyTimes();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpNotReadyStreetSegmentFinder() {
		setUpInvalidStreetSegmentFinder();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForState() {
		setState(null);
		getForStateOrVotingRegion().setStates(Arrays.asList(VALID_STATE));
		getForStateOrVotingRegion().setCityFilePath(VALID_CITY_FILE);
		final InputStream cityStream = new ByteArrayInputStream(CITY_STREAM_STRING.getBytes());
		EasyMock.expect(getValet().acquireCityStream(getFacesService(), VALID_CITY_FILE)).andReturn(cityStream).anyTimes();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForVotingRegion() {
		setState(null);
		Map<String, Collection<String>> votingRegions = new HashMap<String, Collection<String>>();;
		votingRegions.put(VALID_STATE, Arrays.asList(VALID_VOTING_REGION));
		getForStateOrVotingRegion().setVotingRegions(votingRegions);
		getForStateOrVotingRegion().setCityFilePath(VALID_CITY_FILE);
		final InputStream cityStream = new ByteArrayInputStream(CITY_STREAM_STRING.getBytes());
		EasyMock.expect(getValet().acquireCityStream(getFacesService(), VALID_CITY_FILE)).andReturn(cityStream).anyTimes();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpSpecificStreetSegmentFinder() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpValidStreetSegmentFinder() {
		setUpReadyForState();
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownSpecificStreetSegmentFinder() {
	}

	/**
	 * Gets the district lookup service.
	 * 
	 * @author IanBrown
	 * @return the district lookup service.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private DistrictLookupService getDistrictLookupService() {
		return districtLookupService;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the state.
	 * 
	 * @author IanBrown
	 * @return the state.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	private VipState getState() {
		return state;
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private StreetSegmentFinderMelissaValet getValet() {
		return valet;
	}

	/**
	 * Sets the district lookup service.
	 * 
	 * @author IanBrown
	 * @param districtLookupService
	 *            the district lookup service to set.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private void setDistrictLookupService(final DistrictLookupService districtLookupService) {
		this.districtLookupService = districtLookupService;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state to set.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	private void setState(final VipState state) {
		this.state = state;
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private void setValet(final StreetSegmentFinderMelissaValet valet) {
		this.valet = valet;
	}

    public SmartyStreetService getSmartyStreetService() {
        return smartyStreetService;
    }

    public void setSmartyStreetService(final SmartyStreetService smartyStreetService) {
        this.smartyStreetService = smartyStreetService;
    }
}
