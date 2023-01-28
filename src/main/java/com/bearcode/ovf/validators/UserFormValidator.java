package com.bearcode.ovf.validators;

import com.bearcode.ovf.forms.UserAccountForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 9, 2007
 * Time: 7:14:57 PM
 * @author Leonid Ginzburg
 */
public class UserFormValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(UserAccountForm.class);
    }

    public void validate(Object target, Errors errors) {
        UserAccountForm userForm = (UserAccountForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "newPass", "mva.password.empty", "Missing password");
        if ( userForm.getNewPass().length() < 6)
        	errors.rejectValue( "newPass", "", "Password must be at least 6 characters.");
        
        if ( !userForm.getNewPass().equals( userForm.getConfirmPass() ) ) {
            errors.rejectValue("confirmPass", "mva.password.confirnation", "Confirmation should be the same");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "mva.username.empty", "Usename cannot be empty");
        if ( !errors.hasErrors() && !userForm.getUsername().matches(OverseasUserValidator.USERNAME_PATTERN) ) {
            errors.rejectValue("username", "mva.username.not_valid", "Username should be a valid email.");
        }
    }
}
