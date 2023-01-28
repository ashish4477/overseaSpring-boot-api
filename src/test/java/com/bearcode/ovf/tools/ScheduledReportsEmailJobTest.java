/**
 * 
 */
package com.bearcode.ovf.tools;

import static org.junit.Assert.assertSame;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.apache.commons.mail.EmailException;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.Person;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportTime;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.ReportingDashboardService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;

/**
 * Test for {@link ScheduledReportsEmailJob}.
 * 
 * TODO need test for case where the email send fails.
 * 
 * @author IanBrown
 * 
 * @since Mar 8, 2012
 * @version Jul 23, 2012
 */
public final class ScheduledReportsEmailJobTest extends EasyMockSupport {

	/**
	 * the scheduled reports email job to test.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private ScheduledReportsEmailJob scheduledReportsEmailJob;

	/**
	 * the valet to acquire resources.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private ScheduledReportsEmailJobValet valet;

	/**
	 * the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private ReportingDashboardService reportingDashboardService;

	/**
	 * the email service.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private EmailService emailService;

	/**
	 * the faces service.
	 * 
	 * @author IanBrown
	 * @since Jul 23, 2012
	 * @version Jul 23, 2012
	 */
	private FacesService facesService;

	private OvfPropertyService propertyService;

	/**
	 * Sets up to test the scheduled reports email job.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Jul 23, 2012
	 */
	@Before
	public final void setUpScheduledReportsEmailJob() {
		setValet(createMock("Valet", ScheduledReportsEmailJobValet.class));
		setReportingDashboardService(createMock("ReportingDashboardService", ReportingDashboardService.class));
		setFacesService(createMock("FacesService", FacesService.class));
		setEmailService(createMock("EmailService", EmailService.class));
		setPropertyService(createMock("OvfPropertyService", OvfPropertyService.class));
		setScheduledReportsEmailJob(new ScheduledReportsEmailJob());
		getScheduledReportsEmailJob().setValet(getValet());
		getScheduledReportsEmailJob().setReportingDashboardService(getReportingDashboardService());
		getScheduledReportsEmailJob().setFacesService(getFacesService());
		getScheduledReportsEmailJob().setEmailService(getEmailService());
		getScheduledReportsEmailJob().setPropertyService(getPropertyService());
	}

