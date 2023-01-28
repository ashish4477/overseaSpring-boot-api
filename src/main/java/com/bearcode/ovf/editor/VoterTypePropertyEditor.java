/**
 * 
 */
package com.bearcode.ovf.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.stereotype.Component;

import com.bearcode.ovf.model.common.VoterType;

/**
 * Extended {@link PropertyEditorSupport} property editor for {@link VoterType}.
 * 
 * @author IanBrown
 * 
 * @since Aug 23, 2012
 * @version Aug 23, 2012
 */
@Component
public class VoterTypePropertyEditor extends PropertyEditorSupport {

	/** {@inheritDoc} */
	@Override
	public String getAsText() {
		return super.getAsText();
	}

	/** {@inheritDoc} */
	@Override
	public void setAsText(final String text) {
		if (text == null || text.trim().isEmpty()) {
			setValue("");
			return;
		}

		final VoterType voterType = VoterType.valueOf(text);
		setValue(voterType == null ? "" : voterType);
	}
}
