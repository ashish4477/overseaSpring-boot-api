/**
 * 
 */
package com.bearcode.ovf.model.mobile;

/**
 * Extended {@link MobileList} representing a group of variants.
 * 
 * @author IanBrown
 * 
 * @since May 3, 2012
 * @version May 3, 2012
 */
public class MobileGroup extends MobileList<MobileVariant> {

	/**
	 * Constructs a mobile group without a title.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileGroup() {

	}

	/**
	 * Constructs a mobile group with a title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileGroup(final String title) {
		super(title);
	}
}
