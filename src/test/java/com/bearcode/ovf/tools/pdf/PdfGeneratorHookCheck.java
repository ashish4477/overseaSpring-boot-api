/**
 * 
 */
package com.bearcode.ovf.tools.pdf;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context;

/**
 * Abstract test for implementations of {@link PdfGeneratorHook}.
 * 
 * @author IanBrown
 * 
 * @param <H>
 *            the type of PDF generator hook to test.
 * @since May 1, 2012
 * @version May 1, 2012
 */
public abstract class PdfGeneratorHookCheck<H extends PdfGeneratorHook> extends EasyMockSupport {

	/**
	 * the PDF generator hook to test.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private H hook;

	/**
	 * Sets up to test the PDF generator hook.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	@Before
	public final void setUpHook() {
		setUpForHook();
		setHook(createHook());
	}

	/**
	 * Tears down the set up for testing the PDF generator hook.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	@After
	public final void tearDownHook() {
		setHook(null);
		tearDownForHook();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.PdfGeneratorHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)} for the
	 * case where there are all fields.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the PDF generation.
	 * @since May 1, 2012
	 * @version Sep 6, 2012
	 */
	@Test
	public final void testOnCompleted_allFields() throws PdfGeneratorException {
		final Context context = createContext(true, true, true, true);
		replayAll();
		getHook().onInitialize(context);
		onAllFields(context);

		getHook().onCompleted(context);

		assertOnCompleted(context);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.PdfGeneratorHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)} for the
	 * case where there are only form fields.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the PDF generation.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	@Test
	public final void testOnCompleted_onlyFormFields() throws PdfGeneratorException {
		final Context context = createContext(true, true, false, true);
		replayAll();
		getHook().onInitialize(context);
		getHook().onFormFieldOutput(context);

		getHook().onCompleted(context);

		assertOnCompleted(context);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.PdfGeneratorHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)} for the
	 * case where there are only user fields.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the PDF generation.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	@Test
	public final void testOnCompleted_onlyUserFields() throws PdfGeneratorException {
		final Context context = createContext(true, false, true, true);
		replayAll();
		getHook().onInitialize(context);
		getHook().onUserFieldOutput(context);

		getHook().onCompleted(context);

		assertOnCompleted(context);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.PdfGeneratorHook#onFormFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem handling the form field.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	@Test
	public final void testOnFormFieldOutput() throws PdfGeneratorException {
		final Context context = createContext(true, true, false, false);
		replayAll();
		getHook().onInitialize(context);

		getHook().onFormFieldOutput(context);

		assertOnFormFieldOutput(context);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.PdfGeneratorHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)} for the
	 * case where the hook is invoked.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	@Test
	public final void testOnInitialize_invoked() throws PdfGeneratorException {
		final Context context = createContext(true, false, false, false);
		replayAll();

		getHook().onInitialize(context);

		assertOnInitialize(context);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.PdfGeneratorHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)} for the
	 * case where the hook is not invoked.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	@Test
	public final void testOnInitialize_notInvoked() throws PdfGeneratorException {
		final Context context = createContext(false, false, false, false);
		replayAll();

		getHook().onInitialize(context);

		assertOnInitialize(context);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.PdfGeneratorHook#onUserFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem handling the user field.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	@Test
	public final void testOnUserFieldOutput() throws PdfGeneratorException {
		final Context context = createContext(true, false, true, false);
		replayAll();
		getHook().onInitialize(context);

		getHook().onUserFieldOutput(context);

		assertOnInitialize(context);
		verifyAll();
	}

	/**
	 * Custom assertion to ensure that the hook completes properly.
	 * 
	 * @author IanBrown
	 * @param context
	 *            the context.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected abstract void assertOnCompleted(Context context);

	/**
	 * Custom assertion to ensure that the hook is properly updated for the form field(s).
	 * 
	 * @author IanBrown
	 * @param context
	 *            the context.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected abstract void assertOnFormFieldOutput(Context context);

	/**
	 * Custom assertion to ensure that the hook is initialized properly from the context.
	 * 
	 * @author IanBrown
	 * @param context
	 *            the context.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected abstract void assertOnInitialize(Context context);

	/**
	 * Creates context for the specific type of hook.
	 * 
	 * @author IanBrown
	 * @param initialize
	 *            <code>true</code> if the hook should be initialized, <code>false</code> if it shouldn't.
	 * @param formFields
	 *            <code>true</code> if form fields are to be included, <code>false</code> otherwise.
	 * @param userFields
	 *            <code>true</code> if user fields are to be included, <code>false</code> otherwise.
	 * @param complete
	 *            <code>true</code> if the hooks should be completed, <code>false</code> otherwise.
	 * @return the context.
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the context.
	 * @since May 1, 2012
	 * @version May 2, 2012
	 */
	protected abstract Context createContext(boolean initialize, boolean formFields, boolean userFields, boolean complete)
			throws PdfGeneratorException;

	/**
	 * Creates a PDF generator hook of the type to test.
	 * 
	 * @author IanBrown
	 * @return the PDF generator hook.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected abstract H createHook();

	/**
	 * Gets the hook.
	 * 
	 * @author IanBrown
	 * @return the hook.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected final H getHook() {
		return hook;
	}

	/**
	 * Ensure that all fields are initialized.
	 * 
	 * @author IanBrown
	 * @param context
	 *            the context.
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the fields.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected abstract void onAllFields(Context context) throws PdfGeneratorException;

	/**
	 * Sets up to test the specific type of PDF generator hook.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected abstract void setUpForHook();

	/**
	 * Tears down the set up for testing the specific type of PDF generator hook.
	 * 
	 * @author IanBrown
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	protected abstract void tearDownForHook();

	/**
	 * Sets the hook.
	 * 
	 * @author IanBrown
	 * @param hook
	 *            the hook to set.
	 * @since May 1, 2012
	 * @version May 1, 2012
	 */
	private final void setHook(final H hook) {
		this.hook = hook;
	}
}
