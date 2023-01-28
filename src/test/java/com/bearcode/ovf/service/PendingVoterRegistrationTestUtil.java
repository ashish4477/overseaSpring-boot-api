/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.model.pendingregistration.PendingVoterAddress;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAnswer;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author IanBrown
 * 
 * @since Nov 13, 2012
 * @version Nov 13, 2012
 */
public final class PendingVoterRegistrationTestUtil {

	/**
	 * Custom assertion to ensure that the pending voter registration was converted to/from CSV properly.
	 * 
	 * @author IanBrown
	 * @param expectedPendingVoterRegistration
	 *            the expected pending voter registration.
	 * @param actualPendingVoterRegistration
	 *            the actual pending voter registration.
	 * @since Nov 9, 2012
	 * @version Nov 14, 2012
	 */
	public static void assertPendingVoterRegistration(final PendingVoterRegistration expectedPendingVoterRegistration,
			final PendingVoterRegistration actualPendingVoterRegistration) {
		assertEquals("The alternate email address is correct", expectedPendingVoterRegistration.getAlternateEmailAddress(),
				actualPendingVoterRegistration.getAlternateEmailAddress());
		assertEquals("The alternate phone number is correct", expectedPendingVoterRegistration.getAlternatePhoneNumber(),
				actualPendingVoterRegistration.getAlternatePhoneNumber());
		assertPendingVoterAnswers(expectedPendingVoterRegistration.getAnswers(), actualPendingVoterRegistration.getAnswers());
		assertEquals("The birth day is correct", expectedPendingVoterRegistration.getBirthDay(),
				actualPendingVoterRegistration.getBirthDay());
		assertEquals("The created date is correct", expectedPendingVoterRegistration.getCreatedDate(),
				actualPendingVoterRegistration.getCreatedDate());
		assertPendingVoterAddress("current", expectedPendingVoterRegistration.getCurrentAddress(),
				actualPendingVoterRegistration.getCurrentAddress());
		assertEquals("The email address is correct", expectedPendingVoterRegistration.getEmailAddress(),
				actualPendingVoterRegistration.getEmailAddress());
		assertEquals("The face prefix is correct", expectedPendingVoterRegistration.getFacePrefix(),
				actualPendingVoterRegistration.getFacePrefix());
		assertPendingVoterAddress("forwarding", expectedPendingVoterRegistration.getForwardingAddress(),
				actualPendingVoterRegistration.getForwardingAddress());
		assertEquals("The gender is correct", expectedPendingVoterRegistration.getGender(),
				actualPendingVoterRegistration.getGender());
		assertEquals("The identifier is correct", expectedPendingVoterRegistration.getId(), actualPendingVoterRegistration.getId());
		assertPendingVoterName("current", expectedPendingVoterRegistration.getName(), actualPendingVoterRegistration.getName());
		assertEquals("The phone number is correct", expectedPendingVoterRegistration.getPhoneNumber(),
				actualPendingVoterRegistration.getPhoneNumber());
		assertPendingVoterAddress("previous", expectedPendingVoterRegistration.getPreviousAddress(),
				actualPendingVoterRegistration.getPreviousAddress());
		assertPendingVoterName("previous", expectedPendingVoterRegistration.getPreviousName(),
				actualPendingVoterRegistration.getPreviousName());
		assertEquals("The voter history is correct", expectedPendingVoterRegistration.getVoterHistory(),
				actualPendingVoterRegistration.getVoterHistory());
		assertEquals("The voter type is correct", expectedPendingVoterRegistration.getVoterType(),
				actualPendingVoterRegistration.getVoterType());
		assertPendingVoterAddress("voting", expectedPendingVoterRegistration.getVotingAddress(),
				actualPendingVoterRegistration.getVotingAddress());
		assertEquals("The voting region is correct", expectedPendingVoterRegistration.getVotingRegion(),
				actualPendingVoterRegistration.getVotingRegion());
		assertEquals("The voting state is correct", expectedPendingVoterRegistration.getVotingState(),
				actualPendingVoterRegistration.getVotingState());
	}

	/**
	 * Custom assertion to ensure that the pending voter registrations are converted to/from CSV format properly.
	 * 
	 * @author IanBrown
	 * @param expectedPendingVoterRegistrations
	 *            the expected pending voter registrations.
	 * @param actualPendingVoterRegistrations
	 *            the actual pending voter registrations.
	 * @since Nov 9, 2012
	 * @version Nov 14, 2012
	 */
	public static void assertPendingVoterRegistrations(final List<PendingVoterRegistration> expectedPendingVoterRegistrations,
			final List<PendingVoterRegistration> actualPendingVoterRegistrations) {
		assertEquals("All of the pending voter registrations were converted", expectedPendingVoterRegistrations.size(),
				actualPendingVoterRegistrations.size());
		final Iterator<PendingVoterRegistration> expectedIterator = expectedPendingVoterRegistrations.iterator();
		final Iterator<PendingVoterRegistration> actualIterator = actualPendingVoterRegistrations.iterator();
		while (expectedIterator.hasNext()) {
			final PendingVoterRegistration expectedPendingVoterRegistration = expectedIterator.next();
			final PendingVoterRegistration actualPendingVoterRegistration = actualIterator.next();
			assertPendingVoterRegistration(expectedPendingVoterRegistration, actualPendingVoterRegistration);
		}
	}

