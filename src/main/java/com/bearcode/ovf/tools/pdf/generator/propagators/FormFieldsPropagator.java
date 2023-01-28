package com.bearcode.ovf.tools.pdf.generator.propagators;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Date: 03.10.14
 * Time: 19:00
 *
 * @author Leonid Ginzburg
 */
@Component
public class FormFieldsPropagator extends FieldPropagator {

    @Override
    public void propagate(TerminalModel model, WizardContext wizardContext) {
        final Collection<Answer> answers = wizardContext.getAnswers();
        for ( Answer answer : answers ) {
            final QuestionField questionField = answer.getField();
            if (questionField.getInPdfName() != null && !questionField.getInPdfName().trim().isEmpty()) {
                answer.output(model.getFormFields(), false); // output values with no escape xml
            }
        }
    }
}
