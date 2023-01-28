/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jgeocoder.AddressComponent;

import org.springframework.beans.factory.annotation.Autowired;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipSource;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.service.VipService;

/**
 * Extended {@link AbstractStreetSegmentFinder} that uses the VIP database.
 * 
 * @author IanBrown
 * 
 * @since Jul 5, 2012
 * @version Oct 10, 2012
 */
public class StreetSegmentFinderVip extends AbstractStreetSegmentFinder {

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jul 5, 2012
	 * @version Jul 5, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	@Autowired
	private StateService stateService;

	/**
	 * are restricted addresses required?
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	private boolean restrictedAddressesRequired;
	
	/**
	 * map of state names by abbreviation.
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private final Map<String, String> stateNames = new HashMap<String, String>();

	/** {@inheritDoc} */
	@Override
	public boolean areRestrictedAddressesRequired() {
		return isRestrictedAddressesRequired();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findCitiesByVotingRegion(final String stateAbbreviation, final String votingRegionName) {
		final VipSource vipSource = findSourceForState(stateAbbreviation);
		if (vipSource == null) {
			return null;
		}

		return getVipService().findCitiesBySourceStateAndVotingRegion(vipSource, stateAbbreviation, votingRegionName);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findCitiesByZip(final String stateAbbreviation, final String zip) {
		final VipSource vipSource = findSourceForState(stateAbbreviation);
		if (vipSource == null) {
			return null;
		}

		return getVipService().findCitiesBySourceStateAndZip(vipSource, stateAbbreviation, zip);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findStreetNamesByCity(final String stateAbbreviation, final String city) {
		final VipSource vipSource = findSourceForState(stateAbbreviation);
		if (vipSource == null) {
			return null;
		}

		return getVipService().findStreetNamesBySourceStateAndCity(vipSource, stateAbbreviation, city);
	}

	/** {@inheritDoc} */
	@Override
	public VipStreetSegment findStreetSegment(final UserAddress address) {
		final Map<AddressComponent, String> normalizedAddress = normalizeAddress(address, isNeedNormalization());
		if (normalizedAddress == null) {
			return null;
		}

		final String state = normalizedAddress.get(AddressComponent.STATE);
		final VipSource vipSource = findSourceForState(state);
		if (vipSource == null) {
			return null;
		}

		final String fullHouseNumber = normalizedAddress.get(AddressComponent.NUMBER);
		final int dash = fullHouseNumber.indexOf("-");
		final String houseNumberValue;
		final String houseNumberSuffix;
		if (dash == -1) {
			houseNumberValue = fullHouseNumber;
			houseNumberSuffix = null;
		} else {
			houseNumberValue = fullHouseNumber.substring(0, dash);
			houseNumberSuffix = fullHouseNumber.substring(dash + 1);
		}
		final int houseNumber = Integer.parseInt(houseNumberValue);
		final String streetDirection = normalizedAddress.get(AddressComponent.PREDIR);
		final String streetName = normalizedAddress.get(AddressComponent.STREET);
		final String streetSuffix = normalizedAddress.get(AddressComponent.TYPE);
		final String addressDirection = normalizedAddress.get(AddressComponent.POSTDIR);
		final String city = normalizedAddress.get(AddressComponent.CITY);
		final String zip = normalizedAddress.get(AddressComponent.ZIP);
		final VipStreetSegment streetSegment = getVipService().findStreetSegmentForAddress(vipSource, null, houseNumber,
				houseNumberSuffix, streetDirection, streetName, streetSuffix, addressDirection, city, state, zip);
		return streetSegment;
	}

	/** {@inheritDoc} */
	@Override
	public List<VipStreetSegment> findStreetSegments(final String stateAbbreviation, final String zipCode) {
		final VipSource source = findSourceForState(stateAbbreviation);
		if (source == null) {
			return new LinkedList<VipStreetSegment>();
		}

		return getVipService().findStreetSegmentsBySourceAndZip(source, zipCode);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findZipCodes(final String stateAbbreviation) {
		final VipSource source = findSourceForState(stateAbbreviation);
		if (source == null) {
			return new LinkedList<String>();
		}

		return getVipService().findZipCodesBySourceAndState(source, stateAbbreviation);
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	public StateService getStateService() {
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
	public VipService getVipService() {
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
	public boolean isRestrictedAddressesRequired() {
		return restrictedAddressesRequired;
	}

	/**
	 * Sets the restricted addresses required.
	 * 
	 * @author IanBrown
	 * @param restrictedAddressesRequired
	 *            the restricted addresses required.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	public void setRestrictedAddressesRequired(final boolean restrictedAddressesRequired) {
		this.restrictedAddressesRequired = restrictedAddressesRequired;
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
	public void setStateService(final StateService stateService) {
		this.stateService = stateService;
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
	public void setVipService(final VipService vipService) {
		this.vipService = vipService;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean loadDataIfNeeded(final String stateIdentification, final String votingRegionName) {
		final VipSource source = findSourceForState(stateIdentification);
		if (source != null) {
			if (votingRegionName == null) {
				return true;
			}

			String stateName = stateNames.get(stateIdentification);
			if (stateName == null) {
				final State state = getStateService().findByAbbreviation(stateIdentification);
				stateName = state.getName();
				stateNames.put(stateIdentification, stateName);
			}
			final VipState vipState = getVipService().findStateBySourceAndName(source, stateName);
			return vipState != null;
			
// This appears to be fraught with danger due to the fact that some localities seem to span state lines. For now, I'm turning this off.
//			final List<VipLocality> localities = getVipService().findLocalitiesByStateAndType(vipState, "County");
//			final String normalizedVotingRegion = normalizeVotingRegionName(votingRegionName);
//			for (final VipLocality locality : localities) {
//				if (votingRegionName.equalsIgnoreCase(locality.getName())
//						|| normalizedVotingRegion.equalsIgnoreCase(locality.getName())) {
//					return true;
//				}
//			}
		}

		return false;
	}

	/**
	 * Finds the source for the specified state.
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the state identification.
	 * @return
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private VipSource findSourceForState(final String stateIdentification) {
		final State state = getStateService().findByAbbreviation(stateIdentification);
		return state == null ? null : getVipService().findLatestSource(state.getName());
	}
}
