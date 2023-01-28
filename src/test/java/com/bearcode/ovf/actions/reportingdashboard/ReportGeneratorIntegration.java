/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;

/**
 * Extended {@link BaseReportingDashboardControllerExam} integration test for {@link ReportGenerator}.
 * 
 * @author IanBrown
 * 
 * @since Jan 30, 2012
 * @version Mar 13, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "ReportingDashboardIntegration-context.xml" })
public final class ReportGeneratorIntegration extends BaseReportingDashboardControllerExam<ReportGenerator> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportGenerator#viewReport(javax.servlet.http.HttpServletRequest, HttpServletResponse, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 30, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	public final void testViewReport() throws Exception {
		final long reportId = 1l;
		final Report report = getReportingDashboardService().findReportById(reportId);
		assertNotNull("There is a report for the identifier", report);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ViewReport.htm");
		request.setMethod("GET");
		request.addParameter("reportId", Long.toString(reportId));
		fixReportFields();
		final OverseasUser user = getOverseasUserDAO().findById(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The main template is used as the view", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
		assertEquals("The report is in the model", report, actualModel.get("report"));
		final List<String> columnHeaders = (List<String>) actualModel.get("columnHeaders");
		final List<ReportColumn> columns = report.getColumns();
		assertColumnHeaders(columns, columnHeaders);
		final List<List<String>> reportRows = (List<List<String>>) actualModel.get("reportRows");
		final Long totalCount = assertReportRows(columns, reportRows);
		assertTotals(columns, (List<String>) actualModel.get("totals"), totalCount);
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
	}

	/** {@inheritDoc} */
	@Override
	protected final ReportGenerator createBaseReportingDashboardController() {
		final ReportGenerator reportGenerator = applicationContext.getBean(ReportGenerator.class);
		return reportGenerator;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
	}

	/**
	 * Custom assertion to ensure that there are the correct column headers.
	 * 
	 * @author IanBrown
	 * @param columns
	 *            the columns.
	 * @param columnHeaders
	 *            the column headers.
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	private void assertColumnHeaders(final List<ReportColumn> columns, final List<String> columnHeaders) {
		assertNotNull("There are column headers", columnHeaders);
		assertEquals("There are the correct number of column headers", columns.size(), columnHeaders.size());
		for (int columnIdx = 0; columnIdx < columns.size(); ++columnIdx) {
			final ReportColumn column = columns.get(columnIdx);
			final String columnHeader = columnHeaders.get(columnIdx);
			assertEquals("The column header for column #" + (columnIdx + 1) + " is correct", column.getName(), columnHeader);
		}
	}

	/**
	 * Custom assertion to ensure that the row of the report makes sense - in this case, there is an entry for each column. The code
	 * also locates the Number column and returns its value.
	 * 
	 * @author IanBrown
	 * @param columns
	 *            the columns.
	 * @param reportRow
	 *            the report row.
	 * @return the value for the Number column (if any).
	 * @since Jan 30, 2012
	 * @version Mar 2, 2012
	 */
	private Long assertReportRow(final List<ReportColumn> columns, final List<String> reportRow) {
		assertNotNull("There is data for the report row", reportRow);
		assertEquals("There are the right number of columns in the report row", columns.size(), reportRow.size());

		for (int idx = 0; idx < columns.size(); ++idx) {
			final ReportColumn column = columns.get(idx);
			if (column.getName().equals("Number")) {
				return Long.parseLong(reportRow.get(idx));
			}
		}

		return null;
	}

	/**
	 * Custom assertion to ensure that the rows of the report make sense.
	 * 
	 * @author IanBrown
	 * @param columns
	 *            the columns in the report.
	 * @param reportRows
	 *            the report rows.
	 * @return the total for the Number column (<code>null</code> if there is no Number column).
	 * @since Jan 30, 2012
	 * @version Mar 2, 2012
	 */
	private Long assertReportRows(final List<ReportColumn> columns, final List<List<String>> reportRows) {
		Long totalCount = null;

		assertNotNull("There are report rows", reportRows);
		assertTrue("There is at least one row in the report", reportRows.size() > 0);
		for (final List<String> reportRow : reportRows) {
			final Long rowCount = assertReportRow(columns, reportRow);
			totalCount = (totalCount == null) ? rowCount : (totalCount + rowCount);
		}

		return totalCount;
	}

	/**
	 * Custom assertion to ensure that the totals row is correct.
	 * 
	 * @author IanBrown
	 * @param columns
	 *            the columns in the report.
	 * @param totals
	 *            the totals row.
	 * @param totalCount
	 *            the total count expected.
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	private void assertTotals(final List<ReportColumn> columns, final List<String> totals, final Long totalCount) {
		if (totalCount == null) {
			assertNull("There is no totals row", totals);
		} else {
			for (int idx = 0; idx < columns.size(); ++idx) {
				final ReportColumn column = columns.get(idx);
				if (column.getName().equals("Number")) {
					assertEquals("The total count is correct", totalCount.longValue(), Long.parseLong(totals.get(idx)));
					break;
				}
			}
		}
	}
}
