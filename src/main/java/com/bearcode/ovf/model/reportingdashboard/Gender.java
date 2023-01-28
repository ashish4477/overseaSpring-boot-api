/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

/**
 * Enumeration of the gender values.
 * 
 * @author IanBrown
 * 
 * @since Apr 3, 2012
 * @version Apr 3, 2012
 */
public enum Gender {

	/**
	 * female gender.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	FEMALE("F", "Female"),

	/**
	 * male gender.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	MALE("M", "Male");

	/**
	 * Returns the gender corresponding to the input database value.
	 * 
	 * @author IanBrown
	 * @param databaseValue
	 *            the database value.
	 * @return the gender.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	public static final Gender fromDatabaseValue(final String databaseValue) {
		for (final Gender gender : Gender.values()) {
			if (gender.getDatabaseValue().equals(databaseValue)) {
				return gender;
			}
		}

		return null;
	}

	/**
	 * Gets the gender for the input display value.
	 * 
	 * @author IanBrown
	 * @param displayValue
	 *            the display value.
	 * @return the gender.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	public final static Gender fromDisplayValue(final String displayValue) {
		for (final Gender gender : Gender.values()) {
			if (gender.getDisplayValue().equals(displayValue)) {
				return gender;
			}
		}

		return null;
	}

	/**
	 * the value entered into the database.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	private final String databaseValue;

	/**
	 * the value displayed on the reports.
	 * 
	 * @author IanBrown
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	private final String displayValue;

	/**
	 * Constructs a gender object with the specified database and display values.
	 * 
	 * @author IanBrown
	 * @param databaseValue
	 *            the value entered into the database.
	 * @param displayValue
	 *            the displayed on the reports.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	private Gender(final String databaseValue, final String displayValue) {
		this.databaseValue = databaseValue;
		this.displayValue = displayValue;
	}

	/**
	 * Gets the value entered in the database.
	 * 
	 * @author IanBrown
	 * @return the database value.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	public final String getDatabaseValue() {
		return databaseValue;
	}

	/**
	 * Gets the value to be displayed on the reports.
	 * 
	 * @author IanBrown
	 * @return the display value.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	public final String getDisplayValue() {
		return displayValue;
	}
}
