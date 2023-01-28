/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct.model;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipStreetSegment;

/**
 * A validated address.
 * 
 * @author IanBrown
 * 
 * @since Jun 4, 2012
 * @version Jul 31, 2012
 */
public class ValidAddress {

	/**
	 * the validated address.
	 * 
	 * @author IanBrown
	 * @since Jun 4, 2012
	 * @version Jun 31, 2012
	 */
	private UserAddress validatedAddress;

	/**
	 * the matching street segment.
	 * 
	 * @author IanBrown
	 * @since Jun 4, 2012
	 * @version Jun 29, 2012
	 */
	private VipStreetSegment streetSegment;

	/**
	 * Constructs an empty valid address.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	public ValidAddress() {

	}

	/**
	 * Constructs a valid address for the specified address.
	 * 
	 * @author IanBrown
	 * @param validatedAddress
	 *            the validated address.
	 * @param streetSegment
	 *            the optional street segment for the address.
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	public ValidAddress(final UserAddress validatedAddress, final VipStreetSegment streetSegment) {
		setValidatedAddress(validatedAddress);
		setStreetSegment(streetSegment);
	}

	/**
	 * Gets the street segment.
	 * 
	 * @author IanBrown
	 * @return the street segment.
	 * @since Jun 4, 2012
	 * @version Jun 29, 2012
	 */
	public VipStreetSegment getStreetSegment() {
		return streetSegment;
	}

	/**
	 * Gets the validated address.
	 * 
	 * @author IanBrown
	 * @return the validated address.
	 * @since Jun 4, 2012
	 * @version Jul 31, 2012
	 */
	public UserAddress getValidatedAddress() {
		return validatedAddress;
	}

	/**
	 * Sets the street segment.
	 * 
	 * @author IanBrown
	 * @param streetSegment
	 *            the street segment to set.
	 * @since Jun 4, 2012
	 * @version Jun 29, 2012
	 */
	public void setStreetSegment(final VipStreetSegment streetSegment) {
		this.streetSegment = streetSegment;
	}

	/**
	 * Sets the validated address.
	 * 
	 * @author IanBrown
	 * @param validatedAddress
	 *            the validated address to set.
	 * @since Jun 4, 2012
	 * @version Jul 31, 2012
	 */
	public void setValidatedAddress(final UserAddress validatedAddress) {
		this.validatedAddress = validatedAddress;
	}
}
