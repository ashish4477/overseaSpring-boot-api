package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;

public class CheckBoxFormFieldHook extends PdfGeneratorHook {
	private static final String USER_FIELD_CHECKBOX_PARTY_NONE = PdfGenerator.USER_FIELD_CHECKBOX_PREFIX + "PartyNone";
	private static final String USER_FIELD_THIRD_PARTY = PdfGenerator.USER_FIELD_PREFIX + "ThirdParty";

	private static final String USER_FIELD_PARTY = "Party";
	private static final String USER_FIELD_RACE = "Race";
	private static final String REGEX_OPTION_PREFIX = "^[a-zA-Z0-9]*\\)";

	private FieldManager fields;

	public void handleFormField(final PdfGenerationContext ctx) throws PdfGeneratorException {
		if (!ctx.getId().startsWith(PdfGenerator.USER_FIELD_CHECKBOX_PREFIX)) {
			return;
		}

		final String fieldId = ctx.getId();
		final String fieldValue = ctx.getValue();
		final String[] values = fieldValue.split("=");

		if (values.length == 2 || (!fieldValue.equalsIgnoreCase("true") && !fieldValue.equalsIgnoreCase("false") && !fieldValue.isEmpty()) ) {
            final String value;
            if (values.length == 2) {
                value = values[1];
            } else {
                value = fieldValue;
            }
            ctx.getFields().setOutputHeader(
					String.format("Hook " + getClass().getSimpleName() + " Original field id=%s, value=%s", fieldId, fieldValue));
			ctx.getFields().setOutputHeader("Hook " + getClass().getSimpleName() + " The answer has special value='" + value + "'");
			ctx.setId(fieldId + value);
			ctx.setValue(Boolean.TRUE.toString());

			if (fieldId.equalsIgnoreCase(USER_FIELD_CHECKBOX_PARTY_NONE)) {
				// reset ufThirdParty field
				fields.setField(USER_FIELD_THIRD_PARTY, "");
			}
		}

		// special case for field with True as a suffix
		if (fieldId.endsWith("True")) {
			ctx.getFields().setOutputHeader(
					String.format("Hook " + getClass().getSimpleName() + " Original field id=%s, value=%s", fieldId, fieldValue));
			ctx.getFields().setOutputHeader("Hook " + getClass().getSimpleName() + " ucXXXXTrue field");
			final String fn = fieldId.substring(0, fieldId.length() - 4);
			if (Boolean.parseBoolean(fieldValue)) {
				fields.setField(fieldId, "true");
				fields.setField(fn + "False", "");
			} else {
				fields.setField(fieldId, "");
				fields.setField(fn + "False", "true");
			}
		}

		// populate human readable fields
		setFieldAsHumanReadable(fieldId, USER_FIELD_PARTY, values[0]);
		setFieldAsHumanReadable(fieldId, USER_FIELD_RACE, values[0]);
	}

	@Override
	public void onFormFieldOutput(final Context ctx) throws PdfGeneratorException {
		handleFormField((PdfGenerationContext) ctx);
	}

	@Override
	public void onInitialize(final Context ctx) throws PdfGeneratorException {
		this.fields = ((PdfGenerationContext) ctx).getFields();
	}

	private void setFieldAsHumanReadable(final String fieldId, final String field, final String value) throws PdfGeneratorException {
		if (fieldId.equalsIgnoreCase(PdfGenerator.USER_FIELD_CHECKBOX_PREFIX + field)) {
			// remove seq like 'x)' where x is [a-zA-Z0-9]
			final String humanValue = value.replaceFirst(REGEX_OPTION_PREFIX, "").trim();
			fields.setField(PdfGenerator.USER_FIELD_PREFIX + field, humanValue);
		}
	}
}