package com.bearcode.ovf.editor;

import com.bearcode.ovf.model.registrationexport.ExportLevel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * Created by leonid on 11.10.16.
 */
@Component
public class ExportLevelPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText( String text ) throws IllegalArgumentException {
        if ( StringUtils.isBlank( text ) ) {
            setValue( ExportLevel.EXPORT_ALL );
            return;
        }
        final ExportLevel level = ExportLevel.valueOf( text );
        setValue( level != null ? level : ExportLevel.EXPORT_ALL );
    }
}
