/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator.hooks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 * Extended {@link PdfGeneratorHook} that provides for the ability to combine multiple fields into a single result.
 * 
 * @author IanBrown
 * 
 * @since May 1, 2012
 * @version May 2, 2012
 */
public final class CombineFieldsHook extends PdfGeneratorHook {

	/**
	 * Implementation of {@link CombineEntry} that represents a constant.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	static final class CombineConstant implements CombineEntry {

		/**
		 * the key for the constant.
		 * 
		 * @author IanBrown
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		private final String key;

		/**
		 * the value for the constant.
		 * 
		 * @author IanBrown
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		private final String value;

		/**
		 * Constructs a combine constant for the specified key. The value is equal to the key without any leading or trailing
		 * quotes. The value is not trimmed, so it may contain leading or trailing white space.
		 * 
		 * @author IanBrown
		 * @param key
		 *            the key.
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		public CombineConstant(final String key) {
			this.key = key;
			value = key.substring(1, key.length() - 1);
		}

		/** {@inheritDoc} */
		@Override
		public final String getKey() {
			return key;
		}

		/** {@inheritDoc} */
		@Override
		public final String getValue() {
			return value;
		}

		/** {@inheritDoc} */
		@Override
		public final boolean needsDelimiter() {
			return false;
		}

