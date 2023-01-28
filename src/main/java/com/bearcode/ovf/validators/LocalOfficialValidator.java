package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.eod.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 19, 2007
 * Time: 4:11:05 PM
 *
 * @author Leonid Ginzburg
 */
@Component("leoValidator")
public class LocalOfficialValidator implements Validator {

    @Autowired
    private OfficerValidator officerValidator;

    @Autowired
    private AddressValidator addressValidator;

    public boolean supports( Class clazz ) {
        return clazz.equals( LocalOfficial.class ) || clazz.equals( CorrectionsLeo.class );
    }

    public void validate( Object target, Errors errors ) {
        AbstractLocalOfficial leo = (AbstractLocalOfficial) target;
        // fix missed 'http' for link
        if ( leo.getWebsite().length() > 0 && !leo.getWebsite().toLowerCase().startsWith( "http" ) ) {
            leo.setWebsite( "http://" + leo.getWebsite() );
        }
        if ( leo instanceof CorrectionsLeo ) {
            LocalOfficial correctionsFor = ((CorrectionsLeo) leo).getCorrectionFor();
            if ( correctionsFor == null ) {
                errors.reject( "", "" );
                return;
            }
            if ( correctionsFor.getWebsite().length() > 0 && !correctionsFor.getWebsite().toLowerCase().startsWith( "http" ) ) {
                correctionsFor.setWebsite( "http://" + correctionsFor.getWebsite() );
            }
            checkEmpty( (CorrectionsLeo) leo, errors );
        }
        //validate emails
        leo.setGeneralEmail( leo.getGeneralEmail().trim() );
        if ( !leo.getGeneralEmail().isEmpty() && !leo.getGeneralEmail().matches( OverseasUserValidator.USERNAME_PATTERN ) ) {
            errors.rejectValue( "generalEmail", "", "Email should be a valid email." );
        }
        for ( int i=0; i < leo.getOfficers().size(); i++ ) {
            try {
                errors.pushNestedPath( String.format( "officers[%d]", i ) );
                ValidationUtils.invokeValidator( officerValidator, leo.getOfficers().get( i ), errors );
            } finally {
                errors.popNestedPath();
            }
        }
        //validate addresses
        try {
            errors.pushNestedPath( "mailing" );
            ValidationUtils.invokeValidator( addressValidator, leo.getMailing(), errors );
        } finally {
            errors.popNestedPath();
        }
        try {
            errors.pushNestedPath( "physical" );
            ValidationUtils.invokeValidator( addressValidator, leo.getPhysical(), errors );
        } finally {
            errors.popNestedPath();
        }
        if ( leo instanceof LocalOfficial ) {
            for( int j = 0; j < ((LocalOfficial)leo).getAdditionalAddresses().size(); j++ ) {
                AdditionalAddress address = ((LocalOfficial)leo).getAdditionalAddresses().get( j );
                if ( !address.checkEmpty() ) {
                    try {
                        errors.pushNestedPath( String.format( "additionalAddresses[%d].address", j ) );
                        ValidationUtils.invokeValidator( addressValidator, address.getAddress(), errors );
                    } finally {
                        errors.popNestedPath();
                    }
                }
                if ( StringUtils.isNotBlank( address.getEmail() ) ) {
                    if ( !address.getEmail().matches( OverseasUserValidator.USERNAME_PATTERN ) ) {
                        errors.rejectValue( String.format( "additionalAddresses[%d].email", j ), "mva.username.not_valid" );
                    }
                }
            }
        } else if ( leo instanceof CorrectionsLeo ) {
            for( int j = 0; j < ((CorrectionsLeo)leo).getAdditionalAddresses().size(); j++ ) {
                CorrectionAdditionalAddress address = ((CorrectionsLeo)leo).getAdditionalAddresses().get( j );
                if ( !address.isEmpty() ) {
                    try {
                        ValidationUtils.rejectIfEmptyOrWhitespace( errors, String.format( "additionalAddresses[%d].addressTypeName", j ), "mva.address.empty", new String[]{"Type Name"}, "Address type name should not be empty" );
                        errors.pushNestedPath( String.format( "additionalAddresses[%d]", j ) );
                        ValidationUtils.invokeValidator( addressValidator, address, errors );
                    } finally {
                        errors.popNestedPath();
                    }
                }
                if ( StringUtils.isNotBlank( address.getEmail() ) ) {
                    if ( !address.getEmail().matches( OverseasUserValidator.USERNAME_PATTERN ) ) {
                        errors.rejectValue( String.format( "additionalAddresses[%d].email", j ), "mva.username.not_valid" );
                    }
                }
            }
        }
    }

    private void checkEmpty( CorrectionsLeo correction, Errors errors ) {

        if ( correction.checkEmpty() ) {
            if ( correction.getAllCorrect() == 0 ) {
                errors.reject( "eod.corrections.empty" );
            }
            else if ( !correction.checkMessageForAllCorrect() ) {
                correction.updateMessageAllCorrect();
            }
        }
        if ( correction.checkSubmitterEmpty() ) {
            errors.reject( "eod.corrections.empty_submitter" );
        }
        if ( correction.getSubmitterEmail().length() > 0 && !correction.getSubmitterEmail().matches( OverseasUserValidator.USERNAME_PATTERN ) ) {
            errors.rejectValue( "submitterEmail", "", "Email should be a valid email." );
        }
    }

}
