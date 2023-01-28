/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.AbstractSupportsStatesOrVotingRegions;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Service to find information about elections.
 * 
 * @author IanBrown
 * 
 * @since Jun 13, 2012
 * @version Oct 1, 2013
 */
@Service
public class ElectionService extends
        AbstractSupportsStatesOrVotingRegions<CandidateFinder> {

	/**
	 * the string used to represent a custom ballot in the contest order.
	 * 
	 * @author IanBrown
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	public static final String CONTEST_CUSTOM = "CUSTOM";

	/**
	 * the string used to represent an office in the contest order.
	 * 
	 * @author IanBrown
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	public static final String CONTEST_OFFICE = "OFFICE";

	/**
	 * the string used to represent a referendum in the contest order.
	 * 
	 * @author IanBrown
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	public static final String CONTEST_REFERENDUM = "REFERENDUM";

	/**
	 * Returns an ordered list of contests based on the contest order provided.
	 * 
	 * @author IanBrown
	 * @param contests
	 *            the contests.
	 * @param contestOrder
	 *            the contest order.
	 * @return the ordered list of contests.
	 * @since Oct 4, 2012
	 * @version Oct 12, 2012
	 */
	public static List<VipContest> orderContests(
	        final List<VipContest> contests, final List<String> contestOrder) {
		if (contestOrder == null || contestOrder.isEmpty()) {
			return contests;
		}

		final List<VipContest> orderedContests = new ArrayList<VipContest>(
		        contests);
		final Comparator<? super VipContest> comparator = new Comparator<VipContest>() {

			/**
			 * the patterns used to match the names.
			 * 
			 * @author IanBrown
			 * @since Oct 4, 2012
			 * @version Oct 4, 2012
			 */
			private final Map<String, Pattern> patterns = new HashMap<String, Pattern>();

			@Override
			public final int compare(final VipContest o1, final VipContest o2) {
				final int idx1 = contestTypeIndex(o1, contestOrder);
				final int idx2 = contestTypeIndex(o2, contestOrder);
				if (idx1 == idx2) {
					final long vipIdDif = o1.getVipId() - o2.getVipId();
					return vipIdDif < 0l ? -1 : vipIdDif > 0l ? 1 : 0;
				}
				return idx1 < idx2 ? -1 : 1;
			}

			private int contestTypeIndex(final VipContest contest,
			        final List<String> contestOrder) {
				if (contestOrder == null || contestOrder.isEmpty()) {
					return 0;
				}

				final VipBallot ballot = contest.getBallot();
				int index = 0;
				for (final String contestType : contestOrder) {
					final String[] typeValues = contestType.split(":\\s*");
					final String ballotType = typeValues[0];
					final String string = typeValues[1];
					final Pattern pattern = findPattern(string);

					if (ElectionService.CONTEST_OFFICE
					        .equalsIgnoreCase(ballotType)) {
						if (ballot.getCustomBallot() == null
						        && ballot.getReferendum() == null) {
							final Matcher matcher = pattern.matcher(contest
							        .getOffice());
							if (matcher.matches()) {
								return index;
							}
						}

					} else if (ElectionService.CONTEST_REFERENDUM
					        .equalsIgnoreCase(ballotType)
					        && ballot.getReferendum() != null
					        || ElectionService.CONTEST_CUSTOM
					                .equalsIgnoreCase(ballotType)
					        && ballot.getCustomBallot() != null) {
						final VipElectoralDistrict electoralDistrict = contest
						        .getElectoralDistrict();
						final Matcher matcher = pattern
						        .matcher(electoralDistrict.getType());
						if (matcher.matches()) {
							return index;
						}
					}

					++index;
				}

				return index;
			}

			private Pattern findPattern(final String string) {
				Pattern pattern = patterns.get(string);
				if (pattern == null) {
					pattern = Pattern.compile(string, Pattern.CASE_INSENSITIVE
					        | Pattern.UNICODE_CASE);
					patterns.put(string, pattern);
				}
				return pattern;
			}
		};
		Collections.sort(orderedContests, comparator);
		return orderedContests;
	}

	/**
	 * the available candidate finders to be used based on the states.
	 * 
	 * @author IanBrown
	 * @since Jun 13, 2012
	 * @version Jun 13, 2012
	 */
	@Autowired(required = false)
	private List<CandidateFinder> candidateFinders;

	/**
	 * Returns the order in which the contests should be displayed on the FWAB
	 * and What's on my ballot? pages. Standard federal
	 * offices (president, us senate, us representative) do not need to appear
	 * and will be ignored as these are automatically
	 * recognized in a specific order.
	 * <p>
	 * The names used here take the form:
	 * <p>
	 * &lt;type&gt;: &lt;name&gt;
	 * <p>
	 * Where type is one of:
	 * <ul>
	 * <li>OFFICE - indicates a contest for an elected office</li>
	 * <li>REFERENDUM - indicates a contest for a referendum or ballot question</li>
	 * <li>CUSTOM - indicates a contest for a custom question</li>
	 * </ul>
	 * And name is either the name of the office or a * (for all offices, or for
	 * referendum and custom).
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the list of contest names. If this is <code>null</code>, the
	 *         order of the contests is undefined.
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	public List<String> contestOrder(final String state,
	        final String votingRegionName) {
		final CandidateFinder candidateFinder = chooseCandidateFinder(state,
		        votingRegionName);

		return candidateFinder == null ? null : candidateFinder
		        .getContestOrder();
	}

	/**
	 * Finds the candidate bio for the specified candidate VIP identifier.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegionName
	 *            the name of the voting region (may be <code>null</code>).
	 * @param candidateVipId
	 *            the candidate VIP identifier.
	 * @return the candidate bio.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 16, 2012
	 * @version Oct 4 2012
	 */
	public VipCandidateBio findCandidateBio(final String state,
	        final String votingRegionName, final long candidateVipId)
	        throws Exception {
		final CandidateFinder candidateFinder = chooseCandidateFinder(state,
		        votingRegionName);

		return candidateFinder == null ? null : candidateFinder
		        .findCandidateBio(candidateVipId);
	}

	/**
	 * Finds the contests for the specified address for elections in the current
	 * year.
	 * 
	 * @author IanBrown
	 * @param validAddress
	 *            the validated address.
	 * @return the contests.
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jun 13, 2012
	 * @version Oct 4, 2012
	 */
	public List<VipContest> findContests(final ValidAddress validAddress)
	        throws Exception {
		final CandidateFinder candidateFinder = chooseCandidateFinder(validAddress);

		return candidateFinder == null ? null : candidateFinder
		        .findContests(validAddress);
	}

	/**
	 * Finds the election for the specified state abbreviation.
	 * 
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the election for the state.
	 */
	public VipElection findElection(String stateAbbreviation,
	        String votingRegionName) {
		final CandidateFinder candidateFinder = chooseCandidateFinder(
		        stateAbbreviation, votingRegionName);
		return candidateFinder
		        .findElection(stateAbbreviation, votingRegionName);
	}

	/**
	 * Finds the partisan parties for the input list of contests.
	 * 
	 * @author Ian Brown
	 * @param contests
	 *            the contests.
	 * @return the partisan parties.
	 * @since May 10, 2013
	 * @version Oct 1, 2013
	 */
	public List<VipContest> findPartisanContests(final List<VipContest> contests) {
		final List<VipContest> partisanContests = new ArrayList<VipContest>();
		for (final VipContest contest : contests) {
			final String partisanParty = contest.isPartisan() ? contest.getPartisanParty() : null;
			if ((partisanParty != null) && !partisanParty.isEmpty()) {
				partisanContests.add(contest);
			} else if ("PRIMARY".equalsIgnoreCase(contest.getType())) {
				partisanContests.add(contest);
			}
		}
		return partisanContests;
	}

	/**
	 * Finds the referendum detail for the specified referendum VIP identifier.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state to search.
	 * @param votingRegionName
	 *            the name of the voting region (may be <code>null</code>).
	 * @param referendumVipId
	 *            the referendum VIP identifier.
	 * @return the referendum detail.
	 * @since Aug 17, 2012
	 * @version Oct 4, 2012
	 */
	public VipReferendumDetail findReferendumDetail(final String state,
	        final String votingRegionName, final long referendumVipId) {
		final CandidateFinder candidateFinder = chooseCandidateFinder(state,
		        votingRegionName);

		return candidateFinder == null ? null : candidateFinder
		        .findReferendumDetail(referendumVipId);
	}

	/** {@inheritDoc} */
	@Override
	public List<CandidateFinder> getForStateOrVotingRegions() {
		return candidateFinders;
	}

	/** {@inheritDoc} */
	@Override
	public void setForStateOrVotingRegions(
	        final List<CandidateFinder> forStateOrVotingRegions) {
		this.candidateFinders = forStateOrVotingRegions;
	}

	/**
	 * Finds the proper candidate finder for the input state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @since Aug 16, 2012
	 * @version Oct 10, 2012
	 */
	private CandidateFinder chooseCandidateFinder(final String state,
	        final String votingRegionName) {
		final String votingRegion = votingRegionName == null ? null
		        : votingRegionName.toUpperCase();
		for (final CandidateFinder candidateFinder : getForStateOrVotingRegions()) {
			final Collection<String> states = candidateFinder.getStates();
			if (states != null && states.contains(state)) {
				return candidateFinder;
			}

			if (votingRegion != null) {
				final Map<String, Collection<String>> votingRegions = candidateFinder
				        .getVotingRegions();
				if (votingRegions != null) {
					final Collection<String> stateVotingRegions = votingRegions
					        .get(state);
					if (stateVotingRegions.contains(votingRegion)) {
						return candidateFinder;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Finds the proper candidate finder for the valid address.
	 * 
	 * @author IanBrown
	 * @param validAddress
	 *            the valid address.
	 * @return the candidate finder or <code>null</code>.
	 * @since Jun 13, 2012
	 * @version Sep 17, 2012
	 */
	private CandidateFinder chooseCandidateFinder(
	        final ValidAddress validAddress) {
		if (getForStateOrVotingRegions() != null) {
			final VipStreetSegment streetSegment = validAddress
			        .getStreetSegment();
			if (streetSegment != null) {
				final String abbr = streetSegment.getNonHouseAddress()
				        .getState();
				String votingRegionName = null;
				final VipPrecinct precinct = streetSegment.getPrecinct();
				if (precinct != null) {
					final VipLocality locality = precinct.getLocality();
					votingRegionName = locality.getName();
				}
				return chooseCandidateFinder(abbr, votingRegionName);
			}
		}

		return null;
	}
}
