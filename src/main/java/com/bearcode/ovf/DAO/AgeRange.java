/**
 * 
 */
package com.bearcode.ovf.DAO;

/**
 * Enumeration of the age ranges.
 * 
 * @author IanBrown
 * 
 * @since Mar 2, 2012
 * @version Mar 2, 2012
 */
public enum AgeRange {

	/**
	 * the under 18 year old date range.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	UNDER_18("Under 18 years", 0, 0, 18),

	/**
	 * the 18 to 24 year olds.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	AGE_18_24("18 - 24 years", 1, 18, 25),

	/**
	 * the 25 to 40 year olds.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	AGE_25_40("25 to 40 years", 2, 25, 41),

	/**
	 * the 41 to 50 year olds.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	AGE_41_50("41 to 50 years", 3, 41, 51),

	/**
	 * the 51 to 64 year olds.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	AGE_51_65("51 to 65 years", 4, 51, 65),

	/**
	 * the over 65 year olds.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	OVER_65("Over 65 years", 5, 65, 200);

	/**
	 * the index for the age range.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	public int index;

	/**
	 * the value of for the date range.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	public String value;

	/**
	 * the minimum number of years for the age range (inclusive).
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	public int minimumYears;

	/**
	 * the maximum number of years for the age range (exclusive).
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	public int maximumYears;

	private AgeRange(final String value, final int index, final int minimumYears, final int maximumYears) {
		this.value = value;
		this.index = index;
		this.minimumYears = minimumYears;
		this.maximumYears = maximumYears;
	}
}