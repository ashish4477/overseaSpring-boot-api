package com.bearcode.ovf.tools.pdf;

public class PdfGeneratorException extends Exception {
	private static final long serialVersionUID = 1L;

	public PdfGeneratorException(Exception e) {
		super(e);
	}

	public PdfGeneratorException(String message, Exception e) {
		super(message, e);
	}
}