	/**
	 * Tears down the scheduled reports email job after testing.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Jul 23, 2012
	 */
	@After
	public final void tearDownScheduledReportsEmailJob() {
		setScheduledReportsEmailJob(null);
		setEmailService(null);
		setFacesService(null);
		setReportingDashboardService(null);
		setValet(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.ScheduledReportsEmailJob#executeInternal(org.quartz.JobExecutionContext)} for
	 * the case where there are no scheduled reports needed.
	 * 
	 * @author IanBrown
	 * 
	 * @throws JobExecutionException
	 *             if there is a problem executing the job.
	 * @since Mar 8, 2012
	 * @version Jul 23, 2012
	 */
	@Test
	public final void testExecuteInternalJobExecutionContext_noScheduledReports() throws JobExecutionException {
		final JobExecutionContext context = createContext();
		final List<ScheduledReport> scheduledReports = new ArrayList<ScheduledReport>();
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		EasyMock.expect(getReportingDashboardService().findScheduledReportsDue()).andReturn(scheduledReports);
		replayAll();

		getScheduledReportsEmailJob().executeInternal(context);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.ScheduledReportsEmailJob#executeInternal(org.quartz.JobExecutionContext)} for
	 * the case where there is a scheduled report with a repeat interval.
	 * 
	 * @author IanBrown
	 * 
	 * @throws JobExecutionException
	 *             if there is a problem executing the job.
	 * @throws ParseException
	 *             if there is a problem parsing the date.
	 * @throws EmailException
	 *             if there is a problem setting up the email service.
	 * @since Mar 8, 2012
	 * @version Jul 23, 2012
	 */
	@Test
	public final void testExecuteInternalJobExecutionContext_repeat() throws JobExecutionException, ParseException, EmailException {
		final JobExecutionContext context = createContext();
		final ScheduledReport scheduledReport = createMock("ScheduledReport", ScheduledReport.class);
		final List<ScheduledReport> scheduledReports = Arrays.asList(scheduledReport);
		EasyMock.expect(getReportingDashboardService().findScheduledReportsDue()).andReturn(scheduledReports);
		final Email email = createMock("Email", Email.class);
		final String sendTo = "user@email.address";
		
		final String reportTitle = "Report Title";

		final Date endDate = ScheduledReport.DATE_FORMATTER.parse("2012-03-08");
		EasyMock.expect(scheduledReport.getNextExecutionDate()).andReturn(endDate);
		
		final ReportTime duration = ReportTime.QUARTERLY;
		EasyMock.expect(scheduledReport.getDuration()).andReturn(duration);
		
		final Date startDate = duration.computeStartDate(endDate);
		final String startDateString = ScheduledReport.DATE_FORMATTER.format(startDate);
		final String endDateString = ScheduledReport.DATE_FORMATTER.format(endDate);

		final Report report = createMock("Report", Report.class);
		final long reportId = 2332l;
		final String firstName = "FirstName";

		EasyMock.expect(getValet().acquireEmail()).andReturn(email);
		
		email.setTemplate( ScheduledReportsEmailJob.SCHEDULED_REPORT_EMAIL_TEMPLATE );
		EasyMock.expectLastCall();
		
		email.addTo( sendTo );
		EasyMock.expectLastCall();
		
		email.setSubject( reportTitle + " for " + startDateString + " to " + endDateString );
		EasyMock.expectLastCall();
		
		EasyMock.expect(email.model("firstName", firstName)).andReturn( null );
		EasyMock.expect(email.model("reportId", Long.toString(reportId))).andReturn(null);
		EasyMock.expect(email.model("reportTitle", reportTitle)).andReturn(null);
		EasyMock.expect(email.model("startDate", startDateString)).andReturn(null);
		EasyMock.expect(email.model("endDate", endDateString)).andReturn(null);
		EasyMock.expect(email.model("priority", RawEmail.Priority.HIGH )).andReturn( null );

		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(user.getUsername()).andReturn(sendTo);
		EasyMock.expect(scheduledReport.getUser()).andReturn(user);
		final Person name = createMock("Name", Person.class);
		EasyMock.expect(user.getName()).andReturn(name);
		EasyMock.expect(name.getFirstName()).andReturn(firstName);
		EasyMock.expect(user.getAssignedFace()).andReturn(null);
		final FaceConfig defaultFace = createMock("DefaultFace", FaceConfig.class);
		EasyMock.expect(getFacesService().findDefaultConfig()).andReturn(defaultFace);
		final String relativePrefix = "relativePrefix";
		EasyMock.expect(defaultFace.getRelativePrefix()).andReturn(relativePrefix );
		EasyMock.expect(getFacesService().getApprovedFileName(ScheduledReportsEmailJob.SCHEDULED_REPORT_EMAIL_TEMPLATE, relativePrefix)).andReturn(ScheduledReportsEmailJob.SCHEDULED_REPORT_EMAIL_TEMPLATE);
		EasyMock.expect(scheduledReport.getReport()).andReturn(report);
		EasyMock.expect(report.getTitle()).andReturn(reportTitle);
		EasyMock.expect(report.getId()).andReturn(reportId);
		
		getEmailService().queue(email);
		
		scheduledReport.setLastSentDate(endDate);
		final ReportTime interval = ReportTime.MONTHLY;
		EasyMock.expect(scheduledReport.getInterval()).andReturn(interval);
		final Date nextExecutionDate = interval.computeNextInterval(endDate);
		scheduledReport.setNextExecutionDate(nextExecutionDate);
		getReportingDashboardService().saveScheduledReport(scheduledReport);
		getReportingDashboardService().flush();
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getScheduledReportsEmailJob().executeInternal(context);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.ScheduledReportsEmailJob#getEmailService()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	@Test
	public final void testGetEmailService() {
		final EmailService actualEmailService = getScheduledReportsEmailJob().getEmailService();

		assertSame("The email service is set", getEmailService(), actualEmailService);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.ScheduledReportsEmailJob#getReportingDashboardService()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	@Test
	public final void testGetReportingDashboardService() {
		final ReportingDashboardService actualReportingDashboardService = getScheduledReportsEmailJob()
				.getReportingDashboardService();

		assertSame("The reporting dashboard service is set", getReportingDashboardService(), actualReportingDashboardService);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.ScheduledReportsEmailJob#getValet()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	@Test
	public final void testGetValet() {
		final ScheduledReportsEmailJobValet actualValet = getScheduledReportsEmailJob().getValet();

		assertSame("The valet is set", getValet(), actualValet);
	}

	/**
	 * Creates a context for the trigger.
	 * 
	 * @author IanBrown
	 * @return the context.
	 * @since Jul 23, 2012
	 * @version Jul 23, 2012
	 */
	private JobExecutionContext createContext() {
		final JobExecutionContext context = createMock("Context", JobExecutionContext.class);
		final Trigger trigger = createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger);
		final JobDataMap jobDataMap = createMock("JobDataMap", JobDataMap.class);
		EasyMock.expect(trigger.getJobDataMap()).andReturn(jobDataMap);
		EasyMock.expect(jobDataMap.get("oneTimeOnly")).andReturn(null);
		final JobDataMap mergedJobDataMap = new JobDataMap();
		mergedJobDataMap.put("propertyService", getPropertyService());
		return context;
	}

	/**
	 * Gets the email service.
	 * 
	 * @author IanBrown
	 * @return the email service.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private EmailService getEmailService() {
		return emailService;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Jul 23, 2012
	 * @version Jul 23, 2012
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @return the reporting dashboard service.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private ReportingDashboardService getReportingDashboardService() {
		return reportingDashboardService;
	}

	/**
	 * Gets the scheduled reports email job.
	 * 
	 * @author IanBrown
	 * @return the scheduledReports email job.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private ScheduledReportsEmailJob getScheduledReportsEmailJob() {
		return scheduledReportsEmailJob;
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private ScheduledReportsEmailJobValet getValet() {
		return valet;
	}

	/**
	 * Sets the email service.
	 * 
	 * @author IanBrown
	 * @param emailService
	 *            the email service to set.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Jul 23, 2012
	 * @version Jul 23, 2012
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @param reportingDashboardService
	 *            the reporting dashboard service to set.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private void setReportingDashboardService(final ReportingDashboardService reportingDashboardService) {
		this.reportingDashboardService = reportingDashboardService;
	}

	/**
	 * Sets the scheduled reports email job.
	 * 
	 * @author IanBrown
	 * @param scheduledReportsEmailJob
	 *            the scheduled reports email job to set.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private void setScheduledReportsEmailJob(final ScheduledReportsEmailJob scheduledReportsEmailJob) {
		this.scheduledReportsEmailJob = scheduledReportsEmailJob;
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private void setValet(final ScheduledReportsEmailJobValet valet) {
		this.valet = valet;
	}

	public OvfPropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(OvfPropertyService propertyService) {
		this.propertyService = propertyService;
	}

}
