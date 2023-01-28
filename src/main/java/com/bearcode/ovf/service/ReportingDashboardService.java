/**
 *
 */
package com.bearcode.ovf.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bearcode.ovf.DAO.ReportingDashboardDAO;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.DependentRoot;
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
import com.bearcode.ovf.model.questionnaire.QuestionDependency;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportAnswer;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;
import com.bearcode.ovf.model.reportingdashboard.ReportField;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;
import com.bearcode.ovf.model.reportingdashboard.UserFieldNames;

/**
 * The service that supports the reporting dashboard.
 *
 * @author IanBrown
 *
 * @since Jan 3, 2012
 * @version Apr 17, 2012
 */
@Service
public class ReportingDashboardService {

    private static final Logger log = LoggerFactory.getLogger( ReportingDashboardService.class );

    /**
     * Internal class for debugging that provides indented blocks to make things more visible.
     *
     * @author IanBrown
     *
     * @since Mar 29, 2012
     * @version Mar 29, 2012
     */

    private final static class BlockDebug {

        /**
         * the current indent level.
         *
         * @author IanBrown
         * @since Mar 29, 2012
         * @version Mar 29, 2012
         */
        private String indent = "";

        /**
         * Ends an indented block.
         *
         * @author IanBrown
         * @param format
         *            the message format string.
         * @param objects
         *            the objects to format.
         * @since Mar 29, 2012
         * @version Mar 29, 2012
         */
        public final void endDebugBlock(final String format, final Object... objects) {
            indent = indent.substring(0, indent.length() - 2);
            indentMessage(format, objects);
        }

        /**
         * Indents a message.
         *
         * @author IanBrown
         * @param format
         *            the message format string.
         * @param objects
         *            the objects to format.
         * @since Mar 29, 2012
         * @version Mar 29, 2012
         */
        public final void indentMessage(final String format, final Object... objects) {
            log.debug(indent + String.format(format, objects));
        }

        /**
         * Starts an indented block.
         *
         * @author IanBrown
         * @param format
         *            the message format string.
         * @param objects
         *            the objects to format.
         * @since Mar 29, 2012
         * @version Mar 29, 2012
         */
        public final void startDebugBlock(final String format, final Object... objects) {
            indentMessage(format, objects);
            indent += "  ";
        }
    }

    /**
     * the object used to provide indented block debug messages.
     *
     * @author IanBrown
     * @since Mar 29, 2012
     * @version Mar 29, 2012
     */
    private final BlockDebug blockDebug = new BlockDebug();

    /**
     * show debug traces?
     *
     * @author IanBrown
     * @since Mar 30, 2012
     * @version Mar 30, 2012
     */
    private final static boolean DEBUG_TRACE = false;

    /**
     * the reportable templates.
     *
     * @author IanBrown
     * @since Jan 24, 2012
     * @version Jan 24, 2012
     */
    static final String[] REPORTABLE_TEMPLATES = new String[] { FieldType.TEMPLATE_CHECKBOX, FieldType.TEMPLATE_CHECKBOX_FILLED,
            FieldType.TEMPLATE_RADIO, FieldType.TEMPLATE_SELECT };

    /**
     * the column header for the faces column.
     *
     * @author IanBrown
     * @since Feb 3, 2012
     * @version Mar 2, 2012
     */
    static final String FACES_COLUMN_HEADER = "Hosted Site";

    /**
     * the format used to display percentages.
     *
     * @author IanBrown
     * @since Mar 2, 2012
     * @version Mar 2, 2012
     */
    public static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("###.###");

    /**
     * the data access object to read the reporting dashboard data.
     *
     * @author IanBrown
     * @since Jan 4, 2012
     * @version Jan 4, 2012
     */
    @Autowired
    private ReportingDashboardDAO reportingDashboardDAO;

    /**
     * the question field service.
     *
     * @author IanBrown
     * @since Jan 26, 2012
     * @version Jan 26, 2012
     */
    @Autowired
    private QuestionFieldService questionFieldService;

    /**
     * Builds a report column for a metric.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param metric
     *            the name of the metric.
     * @param answers
     *            the specific answers provided for the metric (none currently allowed).
     * @return the metric column.
     * @since Apr 3, 2012
     * @version Apr 3, 2012
     */
    public void addMetricColumnToReport(final Report report, final String metric, final String[] answers) {
        final ReportColumn metricColumn = createColumn();
        metricColumn.setName(metric);
        if (answers != null) {
            throw new UnsupportedOperationException("Adding answers to a metric column is not supported");
        }
        report.addColumn(metricColumn);
    }

    /**
     * Adds a question column to the report.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context.
     * @param flows
     *            the available flow types.
     * @param wizardPages
     *            the wizard pages.
     * @param columnName
     *            the name of the new column.
     * @param page
     *            the name of the page.
     * @param group
     *            the name of the group.
     * @param question
     *            the name of the question.
     * @param answers
     *            the answers to the question.
     * @since Apr 3, 2012
     * @version Apr 3, 2012
     */
    public void addQuestionColumnToReport(final Report report, final WizardContext wizardContext, final FlowType[] flows,
                                          final List<QuestionnairePage> wizardPages, final String columnName, final String page, final String group,
                                          final String question, final String[] answers) {
        final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = pagesToReportableFields( report,
                wizardContext, flows, wizardPages );
        final Map<String, Map<String, Collection<QuestionField>>> pageMap = (reportableFields == null) ? null : reportableFields
                .get(page);
        if (pageMap == null) {
            throw new IllegalArgumentException(page + " is not a known page");
        }

        final Map<String, Collection<QuestionField>> groupMap = pageMap.get(group);
        if (groupMap == null) {
            throw new IllegalArgumentException(group + " is not a known group on page " + page);
        }

        final Collection<QuestionField> questionFields = groupMap.get(question);
        if (questionFields == null) {
            throw new IllegalArgumentException(question + " is not a known question in group " + group + " of page " + page);
        }

        final ReportColumn questionColumn = createColumn();
        questionColumn.setName(columnName);
        for (final QuestionField questionField : questionFields) {
            final ReportField reportField = createField();
            reportField.setQuestion(questionField);
            if (answers != null) {
                for (final String answer : answers) {
                    for (final FieldDictionaryItem option : questionField.getOptions()) {
                        if (option.getViewValue().equals(answer)) {
                            final ReportAnswer reportAnswer = createAnswer();
                            reportAnswer.setPredefinedAnswer(option);
                            reportField.addAnswer(reportAnswer);
                            break;
                        }
                    }
                }
            }
            questionColumn.addField(reportField);
        }
        report.addColumn(questionColumn);
    }

