/**
 * 
 */
package com.bearcode.ovf.tools.candidate;


/**
 * Extended {@link CandidateFinderValetCheck} test for {@link CandidateFinderValetImpl}.
 * 
 * @author IanBrown
 * 
 * @since Jul 3, 2012
 * @version Jul 3, 2012
 */
public final class CandidateFinderValetImplTest extends CandidateFinderValetCheck<CandidateFinderValetImpl> {

	/** {@inheritDoc} */
	@Override
	protected final CandidateFinderValetImpl createValet() {
		return new CandidateFinderValetImpl();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForValet() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForValet() {
	}
}
