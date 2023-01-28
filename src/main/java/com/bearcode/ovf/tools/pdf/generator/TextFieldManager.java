/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator;

import java.io.PrintWriter;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;

public class TextFieldManager extends FieldManager {
	private final PrintWriter printWriter;

	public TextFieldManager(final PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	@Override
	public void setField(final String id, final String value) throws PdfGeneratorException {
		printWriter.format("Set field id=%s, value=%s\n", id, value);
	}

	@Override
	public void setOutputHeader(final String header) {
		printWriter.println(header);
	}

}