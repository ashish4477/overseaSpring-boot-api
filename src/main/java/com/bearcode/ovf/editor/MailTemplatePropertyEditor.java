package com.bearcode.ovf.editor;

import com.bearcode.ovf.model.mail.MailTemplate;
import com.bearcode.ovf.service.MailingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * @author leonid.
 */
@Component
public class MailTemplatePropertyEditor extends PropertyEditorSupport {
    @Autowired
    private MailingListService mailingListService;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(mailingListService.findTemplate( Long.parseLong( text ) ));
    }

    @Override
    public String getAsText() {
        if ( getValue() != null && getValue() instanceof MailTemplate )
            return String.valueOf( ((MailTemplate)getValue()).getId() );
        return null;
    }

}
