package com.bearcode.ovf.tools.pendingregistration;

import com.bearcode.ovf.model.pendingregistration.PendingVoterAddress;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAnswer;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.service.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Date: 20.10.14
 * Time: 21:59
 *
 * @author Leonid Ginzburg
 */
@Component
public class PendingVoterRegistrationCipher implements PendingVoterRegistrationDictionary {

    private static Logger logger = LoggerFactory.getLogger(PendingVoterRegistrationCipher.class);

    @Autowired
    private EncryptionService encryptionService;

     /*=====================  Decryption  ========================================================*/
    /**
     * Decrypts the pending voter registration object.
     *
     * @author IanBrown
     * @param pendingVoterRegistration
     *            the pending voter registration object.
     * @throws Exception
     *             if there is a problem performing the decryption.
     * @since Nov 5, 2012
     * @version Nov 8, 2012
     */
    public boolean decryptAll(final PendingVoterRegistration pendingVoterRegistration) throws Exception {
        final Date createdDate = pendingVoterRegistration.getCreatedDate();
        final String votingState = pendingVoterRegistration.getVotingState();
        final String votingRegion = pendingVoterRegistration.getVotingRegion();
        try {
            final String decryptedString = getEncryptionService().makeDecryption(createdDate, votingState, votingRegion,
                    pendingVoterRegistration.getEncrypted());
            final Map<String, String> parsedString = parseDecryptedString(decryptedString);
            pendingVoterRegistration.setEmailAddress(parsedString.get(EMAIL_ADDRESS));
            pendingVoterRegistration.setAlternateEmailAddress( parsedString.get( ALTERNATE_EMAIL_ADDRESS ) );
            pendingVoterRegistration.setPhoneNumber( parsedString.get( PHONE_NUMBER ) );
            pendingVoterRegistration.setPhoneNumberType( parsedString.get( PHONE_NUMBER_TYPE ) );
            pendingVoterRegistration.setAlternatePhoneNumber(parsedString.get(ALTERNATE_PHONE_NUMBER));
            pendingVoterRegistration.setAlternatePhoneNumberType(parsedString.get(ALTERNATE_PHONE_NUMBER_TYPE));
            pendingVoterRegistration.setBirthDay(parsedString.get(BIRTH_DATE));
            decryptName(createdDate, votingState, votingRegion, pendingVoterRegistration.getPreviousName());
            decryptAddress(createdDate, votingState, votingRegion, pendingVoterRegistration.getVotingAddress());
            decryptAddress(createdDate, votingState, votingRegion, pendingVoterRegistration.getCurrentAddress());
            decryptAddress(createdDate, votingState, votingRegion, pendingVoterRegistration.getForwardingAddress());
            decryptAddress(createdDate, votingState, votingRegion, pendingVoterRegistration.getPreviousAddress());
            decryptAnswers(createdDate, votingState, votingRegion, pendingVoterRegistration.getAnswers());
        } catch (Exception e) {
            logger.warn("Could not decrypt pending voter registration id #" + pendingVoterRegistration.getId());
            return false;
        }
        return true;
    }

    /**
     * Decrypts the pending voter address.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param votingState
     *            the voting state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterAddress
     *            the pending voter address.
     * @throws Exception
     *             if there is a problem with the decryption.
     * @since Nov 5, 2012
     * @version Nov 8, 2012
     */
    private void decryptAddress(final Date createdDate, final String votingState, final String votingRegion,
                                final PendingVoterAddress pendingVoterAddress) throws Exception {
        if (pendingVoterAddress == null) {
            return;
        }

        final String decryptedString = getEncryptionService().makeDecryption(createdDate, votingState, votingRegion,
                pendingVoterAddress.getEncrypted());
        final Map<String, String> parsedString = parseDecryptedString(decryptedString);
        pendingVoterAddress.setStreet1(parsedString.get(STREET1));
        pendingVoterAddress.setStreet2(parsedString.get(STREET2));
        pendingVoterAddress.setDescription(parsedString.get(DESCRIPTION));
        pendingVoterAddress.setCity(parsedString.get(CITY));
        pendingVoterAddress.setStateOrRegion(parsedString.get(STATE_OR_REGION));
        pendingVoterAddress.setPostalCode(parsedString.get(POSTAL_CODE));
        pendingVoterAddress.setCountry(parsedString.get(COUNTRY));
    }

