package com.bearcode.ovf.tools.pdf.generator;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.tools.pdf.AdvancedPdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.PdfTemplateForm;
import com.bearcode.ovf.tools.pdf.generator.hooks.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 03.10.14
 * Time: 18:33
 *
 * @author Leonid Ginzburg
 */
public class OneFormPdfGenerator extends AdvancedPdfGenerator {


protected Logger log = LoggerFactory.getLogger( getClass() );
    /**
   	 * the hooks used to transform the template files.
   	 *
   	 * @author IanBrown
   	 * @since Mar 21, 2012
   	 * @version Mar 21, 2012
   	 */
   	private final List<PdfGeneratorHook> hooks = new ArrayList<PdfGeneratorHook>();
    /**
   	 * the object used to write the PDFs.
   	 *
   	 * @author IanBrown
   	 * @since Mar 21, 2012
   	 * @version Mar 21, 2012
   	 */
   	private PdfStamper pdfStamper;
    private PdfTemplateForm templateForm;
    private Map ufields;

    public OneFormPdfGenerator(OutputStream outputStream, WizardContext wizardContext, TerminalModel model, String language, PdfTemplateForm templateForm) {
        super(outputStream, wizardContext, model, language);
        this.templateForm = templateForm;
    }

    public OneFormPdfGenerator(OutputStream outputStream, WizardContext wizardContext, TerminalModel model, String language, PdfTemplateForm templateForm,Map ffields) {
        super(outputStream, wizardContext, model, language);
        this.templateForm = templateForm;
	    this.ufields=ffields;
    }

    public PdfStamper getPdfStamper() {
        return pdfStamper;
    }

    public void setPdfStamper(PdfStamper pdfStamper) {
        this.pdfStamper = pdfStamper;
    }

    public List<PdfGeneratorHook> getHooks() {
        return hooks;
    }

    public PdfTemplateForm getTemplateForm() {
        return templateForm;
    }

    public void setTemplateForm(PdfTemplateForm templateForm) {
        this.templateForm = templateForm;
    }

    /**
   	 * Generates the PDF from the PDF template.
   	 *
   	 * @author IanBrown
   	 * @param templateInput
   	 *            the input stream of the template.
   	 * @param output
   	 *            the output stream.
   	 * @throws com.itextpdf.text.DocumentException
   	 *             if there is a problem with the document.
   	 * @throws java.io.IOException
   	 *             if there is a problem with the file.
   	 * @throws com.bearcode.ovf.tools.pdf.PdfGeneratorException
   	 *             if there is a problem generating the PDF.
   	 * @since Mar 21, 2012
   	 * @version Mar 21, 2012
   	 */
   	private void generatePdf(final InputStream templateInput, final OutputStream output, final TerminalModel model ) throws DocumentException, IOException,
            PdfGeneratorException {

   		try {
            final PdfReader pdfReader = new PdfReader( templateInput );
   			initHooks();
   			setPdfStamper(new PdfStamper(pdfReader, new BufferedOutputStream( output )));
   			final FieldManager fields = createFieldManager();
   			init(fields);
   			processFormFields(fields, model);
   			processUserFields(fields, model);
   			complete(fields);
   			
   			getPdfStamper().setFormFlattening(true);

   		} finally {
   			if (getPdfStamper() != null) {
   				try {
   					getPdfStamper().close();
   				} finally {
   					setPdfStamper(null);
   				}
   			}
   		}
   	}

    /**
   	 * Initializes the hooks for translating values in the PDF template.
   	 *
   	 * @author IanBrown
   	 * @since Mar 21, 2012
   	 * @version Sep 6, 2012
   	 */
   	private void initHooks() {
   		getHooks().clear();

   		getHooks().add( new CheckBoxFormFieldHook() );
   		getHooks().add( new IdAllFieldHook() );
   		getHooks().add(new SharedFieldHook());
   		getHooks().add(new UserFieldHook());
   		getHooks().add(new CommonFieldHook());
   		getHooks().add(new CheckIfHook());
   		getHooks().add(new PrintIfHook());
   		getHooks().add(new UserAgeHook());
   		getHooks().add(new CombineFieldsHook());
   		getHooks().add(new UserFieldViewOutputHook());
        getHooks().add(new BreakApartHook());
        final WizardContext context = getWizardContext();
        if ( context != null &&
                ( context.getFlowType().equals( FlowType.RAVA ) || context.getFlowType().equals( FlowType.FWAB ) ) ) {
            getHooks().add( new SsnHook() );
        }
   	}

