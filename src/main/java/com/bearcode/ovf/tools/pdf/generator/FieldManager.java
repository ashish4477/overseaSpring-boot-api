package com.bearcode.ovf.tools.pdf.generator;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;

import java.util.Map;


public abstract class FieldManager {

	public void setOutputHeader(String header) {
		
	}
	
	public abstract void setField(String id, String value) throws PdfGeneratorException;
	
	public Object getRawFields() {
		return null;
	}
	
	public Map getFields()
	{
	    return null;
	}
}