		/** {@inheritDoc} */
		@Override
		public final String toString() {
			return getClass().getSimpleName() + " " + getKey() + " = " + getValue();
		}
	}

	/**
	 * Interface for objects to be combined.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	interface CombineEntry {

		/**
		 * Gets the key representing the entry.
		 * 
		 * @author IanBrown
		 * @return the key.
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		String getKey();

		/**
		 * Gets the value of the entry.
		 * 
		 * @author IanBrown
		 * @return the value.
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		String getValue();

		/**
		 * Is a delimiter needed before or after this object?
		 * 
		 * @author IanBrown
		 * @return <code>true</code> if a delimiter is needed, <code>false</code> otherwise.
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		boolean needsDelimiter();
	}

	/**
	 * Implementation of {@link CombineEntry} that represents a field to be filled from the data.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	static final class CombineField implements CombineEntry {

		/**
		 * the key for the combine field.
		 * 
		 * @author IanBrown
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		private final String key;

		/**
		 * the value of the field.
		 * 
		 * @author IanBrown
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		private String value = "";

		/**
		 * Constructs a combine field with the specified key (the name of the field).
		 * 
		 * @author IanBrown
		 * @param key
		 *            the key.
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		public CombineField(final String key) {
			this.key = key;
		}

		/** {@inheritDoc} */
		@Override
		public final String getKey() {
			return key;
		}

		/** {@inheritDoc} */
		@Override
		public final String getValue() {
			return (value == null) ? "" : value;
		}

		/** {@inheritDoc} */
		@Override
		public final boolean needsDelimiter() {
			return !getValue().isEmpty();
		}

		/**
		 * Sets the value of the combine field.
		 * 
		 * @author IanBrown
		 * @param value
		 *            the value.
		 * @since May 1, 2012
		 * @version May 1, 2012
		 */
		public final void setValue(final String value) {
			this.value = value;
		}

		/** {@inheritDoc} */
		@Override
		public final String toString() {
			return getClass().getSimpleName() + " " + getKey() + " = " + getValue();
		}
	}

	/**
	 * the combine field by the name of the field rather than by the name of the PDF key.
	 * 
	 * @author IanBrown
	 * @since May 2, 2012
	 * @version May 2, 2012
	 */
	private final Map<String, CombineField> combineFieldByField = new HashMap<String, CombineField>();

	/**
	 * the tag used to indicate that this hook should be invoked.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	final static String TAG_COMBINE_FIELDS = "combine";

	/**
	 * the entries to be combined.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private final Map<String, List<CombineEntry>> combineEntries = new HashMap<String, List<CombineEntry>>();

	/**
	 * Gets the combine entries by PDF field key.
	 * 
	 * @author IanBrown
	 * @return the combine entries by PDF field key.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	public Map<String, List<CombineEntry>> getCombineEntries() {
		return combineEntries;
	}

	/**
	 * Gets the combine field by data field key map.
	 * 
	 * @author IanBrown
	 * @return the combine field by data field key map.
	 * @since May 2, 2012
	 * @version May 2, 2012
	 */
	public Map<String, CombineField> getCombineFieldByField() {
		return combineFieldByField;
	}

	/** {@inheritDoc} */
	@Override
	public void onCompleted(final Context ctx) throws PdfGeneratorException {
		final PdfGenerationContext context = (PdfGenerationContext) ctx;
		final FieldManager fields = context.getFields();

		for (final Map.Entry<String, List<CombineEntry>> idEntries : getCombineEntries().entrySet()) {
			final String id = idEntries.getKey();
			final List<CombineEntry> entries = idEntries.getValue();
			setOutputField(id, entries, fields);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onFormFieldOutput(final Context ctx) throws PdfGeneratorException {
		try {
			handleField(ctx);
		} catch (final Exception e) {
			throw new PdfGeneratorException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onInitialize(final Context ctx) throws PdfGeneratorException {
		final PdfGenerationContext context = (PdfGenerationContext) ctx;
		final FieldManager fields = context.getFields();
		@SuppressWarnings("unchecked")
		final Map<String, Item> rawFields = (Map<String, Item>) fields.getRawFields();

		if (rawFields != null) {
			for (final String fieldKey : rawFields.keySet()) {
				initializeFieldKey(fieldKey);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onUserFieldOutput(final Context ctx) throws PdfGeneratorException {
		try {
			handleField(ctx);
		} catch (final Exception e) {
			throw new PdfGeneratorException(e);
		}
	}

	/**
	 * Builds the combine entries from the input key.
	 * 
	 * @author IanBrown
	 * @param key
	 *            the key.
	 * @return the combine entries.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private List<CombineEntry> buildCombineEntries(final String key) {
		final List<CombineEntry> keyEntries = new LinkedList<CombineEntry>();
		final int startParen = key.indexOf("(");
		final int endParen = key.lastIndexOf(")");
		final String parameters = key.substring(startParen + 1, endParen);
		final StringTokenizer strtok = new StringTokenizer(parameters, "\" \t,", true);
		while (strtok.hasMoreTokens()) {
			String token = strtok.nextToken("\" \t,");

			if (token.equals(",") || token.equals(" ") || token.equals("\t")) {
				continue;

			} else if (token.equals("\"")) {
				token = strtok.nextToken("\"");
				keyEntries.add(buildConstant("\"" + token + "\""));
				token = strtok.nextToken("\"");

			} else {
				keyEntries.add(buildField(token));
			}
		}

		return keyEntries;
	}

	/**
	 * Builds a combine constant for the specified key.
	 * 
	 * @author IanBrown
	 * @param key
	 *            the key.
	 * @return the combine constant.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private CombineConstant buildConstant(final String key) {
		return new CombineConstant(key);
	}

	/**
	 * Creates a (or uses an existing) combine field for the specified key.
	 * 
	 * @author IanBrown
	 * @param key
	 *            the key.
	 * @return the combine field.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private CombineField buildField(final String key) {
		CombineField combineField = getCombineFieldByField().get(key);
		if (combineField == null) {
			combineField = new CombineField(key);
			getCombineFieldByField().put(key, combineField);
		}
		return combineField;
	}

	/**
	 * Handles the field provided by the context.
	 * 
	 * @author IanBrown
	 * @param ctx
	 *            the context.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private void handleField(final Context ctx) {
		final PdfGenerationContext context = (PdfGenerationContext) ctx;
		final String id = context.getId();
		final String value = context.getValue();
		final CombineField combineField = getCombineFieldByField().get(id);
		if (combineField != null) {
			combineField.setValue(value);
		}
	}

	/**
	 * Initializes the input field key. This value can be something we don't recognize or it can be a combine request.
	 * 
	 * @author IanBrown
	 * @param fieldKey
	 *            the field key.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private void initializeFieldKey(final String fieldKey) throws PdfGeneratorException {
		if (fieldKey == null) {
			// Ignore null keys.
			return;
		}

		final String key = fieldKey.trim();
		if (!key.startsWith(TAG_COMBINE_FIELDS)) {
			// Ignore keys we don't recognize.
			return;
		}

		final List<CombineEntry> keyEntries = buildCombineEntries(key);
		getCombineEntries().put(fieldKey, keyEntries);
	}

	/**
	 * Sets the value for the output field based on the combine entries.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the id of the field.
	 * @param entries
	 *            the entries.
	 * @param fields
	 *            the field manager.
	 * @throws PdfGeneratorException
	 *             if there is a problem setting the field.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private void setOutputField(final String id, final List<CombineEntry> entries, final FieldManager fields)
			throws PdfGeneratorException {
		final StringBuilder sb = new StringBuilder();
		final StringBuilder ssb = new StringBuilder();
		String delimiter = "";
		boolean before = true;

		for (final CombineEntry entry : entries) {
			final String value = entry.getValue();
			if (entry instanceof CombineConstant) {
				if (before) {
					ssb.append(value);
				}

			} else {
				if ((value == null) || value.isEmpty()) {
					before = false;
					ssb.setLength(0);
					ssb.trimToSize();

				} else {
					if (ssb.length() > 0) {
						sb.append(ssb.toString());
						ssb.setLength(0);
						ssb.trimToSize();
					} else {
						sb.append(delimiter);
					}

					sb.append(value);
					delimiter = " ";
					before = true;
				}
			}
		}

		if (ssb.length() > 0) {
			sb.append(ssb.toString());
		}

		fields.setField(id, sb.toString());
	}
}
