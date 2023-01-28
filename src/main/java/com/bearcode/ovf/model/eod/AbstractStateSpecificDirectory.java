package com.bearcode.ovf.model.eod;

import java.util.Collection;
import java.util.Date;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.Person;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author Leonid Ginzburg
 * @author Ian Brown
 */
public abstract class AbstractStateSpecificDirectory {
	private long id;

	private Collection<?> elections;

	private DeliveryOptions citizenRegistration;
	private DeliveryOptions citizenBallotRequest;
	private DeliveryOptions citizenBlankBallot;
	private DeliveryOptions citizenBallotReturn;
	private DeliveryOptions militaryRegistration;
	private DeliveryOptions militaryBallotRequest;
	private DeliveryOptions militaryBlankBallot;
	private DeliveryOptions militaryBallotReturn;

	/**
	 * are witnesses or notarization required for new voters?
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private WitnessNotarizationRequirements newVoterRegistrationWitnessesOrNotarization;

	/**
	 * are witnesses or notarization required for absentee ballot affadavits?
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private WitnessNotarizationRequirements absenteeBallotAffidavitWitnessesOrNotarization;

	/**
	 * are witnesses or notarization required for early voting?
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private WitnessNotarizationRequirements earlyVotingWitnessesOrNotarization;

	/**
	 * the registration delivery options for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	private DeliveryOptions domesticRegistration;

	/**
	 * the ballot request delivery options for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	private DeliveryOptions domesticBallotRequest;

	/**
	 * the ballot return delivery options for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	private DeliveryOptions domesticBallotReturn;

	/**
	 * the early voter delivery options for domestic voters.
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	private DeliveryOptions domesticEarlyVoter;

	/**
	 * the blank ballot delivery options for domestic voters.
	 *
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	private DeliveryOptions domesticBlankBallot;

	/* State contact */
	private Person stateContact;
	private String stateEmail;
	private String statePhone;
	private String stateFax;
	private String website = "";

	private String contactNotes = "";
	private String adminNotes = "";
	private String citizenNotes = "";
	private String militaryNotes = "";

