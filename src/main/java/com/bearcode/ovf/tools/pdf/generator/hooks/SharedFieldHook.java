package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedFieldHook extends PdfGeneratorHook {
	private static final String TAG_SHARE_FIELDS = "share_field";

	// map one of shared field to shared field
	// ufXxx -> share_fields(ufXxx,ifYyyy)
	// ifYyyy -> share_fields(ufXxx,ifYyyy)
	private final Map<String, Set<String>> sharedFields = new HashMap<String, Set<String>>();

	public void handleFormField(final PdfGenerationContext ctx) throws PdfGeneratorException {
		// check if we have shared field
		if (!this.sharedFields.containsKey(ctx.getId())) {
			return;
		}
        if ( ctx.getValue().isEmpty() ) {   //ignore empty values. they should not override something valuable
            return;
        }

		final Set<String> shared = this.sharedFields.get(ctx.getId());
		if (shared == null || shared.isEmpty()) {
			return;
		}

		ctx.getFields().setOutputHeader("Hook " + getClass().getSimpleName());
		for (final String field : shared) {
			if (logger.isDebugEnabled()) {
				logger.debug("Set {} field from id={}, value={}", new Object[] { ctx.getId(), field, ctx.getValue() });
			}
			ctx.getFields().setField(field, ctx.getValue());
		}
	}

	@Override
	public void onFormFieldOutput(final Context ctx) throws PdfGeneratorException {
		try {
			handleFormField((PdfGenerationContext) ctx);
		} catch (final Exception e) {
			throw new PdfGeneratorException(e);
		}
	}

	@Override
	public void onInitialize(final Context context) throws PdfGeneratorException {
		final PdfGenerationContext ctx = (PdfGenerationContext) context;

		@SuppressWarnings("unchecked")
		final Map<String, Item> fields = (Map<String, Item>) ctx.getFields().getRawFields();
		if (fields == null || fields.isEmpty()) {
			return;
		}

		for (final Map.Entry<String, Item> it : fields.entrySet()) {
			String id = it.getKey();
			if (id == null) {
				continue;
			}

			id = id.trim();
			if (id.isEmpty()) {
				continue;
			}

			if (!id.startsWith(TAG_SHARE_FIELDS)) {
				continue;
			}

			final int s = id.indexOf("(");
			if (s == -1) {
				logger.warn("Rejected " + id + " - syntax error");
				continue;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Tag share {}", id);
			}
			final String[] shareFields = id.substring(s + 1, id.length() - 1).split(",");
			for (final String shareField : shareFields) {
				addSharedField(shareField, it.getKey());
			}
		}
	}

	private void addSharedField(String key, String shareField) {
		shareField = shareField.trim();
		key = key.trim();

		if (logger.isDebugEnabled()) {
			logger.debug("Add Shared Key {}=>{}", key, shareField);
		}

		Set<String> sharedSetFields = null;
		if (this.sharedFields.containsKey(key)) {
			sharedSetFields = this.sharedFields.get(key);
		} else {
			sharedSetFields = new HashSet<String>();
			this.sharedFields.put(key, sharedSetFields);
		}
		sharedSetFields.add(shareField);
	}
}