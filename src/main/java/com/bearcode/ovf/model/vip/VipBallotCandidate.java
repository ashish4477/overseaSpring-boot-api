/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractId} linking class to provide an ordered list of candidates for a ballot.
 * 
 * @author IanBrown
 * 
 * @since Jun 25, 2012
 * @version Jun 26, 2012
 */
public class VipBallotCandidate extends AbstractId {

	/**
	 * the ballot.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipBallot ballot;

	/**
	 * the candidate.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipCandidate candidate;

	/**
	 * the sorting order.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private Integer sortOrder;

	/**
	 * Gets the ballot.
	 * 
	 * @author IanBrown
	 * @return the ballot.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public VipBallot getBallot() {
		return ballot;
	}

	/**
	 * Gets the candidate.
	 * 
	 * @author IanBrown
	 * @return the candidate.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public VipCandidate getCandidate() {
		return candidate;
	}

	/**
	 * Gets the sort order.
	 * 
	 * @author IanBrown
	 * @return the sort order.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * Sets the ballot.
	 * 
	 * @author IanBrown
	 * @param ballot
	 *            the ballot to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setBallot(final VipBallot ballot) {
		this.ballot = ballot;
	}

	/**
	 * Sets the candidate.
	 * 
	 * @author IanBrown
	 * @param candidate
	 *            the candidate to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setCandidate(final VipCandidate candidate) {
		this.candidate = candidate;
	}

	/**
	 * Sets the sort order.
	 * 
	 * @author IanBrown
	 * @param sortOrder
	 *            the sort order to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setSortOrder(final Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
}
