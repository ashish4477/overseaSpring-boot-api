/**
 * 
 */
package com.bearcode.ovf.model.pendingregistration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Extended {@link PendingVoterIdentified} to hold pending voter registration information.
 * 
 * @author IanBrown
 * 
 * @since Nov 2, 2012
 * @version Nov 26, 2012
 */
public class PendingVoterRegistration extends PendingVoterIdentified {

	public static final SimpleDateFormat BIRTH_DAY_FORMAT = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

	/**
	 * the name for the ID field in the PDF.
	 */
	public static final String PDF_ID = "pendingId";

	/**
	 * an alternate email address for the voter.
	 */
	private String alternateEmailAddress;

	/**
	 * an alternate phone number for the voter.
	 */
	private String alternatePhoneNumber;

	/**
	 * the additional answers provided by the voter.
	 */
	private List<PendingVoterAnswer> answers;

	/**
	 * the birth day of the voter - month/day/year.
	 */
	private String birthDay;

	/**
	 * the date that the pending voter registration was created.
	 */
	private Date createdDate;

	/**
	 * the voter's current address.
	 */
	private PendingVoterAddress currentAddress;

	/**
	 * the email address of the voter.
	 */
	private String emailAddress;

	/**
	 * encrypted data.
	 */
	private byte[] encrypted;

	/**
	 * the prefix for the face (hosted site) used to enter the registration.
	 */
	private String facePrefix;

	/**
	 * the voter's forwarding address.
	 */
	private PendingVoterAddress forwardingAddress;

	/**
	 * the voter's gender.
	 */
	private String gender;

	/**
	 * the voter's name.
	 */
	private PendingVoterName name;

	/**
	 * the voter's phone number.
	 */
	private String phoneNumber;

	/**
	 * the voter's previous address.
	 */
	private PendingVoterAddress previousAddress;

	/**
	 * the voter's previous name.
	 */
	private PendingVoterName previousName;

	/**
	 * the voter's voting history.
	 */
	private String voterHistory;

	/**
	 * the type of voter.
	 */
	private String voterType;

	/**
	 * the type of voter classification.
	 */
	private String voterClassificationType;

	/**
	 * the voter's official address for casting votes.
	 */
	private PendingVoterAddress votingAddress;

	private String votingRegion;

	/**
	 * the name of the voting state for the user.
	 */
	private String votingState;
	private String phoneNumberType;
	private String alternatePhoneNumberType;

	public PendingVoterRegistration() {
	}

	public String getAlternateEmailAddress() {
		return alternateEmailAddress;
	}

	public String getAlternatePhoneNumber() {
		return alternatePhoneNumber;
	}

	public List<PendingVoterAnswer> getAnswers() {
		return answers;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public PendingVoterAddress getCurrentAddress() {
		return currentAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public byte[] getEncrypted() {
		return encrypted;
	}

	public String getFacePrefix() {
		return facePrefix;
	}

	public PendingVoterAddress getForwardingAddress() {
		return forwardingAddress;
	}

	public String getGender() {
		return gender;
	}

	public PendingVoterName getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public PendingVoterAddress getPreviousAddress() {
		return previousAddress;
	}

	public PendingVoterName getPreviousName() {
		return previousName;
	}

	public String getVoterHistory() {
		return voterHistory;
	}

	public String getVoterType() {
		return voterType;
	}

	public String getVoterClassificationType() {
		return voterClassificationType;
	}

	public PendingVoterAddress getVotingAddress() {
		return votingAddress;
	}

	public String getVotingRegion() {
		return votingRegion;
	}

	public String getVotingState() {
		return votingState;
	}

	public void setAlternateEmailAddress(final String alternateEmailAddress) {
		this.alternateEmailAddress = alternateEmailAddress;
	}

	public void setAlternatePhoneNumber(final String alternatePhoneNumber) {
		this.alternatePhoneNumber = alternatePhoneNumber;
	}

	public void setAnswers(final List<PendingVoterAnswer> answers) {
		this.answers = answers;
	}

	public void setBirthDay(final String birthDay) {
		this.birthDay = birthDay;
	}

	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setCurrentAddress(final PendingVoterAddress currentAddress) {
		this.currentAddress = currentAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setEncrypted(final byte[] encrypted) {
		this.encrypted = encrypted;
	}

	public void setFacePrefix(final String facePrefix) {
		this.facePrefix = facePrefix;
	}

	public void setForwardingAddress(final PendingVoterAddress forwardingAddress) {
		this.forwardingAddress = forwardingAddress;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public void setName(final PendingVoterName name) {
		this.name = name;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPreviousAddress(final PendingVoterAddress previousAddress) {
		this.previousAddress = previousAddress;
	}

	public void setPreviousName(final PendingVoterName previousName) {
		this.previousName = previousName;
	}

	public void setVoterHistory(final String voterHistory) {
		this.voterHistory = voterHistory;
	}

	public void setVoterType(final String voterType) {
		this.voterType = voterType;
	}

	public void setVoterClassificationType(String voterClassificationType) {
		this.voterClassificationType = voterClassificationType;
	}

	public void setVotingAddress(final PendingVoterAddress votingAddress) {
		this.votingAddress = votingAddress;
	}

	public void setVotingRegion(final String votingRegion) {
		this.votingRegion = votingRegion;
	}

	public void setVotingState(final String votingState) {
		this.votingState = votingState;
	}

	public String getPhoneNumberType() {
		return phoneNumberType;
	}

	public void setPhoneNumberType( String phoneNumberType ) {
		this.phoneNumberType = phoneNumberType;
	}

	public String getAlternatePhoneNumberType() {
		return alternatePhoneNumberType;
	}

	public void setAlternatePhoneNumberType( String alternatePhoneNumberType ) {
		this.alternatePhoneNumberType = alternatePhoneNumberType;
	}
}
