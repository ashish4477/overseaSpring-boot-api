/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;


/**
 * Extended {@link StreetSegmentFinderMelissaValetCheck} test for {@link StreetSegmentFinderMelissaValetImpl}.
 * 
 * @author IanBrown
 * 
 * @since Sep 5, 2012
 * @version Sep 5, 2012
 */
public final class StreetSegmentFinderMelissaValetImplTest extends
		StreetSegmentFinderMelissaValetCheck<StreetSegmentFinderMelissaValetImpl> {

	/** {@inheritDoc} */
	@Override
	protected final StreetSegmentFinderMelissaValetImpl createValet() {
		return (StreetSegmentFinderMelissaValetImpl) StreetSegmentFinderMelissaValetImpl.getInstance();
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
