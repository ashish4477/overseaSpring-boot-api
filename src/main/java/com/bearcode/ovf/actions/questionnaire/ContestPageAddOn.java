/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

/**
 * Extended {@link AllowedForAddOn} interface for pages that display election contests for candidates.
 * @author IanBrown
 *
 * @since Sep 19, 2012
 * @version Sep 19, 2012
 */
public interface ContestPageAddOn extends AllowedForAddOn {

	/**
	 * the in PDF name prefix used for the additional candidates.
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	String ADDITIONAL_CANDIDATE_PREFIX = "ufAdditionalCandidate";
}
