/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.vip.*;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.SvrPropertiesService;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;
import org.easymock.EasyMock;
import org.junit.Test;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Extended {@link AllowedForAddOnCheck} test for {@link StateContestsPageAddOn}.
 * 
 * @author IanBrown
 * 
 * @since Aug 7, 2012
 * @version Oct 1, 2013
 */
public final class StateContestsPageAddOnTest extends AllowedForAddOnCheck<StateContestsPageAddOn> {

	/**
	 * Enumeration of the types of elections.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private enum ElectionType {

		/**
		 * a general election.
		 * 
		 * @author IanBrown
		 * @since Aug 8, 2012
		 * @version Aug 8, 2012
		 */
		GENERAL("General"),

		/**
		 * a primary election.
		 * 
		 * @author IanBrown
		 * @since Aug 8, 2012
		 * @version Aug 8, 2012
		 */
		PRIMARY("Primary"),

		/**
		 * A runoff election.
		 * 
		 * @author IanBrown
		 * @since Aug 8, 2012
		 * @version Aug 8, 2012
		 */
		RUNOFF("Runoff");

		/**
		 * the type of election.
		 * 
		 * @author IanBrown
		 * @since Aug 8, 2012
		 * @version Aug 8, 2012
		 */
		private final String type;

		/**
		 * Constructs an election type.
		 * 
		 * @author IanBrown
		 * @param type
		 *            the string representing the election type.
		 * @since Aug 8, 2012
		 * @version Aug 8, 2012
		 */
		private ElectionType(final String type) {
			this.type = type;
		}

