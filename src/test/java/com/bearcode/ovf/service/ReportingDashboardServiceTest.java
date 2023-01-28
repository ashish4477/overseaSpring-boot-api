/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.DAO.ReportingDashboardDAO;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.FaceDependency;
import com.bearcode.ovf.model.questionnaire.FieldDependency;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.FlowDependency;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.PageType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportAnswer;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;
import com.bearcode.ovf.model.reportingdashboard.ReportField;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;
import com.bearcode.ovf.model.reportingdashboard.UserFieldNames;

/**
 * Test for {@link ReportingDashboardService}.
 * 
 * @author IanBrown
 * 
 * @since Jan 4, 2012
 * @version Apr 3, 2012
 */
@SuppressWarnings("rawtypes")
public final class ReportingDashboardServiceTest extends EasyMockSupport {

	/**
	 * the reporting dashboard service to test.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	private ReportingDashboardService reportingDashboardService;

	/**
	 * the reporting dashbaord DAO.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	private ReportingDashboardDAO reportingDashboardDAO;

	/**
	 * the question field service.
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	private QuestionFieldService questionFieldService;

	/**
	 * Sets up the reporting dashboard service to test.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 26, 2012
	 */
	@Before
	public final void setUpReportingDashboardService() {
		setQuestionFieldService(createMock("QuestionFieldService", QuestionFieldService.class));
		setReportingDashboardDAO(createMock("ReportingDashboardDAO", ReportingDashboardDAO.class));
		setReportingDashboardService(createReportingDashboardService());
		getReportingDashboardService().setQuestionFieldService(getQuestionFieldService());
		getReportingDashboardService().setReportingDashboardDAO(getReportingDashboardDAO());
	}

