/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bearcode.ovf.model.common.OverseasUser;

/**
 * A scheduled report for the reporting dashboard.
 * 
 * @author IanBrown
 * 
 * @since Mar 6, 2012
 * @version Mar 6, 2012
 */
public class ScheduledReport {

	/**
	 * the formatter used for the dates.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * the identifier for the scheduled report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private Long id;

	/**
	 * the user to receive the report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private OverseasUser user;

	/**
	 * the report to run.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private Report report;

	/**
	 * the next date that the report should be performed.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private Date nextExecutionDate;

	/**
	 * the date that the report was last sent to the user.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private Date lastSentDate;

	/**
	 * the interval between reports.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private ReportTime interval;

	/**
	 * the duration of the report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private ReportTime duration;

	/**
	 * Formats the next execution date for display.
	 * 
	 * @author IanBrown
	 * @return the formatted date.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	public String formatNextExecutionDate() {
		return (getNextExecutionDate() == null) ? "(not set)" : DATE_FORMATTER.format(getNextExecutionDate());
	}

	/**
	 * Gets the duration.
	 * 
	 * @author IanBrown
	 * @return the duration.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public ReportTime getDuration() {
		return duration;
	}

	/**
	 * Gets the id.
	 * 
	 * @author IanBrown
	 * @return the id.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the interval.
	 * 
	 * @author IanBrown
	 * @return the interval.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public ReportTime getInterval() {
		return interval;
	}

	/**
	 * Gets the last sent date.
	 * 
	 * @author IanBrown
	 * @return the last sent date.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public Date getLastSentDate() {
		return lastSentDate;
	}

	/**
	 * Gets the next execution date.
	 * 
	 * @author IanBrown
	 * @return the next execution date.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public Date getNextExecutionDate() {
		return nextExecutionDate;
	}

	/**
	 * Gets the report.
	 * 
	 * @author IanBrown
	 * @return the report.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public Report getReport() {
		return report;
	}

	/**
	 * Gets the user.
	 * 
	 * @author IanBrown
	 * @return the user.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public OverseasUser getUser() {
		return user;
	}

	/**
	 * Sets the duration.
	 * 
	 * @author IanBrown
	 * @param duration
	 *            the duration to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public void setDuration(final ReportTime duration) {
		this.duration = duration;
	}

	/**
	 * Sets the id.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the id to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the interval.
	 * 
	 * @author IanBrown
	 * @param interval
	 *            the interval to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public void setInterval(final ReportTime interval) {
		this.interval = interval;
	}

	/**
	 * Sets the last sent date.
	 * 
	 * @author IanBrown
	 * @param lastSentDate
	 *            the last sent date to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public void setLastSentDate(final Date lastSentDate) {
		this.lastSentDate = lastSentDate;
	}

	/**
	 * Sets the next execution date.
	 * 
	 * @author IanBrown
	 * @param nextExecutionDate
	 *            the next execution date to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public void setNextExecutionDate(final Date nextExecutionDate) {
		this.nextExecutionDate = nextExecutionDate;
	}

	/**
	 * Sets the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public void setReport(final Report report) {
		this.report = report;
	}

	/**
	 * Sets the user.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public void setUser(final OverseasUser user) {
		this.user = user;
	}
}