    /**
     * Adds a user detail column to the report.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param columnName
     *            the name of the column.
     * @param userDetail
     *            the user detail to add.
     * @param answers
     *            the optional answers.
     * @since Apr 3, 2012
     * @version Apr 3, 2012
     */
    public void addUserDetailColumnToReport(final Report report, final String columnName, final String userDetail,
                                            final String[] answers) {
        final UserFieldNames userFieldName = UserFieldNames.findByUiName( userDetail );
        if (userFieldName == null) {
            throw new IllegalArgumentException(userDetail + " is not a known voter detail, known details are "
                    + Arrays.toString(UserFieldNames.values()));
        }

        final ReportColumn userDetailColumn = createColumn();
        userDetailColumn.setName(columnName);
        final ReportField userDetailField = createField();
        userDetailField.setUserFieldName(userFieldName.getSqlName());
        if (answers != null) {
            for (final String answer : answers) {
                final String answerDatabaseValue = userFieldName.convertDisplayToDatabaseValue(answer);
                final ReportAnswer userDetailAnswer = createAnswer();
                userDetailAnswer.setAnswer(answerDatabaseValue);
                userDetailField.addAnswer(userDetailAnswer);
            }
        }
        userDetailColumn.addField( userDetailField );
        report.addColumn(userDetailColumn);
    }

    /**
     * Builds a report column for the selected question and answers.
     *
     * @author IanBrown
     * @param reportableFields
     *            the reportable fields.
     * @param name
     *            the name of the new column.
     * @param page
     *            the name of the page.
     * @param group
     *            the name of the group.
     * @param question
     *            the name of the question.
     * @param answers
     *            the acceptable answers - an empty list is the same as all.
     * @return the report column.
     * @since Mar 5, 2012
     * @version Mar 5, 2012
     */
    public ReportColumn buildReportColumn(final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields,
                                          final String name, final String page, final String group, final String question, final Collection<String> answers) {
        final ReportColumn reportColumn = createColumn();
        final Map<String, Map<String, Collection<QuestionField>>> reportablePageFields = reportableFields.get( page );
        final Map<String, Collection<QuestionField>> reportableGroupFields = reportablePageFields.get( group );
        final Collection<QuestionField> reportableQuestionFields = reportableGroupFields.get(question);

        reportColumn.setName( name );
        for (final QuestionField reportableQuestionField : reportableQuestionFields) {
            final ReportField reportField = buildReportField(reportableQuestionField, answers);
            reportColumn.addField(reportField);
        }

        return reportColumn;
    }

    /**
     * Builds the list of user fields. Each entry in the list is a map containing the values for one user field:
     * <p>
     * alias: the alias for the table containing the field.<br>
     * tableName: the name of the table containing the field.<br>
     * sqlName: the name of the field in the database.<br>
     * uiName: the name of the field in the UI.<br>
     * answers: the list of possible answers for the field.<br>
     * <p>
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param assignedFace
     *            the face assigned to the user (or <code>null</code>).
     * @return the list of user fields.
     * @since Apr 3, 2012
     * @version Apr 3, 2012
     */
    public List<Map<String, Object>> buildUserFields(final Report report, final FaceConfig assignedFace) {
        final UserFieldNames[] userFieldNames = UserFieldNames.values();
        final List<Map<String, Object>> userFieldOptions = new LinkedList<Map<String, Object>>();

        for (final UserFieldNames userFieldName : userFieldNames) {
            if (userFieldName == UserFieldNames.FLOW_TYPE) {
                // Flow type is presented only if all flows are available.
                if (report.isApplyFlow()) {
                    continue;
                }

            } else if (userFieldName == UserFieldNames.HOSTED_SITE) {
                // Hosted site is presented only if there are multiple faces.
                if (assignedFace != null) {
                    continue;
                } else if (report.isApplyFaces()) {
                    final Set<String> faces = report.getFaces();
                    if ((faces != null) && (faces.size() == 1)) {
                        continue;
                    }
                }
            }

            final HashMap<String, Object> userFieldOption = new HashMap<String, Object>();
            userFieldOption.put("alias", userFieldName.getAlias());
            userFieldOption.put("tableName", userFieldName.getTableName());
            userFieldOption.put("sqlName", userFieldName.getSqlName());
            userFieldOption.put("uiName", userFieldName.getUiName());
            userFieldOption.put( "answers", userFieldName.getAnswers() );
            userFieldOptions.add( userFieldOption );
        }

        return userFieldOptions;
    }

    /**
     * Converts the input map of reportable fields by page, group, and field to the pages object used by the view.
     *
     * @author IanBrown
     * @param reportableFields
     *            the reportable fields.
     * @return the pages.
     * @since Apr 3, 2012
     * @version Apr 3, 2012
     */
    public Map<String, Map<String, Map<String, List<String>>>> convertReportableFieldsToPages(
            final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields) {
        final Map<String, Map<String, Map<String, List<String>>>> pages = new LinkedHashMap<String, Map<String, Map<String, List<String>>>>();

        for (final Map.Entry<String, Map<String, Map<String, Collection<QuestionField>>>> reportablePageEntry : reportableFields
                .entrySet()) {
            final Map<String, Map<String, List<String>>> pageFields = new LinkedHashMap<String, Map<String, List<String>>>();
            pages.put( reportablePageEntry.getKey(), pageFields );

            for (final Map.Entry<String, Map<String, Collection<QuestionField>>> reportableGroupEntry : reportablePageEntry
                    .getValue().entrySet()) {
                final Map<String, List<String>> groupFields = new LinkedHashMap<String, List<String>>();
                pageFields.put(reportableGroupEntry.getKey(), groupFields);

                for (final Map.Entry<String, Collection<QuestionField>> reportableQuestionEntry : reportableGroupEntry.getValue()
                        .entrySet()) {
                    final Set<String> uniqueAnswers = new HashSet<String>();

                    for (final QuestionField questionField : reportableQuestionEntry.getValue()) {
                        final Collection<FieldDictionaryItem> options = questionField.getOptions();
                        if (options != null) {
                            for (final FieldDictionaryItem option : options) {
                                uniqueAnswers.add(option.getViewValue());
                            }
                        }
                    }
                    final ArrayList<String> answers = new ArrayList<String>(uniqueAnswers);
                    Collections.sort(answers);
                    groupFields.put(reportableQuestionEntry.getKey(), answers);
                }
            }
        }

        return pages;
    }

    /**
     * Copies the input report.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param user
     *            the current user - the owner of the new report.
     * @return the copied report.
     * @since Apr 4, 2012
     * @version Apr 4, 2012
     */
    public Report copyReport(final Report report, final OverseasUser user) {
        final Report copiedReport = report.deepCopy();

        copiedReport.setTitle(report.getTitle() + " (copy)");
        copiedReport.setOwner(user);
        copiedReport.setStandard( false );
        return copiedReport;
    }

    /**
     * Creates a report answer.
     *
     * @author IanBrown
     * @return the answer.
     * @since Jan 27, 2012
     * @version Jan 27, 2012
     */
    public ReportAnswer createAnswer() {
        return new ReportAnswer();
    }

    /**
     * Creates a report column.
     *
     * @author IanBrown
     * @return the report column.
     * @since Jan 10, 2012
     * @version Jan 11, 2012
     */
    public ReportColumn createColumn() {
        return new ReportColumn();
    }

