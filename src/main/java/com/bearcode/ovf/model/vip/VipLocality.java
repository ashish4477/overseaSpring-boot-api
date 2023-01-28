/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractVipHasName} representing a locality - a jurisdiction below state.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jul 16, 2012
 */
public class VipLocality extends AbstractVipHasName {

	/**
	 * the state containing the locality.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipState state;

	/**
	 * the type of locality.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String type;

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
