/**
 * 
 */
package com.bearcode.ovf.model.vip;

import org.apache.commons.lang.StringUtils;

import com.bearcode.ovf.model.common.UserAddress;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Extended {@link AbstractId} representing the biographical information for a candidate, beyond the basics needed to choose the
 * candidate.
 * 
 * @author IanBrown
 * 
 * @since Aug 15, 2012
 * @version Oct 24, 2012
 */
public class VipCandidateBio extends AbstractId {

    private static final String[] SCHEMES = {"http","https"};
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(SCHEMES);
	/**
	 * the biography for the candidate.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String biography;

	/**
	 * the candidate described by this bio.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private VipCandidate candidate;

	/**
	 * the URL for the candidate.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String candidateUrl;

	/**
	 * the candidate's email address.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String email;

	/**
	 * the address used by the candidate to file for office.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private UserAddress filedMailingAddress;

	/**
	 * the phone number for the candidate.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String phone;

	/**
	 * the URL for the photograph of the candidate.
	 * 
	 * @author IanBrown
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private String photoUrl;

	/**
	 * Gets the biography.
	 * 
	 * @author IanBrown
	 * @return the biography.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getBiography() {
		return biography;
	}

	/**
	 * Gets the candidate.
	 * 
	 * @author IanBrown
	 * @return the candidate.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public VipCandidate getCandidate() {
		return candidate;
	}

	/**
	 * Gets the candidate URL.
	 * 
	 * @author IanBrown
	 * @return the candidate URL.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getCandidateUrl() {
		return candidateUrl;
	}

	/**
	 * Gets the email.
	 * 
	 * @author IanBrown
	 * @return the email.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the filed mailing address.
	 * 
	 * @author IanBrown
	 * @return the filed mailing address.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public UserAddress getFiledMailingAddress() {
		return filedMailingAddress;
	}

	/**
	 * Gets the phone.
	 * 
	 * @author IanBrown
	 * @return the phone.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Gets the photo URL.
	 * 
	 * @author IanBrown
	 * @return the photo URL.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}

	/**
	 * Is the candidate's biography empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the biography is empty, <code>false</code> if it is not.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isBiographyEmpty() {
		return StringUtils.isEmpty(getBiography());
	}

	/**
	 * Is the candidate URL empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the candidate URl is empty, <code>false</code> if it is not.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isCandidateUrlEmpty() {
		return StringUtils.isEmpty(getCandidateUrl()) || !URL_VALIDATOR.isValid(getCandidateUrl());
	}

	/**
	 * Is the contact information for the candidate empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the contact information is empty, <code>false</code> otherwise.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isContactInformationEmpty() {
		return isCandidateUrlEmpty() && isEmailEmpty() && isFiledMailingAddressEmpty() && isPhoneEmpty() && isPhotoUrlEmpty();
	}

	/**
	 * Is the candidate's email empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the email is empty, <code>false</code> if it is not.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isEmailEmpty() {
		return StringUtils.isEmpty(getEmail());
	}

	/**
	 * Are all of the fields of this bio empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if all of the fields are empty, <code>false</code> otherwise.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isEmpty() {
		return isBiographyEmpty() && isContactInformationEmpty();
	}

	/**
	 * Is the filed mailing address empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the filed mailing address is empty (null or all fields are empty), <code>false</code> otherwise.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isFiledMailingAddressEmpty() {
		return getFiledMailingAddress() == null || getFiledMailingAddress().isEmptySpace();
	}

	/**
	 * Is the candidate's phone number empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the phone is empty, <code>false</code> if it is not.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isPhoneEmpty() {
		return StringUtils.isEmpty(getPhone()) || "0000000".equals(getPhone());
	}

	/**
	 * Is the photo URL for the candidate empty?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the photo URL is empty, <code>false</code> if it is not.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public boolean isPhotoUrlEmpty() {
		return StringUtils.isEmpty(getPhotoUrl()) || !URL_VALIDATOR.isValid(getPhotoUrl());
	}

	/**
	 * Sets the biography.
	 * 
	 * @author IanBrown
	 * @param biography
	 *            the biography to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setBiography(final String biography) {
		this.biography = biography;
	}

	/**
	 * Sets the candidate.
	 * 
	 * @author IanBrown
	 * @param candidate
	 *            the candidate to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setCandidate(final VipCandidate candidate) {
		this.candidate = candidate;
	}

	/**
	 * Sets the candidate URL.
	 * 
	 * @author IanBrown
	 * @param candidateUrl
	 *            the candidate URL to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setCandidateUrl(final String candidateUrl) {
        if ( candidateUrl != null ) {
            String theUrl = candidateUrl.toLowerCase();
            if ( !theUrl.matches("https?\\://.*") ) {
                theUrl = "http://" + theUrl;
            }
            this.candidateUrl = theUrl;
        } else {
            this.candidateUrl = candidateUrl;
        }
    }

	/**
	 * Sets the email.
	 * 
	 * @author IanBrown
	 * @param email
	 *            the email to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * Sets the filed mailing address.
	 * 
	 * @author IanBrown
	 * @param filedMailingAddress
	 *            the filed mailing address to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setFiledMailingAddress(final UserAddress filedMailingAddress) {
		this.filedMailingAddress = filedMailingAddress;
	}

	/**
	 * Sets the phone.
	 * 
	 * @author IanBrown
	 * @param phone
	 *            the phone to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * Sets the photo URL.
	 * 
	 * @author IanBrown
	 * @param photoUrl
	 *            the photo URL to set.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	public void setPhotoUrl(final String photoUrl) {
		this.photoUrl = photoUrl;
	}
}
