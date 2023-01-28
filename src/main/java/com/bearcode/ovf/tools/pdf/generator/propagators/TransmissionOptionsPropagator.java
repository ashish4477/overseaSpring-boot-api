package com.bearcode.ovf.tools.pdf.generator.propagators;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import com.bearcode.ovf.webservices.localelections.model.TransmissionMethodsList;
import com.bearcode.ovf.webservices.localelections.model.TransmissionMethodsListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author leonid.
 */
@Component
public class TransmissionOptionsPropagator extends FieldPropagator {

    @Autowired
    private LocalElectionsService localElectionsService;

    @Override
    public void propagate( TerminalModel model, WizardContext wizardContext ) {
        String state = wizardContext.getWizardResults().getVotingAddress().getState();

        StateVoterInformation svid = localElectionsService.findStateVoterInformation( state );

        if ( svid != null ) {
            for ( TransmissionMethodsList method : svid.getTransmissionMethods() ) {
                for ( TransmissionMethodsListItem item : method.getItems() ) {
                    model.getFormFields().put( createFieldId( method, item ), item.isAllowed() ? Boolean.TRUE.toString() : Boolean.FALSE.toString() );
                }
            }
        }
    }

    private String createFieldId( TransmissionMethodsList method, TransmissionMethodsListItem item ) {
        StringBuilder builder = new StringBuilder();
        builder.append( PdfGenerator.USER_FIELD_CHECKBOX_PREFIX )
                .append( method.getVoterType().getKind() )
                .append( item.getDocumentType().getName().replaceAll( "[ -]", "" ) )
                .append( item.getDocumentTransmissionMethod().getName().replaceAll( "[ -]", "" ) );
        return builder.toString();
    }
}
