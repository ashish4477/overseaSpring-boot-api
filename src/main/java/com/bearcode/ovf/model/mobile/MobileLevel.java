/**
 * 
 */
package com.bearcode.ovf.model.mobile;

/**
 * Abstract base class for the mobile levels.
 * 
 * @author IanBrown
 * 
 * @since May 3, 2012
 * @version May 3, 2012
 */
public abstract class MobileLevel {

	/**
	 * the title of the level.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private String title;

	/**
	 * Constructs a mobile level without a title.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	protected MobileLevel() {

	}

	/**
	 * Constructs a mobile level with the specified title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	protected MobileLevel(final String title) {
		setTitle(title);
	}

	/**
	 * Builds a string representation of this mobile level using the prefix.
	 * 
	 * @author IanBrown
	 * @param prefix
	 *            the prefix.
	 * @return the string representation.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public String buildString(final String prefix) {
		final StringBuilder sb = new StringBuilder(prefix);
		sb.append(getClass().getSimpleName()).append(" ").append(getTitle());
		return sb.toString();
	}

	/**
	 * Gets the title.
	 * 
	 * @author IanBrown
	 * @return the title.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + getTitle();
	}
}
