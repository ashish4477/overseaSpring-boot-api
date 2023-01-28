package com.bearcode.ovf.tools.registrationexport;

import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created by leonid on 3.11.16.
 */
public class ExportUtilities {
    /**
     * Custom assertion to ensure that the wizard results was converted to/from CSV properly.
     *
     * @param expectedResults
     *            the expected wizard results.
     * @param actualResults
     *            the actual wizard results.
     */
    public static void assertWizardResults(final WizardResults expectedResults,
                                                      final WizardResults actualResults ) {
        assertEquals("The alternate email address is correct", expectedResults.getAlternateEmail(),
                actualResults.getAlternateEmail());
        assertEquals("The alternate phone number is correct", expectedResults.getAlternatePhone(),
                actualResults.getAlternatePhone());
        assertAnswers( expectedResults.getAnswers(), actualResults.getAnswers() );
        assertEquals( "The birth day is correct", expectedResults.getBirthDate(),
                actualResults.getBirthDate() );
        assertEquals( "The birth month is correct", expectedResults.getBirthMonth(),
                actualResults.getBirthMonth() + 1 );
        assertEquals("The birth year is correct", expectedResults.getBirthYear(),
                actualResults.getBirthYear());
        assertEquals("The created date is correct", expectedResults.getCreationDate(),
                actualResults.getCreationDate());
        assertWizardResultAddress( "current", expectedResults.getCurrentAddress(),
                actualResults.getCurrentAddress() );
        assertEquals("The email address is correct", expectedResults.getUsername(),
                actualResults.getUsername());
        assertEquals("The face prefix is correct", expectedResults.getFaceUrl(),
                actualResults.getFaceUrl());
        assertWizardResultAddress( "forwarding", expectedResults.getForwardingAddress(),
                actualResults.getForwardingAddress() );
        assertEquals("The gender is correct", expectedResults.getGender(),
                actualResults.getGender());
        assertEquals("The identifier is correct", expectedResults.getId(), actualResults.getId());
        assertWizardResultsPerson( "current", expectedResults.getName(), actualResults.getName() );
        assertEquals("The phone number is correct", expectedResults.getPhone(),
                actualResults.getPhone());
        assertWizardResultAddress( "previous", expectedResults.getPreviousAddress(),
                actualResults.getPreviousAddress() );
        assertWizardResultsPerson( "previous", expectedResults.getPreviousName(),
                actualResults.getPreviousName() );
        assertEquals("The voter history is correct", expectedResults.getVoterHistory(),
                actualResults.getVoterHistory());
        assertEquals("The voter type is correct", expectedResults.getVoterType(),
                actualResults.getVoterType());
        assertWizardResultAddress( "voting", expectedResults.getVotingAddress(),
                actualResults.getVotingAddress() );
        assertEquals("The voting region is correct", expectedResults.getVotingRegionName(),
                actualResults.getVotingRegionName());
        assertEquals("The voting state is correct", expectedResults.getVotingRegionState(),
                actualResults.getVotingRegionState());
    }

    /**
     * Custom assertion to ensure that the wizard results are converted to/from CSV format properly.
     *
     * @param expectedList
     *            the expected wizard results.
     * @param actualList
     *            the actual wizard results.
     */
    public static void assertWizardResults( final List<WizardResults> expectedList,
                                            final List<WizardResults> actualList ) {
        assertEquals("All of the wizard results were converted", expectedList.size(),
                actualList.size());
        final Iterator<WizardResults> expectedIterator = expectedList.iterator();
        final Iterator<WizardResults> actualIterator = actualList.iterator();
        while (expectedIterator.hasNext()) {
            final WizardResults expected = expectedIterator.next();
            final WizardResults actual = actualIterator.next();
            assertWizardResults( expected, actual );
        }
    }

