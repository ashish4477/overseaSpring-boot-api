/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.bearcode.ovf.model.common.Person;

/**
 * Extended {@link Person} for use by mobile devices.
 * 
 * @author IanBrown
 * 
 * @since Apr 11, 2012
 * @version Apr 16, 2012
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MobilePerson extends Person {

	/**
	 * the serial version UID for the class.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private static final long serialVersionUID = 5704075618425892781L;

	/**
	 * Returns a string representation of the person indented using the specified prefix.
	 * 
	 * @author IanBrown
	 * @param prefix
	 *            the indentation prefix.
	 * @return the string representation.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public String buildString(final String prefix) {
		final StringBuilder sb = new StringBuilder(prefix);
		sb.append(getTitle()).append(" ").append(getFirstName()).append(" ").append(getInitial()).append(" ").append(getLastName())
				.append(" ").append(getSuffix());
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return buildString("");
	}
}
