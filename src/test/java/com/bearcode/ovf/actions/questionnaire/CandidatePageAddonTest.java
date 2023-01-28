/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.webservices.DistrictLookupService;
import com.bearcode.ovf.webservices.SmartyStreetService;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link CandidatePageAddon}.
 * 
 * @author IanBrown
 * 
 * @since Jun 18, 2012
 * @version Jun 18, 2012
 */
public final class CandidatePageAddonTest extends EasyMockSupport {

	/**
	 * the candidate page add on to test.
	 * 
	 * @author IanBrown
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private CandidatePageAddon candidatePageAddon;

	/**
	 * the district lookup service.
	 * 
	 * @author IanBrown
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private DistrictLookupService districtLookupService;

    private SmartyStreetService smartyStreetService;

	/**
	 * the question field service.
	 * 
	 * @author IanBrown
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private QuestionFieldService questionFieldService;

	/**
	 * the vote smart service.
	 * 
	 * @author IanBrown
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private VoteSmartService voteSmartService;

	/**
	 * Sets up the candidate page add on to test.
	 * 
	 * @author IanBrown
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@Before
	public final void setUpCandidatePageAddon() {
		setDistrictLookupService(createMock("DistrictLookupService", DistrictLookupService.class));
        setSmartyStreetService(createMock("SmartyStreetService", SmartyStreetService.class));
		setQuestionFieldService(createMock("QuestionFieldService", QuestionFieldService.class));
		setVoteSmartService(createMock("VoteSmartService", VoteSmartService.class));
		setCandidatePageAddon(new CandidatePageAddon());
		ReflectionTestUtils.setField(getCandidatePageAddon(), "districtLookupService", getDistrictLookupService());
        ReflectionTestUtils.setField(getCandidatePageAddon(), "smartyStreetService", getSmartyStreetService());
		ReflectionTestUtils.setField(getCandidatePageAddon(), "questionFieldService", getQuestionFieldService());
		ReflectionTestUtils.setField(getCandidatePageAddon(), "voteSmartService", getVoteSmartService());
	}

	/**
	 * Tears down the candidate page add on after testing.
	 * 
	 * @author IanBrown
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@After
	public final void tearDownCandidatePageAddon() {
		setCandidatePageAddon(null);
		setVoteSmartService(null);
		setQuestionFieldService(null);
		setDistrictLookupService(null);
        setSmartyStreetService(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CandidatePageAddon#fillPageWithQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@Test
	public final void testFillPageWithQuestions() {
		final String street1 = "1 Street";
		final String city = "City";
		final String state = "ST";
		final String zip = "12345";
		final String district = "District";
		final String zip4 = "6789";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		addVotingAddressToWizardResults(street1, city, state, zip, wizardResults);
		addDistrictInfo(street1, city, state, zip, district, zip4);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final int currentStepNumber = 2;
		EasyMock.expect(page.getStepNumber()).andReturn(currentStepNumber);
		final List<Question> questions = new LinkedList<Question>();
		page.setQuestions(questions);
		EasyMock.expect(page.getQuestions()).andReturn(questions).atLeastOnce();
		addFieldType("TYPE_NO_INPUT_ID");
		final Map<Long, Answer> answersMap = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answersMap).atLeastOnce();
		final FieldType radioType = addFieldType("TYPE_RADIO_ID");
		createPresidentList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "President", wizardResults);
		createSenateList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "Senate", wizardResults);
		createSenateSpecialList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "SenateSpecial", wizardResults);
		createRepresentativeList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "Representative", wizardResults);
		createRepresentativeSpecialList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "RepresentativeSpecial", wizardResults);
		replayAll();
		final WizardContext formObject = new WizardContext(wizardResults);
		formObject.setCurrentPage(currentPage);

		getCandidatePageAddon().fillPageWithQuestions(formObject, page);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CandidatePageAddon#fillPageWithQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the primaries.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@Test
	public final void testFillPageWithQuestions_primaries() {
		final String street1 = "1 Street";
		final String city = "City";
		final String state = "ST";
		final String zip = "12345";
		final String district = "District";
		final String zip4 = "6789";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		addVotingAddressToWizardResults(street1, city, state, zip, wizardResults);
		addDistrictInfo(street1, city, state, zip, district, zip4);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final int currentStepNumber = 2;
		EasyMock.expect(page.getStepNumber()).andReturn(currentStepNumber);
		final List<Question> questions = new LinkedList<Question>();
		page.setQuestions(questions);
		EasyMock.expect(page.getQuestions()).andReturn(questions).atLeastOnce();
		addFieldType("TYPE_NO_INPUT_ID");
		final Map<Long, Answer> answersMap = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answersMap).atLeastOnce();
		final FieldType radioType = addFieldType("TYPE_RADIO_ID");
		createPresidentList(zip, zip4, false);
		createPrimaryPresidentList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "PrimaryPresident", wizardResults);
		createSenateList(zip, zip4, false);
		createSenatePrimaryList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "SenatePrimary", wizardResults);
		createSenateSpecialList(zip, zip4, false);
		createRepresentativeList(zip, zip4, false);
		createRepresentativePrimaryList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "RepresentativePrimary", wizardResults);
		createRepresentativeSpecialList(zip, zip4, false);
		replayAll();
		final WizardContext formObject = new WizardContext(wizardResults);
		formObject.setCurrentPage(currentPage);

		getCandidatePageAddon().fillPageWithQuestions(formObject, page);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CandidatePageAddon#fillPageWithQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are no candidates.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@Test
	public final void testFillPageWithQuestions_noCandidates() {
		final String street1 = "1 Street";
		final String city = "City";
		final String state = "ST";
		final String zip = "12345";
		final String district = "District";
		final String zip4 = "6789";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		addVotingAddressToWizardResults(street1, city, state, zip, wizardResults);
		addDistrictInfo(street1, city, state, zip, district, zip4);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final int currentStepNumber = 2;
		EasyMock.expect(page.getStepNumber()).andReturn(currentStepNumber);
		final List<Question> questions = new LinkedList<Question>();
		page.setQuestions(questions);
		EasyMock.expect(page.getQuestions()).andReturn(questions).atLeastOnce();
		addFieldType("TYPE_NO_INPUT_ID");
		final Map<Long, Answer> answersMap = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answersMap).atLeastOnce();
		addFieldType("TYPE_RADIO_ID");
		createPresidentList(zip, zip4, false);
		createPrimaryPresidentList(zip, zip4, false);
		createSenateList(zip, zip4, false);
		createSenatePrimaryList(zip, zip4, false);
		createSenateSpecialList(zip, zip4, false);
		createRepresentativeList(zip, zip4, false);
		createRepresentativePrimaryList(zip, zip4, false);
		createRepresentativeSpecialList(zip, zip4, false);
		replayAll();
		final WizardContext formObject = new WizardContext(wizardResults);
		formObject.setCurrentPage(currentPage);

		getCandidatePageAddon().fillPageWithQuestions(formObject, page);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CandidatePageAddon#getFirstFieldId(com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@Test
	public final void testGetFirstFieldId() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final int stepNumber = 3;
		EasyMock.expect(currentPage.getStepNumber()).andReturn(stepNumber);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		final Long actualFirstFieldId = getCandidatePageAddon().getFirstFieldId(currentPage);

		assertNotNull("A first field ID is returned", actualFirstFieldId);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CandidatePageAddon#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for a non-FWAB page.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_notFWAB() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		getCandidatePageAddon().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CandidatePageAddon#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	@Test
	public final void testPrepareAddOnPage() {
		final String street1 = "1 Street";
		final String city = "City";
		final String state = "ST";
		final String zip = "12345";
		final String district = "District";
		final String zip4 = "6789";
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final FlowType flowType = FlowType.FWAB;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		addVotingAddressToWizardResults(street1, city, state, zip, wizardResults);
		addDistrictInfo(street1, city, state, zip, district, zip4);
		final int currentStepNumber = 2;
		EasyMock.expect(currentPage.getStepNumber()).andReturn(currentStepNumber);
		final List<Question> questions = new LinkedList<Question>();
		currentPage.setQuestions(questions);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).atLeastOnce();
		addFieldType("TYPE_NO_INPUT_ID");
		final Map<Long, Answer> answersMap = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answersMap).atLeastOnce();
		final FieldType radioType = addFieldType("TYPE_RADIO_ID");
		createPresidentList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "President", wizardResults);
		createSenateList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "Senate", wizardResults);
		createSenateSpecialList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "SenateSpecial", wizardResults);
		createRepresentativeList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "Representative", wizardResults);
		createRepresentativeSpecialList(zip, zip4, true);
		addAnswerToWizardResults(radioType, "RepresentativeSpecial", wizardResults);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		getCandidatePageAddon().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Adds an answer for the field type to the wizard results.
	 * 
	 * @author IanBrown
	 * @param fieldType
	 *            the field type.
	 * @param answerName
	 *            the name of the answer.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private final void addAnswerToWizardResults(final FieldType fieldType, final String answerName,
			final WizardResults wizardResults) {
		final Answer answer = createMock(answerName + "Answer", Answer.class);
		EasyMock.expect(fieldType.createAnswerOfType()).andReturn(answer);
		answer.setField((QuestionField) EasyMock.anyObject());
		final QuestionField field = createMock(answerName + "Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field);
		wizardResults.putAnswer(answer);
	}

	/**
	 * Adds the district information.
	 * 
	 * @author IanBrown
	 * @param street1
	 *            the first street address line.
	 * @param city
	 *            the city.
	 * @param state
	 *            the state.
	 * @param zip
	 *            the ZIP code.
	 * @param district
	 *            the name of the district.
	 * @param zip4
	 *            the ZIP+4 code.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void addDistrictInfo(final String street1, final String city, final String state, final String zip,
			final String district, final String zip4) {
		final String[] districtInfo = new String[3];
		EasyMock.expect(getSmartyStreetService().findDistrict(street1, city, state, zip)).andReturn(districtInfo);
		districtInfo[0] = district;
		districtInfo[1] = zip;
		districtInfo[2] = zip4;
	}

	/**
	 * Adds a field type with the specified name.
	 * 
	 * @author IanBrown
	 * @param fieldTypeName
	 *            the name of the field type.
	 * @return the field type.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private FieldType addFieldType(final String fieldTypeName) {
		final FieldType fieldType = createMock(fieldTypeName, FieldType.class);
		EasyMock.expect(
				getQuestionFieldService().findFieldTypeById(
						(Long) ReflectionTestUtils.getField(getCandidatePageAddon(), fieldTypeName))).andReturn(fieldType)
				.atLeastOnce();
		return fieldType;
	}

	/**
	 * Adds a voting address to the wizard results.
	 * 
	 * @author IanBrown
	 * @param street1
	 *            the first street address line.
	 * @param city
	 *            the city.
	 * @param state
	 *            the state.
	 * @param zip
	 *            the ZIP code.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void addVotingAddressToWizardResults(final String street1, final String city, final String state, final String zip,
			final WizardResults wizardResults) {
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		EasyMock.expect(votingAddress.getState()).andReturn(state);
		EasyMock.expect(votingAddress.getZip()).andReturn(zip);
		EasyMock.expect(votingAddress.getStreet1()).andReturn(street1);
		EasyMock.expect(votingAddress.getCity()).andReturn(city);
	}

	/**
	 * Creates a president list for the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param includeOption
	 *            <code>true</code> to include an option.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createPresidentList(final String state, final String zip4, final boolean includeOption) {
		final FieldDictionaryItem presidentOption = createMock("PresidentOption", FieldDictionaryItem.class);
		getVoteSmartService().createPresidentZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state), EasyMock.eq(zip4));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createPresidentZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> presidentOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					presidentOptions.add(presidentOption);
				}
				field.setGenericOptions(presidentOptions);
			}
		});
	}

	/**
	 * Creates a president primary list for the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param includeOption
	 *            <code>true</code> to include an option.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createPrimaryPresidentList(final String state, final String zip4, final boolean includeOption) {
		final FieldDictionaryItem primaryPresidentOption = createMock("PrimaryPresidentOption", FieldDictionaryItem.class);
		getVoteSmartService().createPresidentPrimaryZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state), EasyMock.eq(zip4));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createPresidentPrimaryZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> primaryPresidentOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					primaryPresidentOptions.add(primaryPresidentOption);
				}
				field.setGenericOptions(primaryPresidentOptions);
			}
		});
	}

	/**
	 * Creates a representative list for the state and district.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param district
	 *            the district.
	 * @param includeOption
	 *            <code>true</code> to include an option.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createRepresentativeList(final String state, final String district, final boolean includeOption) {
		final FieldDictionaryItem representativeOption = createMock("RepresentativeOption", FieldDictionaryItem.class);
		getVoteSmartService().createRepresentativeZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state),
				EasyMock.eq(district));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createRepresentativeZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> representativeOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					representativeOptions.add(representativeOption);
				}
				field.setGenericOptions(representativeOptions);
			}
		});
	}

	/**
	 * Creates a representative primary list for the state and district.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param district
	 *            the district.
	 * @param includeOption
	 *            <code>true</code> to include an option.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createRepresentativePrimaryList(final String state, final String district, final boolean includeOption) {
		final FieldDictionaryItem representativePrimaryOption = createMock("RepresentativePrimaryOption", FieldDictionaryItem.class);
		getVoteSmartService().createRepresentativePrimaryZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state),
				EasyMock.eq(district));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createRepresentativePrimaryZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> representativePrimaryOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					representativePrimaryOptions.add(representativePrimaryOption);
				}
				field.setGenericOptions(representativePrimaryOptions);
			}
		});
	}

	/**
	 * Creates a special representative list for the state and district.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param district
	 *            the district.
	 * @param includeOption
	 *            <code>true</code> to include an option.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createRepresentativeSpecialList(final String state, final String district, final boolean includeOption) {
		final FieldDictionaryItem representativeSpecialOption = createMock("RepresentativeSpecialOption", FieldDictionaryItem.class);
		getVoteSmartService().createRepresentativeSpecialZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state),
				EasyMock.eq(district));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createRepresentativeSpecialZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> representativeSpecialOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					representativeSpecialOptions.add(representativeSpecialOption);
				}
				field.setGenericOptions(representativeSpecialOptions);
			}
		});
	}

	/**
	 * Creates a senate list for the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param includeOption
	 *            <code>true</code> to include an option, <code>false</code> otherwise.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createSenateList(final String state, final String zip4, final boolean includeOption) {
		final FieldDictionaryItem senateOption = createMock("SenateOption", FieldDictionaryItem.class);
		getVoteSmartService().createSenateZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state), EasyMock.eq(zip4));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createSenateZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> senateOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					senateOptions.add(senateOption);
				}
				field.setGenericOptions(senateOptions);
			}
		});
	}

	/**
	 * Creates a senate primary list for the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param includeOption
	 *            <code>true</code> to include an option, <code>false</code> otherwise.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createSenatePrimaryList(final String state, final String zip4, final boolean includeOption) {
		final FieldDictionaryItem senatePrimaryOption = createMock("SenatePrimaryOption", FieldDictionaryItem.class);
		getVoteSmartService().createSenatePrimaryZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state), EasyMock.eq(zip4));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createSenatePrimaryZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> senatePrimaryOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					senatePrimaryOptions.add(senatePrimaryOption);
				}
				field.setGenericOptions(senatePrimaryOptions);
			}
		});
	}

	/**
	 * Creates a senate special list for the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param includeOption
	 *            <code>true</code> to include an option, <code>false</code> otherwise.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void createSenateSpecialList(final String state, final String zip4, final boolean includeOption) {
		final FieldDictionaryItem senateSpecialOption = createMock("SenateSpecialOption", FieldDictionaryItem.class);
		getVoteSmartService().createSenateSpecialZipList((QuestionField) EasyMock.anyObject(), EasyMock.eq(state), EasyMock.eq(zip4));
		EasyMock.expectLastCall().andDelegateTo(new VoteSmartService() {
			@Override
			public final void createSenateSpecialZipList(final QuestionField field, final String state, final String district) {
				final Collection<FieldDictionaryItem> senateSpecialOptions = new ArrayList<FieldDictionaryItem>();
				if (includeOption) {
					senateSpecialOptions.add(senateSpecialOption);
				}
				field.setGenericOptions(senateSpecialOptions);
			}
		});
	}

	/**
	 * Gets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @return the candidate page add on.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private CandidatePageAddon getCandidatePageAddon() {
		return candidatePageAddon;
	}

	/**
	 * Gets the district lookup service.
	 * 
	 * @author IanBrown
	 * @return the district lookup service.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private DistrictLookupService getDistrictLookupService() {
		return districtLookupService;
	}

	/**
	 * Gets the question field service.
	 * 
	 * @author IanBrown
	 * @return the question field service.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

	/**
	 * Gets the vote smart service.
	 * 
	 * @author IanBrown
	 * @return the vote smart service.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private VoteSmartService getVoteSmartService() {
		return voteSmartService;
	}

	/**
	 * Sets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @param candidatePageAddon
	 *            the candidate page add on to set.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void setCandidatePageAddon(final CandidatePageAddon candidatePageAddon) {
		this.candidatePageAddon = candidatePageAddon;
	}

	/**
	 * Sets the district lookup service.
	 * 
	 * @author IanBrown
	 * @param districtLookupService
	 *            the district lookup service to set.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void setDistrictLookupService(final DistrictLookupService districtLookupService) {
		this.districtLookupService = districtLookupService;
	}

	/**
	 * Sets the question field service.
	 * 
	 * @author IanBrown
	 * @param questionFieldService
	 *            the question field service to set.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void setQuestionFieldService(final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

	/**
	 * Sets the vote smart service.
	 * 
	 * @author IanBrown
	 * @param voteSmartService
	 *            the vote smart service to set.
	 * @since Jun 18, 2012
	 * @version Jun 18, 2012
	 */
	private void setVoteSmartService(final VoteSmartService voteSmartService) {
		this.voteSmartService = voteSmartService;
	}

    public SmartyStreetService getSmartyStreetService() {
        return smartyStreetService;
    }

    public void setSmartyStreetService(final SmartyStreetService smartyStreetService) {
        this.smartyStreetService = smartyStreetService;
    }
}
