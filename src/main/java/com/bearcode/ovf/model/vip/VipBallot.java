/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.LinkedList;
import java.util.List;

/**
 * Extended {@link AbstractVip} representing a ballot for a contest.
 * 
 * TODO need the write in flag.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jul 10, 2012
 */
public class VipBallot extends AbstractVip {

	/**
	 * the (optional) candidates on the ballot.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 25, 2012
	 */
	private List<VipBallotCandidate> candidates;

	/**
	 * the (optional) custom ballot.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private VipCustomBallot customBallot;

	/**
	 * the (optional) referendum.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipReferendum referendum;

	/**
	 * are write-ins allowed?
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private boolean writeIn;

	/**
	 * Adds the candidate to the ballot.
	 * 
	 * @author IanBrown
	 * @param candidate
	 *            the candidate.
	 * @since Jul 2, 2012
	 * @version Jul 10, 2012
	 */
	public void addCandidate(final VipCandidate candidate) {
		if (getCandidates() == null) {
			setCandidates(new LinkedList<VipBallotCandidate>());
		}
		final VipBallotCandidate ballotCandidate = new VipBallotCandidate();
		ballotCandidate.setBallot(this);
		ballotCandidate.setCandidate(candidate);
		ballotCandidate.setSortOrder(getCandidates().size() + 1);
		getCandidates().add(ballotCandidate);
	}

	/**
	 * Gets the candidates.
	 * 
	 * @author IanBrown
	 * @return the candidates.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public List<VipBallotCandidate> getCandidates() {
		return candidates;
	}

	/**
	 * Gets the custom ballot.
	 * 
	 * @author IanBrown
	 * @return the custom ballot.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public VipCustomBallot getCustomBallot() {
		return customBallot;
	}

	/**
	 * Gets the referendum.
	 * 
	 * @author IanBrown
	 * @return the referendum.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public VipReferendum getReferendum() {
		return referendum;
	}

	/**
	 * Gets the write in.
	 * 
	 * @author IanBrown
	 * @return the write in.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public boolean isWriteIn() {
		return writeIn;
	}

	/**
	 * Sets the candidates.
	 * 
	 * @author IanBrown
	 * @param candidates
	 *            the candidates to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setCandidates(final List<VipBallotCandidate> candidates) {
		this.candidates = candidates;
	}

	/**
	 * Sets the custom ballot.
	 * 
	 * @author IanBrown
	 * @param customBallot
	 *            the custom ballot to set.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public void setCustomBallot(final VipCustomBallot customBallot) {
		this.customBallot = customBallot;
	}

	/**
	 * Sets the referendum.
	 * 
	 * @author IanBrown
	 * @param referendum
	 *            the referendum to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setReferendum(final VipReferendum referendum) {
		this.referendum = referendum;
	}

	/**
	 * Sets the write in.
	 * 
	 * @author IanBrown
	 * @param writeIn
	 *            the write in to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setWriteIn(final boolean writeIn) {
		this.writeIn = writeIn;
	}
}
