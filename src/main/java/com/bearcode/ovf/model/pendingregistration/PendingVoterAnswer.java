/**
 * 
 */
package com.bearcode.ovf.model.pendingregistration;

/**
 * Extended {@link PendingVoterIdentifier} representing a question presented to the voter and the answer provided by the voter.
 * 
 * @author IanBrown
 * 
 * @since Nov 2, 2012
 * @version Nov 7, 2012
 */
public class PendingVoterAnswer extends PendingVoterIdentified {

	/**
	 * the answer given by the voter.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String answer;

	/**
	 * encrypted form of the question and answer.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	private byte[] encrypted;

	/**
	 * the question presented to the voter.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String question;

	/**
	 * Gets the answer.
	 * 
	 * @author IanBrown
	 * @return the answer.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Gets the encrypted.
	 * 
	 * @author IanBrown
	 * @return the encrypted.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public byte[] getEncrypted() {
		return encrypted;
	}

	/**
	 * Gets the question.
	 * 
	 * @author IanBrown
	 * @return the question.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Sets the answer.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the answer to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setAnswer(final String answer) {
		this.answer = answer;
	}

	/**
	 * Sets the encrypted.
	 * 
	 * @author IanBrown
	 * @param encrypted
	 *            the encrypted to set.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public void setEncrypted(final byte[] encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * Sets the question.
	 * 
	 * @author IanBrown
	 * @param question
	 *            the question to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setQuestion(final String question) {
		this.question = question;
	}
}