    /**
   	 * Creates the field manager.
   	 *
   	 * @author IanBrown
   	 * @return the field manager.
   	 * @since Mar 21, 2012
   	 * @version Apr 16, 2012
   	 */
   	private FieldManager createFieldManager() {
   		return new PdfFieldManager( getPdfStamper().getAcroFields() );
   	}

    /**
   	 * Initialization the fields for PDF generation.
   	 *
   	 * @author IanBrown
   	 * @param fields
   	 *            the fields.
   	 * @throws PdfGeneratorException
   	 *             if there is a problem initializing the fields.
   	 * @since Mar 21, 2012
   	 * @version Mar 21, 2012
   	 */
   	private void init(final FieldManager fields) throws PdfGeneratorException {
   		for (final PdfGeneratorHook hook : getHooks()) {
   			hook.onInitialize(new PdfGenerationContext(fields));
   		}
   	}

    private void processFormFields( final FieldManager fields, final TerminalModel model ) throws PdfGeneratorException {
        for ( final String fieldId : model.getFormFields().keySet() ) {
            final String fieldValue = model.getFormFields().get(fieldId);
    log.error("***OneFormPdfGenerator formfields: filedvalue="+fieldValue+" fieldId="+fieldId);

            final PdfGenerationContext ctx = new PdfGenerationContext(fields, fieldId, fieldValue);
            for (final PdfGeneratorHook hook : getHooks()) {
                hook.onFormFieldOutput(ctx);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Set field, id={}, value={}", ctx.getId(), ctx.getValue());
            }
            fields.setField(ctx.getId(), ctx.getValue());
            this.ufields.put(ctx.getId(), ctx.getValue());
    log.error("***OneFormPdfGenerator formfields : CTX: id="+ctx.getId()+" value="+ctx.getValue());
        }

    }

    private void processUserFields( final FieldManager fields, final TerminalModel model ) throws PdfGeneratorException {
        for ( final String fieldId : model.getUserFields().keySet() ) {
            final String fieldValue = model.getUserFields().get(fieldId);
            final PdfGenerationContext ctx = new PdfGenerationContext(fields, fieldId, fieldValue);
            for (final PdfGeneratorHook hook : getHooks()) {
                hook.onUserFieldOutput(ctx);
            }
log.error("***OneFormPdfGenerator userfields: filedvalue="+fieldValue+" fieldId="+fieldId);
log.error("***OneFormPdfGenerator userfields : CTX: id="+ctx.getId()+" value="+ctx.getValue());
            if (logger.isDebugEnabled()) {
                logger.debug("Set field, id={}, value={}", ctx.getId(), ctx.getValue());
            }
            fields.setField(ctx.getId(), ctx.getValue());
            this.ufields.put(ctx.getId(), ctx.getValue());
        }
    }

    /**
   	 * Complete the generation of the PDF for the fields.
   	 *
   	 * @author IanBrown
   	 * @param fields
   	 *            the fields.
   	 * @throws PdfGeneratorException
   	 *             if there is a problem completing the PDF generation.
   	 * @since Mar 21, 2012
   	 * @version Apr 16, 2012
   	 */
   	private void complete(final FieldManager fields) throws PdfGeneratorException {
   		for (final PdfGeneratorHook hook : getHooks()) {
   			hook.onCompleted(new PdfGenerationContext(fields));
   		}
   	}


    @Override
    public void dispose() {
        IOUtils.closeQuietly( getOutputStream() );
    }

    @Override
    public void run() throws PdfGeneratorException {
        try {
            InputStream inputStream = templateForm.openPdfTemplate( getWizardContext(), getLanguage() );
            if ( inputStream != null ) {
                generatePdf(inputStream, getOutputStream(), getModel());
            }
        } catch (final IOException e) {
            throw new PdfGeneratorException(e);
        } catch (final DocumentException e) {
            throw new PdfGeneratorException(e);
        }
    }
}
