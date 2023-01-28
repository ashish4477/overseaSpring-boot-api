package com.bearcode.ovf.tools.pdf.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.*;

import org.apache.commons.io.IOUtils;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.tools.pdf.AdvancedPdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfTemplateForm;
import com.google.common.base.Preconditions;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 03.10.14
 * Time: 18:15
 *
 * @author Leonid Ginzburg
 */
public class CombinedPdfGenerator extends AdvancedPdfGenerator {

    private PdfTemplateForm[] pdfTemplateForms;
    protected Logger log = LoggerFactory.getLogger( getClass() );
    
    public CombinedPdfGenerator(OutputStream outputStream, WizardContext wizardContext, TerminalModel model, String language, PdfTemplateForm[] pdfTemplateForms) {
        super(outputStream, wizardContext, model, language);
        this.pdfTemplateForms = pdfTemplateForms;
    }

    public PdfTemplateForm[] getPdfTemplateForms() {
        return pdfTemplateForms;
    }

    public void setPdfTemplateForms(PdfTemplateForm[] pdfTemplateForms) {
        this.pdfTemplateForms = pdfTemplateForms;
    }

	private File generatePdf(final String tempPrefix, final PdfTemplateForm form,Map fields) throws IOException, PdfGeneratorException {
		final File temp = File.createTempFile(tempPrefix + form.name() + "_", ".pdf");
		
		logger.info("Generate {} Form {}", form.name(), temp);
        final OneFormPdfGenerator formGenerator = new OneFormPdfGenerator(new FileOutputStream(temp), getWizardContext(), getModel(), getLanguage(), form,fields);
        
		formGenerator.run();
		formGenerator.dispose();
		
		return temp;
	}

	private Collection<File> generatePdfs() throws PdfGeneratorException {
		Preconditions.checkNotNull( pdfTemplateForms );
		
		final String tempPrefix = Long.toString(System.currentTimeMillis());
		
		try {
			final Collection<File> pdfs = new ArrayList<File>();
			for ( final PdfTemplateForm form : pdfTemplateForms ) {
				Map fields=new HashMap();
				final File pdf = generatePdf(tempPrefix, form,fields);
	
				if (pdf.length() > 0) 
				{
					if (form.name().equalsIgnoreCase("STATE_FORM"))
                    {
                        final String tempPrefix1 = Long.toString(System.currentTimeMillis());
                        final File temp = File.createTempFile(tempPrefix1 + form.name() + "_", ".pdf");
                        PDDocument pd=PDDocument.load(pdf);
                        PDDocumentCatalog docCatalog = pd.getDocumentCatalog();
                        PDAcroForm acroForm = docCatalog.getAcroForm();
                        if (fields != null && fields.size() > 0)
                        {
                            Iterator it=fields.keySet().iterator();
                            while (it.hasNext())
                            {
                                String key=(String)it.next();
                                PDField pdField=acroForm.getField(key);
                                if (pdField != null)
                                {
                            //log.error("FieldName="+pdField.getFullyQualifiedName());
                            //log.error("FieldName="+pdField.getFieldType());
                                    if (pdField.getFieldType().equalsIgnoreCase("Btn"))
                                    {
                                        String value=(String)fields.get(key);
                                        if (value.equalsIgnoreCase("true"))
                                        {
                                            ((PDCheckbox) pdField).check();
                                        }
                                    }
                                }
                            }
                        }
                        try
                        {
                            pd.save(temp);
                            pdfs.add(temp);
                        }
                        catch (Exception ee)
                        {
                            ee.printStackTrace();
                        }
                    }
                    else
                        pdfs.add(pdf);
				} else {
					if (logger.isDebugEnabled()) {
						logger.warn("Cannot generate form {}", form.name());
					}
				}
			}
			return pdfs;

		} catch (final IOException e) {
			throw new PdfGeneratorException(e);
		}
	}

	private void mergePdf(PdfCopy pdfDest, final File pdfSrc) throws IOException, BadPdfFormatException {
		Preconditions.checkNotNull(pdfDest);
		Preconditions.checkNotNull(pdfSrc);
		
		PdfReader pdfReader = null;
		try {
			pdfReader = new PdfReader(new FileInputStream(pdfSrc));
			for (int page = 0, totalPages = pdfReader.getNumberOfPages(); page < totalPages;) {
				pdfDest.addPage(pdfDest.getImportedPage(pdfReader, ++page));
			}
			
		} finally {
			if (pdfReader != null) {
				pdfReader.close();
			}
		}
	}

	private void mergePdfs(final Collection<File> pdfFiles) throws PdfGeneratorException {
		Preconditions.checkNotNull(pdfFiles);
		if (pdfFiles.isEmpty()) {
			return;
		}
		
    	final Document doc = new Document();

    	PdfCopy combinedPdf = null; 
    	try {
       		combinedPdf = new PdfCopy(doc, new BufferedOutputStream(getOutputStream()));
       		doc.open();
       		
          for (final File pdfFile : pdfFiles) {
        	  mergePdf(combinedPdf, pdfFile);
          }
       		
		} catch (DocumentException e) {
			throw new PdfGeneratorException(e);
		} catch (IOException e) {
			throw new PdfGeneratorException(e);
		} finally {
			doc.close();
			
			if (combinedPdf != null) {
				combinedPdf.close();
			}
		}
   	}

	private void deleteTempPdfs(Collection<File> pdfFiles) {
		Preconditions.checkNotNull(pdfFiles);
		for (final File pdfFile : pdfFiles) {
			try {
				pdfFile.delete();
			} catch (Exception unused) {}
		}
	}

	@Override
    public void run() throws PdfGeneratorException {
        Collection<File> pdfFiles = Collections.emptyList();

        try {
            logger.info("Generate Form");

    		pdfFiles = generatePdfs();
    		mergePdfs( pdfFiles );
    		
            logger.info("Done");
		} finally {
    		deleteTempPdfs( pdfFiles );
		}
    }

	@Override
    public void dispose() {
        IOUtils.closeQuietly( getOutputStream() );
    }
}
