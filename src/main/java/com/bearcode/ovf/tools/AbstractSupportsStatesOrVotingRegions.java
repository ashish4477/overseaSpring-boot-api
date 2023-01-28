/**
 * 
 */
package com.bearcode.ovf.tools;

import org.springframework.stereotype.Component;

/**
 * Abstract implementation of {@link SupportsStatesOrVotingRegions}.
 * 
 * @author IanBrown
 * 
 * @param <F>
 *            the type of object that performs operations for states or voting regions.
 * @since Oct 9, 2012
 * @version Oct 10, 2012
 */
@Component
public abstract class AbstractSupportsStatesOrVotingRegions<F extends ForStateOrVotingRegion> implements
		SupportsStatesOrVotingRegions<F> {

	/** {@inheritDoc} */
	@Override
	public F identifyForStateOrVotingRegion(final String stateIdentification, final String votingRegionName) {
		if (getForStateOrVotingRegions() != null) {
			for (final F forStateOrVotingRegion : getForStateOrVotingRegions()) {
				if (forStateOrVotingRegion.isSupported(stateIdentification, votingRegionName)) {
					return forStateOrVotingRegion;
				}
			}
		}

		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReady(final String stateIdentification, final String votingRegionName) {
		final F forStateOrVotingRegion = identifyForStateOrVotingRegion(stateIdentification, votingRegionName);
		return forStateOrVotingRegion == null ? false : forStateOrVotingRegion.isReady(stateIdentification, votingRegionName);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupported(final String stateIdentification, final String votingRegionName) {
		return identifyForStateOrVotingRegion(stateIdentification, votingRegionName) != null;
	}
}
