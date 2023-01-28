/**
 * 
 */
package com.bearcode.ovf.actions.eod;


/**
 * Extended {@link ViewLeoControllerCheck} test for {@link ViewLeoStandalone}.
 * 
 * @author IanBrown
 * 
 * @since Jul 25, 2012
 * @version Jul 25, 2012
 */
public final class ViewLeoStandaloneTest extends ViewLeoControllerCheck<ViewLeoStandalone> {

	/** {@inheritDoc} */
	@Override
	protected final ViewLeoStandalone createViewLeoController() {
		final ViewLeoStandalone viewLeoStandalone = new ViewLeoStandalone();
		return viewLeoStandalone;
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
		return "/css/eod-standalone.css";
	}
}
