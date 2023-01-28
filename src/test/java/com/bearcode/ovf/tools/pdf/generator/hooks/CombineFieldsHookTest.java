/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator.hooks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.easymock.EasyMock;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHookCheck;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.bearcode.ovf.tools.pdf.generator.hooks.CombineFieldsHook.CombineConstant;
import com.bearcode.ovf.tools.pdf.generator.hooks.CombineFieldsHook.CombineEntry;
import com.bearcode.ovf.tools.pdf.generator.hooks.CombineFieldsHook.CombineField;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 * Extended {link PdfGeneratorHookCheck} test for {@link CombineFieldsHook}.
 * 
 * @author IanBrown
 * 
 * @since May 1, 2012
 * @version May 2, 2012
 */
public final class CombineFieldsHookTest extends PdfGeneratorHookCheck<CombineFieldsHook> {

	/**
	 * the value for the first name field.
	 * 
	 * @author IanBrown
	 * @since May 2, 2012
	 * @version May 2, 2012
	 */
	private final static String FIRST_NAME_VALUE = "First";

	/**
	 * the value for the middle name field.
	 * 
	 * @author IanBrown
	 * @since May 2, 2012
	 * @version May 2, 2012
	 */
	private final static String MIDDLE_NAME_VALUE = "Middle";

	/**
	 * the value for the last name field.
	 * 
	 * @author IanBrown
	 * @since May 2, 2012
	 * @version May 2, 2012
	 */
	private final static String LAST_NAME_VALUE = "Last";

