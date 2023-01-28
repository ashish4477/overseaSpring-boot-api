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
 * Implementation of {@link CandidateFinderValet}.
 * 
 * @author IanBrown
 * 
 * @since Jul 2, 2012
 * @version Aug 17, 2012
 */
class CandidateFinderValetImpl implements CandidateFinderValet {

	/** {@inheritDoc} */
	@Override
	public VipBallot acquireBallot() {
		return new VipBallot();
	}

	/** {@inheritDoc} */
	@Override
	public VipCandidate acquireCandidate() {
		return new VipCandidate();
	}

	/** {@inheritDoc} */
	@Override
	public VipCandidateBio acquireCandidateBio() {
		return new VipCandidateBio();
	}

	/** {@inheritDoc} */
	@Override
	public VipContest acquireContest() {
		return new VipContest();
	}

	/** {@inheritDoc} */
	@Override
	public VipElectoralDistrict acquireElectoralDistrict() {
		return new VipElectoralDistrict();
	}

	/** {@inheritDoc} */
	@Override
	public UserAddress acquireUserAddress() {
		return new UserAddress();
	}

}
