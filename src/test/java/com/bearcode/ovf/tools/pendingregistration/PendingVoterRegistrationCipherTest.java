package com.bearcode.ovf.tools.pendingregistration;

import com.bearcode.ovf.model.common.PhoneNumberType;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAddress;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAnswer;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.service.EncryptionService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Date: 24.10.14
 * Time: 17:01
 *
 * @author Leonid Ginzburg
 */
public class PendingVoterRegistrationCipherTest  extends EasyMockSupport {

    private PendingVoterRegistrationCipher pendingVoterRegistrationCipher;

    private EncryptionService encryptionService;

    @Before
    public void setUp() throws Exception {
        setPendingVoterRegistrationCipher(new PendingVoterRegistrationCipher() );
        setEncryptionService(createMock("EncryptionService", EncryptionService.class));

        ReflectionTestUtils.setField(getPendingVoterRegistrationCipher(), "encryptionService", getEncryptionService());
    }

    @After
    public void tearDown() throws Exception {
        setPendingVoterRegistrationCipher( null );
        setEncryptionService( null );
    }

    /**
   	 * Test method for
   	 * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationCipher#decryptAll(com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration)}
   	 *
   	 * @author IanBrown
   	 *
   	 * @throws Exception
   	 *             if there is a problem with the decryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 8, 2012
   	 */
   	@Test
   	public final void testDecrypt() throws Exception {
   		final PendingVoterRegistration pendingVoterRegistration = setUpRegistrationForDecryption();
   		replayAll();

   		getPendingVoterRegistrationCipher().decryptAll(pendingVoterRegistration);

   		verifyAll();
   	}

    /**
   	 * Test method for
   	 * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationCipher#encryptAll(com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration)}
   	 * .
   	 *
   	 * @author IanBrown
   	 *
   	 * @throws Exception
   	 *             if there is a problem setting up the encryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 7, 2012
   	 */
   	@Test
   	public final void testEncrypt() throws Exception {
   		final PendingVoterRegistration pendingVoterRegistration = setUpRegistrationToEncrypt();
   		replayAll();

   		getPendingVoterRegistrationCipher().encryptAll(pendingVoterRegistration);

   		verifyAll();
   	}

