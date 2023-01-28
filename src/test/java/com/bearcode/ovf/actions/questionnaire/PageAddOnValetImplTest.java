/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

/**
 * Extended {@link PageAddOnValetCheck} test for {@link PageAddOnValetImpl}.
 * @author IanBrown
 *
 * @since Aug 8, 2012
 * @version May 7, 2013
 */
public final class PageAddOnValetImplTest extends PageAddOnValetCheck<PageAddOnValetImpl> {

	/** {@inheritDoc} */
	@Override
	protected final PageAddOnValetImpl createValet() {
		return (PageAddOnValetImpl) PageAddOnValetImpl.getInstance();
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
