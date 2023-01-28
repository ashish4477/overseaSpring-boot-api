/**
 * 
 */
package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.reportingdashboard.*;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Extended {@link BearcodeDAO} to support reporting dashboard objects.
 * 
 * @author IanBrown
 * 
 * @since Jan 4, 2012
 * @version May 2, 2012
 */
@Repository
public class ReportingDashboardDAO extends BearcodeDAO {

	/**
	 * the question field DAO.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	@Autowired
	private QuestionFieldDAO questionFieldDAO;

	/**
	 * Deletes the answer.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer to delete.
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	public void deleteAnswer(final ReportAnswer answer) {
		getHibernateTemplate().delete(answer);
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
		getHibernateTemplate().delete(column);
	}

	/**
	 * Deletes the field.
	 * 
	 * @author IanBrown
	 * @param field
	 *            the field.
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	public void deleteField(final ReportField field) {
		getHibernateTemplate().delete(field);
	}

	/**
	 * Deletes the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @since Jan 11, 2012
	 * @version Mar 29, 2012
	 */
	public void deleteReport(final Report report) {
		final List<ScheduledReport> scheduledReports = findScheduledReports(report);
		for (final ScheduledReport scheduledReport : scheduledReports) {
			deleteScheduledReport(scheduledReport);
		}
		getHibernateTemplate().delete(report);
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
		getHibernateTemplate().delete(scheduledReport);
	}

	/**
	 * Finds all of the reports.
	 * 
	 * @author IanBrown
	 * @return the reports.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public List<Report> findAllReports() {
		return getHibernateTemplate().loadAll(Report.class);
	}

	/**
	 * Finds all of the scheduled reports.
	 * 
	 * @author IanBrown
	 * @return the scheduled reports.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public List<ScheduledReport> findAllScheduledReports() {
		return getHibernateTemplate().loadAll( ScheduledReport.class );
	}

	/**
	 * Finds the answer with the specified identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @return the answer.
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	public ReportAnswer findAnswerById(final long id) {
		return getHibernateTemplate().get( ReportAnswer.class, id );
	}

	/**
	 * Finds a report column by its identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the column identifier.
	 * @return the report column or <code>null</code>.
	 * @since Jan 10, 2012
	 * @version Jan 10, 2012
	 */
	public ReportColumn findColumnById(final long id) {
		return getHibernateTemplate().get(ReportColumn.class, id);
	}

	/**
	 * Finds the custom reports for the specified face.
	 * 
	 * @author IanBrown
	 * @param currentFace
	 *            the current face.
	 * @return the custom reports.
	 * @since Mar 28, 2012
	 * @version Mar 28, 2012
	 */
	public List<Report> findFaceReports(final FaceConfig currentFace) {
		// This isn't ideal, but faces are handled as a set of strings, which aren't handled
		// properly by Hibernate with Criteria requests. So, we'll get everything and only
		// take the ones for the desired face.
		final String urlPath = currentFace.getUrlPath();
		final List<Report> allReports = findAllReports();
		final List<Report> faceReports = new LinkedList<Report>();
		for (final Report report : allReports) {
			// Take custom reports that belong to all faces or at least the current face.
			if (!report.isStandard() && (!report.isApplyFaces() || report.getFaces().contains(urlPath))) {
				faceReports.add(report);
			}
		}
		return faceReports;
	}

