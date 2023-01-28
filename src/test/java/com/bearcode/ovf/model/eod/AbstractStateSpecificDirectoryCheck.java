/**
 * 
 */
package com.bearcode.ovf.model.eod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.Person;

/**
 * Abstract test for implementations of {@link AbstractStateSpecificDirectory}.
 * 
 * @author IanBrown
 * 
 * @param <D>
 *            the type of state specific directory to test.
 * @since Jan 12, 2012
 * @version Jan 18, 2012
 */
public abstract class AbstractStateSpecificDirectoryCheck<D extends AbstractStateSpecificDirectory> extends EasyMockSupport {

	/**
	 * the state specific directory to test.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	private D stateSpecificDirectory;

	/**
	 * Sets up the state-specific directory for testing.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Before
	public final void setUpStateSpecificDirectory() {
		setUpForStateSpecificDirectory();
		setStateSpecificDirectory(createStateSpecificDirectory());
	}

	/**
	 * Tears down the state specific directory after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@After
	public final void tearDownStateSpecificDirectory() {
		setStateSpecificDirectory(null);
		tearDownForStateSpecificDirectory();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getAbsenteeBallotAffidavitWitnessesOrNotarization()}.
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@Test
	public final void testGetAbsenteeBallotAffidavitWitnessesOrNotarization() {
		final WitnessNotarizationRequirements actualAbsenteeBallotAffidavitWitnessesOrNotarization = getStateSpecificDirectory()
				.getAbsenteeBallotAffidavitWitnessesOrNotarization();

		assertNotNull("There is an absentee ballot affidavit witnesses or notarization object",
				actualAbsenteeBallotAffidavitWitnessesOrNotarization);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getAbsenteeBallotRequestIdentificationRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testGetAbsenteeBallotRequestIdentificationRequirements() {
		final String actualAbsenteeBallotRequestIdentificationRequirements = getStateSpecificDirectory()
				.getAbsenteeBallotRequestIdentificationRequirements();

		assertEquals("There are no absentee ballot request identification requirements", "",
				actualAbsenteeBallotRequestIdentificationRequirements);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getAbsenteeBallotRequestRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetAbsenteeBallotRequestRequirements() {
		final String actualAbsenteeBallotRequestRequirements = getStateSpecificDirectory().getAbsenteeBallotRequestRequirements();

		assertEquals("There are no absentee ballot request requirements", "", actualAbsenteeBallotRequestRequirements);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getAdminNotes()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetAdminNotes() {
		final String actualAdminNotes = getStateSpecificDirectory().getAdminNotes();

		assertEquals("There are no administrative notes set", "", actualAdminNotes);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getBallotTrackingSite()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetBallotTrackingSite() {
		final String actualBallotTrackingSite = getStateSpecificDirectory().getBallotTrackingSite();

		assertEquals("There is no ballot tracking site set", "", actualBallotTrackingSite);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getCitizenBallotRequest()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetCitizenBallotRequest() {
		final DeliveryOptions actualCitizenBallotRequest = getStateSpecificDirectory().getCitizenBallotRequest();

		assertNotNull("There is a citizen ballot request set", actualCitizenBallotRequest);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getCitizenBallotReturn()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetCitizenBallotReturn() {
		final DeliveryOptions actualCitizenBallotReturn = getStateSpecificDirectory().getCitizenBallotReturn();

		assertNotNull("There is a citizen ballot return set", actualCitizenBallotReturn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getCitizenBlankBallot()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetCitizenBlankBallot() {
		final DeliveryOptions actualCitizenBlankBallot = getStateSpecificDirectory().getCitizenBlankBallot();

		assertNotNull("There is a citizen blank ballot set", actualCitizenBlankBallot);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getCitizenNotes()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetCitizenNotes() {
		final String actualCitizenNotes = getStateSpecificDirectory().getCitizenNotes();

		assertEquals("There are no citizen notes set", "", actualCitizenNotes);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getCitizenRegistration()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetCitizenRegistration() {
		final DeliveryOptions actualCitizenRegistration = getStateSpecificDirectory().getCitizenRegistration();

		assertNotNull("There is a citizen registration set", actualCitizenRegistration);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getConfirmationOrStatus()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetConfirmationOrStatus() {
		final String actualConfirmationOrStatus = getStateSpecificDirectory().getConfirmationOrStatus();

		assertNull("There is no confirmation or status set", actualConfirmationOrStatus);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getContactNotes()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetContactNotes() {
		final String actualContactNotes = getStateSpecificDirectory().getContactNotes();

		assertEquals("There are no contact notes set", "", actualContactNotes);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getDomesticBallotRequest()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetDomesticBallotRequest() {
		final DeliveryOptions actualDomesticBallotRequest = getStateSpecificDirectory().getDomesticBallotRequest();

		assertNotNull("There is a domestic ballot request set", actualDomesticBallotRequest);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getDomesticBallotReturn()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetDomesticBallotReturn() {
		final DeliveryOptions actualDomesticBallotReturn = getStateSpecificDirectory().getDomesticBallotReturn();

		assertNotNull("There is a domestic ballot return set", actualDomesticBallotReturn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getDomesticEarlyVoter()}.
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	@Test
	public final void testGetDomesticEarlyVoter() {
		final DeliveryOptions actualDomesticEarlyVoter = getStateSpecificDirectory().getDomesticEarlyVoter();

		assertNotNull("There is a domestic early voter set", actualDomesticEarlyVoter);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getDomesitcNotes()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetDomesticNotes() {
		final String actualDomesticNotes = getStateSpecificDirectory().getDomesticNotes();

		assertEquals("There are no domestic notes set", "", actualDomesticNotes);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getDomesticRegistration()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testGetDomesticRegistration() {
		final DeliveryOptions actualDomesticRegistration = getStateSpecificDirectory().getDomesticRegistration();

		assertNotNull("There is a domestic registration set", actualDomesticRegistration);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getEarlyVotingIdentificationRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testGetEarlyVotingIdentificationRequirements() {
		final String actualEarlyVotingIdentificationRequirements = getStateSpecificDirectory()
				.getEarlyVotingIdentificationRequirements();

		assertEquals("There are no early voting identification requirements", "", actualEarlyVotingIdentificationRequirements);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getEarlyVotingInformationSite()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetEarlyVotingInformationSite() {
		final String actualEarlyVotingInformationSite = getStateSpecificDirectory().getEarlyVotingInformationSite();

		assertEquals("There is no early voting information site set", "", actualEarlyVotingInformationSite);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getEarlyVotingRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetEarlyVotingRequirements() {
		final String actualEarlyVotingRequirements = getStateSpecificDirectory().getEarlyVotingRequirements();

		assertEquals("There are no early voting requirements", "", actualEarlyVotingRequirements);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getEarlyVotingWitnessesOrNotarization()}.
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@Test
	public final void testGetEarlyVotingWitnessesOrNotarization() {
		final WitnessNotarizationRequirements actualEarlyVotingWitnessesOrNotarization = getStateSpecificDirectory()
				.getEarlyVotingWitnessesOrNotarization();

		assertNotNull("There is an early voting witnesses or notarization object", actualEarlyVotingWitnessesOrNotarization);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getElections()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetElections() {
		final Collection<?> actualElections = getStateSpecificDirectory().getElections();

		assertNull("There are no elections", actualElections);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getId()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetId() {
		final long actualId = getStateSpecificDirectory().getId();

		assertEquals("There is no ID set", 0l, actualId);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getMailing()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetMailing() {
		final Address actualMailing = getStateSpecificDirectory().getMailing();

		assertNotNull("There is a mailing address", actualMailing);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getMilitaryBallotRequest()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetMilitaryBallotRequest() {
		final DeliveryOptions actualMilitaryBallotRequest = getStateSpecificDirectory().getMilitaryBallotRequest();

		assertNotNull("There is a military ballot request", actualMilitaryBallotRequest);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getMilitaryBallotReturn()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetMilitaryBallotReturn() {
		final DeliveryOptions actualMilitaryBallotReturn = getStateSpecificDirectory().getMilitaryBallotReturn();

		assertNotNull("There is a military ballot return", actualMilitaryBallotReturn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getMilitaryBlankBallot()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetMilitaryBlankBallot() {
		final DeliveryOptions actualMilitaryBlankBallot = getStateSpecificDirectory().getMilitaryBlankBallot();

		assertNotNull("There is a military blank ballot", actualMilitaryBlankBallot);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getMilitaryNotes()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetMilitaryNotes() {
		final String actualMilitaryNotes = getStateSpecificDirectory().getMilitaryNotes();

		assertEquals("There are no military notes set", "", actualMilitaryNotes);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getMilitaryRegistration()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetMilitaryRegistration() {
		final DeliveryOptions actualMilitaryRegistration = getStateSpecificDirectory().getMilitaryRegistration();

		assertNotNull("There is a military registration", actualMilitaryRegistration);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getMilitaryVoterServicesSite()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetMilitaryVoterServicesSite() {
		final String actualMilitaryVoterServicesSite = getStateSpecificDirectory().getMilitaryVoterServicesSite();

		assertEquals("There is no military voter services site set", "", actualMilitaryVoterServicesSite);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getNewVoterRegistrationWitnessesOrNotarization()}.
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@Test
	public final void testGetNewVoterRegistrationWitnessesOrNotarization() {
		final WitnessNotarizationRequirements actualNewVoterRegistrationWitnessesOrNotarization = getStateSpecificDirectory()
				.getNewVoterRegistrationWitnessesOrNotarization();

		assertNotNull("There is a new voter registration witnesses or notarization requirements object",
				actualNewVoterRegistrationWitnessesOrNotarization);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getOnlineVoterRegistrationSite()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetOnlineVoterRegistrationSite() {
		final String actualOnlineVoterRegistrationSite = getStateSpecificDirectory().getOnlineVoterRegistrationSite();

		assertEquals("There is no online voter registration site set", "", actualOnlineVoterRegistrationSite);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getOverseasVoterServicesSite()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetOverseasVoterServicesSite() {
		final String actualOverseasVoterServicesSite = getStateSpecificDirectory().getOverseasVoterServicesSite();

		assertEquals("There is no overseas voter services site set", "", actualOverseasVoterServicesSite);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getPhysical()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetPhysical() {
		final Address actualPhysical = getStateSpecificDirectory().getPhysical();

		assertNotNull("There is a physical address", actualPhysical);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getRegistrationFinderSite()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetRegistrationFinderSite() {
		final String actualRegistrationFinderSite = getStateSpecificDirectory().getRegistrationFinderSite();

		assertEquals("There is no registration finder site set", "", actualRegistrationFinderSite);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getStateContact()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetStateContact() {
		final Person actualStateContact = getStateSpecificDirectory().getStateContact();

		assertNotNull("There is a state contact", actualStateContact);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getStateEmail()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetStateEmail() {
		final String actualStateEmail = getStateSpecificDirectory().getStateEmail();

		assertNull("There is no state email set", actualStateEmail);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getStateFax()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetStateFax() {
		final String actualStateFax = getStateSpecificDirectory().getStateFax();

		assertNull("There is no state fax set", actualStateFax);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getStatePhone()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetStatePhone() {
		final String actualStatePhone = getStateSpecificDirectory().getStatePhone();

		assertNull("There is no state phone set", actualStatePhone);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getUpdated()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetUpdated() {
		final Date actualUpdated = getStateSpecificDirectory().getUpdated();

		assertNull("There is no updated time", actualUpdated);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getVoterRegistrationIdentificationRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testGetVoterRegistrationIdentificationRequirements() {
		final String actualVoterRegistrationIdentificationRequirements = getStateSpecificDirectory()
				.getVoterRegistrationIdentificationRequirements();

		assertEquals("There are no voter registration identification requirements set", "",
				actualVoterRegistrationIdentificationRequirements);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getVoterRegistrationRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetVoterRegistrationRequirements() {
		final String actualVoterRegistrationRequirements = getStateSpecificDirectory().getVoterRegistrationRequirements();

		assertEquals("There are no voter registration requirements set", "", actualVoterRegistrationRequirements);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getVotingInPersonIdentificationRequirements()}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testGetVotingInPersonIdentificationRequirements() {
		final String actualVotingInPersonIdentificationRequirements = getStateSpecificDirectory()
				.getVotingInPersonIdentificationRequirements();

		assertEquals("There are no voting in-person identification requirements", "",
				actualVotingInPersonIdentificationRequirements);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#getWebsite()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testGetWebsite() {
		final String actualWebsite = getStateSpecificDirectory().getWebsite();

		assertEquals("There is no website set", "", actualWebsite);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setAbsenteeBallotAffidavitWitnessesOrNotarization(WitnessNotarizationRequirements)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@Test
	public final void testSetAbsenteeABallotAfffidavitWitnessesOrNotarization() {
		final WitnessNotarizationRequirements absenteeBallotAffidavitWitnessesOrNotarization = createMock(
				"AbsenteeBallotAffidavitWitnessesOrNotarization", WitnessNotarizationRequirements.class);

		getStateSpecificDirectory().setAbsenteeBallotAffidavitWitnessesOrNotarization(
				absenteeBallotAffidavitWitnessesOrNotarization);

		assertSame("The absentee ballot affidavit witnesses or notarization object is set",
				absenteeBallotAffidavitWitnessesOrNotarization, getStateSpecificDirectory()
						.getAbsenteeBallotAffidavitWitnessesOrNotarization());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setAbsenteeBallotRequestIdentificationRequirements(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetAbsenteeBallotRequestIdentificationRequirements() {
		final String absenteeBallotRequestIdentificationRequirements = "Absentee Ballot Request Identification Requirements";

		getStateSpecificDirectory().setAbsenteeBallotRequestIdentificationRequirements(
				absenteeBallotRequestIdentificationRequirements);

		assertEquals("The absentee ballot request identification requirements are set",
				absenteeBallotRequestIdentificationRequirements, getStateSpecificDirectory()
						.getAbsenteeBallotRequestIdentificationRequirements());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setAbsenteeBallotRequestRequirements(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetAbsenteeBallotRequestRequirements() {
		final String absenteeBallotRequestRequirements = "Absentee Ballot Request Requirements";

		getStateSpecificDirectory().setAbsenteeBallotRequestRequirements(absenteeBallotRequestRequirements);

		assertEquals("The absentee ballot request requirements are set", absenteeBallotRequestRequirements,
				getStateSpecificDirectory().getAbsenteeBallotRequestRequirements());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setAdminNotes(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetAdminNotes() {
		final String adminNotes = "Admin Notes";

		getStateSpecificDirectory().setAdminNotes(adminNotes);

		assertEquals("The administrative notes are set", adminNotes, getStateSpecificDirectory().getAdminNotes());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setBallotTrackingSite(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetBallotTrackingSite() {
		final String ballotTrackingSite = "Ballot Tracking Site";

		getStateSpecificDirectory().setBallotTrackingSite(ballotTrackingSite);

		assertEquals("The ballot tracking site is set", ballotTrackingSite, getStateSpecificDirectory().getBallotTrackingSite());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setCitizenBallotRequest(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetCitizenBallotRequest() {
		final DeliveryOptions citizenBallotRequest = createMock("CitizenBallotRequest", DeliveryOptions.class);

		getStateSpecificDirectory().setCitizenBallotRequest(citizenBallotRequest);

		assertSame("The citizen ballot request is set", citizenBallotRequest, getStateSpecificDirectory().getCitizenBallotRequest());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setCitizenBallotReturn(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetCitizenBallotReturn() {
		final DeliveryOptions citizenBallotReturn = createMock("CitizenBallotReturn", DeliveryOptions.class);

		getStateSpecificDirectory().setCitizenBallotReturn(citizenBallotReturn);

		assertSame("The citizen ballot return is set", citizenBallotReturn, getStateSpecificDirectory().getCitizenBallotReturn());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setCitizenBlankBallot(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetCitizenBlankBallot() {
		final DeliveryOptions citizenBlankBallot = createMock("CitizenBlankBallot", DeliveryOptions.class);

		getStateSpecificDirectory().setCitizenBlankBallot(citizenBlankBallot);

		assertSame("The citizen blank ballot is set", citizenBlankBallot, getStateSpecificDirectory().getCitizenBlankBallot());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setCitizenNotes(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetCitizenNotes() {
		final String citizenNotes = "Citizen Notes";

		getStateSpecificDirectory().setCitizenNotes(citizenNotes);

		assertEquals("The citizen notes are set", citizenNotes, getStateSpecificDirectory().getCitizenNotes());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setCitizenRegistration(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetCitizenRegistration() {
		final DeliveryOptions citizenRegistration = createMock("CitizenRegistration", DeliveryOptions.class);

		getStateSpecificDirectory().setCitizenRegistration(citizenRegistration);

		assertSame("The citizen registration is set", citizenRegistration, getStateSpecificDirectory().getCitizenRegistration());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setConfirmationOrStatus(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetConfirmationOrStatus() {
		final String confirmationOrStatus = "Confirmation or Status";

		getStateSpecificDirectory().setConfirmationOrStatus(confirmationOrStatus);

		assertEquals("The confirmation or status is set", confirmationOrStatus, getStateSpecificDirectory()
				.getConfirmationOrStatus());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setContactNotes(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetContactNotes() {
		final String contactNotes = "Contact Notes";

		getStateSpecificDirectory().setContactNotes(contactNotes);

		assertEquals("The contact notes are set", contactNotes, getStateSpecificDirectory().getContactNotes());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setDomesticBallotRequest(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetDomesticBallotRequest() {
		final DeliveryOptions domesticBallotRequest = createMock("DomesticBallotRequest", DeliveryOptions.class);

		getStateSpecificDirectory().setDomesticBallotRequest(domesticBallotRequest);

		assertSame("The domestic ballot request is set", domesticBallotRequest, getStateSpecificDirectory()
				.getDomesticBallotRequest());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setDomesticBallotReturn(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testSetDomesticBallotReturn() {
		final DeliveryOptions domesticBallotReturn = createMock("DomesticBallotReturn", DeliveryOptions.class);

		getStateSpecificDirectory().setDomesticBallotReturn(domesticBallotReturn);

		assertSame("The domestic ballot return is set", domesticBallotReturn, getStateSpecificDirectory().getDomesticBallotReturn());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setDomesticEarlyVoter(DeliveryOptions)}.
	 * 
	 * @author IanBrown
	 * @since Jan 26, 2012
	 * @version Jan 26, 2012
	 */
	@Test
	public final void testSetDomesticEarlyVoter() {
		final DeliveryOptions domesticEarlyVoter = createMock("DomesticEarlyVoter", DeliveryOptions.class);

		getStateSpecificDirectory().setDomesticEarlyVoter(domesticEarlyVoter);

		assertSame("The domestic early voter is set", domesticEarlyVoter, getStateSpecificDirectory().getDomesticEarlyVoter());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setDomesticNotes(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 16, 2012
	 */
	@Test
	public final void testSetDomesticNotes() {
		final String domesticNotes = "Domestic Notes";

		getStateSpecificDirectory().setDomesticNotes(domesticNotes);

		assertEquals("The domestic notes are set", domesticNotes, getStateSpecificDirectory().getDomesticNotes());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setDomesticRegistration(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 17, 2012
	 * @version Jan 17, 2012
	 */
	@Test
	public final void testSetDomesticRegistration() {
		final DeliveryOptions domesticRegistration = createMock("DomesticRegistration", DeliveryOptions.class);

		getStateSpecificDirectory().setDomesticRegistration(domesticRegistration);

		assertSame("The domestic registration is set", domesticRegistration, getStateSpecificDirectory().getDomesticRegistration());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setEarlyVotingIdentificationRequirements(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetEarlyVotingIdentificationRequirements() {
		final String earlyVotingIdentificationRequirements = "Early Voting Identification Requirements";

		getStateSpecificDirectory().setEarlyVotingIdentificationRequirements(earlyVotingIdentificationRequirements);

		assertEquals("The early voting identification requirements are set", earlyVotingIdentificationRequirements,
				getStateSpecificDirectory().getEarlyVotingIdentificationRequirements());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setEarlyVotingInformationSite(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetEarlyVotingInformationSite() {
		final String earlyVotingInformationSite = "Early Voting Information Site";

		getStateSpecificDirectory().setEarlyVotingInformationSite(earlyVotingInformationSite);

		assertEquals("The ballot tracking site is set", earlyVotingInformationSite, getStateSpecificDirectory()
				.getEarlyVotingInformationSite());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setEarlyVotingRequirements(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetEarlyVotingRequirements() {
		final String earlyVotingRequirements = "Early Voting Requirements";

		getStateSpecificDirectory().setEarlyVotingRequirements(earlyVotingRequirements);

		assertEquals("The early voting requirements are set", earlyVotingRequirements, getStateSpecificDirectory()
				.getEarlyVotingRequirements());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setEarlyVotingWitnessesOrNotarization(WitnessNotarizationRequirements)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@Test
	public final void testSetEarlyVotingWitnessesOrNotarization() {
		final WitnessNotarizationRequirements earlyVotingWitnessesOrNotarization = createMock("EarlyVotingWitnessesOrNotarization",
				WitnessNotarizationRequirements.class);

		getStateSpecificDirectory().setEarlyVotingWitnessesOrNotarization(earlyVotingWitnessesOrNotarization);

		assertSame("The early voting witnesses or notarization object is set", earlyVotingWitnessesOrNotarization,
				getStateSpecificDirectory().getEarlyVotingWitnessesOrNotarization());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setElections(java.util.Collection)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetElections() {
		final Election election = createMock("Election", Election.class);
		final Collection<?> elections = Arrays.asList(election);

		getStateSpecificDirectory().setElections(elections);

		assertSame("The elections are set", elections, getStateSpecificDirectory().getElections());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setId(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetId() {
		final long id = 9812l;

		getStateSpecificDirectory().setId(id);

		assertEquals("The ID is set", id, getStateSpecificDirectory().getId());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setMailing(com.bearcode.ovf.model.common.Address)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetMailing() {
		final Address mailing = createMock("Mailing", Address.class);

		getStateSpecificDirectory().setMailing(mailing);

		assertSame("The mailing address is set", mailing, getStateSpecificDirectory().getMailing());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setMilitaryBallotRequest(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetMilitaryBallotRequest() {
		final DeliveryOptions militaryBallotRequest = createMock("MilitaryBallotRequest", DeliveryOptions.class);

		getStateSpecificDirectory().setMilitaryBallotRequest(militaryBallotRequest);

		assertSame("The military ballot request is set", militaryBallotRequest, getStateSpecificDirectory()
				.getMilitaryBallotRequest());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setMilitaryBallotReturn(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetMilitaryBallotReturn() {
		final DeliveryOptions militaryBallotReturn = createMock("MilitaryBallotReturn", DeliveryOptions.class);

		getStateSpecificDirectory().setMilitaryBallotReturn(militaryBallotReturn);

		assertSame("The military ballot return is set", militaryBallotReturn, getStateSpecificDirectory().getMilitaryBallotReturn());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setMilitaryBlankBallot(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetMilitaryBlankBallot() {
		final DeliveryOptions militaryBlankBallot = createMock("MilitaryBlankBallot", DeliveryOptions.class);

		getStateSpecificDirectory().setMilitaryBlankBallot(militaryBlankBallot);

		assertSame("The military blank ballot is set", militaryBlankBallot, getStateSpecificDirectory().getMilitaryBlankBallot());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setMilitaryNotes(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetMilitaryNotes() {
		final String militaryNotes = "Military Notes";

		getStateSpecificDirectory().setMilitaryNotes(militaryNotes);

		assertEquals("The military notes are set", militaryNotes, getStateSpecificDirectory().getMilitaryNotes());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setMilitaryRegistration(com.bearcode.ovf.model.eod.DeliveryOptions)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetMilitaryRegistration() {
		final DeliveryOptions militaryRegistration = createMock("MilitaryRegistration", DeliveryOptions.class);

		getStateSpecificDirectory().setMilitaryRegistration(militaryRegistration);

		assertSame("The military registration is set", militaryRegistration, getStateSpecificDirectory().getMilitaryRegistration());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setMilitaryVoterServicesSite(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetMilitaryVoterServicesSite() {
		final String militaryVoterServicesSite = "Military Voter Services Site";

		getStateSpecificDirectory().setMilitaryVoterServicesSite(militaryVoterServicesSite);

		assertEquals("The military voter services site is set", militaryVoterServicesSite, getStateSpecificDirectory()
				.getMilitaryVoterServicesSite());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setNewVoterRegistrationWitnessesOrNotarization(WitnessNotarizationRequirements)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jan 23, 2012
	 * @version Jan 23, 2012
	 */
	@Test
	public final void testSetNewVoterRegistrationNotarizationOrWitnesses() {
		final WitnessNotarizationRequirements newVoterRegistrationWitnessesOrNotarization = createMock(
				"NewVoterRegistrationWitnessesOrNotarization", WitnessNotarizationRequirements.class);

		getStateSpecificDirectory().setNewVoterRegistrationWitnessesOrNotarization(newVoterRegistrationWitnessesOrNotarization);

		assertSame("The new voter registration witnesses or notarization object is set",
				newVoterRegistrationWitnessesOrNotarization, getStateSpecificDirectory()
						.getNewVoterRegistrationWitnessesOrNotarization());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setOnlineVoterRegistrationSite(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetOnlineVoterRegistraitonSite() {
		final String onlineVoterRegistrationSite = "Online Voter Registration Site";

		getStateSpecificDirectory().setOnlineVoterRegistrationSite(onlineVoterRegistrationSite);

		assertEquals("The online voter registration site is set", onlineVoterRegistrationSite, getStateSpecificDirectory()
				.getOnlineVoterRegistrationSite());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setOverseasVoterServicesSite(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetOverseasVoterServicesSite() {
		final String overseasVoterServicesSite = "Overseas Voter Services Site";

		getStateSpecificDirectory().setOverseasVoterServicesSite(overseasVoterServicesSite);

		assertEquals("The overseas voter services site is set", overseasVoterServicesSite, getStateSpecificDirectory()
				.getOverseasVoterServicesSite());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setPhysical(com.bearcode.ovf.model.common.Address)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetPhysical() {
		final Address physical = createMock("Physical", Address.class);

		getStateSpecificDirectory().setPhysical(physical);

		assertSame("The physical address is set", physical, getStateSpecificDirectory().getPhysical());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setRegistrationFinderSite(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetRegistrationFinderSite() {
		final String registrationFinderSite = "Registration Finder Site";

		getStateSpecificDirectory().setRegistrationFinderSite(registrationFinderSite);

		assertEquals("The registration finder site is set", registrationFinderSite, getStateSpecificDirectory()
				.getRegistrationFinderSite());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setStateContact(com.bearcode.ovf.model.common.Person)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetStateContact() {
		final Person stateContact = createMock("StateContact", Person.class);

		getStateSpecificDirectory().setStateContact(stateContact);

		assertSame("The state contact is set", stateContact, getStateSpecificDirectory().getStateContact());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setStateEmail(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetStateEmail() {
		final String stateEmail = "State Email";

		getStateSpecificDirectory().setStateEmail(stateEmail);

		assertEquals("The state email is set", stateEmail, getStateSpecificDirectory().getStateEmail());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setStateFax(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetStateFax() {
		final String stateFax = "State Fax";

		getStateSpecificDirectory().setStateFax(stateFax);

		assertEquals("The state fax is set", stateFax, getStateSpecificDirectory().getStateFax());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setStatePhone(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetStatePhone() {
		final String statePhone = "State Phone";

		getStateSpecificDirectory().setStatePhone(statePhone);

		assertEquals("The state phone is set", statePhone, getStateSpecificDirectory().getStatePhone());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setUpdated(java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetUpdated() {
		final Date updated = new Date();

		getStateSpecificDirectory().setUpdated(updated);

		assertSame("The updated time is set", updated, getStateSpecificDirectory().getUpdated());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setVoterRegistrationIdentificationRequirements(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetVoterRegistrationIdentificationRequirements() {
		final String voterRegistrationIdentificationRequirements = "Voter Registration Identification Requirements";

		getStateSpecificDirectory().setVoterRegistrationIdentificationRequirements(voterRegistrationIdentificationRequirements);

		assertEquals("The voter registration identification requirements are set", voterRegistrationIdentificationRequirements,
				getStateSpecificDirectory().getVoterRegistrationIdentificationRequirements());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setVoterRegistrationRequirements(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetVoterRegistrationRequirements() {
		final String voterRegistrationRequirements = "Voter Registration Requirements";

		getStateSpecificDirectory().setVoterRegistrationRequirements(voterRegistrationRequirements);

		assertEquals("The voter registration requirements are set", voterRegistrationRequirements, getStateSpecificDirectory()
				.getVoterRegistrationRequirements());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setVotingInPersonIdentificationRequirements(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetVotingInPersonIdentificationRequirements() {
		final String votingInPersonIdentificationRequirements = "Voting In-Person Identification Requirements";

		getStateSpecificDirectory().setVotingInPersonIdentificationRequirements(votingInPersonIdentificationRequirements);

		assertEquals("The voting in-person identification requirements are set", votingInPersonIdentificationRequirements,
				getStateSpecificDirectory().getVotingInPersonIdentificationRequirements());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory#setWebsite(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	@Test
	public final void testSetWebsite() {
		final String website = "Website";

		getStateSpecificDirectory().setWebsite(website);

		assertEquals("The website is set", website, getStateSpecificDirectory().getWebsite());
	}

	/**
	 * Creates a state specific directory of the type to test.
	 * 
	 * @author IanBrown
	 * @return the state specific directory.
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	protected abstract D createStateSpecificDirectory();

	/**
	 * Gets the state specific directory.
	 * 
	 * @author IanBrown
	 * @return the state specific directory.
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	protected final D getStateSpecificDirectory() {
		return stateSpecificDirectory;
	}

	/**
	 * Sets up to test the specific type of state specific directory.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	protected abstract void setUpForStateSpecificDirectory();

	/**
	 * Tears down the set up for testing the specific type of state specific directory.
	 * 
	 * @author IanBrown
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	protected abstract void tearDownForStateSpecificDirectory();

	/**
	 * Sets the state specific directory.
	 * 
	 * @author IanBrown
	 * @param stateSpecificDirectory
	 *            the state specific directory to set.
	 * @since Jan 12, 2012
	 * @version Jan 12, 2012
	 */
	private void setStateSpecificDirectory(final D stateSpecificDirectory) {
		this.stateSpecificDirectory = stateSpecificDirectory;
	}
}
