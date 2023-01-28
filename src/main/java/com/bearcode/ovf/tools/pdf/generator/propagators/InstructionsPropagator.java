package com.bearcode.ovf.tools.pdf.generator.propagators;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.questionnaire.PdfFilling;
import com.bearcode.ovf.service.RelatedService;
import com.bearcode.ovf.tools.QuestionnaireArbiter;
import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 03.10.14
 * Time: 19:08
 *
 * @author Leonid Ginzburg
 */
@Component
public class InstructionsPropagator extends FieldPropagator {

    @Autowired
    protected RelatedService relatedService;

    @Autowired
   	protected VelocityEngine velocityEngine;

    @Autowired
   	private QuestionnaireArbiter arbiter;

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;  // should be last in the list
    }

    @Override
    public void propagate(TerminalModel model, WizardContext wizardContext) {
        try {
            final Collection<PdfFilling> fillings = relatedService.findPdfFillings();
            arbiter.ajustInstructions(fillings, wizardContext);
            final ToolManager toolManager = new ToolManager();
            final ToolContext toolContext = toolManager.createContext();
            final VelocityContext context = new VelocityContext( toolContext );

            putAllIntoContext( context, model.getFormFields() );
            putAllIntoContext( context, model.getUserFields() );
            final LocalOffice leo = model.getLocalOfficial();
            if ( leo != null ) {
                context.put( "leo", leo );
                if ( leo.getOfficers().size() >= 1 ) {
                    context.put( "registrar", leo.getOfficers().get(0) );
                }
            }

        /*
       	 * Create a map with result of pdf instructions evaluation.
         * Instructions with same key will be concatenated.
       	 *
       	 */
            Map<String,String> fillingResult = new HashMap<String, String>();
            // add
            for (final PdfFilling filling : fillings) {
                try {
                    final StringWriter w = new StringWriter();
                    final String key = filling.getInPdfName().trim();
                    if (key.length() > 0) {
                        velocityEngine.evaluate(context, w, "evaluating instructions", filling.getText());
                        String value = w.getBuffer().toString().trim();
                        final String stored = fillingResult.get(key);
                        if (stored != null) {
                            value = stored + "\n" + value;
                        }
                        fillingResult.put( key, unformatValue( value ) );
                    }
                } catch (final Exception e) {
                    logger.error("Instruction evaluation error", e);
                }
            }
            model.getFormFields().putAll( fillingResult );
        } catch (Exception e) {
            logger.error( "Instruction error", e );
        }

    }

    protected void putAllIntoContext(final VelocityContext context, final Map<String, String> model) {
  		for (final String key : model.keySet()) {
  			context.put(key, model.get(key));
  		}
  	}

}
