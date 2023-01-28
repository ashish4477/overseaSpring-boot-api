/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bearcode.ovf.utils.UserInfoFields;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;
import com.bearcode.ovf.model.reportingdashboard.UserFieldNames;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.service.QuestionnaireService;

/**
 * Extended {@link BaseReportingDashboardControllerCheck} test for {@link EditReport}.
 * 
 * @author IanBrown
 * 
 * @since Jan 6, 2012
 * @version Jun 13, 2012
 */
public final class EditReportTest extends BaseReportingDashboardControllerCheck<EditReport> {

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the overseas user service.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	private OverseasUserService overseasUserService;

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addFlows()} for the case where the user is an
	 * administrator.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Jun 13, 2012
	 */
	@Test
	public final void testAddFlows_administrator() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		replayAll();

		final FlowType[] actualFlows = getBaseController().addFlows();

		final List<FlowType> expectedFlowEntries = new ArrayList<>(Arrays.asList(FlowType.values()));
		final FlowType[] expectedFlows = expectedFlowEntries.toArray(new FlowType[expectedFlowEntries.size()]);
		assertArrayEquals("The flows are all of the available ones", expectedFlows, actualFlows);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addFlows()} for the case where the user does
	 * not have an assigned face.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Jun 13, 2012
	 */
	@Test
	public final void testAddFlows_noAssignedFace() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		EasyMock.expect(user.getAssignedFace()).andReturn(null).anyTimes();
		replayAll();

		final FlowType[] actualFlows = getBaseController().addFlows();

		final List<FlowType> expectedFlowEntries = new ArrayList<>(Arrays.asList(FlowType.values()));
		final FlowType[] expectedFlows = expectedFlowEntries.toArray(new FlowType[expectedFlowEntries.size()]);
		assertArrayEquals("The flows are all of the available ones", expectedFlows, actualFlows);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addFlows()} for the case where the user's
	 * assigned face is not US vote.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testAddFlows_notUSVote() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		final FaceConfig assignedFace = createMock( "AssignedFace", FaceConfig.class );
		EasyMock.expect(user.getAssignedFace()).andReturn(assignedFace).anyTimes();
		EasyMock.expect(assignedFace.getRelativePrefix()).andReturn("ovf").anyTimes();
		replayAll();

		final FlowType[] actualFlows = getBaseController().addFlows();

		final FlowType[] expectedFlows = { FlowType.RAVA, FlowType.FWAB };
		assertArrayEquals("The flows are all of the overseas ones", expectedFlows, actualFlows);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addFlows()} for the case where there is no
	 * user.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testAddFlows_noUser() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, null );
		replayAll();

		final FlowType[] actualFlows = getBaseController().addFlows();

		assertEquals( "There are no flows available", 0, actualFlows.length );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addFlows()} for the case where the user's
	 * assigned face is US vote.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testAddFlows_usVote() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		final FaceConfig assignedFace = createMock( "AssignedFace", FaceConfig.class );
		EasyMock.expect(user.getAssignedFace()).andReturn(assignedFace).anyTimes();
		EasyMock.expect(assignedFace.getRelativePrefix()).andReturn("usvote").anyTimes();
		replayAll();

		final FlowType[] actualFlows = getBaseController().addFlows();

		final FlowType[] expectedFlows = { FlowType.DOMESTIC_REGISTRATION, FlowType.DOMESTIC_ABSENTEE };
		assertArrayEquals( "The flows are all of the domestic ones", expectedFlows, actualFlows );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addMetric(HttpServletRequest, ModelMap, long, String, String[])}
	 * .
	 * 
	 * @author IanBrown
	 * @since Mar 13, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testAddMetric() {
		final long reportId = 726l;
		final String metric = "Percentage";
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("reportId", Long.toString(reportId));
		request.setParameter("metric", metric);
		final ModelMap model = createModelMap( user, request, null, true, false );
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		getReportingDashboardService().addMetricColumnToReport( report, metric, null );
		getReportingDashboardService().saveReport( report );
		getReportingDashboardService().flush();
		addAttributeToModelMap( model, "reportId", reportId );
		replayAll();

		final String actualModelAndView = getBaseController().addMetric(request, model, reportId, metric, null);

		assertEquals( "A redirection to the report editor is returned", EditReport.REDIRECT_EDIT_REPORT, actualModelAndView );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addQuestion(HttpServletRequest, ModelMap, long, String, String, String, String, String[])}
	 * .
	 * 
	 * @author IanBrown
	 * @since Mar 13, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testAddQuestion() {
		final long reportId = 726l;
		final String columnName = "Question";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createUser(null);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		EasyMock.expect(user.getPhoneType()).andReturn( "" ).anyTimes();
		EasyMock.expect(user.isOptIn()).andReturn( false ).anyTimes();
		EasyMock.expect(user.getAlternatePhoneType()).andReturn( "" ).anyTimes();
		EasyMock.expect(user.getVoterClassificationType()).andReturn(null).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("reportId", Long.toString(reportId));
		request.setParameter("columnName", columnName);
		request.setParameter("page", page);
		request.setParameter("group", group);
		request.setParameter("question", question);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final QuestionnairePage wizardPage = createMock("Page", QuestionnairePage.class);
		final List<QuestionnairePage> wizardPages = Arrays.asList(wizardPage);
		EasyMock.expect(getQuestionnaireService().findQuestionnairePages()).andReturn(wizardPages);
		final FlowType flowType = FlowType.FWAB;
		EasyMock.expect(report.getFlowType()).andReturn(flowType);
		final FlowType[] flows = FlowType.values();
		EasyMock.expect(model.get("flows")).andReturn(flows);
		final String answer = "Answer";
		final String[] answers = new String[] { answer };
		getReportingDashboardService().addQuestionColumnToReport( EasyMock.eq( report ), (WizardContext) EasyMock.anyObject(),
                EasyMock.aryEq( flows ), EasyMock.eq( wizardPages ), EasyMock.eq( columnName ), EasyMock.eq( page ), EasyMock.eq( group ),
                EasyMock.eq( question ), EasyMock.aryEq( answers ) );
		addAttributeToModelMap( model, "reportId", reportId );
		getReportingDashboardService().saveReport( report );
		getReportingDashboardService().flush();
		replayAll();

		final String actualModelAndView = getBaseController().addQuestion(request, model, reportId, columnName, page, group,
				question, answers);

		assertEquals("A redirection to the report editor is returned", EditReport.REDIRECT_EDIT_REPORT, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#addUserDetail(HttpServletRequest, ModelMap, long, String, String, String[])}
	 * .
	 * 
	 * @author IanBrown
	 * @since Mar 13, 2012
	 * @version Mar 13, 2012
	 */
	@Test
	public final void testAddUserDetail() {
		final long reportId = 726l;
		final String columnName = "Gender";
		final UserFieldNames userFieldName = UserFieldNames.GENDER;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("reportId", Long.toString(reportId));
		request.setParameter("columnName", columnName);
		final String userDetail = userFieldName.getUiName();
		request.setParameter("userDetail", userDetail);
		final String male = "Male";
		request.setParameter("answer", male);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final String[] answers = new String[] { male };
		getReportingDashboardService().addUserDetailColumnToReport(EasyMock.eq(report), EasyMock.eq(columnName),
				EasyMock.eq(userDetail), EasyMock.aryEq(answers));
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();
		addAttributeToModelMap(model, "reportId", reportId);
		replayAll();

		final String actualModelAndView = getBaseController().addUserDetail(request, model, reportId, columnName, userDetail,
				new String[] { male });

		assertEquals("A redirection to the report editor is returned", EditReport.REDIRECT_EDIT_REPORT, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#cloneReport(HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testCloneReport() {
		final long reportId = 89127l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		addOverseasUserToAuthentication( authentication, user );
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("reportId", Long.toString(reportId));
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final Report copiedReport = createMock("CopiedReport", Report.class);
		EasyMock.expect(getReportingDashboardService().copyReport(report, user)).andReturn(copiedReport);
		getReportingDashboardService().saveReport( copiedReport );
		getReportingDashboardService().flush();
		final long copiedReportId = 781223l;
		EasyMock.expect( copiedReport.getId() ).andReturn( copiedReportId );
		addAttributeToModelMap(model, "reportId", copiedReportId);
		replayAll();

		final String actualModelAndView = getBaseController().cloneReport( request, model, reportId );

		assertEquals("A redirection to the report settings editor is returned", EditReport.REDIRECT_REPORT_SETTINGS,
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#createReport(javax.servlet.http.HttpServletRequest, ModelMap)}.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Apr 5, 2012
	 */
	@Test
	public final void testCreateReport() {
		setExpectedContentBlock(EditReport.REPORT_SETTINGS_CONTENT_BLOCK);
		try {
			final Authentication authentication = addAuthenticationToSecurityContext();
			final OverseasUser user = createMock("User", OverseasUser.class);
			addOverseasUserToAuthentication(authentication, user);
			EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
			final MockHttpServletRequest request = new MockHttpServletRequest();
			final ModelMap model = createModelMap(user, request, null, true, false);
			final Report report = createMock("Report", Report.class);
			EasyMock.expect(getReportingDashboardService().createCustomReport(user, null)).andReturn(report);
			addAttributeToModelMap(model, "report", report);
			EasyMock.expect(getOverseasUserService().findRolesByName(UserRole.USER_ROLE_REPORTING_DASHBOARD)).andReturn(null);
			EasyMock.expect(report.getColumns()).andReturn(null);
			EasyMock.expect(report.getOwner()).andReturn(user).anyTimes();
			EasyMock.expect(user.getId()).andReturn(1l).anyTimes();
			report.setTitle("Report");
			addAttributeToModelMap(model, EasyMock.eq("possibleOwners"), EasyMock.eq(Arrays.asList(user)));
			addFacesToModelMap(model);
			replayAll();

			final String actualModelAndView = getBaseController().createReport(request, model);

			assertEquals("The reporting dashboard template is used as the view",
					ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView);
			verifyAll();
		} finally {
			setExpectedContentBlock(EditReport.DEFAULT_CONTENT_BLOCK);
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#deleteReport(javax.servlet.http.HttpServletRequest, ModelMap, Long)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jan 10, 2012
	 * @version Jan 19, 2012
	 */
	@Test
	public final void testDeleteReport() {
		final long reportId = 52812l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("reportId", Long.toString(reportId));
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		getReportingDashboardService().deleteReport(report);
		replayAll();

		final String actualModelAndView = getBaseController().deleteReport(request, model, reportId);

		assertEquals("A redirection to the reporting dashboard is returned", ReportingDashboard.REDIRECT_REPORTING_DASHBOARD,
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#editReport(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 5, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testEditReport() {
		final long reportId = 23l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createUser( null );
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		EasyMock.expect(user.getPhoneType()).andReturn( "" ).anyTimes();
		EasyMock.expect(user.getAlternatePhoneType()).andReturn( "" ).anyTimes();
		EasyMock.expect(user.isOptIn()).andReturn( false ).anyTimes();
		EasyMock.expect(user.getVoterClassificationType()).andReturn(null).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap( user, request, null, true, false );
		request.addParameter("reportId", Long.toString(reportId));
		final Report report = createMock( "Report", Report.class );
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		addAttributeToModelMap( model, "report", report );
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final List<QuestionnairePage> wizardPages = Arrays.asList(page);
		EasyMock.expect(getQuestionnaireService().findQuestionnairePages()).andReturn( wizardPages );
		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = new HashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final String pageName = "Page Name";
		final Map<String, Map<String, Collection<QuestionField>>> pageFields = new HashMap<String, Map<String, Collection<QuestionField>>>();
		reportableFields.put(pageName, pageFields);
		final String groupName = "Group Name";
		final Map<String, Collection<QuestionField>> groupFields = new HashMap<String, Collection<QuestionField>>();
		pageFields.put(groupName, groupFields);
		final String questionName = "Question Name";
		final QuestionField questionField = createMock("QuestionField", QuestionField.class);
		final Collection<QuestionField> questionFields = Arrays.asList( questionField );
		groupFields.put( questionName, questionFields );
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(report.getFlowType()).andReturn(flowType);
		final FlowType[] flows = FlowType.values();
		EasyMock.expect(model.get( "flows" )).andReturn(flows);
		EasyMock.expect(
				getReportingDashboardService().pagesToReportableFields(EasyMock.eq(report), (WizardContext) EasyMock.anyObject(),
						EasyMock.eq(flows), EasyMock.eq(wizardPages))).andReturn( reportableFields );
		final Map<String, Map<String, Map<String, List<String>>>> viewPages = new HashMap<String, Map<String, Map<String, List<String>>>>();
		EasyMock.expect(getReportingDashboardService().convertReportableFieldsToPages(reportableFields)).andReturn(viewPages);
		addAttributeToModelMap( model, "pages", viewPages );
		final List<Map<String, Object>> userFieldNames = new ArrayList<Map<String, Object>>();
		EasyMock.expect(getReportingDashboardService().buildUserFields(report, null)).andReturn( userFieldNames );
		addAttributeToModelMap( model, "userFieldNames", userFieldNames );
		replayAll();

		final String actualModelAndView = getBaseController().editReport(request, model, reportId);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#editReportSettings(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 5, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testEditReportSettings() {
		setExpectedContentBlock(EditReport.REPORT_SETTINGS_CONTENT_BLOCK);
		try {
			final long reportId = 23l;
			final Authentication authentication = addAuthenticationToSecurityContext();
			final OverseasUser user = createMock("User", OverseasUser.class);
			addOverseasUserToAuthentication(authentication, user);
			EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
			final MockHttpServletRequest request = new MockHttpServletRequest();
			final ModelMap model = createModelMap(user, request, null, true, false);
			request.addParameter("reportId", Long.toString(reportId));
			final Report report = createMock("Report", Report.class);
			EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
			addAttributeToModelMap(model, "report", report);
			EasyMock.expect(getOverseasUserService().findRolesByName(UserRole.USER_ROLE_REPORTING_DASHBOARD)).andReturn(null);
			EasyMock.expect(report.getColumns()).andReturn(null);
			EasyMock.expect(report.getOwner()).andReturn(user).anyTimes();
			EasyMock.expect(user.getId()).andReturn(1l).anyTimes();
			addAttributeToModelMap(model, EasyMock.eq("possibleOwners"), EasyMock.eq(Arrays.asList(user)));
			addFacesToModelMap(model);
			replayAll();

			final String actualModelAndView = getBaseController().editReportSettings(request, model, reportId);

			assertEquals("The reporting dashboard template is used as the view",
					ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView);
			verifyAll();
		} finally {
			setExpectedContentBlock(EditReport.DEFAULT_CONTENT_BLOCK);
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#editReportSettings(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, long)}
	 * for the case where there are columns in the report.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 5, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testEditReportSettings_columns() {
		setExpectedContentBlock(EditReport.REPORT_SETTINGS_CONTENT_BLOCK);
		try {
			final long reportId = 23l;
			final Authentication authentication = addAuthenticationToSecurityContext();
			final OverseasUser user = createMock("User", OverseasUser.class);
			addOverseasUserToAuthentication(authentication, user);
			EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
			final MockHttpServletRequest request = new MockHttpServletRequest();
			final ModelMap model = createModelMap(user, request, null, true, false);
			request.addParameter("reportId", Long.toString(reportId));
			final Report report = createMock("Report", Report.class);
			EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
			addAttributeToModelMap(model, "report", report);
			EasyMock.expect(getOverseasUserService().findRolesByName(UserRole.USER_ROLE_REPORTING_DASHBOARD)).andReturn(null);
			final ReportColumn column = createMock("Column", ReportColumn.class);
			final List<ReportColumn> columns = Arrays.asList(column);
			EasyMock.expect(report.getColumns()).andReturn(columns);
			EasyMock.expect(model.remove( "flows" )).andReturn(FlowType.values());
			EasyMock.expect(report.getOwner()).andReturn(user).anyTimes();
			EasyMock.expect(user.getId()).andReturn(1l).anyTimes();
			addAttributeToModelMap(model, EasyMock.eq("possibleOwners"), EasyMock.eq(Arrays.asList(user)));
			addFacesToModelMap(model);
			replayAll();

			final String actualModelAndView = getBaseController().editReportSettings(request, model, reportId);

			assertEquals("The reporting dashboard template is used as the view",
					ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView);
			verifyAll();
		} finally {
			setExpectedContentBlock(EditReport.DEFAULT_CONTENT_BLOCK);
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#saveReport(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jan 10, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testSaveReport() {
		final long reportId = 23l;
		final long secondColumnId = 6756l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("reportId", Long.toString(reportId));
		request.addParameter("columnId", Long.toString(secondColumnId));
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		getReportingDashboardService().updateReportColumns(EasyMock.eq(report), EasyMock.aryEq(new long[] { secondColumnId }));
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();
		EasyMock.expect(report.getId()).andReturn(reportId);
		addAttributeToModelMap(model, "reportId", reportId);
		replayAll();

		final String actualModelAndView = getBaseController().saveReport(request, model, reportId);

		assertEquals("A redirection to the report view is returned", ReportGenerator.REDIRECT_GENERATE_REPORT, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#saveReportSettings(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * for the case where the faces are set.
	 * 
	 * @author IanBrown
	 * @since Mar 13, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testSaveReportSettings_faces() {
		final long reportId = 23l;
		final String title = "Title";
		final String description = "Description";
		final boolean standard = false;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("reportId", Long.toString(reportId));
		request.addParameter("reportTitle", title);
		request.addParameter("reportDescription", description);
		final String standardString = Boolean.toString(standard);
		request.addParameter("standardReport", standardString);
		final String flowTypeValue = FlowType.RAVA.name();
		request.addParameter("flowType", flowTypeValue);
		final String face1UrlPath = "face1/vote";
		request.addParameter("face", face1UrlPath);
		final String face2UrlPath = "face2/vote";
		request.addParameter("face", face2UrlPath);
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final FaceConfig face1Config = createMock("Face1Config", FaceConfig.class);
		final FaceConfig face2Config = createMock("Face2Config", FaceConfig.class);
		final FaceConfig face3Config = createMock("Face3Config", FaceConfig.class);
		EasyMock.expect(getFacesService().findAllConfigs()).andReturn(Arrays.asList(face1Config, face2Config, face3Config));
		EasyMock.expect(face1Config.getUrlPath()).andReturn(face1UrlPath).anyTimes();
		EasyMock.expect(face2Config.getUrlPath()).andReturn(face2UrlPath).anyTimes();
		final String face3UrlPath = "face3/vote";
		EasyMock.expect(face3Config.getUrlPath()).andReturn(face3UrlPath).anyTimes();
		EasyMock.expect(
				getReportingDashboardService().updateReportSettings(EasyMock.eq(report), EasyMock.eq(title),
						EasyMock.eq(description), EasyMock.eq(standardString), EasyMock.eq(flowTypeValue),
						EasyMock.aryEq(new String[] { face1UrlPath, face2UrlPath }), (FaceConfig) EasyMock.eq(null),
						EasyMock.eq(Arrays.asList(face1UrlPath, face2UrlPath, face3UrlPath)), (OverseasUser) EasyMock.eq(null)))
				.andReturn(true);
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();
		EasyMock.expect(report.getId()).andReturn(reportId);
		addAttributeToModelMap(model, "reportId", reportId);
        EasyMock.expect(report.isStandard()).andReturn(false).anyTimes();
        replayAll();

		final String actualModelAndView = getBaseController().saveReportSettings(request, model, reportId);

		assertEquals("A redirection to the report view is returned", ReportGenerator.REDIRECT_GENERATE_REPORT, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#saveReportSettings(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * for the case where the flow type is set.
	 * 
	 * @author IanBrown
	 * @since Mar 13, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testSaveReportSettings_flowType() {
		final long reportId = 23l;
		final String title = "Report Title";
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock( "User", OverseasUser.class );
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		addOverseasUserToAuthentication( authentication, user );
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		request.addParameter("reportId", Long.toString(reportId));
		request.addParameter("reportTitle", title);
		final String flowTypeValue = flowType.name();
		request.addParameter("flowType", flowTypeValue);
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().findReportById(reportId)).andReturn(report);
		final FaceConfig face1Config = createMock("Face1Config", FaceConfig.class);
		final FaceConfig face2Config = createMock("Face2Config", FaceConfig.class);
		final FaceConfig face3Config = createMock("Face3Config", FaceConfig.class);
		EasyMock.expect(getFacesService().findAllConfigs()).andReturn(Arrays.asList(face1Config, face2Config, face3Config));
		final String face1UrlPath = "face1/vote";
		EasyMock.expect(face1Config.getUrlPath()).andReturn(face1UrlPath).anyTimes();
		final String face2UrlPath = "face2/vote";
		EasyMock.expect(face2Config.getUrlPath()).andReturn(face2UrlPath).anyTimes();
		final String face3UrlPath = "face3/vote";
		EasyMock.expect(face3Config.getUrlPath()).andReturn(face3UrlPath).anyTimes();
		EasyMock.expect(
				getReportingDashboardService().updateReportSettings(EasyMock.eq(report), EasyMock.eq(title),
						(String) EasyMock.eq(null), (String) EasyMock.eq(null), EasyMock.eq(flowTypeValue),
						EasyMock.aryEq((String[]) null), (FaceConfig) EasyMock.eq(null),
						EasyMock.eq(Arrays.asList(face1UrlPath, face2UrlPath, face3UrlPath)), (OverseasUser) EasyMock.eq(null)))
				.andReturn(true);
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();
		EasyMock.expect(report.getId()).andReturn(reportId);
		addAttributeToModelMap(model, "reportId", reportId);
        EasyMock.expect(report.isStandard()).andReturn(false).anyTimes();
		replayAll();

		final String actualModelAndView = getBaseController().saveReportSettings(request, model, reportId);

		assertEquals("A redirection to the report view is returned", ReportGenerator.REDIRECT_GENERATE_REPORT, actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.EditReport#saveReportSettings(javax.servlet.http.HttpServletRequest, ModelMap, long)}
	 * for the case where the report is a new report.
	 * 
	 * @author IanBrown
	 * @since Mar 16, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testSaveReportSettings_newReport() {
		final String title = "Title";
		final String description = "Description";
		final boolean standard = false;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap( user, request, null, true, false );
		request.addParameter( "reportTitle", title );
		request.addParameter( "reportDescription", description );
		final String standardString = Boolean.toString( standard );
		request.addParameter("standardReport", standardString);
		final String flowTypeValue = FlowType.RAVA.name();
		request.addParameter("flowType", flowTypeValue);
		final String face1UrlPath = "face1/vote";
		request.addParameter("face", face1UrlPath);
		final String face2UrlPath = "face2/vote";
		request.addParameter( "face", face2UrlPath );
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardService().createCustomReport(user, null)).andReturn(report);
		final FaceConfig face1Config = createMock("Face1Config", FaceConfig.class);
		final FaceConfig face2Config = createMock("Face2Config", FaceConfig.class);
		final FaceConfig face3Config = createMock("Face3Config", FaceConfig.class);
		EasyMock.expect(getFacesService().findAllConfigs()).andReturn(Arrays.asList(face1Config, face2Config, face3Config));
		EasyMock.expect(face1Config.getUrlPath()).andReturn(face1UrlPath).anyTimes();
		EasyMock.expect(face2Config.getUrlPath()).andReturn(face2UrlPath).anyTimes();
		final String face3UrlPath = "face3/vote";
		EasyMock.expect(face3Config.getUrlPath()).andReturn(face3UrlPath).anyTimes();
		EasyMock.expect(
				getReportingDashboardService().updateReportSettings(EasyMock.eq(report), EasyMock.eq(title),
						EasyMock.eq(description), EasyMock.eq(standardString), EasyMock.eq(flowTypeValue),
						EasyMock.aryEq(new String[] { face1UrlPath, face2UrlPath }), (FaceConfig) EasyMock.eq(null),
						EasyMock.eq(Arrays.asList(face1UrlPath, face2UrlPath, face3UrlPath)), (OverseasUser) EasyMock.eq(null)))
				.andReturn(true);
		getReportingDashboardService().saveReport(report);
		getReportingDashboardService().flush();
		final long reportId = 1298l;
		EasyMock.expect(report.getId()).andReturn(reportId);
		addAttributeToModelMap(model, "reportId", reportId);
        EasyMock.expect(report.isStandard()).andReturn(false).anyTimes();
		replayAll();

		final String actualModelAndView = getBaseController().saveReportSettings(request, model, null);

		assertEquals("A redirection to the report editor is returned", EditReport.REDIRECT_EDIT_REPORT, actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EditReport createBaseReportingDashboardController() {
		final EditReport editReport = new EditReport();
		editReport.setQuestionnaireService(getQuestionnaireService());
		editReport.setOverseasUserService(getOverseasUserService());
		return editReport;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return EditReport.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
		setExpectedContentBlock(EditReport.DEFAULT_CONTENT_BLOCK);
		setQuestionnaireService(createMock("QuestionnaireService", QuestionnaireService.class));
		setOverseasUserService(createMock("OverseasUserService", OverseasUserService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
		setOverseasUserService( null );
		setQuestionnaireService( null );
	}

	/**
	 * Adds faces to the model map.
	 * 
	 * @author IanBrown
	 * @param model
	 *            the model map.
	 * @since Feb 3, 2012
	 * @version Mar 30, 2012
	 */
	private void addFacesToModelMap(final ModelMap model) {
		final FaceConfig face = createMock("Face", FaceConfig.class);
		final Collection<FaceConfig> faces = Arrays.asList(face);
		EasyMock.expect(getFacesService().findAllConfigs()).andReturn(faces);
		final String relativePrefix = "faces/face";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		EasyMock.expect(face.getUrlPath()).andReturn("face/vote").anyTimes();
		final Map<String, String> presetPdfAnswersFields = new HashMap<String, String>();
		EasyMock.expect(face.getPresetPdfAnswersFields()).andReturn(presetPdfAnswersFields).anyTimes();
		addAttributeToModelMap( model, EasyMock.eq( "faces" ), EasyMock.anyObject() );
	}

	/**
	 * Creates a user with the assigned face.
	 * 
	 * @author IanBrown
	 * @param assignedFace
	 *            the assigned face.
	 * @return the user.
	 * @since Mar 27, 2012
	 * @version Mar 27, 2012
	 */
	private OverseasUser createUser(final FaceConfig assignedFace) {
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(user.getUsername()).andReturn("Username").anyTimes();
		EasyMock.expect(user.getName()).andReturn(null).anyTimes();
		EasyMock.expect(user.getPreviousName()).andReturn(null).anyTimes();
		EasyMock.expect(user.getPhone()).andReturn("Phone").anyTimes();
		EasyMock.expect(user.getAlternateEmail()).andReturn("Alternate Email").anyTimes();
		EasyMock.expect(user.getAlternatePhone()).andReturn("Alternate Phone").anyTimes();
		EasyMock.expect(user.getVotingRegion()).andReturn(null).anyTimes();
        EasyMock.expect( user.getEodRegionId() ).andReturn( "1" ).anyTimes();
		EasyMock.expect(user.getVoterType()).andReturn(null).anyTimes();
		EasyMock.expect(user.getVoterHistory()).andReturn(null).anyTimes();
		EasyMock.expect(user.getBallotPref()).andReturn("Ballot Pref").anyTimes();
		EasyMock.expect(user.getBirthDate()).andReturn(27).anyTimes();
		EasyMock.expect(user.getBirthYear()).andReturn(2012).anyTimes();
		EasyMock.expect(user.getBirthMonth()).andReturn(3).anyTimes();
		EasyMock.expect(user.getRace()).andReturn("Race").anyTimes();
		EasyMock.expect(user.getEthnicity()).andReturn("Ethnicity").anyTimes();
		EasyMock.expect(user.getGender()).andReturn("Gender").anyTimes();
		EasyMock.expect(user.getParty()).andReturn("Party").anyTimes();
		EasyMock.expect(user.getVotingAddress()).andReturn(null).anyTimes();
		EasyMock.expect(user.getCurrentAddress()).andReturn(null).anyTimes();
		EasyMock.expect(user.getForwardingAddress()).andReturn(null).anyTimes();
		EasyMock.expect(user.getPreviousAddress()).andReturn(null).anyTimes();
		EasyMock.expect(user.getAssignedFace()).andReturn(assignedFace).anyTimes();
		return user;
	}

    @Before
    public void setUpUserFields() {
        UserInfoFields userInfoFields = new UserInfoFields();
        EodApiService eodApiService = createMock( "EodApiService", EodApiService.class );
        EodRegion region = new EodRegion();
        region.setRegionName( "RegionName" );
        EasyMock.expect( eodApiService.getRegion( EasyMock.anyObject( String.class ) ) ).andReturn( region ).anyTimes();
    }

	/**
	 * Gets the user service.
	 * 
	 * @author IanBrown
	 * @return the user service.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	private OverseasUserService getOverseasUserService() {
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
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
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
	private void setOverseasUserService(final OverseasUserService overseasUserService) {
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
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}
}
