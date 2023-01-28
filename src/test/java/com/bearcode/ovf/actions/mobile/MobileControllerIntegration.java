/**
 * 
 */
package com.bearcode.ovf.actions.mobile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.easymock.EasyMock;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.bearcode.ovf.DAO.PdfAnswersDAO;
import com.bearcode.ovf.DAO.QuestionFieldDAO;
import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.DAO.VotingRegionDAO;
import com.bearcode.ovf.actions.commons.AbstractControllerExam;
import com.bearcode.ovf.actions.mobile.MobileController.UserDescription;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.MobileAnswerUtility;
import com.bearcode.ovf.model.MobileAnswerUtility.CurrentVariant;
import com.bearcode.ovf.model.MobileAnswerUtility.WorkResults;
import com.bearcode.ovf.model.common.Country;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.mobile.MobileAddress;
import com.bearcode.ovf.model.mobile.MobileAnswer;
import com.bearcode.ovf.model.mobile.MobileOption;
import com.bearcode.ovf.model.mobile.MobilePage;
import com.bearcode.ovf.model.mobile.MobilePerson;
import com.bearcode.ovf.model.mobile.MobileResults;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.PredefinedAnswer;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.OverseasUserService;

/**
 * Extended {@link AbstractControllerExam} integration test for {@link MobileController}.
 * 
 * @author IanBrown
 * 
 * @since Apr 13, 2012
 * @version Jan 11, 2013
 */
@ContextConfiguration(locations = { "MobileControllerIntegration-context.xml" })
@DirtiesContext
public final class MobileControllerIntegration extends AbstractControllerExam<MobileController> {

	/**
	 * the mailing list service.
	 * @author IanBrown
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	private MailingListService mailingListService;

	/**
	 * the PDF answers data access object.
	 * 
	 * @author IanBrown
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	@Autowired
	private PdfAnswersDAO pdfAnswersDAO;

	/**
	 * the question field DAO.
	 * 
	 * @author IanBrown
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	@Autowired
	private QuestionFieldDAO questionFieldDAO;

	/**
	 * the state DAO.
	 * 
	 * @author IanBrown
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	@Autowired
	private StateDAO stateDAO;
	
	/**
	 * the user service.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Autowired
	private OverseasUserService userService;
	
	/**
	 * the voting region DAO.
	 * @author IanBrown
	 * @since Aug 3, 2012
	 * @version Aug 3, 2012
	 */
	@Autowired
	private VotingRegionDAO votingRegionDAO;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#checkUser(String, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCheckUser() throws Exception {
		final OverseasUser user = userService.findUserById(1l);
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(user.getUsername(), null);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users/" + emailMd5);
		request.setMethod("HEAD");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that there is such a user", HttpStatus.OK.value(), response.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#checkUser(String, javax.servlet.http.HttpServletResponse)} for the
	 * case where the user does not exist.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCheckUser_noSuchUser() throws Exception {
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword("no_such_user", null);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users/" + emailMd5);
		request.setMethod("HEAD");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that there is no such user", HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	/**
	 * Tests loading of the countries.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCountries() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/countries");
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());

