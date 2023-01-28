/**
 * 
 */
package com.bearcode.ovf.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.bearcode.ovf.model.email.RawEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bearcode.commons.config.Environment;
import com.bearcode.ovf.DAO.FormTrackingDAO;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.formtracking.TrackedForm;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.email.Email;

/**
 * Service to assist voters in tracking their forms after they've been created and downloaded by the system. A "thank you" email is
 * always sent immediately after the PDF is downloaded - this service supports the sending of additional emails if desired by the
 * SHS.
 * 
 * @author IanBrown
 * 
 * @since Apr 25, 2012
 * @version Jul 10, 2012
 */
@Service
public class FormTrackingService {

	/**
	 * send exactly one follow-up email?
	 * 
	 * @author IanBrown
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	private boolean oneTimeOnly;

	/**
	 * the format string used to find the configuration files.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 26, 2012
	 */
	static final String CONFIGURATION_PATH_FORMAT = "/WEB-INF/mails/form_tracking_%s.cfg.xml";

	/**
	 * the format string used to find the local configuration files.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	static final String LOCAL_CONFIGURATION_PATH_FORMAT = "formtracking/form_tracking_%s.cfg.xml";

	/**
	 * the prefix for the configuration.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	static final String CONFIGURATION_PREFIX = "/WEB-INF/mails/";

	/**
	 * the prefix for local configurations.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	static final String LOCAL_CONFIGURATION_PREFIX = "formtracking/";

	/**
	 * the data access object used to get the form tracking information.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Autowired
	private FormTrackingDAO formTrackingDAO;

	/**
	 * the service used to get information about the faces.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Autowired
	private FacesService facesService;

	/**
	 * the string used to indicate that no eMail is needed.
	 * 
	 * @author IanBrown
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	public final static String NO_EMAIL = "No eMail needed";

	/**
	 * Builds an eMail command for the tracked form using the specified template.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form.
	 * @param emailTemplate
	 *            the path to the template file to use for the eMail.
	 * @return the Email object.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public Email buildEmail(final TrackedForm trackedForm, final String emailTemplate) {
		final Email email = Email.builder()
				.template( emailTemplate )
				.to( trackedForm.getEmailAddress() )
				.model( "firstName", trackedForm.getFirstName() )
				.model("lastName", trackedForm.getLastName())
				.model( "priority", RawEmail.Priority.LOW )
				.build();
		return email;
	}

	/**
	 * Determines the email template to use for the tracked form.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form.
	 * @return the email template to use or <code>null</code> if no email is needed.
	 * @since Apr 25, 2012
	 * @version Jul 9, 2012
	 */
	public String determineEmailTemplate(final TrackedForm trackedForm) {
		final Environment trackingEnvironment = loadTrackingEnvironment(trackedForm);
		if (trackingEnvironment != null) {
			final boolean buildEmail;
			if (isOneTimeOnly()) {
				buildEmail = true;
			} else {
				final Date lastEmailDate = trackedForm.getLastEmailDate();
				final int timeSpan = trackingEnvironment.getIntProperty("timeSpan", 3);
				final GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(lastEmailDate);
				calendar.add(Calendar.DAY_OF_YEAR, timeSpan);
				final GregorianCalendar now = new GregorianCalendar();
				buildEmail = now.compareTo(calendar) > 0;
			}
			if (buildEmail) {
				return buildEmailTemplate(trackedForm, trackingEnvironment);
			}

			return NO_EMAIL;
		}

		return null;
	}

	/**
	 * Finds all of the tracked forms.
	 * 
	 * @author IanBrown
	 * @return the list of tracked forms.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public List<TrackedForm> findAllTrackedForms() {
		return getFormTrackingDAO().findAllTrackedForms();
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the form tracking DAO.
	 * 
	 * @author IanBrown
	 * @return the form tracking DAO.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public FormTrackingDAO getFormTrackingDAO() {
		return formTrackingDAO;
	}

	/**
	 * Gets the one time only flag.
	 * 
	 * @author IanBrown
	 * @return the one time only flag.
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	public boolean isOneTimeOnly() {
		return oneTimeOnly;
	}

	/**
	 * Starts tracking (if configured) the form described by the input wizard context for which the application has just sent the
	 * "thank you" email.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @since Apr 25, 2012
	 * @version Jul 9, 2012
	 */
	public void saveAfterThankYou(final WizardContext wizardContext) {
		final Environment trackingEnvironment = loadTrackingEnvironment(wizardContext);

		if (trackingEnvironment != null) {
			final TrackedForm trackedForm = new TrackedForm();
			trackedForm.setFace(wizardContext.getCurrentFace());
			trackedForm.setFlowType(wizardContext.getFlowType());
			final WizardResults wizardResults = wizardContext.getWizardResults();
			trackedForm.setEmailAddress(wizardResults.getUsername());
			final WizardResultPerson name = wizardResults.getName();
			trackedForm.setFirstName(name.getFirstName());
			trackedForm.setLastName(name.getLastName());
			trackedForm.setLastEmailDate(computeToday());
			getFormTrackingDAO().makePersistent(trackedForm);
		}
	}

