/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.bearcode.ovf.model.questionnaire.QuestionField;

/**
 * Representation of the answer to a wizard question as provided by a mobile user.
 * 
 * @author IanBrown
 * 
 * @since Apr 11, 2012
 * @version Jul 11, 2012
 */
@JsonIgnoreProperties({ "questionField" })
public class MobileAnswer {

	/**
	 * the option selected by the user.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private MobileOption option;

	/**
	 * the question field.
	 * 
	 * @author IanBrown
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	private QuestionField questionField;

	/**
	 * the identifier for the question field to which the answer applies.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private long questionFieldId;

	/**
	 * Builds a string representation of the answer indented by the specified prefix.
	 * 
	 * @author IanBrown
	 * @param prefix
	 *            the indentation prefix.
	 * @return the string representation.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public String buildString(final String prefix) {
		final StringBuilder sb = new StringBuilder(prefix);
		sb.append("#").append(getQuestionFieldId()).append(" = ").append(getOption());
		return sb.toString();
	}

	/**
	 * Gets the option.
	 * 
	 * @author IanBrown
	 * @return the option.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public MobileOption getOption() {
		return option;
	}

	/**
	 * Gets the question field.
	 * 
	 * @author IanBrown
	 * @return the question field.
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	public QuestionField getQuestionField() {
		return questionField;
	}

	/**
	 * Gets the question field identifier.
	 * 
	 * @author IanBrown
	 * @return the question field identifier.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public long getQuestionFieldId() {
		return questionFieldId;
	}

	/**
	 * Sets the option.
	 * 
	 * @author IanBrown
	 * @param option
	 *            the option to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setOption(final MobileOption option) {
		this.option = option;
	}

	/**
	 * Sets the question field.
	 * 
	 * @author IanBrown
	 * @param questionField
	 *            the question field to set.
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	public void setQuestionField(final QuestionField questionField) {
		this.questionField = questionField;
	}

	/**
	 * Sets the question field identifier.
	 * 
	 * @author IanBrown
	 * @param questionFieldId
	 *            the question field identifier to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setQuestionFieldId(final long questionFieldId) {
		this.questionFieldId = questionFieldId;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return buildString("");
	}
}