	/**
	 * Extracts the pending voter registrations from the CSV data.
	 * 
	 * @author IanBrown
	 * @param csv
	 *            the CSV data.
	 * @return the pending voter registrations.
	 * @throws IOException
	 *             if there is a problem reading the CSV.
	 * @throws ParseException
	 *             if there is a problem parsing the created date.
	 * @since Nov 9, 2012
	 * @version Nov 13, 2012
	 */
	public static List<PendingVoterRegistration> extractFromCsv(final byte[] csv) throws IOException, ParseException {
		final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(csv)));
		return extractFromCsvReader(lnr);
	}

	/**
	 * Extracts the pending voter registrations from the CSV reader.
	 * 
	 * @author IanBrown
	 * @param lnr
	 *            the CSV line number reader.
	 * @return the pending voter registrations.
	 * @throws IOException
	 *             if there is a problem reading the CSV.
	 * @throws ParseException
	 *             if there is a problem parsing the created date.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public static List<PendingVoterRegistration> extractFromCsvReader(final LineNumberReader lnr) throws IOException,
	ParseException {
		try {
			final List<PendingVoterRegistration> pendingVoterRegistrations = new LinkedList<PendingVoterRegistration>();
			String line = lnr.readLine();
			final List<String> headers = extractValuesFromCSVLine(line);
			while ((line = lnr.readLine()) != null) {
				final PendingVoterRegistration pendingVoterRegistration = extractPendingVoterRegistrationFromCSVLine(headers, line);
				pendingVoterRegistrations.add(pendingVoterRegistration);
			}
			return pendingVoterRegistrations;
		} finally {
			lnr.close();
		}
	}

	/**
	 * Custom assertion to ensure that a pending voter address was copied correctly.
	 * 
	 * @author IanBrown
	 * @param addressName
	 *            the name of the address.
	 * @param expectedAddress
	 *            the expected address.
	 * @param actualAddress
	 *            the actual address.
	 * @since Nov 9, 2012
	 * @version Nov 14, 2012
	 */
	private static void assertPendingVoterAddress(final String addressName, final PendingVoterAddress expectedAddress,
			final PendingVoterAddress actualAddress) {
		if (expectedAddress == null) {
			assertNull("There is no " + addressName + " address", actualAddress);
		} else {
			assertNotNull("There is a " + addressName + " address", actualAddress);
			assertEquals("The " + addressName + " street1 is set", expectedAddress.getStreet1(), actualAddress.getStreet1());
			assertEquals("The " + addressName + " street2 is set", expectedAddress.getStreet2(), actualAddress.getStreet2());
			assertEquals("The " + addressName + " description is set", expectedAddress.getDescription(),
					actualAddress.getDescription());
			assertEquals("The " + addressName + " city is set", expectedAddress.getCity(), actualAddress.getCity());
			assertEquals("The " + addressName + " state or region is set", expectedAddress.getStateOrRegion(),
					actualAddress.getStateOrRegion());
			assertEquals("The " + addressName + " postal code is set", expectedAddress.getPostalCode(),
					actualAddress.getPostalCode());
			assertEquals("The " + addressName + " country is set", expectedAddress.getCountry(), actualAddress.getCountry());
			assertNull("The " + addressName + " encrypted is not set", actualAddress.getEncrypted());
		}
	}

	/**
	 * Custom assertion to ensure that the pending voter answers are copied correctly.
	 * 
	 * @author IanBrown
	 * @param expectedAnswers
	 *            the expected list of answers.
	 * @param actualAnswers
	 *            the actual list of answers.
	 * @since Nov 9, 2012
	 * @version Nov 14, 2012
	 */
	private static void assertPendingVoterAnswers(final List<PendingVoterAnswer> expectedAnswers,
			final List<PendingVoterAnswer> actualAnswers) {
		if (expectedAnswers == null) {
			assertNull("There are no answers copied", actualAnswers);
			return;
		}
		assertNotNull("There are copied answers", actualAnswers);
		assertEquals("All of the answers were properly copied", expectedAnswers.size(), actualAnswers.size());
		final Iterator<PendingVoterAnswer> expectedItr = expectedAnswers.iterator();
		final Iterator<PendingVoterAnswer> actualItr = actualAnswers.iterator();
		while (expectedItr.hasNext()) {
			final PendingVoterAnswer expectedAnswer = expectedItr.next();
			final PendingVoterAnswer actualAnswer = actualItr.next();
			assertEquals("The question is set", expectedAnswer.getQuestion(), actualAnswer.getQuestion());
			assertEquals("The answer was is set", expectedAnswer.getAnswer(), actualAnswer.getAnswer());
		}
	}

	/**
	 * Custom assertion to ensure that a pending voter name copied correctly.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the name.
	 * @param expectedName
	 *            the expected name.
	 * @param actualName
	 *            the actual name.
	 * @since Nov 9, 2012
	 * @version Nov 14, 2012
	 */
	private static void assertPendingVoterName(final String name, final PendingVoterName expectedName,
			final PendingVoterName actualName) {
		if (expectedName == null) {
			assertNull("There is no " + name, actualName);
		} else {
			assertNotNull("There is a " + name, actualName);
			assertEquals("The " + name + " title is set", expectedName.getTitle(), actualName.getTitle());
			assertEquals("The " + name + " first name is set", expectedName.getFirstName(), actualName.getFirstName());
			assertEquals("The " + name + " middle name is set", expectedName.getMiddleName(), actualName.getMiddleName());
			assertEquals("The " + name + " last name is set", expectedName.getLastName(), actualName.getLastName());
			assertEquals("The " + name + " suffix is set", expectedName.getSuffix(), actualName.getSuffix());
			assertNull("The " + name + " encrypted is not set", actualName.getEncrypted());
		}
	}

	/**
	 * Extracts a pending voter address.
	 * 
	 * @author IanBrown
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
	 * @return the pending voter address.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private static PendingVoterAddress extractAddress(final String columnGroup, final String header, final String value,
			final Iterator<String> headerItr, final Iterator<String> valueItr, final Stack<Pair<String, String>> headerValueStack) {
		final PendingVoterAddress pendingVoterAddress = new PendingVoterAddress();

		String myHeader = header;
		String myValue = value;
		boolean gotValue = false;
		while (true) {
			gotValue = gotValue || myValue != null;
			if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_STREET1)) {
				pendingVoterAddress.setStreet1(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_STREET2)) {
				pendingVoterAddress.setStreet2(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_DESCRIPTION)) {
				pendingVoterAddress.setDescription(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_CITY)) {
				pendingVoterAddress.setCity(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_STATE_OR_REGION)) {
				pendingVoterAddress.setStateOrRegion(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_POSTAL_CODE)) {
				pendingVoterAddress.setPostalCode(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_COUNTRY)) {
				pendingVoterAddress.setCountry(myValue);
			} else {
				headerValueStack.push(Pair.of(myHeader, myValue));
				break;
			}

			myHeader = headerItr.next();
			myValue = valueItr.next();
		}

		return gotValue ? pendingVoterAddress : null;
	}

	/**
	 * Extracts a pending voter answer.
	 * 
	 * @author IanBrown
	 * @param header
	 *            the header (the question).
	 * @param value
	 *            the value (the answer).
	 * @return the pending voter answer.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private static PendingVoterAnswer extractAnswer(final String header, final String value) {
		if (value == null) {
			return null;
		}
		final PendingVoterAnswer pendingVoterAnswer = new PendingVoterAnswer();
		pendingVoterAnswer.setQuestion(header);
		pendingVoterAnswer.setAnswer(value);
		return pendingVoterAnswer;
	}

	/**
	 * Extracts the pending voter answers.
	 * 
	 * @author IanBrown
	 * @param header
	 *            the header that triggered the extract.
	 * @param value
	 *            the value for the header.
	 * @param headerItr
	 *            the header iterator.
	 * @param valueItr
	 *            the value iterator.
	 * @return the answers.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private static List<PendingVoterAnswer> extractAnswers(final String header, final String value,
			final Iterator<String> headerItr, final Iterator<String> valueItr) {
		final List<PendingVoterAnswer> pendingVoterAnswers = new LinkedList<PendingVoterAnswer>();
		PendingVoterAnswer pendingVoterAnswer = extractAnswer(header, value);
		if (pendingVoterAnswer != null) {
			pendingVoterAnswers.add(pendingVoterAnswer);
		}
		while (headerItr.hasNext()) {
			pendingVoterAnswer = extractAnswer(headerItr.next(), valueItr.next());
			if (pendingVoterAnswer != null) {
				pendingVoterAnswers.add(pendingVoterAnswer);
			}
		}
		return pendingVoterAnswers;
	}

	/**
	 * Extracts a pending voter name.
	 * 
	 * @author IanBrown
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
	 * @return the pending voter name.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private static PendingVoterName extractName(final String columnGroup, final String header, final String value,
			final Iterator<String> headerItr, final Iterator<String> valueItr, final Stack<Pair<String, String>> headerValueStack) {
		final PendingVoterName pendingVoterName = new PendingVoterName();
		String myHeader = header;
		String myValue = value;
		while (true) {
			if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_TITLE)) {
				pendingVoterName.setTitle(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_FIRST_NAME)) {
				pendingVoterName.setFirstName(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_MIDDLE_NAME)) {
				pendingVoterName.setMiddleName(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_LAST_NAME)) {
				pendingVoterName.setLastName(myValue);
			} else if (myHeader.equals(columnGroup + PendingVoterRegistrationDictionary.COLUMN_NAME_SUFFIX)) {
				pendingVoterName.setSuffix(myValue);
			} else {
				headerValueStack.push(Pair.of(myHeader, myValue));
				break;
			}

			myHeader = headerItr.next();
			myValue = valueItr.next();
		}
		return pendingVoterName;
	}

	/**
	 * Extracts a pending voter registration from the input CSV line.
	 * 
	 * @author IanBrown
	 * @param headers
	 *            the column headers.
	 * @param line
	 *            the CSV line to extract.
	 * @return the pending voter registration.
	 * @throws ParseException
	 *             if there is a problem parsing the created date.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private static PendingVoterRegistration extractPendingVoterRegistrationFromCSVLine(final List<String> headers, final String line)
			throws ParseException {
		final List<String> values = extractValuesFromCSVLine(line);
		final PendingVoterRegistration pendingVoterRegistration = new PendingVoterRegistration();
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
				pendingVoterRegistration.setCurrentAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_CURRENT_ADDRESS, header, value, headerItr, valueItr,
						headerValueStack));
			} else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_FORWARDING_ADDRESS)) {
				pendingVoterRegistration.setForwardingAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_FORWARDING_ADDRESS, header, value, headerItr, valueItr,
						headerValueStack));
			} else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_ADDRESS)) {
				pendingVoterRegistration.setPreviousAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_ADDRESS, header, value, headerItr, valueItr,
						headerValueStack));
			} else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_VOTING_ADDRESS)) {
				pendingVoterRegistration.setVotingAddress(extractAddress(
                        PendingVoterRegistrationDictionary.COLUMN_GROUP_VOTING_ADDRESS, header, value, headerItr, valueItr,
						headerValueStack));
			} else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_NAME)) {
				pendingVoterRegistration.setName(extractName(PendingVoterRegistrationDictionary.COLUMN_GROUP_NAME, header, value,
						headerItr, valueItr, headerValueStack));
			} else if (header.startsWith(PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_NAME)) {
				pendingVoterRegistration.setPreviousName(extractName(PendingVoterRegistrationDictionary.COLUMN_GROUP_PREVIOUS_NAME,
						header, value, headerItr, valueItr, headerValueStack));
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_ID)) {
				pendingVoterRegistration.setId(Long.parseLong(value));
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_CREATED_DATE)) {
				pendingVoterRegistration.setCreatedDate(PendingVoterRegistrationDictionary.CREATED_DATE_FORMAT.parse(value));
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_FACE_PREFIX)) {
				pendingVoterRegistration.setFacePrefix(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTING_STATE)) {
				pendingVoterRegistration.setVotingState(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTING_REGION)) {
				pendingVoterRegistration.setVotingRegion(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTER_TYPE)) {
				pendingVoterRegistration.setVoterType(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_VOTER_HISTORY)) {
				pendingVoterRegistration.setVoterHistory(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_EMAIL_ADDRESS)) {
				pendingVoterRegistration.setEmailAddress(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_ALTERNATE_EMAIL_ADDRESS)) {
				pendingVoterRegistration.setAlternateEmailAddress(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_PHONE_NUMBER)) {
				pendingVoterRegistration.setPhoneNumber(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_ALTERNATE_PHONE_NUMBER)) {
				pendingVoterRegistration.setAlternatePhoneNumber(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_BIRTH_DATE)) {
				pendingVoterRegistration.setBirthDay(value);
			} else if (header.equals(PendingVoterRegistrationDictionary.COLUMN_NAME_GENDER)) {
				pendingVoterRegistration.setGender(value);
			} else {
				pendingVoterRegistration.setAnswers(extractAnswers(header, value, headerItr, valueItr));
			}
		}

		return pendingVoterRegistration;
	}

	/**
	 * Extracts the values from the CSV line.
	 * 
	 * @author IanBrown
	 * @param line
	 *            the CSV line to extract.
	 * @return the values.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
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

		assertFalse("The last quote is closed", inQuote);
		assertNull("The last value has been extracted", sb);
		return values;
	}
}
