/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.List;

/**
 * Extended {@link AbstractVip} representation of a custom ballot.
 * 
 * @author IanBrown
 * 
 * @since Jun 28, 2012
 * @version Jun 28, 2012
 */
public class VipCustomBallot extends AbstractVip {

	/**
	 * the responses to the ballot.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private List<VipCustomBallotResponse> ballotResponses;

	/**
	 * the heading.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private String heading;

	/**
	 * Gets the ballot responses.
	 * 
	 * @author IanBrown
	 * @return the ballot responses.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public List<VipCustomBallotResponse> getBallotResponses() {
		return ballotResponses;
	}

	/**
	 * Gets the heading.
	 * 
	 * @author IanBrown
	 * @return the heading.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * Sets the ballot responses.
	 * 
	 * @author IanBrown
	 * @param ballotResponses
	 *            the ballot responses to set.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setBallotResponses(final List<VipCustomBallotResponse> ballotResponses) {
		this.ballotResponses = ballotResponses;
	}

	/**
	 * Sets the heading.
	 * 
	 * @author IanBrown
	 * @param heading
	 *            the heading to set.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setHeading(final String heading) {
		this.heading = heading;
	}
}
