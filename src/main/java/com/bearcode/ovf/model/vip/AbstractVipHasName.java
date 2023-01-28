/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Abstract extended {@link AbstractVip} with a name.
 * @author IanBrown
 *
 * @since Jul 16, 2012
 * @version Sep 12, 2012
 */
public abstract class AbstractVipHasName extends AbstractVip {

	/**
	 * the name of the VIP object.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Sep 12, 2012
	 */
	private String name;

	/**
	 * Gets the name.
	 * 
	 * @author IanBrown
	 * @return the name.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return super.toString() + " " + getName();
	}
}