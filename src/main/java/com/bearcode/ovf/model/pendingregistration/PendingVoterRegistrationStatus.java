/**
 * 
 */
package com.bearcode.ovf.model.pendingregistration;

import java.util.Date;

import com.bearcode.ovf.model.common.OverseasUser;

/**
 * Completion status for a pending voter registration.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Nov 7, 2012
 */
public class PendingVoterRegistrationStatus extends PendingVoterIdentified {

	/**
	 * the date the pending voter registration was removed from the database.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private Date completionDate;

	/**
	 * the user that downloaded the pending voter registration.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private OverseasUser downloadedBy;

	/**
	 * the name of the pending voter.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private PendingVoterName name;

	/**
	 * Gets the completion date.
	 * 
	 * @author IanBrown
	 * @return the completion date.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public Date getCompletionDate() {
		return completionDate;
	}

	/**
	 * Gets the downloaded by.
	 * 
	 * @author IanBrown
	 * @return the downloaded by.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public OverseasUser getDownloadedBy() {
		return downloadedBy;
	}

	/**
	 * Gets the name.
	 * 
	 * @author IanBrown
	 * @return the name.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public PendingVoterName getName() {
		return name;
	}

	/**
	 * Sets the completion date.
	 * 
	 * @author IanBrown
	 * @param completionDate
	 *            the completion date to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public void setCompletionDate(final Date completionDate) {
		this.completionDate = completionDate;
	}

	/**
	 * Sets the downloaded by.
	 * 
	 * @author IanBrown
	 * @param downloadedBy
	 *            the downloaded by to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public void setDownloadedBy(final OverseasUser downloadedBy) {
		this.downloadedBy = downloadedBy;
	}

	/**
	 * Sets the name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public void setName(final PendingVoterName name) {
		this.name = name;
	}
}
