/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bearcode.ovf.model.reportingdashboard.Report;

/**
 * Extended {@link BaseReportingDashboardController} to support the reporting dashboard. The dashboard presents a list of the
 * available reports, both the standard set and the user defined set.
 * 
 * @author IanBrown
 * 
 * @since Jan 3, 2012
 * @version Mar 27, 2012
 */
@Controller
public class ReportingDashboard extends BaseReportingDashboardController {

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/reportingdashboard/ReportingDashboard.jsp";

	/**
	 * redirects to the reporting dashboard.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	static final String REDIRECT_REPORTING_DASHBOARD = "redirect:ReportingDashboard.htm";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "Reporting Dashboard";

	/**
	 * Constructs a reporting dashboard, setting the default values.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Mar 7, 2012
	 */
	public ReportingDashboard() {
		super();
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setPageTitle(DEFAULT_PAGE_TITLE);
	}

	/**
	 * Shows the home page of the reporting dashboard.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @return the model and view.
	 * @since Mar 1, 2012
	 * @version May 16, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/ReportingDashboard.htm", method = RequestMethod.GET)
	public String reportingDashboardHome(final HttpServletRequest request, final ModelMap model) {
		final Map<String, Report> standardReports = getReportingDashboardService().findStandardReports();
		final Map<String, List<String>> columnHeaders = new HashMap<String, List<String>>();
		final Map<String, List<List<String>>> reportRows = new HashMap<String, List<List<String>>>();
		final Map<String, List<String>> totals = new HashMap<String, List<String>>();
		performReport("Usage_by_Request_Type", standardReports.get("Usage_by_Request_Type"), null, null, columnHeaders, reportRows, totals);
		performReport("Completed_by_Request_Type", standardReports.get("Completed_by_Request_Type"), null, null, columnHeaders, reportRows, totals);
		model.addAttribute("columnHeaders", columnHeaders);
		model.addAttribute("reportRows", reportRows);
		model.addAttribute("totals", totals);
		final String modelAndView = buildModelAndView(request, model);
		return modelAndView;
	}

	/**
	 * Performs the report (if any) for the specified period.
	 * 
	 * @author IanBrown
	 * @param reportName
	 *            the name of the report.
	 * @param report
	 *            the report.
	 * @param startDate
	 *            the starting date.
	 * @param endDate
	 *            the ending date.
	 * @param columnHeaders
	 *            the column headers.
	 * @param reportRows
	 *            the report rows.
	 * @param totals
	 *            the totals.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	private void performReport(final String reportName, final Report report, final Date startDate, final Date endDate,
			final Map<String, List<String>> columnHeaders, final Map<String, List<List<String>>> reportRows,
			final Map<String, List<String>> totals) {
		if (report != null) {
			columnHeaders.put(reportName, getReportingDashboardService().extractColumnHeaders(report));
			reportRows.put(reportName, getReportingDashboardService().report(report, startDate, endDate));
			totals.put(reportName, getReportingDashboardService().totals(report, reportRows.get(reportName)));
		}
	}
}
