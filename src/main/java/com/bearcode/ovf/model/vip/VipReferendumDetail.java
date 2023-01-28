/**
 * 
 */
package com.bearcode.ovf.model.vip;

import org.apache.commons.lang3.StringUtils;

/**
 * Extended {@link AbstractId} providing additional details for a referendum.
 * 
 * @author IanBrown
 * 
 * @since Aug 15, 2012
 * @version Oct 24, 2012
 */
public class VipReferendumDetail extends AbstractId {

	/**
	 * the detailed referendum.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private VipReferendum referendum;

	/**
	 * the statement of why the referendum should pass.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String proStatement;

	/**
	 * the statement of the why the referendum should not pass.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String conStatement;

	/**
	 * the threshold required to pass the referendum.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String passageThreshold;

	/**
	 * the effect of abstaining from voting on the referendum.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String effectOfAbstain;

	/**
	 * Gets the con statement.
	 * 
	 * @author IanBrown
	 * @return the con statement.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getConStatement() {
		return conStatement;
	}

	/**
	 * Gets the effect of abstain.
	 * 
	 * @author IanBrown
	 * @return the effect of abstain.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getEffectOfAbstain() {
		return effectOfAbstain;
	}

	/**
	 * Gets the passage threshold.
	 * 
	 * @author IanBrown
	 * @return the passage threshold.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getPassageThreshold() {
		return passageThreshold;
	}

	/**
	 * Gets the pro statement.
	 * 
	 * @author IanBrown
	 * @return the pro statement.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getProStatement() {
		return proStatement;
	}

	/**
	 * Gets the referendum.
	 * 
	 * @author IanBrown
	 * @return the referendum.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public VipReferendum getReferendum() {
		return referendum;
	}

	/**
	 * Is the referendum detail empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the referendum detail is empty, <code>false</code> otherwise.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(getConStatement()) && StringUtils.isEmpty(getEffectOfAbstain())
				&& StringUtils.isEmpty(getPassageThreshold()) && StringUtils.isEmpty(getProStatement());
	}

	/**
	 * Sets the con statement.
	 * 
	 * @author IanBrown
	 * @param conStatement
	 *            the con statement to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setConStatement(final String conStatement) {
		this.conStatement = conStatement;
	}

	/**
	 * Sets the effect of abstain.
	 * 
	 * @author IanBrown
	 * @param effectOfAbstain
	 *            the effect of abstain to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setEffectOfAbstain(final String effectOfAbstain) {
		this.effectOfAbstain = effectOfAbstain;
	}

	/**
	 * Sets the passage threshold.
	 * 
	 * @author IanBrown
	 * @param passageThreshold
	 *            the passage threshold to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setPassageThreshold(final String passageThreshold) {
		this.passageThreshold = passageThreshold;
	}

	/**
	 * Sets the pro statement.
	 * 
	 * @author IanBrown
	 * @param proStatement
	 *            the pro statement to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setProStatement(final String proStatement) {
		this.proStatement = proStatement;
	}

	/**
	 * Sets the referendum.
	 * 
	 * @author IanBrown
	 * @param referendum
	 *            the referendum to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setReferendum(final VipReferendum referendum) {
		this.referendum = referendum;
	}
}
