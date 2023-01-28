/**
 * 
 */
package com.bearcode.ovf.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.bearcode.ovf.model.common.VoterType;

/**
 * Test for {@link VoterTypePropertyEditor}.
 * 
 * @author IanBrown
 * 
 * @since Aug 23, 2012
 * @version Aug 23, 2012
 */
public final class VoterTypePropertyEditorTest {

	/**
	 * the voter type property editor to test.
	 * 
	 * @author IanBrown
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	private VoterTypePropertyEditor voterTypePropertyEditor;

	/**
	 * Sets up to test the voter type property editor.
	 * 
	 * @author IanBrown
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	@Before
	public final void setUpVoterTypePropertyEditor() {
		setVoterTypePropertyEditor(createVoterTypePropertyEditor());
	}

	/**
	 * Tears down the set up for testing the voter type property editor.
	 * 
	 * @author IanBrown
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	@After
	public final void tearDownVoterTypePropertyEditor() {
		setVoterTypePropertyEditor(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.VoterTypePropertyEditor#getAsText()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	@Test
	public final void testGetAsText() {
		final String actualText = getVoterTypePropertyEditor().getAsText();

		assertNull( "The text returned indicates a null", actualText );
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.VoterTypePropertyEditor#setAsText(java.lang.String)} for blank text.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	@Test
	public final void testSetAsTextString_blank() {
		getVoterTypePropertyEditor().setAsText("");

		final Object actualValue = getVoterTypePropertyEditor().getValue();
		assertEquals("The value is the empty string", "", actualValue);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.VoterTypePropertyEditor#setAsText(java.lang.String)} for null text.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	@Test
	public final void testSetAsTextString_null() {
		getVoterTypePropertyEditor().setAsText(null);

		final Object actualValue = getVoterTypePropertyEditor().getValue();
		assertEquals("The value is the empty string", "", actualValue);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.editor.VoterTypePropertyEditor#setAsText(java.lang.String)} for the name of a voter
	 * type.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	@Test
	public final void testSetAsTextString_voterType() {
		final VoterType voterType = VoterType.OVERSEAS_VOTER;

		getVoterTypePropertyEditor().setAsText(voterType.name());

		final Object actualValue = getVoterTypePropertyEditor().getValue();
		assertSame("The value is the voter type", voterType, actualValue);
	}

	/**
	 * Creates a voter type property editor.
	 * 
	 * @author IanBrown
	 * @return the voter type property editor.
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	private VoterTypePropertyEditor createVoterTypePropertyEditor() {
		return new VoterTypePropertyEditor();
	}

	/**
	 * Gets the voter type property editor.
	 * 
	 * @author IanBrown
	 * @return the voter type property editor.
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	private VoterTypePropertyEditor getVoterTypePropertyEditor() {
		return voterTypePropertyEditor;
	}

	/**
	 * Sets the voter type property editor.
	 * 
	 * @author IanBrown
	 * @param voterTypePropertyEditor
	 *            the voter type property editor to set.
	 * @since Aug 23, 2012
	 * @version Aug 23, 2012
	 */
	private void setVoterTypePropertyEditor(final VoterTypePropertyEditor voterTypePropertyEditor) {
		this.voterTypePropertyEditor = voterTypePropertyEditor;
	}

}
