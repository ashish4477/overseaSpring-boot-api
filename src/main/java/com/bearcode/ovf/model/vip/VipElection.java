/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.Date;


/**
 * Extended {@link AbstractVip} representing an election in the VIP data.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jun 26, 2012
 */
public class VipElection extends AbstractVip {

	/**
	 * the date of the election.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private Date date;

	/**
	 * the state holding the election.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipState state;

	/**
	 * the type of election.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String type;

	/**
	 * Gets the date.
	 * 
	 * @author IanBrown
	 * @return the date.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Gets the state.
	 * 
	 * @author IanBrown
	 * @return the state.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public VipState getState() {
		return state;
	}

	/**
	 * Gets the type.
	 * 
	 * @author IanBrown
	 * @return the type.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the date.
	 * 
	 * @author IanBrown
	 * @param date
	 *            the date to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Sets the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setState(final VipState state) {
		this.state = state;
	}

	/**
	 * Sets the type.
	 * 
	 * @author IanBrown
	 * @param type
	 *            the type to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setType(final String type) {
		this.type = type;
	}
}
