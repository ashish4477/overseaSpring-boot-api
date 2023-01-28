/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractVip} representing a street segment.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jun 29, 2012
 */
public class VipStreetSegment extends AbstractVip {

	/**
	 * the ending house number for the segment.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private int endHouseNumber;

	/**
	 * the non-house number portion of the segment address.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipDetailAddress nonHouseAddress;

	/**
	 * are odd/even/both house numbers included?
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String oddEvenBoth;

	/**
	 * the precinct containing the street segment.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipPrecinct precinct;

	/**
	 * the precinct split containing the street segment.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private VipPrecinctSplit precinctSplit;

	/**
	 * the starting house number for the segment.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private int startHouseNumber;

	/**
	 * Gets the end house number.
	 * 
	 * @author IanBrown
	 * @return the end house number.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public int getEndHouseNumber() {
		return endHouseNumber;
	}

	/**
	 * Gets the non-house address.
	 * 
	 * @author IanBrown
	 * @return the non-house address.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public VipDetailAddress getNonHouseAddress() {
		return nonHouseAddress;
	}

	/**
	 * Gets the odd/even/both.
	 * 
	 * @author IanBrown
	 * @return the odd/even/both.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getOddEvenBoth() {
		return oddEvenBoth;
	}

	/**
	 * Gets the precinct.
	 * 
	 * @author IanBrown
	 * @return the precinct.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public VipPrecinct getPrecinct() {
		return precinct;
	}

	/**
	 * Gets the precinct split.
	 * 
	 * @author IanBrown
	 * @return the precinct split.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public VipPrecinctSplit getPrecinctSplit() {
		return precinctSplit;
	}

	/**
	 * Gets the start house number.
	 * 
	 * @author IanBrown
	 * @return the start house number.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public int getStartHouseNumber() {
		return startHouseNumber;
	}

	/**
	 * Sets the end house number.
	 * 
	 * @author IanBrown
	 * @param endHouseNumber
	 *            the end house number to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setEndHouseNumber(final int endHouseNumber) {
		this.endHouseNumber = endHouseNumber;
	}

	/**
	 * Sets the non-house address.
	 * 
	 * @author IanBrown
	 * @param nonHouseAddress
	 *            the non-house address to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setNonHouseAddress(final VipDetailAddress nonHouseAddress) {
		this.nonHouseAddress = nonHouseAddress;
	}

	/**
	 * Sets the odd/even/both.
	 * 
	 * @author IanBrown
	 * @param oddEvenBoth
	 *            the odd/even/both to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setOddEvenBoth(final String oddEvenBoth) {
		this.oddEvenBoth = oddEvenBoth;
	}

	/**
	 * Sets the precinct.
	 * 
	 * @author IanBrown
	 * @param precinct
	 *            the precinct to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setPrecinct(final VipPrecinct precinct) {
		this.precinct = precinct;
	}

	/**
	 * Sets the precinct split.
	 * 
	 * @author IanBrown
	 * @param precinctSplit
	 *            the precinct split to set.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public void setPrecinctSplit(final VipPrecinctSplit precinctSplit) {
		this.precinctSplit = precinctSplit;
	}

	/**
	 * Sets the start house number.
	 * 
	 * @author IanBrown
	 * @param startHouseNumber
	 *            the start house number to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setStartHouseNumber(final int startHouseNumber) {
		this.startHouseNumber = startHouseNumber;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getStartHouseNumber() + "-" + getEndHouseNumber() + " (" + getOddEvenBoth() + ") " + getNonHouseAddress();
	}
}
