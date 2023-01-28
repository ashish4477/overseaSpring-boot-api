/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;

/**
 * Extended {@link PdfGeneratorHook} to handle user fields that have values of the form "view value=output value".
 * @author IanBrown
 *
 * @since Sep 6, 2012
 * @version Sep 6, 2012
 */
public class UserFieldViewOutputHook extends PdfGeneratorHook {

	/** {@inheritDoc} */
	@Override
	public void onInitialize(Context ctx) throws PdfGeneratorException {}
	
	/** {@inheritDoc} */
	@Override
	public void onUserFieldOutput(Context ctx) throws PdfGeneratorException {}
	
	/** {@inheritDoc} */
	@Override
	public void onFormFieldOutput(Context ctx) throws PdfGeneratorException {
		onFormFieldOutput((PdfGenerationContext) ctx);
	}
	
	/**
	 * Handles the case when a form field is output to the context.
	 * <p>
	 * This is the main method for the hook. If the field is a standard user field and contains a value of the form "view value=output value", the result is that the output value is used for the field.
	 * @author IanBrown
	 * @param ctx the PDF generator context.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private void onFormFieldOutput(PdfGenerationContext ctx) {
		if (!ctx.getId().startsWith(PdfGenerator.USER_FIELD_PREFIX)) {
			return;
		}

		final String fieldId = ctx.getId();
		final String fieldValue = ctx.getValue();
		final String[] values = fieldValue.split("=");

		if (values.length == 2) {
			final String outputValue = values[1];
			ctx.getFields().setOutputHeader(
					String.format("Hook " + getClass().getSimpleName() + " Original field id=%s, value=%s", fieldId, fieldValue));
			ctx.getFields().setOutputHeader("Hook " + getClass().getSimpleName() + " The answer has special value='" + outputValue + "'");
			ctx.setValue(outputValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void onCompleted(Context ctx) throws PdfGeneratorException {}
}
