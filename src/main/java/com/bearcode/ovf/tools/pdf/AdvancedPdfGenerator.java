package com.bearcode.ovf.tools.pdf;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

/**
 * Date: 06.10.14
 * Time: 15:42
 *
 * @author Leonid Ginzburg
 */
public abstract class AdvancedPdfGenerator implements PdfGenerator {

    /**
   	 * the logger for the object.
   	 *
   	 * @author IanBrown
   	 * @since Mar 21, 2012
   	 * @version Mar 21, 2012
   	 */
   	protected final Logger logger = LoggerFactory.getLogger(getClass());

    private OutputStream outputStream;

    private TerminalModel model;

    private WizardContext wizardContext;

    private String language;

    protected AdvancedPdfGenerator( final OutputStream outputStream,
                                    final WizardContext wizardContext,
                                    final TerminalModel model,
                                    final String language) {
        this.outputStream = outputStream;
        this.model = model;
        this.wizardContext = wizardContext;
        this.language = language;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public TerminalModel getModel() {
        return model;
    }

    public void setModel(TerminalModel model) {
        this.model = model;
    }

    public WizardContext getWizardContext() {
        return wizardContext;
    }

    public void setWizardContext(WizardContext wizardContext) {
        this.wizardContext = wizardContext;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
