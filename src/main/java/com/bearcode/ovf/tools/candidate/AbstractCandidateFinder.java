/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.util.List;

import com.bearcode.ovf.tools.AbstractForStateOrVotingRegion;

/**
 * Abstract extended {@link AbstractForStateOrVotingRegion} implementation of {@link CandidateFinder}.
 * 
 * @author IanBrown
 * 
 * @since Jun 13, 2012
 * @version Oct 10, 2012
 */
public abstract class AbstractCandidateFinder extends AbstractForStateOrVotingRegion implements CandidateFinder {
	
	/**
	 * the order of the contests.
	 * @author IanBrown
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	private List<String> contestOrder;

	/**
	 * the valet for the candidate finder.
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private CandidateFinderValet valet;

	/**
	 * Constructs a candidate finder with a default valet.
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	protected AbstractCandidateFinder() {
		if (getValet() == null) {
			setValet(new CandidateFinderValetImpl());
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getContestOrder() {
		return contestOrder;
	}

	/** {@inheritDoc} */
	@Override
	public CandidateFinderValet getValet() {
		return valet;
	}

	/** {@inheritDoc} */
	@Override
	public void setContestOrder(final List<String> contestOrder) {
		this.contestOrder = contestOrder;
	}

	/** {@inheritDoc} */
	@Override
	public void setValet(final CandidateFinderValet valet) {
		this.valet = valet;
	}
}
