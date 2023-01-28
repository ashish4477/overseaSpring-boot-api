/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.ForStateOrVotingRegion;

/**
 * Interface for objects that find street segments.
 * 
 * @author IanBrown
 * 
 * @since Jun 5, 2012
 * @version Oct 9, 2012
 */
@Component
public interface StreetSegmentFinder extends ForStateOrVotingRegion {

	/**
	 * Are restricted addresses required by this street segment finder?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the restricted addresses are required, <code>false</code> if they are not.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	boolean areRestrictedAddressesRequired();

	/**
	 * Finds the cities for the specified voting region.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the cities found.
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	List<String> findCitiesByVotingRegion(String stateAbbreviation, String votingRegionName);

	/**
	 * Finds the cities for the specified ZIP code.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param zip
	 *            the ZIP code.
	 * @return the cities.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	List<String> findCitiesByZip(String stateAbbreviation, String zip);

	/**
	 * Finds the street names for the specified city.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param city
	 *            the name of the city.
	 * @return the street names.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	List<String> findStreetNamesByCity(String stateAbbreviation, String city);

	/**
	 * Finds the street segment matching the address.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address.
	 * @return the matching street segment (if any).
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	VipStreetSegment findStreetSegment(UserAddress address);

	/**
	 * Finds the street segments for the specified ZIP code.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param zipCode
	 *            the ZIP code.
	 * @return the street segments.
	 * @since Jul 16, 2012
	 * @version Sep 18, 2012
	 */
	List<VipStreetSegment> findStreetSegments(String stateAbbreviation, String zipCode);

	/**
	 * Finds the ZIP codes for the state.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @return the ZIP codes.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	List<String> findZipCodes(String stateAbbreviation);

	/**
	 * Fixes the input address based on the input street segment.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address to fix - modified on output.
	 * @param streetSegment
	 *            the street segment.
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	void fixAddress(UserAddress address, VipStreetSegment streetSegment);

	/**
	 * Is normalization of the addresses needed?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if normalization is needed, <code>false</code> if simple parsing will do.
	 * @since Oct 5, 2012
	 * @version Oct 5, 2012
	 */
	boolean isNeedNormalization();
	
	/**
	 * Sets the flag indicating if normalization of addresses is needed.
	 * @author IanBrown
	 * @param needNormalization <code>true</code> if normalization is needed, <code>false</code> if simple parsing will do.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	void setNeedNormalization(boolean needNormalization);
}
