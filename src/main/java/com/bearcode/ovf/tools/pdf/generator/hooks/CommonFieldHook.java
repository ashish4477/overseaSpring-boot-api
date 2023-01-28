package com.bearcode.ovf.tools.pdf.generator.hooks;

import java.util.Date;

import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;

public class CommonFieldHook extends PdfGeneratorHook {
	private static final String USER_FIELD_CURRENT_DATE = PdfGenerator.USER_FIELD_PREFIX + "CurrentDate";
	private static final String USER_FIELD_CURRENT_DATE_LONG = PdfGenerator.USER_FIELD_PREFIX + "CurrentDateLong";

	@Override
	public void onCompleted(final Context context) throws PdfGeneratorException {
		// populate current date
		final PdfGenerationContext ctx = (PdfGenerationContext) context;
		ctx.getFields().setOutputHeader("Hook " + getClass().getSimpleName());
		ctx.getFields().setField( USER_FIELD_CURRENT_DATE, PdfGenerator.SHORT_FORMAT.format( new Date() ) );
		ctx.getFields().setField(USER_FIELD_CURRENT_DATE_LONG, PdfGenerator.LONG_FORMAT.format(new Date()));
	}
}