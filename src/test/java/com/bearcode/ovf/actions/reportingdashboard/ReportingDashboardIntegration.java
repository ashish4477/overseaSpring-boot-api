/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;

/**
 * Extended {@link BaseReportingDashboardControllerExam} integration test for {@link ReportingDashboard}.
 * 
 * @author IanBrown
 * 
 * @since Jan 3, 2012
 * @version May 16, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "ReportingDashboardIntegration-context.xml" })
public final class ReportingDashboardIntegration extends BaseReportingDashboardControllerExam<ReportingDashboard> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportingDashboard#reportingDashboardHome(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is an administrative user.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 3, 2012
	 * @version May 16, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	public final void testReportingDashboardHome_administrativeUser() throws Exception {
		fixReportFields();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ReportingDashboard.htm");
		request.setMethod("GET");
		final OverseasUser user = new OverseasUser();
		user.setId(2l);
		final UserRole role = new UserRole();
		role.setRoleName(UserRole.USER_ROLE_ADMIN);
		final Collection<UserRole> roles = Arrays.asList(role);
		user.setRoles(roles);
		setUpAuthentication(user);
		final FaceConfig currentFace = getFacesService().findConfig("localhost");

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is used as the view", BaseReportingDashboardController.DEFAULT_TEMPLATE,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertStandardReports(actualModel, true);
		assertCustomReports(user, currentFace, actualModel, true);
		final Map<String, Report> standardReports = getReportingDashboardService().findStandardReports();
		final List<Report> displayReports = Arrays.asList(standardReports.get("Usage_by_Request_Type"),
				standardReports.get("Completed_by_Request_Type"));
		assertReportsData(displayReports, actualModel);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportingDashboard#reportingDashboardHome(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is a non-owner user.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 3, 2012
	 * @version May 16, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	public final void testReportingDashboardHome_notOwnerUser() throws Exception {
		fixReportFields();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ReportingDashboard.htm");
		request.setMethod("GET");
		final OverseasUser user = new OverseasUser();
		user.setId(2l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is used as the view", BaseReportingDashboardController.DEFAULT_TEMPLATE,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, false);
		final Map<String, Report> standardReports = getReportingDashboardService().findStandardReports();
		final List<Report> displayReports = Arrays.asList(standardReports.get("Usage_by_Request_Type"),
				standardReports.get("Completed_by_Request_Type"));
		assertReportsData(displayReports, actualModel);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportingDashboard#reportingDashboardHome(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is no user.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 3, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	public final void testReportingDashboardHome_noUser() throws Exception {
		fixReportFields();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ReportingDashboard.htm");
		request.setMethod("GET");

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is used as the view", BaseReportingDashboardController.DEFAULT_TEMPLATE,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertStandardReports(actualModel, false);
		assertCustomReports(null, null, actualModel, false);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ReportingDashboard#reportingDashboardHome(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there is an owner user.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 3, 2012
	 * @version May 16, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	public final void testReportingDashboardHome_ownerUser() throws Exception {
		fixReportFields();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ReportingDashboard.htm");
		request.setMethod("GET");
		final OverseasUser user = getOverseasUserDAO().findById(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is used as the view", BaseReportingDashboardController.DEFAULT_TEMPLATE,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
		final Map<String, Report> standardReports = getReportingDashboardService().findStandardReports();
		final List<Report> displayReports = Arrays.asList(standardReports.get("Usage_by_Request_Type"),
				standardReports.get("Completed_by_Request_Type"));
		assertReportsData(displayReports, actualModel);
	}

	/** {@inheritDoc} */
	@Override
	protected final ReportingDashboard createBaseReportingDashboardController() {
		final ReportingDashboard reportingDashboard = applicationContext.getBean(ReportingDashboard.class);
		return reportingDashboard;
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
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
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
	 * Custom assertion to ensure that the data for the report is correct.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param columnHeaders
	 *            the column headers.
	 * @param reportRows
	 *            the report rows.
	 * @param totals
	 *            the totals.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	private void assertReportData(final Report report, final List<String> columnHeaders, final List<List<String>> reportRows,
			final List<String> totals) {
		final List<ReportColumn> columns = report.getColumns();
		assertColumnHeaders(columns, columnHeaders);
		final Long totalCount = assertReportRows(columns, reportRows);
		assertTotals(columns, totals, totalCount);
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
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
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
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
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
	 * Custom assertion to ensure that the data for the reports is in the model.
	 * 
	 * @author IanBrown
	 * @param reports
	 *            the reports.
	 * @param model
	 *            the model.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@SuppressWarnings("unchecked")
	private void assertReportsData(final List<Report> reports, final ModelMap model) {
		final Map<String, List<String>> columnHeaders = (Map<String, List<String>>) model.get("columnHeaders");
		assertNotNull("The column headers are in the model", columnHeaders);
		assertEquals("There are column headers for all of the reports", reports.size(), columnHeaders.size());
		final Map<String, List<List<String>>> reportRows = (Map<String, List<List<String>>>) model.get("reportRows");
		assertNotNull("The report rows are in the model", reportRows);
		assertEquals("There are report rows for all of the reports", reports.size(), reportRows.size());
		final Map<String, List<String>> totals = (Map<String, List<String>>) model.get("totals");
		assertNotNull("The totals are in the model", totals);
		assertEquals("There are totals for all of the reports", reports.size(), totals.size());

		for (final Report report : reports) {
			final String reportName = report.getTitle().replace(' ', '_').replace('&', '_');
			assertReportData(report, columnHeaders.get(reportName), reportRows.get(reportName), totals.get(reportName));
		}
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
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
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
