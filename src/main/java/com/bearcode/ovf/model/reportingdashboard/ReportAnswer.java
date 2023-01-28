/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;

/**
 * An acceptable answer for a report field.
 * 
 * @author IanBrown
 * 
 * @since Jan 5, 2012
 * @version Jan 31, 2012
 */
public class ReportAnswer {

	/**
	 * the default description for an answer.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	static final String DEFAULT_DESCRIPTION = "Answer is not set";

	/**
	 * the format statement used to build a description of a predefined answer.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	static final String PREDEFINED_ANSWER_DESCRIPTION_FORMAT = "%1$s = %2$s";

	/**
	 * the unique identifier for the report answer.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Long id;

	/**
	 * the specific answer value.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private String answer;

	/**
	 * the predefined answer.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private FieldDictionaryItem predefinedAnswer;

	/**
	 * the report field to this answer is acceptable.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportField reportField;

	/**
	 * Creates a deep copy of the report answer.
	 * 
	 * @author IanBrown
	 * @return the copied answer.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	public ReportAnswer deepCopy() {
		final ReportAnswer copiedAnswer = new ReportAnswer();
		copiedAnswer.setAnswer(getAnswer());
		copiedAnswer.setPredefinedAnswer(getPredefinedAnswer());
		return copiedAnswer;
	}

	/**
	 * Gets the specific answer value.
	 * 
	 * @author IanBrown
	 * @return the answer value.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Gets the description of this answer.
	 * 
	 * @author IanBrown
	 * @return the description.
	 * @since Jan 11, 2012
	 * @version Jan 31, 2012
	 */
	public String getDescription() {
		if ((getAnswer() != null) && !getAnswer().isEmpty()) {
			return getAnswer();
		} else if (getPredefinedAnswer() != null) {
			return String.format(PREDEFINED_ANSWER_DESCRIPTION_FORMAT, getPredefinedAnswer().getForField().getTitle(),
					getPredefinedAnswer().getValue());
		}

		return DEFAULT_DESCRIPTION;
	}

	/**
	 * Gets the identifier for the answer.
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
	 * Gets the predefined answer value.
	 * 
	 * @author IanBrown
	 * @return the answer value.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public FieldDictionaryItem getPredefinedAnswer() {
		return predefinedAnswer;
	}

	/**
	 * Gets the field for the answer.
	 * 
	 * @author IanBrown
	 * @return the report field.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public ReportField getReportField() {
		return reportField;
	}

	/**
	 * Sets the specific answer value.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer value.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setAnswer(final String answer) {
		this.answer = answer;
	}

	/**
	 * Sets the identifier for the answer.
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
	 * Sets the predefined answer value.
	 * 
	 * @author IanBrown
	 * @param predefinedAnswer
	 *            the answer value.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setPredefinedAnswer(final FieldDictionaryItem predefinedAnswer) {
		this.predefinedAnswer = predefinedAnswer;
	}

	/**
	 * | Sets the field for the answer.
	 * 
	 * @author IanBrown
	 * @param reportField
	 *            the report field.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	public void setReportField(final ReportField reportField) {
		this.reportField = reportField;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" ").append(getDescription());

		return sb.toString();
	}
}