    @Test
    public final void testCheckDataForLength_fromEnd() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"First line\"\n");
        sb.append("\"Too long second line ");
        while ( sb.length() <= 238 ) {
            sb.append(".");
        }
        sb.append("\"\n\"Final line\"\n");

        getPendingVoterRegistrationCipher().checkDataForLength( sb, true );

        assertTrue("Buffer length after truncation should be less than 245.", sb.length() < 245 );
        assertTrue("Buffer should contain first line.", sb.indexOf( "\"First line\"") >= 0 );
        assertTrue("Buffer should not contain final line.", sb.indexOf( "\"Final line\"") == -1 );
    }

    @Test
    public final void testCheckDataForLength_fromStart() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"First line\"\n");
        sb.append("\"Too long second line ");
        while ( sb.length() <= 238 ) {
            sb.append(".");
        }
        sb.append("\"\n\"Final line\"\n");

        getPendingVoterRegistrationCipher().checkDataForLength( sb, false );

        assertTrue("Buffer length after truncation should be less than 245.", sb.length() < 245 );
        assertTrue("Buffer should contain final line.", sb.indexOf( "\"Final line\"") >= 0 );
        assertTrue("Buffer should not contain first line.", sb.indexOf( "\"First line\"") == -1 );
    }

    /**
    	 * Adds to the string that is returned as "decrypted".
    	 *
    	 * @author IanBrown
    	 * @param name
    	 *            the name of a name=value pair.
    	 * @param value
    	 *            the value of a name=value pair.
    	 * @param sb
    	 *            the string builder.
    	 * @since Nov 5, 2012
    	 * @version Nov 5, 2012
    	 */
    	private void addToDecryptedString(final String name, final String value, final StringBuilder sb) {
    		sb.append(PendingVoterRegistrationCipher.quoteString(name)).append('=')
    		.append(PendingVoterRegistrationCipher.quoteString(value)).append("\n");
    	}

    /**
   	 * Sets up a pending voter address to be decrypted.
   	 *
   	 * @author IanBrown
   	 * @param createdDate
   	 *            the date the pending voter registration was created.
   	 * @param votingState
   	 *            the voting state.
   	 * @param votingRegion
   	 *            the voting region.
   	 * @param name
   	 *            the name of the address.
   	 * @return the pending voter address.
   	 * @throws Exception
   	 *             if there is a problem setting up the decryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 8, 2012
   	 */
   	private PendingVoterAddress setUpAddressForDecryption(final Date createdDate, final String votingState,
   			final String votingRegion, final String name) throws Exception {
   		final PendingVoterAddress pendingVoterAddress = createMock(name, PendingVoterAddress.class);
   		final String street1 = "1 " + name + " Street";
   		final String street2 = "2 " + name;
   		final String description = "Description of " + name;
   		final String city = name + " City";
   		final String stateOrRegion = name.substring(0, 2).toUpperCase();
   		final String postalCode = Integer.toString(name.hashCode());
   		final String country = "Country of " + name;
   		final String encryptedString = "Encrypted string for " + name;
   		EasyMock.expect(pendingVoterAddress.getEncrypted()).andReturn(encryptedString.getBytes());
   		final StringBuilder sb = new StringBuilder();
   		addToDecryptedString(PendingVoterRegistrationDictionary.STREET1, street1, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.STREET2, street2, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.DESCRIPTION, description, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.CITY, city, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.STATE_OR_REGION, stateOrRegion, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.POSTAL_CODE, postalCode, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.COUNTRY, country, sb);
   		EasyMock.expect(
   				getEncryptionService().makeDecryption(EasyMock.eq(createdDate), EasyMock.eq(votingState), EasyMock.eq(votingRegion),
                           EasyMock.aryEq(encryptedString.getBytes()))).andReturn(sb.toString());
   		pendingVoterAddress.setStreet1(street1);
   		pendingVoterAddress.setStreet2(street2);
   		pendingVoterAddress.setDescription(description);
   		pendingVoterAddress.setCity(city);
   		pendingVoterAddress.setStateOrRegion(stateOrRegion);
   		pendingVoterAddress.setPostalCode(postalCode);
   		pendingVoterAddress.setCountry(country);
   		return pendingVoterAddress;
   	}

   	/**
   	 * Sets up an address to get encrypted.
   	 *
   	 * @author IanBrown
   	 * @param sourceDate
   	 *            the date for the source.
   	 * @param name
   	 *            the name of the address to set up.
   	 * @return the pending voter address.
   	 * @throws Exception
   	 *             if there is a problem setting up the encryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 7, 2012
   	 */
   	private PendingVoterAddress setUpAddressToEncrypt(final Date sourceDate, final String name) throws Exception {
   		final PendingVoterAddress pendingVoterAddress = createMock(name, PendingVoterAddress.class);
   		final String street1 = "1 " + name + " Ave";
   		EasyMock.expect(pendingVoterAddress.getStreet1()).andReturn(street1);
   		final String street2 = "2 " + name;
   		EasyMock.expect(pendingVoterAddress.getStreet2()).andReturn(street2);
   		final String description = "Description of " + name;
   		EasyMock.expect(pendingVoterAddress.getDescription()).andReturn(description);
   		final String city = name + " City";
   		EasyMock.expect(pendingVoterAddress.getCity()).andReturn(city);
   		final String stateOrRegion = name.substring(0, 2).toUpperCase();
   		EasyMock.expect(pendingVoterAddress.getStateOrRegion()).andReturn(stateOrRegion);
   		final String postalCode = Integer.toString(name.hashCode());
   		EasyMock.expect(pendingVoterAddress.getPostalCode()).andReturn(postalCode);
   		final String country = "Country of " + name;
   		EasyMock.expect(pendingVoterAddress.getCountry()).andReturn(country);
   		final String encryptedString = "Encrypted string for " + name;
   		EasyMock.expect(
   				getEncryptionService().makeEncryption((Date) EasyMock.anyObject(), (String) EasyMock.anyObject(),
                           (String) EasyMock.anyObject(), (String) EasyMock.anyObject())).andDelegateTo(new EncryptionService() {
   							@Override
   							public byte[] makeEncryption(final Date creationDate, final String state, final String votingRegion, final String string) {
   								assertEquals("The creation date is correct", sourceDate, creationDate);
   								assertTrue("The string contains the street1 line", string.contains(street1));
   								assertTrue("The string contains the street2 line", string.contains(street2));
   								assertTrue("The string contains the description line", string.contains(description));
   								assertTrue("The string contains the city", string.contains(city));
   								assertTrue("The string contains the state or region", string.contains(stateOrRegion));
   								assertTrue("The string contains the postal code", string.contains(postalCode));
   								assertTrue("The string contains the country", string.contains(country));
   								return encryptedString.getBytes();
   							}
   						});
   		pendingVoterAddress.setStreet1(null);
   		pendingVoterAddress.setStreet2(null);
   		pendingVoterAddress.setDescription(null);
   		pendingVoterAddress.setCity(null);
   		pendingVoterAddress.setStateOrRegion(null);
   		pendingVoterAddress.setPostalCode(null);
   		pendingVoterAddress.setCountry(null);
   		pendingVoterAddress.setEncrypted(EasyMock.aryEq(encryptedString.getBytes()));
   		return pendingVoterAddress;
   	}

   	/**
   	 * Sets up a pending voter answer for decryption.
   	 *
   	 * @author IanBrown
   	 * @param createdDate
   	 *            the date the pending voter registration was created.
   	 * @param votingState
   	 *            the voting state.
   	 * @param votingRegion
   	 *            the voting region.
   	 * @param name
   	 *            the name of the answer.
   	 * @return the pending voter answer.
   	 * @throws Exception
   	 *             if there is a problem setting up the decryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 8, 2012
   	 */
   	private PendingVoterAnswer setUpAnswerForDecryption(final Date createdDate, final String votingState,
   			final String votingRegion, final String name) throws Exception {
   		final PendingVoterAnswer pendingVoterAnswer = createMock(name, PendingVoterAnswer.class);
   		final String question = "Question for " + name;
   		final String answer = "Answer for " + name;
   		final String encryptedString = "Encrypted string for " + name;
   		EasyMock.expect(pendingVoterAnswer.getEncrypted()).andReturn(encryptedString.getBytes());
   		final StringBuilder sb = new StringBuilder();
   		addToDecryptedString(question, answer, sb);
   		EasyMock.expect(
   				getEncryptionService().makeDecryption(EasyMock.eq(createdDate), EasyMock.eq(votingState), EasyMock.eq(votingRegion),
                           EasyMock.aryEq(encryptedString.getBytes()))).andReturn(sb.toString());
   		pendingVoterAnswer.setQuestion(question);
   		pendingVoterAnswer.setAnswer(answer);
   		return pendingVoterAnswer;
   	}

   	/**
   	 * Sets up an answer to get encrypted.
   	 *
   	 * @author IanBrown
   	 * @param sourceDate
   	 *            the date for the source.
   	 * @param name
   	 *            the name of the answer.
   	 * @return the pending voter answer.
   	 * @throws Exception
   	 *             if there is a problem setting up the encryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 7, 2012
   	 */
   	private PendingVoterAnswer setUpAnswerToEncrypt(final Date sourceDate, final String name) throws Exception {
   		final PendingVoterAnswer pendingVoterAnswer = createMock(name, PendingVoterAnswer.class);
   		final String question = "Question for " + name;
   		EasyMock.expect(pendingVoterAnswer.getQuestion()).andReturn(question);
   		final String answer = "Answer for " + name;
   		EasyMock.expect(pendingVoterAnswer.getAnswer()).andReturn(answer);
   		final String encryptedString = "Encrypted string for " + name;
   		EasyMock.expect(
   				getEncryptionService().makeEncryption((Date) EasyMock.anyObject(), (String) EasyMock.anyObject(),
                           (String) EasyMock.anyObject(), (String) EasyMock.anyObject())).andDelegateTo(new EncryptionService() {
   							@Override
   							public byte[] makeEncryption(final Date creationDate, final String state, final String votingRegion, final String string) {
   								assertEquals("The creation date is correct", sourceDate, creationDate);
   								assertTrue("The string contains the question", string.contains(question));
   								assertTrue("The string contains the answer", string.contains(answer));
   								return encryptedString.getBytes();
   							}
   						});
   		pendingVoterAnswer.setQuestion(null);
   		pendingVoterAnswer.setAnswer(null);
   		pendingVoterAnswer.setEncrypted(EasyMock.aryEq(encryptedString.getBytes()));
   		return pendingVoterAnswer;
   	}

   	/**
   	 * Creates a name for decryption.
   	 *
   	 * @author IanBrown
   	 * @param createdDate
   	 *            the date the pending voter registration was created.
   	 * @param votingState
   	 *            the voting state.
   	 * @param votingRegion
   	 *            the voting region.
   	 * @param name
   	 *            the name of the name.
   	 * @return the pending voter name.
   	 * @throws Exception
   	 *             if there is a problem setting up the decryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 8, 2012
   	 */
   	private PendingVoterName setUpNameForDecryption(final Date createdDate, final String votingState, final String votingRegion,
   			final String name) throws Exception {
   		final PendingVoterName pendingVoterName = createMock(name, PendingVoterName.class);
   		final String title = "Title";
   		final String firstName = "First" + name;
   		final String middleName = "Middle" + name;
   		final String lastName = "Last" + name;
   		final String suffix = "Suffix";
   		final String encryptedString = "Encrypted string for " + name;
   		EasyMock.expect(pendingVoterName.getEncrypted()).andReturn(encryptedString.getBytes());
   		final StringBuilder sb = new StringBuilder();
   		addToDecryptedString(PendingVoterRegistrationDictionary.TITLE, title, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.FIRST_NAME, firstName, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.MIDDLE_NAME, middleName, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.LAST_NAME, lastName, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.SUFFIX, suffix, sb);
   		EasyMock.expect(
   				getEncryptionService().makeDecryption(EasyMock.eq(createdDate), EasyMock.eq(votingState), EasyMock.eq(votingRegion),
                           EasyMock.aryEq(encryptedString.getBytes()))).andReturn(sb.toString());
   		pendingVoterName.setTitle(title);
   		pendingVoterName.setFirstName(firstName);
   		pendingVoterName.setMiddleName(middleName);
   		pendingVoterName.setLastName(lastName);
   		pendingVoterName.setSuffix(suffix);
   		return pendingVoterName;
   	}

   	/**
   	 * Sets up a name to get encrypted.
   	 *
   	 * @author IanBrown
   	 * @param sourceDate
   	 *            the date for the source.
   	 * @param name
   	 *            the name to set up.
   	 * @return the pending voter name.
   	 * @throws Exception
   	 *             if there is a problem setting up the encryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 7, 2012
   	 */
   	private PendingVoterName setUpNameToEncrypt(final Date sourceDate, final String name) throws Exception {
   		final PendingVoterName pendingVoterName = createMock(name, PendingVoterName.class);
   		final String title = "Title";
   		EasyMock.expect(pendingVoterName.getTitle()).andReturn(title);
   		final String firstName = "First" + name;
   		EasyMock.expect(pendingVoterName.getFirstName()).andReturn(firstName);
   		final String middleName = "Middle" + name;
   		EasyMock.expect(pendingVoterName.getMiddleName()).andReturn(middleName);
   		final String lastName = "Last" + name;
   		EasyMock.expect(pendingVoterName.getLastName()).andReturn(lastName);
   		final String suffix = "Suffix";
   		EasyMock.expect(pendingVoterName.getSuffix()).andReturn(suffix);
   		final String encryptedString = "Encrypted string for " + name;
   		EasyMock.expect(
   				getEncryptionService().makeEncryption((Date) EasyMock.anyObject(), (String) EasyMock.anyObject(),
                           (String) EasyMock.anyObject(), (String) EasyMock.anyObject())).andDelegateTo(new EncryptionService() {
   							@Override
   							public byte[] makeEncryption(final Date creationDate, final String state, final String votingRegion, final String string) {
   								assertEquals("The creation date is correct", sourceDate, creationDate);
   								assertTrue("The string contains the title", string.contains(title));
   								assertTrue("The string contains the first name", string.contains(firstName));
   								assertTrue("The string contains the middle name", string.contains(middleName));
   								assertTrue("The string contains the last name", string.contains(lastName));
   								assertTrue("The string contains the suffix", string.contains(suffix));
   								return encryptedString.getBytes();
   							}
   						});
   		pendingVoterName.setTitle(null);
   		pendingVoterName.setFirstName(null);
   		pendingVoterName.setMiddleName(null);
   		pendingVoterName.setLastName(null);
   		pendingVoterName.setSuffix(null);
   		pendingVoterName.setEncrypted(EasyMock.aryEq(encryptedString.getBytes()));
   		return pendingVoterName;
   	}

   	/**
   	 * Sets up a pending voter registration to be decrypted.
   	 *
   	 * @author IanBrown
   	 * @return the pending voter registration.
   	 * @throws Exception
   	 *             if there is a problem setting up the decryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 8, 2012
   	 */
   	private PendingVoterRegistration setUpRegistrationForDecryption() throws Exception {
   		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
   				PendingVoterRegistration.class);
   		final String votingState = "VS";
   		EasyMock.expect(pendingVoterRegistration.getVotingState()).andReturn(votingState);
   		final String votingRegion = "Voting Region";
   		EasyMock.expect(pendingVoterRegistration.getVotingRegion()).andReturn(votingRegion);
   		final Date createdDate = new Date();
   		EasyMock.expect(pendingVoterRegistration.getCreatedDate()).andReturn(createdDate);
   		final String emailAddress = "email@address.com";
   		final String alternateEmailAddress = "alternate@email.address.com";
   		final String phoneNumber = "0987654321";
		final String phoneNumberType = PhoneNumberType.Mobile.name();
   		final String alternatePhoneNumber = "9876543210";
   		final String birthDay = "11/5/2012";
   		final String encryptedString = "Encrypted String";
   		EasyMock.expect(pendingVoterRegistration.getEncrypted()).andReturn(encryptedString.getBytes());
   		final StringBuilder sb = new StringBuilder();
   		addToDecryptedString(PendingVoterRegistrationDictionary.EMAIL_ADDRESS, emailAddress, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.ALTERNATE_EMAIL_ADDRESS, alternateEmailAddress, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.PHONE_NUMBER, phoneNumber, sb);
		addToDecryptedString(PendingVoterRegistrationDictionary.PHONE_NUMBER_TYPE, phoneNumberType, sb);
   		addToDecryptedString(PendingVoterRegistrationDictionary.ALTERNATE_PHONE_NUMBER, alternatePhoneNumber, sb);
        addToDecryptedString(PendingVoterRegistrationDictionary.ALTERNATE_PHONE_NUMBER_TYPE, phoneNumberType, sb);
  		addToDecryptedString(PendingVoterRegistrationDictionary.BIRTH_DATE, birthDay, sb);
   		EasyMock.expect(
   				getEncryptionService().makeDecryption(EasyMock.eq(createdDate), EasyMock.eq(votingState), EasyMock.eq(votingRegion),
                           EasyMock.aryEq(encryptedString.getBytes()))).andReturn( sb.toString() );
   		pendingVoterRegistration.setEmailAddress( emailAddress );
   		pendingVoterRegistration.setAlternateEmailAddress( alternateEmailAddress );
   		pendingVoterRegistration.setPhoneNumber( phoneNumber );
        pendingVoterRegistration.setPhoneNumberType( phoneNumberType );
   		pendingVoterRegistration.setAlternatePhoneNumber( alternatePhoneNumber );
        pendingVoterRegistration.setAlternatePhoneNumberType( phoneNumberType );
        pendingVoterRegistration.setBirthDay(birthDay);
   		EasyMock.expect(pendingVoterRegistration.getPreviousName()).andReturn(
   				setUpNameForDecryption(createdDate, votingState, votingRegion, "PreviousName"));
   		EasyMock.expect(pendingVoterRegistration.getVotingAddress()).andReturn(
   				setUpAddressForDecryption(createdDate, votingState, votingRegion, "VotingAddress"));
   		EasyMock.expect(pendingVoterRegistration.getCurrentAddress()).andReturn(
   				setUpAddressForDecryption(createdDate, votingState, votingRegion, "CurrentAddress"));
   		EasyMock.expect(pendingVoterRegistration.getForwardingAddress()).andReturn(
   				setUpAddressForDecryption(createdDate, votingState, votingRegion, "ForwardingAddress"));
   		EasyMock.expect(pendingVoterRegistration.getPreviousAddress()).andReturn(null);
   		final PendingVoterAnswer pendingVoterAnswer1 = setUpAnswerForDecryption(createdDate, votingState, votingRegion, "Answer1");
   		final PendingVoterAnswer pendingVoterAnswer2 = setUpAnswerForDecryption(createdDate, votingState, votingRegion, "Answer2");
   		final List<PendingVoterAnswer> pendingVoterAnswers = Arrays.asList(pendingVoterAnswer1, pendingVoterAnswer2);
   		EasyMock.expect(pendingVoterRegistration.getAnswers()).andReturn(pendingVoterAnswers);
   		return pendingVoterRegistration;
   	}

   	/**
   	 * Sets up a registration for encryption.
   	 *
   	 * @author IanBrown
   	 * @return the pending voter registration.
   	 * @throws Exception
   	 *             if there is a problem setting up the encryption.
   	 * @since Nov 5, 2012
   	 * @version Nov 7, 2012
   	 */
   	private PendingVoterRegistration setUpRegistrationToEncrypt() throws Exception {
   		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
   				PendingVoterRegistration.class);
   		final String state = "ST";
   		EasyMock.expect(pendingVoterRegistration.getVotingState()).andReturn(state);
   		final String votingRegion = "Voting Region";
   		EasyMock.expect(pendingVoterRegistration.getVotingRegion()).andReturn(votingRegion);
   		final String emailAddress = "email@address.com";
   		EasyMock.expect(pendingVoterRegistration.getEmailAddress()).andReturn(emailAddress);
   		final String alternateEmailAddress = "alternate@email.address.com";
   		EasyMock.expect(pendingVoterRegistration.getAlternateEmailAddress()).andReturn(alternateEmailAddress);
   		final String phoneNumber = "3456789012";
   		EasyMock.expect(pendingVoterRegistration.getPhoneNumber()).andReturn(phoneNumber);
   		final String phoneNumberType = PhoneNumberType.Mobile.name();
		EasyMock.expect( pendingVoterRegistration.getPhoneNumberType() ).andReturn( phoneNumberType );
        EasyMock.expect( pendingVoterRegistration.getAlternatePhoneNumberType() ).andReturn( phoneNumberType );
		final String alternatePhoneNumber = "4567890123";
   		EasyMock.expect(pendingVoterRegistration.getAlternatePhoneNumber()).andReturn(alternatePhoneNumber);
   		final String birthDay = "11/5/2012";
   		EasyMock.expect(pendingVoterRegistration.getBirthDay()).andReturn(birthDay);
   		final Date sourceDate = new Date();
   		EasyMock.expect(pendingVoterRegistration.getCreatedDate()).andReturn(sourceDate);
   		final String encryptedString = "Encrypted String";
   		EasyMock.expect(
   				getEncryptionService().makeEncryption((Date) EasyMock.anyObject(), (String) EasyMock.anyObject(),
                           (String) EasyMock.anyObject(), (String) EasyMock.anyObject())).andDelegateTo(new EncryptionService() {
   							@Override
   							public byte[] makeEncryption(final Date creationDate, final String state, final String votingRegion, final String string) {
   								assertEquals("The creation date is correct", sourceDate, creationDate);
   								assertTrue("The string contains the email address", string.contains(emailAddress));
   								assertTrue("The string contains the alternate email address", string.contains(alternateEmailAddress));
   								assertTrue("The string contains the phone number", string.contains(phoneNumber));
   								assertTrue("The string contains the alternate phone number", string.contains(alternatePhoneNumber));
   								assertTrue("The string contains the birth day", string.contains(birthDay));
   								return encryptedString.getBytes();
   							}
   						});
   		pendingVoterRegistration.setEmailAddress(null);
   		pendingVoterRegistration.setAlternateEmailAddress(null);
   		pendingVoterRegistration.setPhoneNumber(null);
   		pendingVoterRegistration.setAlternatePhoneNumber(null);
   		pendingVoterRegistration.setEncrypted(EasyMock.aryEq(encryptedString.getBytes()));
   		pendingVoterRegistration.setBirthDay(null);
   		EasyMock.expect(pendingVoterRegistration.getPreviousName()).andReturn(setUpNameToEncrypt(sourceDate, "PreviousName"));
   		EasyMock.expect(pendingVoterRegistration.getVotingAddress()).andReturn(setUpAddressToEncrypt(sourceDate, "VotingAddress"));
   		EasyMock.expect(pendingVoterRegistration.getCurrentAddress())
   		.andReturn(setUpAddressToEncrypt(sourceDate, "CurrentAddress"));
   		EasyMock.expect(pendingVoterRegistration.getForwardingAddress()).andReturn(
   				setUpAddressToEncrypt(sourceDate, "ForwardingAddress"));
   		EasyMock.expect(pendingVoterRegistration.getPreviousAddress()).andReturn(null);
   		final PendingVoterAnswer answer1 = setUpAnswerToEncrypt(sourceDate, "Answer1");
   		final PendingVoterAnswer answer2 = setUpAnswerToEncrypt(sourceDate, "Answer2");
   		final List<PendingVoterAnswer> answers = Arrays.asList(answer1, answer2);
   		EasyMock.expect(pendingVoterRegistration.getAnswers()).andReturn(answers);
   		return pendingVoterRegistration;
   	}

    public PendingVoterRegistrationCipher getPendingVoterRegistrationCipher() {
        return pendingVoterRegistrationCipher;
    }

    public void setPendingVoterRegistrationCipher(PendingVoterRegistrationCipher pendingVoterRegistrationCipher) {
        this.pendingVoterRegistrationCipher = pendingVoterRegistrationCipher;
    }

    public EncryptionService getEncryptionService() {
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
}
