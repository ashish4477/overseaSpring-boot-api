/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract extended {@link MobileLevel} representing a list of mobile level objects.
 * 
 * @author IanBrown
 * 
 * @param <L>
 *            the type of mobile level objects in the collection.
 * @since May 3, 2012
 * @version May 3, 2012
 */
public abstract class MobileList<L extends MobileLevel> extends MobileLevel {

	/**
	 * the children of this level.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private List<L> children;

	/**
	 * Constructs a mobile list without a title.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	protected MobileList() {

	}

	/**
	 * Constructs a mobile list with the specified title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	protected MobileList(final String title) {
		super(title);
	}

	/**
	 * Adds the specified child to the list.
	 * 
	 * @author IanBrown
	 * @param child
	 *            the child.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public void addChild(final L child) {
		if (getChildren() == null) {
			setChildren(new LinkedList<L>());
		}

		getChildren().add(child);
	}

	/**
	 * Gets the children.
	 * 
	 * @author IanBrown
	 * @return the children.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public List<L> getChildren() {
		return children;
	}

	/**
	 * Removes the specified child from the list.
	 * 
	 * @author IanBrown
	 * @param child
	 *            the child.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public void removeChild(final L child) {
		if (getChildren() != null) {
			getChildren().remove(child);
		}
	}

	/**
	 * Sets the children of this list.
	 * 
	 * @author IanBrown
	 * @param children
	 *            the children.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public void setChildren(final List<L> children) {
		this.children = children;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(super.toString());
		sb.append(getChildren());
		return sb.toString();
	}
}
