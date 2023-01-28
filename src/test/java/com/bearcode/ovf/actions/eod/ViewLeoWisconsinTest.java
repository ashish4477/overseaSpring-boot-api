/**
 * 
 */
package com.bearcode.ovf.actions.eod;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Extended {@link ViewLeoControllerCheck} test for {@link ViewLeoWisconsin}.
 * @author IanBrown
 *
 * @since Jul 25, 2012
 * @version Jul 25, 2012
 */
public final class ViewLeoWisconsinTest extends ViewLeoControllerCheck<ViewLeoWisconsin> {

	/** {@inheritDoc} */
	@Override
	protected final ViewLeoWisconsin createViewLeoController() {
		ViewLeoWisconsin viewLeoWisconsin = new ViewLeoWisconsin();
		return viewLeoWisconsin;
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

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return DEFAULT_EXPECTED_SECTION_CSS;
	}
}