    /**
     * Creates a custom report.
     *
     * @author IanBrown
     * @param owner
     *            the owner (user).
     * @param assignedFace
     *            the face assigned to the user.
     * @return the report.
     * @since Mar 28, 2012
     * @version Apr 4, 2012
     */
    public Report createCustomReport(final OverseasUser owner, final FaceConfig assignedFace) {
        final Report report = createReport();
        report.setOwner(owner);
        report.setStandard(false);
        if (assignedFace != null) {
            report.setApplyFaces(true);
            final Set<String> faces = new HashSet<String>();
            faces.add(assignedFace.getUrlPath());
            report.setFaces(faces);
        }
        return report;
    }

    /**
     * Creates a field.
     *
     * @author IanBrown
     * @return the field.
     * @since Jan 20, 2012
     * @version Jan 20, 2012
     */
    public ReportField createField() {
        return new ReportField();
    }

    /**
     * Creates a report.
     *
     * @author IanBrown
     * @return the report.
     * @since Jan 11, 2012
     * @version Jan 11, 2012
     */
    public Report createReport() {
        return new Report();
    }

    /**
     * Creates a scheduled report.
     *
     * @author IanBrown
     * @return the scheduled report.
     * @since Mar 6, 2012
     * @version Mar 6, 2012
     */
    public ScheduledReport createScheduledReport() {
        return new ScheduledReport();
    }

    /**
     * Deletes the answer.
     *
     * @author IanBrown
     * @param answer
     *            the answer.
     * @since Jan 27, 2012
     * @version Jan 27, 2012
     */
    public void deleteAnswer(final ReportAnswer answer) {
        getReportingDashboardDAO().deleteAnswer(answer);
    }

    /**
     * Deletes the column.
     *
     * @author IanBrown
     * @param column
     *            the column.
     * @since Jan 19, 2012
     * @version Jan 19, 2012
     */
    public void deleteColumn(final ReportColumn column) {
        getReportingDashboardDAO().deleteColumn( column );
    }

    /**
     * Deletes the field.
     *
     * @author IanBrown
     * @param field
     *            the field to delete.
     * @since Jan 19, 2012
     * @version Jan 19, 2012
     */
    public void deleteField(final ReportField field) {
        getReportingDashboardDAO().deleteField( field );
    }

    /**
     * Deletes the specified report.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @since Jan 11, 2012
     * @version Jan 11, 2012
     */
    public void deleteReport(final Report report) {
        getReportingDashboardDAO().deleteReport( report );
    }

    /**
     * Deletes the scheduled report.
     *
     * @author IanBrown
     * @param scheduledReport
     *            the scheduled report.
     * @since Mar 9, 2012
     * @version Mar 9, 2012
     */
    public void deleteScheduledReport(final ScheduledReport scheduledReport) {
        getReportingDashboardDAO().deleteScheduledReport( scheduledReport );
    }

    /**
     * Extracts the column headers from the report.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @return the column headers.
     * @since Jan 30, 2012
     * @version Apr 2, 2012
     */
    public List<String> extractColumnHeaders(final Report report) {
        if (report == null) {
            throw new IllegalArgumentException("Need report for extracting column headers");
        }

        final List<String> columnHeaders = new LinkedList<String>();
        final List<ReportColumn> columns = report.getColumns();
        if (columns != null) {
            for (final ReportColumn column : columns) {
                if ( column != null ) {
                    columnHeaders.add(column.getName());
                }
            }
        }

        return columnHeaders;
    }

    /**
     * Finds the reports that have been scheduled.
     *
     * @author IanBrown
     * @return the scheduled reports.
     * @since Mar 6, 2012
     * @version Mar 6, 2012
     */
    public List<ScheduledReport> findAllScheduledReports() {
        return getReportingDashboardDAO().findAllScheduledReports();
    }

    /**
     * Finds the answer with the specified identifier.
     *
     * @author IanBrown
     * @param id
     *            the answer identifier.
     * @return the answer.
     * @since Jan 27, 2012
     * @version Jan 27, 2012
     */
    public ReportAnswer findAnswerById(final long id) {
        return getReportingDashboardDAO().findAnswerById( id );
    }

    /**
     * Finds the column for the specified identifier.
     *
     * @author IanBrown
     * @param id
     *            the identifier.
     * @return the column.
     * @since Jan 11, 2012
     * @version Jan 11, 2012
     */
    public ReportColumn findColumnById(final long id) {
        return getReportingDashboardDAO().findColumnById( id );
    }

    /**
     * Finds the custom reports for the specified user and, for administrative users, current face.
     *
     * @author IanBrown
     * @param user
     *            the user.
     * @param currentFace
     *            the current face.
     * @return the custom reports.
     * @since Mar 1, 2012
     * @version Mar 30, 2012
     */
    public List<Report> findCustomReports(final OverseasUser user, final FaceConfig currentFace) {
        if (user == null) {
            return new ArrayList<Report>();
        }

        if (user.isInRole(UserRole.USER_ROLE_ADMIN)) {
            final List<Report> customReports;
            if (currentFace == null) {
                final List<Report> allReports = getReportingDashboardDAO().findAllReports();
                customReports = new LinkedList<Report>();
                for (final Report report : allReports) {
                    if (!report.isStandard()) {
                        customReports.add(report);
                    }
                }
            } else {
                final List<Report> faceCustomReports = getReportingDashboardDAO().findFaceReports(currentFace);
                final List<Report> userCustomReports = getReportingDashboardDAO().findUserReports(user);
                customReports = new ArrayList<Report>(faceCustomReports);
                for (final Report userCustomReport : userCustomReports) {
                    if (!customReports.contains(userCustomReport)) {
                        customReports.add(userCustomReport);
                    }
                }
            }
            return customReports;
        }

        return getReportingDashboardDAO().findUserReports( user );
    }

    /**
     * Finds a field by its identifier.
     *
     * @author IanBrown
     * @param id
     *            the field identifier.
     * @return the field with the identifier or <code>null</code>.
     * @since Jan 12, 2012
     * @version Jan 12, 2012
     */
    public ReportField findFieldById(final long id) {
        return getReportingDashboardDAO().findFieldById( id );
    }

    /**
     * Finds a report by its identifier.
     *
     * @author IanBrown
     * @param id
     *            the report identifier.
     * @return the report.
     * @since Jan 6, 2012
     * @version Jan 6, 2012
     */
    public Report findReportById(final long id) {
        return getReportingDashboardDAO().findReportById( id );
    }

    /**
     * Finds the scheduled report for the specified identifier.
     *
     * @author IanBrown
     * @param id
     *            the scheduled report identifier.
     * @return the scheduled report.
     * @since Mar 6, 2012
     * @version Mar 6, 2012
     */
    public ScheduledReport findScheduledReportById(final long id) {
        return getReportingDashboardDAO().findScheduledReportById( id );
    }

    /**
     * Finds the reports that the user has scheduled.
     *
     * @author IanBrown
     * @param user
     *            the user.
     * @return the scheduled reports.
     * @since Mar 6, 2012
     * @version Mar 6, 2012
     */
    public List<ScheduledReport> findScheduledReports(final OverseasUser user) {
        if (user == null) {
            return findAllScheduledReports();
        }

        return getReportingDashboardDAO().findScheduledReports( user );
    }

