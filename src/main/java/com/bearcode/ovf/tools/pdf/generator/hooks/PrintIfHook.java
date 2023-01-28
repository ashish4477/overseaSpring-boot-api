package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields.Item;

import java.util.*;

public class PrintIfHook extends PdfGeneratorHook {
	class HookContext {
		private final String fieldId;
		private final String fieldValue;
		private final String printFieldId;

		public HookContext(final String fieldId, final String fieldValue, final String printFieldId) {
			super();
			this.fieldId = fieldId;
			this.fieldValue = fieldValue;
			this.printFieldId = printFieldId;
		}

		public String getFieldId() {
			return fieldId;
		}

		public String getFieldValue() {
			return fieldValue;
		}

		public String getPrintFieldId() {
			return printFieldId;
		}
	}

	static final String TAG = "print_if_";
	static final String TAG_EQUAL = "print_if_equal";
	static final String TAG_NOT_EQUAL = "print_if_not_equal";
	static final String TAG_NOT_EMPTY = "print_if_not_empty";

	private final Map<String, String> fields = new HashMap<String, String>();
	private final Map<String, HookContext> printIfFields = new HashMap<String, HookContext>();
	private final Set<String> watchedFields = new HashSet<String>();

	/**
	 * Gets the fields.
	 * 
	 * @author IanBrown
	 * @return the fields.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	public Map<String, String> getFields() {
		return fields;
	}

	/**
	 * Gets the print if fields.
	 * 
	 * @author IanBrown
	 * @return the print if fields.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	public Map<String, HookContext> getPrintIfFields() {
		return printIfFields;
	}

	/**
	 * Gets the watched fields.
	 * 
	 * @author IanBrown
	 * @return the watched fields.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	public Set<String> getWatchedFields() {
		return watchedFields;
	}

	@Override
	public void onCompleted(final Context context) throws PdfGeneratorException {
		if (!(context instanceof PdfGenerationContext)) {
			throw new IllegalArgumentException("context");
		}

		final PdfGenerationContext ctx = (PdfGenerationContext) context;
		final FieldManager fieldManager = ctx.getFields();

		for (final Map.Entry<String, HookContext> it : getPrintIfFields().entrySet()) {
			final String id = it.getKey();
			final HookContext hc = it.getValue();
			setTextField(fieldManager, id, hc);
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

            HookStructure hook = HookStructure.createHookStructure( id.trim() );

			if (!hook.getHookName().startsWith(TAG)) {
				continue;
			}

			final boolean notEmpty = hook.getHookName().startsWith(TAG_NOT_EMPTY);

			if (logger.isDebugEnabled()) {
				logger.debug("{} tag", hook.getHookName());
			}
			final int expectedFields = 2 + (notEmpty ? 0 : 1);
			if (hook.getArguments().size() < expectedFields) {
				logger.warn("Rejected " + id + " - " + expectedFields + " fields are required, found " + hook.getArguments().toString() );
				continue;
			}

			final String val;
			if (notEmpty) {
				val = null;

			} else {
				// extract value - all between quotes
				final int lq = hook.getArguments().get(1).indexOf("\"");
				final int rq = hook.getArguments().get(1).lastIndexOf("\"");
				if (lq == -1 || rq == -1 || rq <= lq) {
					logger.warn("Rejected " + id + " - syntax error");
					continue;
				}
				val = hook.getArguments().get(1).substring(lq + 1, rq).trim();

				// IAB: I don't see the point to this - it should be perfectly valid to have a check for a field that has a value of
				// "" (or just whitespace).
				// if (val.length() == 0) {
				// logger.warn("Rejected " + id + " - value is blank");
				// continue;
				// }
			}

			final HookContext hc = new HookContext(hook.getArguments().get(0), val, hook.getArguments().get(expectedFields - 1));
			if (getPrintIfFields().put(it.getKey(), hc) != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Override field {}", it.getKey());
				}
			}

			getWatchedFields().add(hc.getFieldId());
			getWatchedFields().add(hc.getPrintFieldId());   // check for not quoted value
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
		if (!getWatchedFields().contains(id)) {
			return;
		}

		getFields().put(id, ctx.getValue());
	}

	private void setTextField(final FieldManager fieldManager, final String id, final HookContext hc) throws PdfGeneratorException {
		final String value = getFields().get(hc.getFieldId());
		if (id.trim().startsWith(TAG_EQUAL)) {
			if (value != null && value.equalsIgnoreCase(hc.getFieldValue())) {
                putTextFieldValue( fieldManager, id, hc.getPrintFieldId() );
			}

		} else if (id.trim().startsWith(TAG_NOT_EQUAL)) {
			if ((value == null && hc.getFieldValue() != null) || (value != null && !value.equalsIgnoreCase(hc.getFieldValue()))) {
                putTextFieldValue( fieldManager, id, hc.getPrintFieldId() );
			}

		} else if (id.trim().startsWith(TAG_NOT_EMPTY)) {
			if ((value == null) || value.trim().isEmpty()) {
                putTextFieldValue( fieldManager, id, hc.getPrintFieldId() );
			} else {
				fieldManager.setField(id, value);
			}
		}
	}

    private void putTextFieldValue( final FieldManager fieldManager, final String id, final String printFieldId ) throws PdfGeneratorException {
        String printValue;
        if ( printFieldId.indexOf("\"") != -1 ) {
            printValue = printFieldId.substring( 1, printFieldId.length() - 1 );
        }
        else {
            printValue = getFields().get( printFieldId );
        }
        fieldManager.setField( id, printValue );
    }
}