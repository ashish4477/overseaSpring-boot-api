/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.service.ReportingDashboardService;

/**
 * Extended {@link BaseReportingDashboardControllerCheck} test for {@link ReportGenerator}.
 * 
 * @author IanBrown
 * 
 * @since Jan 30, 2012
 * @version Apr 2, 2012
 */
public final class ReportGeneratorTest extends BaseReportingDashboardControllerCheck<ReportGenerator> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportGenerator#exportExcelReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem building the response.
	 * @since Mar 6, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testExportExcelReport() throws IOException {
		final long reportId = 6352l;
		final Date startDate = new Date();
		final Date endDate = new Date();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("reportId", Long.toString(reportId));
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final String columnHeader1 = "Column Header";
		final String columnHeader2 = "Number";
		final String columnHeader3 = "Percentage";
		final List<String> columnHeaders = Arrays.asList(columnHeader1, columnHeader2, columnHeader3);
		EasyMock.expect(getReportingDashboardService().extractColumnHeaders(report)).andReturn(columnHeaders);
		final String columnData1 = "Column Data 1";
		final long count1 = 36l;
		final long count2 = 56l;
		final double total = count1 + count2;
		final List<String> reportRow1 = Arrays.asList(columnData1, Long.toString(count1),
				ReportingDashboardService.PERCENTAGE_FORMAT.format(count1 / total));
		final String columnData2 = "Column Data 2";
		final List<String> reportRow2 = Arrays.asList(columnData2, Long.toString(count2),
				ReportingDashboardService.PERCENTAGE_FORMAT.format(count2 / total));
		final List<List<String>> reportRows = Arrays.asList(reportRow1, reportRow2);
		EasyMock.expect(getReportingDashboardService().report(report, startDate, endDate)).andReturn(reportRows);
		final long totalCount = count1 + count2;
		final List<String> totals = Arrays.asList("", Long.toString(totalCount));
		EasyMock.expect(getReportingDashboardService().totals(report, reportRows)).andReturn(totals);
		final String reportTitle = "Report Title";
		EasyMock.expect(report.getTitle()).andReturn(reportTitle);
		final ReportGeneratorValet valet = createMock("Valet", ReportGeneratorValet.class);
		getBaseController().setValet(valet);
		final HSSFWorkbook workbook = createMock("Workbook", HSSFWorkbook.class);
		EasyMock.expect(valet.acquireWorkbook()).andReturn(workbook);
		final HSSFSheet sheet = createMock("Sheet", HSSFSheet.class);
		EasyMock.expect(workbook.createSheet(reportTitle.replaceAll("[^\\d\\w]", "_"))).andReturn(sheet);
		int rownum = 0;
		short cellnum = 0;
		HSSFRow row = createRow(sheet, rownum);
		HSSFCell cell = createCell(row, rownum, cellnum);
		cell.setCellValue(createRichText(valet, rownum, cellnum, "Report " + reportTitle));
		++rownum;
		++rownum;
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		row = createRow(sheet, rownum);
		cell = createCell(row, rownum, cellnum);
		cell.setCellValue(createRichText(valet, rownum, cellnum, "For FPCA, FWAB, DOMESTIC_REGISTRATION and DOMESTIC_ABSENTEE"));
		++rownum;
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		row = createRow(sheet, rownum);
		cell = createCell(row, rownum, cellnum);
		cell.setCellValue(createRichText(valet, rownum, cellnum, "For all hosted sites"));
		++rownum;
		row = createRow(sheet, rownum);
		for (final String columnHeader : columnHeaders) {
			cell = createCell(row, rownum, cellnum);
			cell.setCellValue(createRichText(valet, rownum, cellnum, columnHeader));
			++cellnum;
		}
		++rownum;
		cellnum = 0;
		for (final List<String> reportRow : reportRows) {
			row = createRow(sheet, rownum);
			for (final String columnData : reportRow) {
				cell = createCell(row, rownum, cellnum);
				cell.setCellValue(createRichText(valet, rownum, cellnum, columnData));
				++cellnum;
			}
			++rownum;
			cellnum = 0;
		}
		workbook.write((OutputStream) EasyMock.anyObject());
		row = createRow(sheet, rownum);
		for (final String columnData : totals) {
			cell = createCell(row, rownum, cellnum);
			cell.setCellValue(createRichText(valet, rownum, cellnum, columnData));
			++cellnum;
		}
		++rownum;
		cellnum = 0;
		replayAll();

		getBaseController().exportExcelReport(request, response, model, reportId, startDate, endDate);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.ReportGenerator#getReportingDashboardService()}.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	@Test
	public final void testGetReportingDashboardService() {
		final ReportingDashboardService actualReportingDashboardService = getBaseController().getReportingDashboardService();

		assertSame("The reporting dashboard service is set", getReportingDashboardService(), actualReportingDashboardService);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.ReportGenerator#getValet()}.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	@Test
	public final void testGetValet() {
		final ReportGeneratorValet actualValet = getBaseController().getValet();

		assertNotNull("There is a default valet", actualValet);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.ReportGenerator#setValet(ReportGeneratorValet)}.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	@Test
	public final void testSetValet() {
		final ReportGeneratorValet valet = createMock("Valet", ReportGeneratorValet.class);

		getBaseController().setValet(valet);

		assertSame("The valet is set", valet, getBaseController().getValet());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportGenerator#viewReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * for the case where there is no Number column.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testViewReport_noNumberColumn() {
		final long reportId = 6352l;
		final Date startDate = new Date();
		final Date endDate = new Date();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("reportId", Long.toString(reportId));
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final String columnHeader1 = "Column Header";
		final List<String> columnHeaders = Arrays.asList(columnHeader1);
		EasyMock.expect(getReportingDashboardService().extractColumnHeaders(report)).andReturn(columnHeaders);
		final String columnData1 = "Column Data 1";
		final List<String> reportRow1 = Arrays.asList(columnData1);
		final String columnData2 = "Column Data 2";
		final List<String> reportRow2 = Arrays.asList(columnData2);
		final List<List<String>> reportRows = Arrays.asList(reportRow1, reportRow2);
		EasyMock.expect(getReportingDashboardService().report(report, startDate, endDate)).andReturn(reportRows);
		EasyMock.expect(getReportingDashboardService().totals(report, reportRows)).andReturn(null);
		addAttributeToModelMap(model, "report", report);
		addAttributeToModelMap(model, "startDate", ReportGenerator.DATE_FORMATTER.format(startDate));
		addAttributeToModelMap(model, "endDate", ReportGenerator.DATE_FORMATTER.format(endDate));
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		addAttributeToModelMap(model, EasyMock.eq("headerRows"), EasyMock.anyObject());
		addAttributeToModelMap(model, "columnHeaders", columnHeaders);
		addAttributeToModelMap(model, "reportRows", reportRows);
		replayAll();

		final String actualModelAndView = getBaseController().viewReport(request, model, reportId, startDate, endDate);

		assertEquals("The reporting dashboard template is returned", ReportGenerator.DEFAULT_TEMPLATE, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportGenerator#viewReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * for the case where there is a Number column.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testViewReport_numberColumn() {
		final long reportId = 6352l;
		final Date startDate = new Date();
		final Date endDate = new Date();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("reportId", Long.toString(reportId));
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final String columnHeader1 = "Column Header";
		final String columnHeader2 = "Number";
		final String columnHeader3 = "Percentage";
		final List<String> columnHeaders = Arrays.asList(columnHeader1, columnHeader2, columnHeader3);
		EasyMock.expect(getReportingDashboardService().extractColumnHeaders(report)).andReturn(columnHeaders);
		final String columnData1 = "Column Data 1";
		final long count1 = 36l;
		final long count2 = 56l;
		final double total = count1 + count2;
		final List<String> reportRow1 = Arrays.asList(columnData1, Long.toString(count1),
				ReportingDashboardService.PERCENTAGE_FORMAT.format(count1 / total));
		final String columnData2 = "Column Data 2";
		final List<String> reportRow2 = Arrays.asList(columnData2, Long.toString(count2),
				ReportingDashboardService.PERCENTAGE_FORMAT.format(count2 / total));
		final List<List<String>> reportRows = Arrays.asList(reportRow1, reportRow2);
		EasyMock.expect(getReportingDashboardService().report(report, startDate, endDate)).andReturn(reportRows);
		final long totalCount = count1 + count2;
		final List<String> totals = Arrays.asList("", Long.toString(totalCount));
		EasyMock.expect(getReportingDashboardService().totals(report, reportRows)).andReturn(totals);
		addAttributeToModelMap(model, "report", report);
		addAttributeToModelMap(model, "startDate", ReportGenerator.DATE_FORMATTER.format(startDate));
		addAttributeToModelMap(model, "endDate", ReportGenerator.DATE_FORMATTER.format(endDate));
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		addAttributeToModelMap(model, EasyMock.eq("headerRows"), EasyMock.anyObject());
		addAttributeToModelMap(model, "columnHeaders", columnHeaders);
		addAttributeToModelMap(model, "reportRows", reportRows);
		addAttributeToModelMap(model, "totals", totals);
		replayAll();

		final String actualModelAndView = getBaseController().viewReport(request, model, reportId, startDate, endDate);

		assertEquals("The reporting dashboard template is returned", ReportGenerator.DEFAULT_TEMPLATE, actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final ReportGenerator createBaseReportingDashboardController() {
		final ReportGenerator reportGenerator = new ReportGenerator();
		return reportGenerator;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return ReportGenerator.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
		setExpectedContentBlock(ReportGenerator.DEFAULT_CONTENT_BLOCK);
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
	}

	/**
	 * Creates a cell for the specified row.
	 * 
	 * @author IanBrown
	 * @param row
	 *            the row.
	 * @param rownum
	 *            the number of the row.
	 * @param cellnum
	 *            the cell number on the row.
	 * @return the cell.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private HSSFCell createCell(final HSSFRow row, final int rownum, final short cellnum) {
		final HSSFCell cell = createMock("Cell_" + rownum + "_" + cellnum, HSSFCell.class);
		EasyMock.expect(row.createCell(cellnum)).andReturn(cell);
		return cell;
	}

	/**
	 * Creates a rich text string for the input cell.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to acquire resources.
	 * @param rownum
	 *            the row number.
	 * @param cellnum
	 *            the cell number.
	 * @param text
	 *            the text.
	 * @return the rich text string.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private HSSFRichTextString createRichText(final ReportGeneratorValet valet, final int rownum, final short cellnum,
			final String text) {
		final HSSFRichTextString richText = createMock("RichText_" + rownum + "_" + cellnum, HSSFRichTextString.class);
		EasyMock.expect(valet.acquireRichTextString(text)).andReturn(richText);
		return richText;
	}

	/**
	 * Creates a row for the specified sheet.
	 * 
	 * @author IanBrown
	 * @param sheet
	 *            the sheet.
	 * @param rownum
	 *            the row number.
	 * @return the row.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private HSSFRow createRow(final HSSFSheet sheet, final int rownum) {
		final HSSFRow row = createMock("Row_" + rownum, HSSFRow.class);
		EasyMock.expect(sheet.createRow(rownum)).andReturn(row);
		return row;
	}
}