    /**
     * Finds the scheduled reports that are due.
     *
     * @author IanBrown
     * @return the scheduled reports found.
     * @since Mar 8, 2012
     * @version Mar 9, 2012
     */
    public List<ScheduledReport> findScheduledReportsDue() {
        return getReportingDashboardDAO().findScheduledReportsDue();
    }

    /**
     * Finds the standard reports.
     *
     * @author IanBrown
     * @return the standard reports.
     * @since Feb 29, 2012
     * @version Feb 29, 2012
     */
    public Map<String, Report> findStandardReports() {
        return getReportingDashboardDAO().findStandardReports();
    }

    /**
     * Flushes the reporting dashboard DAO associated with this service.
     *
     * @author IanBrown
     * @since Jan 11, 2012
     * @version Jan 11, 2012
     */
    public void flush() {
        getReportingDashboardDAO().flush();
    }

    /**
     * Gets the question field service.
     *
     * @author IanBrown
     * @return the question field service.
     * @since Jan 26, 2012
     * @version Jan 26, 2012
     */
    public QuestionFieldService getQuestionFieldService() {
        return questionFieldService;
    }

    /**
     * Gets the reporting dashboard DAO.
     *
     * @author IanBrown
     * @return the reporting dashboard DAO.
     * @since Jan 4, 2012
     * @version Jan 4, 2012
     */
    public ReportingDashboardDAO getReportingDashboardDAO() {
        return reportingDashboardDAO;
    }

    /**
     * Converts the questionnaire pages into a map of report-able fields.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the available flows.
     * @param pages
     *            the questionnaire (wizard) pages.
     * @return the report-able fields map by page title, group title, and field title.
     * @since Feb 29, 2012
     * @version Mar 30, 2012
     */
    public Map<String, Map<String, Map<String, Collection<QuestionField>>>> pagesToReportableFields(final Report report,
                                                                                                    final WizardContext wizardContext, final FlowType[] flows, final List<QuestionnairePage> pages) {
        final List<QuestionnairePage> reducedPages = reduceQuestionnairePagesForReporting(report, wizardContext, flows, pages);
        Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields = null;

        if (reducedPages != null) {
            reportableFields = new LinkedHashMap<String, Map<String, Map<String, Collection<QuestionField>>>>();
            for (final QuestionnairePage page : reducedPages) {
                pageToReportableFields(page, reportableFields);
            }
            if (reportableFields.isEmpty()) {
                reportableFields = null;
            }
        }

        return reportableFields;
    }

    /**
     * Reduces the input hierarchy of questionnaire pages for use with the reporting dashboard.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the flows available.
     * @param questionnairePages
     *            the full hierarchy of questionnaire pages.
     * @return the reduced hierarchy of questionnaire pages.
     * @since Jan 24, 2012
     * @version May 17, 2012
     */
    public List<QuestionnairePage> reduceQuestionnairePagesForReporting(final Report report, final WizardContext wizardContext,
                                                                        final FlowType[] flows, final List<QuestionnairePage> questionnairePages) {
        if (!report.isApplyFlow() || !report.isApplyFaces()) {
            return null;
        }

        final List<QuestionnairePage> reducedPages = new LinkedList<QuestionnairePage>();
        if (questionnairePages != null) {
            for (final QuestionnairePage questionnairePage : questionnairePages) {
                if (DEBUG_TRACE) {
                    blockDebug.startDebugBlock("Page %s started", questionnairePage.getTitle());
                }
                if (!checkPageType(flows, questionnairePage)) {
                    if (DEBUG_TRACE) {
                        blockDebug.endDebugBlock("Page %s rejected on page type", questionnairePage.getTitle());
                    }
                    continue;
                }

                final List<Question> groups = questionnairePage.getQuestions();
                reduceGroupsForReporting( report, wizardContext, flows, groups );

                if (!groups.isEmpty()) {
                    reducedPages.add(questionnairePage);
                    if (DEBUG_TRACE) {
                        blockDebug.endDebugBlock("Page %s kept", questionnairePage.getTitle());
                    }
                } else {
                    if (DEBUG_TRACE) {
                        blockDebug.endDebugBlock("Page %s rejected - no groups kept", questionnairePage.getTitle());
                    }
                }
            }
        }

        return reducedPages;
    }

    /**
     * Performs the collection of data for the specified report. One or both dates may be <code>null</code>, in which case the
     * report is open ended on that end.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param startDate
     *            the start date.
     * @param endDate
     *            the end date.
     * @return the report data - rows of data, each row containing one value for each column in the report.
     * @since Mar 1, 2012
     * @version Apr 2, 2012
     */
    @SuppressWarnings("rawtypes")
    public List<List<String>> report(final Report report, final Date startDate, final Date endDate) {
        if (report == null) {
            throw new IllegalArgumentException("A report must be provided");
        }

        final List<ReportColumn> columns = report.getColumns();
        final List reportData = getReportingDashboardDAO().report(report, startDate, endDate);
        final List<List<String>> reportRows = new ArrayList<List<String>>();
        final double totalNumber = calculateTotalNumber(reportData);
        for (final Object reportDataObject : reportData) {
            final List<String> reportRow = new LinkedList<String>();
            final Object[] reportDataRow;
            if (reportDataObject instanceof Object[]) {
                reportDataRow = (Object[]) reportDataObject;
            } else {
                reportDataRow = new Object[] { reportDataObject };
            }

            int columnIdx = 1;
            for (final ReportColumn column : columns) {
                final String columnName = column.getName();
                if (columnName.equals("Number")) {
                    reportRow.add(reportDataRow[0].toString());

                } else if (columnName.equals("Percentage")) {
                    reportRow.add(PERCENTAGE_FORMAT.format(((Number) reportDataRow[0]).doubleValue() * 100. / totalNumber));

                } else {
                    final Object columnData = reportDataRow[columnIdx];
                    if (column.isQuestionColumn()) {
                        if (columnData == null) {
                            final Object predefinedData = reportDataRow[columnIdx + 1];
                            if (predefinedData == null) {
                                reportRow.add("(no value)");
                            } else {
                                final long predefinedId = Long.parseLong(predefinedData.toString());
                                reportRow.add(getQuestionFieldService().findDictionaryItemById(predefinedId).getViewValue());
                            }
                        } else {
                            reportRow.add(columnData.toString());
                        }
                        columnIdx += 2;

                    } else if (column.isUserFieldColumn()) {
                        final ReportField field = column.getFields().iterator().next();
                        final UserFieldNames userFieldName = UserFieldNames.findByUserFieldName(field.getUserFieldName());
                        if (userFieldName == null) {
                            reportRow.add((columnData == null) ? "(no value)" : columnData.toString());
                        } else {
                            reportRow.add(userFieldName.convertDatabaseToDisplay(columnData));
                        }
                        ++columnIdx;

                    } else {
                        reportRow.add((columnData == null) ? "(no value)" : columnData.toString());
                        ++columnIdx;
                    }
                }
            }

            reportRows.add(reportRow);
        }

        return reportRows;
    }

