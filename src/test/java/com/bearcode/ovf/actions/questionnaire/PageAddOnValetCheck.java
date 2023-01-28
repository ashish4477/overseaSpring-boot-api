/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.vip.*;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Abstract test for implementations of {@link PageAddOnValet}.
 * 
 * @author IanBrown
 * 
 * @param <V>
 *            the type of state contests page add on valet to test.
 * @since Aug 8, 2012
 * @version Oct 1, 2013
 */
public abstract class PageAddOnValetCheck<V extends PageAddOnValet> extends EasyMockSupport {

	/**
	 * the valet to test.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private V valet;

	/**
	 * Sets up the page add on valet to test.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version May 7, 2013
	 */
	@Before
	public final void setUpValet() {
		setUpForValet();
		setValet(createValet());
	}

	/**
	 * Tears down the page add on valet after testing.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version May 7, 2013
	 */
	@After
	public final void tearDownValet() {
		setValet(null);
		tearDownForValet();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#acquireMultipleCheckboxesFieldType()}.
	 * 
	 * @author IanBrown
	 * @since Oct 1, 2012
	 * @version Oct 1, 2012
	 */
	@Test
	public final void testAcquireMultipleCheckboxesFieldType() {
		final FieldType actualFieldType = getValet().acquireMultipleCheckboxesFieldType();

		assertNotNull("A field type is returned", actualFieldType);
		assertTrue("The field type is a multiple selection field type", actualFieldType instanceof FieldTypeMultipleSelection);
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createField(com.bearcode.ovf.model.questionnaire.QuestionVariant, java.lang.Long, com.bearcode.ovf.model.questionnaire.FieldType, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	@Test
	public final void testCreateField() {
		final QuestionVariant variant = createMock("Variant",
		        QuestionVariant.class);
		final Long id = 129892l;
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		final String title = "Field Title";
		final String helpText = "Field help text";
		final Collection<QuestionField> fields = new LinkedList<QuestionField>();
		EasyMock.expect(variant.getFields()).andReturn(fields);
		replayAll();

		final QuestionField actualField = getValet().createField(variant, id,
		        fieldType, title, helpText);

		assertNotNull("A question field is created", actualField);
		assertSame("The question field belongs to the variant", variant,
		        actualField.getQuestion());
		assertTrue("The variant contains the question field",
		        fields.contains(actualField));
		assertEquals("The identifier is set", id.longValue(),
		        actualField.getId());
		assertSame("The field type is set", fieldType, actualField.getType());
		assertEquals("The title is set", title, actualField.getTitle());
		assertEquals("The help text is set", helpText,
		        actualField.getHelpText());
		assertEquals("The order is set", 1, actualField.getOrder());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createGroup(com.bearcode.ovf.model.questionnaire.QuestionnairePage, java.lang.Long, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	@Test
	public final void testCreateGroup() {
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final Long id = 8671298l;
		final String name = "Field name";
		final String title = "Field title";
		final List<Question> questions = new LinkedList<Question>();
		EasyMock.expect(page.getQuestions()).andReturn(questions);
		replayAll();

		final Question actualGroup = getValet().createGroup(page, id, name, title);

		assertNotNull("A group is created", actualGroup);
		assertSame("The group belongs to the page", page, actualGroup.getPage());
		assertTrue("The page contains the group", questions.contains(actualGroup));
		assertEquals("The group identifier is set", id.longValue(), actualGroup.getId());
		assertEquals("The name is set", name, actualGroup.getName());
		assertEquals("The title is set", title, actualGroup.getTitle());
		assertEquals("The order is set", 1, actualGroup.getOrder());
		assertNotNull("The group has room for variants", actualGroup.getVariants());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createOptions(boolean, com.bearcode.ovf.model.vip.VipContest, String, String)}
	 * for the case where the contest is for a custom ballot.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testCreateOptions_customBallot() {
		final VipContest contest = createMock("Contest", VipContest.class);
		final VipBallot ballot = createMock("Ballot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot);
		EasyMock.expect(ballot.getCandidates()).andReturn(null);
		EasyMock.expect(ballot.getReferendum()).andReturn(null);
		final VipCustomBallot customBallot = createMock("CustomBallot", VipCustomBallot.class);
		EasyMock.expect(ballot.getCustomBallot()).andReturn(customBallot);
		final VipCustomBallotResponse customBallotResponse = createMock("CustomBallotResponse", VipCustomBallotResponse.class);
		final List<VipCustomBallotResponse> customBallotResponses = Arrays.asList(customBallotResponse);
		EasyMock.expect(customBallot.getBallotResponses()).andReturn(customBallotResponses);
		final VipBallotResponse response = createMock("Response", VipBallotResponse.class);
		EasyMock.expect(customBallotResponse.getBallotResponse()).andReturn(response);
		final String heading = "Heading";
		EasyMock.expect(customBallot.getHeading()).andReturn(heading);
		final String text = "Custom ballot response text";
		EasyMock.expect(response.getText()).andReturn(text);
		replayAll();

		final Collection<FieldDictionaryItem> actualOptions = getValet().createOptions(false, contest, null, null);

		assertNotNull("There are options", actualOptions);
		assertEquals("There is one option", 1, actualOptions.size());
		final FieldDictionaryItem actualOption = actualOptions.iterator().next();
		assertEquals("The option value is set", text + '=' + heading + '|' + text, actualOption.getValue());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createOptions(boolean, com.bearcode.ovf.model.vip.VipContest, String, String)}
	 * for the case where the contest is for a non-standard office.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testCreateOptions_nonStandardOffice() {
		final VipContest contest = createMock("Contest", VipContest.class);
		final String officeName = "Office Name - Court";
        EasyMock.expect(contest.isSpecial()).andReturn(false).anyTimes();
		EasyMock.expect(contest.getOffice()).andReturn(officeName).atLeastOnce();
		EasyMock.expect(contest.getType()).andReturn("Runoff").atLeastOnce();
		final VipElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", VipElectoralDistrict.class);
		EasyMock.expect(contest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		final String districtType = "District Type";
		EasyMock.expect(electoralDistrict.getType()).andReturn(districtType).anyTimes();
		final String districtName = "District Name";
		EasyMock.expect(electoralDistrict.getName()).andReturn(districtName).anyTimes();
		final VipBallot ballot = createMock("Ballot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot);
		final VipBallotCandidate ballotCandidate = createMock("BallotCandidate", VipBallotCandidate.class);
		final List<VipBallotCandidate> ballotCandidates = Arrays.asList(ballotCandidate);
		EasyMock.expect(ballot.getCandidates()).andReturn(ballotCandidates);
		final VipCandidate candidate = createMock("Candidate", VipCandidate.class);
		EasyMock.expect(ballotCandidate.getCandidate()).andReturn(candidate);
		final String candidateName = "Candidate Name";
		EasyMock.expect(candidate.getName()).andReturn(candidateName);
		final String candidateParty = "Candidate Party";
		EasyMock.expect(candidate.getParty()).andReturn(candidateParty);
		EasyMock.expect(candidate.isIncumbent()).andReturn(true);
		replayAll();

		final Collection<FieldDictionaryItem> actualOptions = getValet().createOptions(false, contest, null, null);

		assertNotNull("There are options", actualOptions);
		assertEquals("There are two options", 2, actualOptions.size());
		Iterator<FieldDictionaryItem> optionIterator = actualOptions.iterator();
		final FieldDictionaryItem actualOption = optionIterator.next();
		assertEquals("The first option value is set", candidateName + " {" + candidateParty + ",Incumbent}=" + officeName + " Runoff election"
				+ "|" + candidateParty + "|" + candidateName, actualOption.getValue());
		final FieldDictionaryItem blankOption = optionIterator.next();
		assertEquals("The second option value is set", PageAddOnValet.WRITE_IN + "=" + officeName + " Runoff election||", blankOption.getValue());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createOptions(boolean, com.bearcode.ovf.model.vip.VipContest, String, String)}
	 * for the case where the contest is for a referendum.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testCreateOptions_referendum() {
		final VipContest contest = createMock("Contest", VipContest.class);
		final VipBallot ballot = createMock("Ballot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot);
		EasyMock.expect(ballot.getCandidates()).andReturn(null);
		final VipReferendum referendum = createMock("Referendum", VipReferendum.class);
		EasyMock.expect(ballot.getReferendum()).andReturn(referendum);
		final VipReferendumBallotResponse referendumResponse = createMock("ReferendumResponse", VipReferendumBallotResponse.class);
		final List<VipReferendumBallotResponse> referendumResponses = Arrays.asList(referendumResponse);
		EasyMock.expect(referendum.getBallotResponses()).andReturn(referendumResponses);
		final VipBallotResponse response = createMock("Response", VipBallotResponse.class);
		EasyMock.expect(referendumResponse.getBallotResponse()).andReturn(response);
		final String title = "Referendum title";
		EasyMock.expect(referendum.getTitle()).andReturn(title);
		final String text = "Referendum response text";
		EasyMock.expect(response.getText()).andReturn(text);
		replayAll();

		final Collection<FieldDictionaryItem> actualOptions = getValet().createOptions(false, contest, null, null);

		assertNotNull("There are options", actualOptions);
		assertEquals("There is one option", 1, actualOptions.size());
		final FieldDictionaryItem actualOption = actualOptions.iterator().next();
		assertEquals("The option value is set", text + '=' + title + '|' + text, actualOption.getValue());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createOptions(boolean, com.bearcode.ovf.model.vip.VipContest, String, String)}
	 * for the case where the contest is for a standard office.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 8, 2012
	 * @version Oct 1, 2013
	 */
	@Test
	public final void testCreateOptions_standardOffice() {
		final VipContest contest = createMock("Contest", VipContest.class);
		final String officeName = "Office Name";
        EasyMock.expect(contest.isSpecial()).andReturn(false).anyTimes();
		EasyMock.expect(contest.getOffice()).andReturn(officeName);
		EasyMock.expect(contest.getType()).andReturn("General").atLeastOnce();
		final VipElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", VipElectoralDistrict.class);
		EasyMock.expect(contest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		final String districtType = "Statewide";
		EasyMock.expect(electoralDistrict.getType()).andReturn(districtType).anyTimes();
		final VipBallot ballot = createMock("Ballot", VipBallot.class);
		EasyMock.expect(contest.getBallot()).andReturn(ballot);
		final VipBallotCandidate ballotCandidate = createMock("BallotCandidate", VipBallotCandidate.class);
		final List<VipBallotCandidate> ballotCandidates = Arrays.asList(ballotCandidate);
		EasyMock.expect(ballot.getCandidates()).andReturn(ballotCandidates);
		final VipCandidate candidate = createMock("Candidate", VipCandidate.class);
		EasyMock.expect(ballotCandidate.getCandidate()).andReturn(candidate);
		final String candidateName = "Candidate Name";
		EasyMock.expect(candidate.getName()).andReturn(candidateName);
		final String candidateParty = "Candidate Party";
		EasyMock.expect(candidate.getParty()).andReturn(candidateParty);
		replayAll();

		final Collection<FieldDictionaryItem> actualOptions = getValet().createOptions(true, contest, null, null);

		assertNotNull("There are options", actualOptions);
		assertEquals("There is one option", 1, actualOptions.size());
		final FieldDictionaryItem actualOption = actualOptions.iterator().next();
		assertEquals("The option value is set", candidateName + " {" + candidateParty + "}=" + candidateName,
				actualOption.getValue());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createOptions(String[])}.
	 */
	@Test
	public final void testCreateOptionsStringArray() {
		String[] optionValues = { "Value 1", "Value 2" };
		
		Collection<FieldDictionaryItem> actualOptions = getValet().createOptions(optionValues);
		
		assertEquals("The correct number of options are returned", optionValues.length, actualOptions.size());
		for (final FieldDictionaryItem actualOption : actualOptions) {
			assertTrue(actualOption.getValue() + " is expected", Arrays.asList(optionValues).contains(actualOption.getValue()));
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.PageAddOnValet#createVariant(com.bearcode.ovf.model.questionnaire.Question, java.lang.Long, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	@Test
	public final void testCreateVariant() {
		final Question group = createMock("Group", Question.class);
		final Long id = 6526l;
		final String title = "Variant title";
		final Collection<QuestionVariant> variants = new LinkedList<QuestionVariant>();
		EasyMock.expect(group.getVariants()).andReturn(variants);
		replayAll();

		final QuestionVariant actualVariant = getValet().createVariant(group, id, title);

		assertNotNull("A variant is created", actualVariant);
		assertSame("The variant belongs to the group", group, actualVariant.getQuestion());
		assertTrue("The group contains the variant", variants.contains(actualVariant));
		assertEquals("The identifier is set", id.longValue(), actualVariant.getId());
		assertEquals("The title is set", title, actualVariant.getTitle());
		assertNotNull("The variant has room for fields", actualVariant.getFields());
		verifyAll();
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private void setValet(final V valet) {
		this.valet = valet;
	}

	/**
	 * Creates a state contests page add on valet of the type to test.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	protected abstract V createValet();

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	protected final V getValet() {
		return valet;
	}

	/**
	 * Sets up to test the specific type of state contests page add on valet.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	protected abstract void setUpForValet();

	/**
	 * Tears down the set up for testing the specific type of state contests page add on valet.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	protected abstract void tearDownForValet();

}
