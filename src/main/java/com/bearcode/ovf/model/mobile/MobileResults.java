/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import java.util.List;

/**
 * Represents the results of the wizard questionnaire provided by a mobile user.
 * 
 * @author IanBrown
 * 
 * @since Apr 11, 2012
 * @version Apr 20, 2012
 */
public class MobileResults {

	/**
	 * the user's email address.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String emailAddress;

	/**
	 * the name of the person.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private MobilePerson name;

	/**
	 * the previous name of the person.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private MobilePerson previousName;

	/**
	 * the month that the person was born (1 is January, 12 is December).
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private int birthMonth;

	/**
	 * the day of the month that the person was born (1 - 28, 29, 30, or 31 as appropriate).
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private int birthDay;

	/**
	 * the year that the person was born (full four digit year).
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private int birthYear;

	/**
	 * the alternate email address of the person.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String alternateEmail;

	/**
	 * the phone number of the person.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String phone;

	/**
	 * the alternate phone number of the person.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String alternatePhone;

	/**
	 * the person's current address.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private MobileAddress currentAddress;

	/**
	 * the person's voting address.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private MobileAddress votingAddress;

	/**
	 * the person's forwarding address.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private MobileAddress forwardingAddress;

	/**
	 * the person's previous address.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private MobileAddress previousAddress;

	/**
	 * the name of the voting region.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String votingRegionName;

	/**
	 * the voting region state.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private String votingRegionState;

	/**
	 * the type of voter.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String voterType;

	/**
	 * the voter's history.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String voterHistory;

	/**
	 * the voter's ballot preference.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String ballotPreference;

	/**
	 * the person's race.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String race;

	/**
	 * the person's ethnicity.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String ethnicity;

	/**
	 * the person's gender.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String gender;

	/**
	 * the person's political party.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private String party;

	/**
	 * did the user come in through the mobile application?
	 * 
	 * @author IanBrown
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	private boolean mobile;

	/**
	 * the type of mobile device.
	 * 
	 * @author IanBrown
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	private String mobileDeviceType;

	/**
	 * the answers provided by the user.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private List<MobileAnswer> answers;

	/**
	 * was the PDF downloaded?
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private boolean downloaded;

	/**
	 * Gets the alternate eMail address of the person.
	 * 
	 * @author IanBrown
	 * @return the alternate eMail address.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getAlternateEmail() {
		return alternateEmail;
	}

	/**
	 * Gets the alternate phone number of the person.
	 * 
	 * @author IanBrown
	 * @return the alternate phone.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getAlternatePhone() {
		return alternatePhone;
	}

	/**
	 * Gets the answers provided by the user.
	 * 
	 * @author IanBrown
	 * @return the answers.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public List<MobileAnswer> getAnswers() {
		return answers;
	}

	/**
	 * Gets the ballot preference.
	 * 
	 * @author IanBrown
	 * @return the ballot preference.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getBallotPreference() {
		return ballotPreference;
	}

	/**
	 * Gets the birth day.
	 * 
	 * @author IanBrown
	 * @return the birth day.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public int getBirthDay() {
		return birthDay;
	}

	/**
	 * Gets the birth month.
	 * 
	 * @author IanBrown
	 * @return the birth month.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public int getBirthMonth() {
		return birthMonth;
	}

	/**
	 * Gets the birth year.
	 * 
	 * @author IanBrown
	 * @return the birth year.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public int getBirthYear() {
		return birthYear;
	}

	/**
	 * Gets the current address.
	 * 
	 * @author IanBrown
	 * @return the current address.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public MobileAddress getCurrentAddress() {
		return currentAddress;
	}

	/**
	 * Gets the eMail address.
	 * 
	 * @author IanBrown
	 * @return the eMail address.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Gets the person's ethnicity.
	 * 
	 * @author IanBrown
	 * @return the ethnicity.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getEthnicity() {
		return ethnicity;
	}

	/**
	 * Gets the forwarding address.
	 * 
	 * @author IanBrown
	 * @return the forwarding address.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public MobileAddress getForwardingAddress() {
		return forwardingAddress;
	}

	/**
	 * Gets the person's gender.
	 * 
	 * @author IanBrown
	 * @return the gender.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Gets the mobile device type.
	 * 
	 * @author IanBrown
	 * @return the mobile device type.
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	public String getMobileDeviceType() {
		return mobileDeviceType;
	}

	/**
	 * Gets the name of the person.
	 * 
	 * @author IanBrown
	 * @return the name.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public MobilePerson getName() {
		return name;
	}

	/**
	 * Gets the person's politcal party.
	 * 
	 * @author IanBrown
	 * @return the party.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getParty() {
		return party;
	}

	/**
	 * Gets the phone number of the person.
	 * 
	 * @author IanBrown
	 * @return the phone.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Gets the previous address.
	 * 
	 * @author IanBrown
	 * @return the previous address.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public MobileAddress getPreviousAddress() {
		return previousAddress;
	}

	/**
	 * Gets the previous name of the person.
	 * 
	 * @author IanBrown
	 * @return the previous name.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public MobilePerson getPreviousName() {
		return previousName;
	}

	/**
	 * Gets the race of the person.
	 * 
	 * @author IanBrown
	 * @return the race.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getRace() {
		return race;
	}

	/**
	 * Gets the voter history.
	 * 
	 * @author IanBrown
	 * @return the voter history.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getVoterHistory() {
		return voterHistory;
	}

	/**
	 * Gets the voter type.
	 * 
	 * @author IanBrown
	 * @return the voter type.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getVoterType() {
		return voterType;
	}

	/**
	 * Gets the voting address.
	 * 
	 * @author IanBrown
	 * @return the voting address.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public MobileAddress getVotingAddress() {
		return votingAddress;
	}

	/**
	 * Gets the voting region name.
	 * 
	 * @author IanBrown
	 * @return the voting region name.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public String getVotingRegionName() {
		return votingRegionName;
	}

	/**
	 * Gets the voting region state.
	 * 
	 * @author IanBrown
	 * @return the voting region state.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public String getVotingRegionState() {
		return votingRegionState;
	}

	/**
	 * Was the PDF downloaded?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if the PDF was downloaded, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public boolean isDownloaded() {
		return downloaded;
	}

	/**
	 * Gets the mobile flag.
	 * 
	 * @author IanBrown
	 * @return the mobile flag.
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	public boolean isMobile() {
		return mobile;
	}

	/**
	 * Sets the alternate eMail address of the person.
	 * 
	 * @author IanBrown
	 * @param alternateEmail
	 *            the alternate eMail address to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setAlternateEmail(final String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	/**
	 * Sets the alternate phone number of the person.
	 * 
	 * @author IanBrown
	 * @param alternatePhone
	 *            the alternate phone to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setAlternatePhone(final String alternatePhone) {
		this.alternatePhone = alternatePhone;
	}

	/**
	 * Sets the answers provided by the user.
	 * 
	 * @author IanBrown
	 * @param answers
	 *            the answers.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setAnswers(final List<MobileAnswer> answers) {
		this.answers = answers;
	}

	/**
	 * Sets the ballot preference.
	 * 
	 * @author IanBrown
	 * @param ballotPreference
	 *            the ballot preference to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setBallotPreference(final String ballotPreference) {
		this.ballotPreference = ballotPreference;
	}

	/**
	 * Sets the birth day.
	 * 
	 * @author IanBrown
	 * @param birthDay
	 *            the birth day to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setBirthDay(final int birthDay) {
		this.birthDay = birthDay;
	}

	/**
	 * Sets the birth month.
	 * 
	 * @author IanBrown
	 * @param birthMonth
	 *            the birth month to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setBirthMonth(final int birthMonth) {
		this.birthMonth = birthMonth;
	}

	/**
	 * Sets the birth year.
	 * 
	 * @author IanBrown
	 * @param birthYear
	 *            the birthYear to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setBirthYear(final int birthYear) {
		this.birthYear = birthYear;
	}

	/**
	 * Sets the current address.
	 * 
	 * @author IanBrown
	 * @param currentAddress
	 *            the current address to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setCurrentAddress(final MobileAddress currentAddress) {
		this.currentAddress = currentAddress;
	}

	/**
	 * Sets the downloaded flag.
	 * 
	 * @author IanBrown
	 * @param downloaded
	 *            <code>true</code> if the PDF was downloaded, <code>false</code> otherwise.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setDownloaded(final boolean downloaded) {
		this.downloaded = downloaded;
	}

	/**
	 * Sets the eMail address.
	 * 
	 * @author IanBrown
	 * @param emailAddress
	 *            the eMail address to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Sets the person's ethnicity.
	 * 
	 * @author IanBrown
	 * @param ethnicity
	 *            the ethnicity.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setEthnicity(final String ethnicity) {
		this.ethnicity = ethnicity;
	}

	/**
	 * Sets the forwardingaddress.
	 * 
	 * @author IanBrown
	 * @param forwardingAddress
	 *            the forwarding address to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setForwardingAddress(final MobileAddress forwardingAddress) {
		this.forwardingAddress = forwardingAddress;
	}

	/**
	 * Sets the person's gender.
	 * 
	 * @author IanBrown
	 * @param gender
	 *            the gender.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setGender(final String gender) {
		this.gender = gender;
	}

	/**
	 * Sets the mobile flag.
	 * 
	 * @author IanBrown
	 * @param mobile
	 *            the mobile flag to set.
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	public void setMobile(final boolean mobile) {
		this.mobile = mobile;
	}

	/**
	 * Sets the mobile device type.
	 * 
	 * @author IanBrown
	 * @param mobileDeviceType
	 *            the mobile device type to set.
	 * @since Apr 20, 2012
	 * @version Apr 20, 2012
	 */
	public void setMobileDeviceType(final String mobileDeviceType) {
		this.mobileDeviceType = mobileDeviceType;
	}

