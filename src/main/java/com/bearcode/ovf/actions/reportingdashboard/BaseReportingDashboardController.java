/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.service.ReportingDashboardService;

/**
 * Abstract extended {@link BaseController} used by the controllers of the reporting dashboard.
 * 
 * @author IanBrown
 * 
 * @since Mar 7, 2012
 * @version Jun 13, 2012
 */
public abstract class BaseReportingDashboardController extends BaseController {

	/**
	 * the reporting dashboard template.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 1, 2012
	 */
	protected static final String DEFAULT_TEMPLATE = "templates/ReportingDashboardTemplate";

	/**
	 * the default section name.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	protected static final String DEFAULT_SECTION_NAME = "admin";

	/**
	 * the default section CSS.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	protected static final String DEFAULT_SECTION_CSS = "/css/reportingdashboard.css";

	/**
	 * the reporting dashboard service to use.
	 * 
	 * @author IanBrown
	 * @since Jan 3, 2012
	 * @version Jan 3, 2012
	 */
	@Autowired
	private ReportingDashboardService reportingDashboardService;

	/**
	 * Constructs a default base reporting dashboard controller.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	public BaseReportingDashboardController() {
		super();
		mainTemplate = DEFAULT_TEMPLATE;
		setSectionCss(DEFAULT_SECTION_CSS);
		setSectionName(DEFAULT_SECTION_NAME);
	}

	/**
	 * Adds the custom reports for the user. If there is no user, then no reports are returned.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the custom reports.
	 * @since Mar 1, 2012
	 * @version Mar 28, 2012
	 */
	@ModelAttribute("customReports")
	public List<Report> addCustomReports(final HttpServletRequest request) {
		if (getUser() == null) {
			return new ArrayList<Report>();
		}

		final FaceConfig currentFace = determineCurrentFace(request);
		return getReportingDashboardService().findCustomReports(getUser(), currentFace);
	}

	/**
	 * Adds the standard reports to the model. All standard reports are added as long as there is a user.
	 * 
	 * @author IanBrown
	 * @return the standard reports.
	 * @since Mar 1, 2012
	 * @version Mar 1, 2012
	 */
	@ModelAttribute("standardReports")
	public Map<String, Report> addStandardReports() {
		if (getUser() == null) {
			return new HashMap<String, Report>();
		}

		return getReportingDashboardService().findStandardReports();
	}

	/**
	 * Gets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @return the reporting dashboard service.
	 * @since Jan 3, 2012
	 * @version Jan 3, 2012
	 */
	public ReportingDashboardService getReportingDashboardService() {
		return reportingDashboardService;
	}

	/**
	 * Sets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @param reportingDashboardService
	 *            the reporting dashboard service to set.
	 * @since Jan 3, 2012
	 * @version Jan 3, 2012
	 */
	public void setReportingDashboardService(final ReportingDashboardService reportingDashboardService) {
		this.reportingDashboardService = reportingDashboardService;
	}

	/**
	 * Determines the face assigned to the user.
	 * 
	 * @author IanBrown
	 * @return the assigned face.
	 * @since Mar 29, 2012
	 * @version Mar 30, 2012
	 */
	protected final FaceConfig determineAssignedFace() {
		final FaceConfig assignedFace;
		final OverseasUser user = getUser();
		if ((user == null) || user.isInRole(UserRole.USER_ROLE_ADMIN)) {
			assignedFace = null;
		} else {
			assignedFace = user.getAssignedFace();
		}
		return assignedFace;
	}

	/**
	 * Determines the current face from the request.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the current face.
	 * @since Mar 29, 2012
	 * @version Apr 2, 2012
	 */
	protected final FaceConfig determineCurrentFace(final HttpServletRequest request) {
		final String urlPath = request.getServerName() + request.getContextPath();
		final FaceConfig currentFace = getFacesService().findConfig(urlPath);
		return currentFace;
	}

	/**
	 * Determines the types of flows available.
	 * 
	 * @author IanBrown
	 * @return the flow types.
	 * @since Apr 2, 2012
	 * @version Jun 13, 2012
	 */
	protected final FlowType[] determineFlowTypes() {
		final FlowType[] flows;

		if (getUser() == null) {
			flows = new FlowType[0];
		} else if (getUser().isInRole(UserRole.USER_ROLE_ADMIN)) {
			final List<FlowType> flowEntries = new ArrayList<>(Arrays.asList(FlowType.values()));
			flows = flowEntries.toArray(new FlowType[flowEntries.size()]);
		} else {
			final FaceConfig assignedFace = determineAssignedFace();
			if (assignedFace == null) {
				final List<FlowType> flowEntries = new ArrayList<>(Arrays.asList(FlowType.values()));
				flows = flowEntries.toArray(new FlowType[flowEntries.size()]);
			} else if (assignedFace.getRelativePrefix().contains("usvote")) {
				flows = new FlowType[] { FlowType.DOMESTIC_REGISTRATION, FlowType.DOMESTIC_ABSENTEE };
			} else {
				flows = new FlowType[] { FlowType.RAVA, FlowType.FWAB };
			}
		}

		return flows;
	}

}