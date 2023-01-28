/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractVip} representing a response to a custom ballot.
 * @author IanBrown
 *
 * @since Jun 28, 2012
 * @version Jun 28, 2012
 */
public class VipBallotResponse extends AbstractVip {

	/**
	 * the text of the response.
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private String text;

	/**
	 * Gets the text.
	 * @author IanBrown
	 * @return the text.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @author IanBrown
	 * @param text the text to set.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setText(String text) {
		this.text = text;
	}
}
