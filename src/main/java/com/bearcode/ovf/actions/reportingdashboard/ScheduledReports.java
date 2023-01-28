/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportTime;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;

/**
 * Extended {@link BaseReportingDashboardController} to allow a user to view his or her scheduled reports.
 * 
 * @author IanBrown
 * 
 * @since Mar 6, 2012
 * @version Mar 12, 2012
 */
@Controller
public class ScheduledReports extends BaseReportingDashboardController {

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/reportingdashboard/ScheduledReports.jsp";

	/**
	 * the default section name.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	static final String DEFAULT_SECTION_NAME = "admin";

	/**
	 * redirects to the scheduled reports.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	static final String REDIRECT_SCHEDULED_REPORTS = "redirect:ScheduledReports.htm";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "Reporting Dashboard - Scheduled Reports";

	/**
	 * Constructs a default scheduled reports controller.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 7, 2012
	 */
	public ScheduledReports() {
		super();
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setPageTitle(DEFAULT_PAGE_TITLE);
	}

	/**
	 * Handles the case where the user elects to create a scheduled report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @return the model and view.
	 * @throws ParseException
	 *             if there is a problem parsing the date.
	 * @since Mar 6, 2012
	 * @version Mar 12, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/CreateScheduledReport.htm", method = RequestMethod.GET)
	public String createScheduledReport(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId) throws ParseException {
		final Report report = getReportingDashboardService().findReportById(reportId);
		final ScheduledReport scheduledReport = getReportingDashboardService().createScheduledReport();
		scheduledReport.setUser(getUser());
		scheduledReport.setReport(report);
		scheduledReport.setDuration(ReportTime.MONTHLY);
		final Date date = ScheduledReport.DATE_FORMATTER.parse(ScheduledReport.DATE_FORMATTER.format(new Date()));
		scheduledReport.setNextExecutionDate(ReportTime.MONTHLY.computeNextInterval(date));
		scheduledReport.setInterval(ReportTime.MONTHLY);
		getReportingDashboardService().saveScheduledReport(scheduledReport);
		getReportingDashboardService().flush();
		model.addAttribute("scheduledReportId", scheduledReport.getId());
		return EditScheduledReport.REDIRECT_EDIT_SCHEDULED_REPORT;
	}

	/**
	 * Deletes the scheduled report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param scheduledReportId
	 *            the scheduled report identifier.
	 * @return the model and view.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/DeleteScheduledReport.htm", method = RequestMethod.GET)
	public String deleteScheduledReport(final HttpServletRequest request, final ModelMap model, final long scheduledReportId) {
		final ScheduledReport scheduledReport = getReportingDashboardService().findScheduledReportById(scheduledReportId);
		getReportingDashboardService().deleteScheduledReport(scheduledReport);
		return ScheduledReports.REDIRECT_SCHEDULED_REPORTS;
	}

	/**
	 * Handles a request to show the reports that user has scheduled to run.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @return the model and view.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/ScheduledReports.htm", method = RequestMethod.GET)
	public String scheduledReports(final HttpServletRequest request, final ModelMap model) {
		final List<ScheduledReport> scheduledReports = getReportingDashboardService().findScheduledReports(getUser());
		model.addAttribute("scheduledReports", scheduledReports);
		return buildModelAndView(request, model);
	}
}
