package com.bearcode.ovf.tools.pdf.generator;

import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;

public class PdfGenerationContext implements PdfGeneratorHook.Context {
	protected String id;
	protected String value;
	protected FieldManager fields;

	public PdfGenerationContext(FieldManager fields, String id, String value) {
		this(fields);
		this.id = id;
		this.value = value;
	}
	
	public PdfGenerationContext(FieldManager fields) {
		this.fields = fields;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public FieldManager getFields() {
		return fields;
	}
}