    /**
     * Saves the answer.
     *
     * @author IanBrown
     * @param answer
     *            the answer.
     * @since Jan 27, 2012
     * @version Jan 27, 2012
     */
    public void saveAnswer(final ReportAnswer answer) {
        if (answer == null) {
            throw new IllegalArgumentException("There must be an answer to save");
        }

        getReportingDashboardDAO().makePersistent(answer);
    }

    /**
     * Saves the input column.
     *
     * @author IanBrown
     * @param column
     *            the column.
     * @since Jan 12, 2012
     * @version Jan 12, 2012
     */
    public void saveColumn(final ReportColumn column) {
        if (column == null) {
            throw new IllegalArgumentException("There must be a column to save");
        }

        getReportingDashboardDAO().makePersistent(column);
    }

    /**
     * Saves the field.
     *
     * @author IanBrown
     * @param field
     *            the field.
     * @since Jan 25, 2012
     * @version Jan 25, 2012
     */
    public void saveField(final ReportField field) {
        if (field == null) {
            throw new IllegalArgumentException("There must be a field to save");
        }

        getReportingDashboardDAO().makePersistent(field);
    }

    /**
     * Saves the input report.
     *
     * @author IanBrown
     * @param report
     *            the report to save.
     * @since Jan 9, 2012
     * @version Jan 11, 2012
     */
    public void saveReport(final Report report) {
        if (report == null) {
            throw new IllegalArgumentException("There must be a report to save");
        }

        getReportingDashboardDAO().makePersistent(report);
    }

    /**
     * Saves the specified scheduled report.
     *
     * @author IanBrown
     * @param scheduledReport
     *            the scheduled report.
     * @since Mar 6, 2012
     * @version Mar 6, 2012
     */
    public void saveScheduledReport(final ScheduledReport scheduledReport) {
        if (scheduledReport == null) {
            throw new IllegalArgumentException("There must be a scheduled report to save");
        }

        getReportingDashboardDAO().makePersistent(scheduledReport);
    }

    /**
     * Sets the question field service.
     *
     * @author IanBrown
     * @param questionFieldService
     *            the question field service to set.
     * @since Jan 26, 2012
     * @version Jan 26, 2012
     */
    public void setQuestionFieldService(final QuestionFieldService questionFieldService) {
        this.questionFieldService = questionFieldService;
    }

    /**
     * Sets the reporting dashboard DAO.
     *
     * @author IanBrown
     * @param reportingDashboardDAO
     *            the reporting dashboard DAO to set.
     * @since Jan 4, 2012
     * @version Jan 4, 2012
     */
    public void setReportingDashboardDAO(final ReportingDashboardDAO reportingDashboardDAO) {
        this.reportingDashboardDAO = reportingDashboardDAO;
    }

    /**
     * Produces the totals for the specified report from the data provided.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param reportRows
     *            the report rows.
     * @return the totals - this will be <code>null</code> if there is no "Number" column. If there is a "Number" column, this will
     *         be one row of values for each column, with all number the "Number" column equal to "".
     * @since Mar 1, 2012
     * @version Apr 2, 2012
     */
    public List<String> totals(final Report report, final List<List<String>> reportRows) {
        if (report == null) {
            throw new IllegalArgumentException("A report must be provided");
        } else if (reportRows == null) {
            throw new IllegalArgumentException("A list of report rows must be provided");
        }

        int columnIdx = 0;
        final List<ReportColumn> columns = report.getColumns();
        final int totalColumns = columns.size();
        List<String> totals = null;
        if ((columns != null) && !reportRows.isEmpty()) {
            for (final ReportColumn column : columns) {
                if (column.getName().equals("Number")) {
                    break;
                }
                ++columnIdx;
            }

            if (columnIdx < reportRows.get(0).size()) {
                long total = 0l;
                for (final List<String> reportRow : reportRows) {
                    total += Long.parseLong(reportRow.get(columnIdx));
                }
                totals = new LinkedList<String>();
                for (int idx = 0; idx < totalColumns; ++idx) {
                    if (idx == columnIdx) {
                        totals.add(Long.toString(total));
                    } else if ((idx == 0) || ((columnIdx == 0) && (idx == columnIdx + 1))) {
                        totals.add("Total Records");
                    } else {
                        totals.add("");
                    }
                }
            }
        }

        return totals;
    }

    /**
     * Updates the columns of the report to match the specified order.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param columnIds
     *            the column identifiers.
     * @since Mar 13, 2012
     * @version Apr 3, 2012
     */
    public void updateReportColumns(final Report report, final long[] columnIds) {
        final List<ReportColumn> reportColumns = report.getColumns();
        final List<ReportColumn> existingReportColumns = new ArrayList<ReportColumn>(reportColumns);
        reportColumns.clear();
        for (final long columnId : columnIds) {
            for (final ReportColumn reportColumn : existingReportColumns) {
                if (reportColumn.getId() == columnId) {
                    report.addColumn(reportColumn);
                    break;
                }
            }
        }
    }

    /**
     * Updates the settings for the report. Any new values that are <code>null</code> are left unchanged.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param title
     *            the new title of the report.
     * @param description
     *            the new description of the report.
     * @param standardString
     *            the string specifying the new standard flag for the report.
     * @param flowTypeValue
     *            the string specifying the new flow type for the report.
     * @param faceNames
     *            the names of the new faces for the report.
     * @param assignedFace
     *            the face assigned to the current user.
     * @param allFaces
     *            the list of faces available.
     * @param owner
     *            the new owner of the report.
     * @return <code>true</code> if the report is updated, <code>false</code> otherwise.
     * @since Apr 4, 2012
     * @version Apr 4, 2012
     */
    public boolean updateReportSettings(final Report report, final String title, final String description,
                                        final String standardString, final String flowTypeValue, final String[] faceNames, final FaceConfig assignedFace,
                                        final List<String> allFaces, final OverseasUser owner) {
        boolean updated = false;

        if (title != null) {
            report.setTitle(title);
            updated = true;
        }

        if (description != null) {
            report.setDescription(description);
            updated = true;
        }

        if (standardString != null) {
            final boolean standard = Boolean.valueOf(standardString);
            report.setStandard(standard);
            updated = true;
        }

        if ((flowTypeValue != null) && !flowTypeValue.trim().isEmpty()) {
            final FlowType flowType = FlowType.valueOf(flowTypeValue);
            report.setFlowType(flowType);
            updated = true;
        }

        if (assignedFace == null) {
            if ((faceNames != null) && (faceNames.length > 0)) {
                final Set<String> faces = new HashSet<String>(Arrays.asList(faceNames));
                if (!faces.containsAll(allFaces)) {
                    final Set<String> reportFaces = report.getFaces();
                    if (!report.isApplyFaces() || !faces.containsAll(reportFaces) || !reportFaces.containsAll(faces)) {
                        report.setApplyFaces(true);
                        report.setFaces(faces);
                        updated = true;
                    }

                } else if (report.isApplyFaces()) {
                    report.setApplyFaces(false);
                    report.setFaces(null);
                    updated = true;
                }

            } else if (report.isApplyFaces()) {
                report.setApplyFaces(false);
                report.setFaces(null);
                updated = true;
            }

        } else {
            final Set<String> reportFaces = report.getFaces();
            final String face = assignedFace.getUrlPath();
            if (!report.isApplyFaces() || (reportFaces.size() > 1) || !reportFaces.contains(face)) {
                report.setApplyFaces(true);
                final Set<String> faces = new HashSet<String>();
                faces.add(face);
                report.setFaces(faces);
                updated = true;
            }
        }

        if ((owner != null) && ((report.getOwner() == null) || (owner.getId() != report.getOwner().getId()))) {
            report.setOwner(owner);
            updated = true;
        }

        return updated;
    }

