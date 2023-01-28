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

import javax.servlet.http.HttpServletRequest;

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
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;

/**
 * Extended {@link BaseReportingDashboardControllerExam} integration test for {@link ScheduledReports}.
 * 
 * @author IanBrown
 * 
 * @since Mar 6, 2012
 * @version Mar 9, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "ReportingDashboardIntegration-context.xml" })
public final class ScheduledReportsIntegration extends BaseReportingDashboardControllerExam<ScheduledReports> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ScheduledReports#createScheduledReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 6, 2012
	 * @version Mar 7, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml",
			"com/bearcode/ovf/actions/reportingdashboard/ScheduledReports.xml" })
	public final void testCreateScheduledReport() throws Exception {
		final Long reportId = 1l;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/CreateScheduledReport.htm");
		request.setMethod("GET");
		request.addParameter("reportId", reportId.toString());
		final OverseasUser user = getOverseasUserDAO().findById(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("A redirection to the scheduled report editor is returned",
				EditScheduledReport.REDIRECT_EDIT_SCHEDULED_REPORT, actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final long actualScheduledReportId = (Long) actualModel.get("scheduledReportId");
		assertTrue("There is a scheduled report identifier", actualScheduledReportId != 0l);
		final ScheduledReport actualScheduledReport = getReportingDashboardService().findScheduledReportById(
				actualScheduledReportId);
		assertNotNull("There is a scheduled report", actualScheduledReport);
		assertNotNull("The scheduled report is for a user", actualScheduledReport.getUser());
		assertEquals("The scheduled report is for the correct report", reportId, actualScheduledReport.getReport().getId());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ScheduledReports#deleteScheduledReport(HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml",
			"com/bearcode/ovf/actions/reportingdashboard/ScheduledReports.xml" })
	public final void testDeleteScheduledReport() throws Exception {
		final Long scheduledReportId = 1l;
		final ScheduledReport scheduledReport = getReportingDashboardService().findScheduledReportById(scheduledReportId);
		assertNotNull("The scheduled report exists", scheduledReport);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/DeleteScheduledReport.htm");
		request.setMethod("GET");
		request.addParameter("scheduledReportId", scheduledReportId.toString());
		final OverseasUser user = getOverseasUserDAO().findById(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("A redirection to the scheduled reports view is returned", ScheduledReports.REDIRECT_SCHEDULED_REPORTS,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final ScheduledReport actualScheduledReport = getReportingDashboardService().findScheduledReportById(scheduledReportId);
		assertNull("The scheduled report has been deleted", actualScheduledReport);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ScheduledReports#scheduledReports(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 6, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml",
			"com/bearcode/ovf/actions/reportingdashboard/ScheduledReports.xml" })
	public final void testScheduledReports() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ScheduledReports.htm");
		request.setMethod("GET");
		final OverseasUser user = getOverseasUserDAO().findById(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is as the view",
				ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertNotNull("There are scheduled reports in the model", actualModel.get("scheduledReports"));
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
	}

	/** {@inheritDoc} */
	@Override
	protected final ScheduledReports createBaseReportingDashboardController() {
		final ScheduledReports scheduledReports = applicationContext.getBean(ScheduledReports.class);
		return scheduledReports;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
	}
}
