/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Sets;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bearcode.ovf.editor.ReportDateEditor;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.UserFieldNames;

/**
 * Extended {@link BaseReportingDashboardController} to generate reports to be displayed.
 * 
 * @author IanBrown
 * 
 * @since Jan 30, 2012
 * @version Apr 2, 2012
 */
@Controller
public class ReportGenerator extends BaseReportingDashboardController {

	/**
	 * Basic implementation of a {@link ReportGeneratorValet}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private final class ReportGeneratorValetImpl implements ReportGeneratorValet {

		/** {@inheritDoc} */
		@Override
		public final HSSFRichTextString acquireRichTextString(final String text) {
			return new HSSFRichTextString(text);
		}

		/** {@inheritDoc} */
		@Override
		public final HSSFWorkbook acquireWorkbook() {
			return new HSSFWorkbook();
		}

	}

	/**
	 * the valet to acquire resources.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private ReportGeneratorValet valet;

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 2, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/reportingdashboard/ViewReport.jsp";

	/**
	 * redirects to the report generator.
	 * 
	 * @author IanBrown
	 * @since Jan 31, 2012
	 * @version Mar 13, 2012
	 */
	static final String REDIRECT_GENERATE_REPORT = "redirect:ViewReport.htm";

	/**
	 * the reporting dashboard template.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 2, 2012
	 */
	static final String DEFAULT_TEMPLATE = "templates/ReportingDashboardTemplate";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "Reporting Dashboard - Report";

	/**
	 * the formatter for dates.
	 * 
	 * @author IanBrown
	 * @since Mar 16, 2012
	 * @version Mar 16, 2012
	 */
	static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Constructs a report generator with default values.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 7, 2012
	 */
	public ReportGenerator() {
		super();
		setValet(new ReportGeneratorValetImpl());
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setPageTitle(DEFAULT_PAGE_TITLE);
	}

	/**
	 * Exports the report to an Excel spreadsheet.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param response
	 *            the response.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @param startDate
	 *            the starting date.
	 * @param endDate
	 *            the ending date.
	 * @throws IOException
	 *             if there is a problem building the response.
	 * @since Mar 6, 2012
	 * @version Apr 2, 2012
	 */
	@RequestMapping(value = "reportingdashboard/ExportExcelReport.htm", method = RequestMethod.GET)
	public void exportExcelReport(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			@RequestParam(value = "reportId", required = true) final long reportId, @RequestParam(value = "startDate",
					required = false) final Date startDate, @RequestParam(value = "endDate", required = false) final Date endDate)
			throws IOException {
		final Report report = getReportingDashboardService().findReportById(reportId);
		final List<String> columnHeaders = getReportingDashboardService().extractColumnHeaders(report);
		final List<List<String>> reportRows = getReportingDashboardService().report(report, startDate, endDate);
		final List<String> totals = getReportingDashboardService().totals(report, reportRows);

		final String reportTitle = report.getTitle();
		String name = reportTitle == null || reportTitle.isEmpty() ? "Sheet 1" : reportTitle.replaceAll("[^\\d\\w]", "_")
				.replaceAll("[&/]", "_");
		if (name.length() > 31) {
			name = name.substring(0, 31);
		}
		response.setHeader("Content-Type", "application/force-download");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + (name.length() > 0 ? name : "Unnamed") + ".xls");
		response.setHeader("Content-Transfer-Encoding", "binary");

		final HSSFWorkbook workbook = getValet().acquireWorkbook();
		final HSSFSheet sheet = workbook.createSheet(name);
		int rownum = 0;
		short cellnum = 0;
		HSSFRow row = createRow(sheet, rownum);
		HSSFCell cell = createCell(row, cellnum);
		cell.setCellValue(valet.acquireRichTextString("Report " + reportTitle));
		++rownum;
		++rownum;
		row = createRow(sheet, rownum);
		cell = createCell(row, cellnum);
		final String flowsString = buildFlowsHeader(report);
		cell.setCellValue(valet.acquireRichTextString(flowsString));
		++rownum;
		if (determineAssignedFace() == null) {
			row = createRow(sheet, rownum);
			cell = createCell(row, cellnum);
			final String facesString = buildFacesHeader(report);
			cell.setCellValue(valet.acquireRichTextString(facesString));
			++rownum;
		}
		row = createRow(sheet, rownum);
		for (final String columnHeader : columnHeaders) {
			cell = createCell(row, cellnum);
			cell.setCellValue(valet.acquireRichTextString(columnHeader));
			++cellnum;
		}
		++rownum;
		cellnum = 0;
		for (final List<String> reportRow : reportRows) {
			row = createRow(sheet, rownum);
			for (final String columnData : reportRow) {
				cell = createCell(row, cellnum);
				cell.setCellValue(valet.acquireRichTextString(columnData));
				++cellnum;
			}
			++rownum;
			cellnum = 0;
		}

		if (totals != null && !totals.isEmpty()) {
			row = createRow(sheet, rownum);
			for (final String columnData : totals) {
				cell = createCell(row, cellnum);
				cell.setCellValue(valet.acquireRichTextString(columnData));
				++cellnum;
			}
			++rownum;
			cellnum = 0;
		}

		final ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
		out.close();
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	public ReportGeneratorValet getValet() {
		return valet;
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	public void setValet(final ReportGeneratorValet valet) {
		this.valet = valet;
	}

	/**
	 * Views the report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @param startDate
	 *            the starting date for the report.
	 * @param endDate
	 *            the ending date for the report.
	 * @return the model and view.
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@RequestMapping(value = "reportingdashboard/ViewReport.htm", method = RequestMethod.GET)
	public String viewReport(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId, @RequestParam(value = "startDate", required = false) final Date startDate,
			@RequestParam(value = "endDate", required = false) final Date endDate) {
        /* Fix start date. End date should not be before start date.
         This case demands special behavior. It seems a bug in JS script which forms
          requested dates. So this is request for all dates before end date
        */
        Date localStartDate = startDate;
        if ( endDate != null && endDate.before( localStartDate ) ) {
            localStartDate = null;
        }
        /* Correct endDate. JS works on such way what
        if only startDate is provided this is request for only given day
        */
        Date localEndDate = endDate == null ? localStartDate : endDate;

		final Report report = getReportingDashboardService().findReportById(reportId);
		final List<String> columnHeaders = getReportingDashboardService().extractColumnHeaders(report);
		final List<List<String>> reportRows = getReportingDashboardService().report(report, localStartDate, localEndDate);
		final List<String> totals = getReportingDashboardService().totals(report, reportRows);

		model.addAttribute("report", report);
		final List<String> headerRows = new LinkedList<String>();
		final String flowsString = buildFlowsHeader(report);
		headerRows.add(flowsString);
		if (determineAssignedFace() == null) {
			final String facesString = buildFacesHeader(report);
			headerRows.add(facesString);
		}
		model.addAttribute("headerRows", headerRows);
		model.addAttribute("columnHeaders", columnHeaders);
		model.addAttribute("reportRows", reportRows);
		if (localStartDate != null) {
			model.addAttribute("startDate", DATE_FORMATTER.format(localStartDate));
		}
		if (endDate != null) {
			model.addAttribute("endDate", DATE_FORMATTER.format(endDate));
		}
		if (totals != null) {
			model.addAttribute("totals", totals);
		}
		return buildModelAndView(request, model);
	}

	@RequestMapping(value = {"reportingdashboard/ViewReport.htm", "reportingdashboard/ExportExcelReport.htm"}, method = {RequestMethod.HEAD, RequestMethod.POST} )
	public ResponseEntity<String> answerToHeadRequest() {
		return sendMethodNotAllowed();
	}

	/**
	 * Registers data editors with the binder.
	 * 
	 * @author IanBrown
	 * @param binder
	 *            the data binder.
	 * @since Aug 31, 2012
	 * @version Aug 31, 2012
	 */
	@InitBinder
	protected final void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new ReportDateEditor());
	}

	/**
	 * Builds the header row string for the faces.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @return the header row string.
	 * @since Apr 2, 2012
	 * @version Apr 2, 2012
	 */
	private String buildFacesHeader(final Report report) {
		if (report.isApplyFaces()) {
			final Set<String> faces = report.getFaces();
			final StringBuilder sb = new StringBuilder("For hosted sites:");
			String prefix = " ";
			int faceIdx = 0;
			for (final String face : faces) {
				sb.append(prefix).append(face);
				++faceIdx;
				if (faceIdx == faces.size() - 1) {
					prefix = " and ";
				} else {
					prefix = ", ";
				}
			}
			return sb.toString();
		}

		return "For all hosted sites";
	}

	/**
	 * Builds the flow types header row for the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @return the flow types header row.
	 * @since Apr 2, 2012
	 * @version Jun 13, 2012
	 */
	private String buildFlowsHeader(final Report report) {
		final FlowType[] flowTypes = report.isApplyFlow() ? new FlowType[] { report.getFlowType() } : determineFlowTypes();
		final StringBuilder sb = new StringBuilder("For");
		String prefix = " ";
		int flowIdx = 0;
		for (final FlowType flowType : flowTypes) {
			sb.append(prefix).append(UserFieldNames.FLOW_TYPE.convertDatabaseToDisplay(flowType));
			++flowIdx;
			if (flowIdx == flowTypes.length - 1) {
				prefix = " and ";
			} else {
				prefix = ", ";
			}
		}

		return sb.toString();
	}

	/**
	 * Creates a cell for the specified row.
	 * 
	 * @author IanBrown
	 * @param row
	 *            the row.
	 * @param cellnum
	 *            the cell number.
	 * @return the cell.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private HSSFCell createCell(final HSSFRow row, final short cellnum) {
		return row.createCell(cellnum);
	}

	/**
	 * Creates a row for the specified sheet.
	 * 
	 * @author IanBrown
	 * @param sheet
	 *            the sheet.
	 * @param rownum
	 *            the row number.
	 * @return the row.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private HSSFRow createRow(final HSSFSheet sheet, final int rownum) {
		return sheet.createRow(rownum);
	}
}
