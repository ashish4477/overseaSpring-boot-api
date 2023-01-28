package com.bearcode.ovf.tools.pdf.generator.hooks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields.Item;

public class CheckIfHook extends PdfGeneratorHook {
	class HookContext {
		private final String fieldId;
		private final String fieldValue;

		public HookContext(final String fieldId, final String fieldValue) {
			super();
			this.fieldId = fieldId;
			this.fieldValue = fieldValue;
		}

		public String getFieldId() {
			return fieldId;
		}

		public String getFieldValue() {
			return fieldValue;
		}
	}

	private static final String TAG = "check_if_";
	private static final String TAG_EMPTY = "check_if_empty";
	private static final String TAG_NOT_EMPTY = "check_if_not_empty";

	private static final String TAG_CONTAINS = "check_if_contains";
	// map one of shared field to shared field
	// ufXxx -> share_fields(ufXxx,ifYyyy)
	// ifYyyy -> share_fields(ufXxx,ifYyyy)
	private final Map<String, String> fields = new HashMap<String, String>();
	private final Map<String, Set<HookContext>> checkIfContainsFields = new HashMap<String, Set<HookContext>>();

	private final Map<String, Set<String>> checkIfFields = new HashMap<String, Set<String>>();

	@Override
	public void onCompleted(final Context context) throws PdfGeneratorException {
		if (!(context instanceof PdfGenerationContext)) {
			throw new IllegalArgumentException("context");
		}

		final PdfGenerationContext ctx = (PdfGenerationContext) context;
		final FieldManager fieldManager = ctx.getFields();

		for (final Map.Entry<String, Set<String>> it : checkIfFields.entrySet()) {
			final String id = it.getKey();
			final Set<String> fields = it.getValue();
			setCheckBoxField(fieldManager, id, fields);
		}

		for (final Map.Entry<String, Set<HookContext>> it : checkIfContainsFields.entrySet()) {
			final String id = it.getKey();
			final Set<HookContext> hooks = it.getValue();
			setCheckIfEqualField(fieldManager, id, hooks);
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
	public void onInitialize(final Context context) throws PdfGeneratorException {
		final PdfGenerationContext ctx = (PdfGenerationContext) context;

		@SuppressWarnings("unchecked")
		final Map<String, Item> rawFields = (Map<String, Item>) ctx.getFields().getRawFields();
		if (rawFields == null || rawFields.isEmpty()) {
			return;
		}

		for (final Map.Entry<String, Item> it : rawFields.entrySet()) {
			String id = it.getKey();
			if (id == null || id.trim().isEmpty()) {
				continue;
			}

			id = id.trim();
			if (!id.startsWith(TAG)) {
				continue;
			}

			if (id.startsWith(TAG_CONTAINS)) {
				initializeContainsFields(id, it.getKey());
			} else {
				initializeCheckFields(id, it.getKey());
			}
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

	private void addField(final String checkIfField, String checkField) {
		checkField = checkField.trim();
		logger.debug("Add CheckIf Key '{}'=>'{}'", checkIfField, checkField);

		Set<String> fields = null;
		if (this.checkIfFields.containsKey(checkIfField)) {
			fields = this.checkIfFields.get(checkIfField);
		} else {
			fields = new HashSet<String>();
			this.checkIfFields.put(checkIfField, fields);
		}
		fields.add(checkField);
	}

	private void handleField(final PdfGenerationContext ctx) throws PdfGeneratorException {
		fields.put(ctx.getId(), ctx.getValue());
	}

	private void initializeCheckFields(final String id, final String fullId) {
		final int sb = id.indexOf("(");
		final int eb = id.lastIndexOf(")");
		if (sb == -1 || eb == -1 || eb < sb) {
			//logger.warn("Rejected " + id + " - syntax error");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("{} tag", id);
		}
		final String[] fields = id.substring(sb + 1, id.length() - 1).split(",");
		for (final String field : fields) {
			if (!field.trim().isEmpty()) {
				addField(fullId, field);
			}
		}

	}

	private void initializeContainsFields(final String id, final String fullId) {
		final int sb = id.indexOf("(");
		final int eb = id.lastIndexOf(")");
		if (sb == -1 || eb == -1 || eb < sb) {
			logger.warn("Rejected " + id + " - syntax error");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("{} tag", id);
		}
		final String[] fields = id.substring(sb + 1, id.length() - 1).split(",");
		if (fields.length < 2) {
			logger.warn("Rejected " + id + " - at least two fields are required, found " + Arrays.toString(fields));
			return;
		}

		final String[] rawVals = id.substring(sb + 1, id.length() - 1).split("\"");
		// extract value - all between quotes
		if (rawVals.length < 2) {
			logger.warn("Rejected " + id + " - at least two values are required, found " + Arrays.toString(rawVals));
			return;
		}
		final Set<String> vals = new HashSet<String>();
		for (int i = 1; i < rawVals.length; i++) {
			final String val = rawVals[i].trim();
			if (val.length() > 0 && !val.equals(",")) {
				vals.add(val);
			}
		}

		if (vals.size() == 0) {
			logger.warn("Rejected " + id + " no real values provided");
			return;
		}

		final Set<HookContext> hooks = new HashSet<HookContext>();
		for (final String val : vals) {
			final HookContext hc = new HookContext(fields[0].trim(), val);
			hooks.add(hc);
		}
		if (checkIfContainsFields.put(fullId, hooks) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Override field {}", fullId);
			}
		}

	}

	private void setCheckBoxField(final FieldManager fieldManager, final String id, final Set<String> checkFields)
			throws PdfGeneratorException {
		boolean isEmpty = true;
		String reasonField = "", reasonValue = "";
		for (final String field : checkFields) {
			final String value = fields.get(field);
			reasonField = field;
			reasonValue = value;
			if (value != null && !value.trim().isEmpty()) {
				isEmpty = false;
				break;
			}
		}

		if ((id.trim().startsWith(TAG_EMPTY) && isEmpty) || (id.trim().startsWith(TAG_NOT_EMPTY) && !isEmpty)) {
			if (logger.isDebugEnabled()) {
				logger.debug("{}, reason field={}, value={}", new Object[] { id, reasonField, reasonValue });
			}
			fieldManager.setField(id, Boolean.TRUE.toString());
		}
	}

	private void setCheckIfEqualField(final FieldManager fieldManager, final String id, final Set<HookContext> hooks)
			throws PdfGeneratorException {
		for (final HookContext hc : hooks) {
			final String value = fields.get(hc.getFieldId());
			if (id.trim().startsWith(TAG_CONTAINS)) {
				if (value != null && value.toLowerCase().contains(hc.getFieldValue().toLowerCase())) {
					fieldManager.setField(id, Boolean.TRUE.toString());
					return;
				}
			}
		}
	}
}