    /**
     * Builds a report field for the input reportable question field and sets the selected answers.
     *
     * @author IanBrown
     * @param reportableQuestionField
     *            the reportable question field.
     * @param answers
     *            the selected answers.
     * @return the report field.
     * @since Mar 5, 2012
     * @version Mar 5, 2012
     */
    private ReportField buildReportField(final QuestionField reportableQuestionField, final Collection<String> answers) {
        final ReportField reportField = createField();
        reportField.setQuestion(reportableQuestionField);
        if ((answers != null) && !answers.isEmpty()) {
            final Collection<FieldDictionaryItem> fieldOptions = reportableQuestionField.getOptions();
            final Collection<FieldDictionaryItem> selectedOptions = new LinkedList<FieldDictionaryItem>();
            for (final FieldDictionaryItem fieldOption : fieldOptions) {
                if (answers.contains( fieldOption.getViewValue() )) {
                    selectedOptions.add(fieldOption);
                }
            }

            if (!selectedOptions.containsAll(fieldOptions)) {
                for (final FieldDictionaryItem selectedOption : selectedOptions) {
                    final ReportAnswer reportAnswer = createAnswer();
                    reportAnswer.setPredefinedAnswer(selectedOption);
                    reportField.addAnswer(reportAnswer);
                }
            }
        }
        return reportField;
    }

    /**
     * Calculates the total number for the report.
     *
     * @author IanBrown
     * @param reportData
     *            the report data - rows of data columns where the first column is the number.
     * @return the total number.
     * @since Mar 2, 2012
     * @version Mar 2, 2012
     */
    @SuppressWarnings("rawtypes")
    private long calculateTotalNumber(final List reportData) {
        long total = 0;
        for (final Object reportDataObject : reportData) {
            if (reportDataObject instanceof Object[]) {
                final Object[] reportDataRow = (Object[]) reportDataObject;
                total += ((Number) reportDataRow[0]).longValue();
            } else {
                total += ((Number) reportDataObject).longValue();
            }
        }
        return total;
    }