	/**
	 * Finds the report field for the identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the field identifier.
	 * @return the report field or <code>null</code>.
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	public ReportField findFieldById(final long id) {
		return getHibernateTemplate().get(ReportField.class, id);
	}

	/**
	 * Finds a report by its identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @return the report or <code>null</code> if no such report exists.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public Report findReportById(final long id) {
		return getHibernateTemplate().get(Report.class, id);
	}

	/**
	 * Finds the scheduled report with the specified identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @return the scheduled report.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	public ScheduledReport findScheduledReportById(final long id) {
		return getHibernateTemplate().get( ScheduledReport.class, id );
	}

	/**
	 * Finds the reports scheduled by the user.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @return the scheduled reports.
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<ScheduledReport> findScheduledReports(final OverseasUser user) {
		final Criteria criteria = getSession().createCriteria( ScheduledReport.class ).add( Restrictions.eq( "user", user ) )
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	/**
	 * Finds the scheduled reports that are due today.
	 * 
	 * @author IanBrown
	 * @return the scheduled reports that are due.
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<ScheduledReport> findScheduledReportsDue() {
		final Criteria criteria = getSession()
				.createCriteria( ScheduledReport.class )
				.add( Restrictions.le( "nextExecutionDate", new Date() ) )
				.add( Restrictions.or( Restrictions.isNull( "lastSentDate" ),
                        Restrictions.gtProperty( "nextExecutionDate", "lastSentDate" ) ) )
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	/**
	 * Finds the list of standard reports.
	 * 
	 * @author IanBrown
	 * @return the standard reports by name.
	 * @since Jan 4, 2012
	 * @version Mar 2, 2012
	 */
	public Map<String, Report> findStandardReports() {
		final Criteria criteria = getSession().createCriteria(Report.class).add(Restrictions.eq("standard", true))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		final List<?> standardReportsList = criteria.list();

		final Map<String, Report> standardReports = new LinkedHashMap<String, Report>();
		for (final Object standardReportObject : standardReportsList) {
			final Report standardReport = (Report) standardReportObject;
			standardReports.put(standardReport.getTitle().replace(' ', '_').replace('&', '_').replace('/', '_'), standardReport);
		}

		return standardReports;
	}

	/**
	 * Finds the reports for the user.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @return the user's reports.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<Report> findUserReports(final OverseasUser user) {
		final Criteria criteria = getSession().createCriteria(Report.class).add(Restrictions.eq("owner", user))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	/**
	 * Gets the question field DAO.
	 * 
	 * @author IanBrown
	 * @return the question field DAO.
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	public QuestionFieldDAO getQuestionFieldDAO() {
		return questionFieldDAO;
	}

	/**
	 * Produces the report from the database.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param startDate
	 *            the start date.
	 * @param endDate
	 *            the end date.
	 * @return the report.
	 * @since Mar 1, 2012
	 * @version Mar 2, 2012
	 */
	@SuppressWarnings("rawtypes")
	public List report(final Report report, final Date startDate, final Date endDate) {
		if (report == null) {
			throw new IllegalArgumentException("A report must be provided");
		}

		final List<ReportColumn> columns = report.getColumns();
		if ((columns == null) || columns.isEmpty()) {
			return new LinkedList();
		}

		final SQLQuery sqlQuery = buildSqlQuery(report, columns, startDate, endDate);
		if (sqlQuery != null) {
			return sqlQuery.list();
		}

		return new LinkedList();
	}

	/**
	 * Sets the question field DAO.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the question field DAO.
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	public void setQuestionFieldDAO(final QuestionFieldDAO questionFieldDAO) {
		this.questionFieldDAO = questionFieldDAO;
	}

	/**
	 * Adds the age group column to the query string parts.
	 * 
	 * @author IanBrown
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the column.
	 * @param target
	 *            the target for the query.
	 * @param joins
	 *            the joins for the query.
	 * @param wheres
	 *            the wheres for the query.
	 * @param groups
	 *            the groups for the query.
	 * @return the new index.
	 * @since Mar 5, 2012
	 * @version May 2, 2012
	 */
	private int addAgeGroupColumnToQueryString(final int indx, final ReportColumn column, final StringBuffer target,
			final StringBuffer joins, final StringBuffer wheres, final StringBuffer groups) {
		int newIndx = indx;

		target.append(", CASE");
		target.append(" WHEN pdfs.birth_year = 0 THEN '(no age value)'");
		for (final AgeRange ageGroup : AgeRange.values()) {
			target.append(" WHEN SUBDATE(pdfs.created, INTERVAL ").append(ageGroup.minimumYears)
					.append(" year) >= STR_TO_DATE( CONCAT_WS('/', '01',pdfs.birth_month,pdfs.birth_year), '%d/%m/%Y')")
                    .append(" AND SUBDATE(pdfs.created, INTERVAL ").append(ageGroup.maximumYears)
					.append(" year) < STR_TO_DATE( CONCAT_WS('/', '01',pdfs.birth_month,pdfs.birth_year), '%d/%m/%Y') THEN '").append(ageGroup.value).append("'");
		}
		target.append(" ELSE '(no age value)' END AS value_").append(newIndx);

		if (groups.length() == 0) {
			groups.append("GROUP BY ");
		} else {
			groups.append(",");
		}
		groups.append(" value_").append( newIndx );

		++newIndx;
		return newIndx;
	}

