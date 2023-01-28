/**
 * 
 */

package com.bearcode.ovf.actions.mobile;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.mobile.MobilePage;
import com.bearcode.ovf.model.mobile.MobileResults;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.service.*;
import com.bearcode.ovf.tools.pdf.AdvancedPdfGeneratorFactory;
import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.generator.PdfGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Controller for mobile device application (REST).
 * 
 * @author IanBrown
 * 
 * @since Apr 9, 2012
 * @version Sep 21, 2012
 */
@Controller
public class MobileController {

	/**
	 * Simple class used to describe a user to be created.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	static class UserDescription {

		/**
		 * the user name (email address) of the user.
		 * 
		 * @author IanBrown
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		private String username;

		/**
		 * the password of the user.
		 * 
		 * @author IanBrown
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		private String password;

		/**
		 * the first name of the user.
		 * 
		 * @author IanBrown
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		private String firstName;

		/**
		 * the middle name of the user.
		 * 
		 * @author IanBrown
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		private String middleName;

		/**
		 * the last name of the user.
		 * 
		 * @author IanBrown
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		private String lastName;

		/**
		 * Gets the first name.
		 * 
		 * @author IanBrown
		 * @return the first name.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public String getFirstName() {
			return firstName;
		}

		/**
		 * Gets the last name.
		 * 
		 * @author IanBrown
		 * @return the lastName.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * Gets the middle name.
		 * 
		 * @author IanBrown
		 * @return the middle name.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public String getMiddleName() {
			return middleName;
		}

		/**
		 * Gets the password.
		 * 
		 * @author IanBrown
		 * @return the password.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * Gets the username.
		 * 
		 * @author IanBrown
		 * @return the username.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * Sets the first name.
		 * 
		 * @author IanBrown
		 * @param firstName
		 *            the first name to set.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public void setFirstName(final String firstName) {
			this.firstName = firstName;
		}

		/**
		 * Sets the last name.
		 * 
		 * @author IanBrown
		 * @param lastName
		 *            the lastName to set.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public void setLastName(final String lastName) {
			this.lastName = lastName;
		}

		/**
		 * Sets the middle name.
		 * 
		 * @author IanBrown
		 * @param middleName
		 *            the middle name to set.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public void setMiddleName(final String middleName) {
			this.middleName = middleName;
		}

		/**
		 * Sets the password.
		 * 
		 * @author IanBrown
		 * @param password
		 *            the password to set.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public void setPassword(final String password) {
			this.password = password;
		}

		/**
		 * Sets the username.
		 * 
		 * @author IanBrown
		 * @param username
		 *            the username to set.
		 * @since Jun 7, 2012
		 * @version Jun 7, 2012
		 */
		public void setUsername(final String username) {
			this.username = username;
		}
	}

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	private final static Logger logger = LoggerFactory.getLogger(MobileController.class);

	/**
	 * the service to convert between the database format and the mobile JSON objects.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	@Autowired
	private MobileService mobileService;

	/**
	 * the service used to store the results in database format.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	@Autowired
	private QuestionnaireService questionnaireService;

	/**
	 * the service to get face information.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	@Autowired
	private FacesService facesService;

	/**
	 * the service to get local officials.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Autowired
	private LocalOfficialService localOfficialService;

	/**
	 * the service to work with users.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Autowired
	private OverseasUserService userService;

	/**
	 * the PDF generator factory.
	 * 
	 * @author IanBrown
	 * @since Apr 16, 2012
	 * @version Apr 16, 2012
	 */
	@Autowired
	private AdvancedPdfGeneratorFactory pdfGeneratorFactory;

	/**
	 * the mailing list service.
	 * 
	 * @author IanBrown
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	@Autowired
	private MailingListService mailingListService;

	/**
	 * the service to get the states and countries.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Autowired
	private StateService stateService;

	/**
	 * Determines if there is a user with the specified email address (MD5 hashed).
	 * 
	 * @author IanBrown
	 * @param email
	 *            the email address.
	 * @param response
	 *            the response.
	 * @since Jun 6, 2012
	 * @version Aug 3, 2012
	 */
	@RequestMapping(value = "/users/{email:.*}", method = RequestMethod.HEAD)
	public void checkUser(@PathVariable final String email, final HttpServletResponse response) {
		final OverseasUser user = getUserService().findUserByNameMd5(email);
		response.setStatus(user == null ? HttpStatus.NOT_FOUND.value() : HttpStatus.OK.value());
	}

	/**
	 * Gets the list of countries.
	 * 
	 * @author IanBrown
	 * @return the countries.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@RequestMapping(value = "/countries", method = RequestMethod.GET)
	public @ResponseBody
	List<Country> countries() {
		return (List<Country>) getStateService().findAllCountries();
	}

	/**
	 * Creates a new user using the description read from the request body.
	 * 
	 * @author IanBrown
	 * @param userDescription
	 *            the description of the user.
	 * @param response
	 *            the response.
	 * @return the new user.
	 * @since Jun 6, 2012
	 * @version Aug 3, 2012
	 */
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public @ResponseBody
	OverseasUser createUser(@RequestBody final UserDescription userDescription, final HttpServletResponse response) {
		final String username = userDescription.getUsername();
		if (username == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}
		if (userService.findUserByName(username) != null) {
			response.setStatus(HttpStatus.CONFLICT.value());
			return null;
		}

		final String password = userDescription.getPassword();
		if (password == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}

		final String firstName = userDescription.getFirstName();
		if (firstName == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}

		final String lastName = userDescription.getLastName();
		if (lastName == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}

		final String middleName = userDescription.getMiddleName();
		final OverseasUser user = new OverseasUser();
		user.setUsername(username);
		user.setPassword(password);
		user.getName().setFirstName(firstName);
		if (middleName != null) {
			user.getName().setMiddleName(middleName);
		}
		user.getName().setLastName(lastName);
		getUserService().makeNewUser(user);
		response.setStatus(HttpStatus.CREATED.value());
		return user;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	public FacesService getFacesService() {
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
	public LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Gets the mailing list service.
	 * 
	 * @author IanBrown
	 * @return the mailing list service.
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	/**
	 * Gets the mobile service.
	 * 
	 * @author IanBrown
	 * @return the mobile service.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	public MobileService getMobileService() {
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
	public AdvancedPdfGeneratorFactory getPdfGeneratorFactory() {
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
	public QuestionnaireService getQuestionnaireService() {
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
	public StateService getStateService() {
		return stateService;
	}

	/**
	 * Gets the user with the specified email address (MD5 hashed).
	 * 
	 * @author IanBrown
	 * @param email
	 *            the email address.
	 * @param password
	 *            the password.
	 * @param response
	 *            the response.
	 * @return the user.
	 * @since Jun 6, 2012
	 * @version Aug 3, 2012
	 */
	@RequestMapping(value = "/users/{email:.*}", method = RequestMethod.GET, params = "password")
	public @ResponseBody
	OverseasUser getUser(@PathVariable final String email,
			@RequestParam(value = "password", required = true) final String password, final HttpServletResponse response) {
		final OverseasUser user = getUserService().findUserByNameMd5(email);
		if (user == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}

		if (!user.getPassword().equals(password)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return null;
		}

		return user;
	}

	/**
	 * Gets the user service.
	 * 
	 * @author IanBrown
	 * @return the user service.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	public OverseasUserService getUserService() {
		return userService;
	}

	/**
	 * Return a Heartbeat
	 * 
	 * @return current timestamp
	 */
	@RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
	@ResponseBody
	public Long heartBeat() {
		logger.debug("heartBeat:");
		return new Long(System.currentTimeMillis());
	}

	/**
	 * Gets the local official for the voting region.
	 * 
	 * @author IanBrown
	 * @param countryAbbreviation
	 *            the country abbreviation - only USA works.
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the local official.
	 * @since Apr 23, 2012
	 * @version Sep 18, 2012
	 */
	@RequestMapping(
			value = "/country/{countryAbbreviation}/state/{stateAbbreviation}/voting_region/{votingRegionName}/local_official",
			method = RequestMethod.GET)
	public @ResponseBody
	LocalOfficial localOfficial(@PathVariable final String countryAbbreviation, @PathVariable final String stateAbbreviation,
			@PathVariable final String votingRegionName) {
		if (!"USA".equals(countryAbbreviation)) {
			return null;
		}
		final String decodedRegion = decodeVotingRegion(votingRegionName);

		return getMobileService().findLocalOfficialForRegion(stateAbbreviation, decodedRegion);
	}

	/**
	 * Gets the questions (by page and group) for the specified face, flow type, and voting state.
	 * 
	 * @author IanBrown
	 * @param face
	 *            the face name.
	 * @param flowType
	 *            the flow type.
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @return the list of pages containing the questions.
	 * @throws NoSuchRequestHandlingMethodException
	 *             if the request cannot be handled.
	 * @since Apr 9, 2012
	 * @version Aug 3, 2012
	 */
	@RequestMapping(value = "/questions/{face}/{flowType}/{state}/{votingRegion}", method = RequestMethod.GET)
	public @ResponseBody
	List<MobilePage> questions(@PathVariable final String face, @PathVariable final FlowType flowType,
			@PathVariable final String state, @PathVariable final String votingRegion) throws NoSuchRequestHandlingMethodException {
		final FaceConfig faceConfig = getFacesService().findConfigByPrefix("faces/" + face);
		final String decodedRegion = decodeVotingRegion(votingRegion);

		if (faceConfig == null || !faceConfig.getRelativePrefix().contains(face)) {
			logger.warn("Failed to find face " + face);
			throw new NoSuchRequestHandlingMethodException("questions", getClass());
		}
		try {
			final WizardContext wizardContext = getMobileService().createWizardContext(faceConfig, flowType, state, decodedRegion);
			final List<MobilePage> mobilePages = getMobileService().acquireMobileQuestions(wizardContext);
			return mobilePages;
		} catch (final IllegalArgumentException e) {
			logger.warn("Failed to load the questions for face " + face + ", flow " + flowType + ", state " + state
					+ ", voting region " + votingRegion, e);
			throw new NoSuchRequestHandlingMethodException("questions", getClass());
		}
	}

	/**
	 * Provides the results of asking a mobile user the wizard questions to the system.
	 * 
	 * @author IanBrown
	 * @param face
	 *            the face.
	 * @param flowType
	 *            the type of flow.
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param results
	 *            the results.
	 * @since Apr 11, 2012
	 * @version Sep 21, 2012
	 */
	@RequestMapping(value = "/results/{face}/{flowType}/{state}/{votingRegion}", method = RequestMethod.POST)
	public ResponseEntity<byte[]> results(@PathVariable final String face, @PathVariable final FlowType flowType, @PathVariable final String state,
			@PathVariable final String votingRegion, @RequestBody final MobileResults results) {
		final FaceConfig faceConfig = getFacesService().findConfigByPrefix("faces/" + face);
		final String decodedRegion = decodeVotingRegion(votingRegion);
		final WizardContext wizardContext = getMobileService().createWizardContext(faceConfig, flowType, state, decodedRegion);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

		try {
			getMobileService().addResultsToWizardContext(results, wizardContext);

		} catch (final Exception e) {
			results.setDownloaded(false);
			logger.warn("Failed to match results to wizard", e);
			throw new HttpMessageNotReadableException("Unable to match results to wizard", e);
		}

		try {
			final PdfGenerator generator = getPdfGeneratorFactory().createPdfGenerator( wizardContext,byteOutput );
			generator.run();
			generator.dispose();
			wizardContext.getWizardResults().setLastChangedDate(new Date());
			wizardContext.getWizardResults().setDownloaded(true);

		} catch (final Exception e) {
			results.setDownloaded(false);
			logger.warn("Failed to produce PDF", e);
			throw new HttpMessageNotWritableException("Failed to produce PDF", e);
		}

		try {
			wizardContext.processSaveResults(getQuestionnaireService());
		} catch (final RuntimeException e) {
			logger.warn("Failed to process and save results", e);
			throw e;
		}
		
		try {
			getMailingListService().saveToMailingListIfHasSignup(wizardContext.getWizardResults());
		} catch (final RuntimeException e) {
			logger.warn("Failed to signup for mailing list(s)", e);
			throw e;
		}
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/force-download"));
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String attachment = String.format("attachment; filename=\"%s\";", PdfGeneratorUtil.getFileName(wizardContext) );
        headers.set( "Content-Disposition", attachment );
        headers.set("Content-Transfer-Encoding", "binary");
        return new ResponseEntity<byte[]>(byteOutput.toByteArray(), headers, HttpStatus.CREATED );
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
	public void setFacesService(final FacesService facesService) {
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
	public void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

	/**
	 * Sets the mailing list service.
	 * 
	 * @author IanBrown
	 * @param mailingListService
	 *            the mailing list service to set.
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	public void setMailingListService(final MailingListService mailingListService) {
		this.mailingListService = mailingListService;
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
	public void setMobileService(final MobileService mobileService) {
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
	public void setPdfGeneratorFactory(final AdvancedPdfGeneratorFactory pdfGeneratorFactory) {
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
	public void setQuestionnaireService(final QuestionnaireService questionnaireService) {
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
	public void setStateService(final StateService stateService) {
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
	public void setUserService(final OverseasUserService userService) {
		this.userService = userService;
	}

	/**
	 * Gets the states for the specified country.
	 * 
	 * @author IanBrown
	 * @param countryAbbreviation
	 *            the country - only USA will provide states.
	 * @return the states.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@RequestMapping(value = "/country/{countryAbbreviation}/states", method = RequestMethod.GET)
	public @ResponseBody
	List<State> states(@PathVariable final String countryAbbreviation) {
		if (!"USA".equals(countryAbbreviation)) {
			return new ArrayList<State>();
		}

		return (List<State>) getStateService().findAllStates();
	}

	/**
	 * Retrieves the SVID for the specified country and state.
	 * 
	 * @author IanBrown
	 * @param countryAbbreviation
	 *            the abbreviation of the country.
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @return the SVID or <code>null</code>.
	 * @since Jul 6, 2012
	 * @version Jul 16, 2012
	 */
	@RequestMapping(value = "/country/{countryAbbreviation}/state/{stateAbbreviation}/svid", method = RequestMethod.GET)
	public @ResponseBody
	StateSpecificDirectory svid(@PathVariable final String countryAbbreviation, @PathVariable final String stateAbbreviation) {
		if (!"USA".equals(countryAbbreviation)) {
			return null;
		}

		return getMobileService().findSvidForState(stateAbbreviation);
	}

	/**
	 * Updates an existing user with the specified email address (MD5 hashed).
	 * 
	 * @author IanBrown
	 * @param email
	 *            the email address (MD5 hashed).
	 * @param password
	 *            the password.
	 * @param modifiedUser
	 *            the modified user object.
	 * @param response
	 *            the response.
	 * @since Jul 24, 2012
	 * @version Aug 3, 2012
	 */
	@RequestMapping(value = "/users/{email:.*}", method = RequestMethod.PUT, params = "password")
	public void updateUser(@PathVariable final String email,
			@RequestParam(value = "password", required = true) final String password, @RequestBody final OverseasUser modifiedUser,
			final HttpServletResponse response) {
		final OverseasUser user = getUserService().findUserByNameMd5(email);
		if (user == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}

		if (!user.getPassword().equals(password)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}

		if (user.getId() != modifiedUser.getId()) {
			response.setStatus(HttpStatus.CONFLICT.value());
			return;
		}

		user.setAlternateEmail(modifiedUser.getAlternateEmail());
		user.setAlternatePhone(modifiedUser.getAlternatePhone());
		user.setBallotPref(modifiedUser.getBallotPref());
		user.setBirthDate(modifiedUser.getBirthDate());
		user.setBirthMonth(modifiedUser.getBirthMonth());
		user.setBirthYear(modifiedUser.getBirthYear());
		final UserAddress currentAddress = user.getCurrentAddress();
		final UserAddress newCurrentAddress = updateAddress(modifiedUser.getCurrentAddress(), currentAddress);
		if (newCurrentAddress != currentAddress) {
			user.setCurrentAddress(newCurrentAddress);
		}
		user.setEthnicity(modifiedUser.getEthnicity());
		final UserAddress forwardingAddress = user.getForwardingAddress();
		final UserAddress newForwardingAddress = updateAddress(modifiedUser.getForwardingAddress(), forwardingAddress);
		if (newForwardingAddress != forwardingAddress) {
			user.setForwardingAddress(newForwardingAddress);
		}
		user.setGender(modifiedUser.getGender());
		updateName(modifiedUser.getName(), user.getName());
		user.setParty(modifiedUser.getParty());
		user.setPhone(modifiedUser.getPhone());
		final UserAddress previousAddress = user.getPreviousAddress();
		final UserAddress newPreviousAddress = updateAddress(modifiedUser.getPreviousAddress(), previousAddress);
		if (newPreviousAddress != previousAddress) {
			user.setPreviousAddress(newPreviousAddress);
		}
		updateName(modifiedUser.getPreviousName(), user.getPreviousName());
		user.setRace(modifiedUser.getRace());
		user.setVoterHistory(modifiedUser.getVoterHistory());
		user.setVoterType(modifiedUser.getVoterType());
		final UserAddress votingAddress = user.getVotingAddress();
		final UserAddress newVotingAddress = updateAddress(modifiedUser.getVotingAddress(), votingAddress);
		if (newVotingAddress != votingAddress) {
			user.setVotingAddress(newVotingAddress);
		}
		final VotingRegion modifiedVotingRegion = modifiedUser.getVotingRegion();
		if (modifiedVotingRegion != null) {
			final long votingRegionId = modifiedVotingRegion.getId();
			final VotingRegion votingRegion = getStateService().findRegion(votingRegionId);
			user.setVotingRegion(votingRegion);
		}
		getUserService().saveUser(user);
		response.setStatus(HttpStatus.OK.value());
	}

	/**
	 * Gets the voting regions for the country and state.
	 * 
	 * @author IanBrown
	 * @param countryAbbreviation
	 *            the country abbreviation - only USA works.
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @return the list of voting regions.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@RequestMapping(value = "/country/{countryAbbreviation}/state/{stateAbbreviation}/voting_regions", method = RequestMethod.GET)
	public @ResponseBody
	List<VotingRegion> votingRegions(@PathVariable final String countryAbbreviation, @PathVariable final String stateAbbreviation) {
		if (!"USA".equals(countryAbbreviation)) {
			return new ArrayList<VotingRegion>();
		}

		return (List<VotingRegion>) getStateService().findRegionsForState(stateAbbreviation);
	}

	/**
	 * Decodes the voting region as a string (UTF-8) or an ID.
	 * 
	 * @author IanBrown
	 * @param votingRegion
	 *            the voting region.
	 * @return the decoded region.
	 * @since Aug 3, 2012
	 * @version Aug 9, 2012
	 */
	private String decodeVotingRegion(final String votingRegion) {
		String decodedRegion = votingRegion;
		try {
			if (Pattern.matches("(-|\\+)?\\d+$", votingRegion)) {
				final long votingRegionId = Long.parseLong(votingRegion);
				final VotingRegion region = getStateService().findRegion(votingRegionId);
				decodedRegion = region.getName();
			} else {
				decodedRegion = URLDecoder.decode(votingRegion, "UTF-8");
			}
		} catch (final Exception e) {
			logger.warn(e.getMessage());
		}
		logger.debug("decodedRegion: " + decodedRegion);
		return decodedRegion;
	}

	/**
	 * Updates the address.
	 * 
	 * @author IanBrown
	 * @param modified
	 *            the modified address.
	 * @param existing
	 *            the existing address.
	 * @since Jul 24, 2012
	 * @version Aug 3, 2012
	 */
	private UserAddress updateAddress(final UserAddress modified, final UserAddress existing) {
		if (modified == null || modified.isEmptySpace()) {
			if (existing == null || existing.isEmptySpace()) {
				return existing;
			}

			existing.setCity("");
			existing.setCountry("");
			existing.setCounty("");
			existing.setDescription("");
			existing.setState("");
			existing.setStreet1("");
			existing.setStreet2("");
			existing.setZip("");
			existing.setZip4("");
			return existing;
		}

		final UserAddress working = existing == null ? new UserAddress() : existing;
		working.setCity(modified.getCity());
		working.setCountry(modified.getCountry());
		working.setCounty(modified.getCounty());
		working.setDescription(modified.getDescription());
		working.setState(modified.getState());
		working.setStreet1(modified.getStreet1());
		working.setStreet2(modified.getStreet2());
		working.setType(modified.getType());
		working.setZip(modified.getZip());
		working.setZip4(modified.getZip4());
		return working;
	}

	/**
	 * Updates the existing name from the modified one.
	 * 
	 * @author IanBrown
	 * @param modified
	 *            the modified name.
	 * @param existing
	 *            the existing name.
	 * @since Jul 24, 2012
	 * @version Jul 24, 2012
	 */
	private void updateName(final Person modified, final Person existing) {
		if (modified == null || modified.isEmpty()) {
			if (existing == null || existing.isEmpty()) {
				return;
			}

			existing.setFirstName("");
			existing.setLastName("");
			existing.setMiddleName("");
			existing.setSuffix("");
			existing.setTitle("");
			return;
		}

		existing.setFirstName(modified.getFirstName());
		existing.setLastName(modified.getLastName());
		existing.setMiddleName(modified.getMiddleName());
		existing.setSuffix(modified.getSuffix());
		existing.setTitle(modified.getTitle());
	}

}
