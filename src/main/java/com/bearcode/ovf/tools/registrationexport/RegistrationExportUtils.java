package com.bearcode.ovf.tools.registrationexport;

import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.ExportLevel;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by leonid on 28.09.16.
 */
@Component
public class RegistrationExportUtils  implements PendingVoterRegistrationDictionary {

    @Autowired
    private QuestionnaireService questionnaireService;


    /**
     * the date formatter used for birth days.
     *
     * @author IanBrown
     * @since Nov 26, 2012
     * @version Nov 26, 2012
     */
    public static final SimpleDateFormat BIRTH_DAY_FORMAT = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

    /**
     * Creates a CSV file.
     *
     * @param wizardResultsList
     *            the pending voter registrations.
     * @param answerExportLevel export level for answers
     * @return the CSV file as a byte array.
     * @since Sep 13, 2016
     */
    public byte[] createCsv(final List<WizardResults> wizardResultsList, ExportLevel answerExportLevel) {
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
     * Adds the voter answers to the CSV line values.
     *
     * @author IanBrown
     * @param answersMap
     *            the voter answers.
     * @param lineHeaders
     *            the list of line column headers.
     * @param lineValues
     *            the map of the line values by header.
     * @since Nov 12, 2012
     * @version Nov 12, 2012
     */
    private void addLineCsvAnswers(final Map<String, String> answersMap, final List<String> lineHeaders,
                                   final Map<String, String> lineValues) {
        for ( String answerKey : answersMap.keySet() ) {
            addLineCsvValue( answerKey, answersMap.get( answerKey ), lineHeaders, lineValues );
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
                                final List<Map<String, String>> values, ExportLevel answerExportLevel) {
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
                                    final Map<String, String> lineValues, ExportLevel answerExportLevel) {
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
        addLineCsvValue( COLUMN_FORM_DOWNLOADED, wizardResults.isDownloaded() ? "Yes" : "No", lineHeaders, lineValues );
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
        sb.append( "\"" );
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
            pw.append(prefix).append( header );
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




    private String convertBirthDate( final WizardResults wizardResults ) {
        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, wizardResults.getBirthYear());
        calendar.set(Calendar.MONTH, wizardResults.getBirthMonth()-1 );
        calendar.set( Calendar.DAY_OF_MONTH, wizardResults.getBirthDate() );
        return BIRTH_DAY_FORMAT.format(calendar.getTime());
    }

    /**
     * Converts the answer.
     * @param answersMap map for question title and answer value
     * @param answer
     *            the answer.
     * @param exportLevel level of export
     * @return the pending voter answer.
     */
    private void addAnswerToMap( final Map<String, String> answersMap, final Answer answer, final ExportLevel exportLevel ) {
        final QuestionField field = answer.getField();
        if (field != null && field.getInPdfName() != null && !field.getInPdfName().trim().isEmpty() && !field.isSecurity() ) {
            boolean doConvert = false;
            if ( answer.getId() == null ) {
                doConvert = true;
            }
            else {
                QuestionVariant v = getQuestionnaireService().findQuestionVariantById( field.getQuestion().getId() );
                String dependency = v.getDependencyDescription();
                doConvert = exportLevel == ExportLevel.EXPORT_ALL
                        || ( exportLevel == ExportLevel.EXPORT_FACE &&  dependency.contains("Face"))
                        || ( exportLevel == ExportLevel.EXPORT_STATE && dependency.contains("VotingState"));
            }
            if ( doConvert ) {
                String question = field.getTitle();
                String answerValue = answer.getValue();
                if ( answer instanceof PredefinedAnswer && answerValue.contains("=") ) {
                    String[] parts = answerValue.split("=");
                    if ( parts.length > 1 ) {
                        answerValue = parts[1];
                    }
                }
                answerValue = answerValue.replaceAll("\\n"," ");
                answersMap.put( question, answerValue );
            }
        }
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
    private Map<String,String> convertAnswers(final Collection<Answer> answers, final ExportLevel exportLevel ) {
        if (answers == null) {
            return null;
        }
        Map<String,String> answersMap = new HashMap<String, String>();
        for (final Answer answer : answers) {
            addAnswerToMap( answersMap, answer, exportLevel );
        }
        return answersMap;
    }


    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

}
