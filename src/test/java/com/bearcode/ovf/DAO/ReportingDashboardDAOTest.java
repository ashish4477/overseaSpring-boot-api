/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportAnswer;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;
import com.bearcode.ovf.model.reportingdashboard.ReportField;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link ReportingDashboardDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jan 5, 2012
 * @version Apr 3, 2012
 */
public final class ReportingDashboardDAOTest extends BearcodeDAOCheck<ReportingDashboardDAO> {

	/**
	 * the question field DAO.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	private QuestionFieldDAO questionFieldDAO;

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#deleteAnswer(ReportAnswer)}.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testDeleteAnswer() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		getHibernateTemplate().delete(answer);
		replayAll();

		getBearcodeDAO().deleteAnswer(answer);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#deleteColumn(ReportColumn)}.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test
	public final void testDeleteColumn() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		getHibernateTemplate().delete(column);
		replayAll();

		getBearcodeDAO().deleteColumn(column);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#deleteField(ReportField)}.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test
	public final void testDeleteField() {
		final ReportField field = createMock("Field", ReportField.class);
		getHibernateTemplate().delete(field);
		replayAll();

		getBearcodeDAO().deleteField(field);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#deleteReport(Report)}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Mar 29, 2012
	 */
	@Test
	public final void testDeleteReport() {
		final Report report = createMock("Report", Report.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria scheduledReportCriteria = addCriteriaToSession(session, ScheduledReport.class);
		EasyMock.expect(scheduledReportCriteria.add((Criterion) EasyMock.anyObject())).andReturn(scheduledReportCriteria);
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(scheduledReportCriteria.list()).andReturn(scheduledReports);
		getHibernateTemplate().delete(scheduledReport);
		getHibernateTemplate().delete(report);
		replayAll();

		getBearcodeDAO().deleteReport(report);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#deleteScheduledReport(ScheduledReport)}.
	 * 
	 * @author IanBrown
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@Test
	public final void testDeleteScheduledReport() {
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		getHibernateTemplate().delete(scheduledReport);
		replayAll();

		getBearcodeDAO().deleteScheduledReport(scheduledReport);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findAllReports()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testFindAllReports() {
		final Report standardReport = createMock("StandardReport", Report.class);
		final Report customReport = createMock("CustomReport", Report.class);
		final List<Report> reports = Arrays.asList(standardReport, customReport);
		EasyMock.expect(getHibernateTemplate().loadAll(Report.class)).andReturn(reports);
		replayAll();

		final List<Report> actualReports = getBearcodeDAO().findAllReports();

		assertSame("The reports are found", reports, actualReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findAllScheduledReports()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testFindAllScheduledReports() {
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(getHibernateTemplate().loadAll(ScheduledReport.class)).andReturn(scheduledReports);
		replayAll();

		final List<ScheduledReport> actualScheduledReports = getBearcodeDAO().findAllScheduledReports();

		assertSame("The scheduled reports are found", scheduledReports, actualScheduledReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findAnswerById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testFindAnswerById() {
		final long id = 62826l;
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		EasyMock.expect(getHibernateTemplate().get(ReportAnswer.class, id)).andReturn(answer);
		replayAll();

		final ReportAnswer actualAnswer = getBearcodeDAO().findAnswerById(id);

		assertSame("The answer is found", answer, actualAnswer);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findColumnById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testFindColumnById() {
		final long id = 45326l;
		final ReportColumn column = createMock("Column", ReportColumn.class);
		EasyMock.expect(getHibernateTemplate().get(ReportColumn.class, id)).andReturn(column);
		replayAll();

		final ReportColumn actualColumn = getBearcodeDAO().findColumnById(id);

		assertSame("The column is found", column, actualColumn);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findFaceReports(FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 28, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testFindFaceReports() {
		final FaceConfig currentFace = createMock("CurrentFace", FaceConfig.class);
		final String urlPath = "www.face/vote";
		EasyMock.expect(currentFace.getUrlPath()).andReturn(urlPath);
		final Report standardReport = createMock("StandardReport", Report.class);
		final Report customReport = createMock("CustomReport", Report.class);
		final Report faceReport = createMock("FaceReport", Report.class);
		final List<Report> reports = Arrays.asList(standardReport, customReport, faceReport);
		EasyMock.expect(getHibernateTemplate().loadAll(Report.class)).andReturn(reports);
		EasyMock.expect(standardReport.isStandard()).andReturn(true).anyTimes();
		EasyMock.expect(customReport.isStandard()).andReturn(false).anyTimes();
		EasyMock.expect(customReport.isApplyFaces()).andReturn(true).anyTimes();
		final String otherUrlPath = "www.other/vote";
		EasyMock.expect(customReport.getFaces()).andReturn(new HashSet<String>(Arrays.asList(otherUrlPath))).anyTimes();
		EasyMock.expect(faceReport.isStandard()).andReturn(false).anyTimes();
		EasyMock.expect(faceReport.isApplyFaces()).andReturn(true).anyTimes();
		EasyMock.expect(faceReport.getFaces()).andReturn(new HashSet<String>(Arrays.asList(urlPath))).anyTimes();
		replayAll();

		final List<Report> actualFaceReports = getBearcodeDAO().findFaceReports(currentFace);

		final List<Report> expectedFaceReports = Arrays.asList(faceReport);
		assertEquals("The face reports are found", expectedFaceReports, actualFaceReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findFieldById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testFindFieldById() {
		final long id = 6107l;
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(getHibernateTemplate().get(ReportField.class, id)).andReturn(field);
		replayAll();

		final ReportField actualField = getBearcodeDAO().findFieldById(id);

		assertSame("The field is found", field, actualField);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findReportById(long)}.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testFindReportById() {
		final long id = 78712l;
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(getHibernateTemplate().get(Report.class, id)).andReturn(report);
		replayAll();

		final Report actualReport = getBearcodeDAO().findReportById(id);

		assertSame("The report is found", report, actualReport);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findStandardReportById(long)}.
	 * 
	 * @author IanBrown
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testFindScheduledReportById() {
		final long id = 78712l;
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		EasyMock.expect(getHibernateTemplate().get(ScheduledReport.class, id)).andReturn(scheduledReport);
		replayAll();

		final ScheduledReport actualScheduledReport = getBearcodeDAO().findScheduledReportById(id);

		assertSame("The scheduled report is found", scheduledReport, actualScheduledReport);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findScheduledReports(OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 6, 2012
	 * @version Mar 6, 2012
	 */
	@Test
	public final void testFindScheduledReports() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria scheduledReportCriteria = addCriteriaToSession(session, ScheduledReport.class);
		EasyMock.expect(scheduledReportCriteria.add((Criterion) EasyMock.anyObject())).andReturn(scheduledReportCriteria);
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(scheduledReportCriteria.list()).andReturn(scheduledReports);
		replayAll();

		final List<ScheduledReport> actualScheduledReports = getBearcodeDAO().findScheduledReports(user);

		assertSame("The scheduled reports are found", scheduledReports, actualScheduledReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findScheduledReportsDue()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 9, 2012
	 * @version Mar 9, 2012
	 */
	@Test
	public final void testFindScheduledReportsDue() {
		final Session session = addSessionToHibernateTemplate();
		final Criteria scheduledReportCriteria = addCriteriaToSession(session, ScheduledReport.class);
		EasyMock.expect(scheduledReportCriteria.add((Criterion) EasyMock.anyObject())).andReturn(scheduledReportCriteria).times(2);
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(scheduledReportCriteria.list()).andReturn(scheduledReports);
		replayAll();

		final List<ScheduledReport> actualScheduledReports = getBearcodeDAO().findScheduledReportsDue();

		assertSame("The scheduled reports are found", scheduledReports, actualScheduledReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findStandardReports()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Mar 2, 2012
	 */
	@Test
	public final void testFindStandardReports() {
		final Session session = addSessionToHibernateTemplate();
		final Criteria standardReportCriteria = addCriteriaToSession(session, Report.class);
		EasyMock.expect(standardReportCriteria.add((Criterion) EasyMock.anyObject())).andReturn(standardReportCriteria);
		final Report standardReport = createMock("StandardReport", Report.class);
		final String standardReportTitle = "Standard Report Title";
		EasyMock.expect(standardReport.getTitle()).andReturn(standardReportTitle);
		final List<Report> standardReportsList = Arrays.asList(standardReport);
		EasyMock.expect(standardReportCriteria.list()).andReturn(standardReportsList);
		replayAll();

		final Map<String, Report> actualStandardReports = getBearcodeDAO().findStandardReports();

		final Map<String, Report> expectedStandardReports = new HashMap<String, Report>();
		expectedStandardReports.put(standardReportTitle.replace(' ', '_'), standardReport);
		assertEquals("The standard reports are found", expectedStandardReports, actualStandardReports);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#findUserReports(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testFindUserReports() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria userReportCriteria = addCriteriaToSession(session, Report.class);
		EasyMock.expect(userReportCriteria.add((Criterion) EasyMock.anyObject())).andReturn(userReportCriteria);
		final Report userReport = createMock("UserReport", Report.class);
		final List<Report> userReports = Arrays.asList(userReport);
		EasyMock.expect(userReportCriteria.list()).andReturn(userReports);
		replayAll();

		final List<Report> actualUserReports = getBearcodeDAO().findUserReports(user);

		assertSame("The user reports are found", userReports, actualUserReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#getQuestionFieldDAO()}.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	@Test
	public final void testGetQuestionFieldDAO() {
		final QuestionFieldDAO actualQuestionFieldDAO = getBearcodeDAO().getQuestionFieldDAO();

		assertSame("The question field DAO is set", getQuestionFieldDAO(), actualQuestionFieldDAO);
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where the
	 * only column is an Age Group column.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testReport_ageGroupColumn() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Age Group").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		final long columnData = 9812l;
		final AgeRange ageGroup = AgeRange.AGE_18_24;
		final List result = new ArrayList();
		result.add(new Object[] { BigInteger.valueOf(columnData), ageGroup.value });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where the
	 * faces are applied.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testReport_applyFaces() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(true).anyTimes();
		EasyMock.expect(report.getFaces()).andReturn(null).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Number").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(new Object[] { BigInteger.valueOf(columnData) });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is a
	 * start and an end date.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 30, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_dateRange() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Date endDate = new Date();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Number").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateTo"), EasyMock.anyObject())).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(BigInteger.valueOf(columnData));
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, startDate, endDate);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is an
	 * end date.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 30, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_endDate() {
		final Report report = createMock("Report", Report.class);
		final Date endDate = new Date();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Number").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateTo"), EasyMock.anyObject())).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(BigInteger.valueOf(columnData));
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, endDate);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where specific
	 * faces are to be applied.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_faces() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(true).atLeastOnce();
		final String face = "face";
		final Set<String> faces = new HashSet<String>(Arrays.asList(face));
		EasyMock.expect(report.getFaces()).andReturn(faces).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Face").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("faces", faces)).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(new Object[] { BigInteger.valueOf(columnData) });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where specific
	 * faces are to be applied, the user has an assigned face that isn't in the list, but also has administrative privileges.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_facesAdminWithAssignedFace() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final String assignedFaceName = "Assigned Face Name";
		addAssignedFaceToUser(user, assignedFaceName);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(true).atLeastOnce();
		final String face = "face";
		final Set<String> faces = new HashSet<String>(Arrays.asList(face));
		EasyMock.expect(report.getFaces()).andReturn(faces).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Hosted Site").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).anyTimes();
		EasyMock.expect(field.getUserFieldName()).andReturn("face_name").anyTimes();
		EasyMock.expect(field.getAnswers()).andReturn(null).anyTimes();
		final long columnId = 98922l;
		EasyMock.expect(column.getId()).andReturn(columnId).anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", org.hibernate.type.TextType.INSTANCE)).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("faces", faces)).andReturn(sqlQuery);
		final List result = new ArrayList();
		result.add(new Object[] { face });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#buildReportData(Report)} for the case where specific faces
	 * are to be applied, the user has an assigned face that is in the list.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_facesIncludingAssignedFace() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		final String assignedFaceName = "Assigned Face Name";
		addAssignedFaceToUser(user, assignedFaceName);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(true).atLeastOnce();
		final String face = "face";
		final Set<String> faces = new HashSet<String>(Arrays.asList(face, assignedFaceName));
		EasyMock.expect(report.getFaces()).andReturn(faces).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Face").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("faces", Arrays.asList(assignedFaceName))).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(new Object[] { BigInteger.valueOf(columnData) });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where specific
	 * faces are to be applied, but the user's assigned face is not one of them.
	 * 
	 * @author IanBrown
	 * @since Feb 8, 2012
	 * @version Mar 2, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testReport_facesNotIncludingAssignedFace() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		final String assignedFaceName = "Assigned Face Name";
		addAssignedFaceToUser(user, assignedFaceName);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(true).atLeastOnce();
		final String face = "face";
		final Set<String> faces = new HashSet<String>(Arrays.asList(face));
		EasyMock.expect(report.getFaces()).andReturn(faces).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Face").anyTimes();
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertTrue("An empty list is returned", actualReportData.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is a
	 * flow to apply.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_flow() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(true).atLeastOnce();
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		EasyMock.expect(report.getFlowType()).andReturn(flowType).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Flow Type").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).anyTimes();
		EasyMock.expect(field.getAnswers()).andReturn(null).anyTimes();
		EasyMock.expect(field.getUserFieldName()).andReturn("flow_type").anyTimes();
		final Long columnId = 1298l;
		EasyMock.expect(column.getId()).andReturn(columnId).anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", org.hibernate.type.TextType.INSTANCE)).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter("flowType", flowType.name())).andReturn(sqlQuery);
		final List result = new ArrayList();
		result.add(new Object[] { FlowType.DOMESTIC_ABSENTEE });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there
	 * are no columns in the report.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Apr 2, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testReport_noColumns() {
		final Report report = createMock("Report", Report.class);
		EasyMock.expect(report.getColumns()).andReturn(null);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertNotNull("Report data is returned", actualReportData);
		assertTrue("There are no rows in the report data", actualReportData.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where no
	 * report is provided.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 2, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testReport_noReport() {
		getBearcodeDAO().report(null, null, null);
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where the
	 * only column is a Number column.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testReport_numberColumn() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Number").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(BigInteger.valueOf(columnData));
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@Link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where the
	 * only column is a Percentage column.
	 * 
	 * @author IanBrown
	 * @since Mar 2, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testReport_percentageColumn() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).anyTimes();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Percentage").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(BigInteger.valueOf(columnData));
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is
	 * just a question column with both text and predefined answers for which a predefined value is returned.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 3, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_questionColumnAnswersPredefinedValue() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Predefined").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).atLeastOnce();
		final QuestionField fieldQuestion = createMock("FieldQuestion", QuestionField.class);
		EasyMock.expect(field.getQuestion()).andReturn(fieldQuestion).atLeastOnce();
		final long fieldQuestionId = 672l;
		EasyMock.expect(fieldQuestion.getId()).andReturn(fieldQuestionId).atLeastOnce();
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final ReportAnswer predefinedAnswer = createMock("PredefinedAnswer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer, predefinedAnswer);
		EasyMock.expect(field.getAnswers()).andReturn(answers).atLeastOnce();
		EasyMock.expect(answer.getAnswer()).andReturn("Answer");
		EasyMock.expect(predefinedAnswer.getAnswer()).andReturn(null);
		final FieldDictionaryItem dictionaryItem = createMock("DictionaryItem", FieldDictionaryItem.class);
		EasyMock.expect(predefinedAnswer.getPredefinedAnswer()).andReturn(dictionaryItem);
		final long predefinedId = 23254l;
		EasyMock.expect(dictionaryItem.getId()).andReturn(predefinedId);
		final long columnId = 467762l;
		EasyMock.expect(column.getId()).andReturn(columnId).atLeastOnce();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("fields0", Arrays.asList(fieldQuestionId))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("values0", Arrays.asList("Answer"))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("selvalues0", Arrays.asList(predefinedId))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("selected_value_0", StandardBasicTypes.LONG)).andReturn(sqlQuery);
		final long countData = 127l;
		final List result = new ArrayList();
		result.add(new Object[] { countData, null, predefinedId });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is
	 * just a question column with answers for which a value is returned.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 3, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_questionColumnAnswersValue() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Answer").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).atLeastOnce();
		final QuestionField fieldQuestion = createMock("FieldQuestion", QuestionField.class);
		EasyMock.expect(field.getQuestion()).andReturn(fieldQuestion).atLeastOnce();
		final long fieldQuestionId = 672l;
		EasyMock.expect(fieldQuestion.getId()).andReturn(fieldQuestionId).atLeastOnce();
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer);
		EasyMock.expect(field.getAnswers()).andReturn(answers).atLeastOnce();
		final String columnData = "Column";
		EasyMock.expect(answer.getAnswer()).andReturn(columnData).atLeastOnce();
		final long columnId = 467762l;
		EasyMock.expect(column.getId()).andReturn(columnId).atLeastOnce();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("fields0", Arrays.asList(fieldQuestionId))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("values0", Arrays.asList(columnData))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("selected_value_0", StandardBasicTypes.LONG)).andReturn(sqlQuery);
		final long countData = 127l;
		final List result = new ArrayList();
		result.add(new Object[] { countData, columnData, null });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is
	 * just a question column without any answers for which a predefined value is returned.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_questionColumnNoAnswersPredefinedValue() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Predefined").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).atLeastOnce();
		final QuestionField fieldQuestion = createMock("FieldQuestion", QuestionField.class);
		EasyMock.expect(field.getQuestion()).andReturn(fieldQuestion).atLeastOnce();
		final long fieldQuestionId = 672l;
		EasyMock.expect(fieldQuestion.getId()).andReturn(fieldQuestionId).atLeastOnce();
		EasyMock.expect(field.getAnswers()).andReturn(null).atLeastOnce();
		final long columnId = 467762l;
		EasyMock.expect(column.getId()).andReturn(columnId).atLeastOnce();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("fields0", Arrays.asList(fieldQuestionId))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("selected_value_0", StandardBasicTypes.LONG)).andReturn(sqlQuery);
		final long countData = 127l;
		final long predefinedId = 23254l;
		final List result = new ArrayList();
		result.add(new Object[] { countData, null, predefinedId });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is
	 * just a question column without any answers for which a value is returned.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_questionColumnNoAnswersValue() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Answer").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).atLeastOnce();
		final QuestionField fieldQuestion = createMock("FieldQuestion", QuestionField.class);
		EasyMock.expect(field.getQuestion()).andReturn(fieldQuestion).atLeastOnce();
		final long fieldQuestionId = 672l;
		EasyMock.expect(fieldQuestion.getId()).andReturn(fieldQuestionId).atLeastOnce();
		EasyMock.expect(field.getAnswers()).andReturn(null).atLeastOnce();
		final long columnId = 467762l;
		EasyMock.expect(column.getId()).andReturn(columnId).atLeastOnce();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("fields0", Arrays.asList(fieldQuestionId))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("selected_value_0", StandardBasicTypes.LONG)).andReturn(sqlQuery);
		final long countData = 127l;
		final String columnData = "Column";
		final List result = new ArrayList();
		result.add(new Object[] { countData, columnData, null });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#buildReportData(Report)} for the case where there is just a
	 * question column with just predefined answers for which a predefined value is returned.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Apr 3, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_questionColumnPredefinedAnswersPredefinedValue() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Predefined").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).atLeastOnce();
		final QuestionField fieldQuestion = createMock("FieldQuestion", QuestionField.class);
		EasyMock.expect(field.getQuestion()).andReturn(fieldQuestion).atLeastOnce();
		final long fieldQuestionId = 672l;
		EasyMock.expect(fieldQuestion.getId()).andReturn(fieldQuestionId).atLeastOnce();
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer);
		EasyMock.expect(field.getAnswers()).andReturn(answers).atLeastOnce();
		EasyMock.expect(answer.getAnswer()).andReturn(null);
		final FieldDictionaryItem dictionaryItem = createMock("DictionaryItem", FieldDictionaryItem.class);
		EasyMock.expect(answer.getPredefinedAnswer()).andReturn(dictionaryItem);
		final long predefinedId = 23254l;
		EasyMock.expect(dictionaryItem.getId()).andReturn(predefinedId);
		final long columnId = 467762l;
		EasyMock.expect(column.getId()).andReturn(columnId).atLeastOnce();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("fields0", Arrays.asList(fieldQuestionId))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList("selvalues0", Arrays.asList(predefinedId))).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("selected_value_0", StandardBasicTypes.LONG)).andReturn(sqlQuery);
		final long countData = 127l;
		final List result = new ArrayList();
		result.add(new Object[] { countData, null, predefinedId });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is a
	 * start date.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 30, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_startDate() {
		final Report report = createMock("Report", Report.class);
		final Date startDate = new Date();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Number").anyTimes();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		final long columnData = 9812l;
		final List result = new ArrayList();
		result.add(BigInteger.valueOf(columnData));
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, startDate, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is
	 * just a user field column with answers.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_userFieldColumnAnswers() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Answer").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).atLeastOnce();
		final String userFieldName = "user_field_name";
		EasyMock.expect(field.getUserFieldName()).andReturn(userFieldName).atLeastOnce();
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer);
		EasyMock.expect(field.getAnswers()).andReturn(answers).atLeastOnce();
		final long columnId = 6829l;
		EasyMock.expect(column.getId()).andReturn(columnId).atLeastOnce();
		final String answerText = "Answer Text";
		EasyMock.expect(answer.getAnswer()).andReturn(answerText);
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameterList(EasyMock.eq("values0"), EasyMock.eq(Arrays.asList(answerText)))).andReturn(
				sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		final long countData = 123l;
		final String columnData = "ColumnWithAnswers";
		final List result = new ArrayList();
		result.add(new Object[] { countData, columnData });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.ReportingDashboardDAO#report(Report, Date, Date)} for the case where there is
	 * just a user field column without any answers.
	 * 
	 * @author IanBrown
	 * @since Jan 30, 2012
	 * @version Mar 28, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testReport_userFieldColumnNoAnswers() {
		final Report report = createMock("Report", Report.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		addAssignedFaceToUser(user, null);
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		EasyMock.expect(report.getColumns()).andReturn(columns);
		EasyMock.expect(report.isApplyFaces()).andReturn(false).atLeastOnce();
		EasyMock.expect(report.isApplyFlow()).andReturn(false).atLeastOnce();
		EasyMock.expect(column.isQuestionColumn()).andReturn(false).anyTimes();
		EasyMock.expect(column.isUserFieldColumn()).andReturn(true).anyTimes();
		EasyMock.expect(column.getName()).andReturn("Answer").anyTimes();
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		EasyMock.expect(column.getFields()).andReturn(fields).atLeastOnce();
		final String userFieldName = "user_field_name";
		EasyMock.expect(field.getUserFieldName()).andReturn(userFieldName).atLeastOnce();
		EasyMock.expect(field.getAnswers()).andReturn(null).atLeastOnce();
		final long columnId = 56278l;
		EasyMock.expect(column.getId()).andReturn(columnId).atLeastOnce();
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("counter")).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.setParameter(EasyMock.eq("dateFrom"), EasyMock.anyObject())).andReturn(sqlQuery);
		EasyMock.expect(sqlQuery.addScalar("value_0", StandardBasicTypes.TEXT)).andReturn(sqlQuery);
		final long countData = 672l;
		final String columnData = "Column";
		final List result = new ArrayList();
		result.add(new Object[] { countData, columnData });
		EasyMock.expect(sqlQuery.list()).andReturn(result);
		replayAll();

		final List actualReportData = getBearcodeDAO().report(report, null, null);

		assertSame("The result is returned", result, actualReportData);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final ReportingDashboardDAO createBearcodeDAO() {
		final ReportingDashboardDAO reportingDashboardDAO = new ReportingDashboardDAO();
		setQuestionFieldDAO(createMock("QuestionFieldDAO", QuestionFieldDAO.class));
		reportingDashboardDAO.setQuestionFieldDAO(getQuestionFieldDAO());
		return reportingDashboardDAO;
	}

	/**
	 * Adds an assigned face with the specified face name to the user or no assigned face if the name is <code>null</code>.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @param faceName
	 *            the name of the assigned face or <code>null</code> if no assigned face.
	 * @return the assigned face configuration.
	 * @since Feb 8, 2012
	 * @version Mar 27, 2012
	 */
	private FaceConfig addAssignedFaceToUser(final OverseasUser user, final String faceName) {
		FaceConfig assignedFace;

		if (faceName == null) {
			assignedFace = null;
		} else {
			assignedFace = createMock("AssignedFace", FaceConfig.class);
			EasyMock.expect(assignedFace.getUrlPath()).andReturn(faceName).anyTimes();
		}

		EasyMock.expect(user.getAssignedFace()).andReturn(assignedFace).anyTimes();

		return assignedFace;
	}

	/**
	 * Gets the question field DAO.
	 * 
	 * @author IanBrown
	 * @return the question field DAO.
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	private QuestionFieldDAO getQuestionFieldDAO() {
		return questionFieldDAO;
	}

	/**
	 * Sets the questionFieldDAO.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the questionFieldDAO to set.
	 * @since Jan 30, 2012
	 * @version Jan 30, 2012
	 */
	private void setQuestionFieldDAO(final QuestionFieldDAO questionFieldDAO) {
		this.questionFieldDAO = questionFieldDAO;
	}

}