	/**
	 * Adds the values for the age group column to the SQL query.
	 * 
	 * @author IanBrown
	 * @param sqlQuery
	 *            the SQL query.
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the column.
	 * @return the new index.
	 * @since Mar 5, 2012
	 * @version Mar 5, 2012
	 */
	private int addAgeGroupColumnValuesToSQLQuery(final SQLQuery sqlQuery, final int indx, final ReportColumn column) {
		int newIndx = indx;

		sqlQuery.addScalar( "value_" + newIndx, StandardBasicTypes.TEXT );
		
		++newIndx;
		return newIndx;
	}

	/**
	 * Adds the specified column to the query string parts.
	 * 
	 * @author IanBrown
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the column.
	 * @param columnQuestions
	 *            the questions for the columns.
	 * @param columnAnswers
	 *            the allowable answers for the columns.
	 * @param columnPredefinedAnswers
	 *            the allowable predefined answers for the columns.
	 * @param target
	 *            the target for the query.
	 * @param joins
	 *            the joins for the query.
	 * @param wheres
	 *            the wheres for the query.
	 * @param groups
	 *            the groups for the query.
	 * @return the new index.
	 * @since Jan 30, 2012
	 * @version Mar 5, 2012
	 */
	private int addColumnToQueryString(final int indx, final ReportColumn column, final Map<Long, List<Long>> columnQuestions,
			final Map<Long, List<String>> columnAnswers, final Map<Long, List<Long>> columnPredefinedAnswers,
			final StringBuffer target, final StringBuffer joins, final StringBuffer wheres, final StringBuffer groups) {
		final String columnName = column.getName();
		if (columnName.equals("Number") || columnName.equals("Percentage")) {
			return indx;
		} else if (columnName.equals("Age Group")) {
			return addAgeGroupColumnToQueryString( indx, column, target, joins, wheres, groups );
		} else if (column.isQuestionColumn()) {
			return addQuestionColumnToQueryString( indx, column, columnQuestions, columnAnswers, columnPredefinedAnswers, target,
                    joins, wheres, groups );
		} else if (column.isUserFieldColumn()) {
			return addUserFieldColumnToQueryString( indx, column, columnAnswers, target, joins, wheres, groups );
		}

		return indx;
	}

	/**
	 * Adds the values for the column to the SQL query.
	 * 
	 * @author IanBrown
	 * @param sqlQuery
	 *            the SQL query.
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the column.
	 * @param columnQuestions
	 *            the questions for the columns.
	 * @param columnAnswers
	 *            the answers for the columns.
	 * @param columnPredefinedAnswers
	 *            the predefined answers for the columns.
	 * @return the new index.
	 * @since Jan 30, 2012
	 * @version Mar 5, 2012
	 */
	private int addColumnValuesToSQLQuery(final SQLQuery sqlQuery, final int indx, final ReportColumn column,
			final Map<Long, List<Long>> columnQuestions, final Map<Long, List<String>> columnAnswers,
			final Map<Long, List<Long>> columnPredefinedAnswers) {
		final String columnName = column.getName();
		if (columnName.equals("Number") || columnName.equals("Percentage")) {
			return indx;
		} else if (columnName.equals("Age Group")) {
			return addAgeGroupColumnValuesToSQLQuery( sqlQuery, indx, column );
		} else if (column.isQuestionColumn()) {
			return addQuestionColumnValuesToSQLQuery( sqlQuery, indx, column, columnQuestions, columnAnswers,
                    columnPredefinedAnswers );
		} else if (column.isUserFieldColumn()) {
			return addUserFieldColumnValuesToSQLQuery( sqlQuery, indx, column, columnAnswers );
		}

		return indx;
	}

