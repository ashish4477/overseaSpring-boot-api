/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipSource;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link AbstractCandidateFinder} that uses the VIP database.
 * 
 * @author IanBrown
 * 
 * @since Jul 5, 2012
 * @version Jul 30, 2012
 */
@Component
public class CandidateFinderVip extends AbstractCandidateFinder {

	/**
	 * map of state names by abbreviation.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private final Map<String, String> stateNames = new HashMap<String, String>();

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	@Autowired
	private StateService stateService;

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	@Autowired
	private VipService vipService;

	/** {@inheritDoc} */
	@Override
	public VipCandidateBio findCandidateBio(final long candidateVipId) throws Exception {
		final VipSource source = getVipService().findLatestSource();
		return getVipService().findCandidateBioBySourceAndVipId(source, candidateVipId);
	}

	/** {@inheritDoc} */
	@Override
	public List<VipContest> findContests(final ValidAddress validAddress) throws Exception {
		final VipStreetSegment streetSegment = validAddress.getStreetSegment();
		return findContestsForStreetSegment(streetSegment);
	}

	/** {@inheritDoc} */
	@Override
	public VipReferendumDetail findReferendumDetail(final long referendumVipId) {
		final VipSource source = getVipService().findLatestSource();
		return getVipService().findReferendumDetailBySourceAndVipId(source, referendumVipId);
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	public StateService getStateService() {
		return stateService;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	public VipService getVipService() {
		return vipService;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	public void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}

	/**
	 * Sets the VIP service.
	 * 
	 * @author IanBrown
	 * @param vipService
	 *            the VIP service to set.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	public void setVipService(final VipService vipService) {
		this.vipService = vipService;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean loadDataIfNeeded(final String stateIdentification, final String votingRegionName) {
		final VipSource source = findSourceForState(stateIdentification);
		if (source != null) {
			if (votingRegionName == null) {
				return true;
			}

			String stateName = stateNames.get(stateIdentification);
			if (stateName == null) {
				final State state = getStateService().findByAbbreviation(stateIdentification);
				stateName = state.getName();
				stateNames.put(stateIdentification, stateName);
			}
			final VipState vipState = getVipService().findStateBySourceAndName(source, stateName);
			return vipState != null;

			// This appears to be fraught with danger due to the fact that some localities seem to span state lines. For now, I'm
			// turning this off.
			// final List<VipLocality> localities = getVipService().findLocalitiesByStateAndType(vipState, "County");
			// final String normalizedVotingRegion = normalizeVotingRegionName(votingRegionName);
			// for (final VipLocality locality : localities) {
			// if (votingRegionName.equalsIgnoreCase(locality.getName())
			// || normalizedVotingRegion.equalsIgnoreCase(locality.getName())) {
			// return true;
			// }
			// }
		}

		return false;
	}

	/**
	 * Adds the contests for the electoral district.
	 * 
	 * @author IanBrown
	 * @param electoralDistrict
	 *            the electoral district.
	 * @param contests
	 *            the contests.
	 * @since Jul 9, 2012
	 * @version Aug 9, 2012
	 */
	private void addContestsForElectoralDistrict(final VipElectoralDistrict electoralDistrict, final List<VipContest> contests) {
		final List<VipContest> electoralDistrictContests = getVipService().findContestsForElectoralDistrict(electoralDistrict);
		contests.addAll(electoralDistrictContests);
	}

	/**
	 * Adds the contests for the input electoral districts.
	 * 
	 * @author IanBrown
	 * @param electoralDistricts
	 *            the electoral districts.
	 * @param electoralDistrictIds
	 *            the identfiers for the electoral districts already processed.
	 * @param contests
	 *            the collection of contests.
	 * @since Jul 9, 2012
	 * @version Aug 9, 2012
	 */
	private void addContestsForElectoralDistricts(final List<VipElectoralDistrict> electoralDistricts,
			final Set<Long> electoralDistrictIds, final List<VipContest> contests) {
		for (final VipElectoralDistrict electoralDistrict : electoralDistricts) {
			final long vipId = electoralDistrict.getVipId();
			if (!electoralDistrictIds.contains(vipId)) {
				addContestsForElectoralDistrict(electoralDistrict, contests);
				electoralDistrictIds.add(vipId);
			}
		}
	}

	/**
	 * Adds the contests for the precinct.
	 * 
	 * @author IanBrown
	 * @param precinct
	 *            the precinct.
	 * @param electoralDistrictIds
	 *            the identifiers for the electoral districts handled.
	 * @param contests
	 *            the collection of contests.
	 * @since Jul 9, 2012
	 * @version Aug 9, 2012
	 */
	private void addPrecinctContests(final VipPrecinct precinct, final Set<Long> electoralDistrictIds,
			final List<VipContest> contests) {
		if (precinct != null) {
			addContestsForElectoralDistricts(precinct.getElectoralDistricts(), electoralDistrictIds, contests);
		}
	}

	/**
	 * Adds the contests for the precinct split.
	 * 
	 * @author IanBrown
	 * @param precinctSplit
	 *            the precinct split (may be <code>null</code>).
	 * @param electoralDistrictIds
	 *            the identifiers for the electoral districts handled.
	 * @param contests
	 *            the collection of contests.
	 * @since Jul 9, 2012
	 * @version Aug 9, 2012
	 */
	private void addPrecinctSplitContests(final VipPrecinctSplit precinctSplit, final Set<Long> electoralDistrictIds,
			final List<VipContest> contests) {
		if (precinctSplit != null) {
			addContestsForElectoralDistricts(precinctSplit.getElectoralDistricts(), electoralDistrictIds, contests);
		}
	}

	/**
	 * Finds the contests for the street segment.
	 * 
	 * @author IanBrown
	 * @param streetSegment
	 *            the street segment.
	 * @return the contests.
	 * @since Jul 6, 2012
	 * @version Oct 12, 2012
	 */
	private List<VipContest> findContestsForStreetSegment(final VipStreetSegment streetSegment) {
		final List<VipContest> contests = new LinkedList<VipContest>();
		if (streetSegment != null) {
			final Set<Long> electoralDistrictIds = new HashSet<Long>();
			addPrecinctContests(streetSegment.getPrecinct(), electoralDistrictIds, contests);
			addPrecinctSplitContests(streetSegment.getPrecinctSplit(), electoralDistrictIds, contests);
			Collections.sort(contests, new Comparator<VipContest>() {

				@Override
				public final int compare(final VipContest o1, final VipContest o2) {
					if (o1.getBallotPlacement() == null) {
						long difference = o1.getVipId() - o2.getVipId();
						return difference < 0l ? -1 : difference > 0l ? 1 : 0;
					} else if (o2.getBallotPlacement() == null) {
						return -1;
					}
					return o1.getBallotPlacement() - o2.getBallotPlacement();
				}	
			});
		}

		return contests;
	}

	/**
	 * Finds the source for the specified state.
	 * 
	 * @author IanBrown
	 * @param stateIdentification
	 *            the state identification.
	 * @return
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private VipSource findSourceForState(final String stateIdentification) {
		final State state = getStateService().findByAbbreviation(stateIdentification);
		return state == null ? null : getVipService().findLatestSource(state.getName());
	}

	/** {@inheritDoc} */
	@Override
    public VipElection findElection(String stateAbbreviation,
            String votingRegionName) {
		final VipSource source = findSourceForState(stateAbbreviation);
		return getVipService().findElectionBySource(source);
    }
}
