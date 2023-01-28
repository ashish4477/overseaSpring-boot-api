/**
 * 
 */
package com.bearcode.ovf.model.pendingregistration;

/**
 * An identified object.
 * @author IanBrown
 *
 * @since Nov 2, 2012
 * @version Nov 2, 2012
 */
public abstract class PendingVoterIdentified {

	/**
	 * the identifier.
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private Long id;

	/**
	 * Gets the id.
	 * @author IanBrown
	 * @return the id.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @author IanBrown
	 * @param id the id to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
