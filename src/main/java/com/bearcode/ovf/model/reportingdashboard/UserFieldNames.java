/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import com.bearcode.ovf.model.common.VoterHistory;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.questionnaire.FlowType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enumeration of the standard user fields.
 * 
 * @author IanBrown
 * 
 * @since Feb 7, 2012
 * @version May 15, 2012
 */
public enum UserFieldNames {

	/**
	 * the birth month of the voter.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Apr 2, 2012
	 */
	BIRTH_MONTH("Birth Month", "birth_month", null, "pdf_answers", "pdfs", Integer.class),

	/**
	 * the birth year of the voter.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Apr 2, 2012
	 */
	BIRTH_YEAR("Birth Year", "birth_year", null, "pdf_answers", "pdfs", Integer.class),

	/**
	 * has the user completed the form and downloaded it?
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version May 15, 2012
	 */
	COMPLETED("Completed", "downloaded", null, "pdf_answers", "pdfs", Boolean.class),

	/**
	 * the type of flow.
	 * 
	 * @author IanBrown
	 * @since Apr 2, 2012
	 * @version Apr 2, 2012
	 */
	FLOW_TYPE("Flow Type", "flow_type", null, "pdf_answers", "pdfs", FlowType.class),

	/**
	 * the gender of the voter.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Apr 3, 2012
	 */
	GENDER("Gender", "gender", null, "pdf_answers", "pdfs", Gender.class),

	/**
	 * the hosted site.
	 * 
	 * @author IanBrown
	 * @since Apr 2, 2012
	 * @version Apr 2, 2012
	 */
	HOSTED_SITE("Hosted Site", "face_name", null, "pdf_answers", "pdfs", String.class),

	/**
	 * the mobile application flag.
	 * 
	 * @author IanBrown
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	MOBILE("Mobile", "mobile", null, "pdf_answers", "pdfs", Boolean.class),

	/**
	 * the type of mobile device.
	 * 
	 * @author IanBrown
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	MOBILE_DEVICE_TYPE("Mobile Device Type", "mobile_device", null, "pdf_answers", "pdfs", String.class),

	/**
	 * the title of the wizard questions page.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Apr 2, 2012
	 */
	PAGE_TITLE("Page Title", "page_title", null, "pdf_answers", "pdfs", String.class),

	/**
	 * the voter's history.
	 * 
	 * @author IanBrown
	 * @since Mar 30, 2012
	 * @version Apr 2, 2012
	 */
	VOTER_HISTORY("Voter History", "voter_history", null, "pdf_answers", "pdfs", VoterHistory.class),

	/**
	 * the type of voter.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Apr 2, 2012
	 */
	VOTER_TYPE("Voter Type", "voter_type", null, "pdf_answers", "pdfs", VoterType.class),

	/**
	 * the name of the voting region.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Apr 2, 2012
	 */
	VOTING_REGION_NAME("Voting Region Name", "voting_region_name", null, "pdf_answers", "pdfs", String.class),

	/**
	 * the name of the state containing the voting region.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	VOTING_REGION_STATE("Voting Region State", "voting_region_state", null, "pdf_answers", "pdfs", String.class),

		/**
		 * the name of the country where voter lives.
		 *
		 */
        VOTER_COUNTRY("Voter Country", "country", "current_address_id", "wizard_result_addresses", "waddr", String.class);

	/**
	 * Finds the user field entry that corresponds to the input user interface name.
	 * 
	 * @author IanBrown
	 * @param uiName
	 *            the user interface name.
	 * @return the user field name.
	 * @since Mar 13, 2012
	 * @version Mar 13, 2012
	 */
	public static final UserFieldNames findByUiName(final String uiName) {
		for (final UserFieldNames userFieldName : values()) {
			if (userFieldName.getUiName().equals(uiName)) {
				return userFieldName;
			}
		}

		return null;
	}

