package com.bearcode.ovf.tools.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfGeneratorHook {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void onInitialize(Context ctx) throws PdfGeneratorException {}
	public void onUserFieldOutput(Context ctx) throws PdfGeneratorException {}
	public void onFormFieldOutput(Context ctx) throws PdfGeneratorException {}
	public void onCompleted(Context ctx) throws PdfGeneratorException {}

	public interface Context {
	}
}
