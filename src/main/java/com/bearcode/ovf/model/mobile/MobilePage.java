/**
 * 
 */
package com.bearcode.ovf.model.mobile;

/**
 * Extended {@link MobileList} representing a page of mobile groups.
 * 
 * @author IanBrown
 * 
 * @since May 3, 2012
 * @version May 3, 2012
 */
public class MobilePage extends MobileList<MobileGroup> {

	/**
	 * Constructs a mobile page without a title.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobilePage() {

	}

	/**
	 * Constructs a mobile page with the specified title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title of the page.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobilePage(final String title) {
		super(title);
	}
}
