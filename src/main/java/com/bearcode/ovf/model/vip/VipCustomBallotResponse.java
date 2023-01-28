/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractId} linking a custom ballot to a ballot response.
 * 
 * @author IanBrown
 * 
 * @since Jun 28, 2012
 * @version Jun 28, 2012
 */
public class VipCustomBallotResponse extends AbstractId {

	/**
	 * the ballot response.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private VipBallotResponse ballotResponse;

	/**
	 * the custom ballot.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private VipCustomBallot customBallot;

	/**
	 * the sort order for the ballot response.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private int sortOrder;

	/**
	 * Gets the ballot response.
	 * 
	 * @author IanBrown
	 * @return the ballot response.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public VipBallotResponse getBallotResponse() {
		return ballotResponse;
	}

	/**
	 * Gets the custom ballot.
	 * 
	 * @author IanBrown
	 * @return the custom ballot.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public VipCustomBallot getCustomBallot() {
		return customBallot;
	}

	/**
	 * Gets the sort order.
	 * 
	 * @author IanBrown
	 * @return the sort order.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
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
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setBallotResponse(VipBallotResponse ballotResponse) {
		this.ballotResponse = ballotResponse;
	}

	/**
	 * Sets the custom ballot.
	 * 
	 * @author IanBrown
	 * @param customBallot
	 *            the custom ballot to set.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setCustomBallot(VipCustomBallot customBallot) {
		this.customBallot = customBallot;
	}

	/**
	 * Sets the sort order.
	 * 
	 * @author IanBrown
	 * @param sortOrder
	 *            the sort order to set.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
}
