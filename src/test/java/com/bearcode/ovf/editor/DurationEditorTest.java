/**
 * 
 */
package com.bearcode.ovf.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link DurationEditor}.
 * 
 * @author IanBrown
 * 
 * @since Nov 26, 2012
 * @version Nov 26, 2012
 */
public final class DurationEditorTest extends EasyMockSupport {

	/**
	 * the duration property editor to test.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private DurationEditor durationEditor;

	/**
	 * Sets up to test the duration editor.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Before
	public final void setUpDurationEditor() {
		setDurationEditor(createDurationEditor());
	}

	/**
	 * Tears down the duration editor after testing.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@After
	public final void tearDownDurationEditor() {
		setDurationEditor(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.DurationEditor#getAsText()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testGetAsText() {
		final String actualText = getDurationEditor().getAsText();

		assertNull("There is no text", actualText);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.DurationEditor#setAsText(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testSetAsTextString() {
		final String durationAsString = "P1Y2M3DT4H5M6S";

		getDurationEditor().setAsText(durationAsString);

		final String actualDurationAsString = getDurationEditor().getAsText();
		assertEquals("The duration is set", durationAsString, actualDurationAsString);
	}

	/**
	 * Creates a duration editor.
	 * 
	 * @author IanBrown
	 * @return the duration editor.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private DurationEditor createDurationEditor() {
		return new DurationEditor();
	}

	/**
	 * Gets the duration editor.
	 * 
	 * @author IanBrown
	 * @return the duration editor.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private DurationEditor getDurationEditor() {
		return durationEditor;
	}

	/**
	 * Sets the duration editor.
	 * 
	 * @author IanBrown
	 * @param durationEditor
	 *            the duration editor to set.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void setDurationEditor(final DurationEditor durationEditor) {
		this.durationEditor = durationEditor;
	}
}
