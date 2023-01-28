package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.service.QuestionFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Alexey Polyakov
 *         Date: Aug 13, 2007
 *         Time: 10:29:37 PM
 */
@Component
public class FieldTypeValidator implements Validator {

    @Autowired
    private QuestionFieldService questionFieldService;

    public boolean supports(Class aClass) {
        return true;
    }

    public void validate(Object object, Errors errors) {
        FieldType fieldType = (FieldType) object;
        if(fieldType.getName() == null || fieldType.getName().length() == 0) {
            errors.reject("name", "rava.admin.fieldtype.empty_name");
        } else if(!questionFieldService.checkFieldType(fieldType)) {
            errors.reject("name", "rava.admin.fieldtype.already_exists");
        }
    }

}