	/**
	 * the notes for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	private String domesticNotes = "";

	private Address physical;
	private Address mailing;

	private String confirmationOrStatus;

	private Date updated;

	/**
	 * the voter registration requirements for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	private String voterRegistrationRequirements = "";

	/**
	 * the voter registration identification requirements for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	private String voterRegistrationIdentificationRequirements = "";

	/**
	 * the voting in-person identification requirements for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	private String votingInPersonIdentificationRequirements = "";

	/**
	 * the early voting requirements for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	private String earlyVotingRequirements = "";

	/**
	 * the early voting identification requirements for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	private String earlyVotingIdentificationRequirements = "";

	/**
	 * the absentee ballot request identification requirements for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	private String absenteeBallotRequestIdentificationRequirements = "";

	/**
	 * the absentee ballot request requirements for domestic voters.
	 * 
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	private String absenteeBallotRequestRequirements = "";

	/**
	 * the state website for overseas services.
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private String overseasVoterServicesSite = "";

	/**
	 * the state website for military services.
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private String militaryVoterServicesSite = "";

	/**
	 * the state website to find registrations.
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private String registrationFinderSite = "";

	/**
	 * the state website for online voter registration.
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private String onlineVoterRegistrationSite = "";

	/**
	 * the state website for ballot tracking.
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private String ballotTrackingSite = "";

	/**
	 * the state website for early voting information.
	 * 
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	private String earlyVotingInformationSite = "";

	protected AbstractStateSpecificDirectory() {
		stateContact = new Person();

		citizenRegistration = new DeliveryOptions();
		citizenBallotRequest = new DeliveryOptions();
		citizenBlankBallot = new DeliveryOptions();
		citizenBallotReturn = new DeliveryOptions();
		militaryRegistration = new DeliveryOptions();
		militaryBallotRequest = new DeliveryOptions();
		militaryBlankBallot = new DeliveryOptions();
		militaryBallotReturn = new DeliveryOptions();
		domesticRegistration = new DeliveryOptions();
		domesticBallotRequest = new DeliveryOptions();
		domesticBallotReturn = new DeliveryOptions();
		domesticEarlyVoter = new DeliveryOptions();

		newVoterRegistrationWitnessesOrNotarization = new WitnessNotarizationRequirements();
		absenteeBallotAffidavitWitnessesOrNotarization = new WitnessNotarizationRequirements();
		earlyVotingWitnessesOrNotarization = new WitnessNotarizationRequirements();

		physical = new Address();
		mailing = new Address();
	}

	/**
	 * Gets the absentee ballot affidavit witnesses or notarization.
	 * 
	 * @return the absentee ballot affidavit witnesses or notarization.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public WitnessNotarizationRequirements getAbsenteeBallotAffidavitWitnessesOrNotarization() {
		return absenteeBallotAffidavitWitnessesOrNotarization;
	}

	/**
	 * Gets the absentee ballot request identification requirements.
	 * 
	 * @return the absentee ballot request identification requirements.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public String getAbsenteeBallotRequestIdentificationRequirements() {
		return absenteeBallotRequestIdentificationRequirements;
	}

	/**
	 * Gets the absentee ballot request requirements for domestic voters.
	 * 
	 * @return the absenteeBallot request requirements.
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	public String getAbsenteeBallotRequestRequirements() {
		return absenteeBallotRequestRequirements;
	}

	public String getAdminNotes() {
		return adminNotes;
	}

	/**
	 * Gets the ballot tracking site.
	 * 
	 * @return the ballot tracking site.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public String getBallotTrackingSite() {
		return ballotTrackingSite;
	}

	public DeliveryOptions getCitizenBallotRequest() {
		return citizenBallotRequest;
	}

	public DeliveryOptions getCitizenBallotReturn() {
		return citizenBallotReturn;
	}

	public DeliveryOptions getCitizenBlankBallot() {
		return citizenBlankBallot;
	}

	public String getCitizenNotes() {
		return citizenNotes;
	}

	public DeliveryOptions getCitizenRegistration() {
		return citizenRegistration;
	}

	public String getConfirmationOrStatus() {
		return confirmationOrStatus;
	}

	public String getContactNotes() {
		return contactNotes;
	}

	/**
	 * Gets the domestic ballot request.
	 * 
	 * @return the domestic ballot request.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public DeliveryOptions getDomesticBallotRequest() {
		return domesticBallotRequest;
	}

	/**
	 * Gets the domestic ballot return.
	 * 
	 * @return the domestic ballot return.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public DeliveryOptions getDomesticBallotReturn() {
		return domesticBallotReturn;
	}

	/**
	 * Gets the domestic early voter.
	 * 
	 * @author IanBrown
	 * @return the domestic early voter.
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	public DeliveryOptions getDomesticEarlyVoter() {
		return domesticEarlyVoter;
	}

	/**
	 * Gets the domestic notes.
	 * 
	 * @return the domestic notes.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public String getDomesticNotes() {
		return domesticNotes;
	}

	/**
	 * Gets the domestic registration.
	 * 
	 * @return the domestic registration.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public DeliveryOptions getDomesticRegistration() {
		return domesticRegistration;
	}

	/**
	 * Gets the early voting identification requirements.
	 * 
	 * @return the early voting identification requirements.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public String getEarlyVotingIdentificationRequirements() {
		return earlyVotingIdentificationRequirements;
	}

	/**
	 * Gets the early voting information site.
	 * 
	 * @return the early voting information site.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public String getEarlyVotingInformationSite() {
		return earlyVotingInformationSite;
	}

	/**
	 * Gets the early voting requirements for domestic voters.
	 * 
	 * @return the early voting requirements.
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	public String getEarlyVotingRequirements() {
		return earlyVotingRequirements;
	}

	/**
	 * Gets the early voting witnesses or notarization.
	 * 
	 * @return the early voting witnesses or notarization.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public WitnessNotarizationRequirements getEarlyVotingWitnessesOrNotarization() {
		return earlyVotingWitnessesOrNotarization;
	}

	public Collection<?> getElections() {
		return elections;
	}

	public long getId() {
		return id;
	}

	public Address getMailing() {
		return mailing;
	}

	public DeliveryOptions getMilitaryBallotRequest() {
		return militaryBallotRequest;
	}

	public DeliveryOptions getMilitaryBallotReturn() {
		return militaryBallotReturn;
	}

	public DeliveryOptions getMilitaryBlankBallot() {
		return militaryBlankBallot;
	}

	public String getMilitaryNotes() {
		return militaryNotes;
	}

	public DeliveryOptions getMilitaryRegistration() {
		return militaryRegistration;
	}

	/**
	 * Gets the military voter services website.
	 * 
	 * @return the military voter services website.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public String getMilitaryVoterServicesSite() {
		return militaryVoterServicesSite;
	}

	/**
	 * Gets the new voter registration witnesses or notarization.
	 * 
	 * @return the new voter registration witnesses or notarization.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public WitnessNotarizationRequirements getNewVoterRegistrationWitnessesOrNotarization() {
		return newVoterRegistrationWitnessesOrNotarization;
	}

	/**
	 * Gets the online voter registration site.
	 * 
	 * @return the online voter registration site.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public String getOnlineVoterRegistrationSite() {
		return onlineVoterRegistrationSite;
	}

	/**
	 * Gets the overseas voter services website.
	 * 
	 * @return the overseas voter services website.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public String getOverseasVoterServicesSite() {
		return overseasVoterServicesSite;
	}

	public Address getPhysical() {
		return physical;
	}

	/**
	 * Gets the registration finder site.
	 * 
	 * @return the registration finder site.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public String getRegistrationFinderSite() {
		return registrationFinderSite;
	}

	public Person getStateContact() {
		return stateContact;
	}

	public String getStateEmail() {
		return stateEmail;
	}

	public String getStateFax() {
		return stateFax;
	}

	public String getStatePhone() {
		return statePhone;
	}

	public Date getUpdated() {
		return updated;
	}

	/**
	 * Gets the voter registration identification requirements.
	 * 
	 * @return the voter registration identification requirements.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public String getVoterRegistrationIdentificationRequirements() {
		return voterRegistrationIdentificationRequirements;
	}

	/**
	 * Gets the voter registration requirements for domestic voters.
	 * 
	 * @return the voter registration requirements.
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 12, 2012
	 */
	public String getVoterRegistrationRequirements() {
		return voterRegistrationRequirements;
	}