    /**
     * Extracts the wizard results from the CSV data.
     *
     * @param csv
     *            the CSV data.
     * @return the wizard results.
     * @throws IOException
     *             if there is a problem reading the CSV.
     * @throws ParseException
     *             if there is a problem parsing the created date.
     */
    public static List<WizardResults> extractFromCsv(final byte[] csv) throws IOException, ParseException {
        final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(csv)));
        return extractFromCsvReader(lnr);
    }

    /**
     * Extracts the wizard results from the CSV reader.
     *
     * @param lnr
     *            the CSV line number reader.
     * @return the wizard results list.
     * @throws IOException
     *             if there is a problem reading the CSV.
     * @throws ParseException
     *             if there is a problem parsing the created date.
     */
    public static List<WizardResults> extractFromCsvReader(final LineNumberReader lnr) throws IOException,
            ParseException {
        try {
            final List<WizardResults> resultss = new LinkedList<WizardResults>();
            String line = lnr.readLine();
            final List<String> headers = extractValuesFromCSVLine(line);
            while ((line = lnr.readLine()) != null) {
                final WizardResults results = extractPendingVoterRegistrationFromCSVLine(headers, line);
                resultss.add(results);
            }
            return resultss;
        } finally {
            lnr.close();
        }
    }

    /**
     * Custom assertion to ensure that a voter address was copied correctly.
     *
     * @param addressName
     *            the name of the address.
     * @param expectedAddress
     *            the expected address.
     * @param actualAddress
     *            the actual address.
     */
    private static void assertWizardResultAddress( final String addressName, final WizardResultAddress expectedAddress,
                                                   final WizardResultAddress actualAddress ) {
        if (expectedAddress == null) {
            if ( actualAddress != null ) {
                assertTrue( "There is no " + addressName + " address", actualAddress.isEmptySpace() );
            }
        } else {
            assertNotNull( "There is a " + addressName + " address", actualAddress );
            assertEquals("The " + addressName + " street1 is set", expectedAddress.getStreet1(), actualAddress.getStreet1());
            assertEquals("The " + addressName + " street2 is set", expectedAddress.getStreet2(), actualAddress.getStreet2());
            assertEquals("The " + addressName + " description is set", expectedAddress.getDescription(),
                    actualAddress.getDescription());
            assertEquals("The " + addressName + " city is set", expectedAddress.getCity(), actualAddress.getCity());
            assertEquals("The " + addressName + " state or region is set", expectedAddress.getState(),
                    actualAddress.getState());
            assertEquals("The " + addressName + " postal code is set", expectedAddress.getZip(),
                    actualAddress.getZip());
            assertEquals( "The " + addressName + " country is set", expectedAddress.getCountry(), actualAddress.getCountry() );
        }
    }

    /**
     * Custom assertion to ensure that the voter answers are copied correctly.
     *
     * @param expectedAnswers
     *            the expected list of answers.
     * @param actualAnswers
     *            the actual list of answers.
     */
    private static void assertAnswers( final Collection<Answer> expectedAnswers,
                                       final Collection<Answer> actualAnswers ) {
        if (expectedAnswers == null) {
            assertNull("There are no answers copied", actualAnswers);
            return;
        }
        assertNotNull("There are copied answers", actualAnswers);
        assertEquals("All of the answers were properly copied", expectedAnswers.size(), actualAnswers.size());
        final Iterator<Answer> expectedItr = expectedAnswers.iterator();
        final Iterator<Answer> actualItr = actualAnswers.iterator();
        while (expectedItr.hasNext()) {
            final Answer expectedAnswer = expectedItr.next();
            final Answer actualAnswer = actualItr.next();
            assertEquals("The question is set", expectedAnswer.getField().getTitle(), actualAnswer.getField().getTitle());
            assertEquals("The answer was is set", expectedAnswer.getValue(), actualAnswer.getValue());
        }
    }

    /**
     * Custom assertion to ensure that a  voter name copied correctly.
     *
     * @param name
     *            the name of the name.
     * @param expectedName
     *            the expected name.
     * @param actualName
     *            the actual name.
     */
    private static void assertWizardResultsPerson(final String name, final WizardResultPerson expectedName,
                                               final WizardResultPerson actualName) {
        if (expectedName == null) {
            if ( actualName != null ) {
                assertTrue("There is no " + name, actualName.isEmpty());
            }
        } else {
            assertNotNull( "There is a " + name, actualName );
            assertEquals("The " + name + " title is set", expectedName.getTitle(), actualName.getTitle());
            assertEquals("The " + name + " first name is set", expectedName.getFirstName(), actualName.getFirstName());
            assertEquals("The " + name + " middle name is set", expectedName.getMiddleName(), actualName.getMiddleName());
            assertEquals("The " + name + " last name is set", expectedName.getLastName(), actualName.getLastName());
            assertEquals( "The " + name + " suffix is set", expectedName.getSuffix(), actualName.getSuffix() );
        }
    }

    /**
     * Extracts a voter address.
     *
     * @param columnGroup
     *            the name of the column group (which address is it?)
     * @param header
     *            the header that triggered the column group.
     * @param value
     *            the value for the header.
     * @param headerItr
     *            the header iterator.
     * @param valueItr
     *            the value iterator.
     * @param headerValueStack
     *            the stack of header/values that haven't been used.
     * @return the voter address.
     */
    private static WizardResultAddress extractAddress(final String columnGroup, final String header, final String value,
                                                      final Iterator<String> headerItr, final Iterator<String> valueItr, final Stack<Pair<String, String>> headerValueStack) {
        final WizardResultAddress resultAddress = new WizardResultAddress();

        String myHeader = header;
        String myValue = value;
        boolean gotValue = false;
        while ( true ) {
            gotValue = gotValue || myValue != null;
            if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_STREET1)) {
                resultAddress.setStreet1( myValue );
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_STREET2)) {
                resultAddress.setStreet2( myValue );
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_DESCRIPTION)) {
                resultAddress.setDescription( myValue );
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_CITY)) {
                resultAddress.setCity( myValue );
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_STATE_OR_REGION)) {
                resultAddress.setState( myValue );
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_POSTAL_CODE)) {
                resultAddress.setZip( myValue );
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_COUNTRY)) {
                resultAddress.setCountry( myValue );
            } else {
                headerValueStack.push(Pair.of(myHeader, myValue));
                break;
            }

            if ( !headerItr.hasNext() ) break;
            myHeader = headerItr.next();
            myValue = valueItr.next();
        }

        return gotValue ? resultAddress : null;
    }

    /**
     * Extracts a voter answer.
     *
     * @param header
     *            the header (the question).
     * @param value
     *            the value (the answer).
     * @return the voter answer.
     */
    private static Answer extractAnswer(final String header, final String value) {
        if (value == null) {
            return null;
        }
        final Answer voterAnswer = new EnteredAnswer();
        final QuestionField field = new QuestionField();
        field.setTitle( header );
        voterAnswer.setField( field );
        voterAnswer.setValue( value );
        return voterAnswer;
    }

    /**
     * Extracts the voter answers.
     *
     * @param header
     *            the header that triggered the extract.
     * @param value
     *            the value for the header.
     * @param headerItr
     *            the header iterator.
     * @param valueItr
     *            the value iterator.
     * @return the answers.
     */
    private static List<Answer> extractAnswers(final String header, final String value,
                                                           final Iterator<String> headerItr, final Iterator<String> valueItr) {
        final List<Answer> answers = new LinkedList<Answer>();
        Answer voterAnswer = extractAnswer(header, value);
        if (voterAnswer != null) {
            answers.add( voterAnswer );
        }
        while (headerItr.hasNext()) {
            voterAnswer = extractAnswer(headerItr.next(), valueItr.next());
            if (voterAnswer != null) {
                answers.add( voterAnswer );
            }
        }
        return answers;
    }

    /**
     * Extracts a voter name.
     *
     * @param columnGroup
     *            the name of the column group (which name is it?)
     * @param header
     *            the header that triggered the column group.
     * @param value
     *            the value for the header.
     * @param headerItr
     *            the header iterator.
     * @param valueItr
     *            the value iterator.
     * @param headerValueStack
     *            the stack of header/value pairs that have not been processed.
     * @return the voter name.
     */
    private static WizardResultPerson extractName(final String columnGroup, final String header, final String value,
                                                final Iterator<String> headerItr, final Iterator<String> valueItr, final Stack<Pair<String, String>> headerValueStack) {
        final WizardResultPerson WizardResultsPerson = new WizardResultPerson();
        String myHeader = header;
        String myValue = value;
        while (true) {
            if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_TITLE)) {
                WizardResultsPerson.setTitle(myValue);
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_FIRST_NAME)) {
                WizardResultsPerson.setFirstName(myValue);
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_MIDDLE_NAME)) {
                WizardResultsPerson.setMiddleName(myValue);
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_LAST_NAME)) {
                WizardResultsPerson.setLastName(myValue);
            } else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_SUFFIX)) {
                WizardResultsPerson.setSuffix(myValue);
            } else {
                headerValueStack.push(Pair.of(myHeader, myValue));
                break;
            }

            myHeader = headerItr.next();
            myValue = valueItr.next();
        }
        return WizardResultsPerson;
    }

    /**
     * Extracts a voter registration from the input CSV line.
     ** @param headers
     *            the column headers.
     * @param line
     *            the CSV line to extract.
     * @return the voter registration.
     * @throws ParseException
     *             if there is a problem parsing the created date.
     */
    private static WizardResults extractPendingVoterRegistrationFromCSVLine(final List<String> headers, final String line)
            throws ParseException {
        final List<String> values = extractValuesFromCSVLine(line);
        final WizardResults results = new WizardResults( FlowType.RAVA );
        assertEquals("There is a value for each header (" + headers + " = " + values + ")", headers.size(), values.size());
        final Iterator<String> headerItr = headers.iterator();
        final Iterator<String> valueItr = values.iterator();
        final Stack<Pair<String, String>> headerValueStack = new Stack<Pair<String, String>>();
        while (headerItr.hasNext() || !headerValueStack.isEmpty()) {
            final String header;
            final String value;
            if (headerValueStack.isEmpty()) {
                header = headerItr.next();
                value = valueItr.next();
            } else {
                final Pair<String, String> headerValue = headerValueStack.pop();
                header = headerValue.getLeft();
                value = headerValue.getRight();
            }

            if (value == null || value.trim().isEmpty()) {
                // No value - can skip this.
                continue;
            }

            if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_CURRENT_ADDRESS)) {
                results.setCurrentAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_CURRENT_ADDRESS, header, value, headerItr, valueItr,
                        headerValueStack));
            } else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_FORWARDING_ADDRESS)) {
                results.setForwardingAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_FORWARDING_ADDRESS, header, value, headerItr, valueItr,
                        headerValueStack));
            } else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_ADDRESS)) {
                results.setPreviousAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_ADDRESS, header, value, headerItr, valueItr,
                        headerValueStack));
            } else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_VOTING_ADDRESS)) {
                results.setVotingAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_VOTING_ADDRESS, header, value, headerItr, valueItr,
                        headerValueStack));
            } else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_NAME)) {
                results.setName(extractName(PendingVoterRegistrationDictionary.COLUMN_GROUP_NAME, header, value,
                        headerItr, valueItr, headerValueStack));
            } else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_NAME)) {
                results.setPreviousName(extractName(PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_NAME,
                        header, value, headerItr, valueItr, headerValueStack));
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_ID)) {
                results.setId(Long.parseLong(value));
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_CREATED_DATE)) {
                results.setCreationDate( RegistrationExportUtils.CREATED_DATE_FORMAT.parse( value ) );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_FACE_PREFIX)) {
                results.setFaceUrl( value );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTING_STATE)) {
                results.setVotingRegionState( value );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTING_REGION)) {
                results.setVotingRegionName( value );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTER_TYPE)) {
                results.setVoterType(value);
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTER_HISTORY)) {
                results.setVoterHistory(value);
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_EMAIL_ADDRESS)) {
                results.setUsername( value );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_ALTERNATE_EMAIL_ADDRESS)) {
                results.setAlternateEmail( value );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_PHONE_NUMBER)) {
                results.setPhone( value );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_ALTERNATE_PHONE_NUMBER)) {
                results.setAlternatePhone( value );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_BIRTH_DATE)) {
                Date date = RegistrationExportUtils.BIRTH_DAY_FORMAT.parse( value );
                Calendar calendar = Calendar.getInstance();
                calendar.setTime( date );
                results.setBirthDate( calendar.get( Calendar.DAY_OF_MONTH ) );
                results.setBirthMonth( calendar.get( Calendar.MONTH ) );
                results.setBirthYear( calendar.get( Calendar.YEAR ) );
            } else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_GENDER)) {
                results.setGender( value );
            } else if (
                    header.equals( PendingVoterRegistrationDictionary.COLUMN_ACCOUNT_CREATED ) ||
                    header.equals( PendingVoterRegistrationDictionary.COLUMN_FORM_DOWNLOADED )
                    ) {
                    // do nothing
            } else {
                results.setAnswers(extractAnswers(header, value, headerItr, valueItr));
            }
        }

        return results;
    }

    /**
     * Extracts the values from the CSV line.
     *
     * @param line
     *            the CSV line to extract.
     * @return the values.
     */
    private static List<String> extractValuesFromCSVLine(final String line) {
        final List<String> values = new LinkedList<String>();
        final StringTokenizer stok = new StringTokenizer(line, "\",", true);
        boolean inQuote = false;
        boolean wasComma = true;
        StringBuilder sb = null;
        String prereadToken = null;
        while ((prereadToken != null) || stok.hasMoreTokens()) {
            final String token = prereadToken == null ? stok.nextToken() : prereadToken;
            prereadToken = null;
            if (token.equals(",")) {
                if (wasComma) {
                    values.add(null);
                }
                wasComma = true;
            } else if (token.equals("\"")) {
                wasComma = false;
                if (inQuote) {
                    if (stok.hasMoreTokens()) {
                        prereadToken = stok.nextToken();
                        if (prereadToken.equals("\"")) {
                            sb.append("\"");
                            prereadToken = null;
                        } else {
                            inQuote = false;
                            values.add(sb.toString());
                            sb = null;
                        }
                    } else {
                        inQuote = false;
                        values.add(sb.toString());
                        sb = null;
                    }
                } else {
                    inQuote = true;
                    sb = new StringBuilder();
                }
            } else if (inQuote) {
                wasComma = false;
                sb.append(token);
            } else {
                fail("Unexpected unquoted token: " + token);
            }
        }
        if ( wasComma ) {
            values.add(  null ); // last symbol in the line was comma, need to add an empty element
        }

        assertFalse("The last quote is closed", inQuote);
        assertNull("The last value has been extracted", sb);
        return values;
    }
}

