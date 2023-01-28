/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.reportingdashboard.ReportTime;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;

/**
 * Extended {@link BaseReportingDashboardControllerCheck} test for {@link EditScheduledReport}.
 * 
 * @author IanBrown
 * 
 * @since Mar 7, 2012
 * @version Mar 12, 2012
 */
public final class EditScheduledReportTest extends BaseReportingDashboardControllerCheck<EditScheduledReport> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditScheduledReport#editScheduledReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	@Test
	public final void testEditScheduledReport() {
		final long scheduledReportId = 6723l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("scheduledReportId", Long.toString(scheduledReportId));
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		EasyMock.expect(getReportingDashboardService().findScheduledReportById(scheduledReportId)).andReturn(scheduledReport);
		addAttributeToModelMap(model, "scheduledReport", scheduledReport);
		addAttributeToModelMap(model, EasyMock.eq("timeSpans"), EasyMock.anyObject());
		replayAll();

		final String actualModelAndView = getBaseController().editScheduledReport(request, model, scheduledReportId);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditScheduledReport#saveScheduledReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * for the case where the duration is changed.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ParseException
	 *             if there is a problem parsing the next execution date.
	 * @since Mar 7, 2012
	 * @version Mar 16, 2012
	 */
	@Test
	public final void testSaveScheduledReport_duration() throws ParseException {
		final long scheduledReportId = 6723l;
		final ReportTime newDuration = ReportTime.MONTHLY;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("scheduledReportId", Long.toString(scheduledReportId));
		request.addParameter("duration", newDuration.name());
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		EasyMock.expect(getReportingDashboardService().findScheduledReportById(scheduledReportId)).andReturn(scheduledReport);
		scheduledReport.setDuration(newDuration);
		getReportingDashboardService().saveScheduledReport(scheduledReport);
		getReportingDashboardService().flush();
		replayAll();

		final String actualModelAndView = getBaseController().saveScheduledReport(request, model, scheduledReportId);

		assertEquals("A redirection to the scheduled reports list is returned", ScheduledReports.REDIRECT_SCHEDULED_REPORTS,
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditScheduledReport#saveScheduledReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * for the case where the interval is changed.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ParseException
	 *             if there is a problem parsing the next execution date.
	 * @since Mar 7, 2012
	 * @version Mar 16, 2012
	 */
	@Test
	public final void testSaveScheduledReport_interval() throws ParseException {
		final long scheduledReportId = 6723l;
		final ReportTime newInterval = ReportTime.YEARLY;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("scheduledReportId", Long.toString(scheduledReportId));
		request.addParameter("interval", newInterval.name());
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		EasyMock.expect(getReportingDashboardService().findScheduledReportById(scheduledReportId)).andReturn(scheduledReport);
		scheduledReport.setInterval(newInterval);
		scheduledReport.setNextExecutionDate((Date) EasyMock.anyObject());
		getReportingDashboardService().saveScheduledReport(scheduledReport);
		getReportingDashboardService().flush();
		replayAll();

		final String actualModelAndView = getBaseController().saveScheduledReport(request, model, scheduledReportId);

		assertEquals("A redirection to the scheduled reports list is returned", ScheduledReports.REDIRECT_SCHEDULED_REPORTS,
				actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EditScheduledReport createBaseReportingDashboardController() {
		final EditScheduledReport editScheduledReport = new EditScheduledReport();
		return editScheduledReport;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return EditScheduledReport.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
		setExpectedContentBlock(EditScheduledReport.DEFAULT_CONTENT_BLOCK);
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
	}
}
