/**
 * 
 */
package com.bearcode.ovf.tools.pdf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValet;

/**
 * Abstract test for implementations of {@link ITextPdfGeneratorValet}.
 * 
 * @author IanBrown
 * 
 * @param <V>
 *            the type of combined PDF generator valet to test.
 * @since Mar 21, 2012
 * @version Apr 16, 2012
 */
public abstract class ITextPdfGeneratorValetCheck<V extends ITextPdfGeneratorValet> extends EasyMockSupport {

	/**
	 * the valet to test.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private V valet;

	/**
	 * Sets up the valet for testing.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@Before
	public final void setUpValet() {
		setUpForValet();
		setValet(createValet());
	}

	/**
	 * Tears down the valet after testing.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@After
	public final void tearDownValet() {
		setValet(null);
		tearDownForValet();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValet#acquireInputStream(boolean, java.lang.String)}
	 * for an external file.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the file.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@Test
	public final void testAcquireInputStream_externalFile() throws IOException {
		final File file = File.createTempFile("pdfgeneratortest", ".pdf");
		try {
			final InputStream actualInputStream = getValet().acquireInputStream(false, file.getPath());
			try {
				assertNotNull("A stream is opened", actualInputStream);

			} finally {
				actualInputStream.close();
			}
		} finally {
			file.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValet#acquireInputStream(boolean, java.lang.String)}
	 * for an internal file.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the file.
	 * @since Mar 21, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	// FIXME this test seems to fail on Jenkins.
	public final void testAcquireInputStream_internalFile() throws IOException {
		//final File folder = acquireClassPathFolder();
		final File file = File.createTempFile("pdfgeneratortest", ".pdf"/*, folder*/);
		try {
			final InputStream actualInputStream = getValet().acquireInputStream(false, file.getAbsolutePath());
			try {
				assertNotNull("A stream is opened", actualInputStream);

			} finally {
				actualInputStream.close();
			}
		} finally {
			file.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValet#acquireInputStream(boolean, java.lang.String)}
	 * for an external file that does not exist.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the file.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@Test(expected = FileNotFoundException.class)
	public final void testAcquireInputStream_noSuchExternalFile() throws IOException {
		getValet().acquireInputStream(false, "No Such File.nsf");
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValet#acquireInputStream(boolean, java.lang.String)}
	 * for an internal file that does not exist.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the file.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@Test
	public final void testAcquireInputStream_noSuchInternalFile() throws IOException {
		final InputStream actualInputStream = getValet().acquireInputStream(true, "No Such File.nsf");

		assertNull("No input stream is returned", actualInputStream);
	}

	/**
	 * Creates a valet of the type to test.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract V createValet();

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected final V getValet() {
		return valet;
	}

	/**
	 * Sets up to test the specific type of valet.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void setUpForValet();

	/**
	 * Tears down the set up for testing the specific type of valet after testing.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void tearDownForValet();

	/**
	 * Acquires a directory file for a folder in the class path.
	 * 
	 * @author IanBrown
	 * @return the class path folder.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private File acquireClassPathFolder() {
		final String classPath = System.getProperty("java.class.path");
		final String[] paths = classPath.split(";");

		for (final String path : paths) {
			final File pathFile = new File(path);
			if (pathFile.isDirectory() && pathFile.canWrite() && pathFile.canRead()) {
				return pathFile;
			}
		}

		return null;
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private final void setValet(final V valet) {
		this.valet = valet;
	}

}
