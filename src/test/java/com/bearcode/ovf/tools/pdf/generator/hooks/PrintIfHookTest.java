/**
 * 
 */
package com.bearcode.ovf.tools.pdf.generator.hooks;

import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.generator.FieldManager;
import com.bearcode.ovf.tools.pdf.generator.PdfGenerationContext;
import com.itextpdf.text.pdf.AcroFields.Item;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test for {@link PrintIfHook}.
 * 
 * @author IanBrown
 * 
 * @since May 9, 2012
 * @version May 9, 2012
 */
public final class PrintIfHookTest extends EasyMockSupport {

	/**
	 * the value of the first field.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String FIELD1_VALUE = "field1 value";

	/**
	 * the name of the first field.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String FIELD1 = "field1";

	/**
	 * the value of the second field.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String FIELD2_VALUE = "field2 value";

	/**
	 * the name of the second field.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String FIELD2 = "field2";

	/**
	 * the value for the print field.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String PRINT_FIELD_VALUE = "print value";

	/**
	 * the name of the print field.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String PRINT_FIELD = "printfield";

	/**
	 * the value to match.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String CHECK_FIELD_VALUE = "check value";

	/**
	 * the name of the check field.
	 * 
	 * @author IanBrown
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private static final String CHECK_FIELD = "checkfield";

	/**
	 * an invalid equal hook.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String INVALID_EQUAL_HOOK = PrintIfHook.TAG_EQUAL + " invalid hook";

	/**
	 * an equal hook with too few arguments.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String TOO_FEW_FIELDS_EQUAL_HOOK = PrintIfHook.TAG_EQUAL + "(checkield,\"value\")";

	/**
	 * an equal hook with an invalid value field.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String INVALID_VALUE_EQUAL_HOOK = PrintIfHook.TAG_EQUAL + "(checkfield, \", printfield)";

	/**
	 * a valid equal hook.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String VALID_EQUAL_HOOK = PrintIfHook.TAG_EQUAL + "(checkfield, \"check value\", printfield)";

    /**
   	 * a valid equal hook with direct value.
   	 *
   	 * @author IanBrown
   	 * @since May 9, 2012
   	 * @version May 9, 2012
   	 */
   	private static final String VALID_EQUAL_HOOK_DIRECT = PrintIfHook.TAG_EQUAL + "(checkfield, \"check value\", \"" + PRINT_FIELD_VALUE + "\")";