	/**
	 * Adds the question column to the query string parts.
	 * 
	 * @author IanBrown
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the question column.
	 * @param columnQuestions
	 *            the questions for the columns.
	 * @param columnAnswers
	 *            the allowable answers for the columns.
	 * @param columnPredefinedAnswers
	 *            the allowable predefined answers for the columns.
	 * @param target
	 *            the target for the query.
	 * @param joins
	 *            the joins for the query.
	 * @param wheres
	 *            the wheres for the query.
	 * @param groups
	 *            the groups for the query.
	 * @return the next index.
	 * @since Jan 30, 2012
	 * @version Apr 3, 2012
	 */
	private int addQuestionColumnToQueryString(final int indx, final ReportColumn column,
			final Map<Long, List<Long>> columnQuestions, final Map<Long, List<String>> columnAnswers,
			final Map<Long, List<Long>> columnPredefinedAnswers, final StringBuffer target, final StringBuffer joins,
			final StringBuffer wheres, final StringBuffer groups) {
		int newIndx = indx;
		final Collection<ReportField> fields = column.getFields();
		final List<Long> questionsForColumn = new LinkedList<Long>();
		final List<String> answersForColumn = new LinkedList<String>();
		final List<Long> predefinedAnswersForColumn = new LinkedList<Long>();

		for (final ReportField field : fields) {
			questionsForColumn.add(field.getQuestion().getId());

			final Collection<ReportAnswer> answers = field.getAnswers();
			if ((answers != null) && !answers.isEmpty()) {
				for (final ReportAnswer answer : answers) {
					final String answerText = answer.getAnswer();
					if ((answerText != null) && !answerText.isEmpty()) {
						answersForColumn.add(answerText);
					} else {
						final FieldDictionaryItem predefinedAnswer = answer.getPredefinedAnswer();
						if (predefinedAnswer != null) {
							predefinedAnswersForColumn.add(predefinedAnswer.getId());
						}
					}
				}
			}
		}

		final long columnId = column.getId();
		columnQuestions.put(columnId, questionsForColumn);
		joins.append("INNER JOIN answers AS answ_").append(newIndx).append(" ON pdfs.id = answ_").append(newIndx)
				.append(".pdf_answers_id \n");
		target.append(", answ_").append(newIndx).append(".value AS value_").append(newIndx);
		target.append(", answ_").append(newIndx).append(".selected_value AS selected_value_").append(newIndx).append(" ");
		wheres.append("AND answ_").append(newIndx).append(".field_id IN (:fields").append(newIndx).append( ") \n" );

		if (answersForColumn.isEmpty()) {
			if (!predefinedAnswersForColumn.isEmpty()) {
				wheres.append("AND answ_").append(newIndx).append(".selected_value IN (:selvalues").append(newIndx).append(" ) \n");
				columnPredefinedAnswers.put(columnId, predefinedAnswersForColumn);
			}

		} else {
			if (predefinedAnswersForColumn.isEmpty()) {
				wheres.append("AND answ_").append(newIndx).append(".selected_value IN (:selvalues").append(newIndx).append(" ) \n");
			} else {
				wheres.append("AND (").append("answ_").append(newIndx).append(".value IN (:values").append(newIndx).append(") ")
						.append("OR answ_").append(newIndx).append(".selected_value IN (:selvalues").append(newIndx).append(") ")
						.append(" ) \n");
				columnPredefinedAnswers.put(columnId, predefinedAnswersForColumn);
			}
			columnAnswers.put( columnId, answersForColumn );
		}

		if (groups.length() == 0) {
			groups.append("GROUP BY ");
		} else {
			groups.append( "," );
		}
		groups.append("value_").append( newIndx );
		groups.append(", selected_value_").append( newIndx );

		++newIndx;
		return newIndx;
	}

	/**
	 * Adds the values for the question column to the SQL query.
	 * 
	 * @author IanBrown
	 * @param sqlQuery
	 *            the SQL query.
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the column.
	 * @param columnQuestions
	 *            the questions for the columns.
	 * @param columnAnswers
	 *            the answers for the columns.
	 * @param columnPredefinedAnswers
	 *            the predefined answers for the columns.
	 * @return the new index.
	 * @since Jan 30, 2012
	 * @version Mar 15, 2012
	 */
	private int addQuestionColumnValuesToSQLQuery(final SQLQuery sqlQuery, final int indx, final ReportColumn column,
			final Map<Long, List<Long>> columnQuestions, final Map<Long, List<String>> columnAnswers,
			final Map<Long, List<Long>> columnPredefinedAnswers) {
		int newIndx = indx;

		final Long columnId = column.getId();
		final List<Long> questionsForColumn = columnQuestions.get(columnId);
		final List<String> answersForColumn = columnAnswers.get(columnId);
		final List<Long> predefinedAnswersForColumn = columnPredefinedAnswers.get(columnId);
		sqlQuery.setParameterList( "fields" + newIndx, questionsForColumn );
		//System.err.println("fields" + newIndx + " = " + questionsForColumn);
		if ((answersForColumn != null) && !answersForColumn.isEmpty()) {
			sqlQuery.setParameterList( "values" + newIndx, answersForColumn );
			 //System.err.println("values" + newIndx + " = " + answersForColumn);
		}
		if ((predefinedAnswersForColumn != null) && !predefinedAnswersForColumn.isEmpty()) {
			sqlQuery.setParameterList( "selvalues" + newIndx, predefinedAnswersForColumn );
			 //System.err.println("selvalues" + newIndx + " = " + predefinedAnswersForColumn);
		}
		sqlQuery.addScalar( "value_" + newIndx, StandardBasicTypes.TEXT );
		sqlQuery.addScalar( "selected_value_" + newIndx, StandardBasicTypes.LONG );

		++newIndx;
		return newIndx;
	}

