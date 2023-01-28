/**
 * 
 */
package com.bearcode.ovf.tools;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.easymock.EasyMock;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.formtracking.TrackedForm;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.FormTrackingService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;

/**
 * Integration test for {@link FormTrackingJob}.
 * 
 * @author IanBrown
 * 
 * @since Apr 26, 2012
 * @version Nov 26, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "../actions/commons/applicationContext_test.xml", "FormTrackingJobIntegration-context.xml" })
@DirtiesContext
public final class FormTrackingJobIntegration extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the eMail service to use.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private EmailService emailService;

	/**
	 * the service used to retrieve the faces.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	@Autowired
	private FacesService facesService;

	/**
	 * the form tracking job to test.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private FormTrackingJob formTrackingJob;

	/**
	 * the service used to retrieve information about the form tracking jobs.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	@Autowired
	private FormTrackingService formTrackingService;

	/**
	 * Sets up the form tracking job for testing.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	@Before
	public final void setUpFormTrackingJob() {
		setEmailService(EasyMock.createMock("eMailService", EmailService.class));
		setFormTrackingJob(new FormTrackingJob());
	}

	/**
	 * Tests the handling of several types of jobs:
	 * <ol>
	 * <li>A job that doesn't need any email sent,</li>
	 * <li>A job that is due for an email and for which there will be further email, and,</li>
	 * <li>A job for which the final email should be sent.</li>
	 * </ol>
	 * 
	 * @author IanBrown
	 * @throws SchedulerException
	 *             if there is a problem setting up the scheduler.
	 * @throws EmailException
	 *             if there is a problem setting up the eMail service.
	 * @since Apr 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFormTrackingJob() throws EmailException, SchedulerException {
		final JobExecutionContext context = EasyMock.createMock("Context", JobExecutionContext.class);
		final Trigger trigger = EasyMock.createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final JobDataMap triggerDataMap = EasyMock.createMock("TriggerDataMap", JobDataMap.class);
		EasyMock.expect(trigger.getJobDataMap()).andReturn(triggerDataMap).anyTimes();
		EasyMock.expect(triggerDataMap.get("oneTimeOnly")).andReturn("false").anyTimes();
		final Scheduler scheduler = EasyMock.createMock("Scheduler", Scheduler.class);
		EasyMock.expect(context.getScheduler()).andReturn(scheduler).anyTimes();
		final SchedulerContext schedulerContext = new SchedulerContext();
		EasyMock.expect(scheduler.getContext()).andReturn(schedulerContext).anyTimes();
		final JobDataMap mergedJobDataMap = new JobDataMap();
		EasyMock.expect(context.getMergedJobDataMap()).andReturn(mergedJobDataMap).anyTimes();
		mergedJobDataMap.put("formTrackingService", getFormTrackingService());
		mergedJobDataMap.put("emailService", getEmailService());
		getEmailService().queue((Email) EasyMock.anyObject());
		EasyMock.expectLastCall().times(2);
		addWaitingTrackedForm();
		addDueTrackedForm();
		addFinalEmailTrackedForm();
		addNoConfigurationTrackedForm();
		EasyMock.replay(context, trigger, triggerDataMap, scheduler, getEmailService());

		getFormTrackingJob().execute(context);

		assertTrackedForms();
		EasyMock.verify(getEmailService(), scheduler, triggerDataMap, trigger, context);
	}

	/**
	 * Creates a tracked form that is due to have an eMail sent.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void addDueTrackedForm() {
		final WizardResults wizardResults = new WizardResults(FlowType.RAVA);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final FaceConfig currentFace = getFacesService().findConfigById(1l);
		wizardContext.setCurrentFace(currentFace);
		wizardResults.setUsername("due-user@somewhere.com");
		wizardResults.setName(createPerson("Due User"));

		getFormTrackingService().saveAfterThankYou(wizardContext);

		final TrackedForm dueTrackedForm = findTrackedFormByEmailAddress("due-user@somewhere.com");
		dueTrackedForm.setLastEmailDate(computeLastEmailDate());
		getFormTrackingService().getFormTrackingDAO().makePersistent(dueTrackedForm);
	}

	/**
	 * Creates a tracked form that is due to have its final eMail sent.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void addFinalEmailTrackedForm() {
		final WizardResults wizardResults = new WizardResults(FlowType.RAVA);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final FaceConfig currentFace = getFacesService().findConfigById(1l);
		wizardContext.setCurrentFace(currentFace);
		wizardResults.setUsername("final-email-user@somewhere.com");
		wizardResults.setName(createPerson("Final eMail User"));

		getFormTrackingService().saveAfterThankYou(wizardContext);

		final TrackedForm finalEmailTrackedForm = findTrackedFormByEmailAddress("final-email-user@somewhere.com");
		finalEmailTrackedForm.setLastEmailDate(computeLastEmailDate());
		finalEmailTrackedForm.setNumberOfEmailSent(1);
		getFormTrackingService().getFormTrackingDAO().makePersistent(finalEmailTrackedForm);
	}

	/**
	 * Creates a tracked form for a flow that isn't configured.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void addNoConfigurationTrackedForm() {
		// Start with a flow with a configuration that does exist.
		final WizardResults wizardResults = new WizardResults(FlowType.RAVA);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final FaceConfig currentFace = getFacesService().findConfigById(1l);
		wizardContext.setCurrentFace(currentFace);
		wizardResults.setUsername("no-configuration-user@somewhere.com");
		wizardResults.setName(createPerson("No Configuration User"));

		getFormTrackingService().saveAfterThankYou(wizardContext);

		// Change the flow to one that is not configured.
		final TrackedForm noConfigurationTrackedForm = findTrackedFormByEmailAddress("no-configuration-user@somewhere.com");
		noConfigurationTrackedForm.setLastEmailDate(computeLastEmailDate());
		noConfigurationTrackedForm.setFlowType(FlowType.DOMESTIC_ABSENTEE);
		getFormTrackingService().getFormTrackingDAO().makePersistent(noConfigurationTrackedForm);
	}

	/**
	 * Creates a tracked form that is waiting to actually start.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void addWaitingTrackedForm() {
		final WizardResults wizardResults = new WizardResults(FlowType.RAVA);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final FaceConfig currentFace = getFacesService().findConfigById(1l);
		wizardContext.setCurrentFace(currentFace);
		wizardResults.setUsername("waiting-user@somewhere.com");
		wizardResults.setName(createPerson("Waiting User"));

		getFormTrackingService().saveAfterThankYou(wizardContext);
	}

	/**
	 * Custom assertion to ensure that the due form is still around.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form that should be the due one.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void assertDueTrackedForm(final TrackedForm trackedForm) {
		assertEquals("The due tracked form is still around", "due-user@somewhere.com", trackedForm.getEmailAddress());
		assertEquals("One eMail message has been sent for the due tracked form", 1, trackedForm.getNumberOfEmailSent());
	}

	/**
	 * Custom assertion to ensure that the tracked forms are in the expected state.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void assertTrackedForms() {
		final List<TrackedForm> allTrackedForms = getFormTrackingService().findAllTrackedForms();

		assertEquals("There are the correct number of tracked forms", 2, allTrackedForms.size());
		assertWaitingTrackedForm(allTrackedForms.get(0));
		assertDueTrackedForm(allTrackedForms.get(1));
	}

	/**
	 * Custom assertion to ensure that the waiting tracked form is still around.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form that should be the waiting one.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void assertWaitingTrackedForm(final TrackedForm trackedForm) {
		assertEquals("The waiting tracked form is still around", "waiting-user@somewhere.com", trackedForm.getEmailAddress());
		assertEquals("No eMail messages have been sent for the waiting tracked form", 0, trackedForm.getNumberOfEmailSent());
	}

	/**
	 * Computes a date that is far enough in the past to ensure that a new eMail will need to be sent.
	 * 
	 * @author IanBrown
	 * @return the last eMail date.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private Date computeLastEmailDate() {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_YEAR, -4);
		return calendar.getTime();
	}

	/**
	 * Creates a person with the specified name.
	 * 
	 * @author IanBrown
	 * @param fullName
	 *            the full name of the person.
	 * @return the person.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private WizardResultPerson createPerson(final String fullName) {
		final String[] parts = fullName.split(" ");
		final WizardResultPerson person = new WizardResultPerson();
		int idx = 0;
		person.setFirstName(parts[idx++]);
		if (parts.length == 3) {
			person.setMiddleName(parts[idx++]);
		}
		person.setLastName(parts[idx++]);
		return person;
	}

	/**
	 * Finds a tracked form by its eMail address.
	 * 
	 * @author IanBrown
	 * @param emailAddress
	 *            the eMail address.
	 * @return the tracked form.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private TrackedForm findTrackedFormByEmailAddress(final String emailAddress) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(TrackedForm.class);
		criteria.add(Restrictions.eq("emailAddress", emailAddress));
		final List<TrackedForm> trackedForms = (List<TrackedForm>) getFormTrackingService().getFormTrackingDAO().findBy(criteria);
		final TrackedForm trackedForm = trackedForms.get(0);
		return trackedForm;
	}

	/**
	 * Gets the eMail service.
	 * 
	 * @author IanBrown
	 * @return the eMail service.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private EmailService getEmailService() {
		return emailService;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the form tracking job.
	 * 
	 * @author IanBrown
	 * @return the form tracking job.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private FormTrackingJob getFormTrackingJob() {
		return formTrackingJob;
	}

	/**
	 * Gets the form tracking service.
	 * 
	 * @author IanBrown
	 * @return the form tracking service.
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
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
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
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
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	private void setFormTrackingJob(final FormTrackingJob formTrackingJob) {
		this.formTrackingJob = formTrackingJob;
	}
}
