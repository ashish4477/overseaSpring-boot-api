package com.bearcode.ovf.tools.pendingregistration;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAddress;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAnswer;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.PendingVoterRegistrationService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Date: 23.10.14
 * Time: 15:33
 *
 * @author Leonid Ginzburg
 */
@Component
public class PendingVoterRegistrationUtil implements PendingVoterRegistrationDictionary {

    @Autowired
    private PendingVoterRegistrationService pendingVoterRegistrationService;

    @Autowired
    private PendingVoterRegistrationCipher pendingVoterRegistrationCipher;

    @Autowired
    private QuestionnaireService questionnaireService;


    /**
   	 * Saves the pending voter registration to the database. This method ensures that the personally identifiable information for
   	 * the pending voter registration is encrypted before it is saved.
   	 *
   	 * @author IanBrown
   	 * @param pendingVoterRegistration
   	 *            the pending voter registration.
   	 * @throws Exception
   	 *             if there is a problem saving the pending voter registration.
   	 * @since Nov 5, 2012
   	 * @version Nov 7, 2012
   	 */
   	public void encryptAndSave(final PendingVoterRegistration pendingVoterRegistration) throws Exception {
   		getPendingVoterRegistrationCipher().encryptAll(pendingVoterRegistration);
   		getPendingVoterRegistrationService().save(pendingVoterRegistration);
   	}




   	/**
   	 * Creates a CSV file.
   	 *
   	 * @author IanBrown
   	 * @param pendingVoterRegistrations
   	 *            the pending voter registrations.
   	 * @return the CSV file as a byte array.
   	 * @since Nov 9, 2012
   	 * @version Nov 12, 2012
   	 */
   	public byte[] createCsv(final List<PendingVoterRegistration> pendingVoterRegistrations) {
   		final List<String> headers = new LinkedList<String>();
   		final List<Map<String, String>> values = new LinkedList<Map<String, String>>();
   		buildCsvValues(pendingVoterRegistrations, headers, values);

   		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
   		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(baos));
   		try {
   			writeCsvHeaders(headers, pw);
   			writeCsvValues(headers, values, pw);
   		} finally {
   			pw.close();
   		}

