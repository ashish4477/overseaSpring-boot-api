/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Extended {@link BaseReportingDashboardController} to support editing of a report.
 * 
 * @author IanBrown
 * 
 * @since Jan 6, 2012
 * @version Apr 5, 2012
 */
@Controller
public class EditReport extends BaseReportingDashboardController {

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	@Autowired
	private QuestionnaireService questionnaireService;

	/**
	 * the user service.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	@Autowired
	private OverseasUserService overseasUserService;

	/**
	 * the content block for an error.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	static final String ERROR_CONTENT_BLOCK = "/WEB-INF/pages/blocks/reportingdashboard/EditReportError.jsp";

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/reportingdashboard/EditReport.jsp";

	/**
	 * the error message used to say that no identifier was provided.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	static final String NO_REPORT_IDENTIFIER_ERROR_MESSAGE = "The identifier of a report to edit must be specified";

	/**
	 * the error format string used to say that a bad identifier was provided.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	static final String BAD_REPORT_IDENTIFIER_ERROR_FORMAT = "Cannot find report to edit for identifier %1$d";

	/**
	 * the error message used to say that a user must be logged in.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	static final String NO_USER_ERROR_MESSAGE = "You must be logged in to try to edit a custom report";

	/**
	 * the error message used to say that the user is not the owner of the report.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	static final String NOT_REPORT_OWNER_ERROR_MESSAGE = "You must be the owner of the report to be able to edit it";

	/**
	 * the name of a new column.
	 * 
	 * @author IanBrown
	 * @since Jan 10, 2012
	 * @version Jan 10, 2012
	 */
	static final String NEW_COLUMN_NAME = "New Column";

	/**
	 * redirect to the edit report.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	static final String REDIRECT_EDIT_REPORT = "redirect:EditReport.htm";

	/**
	 * redirect to the report settings.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	static final String REDIRECT_REPORT_SETTINGS = "redirect:ReportSettings.htm";

	/**
	 * the reporting dashboard template.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 1, 2012
	 */
	static final String DEFAULT_TEMPLATE = "templates/ReportingDashboardTemplate";

	/**
	 * the report settings content block.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	static final String REPORT_SETTINGS_CONTENT_BLOCK = "/WEB-INF/pages/blocks/reportingdashboard/ReportSettings.jsp";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "Reporting Dashboard - Edit Report";

	/**
	 * Constructs an edit report controller with default values set.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Mar 7, 2012
	 */
	public EditReport() {
		super();
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setPageTitle(DEFAULT_PAGE_TITLE);
	}

	/**
	 * Adds the flows for the current user to the model.
	 * 
	 * @author IanBrown
	 * @return the flows.
	 * @since Mar 29, 2012
	 * @version Apr 2, 2012
	 */
	@ModelAttribute("flows")
	public FlowType[] addFlows() {
		return determineFlowTypes();
	}

