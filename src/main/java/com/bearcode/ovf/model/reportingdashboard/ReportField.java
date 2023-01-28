/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import java.util.Collection;
import java.util.LinkedList;

import com.bearcode.ovf.model.questionnaire.QuestionField;

/**
 * A field for the column of a report.
 * 
 * @author IanBrown
 * 
 * @since Jan 4, 2012
 * @version Feb 2, 2012
 */
public class ReportField {

	/**
	 * the default description of this field.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	static final String DEFAULT_DESCRIPTION = "Field is not mapped";

	/**
	 * the prefix for the description of a question.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	static final String QUESTION_DESCRIPTION = "Question: ";

	/**
	 * the prefix for the description of a user field name.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	static final String USER_FIELD_NAME_DESCRIPTION = "Field: ";

	/**
	 * the unique identifier for the report field.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Long id;

	/**
	 * the acceptable answers for the field.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Collection<ReportAnswer> answers;

	/**
	 * the report column to which this field supplies data.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportColumn column;

	/**
	 * the wizard question field whose answers supply the data.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private QuestionField question;

	/**
	 * the name of the user field that supplies the data.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private String userFieldName;

	/**
	 * Adds an answer to the report.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer to add.
	 * @since Jan 27, 2012
	 * @version Feb 2, 2012
	 */
	public void addAnswer(final ReportAnswer answer) {
		final ReportField answerField = answer.getReportField();
		if ((answerField != null) && (answerField != this)) {
			throw new IllegalArgumentException(answer + " belongs to a different field " + answerField + " rather than " + this);
		} else if ((answerField == this) && (getAnswers() != null) && getAnswers().contains(answer)) {
			throw new IllegalStateException(answer + " already belongs to the field " + this);
		}

		if (getAnswers() == null) {
			setAnswers(new LinkedList<ReportAnswer>());
		}
		answer.setReportField(this);
		getAnswers().add(answer);
	}

	/**
	 * Performs a deep copy of the field.
	 * 
	 * @author IanBrown
	 * @return the copied field.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	public ReportField deepCopy() {
		final ReportField copiedField = new ReportField();
		copiedField.setAnswers(deepCopyAnswers(copiedField));
		copiedField.setQuestion(getQuestion());
		copiedField.setUserFieldName(getUserFieldName());
		return copiedField;
	}

	/**
	 * Gets the acceptable answers for the field.
	 * 
	 * @author IanBrown
	 * @return the acceptable answers.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public Collection<ReportAnswer> getAnswers() {
		return answers;
	}

	/**
	 * Gets the column to display the field.
	 * 
	 * @author IanBrown
	 * @return the report column.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public ReportColumn getColumn() {
		return column;
	}

	/**
	 * Gets the description of this report field.
	 * 
	 * @author IanBrown
	 * @return the description.
	 * @since Jan 11, 2012
	 * @version Jan 31, 2012
	 */
	public String getDescription() {
		if (getQuestion() != null) {
			return questionDescription();
		} else if ((getUserFieldName() != null) && !getUserFieldName().isEmpty()) {
			return userFieldNameDescription();
		}

		return DEFAULT_DESCRIPTION;
	}

	/**
	 * Gets the identifier for the report field.
	 * 
	 * @author IanBrown
	 * @return the identifier.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the question field containing the data for the report field.
	 * 
	 * @author IanBrown
	 * @return the question field.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public QuestionField getQuestion() {
		return question;
	}

	/**
	 * Gets the name of the user table field containing the data for the field.
	 * 
	 * @author IanBrown
	 * @return the user table field name.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public String getUserFieldName() {
		return userFieldName;
	}

	/**
	 * Removes an answer from the report.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer to remove.
	 * @since Jan 27, 2012
	 * @version Feb 2, 2012
	 */
	public void removeAnswer(final ReportAnswer answer) {
		if (getAnswers() != null) {
			if (!getAnswers().contains(answer)) {
				throw new IllegalArgumentException(answer + " does not belong to the field " + this);
			}

			getAnswers().remove(answer);
			answer.setReportField(null);
		}
	}

	/**
	 * Sets the acceptable answers for the field.
	 * 
	 * @author IanBrown
	 * @param answers
	 *            the acceptable answers.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setAnswers(final Collection<ReportAnswer> answers) {
		this.answers = answers;
	}

	/**
	 * Sets the column to display the field.
	 * 
	 * @author IanBrown
	 * @param column
	 *            the report column.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setColumn(final ReportColumn column) {
		this.column = column;
	}

	/**
	 * Sets the identifier for the report field.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the question field containing the data for the report field.
	 * 
	 * @author IanBrown
	 * @param question
	 *            the question field.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setQuestion(final QuestionField question) {
		this.question = question;
	}

	/**
	 * Sets the name of the user table field containing the data for the report field.
	 * 
	 * @author IanBrown
	 * @param userFieldName
	 *            the user table field name.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setUserFieldName(final String userFieldName) {
		this.userFieldName = userFieldName;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" ").append(getDescription());

		return sb.toString();
	}

	/**
	 * Builds a string for the descriptions of the answers.
	 * 
	 * @author IanBrown
	 * @return the answers description.
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	private String answersDescription() {
		final StringBuffer sb = new StringBuffer("[");
		String prefix = "";

		for (final ReportAnswer answer : getAnswers()) {
			sb.append(prefix).append(answer.getDescription());
			prefix = ",";
		}

		sb.append(']');
		return sb.toString();
	}

	/**
	 * Creates a deep copy of the answers.
	 * 
	 * @author IanBrown
	 * @param copiedField
	 *            the copied field.
	 * @return the copied answers.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private Collection<ReportAnswer> deepCopyAnswers(final ReportField copiedField) {
		if (getAnswers() == null) {
			return null;
		}

		final Collection<ReportAnswer> copiedAnswers = new LinkedList<ReportAnswer>();
		for (final ReportAnswer answer : getAnswers()) {
			final ReportAnswer copiedAnswer = answer.deepCopy();
			copiedAnswer.setReportField(copiedField);
			copiedAnswers.add(copiedAnswer);
		}
		return copiedAnswers;
	}

	/**
	 * Builds the string for the description of a wizard question.
	 * 
	 * @author IanBrown
	 * @return the description.
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	private String questionDescription() {
		final StringBuffer sb = new StringBuffer(QUESTION_DESCRIPTION);
		sb.append(getQuestion().getTitle());

		if (getAnswers() != null) {
			sb.append(' ').append(answersDescription());
		}

		return sb.toString();
	}

	/**
	 * Builds the string for the description of a user field name.
	 * 
	 * @author IanBrown
	 * @return the description.
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	private String userFieldNameDescription() {
		final StringBuffer sb = new StringBuffer(USER_FIELD_NAME_DESCRIPTION);
		sb.append(getUserFieldName());

		if (getAnswers() != null) {
			sb.append(' ').append(answersDescription());
		}

		return sb.toString();
	}
}
