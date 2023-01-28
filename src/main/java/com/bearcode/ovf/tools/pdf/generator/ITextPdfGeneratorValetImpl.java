/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Singleton implementation of {@link ITextPdfGeneratorValet}.
 * 
 * @author IanBrown
 * 
 * @since Mar 21, 2012
 * @version Apr 16, 2012
 */
public enum ITextPdfGeneratorValetImpl implements ITextPdfGeneratorValet {

	/**
	 * the singleton instance of the combined PDF generator valet.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	INSTANCE;

	/**
	 * Gets an instance of the combined PDF generator valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	public final static ITextPdfGeneratorValet getInstance() {
		return INSTANCE;
	}

	/** {@inheritDoc} */
	@Override
	public final InputStream acquireInputStream(final boolean useInternal, final String filename) throws IOException {
		InputStream inputStream;
		if (useInternal) {
			inputStream = getClass().getClassLoader().getResourceAsStream(filename);
		} else {
			inputStream = new FileInputStream(filename);
		}

		return inputStream;
	}
}
