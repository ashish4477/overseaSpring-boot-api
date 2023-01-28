/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.*;
import com.bearcode.ovf.actions.questionnaire.AllowedForAddOn;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.mobile.*;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.utils.BlockLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service to support mobile devices - converts between the database format and the mobile JSON.
 * 
 * @author IanBrown
 * 
 * @since Apr 10, 2012
 * @version Sep 27, 2012
 */
@Service
public class MobileService {

	/**
	 * the application context.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * the local official DAO.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Autowired
	private LocalOfficialDAO localOfficialDAO;

	/**
	 * the block logger for the class.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private final static BlockLogger BLOCK_LOGGER = new BlockLogger();
	// new BlockLogger(LoggerFactory.getLogger(MobileService.class), "debug");

	/**
	 * the class logger.
	 * 
	 * @author IanBrown
	 * @since May 8, 2012
	 * @version May 8, 2012
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(MobileService.class);

	/**
	 * turn block logging on?
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private final static boolean BLOCK_LOGGING_ON = false;

	/**
	 * turn trace logging on?
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private final static boolean TRACE_LOGGING_ON = false;

	/**
	 * Logs the mobile address with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @param mobileAddress
	 *            the mobile address.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobileAddress(final String name, final MobileAddress mobileAddress) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			if (mobileAddress == null) {
				BLOCK_LOGGER.indentMessage("%s=null", name);
				return;
			}

			BLOCK_LOGGER.startBlock("%s={", name);
			BLOCK_LOGGER.indentMessage("type=%s", mobileAddress.getTypeName());
			BLOCK_LOGGER.indentMessage("street1=%s", mobileAddress.getStreet1());
			BLOCK_LOGGER.indentMessage("street2=%s", mobileAddress.getStreet2());
			BLOCK_LOGGER.indentMessage("description=%s", mobileAddress.getDescription());
			BLOCK_LOGGER.indentMessage("city=%s", mobileAddress.getCity());
			BLOCK_LOGGER.indentMessage("state=%s", mobileAddress.getState());
			BLOCK_LOGGER.indentMessage("zip=%s", mobileAddress.getZip());
			BLOCK_LOGGER.indentMessage("zip4=%s", mobileAddress.getZip4());
			BLOCK_LOGGER.indentMessage("country=%s", mobileAddress.getCountry());
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile answer.
	 * 
	 * @author IanBrown
	 * @param mobileAnswer
	 *            the mobile answer.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobileAnswer(final MobileAnswer mobileAnswer) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.startBlock("answer={");
			BLOCK_LOGGER.indentMessage("questionFieldId=%d", mobileAnswer.getQuestionFieldId());
			logMobileOption(mobileAnswer.getOption(), "option");
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile answers.
	 * 
	 * @author IanBrown
	 * @param mobileAnswers
	 *            the list of mobile answers.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobileAnswers(final List<MobileAnswer> mobileAnswers) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			if (mobileAnswers == null) {
				BLOCK_LOGGER.indentMessage("answers=null");
				return;
			}

			BLOCK_LOGGER.startBlock("answers=[");
			for (final MobileAnswer mobileAnswer : mobileAnswers) {
				logMobileAnswer(mobileAnswer);
			}
			BLOCK_LOGGER.endBlock("]");
		}
	}

	/**
	 * Logs the mobile dependencies.
	 * 
	 * @author IanBrown
	 * @param mobileDependencies
	 *            the mobile dependencies.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private static void logMobileDependencies(final Collection<MobileDependency> mobileDependencies) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			if (mobileDependencies == null) {
				BLOCK_LOGGER.indentMessage("dependencies=null");
				return;
			}

			BLOCK_LOGGER.startBlock("dependencies=[");
			for (final MobileDependency mobileDependency : mobileDependencies) {
				logMobileDependency(mobileDependency);
			}
			BLOCK_LOGGER.endBlock("]");
		}
	}

	/**
	 * Logs the mobile dependency.
	 * 
	 * @author IanBrown
	 * @param mobileDependency
	 *            the mobile dependency.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private static void logMobileDependency(final MobileDependency mobileDependency) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.startBlock("dependency={");
			BLOCK_LOGGER.indentMessage("dependsOn=%d", mobileDependency.getDependsOn());
			BLOCK_LOGGER.indentMessage("condition=%d", mobileDependency.getCondition());
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile group.
	 * 
	 * @author IanBrown
	 * @param mobileGroup
	 *            the mobile group to log.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobileGroup(final MobileGroup mobileGroup) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.startBlock("group={");
			logMobileList(mobileGroup, "variants");
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile level.
	 * 
	 * @author IanBrown
	 * @param mobileLevel
	 *            the mobile level.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private static void logMobileLevel(final MobileLevel mobileLevel) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.indentMessage("title=%s", mobileLevel.getTitle());
		}
	}

	/**
	 * Logs the mobile list of children.
	 * 
	 * @author IanBrown
	 * @param mobileList
	 *            the mobile list.
	 * @param nameOfChildren
	 *            the name of the children contained in the list.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private static <L extends MobileLevel> void logMobileList(final MobileList<L> mobileList, final String nameOfChildren) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			logMobileLevel(mobileList);
			if (mobileList.getChildren() == null) {
				BLOCK_LOGGER.indentMessage("%s=null", nameOfChildren);
				return;
			}

			BLOCK_LOGGER.startBlock("%s=[", nameOfChildren);
			for (final L child : mobileList.getChildren()) {
				if (child instanceof MobilePage) {
					logMobilePage((MobilePage) child);
				} else if (child instanceof MobileGroup) {
					logMobileGroup((MobileGroup) child);
				} else if (child instanceof MobileVariant) {
					logMobileVariant((MobileVariant) child);
				} else if (child instanceof MobileQuestion) {
					logMobileQuestion((MobileQuestion) child);
				}
			}
			BLOCK_LOGGER.endBlock("]");
		}
	}

	/**
	 * Logs the mobile options.
	 * 
	 * @author IanBrown
	 * @param mobileOption
	 *            the mobile option.
	 * @param nameOfOption
	 *            the name of the option.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobileOption(final MobileOption mobileOption, final String nameOfOption) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.indentMessage("%s=%d %s", nameOfOption, mobileOption.getId(), mobileOption.getValue());
		}
	}

	/**
	 * Logs the mobile options.
	 * 
	 * @author IanBrown
	 * @param mobileOptions
	 *            the mobile options.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobileOptions(final List<MobileOption> mobileOptions) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			if (mobileOptions == null) {
				BLOCK_LOGGER.indentMessage("options=null");
				return;
			}

			BLOCK_LOGGER.startBlock("options=[");
			for (final MobileOption mobileOption : mobileOptions) {
				logMobileOption(mobileOption, "option");
			}
			BLOCK_LOGGER.endBlock("]");
		}
	}

	/**
	 * Logs the mobile page.
	 * 
	 * @author IanBrown
	 * @param mobilePage
	 *            the mobile page.
	 * @since Apr 30, 2012
	 * @version May 3, 2012
	 */
	private static void logMobilePage(final MobilePage mobilePage) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.startBlock("page={");
			logMobileList(mobilePage, "groups");
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile pages.
	 * 
	 * @author IanBrown
	 * @param mobilePages
	 *            the mobile pages.
	 * @since Apr 30, 2012
	 * @version May 3, 2012
	 */
	private static void logMobilePages(final List<MobilePage> mobilePages) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			if (mobilePages == null) {
				BLOCK_LOGGER.indentMessage("pages=null");
				return;
			}
			BLOCK_LOGGER.startBlock("pages=[");
			for (final MobilePage mobilePage : mobilePages) {
				logMobilePage(mobilePage);
			}
			BLOCK_LOGGER.endBlock("]");
		}
	}

	/**
	 * Logs the mobile person with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the person.
	 * @param mobilePerson
	 *            the person.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobilePerson(final String name, final MobilePerson mobilePerson) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			if (mobilePerson == null) {
				BLOCK_LOGGER.indentMessage("%s=null", name);
				return;
			}

			BLOCK_LOGGER.startBlock("%s={", name);
			BLOCK_LOGGER.indentMessage("title=%s", mobilePerson.getTitle());
			BLOCK_LOGGER.indentMessage("firstName=%s", mobilePerson.getFirstName());
			BLOCK_LOGGER.indentMessage("initial=%s", mobilePerson.getInitial());
			BLOCK_LOGGER.indentMessage("lastName=%s", mobilePerson.getLastName());
			BLOCK_LOGGER.indentMessage("suffix=%s", mobilePerson.getSuffix());
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile question.
	 * 
	 * @author IanBrown
	 * @param mobileQuestion
	 *            the mobile question.
	 * @since Apr 13, 2012
	 * @version May 3, 2012
	 */
	private static void logMobileQuestion(final MobileQuestion mobileQuestion) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.startBlock("question={");
			BLOCK_LOGGER.indentMessage("questionFieldId=%d", mobileQuestion.getQuestionFieldId());
			logMobileLevel(mobileQuestion);
			BLOCK_LOGGER.indentMessage("title=%s", mobileQuestion.getTitle());
			BLOCK_LOGGER.indentMessage("fieldType=%s", mobileQuestion.getFieldType());
			BLOCK_LOGGER.indentMessage("verificationPattern=%s", mobileQuestion.getVerificationPattern());
			logMobileOptions(mobileQuestion.getOptions());
			BLOCK_LOGGER.indentMessage("helpText=%s", mobileQuestion.getHelpText());
			BLOCK_LOGGER.indentMessage("additionalHelp=%s", mobileQuestion.getAdditionalHelp());
			BLOCK_LOGGER.indentMessage("required=%s", mobileQuestion.isRequired() ? "true" : "false");
			BLOCK_LOGGER.indentMessage("security=%s", mobileQuestion.isSecurity() ? "true" : "false");
			BLOCK_LOGGER.indentMessage("encoded=%s", mobileQuestion.isEncoded() ? "true" : "false");
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile results.
	 * 
	 * @author IanBrown
	 * @param mobileResults
	 *            the mobile results.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private static void logMobileResults(final MobileResults mobileResults) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.startBlock("results={");
			BLOCK_LOGGER.indentMessage("emailAddress=%s", mobileResults.getEmailAddress());
			logMobilePerson("name", mobileResults.getName());
			logMobilePerson("previousName", mobileResults.getPreviousName());
			BLOCK_LOGGER.indentMessage("birthMonth=%d", mobileResults.getBirthMonth());
			BLOCK_LOGGER.indentMessage("birthDay=%d", mobileResults.getBirthDay());
			BLOCK_LOGGER.indentMessage("birthYear=%d", mobileResults.getBirthYear());
			BLOCK_LOGGER.indentMessage("alternateEmail=%s", mobileResults.getAlternateEmail());
			BLOCK_LOGGER.indentMessage("phone=%s", mobileResults.getPhone());
			BLOCK_LOGGER.indentMessage("alternatePhone=%s", mobileResults.getAlternatePhone());
			logMobileAddress("currentAddress", mobileResults.getCurrentAddress());
			logMobileAddress("votingAddress", mobileResults.getVotingAddress());
			logMobileAddress("forwardingAddress", mobileResults.getForwardingAddress());
			logMobileAddress("previousAddress", mobileResults.getPreviousAddress());
			BLOCK_LOGGER.indentMessage("votingRegionName=%s", mobileResults.getVotingRegionName());
			BLOCK_LOGGER.indentMessage("votingRegionState=%s", mobileResults.getVotingRegionState());
			BLOCK_LOGGER.indentMessage("voterType=%s", mobileResults.getVoterType());
			BLOCK_LOGGER.indentMessage("voterHistory=%s", mobileResults.getVoterHistory());
			BLOCK_LOGGER.indentMessage("ballotPreference=%s", mobileResults.getBallotPreference());
			BLOCK_LOGGER.indentMessage("ethnicity=%s", mobileResults.getEthnicity());
			BLOCK_LOGGER.indentMessage("race=%s", mobileResults.getRace());
			BLOCK_LOGGER.indentMessage("gender=%s", mobileResults.getGender());
			BLOCK_LOGGER.indentMessage("party=%s", mobileResults.getParty());
			logMobileAnswers(mobileResults.getAnswers());
			BLOCK_LOGGER.indentMessage("downloaded=%s", mobileResults.isDownloaded() ? "true" : "false");
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * Logs the mobile variant.
	 * 
	 * @author IanBrown
	 * @param mobileVariant
	 *            the mobile variant.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private static void logMobileVariant(final MobileVariant mobileVariant) {
		if (BLOCK_LOGGER.isLoggingEnabled()) {
			BLOCK_LOGGER.startBlock("variant={");
			logMobileList(mobileVariant, "questions");
			logMobileDependencies(mobileVariant.getDependencies());
			BLOCK_LOGGER.endBlock("}");
		}
	}

	/**
	 * the DAO used to get question fields.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	@Autowired
	private QuestionFieldDAO questionFieldDAO;

	/**
	 * the DAO used to get the questionnaire pages.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	@Autowired
	private QuestionnairePageDAO pageDAO;

	/**
	 * the DAO used to get the state.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	@Autowired
	private StateDAO stateDAO;

	/**
	 * the DAO used to get the voting region.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 16, 2012
	 */
	@Autowired
	private VotingRegionDAO votingRegionDAO;

	/**
	 * Acquires the mobile questions for the specified context from the questionnaire pages.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @return the mobile groups containing the mobile questions.
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	public List<MobilePage> acquireMobileQuestions(final WizardContext wizardContext) {
		BLOCK_LOGGER.setLoggingOn(BLOCK_LOGGING_ON);
		// trace("Acquiring mobile questions\n");

		final List<QuestionnairePage> questionnairePages = getPageDAO().findPages(wizardContext.getFlowType().getPageType());
		final Set<Question> workedQuestions = new HashSet<Question>();
		final Map<QuestionField, MobileQuestion> mappedFields = new HashMap<QuestionField, MobileQuestion>();
		final List<MobilePage> mobilePages = new LinkedList<MobilePage>();
		for (final QuestionnairePage questionnairePage : questionnairePages) {
			wizardContext.setCurrentPage(questionnairePage);
			final MobilePage mobilePage = convertQuestionnairePageToMobileQuestions(wizardContext, questionnairePage,
					workedQuestions, mappedFields);
			if (mobilePage != null) {
				mobilePages.add(mobilePage);
			}
		}

		// trace("Mobile questions acquired\n\n");
		logMobilePages(mobilePages);
		return mobilePages;
	}

	/**
	 * Adds the results received from a mobile user to the wizard context.
	 * 
	 * @author IanBrown
	 * @param results
	 *            the results.
	 * @param wizardContext
	 *            the wizard context.
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	public void addResultsToWizardContext(final MobileResults results, final WizardContext wizardContext) {
		BLOCK_LOGGER.setLoggingOn(BLOCK_LOGGING_ON);
		logMobileResults(results);
		final WizardResults wizardResults = wizardContext.getWizardResults();
		wizardResults.setUsername(convertString(wizardResults.getUsername(), results.getEmailAddress()));
		wizardResults.setName(convertPerson(wizardResults.getName(), results.getName()));
		wizardResults.setPreviousName(convertPerson(wizardResults.getPreviousName(), results.getPreviousName()));
		wizardResults.setBirthMonth(results.getBirthMonth());
		wizardResults.setBirthDate(results.getBirthDay());
		wizardResults.setBirthYear(results.getBirthYear());
		wizardResults.setAlternateEmail(convertString(wizardResults.getAlternateEmail(), results.getAlternateEmail()));
		wizardResults.setPhone(convertString(wizardResults.getPhone(), results.getPhone()));
		wizardResults.setAlternatePhone(convertString(wizardResults.getAlternatePhone(), results.getAlternatePhone()));
		wizardResults.setCurrentAddress(convertAddress(wizardResults.getCurrentAddress(), results.getCurrentAddress()));
		wizardResults.setVotingAddress(convertAddress(wizardResults.getVotingAddress(), results.getVotingAddress()));
		wizardResults.setForwardingAddress(convertAddress(wizardResults.getForwardingAddress(), results.getForwardingAddress()));
		wizardResults.setPreviousAddress(convertAddress(wizardResults.getPreviousAddress(), results.getPreviousAddress()));
		wizardResults.setVotingRegionName(convertString(wizardResults.getVotingRegionName(), results.getVotingRegionName()));
		wizardResults.setVotingRegionState(convertString(wizardResults.getVotingRegionState(), results.getVotingRegionState()));
		addVotingRegionToWizardResults(wizardResults);
		wizardResults.setVoterType(convertString(wizardResults.getVoterType(), results.getVoterType()));
		wizardResults.setVoterHistory(convertString(wizardResults.getVoterHistory(), results.getVoterHistory()));
		wizardResults.setBallotPref(convertString(wizardResults.getBallotPref(), results.getBallotPreference()));
		wizardResults.setEthnicity(convertString(wizardResults.getEthnicity(), results.getEthnicity()));
		wizardResults.setRace(convertString(wizardResults.getRace(), results.getRace()));
		wizardResults.setGender(convertString(wizardResults.getGender(), results.getGender()));
		wizardResults.setParty(convertString(wizardResults.getParty(), results.getParty()));
		wizardResults.setMobile(true);
		if (results.getMobileDeviceType() == null || results.getMobileDeviceType().trim().isEmpty()) {
			if (wizardResults.getMobileDeviceType().trim().isEmpty()) {
				wizardResults.setMobileDeviceType("Mobile Service");
			}
		} else {
			wizardResults.setMobileDeviceType(results.getMobileDeviceType());
		}
		addAnswersToWizardResults(results.getAnswers(), wizardResults);
		wizardResults.setDownloaded(results.isDownloaded());
	}

	/**
	 * Creates a wizard context for the specified face configuration, flow type, and state.
	 * 
	 * @author IanBrown
	 * @param faceConfig
	 *            the face configuration.
	 * @param flowType
	 *            the flow type.
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the name of the voting region.
	 * @return the wizard context.
	 * @since Apr 10, 2012
	 * @version Jun 22, 2012
	 */
	public WizardContext createWizardContext(final FaceConfig faceConfig, final FlowType flowType, final String state,
			final String votingRegion) {
		final WizardResults wizardResults = new WizardResults(flowType);
		wizardResults.setVotingRegionName(votingRegion);
		wizardResults.setVotingRegionState(state);
		final VotingRegion region = new VotingRegion();
		region.setName(votingRegion);
		final State byAbbreviation = getStateDAO().getByAbbreviation(state);
		if (byAbbreviation == null) {
			throw new IllegalArgumentException("No state with the abbreviation " + state + " can be found");
		}
		region.setState(byAbbreviation);
		final VotingRegion regionByName = getVotingRegionDAO().getRegionByName(region);
		if (regionByName == null) {
			throw new IllegalArgumentException("No region with the name " + votingRegion + " can be found");
		}
		wizardResults.setVotingRegion(regionByName);
		final WizardResultAddress votingAddress = new WizardResultAddress();
		votingAddress.setState(state);
		wizardResults.setVotingAddress(votingAddress);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		wizardContext.setCurrentFace(faceConfig);
		return wizardContext;
	}

	/**
	 * Finds the local official for the specified voting region.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the local official or <code>null</code>.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	public LocalOfficial findLocalOfficialForRegion(final String stateAbbreviation, final String votingRegionName) {
		final State state = getStateDAO().getByAbbreviation(stateAbbreviation);
		if (state == null) {
			return null;
		}

		final VotingRegion desiredRegion = new VotingRegion();
		desiredRegion.setState(state);
		desiredRegion.setName(votingRegionName);
		final VotingRegion votingRegion = getVotingRegionDAO().getRegionByName(desiredRegion);
		if (votingRegion == null) {
			return null;
		}

		return getLocalOfficialDAO().findLeoByRegion(votingRegion);
	}

	/**
	 * Finds the state specific directory (SVID) for the state by abbreviation.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @return the SVID or <code>null</code>.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	public StateSpecificDirectory findSvidForState(final String stateAbbreviation) {
		final State state = getStateDAO().getByAbbreviation(stateAbbreviation);
		if (state == null) {
			return null;
		}

		return getLocalOfficialDAO().findSvidForState(state);
	}

	/**
	 * Gets the application context.
	 * 
	 * @author IanBrown
	 * @return the application context.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * Gets the local official DAO.
	 * 
	 * @author IanBrown
	 * @return the local official DAO.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	public LocalOfficialDAO getLocalOfficialDAO() {
		return localOfficialDAO;
	}

	/**
	 * Gets the page DAO.
	 * 
	 * @author IanBrown
	 * @return the page DAO.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	public QuestionnairePageDAO getPageDAO() {
		return pageDAO;
	}

	/**
	 * Gets the question field DAO.
	 * 
	 * @author IanBrown
	 * @return the question field DAO.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public QuestionFieldDAO getQuestionFieldDAO() {
		return questionFieldDAO;
	}

	/**
	 * Gets the state DAO.
	 * 
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Gets the voting region DAO.
	 * 
	 * @author IanBrown
	 * @return the voting region DAO.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public VotingRegionDAO getVotingRegionDAO() {
		return votingRegionDAO;
	}

	/**
	 * Sets the application context.
	 * 
	 * @author IanBrown
	 * @param applicationContext
	 *            the application context to set.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	public void setApplicationContext(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Sets the local official DAO.
	 * 
	 * @author IanBrown
	 * @param localOfficialDAO
	 *            the local official DAO to set.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	public void setLocalOfficialDAO(final LocalOfficialDAO localOfficialDAO) {
		this.localOfficialDAO = localOfficialDAO;
	}

	/**
	 * Sets the page DAO.
	 * 
	 * @author IanBrown
	 * @param pageDAO
	 *            the page DAO to set.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	public void setPageDAO(final QuestionnairePageDAO pageDAO) {
		this.pageDAO = pageDAO;
	}

	/**
	 * Sets the question field DAO.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the question field DAO to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setQuestionFieldDAO(final QuestionFieldDAO questionFieldDAO) {
		this.questionFieldDAO = questionFieldDAO;
	}

	/**
	 * Sets the state DAO.
	 * 
	 * @author IanBrown
	 * @param stateDAO
	 *            the stateDAO to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setStateDAO(final StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

	/**
	 * Sets the voting region DAO.
	 * 
	 * @author IanBrown
	 * @param votingRegionDAO
	 *            the voting region DAO to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setVotingRegionDAO(final VotingRegionDAO votingRegionDAO) {
		this.votingRegionDAO = votingRegionDAO;
	}

	/**
	 * Adds the answers to the wizard results.
	 * 
	 * @author IanBrown
	 * @param answers
	 *            the answers from the mobile device.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void addAnswersToWizardResults(final List<MobileAnswer> answers, final WizardResults wizardResults) {
		if (answers != null && !answers.isEmpty()) {
			for (final MobileAnswer answer : answers) {
				addAnswerToWizardResults(answer, wizardResults);
			}
		}
	}

	/**
	 * Adds the answer to the wizard results.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Apr 12, 2012
	 * @version Jul 11, 2012
	 */
	private void addAnswerToWizardResults(final MobileAnswer answer, final WizardResults wizardResults) {
		final QuestionField questionField = answer.getQuestionField();
		final QuestionField field = questionField == null ? getQuestionFieldDAO().getById(answer.getQuestionFieldId())
				: questionField;
		if (field == null) {
			throw new IllegalStateException("The field for " + answer + " cannot be found");
		}
		final FieldType type = field.getType();
		final String templateName = type.getTemplateName();

		if (FieldType.TEMPLATE_TEXT.equals(templateName) || FieldType.TEMPLATE_TEXTAREA.equals(templateName)
				|| FieldType.TEMPLATE_CHECKBOX.equals(templateName) || FieldType.TEMPLATE_CHECKBOX_FILLED.equals(templateName)
				|| FieldType.TEMPLATE_TEXT_CONFIRM.equals(templateName)) {
			addEnteredAnswerToWizardResults(answer, field, wizardResults);

		} else if (FieldType.TEMPLATE_DATE.equals(templateName)) {
			addEnteredDateAnswerToWizardResults(answer, field, wizardResults);

		} else if (FieldType.TEMPLATE_SELECT.equals(templateName) || FieldType.TEMPLATE_RADIO.equals(templateName)) {
			addPredefinedAnswerToWizardResults(answer, field, wizardResults);

		} else {
			throw new UnsupportedOperationException("Handling for " + templateName + " is not implemented yet");
		}
	}

	/**
	 * Adds an entered answer to the wizard results.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer from the mobile device.
	 * @param field
	 *            the question field answered.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void addEnteredAnswerToWizardResults(final MobileAnswer answer, final QuestionField field,
			final WizardResults wizardResults) {
		final EnteredAnswer enteredAnswer = new EnteredAnswer();
		enteredAnswer.setField(field);
		enteredAnswer.setEnteredValue(answer.getOption().getValue());
		enteredAnswer.setWizardResults(wizardResults);
		wizardResults.getAnswersAsMap().put(answer.getQuestionFieldId(), enteredAnswer);
	}

	/**
	 * Adds an entered date answer to the wizard results.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer from the mobile device.
	 * @param field
	 *            the question field answered.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void addEnteredDateAnswerToWizardResults(final MobileAnswer answer, final QuestionField field,
			final WizardResults wizardResults) {
		final EnteredDateAnswer enteredDateAnswer = new EnteredDateAnswer();
		enteredDateAnswer.setField(field);
		enteredDateAnswer.setValue(answer.getOption().getValue());
		enteredDateAnswer.setWizardResults(wizardResults);
		wizardResults.getAnswersAsMap().put(answer.getQuestionFieldId(), enteredDateAnswer);
	}

	/**
	 * Adds options for the input options to the mobile question.
	 * 
	 * @author IanBrown
	 * @param options
	 *            the options.
	 * @param mobileQuestion
	 *            the mobile question.
	 * @since Apr 10, 2012
	 * @version Jul 11, 2012
	 */
	private void addOptionsToMobileQuestion(final Collection<FieldDictionaryItem> options, final MobileQuestion mobileQuestion) {
		if (options != null && !options.isEmpty()) {
			final List<MobileOption> mobileOptions = new LinkedList<MobileOption>();
			for (final FieldDictionaryItem option : options) {
				final MobileOption mobileOption = new MobileOption();
				mobileOption.setId(option.getId());
				mobileOption.setItem(option);
				mobileOption.setValue(option.getViewValue());
				mobileOptions.add(mobileOption);
			}
			mobileQuestion.setOptions(mobileOptions);
		}
	}

	/**
	 * Adds a predefined entered answer to the wizard results.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer from the mobile device.
	 * @param field
	 *            the question field answered.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Apr 12, 2012
	 * @version Jul 12, 2012
	 */
	private void addPredefinedAnswerToWizardResults(final MobileAnswer answer, final QuestionField field,
			final WizardResults wizardResults) {
		final MobileOption answerOption = answer.getOption();
		final Long optionId = answerOption.getId();
		final FieldDictionaryItem item = answerOption.getItem();
		if (item == null && (optionId == null || optionId == 0l)) {
			return;
		}

		final PredefinedAnswer predefinedAnswer = new PredefinedAnswer();
		predefinedAnswer.setField(field);
		FieldDictionaryItem option = item;
		if (option == null) {
			for (final FieldDictionaryItem fieldOption : field.getOptions()) {
				if (fieldOption.getId() == optionId) {
					option = fieldOption;
					break;
				}
			}
		}
		if (option == null) {
			throw new IllegalStateException("No field dictionary item can be found for " + answer);
		}
		predefinedAnswer.setSelectedValue(option);
		predefinedAnswer.setWizardResults(wizardResults);
		wizardResults.getAnswersAsMap().put(answer.getQuestionFieldId(), predefinedAnswer);
	}

	/**
	 * Adds the voting region for the voting region state and voting region name of the wizard results.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @since Apr 12, 2012
	 * @version Apr 16, 2012
	 */
	private void addVotingRegionToWizardResults(final WizardResults wizardResults) {
		final String votingRegionState = wizardResults.getVotingRegionState();
		final State state;
		if (votingRegionState.length() == 2) {
			state = getStateDAO().getByAbbreviation(votingRegionState);
		} else {
			state = getStateDAO().getByName(votingRegionState);
		}
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setState(state);
		votingRegion.setName(wizardResults.getVotingRegionName());
		wizardResults.setVotingRegion(getVotingRegionDAO().getRegionByName(votingRegion));
	}

	/**
	 * Builds a mobile question from the input field.
	 * 
	 * @author IanBrown
	 * @param field
	 *            the field.
	 * @return the mobile question.
	 * @since Apr 10, 2012
	 * @version Sep 27, 2012
	 */
	private MobileQuestion buildMobileQuestion(final QuestionField field) {
		final MobileQuestion mobileQuestion = new MobileQuestion(field.getTitle());
		mobileQuestion.setQuestionField(field);
		mobileQuestion.setQuestionFieldId(field.getId());
		mobileQuestion.setFieldType(field.getType().getTemplateName());
		mobileQuestion.setDataRole(field.getDataRole());
		mobileQuestion.setVerificationPattern(field.getVerificationPattern());
		addOptionsToMobileQuestion(field.getOptions(), mobileQuestion);
		mobileQuestion.setHelpText(field.getHelpText());
		mobileQuestion.setAdditionalHelp(field.getAdditionalHelp());
		mobileQuestion.setRequired(field.isRequired());
		mobileQuestion.setSecurity(field.isSecurity());
		mobileQuestion.setEncoded(field.isEncoded());
		return mobileQuestion;
	}

	/**
	 * Checks the field dependencies against the context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param fieldDependencies
	 *            the field dependencies.
	 * @return <code>true</code> if the dependencies are matched, <code>false</code> otherwise.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private boolean checkFieldDependencies(final WizardContext wizardContext, final Collection<FieldDependency> fieldDependencies) {
		if (fieldDependencies != null && !fieldDependencies.isEmpty()) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Not implemented yet\n" + fieldDependencies);
		}

		return true;
	}

	/**
	 * Determines if the variant has any dependencies.
	 * 
	 * @author IanBrown
	 * @param variant
	 *            the variant.
	 * @return <code>true</code> if the variant has dependencies, <code>false</code> otherwise.
	 * @since May 8, 2012
	 * @version May 8, 2012
	 */
	private boolean checkMobileDependencies(final MobileVariant variant) {
		final List<MobileDependency> dependencies = variant.getDependencies();
		return dependencies != null && !dependencies.isEmpty();
	}

	/**
	 * Determines if the keys for the variant match the context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param question
	 *            the question.
	 * @param variant
	 *            the variant.
	 * @param workedQuestions
	 *            the questions that have been worked on prior to the current one.
	 * @param mappedFields
	 *            the fields that have been mapped to mobile questions.
	 * @return the list of mobile dependencies (may be empty) if the variant is to be used, <code>null</code> if the variant is to
	 *         be omitted.
	 * @since Apr 10, 2012
	 * @version Aug 15, 2012
	 */
	private List<MobileDependency> checkVariantDependencies(final WizardContext wizardContext, final Question question,
			final QuestionVariant variant, final Set<Question> workedQuestions,
			final Map<QuestionField, MobileQuestion> mappedFields) {
		boolean success = true;
		final Collection<BasicDependency> keys = variant.getKeys();
		final List<MobileDependency> mobileDependencies = new LinkedList<MobileDependency>();

		if (keys != null && !keys.isEmpty()) {
			boolean matchedQuestion = false;
			DependentRoot grouping = null;
			for (final BasicDependency key : keys) {
				if (!key.checkGroup(grouping)) {
					grouping = key.getDependsOn();
					if (!success) {
						return null;
					}
					success = true;

				} else if (key instanceof QuestionDependency || key instanceof QuestionCheckboxDependency) {
					if (grouping != null && !matchedQuestion) {
						continue;
					}

				} else if (success) {
					continue;
				}

				// trace("     %s", key.getClass().getSimpleName());
				if (key instanceof FaceDependency || key instanceof FlowDependency || key instanceof UserFieldDependency) {
					matchedQuestion = false;
					success = key.checkDependency(wizardContext);
					// if (key instanceof FaceDependency) {
					// final FaceDependency faceDependency = (FaceDependency) key;
					// trace(" %s / %s", faceDependency.getDependsOn().getName(), faceDependency.getDependsOn().getUrlPath());
					// } else if (key instanceof FlowDependency) {
					// final FlowDependency flowDependency = (FlowDependency) key;
					// trace(" %s", flowDependency.getFlowType().toString());
					// } else if (key instanceof UserFieldDependency) {
					// final UserFieldDependency userFieldDependency = (UserFieldDependency) key;
					// trace(" %s=%s", userFieldDependency.getFieldName(), userFieldDependency.getFieldValue().toString());
					// }
					// trace(" = %s\n", Boolean.toString(success));

				} else if (key instanceof QuestionDependency) {
					matchedQuestion = false;
					final QuestionDependency questionDependency = (QuestionDependency) key;
					final Question dependsOn = questionDependency.getDependsOn();
					// trace(" %s / %s", question.getName(), question.getTitle());
					if (!workedQuestions.contains(dependsOn)) {
						// trace(" = false\n");
						success = false;
						continue;
					}

					final QuestionField keyField = dependsOn.getKeyField();
					final MobileQuestion parentQuestion = mappedFields.get(keyField);
					if (parentQuestion == null) {
						// trace(" = false\n");
						success = false;
						continue;
					}
					success = true;
					matchedQuestion = true;
					final FieldDictionaryItem condition = questionDependency.getCondition();
					// trace(" = %s\n", condition.toString());
					final Long mobileCondition = convertConditionToMobileCondition(parentQuestion, condition);
					final MobileDependency mobileDependency = new MobileDependency(parentQuestion.getQuestionFieldId(),
							mobileCondition);
					mobileDependencies.add(mobileDependency);
					
				} else if (key instanceof QuestionCheckboxDependency) {
					matchedQuestion = false;
					final QuestionCheckboxDependency questionCheckboxDependency = (QuestionCheckboxDependency) key;
					final Question dependsOn = questionCheckboxDependency.getDependsOn();
					// trace(" %s / %s", question.getName(), question.getTitle());
					if (!workedQuestions.contains(dependsOn)) {
						// trace(" = false\n");
						success = false;
						continue;
					}

					final QuestionField keyField = dependsOn.getKeyField();
					final MobileQuestion parentQuestion = mappedFields.get(keyField);
					if (parentQuestion == null) {
						// trace(" = false\n");
						success = false;
						continue;
					}
					success = true;
					matchedQuestion = true;
					// TODO need to deal with this here.
					LOGGER.warn("Question checkbox dependency cannot be handled at this time, skipped. This may cause issues with the flow.");

				} else {
					throw new UnsupportedOperationException("Not yet implemented " + key);
				}
			}
		}

		if (success) {
			return mobileDependencies;
		}

		return null;
	}

	/**
	 * Converts the input address from the mobile to database form.
	 * 
	 * @author IanBrown
	 * @param existingAddress
	 *            the existing address.
	 * @param address
	 *            the address.
	 * @return the converted address or the existing address is the input address is <code>null</code>.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private WizardResultAddress convertAddress(final WizardResultAddress existingAddress, final MobileAddress address) {
		return address == null ? existingAddress : WizardResultAddress.create(address);
	}

	/**
	 * Converts the condition to a mobile condition.
	 * 
	 * @author IanBrown
	 * @param parentQuestion
	 *            the parent mobile question from which the options can be retrieved.
	 * @param condition
	 *            the condition.
	 * @return the mobile condition (option ID) corresponding to the condition.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private Long convertConditionToMobileCondition(final MobileQuestion parentQuestion, final FieldDictionaryItem condition) {
		for (final MobileOption option : parentQuestion.getOptions()) {
			if (option.getId() == condition.getId()) {
				return option.getId();
			}
		}

		throw new IllegalStateException(condition.getViewValue() + " does not correspond to a valid response for "
				+ parentQuestion.getTitle());
	}

	/**
	 * Converts the field to a mobile question based on the context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the context.
	 * @param field
	 *            the field.
	 * @param mappedFields
	 *            the fields that have been mapped.
	 * @return the mobile question.
	 * @since Apr 10, 2012
	 * @version May 3, 2012
	 */
	private MobileQuestion convertFieldToMobileQuestion(final WizardContext wizardContext, final QuestionField field,
			final Map<QuestionField, MobileQuestion> mappedFields) {
		// trace("      Field: %s -> %s\n", field.getTitle(), field.getInPdfName());
		if (checkFieldDependencies(wizardContext, field.getFieldDependencies())) {
			final MobileQuestion mobileQuestion = buildMobileQuestion(field);
			mappedFields.put(field, mobileQuestion);
			return mobileQuestion;
		}

		return null;
	}

	/**
	 * Converts the input mobile person to a wizard results person.
	 * 
	 * @author IanBrown
	 * @param existingPerson
	 *            the existing person.
	 * @param person
	 *            the person to convert - may be <code>null</code>.
	 * @return the converted person - will be the existing person if the input is <code>null</code>.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private WizardResultPerson convertPerson(final WizardResultPerson existingPerson, final MobilePerson person) {
		if (person == null) {
			return existingPerson;
		}

		final WizardResultPerson convertedPerson = new WizardResultPerson();
		convertedPerson.updateFrom(person);
		return convertedPerson;
	}

	/**
	 * Converts the specified questionnaire page to mobile questions based on the context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param questionnairePage
	 *            the questionnaire page.
	 * @param workedQuestions
	 *            the questions that have been worked.
	 * @param mappedFields
	 *            the mapped fields.
	 * @return the mobile page.
	 * @since Apr 10, 2012
	 * @version May 3, 2012
	 */
	private MobilePage convertQuestionnairePageToMobileQuestions(final WizardContext wizardContext,
			final QuestionnairePage questionnairePage, final Set<Question> workedQuestions,
			final Map<QuestionField, MobileQuestion> mappedFields) {
		if (questionnairePage instanceof AddOnPage) {
			final AllowedForAddOn allowedForAddOn = getApplicationContext().getBean(((AddOnPage) questionnairePage).getBeanName(),
					AllowedForAddOn.class);
			allowedForAddOn.prepareAddOnPage(wizardContext, questionnairePage);
		}

		final List<MobileGroup> mobileGroups = new LinkedList<MobileGroup>();
		// trace("Page: %s\n", questionnairePage.getTitle());
		final List<Question> questions = questionnairePage.getQuestions();
		if (questions != null) {
			for (final Question question : questions) {
				final MobileGroup mobileGroup = convertQuestionToMobileQuestions(wizardContext, question, workedQuestions,
						mappedFields);
				if (mobileGroup != null) {
					mobileGroups.add(mobileGroup);
				}
				workedQuestions.add(question);
			}
		}

		if (!mobileGroups.isEmpty()) {
			final MobilePage mobilePage = new MobilePage(questionnairePage.getTitle());
			mobilePage.setChildren(mobileGroups);
			return mobilePage;
		}

		return null;
	}

	/**
	 * Converts the specified question to mobile questions based on the context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param question
	 *            the question.
	 * @param workedQuestions
	 *            the questions that have been worked.
	 * @param mappedFields
	 *            the fields that have been mapped.
	 * @return the mobile group.
	 * @since Apr 10, 2012
	 * @version May 8, 2012
	 */
	private MobileGroup convertQuestionToMobileQuestions(final WizardContext wizardContext, final Question question,
			final Set<Question> workedQuestions, final Map<QuestionField, MobileQuestion> mappedFields) {
		// trace("  Question: %s/%s\n", question.getName(), question.getTitle());
		QuestionVariant defaultVariant = null;
		final List<MobileVariant> mobileVariants = new LinkedList<MobileVariant>();
		for (final QuestionVariant variant : question.getVariants()) {
			if (variant.getKeys() == null || variant.getKeys().isEmpty()) {
				defaultVariant = variant;
				continue;
			}
			final MobileVariant mobileVariant = convertVariantToMobileQuestions(wizardContext, question, variant, workedQuestions,
					mappedFields);
			if (mobileVariant != null) {
				if (!mobileVariants.isEmpty()) {
					if (checkMobileDependencies(mobileVariant) != checkMobileDependencies(mobileVariants.get(0))) {
						LOGGER.warn("Found variants with and without question dependencies in group " + question.getTitle());
						continue;
					}
				}
				mobileVariants.add(mobileVariant);
			}
		}

		if (defaultVariant != null) {
			if (mobileVariants.isEmpty()) {
				final MobileVariant mobileVariant = convertVariantToMobileQuestions(wizardContext, question, defaultVariant,
						workedQuestions, mappedFields);
				if (mobileVariant != null) {
					mobileVariants.add(mobileVariant);
				}
			} else if (checkMobileDependencies(mobileVariants.get(0))) {
				LOGGER.warn("Found default variant and variants with question dependencies in group " + question.getTitle());
			}
		}

		if (!mobileVariants.isEmpty()) {
			final MobileGroup mobileGroup = new MobileGroup(question.getTitle());
			mobileGroup.setChildren(mobileVariants);
			return mobileGroup;
		}

		return null;
	}

	/**
	 * Converts the input string to database form.
	 * 
	 * @author IanBrown
	 * @param existingString
	 *            the existing string.
	 * @param string
	 *            the string.
	 * @return the string if not <code>null</code> or the existing string.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private String convertString(final String existingString, final String string) {
		return string == null ? existingString : string;
	}

	/**
	 * Converts the specified variant to mobile questions based on the context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param question
	 *            the question.
	 * @param variant
	 *            the variant.
	 * @param workedQuestions
	 *            the questions that have been worked.
	 * @param mappedFields
	 *            the fields that have been mapped.
	 * @return the mobile variant representing the variant or <code>null</code> if the variant is not needed.
	 * @since Apr 10, 2012
	 * @version May 3, 2012
	 */
	private MobileVariant convertVariantToMobileQuestions(final WizardContext wizardContext, final Question question,
			final QuestionVariant variant, final Set<Question> workedQuestions,
			final Map<QuestionField, MobileQuestion> mappedFields) {
		// trace("    Variant: %s\n", variant.getTitle());
		final MobileVariant mobileVariant = new MobileVariant(variant.getTitle());
		final List<MobileDependency> mobileDependencies = checkVariantDependencies(wizardContext, question, variant,
				workedQuestions, mappedFields);
		if (mobileDependencies != null) {
			final List<MobileQuestion> mobileQuestions = new LinkedList<MobileQuestion>();
			for (final QuestionField field : variant.getFields()) {
				final MobileQuestion mobileQuestion = convertFieldToMobileQuestion(wizardContext, field, mappedFields);
				if (mobileQuestion != null) {
					mobileQuestions.add(mobileQuestion);
				}
			}

			if (!mobileQuestions.isEmpty()) {
				mobileVariant.setChildren(mobileQuestions);
				if (!mobileDependencies.isEmpty()) {
					mobileVariant.setDependencies(mobileDependencies);
				}
				return mobileVariant;
			}
		}

		return null;
	}

	/**
	 * Trace the flow.
	 * 
	 * @author IanBrown
	 * @param format
	 *            the string to use to format the output.
	 * @param args
	 *            the optional arguments.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@SuppressWarnings("unused")
	private void trace(final String format, final Object... args) {
		if (TRACE_LOGGING_ON) {
			System.err.print(String.format(format, args));
		}
	}
}
