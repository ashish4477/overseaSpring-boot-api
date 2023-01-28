package com.bearcode.ovf.validators;

import com.bearcode.ovf.actions.express.forms.ExpressForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 19, 2008
 * Time: 7:40:50 PM
 * @author Leonid Ginzburg
 */
public class ExpressFormValidator  implements Validator {
    public boolean supports(Class clazz) {
        return clazz.equals( ExpressForm.class );
    }

    public void validate(Object target, Errors errors) {
    }

    public void validate( ExpressForm form, Errors errors, int page ) {
        switch ( page ) {
            case 0:
                ValidationUtils.rejectIfEmpty( errors, "country", "", "Please select a country." );
                break;
            case 2:
                if ( /*form.getLogin() != null && form.getLogin().length() > 0 &&
                        form.getPassw() != null && form.getPassw().length() > 0*/
                        form.isDoLogin() ) {
                    break;
                }
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "firstName", "eyv.label.first_name_blank");
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "lastName", "eyv.label.last_name_blank");
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "pickUp.street1", "eyv.label.street1_blank");
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "pickUp.city", "eyv.label.city_blank");
                if ( form.getCountry() != null && form.getCountry().getZipPattern().length() > 0 ) {
                    ValidationUtils.rejectIfEmptyOrWhitespace( errors, "pickUp.zip", "eyv.label.zip_blank");
                    if ( !form.getPickUp().getZip().matches( form.getCountry().getZipPattern() ) ) {
                        errors.rejectValue( "pickUp.zip", "", "Invalid Postal Code" );
                    }
                }

         		// TODO: Put this someplace better
        		// RFC 2822 token definitions for valid email - only used together                  
                final String sp = "!#$%&\'*+-/=?^_`{|}~";
                final String atext = "[a-zA-Z0-9" + sp + "]";
                final String atom = atext + "+"; // one or more atext chars
                final String dotAtom = "\\." + atom;
                final String localPart = atom + "(" + dotAtom + ")*"; // one atom followed by 0 or more dotAtoms.
                // RFC 1035 tokens for domain names:
                final String letter = "[a-zA-Z]";
                final String letDig = "[a-zA-Z0-9]";
                final String letDigHyp = "[a-zA-Z0-9-]";
                final String rfcLabel = letDig + letDigHyp + "{0,61}" + letDig;
                final String domain = rfcLabel + "((\\." + rfcLabel + ")*\\." + letter + "{2,6}){0,1}";
                // Combined together, these form the allowed email regexp allowed by RFC 2822:
                final String EMAIL_VALIDATION_PATTERN = "^" + localPart + "(@" + domain + "){1}$";

                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "notificationEmail", "eyv.label.email_blank");
                if(!form.getNotificationEmail().matches(EMAIL_VALIDATION_PATTERN))
                    errors.rejectValue( "notificationEmail", "eyv.label.email_invalid");
                if ( !form.getNotificationEmail().equals( form.getNotificationEmailConfirm() ) )
                    errors.rejectValue( "notificationEmailConfirm", "eyv.label.email_confirm_nomatch");
                
                if ( !form.isLoggedUser() ) {
                    //ValidationUtils.rejectIfEmptyOrWhitespace( errors, "notificationPass", "eyv.label.password_blank");
                    if ( form.getNotificationPass().length() < 6)
                    	errors.rejectValue( "notificationPass", "", "Password must be at least 6 characters.");
                   // if ( form.getNotificationPass().matches("[^a-zA-Z0-9!@#$%\\^&*+\\-\\._]"))
                    //	errors.rejectValue( "notificationPassConfirm", "", "Password contains invalid characters");
                    
                    if ( !form.getNotificationPass().equals( form.getNotificationPassConfirm() ) )
                        errors.rejectValue( "notificationPassConfirm", "eyv.label.password_confirm_nomatch");
                }
                
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "notificationPhone", "eyv.label.phone_blank");
                ValidationUtils.rejectIfEmpty( errors, "destinationLeo", "", "Please select a Ballot destination.");

                break;
            case 3:
                if ( form.getCountry() != null && form.getCountry().getRate() != 0 ) {
                    ValidationUtils.rejectIfEmptyOrWhitespace( errors, "nameOnCard", "rava.field.is_empty");
                    ValidationUtils.rejectIfEmptyOrWhitespace( errors, "ccNumber", "rava.field.is_empty");
                    ValidationUtils.rejectIfEmptyOrWhitespace( errors, "ccExpiredMonth", "rava.field.is_empty");
                    ValidationUtils.rejectIfEmptyOrWhitespace( errors, "ccExpiredYear", "rava.field.is_empty");
                }

        }
    }
}