	/**
	 * Adds the general report information to the query string parts.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param startDate
	 *            the starting date.
	 * @param endDate
	 *            the ending date
	 * @param assignedFace
	 *            the user's assigned face or <code>null</code> if the user can access any face.
	 * @param target
	 *            the target for the query.
	 * @param joins
	 *            the joins for the query.
	 * @param wheres
	 *            the wheres for the query.
	 * @param groups
	 *            the groups for the query.
	 * @return <code>true</code> if the report contains the assigned face (or there is no assigned face), <code>false</code> if it
	 *         does not.
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	private boolean addReportToQueryString(final Report report, final Date startDate, final Date endDate,
			final FaceConfig assignedFace, final StringBuffer target, final StringBuffer joins, final StringBuffer wheres,
			final StringBuffer groups) {
		if (startDate == null) {
			if (endDate != null) {
				wheres.append("AND pdfs.updated <= :dateTo \n");
			} else {
				wheres.append("AND pdfs.updated >= :dateFrom \n");
			}
		} else {
			if (endDate == null) {
				wheres.append("AND pdfs.updated >= :dateFrom \n");
			} else {
				wheres.append("AND pdfs.updated >= :dateFrom AND pdfs.updated <= :dateTo \n");
			}
		}

		if (report.isApplyFaces() || (!isAdminUser() && (assignedFace != null))) {
			final Set<String> faces = report.getFaces();
			if (!isAdminUser() && (assignedFace != null) && (faces != null) && !faces.isEmpty()
					&& !faces.contains(assignedFace.getUrlPath())) {
				return false;
			}
			if ((!isAdminUser() && (assignedFace != null)) || ((faces != null) && !faces.isEmpty())) {
				wheres.append("AND pdfs.face_name IN (:faces) \n");
			}
		}

		if (report.isApplyFlow()) {
			wheres.append( "AND pdfs.flow_type = :flowType " );
		}

		return true;
	}

	/**
	 * Adds the report values to the SQL query.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param startDate
	 *            the start date.
	 * @param endDate
	 *            the end date.
	 * @param assignedFace
	 *            the user's assigned face or <code>null</code> if the user can access any face.
	 * @param sqlQuery
	 *            the SQL query.
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	private void addReportValuesToSQLQuery(final Report report, final Date startDate, final Date endDate,
			final FaceConfig assignedFace, final SQLQuery sqlQuery) {
		Date localStartDate = startDate;
		if ((startDate == null) && (endDate == null)) {
			final GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(GregorianCalendar.DAY_OF_YEAR, 1);
			localStartDate = calendar.getTime();
		}

		if (localStartDate != null) {
			final GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(localStartDate);
			calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
			calendar.set(GregorianCalendar.MINUTE, 0);
			calendar.set(GregorianCalendar.SECOND, 0);
			calendar.set(GregorianCalendar.MILLISECOND, 0);
			sqlQuery.setParameter("dateFrom", calendar.getTime());
			// System.err.println("dateFrom = " + calendar.getTime());
		}

		if (endDate != null) {
			final GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(endDate);
			calendar.set(GregorianCalendar.HOUR_OF_DAY, 23);
			calendar.set(GregorianCalendar.MINUTE, 59);
			calendar.set(GregorianCalendar.SECOND, 59);
			calendar.set(GregorianCalendar.MILLISECOND, 999);
			sqlQuery.setParameter("dateTo", calendar.getTime());
			// System.err.println("dateTo = " + calendar.getTime());
		}

		if (report.isApplyFaces() || (!isAdminUser() && (assignedFace != null))) {
			if (!isAdminUser() && (assignedFace != null)) {
				final List<String> faces = Arrays.asList(assignedFace.getUrlPath());
				sqlQuery.setParameterList("faces", faces);
				// System.err.println("faces = " + faces);
			} else {
				final Set<String> faces = report.getFaces();
				if ((faces != null) && !faces.isEmpty()) {
					sqlQuery.setParameterList("faces", faces);
					// System.err.println("faces = " + faces);
				}
			}
		}

		if (report.isApplyFlow()) {
			sqlQuery.setParameter( "flowType", report.getFlowType().name() );
			// System.err.println("flowType = " + report.getFlowType().name());
		}
	}

	/**
	 * Adds the specified user field column to the query string parts.
	 * 
	 * @author IanBrown
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the column.
	 * @param columnAnswers
	 *            the column answers.
	 * @param target
	 *            the target for the query.
	 * @param joins
	 *            the joins for the query.
	 * @param wheres
	 *            the wheres for the query.
	 * @param groups
	 *            the groups for the query.
	 * @return the new index.
	 * @since Jan 30, 2012
	 * @version Feb 17, 2012
	 */
	private int addUserFieldColumnToQueryString(final int indx, final ReportColumn column,
			final Map<Long, List<String>> columnAnswers, final StringBuffer target, final StringBuffer joins,
			final StringBuffer wheres, final StringBuffer groups) {
		int newIndx = indx;

		final Collection<ReportField> fields = column.getFields();
		if (fields.size() != 1) {
			throw new UnsupportedOperationException("Only one user field per column is supported");
		}

		final ReportField field = fields.iterator().next();
		final UserFieldNames userField = UserFieldNames.findByUserFieldName(field.getUserFieldName());
        if ( userField != null ) {
            if (userField.getAlias().equals("pdfs")) {
                target.append(", ").append(field.getUserFieldName()).append(" AS value_").append(newIndx);
            } else {
                if ( userField.getSqlKeyName() != null && !userField.getSqlKeyName().isEmpty() ) {
                joins.append("INNER JOIN ").append(userField.getTableName()).append(" AS ").append(userField.getAlias())
                        .append(" ON pdfs.").append(userField.getSqlKeyName()).append("=").append(userField.getAlias()).append(".id ");
                target.append(", ").append(userField.getAlias()).append(".").append(field.getUserFieldName()).append(" AS value_").append(newIndx);
                // If we ever want to support tables other than the PDFs table, we'll want to handle them here by
                // adding the table and its alias to the joins, and use the alias and field name in the target.
                }
                else {
                throw new IllegalArgumentException(
                        "To support tables other than the wizard results (pdf_answers) key field name should be provided");
                }
            }
        }

		final Collection<ReportAnswer> answers = field.getAnswers();
		if ((answers != null) && !answers.isEmpty()) {
			final List<String> answersForColumn = new LinkedList<String>();
			for (final ReportAnswer answer : answers) {
				answersForColumn.add(answer.getAnswer());
			}
			columnAnswers.put( column.getId(), answersForColumn );
			wheres.append("AND pdfs.").append(field.getUserFieldName()).append(" IN (:values").append(newIndx).append( ") \n" );
		}

		if (groups.length() == 0) {
			groups.append("GROUP BY ");
		} else {
			groups.append( "," );
		}
		groups.append(" value_").append(newIndx);

		++newIndx;
		return newIndx;
	}

