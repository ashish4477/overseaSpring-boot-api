/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.UserAddress;

/**
 * Extended {@link UserAddress} for use by mobile devices.
 * 
 * @author IanBrown
 * 
 * @since Apr 11, 2012
 * @version Apr 16, 2012
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileAddress extends UserAddress {

	/**
	 * the serial version UID for the class.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private static final long serialVersionUID = -395368262986246004L;

	/**
	 * Constructs a default mobile address.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public MobileAddress() {

	}

	/**
	 * Constructs a mobile address of the specified type.
	 * 
	 * @author IanBrown
	 * @param type
	 *            the type.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public MobileAddress(final AddressType type) {
		super(type);
	}

	/**
	 * Returns a string representation of the address preceeded by the indentation prefix.
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
		switch (getType()) {
		case STREET:
		case OVERSEAS:
			sb.append(getFullStreet()).append(", ").append(getCity())
					.append(!getState().isEmpty() || !getZip().isEmpty() ? ", " : "").append(getState()).append(" ")
					.append(getZip()).append((getZip4() != null) && !getZip4().isEmpty() ? "-" : "").append(getZip4()).append(" ")
					.append(getCountry());
			break;
		case RURAL_ROUTE:
		case DESCRIBED:
			sb.append(getFullStreet()).append(" ").append(getDescription()).append(", ").append(getCity())
					.append(!getState().isEmpty() || !getZip().isEmpty() ? ", " : "").append(getState()).append(" ")
					.append(getZip()).append(!getZip4().isEmpty() ? "-" : "").append(getZip4()).append(" ").append(getCountry());
			break;
		case MILITARY:
			sb.append(getFullStreet()).append(", ").append(getCity()).append(" ").append(getState()).append(" ").append(getZip());
			break;
		}
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return buildString("");
	}
}