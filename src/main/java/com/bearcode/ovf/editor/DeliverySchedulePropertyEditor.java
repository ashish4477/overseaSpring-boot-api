package com.bearcode.ovf.editor;

import com.bearcode.ovf.model.registrationexport.DeliverySchedule;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * Created by leonid on 11.10.16.
 */
@Component
public class DeliverySchedulePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText( String text ) throws IllegalArgumentException {
        if ( StringUtils.isBlank( text ) ) {
            setValue( DeliverySchedule.NONE );
            return;
        }
        final DeliverySchedule schedule = DeliverySchedule.valueOf( text );
        setValue( schedule != null ? schedule : DeliverySchedule.NONE );
    }
}