	/**
	 * Finds the user field entry that corresponds to the input user field name (if any).
	 * <p>
	 * This method takes the user field name and splits it into an optional table specification and a field name. If both are
	 * supplied, it compares the field name to each entry in the enumeration. If that matches, then it compares the table
	 * specification to the alias and then to the table name of the entry found. If that matches, then the enumeration value is
	 * returned. If there is no table specification, then the code will match either an entry with an alias of 'pdfs' or the first
	 * entry with a matching field name.
	 * 
	 * @author IanBrown
	 * @param userFieldName
	 *            the user field name (can be one of 'fieldName', 'alias.fieldName', or 'tableName.fieldName'.
	 * @return the user field.
	 * @since Feb 7, 2012
	 * @version Apr 2, 2012
	 */
	public static final UserFieldNames findByUserFieldName(final String userFieldName) {
		UserFieldNames pdfsMatch = null;
		UserFieldNames firstMatch = null;
		UserFieldNames exactMatch = null;
		final String parts[] = userFieldName.split("\\Q.\\E");
		if ((parts.length < 1) || (parts.length > 2)) {
			return null;
		}
		final int tableIndex = (parts.length == 1) ? -1 : 0;
		final int fieldIndex = (parts.length == 1) ? 0 : 1;

		for (final UserFieldNames userField : values()) {
			if (parts[fieldIndex].equals(userField.getSqlName())) {
				if (tableIndex == -1) {
					if (userField.getAlias().equals("pdfs")) {
						pdfsMatch = userField;
					} else if (firstMatch == null) {
						firstMatch = userField;
					}
				} else {
					if (userField.getAlias().equals(parts[tableIndex])) {
						exactMatch = userField;
						break;
					} else if (userField.getTableName().equals(parts[tableIndex])) {
						exactMatch = userField;
						break;
					}
				}
			}
		}

		return (exactMatch == null) ? ((pdfsMatch == null) ? firstMatch : pdfsMatch) : exactMatch;
	}

	/**
	 * the alias for the table.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private final String alias;

	/**
	 * the class of the data for the field.
	 * 
	 * @author IanBrown
	 * @since Apr 2, 2012
	 * @version Apr 2, 2012
	 */
	private final Class<?> dataClass;

	/**
	 * the name of the field to be used in the SQL queries.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private final String sqlName;

    /**
     * the name of the key filed to be used in the JOIN section of the SQL query
     */
    private final String sqlKeyName;

	/**
	 * the name of the table containing the data.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private final String tableName;

	/**
	 * the name of the field to be shown on the user interface.
	 * 
	 * @author IanBrown
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	private final String uiName;

	/**
	 * Constructs a users field object with the specified user interface and SQL query names.
	 * 
	 * @author IanBrown
	 * @param uiName
	 *            the name of the field to be shown on the user interface.
	 * @param sqlName
	 *            the name of the field to be used in the SQL queries.
	 * @param sqlKeyName
     *            the name of the key filed to be used in the JOIN section of the SQL query
     *@param tableName
     *            the name of the table containing the data.
     * @param alias
*            the alias for the table.
     * @param dataClass
*            the class of the data.    @since Feb 7, 2012
	 * @version Apr 2, 2012
	 */
	private UserFieldNames(final String uiName, final String sqlName, String sqlKeyName, final String tableName, final String alias,
                           final Class<?> dataClass) {
		this.uiName = uiName;
		this.sqlName = sqlName;
        this.sqlKeyName = sqlKeyName;
        this.tableName = tableName;
		this.alias = alias;
		this.dataClass = dataClass;
	}

	/**
	 * Converts the input value read from the database to the corresponding display string.
	 * 
	 * @author IanBrown
	 * @param databaseValue
	 *            the database value.
	 * @return the display string.
	 * @since Apr 2, 2012
	 * @version Apr 8, 2012
	 */
	public final String convertDatabaseToDisplay(final Object databaseValue) {
		if (databaseValue == null) {
			return "(no value)";

		} else if (Boolean.class.equals(dataClass)) {
			final String displayValue;
			if (databaseValue instanceof Number) {
				displayValue = (((Number) databaseValue).intValue() == 1) ? "Yes" : "No";
			} else {
				displayValue = (Integer.valueOf(databaseValue.toString()) == 1) ? "Yes" : "No";
			}
			return displayValue;

		} else if (FlowType.class.equals(dataClass)) {
			final FlowType flowType;
			if (databaseValue instanceof FlowType) {
				flowType = (FlowType) databaseValue;
			} else {
				flowType = FlowType.valueOf(databaseValue.toString());
			}
			if (flowType == FlowType.RAVA) {
				return "FPCA";
			}
			return (flowType == null) ? "(no value)" : flowType.name();

		} else if (Gender.class.equals(dataClass)) {
			final Gender gender;
			if (databaseValue instanceof Gender) {
				gender = (Gender) databaseValue;
			} else {
				gender = Gender.fromDatabaseValue(databaseValue.toString());
			}
			return (gender == null) ? "(no value)" : gender.getDisplayValue();

		} else if (VoterHistory.class.equals(dataClass)) {
			try {
				final VoterHistory voterHistory;
				if (databaseValue instanceof VoterHistory) {
					voterHistory = (VoterHistory) databaseValue;
				} else {
					voterHistory = VoterHistory.valueOf(databaseValue.toString());
				}
				return (voterHistory == null) ? "(no value)" : voterHistory.getValue();
			} catch (final IllegalArgumentException e) {
				return "(no value)";
			}

		} else if (VoterType.class.equals(dataClass)) {
			try {
				final VoterType voterType;
				if (databaseValue instanceof VoterType) {
					voterType = (VoterType) databaseValue;
				} else {
					voterType = VoterType.valueOf(databaseValue.toString());
				}
				return (voterType == null) ? "(no value)" : voterType.getTitle();
			} catch (final IllegalArgumentException e) {
				return "(no value)";
			}
		}

		return databaseValue.toString();
	}

