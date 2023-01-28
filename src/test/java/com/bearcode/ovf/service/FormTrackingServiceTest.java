/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
 * Test for {@link FormTrackingService}.
 * 
 * @author IanBrown
 * 
 * @since Apr 25, 2012
 * @version Jul 26, 2012
 */
public final class FormTrackingServiceTest extends EasyMockSupport {

	/**
	 * the form tracking service to test.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FormTrackingService formTrackingService;

	/**
	 * the service used to get information about faces.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FacesService facesService;

	/**
	 * the data access object used to work with tracked forms.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FormTrackingDAO formTrackingDAO;

	/**
	 * Sets up the form tracking service to test.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Before
	public final void setUpFormTrackingService() {
		setFacesService(createMock("FacesService", FacesService.class));
		setFormTrackingDAO(createMock("FormTrackingDAO", FormTrackingDAO.class));
		setFormTrackingService(new FormTrackingService());
		getFormTrackingService().setFacesService(getFacesService());
		getFormTrackingService().setFormTrackingDAO(getFormTrackingDAO());
	}

	/**
	 * Tears down the form tracking service after testing.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@After
	public final void tearDownFormTrackingService() {
		setFormTrackingService(null);
		setFormTrackingDAO(null);
		setFacesService(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FormTrackingService#buildEmail(com.bearcode.ovf.model.formtracking.TrackedForm, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Test
	public final void testBuildMailCommand() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final String emailTemplate = "eMail Template";
		final String firstName = "First Name";
		EasyMock.expect(trackedForm.getFirstName()).andReturn(firstName).anyTimes();
		final String lastName = "Last Name";
		EasyMock.expect(trackedForm.getLastName()).andReturn(lastName).anyTimes();
		final String emailAddress = "Email Address";
		EasyMock.expect(trackedForm.getEmailAddress()).andReturn(emailAddress).anyTimes();
		replayAll();

		final Email actualEmail = getFormTrackingService().buildEmail(trackedForm, emailTemplate);

		assertNotNull("A mail command is returned", actualEmail);
		assertEquals("The email template is set", emailTemplate, actualEmail.getTemplate());
		assertEquals("The email address is used as the send to string", emailAddress, actualEmail.getToAsString());
		final Map<?, ?> actualModel = actualEmail.getModel();
		assertEquals("The first name is set in the model", firstName, actualModel.get("firstName"));
		assertEquals("The last name is set in the model", lastName, actualModel.get("lastName"));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FormTrackingService#determineEmailTemplate(com.bearcode.ovf.model.formtracking.TrackedForm)}
	 * for a tracked form that is due.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Apr 26, 2012
	 */
	@Test
	public final void testDetermineEmailTemplate_due() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(trackedForm.getFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(trackedForm.getFlowType()).andReturn(flowType).anyTimes();
		final String packagePath = getClass().getPackage().getName().replace('.', '/');
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String configurationPath = packagePath + "/"
				+ String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final Date lastEmailDate = new Date(System.currentTimeMillis() - 365l * 24l * 60l * 60l * 1000l);
		EasyMock.expect(trackedForm.getLastEmailDate()).andReturn(lastEmailDate).anyTimes();
		final Environment environment = loadEnvironment(configurationPath);
		final String rawTemplateName = environment.getStringProperty("template", null);
		final String expectedEmailTemplate = packagePath + "/formtracking/" + rawTemplateName;
		EasyMock.expect(getFacesService().getApprovedFileName("/WEB-INF/mails/" + rawTemplateName, relativePrefix))
				.andReturn(expectedEmailTemplate).anyTimes();
		replayAll();

		final String actualEmailTemplate = getFormTrackingService().determineEmailTemplate(trackedForm);

		assertEquals("There is an eMail template", expectedEmailTemplate, actualEmailTemplate);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FormTrackingService#determineEmailTemplate(com.bearcode.ovf.model.formtracking.TrackedForm)}
	 * for a tracked form that isn't due.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Apr 26, 2012
	 */
	@Test
	public final void testDetermineEmailTemplate_notDue() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(trackedForm.getFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(trackedForm.getFlowType()).andReturn(flowType).anyTimes();
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String packagePath = getClass().getPackage().getName().replace('.', '/');
		final String configurationPath = packagePath + "/"
				+ String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final Date lastEmailDate = new Date();
		EasyMock.expect(trackedForm.getLastEmailDate()).andReturn(lastEmailDate).anyTimes();
		replayAll();

		final String actualEmailTemplate = getFormTrackingService().determineEmailTemplate(trackedForm);

		assertEquals("No eMail is needed", FormTrackingService.NO_EMAIL, actualEmailTemplate);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FormTrackingService#determineEmailTemplate(com.bearcode.ovf.model.formtracking.TrackedForm)}
	 * for a tracked form belonging to a face that doesn't have a tracking configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Apr 26, 2012
	 */
	@Test
	public final void testDetermineEmailTemplate_noTracking() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(trackedForm.getFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		EasyMock.expect(trackedForm.getFlowType()).andReturn(flowType).anyTimes();
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String configurationPath = "";
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final String localConfigurationName = String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(localConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		replayAll();

		final String actualEmailTemplate = getFormTrackingService().determineEmailTemplate(trackedForm);

		assertNull("No eMail template is returned", actualEmailTemplate);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FormTrackingService#findAllTrackedForms()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Test
	public final void testFindAllTrackedForms() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final List<TrackedForm> trackedForms = Arrays.asList(trackedForm);
		EasyMock.expect(getFormTrackingDAO().findAllTrackedForms()).andReturn(trackedForms).anyTimes();
		replayAll();

		final List<TrackedForm> actualTrackedForms = getFormTrackingService().findAllTrackedForms();

		assertSame("The tracked forms are returned", trackedForms, actualTrackedForms);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FormTrackingService#saveAfterThankYou(WizardContext)}.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testSaveAfterThankYou() {
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(wizardContext.getCurrentFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		final String packagePath = getClass().getPackage().getName().replace('.', '/');
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String configurationPath = packagePath + "/"
				+ String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardContext.getWizardResults()).andReturn(wizardResults).anyTimes();
		final String emailAddress = "Email Address";
		EasyMock.expect(wizardResults.getUsername()).andReturn(emailAddress).anyTimes();
		final WizardResultPerson name = createMock("Name", WizardResultPerson.class);
		EasyMock.expect(wizardResults.getName()).andReturn(name).anyTimes();
		final String firstName = "First Name";
		EasyMock.expect(name.getFirstName()).andReturn(firstName).anyTimes();
		final String lastName = "Last Name";
		EasyMock.expect(name.getLastName()).andReturn(lastName).anyTimes();
		getFormTrackingDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new FormTrackingDAO() {

			/** {@inheritDoc} */
			@Override
			public void makePersistent(final Object object) {
				assertEquals("The object is a tracked form", TrackedForm.class, object.getClass());
				final TrackedForm trackedForm = (TrackedForm) object;
				assertSame("The face is correct", face, trackedForm.getFace());
				assertSame("The flow type is correct", flowType, trackedForm.getFlowType());
				assertEquals("The eMail address is correct", emailAddress, trackedForm.getEmailAddress());
				assertEquals("The first name is correct", firstName, trackedForm.getFirstName());
				assertEquals("The last name is correct", lastName, trackedForm.getLastName());
				final Date today = computeToday();
				assertEquals("The last eMail was sent today", today, trackedForm.getLastEmailDate());
				assertEquals("No eMail messages have been sent", 0, trackedForm.getNumberOfEmailSent());
			}

		}).anyTimes();
		replayAll();

		getFormTrackingService().saveAfterThankYou(wizardContext);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FormTrackingService#saveAfterThankYou(WizardContext)} for the case where there is
	 * no configuration for tracking the form.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Jul 9, 2012
	 */
	@Test
	public final void testSaveAfterThankYou_noTracking() {
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(wizardContext.getCurrentFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String configurationPath = "";
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final String localConfigurationName = String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(localConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		replayAll();

		getFormTrackingService().saveAfterThankYou(wizardContext);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FormTrackingService#saveAfterTrackingEmail(com.bearcode.ovf.model.formtracking.TrackedForm)} for the
	 * case where there is a tracking configuration and the last eMail has been sent.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Apr 26, 2012
	 */
	@Test
	public final void testSaveAfterTrackingEmail_lastEmail() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(trackedForm.getFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(trackedForm.getFlowType()).andReturn(flowType).anyTimes();
		final String packagePath = getClass().getPackage().getName().replace('.', '/');
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String configurationPath = packagePath + "/"
				+ String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final Environment trackingEnvironment = new Environment(configurationPath);
		EasyMock.expect(trackedForm.getNumberOfEmailSent()).andReturn(trackingEnvironment.getIntProperty("frequency", 0) - 1)
				.anyTimes();
		getFormTrackingDAO().makeTransient(trackedForm);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getFormTrackingService().saveAfterTrackingEmail(trackedForm);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FormTrackingService#saveAfterTrackingEmail(com.bearcode.ovf.model.formtracking.TrackedForm)} for the
	 * case where there is a tracking configuration and more eMail needs to be sent in the future.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Jul 9, 2012
	 */
	@Test
	public final void testSaveAfterTrackingEmail_moreEmail() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(trackedForm.getFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.RAVA;
		EasyMock.expect(trackedForm.getFlowType()).andReturn(flowType).anyTimes();
		final String packagePath = getClass().getPackage().getName().replace('.', '/');
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String configurationPath = packagePath + "/"
				+ String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final Environment trackingEnvironment = new Environment(configurationPath);
		final int numberOfEmailSent = 0;
		EasyMock.expect(trackedForm.getNumberOfEmailSent()).andReturn(numberOfEmailSent).anyTimes();
		trackedForm.setLastEmailDate(computeToday());
		trackedForm.setNumberOfEmailSent(numberOfEmailSent + 1);
		getFormTrackingDAO().makePersistent(trackedForm);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getFormTrackingService().saveAfterTrackingEmail(trackedForm);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FormTrackingService#saveAfterTrackingEmail(com.bearcode.ovf.model.formtracking.TrackedForm)} for the
	 * case where there is no tracking configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 25, 2012
	 * @version Jul 9, 2012
	 */
	@Test
	public final void testSaveAfterTrackingEmail_noTracking() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final FaceConfig face = createMock("Face", FaceConfig.class);
		EasyMock.expect(trackedForm.getFace()).andReturn(face).anyTimes();
		final String relativePrefix = "Relative Prefix";
		EasyMock.expect(face.getRelativePrefix()).andReturn(relativePrefix).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		EasyMock.expect(trackedForm.getFlowType()).andReturn(flowType).anyTimes();
		final String rawConfigurationName = String.format(FormTrackingService.CONFIGURATION_PATH_FORMAT, flowType.name());
		final String configurationPath = "";
		EasyMock.expect(getFacesService().getApprovedFileName(rawConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		final String localConfigurationName = String.format(FormTrackingService.LOCAL_CONFIGURATION_PATH_FORMAT, flowType.name());
		EasyMock.expect(getFacesService().getApprovedFileName(localConfigurationName, relativePrefix)).andReturn(configurationPath)
				.anyTimes();
		getFormTrackingDAO().makeTransient(trackedForm);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getFormTrackingService().saveAfterTrackingEmail(trackedForm);

		verifyAll();
	}

	/**
	 * Computes today's date.
	 * 
	 * @author IanBrown
	 * @return today's date.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private Date computeToday() {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.HOUR, 0);
		calendar.set(GregorianCalendar.MINUTE, 0);
		calendar.set(GregorianCalendar.SECOND, 0);
		calendar.set(GregorianCalendar.MILLISECOND, 0);
		final Date today = calendar.getTime();
		return today;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private FacesService getFacesService() {
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
	private FormTrackingDAO getFormTrackingDAO() {
		return formTrackingDAO;
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
	 * Loads the environment file at the specified configuration path.
	 * 
	 * @author IanBrown
	 * @param configurationPath
	 *            the configuration path.
	 * @return the environment.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	private Environment loadEnvironment(final String configurationPath) {
		final Environment environment = new Environment(configurationPath);
		return environment;
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
	private void setFacesService(final FacesService facesService) {
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
	private void setFormTrackingDAO(final FormTrackingDAO formTrackingDAO) {
		this.formTrackingDAO = formTrackingDAO;
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
}
