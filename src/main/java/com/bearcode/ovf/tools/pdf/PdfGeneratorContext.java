package com.bearcode.ovf.tools.pdf;

/**
 * PdfGeneratorContext is a session based object for storing all generator state data
 */
public class PdfGeneratorContext {

	private boolean isFailOnException;

	public boolean isFailOnException() {
		return isFailOnException;
	}

	public void setFailOnException(boolean isFailOnException) {
		this.isFailOnException = isFailOnException;
	}
}
