/**
 * 
 */
package com.bearcode.ovf.model.pendingregistration;


/**
 * Extended {@link PendingVoterIdentified} to hold an address.
 * 
 * @author IanBrown
 * 
 * @since Nov 2, 2012
 * @version Nov 7, 2012
 */
public class PendingVoterAddress extends PendingVoterIdentified {

	/**
	 * the city.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String city;

	/**
	 * the country.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String country;

	/**
	 * the description of the address.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String description;

	/**
	 * the encrypted data.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	private byte[] encrypted;

	/**
	 * the postal code.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String postalCode;

	/**
	 * the state or region.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String stateOrRegion;

	/**
	 * the first line for the street address.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String street1;

	/**
	 * the second line for the street address.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String street2;

	/**
	 * Default constructor.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public PendingVoterAddress() {

	}

	/**
	 * Gets the city.
	 * 
	 * @author IanBrown
	 * @return the city.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Gets the country.
	 * 
	 * @author IanBrown
	 * @return the country.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Gets the description.
	 * 
	 * @author IanBrown
	 * @return the description.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the encrypted.
	 * 
	 * @author IanBrown
	 * @return the encrypted.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public byte[] getEncrypted() {
		return encrypted;
	}

	/**
	 * Gets the postal code.
	 * 
	 * @author IanBrown
	 * @return the postal code.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Gets the state or region.
	 * 
	 * @author IanBrown
	 * @return the state or region.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getStateOrRegion() {
		return stateOrRegion;
	}

	/**
	 * Gets the street 1.
	 * 
	 * @author IanBrown
	 * @return the street 1.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getStreet1() {
		return street1;
	}

	/**
	 * Gets the street 2.
	 * 
	 * @author IanBrown
	 * @return the street 2.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getStreet2() {
		return street2;
	}

	/**
	 * Sets the city.
	 * 
	 * @author IanBrown
	 * @param city
	 *            the city to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * Sets the country.
	 * 
	 * @author IanBrown
	 * @param country
	 *            the country to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setCountry(final String country) {
		this.country = country;
	}

	/**
	 * Sets the description.
	 * 
	 * @author IanBrown
	 * @param description
	 *            the description to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets the encrypted.
	 * 
	 * @author IanBrown
	 * @param encrypted
	 *            the encrypted to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public void setEncrypted(final byte[] encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * Sets the postal code.
	 * 
	 * @author IanBrown
	 * @param postalCode
	 *            the postal code to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * Sets the state or region.
	 * 
	 * @author IanBrown
	 * @param stateOrRegion
	 *            the state or region to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setStateOrRegion(final String stateOrRegion) {
		this.stateOrRegion = stateOrRegion;
	}

	/**
	 * Sets the street 1.
	 * 
	 * @author IanBrown
	 * @param street1
	 *            the street 1 to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setStreet1(final String street1) {
		this.street1 = street1;
	}

	/**
	 * Sets the street 2.
	 * 
	 * @author IanBrown
	 * @param street2
	 *            the street 2 to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setStreet2(final String street2) {
		this.street2 = street2;
	}
}
