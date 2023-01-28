/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Abstract extended {@link AbstractId} that provides the VIP identification.
 * @author IanBrown
 *
 * @since Jun 26, 2012
 * @version Sep 12, 2012
 */
public class AbstractVip extends AbstractId {

	/**
	 * the source of the data.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private VipSource source;
	
	/**
	 * the VIP identifier.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private Long vipId;

	/**
	 * Gets the source.
	 * 
	 * @author IanBrown
	 * @return the source.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public VipSource getSource() {
		return source;
	}

	/**
	 * Gets the VIP identifier.
	 * 
	 * @author IanBrown
	 * @return the VIP identifier.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public Long getVipId() {
		return vipId;
	}

	/**
	 * Sets the source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source to set.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public void setSource(final VipSource source) {
		this.source = source;
	}

	/**
	 * Sets the VIP identifier.
	 * 
	 * @author IanBrown
	 * @param vipId
	 *            the VIP identifier to set.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public void setVipId(final Long vipId) {
		this.vipId = vipId;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return super.toString() + " Vip #" + getVipId();
	}
}