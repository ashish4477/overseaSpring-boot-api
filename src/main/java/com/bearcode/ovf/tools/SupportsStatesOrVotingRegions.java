/**
 * 
 */
package com.bearcode.ovf.tools;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Interface for objects that support performing operations on states or voting regions.
 * 
 * @author IanBrown
 * 
 * @param <F> the type of object that performs operations for states or voting regions.
 * @since Oct 9, 2012
 * @version Oct 10, 2012
 */
@Component
public interface SupportsStatesOrVotingRegions<F extends ForStateOrVotingRegion> {

	/**
	 * Gets the list of objects supporting states or voting regions used to perform the operations.
	 * 
	 * @author IanBrown
	 * @return the list of objects.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	List<F> getForStateOrVotingRegions();

	/**
	 * Identifies the object that will perform operations for the state or voting region.
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the identification of the state.
	 * @param votingRegionName
	 *            the optional name of the voting region (if <code>null</code> the whole state must be supported).
	 * @return the object to perform operations for the specified state or voting region.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	F identifyForStateOrVotingRegion(String stateIdentification, String votingRegionName);

	/**
	 * Is the object to perform operations for the state or voting region ready?
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the identification of the state.
	 * @param votingRegionName
	 *            the optional name of the voting region (if <code>null</code> the whole state must be supported).
	 * @return <code>true</code> if the object to perform the operations is ready, <code>false</code> if it is not.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	boolean isReady(String stateIdentification, String votingRegionName);

	/**
	 * Is the state or the voting region supported?
	 * <p>
	 * If the whole state is required, then the object to perform the operations must be the whole state. If a voting region is
	 * required, then the object to perform the operations can support either the whole state or the individual voting region.
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the identification of the state.
	 * @param votingRegionName
	 *            the optional name of the voting region (if <code>null</code> the whole state must be supported).
	 * @return <code>true</code> if the voting region is supported, <code>false</code> if it is not.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	boolean isSupported(String stateIdentification, String votingRegionName);

	/**
	 * Sets the list of objects supporting states or voting regions used to perform the operations.
	 * 
	 * @author IanBrown
	 * @param forStateOrVotingRegions
	 *            the list of objects.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	void setForStateOrVotingRegions(List<F> forStateOrVotingRegions);
}
