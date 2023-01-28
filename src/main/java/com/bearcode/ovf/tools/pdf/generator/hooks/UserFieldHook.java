package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;

public class UserFieldHook extends PdfGeneratorHook {
	private static final String USER_FIELD_GENDER = PdfGenerator.USER_FIELD_PREFIX + "Gender";
	private static final String USER_FIELD_CHECKBOX_GENDER_FEMALE = PdfGenerator.USER_FIELD_CHECKBOX_PREFIX
			+ "GenderFemale";
	private static final String USER_FIELD_CHECKBOX_GENDER_MALE = PdfGenerator.USER_FIELD_CHECKBOX_PREFIX
			+ "GenderMale";
	private static final String GENDER_FEMALE = "F";
	private static final String GENDER_MALE = "M";

	public void handleUserField(final PdfGenerationContext ctx) throws PdfGeneratorException {
		if (!ctx.getId().equalsIgnoreCase(USER_FIELD_GENDER) || ctx.getValue() == null) {
			return;
		}

		if (ctx.getValue().equalsIgnoreCase(GENDER_MALE)) {
			ctx.getFields().setOutputHeader(
					String.format("Hook " + getClass().getSimpleName() + " Original field id=%s, value=%s", ctx.getId(),
							ctx.getValue()));
			ctx.setId(USER_FIELD_CHECKBOX_GENDER_MALE);
			ctx.setValue(Boolean.TRUE.toString());
		} else if (ctx.getValue().equalsIgnoreCase(GENDER_FEMALE)) {
			ctx.getFields().setOutputHeader(
					String.format("Hook " + getClass().getSimpleName() + " Original field id=%s, value=%s", ctx.getId(),
							ctx.getValue()));
			ctx.setId(USER_FIELD_CHECKBOX_GENDER_FEMALE);
			ctx.setValue(Boolean.TRUE.toString());
		}
	}

	@Override
	public void onUserFieldOutput(final Context ctx) throws PdfGeneratorException {
		try {
			handleUserField((PdfGenerationContext) ctx);
		} catch (final Exception e) {
			throw new PdfGeneratorException(e);
		}
	}
}