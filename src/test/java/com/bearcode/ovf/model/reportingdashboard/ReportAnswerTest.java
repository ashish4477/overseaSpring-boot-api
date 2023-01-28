/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.QuestionField;

/**
 * @author IanBrown
 * 
 * @since Jan 5, 2012
 * @version Jan 19, 2012
 */
public final class ReportAnswerTest extends EasyMockSupport {

	/**
	 * the report answer to test.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportAnswer reportAnswer;

	/**
	 * Sets up the report answer to test.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Before
	public final void setUpReportAnswer() {
		setReportAnswer(createReportAnswer());
	}

	/**
	 * Tears down the report answer after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@After
	public final void tearDownReportAnswer() {
		setReportAnswer(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#deepCopy()} for the case where there is an
	 * answer.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_answer() {
		getReportAnswer().setAnswer("Answer");

		final ReportAnswer actualDeepCopy = getReportAnswer().deepCopy();

		assertReportAnswer(getReportAnswer(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#deepCopy()} for the case where there are no
	 * fields.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_noFields() {
		final ReportAnswer actualDeepCopy = getReportAnswer().deepCopy();

		assertReportAnswer(getReportAnswer(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#deepCopy()} for the case where there is a
	 * predefined answer.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_predefinedAnswer() {
		final FieldDictionaryItem predefinedAnswer = createMock("PredefinedAnswer", FieldDictionaryItem.class);
		getReportAnswer().setPredefinedAnswer(predefinedAnswer);

		final ReportAnswer actualDeepCopy = getReportAnswer().deepCopy();

		assertReportAnswer(getReportAnswer(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#getAnswer()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetAnswer() {
		final String actualAnswer = getReportAnswer().getAnswer();

		assertNull("There is no answer", actualAnswer);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#getDescription()} for the case where the answer
	 * is not mapped.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test
	public final void testGetDescription_notMapped() {
		final String actualDescription = getReportAnswer().getDescription();

		assertEquals("The default description is returned", ReportAnswer.DEFAULT_DESCRIPTION, actualDescription);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#getDescription()} for the case where the answer
	 * has a predefined answer.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test
	public final void testGetDescription_predefinedAnswer() {
		final FieldDictionaryItem predefinedAnswer = createMock("PredefinedAnswer", FieldDictionaryItem.class);
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(predefinedAnswer.getForField()).andReturn(field);
		final String title = "Title";
		EasyMock.expect(field.getTitle()).andReturn(title);
		final String value = "Value";
		EasyMock.expect(predefinedAnswer.getValue()).andReturn(value);
		replayAll();
		getReportAnswer().setPredefinedAnswer(predefinedAnswer);

		final String actualDescription = getReportAnswer().getDescription();

		assertEquals("The predefined answer is described",
				String.format(ReportAnswer.PREDEFINED_ANSWER_DESCRIPTION_FORMAT, title, value), actualDescription);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#getDescription()} for the case where the answer
	 * has a string answer.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test
	public final void testGetDescription_stringAnswer() {
		final String answer = "Answer";
		getReportAnswer().setAnswer(answer);

		final String actualDescription = getReportAnswer().getDescription();

		assertEquals("The string answer is returned", answer, actualDescription);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#getId()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetId() {
		final Long actualId = getReportAnswer().getId();

		assertNull("There is no ID", actualId);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#getPredefinedAnswer()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetPredefinedAnswer() {
		final FieldDictionaryItem actualPredefinedAnswer = getReportAnswer().getPredefinedAnswer();

		assertNull("There is no predefined answer", actualPredefinedAnswer);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#getReportField()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetReportField() {
		final ReportField actualReportField = getReportAnswer().getReportField();

		assertNull("There is no report field", actualReportField);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#setAnswer(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetAnswer() {
		final String answer = "Answer";

		getReportAnswer().setAnswer(answer);

		assertEquals("The answer is set", answer, getReportAnswer().getAnswer());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#setId(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetId() {
		final Long id = 87658122l;

		getReportAnswer().setId(id);

		assertSame("The ID is set", id, getReportAnswer().getId());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#setPredefinedAnswer(com.bearcode.ovf.model.questionnaire.FieldDictionaryItem)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetPredefinedAnswer() {
		final FieldDictionaryItem predefinedAnswer = createMock("PredefinedAnswer", FieldDictionaryItem.class);
		replayAll();

		getReportAnswer().setPredefinedAnswer(predefinedAnswer);

		assertSame("The predefined answer is set", predefinedAnswer, getReportAnswer().getPredefinedAnswer());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ReportAnswer#setReportField(com.bearcode.ovf.model.reportingdashboard.ReportField)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetReportField() {
		final ReportField reportField = createMock("ReportField", ReportField.class);
		replayAll();

		getReportAnswer().setReportField(reportField);

		assertSame("The report field is set", reportField, getReportAnswer().getReportField());
		verifyAll();
	}

	/**
	 * Custom assertion to ensure that the copied report answer is correct.
	 * 
	 * @author IanBrown
	 * @param expectedReportAnswer
	 *            the expected (original) report answer.
	 * @param actualReportAnswer
	 *            the actual (copied) report answer.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private void assertReportAnswer(final ReportAnswer expectedReportAnswer, final ReportAnswer actualReportAnswer) {
		assertNotNull("The report answer was copied", actualReportAnswer);
		assertEquals("The answer is correct", expectedReportAnswer.getAnswer(), actualReportAnswer.getAnswer());
		assertSame("The predefined answer is correct", expectedReportAnswer.getPredefinedAnswer(),
				actualReportAnswer.getPredefinedAnswer());
	}

	/**
	 * Creates a report answer.
	 * 
	 * @author IanBrown
	 * @return the report answer.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportAnswer createReportAnswer() {
		return new ReportAnswer();
	}

	/**
	 * Gets the report answer.
	 * 
	 * @author IanBrown
	 * @return the report answer.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportAnswer getReportAnswer() {
		return reportAnswer;
	}

	/**
	 * Sets the report answer.
	 * 
	 * @author IanBrown
	 * @param reportAnswer
	 *            the report answer to set.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private void setReportAnswer(final ReportAnswer reportAnswer) {
		this.reportAnswer = reportAnswer;
	}

}