		final String content = response.getContentAsString();
		final List<Country> actualCountries = objectMapper.readValue(content, new TypeReference<List<Country>>() {
		});
		assertNotNull("A list of countries is returned", actualCountries);
		assertFalse("There are countries", actualCountries.isEmpty());
	}

	/**
	 * Test method to create and then update a user by adding an address.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Aug 3, 2012
	 * @version Aug 3, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCreateAndUpdateUser() throws Exception {
		final String username = "newuser@somewhereelse.com";
		final String password = "otherpassword";
		final String firstName = "FirstName";
		final String lastName = "LastName";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users");
		request.setMethod("POST");
		request.addHeader("Content-Type", "application/json");
		request.setContentType("application/json");
		final ObjectMapper objectMapper = new ObjectMapper();
		final UserDescription userDescription = new UserDescription();
		userDescription.setUsername(username);
		userDescription.setPassword(password);
		userDescription.setFirstName(firstName);
		userDescription.setLastName(lastName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, userDescription);
		baos.close();
		request.setContent(baos.toByteArray());
		getHandlerAdapter().handle(request, response, getController());

		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(username, null);
		final String passwordMd5 = md5Encoder.encodePassword(password, null);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("/users/" + emailMd5);
		request.setParameter("password", passwordMd5);
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		getHandlerAdapter().handle(request, response, getController());
		String content = response.getContentAsString();
		final OverseasUser workingUser = objectMapper.readValue(content, new TypeReference<OverseasUser>() {
		});

		assertNull("The user has no voting address yet", workingUser.getVotingAddress());
		final UserAddress votingAddress = new UserAddress();
		votingAddress.setStreet1("85 Granite Shed Lane");
		votingAddress.setStreet2("#1");
		votingAddress.setCity("Montpelier");
		votingAddress.setState("VT");
		votingAddress.setZip("05602");
		workingUser.setVotingAddress(votingAddress);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("/users/" + emailMd5);
		request.setParameter("password", passwordMd5);
		request.setMethod("PUT");
		request.addHeader("Content-Type", "application/json");
		request.setContentType("application/json");
		baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, workingUser);
		baos.close();
		request.setContent(baos.toByteArray());

		getHandlerAdapter().handle(request, response, getController());

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("/users/" + emailMd5);
		request.setParameter("password", passwordMd5);
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		getHandlerAdapter().handle(request, response, getController());
		content = response.getContentAsString();
		final OverseasUser actualUser = objectMapper.readValue(content, new TypeReference<OverseasUser>() {
		});
		final UserAddress actualVotingAddress = actualUser.getVotingAddress();
		assertNotNull("The user now has a voting address", actualVotingAddress);
		assertNotNull("The voting address has a database ID", actualVotingAddress.getId());
		assertEquals("The street 1 line is set", votingAddress.getStreet1(), actualVotingAddress.getStreet1());
		assertEquals("The street 2 line is set", votingAddress.getStreet2(), actualVotingAddress.getStreet2());
		assertEquals("The city is set", votingAddress.getCity(), actualVotingAddress.getCity());
		assertEquals("The state is set", votingAddress.getState(), actualVotingAddress.getState());
		assertEquals("The ZIP code is set", votingAddress.getZip(), actualVotingAddress.getZip());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(UserDescription, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCreateUser() throws Exception {
		final String username = "newuser@somewhere.com";
		final String password = "password";
		final String firstName = "First";
		final String lastName = "Last";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users");
		request.setMethod("POST");
		request.addHeader("Content-Type", "application/json");
		request.setContentType("application/json");
		final ObjectMapper objectMapper = new ObjectMapper();
		final UserDescription userDescription = new UserDescription();
		userDescription.setUsername(username);
		userDescription.setPassword(password);
		userDescription.setFirstName(firstName);
		userDescription.setLastName(lastName);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, userDescription);
		baos.close();
		request.setContent(baos.toByteArray());

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that the user was created", HttpStatus.CREATED.value(), response.getStatus());
		final String content = response.getContentAsString();
		final OverseasUser actualUser = objectMapper.readValue(content, new TypeReference<OverseasUser>() {
		});
		assertNotNull("A user is returned", actualUser);
		assertNull("The user's password is cleared", actualUser.getPassword());
		final OverseasUser databaseUser = userService.findUserById(actualUser.getId());
		assertNotNull("The user is stored in the database", databaseUser);
		final PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		final String passwordMd5 = passwordEncoder.encodePassword(password, null);
		assertEquals("The saved password is encoded", passwordMd5, databaseUser.getPassword());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where the user already exists.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCreateUser_alreadyExists() throws Exception {
		final OverseasUser user = userService.findUserById(2l);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users");
		request.setMethod("POST");
		request.addHeader("Content-Type", "application/json");
		request.setContentType("application/json");
		final ObjectMapper objectMapper = new ObjectMapper();
		final UserDescription userDescription = new UserDescription();
		userDescription.setUsername(user.getUsername());
		userDescription.setPassword(user.getPassword());
		userDescription.setFirstName(user.getName().getFirstName());
		userDescription.setMiddleName(user.getName().getMiddleName());
		userDescription.setLastName(user.getName().getLastName());
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, userDescription);
		baos.close();
		request.setContent(baos.toByteArray());

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that the user already exists", HttpStatus.CONFLICT.value(), response.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#createUser(UserDescription, javax.servlet.http.HttpServletResponse)}
	 * for the case where parameters are missing.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCreateUser_missingParameters() throws Exception {
		final String username = "email@somewhere.com";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users");
		request.setMethod("POST");
		request.addHeader("Content-Type", "application/json");
		request.setContentType("application/json");
		final ObjectMapper objectMapper = new ObjectMapper();
		final UserDescription userDescription = new UserDescription();
		userDescription.setUsername(username);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, userDescription);
		baos.close();
		request.setContent(baos.toByteArray());

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that there are missing parameters", HttpStatus.BAD_REQUEST.value(),
				response.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#getUser(String, String, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testGetUser() throws Exception {
		final OverseasUser user = userService.findUserById(2l);
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(user.getUsername(), null);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users/" + emailMd5);
		request.setParameter("password", user.getPassword());
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that the user was retrieved", HttpStatus.OK.value(), response.getStatus());
		final String content = response.getContentAsString();
		final OverseasUser actualUser = objectMapper.readValue(content, new TypeReference<OverseasUser>() {
		});
		assertNotNull("A user is returned", actualUser);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#getUser(String, String, javax.servlet.http.HttpServletResponse)} for
	 * the case where the user does not exist.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testGetUser_noSuchUser() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword("no_such_user", null);
		final String passwordMd5 = md5Encoder.encodePassword("no good", null);
		request.setRequestURI("/users/" + emailMd5);
		request.setParameter("password", passwordMd5);
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that there is no such user", HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.mobile.MobileController#getUser(String, String, javax.servlet.http.HttpServletResponse)} for
	 * the case where the password is no good.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 6, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testGetUser_wrongPassword() throws Exception {
		final OverseasUser user = userService.findUserById(2l);
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(user.getUsername(), null);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/users/" + emailMd5);
		request.setParameter("password", "no good");
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		assertEquals("The response indicates that the wrong password was supplied", HttpStatus.FORBIDDEN.value(),
				response.getStatus());
	}

	/**
	 * Tests loading of a local official.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
     *  TODO the method marked as @Ignore for a while mobile application uses another model for LocalOfficial class.
	 */
    @Ignore
	@Test
	@OVFDBUnitUseData
	public final void testLocalOfficial() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/country/USA/state/VT/voting_region/Windsor/local_official");
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());

		final String content = response.getContentAsString();
		final LocalOfficial actualLocalOfficial = objectMapper.readValue(content, LocalOfficial.class);
		assertNotNull("A local official is returned", actualLocalOfficial);
		assertEquals("The local official is for the expected voting region", "Windsor", actualLocalOfficial.getRegion().getName());
		assertEquals("The local official is for the expected state", "VT", actualLocalOfficial.getRegion().getState().getAbbr());
	}

	/**
	 * Tests loading of the questions for a face for the case where part of the request cannot be matched in the database.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem performing a request.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	@Test(expected = NoSuchRequestHandlingMethodException.class)
	@OVFDBUnitUseData
	public final void testQuestion_unmatched() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final String face = "badface";
		final FlowType flowType = FlowType.RAVA;
		final String state = "FL";
		final String votingRegionName = "Okaloosa County";
		request.setRequestURI("/questions/" + face + "/" + flowType.name() + "/" + state + "/" + votingRegionName);
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		assertSame("The response indicates that things were not found", HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	/**
	 * Tests loading of the questions for a face, flow, state, and voting region, followed by response.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem performing a request.
	 * @since Apr 13, 2012
	 * @version Jan 11, 2013
	 */
	@SuppressWarnings("unchecked")
	@Test
	@OVFDBUnitUseData
	public final void testQuestionResults() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		final String face = "okaloosa";
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final String state = "FL";
		final String votingRegionName = "Okaloosa County";
		request.setRequestURI("/questions/" + face + "/" + flowType.name() + "/" + state + "/" + votingRegionName);
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());
		final String content = response.getContentAsString();
		final List<MobilePage> mobilePages = objectMapper.readValue(content, new TypeReference<List<MobilePage>>() {
		});
		final MobileResults mobileResults = buildResults(face, flowType, state, votingRegionName, mobilePages);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("/results/" + face + "/" + flowType.name() + "/" + state + "/" + votingRegionName);
		request.setMethod("POST");
		request.addHeader("Content-Type", "application/json");
		request.setContentType("application/json");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, mobileResults);
		baos.close();
		request.setContent(baos.toByteArray());
		getMailingListService().saveToMailingListIfHasSignup((WizardResults) EasyMock.anyObject());
		EasyMock.replay(getMailingListService());
		getHandlerAdapter().handle(request, response, getController());
		// final org.codehaus.jackson.map.ObjectWriter ppWriter = objectMapper.defaultPrettyPrintingWriter();
		// System.out.println(ppWriter.writeValueAsString(mobileResults));

		final List<String> actualHeaders = response.getHeaders("Content-Type");
		assertTrue("The content is PDF", actualHeaders.contains("application/pdf"));
		assertNotNull("There is a content string", content);
		assertTrue("There is content", content.length() > 0);
		final DetachedCriteria criteria = DetachedCriteria.forClass(WizardResults.class);
		final Collection<WizardResults> allResults = pdfAnswersDAO.findBy(criteria);
		final WizardResults wizardResults = allResults.iterator().next();
		assertResults(mobileResults, wizardResults);
		EasyMock.verify(getMailingListService());
	}

	/**
	 * Tests loading of the questions for a face, flow, state, and voting region, followed by response for the case where the voting region is provided as an identifier.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem performing a request.
	 * @since Aug 3, 2012
	 * @version Jan 11, 2013
	 */
	@SuppressWarnings("unchecked")
	@Test
	@OVFDBUnitUseData
	public final void testQuestionResults_identifier() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		final String face = "okaloosa";
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final String state = "FL";
		final String votingRegionName = "Okaloosa County";
		VotingRegion requestRegion = new VotingRegion();
		requestRegion.setState(stateDAO.getByAbbreviation(state));
		requestRegion.setName(votingRegionName);
		final VotingRegion region = votingRegionDAO.getRegionByName(requestRegion);
		final long votingRegionId = region.getId();
		request.setRequestURI("/questions/" + face + "/" + flowType.name() + "/" + state + "/" + votingRegionId);
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());
		final String content = response.getContentAsString();
		final List<MobilePage> mobilePages = objectMapper.readValue(content, new TypeReference<List<MobilePage>>() {
		});
		final MobileResults mobileResults = buildResults(face, flowType, state, votingRegionName, mobilePages);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("/results/" + face + "/" + flowType.name() + "/" + state + "/" + votingRegionId);
		request.setMethod("POST");
		request.addHeader("Content-Type", "application/json");
		request.setContentType("application/json");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, mobileResults);
		baos.close();
		request.setContent(baos.toByteArray());
		getMailingListService().saveToMailingListIfHasSignup((WizardResults) EasyMock.anyObject());
		EasyMock.replay(getMailingListService());
		getHandlerAdapter().handle(request, response, getController());
		// final org.codehaus.jackson.map.ObjectWriter ppWriter = objectMapper.defaultPrettyPrintingWriter();
		// System.out.println(ppWriter.writeValueAsString(mobileResults));

		final List<String> actualHeaders = response.getHeaders("Content-Type");
		assertTrue("The content is PDF", actualHeaders.contains("application/pdf"));
		assertNotNull("There is a content string", content);
		assertTrue("There is content", content.length() > 0);
		final DetachedCriteria criteria = DetachedCriteria.forClass(WizardResults.class);
		final Collection<WizardResults> allResults = pdfAnswersDAO.findBy(criteria);
		final WizardResults wizardResults = allResults.iterator().next();
		assertResults(mobileResults, wizardResults);
		EasyMock.verify(getMailingListService());
	}

	/**
	 * Tests loading of the states.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testStates() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/country/USA/states");
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());

		final String content = response.getContentAsString();
		final List<State> actualStates = objectMapper.readValue(content, new TypeReference<List<State>>() {
		});
		assertNotNull("A list of states is returned", actualStates);
		assertFalse("There are states", actualStates.isEmpty());
	}

	/**
	 * Test method for loading the state specific directory.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem retrieving the state specific directory.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testSvid() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/country/USA/state/VT/svid");
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());

		final String content = response.getContentAsString();
		final StateSpecificDirectory svid = objectMapper.readValue(content, new TypeReference<StateSpecificDirectory>() {
		});
		// final org.codehaus.jackson.map.ObjectWriter ppWriter = objectMapper.defaultPrettyPrintingWriter();
		// System.out.println(ppWriter.writeValueAsString(svid));
		assertNotNull("Content is an SVID", svid);
		assertEquals("The SVID is for the correct state", "VT", svid.getState().getAbbr());
	}

	/**
	 * Tests loading of the voting regions.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testVotingRegions() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/country/USA/state/VT/voting_regions");
		request.setMethod("GET");
		request.addHeader("Accept", "application/json");
		final ObjectMapper objectMapper = new ObjectMapper();

		getHandlerAdapter().handle(request, response, getController());

		final String content = response.getContentAsString();
		final List<VotingRegion> actualVotingRegions = objectMapper.readValue(content, new TypeReference<List<VotingRegion>>() {
		});
		assertNotNull("A list of voting regions is returned", actualVotingRegions);
		assertFalse("There are voting regions", actualVotingRegions.isEmpty());
		for (final VotingRegion actualVotingRegion : actualVotingRegions) {
			assertEquals(actualVotingRegion.getFullName() + " is for the correct state", "VT", actualVotingRegion.getState()
					.getAbbr());
		}
	}

	/** {@inheritDoc} */
	@Override
	protected final MobileController createController() {
		final MobileController mobileController = applicationContext.getBean(MobileController.class);
		// We want to be sure that we don't update a mailing list by mistake here.
		mobileController.setMailingListService(getMailingListService());
		return mobileController;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForController() throws Exception {
		setMailingListService(EasyMock.createMock("MailingListService", MailingListService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForController() {
		setMailingListService(null);
	}

	/**
	 * Custom assertion to ensure that the address is copied correctly.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the address.
	 * @param mobileAddress
	 *            the mobile address.
	 * @param wizardResultAddress
	 *            the wizard result address.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private void assertAddress(final String name, final MobileAddress mobileAddress, final WizardResultAddress wizardResultAddress) {
		if (mobileAddress == null) {
			assertNotNull("There is a type for " + name, wizardResultAddress.getType());
			assertEquals("The street 1 for " + name + " is correct", "", wizardResultAddress.getStreet1());
			assertEquals("The street 2 for " + name + " is correct", "", wizardResultAddress.getStreet2());
			assertEquals("The city for " + name + " is correct", "", wizardResultAddress.getCity());
			assertEquals("The county for " + name + " is correct", "", wizardResultAddress.getCounty());
			assertEquals("The state for " + name + " is correct", "", wizardResultAddress.getState());
			assertEquals("The ZIP for " + name + " is correct", "", wizardResultAddress.getZip());
			assertEquals("The ZIP+4 for " + name + " is correct", "", wizardResultAddress.getZip4());
			assertEquals("The country for " + name + " is correct", "", wizardResultAddress.getCountry());
			assertEquals("The description for " + name + " is correct", "", wizardResultAddress.getDescription());
		} else {
			assertEquals("The type for " + name + " is correct", mobileAddress.getType(), wizardResultAddress.getType());
			assertBlankOrEquals("The street 1 for " + name + " is correct", mobileAddress.getStreet1(),
					wizardResultAddress.getStreet1());
			assertBlankOrEquals("The street 2 for " + name + " is correct", mobileAddress.getStreet2(),
					wizardResultAddress.getStreet2());
			assertBlankOrEquals("The city for " + name + " is correct", mobileAddress.getCity(), wizardResultAddress.getCity());
			assertBlankOrEquals("The county for " + name + " is correct", mobileAddress.getCounty(),
					wizardResultAddress.getCounty());
			assertBlankOrEquals("The state for " + name + " is correct", mobileAddress.getState(), wizardResultAddress.getState());
			assertBlankOrEquals("The ZIP for " + name + " is correct", mobileAddress.getZip(), wizardResultAddress.getZip());
			assertBlankOrEquals("The ZIP+4 for " + name + " is correct", mobileAddress.getZip4(), wizardResultAddress.getZip4());
			assertBlankOrEquals("The country for " + name + " is correct", mobileAddress.getCountry(),
					wizardResultAddress.getCountry());
			assertBlankOrEquals("The description for " + name + " is correct", mobileAddress.getDescription(),
					wizardResultAddress.getDescription());
		}
	}

	/**
	 * Custom assertion to ensure that the wizard answer matches the mobile answer.
	 * 
	 * @author IanBrown
	 * @param mobileAnswer
	 *            the mobile answer.
	 * @param wizardAnswer
	 *            the wizard answer.
	 * @since Apr 16, 2012
	 * @version Jul 12, 2012
	 */
	private void assertAnswer(final MobileAnswer mobileAnswer, final Answer wizardAnswer) {
		assertNotNull("There is a wizard answer for " + mobileAnswer, wizardAnswer);
		final MobileOption mobileOption = mobileAnswer.getOption();
		if (mobileOption.getId() == null) {
			assertEquals("The wizard answer for " + mobileAnswer + " is correct", mobileOption.getValue(), wizardAnswer.getValue());

		} else {
			assertEquals("The wizard answer is a predefined one", PredefinedAnswer.class, wizardAnswer.getClass());
			final PredefinedAnswer predefinedAnswer = (PredefinedAnswer) wizardAnswer;
			assertEquals("The predefined answer for " + mobileAnswer + " has the correct ID", mobileOption.getId().longValue(),
					predefinedAnswer.getSelectedValue().getId());
			// assertEquals("The predefined answer for " + mobileAnswer + " has the correct value", mobileOption.getValue(),
			// predefinedAnswer.getValue());
		}
	}

	/**
	 * Custom assertion to ensure that the answers were copied correctly.
	 * 
	 * @author IanBrown
	 * @param mobileAnswers
	 *            the mobile answers.
	 * @param wizardAnswers
	 *            the wizard answers.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private void assertAnswers(final List<MobileAnswer> mobileAnswers, final Collection<Answer> wizardAnswers) {
		assertEquals("There are the correct number of answers", mobileAnswers.size(), wizardAnswers.size());
		for (final MobileAnswer mobileAnswer : mobileAnswers) {
			final Answer wizardAnswer = findWizardAnswer(mobileAnswer, wizardAnswers);
			assertAnswer(mobileAnswer, wizardAnswer);
		}
	}

	/**
	 * Custom assertion to ensure that the actual string matches the expected string or is blank if the expected is
	 * <code>null</code>.
	 * 
	 * @author IanBrown
	 * @param message
	 *            the message.
	 * @param expected
	 *            the expected string.
	 * @param actual
	 *            the actual string.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private void assertBlankOrEquals(final String message, final String expected, final String actual) {
		if (expected == null) {
			assertEquals(message, "", actual);
		} else {
			assertEquals(message, expected, actual);
		}
	}

	/**
	 * Custom assertion to ensure that person was copied correctly.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the person field.
	 * @param mobilePerson
	 *            the mobile person field - may be <code>null</code>.
	 * @param wizardResultsPerson
	 *            the wizard results person field.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private void assertPerson(final String name, final MobilePerson mobilePerson, final WizardResultPerson wizardResultsPerson) {
		if (mobilePerson == null) {
			assertEquals("There is no title for " + name, "", wizardResultsPerson.getTitle());
			assertEquals("There is no first name for " + name, "", wizardResultsPerson.getFirstName());
			assertEquals("There is no middle name for " + name, "", wizardResultsPerson.getMiddleName());
			assertEquals("There is no last name for " + name, "", wizardResultsPerson.getLastName());
			assertEquals("There is no suffix for " + name, "", wizardResultsPerson.getSuffix());
		} else {
			assertEquals("The title for " + name + " is correct", mobilePerson.getTitle(), wizardResultsPerson.getTitle());
			assertEquals("The first name for " + name + " is correct", mobilePerson.getFirstName(),
					wizardResultsPerson.getFirstName());
			assertEquals("The middle name for " + name + " is correct", mobilePerson.getMiddleName(),
					wizardResultsPerson.getMiddleName());
			assertEquals("The last name for " + name + " is correct", mobilePerson.getLastName(), wizardResultsPerson.getLastName());
			assertEquals("The suffix for " + name + " is correct", mobilePerson.getSuffix(), wizardResultsPerson.getSuffix());
		}
	}

	/**
	 * Custom assertion to ensure that the mobile results are copied correctly.
	 * 
	 * @author IanBrown
	 * @param mobileResults
	 *            the mobile results.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private void assertResults(final MobileResults mobileResults, final WizardResults wizardResults) {
		assertBlankOrEquals("The username is set", mobileResults.getEmailAddress(), wizardResults.getUsername());
		assertPerson("name", mobileResults.getName(), wizardResults.getName());
		assertPerson("previousName", mobileResults.getPreviousName(), wizardResults.getPreviousName());
		assertEquals("The birth month is correct", mobileResults.getBirthMonth(), wizardResults.getBirthMonth());
		assertEquals("The birth date is correct", mobileResults.getBirthDay(), wizardResults.getBirthDate());
		assertEquals("The birth year is correct", mobileResults.getBirthYear(), wizardResults.getBirthYear());
		assertBlankOrEquals("The alternate email address is correct", mobileResults.getAlternateEmail(),
				wizardResults.getAlternateEmail());
		assertBlankOrEquals("The phone is correct", mobileResults.getPhone(), wizardResults.getPhone());
		assertBlankOrEquals("The alternate phone is correct", mobileResults.getAlternatePhone(), wizardResults.getAlternatePhone());
		assertAddress("currentAddress", mobileResults.getCurrentAddress(), wizardResults.getCurrentAddress());
		assertAddress("votingAddress", mobileResults.getVotingAddress(), wizardResults.getVotingAddress());
		assertAddress("forwardingAddress", mobileResults.getForwardingAddress(), wizardResults.getForwardingAddress());
		assertAddress("previousAddress", mobileResults.getPreviousAddress(), wizardResults.getPreviousAddress());
		assertBlankOrEquals("votingRegionName", mobileResults.getVotingRegionName(), wizardResults.getVotingRegionName());
		assertBlankOrEquals("votingRegionState", mobileResults.getVotingRegionState(), wizardResults.getVotingRegionState());
		assertNotNull("There is a voting region", wizardResults.getVotingRegion());
		assertBlankOrEquals("The voter type is correct", mobileResults.getVoterType(), wizardResults.getVoterType());
		assertBlankOrEquals("The voter history is correct", mobileResults.getVoterHistory(), wizardResults.getVoterHistory());
		assertBlankOrEquals("The ballot preference is correct", mobileResults.getBallotPreference(), wizardResults.getBallotPref());
		assertBlankOrEquals("The race is correct", mobileResults.getRace(), wizardResults.getRace());
		assertBlankOrEquals("The ethnicity is correct", mobileResults.getEthnicity(), wizardResults.getEthnicity());
		assertBlankOrEquals("The gender is correct", mobileResults.getGender(), wizardResults.getGender());
		assertBlankOrEquals("The party is correct", mobileResults.getParty(), wizardResults.getParty());
		assertAnswers(mobileResults.getAnswers(), wizardResults.getAnswers());
		assertEquals("The downloaded flag is correct", mobileResults.isDownloaded(), wizardResults.isDownloaded());
	}

	/**
	 * Builds the results for the input set of pages.
	 * 
	 * @author IanBrown
	 * @param face
	 *            the face.
	 * @param flowType
	 *            the type of flow.
	 * @param state
	 *            the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param mobilePages
	 *            the mobile pages.
	 * @return the results.
	 * @throws InvocationTargetException
	 *             if there is a problem invoking a method via reflection.
	 * @throws IllegalAccessException
	 *             if there is a problem accessing a method via reflection.
	 * @since Apr 16, 2012
	 * @version Jul 12, 2012
	 */
	private MobileResults buildResults(final String face, final FlowType flowType, final String state,
			final String votingRegionName, final List<MobilePage> mobilePages) throws IllegalAccessException,
			InvocationTargetException {
		final MobileResults mobileResults;
		final State stateObj = stateDAO.getByAbbreviation(state);
		final WizardResults wizardResults = new WizardResults(flowType);
		final WorkResults workResults = new WorkResults();
		MobileAnswerUtility.populateWizardResults(stateObj, null, workResults, wizardResults, null);
		wizardResults.setVotingRegionName(votingRegionName);
		wizardResults.setVotingRegionState(state);
		wizardResults.setForwardingAddress(null);
		wizardResults.setDownloaded(true);
		final Stack<CurrentVariant> currentVariants = new Stack<CurrentVariant>();
		wizardResults.setAnswers(MobileAnswerUtility.buildAnswersForPages(questionFieldDAO, mobilePages, true, currentVariants,
				null));
		mobileResults = MobileAnswerUtility.convertWizardResultsToMobileResults(wizardResults);
		return mobileResults;
	}

	/**
	 * Finds the wizard answer corresponding to the mobile answer.
	 * 
	 * @author IanBrown
	 * @param mobileAnswer
	 *            the mobile answer.
	 * @param wizardAnswers
	 *            the wizard answer.
	 * @return the wizard answer or <code>null</code> if no match was found.
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private Answer findWizardAnswer(final MobileAnswer mobileAnswer, final Collection<Answer> wizardAnswers) {
		for (final Answer wizardAnswer : wizardAnswers) {
			if (wizardAnswer.getField().getId() == mobileAnswer.getQuestionFieldId()) {
				return wizardAnswer;
			}
		}

		return null;
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
	 * Sets the mailing list service.
	 * @author IanBrown
	 * @param mailingListService the mailing list service to set.
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	private void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
}
