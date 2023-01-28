package com.bearcode.ovf.tools.pdf.generator.crypto;

public class CipherServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public CipherServiceException(String message, Exception e) {
		super(message, e);
	}
}