	/**
	 * Adds the values for the user field column to the SQL query.
	 * 
	 * @author IanBrown
	 * @param sqlQuery
	 *            the SQL query.
	 * @param indx
	 *            the current index.
	 * @param column
	 *            the column.
	 * @param columnAnswers
	 *            the answers for the column.
	 * @return the new index.
	 * @since Jan 30, 2012
	 * @version Mar 15, 2012
	 */
	private int addUserFieldColumnValuesToSQLQuery(final SQLQuery sqlQuery, final int indx, final ReportColumn column,
			final Map<Long, List<String>> columnAnswers) {
		int newIndx = indx;

		final List<String> answersForColumn = columnAnswers.get( column.getId() );
		if ((answersForColumn != null) && !answersForColumn.isEmpty()) {
			sqlQuery.setParameterList( "values" + newIndx, answersForColumn );
			//System.err.println("values" + newIndx + " = " + answersForColumn);
		}
		sqlQuery.addScalar( "value_" + newIndx, StandardBasicTypes.TEXT );

		++newIndx;
		return newIndx;
	}

	/**
	 * Adds the column information to the SQL query.
	 * 
	 * @author IanBrown
	 * @param sqlQuery
	 *            the SQL query.
	 * @param report
	 *            the report.
	 * @param columns
	 *            the columns.
	 * @param startDate
	 *            the start date.
	 * @param endDate
	 *            the end date.
	 * @param assignedFace
	 *            the user's assigned face or <code>null</code> if the user can access any face.
	 * @param columnQuestions
	 *            the questions for the columns.
	 * @param columnAnswers
	 *            the allowable answers for the columns.
	 * @param columnPredefinedAnswers
	 *            the allowable predefined answers for the columns.
	 * @since Jan 30, 2012
	 * @version Mar 2, 2012
	 */
	private void addValuesToSQLQuery(final SQLQuery sqlQuery, final Report report, final List<ReportColumn> columns,
			final Date startDate, final Date endDate, final FaceConfig assignedFace, final Map<Long, List<Long>> columnQuestions,
			final Map<Long, List<String>> columnAnswers, final Map<Long, List<Long>> columnPredefinedAnswers) {
		addReportValuesToSQLQuery(report, startDate, endDate, assignedFace, sqlQuery);

		int indx = 0;
		for (final ReportColumn column : columns) {
			indx = addColumnValuesToSQLQuery(sqlQuery, indx, column, columnQuestions, columnAnswers, columnPredefinedAnswers);
		}
	}

