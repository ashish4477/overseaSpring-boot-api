package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.service.OverseasUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OverseasUserValidator implements Validator {

    private static final String CURRENT_ADDRESS = "currentAddress";
    private static final String CURRENT_ADDRESS_STREET_1 = "currentAddress.street1";
    private static final String CURRENT_ADDRESS_CITY = "currentAddress.city";
    private static final String CURRENT_ADDRESS_COUNTY = "currentAddress.county";
    private static final String CURRENT_ADDRESS_STATE = "currentAddress.state";
    private static final String CURRENT_ADDRESS_ZIP = "currentAddress.zip";
    private static final String CURRENT_ADDRESS_COUNTRY = "currentAddress.country";

    private static final String VOTING_ADDRESS = "votingAddress";
    private static final String VOTING_ADDRESS_STREET_1 = "votingAddress.street1";
    private static final String VOTING_ADDRESS_DESCRIPTION = "votingAddress.description";
    private static final String VOTING_ADDRESS_CITY = "votingAddress.city";
    private static final String VOTING_ADDRESS_COUNTY = "votingAddress.county";
    private static final String VOTING_ADDRESS_STATE = "votingAddress.state";
    private static final String VOTING_ADDRESS_ZIP = "votingAddress.zip";

    public static String USERNAME_PATTERN = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
    //  old pattern  "(([^<>\\(\\)\\[\\]\\.,;:\\s@\\\"]+(\\.[^<>\\(\\)\\[\\]\\.,;:\\s@\\\"]+)*)|(\\\".+\\\"))@(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,})";
    public static String PHONE_PATTERN = "[+\\-\\(\\)\\. \\d]*";

    protected Logger log = LoggerFactory.getLogger(OverseasUserValidator.class);

    @Autowired
    OverseasUserService userService;

    @Autowired
    UserAddressValidator addrValidator;

    public void setOverseasUserService(OverseasUserService service) {
        userService = service;
    }

    public boolean supports(Class clazz) {
        return OverseasUser.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        validate(target, errors, new HashSet<>());
    }

    public void validate(Object target, Errors errors, Set<String> skip) {
        OverseasUser user = (OverseasUser) target;

        // check this user against the database
        OverseasUser existingUser = userService.findUserByName(user.getUsername());
        if (existingUser != null) {
            if (user.getId() != existingUser.getId()) {
                // Different users cannot have the same username
                errors.rejectValue("username", "mva.username.exists", "Username is taken");
            } else {
                // same user but existingUser has original properties
                if (user.getPassword().equals(existingUser.getPassword())) {
                    // user has not changed password so we don't need
                    // to validate
                    skip.add("password");
                }
                userService.evict(existingUser);
            }
        } else if (user.getId() != 0l) {
            // Existing user with a new username (email address).
            existingUser = userService.findUserById(user.getId());
            if (user.getPassword().equals(existingUser.getPassword())) {
                // user has not changed password so we don't need to validate
                skip.add("password");
            }
            userService.evict(existingUser);
        }
        if (user.isVoterAlertOnly()) {
            skip.add("password");
        }

        validateUser(user, errors, skip);
    }

    /**
     * Validates user properties into the give Errors instance. fieldToSkip is a list of fields
     * to skip validation on. Only certain fields are allowed to be skipped:
     */
    public void validateUser(OverseasUser user, Errors errors, Set<String> fieldsToSkip) {
        if (user == null) {
            errors.reject("", "Incorrect request");
            log.error("User cannot be null");
            return;
        }
        if (fieldsToSkip == null) {
            fieldsToSkip = Collections.emptySet();
        }

        // Password
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("password")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "mva.password.empty", "Missing password");
            if (!errors.hasErrors() && user.getPassword().length() < 6)
                errors.rejectValue("password", "mva.password.6_char_min", "Password cannot be greater than 200 characters");
            if (!errors.hasErrors() && user.getPassword().length() > 200)
                errors.rejectValue("password", "mva.password.200_char_max", "Password must be at least 6 characters");
        }

        // Password confirmation
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("confirmPassword")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "mva.password.confirmation.empty", "Password confirmation cannot be empty");
            if (!errors.hasErrors() && !user.getPassword().equals(user.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "mva.password.confirmation", "Password confirmation should be the same");
            }
        }

        // Username (email address)
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "mva.username.empty", "Email should not be empty");
        if (!errors.hasFieldErrors("username") && !user.getUsername().matches(USERNAME_PATTERN)) {
            errors.rejectValue("username", "mva.username.not_valid", "Email should be a valid email");
        }

        if (user.getAlternateEmail().length() > 0 && !user.getAlternateEmail().matches(USERNAME_PATTERN)) {
            errors.rejectValue("alternateEmail", "mva.username.not_valid", "Email should be a valid email");
        }

        // Firstname
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("name.firstName")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name.firstName", "mva.firstName.empty", "Missing First Name");
            if (user.getName().getFirstName().length() > 128) {
                errors.rejectValue("name.firstName", "mva.firstName.128_char_max", "First name too long");
            }
        }

        // Lastname
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("name.lastName")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name.lastName", "mva.lastName.empty", "Missing Last Name");
            if (user.getName().getLastName().length() > 255) {
                errors.rejectValue("name.lastName", "mva.lastName.128_char_max", "Last name too long");
            }
        }
        // Optional name fields - just check that they are not too long for db field
        if (user.getName().getMiddleName() != null && user.getName().getMiddleName().length() > 255) {
            errors.rejectValue("name.middleName", "mva.middleName.255_char_max", "Middle name too long");
        }

        if (user.getName().getSuffix() != null && user.getName().getSuffix().length() > 32) {
            errors.rejectValue("name.suffix", "mva.suffix.32_char_max", "Suffix too long");
        }


        // Birthdate
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("birthDate")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthDate", "mva.birthDate.empty", "Missing Birth Date");
            if (user.getBirthDate() > 31 || user.getBirthDate() < 1) {
                errors.rejectValue("birthDate", "mva.birthDate.not_valid", "Invalid Birth Date");
            }
        }
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("birthMonth")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthMonth", "mva.birthMonth.empty", "Missing Birth Month");
            if (user.getBirthMonth() > 12 || user.getBirthMonth() < 1) {
                errors.rejectValue("birthMonth", "mva.birthMonth.not_valid", "Invalid Birth Month");
            }
        }
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("birthYear")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthYear", "mva.birthYear.empty", "Missing Birth Year");
            if (user.getBirthYear() > new GregorianCalendar().get(GregorianCalendar.YEAR) || user.getBirthYear() < 1800) {
                errors.rejectValue("birthYear", "mva.birthYear.not_valid", "Invalid Birth Year");
            }
        }

        // Optional demographic info - just check that they are not too long for db field
        if (user.getParty() != null && user.getParty().length() > 32) {
            errors.rejectValue("party", "mva.party.32_char_max", "party too long");
        }

        if (user.getRace() != null && user.getRace().length() > 32) {
            errors.rejectValue("race", "mva.race.32_char_max", "race too long");
        }

        if (user.getEthnicity() != null && user.getEthnicity().length() > 32) {
            errors.rejectValue("ethnicity", "mva.ethnicity.32_char_max", "Ethnicity too long");
        }

        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("gender")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "mva.gender.required", "Gender is required");
            if (user.getGender() != null && user.getGender().length() > 1) {
                errors.rejectValue("gender", "mva.gender.1_char_max", "Gender has too many characters");
            }
        }

        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("voterHistory")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "voterHistory", "mva.voting_history.empty", "This field is required");
            if (user.getVoterHistory() != null && user.getVoterHistory().getName().length() > 64) {
                errors.rejectValue("voterHistory", "mva.voterHistory.64_char_max", "Voter history too long");
            }
        }
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("voterType")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "voterType", "mva.voter_type.empty", "Voter Type is required");
            if (user.getVoterType() != null && user.getVoterType().getName().length() > 32) {
                errors.rejectValue("voterType", "mva.voterType.32_char_max", "Voter type too long");
            }
        }
        if ( fieldsToSkip.isEmpty() || !fieldsToSkip.contains( "voterClassificationType" ) ) {
            ValidationUtils.rejectIfEmptyOrWhitespace( errors, "voterClassificationType", "mva.voter_classification_type.empty", "Voter Classification is required" );
            if ( user.getVoterClassificationType() != null && user.getVoterClassificationType().getName().length() > 32 )
                errors.rejectValue( "voterClassificationType", "mva.voterClassificationType.32_char_max", "Voter Classification type too long" );
        }
        if (user.getBallotPref() != null && user.getBallotPref().length() > 255) {
            errors.rejectValue("ballotPref", "mva.ballotPref.255_char_max", "Ballot preference too long");
        }


        validateVotingAddress(user, errors, fieldsToSkip);

        validateCurrentAddress(user, errors, fieldsToSkip);

        if (user.getForwardingAddress() != null && !user.getForwardingAddress().isEmptySpace()) {
            errors.pushNestedPath("forwardingAddress");
            ValidationUtils.invokeValidator(addrValidator, user.getForwardingAddress(), errors);
            errors.popNestedPath();
        }

        if (user.getPreviousAddress() != null && !user.getPreviousAddress().isEmptySpace()) {
            errors.pushNestedPath("previousAddress");
            ValidationUtils.invokeValidator(addrValidator, user.getPreviousAddress(), errors);
            errors.popNestedPath();
        }

        // Phone
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("phone")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "mva.phone.empty", "Phone is required");
            if (user.getPhone().length() > 32) {
                errors.rejectValue("phone", "mva.phone.32_char_max", "phone too long");
            }
        }
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("phoneType")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneType", "mva.phone_type.empty", "Phone Type is required");
        }
        
            /*
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        if ( user.getPhone() != null && !user.getPhone().isEmpty() ) {
            try {
                Phonenumber.PhoneNumber phone = phoneNumberUtil.parse( user.getPhone(), "US" );
                if ( !phoneNumberUtil.isValidNumber( phone ) ) {
                    errors.rejectValue( "phone", "mva.phone.wrong_format", "Wrong format" );
                }
            } catch (NumberParseException e) {
                errors.rejectValue( "phone", "mva.phone.wrong_format", "Wrong format" );
            }
            //if ( !user.getPhone().matches( PHONE_PATTERN ) ) {
               // errors.rejectValue( "phone", "mva.phone.wrong_format", "Wrong format" );
            //}
        }
        if ( user.getAlternatePhone() != null && !user.getAlternatePhone().isEmpty() ) {
            try {
                Phonenumber.PhoneNumber altPhone = phoneNumberUtil.parse( user.getAlternatePhone(), "US" );
                if ( !phoneNumberUtil.isValidNumber( altPhone ) ) {
                    errors.rejectValue( "alternatePhone", "mva.phone.wrong_format", "Wrong format" );
                }
            } catch (NumberParseException e) {
                errors.rejectValue( "alternatePhone", "mva.phone.wrong_format", "Wrong format" );
            }
        }
*/
        if (fieldsToSkip.isEmpty() || !fieldsToSkip.contains("eodRegionId")) {
            if (user.getEodRegionId() == null || user.getEodRegionId().equals("0")) {
                user.setEodRegionId("");
            }
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eodRegionId", "mva.eod_region.empty", "Missing Voting Jurisdiction");
        }
        if (errors.hasErrors()) {
            String fieldsWithErrors = errors.getAllErrors().stream().map(ObjectError::getCode)
                    .collect(Collectors.joining(", "));
            log.error("Validation failed for the following fields: [{}]", fieldsWithErrors);
        }
    }

    // Address validation
    private void validateVotingAddress(OverseasUser user, Errors errors, Set<String> fieldsToSkip) {
        if (Objects.isNull(user) ||  fieldsToSkip.contains(VOTING_ADDRESS)) {
            return;
        }
        UserAddress userVotingAddress = user.getVotingAddress();

        if (Objects.isNull(userVotingAddress) || userVotingAddress.isEmptySpace()) {
            if (!fieldsToSkip.contains(VOTING_ADDRESS_STREET_1)) {
                errors.rejectValue(VOTING_ADDRESS_STREET_1, "mva.voting_address.street1.empty", "Address is required");
            }
            if ((!Objects.isNull(userVotingAddress) && (userVotingAddress.getType() == AddressType.RURAL_ROUTE || userVotingAddress.getType() == AddressType.DESCRIBED))
                    && !fieldsToSkip.contains(VOTING_ADDRESS_DESCRIPTION)) {
                errors.rejectValue(VOTING_ADDRESS_DESCRIPTION, "mva.voting_address.description.empty", "Address Description is required");
            }
            if (!fieldsToSkip.contains(VOTING_ADDRESS_CITY)) {
                errors.rejectValue(VOTING_ADDRESS_CITY, "mva.voting_address.city.empty", "City is required");
            }
            if (!fieldsToSkip.contains(VOTING_ADDRESS_COUNTY)) {
                errors.rejectValue(VOTING_ADDRESS_COUNTY, "mva.voting_address.county.empty", "County is required");
            }
            if (!fieldsToSkip.contains(VOTING_ADDRESS_STATE)) {
                errors.rejectValue(VOTING_ADDRESS_STATE, "mva.voting_address.state.empty", "State is required");
            }
            if (!fieldsToSkip.contains(VOTING_ADDRESS_ZIP)) {
                errors.rejectValue(VOTING_ADDRESS_ZIP, "mva.voting_address.zip.empty", "Zip code is required");
            }
        } else {
            if (StringUtils.isBlank(userVotingAddress.getStreet1()) && !fieldsToSkip.contains(VOTING_ADDRESS_STREET_1) && userVotingAddress.getType() != AddressType.DESCRIBED) {
                errors.rejectValue(VOTING_ADDRESS_STREET_1, "mva.voting_address.street1.empty", "Address is required");
            }
            if (userVotingAddress.getType() == AddressType.RURAL_ROUTE || userVotingAddress.getType() == AddressType.DESCRIBED) {
                if (StringUtils.isBlank(userVotingAddress.getDescription()) && !fieldsToSkip.contains(VOTING_ADDRESS_DESCRIPTION)) {
                    errors.rejectValue(VOTING_ADDRESS_DESCRIPTION, "mva.voting_address.description.empty", "Address Description is required");
                }
            }
            if (StringUtils.isBlank(userVotingAddress.getCity()) && !fieldsToSkip.contains(VOTING_ADDRESS_CITY)) {
                errors.rejectValue(VOTING_ADDRESS_CITY, "mva.voting_address.city.empty", "City is required");
            }
            if (StringUtils.isBlank(userVotingAddress.getCounty()) && !fieldsToSkip.contains(VOTING_ADDRESS_COUNTY)) {
                errors.rejectValue(VOTING_ADDRESS_COUNTY, "mva.voting_address.county.empty", "County is required");
            }
            if (StringUtils.isBlank(userVotingAddress.getState()) && !fieldsToSkip.contains(VOTING_ADDRESS_STATE)) {
                errors.rejectValue(VOTING_ADDRESS_STATE, "mva.voting_address.state.empty", "State is required");
            }
            if (StringUtils.isBlank(userVotingAddress.getZip()) && !fieldsToSkip.contains(VOTING_ADDRESS_ZIP)) {
                errors.rejectValue(VOTING_ADDRESS_ZIP, "mva.voting_address.zip.empty", "Zip code is required");
            }
        }

    }

    // Current Address validation
    private void validateCurrentAddress(OverseasUser user, Errors errors, Set<String> fieldsToSkip) {
        if (Objects.isNull(user) || fieldsToSkip.contains(CURRENT_ADDRESS)) {
            return;
        }
        UserAddress userCurrentAddress = user.getCurrentAddress();

        if ( Objects.isNull(userCurrentAddress) || userCurrentAddress.isEmptySpace() ) {
            if (!fieldsToSkip.contains(CURRENT_ADDRESS_STREET_1)) {
                if (Objects.isNull(userCurrentAddress) || userCurrentAddress.getType() != AddressType.MILITARY) {
                    errors.rejectValue(CURRENT_ADDRESS_STREET_1, "mva.current_address.street1.empty", "Address is required");
                }
            }
            if (!fieldsToSkip.contains(CURRENT_ADDRESS_CITY)) {
                if (Objects.isNull(userCurrentAddress) || userCurrentAddress.getType() != AddressType.MILITARY) {
                    errors.rejectValue(CURRENT_ADDRESS_CITY, "mva.current_address.city.empty", "City is required");
                }
            }
            if (!fieldsToSkip.contains(CURRENT_ADDRESS_COUNTY)) {
                errors.rejectValue(CURRENT_ADDRESS_COUNTY, "mva.current_address.county.empty", "County is required");
            }
            if (!fieldsToSkip.contains(CURRENT_ADDRESS_STATE)) {
                errors.rejectValue(CURRENT_ADDRESS_STATE, "mva.current_address.state.empty", "State is required");
            }
            if (!fieldsToSkip.contains(CURRENT_ADDRESS_ZIP)) {
                errors.rejectValue(CURRENT_ADDRESS_ZIP, "mva.current_address.zip.empty", "Zip code is required");
            }
            if (!fieldsToSkip.contains(CURRENT_ADDRESS_COUNTRY)) {
                errors.rejectValue(CURRENT_ADDRESS_COUNTRY, "mva.current_address.country.empty", "Country is required");
            }
        } else {
            if (StringUtils.isBlank(userCurrentAddress.getStreet1()) && !fieldsToSkip.contains(CURRENT_ADDRESS_STREET_1)
                    && userCurrentAddress.getType() != AddressType.MILITARY) {
                errors.rejectValue(CURRENT_ADDRESS_STREET_1, "mva.current_address.street1.empty", "Address 1 is required");
            }
            if (StringUtils.isBlank(userCurrentAddress.getCity()) && !fieldsToSkip.contains(CURRENT_ADDRESS_CITY)
                    && userCurrentAddress.getType() != AddressType.MILITARY) {
                errors.rejectValue(CURRENT_ADDRESS_CITY, "mva.current_address.city.empty", "City is required");
            }
            if (StringUtils.isBlank(userCurrentAddress.getCounty()) && !fieldsToSkip.contains(CURRENT_ADDRESS_COUNTY)) {
                errors.rejectValue(CURRENT_ADDRESS_COUNTY, "mva.current_address.county.empty", "County is required");
            }
            if (StringUtils.isBlank(userCurrentAddress.getState()) && !fieldsToSkip.contains(CURRENT_ADDRESS_STATE)) {
                errors.rejectValue(CURRENT_ADDRESS_STATE, "mva.current_address.state.empty", "State is required");
            }
            if (StringUtils.isBlank(userCurrentAddress.getZip()) && !fieldsToSkip.contains(CURRENT_ADDRESS_ZIP)) {
                errors.rejectValue(CURRENT_ADDRESS_ZIP, "mva.current_address.zip.empty", "Zip code is required");
            }
            if (StringUtils.isBlank(userCurrentAddress.getCountry()) && !fieldsToSkip.contains(CURRENT_ADDRESS_COUNTRY)) {
                errors.rejectValue(CURRENT_ADDRESS_COUNTRY, "mva.current_address.country.empty", "Country is required");
            }
        }
    }

    public void validateUserFields(OverseasUser user, Errors errors, Set<String> fieldsToValidate) {
        // Password
        if (fieldsToValidate.contains("password")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "mva.password.empty", "Missing password");
            if (!errors.hasErrors() && user.getPassword().length() < 6)
                errors.rejectValue("password", "mva.password.6_char_min", "Password cannot be greater than 200 characters");
            if (!errors.hasErrors() && user.getPassword().length() > 200)
                errors.rejectValue("password", "mva.password.200_char_max", "Password must be at least 6 characters");
        }

        // Password confirmation
        if (fieldsToValidate.contains("confirmPassword")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "mva.password.confirmation.empty", "Password confirmation cannot be empty");
            if (!errors.hasErrors() && !user.getPassword().equals(user.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "mva.password.confirmation", "Password confirmation should be the same");
            }
        }
    }

}
