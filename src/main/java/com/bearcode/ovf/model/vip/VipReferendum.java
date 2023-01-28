/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.List;

/**
 * Extended {@link AbstractVip} representing a referendum.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jun 29, 2012
 */
public class VipReferendum extends AbstractVip {

	/**
	 * the ballot responses.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private List<VipReferendumBallotResponse> ballotResponses;

	/**
	 * brief description of the referendum.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String brief;

	/**
	 * the (optional) sub-title.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String subTitle;

	/**
	 * the text of the referendum.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String text;

	/**
	 * the title of the referendum.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String title;

	/**
	 * Gets the ballot responses.
	 * 
	 * @author IanBrown
	 * @return the ballot responses.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public List<VipReferendumBallotResponse> getBallotResponses() {
		return ballotResponses;
	}

	/**
	 * Gets the brief.
	 * 
	 * @author IanBrown
	 * @return the brief.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getBrief() {
		return brief;
	}

	/**
	 * Gets the sub-title.
	 * 
	 * @author IanBrown
	 * @return the sub-title.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * Gets the text.
	 * 
	 * @author IanBrown
	 * @return the text.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the title.
	 * 
	 * @author IanBrown
	 * @return the title.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the ballot responses.
	 * 
	 * @author IanBrown
	 * @param ballotResponses
	 *            the ballot responses to set.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public void setBallotResponses(final List<VipReferendumBallotResponse> ballotResponses) {
		this.ballotResponses = ballotResponses;
	}

	/**
	 * Sets the brief.
	 * 
	 * @author IanBrown
	 * @param brief
	 *            the brief to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setBrief(final String brief) {
		this.brief = brief;
	}

	/**
	 * Sets the sub-title.
	 * 
	 * @author IanBrown
	 * @param subTitle
	 *            the sub-title to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setSubTitle(final String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * Sets the text.
	 * 
	 * @author IanBrown
	 * @param text
	 *            the text to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * Sets the title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
}