	/**
	 * Gets the voting in-person identification requirements.
	 * 
	 * @return the voting in-person identification requirements.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public String getVotingInPersonIdentificationRequirements() {
		return votingInPersonIdentificationRequirements;
	}

	public String getWebsite() {
		return website;
	}

	/**
	 * Sets the absentee ballot affidavit witnesses or notarization.
	 * 
	 * @param absenteeBallotAffidavitWitnessesOrNotarization
	 *            the absentee ballot affidavit witnesses or notarization to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setAbsenteeBallotAffidavitWitnessesOrNotarization(
			final WitnessNotarizationRequirements absenteeBallotAffidavitWitnessesOrNotarization) {
		this.absenteeBallotAffidavitWitnessesOrNotarization = absenteeBallotAffidavitWitnessesOrNotarization;
	}

	/**
	 * Sets the absentee ballot request identification requirements.
	 * 
	 * @param absenteeBallotRequestIdentificationRequirements
	 *            the absentee ballot request identificationRequirements to set.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public void setAbsenteeBallotRequestIdentificationRequirements(final String absenteeBallotRequestIdentificationRequirements) {
		this.absenteeBallotRequestIdentificationRequirements = absenteeBallotRequestIdentificationRequirements;
	}

	/**
	 * Sets the absentee ballot request requirements for domestic voters.
	 * 
	 * @param absenteeBallotRequestRequirements
	 *            the absentee ballot request requirements to set.
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	public void setAbsenteeBallotRequestRequirements(final String absenteeBallotRequestRequirements) {
		this.absenteeBallotRequestRequirements = absenteeBallotRequestRequirements;
	}

	public void setAdminNotes(final String adminNotes) {
		this.adminNotes = adminNotes;
	}

	/**
	 * Sets the ballot tracking site.
	 * 
	 * @param ballotTrackingSite
	 *            the ballot tracking site to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setBallotTrackingSite(final String ballotTrackingSite) {
		this.ballotTrackingSite = ballotTrackingSite;
	}

	public void setCitizenBallotRequest(final DeliveryOptions citizenBallotRequest) {
		this.citizenBallotRequest = citizenBallotRequest;
	}

	public void setCitizenBallotReturn(final DeliveryOptions citizenBallotReturn) {
		this.citizenBallotReturn = citizenBallotReturn;
	}

	public void setCitizenBlankBallot(final DeliveryOptions citizenBlankBallot) {
		this.citizenBlankBallot = citizenBlankBallot;
	}

	public void setCitizenNotes(final String citizenNotes) {
		this.citizenNotes = citizenNotes;
	}

	public void setCitizenRegistration(final DeliveryOptions citizenRegistration) {
		this.citizenRegistration = citizenRegistration;
	}

	public void setConfirmationOrStatus(final String confirmationOrStatus) {
		this.confirmationOrStatus = confirmationOrStatus;
	}

	public void setContactNotes(final String contacNotes) {
		this.contactNotes = contacNotes;
	}

	/**
	 * Sets the domestic ballot request.
	 * 
	 * @param domesticBallotRequest
	 *            the domestic ballot request to set.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public void setDomesticBallotRequest(final DeliveryOptions domesticBallotRequest) {
		this.domesticBallotRequest = domesticBallotRequest;
	}

	/**
	 * Sets the domestic ballot return.
	 * 
	 * @param domesticBallotReturn
	 *            the domestic ballot return to set.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public void setDomesticBallotReturn(final DeliveryOptions domesticBallotReturn) {
		this.domesticBallotReturn = domesticBallotReturn;
	}

	/**
	 * Sets the domestic early voter.
	 * 
	 * @author IanBrown
	 * @param domesticEarlyVoter
	 *            early voter the domestic early voter to set.
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	public void setDomesticEarlyVoter(final DeliveryOptions domesticEarlyVoter) {
		this.domesticEarlyVoter = domesticEarlyVoter;
	}

	/**
	 * Sets the domestic notes.
	 * 
	 * @param domesticNotes
	 *            the domestic notes to set.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public void setDomesticNotes(final String domesticNotes) {
		this.domesticNotes = domesticNotes;
	}

	/**
	 * Sets the domestic registration.
	 * 
	 * @param domesticRegistration
	 *            the domestic registration to set.
	 * @author IanBrown
	 * @version Jan 17, 2012
	 * @since Jan 17, 2012
	 */
	public void setDomesticRegistration(final DeliveryOptions domesticRegistration) {
		this.domesticRegistration = domesticRegistration;
	}