    /**
     * Are the question field's field dependencies matched?
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the flows available.
     * @param questionField
     *            the question field.
     * @return <code>true</code> if there are no field dependencies or at least one is matched, <code>false</code> otherwise.
     * @since Mar 15, 2012
     * @version Mar 27, 2012
     */
    private boolean checkFieldDependencies(final Report report, final WizardContext wizardContext, final FlowType[] flows,
                                           final QuestionField questionField) {
        final Collection<FieldDependency> fieldDependencies = questionField.getFieldDependencies();
        if ((fieldDependencies != null) && !fieldDependencies.isEmpty()) {
            for (final FieldDependency fieldDependency : fieldDependencies) {
                if (checkGroupDependencies(report, wizardContext, flows, fieldDependency.getDependsOn())) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    /**
     * Are any of the variants of the group matched?
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the flows available.
     * @param group
     *            the group.
     * @return <code>true</code> if at least one variant is matched, <code>false</code> otherwise.
     * @since Mar 15, 2012
     * @version Mar 29, 2012
     */
    private boolean checkGroupDependencies(final Report report, final WizardContext wizardContext, final FlowType[] flows,
                                           final Question group) {
        final Collection<QuestionVariant> variants = group.getVariants();

        for (final QuestionVariant variant : variants) {
            if (variant.isDefault() || checkVariantDependencies(report, wizardContext, flows, variant)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check the page type against the flows to ensure that they are compatible.
     *
     * @author IanBrown
     * @param flows
     *            the flows.
     * @param questionnairePage
     *            the page.
     * @return <code>true</code> if the page type uses one or more of the flows,</code>false</code> otherwise.
     * @since Mar 29, 2012
     * @version Mar 29, 2012
     */
    private boolean checkPageType(final FlowType[] flows, final QuestionnairePage questionnairePage) {
        boolean compatible = false;
        final PageType pageType = questionnairePage.getType();
        final List<FlowType> flowList = Arrays.asList(flows);

        if (pageType == PageType.OVERSEAS) {
            compatible = flowList.contains(FlowType.RAVA) || flowList.contains(FlowType.FWAB);
        } else if (pageType == PageType.DOMESTIC_REGISTRATION) {
            compatible = flowList.contains(FlowType.DOMESTIC_REGISTRATION);
        }

        return compatible;
    }

    /**
     * Are the variant's dependencies matched?
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the flows available.
     * @param variant
     *            the variant.
     * @return <code>true</code> if the variant is valid, <code>false</code> the variant is not valid.
     * @since Mar 15, 2012
     * @version Mar 29, 2012
     */
    private boolean checkVariantDependencies(final Report report, final WizardContext wizardContext, final FlowType[] flows,
                                             final QuestionVariant variant) {
        final Collection<BasicDependency> keys = variant.getKeys();
        boolean resultForGroup = true;
        if (keys != null) {
            DependentRoot grouping = null;
            boolean forceCheckEntry = true;
            for (final BasicDependency key : keys) {
                if (!key.checkGroup(grouping)) {
                    grouping = key.getDependsOn();
                    if (!resultForGroup) {
                        return false;
                    }
                    forceCheckEntry = true;
                    resultForGroup = true;
                }

                if (!forceCheckEntry && resultForGroup) {
                    continue;
                }
                forceCheckEntry = false;

                if (key instanceof QuestionDependency) {
                    final QuestionDependency questionDependency = (QuestionDependency) key;
                    if (DEBUG_TRACE) {
                        blockDebug.startDebugBlock("QuestionDependency %s started", questionDependency.getDependsOn().getName(),
                                questionDependency.getDependsOn().getTitle());
                    }
                    resultForGroup = checkGroupDependencies(report, wizardContext, flows, questionDependency.getDependsOn());
                    if (DEBUG_TRACE) {
                        blockDebug.endDebugBlock("QuestionDependency %s %s", questionDependency.getDependsOn().getName(),
                                questionDependency.getDependsOn().getTitle(), resultForGroup ? "satisfied" : "not satisfied");
                    }

                } else {
                    if (key instanceof FlowDependency) {
                        final FlowDependency flowDependency = (FlowDependency) key;
                        if (!Arrays.asList(flows).contains(flowDependency.getFlowType())) {
                            if (DEBUG_TRACE) {
                                blockDebug.indentMessage("FlowDependency %s not satisfied - not supported flow type",
                                        flowDependency.getFlowType().name());
                            }
                            resultForGroup = false;
                            continue;
                        }
                    }
                    resultForGroup = key.checkDependency(wizardContext);
                    if (key instanceof FlowDependency) {
                        final FlowDependency flowDependency = (FlowDependency) key;
                        if (DEBUG_TRACE) {
                            blockDebug.indentMessage("FlowDependency %s %s", flowDependency.getFlowType().name(),
                                    resultForGroup ? "satisfied" : "not satisfied");
                        }
                    } else if (key instanceof FaceDependency) {
                        final FaceDependency faceDependency = (FaceDependency) key;
                        if (!resultForGroup && report.isApplyFaces()) {
                            resultForGroup = report.getFaces().contains(faceDependency.getDependsOn().getUrlPath());
                        }
                        if (DEBUG_TRACE) {
                            blockDebug.indentMessage("FaceDependency %s %s", faceDependency.getDependsOn().getUrlPath(),
                                    resultForGroup ? "satisfied" : "not satisfied");
                        }
                    } else if (key instanceof com.bearcode.ovf.model.questionnaire.UserFieldDependency) {
                        final com.bearcode.ovf.model.questionnaire.UserFieldDependency userFieldDependency = (com.bearcode.ovf.model.questionnaire.UserFieldDependency) key;
                        if (DEBUG_TRACE) {
                            blockDebug.indentMessage("UserFieldDependency %s = %s %s", userFieldDependency.getFieldName(),
                                    userFieldDependency.getFieldValue(), resultForGroup ? "satisfied" : "not satisfied");
                        }
                    }
                }
            }
        }

        return resultForGroup;
    }

    /**
     * Converts the group to a map of report-able fields.
     *
     * @author IanBrown
     * @param group
     *            the group.
     * @param pageReportableFields
     *            the report-able fields for the page that owns the group, by group and question title.
     * @since Feb 29, 2012
     * @version Mar 30, 2012
     */
    private void groupToReportableFields(final Question group,
                                         final Map<String, Map<String, Collection<QuestionField>>> pageReportableFields) {
        final String title = group.getName();
        Map<String, Collection<QuestionField>> groupReportableFields = pageReportableFields.get(title);
        if (groupReportableFields == null) {
            groupReportableFields = new LinkedHashMap<String, Collection<QuestionField>>();
            pageReportableFields.put(title, groupReportableFields);
        }

        final Collection<QuestionVariant> variants = group.getVariants();
        for (final QuestionVariant variant : variants) {
            variantToReportableFields(variant, groupReportableFields);
        }
    }

    /**
     * Converts the questionnaire page to a map of report-able fields.
     *
     * @author IanBrown
     * @param page
     *            the page.
     * @param reportableFields
     *            the report-able fields by page title, group title, and field title.
     * @since Feb 29, 2012
     * @version Mar 15, 2012
     */
    private void pageToReportableFields(final QuestionnairePage page,
                                        final Map<String, Map<String, Map<String, Collection<QuestionField>>>> reportableFields) {
        final String title = page.getTitle();
        Map<String, Map<String, Collection<QuestionField>>> pageReportableFields = reportableFields.get( title );
        if (pageReportableFields == null) {
            pageReportableFields = new LinkedHashMap<String, Map<String, Collection<QuestionField>>>();
            reportableFields.put(title, pageReportableFields);
        }

        final List<Question> groups = page.getQuestions();
        for (final Question group : groups) {
            groupToReportableFields( group, pageReportableFields );
        }
    }

    /**
     * Converts the question to an entry in the map of report-able field.
     *
     * @author IanBrown
     * @param question
     *            the question.
     * @param groupReportableFields
     *            the report-able fields for the group that owns the question, by question title.
     * @since Feb 29, 2012
     * @version Mar 15, 2012
     */
    private void questionToReportableField(final QuestionField question,
                                           final Map<String, Collection<QuestionField>> groupReportableFields) {
        final String title = question.getTitle();
        String groupTitle = null;
        Collection<QuestionField> questions = groupReportableFields.get( title );
        if (questions != null) {
            questions.add(question);

        } else {
            boolean matchedPdfName = false;
            boolean expandBlank = false;
            if (!groupReportableFields.isEmpty()) {
                final String pdfName = question.getInPdfName();
                final boolean blankPdfName = (pdfName == null) || pdfName.trim().isEmpty();
                for (final Map.Entry<String, Collection<QuestionField>> questionEntry : groupReportableFields.entrySet()) {
                    groupTitle = questionEntry.getKey();
                    final Collection<QuestionField> otherQuestions = questionEntry.getValue();
                    final QuestionField firstQuestion = otherQuestions.iterator().next();
                    final String firstPdfName = firstQuestion.getInPdfName();
                    final boolean blankFirstPdfName = (firstPdfName == null) || firstPdfName.trim().isEmpty();
                    if (blankPdfName) {
                        if (blankFirstPdfName) {
                            if (matchedPdfName) {
                                expandBlank = true;
                                break;
                            }

                            matchedPdfName = true;
                            for (final QuestionField otherQuestion : otherQuestions) {
                                if (otherQuestion.getQuestion() == question.getQuestion()) {
                                    expandBlank = true;
                                    break;
                                }
                            }

                            questions = otherQuestions;
                            if (expandBlank) {
                                if (otherQuestions.size() > 1) {
                                    groupReportableFields.remove(questionEntry.getKey());
                                    for (final QuestionField otherQuestion : questions) {
                                        groupReportableFields.put(otherQuestion.getTitle(),
                                                new LinkedList<QuestionField>(Arrays.asList(otherQuestion)));
                                    }
                                    break;
                                }
                            } else if (otherQuestions.size() == 1) {
                                break;
                            }
                        }
                    } else {
                        matchedPdfName = !blankFirstPdfName && pdfName.trim().equals(firstPdfName.trim());
                        if (matchedPdfName) {
                            questions = otherQuestions;
                            break;
                        }
                    }
                }
            }

            if (matchedPdfName && !expandBlank) {
                questions.add(question);
                Long bestId = null;
                String bestTitle = null;
                for (final QuestionField otherQuestion : questions) {
                    final long otherId = otherQuestion.getId();
                    if ((bestId == null) || (otherId < bestId)) {
                        bestId = otherId;
                        bestTitle = otherQuestion.getTitle();
                    }
                }
                if (!bestTitle.equals(groupTitle)) {
                    groupReportableFields.remove(groupTitle);
                    groupReportableFields.put(bestTitle, questions);
                }

            } else {
                groupReportableFields.put(title, new LinkedList<QuestionField>(Arrays.asList(question)));
            }
        }
    }

    /**
     * Reduces the question fields for reporting.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the flows available.
     * @param fields
     *            the question fields - fields may be removed on output.
     * @since Jan 24, 2012
     * @version Mar 29, 2012
     */
    private void reduceFieldsForReporting(final Report report, final WizardContext wizardContext, final FlowType[] flows,
                                          final Collection<QuestionField> fields) {
        final List<String> reportableTemplates = Arrays.asList( REPORTABLE_TEMPLATES );
        for (final Iterator<QuestionField> fieldIt = fields.iterator(); fieldIt.hasNext();) {
            final QuestionField field = fieldIt.next();
            if (DEBUG_TRACE) {
                blockDebug.startDebugBlock("Question %s started", field.getTitle());
            }

            if (!reportableTemplates.contains(field.getType().getTemplateName())
                    || !checkFieldDependencies(report, wizardContext, flows, field)) {
                fieldIt.remove();
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Question %s rejected", field.getTitle());
                }
            } else {
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Question %s kept", field.getTitle());
                }
            }
        }
    }

    /**
     * Reduces the question groups for reporting.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the flows available.
     * @param groups
     *            the groups - groups may be removed on output.
     * @since Jan 24, 2012
     * @version Mar 29, 2012
     */
    private void reduceGroupsForReporting(final Report report, final WizardContext wizardContext, final FlowType[] flows,
                                          final List<Question> groups) {
        for (final Iterator<Question> groupIt = groups.iterator(); groupIt.hasNext();) {
            final Question group = groupIt.next();
            if (DEBUG_TRACE) {
                blockDebug.startDebugBlock("Group %s (%s) started", group.getName(), group.getTitle());
            }
            final Collection<QuestionVariant> variants = group.getVariants();
            reduceVariantsForReporting(report, wizardContext, flows, variants);

            if (variants.isEmpty()) {
                groupIt.remove();
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Group %s (%s) rejected - no variants", group.getName(), group.getTitle());
                }
            } else {
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Group %s (%s) kept", group.getName(), group.getTitle());
                }
            }
        }
    }

    /**
     * Reduces the question variants for reporting.
     *
     * @author IanBrown
     * @param report
     *            the report.
     * @param wizardContext
     *            the wizard context representing the user.
     * @param flows
     *            the flows available.
     * @param variants
     *            the question variants - variants may be removed on output.
     * @since Jan 24, 2012
     * @version Mar 29, 2012
     */
    private void reduceVariantsForReporting(final Report report, final WizardContext wizardContext, final FlowType[] flows,
                                            final Collection<QuestionVariant> variants) {
        QuestionVariant defaultVariant = null;
        boolean useDefault = true;
        for (final Iterator<QuestionVariant> variantIt = variants.iterator(); variantIt.hasNext();) {
            final QuestionVariant variant = variantIt.next();
            if (DEBUG_TRACE) {
                blockDebug.startDebugBlock("Variant %s started", variant.getTitle());
            }

            if (variant.isDefault()) {
                defaultVariant = variant;
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Variant %s tentative - is default", variant.getTitle());
                }
            } else if (checkVariantDependencies(report, wizardContext, flows, variant)) {
                final Collection<QuestionField> fields = variant.getFields();
                reduceFieldsForReporting(report, wizardContext, flows, fields);

                if (fields.isEmpty()) {
                    if (DEBUG_TRACE) {
                        blockDebug.endDebugBlock("Variant %s rejected - no fields", variant.getTitle());
                    }
                    variantIt.remove();
                } else {
                    if (DEBUG_TRACE) {
                        blockDebug.endDebugBlock("Variant %s kept", variant.getTitle());
                    }
                    useDefault = false;
                }
            } else {
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Variant %s rejected - failed dependencies", variant.getTitle());
                }
                variantIt.remove();
            }
        }

        if (!useDefault && (defaultVariant != null)) {
            variants.remove( defaultVariant );

        } else if (useDefault && (defaultVariant != null)) {
            if (DEBUG_TRACE) {
                blockDebug.startDebugBlock("Default variant %s started", defaultVariant.getTitle());
            }
            final Collection<QuestionField> fields = defaultVariant.getFields();
            reduceFieldsForReporting(report, wizardContext, flows, fields);

            if (fields.isEmpty()) {
                variants.remove(defaultVariant);
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Default Variant %s rejected - no fields", defaultVariant.getTitle());
                }
            } else {
                if (DEBUG_TRACE) {
                    blockDebug.endDebugBlock("Default Variant %s kept", defaultVariant.getTitle());
                }
            }
        }
    }

    /**
     * Converts the variant to a map of report-able fields.
     *
     * @author IanBrown
     * @param variant
     *            the variant.
     * @param groupReportableFields
     *            the report-able fields for the group that owns the variant, by question title.
     * @since Feb 29, 2012
     * @version Feb 29, 2012
     */
    private void variantToReportableFields(final QuestionVariant variant,
                                           final Map<String, Collection<QuestionField>> groupReportableFields) {
        final Collection<QuestionField> questions = variant.getFields();

        for (final QuestionField question : questions) {
            questionToReportableField(question, groupReportableFields);
        }
    }

    /**
     * Used in migration process. Delete answers which depend on removing dictionary items
     * @param items Removing dictionary items
     */
    public void syncAnswersWithMigration( Collection<FieldDictionaryItem> items ) {
        if ( items != null && !items.isEmpty() ) {
            Collection<ReportAnswer> answers = reportingDashboardDAO.findAnswersWithItems( items );
            if ( answers != null && !answers.isEmpty() ) {
                reportingDashboardDAO.makeAllTransient( answers );
            }
        }
    }

    /**
     * Used in migration
     * Delete report fields which depend on removing question fields
     * @param fields removing question fields
     */
    public Collection<Report> syncReportFieldsWithMigration( Collection<QuestionField> fields ) {
        if ( fields == null || fields.isEmpty() ) {
            return Collections.emptyList();
        }
        Collection<ReportField> reportFields = reportingDashboardDAO.findFieldsWithQuestions( fields );
        Collection<Report> reports = new HashSet<Report>();
        if ( reportFields != null && !reportFields.isEmpty() ) {
            Collection<ReportColumn> columns = new ArrayList<ReportColumn>();
            for ( ReportField actualField : reportFields ) {
                columns.add( actualField.getColumn() );
                reports.add( actualField.getColumn().getReport() );
            }
            Collection<ReportAnswer> answers = new ArrayList<ReportAnswer>();
            for ( ReportField reportField : reportFields ) {
                answers.addAll( reportField.getAnswers() );
            }
            reportingDashboardDAO.makeAllTransient( answers );
            reportingDashboardDAO.makeAllTransient( reportFields );
            reportingDashboardDAO.makeAllTransient( columns );
        }
        return reports;
    }


    public void updateReportsColumns( Collection<Report> reports ) {
        for ( Report report : reports ) {
            Report actualReport = reportingDashboardDAO.findReportById( report.getId() );
            int indx = 0;
            for ( ReportColumn column : actualReport.getColumns() ) {
                if ( column != null ) {
                    column.setColumnNumber( indx++ );
                }
            }
            reportingDashboardDAO.makePersistent( actualReport );
        }
    }

}
