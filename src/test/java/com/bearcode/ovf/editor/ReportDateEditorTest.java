/**
 * 
 */
package com.bearcode.ovf.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ReportDateEditor}.
 * 
 * @author IanBrown
 * 
 * @since Aug 31, 2012
 * @version Oct 1, 2012
 */
public final class ReportDateEditorTest extends EasyMockSupport {

	/**
	 * the report date editor to test.
	 * 
	 * @author IanBrown
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	private ReportDateEditor reportDateEditor;

	/**
	 * Sets up the report date editor to test.
	 * 
	 * @author IanBrown
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	@Before
	public final void setUpReportDateEditor() {
		setReportDateEditor(createReportDateEditor());
	}

	/**
	 * Tears down the report date editor after testing.
	 * 
	 * @author IanBrown
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	@After
	public final void tearDownReportDateEditor() {
		setReportDateEditor(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.ReportDateEditor#getAsText()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	@Test
	public final void testGetAsText() {
		final String actualText = getReportDateEditor().getAsText();

		assertTrue("There is no text", actualText.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.ReportDateEditor#setAsText(java.lang.String)} using the format day-month-year.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 31, 2012
	 * @version Oct 1, 2012
	 */
	@Test
	public final void testSetAsTextString_dashesDayMonthYear() {
		final Date date = createDate();
		final String formattedDate = ReportDateEditor.DASHES_DAY_MONTH_YEAR.format(date);

		getReportDateEditor().setAsText(formattedDate);

		final Date actualDate = (Date) getReportDateEditor().getValue();
		assertEquals("The date is set", date, actualDate);
		final String expectedFormattedDate = ReportDateEditor.DEFAULT_DATE_FORMAT.format(date);
		final String actualFormattedDate = getReportDateEditor().getAsText();
		assertEquals("The date formats correctly", expectedFormattedDate, actualFormattedDate);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.ReportDateEditor#setAsText(java.lang.String)} using the format year-month-day.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 1, 2012
	 * @version Oct 1, 2012
	 */
	@Test
	public final void testSetAsTextString_dashesYearMonthDay() {
		final Date date = createDate();
		final String formattedDate = ReportDateEditor.DASHES_YEAR_MONTH_DAY.format(date);

		getReportDateEditor().setAsText(formattedDate);

		final Date actualDate = (Date) getReportDateEditor().getValue();
		assertEquals("The date is set", date, actualDate);
		final String expectedFormattedDate = ReportDateEditor.DEFAULT_DATE_FORMAT.format(date);
		final String actualFormattedDate = getReportDateEditor().getAsText();
		assertEquals("The date formats correctly", expectedFormattedDate, actualFormattedDate);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.ReportDateEditor#setAsText(java.lang.String)} using the format month/day/year.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 1, 2012
	 * @version Oct 1, 2012
	 */
	@Test
	public final void testSetAsTextString_slashesMonthDayYear() {
		final Date date = createDate();
		final String formattedDate = ReportDateEditor.SLASHES_MONTH_DAY_YEAR.format(date);

		getReportDateEditor().setAsText(formattedDate);

		final Date actualDate = (Date) getReportDateEditor().getValue();
		assertEquals("The date is set", date, actualDate);
		final String expectedFormattedDate = ReportDateEditor.DEFAULT_DATE_FORMAT.format(date);
		final String actualFormattedDate = getReportDateEditor().getAsText();
		assertEquals("The date formats correctly", expectedFormattedDate, actualFormattedDate);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.ReportDateEditor#setAsText(java.lang.String)} for a bad date format.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSetAsTextString_unsupportedFormat() {
		final Date date = createDate();
		final SimpleDateFormat unsupportedDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		final String formattedDate = unsupportedDateFormat.format(date);

		getReportDateEditor().setAsText(formattedDate);
	}

	/**
	 * Creates an object that is just the date. All time values are zeroed.
	 * 
	 * @author IanBrown
	 * @return the date.
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	private Date createDate() {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		final Date date = calendar.getTime();
		return date;
	}

	/**
	 * Creates a report date editor.
	 * 
	 * @author IanBrown
	 * @return the report date editor.
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	private ReportDateEditor createReportDateEditor() {
		final ReportDateEditor reportDateEditor = new ReportDateEditor();
		return reportDateEditor;
	}

	/**
	 * Gets the report date editor.
	 * 
	 * @author IanBrown
	 * @return the report date editor.
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	private ReportDateEditor getReportDateEditor() {
		return reportDateEditor;
	}

	/**
	 * Sets the report date editor.
	 * 
	 * @author IanBrown
	 * @param reportDateEditor
	 *            the report date editor to set.
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	private void setReportDateEditor(final ReportDateEditor reportDateEditor) {
		this.reportDateEditor = reportDateEditor;
	}
}
