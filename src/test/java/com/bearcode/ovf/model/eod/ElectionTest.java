/**
 * 
 */
package com.bearcode.ovf.model.eod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link Election}.
 * 
 * @author IanBrown
 * 
 * @since Jan 13, 2012
 * @version Jan 25, 2012
 */
public final class ElectionTest extends EasyMockSupport {

	/**
	 * the election to test.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private Election election;

	/**
	 * Sets up the election to test.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Before
	public final void setUpElection() {
		setElection(createElection());
	}

	/**
	 * Tears down the election after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@After
	public final void tearDownElection() {
		setElection(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getCitizenBallotRequest()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetCitizenBallotRequest() {
		final String actualCitizenBallotRequest = getElection().getCitizenBallotRequest();

		assertNull("There is no citizen ballot request", actualCitizenBallotRequest);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getCitizenBallotReturn()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetCitizenBallotReturn() {
		final String actualCitizenBallotReturn = getElection().getCitizenBallotReturn();

		assertNull("There is no citizen ballot return", actualCitizenBallotReturn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getCitizenRegistration()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetCitizenRegistration() {
		final String actualCitizenRegistration = getElection().getCitizenRegistration();

		assertNull("There is no citizen registration", actualCitizenRegistration);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getDomesticBallotRequest()}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetDomesticBallotRequest() {
		final String actualDomesticBallotRequest = getElection().getDomesticBallotRequest();

		assertNull("There is no domestic ballot request", actualDomesticBallotRequest);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getDomesticBallotReturn()}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetDomesticBallotReturn() {
		final String actualDomesticBallotReturn = getElection().getDomesticBallotReturn();

		assertNull("There is no domestic ballot return", actualDomesticBallotReturn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getDomesticEarlyVoting()}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testGetDomesticEarlyVoting() {
		final String actualDomesticEarlyVoting = getElection().getDomesticEarlyVoting();

		assertNull("The domestic early voting is not set", actualDomesticEarlyVoting);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getDomesticNotes()}.
	 * 
	 * @author IanBrown
	 * @since Jan 25, 2012
	 * @version Jan 25, 2012
	 */
	@Test
	public final void testGetDomesticNotes() {
		final String actualDomesticNotes = getElection().getDomesticNotes();

		assertNull("There are no domestic notes", actualDomesticNotes);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setDomesticRegistration(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetDomesticRegistration() {
		final String actualDomesticRegistration = getElection().getDomesticRegistration();

		assertNull("There is no domestic registration", actualDomesticRegistration);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getHeldOn()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetHeldOn() {
		final String actualHeldOn = getElection().getHeldOn();

		assertNull("The election is not held", actualHeldOn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getId()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetId() {
		final long actualId = getElection().getId();

		assertEquals("The ID is not set", 0l, actualId);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getMilitaryBallotRequest()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetMilitaryBallotRequest() {
		final String actualMilitaryBallotRequest = getElection().getMilitaryBallotRequest();

		assertNull("There is no military ballot request", actualMilitaryBallotRequest);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getMilitaryBallotReturn()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetMilitaryBallotReturn() {
		final String actualMilitaryBallotReturn = getElection().getMilitaryBallotReturn();

		assertNull("There is no military ballot return", actualMilitaryBallotReturn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getMilitaryRegistration()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetMilitaryRegistration() {
		final String actualMilitaryRegistration = getElection().getMilitaryRegistration();

		assertNull("There is no military registration", actualMilitaryRegistration);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getNotes()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetNotes() {
		final String actualNotes = getElection().getNotes();

		assertNull("There are no notes", actualNotes);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getOrder()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetOrder() {
		final int actualOrder = getElection().getOrder();

		assertEquals("The order is not set", 0, actualOrder);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getStateInfo()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetStateInfo() {
		final StateSpecificDirectory actualStateInfo = getElection().getStateInfo();

		assertNull("There is no state info", actualStateInfo);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getTitle()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetTitle() {
		final String actualTitle = getElection().getTitle();

		assertNull("There is no title", actualTitle);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#getTypeOfElection()}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testGetTypeOfElection() {
		final TypeOfElection actualTypeOfElection = getElection().getTypeOfElection();

		assertNull("The election has no type", actualTypeOfElection);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setCitizenBallotRequest(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetCitizenBallotRequest() {
		final String citizenBallotRequest = "Citizen Ballot Request";

		getElection().setCitizenBallotRequest(citizenBallotRequest);

		assertEquals("The citizen ballot request is set", citizenBallotRequest, getElection().getCitizenBallotRequest());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setCitizenBallotReturn(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetCitizenBallotReturn() {
		final String citizenBallotReturn = "Citizen Ballot Return";

		getElection().setCitizenBallotReturn(citizenBallotReturn);

		assertEquals("The citizen ballot return is set", citizenBallotReturn, getElection().getCitizenBallotReturn());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setCitizenRegistration(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetCitizenRegistration() {
		final String citizenRegistration = "Citizen Registration";

		getElection().setCitizenRegistration(citizenRegistration);

		assertEquals("The citizen registration is set", citizenRegistration, getElection().getCitizenRegistration());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setDomesticBallotRequest(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetDomesticBallotRequest() {
		final String domesticBallotRequest = "Domestic Ballot Request";

		getElection().setDomesticBallotRequest(domesticBallotRequest);

		assertEquals("The domestic ballot request is set", domesticBallotRequest, getElection().getDomesticBallotRequest());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setDomesticBallotReturn(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetDomesticBallotReturn() {
		final String domesticBallotReturn = "Domestic Ballot Return";

		getElection().setDomesticBallotReturn(domesticBallotReturn);

		assertEquals("The domestic ballot return is set", domesticBallotReturn, getElection().getDomesticBallotReturn());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setDomesticEarlyVoting(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetDomesticEarlyVoting() {
		final String domesticEarlyVoting = "Domestic Early Voting";

		getElection().setDomesticEarlyVoting(domesticEarlyVoting);

		assertEquals("The domestic early voting is set", domesticEarlyVoting, getElection().getDomesticEarlyVoting());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setDomesticNotes(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 25, 2012
	 * @version Jan 25, 2012
	 */
	@Test
	public final void testSetDomesticNotes() {
		final String domesticNotes = "Domestic Notes";

		getElection().setDomesticNotes(domesticNotes);

		assertEquals("The domestic notes are set", domesticNotes, getElection().getDomesticNotes());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setDomesticRegistration(String)}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetDomesticRegistration() {
		final String domesticRegistration = "Domestic Registration";

		getElection().setDomesticRegistration(domesticRegistration);

		assertEquals("The domestic registration is set", domesticRegistration, getElection().getDomesticRegistration());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setHeldOn(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetHeldOn() {
		final String heldOn = "Held On";

		getElection().setHeldOn(heldOn);

		assertEquals("The election is held on", heldOn, getElection().getHeldOn());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setId(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetId() {
		final long id = 98712l;

		getElection().setId(id);

		assertEquals("The ID is set", id, getElection().getId());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setMilitaryBallotRequest(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetMilitaryBallotRequest() {
		final String militaryBallotRequest = "Military Ballot Request";

		getElection().setMilitaryBallotRequest(militaryBallotRequest);

		assertEquals("The military ballot request is set", militaryBallotRequest, getElection().getMilitaryBallotRequest());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setMilitaryBallotReturn(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetMilitaryBallotReturn() {
		final String militaryBallotReturn = "Military Ballot Return";

		getElection().setMilitaryBallotReturn(militaryBallotReturn);

		assertEquals("The military ballot return is set", militaryBallotReturn, getElection().getMilitaryBallotReturn());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setMilitaryRegistration(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetMilitaryRegistration() {
		final String militaryRegistration = "Military Registration";

		getElection().setMilitaryRegistration(militaryRegistration);

		assertEquals("The military registration is set", militaryRegistration, getElection().getMilitaryRegistration());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setNotes(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetNotes() {
		final String notes = "Notes";

		getElection().setNotes(notes);

		assertEquals("The notes are set", notes, getElection().getNotes());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setOrder(int)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetOrder() {
		final int order = 762;

		getElection().setOrder(order);

		assertEquals("The order is set", order, getElection().getOrder());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setStateInfo(com.bearcode.ovf.model.eod.StateSpecificDirectory)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetStateInfo() {
		final StateSpecificDirectory stateInfo = createMock("StateInfo", StateSpecificDirectory.class);

		getElection().setStateInfo(stateInfo);

		assertSame("The state info is set", stateInfo, getElection().getStateInfo());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setTitle(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetTitle() {
		final String title = "Title";

		getElection().setTitle(title);

		assertEquals("The title is set", title, getElection().getTitle());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.Election#setTypeOfElection(TypeOfElection)}.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	public final void testSetTypeOfElection() {
		final TypeOfElection typeOfElection = TypeOfElection.FEDERAL;

		getElection().setTypeOfElection(typeOfElection);

		assertSame("The type of election is set", typeOfElection, getElection().getTypeOfElection());
	}

	/**
	 * Creates an election.
	 * 
	 * @author IanBrown
	 * @return the election.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private Election createElection() {
		return new Election();
	}

	/**
	 * Gets the election.
	 * 
	 * @author IanBrown
	 * @return the election.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private Election getElection() {
		return election;
	}

	/**
	 * Sets the election.
	 * 
	 * @author IanBrown
	 * @param election
	 *            the election to set.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private void setElection(final Election election) {
		this.election = election;
	}

}
