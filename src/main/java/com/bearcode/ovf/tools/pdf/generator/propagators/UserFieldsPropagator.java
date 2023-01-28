package com.bearcode.ovf.tools.pdf.generator.propagators;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import com.bearcode.ovf.utils.UserInfoFields;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Date: 03.10.14
 * Time: 18:54
 *
 * @author Leonid Ginzburg
 */
@Component
public class UserFieldsPropagator extends FieldPropagator {

    @Override
    public void propagate( TerminalModel model, WizardContext wizardContext ) {
        final Map<String, String> userFields = UserInfoFields.getUserValues(wizardContext.getWizardResults());
        for ( String fieldName : userFields.keySet() ) {
            final String fieldId = PdfGenerator.USER_FIELD_PREFIX + fieldName;
            model.getUserFields().put( fieldId, unformatValue( userFields.get( fieldName ) ) );
        }
    }
}