    /**
     * Decrypts the pending voter answer.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param votingState
     *            the voting state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterAnswer
     *            the pending voter answer.
     * @throws Exception
     *             if there is a problem with the decryption.
     * @since Nov 5, 2012
     * @version Nov 15, 2012
     */
    private void decryptAnswer(final Date createdDate, final String votingState, final String votingRegion,
                               final PendingVoterAnswer pendingVoterAnswer) throws Exception {
        final String decryptedString = getEncryptionService().makeDecryption(createdDate, votingState, votingRegion,
                pendingVoterAnswer.getEncrypted());
        final Map<String, String> parsedString = parseDecryptedString(decryptedString);
        final Iterator<Map.Entry<String, String>> answerItr = parsedString.entrySet().iterator();
        if (answerItr.hasNext()) {
            final Map.Entry<String, String> answerEntry = answerItr.next();
            pendingVoterAnswer.setQuestion(answerEntry.getKey());
            pendingVoterAnswer.setAnswer(answerEntry.getValue());
        }
    }

    /**
     * Decrypts the answers.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param votingState
     *            the voting state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterAnswers
     *            the pending voter answers to decrypt.
     * @throws Exception
     *             if there is a problem with the decryption.
     * @since Nov 5, 2012
     * @version Nov 8, 2012
     */
    private void decryptAnswers(final Date createdDate, final String votingState, final String votingRegion,
                                final List<PendingVoterAnswer> pendingVoterAnswers) throws Exception {
        for (final PendingVoterAnswer pendingVoterAnswer : pendingVoterAnswers) {
            decryptAnswer(createdDate, votingState, votingRegion, pendingVoterAnswer);
        }
    }

    /**
     * Decrypts the pending voter name object.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param votingState
     *            the voting state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterName
     *            the pending voter name object.
     * @throws Exception
     *             if there is a problem performing the decryption.
     * @since Nov 5, 2012
     * @version Nov 8, 2012
     */
    public void decryptName(final Date createdDate, final String votingState, final String votingRegion,
                            final PendingVoterName pendingVoterName) throws Exception {
        if (pendingVoterName == null) {
            return;
        }
        final byte[] encrypted = pendingVoterName.getEncrypted();
        if (encrypted == null) {
            return;
        }

        final String decryptedString = getEncryptionService().makeDecryption(createdDate, votingState, votingRegion,
                encrypted);
        final Map<String, String> parsedString = parseDecryptedString(decryptedString);
        pendingVoterName.setTitle(parsedString.get(TITLE));
        pendingVoterName.setFirstName(parsedString.get(FIRST_NAME));
        pendingVoterName.setMiddleName(parsedString.get(MIDDLE_NAME));
        pendingVoterName.setLastName(parsedString.get(LAST_NAME));
        pendingVoterName.setSuffix(parsedString.get(SUFFIX));
    }

    public void decryptName( final PendingVoterRegistration registration ) throws Exception {
        final Date createdDate = registration.getCreatedDate();
        final String votingState = registration.getVotingState();
        final String votingRegion = registration.getVotingRegion();
        decryptName(createdDate, votingState, votingRegion, registration.getPreviousName());
    }

    /**
     * Parses the decrypted strings into the name, value pairs.
     *
     * @author IanBrown
     * @param decryptedString
     *            the decrypted strings.
     * @return the map of values by name.
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    private static Map<String, String> parseDecryptedString(final String decryptedString) {
        // Linked hash map is used to ensure that the iterator over the map returns the values in the order that they appear in the
        // string. This is done so that the answers are returned in order.
        final Map<String, String> parsedString = new LinkedHashMap<String, String>();
        final StringTokenizer stok = new StringTokenizer(decryptedString, "\"\\=\n", true);
        boolean quoted = false;
        boolean inQuote = false;
        boolean foundEquals = false;
        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        StringBuilder sb = name;
        while (stok.hasMoreTokens()) {
            final String token = stok.nextToken();

            if (token.equals("\n")) {
                if (quoted || inQuote || !foundEquals) {
                    //throw new IllegalStateException("Found unexpected newline in " + decryptedString);
                    logger.info("Found unexpected newline in " + decryptedString);
                    sb.append(" ");
                    continue;
                }
                parsedString.put(name.toString(), value.toString());
                name = new StringBuilder();
                value = new StringBuilder();
                sb = name;
                foundEquals = false;

            } else if (token.equals("\"")) {
                if (quoted) {
                    sb.append("\"");
                    quoted = false;
                } else if (inQuote) {
                    inQuote = false;
                } else {
                    inQuote = true;
                }

            } else if (token.equals("\\")) {
                if (quoted) {
                    sb.append("\\");
                    quoted = false;
                } else {
                    quoted = true;
                }

            } else if (token.equals("=")) {
                if (quoted) {
                    sb.append("=");
                    quoted = false;
                } else if (inQuote) {
                    sb.append("=");
                } else if (foundEquals) {
                    throw new IllegalStateException("Found unexpected equals in " + decryptedString);
                }
                sb = value;
                foundEquals = true;

            } else {
                sb.append(token);
            }
        }

        if (name.length() > 0 || value.length() > 0 || quoted || inQuote || foundEquals) {
            throw new IllegalStateException("Unexpected end-of-string found in " + decryptedString);
        }

        return parsedString;
    }

    /*=====================  Encryption  ========================================================*/

