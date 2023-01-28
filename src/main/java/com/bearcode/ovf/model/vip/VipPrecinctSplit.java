/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.Set;

/**
 * Extended {@link AbstractVipHasElectoralDistricts} representation of a precinct split.
 * 
 * @author IanBrown
 * 
 * @since Jun 28, 2012
 * @version Jul 16, 2012
 */
public class VipPrecinctSplit extends AbstractVipHasElectoralDistricts {

	/**
	 * the precinct.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private VipPrecinct precinct;

	/**
	 * Gets the precinct.
	 * 
	 * @author IanBrown
	 * @return the precinct.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public VipPrecinct getPrecinct() {
		return precinct;
	}

	/**
	 * Sets the precinct.
	 * 
	 * @author IanBrown
	 * @param precinct
	 *            the precinct to set.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setPrecinct(final VipPrecinct precinct) {
		this.precinct = precinct;
	}
}
