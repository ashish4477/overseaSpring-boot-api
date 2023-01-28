/**
 * 
 */
package com.bearcode.ovf.model.vip;


/**
 * Extended {@link AbstractVip} representing a precinct.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jul 16, 2012
 */
public class VipPrecinct extends AbstractVipHasElectoralDistricts {

	/**
	 * the locality containing the precinct.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipLocality locality;

	/**
	 * the precinct number.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private String number;

	/**
	 * the (optional) ward of the precinct.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String ward;

	/**
	 * Gets the locality.
	 * 
	 * @author IanBrown
	 * @return the locality.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public VipLocality getLocality() {
		return locality;
	}

	/**
	 * Gets the number.
	 * 
	 * @author IanBrown
	 * @return the number.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Gets the ward.
	 * 
	 * @author IanBrown
	 * @return the ward.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getWard() {
		return ward;
	}

	/**
	 * Sets the locality.
	 * 
	 * @author IanBrown
	 * @param locality
	 *            the locality to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setLocality(final VipLocality locality) {
		this.locality = locality;
	}

	/**
	 * Sets the number.
	 * 
	 * @author IanBrown
	 * @param number
	 *            the number to set.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public void setNumber(final String number) {
		this.number = number;
	}

	/**
	 * Sets the ward.
	 * 
	 * @author IanBrown
	 * @param ward
	 *            the ward to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setWard(final String ward) {
		this.ward = ward;
	}
}
