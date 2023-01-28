package com.bearcode.ovf.validators;

import com.bearcode.ovf.forms.AdminExportConfigForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by leonid on 13.10.16.
 */
@Component
public class AdminExportConfigValidator implements Validator {
    @Override
    public boolean supports( Class<?> clazz ) {
        return AdminExportConfigForm.class.isAssignableFrom( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        AdminExportConfigForm form = (AdminExportConfigForm) target;

        if ( form.getFaceConfigs() == null || form.getFaceConfigs().size() == 0 ) {
            errors.rejectValue( "faceConfigs", "", "You should select as least one Face." );
        }

    }
}