   		return baos.toByteArray();
   	}

	/**
	 * Creates a CSV file.
	 *
	 * @param wizardResultsList
	 *            the pending voter registrations.
	 * @param answerExportLevel export level for answers
	 * @return the CSV file as a byte array.
	 * @since Sep 13, 2016
	 */
	public byte[] createCsv(final List<WizardResults> wizardResultsList, int answerExportLevel) {
		final List<String> headers = new LinkedList<String>();
		final List<Map<String, String>> values = new LinkedList<Map<String, String>>();
		buildCsvValues( wizardResultsList, headers, values, answerExportLevel);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(baos));
		try {
			writeCsvHeaders(headers, pw);
			writeCsvValues(headers, values, pw);
		} finally {
			pw.close();
		}

		return baos.toByteArray();
	}

	/**
   	 * Adds a pending voter address to the CSV line.
   	 *
   	 * @author IanBrown
   	 * @param columnGroup
   	 *            the name of the column group.
   	 * @param address
   	 *            the pending voter address.
   	 * @param lineHeaders
   	 *            the line headers.
   	 * @param lineValues
   	 *            the line values.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void addLineCsvAddress(final String columnGroup, final PendingVoterAddress address, final List<String> lineHeaders,
   			final Map<String, String> lineValues) {
   		if (address != null) {
   			addLineCsvValue(columnGroup + COLUMN_NAME_STREET1, address.getStreet1(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_STREET2, address.getStreet2(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_DESCRIPTION, address.getDescription(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_CITY, address.getCity(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_STATE_OR_REGION, address.getStateOrRegion(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_POSTAL_CODE, address.getPostalCode(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_COUNTRY, address.getCountry(), lineHeaders, lineValues);
   		}
   	}

	/**
	 * Adds a wizard results address to the CSV line.
	 *
	 * @param columnGroup
	 *            the name of the column group.
	 * @param address
	 *            the pending voter address.
	 * @param lineHeaders
	 *            the line headers.
	 * @param lineValues
	 *            the line values.
	 * @since Sep 13, 2016
	 */
	private void addLineCsvAddress(final String columnGroup, final WizardResultAddress address, final List<String> lineHeaders,
								   final Map<String, String> lineValues) {
		if (address != null) {
			addLineCsvValue(columnGroup + COLUMN_NAME_STREET1, address.getStreet1(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_STREET2, address.getStreet2(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_DESCRIPTION, address.getDescription(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_CITY, address.getCity(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_STATE_OR_REGION, address.getState(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_POSTAL_CODE, address.getZip(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_COUNTRY, address.getCountry(), lineHeaders, lineValues);
		}
	}

   	/**
   	 * Adds the pending voter answer to the line CSV values.
   	 *
   	 * @author IanBrown
   	 * @param pendingVoterAnswer
   	 *            the pending voter answer.
   	 * @param lineHeaders
   	 *            the list of line column headers.
   	 * @param lineValues
   	 *            the map of line values by header.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void addLineCsvAnswer(final PendingVoterAnswer pendingVoterAnswer, final List<String> lineHeaders,
   			final Map<String, String> lineValues) {
   		addLineCsvValue(pendingVoterAnswer.getQuestion(), pendingVoterAnswer.getAnswer(), lineHeaders, lineValues);
   	}

	/**
   	 * Adds the pending voter answers to the CSV line values.
   	 *
   	 * @author IanBrown
   	 * @param pendingVoterAnswers
   	 *            the pending voter answers.
   	 * @param lineHeaders
   	 *            the list of line column headers.
   	 * @param lineValues
   	 *            the map of the line values by header.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void addLineCsvAnswers(final List<PendingVoterAnswer> pendingVoterAnswers, final List<String> lineHeaders,
   			final Map<String, String> lineValues) {
   		for (final PendingVoterAnswer pendingVoterAnswer : pendingVoterAnswers) {
   			addLineCsvAnswer(pendingVoterAnswer, lineHeaders, lineValues);
   		}
   	}

	/**
   	 * Adds a pending voter name object to the CSV line entries.
   	 *
   	 * @author IanBrown
   	 * @param columnGroup
   	 *            the name of the column group.
   	 * @param pendingVoterName
   	 *            the pending voter name object.
   	 * @param lineHeaders
   	 *            the line column headers.
   	 * @param lineValues
   	 *            the map of values by header for the line.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void addLineCsvName(final String columnGroup, final PendingVoterName pendingVoterName, final List<String> lineHeaders,
   			final Map<String, String> lineValues) {
   		if (pendingVoterName != null) {
   			addLineCsvValue(columnGroup + COLUMN_NAME_TITLE, pendingVoterName.getTitle(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_FIRST_NAME, pendingVoterName.getFirstName(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_MIDDLE_NAME, pendingVoterName.getMiddleName(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_LAST_NAME, pendingVoterName.getLastName(), lineHeaders, lineValues);
   			addLineCsvValue(columnGroup + COLUMN_NAME_SUFFIX, pendingVoterName.getSuffix(), lineHeaders, lineValues);
   		}
   	}

	/**
	 * Adds a wizard results name object to the CSV line entries.
	 *
	 * @param columnGroup
	 *            the name of the column group.
	 * @param wizardResultsName
	 *            the pending voter name object.
	 * @param lineHeaders
	 *            the line column headers.
	 * @param lineValues
	 *            the map of values by header for the line.
	 * @since Sep 13, 2016
	 */
	private void addLineCsvName(final String columnGroup, final WizardResultPerson wizardResultsName, final List<String> lineHeaders,
								final Map<String, String> lineValues) {
		if ( wizardResultsName != null) {
			addLineCsvValue(columnGroup + COLUMN_NAME_TITLE, wizardResultsName.getTitle(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_FIRST_NAME, wizardResultsName.getFirstName(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_MIDDLE_NAME, wizardResultsName.getMiddleName(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_LAST_NAME, wizardResultsName.getLastName(), lineHeaders, lineValues);
			addLineCsvValue(columnGroup + COLUMN_NAME_SUFFIX, wizardResultsName.getSuffix(), lineHeaders, lineValues);
		}
	}
	/**
   	 * Adds a line CSV value.
   	 *
   	 * @author IanBrown
   	 * @param columnName
   	 *            the name of the CSV column.
   	 * @param value
   	 *            the value.
   	 * @param lineHeaders
   	 *            the headers for the lines.
   	 * @param lineValues
   	 *            the values for the lines.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void addLineCsvValue(final String columnName, final String value, final List<String> lineHeaders,
   			final Map<String, String> lineValues) {
   		if (value == null || value.trim().isEmpty()) {
   			return;
   		}

   		lineHeaders.add(quoteCsvValue(columnName));
   		lineValues.put(quoteCsvValue(columnName), quoteCsvValue(value));
   	}

   	/**
   	 * Builds the CSV values from the pending voter registrations. Basically, this method builds two things - a list of column
   	 * headers, and a list of maps of values by column header.
   	 *
   	 * @author IanBrown
   	 * @param pendingVoterRegistrations
   	 *            the pending registrations.
   	 * @param headers
   	 *            the column headers.
   	 * @param values
   	 *            the values, each row represents one pending voter registration.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void buildCsvValues(final List<PendingVoterRegistration> pendingVoterRegistrations, final List<String> headers,
   			final List<Map<String, String>> values) {
   		for (final PendingVoterRegistration pendingVoterRegistration : pendingVoterRegistrations) {
   			final List<String> lineHeaders = new LinkedList<String>();
   			final Map<String, String> lineValues = new HashMap<String, String>();
   			buildLineCsvValues(pendingVoterRegistration, lineHeaders, lineValues);
   			mergeCsvHeaders(headers, lineHeaders);
   			values.add(lineValues);
   		}
   	}

	/**
	 * Builds the CSV values from the wizard results. Basically, this method builds two things - a list of column
	 * headers, and a list of maps of values by column header.
	 *
	 * @author IanBrown
	 * @param wizardResultsList
	 *            the pending registrations.
	 * @param headers
	 *            the column headers.
	 * @param values
	 *            the values, each row represents one pending voter registration.
	 * @since Sep 13, 2016
	 */
	private void buildCsvValues(final List<WizardResults> wizardResultsList, final List<String> headers,
								final List<Map<String, String>> values, int answerExportLevel) {
		for (final WizardResults wizardResults : wizardResultsList ) {
			if (!checkPendingVoterRegistrationAnswer(wizardResults.getAnswers())) {
				continue;
			}

			final List<String> lineHeaders = new LinkedList<String>();
			final Map<String, String> lineValues = new HashMap<String, String>();
			buildLineCsvValues( wizardResults, lineHeaders, lineValues, answerExportLevel);
			mergeCsvHeaders(headers, lineHeaders);
			values.add(lineValues);
		}
	}

	/**
   	 * Builds the CSV values for a single pending voter registration (CSV line).
   	 *
   	 * @author IanBrown
   	 * @param pendingVoterRegistration
   	 *            the pending voter registrations.
   	 * @param lineHeaders
   	 *            the list of headers for the pending voter registration.
   	 * @param lineValues
   	 *            the map of values by header for the pending voter registration.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void buildLineCsvValues(final PendingVoterRegistration pendingVoterRegistration, final List<String> lineHeaders,
   			final Map<String, String> lineValues) {
   		addLineCsvValue(COLUMN_NAME_ID, Long.toString(pendingVoterRegistration.getId()), lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_CREATED_DATE, CREATED_DATE_FORMAT.format( pendingVoterRegistration.getCreatedDate() ),
   				lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_FACE_PREFIX, pendingVoterRegistration.getFacePrefix(), lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_VOTING_STATE, pendingVoterRegistration.getVotingState(), lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_VOTING_REGION, pendingVoterRegistration.getVotingRegion(), lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_VOTER_TYPE, pendingVoterRegistration.getVoterType(), lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_VOTER_HISTORY, pendingVoterRegistration.getVoterHistory(), lineHeaders, lineValues);
   		addLineCsvName( COLUMN_GROUP_NAME, pendingVoterRegistration.getName(), lineHeaders, lineValues );
   		addLineCsvName(COLUMN_GROUP_PREVIOUS_NAME, pendingVoterRegistration.getPreviousName(), lineHeaders, lineValues);
   		addLineCsvValue( COLUMN_NAME_EMAIL_ADDRESS, pendingVoterRegistration.getEmailAddress(), lineHeaders, lineValues );
   		addLineCsvValue(COLUMN_NAME_ALTERNATE_EMAIL_ADDRESS, pendingVoterRegistration.getAlternateEmailAddress(), lineHeaders,
   				lineValues);
   		addLineCsvValue(COLUMN_NAME_PHONE_NUMBER, pendingVoterRegistration.getPhoneNumber(), lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_PHONE_TYPE, pendingVoterRegistration.getPhoneNumberType(), lineHeaders, lineValues  );
		addLineCsvValue(COLUMN_NAME_ALTERNATE_PHONE_NUMBER, pendingVoterRegistration.getAlternatePhoneNumber(), lineHeaders,
   				lineValues);
		addLineCsvValue(COLUMN_NAME_ALTERNATE_PHONE_NUMBER_TYPE, pendingVoterRegistration.getAlternatePhoneNumberType(), lineHeaders,
				lineValues);
   		addLineCsvValue(COLUMN_NAME_BIRTH_DATE, pendingVoterRegistration.getBirthDay(), lineHeaders, lineValues);
   		addLineCsvValue(COLUMN_NAME_GENDER, pendingVoterRegistration.getGender(), lineHeaders, lineValues);
   		addLineCsvAddress(COLUMN_GROUP_VOTING_ADDRESS, pendingVoterRegistration.getVotingAddress(), lineHeaders, lineValues);
   		addLineCsvAddress(COLUMN_GROUP_CURRENT_ADDRESS, pendingVoterRegistration.getCurrentAddress(), lineHeaders, lineValues);
   		addLineCsvAddress(COLUMN_GROUP_FORWARDING_ADDRESS, pendingVoterRegistration.getForwardingAddress(), lineHeaders, lineValues);
   		addLineCsvAddress(COLUMN_GROUP_PREVIOUS_ADDRESS, pendingVoterRegistration.getPreviousAddress(), lineHeaders, lineValues);
   		addLineCsvAnswers(pendingVoterRegistration.getAnswers(), lineHeaders, lineValues);
   	}

	/**
	 * Builds the CSV values for a single wizard results (CSV line).
	 *
	 * @author IanBrown
	 * @param wizardResults
	 *            the pending voter registrations.
	 * @param lineHeaders
	 *            the list of headers for the pending voter registration.
	 * @param lineValues
	 *            the map of values by header for the pending voter registration.
	 * @since Nov 12, 2012
	 * @version Nov 12, 2012
	 */
	private void buildLineCsvValues(final WizardResults wizardResults, final List<String> lineHeaders,
									final Map<String, String> lineValues, int answerExportLevel) {
		addLineCsvValue(COLUMN_NAME_ID, Long.toString( wizardResults.getId() ), lineHeaders, lineValues);
		addLineCsvValue(COLUMN_NAME_CREATED_DATE, CREATED_DATE_FORMAT.format( wizardResults.getCreationDate() ),
				lineHeaders, lineValues);
		addLineCsvValue(COLUMN_NAME_FACE_PREFIX, wizardResults.getFaceUrl(), lineHeaders, lineValues);
		addLineCsvValue(COLUMN_NAME_VOTING_STATE, wizardResults.getVotingRegionState(), lineHeaders, lineValues);
		addLineCsvValue(COLUMN_NAME_VOTING_REGION, wizardResults.getVotingRegionName(), lineHeaders, lineValues);
		addLineCsvValue(COLUMN_NAME_VOTER_TYPE, wizardResults.getVoterType(), lineHeaders, lineValues);
		addLineCsvValue( COLUMN_NAME_VOTER_HISTORY, wizardResults.getVoterHistory(), lineHeaders, lineValues );
		String accountCreated = wizardResults.getUser() == null ? "No" : "Yes";
		addLineCsvValue(COLUMN_ACCOUNT_CREATED, accountCreated, lineHeaders, lineValues );
		addLineCsvName(COLUMN_GROUP_NAME, wizardResults.getName(), lineHeaders, lineValues );
		addLineCsvName(COLUMN_GROUP_PREVIOUS_NAME, wizardResults.getPreviousName(), lineHeaders, lineValues);
		addLineCsvValue( COLUMN_NAME_EMAIL_ADDRESS, wizardResults.getUsername(), lineHeaders, lineValues );
		addLineCsvValue(COLUMN_NAME_ALTERNATE_EMAIL_ADDRESS, wizardResults.getAlternateEmail(), lineHeaders,
				lineValues);
		addLineCsvValue(COLUMN_NAME_PHONE_NUMBER, wizardResults.getPhone(), lineHeaders, lineValues);
		addLineCsvValue(COLUMN_NAME_PHONE_TYPE, wizardResults.getPhoneType(), lineHeaders, lineValues  );
		addLineCsvValue(COLUMN_NAME_ALTERNATE_PHONE_NUMBER, wizardResults.getAlternatePhone(), lineHeaders,
				lineValues);
		addLineCsvValue(COLUMN_NAME_ALTERNATE_PHONE_NUMBER_TYPE, wizardResults.getAlternatePhoneType(), lineHeaders,
				lineValues);
		addLineCsvValue(COLUMN_NAME_BIRTH_DATE, convertBirthDate( wizardResults ), lineHeaders, lineValues);
		addLineCsvValue(COLUMN_NAME_GENDER, wizardResults.getGender(), lineHeaders, lineValues);
		addLineCsvAddress(COLUMN_GROUP_VOTING_ADDRESS, wizardResults.getVotingAddress(), lineHeaders, lineValues);
		addLineCsvAddress(COLUMN_GROUP_CURRENT_ADDRESS, wizardResults.getCurrentAddress(), lineHeaders, lineValues);
		addLineCsvAddress(COLUMN_GROUP_FORWARDING_ADDRESS, wizardResults.getForwardingAddress(), lineHeaders, lineValues);
		addLineCsvAddress(COLUMN_GROUP_PREVIOUS_ADDRESS, wizardResults.getPreviousAddress(), lineHeaders, lineValues);
		addLineCsvAnswers( convertAnswers( wizardResults.getAnswers(), answerExportLevel ), lineHeaders, lineValues);
	}

	/**
   	 * Merges the line headers into the headers list for the CSV. The idea here is to ensure that the order of the line headers
   	 * stays the same.
   	 *
   	 * @author IanBrown
   	 * @param headers
   	 *            the headers.
   	 * @param lineHeaders
   	 *            the line headers.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void mergeCsvHeaders(final List<String> headers, final List<String> lineHeaders) {
   		if (headers.isEmpty()) {
   			headers.addAll(lineHeaders);
   		} else {
   			List<String> newHeaders = null;
   			for (final String lineHeader : lineHeaders) {
   				int lineHeaderIdx = headers.indexOf(lineHeader);
   				if (lineHeaderIdx < 0) {
   					if (newHeaders == null) {
   						newHeaders = new LinkedList<String>();
   					}
   					newHeaders.add(lineHeader);
   				} else {
   					if (newHeaders != null) {
   						for (final String newHeader : newHeaders) {
   							headers.add(lineHeaderIdx, newHeader);
   							++lineHeaderIdx;
   						}
   						newHeaders = null;
   					}
   				}
   			}

   			if (newHeaders != null) {
   				headers.addAll(newHeaders);
   			}
   		}
   	}

   	/**
   	 * Quotes a value for the CSV file.
   	 *
   	 * @author IanBrown
   	 * @param value
   	 *            the value.
   	 * @return the quoted value.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private String quoteCsvValue(final String value) {
   		final StringBuilder sb = new StringBuilder("\"");
   		for (final StringTokenizer stok = new StringTokenizer(value, "\"", true); stok.hasMoreTokens();) {
   			final String token = stok.nextToken();
   			if (token.equals("\"")) {
   				sb.append("\"\"");
   			} else {
   				sb.append(token);
   			}
   		}
   		sb.append("\"");
   		return sb.toString();
   	}

   	/**
   	 * Writes the column headers for the CSV.
   	 *
   	 * @author IanBrown
   	 * @param headers
   	 *            the headers.
   	 * @param pw
   	 *            the print writer.
   	 * @since Nov 12, 2012
   	 * @version Nov 12, 2012
   	 */
   	private void writeCsvHeaders(final List<String> headers, final PrintWriter pw) {
   		String prefix = "";
   		for (final String header : headers) {
   			pw.append(prefix).append(header);
   			prefix = ",";
   		}
   		pw.println( "" );
   	}

   	/**
   	 * Write the values to the CSV values.
   	 *
   	 * @author IanBrown
   	 * @param headers
   	 *            the headers.
   	 * @param values
   	 *            the values.
   	 * @param pw
   	 *            the print writer.
   	 * @since Nov 12, 2012
   	 * @version Nov 13, 2012
   	 */
   	private void writeCsvValues(final List<String> headers, final List<Map<String, String>> values, final PrintWriter pw) {
   		String prefix = "";
   		for (final Map<String, String> lineValues : values) {
   			for (final String header : headers) {
   				final String value = lineValues.get(header);
   				pw.append(prefix);
   				if (value != null) {
   					pw.append(value);
   				}
   				prefix = ",";
   			}
   			pw.println( "" );
   			prefix = "";
   		}
   	}

    /**
   	 * Checks for an answer to a question about pending voter registrations. If there is no such question answered, then the method
   	 * return <code>true</code>. If there is such a question answered, then the answer must be "true" or the method returns
   	 * <code>false</code>.
   	 *
   	 * @author IanBrown
   	 * @param answers
   	 *            the answers.
   	 * @return <code>true</code> if there is no question or the question was answered in the affirmative, <code>false</code> if the
   	 *         question was declined.
   	 * @since Nov 28, 2012
   	 * @version Nov 29, 2012
   	 */
   	private boolean checkPendingVoterRegistrationAnswer(final Collection<Answer> answers) {
   		for (final Answer answer : answers) {
   			final QuestionField field = answer.getField();
   			if (ONLINE_DATA_TRANSFER_QUESTION.equals(field.getTitle())) {
   				final String value = answer.getValue();
   				return "TRUE".equalsIgnoreCase(value);
   			}
   		}

   		return true;
   	}



    /**
  	 * Converts the input wizard context to a pending voter registration.
  	 *
  	 * @author IanBrown
  	 * @param wizardContext
  	 *            the wizard context.
  	 * @return the pending voter registration.
  	 * @since Nov 5, 2012
  	 * @version Nov 28, 2012
  	 */
  	public PendingVoterRegistration convertWizardContext(final WizardContext wizardContext) {
  		final FlowType flowType = wizardContext.getFlowType();
  		if (flowType != FlowType.RAVA) {
  			throw new IllegalArgumentException("Cannot create pending voter registration from " + flowType + " flow");
  		}

  		final WizardResults wizardResults = wizardContext.getWizardResults();
  		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration =
                getPendingVoterRegistrationService().findPendingVoterRegistrationConfiguration(wizardContext, wizardResults);
  		if (pendingVoterRegistrationConfiguration == null) {
  			return null;
  		}

  		if (!checkPendingVoterRegistrationAnswer(wizardResults.getAnswers())) {
  			return null;
  		}

  		final PendingVoterRegistration pendingVoterRegistration = new PendingVoterRegistration();
  		//final VotingRegion votingRegion = wizardResults.getVotingRegion();
  		pendingVoterRegistration.setCreatedDate( wizardResults.getCreationDate() );
  		pendingVoterRegistration.setVotingState( wizardResults.getVotingRegionState() );
  		pendingVoterRegistration.setVotingRegion( wizardResults.getVotingRegionName() );
  		pendingVoterRegistration.setVoterType( wizardResults.getVoterType() );
  		pendingVoterRegistration.setVoterHistory( wizardResults.getVoterHistory() );
        pendingVoterRegistration.setFacePrefix( wizardContext.getCurrentFace().getRelativePrefix() );
  		pendingVoterRegistration.setName( convertName( wizardResults.getName() ) );
  		pendingVoterRegistration.setPreviousName( convertName( wizardResults.getPreviousName() ) );
  		pendingVoterRegistration.setEmailAddress( wizardResults.getUsername() );
  		pendingVoterRegistration.setAlternateEmailAddress( wizardResults.getAlternateEmail() );
  		pendingVoterRegistration.setPhoneNumber( wizardResults.getPhone() );
		pendingVoterRegistration.setPhoneNumberType( wizardResults.getPhoneType() );
		pendingVoterRegistration.setAlternatePhoneNumber(wizardResults.getAlternatePhone());
		pendingVoterRegistration.setAlternatePhoneNumberType(wizardResults.getAlternatePhoneType());
  		final String birthDay = convertBirthDate( wizardResults );
  		pendingVoterRegistration.setBirthDay(birthDay);
  		pendingVoterRegistration.setGender(wizardResults.getGender());
  		pendingVoterRegistration.setVotingAddress(convertAddress(wizardResults.getVotingAddress()));
  		pendingVoterRegistration.setCurrentAddress(convertAddress(wizardResults.getCurrentAddress()));
  		pendingVoterRegistration.setForwardingAddress(convertAddress(wizardResults.getForwardingAddress()));
  		pendingVoterRegistration.setPreviousAddress(convertAddress(wizardResults.getPreviousAddress()));
  		pendingVoterRegistration.setAnswers(convertAnswers(wizardResults.getAnswers(), pendingVoterRegistrationConfiguration));
  		return pendingVoterRegistration;
  	}

	private String convertBirthDate( final WizardResults wizardResults ) {
		final Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, wizardResults.getBirthYear());
		calendar.set(Calendar.MONTH, wizardResults.getBirthMonth()-1 );
		calendar.set(Calendar.DAY_OF_MONTH, wizardResults.getBirthDate());
		return PendingVoterRegistration.BIRTH_DAY_FORMAT.format(calendar.getTime());
	}

   	/**
   	 * Converts the input address.
   	 *
   	 * @author IanBrown
   	 * @param address
   	 *            the address.
   	 * @return the pending voter address.
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	private PendingVoterAddress convertAddress(final WizardResultAddress address) {
   		if (address == null) {
   			return null;
   		}

   		final PendingVoterAddress pendingVoterAddress = new PendingVoterAddress();
   		pendingVoterAddress.setStreet1(address.getStreet1());
   		pendingVoterAddress.setStreet2(address.getStreet2());
   		pendingVoterAddress.setDescription(address.getDescription());
   		pendingVoterAddress.setCity(address.getCity());
   		pendingVoterAddress.setStateOrRegion(address.getState());
   		pendingVoterAddress.setPostalCode(address.getZip()
   				+ (address.getZip4() == null || address.getZip4().trim().isEmpty() ? "" : "-" + address.getZip4()));
   		pendingVoterAddress.setCountry(address.getCountry());
   		return pendingVoterAddress;
   	}

   	/**
   	 * Converts the answer.
   	 *
   	 * @author IanBrown
   	 * @param answer
   	 *            the answer.
   	 * @return the pending voter answer.
   	 * @since Nov 5, 2012
   	 * @version Nov 29, 2012
   	 */
   	private PendingVoterAnswer convertAnswer(final Answer answer, final PendingVoterRegistrationConfiguration configuration ) {
   		final QuestionField field = answer.getField();
   		if (field.getInPdfName() != null && !field.getInPdfName().trim().isEmpty() && !field.isSecurity() ) {
               boolean doConvert = false;
               if ( answer.getId() == null ) {
                   doConvert = true;
               }
               else {
                   QuestionVariant v = getQuestionnaireService().findQuestionVariantById( field.getQuestion().getId() );
                   String dependency = v.getDependencyDescription();
                   int exportLevel = configuration.getExportAnswersLevel();
                   doConvert = exportLevel == PendingVoterRegistrationConfiguration.EXPORT_ALL
                           || ( exportLevel == PendingVoterRegistrationConfiguration.EXPORT_FACE &&  dependency.contains("Face"))
                           || ( exportLevel == PendingVoterRegistrationConfiguration.EXPORT_STATE && dependency.contains("VotingState"));
               }
               if ( doConvert ) {
                   final PendingVoterAnswer pendingVoterAnswer = new PendingVoterAnswer();
                   pendingVoterAnswer.setQuestion(field.getTitle());
                   String answerValue = answer.getValue();
                   if ( answer instanceof PredefinedAnswer && answerValue.contains("=") ) {
                       String[] parts = answerValue.split("=");
                       if ( parts.length > 1 ) {
                           answerValue = parts[1];
                       }
                   }
                   pendingVoterAnswer.setAnswer( answerValue.replaceAll("\\n"," ") );
                   return pendingVoterAnswer;
               }
   		}

   		return null;
   	}

   	/**
   	 * Converts the answers.
   	 *
   	 * @author IanBrown
   	 * @param answers
   	 *            the answers.
   	 * @return the pending voter answers.
   	 * @since Nov 5, 2012
   	 * @version Nov 29, 2012
   	 */
   	private List<PendingVoterAnswer> convertAnswers(final Collection<Answer> answers, final PendingVoterRegistrationConfiguration configuration ) {
   		if (answers == null) {
   			return null;
   		}

   		final List<PendingVoterAnswer> pendingVoterAnswers = new LinkedList<PendingVoterAnswer>();
   		for (final Answer answer : answers) {
   			final PendingVoterAnswer pendingVoterAnswer = convertAnswer(answer, configuration);
   			if (pendingVoterAnswer != null) {
   				pendingVoterAnswers.add(pendingVoterAnswer);
   			}
   		}
   		return pendingVoterAnswers;
   	}

	/**
	 * Converts the answer.
	 *
	 * @author IanBrown
	 * @param answer
	 *            the answer.
	 * @return the pending voter answer.
	 * @since Nov 5, 2012
	 * @version Nov 29, 2012
	 */
	private PendingVoterAnswer convertAnswer(final Answer answer, final int exportLevel ) {
		final QuestionField field = answer.getField();
		if (field.getInPdfName() != null && !field.getInPdfName().trim().isEmpty() && !field.isSecurity() ) {
			boolean doConvert = false;
			if ( answer.getId() == null ) {
				doConvert = true;
			}
			else {
				QuestionVariant v = getQuestionnaireService().findQuestionVariantById( field.getQuestion().getId() );
				String dependency = v.getDependencyDescription();
				doConvert = exportLevel == PendingVoterRegistrationConfiguration.EXPORT_ALL
						|| ( exportLevel == PendingVoterRegistrationConfiguration.EXPORT_FACE &&  dependency.contains("Face"))
						|| ( exportLevel == PendingVoterRegistrationConfiguration.EXPORT_STATE && dependency.contains("VotingState"));
			}
			if ( doConvert ) {
				final PendingVoterAnswer pendingVoterAnswer = new PendingVoterAnswer();
				pendingVoterAnswer.setQuestion(field.getTitle());
				String answerValue = answer.getValue();
				if ( answer instanceof PredefinedAnswer && answerValue.contains("=") ) {
					String[] parts = answerValue.split("=");
					if ( parts.length > 1 ) {
						answerValue = parts[1];
					}
				}
				pendingVoterAnswer.setAnswer( answerValue.replaceAll("\\n"," ") );
				return pendingVoterAnswer;
			}
		}

		return null;
	}

	/**
	 * Converts the answers.
	 *
	 * @author IanBrown
	 * @param answers
	 *            the answers.
	 * @return the pending voter answers.
	 * @since Nov 5, 2012
	 * @version Nov 29, 2012
	 */
	private List<PendingVoterAnswer> convertAnswers(final Collection<Answer> answers, final int exportLevel ) {
		if (answers == null) {
			return null;
		}

		final List<PendingVoterAnswer> pendingVoterAnswers = new LinkedList<PendingVoterAnswer>();
		for (final Answer answer : answers) {
			final PendingVoterAnswer pendingVoterAnswer = convertAnswer(answer, exportLevel );
			if (pendingVoterAnswer != null) {
				pendingVoterAnswers.add(pendingVoterAnswer);
			}
		}
		return pendingVoterAnswers;
	}

	/**
   	 * Converts the input name object.
   	 *
   	 * @author IanBrown
   	 * @param name
   	 *            the name.
   	 * @return the converted name.
   	 * @since Nov 5, 2012
   	 * @version Nov 5, 2012
   	 */
   	private PendingVoterName convertName(final WizardResultPerson name) {
   		if (name == null) {
   			return null;

   		}
   		final PendingVoterName pendingVoterName = new PendingVoterName();
   		pendingVoterName.setTitle(name.getTitle());
   		pendingVoterName.setFirstName(name.getFirstName());
   		pendingVoterName.setMiddleName(name.getMiddleName());
   		pendingVoterName.setLastName(name.getLastName());
   		pendingVoterName.setSuffix(name.getSuffix());
   		return pendingVoterName;
   	}


    public PendingVoterRegistrationService getPendingVoterRegistrationService() {
        return pendingVoterRegistrationService;
    }

    public PendingVoterRegistrationCipher getPendingVoterRegistrationCipher() {
        return pendingVoterRegistrationCipher;
    }

    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }
}
