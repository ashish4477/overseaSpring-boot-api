/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.ovf.actions.commons.AbstractComponentCheck;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.FieldTypeMultipleSelection;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.SvrPropertiesService;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link AllowedForAddOnCheck} test for {@link PartisanPartyAddOn}.
 * 
 * @author Ian Brown
 * 
 */
public final class PartisanPartyAddOnTest extends
    AllowedForAddOnCheck<PartisanPartyAddOn> {

	/**
	 * the header text.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version May 7, 2013
	 */
	private final static String[] HEADER_TEXT = { "Header 1", "Header 2" };

	/**
	 * the header title.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version May 7, 2013
	 */
	private final static String HEADER_TITLE = "Header Title";

	/**
	 * the election service.
	 */
	private ElectionService electionService;

	/**
	 * the question field service.
	 */
	private QuestionFieldService questionFieldService;

	/**
	 * the voting precinct service.
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn#getFirstFieldId(com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * .
	 */
	@Test
	public final void testGetFirstFieldId() {
		final WizardResults wizardResults = createMock("WizardResults",
		    WizardResults.class);
		final QuestionnairePage currentPage = createMock("CurrentPage",
		    QuestionnairePage.class);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final int stepNumber = 3;
		EasyMock.expect(currentPage.getStepNumber()).andReturn(stepNumber);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		final Long actualFirstFieldId = getComponent().getFirstFieldId(currentPage);

		assertNotNull("A first field ID is returned", actualFirstFieldId);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are no contests.
	 * 
	 * @throws Exception
	 *           if there is a problem finding contests.
	 */
	@Test
	public final void testPrepareAddOnPage_noContests() throws Exception {
		WizardResults wizardResults = createMock("WizardResults",
		    WizardResults.class);
		QuestionnairePage currentPage = createMock("CurrentPage",
		    QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final String stateAbbreviation = "AB";
		final State votingState = createState(stateAbbreviation);
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, votingState, votingRegionName);
		EasyMock
		    .expect(
		        getVotingPrecinctService().isReady(stateAbbreviation,
		            votingRegionName)).andReturn(true);
		EasyMock.expect(
		    getElectionService().isReady(stateAbbreviation, votingRegionName))
		    .andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress",
		    WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress",
		    ValidAddress.class);
		EasyMock.expect(
		    getVotingPrecinctService().validateAddress(votingAddress, votingState))
		    .andReturn(validAddress);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(
		    contests);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are no partisan contests.
	 * 
	 * @throws Exception
	 *           if there is a problem finding contests.
	 */
	@Test
	public final void testPrepareAddOnPage_noPartisanContests() throws Exception {
		WizardResults wizardResults = createMock("WizardResults",
		    WizardResults.class);
		QuestionnairePage currentPage = createMock("CurrentPage",
		    QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final String stateAbbreviation = "AB";
		final State votingState = createState(stateAbbreviation);
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, votingState, votingRegionName);
		EasyMock
		    .expect(
		        getVotingPrecinctService().isReady(stateAbbreviation,
		            votingRegionName)).andReturn(true);
		EasyMock.expect(
		    getElectionService().isReady(stateAbbreviation, votingRegionName))
		    .andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress",
		    WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress",
		    ValidAddress.class);
		EasyMock.expect(
		    getVotingPrecinctService().validateAddress(votingAddress, votingState))
		    .andReturn(validAddress);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(
		    contests);
		VipContest nonPartisanContest = createContest(false, null);
		contests.add(nonPartisanContest);
		EasyMock.expect(getElectionService().findPartisanContests(contests)).andReturn(null);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where the flow is not FWAB.
	 */
	@Test
	public final void testPrepareAddOnPage_notFWAB() {
		WizardResults wizardResults = createMock("WizardResults",
		    WizardResults.class);
		QuestionnairePage currentPage = createMock("CurrentPage",
		    QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.RAVA);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are partisan contests.
	 * 
	 * @throws Exception
	 *           if there is a problem finding contests.
	 */
	@Test
	public final void testPrepareAddOnPage_partisanContests() throws Exception {
		WizardResults wizardResults = createMock("WizardResults",
		    WizardResults.class);
		QuestionnairePage currentPage = createMock("CurrentPage",
		    QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		final String currentPageTitle = "Current Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(currentPageTitle);
		wizardResults.setCurrentPageTitle(currentPageTitle);
		final String stateAbbreviation = "AB";
		final State votingState = createState(stateAbbreviation);
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, votingState, votingRegionName);
		EasyMock
		    .expect(
		        getVotingPrecinctService().isReady(stateAbbreviation,
		            votingRegionName)).andReturn(true);
		EasyMock.expect(
		    getElectionService().isReady(stateAbbreviation, votingRegionName))
		    .andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress",
		    WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress",
		    ValidAddress.class);
		EasyMock.expect(
		    getVotingPrecinctService().validateAddress(votingAddress, votingState))
		    .andReturn(validAddress);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(
		    contests);
		VipContest nonPartisanContest = createContest(false, null);
		contests.add(nonPartisanContest);
		final String party1 = "Party 1";
		VipContest partisanContest1 = createContest(true, party1);
		contests.add(partisanContest1);
		final String party2 = "Party 2";
		VipContest partisanContest2 = createContest(true, party2);
		contests.add(partisanContest2);
		EasyMock.expect(getElectionService().findPartisanContests(contests)).andReturn(Arrays.asList(partisanContest1, partisanContest2));
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT);
		final FieldType radioButtonFieldType = addFieldType(FieldType.TEMPLATE_RADIO);
		String noPartyName = singlelineProperty(stateAbbreviation, votingRegionName, PartisanPartyAddOn.NO_PARTY_PROPERTY, null, PartisanPartyAddOn.DEFAULT_NO_PARTY_NAME);
		List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		Map<Long, Answer> answers = new LinkedHashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers)
		    .anyTimes();
		buildHeaderGroup(currentPage, noInputFieldType, stateAbbreviation,
		    votingRegionName, HEADER_TITLE, HEADER_TEXT, softId, wizardResults,
		    answers);
		buildPartisanGroup(currentPage, noInputFieldType, radioButtonFieldType,
		    stateAbbreviation, votingRegionName, new String[] { party1, party2, noPartyName },
		    softId, wizardResults, answers);
		replayAll();
		final WizardContext form = new WizardContext(wizardResults);
		form.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Adds a field type with the specified template.
	 * 
	 * @author IanBrown
	 * @param template
	 *          the template.
	 * @return the field type.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2012
	 */
	private FieldType addFieldType(final String template) {
		final FieldType fieldType = createMock(template, FieldType.class);
		EasyMock
		    .expect(getQuestionFieldService().findFieldTypeByTemplate(template))
		    .andReturn(fieldType);
		return fieldType;
	}

	/**
	 * Adds the step number to the current page.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *          the current page.
	 * @return the step number.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private int addStepNumber(final QuestionnairePage currentPage) {
		final int stepNumber = 4;
		EasyMock.expect(currentPage.getStepNumber()).andReturn(stepNumber);
		return stepNumber;
	}

	/**
	 * Adds the voting state to the wizard results.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *          the wizard results.
	 * @param votingState
	 *          the voting state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @return the voting state.
	 * @since Aug 7, 2012
	 * @version Oct 20, 2012
	 */
	private VotingRegion addVotingRegion(final WizardResults wizardResults,
	    final State votingState, final String votingRegionName) {
		final VotingRegion votingRegion = createMock("VotingRegion",
		    VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion)
		    .atLeastOnce();
		EasyMock.expect(votingRegion.getState()).andReturn(votingState)
		    .atLeastOnce();
		EasyMock.expect(votingRegion.getName()).andReturn(votingRegionName)
		    .anyTimes();
		return votingRegion;
	}

	/**
	 * Builds the header group for the page.
	 * 
	 * @param currentPage
	 *          the current page.
	 * @param noInputFieldType
	 *          the no-input type of field.
	 * @param stateAbbreviation
	 *          the state abbreviation.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param headerTitle
	 *          TODO
	 * @param headerText
	 *          TODO
	 * @param softId
	 *          the software identifiers.
	 * @param wizardResults
	 *          the wizard results.
	 * @param answers
	 *          the answers.
	 */
	private void buildHeaderGroup(QuestionnairePage currentPage,
	    FieldType noInputFieldType, String stateAbbreviation,
	    String votingRegionName, String headerTitle, String[] headerText,
	    long[] softId, WizardResults wizardResults, Map<Long, Answer> answers) {
		final String headerLine = singlelineProperty(stateAbbreviation,
		    votingRegionName, PartisanPartyAddOn.HEADER_TITLE_PROPERTY,
		    headerTitle, PartisanPartyAddOn.HEADER_GROUP_TITLE);
		final Question headerGroup = createGroup(currentPage, "HeaderGroup",
		    softId, StateContestsPageAddOn.HEADER_GROUP_NAME, headerLine);
		final QuestionVariant headerVariant = createVariant(headerGroup,
		    "HeaderVariant", softId);
		multilineProperty(stateAbbreviation, votingRegionName,
		    PartisanPartyAddOn.HEADER_TEXT_PROPERTY, headerText,
		    PartisanPartyAddOn.HEADER_TEXT);
		createField(headerVariant, "HeaderField", softId, noInputFieldType,
		    PartisanPartyAddOn.HEADER_FIELD_TITLE, null, answers, false,
		    wizardResults);
	}

	/**
	 * Builds the group that requests the partisan party.
	 * 
	 * @param currentPage
	 *          the current page.
	 * @param noInputFieldType
	 *          the no-input field type.
	 * @param radioButtonFieldType
	 *          the radio button field type.
	 * @param stateAbbreviation
	 *          the state abbreviation.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param partyNames
	 *          the names of the parties.
	 * @param softId
	 *          the soft identifiers.
	 * @param wizardResults
	 *          the wizard results.
	 * @param answers
	 *          the answers.
	 */
	private void buildPartisanGroup(QuestionnairePage currentPage,
	    FieldType noInputFieldType, FieldType radioButtonFieldType,
	    String stateAbbreviation, String votingRegionName, String[] partyNames,
	    long[] softId, WizardResults wizardResults, Map<Long, Answer> answers) {
		final String partisanGroupTitle = singlelineProperty(stateAbbreviation,
		    votingRegionName, PartisanPartyAddOn.PARTISAN_TITLE_PROPERTY, null,
		    PartisanPartyAddOn.PARTISAN_GROUP_TITLE);
		final Question partisanGroup = createGroup(currentPage, "PartisanGroup",
		    softId, PartisanPartyAddOn.PARTISAN_GROUP_NAME, partisanGroupTitle);
		final QuestionVariant partisanVariant = createVariant(partisanGroup,
		    "PartisanVariant", softId);
		multilineProperty(stateAbbreviation, votingRegionName,
		    PartisanPartyAddOn.PARTISAN_TEXT_PROPERTY, null,
		    PartisanPartyAddOn.PARTISAN_TEXT);
		QuestionField partisanField = createField(partisanVariant,
		    PartisanPartyAddOn.PARTISAN_FIELD_NAME, softId, radioButtonFieldType,
		    PartisanPartyAddOn.PARTISAN_FIELD_TITLE, null, answers, true,
		    wizardResults);
		partisanField.setRequired(false);
		partisanField.setSecurity(true);
		Collection<FieldDictionaryItem> options = new LinkedList<FieldDictionaryItem>();
		for (final String partyName : partyNames) {
			final FieldDictionaryItem partyOption = createMock(
			    makeIdentifier(partyName) + "Option", FieldDictionaryItem.class);
			options.add(partyOption);
		}
		EasyMock.expect(getValet().createOptions(EasyMock.aryEq(partyNames))).andReturn(options);
		partisanField.setGenericOptions(options);
	}

	/**
	 * Creates a contest.
	 * 
	 * @param partisan
	 *          <code>true</code> if the contest is partisan, <code>false</code>
	 *          otherwise.
	 * @param partyName
	 *          the name of the party.
	 * @return the contest.
	 */
	private VipContest createContest(boolean partisan, String partyName) {
		final VipContest contest = createMock("Contest" + partisan
		    + ((partyName == null) ? "" : partyName.replace(" ", "")),
		    VipContest.class);
		EasyMock.expect(contest.isPartisan()).andReturn(partisan).anyTimes();
		EasyMock.expect(contest.getPartisanParty()).andReturn(partyName).anyTimes();
		return contest;
	}

	/**
	 * Creates a state for the specified abbreviation.
	 * 
	 * @author IanBrown
	 * @param abbreviation
	 *          the abbreviation.
	 * @return the state.
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	private State createState(final String abbreviation) {
		final State state = createMock("State_" + abbreviation, State.class);
		EasyMock.expect(state.getAbbr()).andReturn(abbreviation).anyTimes();
		return state;
	}

	/**
	 * Gets the election service.
	 * 
	 * @return the electionService.
	 */
	private ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the question field service.
	 * 
	 * @return the questionFieldService.
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @return the votingPrecinctService.
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Sets the election service.
	 * 
	 * @param electionService
	 *          the electionService to set.
	 */
	private void setElectionService(ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Sets the question field service.
	 * 
	 * @param questionFieldService
	 *          the questionFieldService to set
	 */
	private void setQuestionFieldService(QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @param votingPrecinctService
	 *          the votingPrecinctService to set.
	 */
	private void setVotingPrecinctService(
	    VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/** @{@inheritDoc */
	@Override
	protected final PartisanPartyAddOn createAllowedForAddOn() {
		PartisanPartyAddOn partisanPartyAddOn = new PartisanPartyAddOn();
		partisanPartyAddOn.setValet(getValet());
		ReflectionTestUtils.setField(partisanPartyAddOn, "votingPrecinctService",
		    getVotingPrecinctService());
		ReflectionTestUtils.setField(partisanPartyAddOn, "electionService",
		    getElectionService());
		ReflectionTestUtils.setField(partisanPartyAddOn, "questionFieldService",
		    getQuestionFieldService());
		ReflectionTestUtils.setField(partisanPartyAddOn, "svrPropertiesService",
		    getSvrPropertiesService());
		return partisanPartyAddOn;
	}

	/** @{@inheritDoc */
	@Override
	protected final void setUpForAllowedForAddOn() {
		setValet(createMock("Valet", PageAddOnValet.class));
		setVotingPrecinctService(createMock("VotingPrecinctService",
		    VotingPrecinctService.class));
		setElectionService(createMock("ElectionService", ElectionService.class));
		setQuestionFieldService(createMock("QuestionFieldService",
		    QuestionFieldService.class));
		setSvrPropertiesService(createMock("SvrPropertiesService",
		    SvrPropertiesService.class));
	}

	/** @{@inheritDoc */
	@Override
	protected final void tearDownForAllowedForAddOn() {
		setSvrPropertiesService(null);
		setQuestionFieldService(null);
		setElectionService(null);
		setVotingPrecinctService(null);
		setValet(null);
	}
}
