package com.bearcode.ovf.utils;

import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Utility to map common user account information to specific Answer fields
 *
 * @author dhughes
 */
@Component
public class UserInfoFields {
    private static final String US_REGION = "US";
    private static final String EMPTY_STRING = "";
    private final Logger logger = LoggerFactory.getLogger( UserInfoFields.class );

    private static final String TITLE_NAME = "Title";
	private static final String PREVIOUS_TITLE = "Previous Title";
	private static final String PREVIOUS_SUFFIX = "Previous Suffix";
	private static final String PREVIOUS_MIDDLE_NAME = "Previous Middle Name";
	private static final String PREVIOUS_LAST_NAME = "Previous Last Name";
	private static final String PREVIOUS_FIRST_NAME = "Previous First Name";

    public static final String VOTING_ADDRESS_PREFIX = "Voting ";
    public static final String CURRENT_ADDRESS_PREFIX = "Current ";
    public static final String FORWARD_ADDRESS_PREFIX = "Forwarding ";
    public static final String MAILING_ADDRESS_PREFIX = "Mailing ";
    public static final String PREVIOUS_ADDRESS_PREFIX = "Previous ";

	public static final String VOTING_ADDRESS = "Voting Address";
    public static final String VOTING_CITY = "Voting City";
    public static final String VOTING_STATE = "Voting State";
    public static final String VOTING_ZIP = "Voting Zip Code";
    public static final String VOTING_REGION = "Voting Region";

    public static final String CURRENT_ADDRESS = "Current Address";
    public static final String CURRENT_CITY = "Current City";
    public static final String CURRENT_STATE = "Current State";
    public static final String CURRENT_POSTAL_CODE = "Current Postal Code";
    public static final String CURRENT_COUNTRY = "Current Country";

    public static final String FORWARD_ADDRESS = "Forwarding Address";
    public static final String FORWARD_STATE = "Forwarding State";
    public static final String FORWARD_CITY = "Forwarding City";
    public static final String FORWARD_POSTAL_CODE = "Forwarding Postal Code";
    public static final String FORWARD_COUNTRY = "Forwarding Country";


    public static final String PHONE = "Phone";
    public static final String PHONE2 = "Phone2";
    public static final String EMAIL = "Email";
    public static final String ALT_EMAIL = "Alt Email";

    public static final String FULL_NAME = "Full Name";
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    public static final String MIDDLE_NAME = "Middle Name";
    public static final String SUFFIX_NAME = "Suffix";
    public static final String PREVIOUS_NAME = "Previous Name";
    public static final String VOTER_TYPE = "Voter Type";
    public static final String BIRTH_MONTH = "Birth Month";
    public static final String BIRTH_DATE = "Birth Date";
    public static final String BIRTH_YEAR = "Birth Year";
    public static final String BIRTH_DATE_FORMATTED = "Birth Formatted";

    public static final String PARTY = "Party";
    public static final String RACE = "Race";
    public static final String ETHNICITY = "Ethnicity";
    public static final String GENDER = "Gender";

    public static final String BALLOT_PREF = "Ballot Pref";
    public static final String VOTER_HISTORY = "Voter History";


    @Autowired
    private StateService stateService;

    @Autowired
    private EodApiService eodApiService;

    private static UserInfoFields instance;

    public static Map<String, String> getUserValues( OverseasUser user ) {

        return getInstance().getUserValues( user, false );
    }

    public static Map<String, String> getUserValues( WizardResults form ) {

        OverseasUser u = new OverseasUser();
        u.populateFromWizardResults( form );
        return getInstance().getUserValues( u, true );
    }