    /**
     * Encrypts the pending voter registration object.
     *
     * @author IanBrown
     * @param pendingVoterRegistration
     *            the pending voter registration object.
     * @throws Exception
     *             if there is a problem encrypting the pending voter registration.
     * @since Nov 5, 2012
     * @version Nov 7, 2012
     */
    public void encryptAll(final PendingVoterRegistration pendingVoterRegistration) throws Exception {
        final Date createdDate = pendingVoterRegistration.getCreatedDate();
        final String state = pendingVoterRegistration.getVotingState();
        final String votingRegion = pendingVoterRegistration.getVotingRegion();
        final StringBuilder sb = new StringBuilder();
        addToStringToEncrypt(EMAIL_ADDRESS, pendingVoterRegistration.getEmailAddress(), sb);
        addToStringToEncrypt(ALTERNATE_EMAIL_ADDRESS, pendingVoterRegistration.getAlternateEmailAddress(), sb);
        addToStringToEncrypt(PHONE_NUMBER, pendingVoterRegistration.getPhoneNumber(), sb);
        addToStringToEncrypt(PHONE_NUMBER_TYPE, pendingVoterRegistration.getPhoneNumberType(), sb );
        addToStringToEncrypt(ALTERNATE_PHONE_NUMBER, pendingVoterRegistration.getAlternatePhoneNumber(), sb);
        addToStringToEncrypt(ALTERNATE_PHONE_NUMBER_TYPE, pendingVoterRegistration.getAlternatePhoneNumberType(), sb);
        addToStringToEncrypt(BIRTH_DATE, pendingVoterRegistration.getBirthDay(), sb);
        pendingVoterRegistration.setEmailAddress(null);
        pendingVoterRegistration.setAlternateEmailAddress(null);
        pendingVoterRegistration.setPhoneNumber(null);
        pendingVoterRegistration.setAlternatePhoneNumber(null);
        pendingVoterRegistration.setBirthDay(null);
        // could buffer be longer than 245 ??  TODO check it
        pendingVoterRegistration.setEncrypted(getEncryptionService().makeEncryption(createdDate, state, votingRegion, sb.toString()));
        encryptName(createdDate, state, votingRegion, pendingVoterRegistration.getPreviousName());
        encryptAddress(createdDate, state, votingRegion, pendingVoterRegistration.getVotingAddress());
        encryptAddress(createdDate, state, votingRegion, pendingVoterRegistration.getCurrentAddress());
        encryptAddress(createdDate, state, votingRegion, pendingVoterRegistration.getForwardingAddress());
        encryptAddress(createdDate, state, votingRegion, pendingVoterRegistration.getPreviousAddress());
        encryptAnswers(createdDate, state, votingRegion, pendingVoterRegistration.getAnswers());
    }

    /**
     * Adds the specified name=value pair to the string to be encrypted.
     *
     * @author IanBrown
     * @param name
     *            the name of the pair.
     * @param value
     *            the value of the pair.
     * @param sb
     *            the string builder.
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    private void addToStringToEncrypt(final String name, final String value, final StringBuilder sb) {
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        sb.append(quoteString(name)).append('=').append(quoteString(value)).append("\n");
    }

    /**
     * Encrypts the pending voter address.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param state
     *            the state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterAddress
     *            the pending voter address.
     * @throws Exception
     *             if there is a problem encrypting the address.
     * @since Nov 5, 2012
     * @version Nov 7, 2012
     */
    private void encryptAddress(final Date createdDate, final String state, final String votingRegion,
                                final PendingVoterAddress pendingVoterAddress) throws Exception {
        if (pendingVoterAddress == null) {
            return;
        }

        final StringBuilder sb = new StringBuilder();
        addToStringToEncrypt(STREET1, pendingVoterAddress.getStreet1(), sb);
        addToStringToEncrypt(STREET2, pendingVoterAddress.getStreet2(), sb);
        addToStringToEncrypt(DESCRIPTION, pendingVoterAddress.getDescription(), sb);
        addToStringToEncrypt(CITY, pendingVoterAddress.getCity(), sb);
        addToStringToEncrypt(STATE_OR_REGION, pendingVoterAddress.getStateOrRegion(), sb);
        addToStringToEncrypt(POSTAL_CODE, pendingVoterAddress.getPostalCode(), sb);
        addToStringToEncrypt(COUNTRY, pendingVoterAddress.getCountry(), sb);
        pendingVoterAddress.setStreet1(null);
        pendingVoterAddress.setStreet2(null);
        pendingVoterAddress.setDescription(null);
        pendingVoterAddress.setCity(null);
        pendingVoterAddress.setStateOrRegion(null);
        pendingVoterAddress.setPostalCode(null);
        pendingVoterAddress.setCountry(null);
        // sb could be longer than 245
        checkDataForLength( sb, false );  // truncate from start, country and state are more important
        pendingVoterAddress.setEncrypted(getEncryptionService().makeEncryption(createdDate, state, votingRegion, sb.toString()));
    }

