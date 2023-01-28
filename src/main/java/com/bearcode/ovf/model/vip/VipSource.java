/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.Date;

/**
 * Extended {@link AbstractId} representing the source of VIP data.
 * 
 * @author IanBrown
 * 
 * @since Jun 26, 2012
 * @version Oct 11, 2012
 */
public class VipSource extends AbstractId {

	/**
	 * is the source completely loaded?
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private boolean complete;

	/**
	 * the date and time of the source data.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private Date dateTime;

	/**
	 * the date that the data was last modified.
	 * 
	 * @author IanBrown
	 * @since Oct 11, 2012
	 * @version Oct 11, 2012
	 */
	private Date lastModified;

	/**
	 * the name of the source.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private String name;

	/**
	 * the source specific identifier.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private Long sourceId;

	/**
	 * the VIP identifier.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private Long vipId;

	/**
	 * Gets the date/time.
	 * 
	 * @author IanBrown
	 * @return the date/time.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * Gets the last modified.
	 * 
	 * @author IanBrown
	 * @return the last modified.
	 * @since Oct 11, 2012
	 * @version Oct 11, 2012
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * Gets the name.
	 * 
	 * @author IanBrown
	 * @return the name.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the source identifier.
	 * 
	 * @author IanBrown
	 * @return the source identifier.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public Long getSourceId() {
		return sourceId;
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
	 * Gets the complete.
	 * 
	 * @author IanBrown
	 * @return the complete.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Sets the complete.
	 * 
	 * @author IanBrown
	 * @param complete
	 *            the complete to set.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

	/**
	 * Sets the date/time.
	 * 
	 * @author IanBrown
	 * @param dateTime
	 *            the date/time to set.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public void setDateTime(final Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Sets the last modified.
	 * 
	 * @author IanBrown
	 * @param lastModified
	 *            the last modified to set.
	 * @since Oct 11, 2012
	 * @version Oct 11, 2012
	 */
	public void setLastModified(final Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Sets the name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name to set.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the source identifier.
	 * 
	 * @author IanBrown
	 * @param sourceId
	 *            the source identifier to set.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public void setSourceId(final Long sourceId) {
		this.sourceId = sourceId;
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
}
