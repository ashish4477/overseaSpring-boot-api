/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.io.InputStream;

import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.FacesService;

/**
 * Interface for classes that provide valet services to acquire resources for {@link StreetSegmentFinderMelissa}.
 * 
 * @author IanBrown
 * 
 * @since Sep 4, 2012
 * @version Oct 5, 2012
 */
public interface StreetSegmentFinderMelissaValet {

	/**
	 * Acquires an input stream to read from the city file.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service.
	 * @param cityFilePath
	 *            the path to the city file.
	 * @return the input stream.
	 * @since Sep 4, 2012
	 * @version Sep 6, 2012
	 */
	InputStream acquireCityStream(FacesService facesService, String cityFilePath);

	/**
	 * Acquires a locality in the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param type
	 *            the type of locality.
	 * @param name
	 *            the name of the locality.
	 * @return the locality.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	VipLocality acquireLocality(VipState state, String type, String name);

	/**
	 * Acquires a precinct with the specified name.
	 * 
	 * @author IanBrown
	 * @param locality
	 *            the locality of the precinct.
	 * @param districtName
	 *            the name of the precinct.
	 * @return the precinct.
	 * @since Sep 10, 2012
	 * @version Sep 17, 2012
	 */
	VipPrecinct acquirePrecinct(VipLocality locality, String districtName);

	/**
	 * Acquires a state with the specified abbreviation.
	 * 
	 * @author IanBrown
	 * @param stateAbbr
	 *            the abbreviation.
	 * @return the state.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	VipState acquireState(String stateAbbr);

	/**
	 * Acquires a street segment for the specified location.
	 * 
	 * @author IanBrown
	 * @param street1
	 *            the first line of the street address.
	 * @param city
	 *            the city.
	 * @param votingRegion
	 *            the voting region.
	 * @param state
	 *            the state.
	 * @param zip
	 *            the ZIP code.
	 * @param needNormalization is address normalization needed or just parsing?
	 * @return the street segment.
	 * @since Sep 4, 2012
	 * @version Oct 5, 2012
	 */
	VipStreetSegment acquireStreetSegment(String street1, String city, String votingRegion, String state, String zip, boolean needNormalization);

}
