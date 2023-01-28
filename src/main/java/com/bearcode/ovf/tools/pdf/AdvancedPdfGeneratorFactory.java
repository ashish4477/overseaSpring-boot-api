package com.bearcode.ovf.tools.pdf;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.tools.pdf.generator.CombinedPdfGenerator;
import com.bearcode.ovf.tools.pdf.generator.PdfGeneratorUtil;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import com.bearcode.ovf.tools.pdf.generator.propagators.FieldPropagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.OutputStream;
import java.util.*;

/**
 * Date: 06.10.14
 * Time: 16:25
 *
 * @author Leonid Ginzburg
 */
@Component
public class AdvancedPdfGeneratorFactory {

    /**
   	 * the language to use. If there are files in the language folder, they will be used in place of the fallback defaults.
   	 * @author IanBrown
   	 * @since Sep 5, 2012
   	 * @version Sep 5, 2012
   	 */
   	private String language = "EN";

    @Autowired
    private List<FieldPropagator> propagators;

    @PostConstruct
    public void sortPropagators() {
        Collections.sort(propagators, new Comparator<FieldPropagator>() {
            @Override
            public int compare(FieldPropagator o1, FieldPropagator o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
    }

    public TerminalModel createModel( WizardContext context ) {
        TerminalModel model = new TerminalModel();
        for ( FieldPropagator propagator : propagators ) {
            propagator.propagate( model, context );
        }
        return model;
    }

    public PdfGenerator createPdfGenerator( final WizardContext wizardContext, final OutputStream outputStream, final String language )
   			throws PdfGeneratorException {
        final TerminalModel model = createModel(wizardContext);
        AdvancedPdfGenerator generator = new CombinedPdfGenerator( outputStream, wizardContext, model, language, PdfTemplateForm.values() );

        return generator;
    }

    public PdfGenerator createPdfGenerator( final WizardContext wizardContext, final OutputStream outputStream ) throws PdfGeneratorException {
        return createPdfGenerator( wizardContext, outputStream, getLanguage() );
    }

    /**
   	 * Gets the language.
   	 * @author IanBrown
   	 * @return the language.
   	 * @since Sep 5, 2012
   	 * @version Sep 5, 2012
   	 */
   	public String getLanguage() {
   		return language;
   	}

   	/**
   	 * Sets the language.
   	 * @author IanBrown
   	 * @param language the language to set.
   	 * @since Sep 5, 2012
   	 * @version Sep 5, 2012
   	 */
   	public void setLanguage(String language) {
   		this.language = language;
   	}

}