	/**
	 * Handles the submission of the report editor for the case where the user wants to add a metric.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @param metric
	 *            the name of the metric.
	 * @param answers
	 *            the answers entered by the user.
	 * @return the model and view.
	 * @since Mar 13, 2012
	 * @version Apr 5, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/EditReport.htm", method = RequestMethod.POST, params = "addMetric")
	public String addMetric(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId, @RequestParam(value = "metric", required = true) final String metric,
			@RequestParam(value = "answer", required = false) final String[] answers) {
		final Report report = getReportingDashboardService().findReportById(reportId);
		final long[] columnIds = MapUtils.getLongs(request.getParameterMap(), "columnId", null);
		if (columnIds != null) {
			getReportingDashboardService().updateReportColumns(report, columnIds);
		}
		getReportingDashboardService().addMetricColumnToReport(report, metric, answers);
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();

		model.addAttribute("reportId", reportId);
		return REDIRECT_EDIT_REPORT;
	}

	/**
	 * Handles the submission of the report editor for the case where the user wants to add a question column.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @param columnName
	 *            the name of the column.
	 * @param page
	 *            the wizard page.
	 * @param group
	 *            the group on the page.
	 * @param question
	 *            the question in the group.
	 * @param answers
	 *            the answers entered by the user.
	 * @return the model and view.
	 * @since Mar 13, 2012
	 * @version Apr 3, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/EditReport.htm", method = RequestMethod.POST, params = "addQuestion")
	public String addQuestion(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId, final String columnName,
			@RequestParam(value = "wizardPage", required = true) final String page, @RequestParam(value = "wizardGroup",
					required = false) final String group,
			@RequestParam(value = "wizardQuestion", required = true) final String question, @RequestParam(value = "answer",
					required = false) final String[] answers) {
		final Report report = getReportingDashboardService().findReportById(reportId);
		final WizardContext wizardContext = buildWizardContext(request, report);
		final List<QuestionnairePage> wizardPages = getQuestionnaireService().findQuestionnairePages();
		final FlowType[] flows = (FlowType[]) model.get("flows");
		getReportingDashboardService().addQuestionColumnToReport(report, wizardContext, flows, wizardPages, columnName, page,
				group, question, answers);
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();

		model.addAttribute("reportId", reportId);
		return REDIRECT_EDIT_REPORT;
	}

	/**
	 * Handles the submission of the report editor for the case where the user wants to add a user detail column.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @param columnName
	 *            the name of the column.
	 * @param userDetail
	 *            the name of the user detail.
	 * @param answers
	 *            the answers entered by the user.
	 * @return the model and view.
	 * @since Mar 13, 2012
	 * @version Apr 3, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/EditReport.htm", method = RequestMethod.POST, params = "addUserDetail")
	public String addUserDetail(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId, final String columnName,
			@RequestParam(value = "userDetail", required = true) final String userDetail, @RequestParam(value = "answer",
					required = false) final String[] answers) {
		final Report report = getReportingDashboardService().findReportById(reportId);
		getReportingDashboardService().addUserDetailColumnToReport(report, columnName, userDetail, answers);
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();

		model.addAttribute("reportId", reportId);
		return REDIRECT_EDIT_REPORT;
	}

	/**
	 * Clones an existing report and allows the user to edit the copy.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @return the copied report.
	 * @since Jan 11, 2012
	 * @version Apr 4, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/CloneReport.htm", method = RequestMethod.GET)
	public String cloneReport(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final Long reportId) {
		final Report report = getReportingDashboardService().findReportById(reportId);
		final Report copiedReport = getReportingDashboardService().copyReport(report, getUser());
		getReportingDashboardService().saveReport(copiedReport);
		getReportingDashboardService().flush();

		model.addAttribute("reportId", copiedReport.getId());
		return REDIRECT_REPORT_SETTINGS;
	}

	/**
	 * Creates a new, blank report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @return the model and view.
	 * @since Mar 5, 2012
	 * @version Apr 5, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/CreateReport.htm", method = RequestMethod.GET)
	public String createReport(final HttpServletRequest request, final ModelMap model) {
		final Report report = getReportingDashboardService().createCustomReport(getUser(), determineAssignedFace());
		report.setTitle("Report");

		return editReportSettings(request, model, report);
	}

	/**
	 * Handles a request to edit the settings for a report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @return the model and view.
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/DeleteReport.htm", method = RequestMethod.GET)
	public String deleteReport(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId) {
		final Report report = getReportingDashboardService().findReportById(reportId);

		getReportingDashboardService().deleteReport(report);

		return ReportingDashboard.REDIRECT_REPORTING_DASHBOARD;
	}

	/**
	 * Handles the start of editing a report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @return the model and view.
	 * @since Mar 5, 2012
	 * @version Mar 16, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/EditReport.htm", method = RequestMethod.GET)
	public String editReport(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId) {
		final Report report = getReportingDashboardService().findReportById(reportId);
		return editReport(request, model, report);
	}

	/**
	 * Handles a request to edit the settings of a report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @return the model and view.
	 * @since Mar 5, 2012
	 * @version Mar 15, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/ReportSettings.htm", method = RequestMethod.GET)
	public String editReportSettings(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final long reportId) {
		final Report report = getReportingDashboardService().findReportById(reportId);
		return editReportSettings(request, model, report);
	}

	/**
	 * Gets the user service.
	 * 
	 * @author IanBrown
	 * @return the user service.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	public OverseasUserService getOverseasUserService() {
		return overseasUserService;
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	public QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Handles the submission of the report editor for the case where the user wants to save his or her changes.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @return the model and view.
	 * @since Jan 11, 2012
	 * @version Apr 4, 2012
	 */
	@RequestMapping(value = "/reportingdashboard/EditReport.htm", method = RequestMethod.POST, params = "saveReport")
	public String saveReport(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = true) final Long reportId) {
		final Report report = (reportId == null) ? getReportingDashboardService().createCustomReport(getUser(),
				determineAssignedFace()) : getReportingDashboardService().findReportById(reportId);
		final long[] columnIds = MapUtils.getLongs(request.getParameterMap(), "columnId", null);
		if (columnIds != null) {
			getReportingDashboardService().updateReportColumns(report, columnIds);
			getReportingDashboardService().saveReport(report);
			getReportingDashboardService().flush();
		}

		model.addAttribute("reportId", report.getId());
		return ReportGenerator.REDIRECT_GENERATE_REPORT;
	}

	/**
	 * Handles the submission of the report editor for the case where the user wants to save his or her changes to the report
	 * settings.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param reportId
	 *            the report identifier.
	 * @return the model and view.
	 * @since Mar 13, 2012
	 * @version Apr 4, 2012
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/reportingdashboard/ReportSettings.htm", method = RequestMethod.POST, params = "saveReportSettings")
	public String saveReportSettings(final HttpServletRequest request, final ModelMap model, @RequestParam(value = "reportId",
			required = false) final Long reportId) {
		final FaceConfig assignedFace = determineAssignedFace();
		final List<String> allFaces = buildFaces();
		final Report report = (reportId == null) ? getReportingDashboardService().createCustomReport(getUser(), assignedFace)
				: getReportingDashboardService().findReportById(reportId);
		final Map parameterMap = request.getParameterMap();
		final String title = MapUtils.getString(parameterMap, "reportTitle", null);
		final String description = MapUtils.getString(parameterMap, "reportDescription", null);
		final String standardString = MapUtils.getString(parameterMap, "standardReport", null);
		final String flowTypeValue = MapUtils.getString(parameterMap, "flowType", null);
        final boolean standard = MapUtils.getBoolean(parameterMap, "standard", false);
		final String[] faceNames = (String[]) parameterMap.get("face");
		final long ownerId = MapUtils.getLong(parameterMap, "ownerId", 0l);
		final OverseasUser owner = (ownerId == 0l) ? null : getOverseasUserService().findUserById(ownerId);
		if (getReportingDashboardService().updateReportSettings(report, title, description, standardString, flowTypeValue,
				faceNames, assignedFace, allFaces, owner)) {
            if ( standard ) {
                report.setStandard( standard );
                report.setOwner(null);
            }
            else if ( report.isStandard() ) {
                report.setStandard( standard );
                if ( owner == null ) {
                    OverseasUser user = (OverseasUser)model.get("user");
                    report.setOwner( user );
                }
            }
            getReportingDashboardService().saveReport(report);
			getReportingDashboardService().flush();
		}

		model.addAttribute("reportId", report.getId());
		return (reportId == null) ? REDIRECT_EDIT_REPORT : ReportGenerator.REDIRECT_GENERATE_REPORT;
	}

	/**
	 * Sets the user service.
	 * 
	 * @author IanBrown
	 * @param overseasUserService
	 *            the user service to set.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	public void setOverseasUserService(final OverseasUserService overseasUserService) {
		this.overseasUserService = overseasUserService;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	public void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	/**
	 * Adds the questionnaire pages information to the model.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param wizardContext
	 *            the wizard context representing the user.
	 * @param model
	 *            the model.
	 * @since Mar 5, 2012
	 * @version Apr 3, 2012
	 */
	private void addPagesToModel(final Report report, final WizardContext wizardContext, final ModelMap model) {
		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = determineReportableFields(report,
				wizardContext, model);
		if (reportableFields != null) {
			final Map<String, Map<String, Map<String, List<String>>>> pages = getReportingDashboardService()
					.convertReportableFieldsToPages(reportableFields);
			model.addAttribute("pages", pages);
		}
	}

	/**
	 * Adds the possible owners of the report to the model. Admin users (only) can set the owner of the report to be any reporting
	 * dashboard user on the current face (or to themselves, the default).
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param report
	 *            the report.
	 * @param model
	 *            the model.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	private void addPossibleOwners(final HttpServletRequest request, final Report report, final ModelMap model) {
		if (getUser().isInRole(UserRole.USER_ROLE_ADMIN)) {
			final Collection<UserRole> reportingDashboardRoles = getOverseasUserService().findRolesByName(
					UserRole.USER_ROLE_REPORTING_DASHBOARD);
			final List<OverseasUser> possibleOwners = new LinkedList<OverseasUser>();
			if ((reportingDashboardRoles != null) && !reportingDashboardRoles.isEmpty()) {
				final FaceConfig currentFace = determineCurrentFace(request);
				if (currentFace != null) {
					final String urlPath = currentFace.getUrlPath();
					final UserRole reportingDashboardRole = reportingDashboardRoles.iterator().next();
					final UserFilterForm userFilterForm = new UserFilterForm();
					userFilterForm.setRoleId(reportingDashboardRole.getId());
					final Collection<OverseasUser> reportingDashboardUsers = getOverseasUserService().findUsers(userFilterForm);
					for (final OverseasUser reportingDashboardUser : reportingDashboardUsers) {
						final FaceConfig assignedFace = reportingDashboardUser.getAssignedFace();
						if (assignedFace == null) {
							continue;
						}
						if (assignedFace.getUrlPath().equals(urlPath)) {
							possibleOwners.add(reportingDashboardUser);
						}
					}
				}
			}
			possibleOwners.add(getUser());
			final OverseasUser reportOwner = report.getOwner();
            boolean foundOwner = false;
            if ( reportOwner != null ) {
                final long reportOwnerId = reportOwner.getId();
                for (final OverseasUser possibleOwner : possibleOwners) {
                    if (possibleOwner.getId() == reportOwnerId) {
                        foundOwner = true;
                        break;
                    }
                }
            }
            if (!foundOwner) {
				possibleOwners.add(reportOwner);
			}
			model.addAttribute("possibleOwners", possibleOwners);
		}
	}

	/**
	 * Adds the user fields names as an array of maps to the model.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param model
	 *            the model.
	 * @since Feb 7, 2012
	 * @version Apr 3, 2012
	 */
	private void addUserFieldNamesToModel(final Report report, final ModelMap model) {
		final FaceConfig assignedFace = determineAssignedFace();
		final List<Map<String, Object>> userFieldOptions = getReportingDashboardService().buildUserFields(report, assignedFace);
		model.addAttribute("userFieldNames", userFieldOptions);
	}

	/**
	 * Builds the faces list.
	 * 
	 * @author IanBrown
	 * @return the faces list.
	 * @since Mar 15, 2012
	 * @version Mar 15, 2012
	 */
	private List<String> buildFaces() {
		final Collection<FaceConfig> faceConf = getFacesService().findAllConfigs();
		final Set<String> facesUnsorted = new HashSet<String>();
		for (final FaceConfig config : faceConf) {
			final String urlPath = config.getUrlPath();
			final String upperCaseUrlPath = urlPath.toUpperCase();
			if (!upperCaseUrlPath.contains("BASIC") && !upperCaseUrlPath.contains("OBAMA") && !upperCaseUrlPath.contains("MCCAIN")) {
				facesUnsorted.add(urlPath);
			}
		}
		final List<String> faces = new ArrayList<String>(facesUnsorted);
		Collections.sort(faces);
		return faces;
	}

	/**
	 * Builds the wizard context for the report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param report
	 *            the report.
	 * @return the wizard context.
	 * @since Mar 29, 2012
	 * @version Mar 30, 2012
	 */
	private WizardContext buildWizardContext(final HttpServletRequest request, final Report report) {
		final FaceConfig currentFace = determineCurrentFace(request);
		final WizardResults wizardResults = new WizardResults(report.getFlowType());
		wizardResults.setUser(getUser());
		wizardResults.populateFromFaceConfig(currentFace);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		wizardContext.setCurrentFace(determineAssignedFace());
		return wizardContext;
	}

	/**
	 * Determines the reportable fields for the context and model.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param wizardContext
	 *            the wizard context.
	 * @param model
	 *            the model.
	 * @return the reportable fields.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	private Map<String, Map<String, Map<String, Collection<QuestionField>>>> determineReportableFields(final Report report,
			final WizardContext wizardContext, final ModelMap model) {
		final List<QuestionnairePage> wizardPages = getQuestionnaireService().findQuestionnairePages();
		final FlowType[] flows = (FlowType[]) model.get("flows");
		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = getReportingDashboardService()
				.pagesToReportableFields(report, wizardContext, flows, wizardPages);
		return reportableFields;
	}

	/**
	 * Handles the editing of the input report, which may not have been saved in the database.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param report
	 *            the report.
	 * @return the model and view.
	 * @since Mar 16, 2012
	 * @version Mar 29, 2012
	 */
	private String editReport(final HttpServletRequest request, final ModelMap model, final Report report) {
		model.addAttribute("report", report);

		final WizardContext wizardContext = buildWizardContext(request, report);
		addPagesToModel(report, wizardContext, model);
		addUserFieldNamesToModel(report, model);

		return buildModelAndView(request, model);
	}

	/**
	 * Handles a request to edit the settings of a report.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param report
	 *            the report.
	 * @return the model and view.
	 * @since Mar 16, 2012
	 * @version Mar 29, 2012
	 */
	private String editReportSettings(final HttpServletRequest request, final ModelMap model, final Report report) {
		setContentBlock(REPORT_SETTINGS_CONTENT_BLOCK);
		try {
			model.addAttribute("report", report);
			final List<String> faces = buildFaces();
			model.addAttribute("faces", faces);

			addPossibleOwners(request, report, model);

			final List<ReportColumn> columns = report.getColumns();
			if ((columns != null) && !columns.isEmpty()) {
				model.remove("flows");
			}

			return buildModelAndView(request, model);
		} finally {
			setContentBlock(DEFAULT_CONTENT_BLOCK);
		}
	}
}