		/**
		 * Gets the string representing the type of election.
		 * 
		 * @author IanBrown
		 * @return the type.
		 * @since Aug 8, 2012
		 * @version Aug 8, 2012
		 */
		public final String getType() {
			return type;
		}
	}

	/**
	 * the election service.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private ElectionService electionService;

	/**
	 * the question field service.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private QuestionFieldService questionFieldService;

	/**
	 * the candidate page add on.
	 * 
	 * @author IanBrown
	 * @since Aug 27, 2012
	 * @version Aug 27, 2012
	 */
	private CandidatePageAddon candidatePageAddon;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * the header title.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private final static String HEADER_TITLE = "Header Title";

    /**
     * Text of page title to make this test working on any locale settings.
     */
    private String titleText;

	/**
	 * the header text.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private final static String[] HEADER_TEXT = { "Header 1", "Header 2" };


    public StateContestsPageAddOnTest() {
        try {
            final Date electionDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-08-2013");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy");
            titleText = "STATE GENERAL ELECTION BALLOT<br/>VOTING REGION, STATE<br/>"+ outputFormat.format( electionDate).toUpperCase();
            // text should be generated using format method to make it independent of locale settings.
        } catch (ParseException e) {
            // exception? are you kidding?
        }

    }

    /**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#getFirstFieldId(com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	@Test
	public final void testGetFirstFieldId() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		addPageTitle(currentPage, wizardResults);
		final int stepNumber = 9829;
		EasyMock.expect(currentPage.getStepNumber()).andReturn(stepNumber);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		final Long actualFirstFieldId = getComponent().getFirstFieldId(currentPage);

		assertNotNull("A first field ID is returned", actualFirstFieldId);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is an answer for the question.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_answered() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		//final State votingState = createState(stateAbbreviation);
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = createMock("Answer", Answer.class);
			final VipContest contest = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide",
					null, contests, ElectionType.GENERAL);
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a custom ballot.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_customBallot() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		//final State votingState = createState(stateAbbreviation);
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.DOMESTIC_VOTER.name());
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide",
					null, contests, ElectionType.GENERAL);
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		final String customBallotDistrictType = "Custom Ballot District Type";
		final VipContest contest = createContestForCustomBallot(contests, customBallotDistrictType);
		final int additionalContestIdx = 1;
		addGroupForCustomBallot(currentPage, softId, additionalContestIdx, noInputFieldType, radioFieldType, multiChoiceFieldType,
				contest, null, answers, wizardResults);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where the flow is not supported.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 7, 2012
	 * @version Sep 13, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_flowNotSupported() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		addPageTitle(currentPage, wizardResults);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.RAVA);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a general election for president.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2018
	 */
	@Test
	public final void testPrepareAddOnPage_generalPresident() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		//final State votingState = createState(stateAbbreviation);
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = standardContest == StandardContest.PRESIDENT ? createContestForOffice(false,
					standardContest.getOffice()[0], "Statewide", "Statewide", null, contests, ElectionType.GENERAL) : null;
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a general election for representative.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_generalRepresentative() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = standardContest == StandardContest.REPRESENTATIVE ? createContestForOffice(false,
					standardContest.getOffice()[0], "Statewide", "Statewide", null, contests, ElectionType.GENERAL) : null;
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a general election for senator.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_generalSenator() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = standardContest == StandardContest.SENATOR ? createContestForOffice(false,
					standardContest.getOffice()[0], "Statewide", "Statewide", null, contests, ElectionType.GENERAL) : null;
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are additional, state level contests, but the person is indefinitely overseas.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_indefinitelyOverseas() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.OVERSEAS_VOTER.name());
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide",
					null, contests, ElectionType.GENERAL);
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		createContestForOffice(false, "Governor", "Statewide", "Statewide", null, contests, ElectionType.GENERAL);
		final String referendumDistrictType = "Referendum District";
		final String effectOfAbstain = "Effect of Abstain";
		createContestForReferendum(contests, referendumDistrictType, "Title", "Subtitle", "Brief", "Text", effectOfAbstain);
		final String customBallotDistrictType = "Custom Ballot District Type";
		createContestForCustomBallot(contests, customBallotDistrictType);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertEquals("There are three contests left unprocessed", 3, contests.size());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are no contests being held.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_noContests() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, null, null, null, answers, null, null, wizardResults, ElectionType.GENERAL);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are ordered contests.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Oct 4, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_orderedContests() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		final String officeOne = "School Board";
		final String contestOfficeOne = ElectionService.CONTEST_OFFICE + ": " + officeOne.toUpperCase();
		final String referendumDistrictType = "Referendum District";
		final String contestReferendum = ElectionService.CONTEST_REFERENDUM + ": " + referendumDistrictType.toUpperCase();
		final String officeTwo = "Judge";
		final String contestOfficeTwo = ElectionService.CONTEST_OFFICE + ": " + officeTwo.toUpperCase();
		final String customBallotDistrictType = "Custom Ballot District Type";
		final String contestCustomBallot = ElectionService.CONTEST_CUSTOM + ": " + customBallotDistrictType.toUpperCase();
		final List<String> contestOrder = Arrays.asList(contestOfficeOne, contestReferendum, contestCustomBallot, contestOfficeTwo);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(contestOrder);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.DOMESTIC_VOTER.name());
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide",
					null, contests, ElectionType.GENERAL);
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		final VipContest contest = createContestForCustomBallot(contests, customBallotDistrictType);
		final String districtType2 = "Judge District";
		final String districtName2 = "Judge District 2";
		final VipContest office2 = createContestForOffice(false, officeTwo, districtType2, districtName2, null, contests,
				ElectionType.GENERAL);
		final String districtType1 = "School District";
		final String districtName1 = "School District 1";
		final int numberVotingFor1 = 3;
		final VipContest office1 = createContestForOffice(false, officeOne, districtType1, districtName1, numberVotingFor1,
				contests, ElectionType.GENERAL);
		final String referendumTitle = "Referendum Title";
		final String referendumSubTitle = "Referendum Subtitle";
		final String referendumBrief = "Referendum Brief";
		final String referendumText = "Referendum Text";
		final String effectOfAbstain = null; // "Effect of Abstain";
		final VipContest referendum = createContestForReferendum(contests, referendumDistrictType, referendumTitle,
				referendumSubTitle, referendumBrief, referendumText, effectOfAbstain);
		int additionalContestIdx = 1;
		addGroupForRegionalContest(officeOne, districtType1, districtName1, null, numberVotingFor1, currentPage, stateAbbreviation,
				votingRegionName, softId, additionalContestIdx, noInputFieldType, radioFieldType, multiChoiceFieldType, office1,
				false, null, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		additionalContestIdx += numberVotingFor1;
		addGroupForReferendum(currentPage, softId, additionalContestIdx, noInputFieldType, radioFieldType, multiChoiceFieldType,
				referendum, referendumTitle, referendumSubTitle, referendumBrief, referendumText, effectOfAbstain, null, answers,
				wizardResults);
		additionalContestIdx += 1 + (effectOfAbstain == null ? 0 : 1);
		addGroupForCustomBallot(currentPage, softId, additionalContestIdx, noInputFieldType, radioFieldType, multiChoiceFieldType,
				contest, null, answers, wizardResults);
		++additionalContestIdx;
		addGroupForRegionalContest(officeTwo, districtType2, districtName2, null, null, currentPage, stateAbbreviation,
				votingRegionName, softId, additionalContestIdx, noInputFieldType, radioFieldType, multiChoiceFieldType, office2,
				false, null, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		++additionalContestIdx;
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Creates an election.
	 * @param stateAbbreviation the abbreviation of the state.
	 * @param electionType the type of election.
	 * @return the election.
	 * @throws ParseException if there is a problem parsing the date.
	 */
	private VipElection createElection(String stateAbbreviation, String electionType) throws ParseException {
	    final VipElection election = createMock("Election", VipElection.class);
	    EasyMock.expect(election.getType()).andReturn(electionType);
	    final VipState state = createMock("State", VipState.class);
	    EasyMock.expect(election.getState()).andReturn(state);
	    EasyMock.expect(state.getName()).andReturn("State");
	    final Date electionDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-08-2013");
		EasyMock.expect(election.getDate()).andReturn(electionDate);
	    return election;
    }

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a primary election for president.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_primaryPresident() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = standardContest == StandardContest.PRESIDENT ? createContestForOffice(false,
					standardContest.getOffice()[0], "Statewide", "Statewide", null, contests, ElectionType.PRIMARY) : null;
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.PRIMARY);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a primary runoff election for senator.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_primaryRunoffSenator() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = standardContest == StandardContest.SENATOR ? createContestForOffice(false,
					standardContest.getOffice()[0], "Statewide", "Statewide", null, contests, ElectionType.PRIMARY,
					ElectionType.RUNOFF) : null;
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.PRIMARY, ElectionType.RUNOFF);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a referendum.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_referendum() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		EasyMock.expect(wizardResults.getFlowType()).andReturn( FlowType.FWAB );
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.DOMESTIC_VOTER.name());
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide",
					null, contests, ElectionType.GENERAL);
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		final String referendumDistrictType = "Referendum District";
		final String referendumTitle = "Referendum Title";
		final String referendumSubTitle = "Referendum Subtitle";
		final String referendumBrief = "Referendum Brief";
		final String referendumText = "Referendum Text";
		final String effectOfAbstain = null; // "Effect of Abstain";
		final VipContest contest = createContestForReferendum(contests, referendumDistrictType, referendumTitle,
				referendumSubTitle, referendumBrief, referendumText, effectOfAbstain);
		int additionalContestIdx = 1;
		addGroupForReferendum(currentPage, softId, additionalContestIdx, noInputFieldType, radioFieldType, multiChoiceFieldType,
				contest, referendumTitle, referendumSubTitle, referendumBrief, referendumText, effectOfAbstain, null, answers,
				wizardResults);
		additionalContestIdx += 1 + (effectOfAbstain == null ? 0 : 1);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a regional candidate contest.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_regionalCandidate() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.DOMESTIC_VOTER.name());
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide",
					null, contests, ElectionType.GENERAL);
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		final String districtType = "School District";
		final String districtName = "School District 1";
		final int numberVotingFor = 3;
		final VipContest schoolBoard = createContestForOffice(false, "School Board", districtType, districtName, numberVotingFor,
				contests, ElectionType.GENERAL);
		final int additionalContestIdx = 1;
		addGroupForRegionalContest("School Board", districtType, districtName, null, numberVotingFor, currentPage,
				stateAbbreviation, votingRegionName, softId, additionalContestIdx, noInputFieldType, radioFieldType,
				multiChoiceFieldType, schoolBoard, false, null, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a special regional candidate contest.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_specialRegionalCandidate() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final int numberVotingFor = 1;
		final long[] softId = new long[numberVotingFor];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.DOMESTIC_VOTER.name());
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest contest = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide",
					null, contests, ElectionType.GENERAL);
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, contest, null, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		final String districtType = "County";
		final String districtName = "County 1";
		final VipContest stateSentator = createContestForOffice(true, "State Senator", districtType, districtName, numberVotingFor,
				contests, ElectionType.PRIMARY, ElectionType.RUNOFF);
		final int additionalContestIdx = numberVotingFor;
		addGroupForRegionalContest("State Senator", districtType, districtName, null, numberVotingFor, currentPage,
				stateAbbreviation, votingRegionName, softId, additionalContestIdx, noInputFieldType, radioFieldType,
				multiChoiceFieldType, stateSentator, true, null, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.PRIMARY, ElectionType.RUNOFF);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a special election for senator.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testPrepareAddOnPage_specialSenator() throws Exception {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final int stepNumber = addStepNumber(currentPage);
		final long[] softId = new long[1];
		softId[0] = AllowedForAddOn.firstFieldId + stepNumber * 1000;
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(validAddress);
		EasyMock.expect(getElectionService().contestOrder(stateAbbreviation, votingRegionName)).andReturn(null);
		final VipElection election = createElection(stateAbbreviation, "STG");
		EasyMock.expect(getElectionService().findElection(stateAbbreviation, votingRegionName)).andReturn(election);
		final List<VipContest> contests = new LinkedList<VipContest>();
		EasyMock.expect(getElectionService().findContests(validAddress)).andReturn(contests);
		final FieldType noInputFieldType = addFieldType(FieldType.TEMPLATE_NOT_INPUT, false);
		final FieldType radioFieldType = addFieldType(FieldType.TEMPLATE_RADIO, false);
		final FieldType multiChoiceFieldType = addFieldType("multiple_checkboxes", true);
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Map<Long, Answer> answers = new HashMap<Long, Answer>();
		EasyMock.expect(wizardResults.getAnswersAsMap()).andReturn(answers).anyTimes();
		EasyMock.expect(wizardResults.getAnswers()).andReturn(answers.values()).anyTimes();
		buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, "election", titleText);
		buildHeaderGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, HEADER_TITLE, HEADER_TEXT,
				answers, wizardResults);
		buildContestsGroup(currentPage, stateAbbreviation, votingRegionName, noInputFieldType, softId, answers, wizardResults);
		final StandardContest[] standardContests = StandardContest.orderedValues();
		for (final StandardContest standardContest : standardContests) {
			final Answer answer = null;
			final VipContest standard;
			final VipContest special;
			if (standardContest == StandardContest.SENATOR) {
				standard = createContestForOffice(false, standardContest.getOffice()[0], "Statewide", "Statewide", null, contests,
						ElectionType.GENERAL);
				special = createContestForOffice(true, standardContest.getOffice()[0], "Statewide", "Statewide", null, contests,
						ElectionType.GENERAL);
			} else {
				standard = null;
				special = null;
			}
			addGroupForStandardContest(standardContest, currentPage, softId, noInputFieldType, radioFieldType,
					multiChoiceFieldType, standard, special, answer, answers, null, PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY, wizardResults, ElectionType.GENERAL);
		}
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		assertTrue("There are no contests left unprocessed", contests.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where the address cannot be matched.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 7, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_unmatchedAddress() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String stateAbbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, stateAbbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(stateAbbreviation, votingRegionName)).andReturn(true);
		final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress);
		EasyMock.expect(getVotingPrecinctService().validateAddress(votingAddress, stateAbbreviation)).andReturn(null);
		getCandidatePageAddon().prepareAddOnPage(wizardContext, currentPage);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where the voting region is not supported.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 7, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_votingRegionNotSupported() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		final QuestionnairePage currentPage = createMock("CurrentPage", QuestionnairePage.class);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(FlowType.FWAB);
		addPageTitle(currentPage, wizardResults);
		final String abbreviation = "AB";
		final String votingRegionName = "Voting Region";
		addVotingRegion(wizardResults, abbreviation, votingRegionName);
		EasyMock.expect(getVotingPrecinctService().isReady(abbreviation, votingRegionName)).andReturn(true);
		EasyMock.expect(getElectionService().isReady(abbreviation, votingRegionName)).andReturn(false);
		getCandidatePageAddon().prepareAddOnPage(wizardContext, currentPage);
		replayAll();
		wizardContext.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(wizardContext, currentPage);

		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final StateContestsPageAddOn createAllowedForAddOn() {
		final StateContestsPageAddOn stateContestsPageAddOn = new StateContestsPageAddOn();
		stateContestsPageAddOn.setValet(getValet());
		stateContestsPageAddOn.setElectionService(getElectionService());
		stateContestsPageAddOn.setVotingPrecinctService(getVotingPrecinctService());
		stateContestsPageAddOn.setQuestionFieldService(getQuestionFieldService());
		stateContestsPageAddOn.setCandidatePageAddon(getCandidatePageAddon());
		stateContestsPageAddOn.setSvrPropertiesService(getSvrPropertiesService());
		return stateContestsPageAddOn;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForAllowedForAddOn() {
		setValet(createMock("Valet", PageAddOnValet.class));
		setElectionService(createMock("ElectionService", ElectionService.class));
		setVotingPrecinctService(createMock("VotingPrecinctService", VotingPrecinctService.class));
		setQuestionFieldService(createMock("QuestionFieldService", QuestionFieldService.class));
		setSvrPropertiesService(createMock("SvrPropertiesService", SvrPropertiesService.class));
		setCandidatePageAddon(createMock("CandidatePageAddon", CandidatePageAddon.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForAllowedForAddOn() {
		setSvrPropertiesService(null);
		setCandidatePageAddon(null);
		setQuestionFieldService(null);
		setVotingPrecinctService(null);
		setElectionService(null);
		setValet(null);
	}

	/**
	 * Adds a field type with the specified template.
	 * 
	 * @author IanBrown
	 * @param template
	 *            the template.
	 * @param multipleChoice
	 *            is this field type multiple choice?
	 * @return the field type.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2012
	 */
	private FieldType addFieldType(final String template, final boolean multipleChoice) {
		final FieldType fieldType;
		if (multipleChoice) {
			fieldType = createMock(template, FieldTypeMultipleSelection.class);
			EasyMock.expect(getValet().acquireMultipleCheckboxesFieldType()).andReturn(fieldType).anyTimes();
		} else {
			fieldType = createMock(template, FieldType.class);
			EasyMock.expect(getQuestionFieldService().findFieldTypeByTemplate(template)).andReturn(fieldType);
		}
		return fieldType;
	}

	/**
	 * Adds a group for the specified custom ballot.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param softId
	 *            the soft identifier - modified on output.
	 * @param additionalContestIdx
	 *            the index for the additional contest.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple choice field type.
	 * @param contest
	 *            the contest.
	 * @param answer
	 *            the answer - may be <code>null</code>.
	 * @param answers
	 *            the answers by field ID.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	private void addGroupForCustomBallot(final QuestionnairePage currentPage, final long[] softId, final int additionalContestIdx,
			final FieldType noInputFieldType, final FieldType radioFieldType, final FieldType multiChoiceFieldType,
			final VipContest contest, final Answer answer, final Map<Long, Answer> answers, final WizardResults wizardResults) {
		final String name = "custom"; // We probably want something to distinguish these!
		final String title = StateContestsPageAddOn.CUSTOM_TITLE;
		final String alternativeGroupName = StateContestsPageAddOn.STANDARD_CUSTOM_GROUP_NAME;
		final String noContestTitle = "";
		final String questionName = "Custom";
		final String inPdfName = ContestPageAddOn.ADDITIONAL_CANDIDATE_PREFIX + additionalContestIdx;
		createGroupForContest(currentPage, softId, noInputFieldType, radioFieldType, multiChoiceFieldType, false, contest, name,
				title, null, alternativeGroupName, noContestTitle, questionName, inPdfName, null, answers, answer, null, null, wizardResults);
	}

	/**
	 * Adds a group for a referendum.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param softId
	 *            the soft identifier.
	 * @param additionalContestIdx
	 *            the index for the additional context.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple choice field type.
	 * @param contest
	 *            the referendum contest.
	 * @param referendumTitle
	 *            the title of the referendum.
	 * @param referendumSubTitle
	 *            the subtitle of the referendum.
	 * @param referendumBrief
	 *            the brief of the referendum.
	 * @param referendumText
	 *            the text of the referendum.
	 * @param effectOfAbstain
	 *            the effect of abstaining.
	 * @param answer
	 *            the answer - may be <code>null</code>.
	 * @param answers
	 *            the answers by ID.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	private void addGroupForReferendum(final QuestionnairePage currentPage, final long[] softId, final long additionalContestIdx,
			final FieldType noInputFieldType, final FieldType radioFieldType, final FieldType multiChoiceFieldType,
			final VipContest contest, final String referendumTitle, final String referendumSubTitle, final String referendumBrief,
			final String referendumText, final String effectOfAbstain, final Answer answer, final Map<Long, Answer> answers,
			final WizardResults wizardResults) {
		final String name = referendumBrief == null ? referendumText : referendumBrief;
		final String title = (referendumTitle + (referendumSubTitle == null ? "" : "<br/>" + referendumSubTitle)).toUpperCase();
		final String alternativeGroupName = StateContestsPageAddOn.STANDARD_REFERENDUM_GROUP_NAME;
		final String noContestTitle = "";
		final String questionName = "Referendum";
		final String inPdfName = ContestPageAddOn.ADDITIONAL_CANDIDATE_PREFIX + additionalContestIdx;
		createGroupForContest(currentPage, softId, noInputFieldType, radioFieldType, multiChoiceFieldType, false, contest, name,
				title, null, alternativeGroupName, noContestTitle, questionName, inPdfName, effectOfAbstain, answers, answer,
				null, null, wizardResults);
	}

	/**
	 * Adds a group for the specified regional contest.
	 * 
	 * @author IanBrown
	 * @param office
	 *            the office of the regional contest.
	 * @param districtType
	 *            the type of district.
	 * @param districtName
	 *            the name of the district.
	 * @param districtNumber
	 *            the number of the district.
	 * @param numberVotingFor
	 *            the number of candidates to vote for.
	 * @param currentPage
	 *            the current page.
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param softId
	 *            the soft identifier.
	 * @param additionalContestIdx
	 *            the index for the additional context.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio button field type.
	 * @param multiChoiceFieldType
	 *            the multiple choice field type.
	 * @param contest
	 *            the contest.
	 * @param special
	 *            <code>true</code> for a special election.
	 * @param answer
	 *            the answer for the question (may be <code>null</code>).
	 * @param answers
	 *            the answers by ID.
	 * @param partisanParty the partisan party.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param wizardResults
	 *            the wizard results.
	 * @param electionTypes
	 *            the types of elections.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	private void addGroupForRegionalContest(final String office, final String districtType, final String districtName,
			final Integer districtNumber, final Integer numberVotingFor, final QuestionnairePage currentPage,
			final String stateAbbreviation, final String votingRegionName, final long[] softId, final int additionalContestIdx,
			final FieldType noInputFieldType, final FieldType radioFieldType, final FieldType multiChoiceFieldType,
			final VipContest contest, final boolean special, final Answer answer, final Map<Long, Answer> answers,
			final String partisanParty,
			final String noPartyName, final WizardResults wizardResults, final ElectionType... electionTypes) {
		final String type = buildElectionTypeString(electionTypes);
		final String elect = numberVotingFor != null && numberVotingFor > 1 ? "VOTE FOR UP TO " + numberVotingFor : "VOTE FOR ONE";
		final String district = buildDistrictString(stateAbbreviation, votingRegionName, districtType, districtName, districtNumber);
		final String title = StateContestsPageAddOn.buildOfficeTitle(office, special, type, district, elect);
		final String alternativeGroupName = "No contest for office of " + office.toLowerCase();
		createGroupForOffice(currentPage, softId, additionalContestIdx, noInputFieldType, radioFieldType, multiChoiceFieldType,
				false, contest, title, special, office, null, alternativeGroupName, type, numberVotingFor, answer, answers, partisanParty, noPartyName, wizardResults);
	}

	/**
	 * Adds a group for a standard contest.
	 * 
	 * @author IanBrown
	 * @param standardContest
	 *            the standard contest.
	 * @param currentPage
	 *            the current page.
	 * @param softId
	 *            the soft identifier - may change on output.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple choice field type.
	 * @param contest
	 *            the contest - may be <code>null</code>.
	 * @param specialContest
	 *            the special contest - may be <code>null</code>.
	 * @param answer
	 *            the answer to the question - may be <code>null</code>
	 * @param answers
	 *            the answers by ID.
	 * @param partisanParty the partisan party.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param wizardResults
	 *            the wizard results.
	 * @param electionTypes
	 *            the types of the election.
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	private void addGroupForStandardContest(final StandardContest standardContest, final QuestionnairePage currentPage,
			final long[] softId, final FieldType noInputFieldType, final FieldType radioFieldType,
			final FieldType multiChoiceFieldType, final VipContest contest, final VipContest specialContest, final Answer answer,
			final Map<Long, Answer> answers,
			final String partisanParty, final String noPartyName, 
			final WizardResults wizardResults, final ElectionType... electionTypes) {
		final String officeTitle = standardContest.getOffice()[0].toUpperCase();
		final String office = standardContest.getOffice()[0];
		final String alternativeGroupName = standardContest.getAlternativeGroupName();
		final String type = buildElectionTypeString(electionTypes);
		String title = contest == null && specialContest == null ? officeTitle : StateContestsPageAddOn
				.buildOfficeTitle(officeTitle, false, type, null,
						standardContest == StandardContest.PRESIDENT ? "VOTE FOR ONE TEAM" : "VOTE FOR ONE");
		createGroupForOffice(currentPage, softId, null, noInputFieldType, radioFieldType, multiChoiceFieldType, true, contest,
				title, false, office, standardContest.getInPdfName(), alternativeGroupName, type, null, answer, answers, partisanParty, noPartyName, wizardResults);
		if (specialContest != null) {
			title = StateContestsPageAddOn.buildOfficeTitle(officeTitle, true, type, null,
					standardContest == StandardContest.PRESIDENT ? "VOTE FOR ONE TEAM" : "VOTE FOR ONE");
			createGroupForOffice(currentPage, softId, null, noInputFieldType, radioFieldType, multiChoiceFieldType, true,
					specialContest, title, true, office, standardContest.getInPdfName(), alternativeGroupName, type, null, answer, answers, partisanParty, noPartyName, wizardResults);
		}
	}

	/**
	 * Adds radio options for the specified contest.
	 * 
	 * @author IanBrown
	 * @param standard
	 *            is this a standard contest?
	 * @param name
	 *            the name of the contest.
	 * @param field
	 *            the field.
	 * @param contest
	 *            the contest.
	 * @param partisanParty the partisan party.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2013
	 */
	private void addOptionsForContest
		(final boolean standard, final String name, 
				final QuestionField field,
				final VipContest contest, 
				final String partisanParty, String noPartyName) {
		if (contest != null) {
			final FieldDictionaryItem option = createMock(makeIdentifier(name) + "Option", FieldDictionaryItem.class);
			final Collection<FieldDictionaryItem> options = Arrays.asList(option);
			EasyMock.expect(getValet().createOptions(EasyMock.eq(standard), EasyMock.same(contest), EasyMock.eq(partisanParty), EasyMock.eq(noPartyName))).andReturn(options);
			field.setGenericOptions(options);
		}
	}

	/**
	 * Adds the page title.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private void addPageTitle(final QuestionnairePage currentPage, final WizardResults wizardResults) {
		final String pageTitle = "Page Title";
		EasyMock.expect(currentPage.getTitle()).andReturn(pageTitle);
		wizardResults.setCurrentPageTitle(pageTitle);
	}

	/**
	 * Adds the step number to the current page.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
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
	 *            the wizard results.
	 * @param votingState
	 *            the voting state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the voting state.
	 * @since Aug 7, 2012
	 * @version Oct 20, 2012
	 */
	private void addVotingRegion(final WizardResults wizardResults, final String votingState, final String votingRegionName) {
		EasyMock.expect(wizardResults.getVotingRegionName()).andReturn(votingRegionName).atLeastOnce();
		EasyMock.expect(wizardResults.getVotingRegionState()).andReturn(votingState).atLeastOnce();
	}

	/**
	 * Builds the group used to display the contests information.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param softId
	 *            the soft identifier - may be changed on output.
	 * @param answers
	 *            the map of answers by field ID.
	 * @param wizardResults
	 *            the wizard results.
	 * @return the candidates group.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2013
	 */
	private Question buildContestsGroup(final QuestionnairePage currentPage, final String stateAbbreviation,
			final String votingRegionName, final FieldType noInputFieldType, final long[] softId, final Map<Long, Answer> answers,
			final WizardResults wizardResults) {
		final String contestsGroupTitle = singlelineProperty(stateAbbreviation, votingRegionName,
				StateContestsPageAddOn.CONTESTS_TITLE_PROPERTY, null, StateContestsPageAddOn.CONTESTS_GROUP_TITLE);
		final Question contestsGroup = createGroup(currentPage, "ContestsGroup", softId,
				StateContestsPageAddOn.CONTESTS_GROUP_NAME, contestsGroupTitle);
		final QuestionVariant contestsVariant = createVariant(contestsGroup, "ContestsVariant", softId);
		multilineProperty(stateAbbreviation, votingRegionName, StateContestsPageAddOn.CONTESTS_TEXT_PROPERTY, null,
				StateContestsPageAddOn.CONTESTS_TEXT);
		createField(contestsVariant, "ContestsField", softId, noInputFieldType, StateContestsPageAddOn.CONTESTS_FIELD_TITLE, null,
				answers, false, wizardResults);
		return contestsGroup;
	}

	/**
	 * Builds a string for the specified district.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param districtType
	 *            the type of district.
	 * @param districtName
	 *            the name of the district.
	 * @param districtNumber
	 *            the number of the district.
	 * @return the district string.
	 * @since Oct 23, 2012
	 * @version Oct 29, 2012
	 */
	private String buildDistrictString(final String stateAbbreviation, final String votingRegionName, final String districtType,
			final String districtName, final Integer districtNumber) {
		if (!StateContestsPageAddOn.SHOW_DISTRICT) {
			return null;
		}

		final String districtFormat;
		if ("STATEWIDE".equalsIgnoreCase(districtType)) {
			districtFormat = singlelineProperty(stateAbbreviation, votingRegionName,
					StateContestsPageAddOn.STATEWIDE_DISTRICT_PROPERTY, null, StateContestsPageAddOn.STATEWIDE_DISTRICT_FORMAT);
		} else {
			districtFormat = singlelineProperty(stateAbbreviation, votingRegionName,
					StateContestsPageAddOn.REGIONAL_DISTRICT_PROPERTY, null, StateContestsPageAddOn.REGIONAL_DISTRICT_FORMAT);
		}

		final String district = MessageFormat.format(districtFormat, districtType, districtName, districtNumber);
		return district;
	}

	/**
	 * Builds the type string for the specified election types.
	 * 
	 * @author IanBrown
	 * @param electionTypes
	 *            the election types.
	 * @return the election type string.
	 * @since Aug 8, 2012
	 * @version Oct 20, 2012
	 */
	private String buildElectionTypeString(final ElectionType... electionTypes) {
		final StringBuilder typeBuilder = new StringBuilder();
		String prefix = "";
		for (final ElectionType electionType : electionTypes) {
			typeBuilder.append(prefix).append(electionType.getType().toUpperCase());
			prefix = " ";
		}
		final String type = typeBuilder.toString();
		return type;
	}

	/**
	 * Builds the header group that always shows up at the top of the page.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param softId
	 *            the soft identifier - may be updated.
	 * @param headerTitle
	 *            the header title.
	 * @param headerText
	 *            the text lines for the header.
	 * @param answers
	 *            the map of answers by field ID.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Aug 7, 2012
	 * @version Oct 29, 2012
	 */
	private void buildHeaderGroup(final QuestionnairePage currentPage, final String stateAbbreviation,
			final String votingRegionName, final FieldType noInputFieldType, final long[] softId, final String headerTitle,
			final String[] headerText, final Map<Long, Answer> answers, final WizardResults wizardResults) {
		final String headerLine = singlelineProperty(stateAbbreviation, votingRegionName,
				StateContestsPageAddOn.HEADER_TITLE_PROPERTY, headerTitle, StateContestsPageAddOn.HEADER_GROUP_TITLE);
		final Question headerGroup = createGroup(currentPage, "HeaderGroup", softId, StateContestsPageAddOn.HEADER_GROUP_NAME,
				headerLine);
		final QuestionVariant headerVariant = createVariant(headerGroup, "HeaderVariant", softId);
		multilineProperty(stateAbbreviation, votingRegionName, StateContestsPageAddOn.HEADER_TEXT_PROPERTY, headerText,
				StateContestsPageAddOn.HEADER_TEXT);
		createField(headerVariant, "HeaderField", softId, noInputFieldType, StateContestsPageAddOn.HEADER_FIELD_TITLE, null,
				answers, false, wizardResults);
	}
	

	/**
	 * Builds an election group.
	 * @param currentPage the current page.
	 * @param stateAbbreviation the state abbreviation.
	 * @param votingRegionName the voting region name.
	 * @param noInputFieldType the no-input field type.
	 * @param softId the soft identifier.
	 * @param title the title of the group.
	 * @param text the text for the group.
	 */
	private void buildElectionGroup(final QuestionnairePage currentPage,
            final String stateAbbreviation, final String votingRegionName,
            final FieldType noInputFieldType, final long[] softId, final String title,
            final String text) {
		final Question electionGroup = createGroup(currentPage, "ElectionGroup", softId, StateContestsPageAddOn.ELECTION_GROUP_NAME,
				text);
		@SuppressWarnings("unused")
        final QuestionVariant electionVariant = createVariant(electionGroup, "ElectionVariant", softId);
    }


	/**
	 * Creates a contest for a custom ballot.
	 * 
	 * @author IanBrown
	 * @param contests
	 *            the contests.
	 * @param customBallotDistrictType
	 *            the district type for the custom ballot.
	 * @return the contest.
	 * @since Aug 9, 2012
	 * @version May 8, 2013
	 */
	private VipContest createContestForCustomBallot(final List<VipContest> contests, final String customBallotDistrictType) {
		final VipContest contest = createMock("JudgeRetention", VipContest.class);
		EasyMock.expect(contest.getType()).andReturn("judge retention").anyTimes();
		EasyMock.expect(contest.isPartisan()).andReturn(false).anyTimes();
		EasyMock.expect(contest.getNumberVotingFor()).andReturn(null).anyTimes();
		final VipBallot ballot = createMock("JudgeRetentionBallot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot).anyTimes();
		EasyMock.expect(ballot.getCandidates()).andReturn(null).anyTimes();
		EasyMock.expect(ballot.getReferendum()).andReturn(null).anyTimes();
		final VipCustomBallot customBallot = createMock("JudgeRetentionCustomBallot", VipCustomBallot.class);
		EasyMock.expect(ballot.getCustomBallot()).andReturn(customBallot).anyTimes();
		EasyMock.expect(customBallot.getHeading()).andReturn("Custom Ballot Heading").anyTimes();
		final VipElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", VipElectoralDistrict.class);
		EasyMock.expect(contest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		EasyMock.expect(electoralDistrict.getType()).andReturn(customBallotDistrictType).anyTimes();
		contests.add(contest);
		return contest;
	}

	/**
	 * Creates a contest for the specified office of the specified types.
	 * 
	 * @author IanBrown
	 * @param special
	 *            <code>true</code> for a special election.
	 * @param office
	 *            the office.
	 * @param districtType
	 *            the type of district.
	 * @param districtName
	 *            the name of the district.
	 * @param numberVotingFor
	 *            the number of candidates to vote for.
	 * @param contests
	 *            the contests.
	 * @param electionTypes
	 *            the types of elections.
	 * @return the contest.
	 * @since Aug 8, 2012
	 * @version May 8, 2013
	 */
	private VipContest createContestForOffice(final boolean special, final String office, final String districtType,
			final String districtName, final Integer numberVotingFor, final List<VipContest> contests,
			final ElectionType... electionTypes) {
		final String contestName = makeIdentifier(office);
		final VipContest contest = createMock((special ? "Special" : "") + contestName, VipContest.class);
		final String type = buildElectionTypeString(electionTypes);
		EasyMock.expect(contest.getType()).andReturn(type).anyTimes();
		EasyMock.expect(contest.isPartisan()).andReturn(false).anyTimes();
		EasyMock.expect(contest.isSpecial()).andReturn(special).anyTimes();
		EasyMock.expect(contest.getOffice()).andReturn(office).anyTimes();
		EasyMock.expect(contest.getNumberVotingFor()).andReturn(numberVotingFor).anyTimes();
		final VipElectoralDistrict electoralDistrict = createMock(makeIdentifier(districtName), VipElectoralDistrict.class);
		EasyMock.expect(contest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		EasyMock.expect(electoralDistrict.getName()).andReturn(districtName).anyTimes();
		EasyMock.expect(electoralDistrict.getType()).andReturn(districtType).anyTimes();
		EasyMock.expect(electoralDistrict.getNumber()).andReturn(null).anyTimes();
		final VipBallot ballot = createMock(contestName + "Ballot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot).anyTimes();
		final VipBallotCandidate candidate = createMock("Candidate", VipBallotCandidate.class);
		final List<VipBallotCandidate> candidates = Arrays.asList(candidate);
		EasyMock.expect(ballot.getCandidates()).andReturn(candidates).anyTimes();
		EasyMock.expect(ballot.getReferendum()).andReturn(null).anyTimes();
		EasyMock.expect(ballot.getCustomBallot()).andReturn(null).anyTimes();
		contests.add(contest);
		return contest;
	}

	/**
	 * Creates a contest for a referendum.
	 * 
	 * @author IanBrown
	 * @param contests
	 *            the contests.
	 * @param referendumDistrictType
	 *            the type of referendum district.
	 * @param title
	 *            the title of the referendum.
	 * @param subTitle
	 *            the sub-title of the referendum.
	 * @param brief
	 *            the brief text for the referendum.
	 * @param text
	 *            the full text for the referendum.
	 * @param effectOfAbstain
	 *            the effect of abstaining.
	 * @return the contest.
	 * @since Aug 9, 2012
	 * @version May 8, 2013
	 */
	private VipContest createContestForReferendum(final List<VipContest> contests, final String referendumDistrictType,
			final String title, final String subTitle, final String brief, final String text, final String effectOfAbstain) {
		final VipContest contest = createMock("ReferendumContest", VipContest.class);
		EasyMock.expect(contest.getOffice()).andReturn(null).anyTimes();
		EasyMock.expect(contest.getType()).andReturn("referendum").anyTimes();
		EasyMock.expect(contest.isPartisan()).andReturn(false).anyTimes();
		final VipBallot ballot = createMock("ReferendumBallot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot).anyTimes();
		EasyMock.expect(ballot.getCandidates()).andReturn(null).anyTimes();
		final VipReferendum referendum = createMock("Referendum", VipReferendum.class);
		EasyMock.expect(ballot.getReferendum()).andReturn(referendum).anyTimes();
		EasyMock.expect(ballot.getCustomBallot()).andReturn(null).anyTimes();
		EasyMock.expect(referendum.getTitle()).andReturn(title).anyTimes();
		EasyMock.expect(referendum.getSubTitle()).andReturn(subTitle).anyTimes();
		EasyMock.expect(referendum.getBrief()).andReturn(brief).anyTimes();
		EasyMock.expect(referendum.getText()).andReturn(text).anyTimes();
		final long referendumVipId = effectOfAbstain == null ? 1l : effectOfAbstain.hashCode();
		EasyMock.expect(referendum.getVipId()).andReturn(referendumVipId).anyTimes();
		final VipReferendumDetail referendumDetail = createMock("ReferendumDetail", VipReferendumDetail.class);
		EasyMock.expect(
				getElectionService().findReferendumDetail(EasyMock.<String> anyObject(), EasyMock.<String> anyObject(),
						EasyMock.eq(referendumVipId))).andReturn(referendumDetail).anyTimes();
		EasyMock.expect(referendumDetail.getEffectOfAbstain()).andReturn(effectOfAbstain).anyTimes();
		EasyMock.expect(contest.getNumberVotingFor()).andReturn(null).anyTimes();
		final VipElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", VipElectoralDistrict.class);
		EasyMock.expect(contest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		EasyMock.expect(electoralDistrict.getType()).andReturn(referendumDistrictType).anyTimes();
		contests.add(contest);
		return contest;
	}

	/**
	 * Creates a group for the contest.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param softId
	 *            the soft identifier - changes on output.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple choice field type.
	 * @param standard
	 *            is this a standard
	 * @param contest
	 *            the contest.
	 * @param name
	 *            the name of the contest.
	 * @param title
	 *            the title of the contest.
	 * @param numberVotingFor
	 *            the number of candidates that can be chosen.
	 * @param alternativeGroupName
	 *            the alternative name for the group.
	 * @param noContestTitle
	 *            the title for the case where there is no contest.
	 * @param questionName
	 *            the name of the question.
	 * @param inPdfName
	 *            the name in the PDF file.
	 * @param additionalInfo
	 *            additional information to be displayed in a no-input field.
	 * @param answers
	 *            the answers by field ID.
	 * @param answer
	 *            the answer - may be <code>null</code>.
	 * @param partisanParty the partisan party.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	private void createGroupForContest(final QuestionnairePage currentPage, final long[] softId, final FieldType noInputFieldType,
			final FieldType radioFieldType, final FieldType multiChoiceFieldType, final boolean standard, final VipContest contest,
			final String name, final String title, final Integer numberVotingFor, final String alternativeGroupName,
			final String noContestTitle, final String questionName, final String inPdfName, final String additionalInfo,
			final Map<Long, Answer> answers, final Answer answer,
			final String partisanParty, final String noPartyName, final WizardResults wizardResults) {
		final Question group = createGroup(currentPage, name + "Group", softId, alternativeGroupName, title);
		final QuestionVariant variant = createVariant(group, name + "Variant", softId);
		QuestionField field;
		if (contest == null) {
			field = createField(variant, name + "Field", softId, noInputFieldType, noContestTitle, null, answers, false,
					wizardResults);
		} else {
			field = createField(variant, name + "Field", softId, numberVotingFor == null || numberVotingFor == 1 ? radioFieldType
					: multiChoiceFieldType, questionName, answer, answers, true, wizardResults);
			if (numberVotingFor != null && numberVotingFor > 1) {
				field.setVerificationPattern(numberVotingFor.toString());
				softId[0] += numberVotingFor - 1;
			}
			field.setRequired(false);
			field.setSecurity(true);
			field.setInPdfName(EasyMock.anyObject(String.class)  /*inPdfName*/);
		}
		addOptionsForContest(standard, name, field, contest, partisanParty, noPartyName);
		if (additionalInfo != null) {
			final QuestionField additionalField = createMock("AdditionalField", QuestionField.class);
			EasyMock.expect(
					getValet().createField(EasyMock.same(variant), EasyMock.eq(softId[0]), EasyMock.same(noInputFieldType),
							EasyMock.eq(additionalInfo), EasyMock.<String> eq(null))).andReturn(additionalField);
			++softId[0];
		}
	}

	/**
	 * Creates a group for an office.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param softId
	 *            the soft identifier - changed on output.
	 * @param additionalContestIdx
	 *            the index for an additional contest.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple choice field type.
	 * @param standard
	 *            is this a standard contest?
	 * @param contest
	 *            the contest.
	 * @param title
	 *            the title of the contest.
	 * @param special
	 *            <code>true</code> if this is a special office.
	 * @param office
	 *            the office of the contest.
	 * @param standardInPdfName the standard name in the PDF.
	 * @param alternativeGroupName
	 *            the alternate name for the group.
	 * @param type
	 *            the type of election.
	 * @param numberVotingFor
	 *            the number of candidates being voting for.
	 * @param answer
	 *            the answer to the question.
	 * @param answers
	 *            the answers by field ID.
	 * @param partisanParty the partisan party.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param wizardResults
	 *            the wizard results.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	private void createGroupForOffice(final QuestionnairePage currentPage, final long[] softId, final Integer additionalContestIdx,
			final FieldType noInputFieldType, final FieldType radioFieldType, final FieldType multiChoiceFieldType,
			final boolean standard, final VipContest contest, final String title, final boolean special, final String office,
			String standardInPdfName, final String alternativeGroupName, final String type, final Integer numberVotingFor,
			final Answer answer, final Map<Long, Answer> answers, 
			final String partisanParty, 
			String noPartyName, final WizardResults wizardResults) {
		final String noContestTitle = StateContestsPageAddOn.NO_OFFICE_CONTEST_TITLE + office;
		final String questionName;
		final String inPdfName;
		if (type.equalsIgnoreCase("GENERAL")) {
			questionName = office + " (" + (special ? "Special " : "") + " election)";
			inPdfName = additionalContestIdx == null ? standardInPdfName + (special ? "_special" : "")
					: ContestPageAddOn.ADDITIONAL_CANDIDATE_PREFIX + additionalContestIdx;
		} else {
			questionName = office + " (" + (special ? "Special " : "") + type + " election)";
			inPdfName = additionalContestIdx == null ? (standardInPdfName + "_" + (special ? "special_" : "") + type
					.toLowerCase()).replace(' ', '_') : ContestPageAddOn.ADDITIONAL_CANDIDATE_PREFIX + additionalContestIdx;
		}
		createGroupForContest(currentPage, softId, noInputFieldType, radioFieldType, multiChoiceFieldType, standard, contest,
				(special ? "Special" : "") + office, title, numberVotingFor, alternativeGroupName, noContestTitle, questionName,
				inPdfName, null, answers, answer, partisanParty, noPartyName, wizardResults);
	}

	/**
	 * Creates a state for the specified abbreviation.
	 * 
	 * @author IanBrown
	 * @param abbreviation
	 *            the abbreviation.
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
	 * Gets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @return the candidate page add on.
	 * @since Aug 27, 2012
	 * @version Aug 27, 2012
	 */
	private CandidatePageAddon getCandidatePageAddon() {
		return candidatePageAddon;
	}

	/**
	 * Gets the election service.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the question field service.
	 * 
	 * @author IanBrown
	 * @return the question field service.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Sets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @param candidatePageAddon
	 *            the candidate page add on to set.
	 * @since Aug 27, 2012
	 * @version Aug 27, 2012
	 */
	private void setCandidatePageAddon(final CandidatePageAddon candidatePageAddon) {
		this.candidatePageAddon = candidatePageAddon;
	}

	/**
	 * Sets the election service.
	 * 
	 * @author IanBrown
	 * @param electionService
	 *            the election service to set.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Sets the question field service.
	 * 
	 * @author IanBrown
	 * @param questionFieldService
	 *            the question field service to set.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private void setQuestionFieldService(final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

}
