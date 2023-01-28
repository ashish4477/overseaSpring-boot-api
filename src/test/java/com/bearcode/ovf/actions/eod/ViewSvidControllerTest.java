/**
 * 
 */
package com.bearcode.ovf.actions.eod;


/**
 * Extended {@link ViewLeoControllerCheck} test for {@link ViewSvidController}.
 * 
 * @author IanBrown
 * 
 * @since Jul 25, 2012
 * @version Jul 25, 2012
 */
public final class ViewSvidControllerTest extends ViewLeoControllerCheck<ViewSvidController> {

	/** {@inheritDoc} */
	@Override
	protected final ViewSvidController createViewLeoController() {
		final ViewSvidController viewSvidController = new ViewSvidController();
		return viewSvidController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return DEFAULT_EXPECTED_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isShowEod() {
		return false;
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
