package com.bearcode.ovf.editor;

import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.service.MailingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * @author leonid.
 */
@Component
public class MailingListPropertyEditor extends PropertyEditorSupport {
    @Autowired
    private MailingListService mailingListService;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(mailingListService.findMailingList( Long.parseLong( text ) ));
    }

    @Override
    public String getAsText() {
        if ( getValue() != null && getValue() instanceof MailingList )
            return String.valueOf( ((MailingList)getValue()).getId() );
        return null;
    }

}
