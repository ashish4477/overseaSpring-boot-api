/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import java.util.List;

/**
 * Extended {@link MobileList} of mobile questions that may be dependent on the answer to a previous question.
 * 
 * @author IanBrown
 * 
 * @since May 3, 2012
 * @version Sep 10, 2012
 */
public class MobileVariant extends MobileList<MobileQuestion> {

	/**
	 * the list of dependencies that determine whether this variant is shown.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private List<MobileDependency> dependencies;

	/**
	 * Constructs a mobile variant without a title.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileVariant() {

	}

	/**
	 * Constructs a mobile variant with the specified title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title of the variant.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileVariant(final String title) {
		super(title);
	}

	/**
	 * Gets the dependencies.
	 * 
	 * @author IanBrown
	 * @return the dependencies.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public List<MobileDependency> getDependencies() {
		return dependencies;
	}

	/**
	 * Sets the dependencies.
	 * 
	 * @author IanBrown
	 * @param dependencies
	 *            the dependencies to set.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public void setDependencies(final List<MobileDependency> dependencies) {
		this.dependencies = dependencies;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(super.toString());
		if (getDependencies() != null && !getDependencies().isEmpty()) {
			sb.append(" if ").append(getDependencies());
		}
		return sb.toString();
	}
}
