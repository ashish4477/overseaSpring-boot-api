/**
 * 
 */
package com.bearcode.ovf.actions.eod;

/**
 * Extended {@Link ViewLeoControllerCheck} test for {@link ViewLeoNass}.
 * 
 * @author IanBrown
 * 
 * @since Jul 25, 2012
 * @version Jul 25, 2012
 */
public final class ViewLeoNassTest extends ViewLeoControllerCheck<ViewLeoNass> {

	/** {@inheritDoc} */
	@Override
	protected final ViewLeoNass createViewLeoController() {
		final ViewLeoNass viewLeoNass = new ViewLeoNass();
		return viewLeoNass;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isShowEod() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isShowSvid() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForViewLeoController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForViewLeoController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return DEFAULT_EXPECTED_SECTION_CSS;
	}
}
