package com.bearcode.ovf.editor;

import com.bearcode.ovf.service.QuestionFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * Date: 22.11.11
 * Time: 17:14
 *
 * @author Leonid Ginzburg
 */
@Component
public class FieldTypePropertyEditor extends PropertyEditorSupport {

    @Autowired
    private QuestionFieldService questionFieldService;

    @Override
    public String getAsText() {
        if ( getValue() != null )
            return super.getAsText();
        return null;
    }

    @Override
    public void setAsText( String text ) throws IllegalArgumentException {
        setValue( questionFieldService.findFieldTypeById( Long.parseLong( text ) ) );
    }
}
