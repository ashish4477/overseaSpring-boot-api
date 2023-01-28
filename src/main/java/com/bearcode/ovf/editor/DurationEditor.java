/**
 * 
 */
package com.bearcode.ovf.editor;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * Property editor for the {@link javax.xml.types.Duration} class.
 * 
 * @author IanBrown
 * 
 * @since Nov 26, 2012
 * @version Nov 26, 2012
 */
public class DurationEditor extends PropertyEditorSupport implements PropertyEditor {

	/** {@inheritDoc} */
	@Override
	public String getAsText() {
		return getValue() == null ? null : getValue().toString();
	}

	/** {@inheritDoc} */
	@Override
	public void setAsText(final String text) {
		try {
			final DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			setValue(datatypeFactory.newDuration(text));
		} catch (final DatatypeConfigurationException e) {
			setValue(null);
		}
	}
}
