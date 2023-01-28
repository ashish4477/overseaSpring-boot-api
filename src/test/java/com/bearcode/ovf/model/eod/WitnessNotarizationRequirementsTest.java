/**
 * 
 */
package com.bearcode.ovf.model.eod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link WitnessNotarizationRequirements}.
 * 
 * @author IanBrown
 * 
 * @since Jan 23, 2012
 * @version Feb 3, 2012
 */
public final class WitnessNotarizationRequirementsTest {

	/**
	 * the witness/notarization requirements to test.
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	private WitnessNotarizationRequirements witnessNotarizationRequirements;

	/**
	 * Sets up to test the witness/notarization requirements.
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@Before
	public final void setUpWitnessNotarizationRequirements() {
		setWitnessNotarizationRequirements(createWitnessNotarizationRequirements());
	}

	/**
	 * Tears down the witness/notarization requirements after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@After
	public final void tearDownWitnessNotarizationRequirements() {
		setWitnessNotarizationRequirements(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.WitnessNotarizationRequirements#getNotarizationWitnessRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	@Test
	public final void testGetNotarizationWitnessRequirements() {
		final String actualNotarizationWitnessRequirements = getWitnessNotarizationRequirements()
				.getNotarizationWitnessRequirements();

		assertNull("The notarization/witness requirements are not set", actualNotarizationWitnessRequirements);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.WitnessNotarizationRequirements#setNotarizationWitnessRequirements(String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	@Test
	public final void testSetNotarizationWitnessRequirements() {
		final String notarizationWitnessRequirements = "Notarization/Witness Requirements";

		getWitnessNotarizationRequirements().setNotarizationWitnessRequirements(notarizationWitnessRequirements);

		assertEquals("The notarization/witness requirements are set", notarizationWitnessRequirements,
				getWitnessNotarizationRequirements().getNotarizationWitnessRequirements());
	}

	/**
	 * Creates a witness/notarization requirements object.
	 * 
	 * @author IanBrown
	 * @return the witness/notarization requirements object.
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	private WitnessNotarizationRequirements createWitnessNotarizationRequirements() {
		return new WitnessNotarizationRequirements();
	}

	/**
	 * Gets the witness/notarization requirements.
	 * 
	 * @author IanBrown
	 * @return the witness/notarization requirements.
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	private WitnessNotarizationRequirements getWitnessNotarizationRequirements() {
		return witnessNotarizationRequirements;
	}

	/**
	 * Sets the witness/notarization requirements.
	 * 
	 * @author IanBrown
	 * @param witnessNotarizationRequirements
	 *            the witness/notarization requirements to set.
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	private void setWitnessNotarizationRequirements(final WitnessNotarizationRequirements witnessNotarizationRequirements) {
		this.witnessNotarizationRequirements = witnessNotarizationRequirements;
	}

}