	/**
	 * Builds the query string from the report and its columns.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param columns
	 *            the columns.
	 * @param startDate
	 *            the start date.
	 * @param endDate
	 *            the end date.
	 * @param assignedFace
	 *            the user's assigned face - <code>null</code> means that he/she can access any face.
	 * @param columnQuestions
	 *            the questions for the columns.
	 * @param columnAnswers
	 *            the answers for the columns.
	 * @param columnPredefinedAnswers
	 *            the predefined answers for the columns.
	 * @return the query string
	 * @since Jan 30, 2012
	 * @version Apr 3, 2012
	 */
	private String buildQueryString(final Report report, final List<ReportColumn> columns, final Date startDate,
			final Date endDate, final FaceConfig assignedFace, final Map<Long, List<Long>> columnQuestions,
			final Map<Long, List<String>> columnAnswers, final Map<Long, List<Long>> columnPredefinedAnswers) {
		final StringBuffer target = new StringBuffer("SELECT COUNT(DISTINCT pdfs.id) AS counter");
		final String from = "FROM pdf_answers AS pdfs \n";
		final StringBuffer joins = new StringBuffer("");
		final StringBuffer wheres = new StringBuffer("WHERE pdfs.reportable=1 \n");
		final StringBuffer groups = new StringBuffer("");

		if (!addReportToQueryString(report, startDate, endDate, assignedFace, target, joins, wheres, groups)) {
			return null;
		}

		int indx = 0;
		for (final ReportColumn column : columns) {
			indx = addColumnToQueryString(indx, column, columnQuestions, columnAnswers, columnPredefinedAnswers, target, joins,
					wheres, groups);
		}

		target.append( " \n" );
		final StringBuffer queryString = new StringBuffer(target).append(from).append(joins).append(wheres).append(groups)
				.append(";\n");
		return queryString.toString();
	}

	/**
	 * Builds a SQL query for the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @param columns
	 *            the columns of the report.
	 * @param startDate
	 *            the starting date for the report.
	 * @param endDate
	 *            the ending date for the report.
	 * @return the SQL query or <code>null</code> if the query cannot be built.
	 * @since Jan 30, 2012
	 * @version Mar 15, 2012
	 */
	private SQLQuery buildSqlQuery(final Report report, final List<ReportColumn> columns, final Date startDate, final Date endDate) {
		final FaceConfig assignedFace = determineAssignedFace(report);
		final Map<Long, List<Long>> columnQuestions = new HashMap<Long, List<Long>>();
		final Map<Long, List<String>> columnAnswers = new HashMap<Long, List<String>>();
		final Map<Long, List<Long>> columnPredefinedAnswers = new HashMap<Long, List<Long>>();
		final String queryString = buildQueryString(report, columns, startDate, endDate, assignedFace, columnQuestions,
				columnAnswers, columnPredefinedAnswers);
		if (queryString == null) {
			return null;
		}

		//System.err.println(report);
		//System.err.println(queryString);
		final SQLQuery sqlQuery = createSqlQueryFromString(queryString);
		// System.err.println(sqlQuery.toString());
		addValuesToSQLQuery( sqlQuery, report, columns, startDate, endDate, assignedFace, columnQuestions, columnAnswers,
                columnPredefinedAnswers );
		return sqlQuery;
	}

