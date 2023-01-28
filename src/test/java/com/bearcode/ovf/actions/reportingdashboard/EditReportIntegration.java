/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;
import com.bearcode.ovf.model.reportingdashboard.ReportField;
import com.bearcode.ovf.model.reportingdashboard.UserFieldNames;
import com.bearcode.ovf.service.QuestionnaireService;

/**
 * Extended {@link BaseReportingDashboardControllerExam} integration test for {@link EditReport}.
 * 
 * @author IanBrown
 * 
 * @since Jan 6, 2012
 * @version Apr 3, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "ReportingDashboardIntegration-context.xml" })
public final class EditReportIntegration extends BaseReportingDashboardControllerExam<EditReport> {

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
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addMetric(javax.servlet.http.HttpServletRequest, ModelMap, long, String, String[])}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 13, 2012
	 * @version Apr 3, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testAddMetric() throws Exception {
		final String metricName = "Age Group";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/EditReport.htm");
		request.setMethod("POST");
		request.setParameter("addMetric", "addMetric");
		final Report report = getReportingDashboardService().findReportById(1l);
		final Long reportId = report.getId();
		request.addParameter("reportId", reportId.toString());
		final List<ReportColumn> columns = report.getColumns();
		final int initialColumns = columns.size();
		request.addParameter("metric", metricName);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("A redirection to the report editor is returned", EditReport.REDIRECT_EDIT_REPORT,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertEquals("The report identifier is in the model", reportId, actualModel.get("reportId"));
		assertEquals("A column has been added to the report", initialColumns + 1, columns.size());
		final ReportColumn column = columns.get(initialColumns);
		assertEquals("The metric was added as a column", metricName, column.getName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addUserDetails(javax.servlet.http.HttpServletRequest, ModelMap, long, String, String[])}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 13, 2012
	 * @version Mar 13, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testAddUserDetails() throws Exception {
		final String columnName = "Voting Region";
		final UserFieldNames userDetailName = UserFieldNames.VOTING_REGION_NAME;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/EditReport.htm");
		request.setMethod("POST");
		request.setParameter("addUserDetail", "addUserDetail");
		final Report report = getReportingDashboardService().findReportById(1l);
		final Long reportId = report.getId();
		request.addParameter("reportId", reportId.toString());
		final List<ReportColumn> columns = report.getColumns();
		final int initialColumns = columns.size();
		request.addParameter("columnName", columnName);
		request.addParameter("userDetail", userDetailName.getUiName());

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("A redirection to the report editor is returned", EditReport.REDIRECT_EDIT_REPORT,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertEquals("The report identifier is in the model", reportId, actualModel.get("reportId"));
		assertEquals("A column has been added to the report", initialColumns + 1, columns.size());
		final ReportColumn column = columns.get(initialColumns);
		assertEquals("The user detail was added as a column", columnName, column.getName());
		final Collection<ReportField> fields = column.getFields();
		assertEquals("There is one field for the column", 1, fields.size());
		final ReportField field = fields.iterator().next();
		assertEquals("The user detail is set as the field name", userDetailName.getSqlName(), field.getUserFieldName());
	}

	/**
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#cloneReport(HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 11, 2012
	 * @version Mar 13, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	public final void testCloneReport() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/CloneReport.htm");
		request.setMethod("GET");
		final Report report = getReportingDashboardService().findReportById(1l);
		final Long reportId = report.getId();
		request.addParameter("reportId", reportId.toString());

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("A redirection to the report settings editor is returned", EditReport.REDIRECT_REPORT_SETTINGS,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final long actualReportId = (Long) actualModel.get("reportId");
		assertTrue("There is a new report identifier", (actualReportId != 0l) && (actualReportId != reportId));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#createReport(javax.servlet.http.HttpServletRequest, ModelMap)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 5, 2012
	 * @version Mar 28, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testCreateReport() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/CreateReport.htm");
		request.setMethod("GET");
		final OverseasUser user = new OverseasUser();
		user.setId(2l);
		final UserRole role = new UserRole();
		role.setRoleName(UserRole.USER_ROLE_ADMIN);
		final Collection<UserRole> roles = Arrays.asList(role);
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is used as the view",
				ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final Report actualReport = (Report) actualModel.get("report");
		assertNotNull("A report is added to the model", actualReport);
		assertFalse("The report is custom report", actualReport.isStandard());
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#deleteReport(javax.servlet.http.HttpServletRequest, ModelMap, Long)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 11, 2012
	 * @version Mar 7, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testDeleteReport() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/DeleteReport.htm");
		request.setMethod("GET");
		final Report report = getReportingDashboardService().findReportById(1l);
		final Long reportId = report.getId();
		request.addParameter("reportId", reportId.toString());

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("A redirection to the reporting dashboard is returned", ReportingDashboard.REDIRECT_REPORTING_DASHBOARD,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final Report actualReport = getReportingDashboardService().findReportById(reportId);
		assertNull("The report has been deleted", actualReport);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#editReport(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 5, 2012
	 * @version Mar 30, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testEditReport() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/EditReport.htm");
		request.setMethod("GET");
		request.setParameter("reportId", "2");
		final OverseasUser user = new OverseasUser();
		user.setId(2l);
        user.setUsername("username");
		final UserRole role = new UserRole();
		role.setRoleName(UserRole.USER_ROLE_ADMIN);
		final Collection<UserRole> roles = Arrays.asList(role);
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is used as the view",
				ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final Report report = (Report) actualModel.get("report");
		assertNotNull("A report is added to the model", report);
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#editReportSettings(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 5, 2012
	 * @version Mar 28, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testEditReportSettings() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ReportSettings.htm");
		request.setMethod("GET");
		request.setParameter("reportId", "2");
		final OverseasUser user = new OverseasUser();
		user.setId(1l);
		final Collection<UserRole> roles = new ArrayList<UserRole>();
		user.setRoles(roles);
		setUpAuthentication(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The reporting dashboard template is used as the view",
				ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final Report report = (Report) actualModel.get("report");
		assertNotNull("A report is added to the model", report);
		assertFalse("The report is custom report", report.isStandard());
		assertStandardReports(actualModel, true);
		assertCustomReports(user, null, actualModel, true);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#saveReport(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * for the case where a column is deleted.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 11, 2012
	 * @version Mar 13, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testSaveReport_deleteColumn() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/EditReport.htm");
		request.setMethod("POST");
		request.addParameter("saveReport", "saveReport");
		request.addParameter("reportId", Long.toString(1l));
		final Report report = getReportingDashboardService().findReportById(1l);
		final List<ReportColumn> columns = report.getColumns();
		final List<Long> columnIds = new LinkedList<Long>();
		for (final ReportColumn column : columns) {
			columnIds.add(column.getId());
		}
		for (int columnIdx = columnIds.size() - 1; columnIdx > 0; --columnIdx) {
			request.addParameter("columnId", Long.toString(columnIds.get(columnIdx)));
		}

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The view is redirected to the report view", ReportGenerator.REDIRECT_GENERATE_REPORT,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		final Report actualReport = getReportingDashboardService().findReportById(1l);
		final List<ReportColumn> actualColumns = actualReport.getColumns();
		int columnIdx = columnIds.size() - 1;
		for (final ReportColumn actualColumn : actualColumns) {
			assertEquals(actualColumn + " is in the correct place", columnIds.get(columnIdx), actualColumn.getId());
			--columnIdx;
		}
		assertEquals("A column has been removed", 0, columnIdx);
		assertNull("The column has been deleted", getReportingDashboardService().findColumnById(columnIds.get(columnIdx)));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#saveReport(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * for the case where the columns are reordered.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 11, 2012
	 * @version Mar 13, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testSaveReport_reorderColumns() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/EditReport.htm");
		request.setMethod("POST");
		request.addParameter("saveReport", "saveReport");
		request.addParameter("reportId", Long.toString(1l));
		final Report report = getReportingDashboardService().findReportById(1l);
		final List<ReportColumn> columns = report.getColumns();
		final List<Long> columnIds = new LinkedList<Long>();
		for (final ReportColumn column : columns) {
			columnIds.add(column.getId());
		}
		for (int columnIdx = columnIds.size() - 1; columnIdx >= 0; --columnIdx) {
			request.addParameter("columnId", Long.toString(columnIds.get(columnIdx)));
		}

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The view is redirected to the report view", ReportGenerator.REDIRECT_GENERATE_REPORT,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertEquals("The report identifier is in the model", 1l, actualModel.get("reportId"));
		final Report actualReport = getReportingDashboardService().findReportById(1l);
		final List<ReportColumn> actualColumns = actualReport.getColumns();
		int columnIdx = columnIds.size() - 1;
		for (final ReportColumn actualColumn : actualColumns) {
			assertEquals(actualColumn + " is in the correct place", columnIds.get(columnIdx), actualColumn.getId());
			--columnIdx;
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#saveReportSettings(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Mar 13, 2012
	 * @version Mar 13, 2012
	 */
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/ReportingDashboard.xml" })
	@Test
	public final void testSaveReportSettings() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/ReportSettings.htm");
		request.setMethod("POST");
		request.addParameter("saveReportSettings", "saveReportSettings");
		request.addParameter("reportId", Long.toString(1l));
		final Report report = getReportingDashboardService().findReportById(1l);
		final FlowType flowType = FlowType.FWAB;
		request.addParameter("flowType", flowType.toString());
		final boolean applyFaces = true;
		request.addParameter("applyFaces", Boolean.toString(applyFaces));
		final String face = "usvote";
		request.addParameter("face", face);
		final boolean standard = report.isStandard();
		request.addParameter("standard", Boolean.toString(standard));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The view is redirected to the report view", ReportGenerator.REDIRECT_GENERATE_REPORT,
				actualModelAndView.getViewName());
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model map is used as the model", actualModel);
		assertEquals("The report identifier is in the model", 1l, actualModel.get("reportId"));
		assertSame("The flow type is set", flowType, report.getFlowType());
		assertEquals("The apply faces flag is set", applyFaces, report.isApplyFaces());
		assertEquals("The faces are set", new HashSet<String>(Arrays.asList(face)), report.getFaces());
		assertEquals("The standard flag is not changed", standard, report.isStandard());
	}

	/** {@inheritDoc} */
	@Override
	protected final EditReport createBaseReportingDashboardController() {
		final EditReport editReport = applicationContext.getBean(EditReport.class);
		return editReport;
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
	 * Custom assertion to ensure that the fields for the column match those for the input collection of question fields.
	 * 
	 * @author IanBrown
	 * @param column
	 *            the column.
	 * @param questionFields
	 *            the question fields.
	 * @since Mar 13, 2012
	 * @version Mar 13, 2012
	 */
	private void assertFields(final ReportColumn column, final Collection<QuestionField> questionFields) {
		final Collection<ReportField> fields = column.getFields();
		assertEquals("There are the correct number of report fields", questionFields.size(), fields.size());

		for (final ReportField field : fields) {
			final QuestionField question = field.getQuestion();
			assertNotNull(field + " is a question field", question);
			assertTrue("The question field for " + field + " is in the ones for the user selected question",
					questionFields.contains(question));
		}
	}

	/**
	 * Gets the questionnaireService.
	 * 
	 * @author IanBrown
	 * @return the questionnaireService.
	 * @since Mar 13, 2012
	 * @version Mar 13, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}
}
