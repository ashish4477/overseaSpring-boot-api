/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;

/**
 * Interface for valet object that acquires necessary resources for {@link CandidateFinder}.
 * 
 * @author IanBrown
 * 
 * @since Jul 2, 2012
 * @version Aug 17, 2012
 */
public interface CandidateFinderValet {

	/**
	 * Acquires a ballot.
	 * 
	 * @author IanBrown
	 * @return the ballot.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	VipBallot acquireBallot();

	/**
	 * Acquires a candidate.
	 * 
	 * @author IanBrown
	 * @return the candidate.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	VipCandidate acquireCandidate();

	/**
	 * Acquires a candidate bio object.
	 * 
	 * @author IanBrown
	 * @return the candidate bio.
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	VipCandidateBio acquireCandidateBio();

	/**
	 * Acquires a contest.
	 * 
	 * @author IanBrown
	 * @return the contest.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	VipContest acquireContest();

	/**
	 * Acquires an electoral distrct.
	 * 
	 * @author IanBrown
	 * @return the electoral district.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	VipElectoralDistrict acquireElectoralDistrict();

	/**
	 * Acquires a user address.
	 * 
	 * @author IanBrown
	 * @return the user address.
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	UserAddress acquireUserAddress();

}
