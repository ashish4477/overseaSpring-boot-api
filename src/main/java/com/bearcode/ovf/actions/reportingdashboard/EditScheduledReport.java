/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bearcode.ovf.model.reportingdashboard.ReportTime;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;

/**
 * Extended {@link BaseReportingDashboardController} to handle editing a scheduled report.
 * 
 * @author IanBrown
 * 
 * @since Mar 6, 2012
 * @version Mar 20, 2012
 */
@Controller
public class EditScheduledReport extends BaseReportingDashboardController {

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/reportingdashboard/EditScheduledReport.jsp";

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
	static final String REDIRECT_EDIT_SCHEDULED_REPORT = "redirect:EditScheduledReport.htm";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "Reporting Dashboard - Edit Scheduled Report";

	/**
	 * Constructs a default edit scheduled report controller.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 7, 2012
	 */
	public EditScheduledReport() {
		super();
		mainTemplate = DEFAULT_TEMPLATE;
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setPageTitle(DEFAULT_PAGE_TITLE);
	}

	/**
	 * Handles a request to edit a scheduled report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param scheduledReportId
	 *            the identifier for the scheduled report.
	 * @return the model and view.
	 * @since Mar 6, 2012
	 * @version Mar 20, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/EditScheduledReport.htm", method = RequestMethod.GET)
	public String editScheduledReport(final HttpServletRequest request, final ModelMap model, @RequestParam(
			value = "scheduledReportId", required = true) final long scheduledReportId) {
		final ScheduledReport scheduledReport = getReportingDashboardService().findScheduledReportById(scheduledReportId);
		model.addAttribute("scheduledReport", scheduledReport);
		model.addAttribute("timeSpans", ReportTime.values());
		return buildModelAndView(request, model);
	}

	/**
	 * Handles the submission of the scheduled report editor for the case where the user wants to save his or her changes.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param scheduledReportId
	 *            the scheduled report identifier.
	 * @return the model and view.
	 * @throws ParseException
	 *             if there is a problem parsing the next execution date.
	 * @since Mar 6, 2012
	 * @version Mar 16, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/EditScheduledReport.htm", method = RequestMethod.POST)
	public String saveScheduledReport(final HttpServletRequest request, final ModelMap model, @RequestParam(
			value = "scheduledReportId", required = true) final long scheduledReportId) throws ParseException {
		final ScheduledReport scheduledReport = getReportingDashboardService().findScheduledReportById(scheduledReportId);

		final String durationName = request.getParameter("duration");
		if (durationName != null) {
			final ReportTime newDuration = ReportTime.valueOf(durationName);
			scheduledReport.setDuration(newDuration);
		}

		// final String nextExecutionDateString = request.getParameter("nextExecutionDate");
		// if (nextExecutionDateString != null) {
		// final Date newNextExecutionDate = ScheduledReport.DATE_FORMATTER.parse(nextExecutionDateString);
		// scheduledReport.setNextExecutionDate(newNextExecutionDate);
		// }

		final String intervalName = request.getParameter("interval");
		if (intervalName != null) {
			final ReportTime newInterval = ReportTime.valueOf(intervalName);
			scheduledReport.setInterval(newInterval);
			scheduledReport.setNextExecutionDate(newInterval.computeNextInterval(new Date()));
		}

		getReportingDashboardService().saveScheduledReport(scheduledReport);
		getReportingDashboardService().flush();

		return ScheduledReports.REDIRECT_SCHEDULED_REPORTS;
	}
}
