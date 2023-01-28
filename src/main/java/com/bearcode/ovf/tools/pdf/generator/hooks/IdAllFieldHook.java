package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields.Item;

import java.util.Map;

public class IdAllFieldHook extends PdfGeneratorHook {
	private static final String USER_FIELD_ID_ALL = PdfGenerator.USER_FIELD_PREFIX + "IdAll";

	public void handleFormField(final PdfGenerationContext ctx) throws PdfGeneratorException {
		// check if we have ufIdAll field and the current field is Identify field...
		if (!isIdentifyField(ctx.getId())) {
			return;
		}
        if ( ctx.getValue().isEmpty() ) {   //ignore empty values. they should not override something valuable
            return;
        }

		@SuppressWarnings("unchecked")
		final Map<String, Item> fields = (Map<String, Item>) ctx.getFields().getRawFields();
		final Item ufIdAll = fields.get(USER_FIELD_ID_ALL);
		if (ufIdAll == null) {
			return;
		}

		// ... then populate ufIdAll
		if (logger.isDebugEnabled()) {
			logger.debug("Set {} field from id={}, value={}", new Object[] { USER_FIELD_ID_ALL, ctx.getId(), ctx.getValue() });
		}
		ctx.getFields()
				.setOutputHeader(
						String.format("Hook " + getClass().getSimpleName() + " Original field id=%s, value=%s", ctx.getId(),
								ctx.getValue()));
		ctx.getFields().setField(USER_FIELD_ID_ALL, ctx.getValue());
	}

	@Override
	public void onFormFieldOutput(final Context ctx) throws PdfGeneratorException {
		try {
			handleFormField((PdfGenerationContext) ctx);
		} catch (final Exception e) {
			throw new PdfGeneratorException(e);
		}
	}

	private boolean isIdentifyField(final String fieldId) {
		return (fieldId != null && (fieldId.equalsIgnoreCase("ufDriversLicense") || // US
				fieldId.equalsIgnoreCase("ufDriversLicence") || // UK
				fieldId.equalsIgnoreCase("ufStateID") || fieldId.equalsIgnoreCase("ufSSN") || fieldId.equalsIgnoreCase("ufSSN_4")));
	}
}