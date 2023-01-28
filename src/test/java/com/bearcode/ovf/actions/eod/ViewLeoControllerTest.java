/**
 * 
 */
package com.bearcode.ovf.actions.eod;

/**
 * Extended {@link ViewLeoControllerCheck} test for {@link ViewLeoController}.
 * 
 * @author IanBrown
 * 
 * @since Dec 23, 2011
 * @version Jul 25, 2012
 */
public final class ViewLeoControllerTest extends ViewLeoControllerCheck<ViewLeoController> {

	/** {@inheritDoc} */
	@Override
	protected final ViewLeoController createViewLeoController() {
		final ViewLeoController viewLeoController = new ViewLeoController();
		return viewLeoController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return DEFAULT_EXPECTED_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isShowEod() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isShowSvid() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForViewLeoController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForViewLeoController() {
	}
}
