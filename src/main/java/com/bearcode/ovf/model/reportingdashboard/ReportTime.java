/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Enumeration of the time duration and frequency for producing scheduled reports.
 * 
 * @author IanBrown
 * 
 * @since Mar 6, 2012
 * @version Mar 12, 2012
 */
public enum ReportTime implements Serializable {

	/**
	 * daily report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 12, 2012
	 */
	DAILY(Calendar.DAY_OF_YEAR, -1),

	/**
	 * weekly report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 12, 2012
	 */
	WEEKLY(Calendar.DAY_OF_YEAR, -7),

	/**
	 * monthly report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 12, 2012
	 */
	MONTHLY(Calendar.MONTH, -1),

	/**
	 * quarterly report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 12, 2012
	 */
	QUARTERLY(Calendar.MONTH, -3),

	/**
	 * yearly report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 12, 2012
	 */
	YEARLY(Calendar.YEAR, -1);

	/**
	 * the calendar field to modify.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private final int calendarField;

	/**
	 * the size of the modification.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private final int timeAdjustment;

	/**
	 * Constructs a report time to adjust the date/time by the specified amount.
	 * 
	 * @author IanBrown
	 * @param calendarField
	 *            the calendar field to adjust.
	 * @param timeAdjustment
	 *            the size of the time adjustment.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private ReportTime(final int calendarField, final int timeAdjustment) {
		this.calendarField = calendarField;
		this.timeAdjustment = timeAdjustment;
	}

	/**
	 * Computes the next interval from the current date.
	 * 
	 * @author IanBrown
	 * @param date
	 *            the date.
	 * @return the next interval.
	 * @since Mar 6, 2012
	 * @version Mar 8, 2012
	 */
	public final Date computeNextInterval(final Date date) {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(getCalendarField(), -getTimeAdjustment());
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Computes the start date for the duration based on the input end date.
	 * 
	 * @author IanBrown
	 * @param endDate
	 *            the end date.
	 * @return the start date.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public final Date computeStartDate(final Date endDate) {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(endDate.getTime());
		calendar.add(getCalendarField(), getTimeAdjustment());
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Gets the calendar field to adjust.
	 * 
	 * @author IanBrown
	 * @return the calendar field.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public int getCalendarField() {
		return calendarField;
	}

	/**
	 * Gets the time adjustment.
	 * 
	 * @author IanBrown
	 * @return the time adjustment.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public int getTimeAdjustment() {
		return timeAdjustment;
	}
}