	/**
	 * the last name of the person.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private final static String LAST_NAME = "ufLastName";

	/**
	 * the first name of the person.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private final static String FIRST_NAME = "ufFirstName";

	/**
	 * the middle name of the person.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private final static String MIDDLE_NAME = "ufMiddleName";

	/**
	 * the first delimiter string.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private final static String FIRST_DELIMITER = ", ";

	/** {@inheritDoc} */
	@Override
	protected final void assertOnCompleted(final Context context) {
		// Nothing to do - it is handled by the mocks.
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertOnFormFieldOutput(final Context context) {
		assertOnFieldOutput((PdfGenerationContext) context);
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertOnInitialize(final Context context) {
		assertOnInitialize((PdfGenerationContext) context);
	}

	/** {@inheritDoc} */
	@Override
	protected final Context createContext(final boolean initialize, final boolean formFields, final boolean userFields,
			final boolean complete) throws PdfGeneratorException {
		final PdfGenerationContext context = createPdfGenerationContext(initialize, formFields, userFields, complete);
		addFields(formFields, userFields, context);
		return context;
	}

	/** {@inheritDoc} */
	@Override
	protected final CombineFieldsHook createHook() {
		return new CombineFieldsHook();
	}

	/** {@inheritDoc} */
	@Override
	protected final void onAllFields(final Context context) throws PdfGeneratorException {
		onAllFields((PdfGenerationContext) context);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForHook() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForHook() {
	}

	/**
	 * Adds the fields.
	 * 
	 * @author IanBrown
	 * @param formFields
	 *            <code>true</code> to add form fields, <code>false</code> otherwise.
	 * @param userFields
	 *            <code>true</code> to add user fields, <code>false</code> otherwise.
	 * @param context
	 *            the context being built.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private void addFields(final boolean formFields, final boolean userFields, final PdfGenerationContext context) {
		final PdfGenerationContext formFieldContext = new PdfGenerationContext(null) {

			/**
			 * the index into the ID list.
			 * 
			 * @author IanBrown
			 * @since May 1, 2012
			 * @version May 1, 2012
			 */
			private int idIdx = 0;

			/**
			 * the index into the value list.
			 * 
			 * @author IanBrown
			 * @since May 1, 2012
			 * @version May 1, 2012
			 */
			private int valueIdx = 0;

			/**
			 * all of the IDs.
			 * 
			 * @author IanBrown
			 * @since May 1, 2012
			 * @version May 1, 2012
			 */
			private final List<String> allIds = Arrays.asList(LAST_NAME, FIRST_NAME, MIDDLE_NAME);

			/**
			 * all of the values.
			 * 
			 * @author IanBrown
			 * @since May 1, 2012
			 * @version May 2, 2012
			 */
			private final List<String> allValues = Arrays.asList(LAST_NAME_VALUE, FIRST_NAME_VALUE, MIDDLE_NAME_VALUE);

			/** {@inheritDoc} */
			@Override
			public final String getId() {
				final int idx[] = new int[2];
				idx[0] = idIdx;
				final String id = retrieve(LAST_NAME, FIRST_NAME, allIds, idx);
				idIdx = idx[1];
				return id;
			}

			/** {@inheritDoc} */
			@Override
			public final String getValue() {
				final int idx[] = new int[2];
				idx[0] = valueIdx;
				final String value = retrieve(LAST_NAME_VALUE, FIRST_NAME_VALUE, allValues, idx);
				valueIdx = idx[1];
				return value;
			}

			/**
			 * Retrieves the next value.
			 * 
			 * @author IanBrown
			 * @param form
			 *            the form values.
			 * @param user
			 *            the user values.
			 * @param all
			 *            all values.
			 * @param idx
			 *            array of indexs: [0] is the input, [1] is the output.
			 * @return the value.
			 * @since May 1, 2012
			 * @version May 1, 2012
			 */
			private final String retrieve(final String form, final String user, final List<String> all, final int[] idx) {
				final String value;
				if (formFields) {
					if (userFields) {
						value = all.get(idx[0]);
						idx[1] = (idx[0] + 1) % all.size();
					} else {
						value = form;
						idx[1] = idx[0];
					}

				} else {
					value = user;
					idx[1] = idx[0];
				}

				return value;
			}
		};

		EasyMock.expect(context.getId()).andDelegateTo(formFieldContext).anyTimes();
		EasyMock.expect(context.getValue()).andDelegateTo(formFieldContext).anyTimes();
	}

	/**
	 * Custom assertion to ensure that the hook has the fields from the context.
	 * 
	 * @author IanBrown
	 * @param context
	 *            the context.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private void assertOnFieldOutput(final PdfGenerationContext context) {
		final Set<String> fieldsSeen = new LinkedHashSet<String>();
		String id = context.getId();
		while (!fieldsSeen.contains(id)) {
			fieldsSeen.add(id);
			final String value = context.getValue();
			final CombineField combineField = getHook().getCombineFieldByField().get(id);
			assertNotNull("There is a combine field for " + id, combineField);
			assertEquals("The combine field for " + id + " has the expected value", value, combineField.getValue());
			id = context.getId();
		}
	}

	/**
	 * Custom assertion to ensure that the hook is initialized properly from the context.
	 * 
	 * @author IanBrown
	 * @param context
	 *            the context.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private void assertOnInitialize(final PdfGenerationContext context) {
		final FieldManager fieldManager = context.getFields();
		@SuppressWarnings("unchecked")
		final Map<String, Item> rawFields = (Map<String, Item>) fieldManager.getRawFields();
		int countKeys = 0;

		for (final String fieldKey : rawFields.keySet()) {
			if (fieldKey.startsWith(CombineFieldsHook.TAG_COMBINE_FIELDS)) {
				checkFieldKey(fieldKey);
				++countKeys;
			}
		}

		assertEquals("There are the correct number of keys mapped", countKeys, getHook().getCombineEntries().size());
		if (!rawFields.isEmpty()) {
			assertNotNull("There is an entry for " + FIRST_NAME, getHook().getCombineFieldByField().get(FIRST_NAME));
			assertNotNull("There is an entry for " + MIDDLE_NAME, getHook().getCombineFieldByField().get(MIDDLE_NAME));
			assertNotNull("There is an entry for " + LAST_NAME, getHook().getCombineFieldByField().get(LAST_NAME));
		}
	}

	/**
	 * Checks to see that the constant has been properly initialized.
	 * 
	 * @author IanBrown
	 * @param constant
	 *            the constant.
	 * @param combineEntry
	 *            the combine entry to check.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private void checkConstant(final String constant, final CombineEntry combineEntry) {
		assertSame("The entry for " + constant + " is the correct type", CombineConstant.class, combineEntry.getClass());
		final CombineConstant combineConstant = (CombineConstant) combineEntry;
		assertEquals("The key for " + constant + " is correct", constant, combineConstant.getKey());
	}

	/**
	 * Checks to see that the field has been properly initialized.
	 * 
	 * @author IanBrown
	 * @param field
	 *            the name of the field.
	 * @param combineEntry
	 *            the combine entry to check.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private void checkField(final String field, final CombineEntry combineEntry) {
		assertSame("The entry for " + field + " is the correct type", CombineField.class, combineEntry.getClass());
		final CombineField combineField = (CombineField) combineEntry;
		assertEquals("The key for " + field + " is correct", field, combineField.getKey());
	}

	/**
	 * Custom assertion to ensure that the key is properly mapped.
	 * 
	 * @author IanBrown
	 * @param fieldKey
	 *            the field key.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private void checkFieldKey(final String fieldKey) {
		final List<CombineEntry> combineEntries = getHook().getCombineEntries().get(fieldKey);
		assertNotNull("There are entries corresponding to the field key " + fieldKey, combineEntries);

		final int startParen = fieldKey.indexOf("(");
		final int endParen = fieldKey.lastIndexOf(")");
		final String parameters = fieldKey.substring(startParen + 1, endParen);
		final StringTokenizer strtok = new StringTokenizer(parameters, "\" \t,", true);
		int entryNumber = 0;
		while (strtok.hasMoreTokens()) {
			String token = strtok.nextToken("\" \t,");

			if (token.equals(",") || token.equals(" ") || token.equals("\t")) {
				continue;

			} else if (token.equals("\"")) {
				token = strtok.nextToken("\"");
				checkConstant("\"" + token + "\"", combineEntries.get(entryNumber));
				token = strtok.nextToken("\"");
				++entryNumber;

			} else {
				checkField(token, combineEntries.get(entryNumber));
				++entryNumber;
			}
		}
	}

	/**
	 * Creates a PDF generation context.
	 * 
	 * @author IanBrown
	 * @param initialize
	 *            if the hook should be initialized, false if it shouldn't.
	 * @param formFields
	 *            <code>true</code> if form fields are to be included, <code>false</code> otherwise.
	 * @param userFields
	 *            <code>true</code> if user fields are to be included, <code>false</code> otherwise.
	 * @param complete
	 *            <code>true</code> if the hooks should be completed, <code>false</code> otherwise.
	 * @return the PDF generation context.
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the context.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private PdfGenerationContext createPdfGenerationContext(final boolean initialize, final boolean formFields,
			final boolean userFields, final boolean complete) throws PdfGeneratorException {
		final PdfGenerationContext context = createMock("Context", PdfGenerationContext.class);
		final FieldManager fieldManager = createMock("FieldManager", FieldManager.class);
		EasyMock.expect(context.getFields()).andReturn(fieldManager).anyTimes();
		final Map<String, Item> rawFields = new LinkedHashMap<String, Item>();
		EasyMock.expect(fieldManager.getRawFields()).andReturn(rawFields).anyTimes();
		final String key = CombineFieldsHook.TAG_COMBINE_FIELDS + "(" + LAST_NAME + ", \"" + FIRST_DELIMITER + "\", " + FIRST_NAME
				+ ", " + MIDDLE_NAME + ")";

		if (initialize) {
			final Item item = createMock("Item", Item.class);
			rawFields.put(key, item);
		}

		if (complete) {
			final StringBuilder valueBuilder = new StringBuilder();
			if (formFields) {
				valueBuilder.append(LAST_NAME_VALUE);
			}
			if (userFields) {
				if (formFields) {
					valueBuilder.append(FIRST_DELIMITER);
				}
				valueBuilder.append(FIRST_NAME_VALUE);
				if (formFields) {
					valueBuilder.append(" ").append(MIDDLE_NAME_VALUE);
				}
			}
			fieldManager.setField(key, valueBuilder.toString());
		}

		return context;
	}

	/**
	 * Ensure that all fields are initialized.
	 * 
	 * @author IanBrown
	 * @param context
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the fields.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	private void onAllFields(final PdfGenerationContext context) throws PdfGeneratorException {
		getHook().onUserFieldOutput(context);
		getHook().onUserFieldOutput(context);
		getHook().onFormFieldOutput(context);
	}
}