	/**
	 * a valid not equal hook.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String VALID_NOT_EQUAL_HOOK = PrintIfHook.TAG_NOT_EQUAL + "(checkfield, \"check value\", printfield)";

	/**
	 * an invalid not empty hook.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String INVALID_NOT_EMPTY_HOOK = PrintIfHook.TAG_NOT_EMPTY + " invalid hook";

	/**
	 * a not empty hook with too few arguments.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String TOO_FEW_FIELDS_NOT_EMPTY_HOOK = PrintIfHook.TAG_NOT_EMPTY + "(field1)";

	/**
	 * a valid not empty hook.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private static final String VALID_NOT_EMPTY_HOOK = PrintIfHook.TAG_NOT_EMPTY + "(field1, field2)";

	/**
	 * the hook to test.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private PrintIfHook hook;

	/**
	 * the field manager to use.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private FieldManager fieldManager;

	/**
	 * the PDF generation context to use.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private PdfGenerationContext context;

	/**
	 * the map of raw fields.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private Map<String, Item> rawFields;

	/**
	 * Sets up the hook to test.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Before
	public final void setUpHook() {
		setRawFields(new LinkedHashMap<String, Item>());
		setFieldManager(createFieldManager(getRawFields()));
		setContext(createPdfGenerationContext(getFieldManager()));
		setHook(createHook());
	}

	/**
	 * Tears down the hook after testing.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@After
	public final void tearDownHook() {
		setHook(null);
		setContext(null);
		setFieldManager(null);
		setRawFields(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where there the value for the equals is correct.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_equal() throws PdfGeneratorException {
		initializeHook(VALID_EQUAL_HOOK);
		setUpField(CHECK_FIELD, CHECK_FIELD_VALUE);
		setUpField(PRINT_FIELD, PRINT_FIELD_VALUE);
		setUpResult(VALID_EQUAL_HOOK, PRINT_FIELD_VALUE);
		replayAll();
		getHook().onInitialize(getContext());
		getHook().onUserFieldOutput(getContext());
		getHook().onFormFieldOutput(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

    /**
   	 * Test method for
   	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
   	 * for case where there the value for the equals is correct.
   	 *
   	 * @author IanBrown
   	 *
   	 * @throws PdfGeneratorException
   	 *             if there is a problem with the hook.
   	 * @since May 9, 2012
   	 * @version May 10, 2012
   	 */
   	@Test
   	public final void testOnCompleted_equalDirect() throws PdfGeneratorException {
   		initializeHook(VALID_EQUAL_HOOK_DIRECT);
   		setUpField(CHECK_FIELD, CHECK_FIELD_VALUE);
        setUpField(CHECK_FIELD, CHECK_FIELD_VALUE);
   		setUpResult(VALID_EQUAL_HOOK_DIRECT, PRINT_FIELD_VALUE);
   		replayAll();
   		getHook().onInitialize(getContext());
   		getHook().onUserFieldOutput(getContext());
   		getHook().onFormFieldOutput(getContext());

   		getHook().onCompleted(getContext());

   		verifyAll();
   	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where there are no values for an equal hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnCompleted_equalNoValues() throws PdfGeneratorException {
		initializeHook(VALID_EQUAL_HOOK);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where there the value for the equals is not correct.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_equalWrongValue() throws PdfGeneratorException {
		initializeHook(VALID_EQUAL_HOOK);
		setUpField(CHECK_FIELD, "wrong value");
		setUpField(PRINT_FIELD, PRINT_FIELD_VALUE);
		replayAll();
		getHook().onInitialize(getContext());
		getHook().onUserFieldOutput(getContext());
		getHook().onUserFieldOutput(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where the first value for a not empty hook is matched.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_notEmpty() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EMPTY_HOOK);
		setUpField(FIELD1, FIELD1_VALUE);
		setUpField(FIELD2, FIELD2_VALUE);
		setUpResult(VALID_NOT_EMPTY_HOOK, FIELD1_VALUE);
		replayAll();
		getHook().onInitialize(getContext());
		getHook().onFormFieldOutput(getContext());
		getHook().onUserFieldOutput(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where the first value for a not empty hook is empty.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_notEmptyEmpty() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EMPTY_HOOK);
		setUpField(FIELD1, "");
		setUpField(FIELD2, FIELD2_VALUE);
		setUpResult(VALID_NOT_EMPTY_HOOK, FIELD2_VALUE);
		replayAll();
		getHook().onInitialize(getContext());
		getHook().onFormFieldOutput(getContext());
		getHook().onUserFieldOutput(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where there are no values for a not empty hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_notEmptyNoValues() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EMPTY_HOOK);
		setUpResult(VALID_NOT_EMPTY_HOOK, null);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where the first value for a not empty hook is null.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_notEmptyNull() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EMPTY_HOOK);
		setUpField(FIELD2, FIELD2_VALUE);
		setUpResult(VALID_NOT_EMPTY_HOOK, FIELD2_VALUE);
		replayAll();
		getHook().onInitialize(getContext());
		getHook().onUserFieldOutput(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where there the value for the not equals is correct.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_notEqual() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EQUAL_HOOK);
		setUpField(CHECK_FIELD, "other value");
		setUpField(PRINT_FIELD, PRINT_FIELD_VALUE);
		setUpResult(VALID_NOT_EQUAL_HOOK, PRINT_FIELD_VALUE);
		replayAll();
		getHook().onInitialize(getContext());
		getHook().onUserFieldOutput(getContext());
		getHook().onFormFieldOutput(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where there are no values for a not equal hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_notEqualNoValues() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EQUAL_HOOK);
		setUpResult(VALID_NOT_EQUAL_HOOK, null);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onCompleted(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for case where there the value for the not equal check is equal.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnCompleted_notEqualWrongValue() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EQUAL_HOOK);
		setUpField(CHECK_FIELD, CHECK_FIELD_VALUE);
		setUpField(PRINT_FIELD, PRINT_FIELD_VALUE);
		replayAll();
		getHook().onInitialize(getContext());
		getHook().onUserFieldOutput(getContext());
		getHook().onUserFieldOutput(getContext());

		getHook().onCompleted(getContext());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onFormFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field for an equal hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnFormFieldOutput_equal() throws PdfGeneratorException {
		initializeHook(VALID_EQUAL_HOOK);
		setUpField(PRINT_FIELD, PRINT_FIELD_VALUE);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onFormFieldOutput(getContext());

		assertFields(new String[] { PRINT_FIELD, PRINT_FIELD_VALUE });
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onFormFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field for a not empty hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnFormFieldOutput_notEmpty() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EMPTY_HOOK);
		setUpField(FIELD2, FIELD2_VALUE);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onFormFieldOutput(getContext());

		assertFields(new String[] { FIELD2, FIELD2_VALUE });
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onFormFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field for a not equal hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 10, 2012
	 */
	@Test
	public final void testOnFormFieldOutput_notEqual() throws PdfGeneratorException {
		initializeHook(VALID_NOT_EQUAL_HOOK);
		setUpField(CHECK_FIELD, CHECK_FIELD_VALUE);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onFormFieldOutput(getContext());

		assertFields(new String[] { CHECK_FIELD, CHECK_FIELD_VALUE });
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onFormFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field that isn't needed.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnFormFieldOutput_notNeeded() throws PdfGeneratorException {
		initializeHook(VALID_EQUAL_HOOK);
		setUpField("unused field", null);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onFormFieldOutput(getContext());

		assertFields();
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where an invalid equal hook is specified. Note that not_equal has the same rules as equal, so there is no
	 * separate check for not_equal.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_invalidEqualHook() throws PdfGeneratorException {
		checkOnInitialize(INVALID_EQUAL_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where an equal hook that has an invalid value is specified. Note that not_equal has the same rules as equal, so
	 * there is no separate check for not_equal.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_invalidValueEqualHook() throws PdfGeneratorException {
		checkOnInitialize(INVALID_VALUE_EQUAL_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where an invalid not empty hook is specified.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_invaliNotEmptyHook() throws PdfGeneratorException {
		checkOnInitialize(INVALID_NOT_EMPTY_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where no hook is specified.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_noHook() throws PdfGeneratorException {
		replayAll();

		getHook().onInitialize(context);

		assertInitialized();
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where an equal hook that has too few arguments is specified. Note that not_equal has the same rules as equal, so
	 * there is no separate check for not_equal.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_tooFewFieldsEqualHook() throws PdfGeneratorException {
		checkOnInitialize(TOO_FEW_FIELDS_EQUAL_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where a not empty hook that has too few arguments is specified.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_tooFewFieldsNotEmptyHook() throws PdfGeneratorException {
		checkOnInitialize(TOO_FEW_FIELDS_NOT_EMPTY_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where a valid equal hook is specified.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_validEqualHook() throws PdfGeneratorException {
		checkOnInitialize(VALID_EQUAL_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where a valid not empty hook is specified.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_validNotEmptyHook() throws PdfGeneratorException {
		checkOnInitialize(VALID_NOT_EMPTY_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for the case where a valid not equal hook is specified.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnInitialize_validNotEqualHook() throws PdfGeneratorException {
		checkOnInitialize(VALID_NOT_EQUAL_HOOK);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onUserFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field for an equal hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnUserFieldOutput_equal() throws PdfGeneratorException {
		final String fieldName = CHECK_FIELD;
		final String fieldValue = "check field value";
		initializeHook(VALID_EQUAL_HOOK);
		setUpField(fieldName, fieldValue);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onUserFieldOutput(getContext());

		assertFields(new String[] { fieldName, fieldValue });
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onUserFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field for a not empty hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnUserFieldOutput_notEmpty() throws PdfGeneratorException {
		final String fieldName = "field1";
		final String fieldValue = "field1 value";
		initializeHook(VALID_NOT_EMPTY_HOOK);
		setUpField(fieldName, fieldValue);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onUserFieldOutput(getContext());

		assertFields(new String[] { fieldName, fieldValue });
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onUserFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field for a not equal hook.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnUserFieldOutput_notEqual() throws PdfGeneratorException {
		final String fieldName = PRINT_FIELD;
		final String fieldValue = "print field value";
		initializeHook(VALID_NOT_EQUAL_HOOK);
		setUpField(fieldName, fieldValue);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onUserFieldOutput(getContext());

		assertFields(new String[] { fieldName, fieldValue });
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pdf.generator.hooks.PrintIfHook#onUserFieldOutput(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)}
	 * for a field that isn't needed.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem with the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	@Test
	public final void testOnUserFieldOutput_notNeeded() throws PdfGeneratorException {
		initializeHook(VALID_EQUAL_HOOK);
		setUpField("unused field", null);
		replayAll();
		getHook().onInitialize(getContext());

		getHook().onUserFieldOutput(getContext());

		assertFields();
		verifyAll();
	}

	/**
	 * @author IanBrown
	 * @param hook
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	protected void initializeHook(final String hook) {
		final Item value = createMock("Value", Item.class);
		getRawFields().put(hook, value);
	}

	/**
	 * Custom assertion to ensure that the fields are initialized.
	 * 
	 * @author IanBrown
	 * @param fields
	 *            the fields (name and value) that should be initialized.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void assertFields(final String[]... fields) {
		final Map<String, String> hookFields = getHook().getFields();
		if ((fields == null) || (fields.length == 0)) {
			assertTrue("There are no fields", hookFields.isEmpty());

		} else {
			assertEquals("There are the correct number of fields", fields.length, hookFields.size());
			for (final String[] field : fields) {
				assertEquals("The field " + field[0] + " is correct", field[1], hookFields.get(field[0]));
			}
		}
	}

	/**
	 * Custom assertion to ensure that the hook is initialized properly.
	 * 
	 * @author IanBrown
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void assertInitialized() {
		for (final String id : getRawFields().keySet()) {
			if (INVALID_EQUAL_HOOK.equals(id) || TOO_FEW_FIELDS_EQUAL_HOOK.equals(id) || INVALID_VALUE_EQUAL_HOOK.equals(id)
					|| INVALID_NOT_EMPTY_HOOK.equals(id) || TOO_FEW_FIELDS_NOT_EMPTY_HOOK.equals(id)) {
				assertFalse(id + " is not initialized", getHook().getPrintIfFields().containsKey(id));

			} else {
				assertTrue(id + " is initialized", getHook().getPrintIfFields().containsKey(id));

				if (VALID_EQUAL_HOOK.equals(id) || VALID_NOT_EQUAL_HOOK.equals(id)) {
					assertTrue("checkfield is watched", getHook().getWatchedFields().contains(CHECK_FIELD));
					assertTrue("printfield is watched", getHook().getWatchedFields().contains(PRINT_FIELD));

				} else if (VALID_NOT_EMPTY_HOOK.equals(id)) {
					assertTrue("field1 is watched", getHook().getWatchedFields().contains("field1"));
					assertTrue("field2 is watched", getHook().getWatchedFields().contains(FIELD2));

				} else {
					throw new UnsupportedOperationException(id + " not implemented yet");
				}
			}
		}
	}

	/**
	 * Checks the {@link PrintIfHook#onInitialize(com.bearcode.ovf.tools.pdf.PdfGeneratorHook.Context)} for the specified hook.
	 * 
	 * @author IanBrown
	 * @param hook
	 *            the hook text.
	 * @throws PdfGeneratorException
	 *             if there is a problem initializing the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void checkOnInitialize(final String hook) throws PdfGeneratorException {
		initializeHook(hook);
		replayAll();

		getHook().onInitialize(context);

		assertInitialized();
		verifyAll();
	}

	/**
	 * Creates a field manager.
	 * 
	 * @author IanBrown
	 * @param rawFields
	 *            the raw fields map.
	 * @return the field manager.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private FieldManager createFieldManager(final Map<String, Item> rawFields) {
		final FieldManager fieldManager = createMock("FieldManager", FieldManager.class);
		EasyMock.expect(fieldManager.getRawFields()).andReturn(rawFields).anyTimes();
		return fieldManager;
	}

	/**
	 * Creates a print if hook.
	 * 
	 * @author IanBrown
	 * @return the print if hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private PrintIfHook createHook() {
		return new PrintIfHook();
	}

	/**
	 * Creates a PDF generation hook.
	 * 
	 * @author IanBrown
	 * @param fieldManager
	 *            the field manager.
	 * @return the PDF generation hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private PdfGenerationContext createPdfGenerationContext(final FieldManager fieldManager) {
		final PdfGenerationContext context = createMock("Context", PdfGenerationContext.class);
		EasyMock.expect(context.getFields()).andReturn(fieldManager).anyTimes();
		return context;
	}

	/**
	 * Gets the context.
	 * 
	 * @author IanBrown
	 * @return the context.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private PdfGenerationContext getContext() {
		return context;
	}

	/**
	 * Gets the field manager.
	 * 
	 * @author IanBrown
	 * @return the field manager.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private FieldManager getFieldManager() {
		return fieldManager;
	}

	/**
	 * Gets the hook.
	 * 
	 * @author IanBrown
	 * @return the hook.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private PrintIfHook getHook() {
		return hook;
	}

	/**
	 * Gets the raw fields map.
	 * 
	 * @author IanBrown
	 * @return the raw fields map.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private Map<String, Item> getRawFields() {
		return rawFields;
	}

	/**
	 * Sets the context.
	 * 
	 * @author IanBrown
	 * @param context
	 *            the context to set.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void setContext(final PdfGenerationContext context) {
		this.context = context;
	}

	/**
	 * Sets the field manager.
	 * 
	 * @author IanBrown
	 * @param fieldManager
	 *            the field manager to set.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void setFieldManager(final FieldManager fieldManager) {
		this.fieldManager = fieldManager;
	}

	/**
	 * Sets the hook.
	 * 
	 * @author IanBrown
	 * @param hook
	 *            the hook to set.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void setHook(final PrintIfHook hook) {
		this.hook = hook;
	}

	/**
	 * Sets the raw fields map.
	 * 
	 * @author IanBrown
	 * @param rawFields
	 *            the raw fields map to set.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void setRawFields(final Map<String, Item> rawFields) {
		this.rawFields = rawFields;
	}

	/**
	 * Sets up a field to be processed by the hook.
	 * 
	 * @author IanBrown
	 * @param fieldName
	 *            the name of the field.
	 * @param fieldValue
	 *            the value of the field - a value of <code>null</code> is not set.
	 * @since May 9, 2012
	 * @version May 9, 2012
	 */
	private void setUpField(final String fieldName, final String fieldValue) {
		EasyMock.expect(getContext().getId()).andReturn(fieldName);
		if (fieldValue != null) {
			EasyMock.expect(getContext().getValue()).andReturn(fieldValue);
		}
	}

	/**
	 * Sets up an expected result.
	 * 
	 * @author IanBrown
	 * @param hook
	 *            the hook to set up.
	 * @param value
	 *            the value.
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the result.
	 * @since May 10, 2012
	 * @version May 10, 2012
	 */
	private void setUpResult(final String hook, final String value) throws PdfGeneratorException {
		getFieldManager().setField(hook, value);
	}
}
