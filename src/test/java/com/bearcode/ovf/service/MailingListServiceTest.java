/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.MailingAddressDAO;
import com.bearcode.ovf.DAO.MailingListDAO;
import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test for {@link MailingListService}.
 * 
 * @author IanBrown
 * 
 * @since Apr 19, 2012
 * @version Apr 19, 2012
 */
public final class MailingListServiceTest extends EasyMockSupport {

	/**
	 * the mailing list service to test.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private MailingListService mailingListService;

	/**
	 * the data access object used to get the state information.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private StateDAO stateDAO;

	/**
	 * the data access object used to work with the mailing lists.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private MailingAddressDAO mailingAddressDAO;

    /**
     * the data access object used to work with the mailing lists.
     *
     * @author Leonid Ginzburg
     * @since Jul 11, 2012
     * @version Jul 11, 2012
     */

    private MailingListDAO mailingListDAO;


	/**
	 * Sets up the mailing list service to test.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Before
	public final void setUpMailingListService() {
		setStateDAO(createMock("StateDAO", StateDAO.class));
		setMailingAddressDAO( createMock( "MailingAddressDAO", MailingAddressDAO.class ) );
        setMailingListDAO( createMock( "MailingListDAO", MailingListDAO.class ) );
		setMailingListService( new MailingListService() );
		getMailingListService().setStateDAO( getStateDAO() );
		getMailingListService().setMailingAddressDAO( getMailingAddressDAO() );
        getMailingListService().setMailingListDAO( getMailingListDAO() );
	}

	/**
	 * Tears down the mailing list service after testing.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@After
	public final void tearDownMailingListService() {
		setMailingListService(null);
		setMailingAddressDAO( null );
		setStateDAO(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MailingListService#convertToCSV(java.util.List)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testConvertToCSV() {
		final MailingAddress mailingAddress1 = createMailingListEntry("1MailingListEntry");
		final MailingAddress mailingAddress2 = createMailingListEntry("2MailingListEntry");
		final List<MailingAddress> mailingAddress = Arrays.asList( mailingAddress1, mailingAddress2 );
		replayAll();

		final String actualCSV = getMailingListService().convertToCSV( mailingAddress );

		final String expectedCSV = buildExpectedCSV( mailingAddress );
		assertEquals("The CSV is as expected", expectedCSV, actualCSV);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MailingListService#findAllMailingAddress()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testFindAll() {
		final MailingAddress mailingAddress = createMock("MailingListEntry", MailingAddress.class);
		final List<MailingAddress> mailingAddresses = Arrays.asList( mailingAddress );
		EasyMock.expect( getMailingAddressDAO().findAll()).andReturn( mailingAddresses ).anyTimes();
		replayAll();

		final List<MailingAddress> actualMailingAddresses = getMailingListService().findAllMailingAddress();

		assertSame("The mailing list is returned", mailingAddresses, actualMailingAddresses );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MailingListService#findByEmail(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testFindByEmail() {
		final String email = "Email";
		final MailingAddress mailingAddress = createMock("MailingListEntry", MailingAddress.class);
		EasyMock.expect( getMailingAddressDAO().getByEmail(email)).andReturn( mailingAddress ).anyTimes();
		replayAll();

		final MailingAddress actualMailingAddress = getMailingListService().findByEmail(email);

		assertSame("The mailing list entry is returned", mailingAddress, actualMailingAddress );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MailingListService#findByFace(com.bearcode.ovf.model.common.FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testFindByFace() {
		final FaceConfig face = createMock("Face", FaceConfig.class);
		final String urlPath = "url/path";
		EasyMock.expect(face.getUrlPath()).andReturn(urlPath).anyTimes();
		final MailingAddress mailingAddress = createMock("MailingListEntry", MailingAddress.class);
		final List<MailingAddress> mailingAddresses = Arrays.asList( mailingAddress );
		EasyMock.expect( getMailingAddressDAO().getByUrl(urlPath)).andReturn( mailingAddresses ).anyTimes();
		replayAll();

		final List<MailingAddress> actualMailingAddresses = getMailingListService().findByFace(face);

		assertSame("The mailing list is returned", mailingAddresses, actualMailingAddresses );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MailingListService#findStateByAbbreviation(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testFindStateByAbbreviation() {
		final String abbreviation = "AB";
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(abbreviation)).andReturn(state).anyTimes();
		replayAll();

		final State actualState = getMailingListService().findStateByAbbreviation(abbreviation);

		assertSame("The state is returned", state, actualState);
		verifyAll();
	}


	/**
	 * Test method for {@link com.bearcode.ovf.service.MailingListService#saveMailingAddress(com.bearcode.ovf.model.mail.MailingAddress)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testSaveMailingList() {
		final MailingAddress mailingAddress = createMock("MailingListEntry", MailingAddress.class);
		getMailingAddressDAO().makePersistent( mailingAddress );
		replayAll();

		getMailingListService().saveMailingAddress( mailingAddress );

		verifyAll();
	}


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MailingListService#saveToMailingList(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * for the case where the user has an invalid email address.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testSaveToMailingList_invalidUsername() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final boolean forceSignup = false;
		EasyMock.expect(wizardResults.getUsername()).andReturn("invalid username").anyTimes();
        EasyMock.expect( wizardResults.getFieldTypeIfSignUp() ).andReturn( null ).anyTimes();
		replayAll();

		getMailingListService().saveToMailingList(wizardResults);

		verifyAll();
	}


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MailingListService#saveToMailingListIfHasSignup(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testSaveToMailingListIfHasSignup_noSignup() {
		final String username = "valid@username.entry";
		final WizardResultPerson name = createPerson("Name");
		final WizardResultAddress votingAddress = createAddress("VotingAddress");
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		final WizardResultAddress currentAddress = createAddress("CurrentAddress");
		final WizardResults wizardResults = createWizardResults(username, false, name, votingAddress, votingRegion, currentAddress);
        EasyMock.expect( wizardResults.getFieldTypeIfSignUp() ).andReturn( null ).anyTimes();
		replayAll();

		getMailingListService().saveToMailingListIfHasSignup(wizardResults);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MailingListService#saveToMailingListIfHasSignup(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testSaveToMailingListIfHasSignup_signup() {
		final String username = "valid@username.entry";
		final WizardResultPerson name = createPerson("Name");
		final WizardResultAddress votingAddress = createAddress( "VotingAddress" );
		final VotingRegion votingRegion = createMock( "VotingRegion", VotingRegion.class );
		final State state = createMock( "VotingState", State.class );
		final WizardResultAddress currentAddress = createAddress("CurrentAddress");
		final WizardResults wizardResults = createWizardResults(username, true, name, votingAddress, votingRegion, currentAddress);
        final FieldType fieldType = createMock( "FieldType", FieldType.class );
        EasyMock.expect( votingRegion.getState() ).andReturn( state ).anyTimes();
		EasyMock.expect( wizardResults.getFieldTypeIfSignUp() ).andReturn( fieldType ).anyTimes();
		EasyMock.expect( getMailingAddressDAO().getByEmail(username)).andReturn(null).anyTimes();
        final MailingList mailingList = createMock( "MailingList", MailingList.class );
        EasyMock.expect( getMailingListDAO().findByFieldType( fieldType ) ).andReturn( mailingList ).anyTimes();
        EasyMock.expect( getMailingListDAO().findLinkByListAndAddress( EasyMock.eq( mailingList ), EasyMock.<MailingAddress>anyObject() ) ).andReturn( null ).anyTimes();
		getMailingAddressDAO().makePersistent(EasyMock.anyObject());
        getMailingListDAO().makePersistent(EasyMock.anyObject());
		replayAll();

		getMailingListService().saveToMailingListIfHasSignup(wizardResults);

		verifyAll();
	}

	/**
	 * Adds the values from the mailing list entry to the CSV builder.
	 * 
	 * @author IanBrown
	 * @param mailingAddress
	 *            the mailing list entry.
	 * @param csv
	 *            the string builder used to create the CSV.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private void addMailingListEntryToCSV(final MailingAddress mailingAddress, final StringBuilder csv) {
		csv.append("\"").append( mailingAddress.getEmail()).append("\",\"").append( mailingAddress.getFirstName())
				.append("\",\"").append( mailingAddress.getLastName()).append("\",\"")
				.append( mailingAddress.getRegion().getName()).append("\",\"")
				.append( mailingAddress.getRegion().getState().getAbbr()).append("\"");
	}

	/**
	 * Builds the expected CSV for the input mailing list.
	 * 
	 * @author IanBrown
	 * @param mailingAddresses
	 *            the mailing list.
	 * @return the expected CSV.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private String buildExpectedCSV(final List<MailingAddress> mailingAddresses ) {
		final StringBuilder csv = new StringBuilder("");
		String prefix = "";
		for (final MailingAddress mailingAddress : mailingAddresses ) {
			csv.append(prefix);
			prefix = "\n";
			addMailingListEntryToCSV( mailingAddress, csv);
		}
		return csv.toString();
	}

	/**
	 * Creates a wizard results address for the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the address.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private WizardResultAddress createAddress(final String name) {
		final WizardResultAddress address = createMock(name, WizardResultAddress.class);
		EasyMock.expect(address.getStreet1()).andReturn("Street1").anyTimes();
		EasyMock.expect(address.getStreet2()).andReturn("Street2").anyTimes();
		EasyMock.expect(address.getCity()).andReturn("City").anyTimes();
		EasyMock.expect(address.getState()).andReturn("State").anyTimes();
		EasyMock.expect(address.getZip()).andReturn("ZIP").anyTimes();
		EasyMock.expect(address.getZip4()).andReturn("ZIP4").anyTimes();
		EasyMock.expect(address.getCountry()).andReturn("Country").anyTimes();
		return address;
	}

	/**
	 * Creates a mailing list entry with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the mailing list entry.
	 * @return the mailing list entry.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private MailingAddress createMailingListEntry(final String name) {
		final MailingAddress mailingAddress = createMock("MLE" + name, MailingAddress.class);
		EasyMock.expect( mailingAddress.getEmail()).andReturn(name + "@email.address").anyTimes();
		EasyMock.expect( mailingAddress.getFirstName()).andReturn("First" + name).anyTimes();
		EasyMock.expect( mailingAddress.getLastName()).andReturn("Last" + name).anyTimes();
		final VotingRegion region = createMock("Region" + name, VotingRegion.class);
		final String regionName = name + "Region";
		EasyMock.expect(region.getName()).andReturn(regionName).anyTimes();
		final State state = createMock("State" + name, State.class);
		EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		EasyMock.expect(state.getAbbr()).andReturn(name.substring(0, 2)).anyTimes();
		EasyMock.expect( mailingAddress.getRegion()).andReturn(region).anyTimes();
        EasyMock.expect( mailingAddress.getState()).andReturn(state).anyTimes();
		return mailingAddress;
	}

	/**
	 * Creates a person object for the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the person.
	 * @return the person.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private WizardResultPerson createPerson(final String name) {
		final WizardResultPerson person = createMock("Name", WizardResultPerson.class);
		EasyMock.expect(person.getFirstName()).andReturn("First").anyTimes();
		EasyMock.expect(person.getLastName()).andReturn("Last").anyTimes();
		return person;
	}

	/**
	 * Creates a wizard results object for the specified values.
	 * 
	 * @author IanBrown
	 * @param username
	 *            the username.
	 * @param signup
	 *            <code>true</code> if the user wants to sign up, <code>false</code> if the user does not want to sign up.
	 * @param name
	 *            the name.
	 * @param votingAddress
	 *            the voting address.
	 * @param votingRegion
	 *            the voting region.
	 * @param currentAddress
	 *            the current address.
	 * @return the wizard results.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private WizardResults createWizardResults(final String username, final boolean signup, final WizardResultPerson name,
			final WizardResultAddress votingAddress, final VotingRegion votingRegion, final WizardResultAddress currentAddress) {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardResults.getUsername()).andReturn(username).anyTimes();
		EasyMock.expect(wizardResults.hasMailingListSignUp()).andReturn(signup).anyTimes();
		EasyMock.expect(wizardResults.getName()).andReturn(name).anyTimes();
		EasyMock.expect(wizardResults.getBirthYear()).andReturn(2012).anyTimes();
		EasyMock.expect(wizardResults.getVoterType()).andReturn(VoterType.OVERSEAS_VOTER.name()).anyTimes();
		EasyMock.expect(wizardResults.getPhone()).andReturn("phone").anyTimes();
		EasyMock.expect(wizardResults.getFaceUrl()).andReturn("face URL").anyTimes();
		EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).anyTimes();
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		EasyMock.expect(wizardResults.getCurrentAddress()).andReturn(currentAddress).anyTimes();
		EasyMock.expect(wizardResults.getEodRegionId()).andReturn("1").anyTimes();
		EasyMock.expect(wizardResults.getVotingRegionName()).andReturn("Region name").anyTimes();
		EasyMock.expect(wizardResults.getVotingRegionState()).andReturn("SA").anyTimes();
		EasyMock.expect( getStateDAO().getByAbbreviation( "SA" ) ).andReturn( new State() ).anyTimes();
		return wizardResults;
	}

	/**
	 * Gets the mailing list DAO.
	 * 
	 * @author IanBrown
	 * @return the mailing list DAO.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private MailingAddressDAO getMailingAddressDAO() {
		return mailingAddressDAO;
	}

	/**
	 * Gets the mailing list service.
	 * 
	 * @author IanBrown
	 * @return the mailing list service.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private MailingListService getMailingListService() {
		return mailingListService;
	}

	/**
	 * Gets the state DAO.
	 * 
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Sets the mailing list DAO.
	 * 
	 * @author IanBrown
	 * @param mailingAddressDAO
	 *            the mailing list DAO to set.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private void setMailingAddressDAO( final MailingAddressDAO mailingAddressDAO ) {
		this.mailingAddressDAO = mailingAddressDAO;
	}

	/**
	 * Sets the mailing list service.
	 * 
	 * @author IanBrown
	 * @param mailingListService
	 *            the mailing list service to set.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private void setMailingListService(final MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	/**
	 * Sets the state DAO.
	 * 
	 * @author IanBrown
	 * @param stateDAO
	 *            the state DAO to set.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private void setStateDAO(final StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

    public MailingListDAO getMailingListDAO() {
        return mailingListDAO;
    }

    public void setMailingListDAO( MailingListDAO mailingListDAO ) {
        this.mailingListDAO = mailingListDAO;
    }
}
