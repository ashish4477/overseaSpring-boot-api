package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.common.UserAddress;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 9, 2007
 * Time: 7:14:57 PM
 * @author Leonid Ginzburg
 */

@Component
public class UserAddressValidator extends AddressValidator {
    /*  Zip formats: http://www.barnesandnoble.com/help/cds2.asp?pid=8134
    * Canada ANA NAN
The Netherlands NNNN AA
Poland NN-NNN
Sweden, Slovakia, The Czech Rep NNN NN
South Korea NNN-NNN
Portugal NNNN-NNN or NNNN
UK postcodes - look here: http://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom
*/
    // old pattern "(\\d{3,9})|(\\p{Alpha}\\d\\p{Alpha} \\d\\p{Alpha}\\d)|(\\d{4} \\p{Alpha}{2})|(\\d{2,4}\\-\\d{3})|(\\d{3} \\d{2})|([A-Z]{1,2}[\\dR][\\dA-Z]? \\d[ABD-HJLNP-UW-Z]{2})"
    private final static String INTERNATIONAL_ZIP_PATTERN = "[\\d\\p{Alpha}\\- ]{3,10}";
    private final static String ZIP_PATTERN = "\\d{5}";
    private final static String ZIP4_PATTERN = "\\d{4}";

    public boolean supports(Class clazz) {
        return UserAddress.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        UserAddress address = (UserAddress) target;
		//super.validate(address, errors);
        switch ( address.getType() ) {
            case OVERSEAS:
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "street1", "mva.address.empty", new String[]{"Street"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "city", "mva.address.empty", new String[]{"City"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "country", "mva.address.empty", new String[]{"Country"} );
                if ( address.getZip() != null && address.getZip().length() > 0
                        && !address.getZip().matches( INTERNATIONAL_ZIP_PATTERN )) {
                    errors.rejectValue( "zip", "mva.address.wrong_postal_code", "Wrong Postal Code format." );
                }
                break;
            case STREET:
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "street1", "mva.address.empty", new String[]{"Street"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "city", "mva.address.empty", new String[]{"City"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "state", "mva.address.empty", new String[]{"State"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "zip", "mva.address.empty", new String[]{"Zip code"} );
                if ( !address.getZip().matches( ZIP_PATTERN )) {
                    errors.rejectValue( "zip", "mva.address.wrong_zip", "Wrong Zip Code format." );
                }
                if ( address.getZip4() != null && address.getZip4().length() > 0 && !address.getZip4().matches( ZIP4_PATTERN )) {
                    errors.rejectValue( "zip4", "mva.address.wrong_zip", "Wrong Zip Code format." );
                }
                break;
            case MILITARY:
                // street1 = unit & box number
                // street2 = organization
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "street1", "mva.address.empty" , new String[]{"This"});
                // city = APO/FPO
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "city", "mva.address.empty", new String[]{"This"} );
                // state = AE/AP/AA
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "state", "mva.address.empty", new String[]{"This"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "zip", "mva.address.empty", new String[]{"Zip code"} );
                if ( !address.getZip().matches( INTERNATIONAL_ZIP_PATTERN )) {
                    errors.rejectValue( "zip", "mva.address.wrong_postal_code", "Wrong Postal Code format." );
                }
                if ( !address.getCity().matches( "(A|F|D)PO" ) ) {
                    errors.rejectValue( "city", "mva.address.wrong_apo_fpo", "Wrong APO/FPO/DPO code." );
                }
                if ( !address.getState().matches( "A(A|E|P)" ) ) {
                    errors.rejectValue( "state", "mva.address.wrong_aa_ae_ap", "Wrong AA/AE/AP location." );
                }
                break;
            case RURAL_ROUTE:
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "street1", "mva.address.empty" );
            case DESCRIBED:
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "description", "mva.address.empty", new String[]{"This"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "city", "mva.address.empty", new String[]{"City"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "state", "mva.address.empty", new String[]{"State"} );
                ValidationUtils.rejectIfEmptyOrWhitespace( errors, "zip", "mva.address.empty", new String[]{"Zip code"} );
                if ( !address.getZip().matches( ZIP_PATTERN )) {
                    errors.rejectValue( "zip", "mva.address.wrong_zip", "Wrong Zip Code format." );
                }
                if ( address.getZip4() != null && address.getZip4().length() > 0 && !address.getZip4().matches( ZIP4_PATTERN )) {
                    errors.rejectValue( "zip4", "mva.address.wrong_zip", "Wrong Zip Code format." );
                }
                break;

        }
        checkFieldsLength( address, errors );
		if ( address.getCountry().length() > 255)
        	errors.rejectValue( "country", "mva.address.255_char_max", "field too long.");

    }
}
