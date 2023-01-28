/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import net.sourceforge.jgeocoder.AddressComponent;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.FacesService;

/**
 * Singleton implementation of {@link StreetSegmentFinderMelissaValet}.
 * 
 * @author IanBrown
 * 
 * @since Sep 5, 2012
 * @version Oct 5, 2012
 */
public enum StreetSegmentFinderMelissaValetImpl implements StreetSegmentFinderMelissaValet {

	/**
	 * the singleton instance of the valet.
	 * 
	 * @author IanBrown
	 * @since Sep 5, 2012
	 * @version Sep 5, 2012
	 */
	INSTANCE;

	/**
	 * Gets an instance of the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Sep 5, 2012
	 * @version Sep 5, 2012
	 */
	public final static StreetSegmentFinderMelissaValet getInstance() {
		return INSTANCE;
	}

	/** {@inheritDoc} */
	@Override
	public final InputStream acquireCityStream(final FacesService facesService, final String cityFilePath) {
		if (facesService == null) {
			throw new IllegalArgumentException("A faces service is required");
		} else if (cityFilePath == null) {
			throw new IllegalArgumentException("A city file path is required");
		}

		return facesService.openResource(cityFilePath);
	}

	/** {@inheritDoc} */
	@Override
	public VipLocality acquireLocality(final VipState state, final String type, final String name) {
		final VipLocality locality = new VipLocality();
		locality.setState(state);
		locality.setType(type);
		locality.setName(name);
		return locality;
	}

	/** {@inheritDoc} */
	@Override
	public VipPrecinct acquirePrecinct(final VipLocality locality, final String districtName) {
		final VipPrecinct precinct = new VipPrecinct();
		precinct.setLocality(locality);
		final VipElectoralDistrict electoralDistrict = new VipElectoralDistrict();
		electoralDistrict.setName(districtName);
		precinct.setElectoralDistricts(Arrays.asList(electoralDistrict));
		return precinct;
	}

	/** {@inheritDoc} */
	@Override
	public VipState acquireState(final String stateAbbr) {
		final VipState state = new VipState();
		state.setName(stateAbbr);
		return state;
	}

	/** {@inheritDoc} */
	@Override
	public final VipStreetSegment acquireStreetSegment(final String street1, final String city, final String votingRegion,
			final String state, final String zip, boolean needNormalization) {
		final UserAddress address = new UserAddress();
		address.setStreet1(street1);
		address.setCity(city);
		address.setCounty(votingRegion);
		address.setState(state);
		address.setZip(zip);
		final Map<AddressComponent, String> normalizedAddress = AbstractStreetSegmentFinder.normalizeAddress(address, needNormalization);
		if (normalizedAddress == null) {
			return null;
		}

		final VipStreetSegment streetSegment = new VipStreetSegment();
		final int houseNumber = Integer.parseInt(normalizedAddress.get(AddressComponent.NUMBER));
		streetSegment.setStartHouseNumber(houseNumber);
		streetSegment.setEndHouseNumber(houseNumber);
		final VipDetailAddress nonHouseAddress = new VipDetailAddress();
		streetSegment.setNonHouseAddress(nonHouseAddress);
		nonHouseAddress.setStreetDirection(normalizedAddress.get(AddressComponent.PREDIR));
		nonHouseAddress.setStreetName(normalizedAddress.get(AddressComponent.STREET));
		nonHouseAddress.setStreetSuffix(normalizedAddress.get(AddressComponent.TYPE));
		nonHouseAddress.setAddressDirection(normalizedAddress.get(AddressComponent.POSTDIR));
		nonHouseAddress.setCity(normalizedAddress.get(AddressComponent.CITY));
		nonHouseAddress.setState(normalizedAddress.get(AddressComponent.STATE));
		nonHouseAddress.setZip(normalizedAddress.get(AddressComponent.ZIP));
		return streetSegment;
	}
}
