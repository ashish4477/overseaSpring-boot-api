/**
 * 
 */
package com.bearcode.ovf.tools;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Interface for objects that perform operations for a state or voting region.
 * 
 * @author IanBrown
 * 
 * @since Oct 9, 2012
 * @version Oct 10, 2012
 */
@Component
public interface ForStateOrVotingRegion {

	/**
	 * Gets the whole states that this objects works on.
	 * 
	 * @author IanBrown
	 * @return the identifications of the states.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	Collection<String> getStates();

	/**
	 * Gets the voting regions that this object works on.
	 * 
	 * @author IanBrown
	 * @return the map of voting regions by state.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	Map<String, Collection<String>> getVotingRegions();

	/**
	 * Gets the type of voting region supported.
	 * <p>
	 * This maps to the locality type.
	 * 
	 * @author IanBrown
	 * @return the type of voting region.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	String getVotingRegionType();

	/**
	 * Is the state or voting region ready?
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the identification of the state.
	 * @param votingRegionName
	 *            the optional name of the voting region.
	 * @return <code>true</code> if the state or voting region is ready, <code>false</code> if not.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	boolean isReady(String stateIdentification, String votingRegionName);

	/**
	 * Sets the whole states that this object works on.
	 * 
	 * @author IanBrown
	 * @param states
	 *            the identifications of the states.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	void setStates(Collection<String> states);

	/**
	 * Sets the voting regions that this object works on.
	 * 
	 * @author IanBrown
	 * @param votingRegions
	 *            the map of voting regions by state.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	void setVotingRegions(Map<String, Collection<String>> votingRegions);

	/**
	 * Sets the type of voting region supported.
	 * <p>
	 * This maps to the locality type.
	 * 
	 * @author IanBrown
	 * @param votingRegionType
	 *            the type of voting region.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	void setVotingRegionType(String votingRegionType);

	/**
	 * Is the state or voting region supported?
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the state identification.
	 * @param votingRegionName
	 *            the optional voting region name.
	 * @return <code>true</code> if the state or voting region is supported, <code>false</code> otherwise.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	boolean isSupported(String stateIdentification, String votingRegionName);
}
