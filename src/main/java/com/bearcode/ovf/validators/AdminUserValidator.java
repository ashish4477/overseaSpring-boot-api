package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Date: 02.11.11
 * Time: 14:54
 *
 * @author Leonid Ginzburg
 */
@Component
public class AdminUserValidator implements Validator {

    @Autowired
    OverseasUserService userService;

    @Override
    public boolean supports( Class clazz ) {
        return OverseasUser.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        OverseasUser user = (OverseasUser) target;

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "username", "mva.username.empty", "Email should not be empty" );
        if ( !errors.hasFieldErrors( "username" ) ) {
            // check this user against the database
            OverseasUser existingUser = userService.findUserByName( user.getUsername() );
            if ( existingUser != null ) {
                if ( user.getId() != existingUser.getId() ) {
                    // Different users cannot have the same username
                    errors.rejectValue( "username", "mva.username.exists", "Username is taken" );
                }
            }
        }
        // Firstname
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name.firstName", "mva.firstName.empty", "Missing First Name" );
        if ( user.getName().getFirstName().length() > 128 )
            errors.rejectValue( "name.firstName", "mva.firstName.128_char_max", "First name too long." );
        // Lastname
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name.lastName", "mva.lastName.empty", "Missing Last Name" );
        if ( user.getName().getLastName().length() > 255 )
            errors.rejectValue( "name.firstName", "mva.lasstName.128_char_max", "Last name too long." );

    }
}
