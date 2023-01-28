package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.questionnaire.*;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class WizardResultsTest {

	@Test
	public final void testWizardResultsFlowType() {
		WizardResults wr = new WizardResults(FlowType.RAVA);
		assertTrue(wr.getFlowType() == FlowType.RAVA);
		assertNotNull(wr.getCreationDate());
		assertTrue(wr.getCreationDate() == wr.getLastChangedDate());
		assertEquals(0, wr.getId());
		assertEquals("", wr.getFaceUrl());
		assertEquals("", wr.getCurrentPageTitle());
		assertNull(wr.getUser());
		assertEquals("", wr.getUsername());
		assertNotNull(wr.getName());
		assertNotNull(wr.getPreviousName());

		assertEquals("", wr.getPhone());
		assertEquals("", wr.getAlternatePhone());
		assertEquals("", wr.getAlternateEmail());

		assertNotNull(wr.getCurrentAddress());
		assertEquals(AddressType.OVERSEAS, wr.getCurrentAddress().getType());

		assertNotNull(wr.getVotingAddress());
		assertEquals(AddressType.STREET, wr.getVotingAddress().getType());
		
		assertNotNull(wr.getForwardingAddress());
		assertEquals(AddressType.OVERSEAS, wr.getForwardingAddress().getType());
		
		assertNotNull(wr.getPreviousAddress());
		assertEquals(AddressType.STREET, wr.getPreviousAddress().getType());
		
		assertNull(wr.getVotingRegion());
		assertEquals("", wr.getVotingRegionName());
		assertEquals("", wr.getVotingRegionState());
		assertEquals("", wr.getVoterType());
		assertEquals("", wr.getVoterHistory());
		
		assertEquals("", wr.getBallotPref());

		assertEquals(0, wr.getBirthDate());
		assertEquals(0, wr.getBirthYear());
		assertEquals(0, wr.getBirthMonth());

		assertEquals("", wr.getRace());
		assertEquals("", wr.getEthnicity());
		assertEquals("", wr.getGender());
		assertEquals("", wr.getParty());
		
		assertFalse(wr.isDownloaded());
		assertFalse(wr.isEmailSent());
	}

	@Test
	public final void testAnonymize() {
		WizardResults wr = createTestWizardResults("anonymize");
		
		assertEquals(0, wr.anonymize().size());
		
		assertEquals("", wr.getUsername());
		assertEquals("", wr.getPhone());
		assertEquals("", wr.getAlternateEmail());
		assertEquals("", wr.getAlternatePhone());
		
		testAnonymizePersonName(wr.getName());
		testAnonymizePersonName(wr.getPreviousName());
		
		testAnonymizePersonAddress(wr.getVotingAddress());
		testAnonymizePersonAddress(wr.getCurrentAddress());
		testAnonymizePersonAddress(wr.getForwardingAddress());
		testAnonymizePersonAddress(wr.getPreviousAddress());
	}
	
	@Test
	public final void testCopyFromTemporary() {
		WizardResults src = createTestWizardResults("src");
		WizardResults dest = createTestWizardResults("dest");
		
		dest.copyFromTemporary(src);
		
		testCopyFromTempNames(src.getName(), dest.getName());
		testCopyFromTempNames(src.getPreviousName(), dest.getPreviousName());

		assertEquals(src.getUsername(), dest.getUsername());		
		assertEquals(src.getPhone(), dest.getPhone());		
		assertEquals(src.getAlternateEmail(), dest.getAlternateEmail());		
		assertEquals(src.getAlternatePhone(), dest.getAlternatePhone());
		
		testCopyFromTempAddresses(src.getVotingAddress(), dest.getVotingAddress());
		testCopyFromTempAddresses(src.getCurrentAddress(), dest.getCurrentAddress());
		testCopyFromTempAddresses(src.getForwardingAddress(), dest.getForwardingAddress());
		testCopyFromTempAddresses(src.getPreviousAddress(), dest.getPreviousAddress());

	}

	@Test
	public final void testCreateTemporary() {
		final WizardResults src = createTestWizardResults("src");
		
		final WizardResults temporary = src.createTemporary();
		
		assertWizardResults(src, temporary);
	}

	@Test
	public final void testPopulateFromFaceConfig() {
        WizardResults wr = createTestWizardResults( "populate" );
        FaceConfig face = createTestFaceConfig();
        wr.populateFromFaceConfig( face );
        assertEquals( "MN", wr.getVotingAddress().getState() );
	}

    @Test
	public final void testPopulateFromUser() {
    	final WizardResults wr = createTestWizardResults( "fromUser" );
    	final OverseasUser user = createTestUser( "user" );
    	
    	wr.populateFromUser(user);
    	
    	assertPopulateFromUser(user, wr);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testPutAnswer_belongsToOther() {
	   	final WizardResults wr = createTestWizardResults("answer");	
	    final Answer answer = EasyMock.createMock("Answer", Answer.class);
	    final WizardResults other = createTestWizardResults("other");
	    EasyMock.expect(answer.getWizardResults()).andReturn(other).atLeastOnce();
	    EasyMock.replay(answer);
	   	
		wr.putAnswer(answer);
	}

	@Test
	public final void testPutAnswer() {
		final long fieldId = 8728l;
	   	final WizardResults wr = createTestWizardResults("answer");	
	    final Answer answer = EasyMock.createMock("Answer", Answer.class);
	    EasyMock.expect(answer.getWizardResults()).andReturn(null);
	    answer.setWizardResults(wr);
	    final QuestionField field = EasyMock.createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).atLeastOnce();
		EasyMock.expect(field.getId()).andReturn(fieldId).atLeastOnce();
		final Answer clone = EasyMock.createMock("Clone", Answer.class);
		EasyMock.expect(answer.clone()).andReturn(clone);
	    EasyMock.replay(answer, field, clone);
	   	
		wr.putAnswer(answer);
		
		final Collection<Answer> answers = wr.getAnswers();
		assertEquals("There is one answer", 1, answers.size());
		assertTrue("The answer was added", answers.contains(clone));
		assertEquals("The answer is for the field ID", clone, wr.getAnswersAsMap().get(fieldId));
		EasyMock.verify(answer, field, clone);
	}

	@Test
	public final void testHasMailingListSignUp_noAnswers() {
	   	final WizardResults wr = createTestWizardResults("None");	

	   	final boolean actualHasSignup = wr.hasMailingListSignUp();
	   	
	   	assertFalse("The wizard results do not have a mailing list signup", actualHasSignup);
	}

	@Test
	public final void testHasMailingListSignUp_noSignup() {
		final long fieldId = 8728l;
	   	final WizardResults wr = createTestWizardResults("NoSignup");	
	    final Answer answer = EasyMock.createMock("Answer", Answer.class);
	    EasyMock.expect(answer.getWizardResults()).andReturn(null);
	    answer.setWizardResults(wr);
	    final QuestionField field = EasyMock.createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).atLeastOnce();
		EasyMock.expect(field.getId()).andReturn(fieldId).atLeastOnce();
		final Answer clone = EasyMock.createMock("Clone", Answer.class);
		EasyMock.expect(answer.clone()).andReturn(clone);
	    EasyMock.expect(clone.getField()).andReturn(field).atLeastOnce();
	    final FieldType fieldType = EasyMock.createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).atLeastOnce();
		EasyMock.expect(fieldType.isMailingListSignUp()).andReturn(false).atLeastOnce();
	    EasyMock.replay(answer, field, clone, fieldType);
	    wr.putAnswer(answer);

	   	final boolean actualHasSignup = wr.hasMailingListSignUp();
	   	
	   	assertFalse("The wizard results do not have a mailing list signup", actualHasSignup);
		EasyMock.verify(answer, field, clone, fieldType);
	}

	@Test
	public final void testHasMailingListSignUp_signup() {
		final long fieldId = 8728l;
	   	final WizardResults wr = createTestWizardResults("NoSignup");	
	    final Answer answer = EasyMock.createMock("Answer", Answer.class);
	    EasyMock.expect(answer.getWizardResults()).andReturn(null);
	    answer.setWizardResults(wr);
	    final QuestionField field = EasyMock.createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).atLeastOnce();
		EasyMock.expect(field.getId()).andReturn(fieldId).atLeastOnce();
		final Answer clone = EasyMock.createMock("Clone", Answer.class);
		EasyMock.expect(answer.clone()).andReturn(clone);
	    EasyMock.expect(clone.getField()).andReturn(field).atLeastOnce();
	    final FieldType fieldType = EasyMock.createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).atLeastOnce();
		EasyMock.expect(fieldType.isMailingListSignUp()).andReturn(true).atLeastOnce();
        EasyMock.expect(clone.getValue()).andReturn("true").atLeastOnce();
	    EasyMock.replay(answer, field, clone, fieldType);
	    wr.putAnswer(answer);

	   	final boolean actualHasSignup = wr.hasMailingListSignUp();
	   	
	   	assertTrue("The wizard results do have a mailing list signup", actualHasSignup);
		EasyMock.verify(answer, field, clone, fieldType);
	}
	
	private WizardResults createTestWizardResults(String prefix) {
		WizardResults wr = new WizardResults(FlowType.RAVA);
		wr.setUsername(prefix + "username");
		wr.setPhone(prefix + "phone");
		wr.setAlternateEmail(prefix + "alternateEmail");
		wr.setAlternatePhone(prefix + "alternatePhone");
		
		WizardResultPerson wrp = createTestPerson(prefix + "Current");
		wr.setName(wrp);
		
		WizardResultPerson wrpp = createTestPerson(prefix + "Previous");
		wr.setPreviousName(wrpp);
		
		WizardResultAddress votingAddress = createTestAddress(prefix + "votingAddress");
		wr.setVotingAddress(votingAddress);
		WizardResultAddress currentAddress = createTestAddress(prefix + "currentAddress");
		wr.setVotingAddress(currentAddress);
		WizardResultAddress forwardingAddress = createTestAddress(prefix + "forwardingAddress");
		wr.setVotingAddress(forwardingAddress);
		WizardResultAddress previousAddress = createTestAddress(prefix + "previousAddress");
		wr.setVotingAddress(previousAddress);
		return wr;
	}

	private WizardResultPerson createTestPerson(String person) {
		WizardResultPerson wrp = new WizardResultPerson();
		wrp.setFirstName(person + "FirstName");
		wrp.setLastName(person + "LastName");
		wrp.setInitial(person + "Initial");
		wrp.setSuffix(person + "Suffix");
		wrp.setTitle(person + "Title");
		return wrp;
	}
	
	private WizardResultAddress createTestAddress(String address) {
		WizardResultAddress wra = new WizardResultAddress();
		wra.setStreet1(address + "Street1");
		wra.setStreet2(address + "Street2");
		wra.setZip4(address + "Zip4");
		wra.setDescription(address + "Description");
		return wra;
	}
	
    private FaceConfig createTestFaceConfig() {
        FaceConfig face = new FaceConfig();
        face.setRelativePrefix( "faces/minnesota" );
        return face;
    }

	private void testAnonymizePersonAddress(WizardResultAddress address) {
		assertEquals("", address.getStreet1());
		assertEquals("", address.getStreet2());
		assertEquals("", address.getZip4());
		assertEquals("", address.getDescription());
	}

	private void testAnonymizePersonName(WizardResultPerson name) {
		assertTrue(name.getFirstName().startsWith("--REMOVED:") && name.getFirstName().endsWith("--"));
		assertTrue(name.getLastName().startsWith("--REMOVED:") && name.getLastName().endsWith("--"));
		assertTrue(name.getInitial().startsWith("--REMOVED:") && name.getInitial().endsWith("--"));
		assertEquals("", name.getSuffix());
		assertEquals("", name.getTitle());
	}

	private void testCopyFromTempAddresses(WizardResultAddress src, WizardResultAddress dest) {
		assertEquals(src.getType(), dest.getType());
		assertEquals(src.getStreet1(), dest.getStreet1());
		assertEquals(src.getStreet2(), dest.getStreet2());
		assertEquals(src.getCity(), dest.getCity());
		assertEquals(src.getState(), dest.getState());
		assertEquals(src.getZip(), dest.getZip());
		assertEquals(src.getZip4(), dest.getZip4());
		assertEquals(src.getCountry(), dest.getCountry());
		assertEquals(src.getCounty(), dest.getCounty());
		assertEquals(src.getDescription(), dest.getDescription());
	}

	private void testCopyFromTempNames(WizardResultPerson srcName, WizardResultPerson destName) {
		assertEquals(srcName.getInitial(), destName.getInitial());
		assertEquals(srcName.getFirstName(), destName.getFirstName());
		assertEquals(srcName.getLastName(), destName.getLastName());
		assertEquals(srcName.getSuffix(), destName.getSuffix());
		assertEquals(srcName.getTitle(), destName.getTitle());
	}

	/**
	 * Custom assertion to ensure that the expected wizard results match the actual.
	 * @author IanBrown
	 * @param expected the expected wizard results.
	 * @param actual the actual wizard results.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private void assertWizardResults(final WizardResults expected, final WizardResults actual) {
		assertEquals(expected.getAlternateEmail(), actual.getAlternateEmail());
		assertEquals(expected.getAlternatePhone(), actual.getAlternatePhone());
		assertEquals(expected.getBallotPref(), actual.getBallotPref());
		assertEquals(expected.getBirthDate(), actual.getBirthDate());
		assertEquals(expected.getBirthMonth(), actual.getBirthMonth());
		assertEquals(expected.getBirthYear(), actual.getBirthYear());
		//assertEquals(expected.getCreationDate(), actual.getCreationDate());
		testCopyFromTempAddresses(expected.getCurrentAddress(), actual.getCurrentAddress());
		assertEquals(expected.getCurrentPageTitle(), actual.getCurrentPageTitle());
		assertEquals(expected.getEthnicity(), actual.getEthnicity());
		assertEquals(expected.getFaceUrl(), actual.getFaceUrl());
		assertEquals(expected.getFlowType(), actual.getFlowType());
		testCopyFromTempAddresses(expected.getForwardingAddress(), actual.getForwardingAddress());
		assertEquals(expected.getGender(), actual.getGender());
		//assertEquals(expected.getLastChangedDate(), actual.getLastChangedDate());
		assertEquals(expected.getMobileDeviceType(), actual.getMobileDeviceType());
		testCopyFromTempNames(expected.getName(), actual.getName());
		assertEquals(expected.getParty(), actual.getParty());
		assertEquals(expected.getPhone(), actual.getPhone());
		testCopyFromTempAddresses(expected.getPreviousAddress(), actual.getPreviousAddress());
		testCopyFromTempNames(expected.getPreviousName(), actual.getPreviousName());
		assertEquals(expected.getRace(), actual.getRace());
		assertEquals(expected.getUsername(), actual.getUsername());
		assertEquals(expected.getVoterHistory(), actual.getVoterHistory());
		assertEquals(expected.getVoterType(), actual.getVoterType());
		testCopyFromTempAddresses(expected.getVotingAddress(), actual.getVotingAddress());
		assertEquals(expected.getVotingRegionName(), actual.getVotingRegionName());
		assertEquals(expected.getVotingRegionState(), actual.getVotingRegionState());
	}

	/**
	 * Creates a test user with the specified prefix.
	 * @author IanBrown
	 * @param prefix the prefix.
	 * @return the test user.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private OverseasUser createTestUser(final String prefix) {
		final OverseasUser user = new OverseasUser();
		user.setAlternateEmail(prefix + "AlternateEmail");
		user.setAlternatePhone(prefix + "AlternatePhone");
		user.setBallotPref(prefix + "BallotPref");
		user.setBirthDate(prefix.hashCode() % 100);
		user.setBirthMonth((prefix.hashCode() / 100) % 100);
		user.setBirthYear((prefix.hashCode() / 10000) % 100);
		user.setCurrentAddress(createTestUserAddress(prefix + "Current", AddressType.OVERSEAS));
		user.setEthnicity(prefix + "Ethnicity");
		user.setForwardingAddress(createTestUserAddress(prefix + "Forwarding", AddressType.RURAL_ROUTE));
		user.setGender(prefix + "Gender");
		user.setName(createTestUserPerson(prefix + "Name"));
		user.setParty(prefix + "Party");
		user.setPhone(prefix + "Phone");
		user.setPreviousAddress(createTestUserAddress(prefix + "Previous", AddressType.MILITARY));
		user.setPreviousName(createTestUserPerson(prefix + "Previous"));
		user.setRace(prefix + "Race");
		user.setUsername(prefix + "Username");
		user.setVoterHistory(VoterHistory.OVERSEAS_AND_DOMESTIC_VOTER);
		user.setVoterType(VoterType.OVERSEAS_VOTER);
		user.setVotingAddress(createTestUserAddress(prefix + "Voting", AddressType.STREET));
        user.setEodRegionId( prefix + "EodRegionId" );
		return user;
	}

	/**
	 * Creates a test voting region with the specified prefix.
	 * @author IanBrown
	 * @param prefix the prefix.
	 * @return the test voting region.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private VotingRegion createTestVotingRegion(final String prefix) {
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setName(prefix + "Name");
		votingRegion.setState(createTestState(prefix));
		return votingRegion;
	}

	/**
	 * Creates a test state with the specified prefix.
	 * @author IanBrown
	 * @param prefix the prefix.
	 * @return the state.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private State createTestState(final String prefix) {
		final State state = new State();
		state.setAbbr(prefix.substring(0,2));
		state.setFipsCode(prefix.hashCode() % 100000);
		state.setName(prefix + "State");
		return state;
	}

	/**
	 * Creates a test person with the specified prefix.
	 * @author IanBrown
	 * @param prefix the prefix.
	 * @return the test person.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private Person createTestUserPerson(final String prefix) {
		final Person person = new Person();
		person.setFirstName(prefix + "First");
		person.setLastName(prefix + "Last");
		person.setMiddleName(prefix + "Middle");
		person.setSuffix(prefix + "Suffix");
		person.setTitle(prefix + "Title");
		return person;
	}

	/**
	 * Creates a test user address with the specified prefix.
	 * @author IanBrown
	 * @param prefix the prefix.
	 * @param type the type of address.
	 * @return the test user address.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private UserAddress createTestUserAddress(final String prefix, AddressType type) {
		final UserAddress userAddress = new UserAddress();
		userAddress.setAddressTo(prefix + "AddressTo");
		userAddress.setCity(prefix + "City");
		userAddress.setCountry(prefix + "Country");
		userAddress.setCounty(prefix + "County");
		userAddress.setDescription(prefix + "Description");
		userAddress.setState(prefix.substring(0,2));
		userAddress.setStreet1(prefix + "Street1");
		userAddress.setStreet2(prefix + "Street2");
		userAddress.setType(type);
		userAddress.setZip(Integer.toString(prefix.hashCode() % 100000));
		userAddress.setZip4(Integer.toString((prefix.hashCode() / 100000) % 10000));
		return userAddress;
	}

	/**
	 * Custom assertion to ensure that the wizard results are populated from the user.
	 * @author IanBrown
	 * @param user the user.
	 * @param wizardResults the wizard results.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private void assertPopulateFromUser(final OverseasUser user, final WizardResults wizardResults) {
		assertEquals(user.getAlternateEmail(), wizardResults.getAlternateEmail());
		assertEquals(user.getAlternatePhone(), wizardResults.getAlternatePhone());
		assertEquals(user.getBirthDate(), wizardResults.getBirthDate());
		assertEquals(user.getBirthMonth(), wizardResults.getBirthMonth());
		assertEquals(user.getBirthYear(), wizardResults.getBirthYear());
		assertPopulateFromUserAddress(user.getCurrentAddress(), wizardResults.getCurrentAddress());
		assertEquals(user.getEthnicity(), wizardResults.getEthnicity());
		assertPopulateFromUserAddress(user.getForwardingAddress(), wizardResults.getForwardingAddress());
		assertEquals(user.getGender(), wizardResults.getGender());
		assertPopulateFromUserPerson(user.getName(), wizardResults.getName());
		assertEquals(user.getParty(), wizardResults.getParty());
		assertEquals(user.getPhone(), wizardResults.getPhone());
		assertPopulateFromUserAddress(user.getPreviousAddress(), wizardResults.getPreviousAddress());
		assertPopulateFromUserPerson(user.getPreviousName(), wizardResults.getPreviousName());
		assertEquals(user.getRace(), wizardResults.getRace());
		assertEquals(user.getUsername(), wizardResults.getUsername());
		assertEquals(user.getVoterHistory().name(), wizardResults.getVoterHistory());
		assertEquals(user.getVoterType().name(), wizardResults.getVoterType());
		assertPopulateFromUserAddress(user.getVotingAddress(), wizardResults.getVotingAddress());
		assertEquals( user.getEodRegionId(), wizardResults.getEodRegionId() );
	}

	/**
	 * Custom assertion to ensure that the wizard result person is populated from the user person.
	 * @author IanBrown
	 * @param userPerson the user person.
	 * @param wizardResultPerson the wizard result person.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private void assertPopulateFromUserPerson(Person userPerson, WizardResultPerson wizardResultPerson) {
		assertEquals(userPerson.getFirstName(), wizardResultPerson.getFirstName());
		assertEquals(userPerson.getLastName(), wizardResultPerson.getLastName());
		assertEquals(userPerson.getMiddleName(), wizardResultPerson.getMiddleName());
		assertEquals(userPerson.getSuffix(), wizardResultPerson.getSuffix());
		assertEquals(userPerson.getTitle(), wizardResultPerson.getTitle());
	}

	/**
	 * Custom assertion to ensure that the wizard results answer is populated from the user address.
	 * @author IanBrown
	 * @param userAddress
	 * @param wizardResultAddress
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private void assertPopulateFromUserAddress(final UserAddress userAddress, final WizardResultAddress wizardResultAddress) {
		//assertEquals(userAddress.getAddressTo(), wizardResultAddress.getAddressTo());
		assertEquals(userAddress.getCity(), wizardResultAddress.getCity());
		assertEquals(userAddress.getCountry(), wizardResultAddress.getCountry());
		assertEquals(userAddress.getCounty(), wizardResultAddress.getCounty());
		assertEquals(userAddress.getDescription(), wizardResultAddress.getDescription());
		assertEquals(userAddress.getState(), wizardResultAddress.getState());
		assertEquals(userAddress.getStreet1(), wizardResultAddress.getStreet1());
		assertEquals(userAddress.getStreet2(), wizardResultAddress.getStreet2());
		assertEquals(userAddress.getType(), wizardResultAddress.getType());
		assertEquals(userAddress.getZip(), wizardResultAddress.getZip());
		assertEquals(wizardResultAddress.getZip4(), wizardResultAddress.getZip4());
	}
}
