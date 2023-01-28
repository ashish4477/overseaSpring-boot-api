/**
 * 
 */
package com.bearcode.ovf.model.mobile;


/**
 * Represents a dependency between a {@link MobileVariant} and a {@link MobileQuestion}.
 * 
 * @author IanBrown
 * 
 * @since May 3, 2012
 * @version May 3, 2012
 */
public class MobileDependency {

	/**
	 * the ID of the question that determines whether the variant is shown.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private Long dependsOn;

	/**
	 * the ID of the option for the question that determines whether the variant is shown.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private Long condition;

	/**
	 * Constructs an empty mobile dependency.
	 * 
	 * @author IanBrown
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileDependency() {

	}

	/**
	 * Constructs a mobile dependency for a variant that says it is to show up if the question is answered using the selected
	 * option.
	 * 
	 * @author IanBrown
	 * @param dependsOn
	 *            the question that determines whether the dependency is met.
	 * @param condition
	 *            the condition under which the variant is shown.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public MobileDependency(final Long dependsOn, final Long condition) {
		setDependsOn(dependsOn);
		setCondition(condition);
	}

	/**
	 * Gets the condition.
	 * 
	 * @author IanBrown
	 * @return the condition.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public Long getCondition() {
		return condition;
	}

	/**
	 * Gets the depends on.
	 * 
	 * @author IanBrown
	 * @return the depends on.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public Long getDependsOn() {
		return dependsOn;
	}

	/**
	 * Sets the condition.
	 * 
	 * @author IanBrown
	 * @param condition
	 *            the condition to set.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public void setCondition(final Long condition) {
		this.condition = condition;
	}

	/**
	 * Sets the depends on.
	 * 
	 * @author IanBrown
	 * @param dependsOn
	 *            the depends on to set.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	public void setDependsOn(final Long dependsOn) {
		this.dependsOn = dependsOn;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + getDependsOn() + "=" + getCondition();
	}
}
