package com.bearcode.ovf.tools.pendingregistration;

import java.text.SimpleDateFormat;

/**
 * Date: 20.10.14
 * Time: 22:07
 *
 * @author Leonid Ginzburg
 */
public interface PendingVoterRegistrationDictionary {
    /**
   	 * the title of the question about online data transfer.
   	 *
   	 * @author IanBrown
   	 * @since Nov 28, 2012
   	 * @version Dec 13, 2012
   	 */
   	public final static String ONLINE_DATA_TRANSFER_QUESTION = "Online Data Transfer";

   	/**
   	 * the string used to mark the alternate email address in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String ALTERNATE_EMAIL_ADDRESS = "Alt Email";

   	/**
   	 * the string used to mark the alternate phone number in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String ALTERNATE_PHONE_NUMBER = "Alt Phone";

   	/**
   	 * the string used to mark the birth date in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Dec 13, 2012
   	 */
   	static final String BIRTH_DATE = "Birth";

   	/**
   	 * the string used to mark the city in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String CITY = "City";

   	/**
   	 * the name of the current address group.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_GROUP_CURRENT_ADDRESS = "Current Address ";

   	/**
   	 * the name of the forwarding address group.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_GROUP_FORWARDING_ADDRESS = "Forwarding Address ";

   	/**
   	 * the name of the name column group.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_GROUP_NAME = "Current ";

   	/**
   	 * the name of the previous address group.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_GROUP_PREVIOUS_ADDRESS = "Previous Address ";

   	/**
   	 * the name of the previous name group.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_GROUP_PREVIOUS_NAME = "Previous ";

   	/**
   	 * the name of the voting address group.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_GROUP_VOTING_ADDRESS = "Voting Address ";

   	/**
   	 * the name of the alternate email address column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_ALTERNATE_EMAIL_ADDRESS = "Alternate Email";

   	/**
   	 * the name of the alternate phone number column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_ALTERNATE_PHONE_NUMBER = "Alternate Phone";

	/**
	 * the name of the alternate phone number column.
	 *
	 * @author IanBrown
	 * @since Nov 12, 2012
	 * @version Nov 12, 2012
	 */
	static final String COLUMN_NAME_ALTERNATE_PHONE_NUMBER_TYPE = "Alternate Phone Type";

	/**
   	 * the name of the birth date column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Dec 13, 2012
   	 */
   	static final String COLUMN_NAME_BIRTH_DATE = "Birth Date";

   	/**
   	 * the name of the city column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_CITY = "City";

   	/**
   	 * the name of the country column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_COUNTRY = "Country";

   	/**
   	 * the name for the created date column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_CREATED_DATE = "Created";

   	/**
   	 * the name of the description column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_DESCRIPTION = "Description";

   	/**
   	 * the name of the email address column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_EMAIL_ADDRESS = "Email";

   	/**
   	 * the name of the face prefix column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_FACE_PREFIX = "SHS";

   	/**
   	 * the name of the first name column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_FIRST_NAME = "First Name";

   	/**
   	 * the name of the gender column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_GENDER = "Gender";

   	/**
   	 * the name of the identifier column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_ID = "ID";

   	/**
   	 * the name of the last name column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_LAST_NAME = "Last Name";

   	/**
   	 * the name of the middle name column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_MIDDLE_NAME = "Middle Name";

   	/**
   	 * the name of the phone number column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_PHONE_NUMBER = "Phone";

	/**
	 * the name of the phone number type.
	 *
	 * @author IanBrown
	 * @since Nov 12, 2012
	 * @version Nov 12, 2012
	 */
	static final String COLUMN_NAME_PHONE_TYPE = "Phone Type";

	/**
   	 * the name of the postal code column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_POSTAL_CODE = "Postal Code";

   	/**
   	 * the name of the state or region column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_STATE_OR_REGION = "State or Region";

   	/**
   	 * the name of the street1 column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_STREET1 = "Street1";

   	/**
   	 * the name of the street2 column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_STREET2 = "Street2";

   	/**
   	 * the name of the suffix column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_SUFFIX = "Suffix";

   	/**
   	 * the name of the title column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_TITLE = "Title";

   	/**
   	 * the name of the voter history column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_VOTER_HISTORY = "Voter History";

   	/**
   	 * the name of the type of voter column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_VOTER_TYPE = "Voter Type";

   	/**
   	 * the name of the voting region column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	static final String COLUMN_NAME_VOTING_REGION = "Voting Region";

   	/**
   	 * the name of the voting state column.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 16, 2012
   	 */
   	static final String COLUMN_NAME_VOTING_STATE = "Voting State";

	/**
	 * the name of the voting state column.
	 *
	 * @author IanBrown
	 * @since Nov 12, 2012
	 * @version Nov 16, 2012
	 */
	static final String COLUMN_ACCOUNT_CREATED = "Account Created";

	/**
   	 * the string used to mark the country in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String COUNTRY = "Country";

   	/**
   	 * the format string for displaying the created date.
   	 *
   	 * @author IanBrown
   	 * @since Nov 12, 2012
   	 * @version Nov 13, 2012
   	 */
   	final static SimpleDateFormat CREATED_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");

   	/**
   	 * the string used to mark the description line in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String DESCRIPTION = "Description";

   	/**
   	 * the string used to mark the email address in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String EMAIL_ADDRESS = "Email";

   	/**
   	 * the string used to mark the first name in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String FIRST_NAME = "First Name";

   	/**
   	 * the string used to mark the last name in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String LAST_NAME = "Last Name";

   	/**
   	 * the string used to mark the middle name in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String MIDDLE_NAME = "Middle Name";

   	/**
   	 * the string used to mark the phone number in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String PHONE_NUMBER = "Phone";

	/**
	 * the string used to mark the phone number type in the encrypted string.
	 *
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	static final String PHONE_NUMBER_TYPE = "Type";

	/**
	 * the string used to mark the phone number type in the encrypted string.
	 *
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	static final String ALTERNATE_PHONE_NUMBER_TYPE = "Alt Type";

	/**
   	 * the string used to mark the postal code in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String POSTAL_CODE = "Postal Code";

   	/**
   	 * the string used to mark the state or region in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String STATE_OR_REGION = "State or Region";

   	/**
   	 * the string used to mark the street1 line in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String STREET1 = "Street1";

   	/**
   	 * the string used to mark the street2 line in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String STREET2 = "Street2";

   	/**
   	 * the string used to mark the suffix in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String SUFFIX = "Suffix";

   	/**
   	 * the string used to mark the title in the encrypted string.
   	 *
   	 * @author IanBrown
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	static final String TITLE = "Title";

	/**
	 * name of column shows if user downloaded the form
	 */
	static final String COLUMN_FORM_DOWNLOADED = "Form Downloaded";

}
