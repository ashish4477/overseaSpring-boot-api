/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportTime;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;
import com.bearcode.ovf.service.ReportingDashboardService;

/**
 * Extended {@link BaseReportingDashboardControllerChecks} test for {@link ScheduledReports}.
 * 
 * @author IanBrown
 * 
 * @since Mar 6, 2012
 * @version Mar 12, 2012
 */
public final class ScheduledReportsTest extends BaseReportingDashboardControllerCheck<ScheduledReports> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ScheduledReports#createScheduledReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws ParseException
	 *             if there is a problem parsing the date.
	 * @since Mar 6, 2012
	 * @version Mar 12, 2012
	 */
	@Test
	public final void testCreateScheduledReport() throws ParseException {
		final long reportId = 76127l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(null, request, null, false, false);
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		EasyMock.expect(getReportingDashboardService().createScheduledReport()).andReturn(scheduledReport);
		scheduledReport.setUser(user);
		scheduledReport.setReport(report);
		scheduledReport.setDuration(ReportTime.MONTHLY);
		scheduledReport.setNextExecutionDate((Date) EasyMock.anyObject());
		scheduledReport.setInterval(ReportTime.MONTHLY);
		getReportingDashboardService().saveScheduledReport(scheduledReport);
		getReportingDashboardService().flush();
		final long scheduledReportId = 25627l;
		EasyMock.expect(scheduledReport.getId()).andReturn(scheduledReportId);
		addAttributeToModelMap(model, "scheduledReportId", scheduledReportId);
		replayAll();

		final String actualModelAndView = getBaseController().createScheduledReport(request, model, reportId);

		assertEquals("A redirection to the scheduled report editor is returned",
				EditScheduledReport.REDIRECT_EDIT_SCHEDULED_REPORT, actualModelAndView);
		verifyAll();
	}

	/**
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ScheduledReports#deleteScheduledReport(HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@Test
	public final void testDeleteScheduledReport() {
		final long scheduledReportId = 76127l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		EasyMock.expect(getReportingDashboardService().findScheduledReportById(scheduledReportId)).andReturn(scheduledReport);
		getReportingDashboardService().deleteScheduledReport(scheduledReport);
		replayAll();

		final String actualModelAndView = getBaseController().deleteScheduledReport(request, model, scheduledReportId);

		assertEquals("A redirection to the scheduled reports view is returned", ScheduledReports.REDIRECT_SCHEDULED_REPORTS,
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.ScheduledReports#getReportingDashboardService()}.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testGetReportingDashboardService() {
		final ReportingDashboardService actualReportingDashboardService = getBaseController().getReportingDashboardService();

		assertSame("The reporting dashboard service is set", getReportingDashboardService(), actualReportingDashboardService);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.ScheduledReports#scheduledReports(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testScheduledReports() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(getReportingDashboardService().findScheduledReports(user)).andReturn(scheduledReports);
		addAttributeToModelMap(model, "scheduledReports", scheduledReports);
		replayAll();

		final String actualModelAndView = getBaseController().scheduledReports(request, model);

		assertEquals("The reporting dashboard template is returned",
				ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final ScheduledReports createBaseReportingDashboardController() {
		final ScheduledReports scheduledReports = new ScheduledReports();
		return scheduledReports;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return ScheduledReports.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
		setExpectedContentBlock(ScheduledReports.DEFAULT_CONTENT_BLOCK);
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
	}
}