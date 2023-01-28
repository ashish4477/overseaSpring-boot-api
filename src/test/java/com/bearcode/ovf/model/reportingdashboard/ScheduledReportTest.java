/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Date;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.common.OverseasUser;

/**
 * Test for {@link ScheduledReport}.
 * 
 * @author IanBrown
 * 
 * @since Mar 6, 2012
 * @version Mar 6, 2012
 */
public final class ScheduledReportTest extends EasyMockSupport {

	/**
	 * the scheduled report to test.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private ScheduledReport scheduledReport;

	/**
	 * Sets up the scheduled report to test.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Before
	public final void setUpScheduledReport() {
		setScheduledReport(new ScheduledReport());
	}

	/**
	 * Tears down the scheduled report after testing.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@After
	public final void tearDownScheduledReport() {
		setScheduledReport(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#formatNextExecutionDate()}.
	 * 
	 * @author IanBrown
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@Test
	public final void testFormatNextExecutionDate() {
		final Date nextExecutionDate = new Date();
		getScheduledReport().setNextExecutionDate(nextExecutionDate);

		final String actualFormattedDate = getScheduledReport().formatNextExecutionDate();

		final String expectedFormattedDate = ScheduledReport.DATE_FORMATTER.format(nextExecutionDate);
		assertEquals("The next execution date is formatted correctly", expectedFormattedDate, actualFormattedDate);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#getDuration()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetDuration() {
		final ReportTime actualDuration = getScheduledReport().getDuration();

		assertNull("There is no duration set", actualDuration);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#getId()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetId() {
		final Long actualId = getScheduledReport().getId();

		assertNull("There is no ID set", actualId);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#getInterval()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetInterval() {
		final ReportTime actualInterval = getScheduledReport().getInterval();

		assertNull("There is no interval set", actualInterval);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#getLastSentDate()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetLastSentDate() {
		final Date actualLastSent = getScheduledReport().getLastSentDate();

		assertNull("There is no last sent date set", actualLastSent);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#getNextExecutionDate()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetNextExecutionDate() {
		final Date actualNextExecutionDate = getScheduledReport().getNextExecutionDate();

		assertNull("There is no next execution date set", actualNextExecutionDate);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#getReport()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetReport() {
		final Report actualReport = getScheduledReport().getReport();

		assertNull("There is no report set", actualReport);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#getUser()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetUser() {
		final OverseasUser actualUser = getScheduledReport().getUser();

		assertNull("There is no user set", actualUser);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#setDuration(com.bearcode.ovf.model.reportingdashboard.ReportTime)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSetDuration() {
		final ReportTime duration = ReportTime.DAILY;

		getScheduledReport().setDuration(duration);

		assertSame("The duration is set", duration, getScheduledReport().getDuration());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#setId(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSetId() {
		final Long id = 1237l;

		getScheduledReport().setId(id);

		assertSame("The ID is set", id, getScheduledReport().getId());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#setInterval(com.bearcode.ovf.model.reportingdashboard.ReportTime)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSetInterval() {
		final ReportTime interval = ReportTime.QUARTERLY;

		getScheduledReport().setInterval(interval);

		assertSame("The interval is set", interval, getScheduledReport().getInterval());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#setLastSentDate(java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSetLastSentDate() {
		final Date lastSent = new Date();

		getScheduledReport().setLastSentDate(lastSent);

		assertSame("The last sent date is set", lastSent, getScheduledReport().getLastSentDate());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#setNextExecutionDate(java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSetNextExecutionDate() {
		final Date nextExecutionDate = new Date();

		getScheduledReport().setNextExecutionDate(nextExecutionDate);

		assertSame("The next execution date is set", nextExecutionDate, getScheduledReport().getNextExecutionDate());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#setReport(com.bearcode.ovf.model.reportingdashboard.Report)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSetReport() {
		final Report report = createMock("Report", Report.class);

		getScheduledReport().setReport(report);

		assertSame("The report is set", report, getScheduledReport().getReport());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ScheduledReport#setUser(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSetUser() {
		final OverseasUser user = createMock("User", OverseasUser.class);

		getScheduledReport().setUser(user);

		assertSame("The user is set", user, getScheduledReport().getUser());
	}

	/**
	 * Gets the scheduled report.
	 * 
	 * @author IanBrown
	 * @return the scheduled report.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private ScheduledReport getScheduledReport() {
		return scheduledReport;
	}

	/**
	 * Sets the scheduled report.
	 * 
	 * @author IanBrown
	 * @param scheduledReport
	 *            the scheduled report to set.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	private void setScheduledReport(final ScheduledReport scheduledReport) {
		this.scheduledReport = scheduledReport;
	}

}