	/**
	 * Sets the early voting identification requirements.
	 * 
	 * @param earlyVotingIdentificationRequirements
	 *            the early voting identification requirements to set.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public void setEarlyVotingIdentificationRequirements(final String earlyVotingIdentificationRequirements) {
		this.earlyVotingIdentificationRequirements = earlyVotingIdentificationRequirements;
	}

	/**
	 * Sets the early voting information site.
	 * 
	 * @param earlyVotingInformationSite
	 *            the early voting information site to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setEarlyVotingInformationSite(final String earlyVotingInformationSite) {
		this.earlyVotingInformationSite = earlyVotingInformationSite;
	}

	/**
	 * Sets the early voting requirements for domestic voters.
	 * 
	 * @param earlyVotingRequirements
	 *            the early voting requirements to set.
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 13, 2012
	 */
	public void setEarlyVotingRequirements(final String earlyVotingRequirements) {
		this.earlyVotingRequirements = earlyVotingRequirements;
	}

	/**
	 * Sets the early voting witnesses or notarization.
	 * 
	 * @param earlyVotingWitnessesOrNotarization
	 *            the early voting witnesses or notarization to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setEarlyVotingWitnessesOrNotarization(final WitnessNotarizationRequirements earlyVotingWitnessesOrNotarization) {
		this.earlyVotingWitnessesOrNotarization = earlyVotingWitnessesOrNotarization;
	}

	public void setElections(final Collection<?> elections) {
		this.elections = elections;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setMailing(final Address mailing) {
		if (mailing != null) {
			this.mailing = mailing;
		}
	}

	public void setMilitaryBallotRequest(final DeliveryOptions militaryBallotRequest) {
		this.militaryBallotRequest = militaryBallotRequest;
	}

	public void setMilitaryBallotReturn(final DeliveryOptions militaryBallotReturn) {
		this.militaryBallotReturn = militaryBallotReturn;
	}

	public void setMilitaryBlankBallot(final DeliveryOptions militaryBlankBallot) {
		this.militaryBlankBallot = militaryBlankBallot;
	}

	public void setMilitaryNotes(final String militaryNotes) {
		this.militaryNotes = militaryNotes;
	}

	public void setMilitaryRegistration(final DeliveryOptions militaryRegistration) {
		this.militaryRegistration = militaryRegistration;
	}

	/**
	 * Sets the military voter services website.
	 * 
	 * @param militaryVoterServicesSite
	 *            the military voter services website to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setMilitaryVoterServicesSite(final String militaryVoterServicesSite) {
		this.militaryVoterServicesSite = militaryVoterServicesSite;
	}

	/**
	 * Sets the newVoterRegistrationWitnessesOrNotarization.
	 * 
	 * @param newVoterRegistrationWitnessesOrNotarization
	 *            the newVoterRegistrationWitnessesOrNotarization to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setNewVoterRegistrationWitnessesOrNotarization(
			final WitnessNotarizationRequirements newVoterRegistrationWitnessesOrNotarization) {
		this.newVoterRegistrationWitnessesOrNotarization = newVoterRegistrationWitnessesOrNotarization;
	}

	/**
	 * Sets the online voter registration site.
	 * 
	 * @param onlineVoterRegistrationSite
	 *            the online voter registration site to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setOnlineVoterRegistrationSite(final String onlineVoterRegistrationSite) {
		this.onlineVoterRegistrationSite = onlineVoterRegistrationSite;
	}

	/**
	 * Sets the overseas voter services website.
	 * 
	 * @param overseasVoterServicesSite
	 *            the overseas voter services website to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setOverseasVoterServicesSite(final String overseasVoterServicesSite) {
		this.overseasVoterServicesSite = overseasVoterServicesSite;
	}

	public void setPhysical(final Address physical) {
		if (physical != null) {
			this.physical = physical;
		}
	}

	/**
	 * Sets the registration finder.
	 * 
	 * @param registrationFinderSite
	 *            site the registration finder site to set.
	 * @author IanBrown
	 * @version Jan 23, 2012
	 * @since Jan 23, 2012
	 */
	public void setRegistrationFinderSite(final String registrationFinderSite) {
		this.registrationFinderSite = registrationFinderSite;
	}

