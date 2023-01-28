/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Abstract object that has a unique database identifier.
 * @author IanBrown
 *
 * @since Jun 26, 2012
 * @version Sep 12, 2012
 */
public class AbstractId {

	/**
	 * the ballot identifier.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private Long id;

	/**
	 * Gets the id.
	 * 
	 * @author IanBrown
	 * @return the id.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the id to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return super.toString() + " #" + getId();
	}
}