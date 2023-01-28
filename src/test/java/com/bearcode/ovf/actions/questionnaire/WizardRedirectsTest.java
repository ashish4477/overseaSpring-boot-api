/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bearcode.ovf.actions.commons.AbstractControllerCheck;

/**
 * Extended {@link AbstractControllerCheck} test for {@link WizardRedirects}.
 * 
 * @author IanBrown
 * 
 * @since Jun 19, 2012
 * @version Jun 19, 2012
 */
public final class WizardRedirectsTest extends AbstractControllerCheck<WizardRedirects> {

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.WizardRedirects#redirectOldRava(java.lang.String)} for the case
	 * where the fwab string is null.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testRedirectOldRava_nullFwab() {
		final String fwab = null;

		final String actualRedirect = getController().redirectOldRava(fwab);

		assertTrue("Redirect to RAVA", actualRedirect.toUpperCase().contains("RAVA"));
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.WizardRedirects#redirectOldRava(java.lang.String)} for the case
	 * where the fwab string is not null.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testRedirectOldRava_nonNullFwab() {
		final String fwab = "not null";

		final String actualRedirect = getController().redirectOldRava(fwab);

		assertTrue("Redirect to FWAB", actualRedirect.toUpperCase().contains("FWAB"));
	}

	/** {@inheritDoc} */
	@Override
	protected final WizardRedirects createController() {
		final WizardRedirects wizardRedirects = new WizardRedirects();
		return wizardRedirects;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForController() {
	}
}
