package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Component
public class AddressValidator implements Validator {

    private final static String ZIP_PATTERN = "\\d{5}";
    private final static String ZIP4_PATTERN = "\\d{4}";

    public boolean supports(Class clazz) {
        return Address.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Address address = (Address) target;

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "street1", "mva.address.empty", new String[]{"Street"}, "Missing Field");
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "city", "mva.address.empty", new String[]{"City"}, "Missing Field");
       	ValidationUtils.rejectIfEmptyOrWhitespace( errors, "state", "mva.address.empty", new String[]{"State"}, "Missing Field");
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "zip", "mva.address.empty", new String[]{"Zip"}, "Missing Field");
        if ( address.getZip() != null && address.getZip().length() > 0 && !address.getZip().matches( ZIP_PATTERN )) {
            errors.rejectValue( "zip", "mva.address.wrong_zip", "Wrong Zip Code format." );
        }
        if ( address.getZip4() != null && address.getZip4().length() > 0 && !address.getZip4().matches( ZIP4_PATTERN )) {
            errors.rejectValue( "zip4", "mva.address.wrong_zip", "Wrong Zip Code format." );
        }

        checkFieldsLength( address, errors );
    }

    protected void checkFieldsLength( Address address, Errors errors ) {
        if ( address.getAddressTo().length() > 255)
        	errors.rejectValue( "addressTo", "mva.address.255_char_max", "field too long.");
        if ( address.getStreet1().length() > 255)
        	errors.rejectValue( "street1", "mva.address.255_char_max", "field too long.");
        if ( address.getStreet2().length() > 255)
        	errors.rejectValue( "street2", "mva.address.255_char_max", "field too long.");
        if ( address.getCity().length() > 255)
        	errors.rejectValue( "city", "mva.address.255_char_max", "field too long.");
        if ( address.getState().length() > 32)
        	errors.rejectValue( "state", "mva.address.32_char_max", "field too long.");
        if ( address.getZip().length() > 10)
        	errors.rejectValue( "zip", "mva.address.10_char_max", "field too long.");
        if ( address.getZip4().length() > 5)
        	errors.rejectValue( "zip4", "mva.address.5_char_max", "field too long.");
    }
}
