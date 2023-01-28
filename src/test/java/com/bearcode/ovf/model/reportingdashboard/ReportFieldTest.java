/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.questionnaire.QuestionField;

/**
 * Test for {@link ReportField}.
 * 
 * @author IanBrown
 * 
 * @since Jan 5, 2012
 * @version Feb 3, 2012
 */
public final class ReportFieldTest extends EasyMockSupport {

	/**
	 * the report field to test.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportField reportField;

	/**
	 * Sets up the report field for testing.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Before
	public final void setUpReportField() {
		setReportField(createReportField());
	}

	/**
	 * Tears down the report field after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@After
	public final void tearDownReportField() {
		setReportField(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#addAnswer(ReportAnswer)}.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testAddAnswer() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		EasyMock.expect(answer.getReportField()).andReturn(null);
		answer.setReportField(getReportField());
		replayAll();

		getReportField().addAnswer(answer);

		assertTrue("The answer is in the field", getReportField().getAnswers().contains(answer));
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#addAnswer(ReportAnswer)} for the case where the
	 * answer already belongs to a different field.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAddAnswer_answerInOtherField() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final ReportField answerField = createMock("AnswerField", ReportField.class);
		EasyMock.expect(answer.getReportField()).andReturn(answerField);
		replayAll();

		getReportField().addAnswer(answer);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#addAnswer(ReportAnswer)} for the case where the
	 * answer is already in the field.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test(expected = IllegalStateException.class)
	public final void testAddAnswer_belongsToField() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		EasyMock.expect(answer.getReportField()).andReturn(null);
		answer.setReportField(getReportField());
		EasyMock.expect(answer.getReportField()).andReturn(getReportField());
		replayAll();
		getReportField().addAnswer(answer);

		getReportField().addAnswer(answer);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#deepCopy()} for the case where there are
	 * answers.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_answers() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer);
		getReportField().setAnswers(answers);
		final ReportAnswer copiedAnswer = createMock("CopiedAnswer", ReportAnswer.class);
		EasyMock.expect(answer.deepCopy()).andReturn(copiedAnswer);
		copiedAnswer.setReportField((ReportField) EasyMock.anyObject());
		replayAll();

		final ReportField actualDeepCopy = getReportField().deepCopy();

		assertReportField(getReportField(), actualDeepCopy);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#deepCopy()} for the case where there are no
	 * fields.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_noFields() {
		final ReportField actualDeepCopy = getReportField().deepCopy();

		assertReportField(getReportField(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#deepCopy()} for the case where there is a
	 * question.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_question() {
		final QuestionField question = createMock("Question", QuestionField.class);
		getReportField().setQuestion(question);

		final ReportField actualDeepCopy = getReportField().deepCopy();

		assertReportField(getReportField(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#deepCopy()} for the case where there is a user
	 * name field.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_userNameField() {
		getReportField().setUserFieldName("User Name Field");

		final ReportField actualDeepCopy = getReportField().deepCopy();

		assertReportField(getReportField(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getAnswers()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetAnswers() {
		final Collection<ReportAnswer> actualAnswers = getReportField().getAnswers();

		assertNull("There are no answers", actualAnswers);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getColumn()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetColumn() {
		final ReportColumn actualColumn = getReportField().getColumn();

		assertNull("There is no column", actualColumn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getDescription()} for the case where there is no
	 * question or user field name.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testGetDescription_notMapped() {
		final String actualDescription = getReportField().getDescription();

		assertEquals("The default description is returned", ReportField.DEFAULT_DESCRIPTION, actualDescription);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getDescription()} for the case where there is a
	 * question and no answers.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testGetDescription_question() {
		final QuestionField question = createMock("Question", QuestionField.class);
		final String title = "Title";
		EasyMock.expect(question.getTitle()).andReturn(title);
		replayAll();
		getReportField().setQuestion(question);

		final String actualDescription = getReportField().getDescription();

		assertEquals("The question description is returned", ReportField.QUESTION_DESCRIPTION + title, actualDescription);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getDescription()} for the case where there is a
	 * question with answers.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testGetDescription_questionWithAnswers() {
		final QuestionField question = createMock("Question", QuestionField.class);
		final String title = "Title";
		EasyMock.expect(question.getTitle()).andReturn(title);
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer);
		final String answerDescription = "Answer";
		EasyMock.expect(answer.getDescription()).andReturn(answerDescription);
		replayAll();
		getReportField().setQuestion(question);
		getReportField().setAnswers(answers);

		final String actualDescription = getReportField().getDescription();

		assertEquals("The question description is returned", ReportField.QUESTION_DESCRIPTION + title + " [" + answerDescription
				+ "]", actualDescription);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getDescription()} for the case where there is a
	 * user field name and no answers.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testGetDescription_userFieldName() {
		final String userFieldName = "User Field Name";
		getReportField().setUserFieldName(userFieldName);

		final String actualDescription = getReportField().getDescription();

		assertEquals("The question description is returned", ReportField.USER_FIELD_NAME_DESCRIPTION + userFieldName,
				actualDescription);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getDescription()} for the case where there is a
	 * user field name with answers.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testGetDescription_userFieldNameWithAnswers() {
		final String userFieldName = "User Field Name";
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer);
		final String answerDescription = "Answer";
		EasyMock.expect(answer.getDescription()).andReturn(answerDescription);
		replayAll();
		getReportField().setUserFieldName(userFieldName);
		getReportField().setAnswers(answers);

		final String actualDescription = getReportField().getDescription();

		assertEquals("The question description is returned", ReportField.USER_FIELD_NAME_DESCRIPTION + userFieldName + " ["
				+ answerDescription + "]", actualDescription);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getId()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetId() {
		final Long actualId = getReportField().getId();

		assertNull("There is no ID", actualId);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getQuestion()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetQuestion() {
		final QuestionField actualQuestion = getReportField().getQuestion();

		assertNull("There is no question", actualQuestion);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#getUserFieldName()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetUserFieldName() {
		final String actualUserFieldName = getReportField().getUserFieldName();

		assertNull("There is no user field name", actualUserFieldName);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#removeAnswer(ReportAnswer)}.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Feb 3, 2012
	 */
	@Test
	public final void testRemoveAnswer() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		EasyMock.expect(answer.getReportField()).andReturn(null);
		answer.setReportField(getReportField());
		answer.setReportField(null);
		replayAll();
		getReportField().addAnswer(answer);

