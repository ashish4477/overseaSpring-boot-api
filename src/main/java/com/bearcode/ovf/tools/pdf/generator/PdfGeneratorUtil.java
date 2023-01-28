package com.bearcode.ovf.tools.pdf.generator;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfGeneratorUtil {

    private static final String FILE_NAME_TEMPLATE = "%state-%flow-%year.pdf";

    /**
	 * Returns formatted output file name
	 * 
	 * @param fileNameMask  file name template
     * @param params params
	 * 
	 * @return output file name
	 * 
	 */
	public static String getFileName(String fileNameMask, Map<String, String> params) {
		if (fileNameMask == null)
			throw new IllegalArgumentException("fileNameMask");

		if (params == null || params.isEmpty()) {
			return fileNameMask;
		}

		String fileName = fileNameMask;
		for (final Map.Entry<String, String> it : params.entrySet()) {
			fileName = fileName.replaceAll(it.getKey(), it.getValue());
		}
		return fileName;
	}

    /**
     * Build formatted file name of the form file to be sent to user
     * @param context
     *      the wizard context
     * @return  formatted name
     */
    public static String getFileName( final WizardContext context ) {
        return getFileName(FILE_NAME_TEMPLATE, getFileNameParameters(context));
    }

    public static Map<String, String> getFileNameParameters( final WizardContext context ) {
   		final Calendar now = Calendar.getInstance();
   		final Map<String, String> params = new HashMap<String, String>();
   		params.put("%year", Integer.toString(now.get(Calendar.YEAR)));
   		params.put("%month", String.format("%02d", now.get(Calendar.MONTH) + 1));
   		params.put("%day_of_month", String.format("%02d", now.get(Calendar.DAY_OF_MONTH)));
   		params.put("%hour", String.format("%02d", now.get(Calendar.HOUR_OF_DAY)));
   		params.put("%minute", String.format("%02d", now.get(Calendar.MINUTE)));
   		params.put("%second", String.format("%02d", now.get(Calendar.SECOND)));
        final WizardResults wizardResults = context.getWizardResults();
        final WizardResultAddress votingAddress = wizardResults.getVotingAddress();
        final String state = votingAddress.getState();
        params.put("%state", getStatePlusElection( wizardResults ));
        params.put("%flow", wizardResults.getFlowType().getFileNameParam());
        return params;
   	}

	public static String getStatePlusElection( final WizardResults results ) {
        String electionName = getElectionName( results );
		final WizardResultAddress votingAddress = results.getVotingAddress();
		String state = votingAddress.getState().toUpperCase();
		if ( !StringUtils.isBlank( electionName ) ) {
			if ( electionName.matches( ".*[\\-_].*" ) ) {
				String parts[] = electionName.split( "[\\-_]" );
                StringBuilder builder = new StringBuilder();
                for ( String part : parts ) {
                    if ( builder.length() > 0 ) builder.append( "-" );
                    builder.append( StringUtils.capitalize( part ) );
                }
				electionName = builder.toString();
			}
            else {
                electionName = StringUtils.capitalize( electionName );
            }
			state = state + "-" + electionName;
		}
		return state;
	}

    public static String getElectionName( final WizardResults results ) {
        String electionName = null;
        Pattern pattern = Pattern.compile( "ucElection(.*)" );
        for ( Answer answer : results.getAnswers() ) {
            Matcher matcher = pattern.matcher( answer.getField().getInPdfName() );
            if ( matcher.matches() ) {
                String inName = matcher.group( 1 );
                if ( StringUtils.isBlank( inName ) ) {
                    electionName = answer.getValue();
                } else {
                    electionName = inName;
                }
                break;
            }
        }
        if ( !StringUtils.isBlank( electionName ) && electionName.matches( ".*=.*" ) ) {
            String parts[] = electionName.split( "=" );
            electionName = parts[0];
        }
        return electionName;
    }
}
