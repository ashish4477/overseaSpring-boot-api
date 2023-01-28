/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator;

import java.io.IOException;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfFieldManager extends FieldManager {

	private final AcroFields fields;
	protected Logger log = LoggerFactory.getLogger( getClass() );


	public PdfFieldManager(final AcroFields fields) {
		this.fields = fields;
	}

	@Override
	public Object getRawFields() {
		return fields.getFields();
	}
	
	public Map getFields()
	{
	    return fields.getFields();
	}

	@Override
	public void setField(final String id, final String value) throws PdfGeneratorException {
		try {
			if (value == null) {
				fields.setField(id, "");
			} else {
				fields.setField(id, value);
			}
    log.error("PDFField: fieldId="+id+" value="+value);
    log.info("PDFField: fieldId="+id+" value="+value);
		} catch (final IOException e) {
			throw new PdfGeneratorException(e);
		} catch (final DocumentException e) {
			throw new PdfGeneratorException(e);
		}
	}
}