	/**
	 * Updates the tracking information when an eMail is sent. The tracking information is deleted if there is no tracking
	 * configuration or if the last eMail has been sent.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form.
	 * @since Apr 25, 2012
	 * @version Jul 9, 2012
	 */
	public void saveAfterTrackingEmail(final TrackedForm trackedForm) {
		final Environment trackingEnvironment = loadTrackingEnvironment(trackedForm);

		if (isOneTimeOnly() || trackingEnvironment == null || trackedForm.getNumberOfEmailSent() >= trackingEnvironment.getIntProperty("frequency", 0)) {
			getFormTrackingDAO().makeTransient(trackedForm);

		} else {
			final int numberOfEmailSent = trackedForm.getNumberOfEmailSent() + 1;
			final int frequency = trackingEnvironment.getIntProperty("frequency", 1);
			if (numberOfEmailSent < frequency) {
				trackedForm.setLastEmailDate(computeToday());
				trackedForm.setNumberOfEmailSent(numberOfEmailSent);
				getFormTrackingDAO().makePersistent(trackedForm);
			} else {
				getFormTrackingDAO().makeTransient(trackedForm);
			}
		}
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the form tracking DAO.
	 * 
	 * @author IanBrown
	 * @param formTrackingDAO
	 *            the form tracking DAO to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setFormTrackingDAO(final FormTrackingDAO formTrackingDAO) {
		this.formTrackingDAO = formTrackingDAO;
	}

	/**
	 * Sets the one time only flag.
	 * 
	 * @author IanBrown
	 * @param oneTimeOnly
	 *            the one time only flag to set.
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	public void setOneTimeOnly(final boolean oneTimeOnly) {
		this.oneTimeOnly = oneTimeOnly;
	}

	/**
	 * Builds the name of the eMail template from the tracked form and tracking environment.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form.
	 * @param trackingEnvironment
	 *            the tracking environment.
	 * @return the eMail template or <code>null</code> if none can be found.
	 * @since Apr 25, 2012
	 * @version Apr 26, 2012
	 */
	private String buildEmailTemplate(final TrackedForm trackedForm, final Environment trackingEnvironment) {
		final FaceConfig face = trackedForm.getFace();
		final String relativePrefix = face.getRelativePrefix();
		final String rawEmailTemplate = trackingEnvironment.getStringProperty("template", null);
		if (rawEmailTemplate == null) {
			return null;
		}

		String emailTemplate = getFacesService().getApprovedFileName(CONFIGURATION_PREFIX + rawEmailTemplate, relativePrefix);
		if (emailTemplate.isEmpty()) {
			emailTemplate = getFacesService().getApprovedFileName(LOCAL_CONFIGURATION_PREFIX + rawEmailTemplate, relativePrefix);
		}
		return emailTemplate.isEmpty() ? null : emailTemplate;
	}

	/**
	 * Computes today's date.
	 * 
	 * @author IanBrown
	 * @return the date for today.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private Date computeToday() {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Loads the tracking environment for the face and flow.
	 * 
	 * @author IanBrown
	 * @param face
	 *            the face.
	 * @param flowType
	 *            the type of flow.
	 * @return the tracking environment.
	 * @since Apr 25, 2012
	 * @version Jul 10, 2012
	 */
	private Environment loadTrackingEnvironment(final FaceConfig face, final FlowType flowType) {
		final String relativePrefix = face.getRelativePrefix();
		final String rawConfigurationPath = String.format(CONFIGURATION_PATH_FORMAT, flowType.name());
		String configurationPath = getFacesService().getApprovedFileName(rawConfigurationPath, relativePrefix);

		if (configurationPath.isEmpty()) {
			final String localConfigurationPath = String.format(LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
			configurationPath = getFacesService().getApprovedFileName(localConfigurationPath, relativePrefix);
		}

		if (!configurationPath.isEmpty()) {
			configurationPath = configurationPath.replaceAll( "WEB-INF/", "../");
			final Environment environment = new Environment(configurationPath);
			return environment;
		}

		return null;
	}

	/**
	 * Loads the configuration for tracking the specified form into an environment object.
	 * 
	 * @author IanBrown
	 * @param trackedForm
	 *            the tracked form.
	 * @return the environment object or <code>null</code> if no tracking information can be found.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private Environment loadTrackingEnvironment(final TrackedForm trackedForm) {
		final FaceConfig face = trackedForm.getFace();
		final FlowType flowType = trackedForm.getFlowType();
		return loadTrackingEnvironment(face, flowType);
	}

	/**
	 * Loads the tracking environment for the wizard context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @return the tracking environment.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private Environment loadTrackingEnvironment(final WizardContext wizardContext) {
		final FaceConfig face = wizardContext.getCurrentFace();
		final FlowType flowType = wizardContext.getFlowType();

		return loadTrackingEnvironment(face, flowType);
	}
}
