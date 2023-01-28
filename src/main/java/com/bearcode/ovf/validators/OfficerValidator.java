package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.eod.Officer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 14.11.12
 * Time: 17:16
 *
 * @author Leonid Ginzburg
 */
@Component
public class OfficerValidator implements Validator {
    @Override
    public boolean supports( Class<?> clazz ) {
        return Officer.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        Officer officer = (Officer) target;
        officer.setPhone( fixAndValidatePhone( officer.getPhone(), errors, "phone" ) );
        officer.setFax( fixAndValidatePhone( officer.getFax(), errors, "fax" ) );
        officer.setEmail( officer.getEmail().trim() );
        if ( officer.getEmail().length() > 0 && !officer.getEmail().matches( OverseasUserValidator.USERNAME_PATTERN ) ) {
            errors.rejectValue( "email", "", "Email should be a valid email." );
        }
        if ( !officer.isEmpty() ) {
            ValidationUtils.rejectIfEmptyOrWhitespace( errors, "officeName", "rava.admin.fieldtype.empty_name" );    // empty office name
        }

    }

    /**
     * converts a phone number String to (XXX) XXX-XXXX format.
     * <p/>
     * Takes a phone number string and an Errors object. If the String is empty,
     * an empty string is returned and the Errors object is not modified. If the
     * string is non-empty AND can be converted, the converted format is returned
     * and the Errors object is not modified. If the string is non-empty and cannot
     * be converted to the format, the Erros.reject() mehtod is called and an empty
     * string is returned.
     *
     * @param origPhone - phone number as entered
     * @param errors
     * @param field     - name of field
     * @return
     */
    private String fixAndValidatePhone( String origPhone, Errors errors, String field ) {
        String fixed = "";
        origPhone = origPhone.trim();
        if ( origPhone.length() > 0 && !origPhone.matches( "^\\s+$" ) ) { // treat string of just spaces as an empty string
            Pattern pattern = Pattern.compile( "^\\(?(\\d\\d\\d)[^\\d]*(\\d\\d\\d)[^\\d]*(\\d\\d\\d\\d)(\\s+.*)?" );
            Matcher matcher = pattern.matcher( origPhone );
            if ( matcher.matches() ) {
                fixed = "(" + matcher.group( 1 ) + ") " + matcher.group( 2 ) + "-" + matcher.group( 3 );
                if ( matcher.group( 4 ) != null ) {
                    fixed += matcher.group( 4 );
                }
            } else {
                errors.rejectValue( field, "eod.corrections.invalid_phone" );
            }
        }
        return fixed;
    }
}
