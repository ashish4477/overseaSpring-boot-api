/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractVipHasName} representing a candidate.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version May 8, 2013
 */
public class VipCandidate extends AbstractVipHasName {

	/**
	 * is this candidate the incumbent?
	 * 
	 * @author IanBrown
	 * @since May 8, 2013
	 * @version May 8, 2013
	 */
	private boolean incumbent;
	
	/**
	 * the party for the candidate.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String party;

	/**
	 * Gets the party.
	 * 
	 * @author IanBrown
	 * @return the party.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getParty() {
		return party;
	}

	/**
	 * Is this candidate the incumbent?
	 * @author IanBrown
	 * @return the incumbent.
	 * @since May 13, 2013
	 * @version May 13, 2013
	 */
    public boolean isIncumbent() {
	    return incumbent;
    }

	/**
	 * Sets the incumbent flag.
	 * @author IanBrown
	 * @param incumbent the incumbent to set.
	 * @since May 13, 2013
	 * @version May 13, 2013
	 */
    public void setIncumbent(boolean incumbent) {
	    this.incumbent = incumbent;
    }

	/**
	 * Sets the party.
	 * 
	 * @author IanBrown
	 * @param party
	 *            the party to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setParty(final String party) {
		this.party = party;
	}
}
