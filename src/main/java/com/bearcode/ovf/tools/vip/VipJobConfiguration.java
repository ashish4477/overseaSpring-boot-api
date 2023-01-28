/**
 * 
 */
package com.bearcode.ovf.tools.vip;

/**
 * Simple bean that can be used to configure a VIP job. Because this bean is a Spring bean, its values can be set using bean
 * overrides, allowing for local configurations.
 * 
 * @author IanBrown
 * 
 * @since Sep 26, 2012
 * @version Sep 26, 2012
 */
public class VipJobConfiguration {

	/**
	 * the target for the VIP job.
	 * 
	 * @author IanBrown
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	private String vipJobTarget;

	/**
	 * Gets the VIP job target.
	 * 
	 * @author IanBrown
	 * @return the VIP job target.
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	public String getVipJobTarget() {
		return vipJobTarget;
	}

	/**
	 * Sets the VIP job target.
	 * 
	 * @author IanBrown
	 * @param vipJobTarget
	 *            the VIP job target to set.
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	public void setVipJobTarget(final String vipJobTarget) {
		this.vipJobTarget = vipJobTarget;
	}
}