	/**
	 * Tears down the reporting dashboard service after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	@After
	public final void teardownReportingDashboardService() {
		setReportingDashboardService(null);
		setReportingDashboardDAO(null);
		setQuestionFieldService(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#addMetricColumnToReport(Report, String, String[])}.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testAddMetricColumnToReport() {
		final Report report = createMock("Report", Report.class);
		final String metric = "Number";
		final String[] answers = null;
		EasyMock.expect(report.addColumn((ReportColumn) EasyMock.anyObject())).andDelegateTo(new Report() {

			@Override
			public boolean addColumn(final ReportColumn column) {
				assertEquals("The column name is the name of the metric", metric, column.getName());
				return true;
			}
		});
		replayAll();

		getReportingDashboardService().addMetricColumnToReport(report, metric, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addQuestionColumnToReport(Report, WizardContext, FlowType[], List, String, String, String, String, String[])}
	 * for the case where there are answers.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testAddQuestionColumnToReport_answers() {
		final Report report = createMock("Report", Report.class);
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final FlowType[] flows = FlowType.values();
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> wizardPages = Arrays.asList(questionnairePage);
		final String columnName = "Column Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final String answer = "Answer";
		final String[] answers = new String[] { answer };
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		buildQuestionnairePage(questionnairePage, page, group, question, answers);
		EasyMock.expect(report.addColumn((ReportColumn) EasyMock.anyObject())).andDelegateTo(new Report() {

			@Override
			public final boolean addColumn(final ReportColumn column) {
				assertEquals("The name of the column is set", columnName, column.getName());
				final Collection<ReportField> fields = column.getFields();
				assertEquals("There is one field in the column", 1, fields.size());
				final ReportField field = fields.iterator().next();
				assertNotNull("The field is tied to a specific question field", field.getQuestion());
				assertEquals("The title of the question is expected", question, field.getQuestion().getTitle());
				final Collection<ReportAnswer> reportAnswers = field.getAnswers();
				assertEquals("The field one answer", 1, reportAnswers.size());
				final ReportAnswer reportAnswer = reportAnswers.iterator().next();
				final FieldDictionaryItem predefinedAnswer = reportAnswer.getPredefinedAnswer();
				assertNotNull("The answer is tied to a specific predefined answer", predefinedAnswer);
				assertEquals("The predefined answer has the expected value", answer, predefinedAnswer.getViewValue());
				return true;
			}
		});
		replayAll();

		getReportingDashboardService().addQuestionColumnToReport(report, wizardContext, flows, wizardPages, columnName, page,
				group, question, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addQuestionColumnToReport(Report, WizardContext, FlowType[], List, String, String, String, String, String[])}
	 * for the case where the group is not found.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAddQuestionColumnToReport_groupNotFound() {
		final Report report = createMock("Report", Report.class);
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final FlowType[] flows = FlowType.values();
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> wizardPages = Arrays.asList(questionnairePage);
		final String columnName = "Column Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final String[] answers = null;
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		buildQuestionnairePage(questionnairePage, page, "DifferentGroup", question, answers);
		replayAll();

		getReportingDashboardService().addQuestionColumnToReport(report, wizardContext, flows, wizardPages, columnName, page,
				group, question, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addQuestionColumnToReport(Report, WizardContext, FlowType[], List, String, String, String, String, String[])}
	 * for the case where there are no answers specified.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testAddQuestionColumnToReport_noAnswers() {
		final Report report = createMock("Report", Report.class);
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final FlowType[] flows = FlowType.values();
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> wizardPages = Arrays.asList(questionnairePage);
		final String columnName = "Column Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final String[] answers = null;
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		buildQuestionnairePage(questionnairePage, page, group, question, answers);
		EasyMock.expect(report.addColumn((ReportColumn) EasyMock.anyObject())).andDelegateTo(new Report() {

			@Override
			public final boolean addColumn(final ReportColumn column) {
				assertEquals("The name of the column is set", columnName, column.getName());
				final Collection<ReportField> fields = column.getFields();
				assertEquals("There is one field in the column", 1, fields.size());
				final ReportField field = fields.iterator().next();
				assertNotNull("The field is tied to a specific question field", field.getQuestion());
				assertEquals("The title of the question is expected", question, field.getQuestion().getTitle());
				assertNull("The field has no answers", field.getAnswers());
				return true;
			}
		});
		replayAll();

		getReportingDashboardService().addQuestionColumnToReport(report, wizardContext, flows, wizardPages, columnName, page,
				group, question, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addQuestionColumnToReport(Report, WizardContext, FlowType[], List, String, String, String, String, String[])}
	 * for the case where the page is not found.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAddQuestionColumnToReport_pageNotFound() {
		final Report report = createMock("Report", Report.class);
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final FlowType[] flows = FlowType.values();
		final List<QuestionnairePage> wizardPages = new ArrayList<QuestionnairePage>();
		final String columnName = "Column Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final String[] answers = null;
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		replayAll();

		getReportingDashboardService().addQuestionColumnToReport(report, wizardContext, flows, wizardPages, columnName, page,
				group, question, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addQuestionColumnToReport(Report, WizardContext, FlowType[], List, String, String, String, String, String[])}
	 * for the case where the question is not found.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAddQuestionColumnToReport_questionNotFound() {
		final Report report = createMock("Report", Report.class);
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final FlowType[] flows = FlowType.values();
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> wizardPages = Arrays.asList(questionnairePage);
		final String columnName = "Column Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final String[] answers = null;
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		buildQuestionnairePage(questionnairePage, page, group, "DifferentQuestion", answers);
		replayAll();

		getReportingDashboardService().addQuestionColumnToReport(report, wizardContext, flows, wizardPages, columnName, page,
				group, question, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addUserDetailColumnToReport(Report, String, String, String[])} for
	 * the case where answers are provided.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testAddUserDetailColumnToReport_answers() {
		final Report report = createMock("Report", Report.class);
		final String columnName = "Column Name";
		final UserFieldNames userFieldName = UserFieldNames.GENDER;
		final String userDetail = userFieldName.getUiName();
		final String answer = "Female";
		final String[] answers = new String[] { answer };
		EasyMock.expect(report.addColumn((ReportColumn) EasyMock.anyObject())).andDelegateTo(new Report() {

			@Override
			public boolean addColumn(final ReportColumn column) {
				assertEquals("The column name is set", columnName, column.getName());
				final Collection<ReportField> fields = column.getFields();
				assertEquals("There is one field in the column", 1, fields.size());
				final ReportField field = fields.iterator().next();
				assertEquals("The field is the voter detail", userFieldName.getSqlName(), field.getUserFieldName());
				final Collection<ReportAnswer> fieldAnswers = field.getAnswers();
				assertEquals("There is one answer", 1, fieldAnswers.size());
				final ReportAnswer fieldAnswer = fieldAnswers.iterator().next();
				assertEquals("The answer is selected", userFieldName.convertDisplayToDatabaseValue(answer), fieldAnswer.getAnswer());
				return true;
			}
		});
		replayAll();

		getReportingDashboardService().addUserDetailColumnToReport(report, columnName, userDetail, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addUserDetailColumnToReport(Report, String, String, String[])} for
	 * the case where no answers are provided.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testAddUserDetailColumnToReport_noAnswers() {
		final Report report = createMock("Report", Report.class);
		final String columnName = "Column Name";
		final UserFieldNames userFieldName = UserFieldNames.BIRTH_MONTH;
		final String userDetail = userFieldName.getUiName();
		final String[] answers = null;
		EasyMock.expect(report.addColumn((ReportColumn) EasyMock.anyObject())).andDelegateTo(new Report() {

			@Override
			public boolean addColumn(final ReportColumn column) {
				assertEquals("The column name is set", columnName, column.getName());
				final Collection<ReportField> fields = column.getFields();
				assertEquals("There is one field in the column", 1, fields.size());
				final ReportField field = fields.iterator().next();
				assertEquals("The field is the voter detail", userFieldName.getSqlName(), field.getUserFieldName());
				assertNull("There are no answers", field.getAnswers());
				return true;
			}
		});
		replayAll();

		getReportingDashboardService().addUserDetailColumnToReport(report, columnName, userDetail, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#addUserDetailColumnToReport(Report, String, String, String[])} for
	 * the case where the user detail cannot be found.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAddUserDetailColumnToReport_userDetailNotFound() {
		final Report report = createMock("Report", Report.class);
		final String columnName = "Column Name";
		final String userDetail = "Unknown Detail";
		final String[] answers = null;
		replayAll();

		getReportingDashboardService().addUserDetailColumnToReport(report, columnName, userDetail, answers);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#buildReportColumn(Map, String, String, String, String, Collection)}
	 * for the case where all of the answers are specified.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	@Test
	public final void testBuildReportColumn_allAnswers() {
		final String name = "Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final Collection<String> answers = Arrays.asList("Option1", "Option2");
		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final Map<String, Map<String, Collection<QuestionField>>> pageFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		reportableFields.put(page, pageFields);
		final Map<String, Collection<QuestionField>> groupFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageFields.put(group, groupFields);
		final QuestionField questionField = createMock("QuestionField", QuestionField.class);
		final Collection<QuestionField> questionFields = Arrays.asList(questionField);
		groupFields.put(question, questionFields);
		final FieldDictionaryItem option1 = createMock("Option1", FieldDictionaryItem.class);
		final FieldDictionaryItem option2 = createMock("Option2", FieldDictionaryItem.class);
		final Collection<FieldDictionaryItem> options = Arrays.asList(option1, option2);
		EasyMock.expect(questionField.getOptions()).andReturn(options).anyTimes();
		EasyMock.expect(option1.getViewValue()).andReturn("Option1").anyTimes();
		EasyMock.expect(option2.getViewValue()).andReturn("Option2").anyTimes();
		replayAll();

		final ReportColumn actualReportColumn = getReportingDashboardService().buildReportColumn(reportableFields, name, page,
				group, question, answers);

		assertNotNull("A report column is built", actualReportColumn);
		assertEquals("The name of the column is set", name, actualReportColumn.getName());
		final Collection<ReportField> actualReportFields = actualReportColumn.getFields();
		assertEquals("There is one report field", 1, actualReportFields.size());
		final ReportField actualReportField = actualReportFields.iterator().next();
		assertEquals("The question for the report field is set", questionField, actualReportField.getQuestion());
		assertNull("There are no answers for the report field", actualReportField.getAnswers());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#buildReportColumn(Map, String, String, String, String, Collection)}
	 * for the case where no answers are specified.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	@Test
	public final void testBuildReportColumn_noAnswers() {
		final String name = "Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final Collection<String> answers = new ArrayList<String>();
		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final Map<String, Map<String, Collection<QuestionField>>> pageFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		reportableFields.put(page, pageFields);
		final Map<String, Collection<QuestionField>> groupFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageFields.put(group, groupFields);
		final QuestionField questionField = createMock("QuestionField", QuestionField.class);
		final Collection<QuestionField> questionFields = Arrays.asList(questionField);
		groupFields.put(question, questionFields);
		final FieldDictionaryItem option1 = createMock("Option1", FieldDictionaryItem.class);
		final FieldDictionaryItem option2 = createMock("Option2", FieldDictionaryItem.class);
		final Collection<FieldDictionaryItem> options = Arrays.asList(option1, option2);
		EasyMock.expect(questionField.getOptions()).andReturn(options).anyTimes();
		EasyMock.expect(option1.getViewValue()).andReturn("Option1").anyTimes();
		EasyMock.expect(option2.getViewValue()).andReturn("Option2").anyTimes();
		replayAll();

		final ReportColumn actualReportColumn = getReportingDashboardService().buildReportColumn(reportableFields, name, page,
				group, question, answers);

		assertNotNull("A report column is built", actualReportColumn);
		assertEquals("The name of the column is set", name, actualReportColumn.getName());
		final Collection<ReportField> actualReportFields = actualReportColumn.getFields();
		assertEquals("There is one report field", 1, actualReportFields.size());
		final ReportField actualReportField = actualReportFields.iterator().next();
		assertEquals("The question for the report field is set", questionField, actualReportField.getQuestion());
		assertNull("There are no answers for the report field", actualReportField.getAnswers());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#buildReportColumn(Map, String, String, String, String, Collection)}
	 * for the case where some of the answers are specified.
	 * 
	 * @author IanBrown
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	@Test
	public final void testBuildReportColumn_someAnswers() {
		final String name = "Name";
		final String page = "Page";
		final String group = "Group";
		final String question = "Question";
		final Collection<String> answers = Arrays.asList("Option2");
		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final Map<String, Map<String, Collection<QuestionField>>> pageFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		reportableFields.put(page, pageFields);
		final Map<String, Collection<QuestionField>> groupFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageFields.put(group, groupFields);
		final QuestionField questionField = createMock("QuestionField", QuestionField.class);
		final Collection<QuestionField> questionFields = Arrays.asList(questionField);
		groupFields.put(question, questionFields);
		final FieldDictionaryItem option1 = createMock("Option1", FieldDictionaryItem.class);
		final FieldDictionaryItem option2 = createMock("Option2", FieldDictionaryItem.class);
		final Collection<FieldDictionaryItem> options = Arrays.asList(option1, option2);
		EasyMock.expect(questionField.getOptions()).andReturn(options).anyTimes();
		EasyMock.expect(option1.getViewValue()).andReturn("Option1").anyTimes();
		EasyMock.expect(option2.getViewValue()).andReturn("Option2").anyTimes();
		replayAll();

		final ReportColumn actualReportColumn = getReportingDashboardService().buildReportColumn(reportableFields, name, page,
				group, question, answers);

		assertNotNull("A report column is built", actualReportColumn);
		assertEquals("The name of the column is set", name, actualReportColumn.getName());
		final Collection<ReportField> actualReportFields = actualReportColumn.getFields();
		assertEquals("There is one report field", 1, actualReportFields.size());
		final ReportField actualReportField = actualReportFields.iterator().next();
		assertEquals("The question for the report field is set", questionField, actualReportField.getQuestion());
		final Collection<ReportAnswer> actualReportAnswers = actualReportField.getAnswers();
		assertEquals("There is one report answer", 1, actualReportAnswers.size());
		final ReportAnswer actualReportAnswer = actualReportAnswers.iterator().next();
		assertEquals("The predefined answer is set", option2, actualReportAnswer.getPredefinedAnswer());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#buildUserFields(Report, FaceConfig)} for the case
	 * where all flows and a single face is used.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testBuildUserFields_allFlowsAFace() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final Set<String> faces = new HashSet<String>(Arrays.asList("Face"));
		EasyMock.expect(report.getFaces()).andReturn(faces).anyTimes();
		final FaceConfig assignedFace = null;
		replayAll();

		final List<Map<String, Object>> actualUserFields = getReportingDashboardService().buildUserFields(report, assignedFace);

		assertUserFields(report, assignedFace, actualUserFields);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#buildUserFields(Report, FaceConfig)} for the case
	 * where all flows and faces are used.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testBuildUserFields_allFlowsAllFaces() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		final FaceConfig assignedFace = null;
		replayAll();

		final List<Map<String, Object>> actualUserFields = getReportingDashboardService().buildUserFields(report, assignedFace);

		assertUserFields(report, assignedFace, actualUserFields);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#buildUserFields(Report, FaceConfig)} for the case
	 * where all flows and an assigned face is used.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testBuildUserFields_allFlowsAssignedFace() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		replayAll();

		final List<Map<String, Object>> actualUserFields = getReportingDashboardService().buildUserFields(report, assignedFace);

		assertUserFields(report, assignedFace, actualUserFields);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#buildUserFields(Report, FaceConfig)} for the case
	 * where all flows and multiple faces are used.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testBuildUserFields_allFlowsMultipleFaces() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final Set<String> faces = new HashSet<String>(Arrays.asList("Face1", "Face2"));
		EasyMock.expect(report.getFaces()).andReturn(faces).anyTimes();
		final FaceConfig assignedFace = null;
		replayAll();

		final List<Map<String, Object>> actualUserFields = getReportingDashboardService().buildUserFields(report, assignedFace);

		assertUserFields(report, assignedFace, actualUserFields);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#buildUserFields(Report, FaceConfig)} for the case
	 * where a single flow and all faces are used.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testBuildUserFields_singleFlowAllFaces() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		final FaceConfig assignedFace = null;
		replayAll();

		final List<Map<String, Object>> actualUserFields = getReportingDashboardService().buildUserFields(report, assignedFace);

		assertUserFields(report, assignedFace, actualUserFields);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#convertReportableFieldsToPages(Map)}.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testConvertReportableFieldToPages() {
		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final String page = "Page";
		final Map<String, Map<String, Collection<QuestionField>>> pageReportableFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		reportableFields.put(page, pageReportableFields);
		final String group = "Group";
		final Map<String, Collection<QuestionField>> groupReportableFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageReportableFields.put(group, groupReportableFields);
		final String question = "Question";
		final QuestionField fieldNoAnswers = createMock("FieldNoAnswers", QuestionField.class);
		final QuestionField fieldPredefinedAnswer = createMock("FieldPredefinedAnswer", QuestionField.class);
		final Collection<QuestionField> questionReportableFields = Arrays.asList(fieldNoAnswers, fieldPredefinedAnswer);
		groupReportableFields.put(question, questionReportableFields);
		EasyMock.expect(fieldNoAnswers.getOptions()).andReturn(null).anyTimes();
		final FieldDictionaryItem predefinedAnswer = createMock("PredefinedAnswer", FieldDictionaryItem.class);
		final Collection<FieldDictionaryItem> predefinedAnswers = Arrays.asList(predefinedAnswer);
		EasyMock.expect(fieldPredefinedAnswer.getOptions()).andReturn(predefinedAnswers).anyTimes();
		final String viewValue = "View Value";
		EasyMock.expect(predefinedAnswer.getViewValue()).andReturn(viewValue).anyTimes();
		replayAll();

		final Map<String, Map<String, Map<String, List<String>>>> actualPages = getReportingDashboardService()
				.convertReportableFieldsToPages(reportableFields);

		assertEquals("There are the correct number of pages", 1, actualPages.size());
		final Map<String, Map<String, List<String>>> pageMap = actualPages.get(page);
		assertEquals(page + " has the correct number of groups", 1, pageMap.size());
		final Map<String, List<String>> groupMap = pageMap.get(group);
		assertEquals(group + " has the correct number of questions", 1, groupMap.size());
		final List<String> answerList = groupMap.get(question);
		assertEquals(question + " has the correct answers", Arrays.asList(viewValue), answerList);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#copyReport(Report, OverseasUser)}.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testCopyReport() {
		final Report report = createMock("Report", Report.class);
		final OverseasUser user = createMock("User", OverseasUser.class);
		final Report copy = createMock("Copy", Report.class);
		EasyMock.expect(report.deepCopy()).andReturn(copy);
		final String reportTitle = "Report Title";
		EasyMock.expect(report.getTitle()).andReturn(reportTitle);
		copy.setTitle((String) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new Report() {

			@Override
			public final void setTitle(final String title) {
				assertTrue("The title " + title + " contains the original title " + reportTitle, title.contains(reportTitle));
			}
		});
		copy.setStandard(false);
		copy.setOwner(user);
		replayAll();

		final Report actualCopy = getReportingDashboardService().copyReport(report, user);

		assertSame("The copied report is returned", copy, actualCopy);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#createAnswer()}.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testCreateAnswer() {
		final ReportAnswer actualAnswer = getReportingDashboardService().createAnswer();

		assertNotNull("An answer is created", actualAnswer);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#createColumn()}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testCreateColumn() {
		final ReportColumn actualColumn = getReportingDashboardService().createColumn();

		assertNotNull("A column is created", actualColumn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#createCustomReport(OverseasUser, FaceConfig)} for
	 * the case where an assigned face is provided.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testCreateCustomReport_assignedFace() {
		final OverseasUser owner = createMock("Owner", OverseasUser.class);
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		final String faceName = "face/vote";
		EasyMock.expect(assignedFace.getUrlPath()).andReturn(faceName);
		replayAll();

		final Report actualCustomReport = getReportingDashboardService().createCustomReport(owner, assignedFace);

		assertNotNull("A report is created", actualCustomReport);
		assertFalse("The report is a custom report", actualCustomReport.isStandard());
		assertSame("The owner of the report is set", owner, actualCustomReport.getOwner());
		assertTrue("Faces are applied", actualCustomReport.isApplyFaces());
		final Set<String> expectedFaces = new HashSet<String>(Arrays.asList(faceName));
		assertEquals("The faces are set", expectedFaces, actualCustomReport.getFaces());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#createCustomReport(OverseasUser, FaceConfig)} for
	 * the case where no assigned face is provided.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testCreateCustomReport_noAssignedFace() {
		final OverseasUser owner = createMock("Owner", OverseasUser.class);
		final FaceConfig assignedFace = null;
		replayAll();

		final Report actualCustomReport = getReportingDashboardService().createCustomReport(owner, assignedFace);

		assertNotNull("A report is created", actualCustomReport);
		assertFalse("The report is a custom report", actualCustomReport.isStandard());
		assertSame("The owner of the report is set", owner, actualCustomReport.getOwner());
		assertFalse("Faces are not applied", actualCustomReport.isApplyFaces());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#createField()}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 20, 2012
	 */
	@Test
	public final void testCreateField() {
		final ReportField actualField = getReportingDashboardService().createField();

		assertNotNull("A field is created", actualField);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#createReport()}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testCreateReport() {
		final Report actualReport = getReportingDashboardService().createReport();

		assertNotNull("A report is created", actualReport);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#createScheduledReport()}.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testCreateScheduledReport() {
		final ScheduledReport actualScheduledReport = getReportingDashboardService().createScheduledReport();

		assertNotNull("A scheduled report is created", actualScheduledReport);
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.service.ReportingDashboardService#deleteAnswer(ReportAnswer)}.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testDeleteAnswer() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		getReportingDashboardDAO().deleteAnswer(answer);
		replayAll();

		getReportingDashboardService().deleteAnswer(answer);

		verifyAll();
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.service.ReportingDashboardService#deleteColumn(ReportColumn)}.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testDeleteColumn() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		getReportingDashboardDAO().deleteColumn(column);
		replayAll();

		getReportingDashboardService().deleteColumn(column);

		verifyAll();
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.service.ReportingDashboardService#deleteField(ReportField)}.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testDeleteField() {
		final ReportField field = createMock("Field", ReportField.class);
		getReportingDashboardDAO().deleteField(field);
		replayAll();

		getReportingDashboardService().deleteField(field);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#deleteReport(Report)}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testDeleteReport() {
		final Report report = createMock("Report", Report.class);
		getReportingDashboardDAO().deleteReport(report);
		replayAll();

		getReportingDashboardService().deleteReport(report);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#deleteScheduledReport(ScheduledReport)}.
	 * 
	 * @author IanBrown
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@Test
	public final void testDeleteScheduledReport() {
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		getReportingDashboardDAO().deleteScheduledReport(scheduledReport);
		replayAll();

		getReportingDashboardService().deleteScheduledReport(scheduledReport);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#extractColumnHeaders(Report)}.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testExtractColumnHeaders() {
		final Report report = createMock("Report", Report.class);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final String name = "Column Name";
		EasyMock.expect(column.getName()).andReturn(name);
		replayAll();

		final List<String> actualColumnHeaders = getReportingDashboardService().extractColumnHeaders(report);

		assertNotNull("A list of column headers is returned", actualColumnHeaders);
		final List<String> expectedColumnHeaders = Arrays.asList(name);
		assertEquals("The column headers are correct", expectedColumnHeaders, actualColumnHeaders);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#extractColumnHeaders(Report)} for the case where
	 * the report has no columns.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testExtractColumnHeaders_noColumns() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.getColumns()).andReturn(null);
		replayAll();

		final List<String> actualColumnHeaders = getReportingDashboardService().extractColumnHeaders(report);

		assertNotNull("A list of column headers is returned", actualColumnHeaders);
		assertTrue("There are no column headers", actualColumnHeaders.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#extractColumnHeaders(Report)} for the case where no
	 * report is provided.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testExtractColumnHeaders_noReport() {
		getReportingDashboardService().extractColumnHeaders(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findAllScheduledReports()}.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testFindAllScheduledReports() {
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(getReportingDashboardDAO().findAllScheduledReports()).andReturn(scheduledReports);
		replayAll();

		final List<ScheduledReport> actualScheduledReports = getReportingDashboardService().findAllScheduledReports();

		assertSame("The scheduled reports are returned", scheduledReports, actualScheduledReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findAnswerById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testFindAnswerById() {
		final long id = 272l;
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		EasyMock.expect(getReportingDashboardDAO().findAnswerById(id)).andReturn(answer);
		replayAll();

		final ReportAnswer actualAnswer = getReportingDashboardService().findAnswerById(id);

		assertSame("The answer is returned", answer, actualAnswer);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findColumnById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testFindColumnById() {
		final long id = 7522l;
		final ReportColumn column = createMock("Column", ReportColumn.class);
		EasyMock.expect(getReportingDashboardDAO().findColumnById(id)).andReturn(column);
		replayAll();

		final ReportColumn actualColumn = getReportingDashboardService().findColumnById(id);

		assertSame("The column is returned", column, actualColumn);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#findCustomReports(com.bearcode.ovf.model.common.OverseasUser, FaceConfig)}
	 * for the case where no user is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testFindCustomReport_noUser() {
		replayAll();

		final List<Report> actualCustomReports = getReportingDashboardService().findCustomReports(null, null);

		assertTrue("No custom reports are returned", actualCustomReports.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#findCustomReports(com.bearcode.ovf.model.common.OverseasUser, FaceConfig)}
	 * for the case where an administrative user and a face is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testFindCustomReports_administrativeFace() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true);
		final FaceConfig currentFace = createMock("CurrentFace", FaceConfig.class);
		final Report userReport = createMock("UserReport", Report.class);
		final List<Report> reports = Arrays.asList(userReport);
		EasyMock.expect(getReportingDashboardDAO().findFaceReports(currentFace)).andReturn(reports);
		EasyMock.expect(getReportingDashboardDAO().findUserReports(user)).andReturn(new ArrayList<Report>());
		replayAll();

		final List<Report> actualCustomReports = getReportingDashboardService().findCustomReports(user, currentFace);

		assertEquals("The reports are returned", reports, actualCustomReports);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#findCustomReports(com.bearcode.ovf.model.common.OverseasUser, FaceConfig)}
	 * for the case where an administrative user and no face is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testFindCustomReports_administrativeNoFace() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true);
		final Report standardReport = createMock("StandardReport", Report.class);
		EasyMock.expect(standardReport.isStandard()).andReturn(true);
		final Report userReport = createMock("UserReport", Report.class);
		EasyMock.expect(userReport.isStandard()).andReturn(false);
		final List<Report> reports = Arrays.asList(standardReport, userReport);
		EasyMock.expect(getReportingDashboardDAO().findAllReports()).andReturn(reports);
		replayAll();

		final List<Report> actualCustomReports = getReportingDashboardService().findCustomReports(user, null);

		final List<Report> expectedCustomReports = Arrays.asList(userReport);
		assertEquals("The user reports are returned", expectedCustomReports, actualCustomReports);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#findCustomReports(com.bearcode.ovf.model.common.OverseasUser, FaceConfig)}
	 * for the case where a non-administrative user is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version Jan 28, 2012
	 */
	@Test
	public final void testFindCustomReports_nonAdministrative() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false);
		final Report userReport = createMock("UserReport", Report.class);
		final List<Report> customReports = Arrays.asList(userReport);
		EasyMock.expect(getReportingDashboardDAO().findUserReports(user)).andReturn(customReports);
		replayAll();

		final List<Report> actualCustomReports = getReportingDashboardService().findCustomReports(user, null);

		assertEquals("The custom reports are returned", customReports, actualCustomReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findFieldById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testFindFieldById() {
		final long id = 2367l;
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(getReportingDashboardDAO().findFieldById(id)).andReturn(field);
		replayAll();

		final ReportField actualField = getReportingDashboardService().findFieldById(id);

		assertSame("The field is returned", field, actualField);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findReportById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testFindReportById() {
		final long id = 91266l;
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getReportingDashboardDAO().findReportById(id)).andReturn(report);
		replayAll();

		final Report actualReport = getReportingDashboardService().findReportById(id);

		assertSame("The report is returned", report, actualReport);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findScheduledReportById(long)}.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testFindScheduledReportById() {
		final long id = 12892l;
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		EasyMock.expect(getReportingDashboardDAO().findScheduledReportById(id)).andReturn(scheduledReport);
		replayAll();

		final ScheduledReport actualScheduledReport = getReportingDashboardService().findScheduledReportById(id);

		assertSame("The scheduled report is returned", scheduledReport, actualScheduledReport);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findScheduledReports(OverseasUser)}.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testFindScheduledReports() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(getReportingDashboardDAO().findScheduledReports(user)).andReturn(scheduledReports);
		replayAll();

		final List<ScheduledReport> actualScheduledReports = getReportingDashboardService().findScheduledReports(user);

		assertSame("The scheduled reports are returned", scheduledReports, actualScheduledReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findScheduledReports(OverseasUser)} for the case
	 * where no overseas user is provided.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testFindScheduledReports_noUser() {
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(getReportingDashboardDAO().findAllScheduledReports()).andReturn(scheduledReports);
		replayAll();

		final Collection<ScheduledReport> actualScheduledReports = getReportingDashboardService().findScheduledReports(null);

		assertSame("The scheduled reports are returned", scheduledReports, actualScheduledReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findScheduledReportsDue()}.
	 * 
	 * @author IanBrown
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@Test
	public final void testFindScheduledReportsDue() {
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(getReportingDashboardDAO().findScheduledReportsDue()).andReturn(scheduledReports);
		replayAll();

		final List<ScheduledReport> actualScheduledReports = getReportingDashboardService().findScheduledReportsDue();

		assertSame("The scheduled reports are returned", scheduledReports, actualScheduledReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#findStandardReports()}.
	 * 
	 * @author IanBrown
	 * @since Feb 29, 2012
	 * @version Feb 29, 2012
	 */
	@Test
	public final void testFindStandardReports() {
		final Map<String, Report> standardReports = new HashMap<String, Report>();
		standardReports.put("StandardReport", createMock("StandardReport", Report.class));
		EasyMock.expect(getReportingDashboardDAO().findStandardReports()).andReturn(standardReports);
		replayAll();

		final Map<String, Report> actualStandardReports = getReportingDashboardService().findStandardReports();

		assertSame("The standard reports are returned", standardReports, actualStandardReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#flush()}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testFlush() {
		getReportingDashboardDAO().flush();
		replayAll();

		getReportingDashboardService().flush();

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#getQuestionFieldService()}.
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	@Test
	public final void testGetQuestionFieldService() {
		final QuestionFieldService actualQuestionFieldService = getReportingDashboardService().getQuestionFieldService();

		assertSame("The question field service is set", getQuestionFieldService(), actualQuestionFieldService);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#getReportingDashboardDAO()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 4, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetReportingDashboardDAO() {
		final ReportingDashboardDAO actualReportingDashboardDAO = getReportingDashboardService().getReportingDashboardDAO();

		assertSame("The reporting dashboard DAO is set", getReportingDashboardDAO(), actualReportingDashboardDAO);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#pagesToReportableFields(Report, WizardContext, FlowType[], List)}
	 * for the case where there are no pages.
	 * 
	 * @author IanBrown
	 * @since Feb 29, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testPagesToReportableFields_noPages() {
		final Report report = createMock("Report", Report.class);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final FlowType[] flows = FlowType.values();

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> actualReportableFields = getReportingDashboardService()
				.pagesToReportableFields(report, wizardContext, flows, null);

		assertNull("There are no reportable fields", actualReportableFields);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#pagesToReportableFields(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a single group, with a single variant, and a single question.
	 * 
	 * @author IanBrown
	 * @since Feb 29, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testPagesToReportableFields_singleQuestion() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final List<QuestionnairePage> pages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.OVERSEAS).anyTimes();
		final Question group = createMock("Group", Question.class);
		final String pageTitle = "Page Title";
		EasyMock.expect(page.getTitle()).andReturn(pageTitle);
		final List<Question> groups = Arrays.asList(group);
		EasyMock.expect(page.getQuestions()).andReturn(groups).atLeastOnce();
		final String groupTitle = "Group Title";
		EasyMock.expect(group.getName()).andReturn(groupTitle);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(group.getVariants()).andReturn(variants).atLeastOnce();
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null);
		final QuestionField question = createMock("Question", QuestionField.class);
		final Collection<QuestionField> questions = Arrays.asList(question);
		EasyMock.expect(variant.getFields()).andReturn(questions).atLeastOnce();
		final String questionTitle = "Question Title";
		EasyMock.expect(question.getTitle()).andReturn(questionTitle);
		final FieldType questionType = createMock("QuestionType", FieldType.class);
		EasyMock.expect(question.getType()).andReturn(questionType);
		EasyMock.expect(questionType.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(question.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> actualReportableFields = getReportingDashboardService()
				.pagesToReportableFields(report, wizardContext, flows, pages);

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> expectedReportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final Map<String, Map<String, Collection<QuestionField>>> pageReportableFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		expectedReportableFields.put(pageTitle, pageReportableFields);
		final Map<String, Collection<QuestionField>> groupReportableFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageReportableFields.put(groupTitle, groupReportableFields);
		final Collection<QuestionField> questionReportableFields = Arrays.asList(question);
		groupReportableFields.put(questionTitle, questionReportableFields);
		assertEquals("The reportable fields are correct", expectedReportableFields, actualReportableFields);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#pagesToReportableFields(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a single group, with two variants each with a single question with PDF variables that
	 * match.
	 * 
	 * @author IanBrown
	 * @since Feb 29, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testPagesToReportableFields_variantsMatchingPdf() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final List<QuestionnairePage> pages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final String pageTitle = "Page Title";
		EasyMock.expect(page.getTitle()).andReturn(pageTitle);
		final List<Question> groups = Arrays.asList(group);
		EasyMock.expect(page.getQuestions()).andReturn(groups).atLeastOnce();
		final String groupTitle = "Group Title";
		EasyMock.expect(group.getName()).andReturn(groupTitle);
		final QuestionVariant variant1 = createMock("Variant1", QuestionVariant.class);
		final QuestionVariant variant2 = createMock("Variant2", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant1, variant2);
		EasyMock.expect(group.getVariants()).andReturn(variants).atLeastOnce();
		EasyMock.expect(variant1.getKeys()).andReturn(null);
		EasyMock.expect(variant1.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(variant2.getKeys()).andReturn(null);
		EasyMock.expect(variant2.isDefault()).andReturn(false).anyTimes();
		final QuestionField question1 = createMock("Question1", QuestionField.class);
		final Collection<QuestionField> questions1 = Arrays.asList(question1);
		EasyMock.expect(variant1.getFields()).andReturn(questions1).atLeastOnce();
		EasyMock.expect(question1.getInPdfName()).andReturn("PDF");
		EasyMock.expect(question1.getId()).andReturn(1l).anyTimes();
		final String question1Title = "Question One Title";
		EasyMock.expect(question1.getTitle()).andReturn(question1Title).anyTimes();
		final QuestionField question2 = createMock("Question2", QuestionField.class);
		final Collection<QuestionField> questions2 = Arrays.asList(question2);
		EasyMock.expect(variant2.getFields()).andReturn(questions2).atLeastOnce();
		EasyMock.expect(question2.getInPdfName()).andReturn("PDF");
		final FieldType questionType1 = createMock("QuestionType2", FieldType.class);
		EasyMock.expect(question2.getId()).andReturn(2l).anyTimes();
		final String question2Title = "Question Two Title";
		EasyMock.expect(question2.getTitle()).andReturn(question2Title);
		EasyMock.expect(question1.getType()).andReturn(questionType1);
		EasyMock.expect(questionType1.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[1]);
		EasyMock.expect(question1.getFieldDependencies()).andReturn(null);
		final FieldType questionType2 = createMock("QuestionType2", FieldType.class);
		EasyMock.expect(question2.getType()).andReturn(questionType2);
		EasyMock.expect(questionType2.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[2]);
		EasyMock.expect(question2.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> actualReportableFields = getReportingDashboardService()
				.pagesToReportableFields(report, wizardContext, flows, pages);

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> expectedReportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final Map<String, Map<String, Collection<QuestionField>>> pageReportableFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		expectedReportableFields.put(pageTitle, pageReportableFields);
		final Map<String, Collection<QuestionField>> groupReportableFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageReportableFields.put(groupTitle, groupReportableFields);
		final Collection<QuestionField> questionReportableFields = Arrays.asList(question1, question2);
		groupReportableFields.put(question1Title, questionReportableFields);
		assertEquals("The reportable fields are correct", expectedReportableFields, actualReportableFields);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#pagesToReportableFields(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a single group, with two variants with questions without PDFs and one contains
	 * multiple questions.
	 * 
	 * @author IanBrown
	 * @since Feb 29, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testPagesToReportableFields_variantsMultiplesWithoutPdf() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final List<QuestionnairePage> pages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.OVERSEAS).anyTimes();
		final Question group = createMock("Group", Question.class);
		final String pageTitle = "Page Title";
		EasyMock.expect(page.getTitle()).andReturn(pageTitle);
		final List<Question> groups = Arrays.asList(group);
		EasyMock.expect(page.getQuestions()).andReturn(groups).atLeastOnce();
		final String groupTitle = "Group Title";
		EasyMock.expect(group.getName()).andReturn(groupTitle);
		final QuestionVariant variant1 = createMock("Variant1", QuestionVariant.class);
		final QuestionVariant variant2 = createMock("Variant2", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant1, variant2);
		EasyMock.expect(group.getVariants()).andReturn(variants).atLeastOnce();
		EasyMock.expect(variant1.getKeys()).andReturn(null);
		EasyMock.expect(variant1.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(variant2.getKeys()).andReturn(null);
		EasyMock.expect(variant2.isDefault()).andReturn(false).anyTimes();
		final QuestionField question1 = createMock("Question1", QuestionField.class);
		final Collection<QuestionField> questions1 = Arrays.asList(question1);
		EasyMock.expect(variant1.getFields()).andReturn(questions1).atLeastOnce();
		final String question1Title = "Question One Title";
		EasyMock.expect(question1.getInPdfName()).andReturn(null).atLeastOnce();
		EasyMock.expect(question1.getId()).andReturn(1l).anyTimes();
		EasyMock.expect(question1.getQuestion()).andReturn(variant1).atLeastOnce();
		EasyMock.expect(question1.getTitle()).andReturn(question1Title).atLeastOnce();
		final FieldType questionType1 = createMock("QuestionType1", FieldType.class);
		EasyMock.expect(question1.getType()).andReturn(questionType1);
		EasyMock.expect(questionType1.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(question1.getFieldDependencies()).andReturn(null);
		final QuestionField question2 = createMock("Question2", QuestionField.class);
		final QuestionField question3 = createMock("Question3", QuestionField.class);
		final Collection<QuestionField> questions2 = Arrays.asList(question2, question3);
		EasyMock.expect(variant2.getFields()).andReturn(questions2).atLeastOnce();
		EasyMock.expect(question2.getInPdfName()).andReturn(null).atLeastOnce();
		EasyMock.expect(question2.getQuestion()).andReturn(variant2).atLeastOnce();
		EasyMock.expect(question3.getQuestion()).andReturn(variant2).atLeastOnce();
		EasyMock.expect(question3.getInPdfName()).andReturn(null).atLeastOnce();
		EasyMock.expect(question2.getId()).andReturn(2l).anyTimes();
		final String question2Title = "Question Two Title";
		EasyMock.expect(question2.getTitle()).andReturn(question2Title).atLeastOnce();
		final FieldType questionType2 = createMock("QuestionType2", FieldType.class);
		EasyMock.expect(question2.getType()).andReturn(questionType2);
		EasyMock.expect(questionType2.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[1]);
		EasyMock.expect(question2.getFieldDependencies()).andReturn(null);
		final String question3Title = "Question Three Title";
		EasyMock.expect(question3.getTitle()).andReturn(question3Title).atLeastOnce();
		final FieldType questionType3 = createMock("QuestionType3", FieldType.class);
		EasyMock.expect(question3.getType()).andReturn(questionType3);
		EasyMock.expect(questionType3.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[2]);
		EasyMock.expect(question3.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> actualReportableFields = getReportingDashboardService()
				.pagesToReportableFields(report, wizardContext, flows, pages);

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> expectedReportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final Map<String, Map<String, Collection<QuestionField>>> pageReportableFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		expectedReportableFields.put(pageTitle, pageReportableFields);
		final Map<String, Collection<QuestionField>> groupReportableFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageReportableFields.put(groupTitle, groupReportableFields);
		final Collection<QuestionField> question1ReportableFields = Arrays.asList(question1);
		groupReportableFields.put(question1Title, question1ReportableFields);
		final Collection<QuestionField> question2ReportableFields = Arrays.asList(question2);
		groupReportableFields.put(question2Title, question2ReportableFields);
		final Collection<QuestionField> question3ReportableFields = Arrays.asList(question3);
		groupReportableFields.put(question3Title, question3ReportableFields);
		assertEquals("The reportable fields are correct", expectedReportableFields, actualReportableFields);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#pagesToReportableFields(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a single group, with two variants each with a single question with PDF variables that
	 * don't match.
	 * 
	 * @author IanBrown
	 * @since Feb 29, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testPagesToReportableFields_variantsNonmatchingPdf() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final List<QuestionnairePage> pages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final String pageTitle = "Page Title";
		EasyMock.expect(page.getTitle()).andReturn(pageTitle);
		final List<Question> groups = Arrays.asList(group);
		EasyMock.expect(page.getQuestions()).andReturn(groups).atLeastOnce();
		final String groupTitle = "Group Title";
		EasyMock.expect(group.getName()).andReturn(groupTitle);
		final QuestionVariant variant1 = createMock("Variant1", QuestionVariant.class);
		final QuestionVariant variant2 = createMock("Variant2", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant1, variant2);
		EasyMock.expect(group.getVariants()).andReturn(variants).atLeastOnce();
		EasyMock.expect(variant1.getKeys()).andReturn(null);
		EasyMock.expect(variant1.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(variant2.getKeys()).andReturn(null);
		EasyMock.expect(variant2.isDefault()).andReturn(false).anyTimes();
		final QuestionField question1 = createMock("Question1", QuestionField.class);
		final Collection<QuestionField> questions1 = Arrays.asList(question1);
		EasyMock.expect(variant1.getFields()).andReturn(questions1).atLeastOnce();
		EasyMock.expect(question1.getInPdfName()).andReturn("PDF1");
		final String question1Title = "Question One Title";
		EasyMock.expect(question1.getTitle()).andReturn(question1Title);
		final FieldType questionType1 = createMock("QuestionType1", FieldType.class);
		EasyMock.expect(question1.getType()).andReturn(questionType1);
		EasyMock.expect(questionType1.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(question1.getFieldDependencies()).andReturn(null);
		final QuestionField question2 = createMock("Question2", QuestionField.class);
		final Collection<QuestionField> questions2 = Arrays.asList(question2);
		EasyMock.expect(variant2.getFields()).andReturn(questions2).atLeastOnce();
		EasyMock.expect(question2.getInPdfName()).andReturn("PDF2");
		final String question2Title = "Question Two Title";
		EasyMock.expect(question2.getTitle()).andReturn(question2Title);
		final FieldType questionType2 = createMock("QuestionType2", FieldType.class);
		EasyMock.expect(question2.getType()).andReturn(questionType2);
		EasyMock.expect(questionType2.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[2]);
		EasyMock.expect(question2.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> actualReportableFields = getReportingDashboardService()
				.pagesToReportableFields(report, wizardContext, flows, pages);

		final Map<String, Map<String, Map<String, Collection<QuestionField>>>> expectedReportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
		final Map<String, Map<String, Collection<QuestionField>>> pageReportableFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
		expectedReportableFields.put(pageTitle, pageReportableFields);
		final Map<String, Collection<QuestionField>> groupReportableFields = new LinkedHashMap<String, Collection<QuestionField>>();
		pageReportableFields.put(groupTitle, groupReportableFields);
		final Collection<QuestionField> question1ReportableFields = Arrays.asList(question1);
		groupReportableFields.put(question1Title, question1ReportableFields);
		final Collection<QuestionField> question2ReportableFields = Arrays.asList(question2);
		groupReportableFields.put(question2Title, question2ReportableFields);
		assertEquals("The reportable fields are correct", expectedReportableFields, actualReportableFields);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. The field has a field dependency that is
	 * reportable as well.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_fieldDependency() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.OVERSEAS).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		EasyMock.expect(variant.getKeys()).andReturn(null);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		final FieldDependency fieldDependency = createMock("FieldDependency", FieldDependency.class);
		final Collection<FieldDependency> fieldDependencies = Arrays.asList(fieldDependency);
		EasyMock.expect(field.getFieldDependencies()).andReturn(fieldDependencies);
		final Question dependsOn = createMock("DependsOn", Question.class);
		EasyMock.expect(fieldDependency.getDependsOn()).andReturn(dependsOn);
		final QuestionVariant dependsOnVariant = createMock("DependsOnVariant", QuestionVariant.class);
		final Collection<QuestionVariant> dependsOnVariants = Arrays.asList(dependsOnVariant);
		EasyMock.expect(dependsOn.getVariants()).andReturn(dependsOnVariants);
		EasyMock.expect(dependsOnVariant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(dependsOnVariant.getKeys()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertEquals("Nothing is removed from the questionnaire pages", questionnairePages, actualReducedPages);
		assertFalse("No group is removed from the page", groups.isEmpty());
		assertFalse("No variant is removed from the group", variants.isEmpty());
		assertFalse("No field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a variant with no fields.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_noFields() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		EasyMock.expect(variant.getKeys()).andReturn(null);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(variant.getFields()).andReturn(new ArrayList<QuestionField>());
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", actualReducedPages.isEmpty());
		assertTrue("The group is removed from the page", groups.isEmpty());
		assertTrue("The variant is removed from the group", variants.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with no groups (questions).
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_noGroups() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		EasyMock.expect(page.getQuestions()).andReturn(new ArrayList<Question>());
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", actualReducedPages.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there are no pages.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version May 17, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_noPages() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final List<QuestionnairePage> questionnairePages = new ArrayList<QuestionnairePage>();
		final FlowType[] flows = FlowType.values();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", (actualReducedPages == null) || actualReducedPages.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is not reportable.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_noReportableFields() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		EasyMock.expect(variant.getKeys()).andReturn(null);
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn("Not reportable");
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", actualReducedPages.isEmpty());
		assertTrue("The group is removed from the page", groups.isEmpty());
		assertTrue("The variant is removed from the group", variants.isEmpty());
		assertTrue("The field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with no variants.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_noVariants() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		EasyMock.expect(group.getVariants()).andReturn(new ArrayList<QuestionVariant>());
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", actualReducedPages.isEmpty());
		assertTrue("The group is removed from the page", groups.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. The field has a field dependency that is not
	 * reportable.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reducedFieldDependency() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		EasyMock.expect(variant.getKeys()).andReturn(null);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		final FieldDependency fieldDependency = createMock("FieldDependency", FieldDependency.class);
		final Collection<FieldDependency> fieldDependencies = Arrays.asList(fieldDependency);
		EasyMock.expect(field.getFieldDependencies()).andReturn(fieldDependencies);
		final Question dependsOn = createMock("DependsOn", Question.class);
		EasyMock.expect(fieldDependency.getDependsOn()).andReturn(dependsOn);
		final QuestionVariant dependsOnVariant = createMock("DependsOnVariant", QuestionVariant.class);
		final Collection<QuestionVariant> dependsOnVariants = Arrays.asList(dependsOnVariant);
		EasyMock.expect(dependsOn.getVariants()).andReturn(dependsOnVariants);
		final FlowDependency dependsOnFlowDependency = createMock("DependsOnFlowDependency", FlowDependency.class);
		final Collection<BasicDependency> dependsOnDependencies = Arrays.asList((BasicDependency) dependsOnFlowDependency);
		EasyMock.expect(dependsOnVariant.getKeys()).andReturn(dependsOnDependencies);
		EasyMock.expect(dependsOnVariant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(dependsOnFlowDependency.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(dependsOnFlowDependency.checkDependency(wizardContext)).andReturn(false);
		EasyMock.expect(dependsOnFlowDependency.checkGroup(null)).andReturn(false).anyTimes();
		EasyMock.expect(dependsOnFlowDependency.getDependsOn()).andReturn(null).anyTimes();
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", actualReducedPages.isEmpty());
		assertTrue("The group is removed from the page", groups.isEmpty());
		assertTrue("The variant is removed from the group", variants.isEmpty());
		assertTrue("The field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reportableFields() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		EasyMock.expect(variant.getKeys()).andReturn(null);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(field.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertEquals("Nothing is removed from the questionnaire pages", questionnairePages, actualReducedPages);
		assertFalse("No group is removed from the page", groups.isEmpty());
		assertFalse("No variant is removed from the group", variants.isEmpty());
		assertFalse("No field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. There is an assigned face, but no
	 * dependencies.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reportableFieldsAssignedFace() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		final OverseasUser overseasUser = new OverseasUser();
		overseasUser.setAssignedFace(assignedFace);
		wizardResults.setUser(overseasUser);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		EasyMock.expect(variant.getKeys()).andReturn(null);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(field.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertEquals("Nothing is removed from the questionnaire pages", questionnairePages, actualReducedPages);
		assertFalse("No group is removed from the page", groups.isEmpty());
		assertFalse("No variant is removed from the group", variants.isEmpty());
		assertFalse("No field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. There is an assigned face and the variant is
	 * dependent on that face.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reportableFieldsCorrectFace() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		final OverseasUser overseasUser = new OverseasUser();
		overseasUser.setAssignedFace(assignedFace);
		wizardResults.setUser(overseasUser);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		final FaceDependency faceDependency = createMock("FaceDependency", FaceDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) faceDependency);
		EasyMock.expect(variant.getKeys()).andReturn(keys);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(faceDependency.checkDependency(wizardContext)).andReturn(true);
		EasyMock.expect(faceDependency.checkGroup(null)).andReturn(false).anyTimes();
		EasyMock.expect(faceDependency.getDependsOn()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(field.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertEquals("Nothing is removed from the questionnaire pages", questionnairePages, actualReducedPages);
		assertFalse("No group is removed from the page", groups.isEmpty());
		assertFalse("No variant is removed from the group", variants.isEmpty());
		assertFalse("No field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. The variant belongs to the flow.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reportableFieldsCorrectFlow() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		final FlowDependency flowDependency = createMock("FlowDependency", FlowDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) flowDependency);
		EasyMock.expect(variant.getKeys()).andReturn(keys);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(flowDependency.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(flowDependency.checkDependency(wizardContext)).andReturn(true);
		EasyMock.expect(flowDependency.checkGroup(null)).andReturn(true).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(field.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertEquals("Nothing is removed from the questionnaire pages", questionnairePages, actualReducedPages);
		assertFalse("No group is removed from the page", groups.isEmpty());
		assertFalse("No variant is removed from the group", variants.isEmpty());
		assertFalse("No field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. The variant is flow independent.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reportableFieldsFlowIndependent() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList(dependency);
		EasyMock.expect(variant.getKeys()).andReturn(keys);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(dependency.checkDependency(wizardContext)).andReturn(true);
		EasyMock.expect(dependency.checkGroup(null)).andReturn(true).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(field.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertEquals("Nothing is removed from the questionnaire pages", questionnairePages, actualReducedPages);
		assertFalse("No group is removed from the page", groups.isEmpty());
		assertFalse("No variant is removed from the group", variants.isEmpty());
		assertFalse("No field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. There is an assigned face, but no face
	 * dependencies.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reportableFieldsNoFaceDependencies() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		final OverseasUser overseasUser = new OverseasUser();
		overseasUser.setAssignedFace(assignedFace);
		wizardResults.setUser(overseasUser);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList(dependency);
		EasyMock.expect(variant.getKeys()).andReturn(keys);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(dependency.checkDependency(wizardContext)).andReturn(true);
		EasyMock.expect(dependency.checkGroup(null)).andReturn(true).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = new ArrayList<QuestionField>(Arrays.asList(field));
		EasyMock.expect(variant.getFields()).andReturn(fields);
		final FieldType type = createMock("Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type);
		EasyMock.expect(type.getTemplateName()).andReturn(ReportingDashboardService.REPORTABLE_TEMPLATES[0]);
		EasyMock.expect(field.getFieldDependencies()).andReturn(null);
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertEquals("Nothing is removed from the questionnaire pages", questionnairePages, actualReducedPages);
		assertFalse("No group is removed from the page", groups.isEmpty());
		assertFalse("No variant is removed from the group", variants.isEmpty());
		assertFalse("No field is removed from the variant", fields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a field that is reportable. There is an assigned face, but the variant
	 * is dependent on a different face.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_reportableFieldsWrongFace() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		final OverseasUser overseasUser = new OverseasUser();
		overseasUser.setAssignedFace(assignedFace);
		wizardResults.setUser(overseasUser);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		final FaceDependency faceDependency = createMock("FaceDependency", FaceDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) faceDependency);
		EasyMock.expect(variant.getKeys()).andReturn(keys);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(faceDependency.checkDependency(wizardContext)).andReturn(false);
		EasyMock.expect(faceDependency.checkGroup(null)).andReturn(true).anyTimes();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceDependency.getDependsOn()).andReturn(faceConfig).anyTimes();
		EasyMock.expect(faceConfig.getUrlPath()).andReturn("Face").anyTimes();
		final FlowType[] flows = FlowType.values();
		EasyMock.expect(report.getFaces()).andReturn(new HashSet<String>(Arrays.asList("Different Face"))).anyTimes();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", actualReducedPages.isEmpty());
		assertTrue("The group is removed from the page", groups.isEmpty());
		assertTrue("The variant is removed from the group", variants.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#reduceQuestionnairePagesForReporting(Report, WizardContext, FlowType[], List)}
	 * for the case where there is a page with a group with a variant that does not belong to the flow.
	 * 
	 * @author IanBrown
	 * @since Mar 15, 2012
	 * @version Mar 30, 2012
	 */
	@Test
	public final void testReduceQuestionnairePagesForReporting_wrongFlow() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.isApplyFlow()).andReturn(true).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage page = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(page);
		EasyMock.expect(page.getType()).andReturn(PageType.DOMESTIC_REGISTRATION).anyTimes();
		final Question group = createMock("Group", Question.class);
		final List<Question> groups = new ArrayList<Question>(Arrays.asList(group));
		EasyMock.expect(page.getQuestions()).andReturn(groups);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = new ArrayList<QuestionVariant>(Arrays.asList(variant));
		EasyMock.expect(group.getVariants()).andReturn(variants);
		final FlowDependency flowDependency = createMock("FlowDependency", FlowDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) flowDependency);
		EasyMock.expect(variant.getKeys()).andReturn(keys);
		EasyMock.expect(variant.isDefault()).andReturn(false).anyTimes();
		EasyMock.expect(flowDependency.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(flowDependency.checkDependency(wizardContext)).andReturn(false);
		EasyMock.expect(flowDependency.checkGroup(null)).andReturn(true).anyTimes();
		final FlowType[] flows = FlowType.values();
		replayAll();

		final List<QuestionnairePage> actualReducedPages = getReportingDashboardService().reduceQuestionnairePagesForReporting(
				report, wizardContext, flows, questionnairePages);

		assertTrue("No pages are returned", actualReducedPages.isEmpty());
		assertTrue("The group is removed from the page", groups.isEmpty());
		assertTrue("The variant is removed from the group", variants.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where
	 * faces are applied to the report.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testReport_applyFaces() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final ReportColumn faceColumn = createMock("FaceColumn", ReportColumn.class);
		final ReportColumn numberColumn = createMock("NumberColumn", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(faceColumn, numberColumn);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final long number = 24l;
		final String face = "face";
		final Object[] resultRow = { number, face };
		final List reportResult = Arrays.asList(new Object[] { resultRow });
		EasyMock.expect(getReportingDashboardDAO().report(report, startDate, endDate)).andReturn(reportResult);
		EasyMock.expect(faceColumn.getName()).andReturn("Hosted Site").anyTimes();
		EasyMock.expect(faceColumn.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(faceColumn.isUserFieldColumn()).andReturn(true).anyTimes();
		final ReportField faceField = createMock("FaceField", ReportField.class);
		final Collection<ReportField> faceFields = Arrays.asList(faceField);
		EasyMock.expect(faceColumn.getFields()).andReturn(faceFields).anyTimes();
		EasyMock.expect(faceField.getUserFieldName()).andReturn("face_name").anyTimes();
		EasyMock.expect(numberColumn.getName()).andReturn("Number").anyTimes();
		replayAll();

		final List<List<String>> actualReportRows = getReportingDashboardService().report(report, startDate, endDate);

		assertEquals("There are the correct number of rows in the report", 1, actualReportRows.size());
		final List<String> actualReportRow = actualReportRows.get(0);
		final List<String> expectedReportRow = Arrays.asList(face, Long.toString(number));
		assertEquals("The row is correct", expectedReportRow, actualReportRow);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where the
	 * flow is applied to the report.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testReport_applyFlow() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final ReportColumn flowColumn = createMock("FlowColumn", ReportColumn.class);
		final ReportColumn dataColumn = createMock("NumberColumn", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(flowColumn, dataColumn);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final long number = 24l;
		final Object[] resultRow = { number, FlowType.RAVA.name() };
		final List reportResult = Arrays.asList(new Object[] { resultRow });
		EasyMock.expect(getReportingDashboardDAO().report(report, startDate, endDate)).andReturn(reportResult);
		EasyMock.expect(flowColumn.getName()).andReturn("Flow").anyTimes();
		EasyMock.expect(flowColumn.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(flowColumn.isUserFieldColumn()).andReturn(true).anyTimes();
		final ReportField flowField = createMock("FlowField", ReportField.class);
		final Collection<ReportField> flowFields = Arrays.asList(flowField);
		EasyMock.expect(flowColumn.getFields()).andReturn(flowFields).anyTimes();
		EasyMock.expect(flowField.getUserFieldName()).andReturn("flow_type").anyTimes();
		EasyMock.expect(dataColumn.getName()).andReturn("Number").anyTimes();
		replayAll();

		final List<List<String>> actualReportRows = getReportingDashboardService().report(report, startDate, endDate);

		assertEquals("There are the correct number of rows in the report", 1, actualReportRows.size());
		final List<String> actualReportRow = actualReportRows.get(0);
		final List<String> expectedReportRow = Arrays.asList(UserFieldNames.FLOW_TYPE.convertDatabaseToDisplay(FlowType.RAVA),
				Long.toString(number));
		assertEquals("The row is correct", expectedReportRow, actualReportRow);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where
	 * there are no columns in the report.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 14, 2012
	 */
	@Test
	public final void testReport_noColumns() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.getColumns()).andReturn(null);
		final List reportData = new ArrayList();
		EasyMock.expect(getReportingDashboardDAO().report(report, null, null)).andReturn(reportData);
		replayAll();

		getReportingDashboardService().report(report, null, null);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where no
	 * report is provided.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 1, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testReport_noReport() {
		getReportingDashboardService().report(null, null, null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where
	 * there is a Number column in the report.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testReport_numberColumn() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final ReportColumn dataColumn = createMock("NumberColumn", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(dataColumn);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final long number = 24l;
		final Object resultRow = number;
		final List reportResult = Arrays.asList(resultRow);
		EasyMock.expect(getReportingDashboardDAO().report(report, startDate, endDate)).andReturn(reportResult);
		EasyMock.expect(dataColumn.getName()).andReturn("Number").atLeastOnce();
		replayAll();

		final List<List<String>> actualReportRows = getReportingDashboardService().report(report, startDate, endDate);

		assertEquals("There are the correct number of rows in the report", 1, actualReportRows.size());
		final List<String> actualReportRow = actualReportRows.get(0);
		final List<String> expectedReportRow = Arrays.asList(Long.toString(number));
		assertEquals("The row is correct", expectedReportRow, actualReportRow);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where
	 * there is a Percentage column in the report.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testReport_percentageColumn() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final ReportColumn dataColumn = createMock("NumberColumn", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(dataColumn);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final long number1 = 10l;
		final Object resultRow1 = number1;
		final long number2 = 90l;
		final Object resultRow2 = number2;
		final List reportResult = Arrays.asList(resultRow1, resultRow2);
		EasyMock.expect(getReportingDashboardDAO().report(report, startDate, endDate)).andReturn(reportResult);
		EasyMock.expect(dataColumn.getName()).andReturn("Percentage").atLeastOnce();
		replayAll();

		final List<List<String>> actualReportRows = getReportingDashboardService().report(report, startDate, endDate);

		assertEquals("There are the correct number of rows in the report", 2, actualReportRows.size());
		final double total = number1 + number2;
		final Long[] numbers = new Long[] { number1, number2 };
		for (int idx = 0; idx < 2; ++idx) {
			final List<String> actualReportRow = actualReportRows.get(idx);
			assertEquals("The percentage for row " + idx + " is correct",
					ReportingDashboardService.PERCENTAGE_FORMAT.format(numbers[idx] * 100. / total), actualReportRow.get(0));
		}
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where
	 * there is a question column in the report for which an answer was provided.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testReport_questionColumnAnswer() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final ReportColumn questionColumn = createMock("QuestionColumn", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(questionColumn);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final String dataValue = "Data Value";
		final Object[] resultRow = { 1l, dataValue, null };
		final List reportResult = Arrays.asList(new Object[] { resultRow });
		EasyMock.expect(getReportingDashboardDAO().report(report, startDate, endDate)).andReturn(reportResult);
		EasyMock.expect(questionColumn.getName()).andReturn("Question Column").atLeastOnce();
		EasyMock.expect(questionColumn.isQuestionColumn()).andReturn(true).atLeastOnce();
		replayAll();

		final List<List<String>> actualReportRows = getReportingDashboardService().report(report, startDate, endDate);

		assertEquals("There are the correct number of rows in the report", 1, actualReportRows.size());
		final List<String> actualReportRow = actualReportRows.get(0);
		final List<String> expectedReportRow = Arrays.asList(dataValue);
		assertEquals("The row is correct", expectedReportRow, actualReportRow);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where
	 * there is a question column in the report for which a predefined answer is provided.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testReport_questionColumnPredefined() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final ReportColumn questionColumn = createMock("QuestionColumn", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(questionColumn);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final long predefinedId = 23254l;
		final Object[] resultRow = { 1l, null, predefinedId };
		final List reportResult = Arrays.asList(new Object[] { resultRow });
		EasyMock.expect(getReportingDashboardDAO().report(report, startDate, endDate)).andReturn(reportResult);
		EasyMock.expect(questionColumn.getName()).andReturn("Question Column").atLeastOnce();
		EasyMock.expect(questionColumn.isQuestionColumn()).andReturn(true).atLeastOnce();
		final FieldDictionaryItem dictionaryItem = createMock("DictionaryItem", FieldDictionaryItem.class);
		final String dataValue = "Data Value";
		EasyMock.expect(getQuestionFieldService().findDictionaryItemById(predefinedId)).andReturn(dictionaryItem);
		EasyMock.expect(dictionaryItem.getViewValue()).andReturn(dataValue);
		replayAll();

		final List<List<String>> actualReportRows = getReportingDashboardService().report(report, startDate, endDate);

		assertEquals("There are the correct number of rows in the report", 1, actualReportRows.size());
		final List<String> actualReportRow = actualReportRows.get(0);
		final List<String> expectedReportRow = Arrays.asList(dataValue);
		assertEquals("The row is correct", expectedReportRow, actualReportRow);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#report(Report, Date, Date)} for the case where
	 * there is a user data column in the report.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testReport_userDataColumn() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final ReportColumn userDataColumn = createMock("UserDataColumn", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(userDataColumn);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final String dataValue = "Data Value";
		final Object[] resultRow = { 1l, dataValue };
		final List reportResult = Arrays.asList(new Object[] { resultRow });
		EasyMock.expect(getReportingDashboardDAO().report(report, startDate, endDate)).andReturn(reportResult);
		EasyMock.expect(userDataColumn.getName()).andReturn("User Data Column").anyTimes();
		EasyMock.expect(userDataColumn.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(userDataColumn.isUserFieldColumn()).andReturn(true).anyTimes();
		final ReportField userDataField = createMock("UserDataField", ReportField.class);
		final Collection<ReportField> userDataFields = Arrays.asList(userDataField);
		EasyMock.expect(userDataColumn.getFields()).andReturn(userDataFields).anyTimes();
		final UserFieldNames userFieldName = UserFieldNames.PAGE_TITLE;
		EasyMock.expect(userDataField.getUserFieldName())
				.andReturn(userFieldName.getTableName() + "." + userFieldName.getSqlName()).anyTimes();
		replayAll();

		final List<List<String>> actualReportRows = getReportingDashboardService().report(report, startDate, endDate);

		assertEquals("There are the correct number of rows in the report", 1, actualReportRows.size());
		final List<String> actualReportRow = actualReportRows.get(0);
		final List<String> expectedReportRow = Arrays.asList(dataValue);
		assertEquals("The row is correct", expectedReportRow, actualReportRow);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashbaordService#saveAnswer(ReportAnswer)} .
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testSaveAnswer() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		getReportingDashboardDAO().makePersistent(answer);
		replayAll();

		getReportingDashboardService().saveAnswer(answer);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashbaordService#saveAnswer(ReportAnswer)} for the case where no
	 * answer is provided .
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSaveAnswer_noAnswer() {
		getReportingDashboardService().saveAnswer(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashbaordService#saveColumn(ReportColumn)}.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testSaveColumn() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		getReportingDashboardDAO().makePersistent(column);
		replayAll();

		getReportingDashboardService().saveColumn(column);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashbaordService#saveColumn(ReportColumn)} for the case where there
	 * is no column.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 27, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSaveColumn_noColumn() {
		getReportingDashboardService().saveColumn(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashbaordService#saveField(ReportField)} .
	 * 
	 * @author IanBrown
	 * @since Jan 25, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testSaveField() {
		final ReportField field = createMock("Field", ReportField.class);
		getReportingDashboardDAO().makePersistent(field);
		replayAll();

		getReportingDashboardService().saveField(field);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashbaordService#saveField(ReportField)} for the case where there is
	 * no field.
	 * 
	 * @author IanBrown
	 * @since Jan 25, 2012
	 * @version Jan 27, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSaveField_noField() {
		getReportingDashboardService().saveField(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#saveReport(Report)}.
	 * 
	 * @author IanBrown
	 * @since Jan 10, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSaveReport() {
		final Report report = createMock("Report", Report.class);
		getReportingDashboardDAO().makePersistent(report);
		replayAll();

		getReportingDashboardService().saveReport(report);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#saveReport(Report)} for the case where there is no
	 * report.
	 * 
	 * @author IanBrown
	 * @since Jan 10, 2012
	 * @version Jan 10, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSaveReport_noReport() {
		getReportingDashboardService().saveReport(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#saveScheduledReport(ScheduledReport)}.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testSaveScheduledReport() {
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		getReportingDashboardDAO().makePersistent(scheduledReport);
		replayAll();

		getReportingDashboardService().saveScheduledReport(scheduledReport);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#saveScheduledReport(ScheduledReport)} for the case
	 * where there is no scheduled report.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testSaveScheduledReport_noScheduledReport() {
		getReportingDashboardService().saveScheduledReport(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#totals(Report, List)} for the case where there is
	 * no Number column.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testTotals_noNumberColumn() {
		final Report report = createMock("Report", Report.class);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(column.getName()).andReturn("Percentage");
		final List<String> reportRow = Arrays.asList("100.00");
		final List<List<String>> reportRows = Arrays.asList(reportRow);
		replayAll();

		final List<String> actualTotals = getReportingDashboardService().totals(report, reportRows);

		assertNull("There are no totals", actualTotals);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#totals(Report, List)} for the case where no report
	 * is provided.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testTotals_noReport() {
		getReportingDashboardService().totals(null, null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#totals(Report, List)} for the case where there are
	 * no rows in the report.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@Test
	public final void testTotals_noReportRows() {
		final Report report = createMock("Report", Report.class);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		final List<List<String>> reportRows = new ArrayList<List<String>>();
		replayAll();

		final List<String> actualTotals = getReportingDashboardService().totals(report, reportRows);

		assertNull("There are no totals", actualTotals);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#totals(Report, List)} for the case where no report
	 * rows list is provided.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testTotals_noReportRowsList() {
		final Report report = createMock("Report", Report.class);

		getReportingDashboardService().totals(report, null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#totals(Report, List)} for the case where there is a
	 * Number column.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testTotals_numberColumn() {
		final Report report = createMock("Report", Report.class);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(column.getName()).andReturn("Number");
		final long count1 = 123l;
		final long count2 = 484l;
		final long total = count1 + count2;
		final List<String> reportRow1 = Arrays.asList(Long.toString(count1));
		final List<String> reportRow2 = Arrays.asList(Long.toString(count2));
		final List<List<String>> reportRows = Arrays.asList(reportRow1, reportRow2);
		replayAll();

		final List<String> actualTotals = getReportingDashboardService().totals(report, reportRows);

		final List<String> expectedTotals = Arrays.asList(Long.toString(total));
		assertEquals("The total row is correct", expectedTotals, actualTotals);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#totals(Report, List)} for the case where there is a
	 * Number column followed by another column.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testTotals_numberColumnPlusAnotherColumn() {
		final Report report = createMock("Report", Report.class);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column, createMock("OtherColumn", ReportColumn.class));
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(column.getName()).andReturn("Number");
		final long count1 = 123l;
		final long count2 = 484l;
		final long total = count1 + count2;
		final List<String> reportRow1 = Arrays.asList(Long.toString(count1), "Other1");
		final List<String> reportRow2 = Arrays.asList(Long.toString(count2), "Other2");
		final List<List<String>> reportRows = Arrays.asList(reportRow1, reportRow2);
		replayAll();

		final List<String> actualTotals = getReportingDashboardService().totals(report, reportRows);

		final List<String> expectedTotals = Arrays.asList(Long.toString(total), "Total Records");
		assertEquals("The total row is correct", expectedTotals, actualTotals);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportColumns(Report, long[])} for the case
	 * where the column order is changed.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testUpdateReportColumn_changeOrder() {
		final Report report = createMock("Report", Report.class);
		final long columnId1 = 1l;
		final long columnId2 = 2l;
		final long[] columnIds = new long[] { columnId1, columnId2 };
		final ReportColumn column1 = createMock("Column1", ReportColumn.class);
		final ReportColumn column2 = createMock("Column2", ReportColumn.class);
		final List<ReportColumn> originalColumns = Arrays.asList(column2, column1);
		final List<ReportColumn> columns = new ArrayList<ReportColumn>(originalColumns);
		EasyMock.expect(report.getColumns()).andReturn(columns).anyTimes();
		EasyMock.expect(column1.getId()).andReturn(columnId1).anyTimes();
		EasyMock.expect(column2.getId()).andReturn(columnId2).anyTimes();
		EasyMock.expect(report.addColumn(column1)).andReturn(true).anyTimes();
		EasyMock.expect(report.addColumn(column2)).andReturn(true).anyTimes();
		replayAll();

		getReportingDashboardService().updateReportColumns(report, columnIds);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportColumns(Report, long[])} for the case
	 * where no changes are made.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testUpdateReportColumn_noChange() {
		final Report report = createMock("Report", Report.class);
		final long columnId1 = 1l;
		final long columnId2 = 2l;
		final long[] columnIds = new long[] { columnId1, columnId2 };
		final ReportColumn column1 = createMock("Column1", ReportColumn.class);
		final ReportColumn column2 = createMock("Column2", ReportColumn.class);
		final List<ReportColumn> originalColumns = Arrays.asList(column1, column2);
		final List<ReportColumn> columns = new ArrayList<ReportColumn>(originalColumns);
		EasyMock.expect(report.getColumns()).andReturn(columns).anyTimes();
		EasyMock.expect(column1.getId()).andReturn(columnId1).anyTimes();
		EasyMock.expect(column2.getId()).andReturn(columnId2).anyTimes();
		EasyMock.expect(report.addColumn(column1)).andReturn(true).anyTimes();
		EasyMock.expect(report.addColumn(column2)).andReturn(true).anyTimes();
		replayAll();

		getReportingDashboardService().updateReportColumns(report, columnIds);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportColumns(Report, long[])} for the case
	 * where a column is removed.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	@Test
	public final void testUpdateReportColumn_removeColumn() {
		final Report report = createMock("Report", Report.class);
		final long columnId1 = 1l;
		final long columnId2 = 2l;
		final long[] columnIds = new long[] { columnId2 };
		final ReportColumn column1 = createMock("Column1", ReportColumn.class);
		final ReportColumn column2 = createMock("Column2", ReportColumn.class);
		final List<ReportColumn> originalColumns = Arrays.asList(column2, column1);
		final List<ReportColumn> columns = new ArrayList<ReportColumn>(originalColumns);
		EasyMock.expect(report.getColumns()).andReturn(columns).anyTimes();
		EasyMock.expect(column1.getId()).andReturn(columnId1).anyTimes();
		EasyMock.expect(column2.getId()).andReturn(columnId2).anyTimes();
		EasyMock.expect(report.addColumn(column2)).andReturn(true).anyTimes();
		replayAll();

		getReportingDashboardService().updateReportColumns(report, columnIds);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where the user asks for a specific face when there were no faces set.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_applyAFace() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final String standardString = null;
		final String flowTypeValue = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final String[] faceNames = new String[] { face1UrlPath };
		final FaceConfig assignedFace = null;
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		EasyMock.expect(report.getFaces()).andReturn(null).anyTimes();
		report.setApplyFaces(true);
		final Set<String> faces = new HashSet<String>(Arrays.asList(face1UrlPath));
		report.setFaces(faces);
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where the user asks for all faces when there was a face set.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_applyAllFaces() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final String standardString = null;
		final String flowTypeValue = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final String[] faceNames = new String[] { face1UrlPath, face2UrlPath };
		final FaceConfig assignedFace = null;
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final Set<String> oldFaces = new HashSet<String>(Arrays.asList(face2UrlPath));
		EasyMock.expect(report.getFaces()).andReturn(oldFaces).anyTimes();
		report.setApplyFaces(false);
		report.setFaces(null);
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where the user asks for a specific face when a different face was set.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_applyDifferentFace() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final String standardString = null;
		final String flowTypeValue = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final String[] faceNames = new String[] { face1UrlPath };
		final FaceConfig assignedFace = null;
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final Set<String> oldFaces = new HashSet<String>(Arrays.asList(face2UrlPath));
		EasyMock.expect(report.getFaces()).andReturn(oldFaces).anyTimes();
		report.setApplyFaces(true);
		final Set<String> faces = new HashSet<String>(Arrays.asList(face1UrlPath));
		report.setFaces(faces);
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where the user asks for the face that was already set.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_applySameFace() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final String standardString = null;
		final String flowTypeValue = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final String[] faceNames = new String[] { face1UrlPath };
		final FaceConfig assignedFace = null;
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		final Set<String> oldFaces = new HashSet<String>(Arrays.asList(face1UrlPath));
		EasyMock.expect(report.getFaces()).andReturn(oldFaces).anyTimes();
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertFalse("The report was not updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where there is an assigned face.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_assignedFace() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final String standardString = null;
		final String flowTypeValue = null;
		final String[] faceNames = null;
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.getFaces()).andReturn(null).anyTimes();
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		EasyMock.expect(assignedFace.getUrlPath()).andReturn(face1UrlPath).anyTimes();
		report.setApplyFaces(true);
		final Set<String> faces = new HashSet<String>(Arrays.asList(face1UrlPath));
		report.setFaces(EasyMock.eq(faces));
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where a description change is requested.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_description() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = "Description";
		final String standardString = null;
		final String flowTypeValue = null;
		final String[] faceNames = null;
		final FaceConfig assignedFace = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		report.setDescription(description);
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where a change to the flow type is requested.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_flowType() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final String standardString = null;
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final String flowTypeValue = flowType.name();
		final String[] faceNames = null;
		final FaceConfig assignedFace = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		report.setFlowType(flowType);
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where no changes are requested.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_noChanges() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final String standardString = null;
		final String flowTypeValue = null;
		final String[] faceNames = null;
		final FaceConfig assignedFace = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertFalse("The report was not updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where a change to the standard flag is requested.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_standard() {
		final Report report = createMock("Report", Report.class);
		final String title = null;
		final String description = null;
		final boolean standard = true;
		final String standardString = Boolean.toString(standard);
		final String flowTypeValue = null;
		final String[] faceNames = null;
		final FaceConfig assignedFace = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		report.setStandard(standard);
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.ReportingDashboardService#updateReportSettings(Report, String, String, String, String, String[], FaceConfig, List, OverseasUser)}
	 * for the case where a title change is requested.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	@Test
	public final void testUpdateReportSettings_title() {
		final Report report = createMock("Report", Report.class);
		final String title = "Title";
		final String description = null;
		final String standardString = null;
		final String flowTypeValue = null;
		final String[] faceNames = null;
		final FaceConfig assignedFace = null;
		final String face1UrlPath = "face1/vote";
		final String face2UrlPath = "face2/vote";
		final List<String> allFaces = Arrays.asList(face1UrlPath, face2UrlPath);
		final OverseasUser owner = null;
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		report.setTitle(title);
		replayAll();

		final boolean actualUpdated = getReportingDashboardService().updateReportSettings(report, title, description,
				standardString, flowTypeValue, faceNames, assignedFace, allFaces, owner);

		assertTrue("The report was updated", actualUpdated);
		verifyAll();
	}

	/**
	 * Custom assertion to ensure that the user fields are set correctly.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param assignedFace
	 *            the assigned face.
	 * @param actualUserFields
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	private void assertUserFields(final Report report, final FaceConfig assignedFace,
			final List<Map<String, Object>> actualUserFields) {
		final Set<String> faces = (report.isApplyFaces() ? report.getFaces() : null);
		final boolean oneFace = report.isApplyFaces() && (faces != null) && (faces.size() == 1);
		final int expectedNumber = UserFieldNames.values().length - (report.isApplyFlow() ? 1 : 0)
				- ((assignedFace == null) ? 0 : 1) - (oneFace ? 1 : 0);
		assertEquals("The correct number of user fields are returned", expectedNumber, actualUserFields.size());
		final Iterator<Map<String, Object>> actualUserFieldIterator = actualUserFields.iterator();
		for (final UserFieldNames userFieldName : UserFieldNames.values()) {
			if ((userFieldName == UserFieldNames.FLOW_TYPE) && report.isApplyFlow()) {
				continue;
			} else if ((userFieldName == UserFieldNames.HOSTED_SITE) && ((assignedFace != null) || oneFace)) {
				continue;
			}

			assertTrue("There is an entry for " + userFieldName, actualUserFieldIterator.hasNext());
			final Map<String, Object> actualUserField = actualUserFieldIterator.next();
			assertEquals("The alias for " + userFieldName + " is set", userFieldName.getAlias(), actualUserField.get("alias"));
			assertEquals("The table name for " + userFieldName + " is set", userFieldName.getTableName(),
					actualUserField.get("tableName"));
			assertEquals("The SQL name for " + userFieldName + " is set", userFieldName.getSqlName(),
					actualUserField.get("sqlName"));
			assertEquals("The UI name for " + userFieldName + " is set", userFieldName.getUiName(), actualUserField.get("uiName"));
			final List<String> expectedAnswers = userFieldName.getAnswers();
			assertEquals("The answers for " + userFieldName + " are set", expectedAnswers, actualUserField.get("answers"));
		}
	}

	/**
	 * Builds a group (question) with the specified names and optional answers.
	 * 
	 * @author IanBrown
	 * @param groupQuestion
	 *            the group question.
	 * @param group
	 *            the group name.
	 * @param question
	 *            the question name.
	 * @param answers
	 *            the answers.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	private void buildGroup(final Question groupQuestion, final String group, final String question, final String[] answers) {
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(groupQuestion.getName()).andReturn(group).anyTimes();
		EasyMock.expect(groupQuestion.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.isDefault()).andReturn(true).anyTimes();
		final QuestionField questionField = createMock("Question", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(questionField);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		buildQuestion(questionField, question, answers);
	}

	/**
	 * Builds a question (field) with the specified name and optional answers.
	 * 
	 * @author IanBrown
	 * @param questionField
	 *            the question field.
	 * @param question
	 *            the question name.
	 * @param answers
	 *            the answers.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	private void buildQuestion(final QuestionField questionField, final String question, final String[] answers) {
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(questionField.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_CHECKBOX).anyTimes();
		EasyMock.expect(questionField.getFieldDependencies()).andReturn(null).anyTimes();
		EasyMock.expect(questionField.getTitle()).andReturn(question).anyTimes();
		if (answers != null) {
			final Collection<FieldDictionaryItem> options = new ArrayList<FieldDictionaryItem>();
			EasyMock.expect(questionField.getOptions()).andReturn(options).anyTimes();
			for (final String answer : answers) {
				final FieldDictionaryItem option = createMock("Option", FieldDictionaryItem.class);
				EasyMock.expect(option.getViewValue()).andReturn(answer).anyTimes();
				options.add(option);
			}
		}
	}

	/**
	 * Builds a questionnaire page with the specified names and with the optional answers.
	 * 
	 * @author IanBrown
	 * @param questionnairePage
	 *            the questionnaire page.
	 * @param page
	 *            the page name.
	 * @param group
	 *            the group name.
	 * @param question
	 *            the question name.
	 * @param answers
	 *            the answers.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	private void buildQuestionnairePage(final QuestionnairePage questionnairePage, final String page, final String group,
			final String question, final String[] answers) {
		EasyMock.expect(questionnairePage.getTitle()).andReturn(page).anyTimes();
		EasyMock.expect(questionnairePage.getType()).andReturn(PageType.OVERSEAS).anyTimes();
		final Question groupQuestion = createMock("Group", Question.class);
		final List<Question> groups = Arrays.asList(groupQuestion);
		EasyMock.expect(questionnairePage.getQuestions()).andReturn(groups).anyTimes();
		buildGroup(groupQuestion, group, question, answers);
	}

	/**
	 * Creates a reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @return the reporting dashboard service.
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	private ReportingDashboardService createReportingDashboardService() {
		return new ReportingDashboardService();
	}

	/**
	 * Gets the question field service.
	 * 
	 * @author IanBrown
	 * @return the question field service.
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

	/**
	 * Gets the reporting dashboard DAO.
	 * 
	 * @author IanBrown
	 * @return the reporting dashboard DAO.
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	private ReportingDashboardDAO getReportingDashboardDAO() {
		return reportingDashboardDAO;
	}

	/**
	 * Gets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @return the reporting dashboard service.
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	private ReportingDashboardService getReportingDashboardService() {
		return reportingDashboardService;
	}

	/**
	 * Sets the question field service.
	 * 
	 * @author IanBrown
	 * @param questionFieldService
	 *            the questionFieldService to set.
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	private void setQuestionFieldService(final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

	/**
	 * Sets the reporting dashboard DAO.
	 * 
	 * @author IanBrown
	 * @param reportingDashboardDAO
	 *            the reporting dashboard DAO to set.
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	private void setReportingDashboardDAO(final ReportingDashboardDAO reportingDashboardDAO) {
		this.reportingDashboardDAO = reportingDashboardDAO;
	}

	/**
	 * Sets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @param reportingDashboardService
	 *            the reporting dashboard service to set.
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	private void setReportingDashboardService(final ReportingDashboardService reportingDashboardService) {
		this.reportingDashboardService = reportingDashboardService;
	}
}
