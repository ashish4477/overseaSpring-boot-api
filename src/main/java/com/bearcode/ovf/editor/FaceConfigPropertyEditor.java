package com.bearcode.ovf.editor;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.service.FacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * Created by leonid on 10.10.16.
 */
@Component
public class FaceConfigPropertyEditor extends PropertyEditorSupport{

    @Autowired
    private FacesService facesService;

    @Override
    public String getAsText() {
        Object value = getValue();
        return (value != null && value instanceof FaceConfig) ? ((FaceConfig)value).getId().toString(): "";
    }

    @Override
    public void setAsText( String text ) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
            return;
        }
        FaceConfig faceConfig = null;
        try {
            Long id = Long.parseLong( text );
            if ( id != null ) {
                faceConfig = facesService.findConfigById( id );
            }
        } catch (NumberFormatException e) {
            //nothing
        }
        setValue( faceConfig );

    }
}
