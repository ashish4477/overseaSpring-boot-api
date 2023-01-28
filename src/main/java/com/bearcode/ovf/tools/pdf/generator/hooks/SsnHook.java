package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.PdfGeneratorHook;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;

import java.util.regex.Pattern;

/**
 * @author leonid.
 */
public class SsnHook extends PdfGeneratorHook {

    private static final Pattern ssnPattern = Pattern.compile( "(S|s){2}(N|n)" );

    /** {@inheritDoc} */
    @Override
    public void onInitialize(Context ctx) throws PdfGeneratorException {}

    /** {@inheritDoc} */
    @Override
    public void onUserFieldOutput(Context ctx) throws PdfGeneratorException {}

    /** {@inheritDoc} */
    @Override
    public void onFormFieldOutput(Context ctx) throws PdfGeneratorException {
        onFormFieldOutput((PdfGenerationContext) ctx);
    }

    /**
     * Handles the case when a form field is output to the context.
     * <p>
     * This is the main method for the hook. If the field is a standard user field and contains a value of the form "view value=output value", the result is that the output value is used for the field.
     * @author IanBrown
     * @param ctx the PDF generator context.
     * @since Sep 6, 2012
     * @version Sep 6, 2012
     */
    private void onFormFieldOutput(PdfGenerationContext ctx) {
        if (!ssnPattern.matcher( ctx.getId() ).find() ) {
            return;
        }

        final String fieldId = ctx.getId();
        final String fieldValue = ctx.getValue();
        if ( fieldValue.length() == 4 ) {
            ctx.getFields().setOutputHeader(
                    String.format("Hook %s Add dashes before last 4 digits of SSN, id=%s, value=%s", getClass().getSimpleName(), fieldId, fieldValue));
            ctx.setValue( String.format( "XXXXX%s", fieldValue ) );
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onCompleted(Context ctx) throws PdfGeneratorException {}
}
