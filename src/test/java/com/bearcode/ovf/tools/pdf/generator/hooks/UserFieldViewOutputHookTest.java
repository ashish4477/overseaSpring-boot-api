/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator.hooks;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHookCheck;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 * Extended {@link PdfGeneratorHookCheck} test for {@link UserFieldViewOutputHook}.
 * 
 * @author IanBrown
 * 
 * @since Sep 6, 2012
 * @version Sep 6, 2012
 */
public final class UserFieldViewOutputHookTest extends PdfGeneratorHookCheck<UserFieldViewOutputHook> {

	/**
	 * the field identifier.
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private final static String ID = PdfGenerator.USER_FIELD_PREFIX + "FieldId";
	
	/**
	 * the view value.
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private final static String VIEW_VALUE = "View Value";
	
	/**
	 * the output value.
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private final static String OUTPUT_VALUE = "Output Value";
	
	/**
	 * the actual value provided for the field.
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private final static String VALUE;
	
	static {
		VALUE = VIEW_VALUE + "=" + OUTPUT_VALUE;
	}
	
	/** {@inheritDoc} */
	@Override
	protected final void assertOnCompleted(final Context context) {
		// Nothing to do - it is handled by the mocks.
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertOnFormFieldOutput(final Context context) {
		// Nothing to do - it is handled by the mocks.
	}

	/** {@inheritDoc} */
	@Override
	protected final void assertOnInitialize(final Context context) {
		// Nothing to do - it is handled by the mocks.
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
	protected final UserFieldViewOutputHook createHook() {
		return new UserFieldViewOutputHook();
	}

	/** {@inheritDoc} */
	@Override
	protected final void onAllFields(final Context context) throws PdfGeneratorException {
		getHook().onFormFieldOutput(context);
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
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
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
			private final List<String> allIds = Arrays.asList(ID);

			/**
			 * all of the values.
			 * 
			 * @author IanBrown
			 * @since May 1, 2012
			 * @version May 2, 2012
			 */
			private final List<String> allValues = Arrays.asList(VALUE);

			/** {@inheritDoc} */
			@Override
			public final String getId() {
				final int idx[] = new int[2];
				idx[0] = idIdx;
				final String id = retrieve(ID, null, allIds, idx);
				idIdx = idx[1];
				return id;
			}

			/** {@inheritDoc} */
			@Override
			public final String getValue() {
				final int idx[] = new int[2];
				idx[0] = valueIdx;
				final String value = retrieve(VALUE, null, allValues, idx);
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
	 * Creates the PDF generation context for the specified values.
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
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private PdfGenerationContext createPdfGenerationContext(final boolean initialize, final boolean formFields,
			final boolean userFields, final boolean complete) {
		final PdfGenerationContext context = createMock("Context", PdfGenerationContext.class);
		final FieldManager fieldManager = createMock("FieldManager", FieldManager.class);
		EasyMock.expect(context.getFields()).andReturn(fieldManager).anyTimes();
		final Map<String, Item> rawFields = new LinkedHashMap<String, Item>();
		EasyMock.expect(fieldManager.getRawFields()).andReturn(rawFields).anyTimes();
		if (formFields) {
			fieldManager.setOutputHeader((String) EasyMock.anyObject());
			EasyMock.expectLastCall().anyTimes();
			context.setValue(OUTPUT_VALUE);
		}
		return context;
	}
}