	public void setStateContact(final Person stateContact) {
		this.stateContact = stateContact;
	}

	public void setStateEmail(final String stateEmail) {
		this.stateEmail = stateEmail;
	}

	public void setStateFax(final String stateFax) {
		this.stateFax = stateFax;
	}

	public void setStatePhone(final String statePhone) {
		this.statePhone = statePhone;
	}

	public void setUpdated(final Date updated) {
		this.updated = updated;
	}

	/**
	 * Sets the voter registration identification requirements.
	 * 
	 * @param voterRegistrationIdentificationRequirements
	 *            the voter registration identification requirements to set.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public void setVoterRegistrationIdentificationRequirements(final String voterRegistrationIdentificationRequirements) {
		this.voterRegistrationIdentificationRequirements = voterRegistrationIdentificationRequirements;
	}

	/**
	 * Sets the voter registration requirements for domestic voters.
	 * 
	 * @param voterRegistrationRequirements
	 *            the voter registration requirements to set.
	 * @author IanBrown
	 * @version Jan 13, 2012
	 * @since Jan 12, 2012
	 */
	public void setVoterRegistrationRequirements(final String voterRegistrationRequirements) {
		this.voterRegistrationRequirements = voterRegistrationRequirements;
	}

	/**
	 * Sets the voting in-person identification requirements.
	 * 
	 * @param votingInPersonIdentificationRequirements
	 *            the voting in-person identification requirements to set.
	 * @author IanBrown
	 * @version Jan 18, 2012
	 * @since Jan 18, 2012
	 */
	public void setVotingInPersonIdentificationRequirements(final String votingInPersonIdentificationRequirements) {
		this.votingInPersonIdentificationRequirements = votingInPersonIdentificationRequirements;
	}

	public void setWebsite(final String website) {
		this.website = website;
	}

	public DeliveryOptions getDomesticBlankBallot() {
		return domesticBlankBallot;
	}

	public void setDomesticBlankBallot( DeliveryOptions domesticBlankBallot ) {
		this.domesticBlankBallot = domesticBlankBallot;
	}
}
