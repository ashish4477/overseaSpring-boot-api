/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractId} linking a referendum to a ballot response.
 * 
 * @author IanBrown
 * 
 * @since Jun 29, 2012
 * @version Jun 29, 2012
 */
public class VipReferendumBallotResponse extends AbstractId {

	/**
	 * the ballot response.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private VipBallotResponse ballotResponse;

	/**
	 * the referendum.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private VipReferendum referendum;

	/**
	 * the sort order for the ballot response.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private int sortOrder;

	/**
	 * Gets the ballot response.
	 * 
	 * @author IanBrown
	 * @return the ballot response.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public VipBallotResponse getBallotResponse() {
		return ballotResponse;
	}

	/**
	 * Gets the referendum.
	 * 
	 * @author IanBrown
	 * @return the referendum.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public VipReferendum getReferendum() {
		return referendum;
	}

	/**
	 * Gets the sort order.
	 * 
	 * @author IanBrown
	 * @return the sort order.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * Sets the ballot response.
	 * 
	 * @author IanBrown
	 * @param ballotResponse
	 *            the ballot response to set.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public void setBallotResponse(final VipBallotResponse ballotResponse) {
		this.ballotResponse = ballotResponse;
	}

	/**
	 * Sets the referendum.
	 * 
	 * @author IanBrown
	 * @param referendum
	 *            the referendum to set.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public void setReferendum(final VipReferendum referendum) {
		this.referendum = referendum;
	}

	/**
	 * Sets the sort order.
	 * 
	 * @author IanBrown
	 * @param sortOrder
	 *            the sort order to set.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public void setSortOrder(final int sortOrder) {
		this.sortOrder = sortOrder;
	}
}