	/**
	 * Sets the name of the person.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setName(final MobilePerson name) {
		this.name = name;
	}

	/**
	 * Sets the person's political party.
	 * 
	 * @author IanBrown
	 * @param party
	 *            the party.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setParty(final String party) {
		this.party = party;
	}

	/**
	 * Sets the phone number of the person.
	 * 
	 * @author IanBrown
	 * @param phone
	 *            the phone to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * Sets the previous address.
	 * 
	 * @author IanBrown
	 * @param previousAddress
	 *            the previous address to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setPreviousAddress(final MobileAddress previousAddress) {
		this.previousAddress = previousAddress;
	}

	/**
	 * Sets the previous name of the person.
	 * 
	 * @author IanBrown
	 * @param previousName
	 *            the previous name to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setPreviousName(final MobilePerson previousName) {
		this.previousName = previousName;
	}

	/**
	 * Sets the race of the person.
	 * 
	 * @author IanBrown
	 * @param race
	 *            the race.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setRace(final String race) {
		this.race = race;
	}

	/**
	 * Sets the voter history.
	 * 
	 * @author IanBrown
	 * @param voterHistory
	 *            the voter history to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setVoterHistory(final String voterHistory) {
		this.voterHistory = voterHistory;
	}

	/**
	 * Sets the voter type.
	 * 
	 * @author IanBrown
	 * @param voterType
	 *            the voter type to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setVoterType(final String voterType) {
		this.voterType = voterType;
	}

	/**
	 * Sets the voting address.
	 * 
	 * @author IanBrown
	 * @param votingAddress
	 *            the voting address to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setVotingAddress(final MobileAddress votingAddress) {
		this.votingAddress = votingAddress;
	}

	/**
	 * Sets the voting region name.
	 * 
	 * @author IanBrown
	 * @param votingRegionName
	 *            the voting region name to set.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	public void setVotingRegionName(final String votingRegionName) {
		this.votingRegionName = votingRegionName;
	}

	/**
	 * Sets the voting region state.
	 * 
	 * @author IanBrown
	 * @param votingRegionState
	 *            the voting region state to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setVotingRegionState(final String votingRegionState) {
		this.votingRegionState = votingRegionState;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Results");
		sb.append(" for ").append(getEmailAddress());
		appendPerson(getName(), " ", sb);
		appendPerson(getPreviousName(), " was ", sb);
		sb.append(" ").append(getBirthMonth()).append("/").append(getBirthDay()).append("/").append(getBirthYear());
		appendString(getAlternateEmail(), " Alt ", sb);
		appendString(getPhone(), " #", sb);
		appendString(getAlternatePhone(), " Alt#", sb);
		appendAddress(getCurrentAddress(), " ", sb);
		appendAddress(getVotingAddress(), " voting ", sb);
		appendAddress(getForwardingAddress(), " forward ", sb);
		appendAddress(getPreviousAddress(), " previous ", sb);
		appendString(getVotingRegionName(), " ", sb);
		appendString(getVotingRegionState(), " ", sb);
		appendString(getVoterType(), " type ", sb);
		appendString(getVoterHistory(), " history ", sb);
		appendString(getBallotPreference(), " preference ", sb);
		appendString(getEthnicity(), " ", sb);
		appendString(getRace(), " ", sb);
		appendString(getGender(), " ", sb);
		appendString(getParty(), " ", sb);
		if (isDownloaded()) {
			sb.append(" (downloaded)");
		}
		if ((getAnswers() != null) && !getAnswers().isEmpty()) {
			for (final MobileAnswer answer : getAnswers()) {
				sb.append(answer.buildString("\n  "));
			}
		}
		return sb.toString();
	}

	/**
	 * Appends the address (if any) to the string builder using the indentation prefix.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address.
	 * @param prefix
	 *            the indentation prefix.
	 * @param sb
	 *            the string builder.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void appendAddress(final MobileAddress address, final String prefix, final StringBuilder sb) {
		if (address != null) {
			sb.append(address.buildString(prefix));
		}
	}

	/**
	 * Appends the person (if any) to the string builder using the indentation prefix.
	 * 
	 * @author IanBrown
	 * @param person
	 *            the person (may be <code>null</code>).
	 * @param prefix
	 *            the identation prefix.
	 * @param sb
	 *            the string builder.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void appendPerson(final MobilePerson person, final String prefix, final StringBuilder sb) {
		if (person != null) {
			sb.append(person.buildString(prefix));
		}
	}

	/**
	 * Appends the string (if any) to the string builder using the indentation prefix.
	 * 
	 * @author IanBrown
	 * @param string
	 *            the string (may be <code>null</code>).
	 * @param prefix
	 *            the indentation prefix.
	 * @param sb
	 *            the string builder.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void appendString(final String string, final String prefix, final StringBuilder sb) {
		if (string != null) {
			sb.append(prefix).append(string);
		}
	}
}