	/**
	 * Builds the SQL query from the input query string.
	 * 
	 * @author IanBrown
	 * @param queryString
	 *            the query string.
	 * @return the SQL query.
	 * @since Jan 30, 2012
	 * @version Feb 8, 2012
	 */
	private SQLQuery createSqlQueryFromString(final String queryString) {
		final SQLQuery sqlQuery = getSession().createSQLQuery( queryString );
		sqlQuery.addScalar( "counter" );
		return sqlQuery;
	}

	/**
	 * Determines whether the user has an assigned face. If so, he/she can only get reports for that face.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @return the assigned face - <code>null</code> means no assigned face.
	 * @since Feb 8, 2012
	 * @version Feb 8, 2012
	 */
	private FaceConfig determineAssignedFace(final Report report) {
		/*
		 * TS:2414. we need to have a type of admin user that can access only one face in the reporting system and cannot access
		 * other admin functionality. So such user will always get result only for his face.
		 */
		final SecurityContext context = SecurityContextHolder.getContext();
		final Authentication authentication = (context == null) ? null : context.getAuthentication();
		final Object user = (authentication == null) ? null : authentication.getPrincipal();
		if (user instanceof OverseasUser) {
			final OverseasUser oUser = (OverseasUser) user;
			final FaceConfig assignedFace = oUser.getAssignedFace();
			if ((assignedFace != null) && !oUser.isInRole(UserRole.USER_ROLE_ADMIN)) {
				return assignedFace;
			}
		}
		/* END of TS:2414 */

		return null;
	}

	/**
	 * Finds the scheduled entries for the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @return the scheduled reports.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	@SuppressWarnings("unchecked")
	private List<ScheduledReport> findScheduledReports(final Report report) {
		final Criteria criteria = getSession().createCriteria(ScheduledReport.class).add(Restrictions.eq("report", report))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	/**
	 * Determines if the user is an administrative user.
	 * 
	 * @author IanBrown
	 * @return
	 * @since Feb 8, 2012
	 * @version Feb 8, 2012
	 */
	private boolean isAdminUser() {
		final SecurityContext context = SecurityContextHolder.getContext();
		final Authentication authentication = (context == null) ? null : context.getAuthentication();
		final Object user = (authentication == null) ? null : authentication.getPrincipal();
		if (user instanceof OverseasUser) {
			final OverseasUser oUser = (OverseasUser) user;
			return oUser.isInRole(UserRole.USER_ROLE_ADMIN);
		}

		return false;
	}

	/**
	 * Find Report Answers which depend on given Field Dictionary Items
	 * @param items Field Dictionary Items
	 * @return collection of answers
	 */
	@SuppressWarnings("unchecked")
	public Collection<ReportAnswer> findAnswersWithItems( Collection<FieldDictionaryItem> items ) {
		Collection<Long> ids = new ArrayList<Long>(  );
        for ( FieldDictionaryItem item : items ) {
            ids.add( item.getId() );
        }
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( ReportAnswer.class )
                .createAlias( "predefinedAnswer", "predefinedAnswer" )
                .add( Restrictions.in( "predefinedAnswer.id", ids ) );

		return criteria.list();
	}

    /**
     * Find Report Fields which depend on given Question Fields
     * @param fields Question Fields
     * @return collection of Report Fields
     */
    @SuppressWarnings("unchecked")
    public Collection<ReportField> findFieldsWithQuestions( Collection<QuestionField> fields ) {
        Collection<Long> ids = new ArrayList<Long>();
        for ( QuestionField field : fields ) {
            ids.add( field.getId() );
        }
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( ReportField.class )
                .createAlias( "question", "question" )
                .add( Restrictions.in( "question.id", ids ) );
        return criteria.list();
    }
}
