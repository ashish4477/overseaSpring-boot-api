/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.util.List;
import org.springframework.stereotype.Component;

import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.tools.ForStateOrVotingRegion;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link ForStateOrVotingRegion} interface for objects that find
 * information about candidates.
 * 
 * @author IanBrown
 * 
 * @since Jun 11, 2012
 * @version Jul 30, 2013
 */
@Component
public interface CandidateFinder extends ForStateOrVotingRegion {

	/**
	 * Finds the candidate bio for the specified candidate VIP identifier.
	 * 
	 * @author IanBrown
	 * @param candidateVipId
	 *            the candidate VIP identifier.
	 * @return the candidate bio.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 16, 2012
	 * @version Aug 17, 2012
	 */
	VipCandidateBio findCandidateBio(long candidateVipId) throws Exception;

	/**
	 * Finds the contests for the valid address for all elections in current
	 * election year.
	 * 
	 * @author IanBrown
	 * @param validAddress
	 *            the valid address.
	 * @return the contests.
	 * @throws Exception
	 *             if the contests cannot be found.
	 * @since Jun 11, 2012
	 * @version Aug 9, 2012
	 */
	List<VipContest> findContests(ValidAddress validAddress) throws Exception;

	/**
	 * Finds the election for the specified state and voting region.
	 * 
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the voting region name.
	 * @return the election.
	 */
	VipElection findElection(String stateAbbreviation, String votingRegionName);

	/**
	 * @author IanBrown
	 * @param referendumVipId
	 * @return
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	VipReferendumDetail findReferendumDetail(long referendumVipId);

	/**
	 * Gets the order in which the contests should be displayed on the FWAB and
	 * What's on my ballot? pages. Standard federal offices
	 * (president, us senate, us representative) do not need to appear and will
	 * be ignored as these are automatically recognized in
	 * a specific order.
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
	 * @return the contest order or <code>null</code> if there is no explicit
	 *         order.
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	List<String> getContestOrder();

	/**
	 * Gets the valet for the candidate finder.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	CandidateFinderValet getValet();

	/**
	 * Sets the order in which contests should be displayed on the FWAB and
	 * What's on my ballot? pages.
	 * 
	 * @author IanBrown
	 * @param contestOrder
	 *            the contest order or <code>null</code> if there is no explicit
	 *            order.
	 * @see {@link #getContestOrder()}
	 * @since Oct 4, 2012
	 * @version Oct 4, 2012
	 */
	void setContestOrder(List<String> contestOrder);

	/**
	 * Sets the valet for the candidate finder.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	void setValet(CandidateFinderValet valet);
}
