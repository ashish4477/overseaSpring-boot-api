/**
 * 
 */
package com.bearcode.ovf.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportTime;
import com.bearcode.ovf.model.reportingdashboard.ScheduledReport;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.ReportingDashboardService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;

/**
 * Quartz job to email the scheduled reports to their users.
 * 
 * @author IanBrown
 * 
 * @since Mar 8, 2012
 * @version Jul 23, 2012
 */
public class ScheduledReportsEmailJob extends QuartzJobBean implements StatefulJob {

	/**
	 * Basic implementation of {@link ScheduledReportsEmailJobValet}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private final class ScheduledReportsEmailJobValetImpl implements ScheduledReportsEmailJobValet {

		/** {@inheritDoc} */
		@Override
		public final Email acquireEmail() {
			return Email.builder().build();
		}
	}

	/**
	 * has the one time flag fired?
	 * 
	 * @author IanBrown
	 * @since Jul 23, 2012
	 * @version Jul 23, 2012
	 */
	private static boolean oneTimeFired;

	/**
	 * the name of the template used to generate the scheduled reports email.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	public static final String SCHEDULED_REPORT_EMAIL_TEMPLATE = "/WEB-INF/mails/scheduled_report.xml";

	/**
	 * the valet used to acquire resources.
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
	 * the faces service.
	 * 
	 * @author IanBrown
	 * @since Jul 23, 2012
	 * @version Jul 23, 2012
	 */
	private FacesService facesService;

	/**
	 * the email service.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	private EmailService emailService;

	private OvfPropertyService propertyService;

	public OvfPropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(OvfPropertyService propertyService) {
		this.propertyService = propertyService;
	}

	/**
	 * Constructs a scheduled reports email job.
	 * 
	 * @author IanBrown
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	public ScheduledReportsEmailJob() {
		super();
		setValet(new ScheduledReportsEmailJobValetImpl());
	}

	/**
	 * Gets the email service.
	 * 
	 * @author IanBrown
	 * @return the email service.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	public EmailService getEmailService() {
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
	public FacesService getFacesService() {
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
	public ReportingDashboardService getReportingDashboardService() {
		return reportingDashboardService;
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	public ScheduledReportsEmailJobValet getValet() {
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
	public void setEmailService(final EmailService emailService) {
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
	public void setFacesService(final FacesService facesService) {
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
	public void setReportingDashboardService(final ReportingDashboardService reportingDashboardService) {
		this.reportingDashboardService = reportingDashboardService;
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
	public void setValet(final ScheduledReportsEmailJobValet valet) {
		this.valet = valet;
	}

	/** {@inheritDoc} */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		try {
			if (!Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED))) {
				return;
			}
			final boolean oneTimeOnly;
			final JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			final Object value = jobDataMap.get("oneTimeOnly");
			if (value instanceof Boolean) {
				oneTimeOnly = (Boolean) value;
			} else if (value instanceof String) {
				oneTimeOnly = Boolean.parseBoolean((String) value);
			} else {
				oneTimeOnly = false;
			}
			if (oneTimeOnly) {
				if (oneTimeFired) {
					return;
				}
				oneTimeFired = true;
			}
			final List<ScheduledReport> scheduledReports = oneTimeOnly ? getReportingDashboardService().findAllScheduledReports()
					: getReportingDashboardService().findScheduledReportsDue();

			if (!scheduledReports.isEmpty()) {
				for (final ScheduledReport scheduledReport : scheduledReports) {
					try {
						executeScheduledReport(scheduledReport);
					} catch (final EmailException e) {
						throw new JobExecutionException("Failed to email " + scheduledReport.getReport().getTitle() + " to "
								+ scheduledReport.getUser().getName());
					}
				}
				getReportingDashboardService().flush();
			}
		} catch (final Exception e) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.close();
			throw new JobExecutionException("Failed on exception: " + sw.toString());
		}
	}

	/**
	 * Emails the scheduled report to the user that owns it.
	 * 
	 * @author IanBrown
	 * @param scheduledReport
	 *            the scheduled report.
	 * @return the end date for the report.
	 * @throws EmailException
	 *             if there is a problem emailing the report.
	 * @since Mar 8, 2012
	 * @version Jul 23, 2012
	 */
	private Date emailScheduledReport(final ScheduledReport scheduledReport) throws EmailException {
		final OverseasUser user = scheduledReport.getUser();
		final FaceConfig assignedFace = user.getAssignedFace();
		final FaceConfig faceConfig = (assignedFace == null) ? getFacesService().findDefaultConfig() : assignedFace;
		final String template = getFacesService().getApprovedFileName(SCHEDULED_REPORT_EMAIL_TEMPLATE, faceConfig.getRelativePrefix());
		if (StringUtils.isBlank(template)) {
			return null;
		}

		final Report report = scheduledReport.getReport();
		final String reportTitle = report.getTitle();
		final Date endDate = scheduledReport.getNextExecutionDate();
		final ReportTime duration = scheduledReport.getDuration();
		final Date startDate = duration.computeStartDate(endDate);
		final String startDateString = ScheduledReport.DATE_FORMATTER.format(startDate);
		final String endDateString = ScheduledReport.DATE_FORMATTER.format(endDate);
		
		final Email email = getValet().acquireEmail();
		email.setTemplate(template);
		email.addTo( user.getUsername() );
		email.setSubject( reportTitle + " for " + startDateString + " to " + endDateString );
		email.model( "firstName", user.getName().getFirstName() );
		email.model("reportId", Long.toString( report.getId() ));
		email.model("reportTitle", reportTitle);
		email.model("startDate", startDateString);
		email.model("endDate", endDateString);
		email.model("priority", RawEmail.Priority.HIGH );

		getEmailService().queue(email);

		return endDate;
	}

	/**
	 * Executes the scheduled report.
	 * 
	 * @author IanBrown
	 * @param scheduledReport
	 *            the scheduled report.
	 * @throws EmailException
	 *             if there is a problem sending the email.
	 * @since Mar 8, 2012
	 * @version Jul 23, 2012
	 */
	private void executeScheduledReport(final ScheduledReport scheduledReport) throws EmailException {
		final Date endDate = emailScheduledReport(scheduledReport);
		if (endDate != null) {
			updateScheduledReport(scheduledReport, endDate);
		}
	}

	/**
	 * Updates the scheduled report.
	 * 
	 * @author IanBrown
	 * @param scheduledReport
	 *            the scheduled report.
	 * @param endDate
	 *            the end date for the report.
	 * @since Mar 8, 2012
	 * @version Mar 12, 2012
	 */
	private void updateScheduledReport(final ScheduledReport scheduledReport, final Date endDate) {
		scheduledReport.setLastSentDate(endDate);
		final ReportTime interval = scheduledReport.getInterval();
		final Date nextExecutionDate = interval.computeNextInterval(endDate);
		scheduledReport.setNextExecutionDate(nextExecutionDate);
		getReportingDashboardService().saveScheduledReport(scheduledReport);
	}
}
