/**
 * 
 */
package com.bearcode.ovf.actions.mobile;

import com.bearcode.ovf.actions.mobile.MobileController.UserDescription;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.mobile.MobilePage;
import com.bearcode.ovf.model.mobile.MobileResults;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.reportingdashboard.Gender;
import com.bearcode.ovf.service.*;
import com.bearcode.ovf.tools.pdf.AdvancedPdfGeneratorFactory;
import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.io.OutputStream;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for {@link com.bearcode.ovf.actions.mobile.MobileController}.
 * 
 * @author IanBrown
 * 
 * @since Apr 10, 2012
 * @version Sep 21, 2012
 */
public final class MobileControllerTest extends EasyMockSupport {

	/**
	 * the faces service to inject.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private FacesService facesService;

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * the mailing list service.
	 * @author IanBrown
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	private MailingListService mailingListService;

	/**
	 * the mobile controller to test.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private MobileController mobileController;

	/**
	 * the mobile service to inject.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private MobileService mobileService;

	/**
	 * the PDF generator factory.
	 * 
	 * @author IanBrown
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private AdvancedPdfGeneratorFactory pdfGeneratorFactory;

	/**
	 * the questionnaire service to inject.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private StateService stateService;
	
	/**
	 * the user service.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	private OverseasUserService userService;

	/**
	 * Sets up the mobile controller to test.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Sep 21, 2012
	 */
	@Before
	public final void setUpMobileController() {
		setFacesService(createMock("FacesService", FacesService.class));
		setMobileService(createMock("MobileService", MobileService.class));
		setQuestionnaireService(createMock("QuestionnaireService", QuestionnaireService.class));
		setStateService(createMock("StateService", StateService.class));
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
		setUserService(createMock("UserService", OverseasUserService.class));
		setPdfGeneratorFactory(createMock("PdfGeneratorFactory", AdvancedPdfGeneratorFactory.class));
		setMailingListService(createMock("MailingListService", MailingListService.class));
		setMobileController(new MobileController());
		getMobileController().setFacesService(getFacesService());
		getMobileController().setMobileService(getMobileService());
		getMobileController().setQuestionnaireService(getQuestionnaireService());
		getMobileController().setStateService(getStateService());
		getMobileController().setLocalOfficialService(getLocalOfficialService());
		getMobileController().setPdfGeneratorFactory(getPdfGeneratorFactory());
		getMobileController().setUserService(getUserService());
		getMobileController().setMailingListService(getMailingListService());
	}

