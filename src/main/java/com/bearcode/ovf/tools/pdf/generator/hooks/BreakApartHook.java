package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Date: 03.02.14
 * Time: 21:51
 *
 * @author Leonid Ginzburg
 */
public class BreakApartHook extends PdfGeneratorHook {

    class HookContext {
        private String fieldId;
        private HookTag tag;

        HookContext( final String fieldId, final HookTag tag ) {
            this.fieldId = fieldId;
            this.tag = tag;
        }

        String getFieldId() {
            return fieldId;
        }

        public HookTag getTag() {
            return tag;
        }
    }

    enum HookTag {
        TAG_LEFT( "part_left", 0 ),
        TAG_RIGHT( "part_right", 1 ),
        TAG_NAME( "candidate_name", 2 ),
        TAG_PARTY( "candidate_party", 1 ),
        TAG_OFFICE( "candidate_office", 0 );

        private String tag;
        private int index;

        HookTag( String tag, int index ) {
            this.tag = tag;
            this.index = index;
        }

        public String getTag() {
            return tag;
        }

        public int getIndex() {
            return index;
        }
    }

    private static final String TAG = "part_";
    private static final String TAG_LEFT = "part_left";
    private static final String TAG_RIGHT = "part_right";

    private final Map<String, HookContext> breakApartFields = new HashMap<String, HookContext>();
  	private final Set<String> watchedFields = new HashSet<String>();
    private final Map<String, String> fields = new HashMap<String, String>();

    public Map<String, HookContext> getBreakApartFields() {
        return breakApartFields;
    }

    public Set<String> getWatchedFields() {
        return watchedFields;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    @Override
    public void onInitialize(Context context) throws PdfGeneratorException {
        final PdfGenerationContext ctx = (PdfGenerationContext) context;

        @SuppressWarnings("unchecked")
        final Map<String, AcroFields.Item> rawFields = (Map<String, AcroFields.Item>) ctx.getFields().getRawFields();
        if (rawFields == null || rawFields.isEmpty()) {
            return;
        }

        for (final Map.Entry<String, AcroFields.Item> it : rawFields.entrySet()) {
            String id = it.getKey();
            if (id == null || id.trim().isEmpty()) {
                continue;
            }

            HookStructure hook = HookStructure.createHookStructure( id.trim() );

            for ( HookTag tag : HookTag.values() ) {
                if ( hook.getHookName().equalsIgnoreCase( tag.getTag() ) ) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("{} tag", hook.getHookName());
                    }
                    if (hook.getArguments().size() < 1) {
                        logger.warn("Rejected " + id + " - 1 field is required, found " + hook.getArguments().toString() );
                        break;
                    }

                    final HookContext hc = new HookContext( hook.getArguments().get(0), tag );
                    if ( getBreakApartFields().put( it.getKey(), hc ) != null ) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Override field {}", it.getKey());
                        }
                    }

                    getWatchedFields().add(hc.getFieldId());

                }
            }
        }
    }

    @Override
    public void onFormFieldOutput(Context context) throws PdfGeneratorException {
        final PdfGenerationContext ctx = (PdfGenerationContext) context;
        final String id = ctx.getId();
        if (!getWatchedFields().contains(id)) {
            return;
        }

        getFields().put(id, ctx.getValue());
    }

    @Override
    public void onCompleted(Context context) throws PdfGeneratorException {
        final PdfGenerationContext ctx = (PdfGenerationContext) context;
        final FieldManager fieldManager = ctx.getFields();

      		for (final Map.Entry<String, HookContext> it : getBreakApartFields().entrySet()) {
      			final String id = it.getKey();
      			final HookContext hc = it.getValue();
      			setTextField(fieldManager, id, hc);
      		}
    }

    private void setTextField(final FieldManager fieldManager, final String id, final HookContext hc) throws PdfGeneratorException {
   		final String value = getFields().get(hc.getFieldId());
        if ( value == null || value.trim().length() == 0 ) return;
        final String[] parts = value.split("\\|",-1);
        String printValue;
        if ( parts.length > hc.getTag().getIndex() ) {
                printValue = parts[ hc.getTag().getIndex() ].trim();
        }
        else {
            printValue = value;
        }
        fieldManager.setField( id, printValue );
    }
}