	/**
	 * Converts the input display value to the corresponding database value.
	 * 
	 * @author IanBrown
	 * @param displayValue
	 *            the display value.
	 * @return the display value.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	public String convertDisplayToDatabaseValue(final String displayValue) {
		String databaseValue = null;

		if (Boolean.class.equals(getDataClass())) {
			databaseValue = displayValue.equals("Yes") ? "1" : "0";

		} else if (FlowType.class.equals(getDataClass())) {
			// Cannot select flow types here - the flow type is part of the settings page.

		} else if (Gender.class.equals(getDataClass())) {
			final Gender gender = Gender.fromDisplayValue(displayValue);
			databaseValue = gender.getDatabaseValue();

		} else if (this == HOSTED_SITE) {
			// Cannot select hosted site (face) here - the hosted site is part of the settings page.

		} else if (VoterHistory.class.equals(getDataClass())) {
			for (final VoterHistory voterHistory : VoterHistory.values()) {
				if (voterHistory.getValue().equals(displayValue)) {
					databaseValue = voterHistory.name();
					break;
				}
			}

		} else if (VoterType.class.equals(getDataClass())) {
			for (final VoterType voterType : VoterType.values()) {
				if (voterType.getTitle().equals(displayValue)) {
					databaseValue = voterType.name();
					break;
				}
			}
		}

		return databaseValue;
	}

	/**
	 * Gets the alias for the table.
	 * 
	 * @author IanBrown
	 * @return the alias.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	public final String getAlias() {
		return alias;
	}

	/**
	 * Gets the list of valid answers for the user field.
	 * 
	 * @author IanBrown
	 * @return the list of valid answers, an empty list there are no predefined answers, or <code>null</code> if no answers can be
	 *         selected.
	 * @since Apr 3, 2012
	 * @version Apr 3, 2012
	 */
	public final List<String> getAnswers() {
		final List<String> answers = new ArrayList<String>();
		if (Boolean.class.equals(getDataClass())) {
			answers.addAll(Arrays.asList("Yes", "No"));

		} else if ((this == FLOW_TYPE) || (this == HOSTED_SITE)) {
			return null;

		} else if (Gender.class.equals(getDataClass())) {
			for (final Gender gender : Gender.values()) {
				answers.add(gender.getDisplayValue());
			}

		} else if (VoterHistory.class.equals(getDataClass())) {
			for (final VoterHistory voterHistory : VoterHistory.values()) {
				answers.add(voterHistory.getValue());
			}

		} else if (VoterType.class.equals(getDataClass())) {
			for (final VoterType voterType : VoterType.values()) {
				answers.add(voterType.getTitle());
			}
		}

		return answers;
	}

	/**
	 * Gets the data class for the field.
	 * 
	 * @author IanBrown
	 * @return the data class.
	 * @since Apr 2, 2012
	 * @version Apr 2, 2012
	 */
	public final Class<?> getDataClass() {
		return dataClass;
	}

	/**
	 * Gets the name of the field to be used in the SQL queries.
	 * 
	 * @author IanBrown
	 * @return the SQL name.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	public final String getSqlName() {
		return sqlName;
	}

	/**
	 * Gets the name of the table.
	 * 
	 * @author IanBrown
	 * @return the table name.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	public final String getTableName() {
		return tableName;
	}

	/**
	 * Gets the name of the field to be shown on the user interface.
	 * 
	 * @author IanBrown
	 * @return the user interface name.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	public final String getUiName() {
		return uiName;
	}

    /**
     * Get the key field name
     * @return  the key field name
     */
    public String getSqlKeyName() {
        return sqlKeyName;
    }
}