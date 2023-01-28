/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collection;

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
import com.bearcode.ovf.model.reportingdashboard.ReportTime;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;

/**
 * Extended {@link BaseReportingDashboardControllerExam} test for {@link EditScheduledReport}.
 * 
 * @author IanBrown
 * 
 * @since Mar 7, 2012
 * @version Mar 28, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "ReportingDashboardIntegration-context.xml" })
public final class EditScheduledReportIntegration extends BaseReportingDashboardControllerExam<EditScheduledReport> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditScheduledReport#editScheduledReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the scheduled report.
	 * @since Mar 7, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml",
			"com/bearcode/ovf/actions/reportingdashboard/ScheduledReports.xml" })
	public final void testEditScheduledReport() throws Exception {
		final long scheduledReportId = 1l;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/EditScheduledReport.htm");
		request.setMethod("GET");
		request.addParameter("scheduledReportId", Long.toString(scheduledReportId));
		final OverseasUser user = getOverseasUserDAO().findById(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is as the view",
				ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final ScheduledReport actualScheduledReport = (ScheduledReport) actualModel.get("scheduledReport");
		assertNotNull("There is a scheduled report in the model", actualScheduledReport);
		assertEquals("The scheduled report has the expected ID", (Long) scheduledReportId, actualScheduledReport.getId());
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditScheduledReport#saveScheduledReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 7, 2012
	 * @version Mar 12, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml",
			"com/bearcode/ovf/actions/reportingdashboard/ScheduledReports.xml" })
	public final void testSaveScheduledReport() throws Exception {
		final long scheduledReportId = 1l;
		final ScheduledReport scheduledReport = getReportingDashboardService().findScheduledReportById(scheduledReportId);
		final ReportTime newDuration = rollReportTime(scheduledReport.getDuration());
		final ReportTime newInterval = rollReportTime(scheduledReport.getInterval());
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/EditScheduledReport.htm");
		request.setMethod("POST");
		request.addParameter("scheduledReportId", Long.toString(scheduledReportId));
		request.addParameter("userId", Long.toString(scheduledReport.getUser().getId()));
		request.addParameter("reportId", Long.toString(scheduledReport.getReport().getId()));
		request.addParameter("duration", newDuration.name());
		request.addParameter("interval", newInterval.name());
		final OverseasUser user = getOverseasUserDAO().findById(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("A redirection to the scheduled reports list is returned", ScheduledReports.REDIRECT_SCHEDULED_REPORTS,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final ScheduledReport actualScheduledReport = getReportingDashboardService().findScheduledReportById(scheduledReportId);
		assertSame("The duration has changed", newDuration, actualScheduledReport.getDuration());
		assertSame("The interval has changed", newInterval, actualScheduledReport.getInterval());
	}

	/** {@inheritDoc} */
	@Override
	protected final EditScheduledReport createBaseReportingDashboardController() {
		final EditScheduledReport editScheduledReport = applicationContext.getBean(EditScheduledReport.class);
		return editScheduledReport;
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
	 * Increments the value of the report time to the entry in the enumeration, rolling to the first value if necessary.
	 * 
	 * @author IanBrown
	 * @param reportTime
	 *            the report time.
	 * @return the new report time.
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	private ReportTime rollReportTime(final ReportTime reportTime) {
		if (reportTime == null) {
			return ReportTime.DAILY;
		}

		int reportTimeIdx = reportTime.ordinal();
		reportTimeIdx = (reportTimeIdx + 1) % ReportTime.values().length;
		return ReportTime.values()[reportTimeIdx];
	}
}
