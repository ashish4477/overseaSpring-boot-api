/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.AbstractSupportsStatesOrVotingRegions;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Service to handle voting precincts. Used by SHS sites to support address validation and finer grained election selection.
 * 
 * @author IanBrown
 * 
 * @since Jun 4, 2012
 * @version Oct 9, 2012
 */
@Service
public class VotingPrecinctService extends AbstractSupportsStatesOrVotingRegions<StreetSegmentFinder> {

	/**
	 * the street segment finders.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Autowired(required = false)
	private List<StreetSegmentFinder> streetSegmentFinders;

	/**
	 * Are restricted addresses required by the state and voting region?
	 * <p>
	 * A restricted address means that the user will be presented with pulldowns for the voting address rather than being given a
	 * simple open-ended text field.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return <code>true</code> if restricted addresses are required, <code>false</code> if they are not.
	 * @since Sep 19, 2012
	 * @version Oct 9, 2012
	 */
	public boolean areRestrictedAddressesRequired(final String stateAbbreviation, final String votingRegionName) {
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateAbbreviation, votingRegionName);
		if (streetSegmentFinder == null) {
			return false;
		}

		return streetSegmentFinder.areRestrictedAddressesRequired();
	}

	/**
	 * Finds the cities for the specified voting region.
	 * <p>
	 * The name of the voting region should include a valid type of region, such as "Named County".
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region - should specify the type of region.
	 * @return the cities.
	 * @since Jul 28, 2012
	 * @version Oct 9, 2012
	 */
	public List<String> findCitiesByVotingRegion(final String stateAbbreviation, final String votingRegionName) {
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateAbbreviation, votingRegionName);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(stateAbbreviation, votingRegionName)) {
			return null;
		}

		return streetSegmentFinder.findCitiesByVotingRegion( stateAbbreviation, votingRegionName );
	}

	/**
	 * Finds the names of the cities by the ZIP.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param zip
	 *            the ZIP code.
	 * @return the cities.
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	public List<String> findCitiesByZip(final String stateAbbreviation, final String zip) {
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateAbbreviation, null);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(stateAbbreviation, null)) {
			return null;
		}

		return streetSegmentFinder.findCitiesByZip( stateAbbreviation, zip );
	}

	/**
	 * Finds the names of the streets in the specified city.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param city
	 *            the name of the city.
	 * @return the street names.
	 * @since Jul 31, 2012
	 * @version Oct 9, 2012
	 */
	public List<String> findStreetNamesByCity(final String stateAbbreviation, final String city) {
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateAbbreviation, null);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(stateAbbreviation, null)) {
			return null;
		}

		return streetSegmentFinder.findStreetNamesByCity( stateAbbreviation, city );
	}

	/**
	 * Finds the street segments for the ZIP code belonging to the specified state.
	 * 
	 * @author IanBrown
	 * @param zipCode
	 *            the ZIP code.
	 * @param votingState
	 *            the voting state.
	 * @return the street segments.
	 * @since Jul 16, 2012
	 * @version Oct 9, 2012
	 */
	public List<VipStreetSegment> findStreetSegments(final String zipCode, final State votingState) {
		if (zipCode == null || votingState == null) {
			return null;
		}

		final String stateIdentification = votingState.getAbbr();
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateIdentification, null);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(stateIdentification, null)) {
			return null;
		}

		return streetSegmentFinder.findStreetSegments( stateIdentification, zipCode );
	}

	/**
	 * Finds the ZIP codes for the state.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state.
	 * @return the ZIP codes.
	 * @since Jul 17, 2012
	 * @version Oct 9, 2012
	 */
	public List<String> findZipCodes(final State votingState) {
		if (votingState == null) {
			return null;
		}

		final String stateIdentification = votingState.getAbbr();
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateIdentification, null);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(stateIdentification, null)) {
			return null;
		}

		return streetSegmentFinder.findZipCodes( stateIdentification );
	}

	/**
	 * Fixes the address based on the street segment.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address.
	 * @param streetSegment
	 *            the street segment.
	 * @since Aug 6, 2012
	 * @version Oct 9, 2012
	 */
	public void fixAddress(final UserAddress address, final VipStreetSegment streetSegment) {
		final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
		final String stateIdentification = nonHouseAddress.getState();
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateIdentification, null);
		if (streetSegmentFinder != null && streetSegmentFinder.isReady(stateIdentification, null)) {
			streetSegmentFinder.fixAddress(address, streetSegment);
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<StreetSegmentFinder> getForStateOrVotingRegions() {
		return streetSegmentFinders;
	}

	/** {@inheritDoc} */
	@Override
	public void setForStateOrVotingRegions(final List<StreetSegmentFinder> forStateOrVotingRegions) {
		streetSegmentFinders = forStateOrVotingRegions;
	}

	/**
	 * Validates the input address by matching it to a precinct.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address to validate.
	 * @param votingState
	 *            the voting state.
	 * @return the validated address, including the matching precinct, or <code>null</code> if the address is not valid.
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	public ValidAddress validateAddress(final UserAddress address, final State votingState) {
		final AddressType type = address.getType();
		if (type == null || type == AddressType.MILITARY || type == AddressType.OVERSEAS || type == AddressType.EOD
				|| type == AddressType.UNKNOWN) {
			return null;
		}
		if (votingState == null) {
			return null;
		}

		final String stateIdentification = votingState.getAbbr();
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateIdentification, null);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(stateIdentification, null)) {
			final ValidAddress validAddress = new ValidAddress(address, null);
			return validAddress;
		}

		final VipStreetSegment streetSegment = streetSegmentFinder.findStreetSegment(address);
		if (streetSegment == null) {
			return null;
		}

		streetSegmentFinder.fixAddress(address, streetSegment);
		final ValidAddress validAddress = new ValidAddress(address, streetSegment);
		return validAddress;
	}

	/**
	 * Validates the input address by matching it to a precinct.
	 *
	 * @author IanBrown
	 * @param address
	 *            the address to validate.
	 * @param votingStateAbbr
	 *            the voting state name.
	 * @return the validated address, including the matching precinct, or <code>null</code> if the address is not valid.
	 * @since Jun 4, 2012
	 * @version Oct 9, 2012
	 */
	public ValidAddress validateAddress(final UserAddress address, final String votingStateAbbr) {
		final AddressType type = address.getType();
		if (type == null || type == AddressType.MILITARY || type == AddressType.OVERSEAS || type == AddressType.EOD
				|| type == AddressType.UNKNOWN) {
			return null;
		}
		if (votingStateAbbr == null) {
			return null;
		}

		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(votingStateAbbr, null);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(votingStateAbbr, null)) {
			final ValidAddress validAddress = new ValidAddress(address, null);
			return validAddress;
		}

		final VipStreetSegment streetSegment = streetSegmentFinder.findStreetSegment(address);
		if (streetSegment == null) {
			return null;
		}

		streetSegmentFinder.fixAddress(address, streetSegment);
		final ValidAddress validAddress = new ValidAddress(address, streetSegment);
		return validAddress;
	}

	/**
	 * Validates the input address by matching it to a precinct.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address to validate.
	 * @param votingRegion
	 *            the voting region.
	 * @return the validated address, including the matching precinct, or <code>null</code> if the address is not valid.
	 * @since Sep 14, 2012
	 * @version Oct 9, 2012
	 */
	public ValidAddress validateAddress(final UserAddress address, final VotingRegion votingRegion) {
		final AddressType type = address.getType();
		if (type == null || type == AddressType.MILITARY || type == AddressType.OVERSEAS || type == AddressType.EOD
				|| type == AddressType.UNKNOWN) {
			return null;
		}
		if (votingRegion == null) {
			return null;
		}

		final State votingState = votingRegion.getState();
		final String stateIdentification = votingState.getAbbr();
		final String votingRegionName = votingRegion.getName();
		final StreetSegmentFinder streetSegmentFinder = identifyForStateOrVotingRegion(stateIdentification, votingRegionName);
		if (streetSegmentFinder == null || !streetSegmentFinder.isReady(stateIdentification, votingRegionName)) {
			final ValidAddress validAddress = new ValidAddress(address, null);
			return validAddress;
		}

		final VipStreetSegment streetSegment = streetSegmentFinder.findStreetSegment(address);
		if (streetSegment == null) {
			return null;
		}

		streetSegmentFinder.fixAddress(address, streetSegment);
		final ValidAddress validAddress = new ValidAddress(address, streetSegment);
		return validAddress;
	}
}
