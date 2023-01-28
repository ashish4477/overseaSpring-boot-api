/**
 * 
 */
package com.bearcode.ovf.tools.pdf;

import com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValetImpl;

/**
 * Extended {@link ITextPdfGeneratorValetCheck} test for {@link ITextPdfGeneratorValetImpl}.
 * 
 * @author IanBrown
 * 
 * @since Mar 21, 2012
 * @version Apr 16, 2012
 */
public final class ITextPdfGeneratorValetImplTest extends ITextPdfGeneratorValetCheck<ITextPdfGeneratorValetImpl> {

	/** {@inheritDoc} */
	@Override
	protected final ITextPdfGeneratorValetImpl createValet() {
		return (ITextPdfGeneratorValetImpl) ITextPdfGeneratorValetImpl.getInstance();
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForValet() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForValet() {
	}
}
