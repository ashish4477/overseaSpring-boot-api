/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.Election;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.questionnaire.AddOnPage;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.LocalOfficialService;

/**
 * Extended {@link AbstractFieldFillerPageAddOnCheck} test for {@link SvidPageAddOn}.
 * 
 * @author IanBrown
 * 
 * @since Apr 24, 2012
 * @version Jul 11, 2012
 */
public final class SvidPageAddOnTest extends AbstractFieldFillerPageAddOnCheck<SvidPageAddOn> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * \ for the case where no input fields are used and there are the correct number of elections for the field index.
	 * 
	 * @author IanBrown
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testPrepageAddOnPage_noInput() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		final Election election = createMock("Election", Election.class);
		final Collection elections = Arrays.asList(election);
		EasyMock.expect(svid.getElections()).andReturn(elections).anyTimes();
		EasyMock.expect(field.getTitle()).andReturn(SvidPageAddOn.NO_INPUT_ELECTION_VARIABLE + " 1");
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_NOT_INPUT).anyTimes();
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.YEAR, 1);
		final String heldOn = SvidPageAddOn.SUPPORTED_DATE_FORMATS[0].format(calendar.getTime());
		EasyMock.expect(election.getHeldOn()).andReturn(heldOn).anyTimes();
		final String electionTitle = "Election Title";
		EasyMock.expect(election.getTitle()).andReturn(electionTitle).anyTimes();
		field.setHelpText(EasyMock.eq(electionTitle + " held on " + heldOn));
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * \ for the case where no input fields are used and there are more elections than are needed for the field index.
	 * 
	 * @author IanBrown
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testPrepageAddOnPage_noInputExtraElections() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		final Election previousElection = createMock("PreviousElection", Election.class);
		final Election election = createMock("Election", Election.class);
		final Election nextElection = createMock("NextElection", Election.class);
		final Collection elections = Arrays.asList(previousElection, election,
				nextElection);
		EasyMock.expect(svid.getElections()).andReturn(elections).anyTimes();
		EasyMock.expect(field.getTitle()).andReturn(SvidPageAddOn.NO_INPUT_ELECTION_VARIABLE + " 2");
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_NOT_INPUT).anyTimes();
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.YEAR, 1);
		final String heldOn = SvidPageAddOn.SUPPORTED_DATE_FORMATS[0].format(calendar.getTime());
		EasyMock.expect(previousElection.getHeldOn()).andReturn(heldOn).anyTimes();
		EasyMock.expect(election.getHeldOn()).andReturn(heldOn).anyTimes();
		EasyMock.expect(nextElection.getHeldOn()).andReturn(heldOn).anyTimes();
		final String electionTitle = "Election Title";
		EasyMock.expect(election.getTitle()).andReturn(electionTitle).anyTimes();
		field.setHelpText(EasyMock.eq(electionTitle + " held on " + heldOn));
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * \ for the case where no input fields are used and there are too few elections for the field index.
	 * 
	 * @author IanBrown
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testPrepageAddOnPage_noInputTooFewElections() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		final Election election = createMock("Election", Election.class);
		final Collection elections = Arrays.asList(election);
		EasyMock.expect(svid.getElections()).andReturn(elections).anyTimes();
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.YEAR, 1);
		final String heldOn = SvidPageAddOn.SUPPORTED_DATE_FORMATS[0].format(calendar.getTime());
		EasyMock.expect(election.getHeldOn()).andReturn(heldOn).anyTimes();
		EasyMock.expect(field.getTitle()).andReturn(SvidPageAddOn.NO_INPUT_ELECTION_VARIABLE + " 2");
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_NOT_INPUT).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are no elections.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_noElections() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		EasyMock.expect(svid.getElections()).andReturn(null).anyTimes();
		EasyMock.expect(field.getTitle()).andReturn("Not the expected title").anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is no SVID.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_noSVID() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(null).anyTimes();
		final String titlePrefix = "Prefix";
		final String titleSuffix = "Postfix";
		EasyMock.expect(field.getTitle()).andReturn(titlePrefix + " " + SvidPageAddOn.ELECTION_VARIABLE + " " + titleSuffix)
				.anyTimes();
		field.setTitle(titlePrefix + " " + titleSuffix);
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_TEXT).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are no up-coming elections.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testPrepareAddOnPage_noUpComingElections() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		final Election election = createMock("Election", Election.class);
		final Collection elections = Arrays.asList(election);
		EasyMock.expect(svid.getElections()).andReturn(elections).anyTimes();
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.YEAR, -1);
		final String heldOn = SvidPageAddOn.SUPPORTED_DATE_FORMATS[0].format(calendar.getTime());
		EasyMock.expect(election.getHeldOn()).andReturn(heldOn).anyTimes();
		final String titlePrefix = "Prefix";
		final String titleSuffix = "Postfix";
		EasyMock.expect(field.getTitle()).andReturn(titlePrefix + " " + SvidPageAddOn.ELECTION_VARIABLE + " " + titleSuffix)
				.anyTimes();
		field.setTitle(titlePrefix + " " + titleSuffix);
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_RADIO).anyTimes();
		EasyMock.expect(fieldType.isGenericOptionsAllowed()).andReturn(false).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is no voting region set.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testPrepareAddOnPage_noVotingRegion() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(null).anyTimes();
		final String titlePrefix = "Prefix";
		final String titleSuffix = "Postfix";
		EasyMock.expect(field.getTitle()).andReturn(titlePrefix + " " + SvidPageAddOn.ELECTION_VARIABLE + " " + titleSuffix)
				.anyTimes();
		field.setTitle(titlePrefix + " " + titleSuffix);
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_RADIO).anyTimes();
		EasyMock.expect(fieldType.isGenericOptionsAllowed()).andReturn(true).anyTimes();
		field.setGenericOptions((Collection<FieldDictionaryItem>) EasyMock.anyObject());
		final Collection<FieldDictionaryItem> genericOptions = new LinkedList<FieldDictionaryItem>();
		EasyMock.expect(field.getGenericOptions()).andReturn(genericOptions).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		assertEquals("There is one generic option", 1, genericOptions.size());
		final FieldDictionaryItem genericOption = genericOptions.iterator().next();
		assertEquals("The generic option has the correct value", SvidPageAddOn.NO_UPCOMING_ELECTION, genericOption.getValue());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is an election whose date cannot be determined.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testPrepareAddOnPage_unrecognizedHeldOnDate() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		final Election election = createMock("Election", Election.class);
		final Collection elections = Arrays.asList(election);
		EasyMock.expect(svid.getElections()).andReturn(elections).anyTimes();
		final String heldOn = "Unrecognizable";
		EasyMock.expect(election.getHeldOn()).andReturn(heldOn).anyTimes();
		final String titlePrefix = "Prefix";
		final String titleSuffix = "Postfix";
		EasyMock.expect(field.getTitle()).andReturn(titlePrefix + " " + SvidPageAddOn.ELECTION_VARIABLE + " " + titleSuffix)
				.anyTimes();
		final String electionTitle = "Election Title";
		EasyMock.expect(election.getTitle()).andReturn(electionTitle).anyTimes();
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_SELECT).anyTimes();
		EasyMock.expect(fieldType.isGenericOptionsAllowed()).andReturn(true).anyTimes();
		field.setTitle(titlePrefix + " " + titleSuffix);
		field.setGenericOptions((Collection<FieldDictionaryItem>) EasyMock.anyObject());
		final Collection<FieldDictionaryItem> genericOptions = new LinkedList<FieldDictionaryItem>();
		EasyMock.expect(field.getGenericOptions()).andReturn(genericOptions).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		assertEquals("There is one option", 1, genericOptions.size());
		final FieldDictionaryItem genericOption = genericOptions.iterator().next();
		assertEquals("The generic option has the correct value", electionTitle + " held on " + heldOn, genericOption.getValue());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are up-coming elections.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testPrepareAddOnPage_upComingElections() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final State state = createMock("State", State.class);
		EasyMock.expect(votingRegion.getState()).andReturn(state).anyTimes();
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialService().findSvidForState(state)).andReturn(svid).anyTimes();
		final Election election = createMock("Election", Election.class);
		final Collection elections = Arrays.asList(election);
		EasyMock.expect(svid.getElections()).andReturn(elections).anyTimes();
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.YEAR, 1);
		final String heldOn = SvidPageAddOn.SUPPORTED_DATE_FORMATS[0].format(calendar.getTime());
		EasyMock.expect(election.getHeldOn()).andReturn(heldOn).anyTimes();
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(fieldType.getTemplateName()).andReturn(FieldType.TEMPLATE_RADIO).anyTimes();
		EasyMock.expect(fieldType.isGenericOptionsAllowed()).andReturn(true).anyTimes();
		final String titlePrefix = "Prefix";
		final String titleSuffix = "Postfix";
		EasyMock.expect(field.getTitle()).andReturn(titlePrefix + " " + SvidPageAddOn.ELECTION_VARIABLE + " " + titleSuffix)
				.anyTimes();
		final String electionTitle = "Election Title";
		EasyMock.expect(election.getTitle()).andReturn(electionTitle).anyTimes();
		field.setTitle(titlePrefix + " " + titleSuffix);
		field.setGenericOptions((Collection<FieldDictionaryItem>) EasyMock.anyObject());
		final Collection<FieldDictionaryItem> genericOptions = new LinkedList<FieldDictionaryItem>();
		EasyMock.expect(field.getGenericOptions()).andReturn(genericOptions).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		assertEquals("There is one option", 1, genericOptions.size());
		final FieldDictionaryItem genericOption = genericOptions.iterator().next();
		assertEquals("The generic option has the correct value", electionTitle + " held on " + heldOn, genericOption.getValue());
	}

	/** {@inheritDoc} */
	@Override
	protected final SvidPageAddOn createAddOn() {
		final SvidPageAddOn svidPageAddOn = new SvidPageAddOn();
		svidPageAddOn.setLocalOfficialService(getLocalOfficialService());
		return svidPageAddOn;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForAddOn() {
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForAddOn() {
		setLocalOfficialService(null);
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}
}
