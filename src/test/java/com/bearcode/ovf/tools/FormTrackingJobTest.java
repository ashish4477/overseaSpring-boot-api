/**
 * 
 */
package com.bearcode.ovf.tools;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.bearcode.ovf.model.formtracking.TrackedForm;
import com.bearcode.ovf.service.FormTrackingService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;

/**
 * Test for {@link FormTrackingJob}.
 * 
 * @author IanBrown
 * 
 * @since Apr 25, 2012
 * @version Jul 20, 2012
 */
public final class FormTrackingJobTest extends EasyMockSupport {

	/**
	 * the form tracking job to test.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FormTrackingJob formTrackingJob;

	/**
	 * the service used to get and update the form tracking information.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FormTrackingService formTrackingService;

	/**
	 * the service used to send the email messages.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private EmailService emailService;

	private OvfPropertyService propertyService;

	/**
	 * Sets up to test the form tracking job.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Before
	public final void setUpFormTrackingJob() {
		setFormTrackingService(createMock("FormTrackingService", FormTrackingService.class));
		setEmailService(createMock("EmailService", EmailService.class));
		setPropertyService(createMock("OvfPropertyService", OvfPropertyService.class));
		setFormTrackingJob(new FormTrackingJob());
	}

	/**
	 * Tears down the set up for testing the form tracking job.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@After
	public final void tearDownFormTrackingJob() {
		setFormTrackingJob(null);
		setEmailService(null);
		setFormTrackingService(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.FormTrackingJob#execute(org.quartz.JobExecutionContext)} for the case where
	 * there are tracked forms that require email be sent.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem scheduling the job.
	 * @throws EmailException
	 *             if there is a problem sending the email.
	 * @since Apr 25, 2012
	 * @version Jul 20, 2012
	 */
	@Test
	public final void testExecute() throws SchedulerException, EmailException {
		final JobExecutionContext context = createJobExecutionContext();
		final Trigger trigger = createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final JobDataMap triggerDataMap = createMock("TriggerDataMap", JobDataMap.class);
		EasyMock.expect(trigger.getJobDataMap()).andReturn(triggerDataMap).anyTimes();
		EasyMock.expect(triggerDataMap.get("oneTimeOnly")).andReturn(false).anyTimes();
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final List<TrackedForm> trackedForms = Arrays.asList(trackedForm);
		EasyMock.expect(getFormTrackingService().findAllTrackedForms()).andReturn(trackedForms).anyTimes();
		getFormTrackingService().setOneTimeOnly(false);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(getFormTrackingService().isOneTimeOnly()).andReturn(false).anyTimes();
		final String emailTemplate = "Email Template";
		EasyMock.expect(getFormTrackingService().determineEmailTemplate(trackedForm)).andReturn(emailTemplate).anyTimes();
		final Email email = createMock("Email", Email.class);
		EasyMock.expect(getFormTrackingService().buildEmail(trackedForm, emailTemplate)).andReturn(email).anyTimes();
		getEmailService().queue(email);
		EasyMock.expect(trackedForm.getEmailAddress()).andReturn("email@somewhere.com");
		getFormTrackingService().saveAfterTrackingEmail(trackedForm);
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getFormTrackingJob().execute(context);

		assertSame("The form tracking service is set", getFormTrackingService(), getFormTrackingJob().getFormTrackingService());
		assertSame("The email service is set", getEmailService(), getFormTrackingJob().getEmailService());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.FormTrackingJob#execute(org.quartz.JobExecutionContext)} for the case where
	 * there are no tracked forms that require an email.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem scheduling the job.
	 * @since Apr 25, 2012
	 * @version Jul 11, 2012
	 */
	@Test
	public final void testExecute_noFormsNeedEmail() throws SchedulerException {
		final JobExecutionContext context = createJobExecutionContext();
		final Trigger trigger = createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final JobDataMap triggerDataMap = createMock("TriggerDataMap", JobDataMap.class);
		EasyMock.expect(trigger.getJobDataMap()).andReturn(triggerDataMap).anyTimes();
		EasyMock.expect(triggerDataMap.get("oneTimeOnly")).andReturn(false).anyTimes();
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final List<TrackedForm> trackedForms = Arrays.asList(trackedForm);
		EasyMock.expect(getFormTrackingService().findAllTrackedForms()).andReturn(trackedForms).anyTimes();
		EasyMock.expect(getFormTrackingService().isOneTimeOnly()).andReturn(false).anyTimes();
		getFormTrackingService().setOneTimeOnly(false);
		EasyMock.expectLastCall().anyTimes();
		final String emailTemplate = FormTrackingService.NO_EMAIL;
		EasyMock.expect(getFormTrackingService().determineEmailTemplate(trackedForm)).andReturn(emailTemplate).anyTimes();
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getFormTrackingJob().execute(context);

		assertSame("The form tracking service is set", getFormTrackingService(), getFormTrackingJob().getFormTrackingService());
		assertSame("The email service is set", getEmailService(), getFormTrackingJob().getEmailService());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.FormTrackingJob#execute(org.quartz.JobExecutionContext)} for the case where
	 * there are no outstanding tracked forms.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem scheduling the job.
	 * @since Apr 25, 2012
	 * @version Jul 20, 2012
	 */
	@Test
	public final void testExecute_noOutstandingForms() throws SchedulerException {
		final JobExecutionContext context = createJobExecutionContext();
		final Trigger trigger = createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final JobDataMap triggerDataMap = createMock("TriggerDataMap", JobDataMap.class);
		EasyMock.expect(trigger.getJobDataMap()).andReturn(triggerDataMap).anyTimes();
		EasyMock.expect(triggerDataMap.get("oneTimeOnly")).andReturn(false).anyTimes();
		final List<TrackedForm> trackedForms = new ArrayList<TrackedForm>();
		EasyMock.expect(getFormTrackingService().findAllTrackedForms()).andReturn(trackedForms).anyTimes();
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getFormTrackingJob().execute(context);

		assertSame("The form tracking service is set", getFormTrackingService(), getFormTrackingJob().getFormTrackingService());
		assertSame("The email service is set", getEmailService(), getFormTrackingJob().getEmailService());
		verifyAll();
	}

	/**
	 * Creates the job execution context.
	 * 
	 * @author IanBrown
	 * @return the job execution context.
	 * @throws SchedulerException
	 *             if there is a problem setting up the job execution context.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private JobExecutionContext createJobExecutionContext() throws SchedulerException {
		final JobExecutionContext context = createMock("JobExecutionContext", JobExecutionContext.class);
		final Scheduler scheduler = createMock("Scheduler", Scheduler.class);
		EasyMock.expect(context.getScheduler()).andReturn(scheduler).anyTimes();
		final SchedulerContext schedulerContext = new SchedulerContext();
		EasyMock.expect(scheduler.getContext()).andReturn(schedulerContext).anyTimes();
		final JobDataMap mergedJobDataMap = new JobDataMap();
		EasyMock.expect(context.getMergedJobDataMap()).andReturn(mergedJobDataMap).anyTimes();
		mergedJobDataMap.put("formTrackingService", getFormTrackingService());
		mergedJobDataMap.put("emailService", getEmailService());
		mergedJobDataMap.put("propertyService", getPropertyService());
		return context;
	}

	/**
	 * Gets the eMail service.
	 * 
	 * @author IanBrown
	 * @return the eMail service.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private EmailService getEmailService() {
		return emailService;
	}

	/**
	 * Gets the form tracking job.
	 * 
	 * @author IanBrown
	 * @return the form tracking job.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FormTrackingJob getFormTrackingJob() {
		return formTrackingJob;
	}

	/**
	 * Gets the form tracking service.
	 * 
	 * @author IanBrown
	 * @return the form tracking service.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FormTrackingService getFormTrackingService() {
		return formTrackingService;
	}

	/**
	 * Sets the eMail service.
	 * 
	 * @author IanBrown
	 * @param emailService
	 *            the eMail service to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Sets the form tracking job.
	 * 
	 * @author IanBrown
	 * @param formTrackingJob
	 *            the form tracking job to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private void setFormTrackingJob(final FormTrackingJob formTrackingJob) {
		this.formTrackingJob = formTrackingJob;
	}

	/**
	 * Sets the form tracking service.
	 * 
	 * @author IanBrown
	 * @param formTrackingService
	 *            the form tracking service to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private void setFormTrackingService(final FormTrackingService formTrackingService) {
		this.formTrackingService = formTrackingService;
	}

	public OvfPropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService(OvfPropertyService propertyService) {
        this.propertyService = propertyService;
    }

}