    /**
     * Encrypts a pending voter answer.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param state
     *            the state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterAnswer
     *            the pending voter answer.
     * @throws Exception
     *             if there is a problem encrypting the answer.
     * @since Nov 5, 2012
     * @version Nov 7, 2012
     */
    private void encryptAnswer(final Date createdDate, final String state, final String votingRegion,
                               final PendingVoterAnswer pendingVoterAnswer) throws Exception {
        final StringBuilder sb = new StringBuilder();
        addToStringToEncrypt(pendingVoterAnswer.getQuestion(), pendingVoterAnswer.getAnswer(), sb);
        pendingVoterAnswer.setQuestion(null);
        pendingVoterAnswer.setAnswer(null);
        // sb could be longer than 245 for free text fields
        checkDataForLength( sb, true );
        pendingVoterAnswer.setEncrypted(getEncryptionService().makeEncryption(createdDate, state, votingRegion, sb.toString()));
    }

    /**
     * Encrypts the pending voter answers.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param state
     *            the state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterAnswers
     *            the pending voter answers.
     * @throws Exception
     *             if there is a problem encrypting the answers.
     * @since Nov 5, 2012
     * @version Nov 7, 2012
     */
    private void encryptAnswers(final Date createdDate, final String state, final String votingRegion,
                                final List<PendingVoterAnswer> pendingVoterAnswers) throws Exception {
        for (final PendingVoterAnswer pendingVoterAnswer : pendingVoterAnswers) {
            encryptAnswer(createdDate, state, votingRegion, pendingVoterAnswer);
        }
    }

    /**
     * Encrypts the specified name.
     *
     * @author IanBrown
     * @param createdDate
     *            the date the pending voter registration was created.
     * @param state
     *            the state.
     * @param votingRegion
     *            the voting region.
     * @param pendingVoterName
     *            the pending voter name to encrypt.
     * @throws Exception
     *             if there is a problem encrypting the name,
     * @since Nov 5, 2012
     * @version Nov 7, 2012
     */
    private void encryptName(final Date createdDate, final String state, final String votingRegion,
                             final PendingVoterName pendingVoterName) throws Exception {
        if (pendingVoterName == null) {
            return;
        }

        final StringBuilder sb = new StringBuilder();
        addToStringToEncrypt(TITLE, pendingVoterName.getTitle(), sb);
        addToStringToEncrypt(FIRST_NAME, pendingVoterName.getFirstName(), sb);
        addToStringToEncrypt(MIDDLE_NAME, pendingVoterName.getMiddleName(), sb);
        addToStringToEncrypt(LAST_NAME, pendingVoterName.getLastName(), sb);
        addToStringToEncrypt(SUFFIX, pendingVoterName.getSuffix(), sb);
        pendingVoterName.setTitle(null);
        pendingVoterName.setFirstName(null);
        pendingVoterName.setMiddleName(null);
        pendingVoterName.setLastName(null);
        pendingVoterName.setSuffix(null);
        // could buffer be longer than 245 ??
        checkDataForLength( sb, true );
        pendingVoterName.setEncrypted(getEncryptionService().makeEncryption(createdDate, state, votingRegion, sb.toString()));
    }

    /**
     * Quotes the input string. If there are quotes in it, they are escaped.
     *
     * @author IanBrown
     * @param string
     *            the string to quote.
     * @return the quoted string.
     * @since Nov 5, 2012
     * @version Nov 13, 2012
     */
    public static String quoteString(final String string) {
        final StringBuilder sb = new StringBuilder("\"");
        final StringTokenizer stok = new StringTokenizer(string, "\\\"", true);
        while (stok.hasMoreTokens()) {
            final String token = stok.nextToken();
            if (token.equals("\"")) {
                sb.append("\\\"");
            } else if (token.equals("\\")) {
                sb.append("\\\\");
            } else {
                sb.append(token);
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    /**
     * Encryption could not work with data longer than 245 bytes. so we have to truncate it.
     * @param sb incoming data (buffer)
     * @param truncateEnd truncate end if true, truncate head otherwise
     */
    public void checkDataForLength( StringBuilder sb, boolean truncateEnd ) {
        while ( sb.length() >= 245 ) {
            // too long address, encryption does not work with so long data
            if ( truncateEnd ) {
                int last = sb.lastIndexOf("\"\n");
                sb.delete( last+1, sb.length() );
            } else {
                // truncate it from start
                int first = sb.indexOf("\"\n");
                sb.delete(0, first+2 );
            }
        }
    }


    public EncryptionService getEncryptionService() {
        return encryptionService;
    }
}