	/**
	 * Tears down the mobile controller.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Sep 21, 2012
	 */
	@After
	public final void tearDownMobileController() {
		setMobileController(null);
		setMailingListService(null);
		setUserService(null);
		setPdfGeneratorFactory(null);
		setLocalOfficialService(null);
		setStateService(null);
		setQuestionnaireService(null);
		setMobileService(null);
		setFacesService(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#checkUser(String, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Test
	public final void testCheckUser() {
		final String username = "email@somewhere.com";
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(username, null);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final OverseasUser user = createMock("OverseasUser", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(user);
		replayAll();

		getMobileController().checkUser(emailMd5, response);

		assertEquals("The response says that the user was found", HttpStatus.OK.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#checkUser(String, javax.servlet.http.HttpServletResponse)} for the
	 * case where the user doesn't exist.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Test
	public final void testCheckUser_noSuchUser() {
		final String username = "email@somewhere.com";
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(username, null);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(null);
		replayAll();

		getMobileController().checkUser(emailMd5, response);

		assertEquals("The response says that the user was not found", HttpStatus.NOT_FOUND.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#countries()}.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	public final void testCountries() {
		final Country country = createMock("Country", Country.class);
		final List<Country> countries = Arrays.asList(country);
		EasyMock.expect(getStateService().findAllCountries()).andReturn(countries).anyTimes();
		replayAll();

		final List<Country> actualCountries = getMobileController().countries();

		assertSame("The countries are returned", countries, actualCountries);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(com.bearcode.ovf.actions.mobile.MobileController.UserDescription, javax.servlet.http.HttpServletResponse)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCreateUser() {
		final UserDescription userDescription = createMock("UserDescription", UserDescription.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String username = "Username";
		EasyMock.expect(userDescription.getUsername()).andReturn(username);
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(null);
		final String password = "Password";
		EasyMock.expect(userDescription.getPassword()).andReturn(password);
		final String firstName = "First";
		EasyMock.expect(userDescription.getFirstName()).andReturn(firstName);
		final String lastName = "Last";
		EasyMock.expect(userDescription.getLastName()).andReturn(lastName);
		final String middleName = "Middle";
		EasyMock.expect(userDescription.getMiddleName()).andReturn(middleName);
		getUserService().makeNewUser((OverseasUser) EasyMock.anyObject());
		replayAll();

		final OverseasUser actualUser = getMobileController().createUser(userDescription, response);

		assertNotNull("A user is returned", actualUser);
		assertEquals("The user's username is set", username, actualUser.getUsername());
		assertEquals("The user's password is set", password, actualUser.getPassword());
		assertEquals("The user's first name is set", firstName, actualUser.getName().getFirstName());
		assertEquals("The user's middle name is set", middleName, actualUser.getName().getMiddleName());
		assertEquals("The user's last name is set", lastName, actualUser.getName().getLastName());
		assertEquals("The response indicates success", HttpStatus.CREATED.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(com.bearcode.ovf.actions.mobile.MobileController.UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where the user already exists.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCreateUser_alreadyExists() {
		final UserDescription userDescription = createMock("UserDescription", UserDescription.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String username = "Username";
		EasyMock.expect(userDescription.getUsername()).andReturn(username);
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(user);
		replayAll();

		final OverseasUser actualUser = getMobileController().createUser(userDescription, response);

		assertNull("No user is returned", actualUser);
		assertEquals("The response indicates that the user exists", HttpStatus.CONFLICT.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(com.bearcode.ovf.actions.mobile.MobileController.UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where no first name is provided.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCreateUser_noFirstName() {
		final UserDescription userDescription = createMock("UserDescription", UserDescription.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String username = "Username";
		EasyMock.expect(userDescription.getUsername()).andReturn(username);
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(null);
		final String password = "Password";
		EasyMock.expect(userDescription.getPassword()).andReturn(password);
		EasyMock.expect(userDescription.getFirstName()).andReturn(null);
		replayAll();

		final OverseasUser actualUser = getMobileController().createUser(userDescription, response);

		assertNull("No user is returned", actualUser);
		assertEquals("The response indicates missing parameters", HttpStatus.BAD_REQUEST.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(com.bearcode.ovf.actions.mobile.MobileController.UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where no last name is provided.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCreateUser_noLastName() {
		final UserDescription userDescription = createMock("UserDescription", UserDescription.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String username = "Username";
		EasyMock.expect(userDescription.getUsername()).andReturn(username);
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(null);
		final String password = "Password";
		EasyMock.expect(userDescription.getPassword()).andReturn(password);
		final String firstName = "First";
		EasyMock.expect(userDescription.getFirstName()).andReturn(firstName);
		EasyMock.expect(userDescription.getLastName()).andReturn(null);
		replayAll();

		final OverseasUser actualUser = getMobileController().createUser(userDescription, response);

		assertNull("No user is returned", actualUser);
		assertEquals("The response indicates missing parameters", HttpStatus.BAD_REQUEST.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(com.bearcode.ovf.actions.mobile.MobileController.UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where no middle name is provided.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCreateUser_noMiddleName() {
		final UserDescription userDescription = createMock("UserDescription", UserDescription.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String username = "Username";
		EasyMock.expect(userDescription.getUsername()).andReturn(username);
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(null);
		final String password = "Password";
		EasyMock.expect(userDescription.getPassword()).andReturn(password);
		final String firstName = "First";
		EasyMock.expect(userDescription.getFirstName()).andReturn(firstName);
		final String lastName = "Last";
		EasyMock.expect(userDescription.getLastName()).andReturn(lastName);
		EasyMock.expect(userDescription.getMiddleName()).andReturn(null);
		getUserService().makeNewUser((OverseasUser) EasyMock.anyObject());
		replayAll();

		final OverseasUser actualUser = getMobileController().createUser(userDescription, response);

		assertNotNull("A user is returned", actualUser);
		assertEquals("The user's username is set", username, actualUser.getUsername());
		assertEquals("The user's password is set", password, actualUser.getPassword());
		assertEquals("The user's first name is set", firstName, actualUser.getName().getFirstName());
		assertEquals("The user's middle name is blank", "", actualUser.getName().getMiddleName());
		assertEquals("The user's last name is set", lastName, actualUser.getName().getLastName());
		assertEquals("The response indicates success", HttpStatus.CREATED.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(com.bearcode.ovf.actions.mobile.MobileController.UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where no password is provided.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCreateUser_noPassword() {
		final UserDescription userDescription = createMock("UserDescription", UserDescription.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String username = "Username";
		EasyMock.expect(userDescription.getUsername()).andReturn(username);
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(null);
		EasyMock.expect(userDescription.getPassword()).andReturn(null);
		replayAll();

		final OverseasUser actualUser = getMobileController().createUser(userDescription, response);

		assertNull("No user is returned", actualUser);
		assertEquals("The response indicates missing parameters", HttpStatus.BAD_REQUEST.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(com.bearcode.ovf.actions.mobile.MobileController.UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where no username is provided.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCreateUser_noUsername() {
		final UserDescription userDescription = createMock("UserDescription", UserDescription.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(userDescription.getUsername()).andReturn(null);
		replayAll();

		final OverseasUser actualUser = getMobileController().createUser(userDescription, response);

		assertNull("No user is returned", actualUser);
		assertEquals("The response indicates missing parameters", HttpStatus.BAD_REQUEST.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#getUser(String, String, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Test
	public final void testGetUser() {
		final String email = "email@somewhere.com";
		final String password = "password";
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(user);
		EasyMock.expect(user.getPassword()).andReturn(passwordMd5);
		replayAll();

		final OverseasUser actualUser = getMobileController().getUser(emailMd5, passwordMd5, response);

		assertSame("The user is returned", user, actualUser);
		assertEquals("The response is good", HttpStatus.OK.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#getUser(String, String, javax.servlet.http.HttpServletResponse)} for
	 * the case where the user doesn't exist.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Test
	public final void testGetUser_noSuchUser() {
		final String email = "email@somewhere.com";
		final String password = "password";
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(null);
		replayAll();

		final OverseasUser actualUser = getMobileController().getUser(emailMd5, passwordMd5, response);

		assertNull("No user is returned", actualUser);
		assertEquals("The response says that the user was not found", HttpStatus.NOT_FOUND.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#getUser(String, String, javax.servlet.http.HttpServletResponse)} for
	 * the case where the password is no good.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Test
	public final void testGetUser_wrongPassword() {
		final String email = "email@somewhere.com";
		final String password = "password";
		final String realPassword = "real password";
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		final String realPasswordMd5 = md5Encoder.encodePassword(realPassword, null);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(user);
		EasyMock.expect(user.getPassword()).andReturn(realPasswordMd5);
		replayAll();

		final OverseasUser actualUser = getMobileController().getUser(emailMd5, passwordMd5, response);

		assertNull("No user is returned", actualUser);
		assertEquals("The response says that the wrong password was provided", HttpStatus.FORBIDDEN.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#localOfficial(String, String, String)}.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testLocalOfficial() {
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		EasyMock.expect(getMobileService().findLocalOfficialForRegion("VT", "Windsor")).andReturn(localOfficial);
		replayAll();

		final LocalOfficial actualLocalOfficial = getMobileController().localOfficial("USA", "VT", "Windsor");

		assertSame("The local official is returned", localOfficial, actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#localOfficial(String, String, String)} for the case
	 * where a country other than the USA is requested.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	public final void testLocalOfficial_notUSA() {
		final LocalOfficial actualLocalOfficial = getMobileController().localOfficial("CAN", "ONT", "Somewhere");

		assertNull("There is no local official", actualLocalOfficial);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#localOfficial(String, String, String)} for the case
	 * where the voting region does not exist.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testLocalOfficial_notValidVotingRegion() {
		EasyMock.expect(getMobileService().findLocalOfficialForRegion("VT", "Somewhere")).andReturn(null);
		replayAll();

		final LocalOfficial actualLocalOfficial = getMobileController().localOfficial("USA", "VT", "Somewhere");

		assertNull("There is no local official", actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#questions(java.lang.String, com.bearcode.ovf.model.questionnaire.FlowType, java.lang.String, String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws NoSuchRequestHandlingMethodException
	 *             if there is a problem handling the request.
	 * @since Apr 10, 2012
	 * @version Jun 22, 2012
	 */
	@Test
	public final void testQuestions() throws NoSuchRequestHandlingMethodException {
		final String face = "face";
		final FlowType flowType = FlowType.RAVA;
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn("faces/" + face);
		EasyMock.expect(getFacesService().findConfigByPrefix("faces/" + face)).andReturn(faceConfig).anyTimes();
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		EasyMock.expect(getMobileService().createWizardContext(faceConfig, flowType, state, votingRegion)).andReturn(wizardContext)
				.anyTimes();
		final MobilePage mobilePage = createMock("MobilePageOld", MobilePage.class);
		final List<MobilePage> mobilePages = Arrays.asList(mobilePage);
		EasyMock.expect(getMobileService().acquireMobileQuestions(wizardContext)).andReturn(mobilePages);
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileController().questions(face, flowType, state, votingRegion);

		assertSame("The mobile pages were returned", mobilePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#questions(java.lang.String, com.bearcode.ovf.model.questionnaire.FlowType, java.lang.String, String)}
	 * using an identifier for the voting region.
	 * 
	 * @author IanBrown
	 * 
	 * @throws NoSuchRequestHandlingMethodException
	 *             if there is a problem handling the request.
	 * @since Aug 3, 2012
	 * @version Aug 3, 2012
	 */
	@Test
	public final void testQuestions_identifier() throws NoSuchRequestHandlingMethodException {
		final String face = "face";
		final FlowType flowType = FlowType.RAVA;
		final String state = "ST";
		final long votingRegionId = 987127l;
		final String votingRegion = "Voting Region";
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn("faces/" + face);
		EasyMock.expect(getFacesService().findConfigByPrefix("faces/" + face)).andReturn(faceConfig).anyTimes();
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(getStateService().findRegion(votingRegionId)).andReturn(region);
		EasyMock.expect(region.getName()).andReturn(votingRegion);
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		EasyMock.expect(getMobileService().createWizardContext(faceConfig, flowType, state, votingRegion)).andReturn(wizardContext)
				.anyTimes();
		final MobilePage mobilePage = createMock("MobilePageOld", MobilePage.class);
		final List<MobilePage> mobilePages = Arrays.asList(mobilePage);
		EasyMock.expect(getMobileService().acquireMobileQuestions(wizardContext)).andReturn(mobilePages);
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileController().questions(face, flowType, state, Long.toString(votingRegionId));

		assertSame("The mobile pages were returned", mobilePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#questions(java.lang.String, com.bearcode.ovf.model.questionnaire.FlowType, java.lang.String, String)}
	 * for the case where the face doesn't exist.
	 * 
	 * @author IanBrown
	 * 
	 * @throws NoSuchRequestHandlingMethodException
	 *             if there is a problem handling the request.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	@Test(expected = NoSuchRequestHandlingMethodException.class)
	public final void testQuestions_unmatchedFace() throws NoSuchRequestHandlingMethodException {
		final String face = "badface";
		final FlowType flowType = FlowType.RAVA;
		final String state = "ST";
		final String votingRegion = "Voting Region";
		EasyMock.expect(getFacesService().findConfigByPrefix("faces/" + face)).andReturn(null).anyTimes();
		replayAll();

		getMobileController().questions(face, flowType, state, votingRegion);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#results(String, com.bearcode.ovf.model.questionnaire.FlowType, String, String, com.bearcode.ovf.model.mobile.MobileResults)}
	 * .
	 * 
	 * @author IanBrown
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the PDF generator.
	 * @since Apr 11, 2012
	 * @version Sep 21, 2012
	 */
	@Test
	public void testResults() throws PdfGeneratorException {
		final String face = "face";
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final MobileResults results = createMock("Results", MobileResults.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigByPrefix("faces/" + face)).andReturn(faceConfig).anyTimes();
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		EasyMock.expect(getMobileService().createWizardContext(faceConfig, flowType, state, votingRegion)).andReturn(wizardContext)
				.anyTimes();
		getMobileService().addResultsToWizardContext(results, wizardContext);
		final PdfGenerator pdfGenerator = createMock("PdfGenerator", PdfGenerator.class);
		EasyMock.expect(getPdfGeneratorFactory().createPdfGenerator(EasyMock.eq(wizardContext), EasyMock.anyObject(OutputStream.class)))
				.andReturn(pdfGenerator).anyTimes();
		pdfGenerator.run();
		pdfGenerator.dispose();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardContext.getWizardResults()).andReturn(wizardResults).anyTimes();
		EasyMock.expect( wizardResults.getAnswers() ).andReturn( Collections.<Answer>emptyList() ).anyTimes();
        final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
        EasyMock.expect( votingAddress.getState()).andReturn(state).anyTimes();
        EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).anyTimes();
        EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).anyTimes();
		wizardResults.setLastChangedDate((Date) EasyMock.anyObject());
		wizardResults.setDownloaded(true);
		wizardContext.processSaveResults(getQuestionnaireService());
		getMailingListService().saveToMailingListIfHasSignup((WizardResults) EasyMock.anyObject());
		replayAll();

		getMobileController().results(face, flowType, state, votingRegion, results);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#results(String, com.bearcode.ovf.model.questionnaire.FlowType, String, String, com.bearcode.ovf.model.mobile.MobileResults)}
	 * for the case where the identifier for the voting region is provided.
	 * 
	 * @author IanBrown
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the PDF generator.
	 * @since Apr 11, 2012
	 * @version Sep 21, 2012
	 */
	@Test
	public void testResults_identifier() throws PdfGeneratorException {
		final String face = "face";
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final String state = "ST";
		final long votingRegionId = 298l;
		final String votingRegion = "Voting Region";
		final MobileResults results = createMock("Results", MobileResults.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigByPrefix("faces/" + face)).andReturn(faceConfig).anyTimes();
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(getStateService().findRegion(votingRegionId)).andReturn(region);
		EasyMock.expect(region.getName()).andReturn(votingRegion);
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		EasyMock.expect(getMobileService().createWizardContext(faceConfig, flowType, state, votingRegion)).andReturn(wizardContext)
				.anyTimes();
		getMobileService().addResultsToWizardContext(results, wizardContext);
		final PdfGenerator pdfGenerator = createMock("PdfGenerator", PdfGenerator.class);
		EasyMock.expect(getPdfGeneratorFactory().createPdfGenerator(EasyMock.eq(wizardContext), EasyMock.anyObject(OutputStream.class)))
				.andReturn(pdfGenerator).anyTimes();
		pdfGenerator.run();
		pdfGenerator.dispose();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardContext.getWizardResults()).andReturn(wizardResults).anyTimes();
        final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
        EasyMock.expect( votingAddress.getState()).andReturn(state).anyTimes();
        EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).anyTimes();
        EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).anyTimes();
		EasyMock.expect( wizardResults.getAnswers() ).andReturn( Collections.<Answer>emptyList() ).anyTimes();
		wizardResults.setLastChangedDate((Date) EasyMock.anyObject());
		wizardResults.setDownloaded(true);
		wizardContext.processSaveResults(getQuestionnaireService());
		getMailingListService().saveToMailingListIfHasSignup((WizardResults) EasyMock.anyObject());
		replayAll();

		getMobileController().results(face, flowType, state, Long.toString(votingRegionId), results);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#states(String)} for the case where the country is not
	 * the USA.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	public final void testStates_notUSA() {
		final List<State> actualStates = getMobileController().states("CAN");

		assertNotNull("There is a list of states", actualStates);
		assertTrue("The list of states is empty", actualStates.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#states(String)} for the case where the country is the
	 * USA.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	public final void testStates_USA() {
		final State state = createMock("State", State.class);
		final List<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states).anyTimes();
		replayAll();

		final List<State> actualStates = getMobileController().states("USA");

		assertSame("The states are returned", states, actualStates);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#svid(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 6, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testSvid() {
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getMobileService().findSvidForState("VT")).andReturn(svid);
		replayAll();

		final StateSpecificDirectory actualSvid = getMobileController().svid("USA", "VT");

		assertSame("The SVID is returned", svid, actualSvid);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#svid(String, String)} for the case where a country
	 * other than the USA is requested.
	 * 
	 * @author IanBrown
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	@Test
	public final void testSvid_notUSA() {
		final StateSpecificDirectory actualSvid = getMobileController().svid("CAN", "ONT");

		assertNull("There is no SVID", actualSvid);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#svid(String, String)} for the case where the state is
	 * unknown.
	 * 
	 * @author IanBrown
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	@Test
	public final void testSvid_notValidState() {
		EasyMock.expect(getMobileService().findSvidForState("ONT")).andReturn(null);
		replayAll();

		final StateSpecificDirectory actualSvid = getMobileController().svid("USA", "ONT");

		assertNull("No SVID is returned", actualSvid);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#updateUser(String, String, com.bearcode.ovf.model.common.OverseasUser, javax.servlet.http.HttpServletResponse)}
	 * 
	 * @author IanBrown
	 * @since Jul 24, 2012
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testUpdateUser() {
		final String email = "email@somewhere.com";
		final String password = "password";
		final OverseasUser modifiedUser = createMock("ModifiedUser", OverseasUser.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(user);
		EasyMock.expect(user.getPassword()).andReturn(passwordMd5);
		final long id = 762l;
		EasyMock.expect(modifiedUser.getId()).andReturn(id);
		EasyMock.expect(user.getId()).andReturn(id);
		final String alternateEmail = "Alternate Email";
		EasyMock.expect(modifiedUser.getAlternateEmail()).andReturn(alternateEmail);
		user.setAlternateEmail(alternateEmail);
		final String alternatePhone = "Alternate Phone";
		EasyMock.expect(modifiedUser.getAlternatePhone()).andReturn(alternatePhone);
		user.setAlternatePhone(alternatePhone);
		final String ballotPref = "Ballot Pref";
		EasyMock.expect(modifiedUser.getBallotPref()).andReturn(ballotPref);
		user.setBallotPref(ballotPref);
		final int birthDate = 15;
		EasyMock.expect(modifiedUser.getBirthDate()).andReturn(birthDate);
		user.setBirthDate(birthDate);
		final int birthMonth = 6;
		EasyMock.expect(modifiedUser.getBirthMonth()).andReturn(birthMonth);
		user.setBirthMonth(birthMonth);
		final int birthYear = 1950;
		EasyMock.expect(modifiedUser.getBirthYear()).andReturn(birthYear);
		user.setBirthYear(birthYear);
		final UserAddress modifiedCurrentAddress = createMock("ModifiedCurrentAddress", UserAddress.class);
		EasyMock.expect(modifiedUser.getCurrentAddress()).andReturn(modifiedCurrentAddress);
		final UserAddress currentAddress = createMock("CurrentAddress", UserAddress.class);
		EasyMock.expect(user.getCurrentAddress()).andReturn(currentAddress);
		updateAddress("current", modifiedCurrentAddress, currentAddress);
		final String ethnicity = "Ethnicity";
		EasyMock.expect(modifiedUser.getEthnicity()).andReturn(ethnicity);
		user.setEthnicity(ethnicity);
		final UserAddress modifiedForwardingAddress = createMock("ModifiedForwardingAddress", UserAddress.class);
		EasyMock.expect(modifiedUser.getForwardingAddress()).andReturn(modifiedForwardingAddress);
		final UserAddress forwardingAddress = createMock("ForwardingAddress", UserAddress.class);
		EasyMock.expect(user.getForwardingAddress()).andReturn(forwardingAddress);
		updateAddress("forwarding", modifiedForwardingAddress, forwardingAddress);
		EasyMock.expect(modifiedUser.getGender()).andReturn(Gender.MALE.name());
		user.setGender(Gender.MALE.name());
		final Person modifiedName = createMock("ModifiedName", Person.class);
		EasyMock.expect(modifiedUser.getName()).andReturn(modifiedName);
		final Person name = createMock("Name", Person.class);
		EasyMock.expect(user.getName()).andReturn(name);
		updateName("name", modifiedName, name);
		final String party = "Party";
		EasyMock.expect(modifiedUser.getParty()).andReturn(party);
		user.setParty(party);
		final String phone = "12345678901";
		EasyMock.expect(modifiedUser.getPhone()).andReturn(phone);
		user.setPhone(phone);
		final UserAddress modifiedPreviousAddress = createMock("ModifiedPreviousAddress", UserAddress.class);
		EasyMock.expect(modifiedUser.getPreviousAddress()).andReturn(modifiedPreviousAddress);
		final UserAddress previousAddress = createMock("PreviousAddress", UserAddress.class);
		EasyMock.expect(user.getPreviousAddress()).andReturn(previousAddress);
		updateAddress("previous", modifiedPreviousAddress, previousAddress);
		final Person modifiedPreviousName = createMock("ModifiedPreviousName", Person.class);
		EasyMock.expect(modifiedUser.getPreviousName()).andReturn(modifiedPreviousName);
		final Person previousName = createMock("PreviousName", Person.class);
		EasyMock.expect(user.getPreviousName()).andReturn(previousName);
		updateName("previousName", modifiedPreviousName, previousName);
		final String race = "Race";
		EasyMock.expect(modifiedUser.getRace()).andReturn(race);
		user.setRace(race);
		final VoterHistory voterHistory = VoterHistory.FIRST_TIME_VOTER;
		EasyMock.expect(modifiedUser.getVoterHistory()).andReturn(voterHistory);
		user.setVoterHistory(voterHistory);
		final VoterType voterType = VoterType.OVERSEAS_VOTER;
		EasyMock.expect(modifiedUser.getVoterType()).andReturn(voterType);
		user.setVoterType(voterType);
		final UserAddress modifiedVotingAddress = createMock("ModifiedVotingAddress", UserAddress.class);
		EasyMock.expect(modifiedUser.getVotingAddress()).andReturn(modifiedVotingAddress);
		final UserAddress votingAddress = createMock("VotingAddress", UserAddress.class);
		EasyMock.expect(user.getVotingAddress()).andReturn(votingAddress);
		updateAddress("voting", modifiedVotingAddress, votingAddress);
		final VotingRegion modifiedVotingRegion = createMock("ModifiedVotingRegion", VotingRegion.class);
		EasyMock.expect(modifiedUser.getVotingRegion()).andReturn(modifiedVotingRegion);
		final long votingRegionId = 282l;
		EasyMock.expect(modifiedVotingRegion.getId()).andReturn(votingRegionId);
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(getStateService().findRegion(votingRegionId)).andReturn(votingRegion);
		user.setVotingRegion(votingRegion);
		getUserService().saveUser(user);
		replayAll();

		getMobileController().updateUser(emailMd5, passwordMd5, modifiedUser, response);

		assertEquals("The response says that the user was updated", HttpStatus.OK.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#updateUser(String, String, com.bearcode.ovf.model.common.OverseasUser, javax.servlet.http.HttpServletResponse)}
     * for the case where the
	 * user doesn't exist.
	 * 
	 * @author IanBrown
	 * @since Jul 24, 2012
	 * @version Jul 24, 2012
	 */
	@Test
	public final void testUpdateUser_noSuchUser() {
		final String email = "email@somewhere.com";
		final String password = "password";
		final OverseasUser modifiedUser = createMock("ModifiedUser", OverseasUser.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(null);
		replayAll();

		getMobileController().updateUser(emailMd5, passwordMd5, modifiedUser, response);

		assertEquals("The response says that the user was not found", HttpStatus.NOT_FOUND.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#updateUser(String, String, com.bearcode.ovf.model.common.OverseasUser, javax.servlet.http.HttpServletResponse)}
     * for the case where the
	 * password matches, but the ID does not.
	 * 
	 * @author IanBrown
	 * @since Jul 24, 2012
	 * @version Jul 24, 2012
	 */
	@Test
	public final void testUpdateUser_wrongId() {
		final String email = "email@somewhere.com";
		final String password = "password";
		final OverseasUser modifiedUser = createMock("ModifiedUser", OverseasUser.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(user);
		EasyMock.expect(user.getPassword()).andReturn(passwordMd5);
		final long modifiedId = 8728l;
		EasyMock.expect(modifiedUser.getId()).andReturn(modifiedId);
		final long id = 762l;
		EasyMock.expect(user.getId()).andReturn(id);
		replayAll();

		getMobileController().updateUser(emailMd5, passwordMd5, modifiedUser, response);

		assertEquals("The response says that the wrong ID was provided", HttpStatus.CONFLICT.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#updateUser(String, String, com.bearcode.ovf.model.common.OverseasUser, javax.servlet.http.HttpServletResponse)} for the case where the
	 * password doesn't match.
	 * 
	 * @author IanBrown
	 * @since Jul 24, 2012
	 * @version Jul 24, 2012
	 */
	@Test
	public final void testUpdateUser_wrongPassword() {
		final String email = "email@somewhere.com";
		final String password = "password";
		final OverseasUser modifiedUser = createMock("ModifiedUser", OverseasUser.class);
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByNameMd5(emailMd5)).andReturn(user);
		final String realPassword = "real password";
		final String realPasswordMd5 = md5Encoder.encodePassword(realPassword, null);
		EasyMock.expect(user.getPassword()).andReturn(realPasswordMd5);
		replayAll();

		getMobileController().updateUser(emailMd5, passwordMd5, modifiedUser, response);

		assertEquals("The response says that the wrong password was provided", HttpStatus.FORBIDDEN.value(), response.getStatus());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#votingRegions(String, String)} for the case where a
	 * country other than the USA is requested.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	public final void testVotingRegions_notUSA() {
		final List<VotingRegion> actualVotingRegions = getMobileController().votingRegions("CAN", "ONT");

		assertNotNull("A list of voting regions is returned", actualVotingRegions);
		assertTrue("There are no voting regions in the list", actualVotingRegions.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#votingRegions(String, String)} for the case where the
	 * state is not a valid one.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	public final void testVotingRegions_notValidState() {
		final List<VotingRegion> votingRegions = new ArrayList<VotingRegion>();
		EasyMock.expect(getStateService().findRegionsForState("ONT")).andReturn(votingRegions).anyTimes();
		replayAll();

		final List<VotingRegion> actualVotingRegions = getMobileController().votingRegions("USA", "ONT");

		assertSame("The voting regions are returned", votingRegions, actualVotingRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.mobile.MobileController#votingRegions(String, String)} for a US state.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	public final void testVotingRegions_usaState() {
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		final List<VotingRegion> votingRegions = Arrays.asList(votingRegion);
		EasyMock.expect(getStateService().findRegionsForState("VT")).andReturn(votingRegions).anyTimes();
		replayAll();

		final List<VotingRegion> actualVotingRegions = getMobileController().votingRegions("USA", "VT");

		assertSame("The voting regions are returned", votingRegions, actualVotingRegions);
		verifyAll();
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Gets the mailing list service.
	 * @author IanBrown
	 * @return the mailing list service.
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	private MailingListService getMailingListService() {
		return mailingListService;
	}

	/**
	 * Gets the mobile controller.
	 * 
	 * @author IanBrown
	 * @return the mobile controller.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private MobileController getMobileController() {
		return mobileController;
	}

	/**
	 * Gets the mobile service.
	 * 
	 * @author IanBrown
	 * @return the mobile service.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private MobileService getMobileService() {
		return mobileService;
	}

	/**
	 * Gets the PDF generator factory.
	 * 
	 * @author IanBrown
	 * @return the PDF generator factory.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private AdvancedPdfGeneratorFactory getPdfGeneratorFactory() {
		return pdfGeneratorFactory;
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private StateService getStateService() {
		return stateService;
	}

	/**
	 * Gets the user service.
	 * 
	 * @author IanBrown
	 * @return the user service.
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	private OverseasUserService getUserService() {
		return userService;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

	/**
	 * Sets the mailing list service.
	 * @author IanBrown
	 * @param mailingListService the mailing list service to set.
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	private void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	/**
	 * Sets the mobile controller.
	 * 
	 * @author IanBrown
	 * @param mobileController
	 *            the mobile controller to set.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private void setMobileController(final MobileController mobileController) {
		this.mobileController = mobileController;
	}

	/**
	 * Sets the mobile service.
	 * 
	 * @author IanBrown
	 * @param mobileService
	 *            the mobile service to set.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private void setMobileService(final MobileService mobileService) {
		this.mobileService = mobileService;
	}

	/**
	 * Sets the PDF generator factory.
	 * 
	 * @author IanBrown
	 * @param pdfGeneratorFactory
	 *            the PDF generator factory to set.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private void setPdfGeneratorFactory(final AdvancedPdfGeneratorFactory pdfGeneratorFactory) {
		this.pdfGeneratorFactory = pdfGeneratorFactory;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}

	/**
	 * Sets the user service.
	 * 
	 * @author IanBrown
	 * @param userService
	 *            the user service to set.
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	private void setUserService(final OverseasUserService userService) {
		this.userService = userService;
	}

	/**
	 * Updates the address.
	 * 
	 * @author IanBrown
	 * @param prefix
	 *            the address prefix.
	 * @param modified
	 *            the modified version of the address.
	 * @param existing
	 *            the existing version of the address.
	 * @since Jul 24, 2012
	 * @version Jul 25, 2012
	 */
	private void updateAddress(final String prefix, final UserAddress modified, final UserAddress existing) {
		EasyMock.expect(modified.isEmptySpace()).andReturn(false).anyTimes();
		final String city = prefix + " City";
		EasyMock.expect(modified.getCity()).andReturn(city);
		existing.setCity(city);
		final String country = "Country of " + prefix;
		EasyMock.expect(modified.getCountry()).andReturn(country);
		existing.setCountry(country);
		final String county = prefix + " County";
		EasyMock.expect(modified.getCounty()).andReturn(county);
		existing.setCounty(county);
		final String description = "Description of " + prefix;
		EasyMock.expect(modified.getDescription()).andReturn(description);
		existing.setDescription(description);
		final String state = prefix.substring(0, 2);
		EasyMock.expect(modified.getState()).andReturn(state);
		existing.setState(state);
		final String street1 = prefix.hashCode() % 100 + " " + prefix + " Street";
		EasyMock.expect(modified.getStreet1()).andReturn(street1);
		existing.setStreet1(street1);
		final String street2 = prefix + " Unit";
		EasyMock.expect(modified.getStreet2()).andReturn(street2);
		existing.setStreet2(street2);
		final AddressType type = "current".equals(prefix) ? AddressType.OVERSEAS
				: "forwarding".equals(prefix) ? AddressType.MILITARY : AddressType.STREET;
		EasyMock.expect(modified.getType()).andReturn(type);
		existing.setType(type);
		final String zip = Integer.toString(prefix.hashCode() % 100000);
		EasyMock.expect(modified.getZip()).andReturn(zip);
		existing.setZip(zip);
		final String zip4 = Integer.toString(prefix.hashCode() / 100 % 10000);
		EasyMock.expect(modified.getZip4()).andReturn(zip4);
		existing.setZip4(zip4);
	}

	/**
	 * Sets up to update the name.
	 * 
	 * @author IanBrown
	 * @param prefix
	 *            the prefix.
	 * @param modified
	 *            the modified name.
	 * @param existing
	 *            the existing name.
	 * @since Jul 24, 2012
	 * @version Jul 25, 2012
	 */
	private void updateName(final String prefix, final Person modified, final Person existing) {
		EasyMock.expect(modified.isEmpty()).andReturn(false).anyTimes();
		final String firstName = "First" + prefix;
		EasyMock.expect(modified.getFirstName()).andReturn(firstName);
		existing.setFirstName(firstName);
		final String lastName = prefix + "Last";
		EasyMock.expect(modified.getLastName()).andReturn(lastName);
		existing.setLastName(lastName);
		final String middleName = prefix + "Middle";
		EasyMock.expect(modified.getMiddleName()).andReturn(middleName);
		existing.setMiddleName(middleName);
		final String suffix = prefix.substring(prefix.length() - 2);
		EasyMock.expect(modified.getSuffix()).andReturn(suffix);
		existing.setSuffix(suffix);
		final String title = prefix.substring(0, 2);
		EasyMock.expect(modified.getTitle()).andReturn(title);
		existing.setTitle(title);
	}
}
