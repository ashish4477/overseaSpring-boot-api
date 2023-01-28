package com.bearcode.ovf.editor;

import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * Date: 27.12.11
 * Time: 16:29
 *
 * @author Leonid Ginzburg
 */
@Component
public class QuestionPropertyEditor extends PropertyEditorSupport {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Override
    public String getAsText() {
        if ( getValue() != null && getValue() instanceof Question )
            return String.valueOf( ((Question)getValue()).getId() ) ;
        return null;
    }

    @Override
    public void setAsText( String text ) throws IllegalArgumentException {
        setValue( questionnaireService.findQuestionById( Long.parseLong( text ) ) );
    }
}
