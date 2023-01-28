/**
 * 
 */
package com.bearcode.ovf.model.eod;

/**
 * Enumeration of the types of elections.
 * 
 * @author IanBrown
 * 
 * @since Jan 13, 2012
 * @version Feb 6, 2012
 */
public enum TypeOfElection {

	/**
	 * a caucus.
	 * 
	 * @author IanBrown
	 * @since Feb 6, 2012
	 * @version Feb 6, 2012
	 */
	CAUCUS,

    /**
     * Local / Municipal election
     */
    LOCAL_MUNICIPAL,

	/**
	 * an election for a state official.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	STATE,

	/**
	 * an election for a federal official.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	FEDERAL;
}
