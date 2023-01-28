/**
 * 
 */
package com.bearcode.ovf.model.vip;

/**
 * Extended {@link AbstractId} representing a detail address.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Aug 10, 2012
 */
public class VipDetailAddress extends AbstractId {

	/**
	 * the direction of the addresses.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String addressDirection;

	/**
	 * the name of the city (or town).
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String city;

	/**
	 * the house number prefix.
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private String houseNumberPrefix;

	/**
	 * the house number suffix.
	 * 
	 * @author IanBrown
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private String houseNumberSuffix;

	/**
	 * the state of the address.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String state;

	/**
	 * the direction of the street.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String streetDirection;

	/**
	 * the name of the street.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String streetName;

	/**
	 * the suffix for the street.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String streetSuffix;

	/**
	 * the ZIP code of the address.
	 * 
	 * @author IanBrown
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	private String zip;

	/**
	 * Gets the address direction.
	 * 
	 * @author IanBrown
	 * @return the address direction.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getAddressDirection() {
		return addressDirection;
	}

	/**
	 * Gets the city.
	 * 
	 * @author IanBrown
	 * @return the city.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Gets the house number prefix.
	 * 
	 * @author IanBrown
	 * @return the house number prefix.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public String getHouseNumberPrefix() {
		return houseNumberPrefix;
	}

	/**
	 * Gets the house number suffix.
	 * 
	 * @author IanBrown
	 * @return the house number suffix.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public String getHouseNumberSuffix() {
		return houseNumberSuffix;
	}

	/**
	 * Gets the state.
	 * 
	 * @author IanBrown
	 * @return the state.
	 * @since Jun 22, 2012
	 * @version Jun 27, 2012
	 */
	public String getState() {
		return state;
	}

	/**
	 * Gets the street direction.
	 * 
	 * @author IanBrown
	 * @return the street direction.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getStreetDirection() {
		return streetDirection;
	}

	/**
	 * Gets the street name.
	 * 
	 * @author IanBrown
	 * @return the street name.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getStreetName() {
		return streetName;
	}

	/**
	 * Gets the street suffix.
	 * 
	 * @author IanBrown
	 * @return the street suffix.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getStreetSuffix() {
		return streetSuffix;
	}

	/**
	 * Gets the zip.
	 * 
	 * @author IanBrown
	 * @return the zip.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * Sets the address direction.
	 * 
	 * @author IanBrown
	 * @param addressDirection
	 *            the address direction to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setAddressDirection(final String addressDirection) {
		this.addressDirection = addressDirection;
	}

	/**
	 * Sets the city.
	 * 
	 * @author IanBrown
	 * @param city
	 *            the city to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * Sets the house number prefix.
	 * 
	 * @author IanBrown
	 * @param houseNumberPrefix
	 *            the house number prefix to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setHouseNumberPrefix(final String houseNumberPrefix) {
		this.houseNumberPrefix = houseNumberPrefix;
	}

	/**
	 * Sets the house number suffix.
	 * 
	 * @author IanBrown
	 * @param houseNumberSuffix
	 *            the house number suffix to set.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	public void setHouseNumberSuffix(final String houseNumberSuffix) {
		this.houseNumberSuffix = houseNumberSuffix;
	}

	/**
	 * Sets the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setState(final String state) {
		this.state = state;
	}

	/**
	 * Sets the street direction.
	 * 
	 * @author IanBrown
	 * @param streetDirection
	 *            the street direction to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setStreetDirection(final String streetDirection) {
		this.streetDirection = streetDirection;
	}

	/**
	 * Sets the street name.
	 * 
	 * @author IanBrown
	 * @param streetName
	 *            the street name to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setStreetName(final String streetName) {
		this.streetName = streetName;
	}

	/**
	 * Sets the street suffix.
	 * 
	 * @author IanBrown
	 * @param streetSuffix
	 *            the street suffix to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setStreetSuffix(final String streetSuffix) {
		this.streetSuffix = streetSuffix;
	}

	/**
	 * Sets the zip.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the zip to set.
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	public void setZip(final String zip) {
		this.zip = zip;
	}

	/**
	 * Gets the fully specified street.
	 * 
	 * @author IanBrown
	 * @return the street.
	 * @since Jun 29, 2012
	 * @version Aug 10, 2012
	 */
	public String toStreet() {
		final StringBuilder sb = new StringBuilder();
		String prefix = "";
		if (getHouseNumberSuffix() != null && !getHouseNumberSuffix().isEmpty()) {
			sb.append("-").append(getHouseNumberSuffix());
			prefix = " ";
		}
		if (getStreetDirection() != null && !getStreetDirection().isEmpty()) {
			sb.append(getStreetDirection());
			prefix = " ";
		}
		sb.append(prefix).append(getStreetName());
		prefix = " ";
		if (getStreetSuffix() != null && !getStreetSuffix().isEmpty()) {
			sb.append(prefix).append(getStreetSuffix());
		}
		if (getAddressDirection() != null && !getAddressDirection().isEmpty()) {
			sb.append(prefix).append(getAddressDirection());
		}
		return sb.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(toStreet());
		String prefix = ",";
		sb.append(prefix).append(getCity());
		sb.append(prefix).append(getState());
		prefix = " ";
		sb.append(prefix).append(getZip());
		return sb.toString();
	}
}
