package com.bearcode.ovf.validators;

import com.bearcode.ovf.forms.cf.CandidateFinderForm;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.webservices.DistrictLookupService;
import com.bearcode.ovf.webservices.SmartyStreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CandidateFinderValidator implements Validator {

    @Autowired
    private DistrictLookupService districtLookupService;

    @Autowired
    private SmartyStreetService smartyStreetService;


    public boolean supports( Class klass ) {
        return klass.equals( CandidateFinderForm.class );
    }

    public void validate( Object obj, Errors errors ) {
        CandidateFinderForm cff = (CandidateFinderForm) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "address.street1", "cf.label.street1_blank" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "address.city", "cf.label.city_blank" );
        ValidationUtils.rejectIfEmptyOrWhitespace( errors, "address.state", "cf.label.state_blank" );

        Address userAddr = cff.getAddress();
        String[] checkedAddr = smartyStreetService.findDistrict(
                userAddr.getStreet1(),
                userAddr.getCity(),
                userAddr.getState(),
                userAddr.getZip() );

        if ( checkedAddr[0].length() == 0 || checkedAddr[1].length() == 0 ) {
            errors.reject( "cf.label.address_invalid" );
        } else {
            cff.setDistrict( checkedAddr[0] );
            cff.getAddress().setZip( checkedAddr[1] );
            cff.getAddress().setZip4( checkedAddr[2] );
        }

        if ( cff.isAddToList() ) {
            ValidationUtils.rejectIfEmptyOrWhitespace( errors, "email", "cf.label.email_blank" );
            if ( !cff.getEmail().equals( cff.getConfirmEmail() ) ) {
                errors.reject( "cf.label.email_confirm_nomatch" );
            }
        }
    }
}