    /**
     * Returns a Map of all user fields where keys are human readable strings.
     *
     * @param user            Overseas User
     * @param removeKeySpaces Space characters are removed from the keys if true
     * @return Map of values
     */
    public Map<String, String> getUserValues( OverseasUser user, boolean removeKeySpaces ) {

        Map<String, String> fields = new HashMap<String, String>();

        UserAddress voting = user.getVotingAddress();
        if ( voting != null ) {
            voting.populateAddressFields( fields, VOTING_ADDRESS_PREFIX, false );
        }

        UserAddress overseas = user.getCurrentAddress();
        if ( overseas != null ) {
            overseas.populateAddressFields( fields, CURRENT_ADDRESS_PREFIX, true );
        }

        UserAddress forward = user.getForwardingAddress();
        if ( forward != null ) {
            forward.populateAddressFields( fields, FORWARD_ADDRESS_PREFIX, true );
        }
        UserAddress mailing = forward == null ? overseas : forward;
        if ( mailing != null ) {
            mailing.populateAddressFields( fields, MAILING_ADDRESS_PREFIX, true );
        }

        UserAddress previous = user.getPreviousAddress();
        if ( previous != null ) {
            previous.populateAddressFields( fields, PREVIOUS_ADDRESS_PREFIX, true );
        }

        if ( StringUtils.isNotEmpty( user.getEodRegionId() ) ) {
            EodRegion eodRegion = eodApiService.getRegion( user.getEodRegionId() );
            fields.put( VOTING_REGION, eodRegion != null ? eodRegion.getName() : EMPTY_STRING);
        }
        else {
            fields.put( VOTING_REGION, user.getVotingRegion() != null ? user.getVotingRegion().getName() : EMPTY_STRING);
        }
        fields.put( VOTER_TYPE, user.getVoterType() != null ? user.getVoterType().getName() : EMPTY_STRING);
        fields.put( VOTER_HISTORY, user.getVoterHistory() != null ? user.getVoterHistory().getName() : EMPTY_STRING);

        fields.put( PHONE, adjustPhone(user.getPhone()) );
        fields.put( PHONE2, adjustPhone(user.getAlternatePhone()) );
        fields.put( EMAIL, user.getUsername() );
        fields.put( ALT_EMAIL, user.getAlternateEmail() );

        fields.put( FULL_NAME, StringEscapeUtils.escapeXml( user.getName().getFullName() ) );
        fields.put( FIRST_NAME, StringEscapeUtils.escapeXml( user.getName().getFirstName() ) );
        fields.put( LAST_NAME, StringEscapeUtils.escapeXml( user.getName().getLastName() ) );
        fields.put( MIDDLE_NAME, StringEscapeUtils.escapeXml( user.getName().getMiddleName() ) );
        fields.put( SUFFIX_NAME, StringEscapeUtils.escapeXml( user.getName().getSuffix() ) );
        fields.put( TITLE_NAME, StringEscapeUtils.escapeXml( user.getName().getTitle() ) );

        fields.put( PREVIOUS_NAME, StringEscapeUtils.escapeXml( user.getPreviousName().getFullName() ) );
        fields.put( PREVIOUS_FIRST_NAME, StringEscapeUtils.escapeXml( user.getPreviousName().getFirstName() ) );
        fields.put( PREVIOUS_LAST_NAME, StringEscapeUtils.escapeXml( user.getPreviousName().getLastName() ) );
        fields.put( PREVIOUS_MIDDLE_NAME, StringEscapeUtils.escapeXml( user.getPreviousName().getMiddleName() ) );
        fields.put( PREVIOUS_SUFFIX, StringEscapeUtils.escapeXml( user.getPreviousName().getSuffix() ) );
        fields.put( PREVIOUS_TITLE, StringEscapeUtils.escapeXml( user.getPreviousName().getTitle() ) );

        fields.put( BIRTH_MONTH, String.format( "%02d", user.getBirthMonth() ) );
        fields.put( BIRTH_DATE, String.format( "%02d", user.getBirthDate() ) );
        fields.put( BIRTH_YEAR, String.valueOf( user.getBirthYear() ) );
        fields.put( BIRTH_DATE_FORMATTED, String.format( "%02d/%02d/%d",  user.getBirthMonth(),  user.getBirthDate(), user.getBirthYear() ) );
        fields.put( RACE, user.getRace() );
        fields.put( ETHNICITY, user.getEthnicity() );
        fields.put( GENDER, user.getGender() );
        fields.put( BALLOT_PREF, user.getBallotPref() );

        if ( removeKeySpaces ) {
            Map<String, String> fields2 = new HashMap<String, String>();
            for ( String key : fields.keySet() ) {
                fields2.put( stringToKey( key ), fields.get( key ) );
            }
            return fields2;
        }
        return fields;
    }

    private String adjustPhone(String originalPhone) {
        if (StringUtils.isEmpty(originalPhone)) {
            return EMPTY_STRING;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(originalPhone, US_REGION);
            //if (phoneNumberUtil.isValidNumber(phoneNumber)) {
                return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            //}
        } catch (NumberParseException e) {
            logger.error(e.getMessage());
        }
        return EMPTY_STRING;
    }

    private String stringToKey( String string ) {
        return string.replaceAll( " ", EMPTY_STRING);
    }

    /**
     * Returns the list of fields that can be used in dependencies
     *
     * @return List of Strings
     */
    public List<String> getDependencyFields() {
        List<String> fields = new ArrayList<String>();
        fields.add( stringToKey( VOTING_STATE ) );
        fields.add( stringToKey( VOTER_TYPE ) );
        fields.add( stringToKey( VOTER_HISTORY ) );
        return fields;
    }

    public List<String> getDependencyOptions( String fieldName ) {
        List<String> list = new ArrayList<String>();
        if ( fieldName.equals( stringToKey( VOTING_STATE ) ) ) {
            Collection<State> states = stateService.findAllStates();
            for ( State state : states ) {
                list.add( state.getAbbr() );
            }
        }
        if ( fieldName.equals( stringToKey( VOTER_TYPE ) ) ) {
            for ( VoterType type : VoterType.values() ) {
                list.add( type.getName() );
            }
        }
        if ( fieldName.equals( stringToKey( VOTER_HISTORY ) ) ) {
            for ( VoterHistory type : VoterHistory.values() ) {
                list.add( type.getName() );
            }
        }
        return list;
    }

    public UserInfoFields() {
        if ( instance == null ) {
            instance = this;
        }
    }

    public static UserInfoFields getInstance() {
        return instance == null ? new UserInfoFields() : instance;
    }
}
