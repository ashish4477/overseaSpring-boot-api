/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractVipHasName} representing an electoral district.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jul 16, 2012
 */
public class VipElectoralDistrict extends AbstractVipHasName {

	/**
	 * the number of the electoral district.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 26, 2012
	 */
	private Integer number;

	/**
	 * the type of electoral district.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String type;

	/**
	 * Gets the number.
	 * 
	 * @author IanBrown
	 * @return the number.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public Integer getNumber() {
		return number;
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
	 * Sets the number.
	 * 
	 * @author IanBrown
	 * @param number
	 *            the number to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setNumber(final Integer number) {
		this.number = number;
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
