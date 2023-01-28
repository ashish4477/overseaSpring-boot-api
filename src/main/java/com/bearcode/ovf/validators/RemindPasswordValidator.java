package com.bearcode.ovf.validators;

import com.bearcode.ovf.forms.UserAccountForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 19, 2007
 * Time: 10:13:53 PM
 * @author Leonid Ginzburg
 */
public class RemindPasswordValidator implements Validator {
    public boolean supports(Class clazz) {
        return clazz.equals(UserAccountForm.class);
    }

    public void validate(Object target, Errors errors) {
        UserAccountForm form = (UserAccountForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "mva.username.empty", "username cannot be empty");
        if ( !errors.hasErrors() && !form.getUsername().matches(OverseasUserValidator.USERNAME_PATTERN) ) {
            errors.rejectValue("username", "mva.username.not_valid", "Username should be an email.");
        }
    }
}
