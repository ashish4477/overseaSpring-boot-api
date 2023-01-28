/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.service.ReportingDashboardService;

/**
 * Extended {@link BaseControllerCheck} test for {@link ReportingDashboard}.
 * 
 * @author IanBrown
 * 
 * @since Jan 4, 2012
 * @version May 17, 2012
 */
public final class ReportingDashboardTest extends BaseReportingDashboardControllerCheck<ReportingDashboard> {

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.ReportingDashboard#getReportingDashboardService()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	@Test
	public final void testGetReportingDashboardService() {
		final ReportingDashboardService actualReportingDashboardService = getBaseController().getReportingDashboardService();

		assertSame("The reporting dashboard service is set", getReportingDashboardService(), actualReportingDashboardService);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportingDashboard#reportingDashboardHome(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version May 17, 2012
	 */
	@Test
	public final void testReportingDashboardHome() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Map<String, Report> standardReports = new HashMap<String, Report>();
		final Report usageByRequestType = createReport("Usage by Request Type");
		standardReports.put("Usage_by_Request_Type", usageByRequestType);
		final Report completedByRequestType = createReport("Completed by Request Type");
		standardReports.put("Completed_by_Request_Type", completedByRequestType);
		EasyMock.expect(getReportingDashboardService().findStandardReports()).andReturn(standardReports);
		final Map<String, List<String>> columnHeaders = new HashMap<String, List<String>>();
		final Map<String, List<List<String>>> reportRows = new HashMap<String, List<List<String>>>();
		final Map<String, List<String>> totals = new HashMap<String, List<String>>();
		columnHeaders.put("Usage_by_Request_Type", createColumnHeaders(usageByRequestType));
		reportRows.put("Usage_by_Request_Type", createReportRows(usageByRequestType));
		totals.put("Usage_by_Request_Type", createTotals(usageByRequestType, reportRows.get("Usage_by_Request_Type")));
		columnHeaders.put("Completed_by_Request_Type", createColumnHeaders(completedByRequestType));
		reportRows.put("Completed_by_Request_Type", createReportRows(completedByRequestType));
		totals.put("Completed_by_Request_Type", createTotals(completedByRequestType, reportRows.get("Completed_by_Request_Type")));
		addAttributeToModelMap(model, EasyMock.eq("columnHeaders"), EasyMock.eq(columnHeaders));
		addAttributeToModelMap(model, EasyMock.eq("reportRows"), EasyMock.eq(reportRows));
		addAttributeToModelMap(model, EasyMock.eq("totals"), EasyMock.eq(totals));
		replayAll();

		final String actualModelAndView = getBaseController().reportingDashboardHome(request, model);

		assertEquals("The reporting dashboard template is returned", BaseReportingDashboardController.DEFAULT_TEMPLATE,
				actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final ReportingDashboard createBaseReportingDashboardController() {
		final ReportingDashboard reportingDashboard = new ReportingDashboard();
		return reportingDashboard;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return ReportingDashboard.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
		setExpectedContentBlock(ReportingDashboard.DEFAULT_CONTENT_BLOCK);
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
	}

	/**
	 * Creates column headers for the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @return the column headers.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	private List<String> createColumnHeaders(final Report report) {
		final List<String> columnHeaders = Arrays.asList(report.toString());
		EasyMock.expect(getReportingDashboardService().extractColumnHeaders(report)).andReturn(columnHeaders);
		return columnHeaders;
	}

	/**
	 * Creates a report for testing.
	 * 
	 * @author IanBrown
	 * @param reportTitle
	 *            the title for the report.
	 * @return the report.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	private Report createReport(final String reportTitle) {
		final Report report = createMock(reportTitle.replace(' ', '_').replace('&', '_'), Report.class);
		EasyMock.expect(report.getTitle()).andReturn(reportTitle).anyTimes();
		return report;
	}

	/**
	 * Creates rows of data for the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @return the report rows.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@SuppressWarnings("unchecked")
	private List<List<String>> createReportRows(final Report report) {
		final List<String> row1 = Arrays.asList(report.toString() + "Row1");
		final List<String> row2 = Arrays.asList(report.toString() + "Row2");
		final List<List<String>> reportRows = Arrays.asList(row1, row2);
		EasyMock.expect(
				getReportingDashboardService()
						.report(EasyMock.eq(report), (Date) EasyMock.anyObject(), (Date) EasyMock.anyObject())).andReturn(
				reportRows);
		return reportRows;
	}

	/**
	 * Creates the totals for the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param reportRows
	 *            the report rows.
	 * @return the totals.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	private List<String> createTotals(final Report report, final List<List<String>> reportRows) {
		final List<String> totals = Arrays.asList(report.toString());
		EasyMock.expect(getReportingDashboardService().totals(report, reportRows)).andReturn(totals);
		return totals;
	}
}
