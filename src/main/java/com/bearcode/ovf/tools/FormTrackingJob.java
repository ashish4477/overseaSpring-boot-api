/**
 * 
 */
package com.bearcode.ovf.tools;

import java.util.List;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.apache.commons.mail.EmailException;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bearcode.ovf.model.formtracking.TrackedForm;
import com.bearcode.ovf.service.FormTrackingService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;

/**
 * Extended {@link QuartzJobBean} to provide a background task to determine if form tracking email messages need to be sent.
 * 
 * @author IanBrown
 * 
 * @since Apr 25, 2012
 * @version Jul 20, 2012
 */
public class FormTrackingJob extends QuartzJobBean implements StatefulJob {

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(FormTrackingJob.class);

	/**
	 * the service used to get and update the tracked form information.
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

	public OvfPropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(OvfPropertyService propertyService) {
		this.propertyService = propertyService;
	}

	/**
	 * Gets the eMail service.
	 * 
	 * @author IanBrown
	 * @return the eMail service.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public EmailService getEmailService() {
		return emailService;
	}

	/**
	 * Gets the form tracking service.
	 * 
	 * @author IanBrown
	 * @return the form tracking service.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public FormTrackingService getFormTrackingService() {
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
	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
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
	public void setFormTrackingService(final FormTrackingService formTrackingService) {
		this.formTrackingService = formTrackingService;
	}

	/** {@inheritDoc} */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED))) {
			return;
		}
		final JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
		final boolean oneTimeOnly;
		final Object value = jobDataMap.get("oneTimeOnly");
		if (value instanceof Boolean) {
			oneTimeOnly = (Boolean) value;
		} else if (value instanceof String) {
			oneTimeOnly = Boolean.parseBoolean((String) value);
		} else {
			oneTimeOnly = false;
		}
		final List<TrackedForm> trackedForms = getFormTrackingService().findAllTrackedForms();

		for (final TrackedForm trackedForm : trackedForms) {
			trackForm(trackedForm, oneTimeOnly);
		}
	}

	/**
	 * Performs the next step in tracking the specified form. This will be either to do nothing or to send an email for the form and
	 * then update the tracking information.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form.
	 * @param oneTimeOnly
	 *            <code>true</code> to send one email, <code>false</code> to use configured state.
	 * @since Apr 25, 2012
	 * @version Jul 20, 2012
	 */
	private void trackForm(final TrackedForm trackedForm, final boolean oneTimeOnly) {
		final boolean wasOneTimeOnly = getFormTrackingService().isOneTimeOnly();
		getFormTrackingService().setOneTimeOnly(oneTimeOnly);
		String emailTemplate = getFormTrackingService().determineEmailTemplate(trackedForm);

		if (emailTemplate != null && !FormTrackingService.NO_EMAIL.equals(emailTemplate)) {
			try {
				final Email email = getFormTrackingService().buildEmail(trackedForm, emailTemplate);
				getEmailService().queue(email);
				LOGGER.info("Sent follow-up email to " + trackedForm.getEmailAddress());
			} catch (final EmailException e) {
				LOGGER.warn("Failed to send email to " + trackedForm.getEmailAddress(), e);
			}
		}

		// Note that the tracking form is updated even if the email fails to send or if no template can be found.
		if (emailTemplate == null || !FormTrackingService.NO_EMAIL.equals(emailTemplate)) {
			getFormTrackingService().saveAfterTrackingEmail(trackedForm);
		}
		getFormTrackingService().setOneTimeOnly(wasOneTimeOnly);
	}
}
