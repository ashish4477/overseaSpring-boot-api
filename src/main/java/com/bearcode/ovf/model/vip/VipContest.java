/**
 * 
 */
package com.bearcode.ovf.model.vip;

import java.util.List;

/**
 * Extended {@link AbstractVip} representing a contest in an election (can be
 * between candidates, on the adoption of a resolution,
 * the retention of judges, etc.)
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Jul 30, 2013
 */
public class VipContest extends AbstractVip {

	/**
	 * the ballot for the contest.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipBallot ballot;

	/**
	 * the placement of the contest on the ballot.
	 * 
	 * @author IanBrown
	 * @since Oct 11, 2012
	 * @version Oct 11, 2012
	 */
	private Integer ballotPlacement;

	/**
	 * the election.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private VipElection election;

	/**
	 * the electoral district of the contest.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private VipElectoralDistrict electoralDistrict;

	/**
	 * the number of candidates that can be elected to this office.
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private Integer numberElected;

	/**
	 * the number of candidates that can be voted for for this office.
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private Integer numberVotingFor;

	/**
	 * the office (if appropriate).
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private String office;

	/**
	 * is this a partisan contest?
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private boolean partisan;

	/**
	 * the name of the party participating in the contest.
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private String primaryParty;

	/**
	 * is this a special election?
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private boolean special;

	/**
	 * the type of contest.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String type;

	/**
	 * Gets the ballot.
	 * 
	 * @author IanBrown
	 * @return the ballot.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public VipBallot getBallot() {
		return ballot;
	}

	/**
	 * Gets the ballot placement.
	 * 
	 * @author IanBrown
	 * @return the ballot placement.
	 * @since Oct 11, 2012
	 * @version Oct 11, 2012
	 */
	public Integer getBallotPlacement() {
		return ballotPlacement;
	}

	/**
	 * Gets the election.
	 * 
	 * @author IanBrown
	 * @return the election.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public VipElection getElection() {
		return election;
	}

	/**
	 * Gets the electoral district.
	 * 
	 * @author IanBrown
	 * @return the electoral district.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public VipElectoralDistrict getElectoralDistrict() {
		return electoralDistrict;
	}

	/**
	 * Gets the number elected.
	 * 
	 * @author IanBrown
	 * @return the number elected.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public Integer getNumberElected() {
		return numberElected;
	}

	/**
	 * Gets the number voting for.
	 * 
	 * @author IanBrown
	 * @return the number voting for.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public Integer getNumberVotingFor() {
		return numberVotingFor;
	}

	/**
	 * Gets the office.
	 * 
	 * @author IanBrown
	 * @return the office.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	public String getOffice() {
		return office;
	}

	/**
	 * Gets the partisan party for the contest.
	 * 
	 * @author IanBrown
	 * 
	 * @return the partisan party - this is either the primary party (if there
	 *         is one), the party shared by all of the candidates, or
	 *         <code>null</code>.
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	public String getPartisanParty() {
		String partisanParty = null;
		
		if (isPartisan()) {
			partisanParty = (getPrimaryParty() != null) ? getPrimaryParty() : determinePartisanPartyFromBallot();
		}
		
		return partisanParty;
	}

	/**
	 * Gets the primary party.
	 * 
	 * @author IanBrown
	 * @return the primary party.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public String getPrimaryParty() {
		return primaryParty;
	}

	/**
	 * Gets the type.
	 * 
	 * @author IanBrown
	 * @return the type.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the partisan.
	 * 
	 * @author IanBrown
	 * @return the partisan.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public boolean isPartisan() {
		return partisan;
	}

	/**
	 * Gets the special.
	 * 
	 * @author IanBrown
	 * @return the special.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public boolean isSpecial() {
		return special;
	}

	/**
	 * Sets the ballot.
	 * 
	 * @author IanBrown
	 * @param ballot
	 *            the ballot to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setBallot(final VipBallot ballot) {
		this.ballot = ballot;
	}

	/**
	 * Sets the ballot placement.
	 * 
	 * @author IanBrown
	 * @param ballotPlacement
	 *            the ballot placement to set.
	 * @since Oct 11, 2012
	 * @version Oct 11, 2012
	 */
	public void setBallotPlacement(final Integer ballotPlacement) {
		this.ballotPlacement = ballotPlacement;
	}

	/**
	 * Sets the election.
	 * 
	 * @author IanBrown
	 * @param election
	 *            the election to set.
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	public void setElection(final VipElection election) {
		this.election = election;
	}

	/**
	 * Sets the electoral district.
	 * 
	 * @author IanBrown
	 * @param electoralDistrict
	 *            the electoral district to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setElectoralDistrict(
	        final VipElectoralDistrict electoralDistrict) {
		this.electoralDistrict = electoralDistrict;
	}

	/**
	 * Sets the number elected.
	 * 
	 * @author IanBrown
	 * @param numberElected
	 *            the number elected to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setNumberElected(final Integer numberElected) {
		this.numberElected = numberElected;
	}

	/**
	 * Sets the number voting for.
	 * 
	 * @author IanBrown
	 * @param numberVotingFor
	 *            the number voting for to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setNumberVotingFor(final Integer numberVotingFor) {
		this.numberVotingFor = numberVotingFor;
	}

	/**
	 * Sets the office.
	 * 
	 * @author IanBrown
	 * @param office
	 *            the office to set.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	public void setOffice(final String office) {
		this.office = office;
	}

	/**
	 * Sets the partisan.
	 * 
	 * @author IanBrown
	 * @param partisan
	 *            the partisan to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setPartisan(final boolean partisan) {
		this.partisan = partisan;
	}

	/**
	 * Sets the primary party.
	 * 
	 * @author IanBrown
	 * @param primaryParty
	 *            the primary party to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setPrimaryParty(final String primaryParty) {
		this.primaryParty = primaryParty;
	}

	/**
	 * Sets the special.
	 * 
	 * @author IanBrown
	 * @param special
	 *            the special to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setSpecial(final boolean special) {
		this.special = special;
	}

	/**
	 * Sets the type.
	 * 
	 * @author IanBrown
	 * @param type
	 *            the type to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Determines the partisan party from the ballot.
	 * 
	 * @author IanBrown
	 * 
	 * @return the partisan party - <code>null</code> if there are no candidates or the candidates do not share a party.
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	private String determinePartisanPartyFromBallot() {
	    return getBallot() == null || getBallot().getCandidates() == null || getBallot().getCandidates().isEmpty() ? null : determinePartisanPartyFromCandidates(getBallot().getCandidates());
    }

	/**
	 * Determines the partisan party from the list of candidates.
	 * 
	 * @author IanBrown
	 * 
	 * @param candidates the candidates.
	 * @return the partisan party - <code>null</code> if the candidates do not share a party.
	 * @since Jul 30, 2013
	 * @version Jul 30, 2013
	 */
	private String determinePartisanPartyFromCandidates(
            final List<VipBallotCandidate> candidates) {
	    String partisanParty = null;
	    for (final VipBallotCandidate candidate : candidates) {
	    	final String candidateParty = candidate.getCandidate().getParty();
	    	if (partisanParty == null) {
	    		partisanParty = candidateParty;
	    	} else if (!partisanParty.equals(candidateParty)) {
	    		return null;
	    	}
	    }
	    return partisanParty;
    }
}
