/**
 * 
 */
package com.bearcode.ovf.forms.cf;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;

/**
 * Form used to hold the information used by What's on my ballot?
 * 
 * @author IanBrown
 * 
 * @since Aug 13, 2012
 * @version Oct 12, 2012
 */
public class WhatsOnMyBallotForm {

	/**
	 * the user address.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private UserAddress address;

	/**
	 * the partisan party.
	 * 
	 * @author IanBrown
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private String partisanParty;

	/**
	 * the voting region.
	 * 
	 * @author IanBrown
	 * @since Oct 12, 2012
	 * @version Oct 12, 2012
	 */
	private VotingRegion region;

	/**
	 * the voter type.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private VoterType voterType;

	/**
	 * the voting state.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private State votingState;
	
	/**
	 * Gets the address.
	 * 
	 * @author IanBrown
	 * @return the address.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	public UserAddress getAddress() {
		return address;
	}

	/**
	 * Gets the partisan party.
	 * @author IanBrown
	 * @return the partisan party.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	public String getPartisanParty() {
		return partisanParty;
	}

	/**
	 * Gets the voting region.
	 * 
	 * @author IanBrown
	 * @return the voting region.
	 * @since Oct 12, 2012
	 * @version Oct 12, 2012
	 */
	public VotingRegion getRegion() {
		return region;
	}

	/**
	 * Gets the voter type.
	 * 
	 * @author IanBrown
	 * @return the voter type.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	public VoterType getVoterType() {
		return voterType;
	}

	/**
	 * Gets the voting state.
	 * 
	 * @author IanBrown
	 * @return the voting state.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public State getVotingState() {
		return votingState;
	}

	/**
	 * Sets the address.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address to set.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	public void setAddress(final UserAddress address) {
		this.address = address;
	}

	/**
	 * Sets the partisan party.
	 * @author IanBrown
	 * @param partisanParty the partisan  party.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	public void setPartisanParty(final String partisanParty) {
	    this.partisanParty = partisanParty;
    }

	/**
	 * Sets the voting region.
	 * 
	 * @author IanBrown
	 * @param region
	 *            the voting region to set.
	 * @since Oct 12, 2012
	 * @version Oct 12, 2012
	 */
	public void setRegion(final VotingRegion region) {
		this.region = region;
	}

	/**
	 * Sets the voter type.
	 * 
	 * @author IanBrown
	 * @param voterType
	 *            the voter type to set.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	public void setVoterType(final VoterType voterType) {
		this.voterType = voterType;
	}
	
	/**
	 * Sets the voting state.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state to set.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public void setVotingState(final State votingState) {
		this.votingState = votingState;
	}
}
