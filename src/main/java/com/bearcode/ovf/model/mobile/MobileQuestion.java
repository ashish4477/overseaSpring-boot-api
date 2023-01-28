/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.bearcode.ovf.model.questionnaire.QuestionField;

/**
 * Extended {@link MobileLevel} representation of a wizard question in the mobile environment.
 * 
 * @author IanBrown
 * 
 * @since Apr 9, 2012
 * @version Sep 27, 2012
 */
@JsonIgnoreProperties({ "questionField" })
public class MobileQuestion extends MobileLevel {

	/**
	 * the additional help text.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String additionalHelp;

	/**
	 * is this an encoded field?
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private boolean encoded;

	/**
	 * the role that the data plays.
	 * 
	 * @author IanBrown
	 * @since Sep 27, 2012
	 * @version Sep 27, 2012
	 */
	private String dataRole;

	/**
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private String fieldType;

	/**
	 * the help text.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String helpText;

	/**
	 * the options for answering the question (if <code>null</code> or empty, any answers matching the verification pattern are
	 * accepted).
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private List<MobileOption> options;

	/**
	 * the actual question field.
	 * 
	 * @author IanBrown
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	private QuestionField questionField;

	/**
	 * the identifier for the question field in the questionnaire.
	 * 
	 * @author IanBrown
	 * @since Apr 9, 2012
	 * @version Apr 9, 2012
	 */
	private long questionFieldId;

	/**
	 * is an answer required?
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private boolean required;

	/**
	 * is this a security question?
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private boolean security;

	/**
	 * the pattern used to verify the input.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String verificationPattern;

	/**
	 * Constructs a mobile question without a title.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileQuestion() {

	}

	/**
	 * Constructs a mobile question with the specified title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title of the question.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileQuestion(final String title) {
		super(title);
	}

	/**
	 * Gets the additional help.
	 * 
	 * @author IanBrown
	 * @return the additional help.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getAdditionalHelp() {
		return additionalHelp;
	}

	/**
	 * Gets the data role.
	 * 
	 * @author IanBrown
	 * @return the data role.
	 * @since Sep 27, 2012
	 * @version Sep 27, 2012
	 */
	public String getDataRole() {
		return dataRole;
	}

	/**
	 * Gets the field type.
	 * 
	 * @author IanBrown
	 * @return the field type.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * Gets the help text.
	 * 
	 * @author IanBrown
	 * @return the help text.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getHelpText() {
		return helpText;
	}

	/**
	 * Gets the options.
	 * 
	 * @author IanBrown
	 * @return the options.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public List<MobileOption> getOptions() {
		return options;
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
	 * Gets the question field id.
	 * 
	 * @author IanBrown
	 * @return the question field id.
	 * @since Apr 9, 2012
	 * @version Apr 9, 2012
	 */
	public long getQuestionFieldId() {
		return questionFieldId;
	}

	/**
	 * Gets the verification pattern.
	 * 
	 * @author IanBrown
	 * @return the verification pattern.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getVerificationPattern() {
		return verificationPattern;
	}

	/**
	 * Is this an encoded field?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if this field is encoded, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public boolean isEncoded() {
		return encoded;
	}

	/**
	 * Is an answer required?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if an answer is required, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Is this a security question?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if this is a security question, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public boolean isSecurity() {
		return security;
	}

	/**
	 * Sets the additional help.
	 * 
	 * @author IanBrown
	 * @param additionalHelp
	 *            the additional help to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setAdditionalHelp(final String additionalHelp) {
		this.additionalHelp = additionalHelp;
	}

	/**
	 * Sets the data role.
	 * 
	 * @author IanBrown
	 * @param dataRole
	 *            the data role to set.
	 * @since Sep 27, 2012
	 * @version Sep 27, 2012
	 */
	public void setDataRole(final String dataRole) {
		this.dataRole = dataRole;
	}

	/**
	 * Sets the encoded flag.
	 * 
	 * @author IanBrown
	 * @param encoded
	 *            <code>true</code> if this field is encoded, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setEncoded(final boolean encoded) {
		this.encoded = encoded;
	}

	/**
	 * Sets the field type.
	 * 
	 * @author IanBrown
	 * @param fieldType
	 *            the field type to set.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	public void setFieldType(final String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * Sets the help text.
	 * 
	 * @author IanBrown
	 * @param helpText
	 *            the help text to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setHelpText(final String helpText) {
		this.helpText = helpText;
	}

	/**
	 * Sets the options.
	 * 
	 * @author IanBrown
	 * @param options
	 *            the options to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setOptions(final List<MobileOption> options) {
		this.options = options;
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
	 * Sets the question field id.
	 * 
	 * @author IanBrown
	 * @param questionFieldId
	 *            the question field id to set.
	 * @since Apr 9, 2012
	 * @version Apr 9, 2012
	 */
	public void setQuestionFieldId(final long questionFieldId) {
		this.questionFieldId = questionFieldId;
	}

	/**
	 * Sets the required flag.
	 * 
	 * @author IanBrown
	 * @param required
	 *            <code>true</code> if an answer is required, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setRequired(final boolean required) {
		this.required = required;
	}

	/**
	 * Sets the security flag.
	 * 
	 * @author IanBrown
	 * @param security
	 *            <code>true</code> if this is a security question, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setSecurity(final boolean security) {
		this.security = security;
	}

	/**
	 * Sets the verification pattern.
	 * 
	 * @author IanBrown
	 * @param verificationPattern
	 *            the verification pattern to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setVerificationPattern(final String verificationPattern) {
		this.verificationPattern = verificationPattern;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (#").append(getQuestionFieldId()).append(") [").append(getFieldType()).append("]");
		if (getOptions() != null && !getOptions().isEmpty()) {
			sb.append(" = ").append(getOptions());
		}
		return sb.toString();
	}
}
