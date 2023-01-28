/**
 * 
 */
package com.bearcode.ovf.model.eod;

/**
 * Witness and notarization requirements.
 * 
 * @author IanBrown
 * 
 * @since Jan 23, 2012
 * @version Feb 3, 2012
 */
public class WitnessNotarizationRequirements {

	/**
	 * requirements notes for notarization and witnesses.
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	private String notarizationWitnessRequirements;

	/**
	 * Gets the notarization/witness requirements.
	 * 
	 * @author IanBrown
	 * @return the notarization/witness requirements.
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	public String getNotarizationWitnessRequirements() {
		return notarizationWitnessRequirements;
	}

	/**
	 * Sets the notarization/witness requirements.
	 * 
	 * @author IanBrown
	 * @param notarizationWitnessRequirements
	 *            the notarization/witness requirements to set.
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	public void setNotarizationWitnessRequirements(final String notarizationWitnessRequirements) {
		this.notarizationWitnessRequirements = notarizationWitnessRequirements;
	}
}
