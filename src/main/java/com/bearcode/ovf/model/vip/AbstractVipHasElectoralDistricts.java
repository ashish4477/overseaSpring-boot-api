/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.List;

/**
 * Abstract extended {@link AbstractVip} object with electoral districts.
 * 
 * @author IanBrown
 * 
 * @since Jul 16, 2012
 * @version Aug 9, 2012
 */
public abstract class AbstractVipHasElectoralDistricts extends AbstractVipHasName {

	/**
	 * the electoral districts.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Aug 9, 2012
	 */
	private List<VipElectoralDistrict> electoralDistricts;
	
	/**
	 * Gets the electoral districts.
	 * 
	 * @author IanBrown
	 * @return the electoral districts.
	 * @since Jun 22, 2012
	 * @version Aug 9, 2012
	 */
	public List<VipElectoralDistrict> getElectoralDistricts() {
		return electoralDistricts;
	}

	/**
	 * Sets the electoral districts.
	 * 
	 * @author IanBrown
	 * @param electoralDistricts
	 *            the electoral districts to set.
	 * @since Jun 22, 2012
	 * @version Aug 9, 2012
	 */
	public void setElectoralDistricts(final List<VipElectoralDistrict> electoralDistricts) {
		this.electoralDistricts = electoralDistricts;
	}

}