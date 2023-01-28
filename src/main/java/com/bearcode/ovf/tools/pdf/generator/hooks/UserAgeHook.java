package com.bearcode.ovf.tools.pdf.generator.hooks;

import java.util.Calendar;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;

public class UserAgeHook extends PdfGeneratorHook {

	private final static String USER_FIELD_AGE = "ufUserAge";
	private final static String USER_FIELD_DOB_DAY = "ufBirthDate";
	private final static String USER_FIELD_DOB_MONTH = "ufBirthMonth";
	private final static String USER_FIELD_DOB_YEAR = "ufBirthYear";

	private String day;
	private String month;
	private String year;

	@Override
	public void onCompleted(final Context context) throws PdfGeneratorException {
		if (day == null || month == null || year == null) {
			return;
		}

		int years = -1;
		try {
			final int d = Integer.parseInt(day);
			final int m = Integer.parseInt(month);
			final int y = Integer.parseInt(year);

			final Calendar dob = Calendar.getInstance();
			dob.clear();
			dob.set(y, m - 1, d);

			final Calendar now = Calendar.getInstance();
			final Calendar clone = (Calendar) dob.clone();

			while (!clone.after(now)) {
				clone.add(Calendar.YEAR, 1);
				years++;
			}

		} catch (final Exception e) {
			// Ooops! wrong day/month/year???
		}

		if (years != -1) {
			final PdfGenerationContext ctx = (PdfGenerationContext) context;
			ctx.getFields().setField(USER_FIELD_AGE, Integer.toString(years));
		}
	}

	@Override
	public void onFormFieldOutput(final Context ctx) throws PdfGeneratorException {
		try {
			handleField((PdfGenerationContext) ctx);
		} catch (final Exception e) {
			throw new PdfGeneratorException(e);
		}
	}

	@Override
	public void onUserFieldOutput(final Context ctx) throws PdfGeneratorException {
		try {
			handleField((PdfGenerationContext) ctx);
		} catch (final Exception e) {
			throw new PdfGeneratorException(e);
		}
	}

	private void handleField(final PdfGenerationContext ctx) throws PdfGeneratorException {
		final String id = ctx.getId();
		if (USER_FIELD_DOB_DAY.equals(id)) {
			this.day = ctx.getValue();
		} else if (USER_FIELD_DOB_MONTH.equals(id)) {
			this.month = ctx.getValue();
		} else if (USER_FIELD_DOB_YEAR.equals(id)) {
			this.year = ctx.getValue();
		}
	}
}