		getReportField().removeAnswer(answer);

		final Collection<ReportAnswer> actualAnswers = getReportField().getAnswers();
		assertTrue("There are no answers in the field", actualAnswers.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#removeAnswer(ReportAnswer)} for the case where
	 * there are no answers.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testRemoveAnswer_noAnswers() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		replayAll();

		getReportField().removeAnswer(answer);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#removeAnswer(ReportAnswer)} for the case where
	 * the field is not in the column.
	 * 
	 * @author IanBrown
	 * @since Jan 27, 2012
	 * @version Jan 27, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testRemoveAnswer_notInField() {
		final ReportAnswer answwer = createMock("Answer", ReportAnswer.class);
		final ReportAnswer answerInField = createMock("AnswerInField", ReportAnswer.class);
		EasyMock.expect(answerInField.getReportField()).andReturn(null);
		answerInField.setReportField(getReportField());
		EasyMock.expect(answwer.getReportField()).andReturn(null);
		replayAll();
		getReportField().addAnswer(answerInField);

		getReportField().removeAnswer(answwer);

		final Collection<ReportAnswer> actualAnswers = getReportField().getAnswers();
		final Collection<ReportAnswer> expectedAnswers = Arrays.asList(answerInField);
		assertEquals("The answer that was in the field remains in the field", expectedAnswers, actualAnswers);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#setAnswers(java.util.Collection)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetAnswers() {
		final ReportAnswer answer = createMock("Answer", ReportAnswer.class);
		final Collection<ReportAnswer> answers = Arrays.asList(answer);
		replayAll();

		getReportField().setAnswers(answers);

		assertSame("The answers are set", answers, getReportField().getAnswers());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ReportField#setColumn(com.bearcode.ovf.model.reportingdashboard.ReportColumn)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetColumn() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		replayAll();

		getReportField().setColumn(column);

		assertSame("The column is set", column, getReportField().getColumn());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#setId(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetId() {
		final Long id = 766192l;

		getReportField().setId(id);

		assertSame("The ID is set", id, getReportField().getId());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ReportField#setQuestion(com.bearcode.ovf.model.questionnaire.QuestionField)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetQuestion() {
		final QuestionField question = createMock("QuestionField", QuestionField.class);
		replayAll();

		getReportField().setQuestion(question);

		assertSame("The question is set", question, getReportField().getQuestion());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportField#setUserFieldName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetUserFieldName() {
		final String userFieldName = "User Field Name";

		getReportField().setUserFieldName(userFieldName);

		assertEquals("The user field name is set", userFieldName, getReportField().getUserFieldName());
	}

	/**
	 * Custom assertion to ensure that the answers are copied correctly.
	 * 
	 * @author IanBrown
	 * @param expectedAnswers
	 *            the expected (original) answers.
	 * @param actualAnswers
	 *            the actual (copied) answers.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private void assertAnswers(final Collection<ReportAnswer> expectedAnswers, final Collection<ReportAnswer> actualAnswers) {
		if (expectedAnswers == null) {
			assertNull("There are no answers", actualAnswers);
		} else {
			assertNotNull("There are answers", actualAnswers);
			assertEquals("There are the correct number of answers", expectedAnswers.size(), actualAnswers.size());
		}
	}

	/**
	 * Custom assertion to ensure that the report field is copied correctly.
	 * 
	 * @author IanBrown
	 * @param expectedReportField
	 *            the expected (original) report field.
	 * @param actualReportField
	 *            the actual (copied) report field.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private void assertReportField(final ReportField expectedReportField, final ReportField actualReportField) {
		assertNotNull("The report field was copied", actualReportField);
		assertAnswers(expectedReportField.getAnswers(), actualReportField.getAnswers());
		assertSame("The question is correct", expectedReportField.getQuestion(), actualReportField.getQuestion());
		assertEquals("The user field name is correct", expectedReportField.getUserFieldName(), actualReportField.getUserFieldName());
	}

	/**
	 * Creates a report field.
	 * 
	 * @author IanBrown
	 * @return the report field.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportField createReportField() {
		return new ReportField();
	}

	/**
	 * Gets the report field.
	 * 
	 * @author IanBrown
	 * @return the report field.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportField getReportField() {
		return reportField;
	}

	/**
	 * Sets the report field.
	 * 
	 * @author IanBrown
	 * @param reportField
	 *            the report field to set.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private void setReportField(final ReportField reportField) {
		this.reportField = reportField;
	}
}
