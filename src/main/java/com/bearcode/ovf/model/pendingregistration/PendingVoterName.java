/**
 * 
 */
package com.bearcode.ovf.model.pendingregistration;

/**
 * Extended {@link PendingVoterIdentified} to hold a name.
 * 
 * @author IanBrown
 * 
 * @since Nov 2, 2012
 * @version Nov 15, 2012
 */
public class PendingVoterName extends PendingVoterIdentified {

	/**
	 * the encrypted data.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	private byte[] encrypted;

	/**
	 * the first name.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String firstName;

	/**
	 * the last name.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String lastName;

	/**
	 * the middle name or initial.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String middleName;

	/**
	 * the suffix.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String suffix;

	/**
	 * the title for the name.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	private String title;

	/**
	 * Default constructor.
	 * 
	 * @author IanBrown
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public PendingVoterName() {

	}

	/**
	 * Constructs a copy of the input pending voter name.
	 * 
	 * @author IanBrown
	 * @param pendingVoterName
	 *            the pending voter name.
	 * @since Nov 15, 2012
	 * @version Nov 15, 2012
	 */
	public PendingVoterName(final PendingVoterName pendingVoterName) {
		setEncrypted(pendingVoterName.getEncrypted());
		setTitle(pendingVoterName.getTitle());
		setFirstName(pendingVoterName.getFirstName());
		setMiddleName(pendingVoterName.getMiddleName());
		setLastName(pendingVoterName.getLastName());
		setSuffix(pendingVoterName.getSuffix());
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
	 * Gets the first name.
	 * 
	 * @author IanBrown
	 * @return the first name.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the last name.
	 * 
	 * @author IanBrown
	 * @return the last name.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the middle name.
	 * 
	 * @author IanBrown
	 * @return the middle name.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Gets the suffix.
	 * 
	 * @author IanBrown
	 * @return the suffix.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Gets the title.
	 * 
	 * @author IanBrown
	 * @return the title.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the encrypted.
	 * 
	 * @author IanBrown
	 * @param encrypted
	 *            the encrypted to set.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public void setEncrypted(final byte[] encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * Sets the first name.
	 * 
	 * @author IanBrown
	 * @param firstName
	 *            the first name to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @author IanBrown
	 * @param lastName
	 *            the last name to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the middle name.
	 * 
	 * @author IanBrown
	 * @param middleName
	 *            the middle name to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Sets the suffix.
	 * 
	 * @author IanBrown
	 * @param suffix
	 *            the suffix to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Sets the title.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title to set.
	 * @since Nov 2, 2012
	 * @version Nov 2, 2012
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
}
