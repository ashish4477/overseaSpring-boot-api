/**
 * 
 */
package com.bearcode.ovf.tools;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Abstract implementation of {@link ForStateOrVotingRegion}.
 * 
 * @author IanBrown
 * 
 * @since Oct 9, 2012
 * @version Oct 10, 2012
 */
@Component
public abstract class AbstractForStateOrVotingRegion implements ForStateOrVotingRegion {

	/**
	 * Gets the whole states that are supported.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private Collection<String> states;

	/**
	 * Gets the
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private Map<String, Collection<String>> votingRegions;

	/**
	 * the type of voting region (locality type).
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private String votingRegionType;

	/** {@inheritDoc} */
	@Override
	public Collection<String> getStates() {
		return states;
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Collection<String>> getVotingRegions() {
		return votingRegions;
	}

	/** {@inheritDoc} */
	@Override
	public String getVotingRegionType() {
		return votingRegionType;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReady(final String stateIdentification, final String votingRegionName) {
		return isSupported(stateIdentification, votingRegionName)
				&& loadDataIfNeeded(stateIdentification, votingRegionName);
	}

	/** {@inheritDoc} */
	@Override
	public void setStates(final Collection<String> states) {
		this.states = states;
	}

	/** {@inheritDoc} */
	@Override
	public void setVotingRegions(final Map<String, Collection<String>> votingRegions) {
		this.votingRegions = votingRegions;
	}

	/** {@inheritDoc} */
	@Override
	public void setVotingRegionType(final String votingRegionType) {
		this.votingRegionType = votingRegionType;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupported(final String stateIdentification, final String votingRegionName) {
		if (getStates() != null) {
			for (final String state : getStates()) {
				if (state.equalsIgnoreCase(stateIdentification)) {
					return true;
				}
			}
		}

		if (votingRegionName != null && getVotingRegions() != null) {
			final Collection<String> stateVotingRegions = getVotingRegions().get(stateIdentification);
			if (stateVotingRegions == null || stateVotingRegions.isEmpty()) {
				return false;
			}

			final String votingRegionValue = normalizeVotingRegionName(votingRegionName);
			for (final String stateVotingRegion : stateVotingRegions) {
				if (stateVotingRegion.equalsIgnoreCase(votingRegionValue) || stateVotingRegion.equalsIgnoreCase(votingRegionName)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Loads the data for the state or voting region if it is needed.
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the state identification.
	 * @param votingRegionName
	 *            the optional name of the voting region.
	 * @return <code>true</code> if the data is loaded, <code>false</code> if the data cannot be loaded.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected abstract boolean loadDataIfNeeded(String stateIdentification, String votingRegionName);

	/**
	 * Normalizes the name of the voting region by removing the voting region type if it is in the name.
	 * 
	 * @author IanBrown
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the normalized name of the voting region.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	protected String normalizeVotingRegionName(final String votingRegionName) {
		String votingRegionValue = votingRegionName.toUpperCase();
		final String regionType = getVotingRegionType().toUpperCase();
		final int idx = votingRegionValue.indexOf(regionType);
		if (idx >= 0) {
			if (idx + regionType.length() == votingRegionValue.length()) {
				votingRegionValue = votingRegionValue.substring(0, idx).trim();
			} else {
				votingRegionValue = votingRegionValue.substring(0, idx).trim() + " "
						+ votingRegionValue.substring(idx + regionType.length()).trim();
			}
		}
		return votingRegionValue;
	}
}
