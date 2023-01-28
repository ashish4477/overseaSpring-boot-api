/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for objects that provide resources to {@link com.bearcode.ovf.tools.pdf.PdfTemplateForm} objects.
 * 
 * @author IanBrown
 * 
 * @since Mar 21, 2012
 * @version Apr 16, 2012
 */
public interface ITextPdfGeneratorValet {

	/**
	 * Acquires an input stream for the specified filename.
	 * 
	 * @author IanBrown
	 * @param useInternal
	 *            <code>true</code> to use the internal PDFs path, <code>false</code> to use an external path.
	 * @param filename
	 *            the name of the file.
	 * @return the input stream.
	 * @throws IOException
	 *             if there is a problem opening the input stream.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	InputStream acquireInputStream(boolean useInternal, String filename) throws IOException;
}
