/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.Election;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.VirtualDictionaryItem;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.LocalOfficialService;

/**
 * Extended {@link AbstractFieldFillerPageAddOn} that provides information from the SVID.
 * 
 * @author IanBrown
 * 
 * @since Apr 24, 2012
 * @version Jul 11, 2012
 */
@Component
public class SvidPageAddOn extends AbstractFieldFillerPageAddOn {

	/**
	 * Comparator for elections.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private static final class ElectionComparator implements Comparator<Election> {

		/** {@inheritDoc} */
		@Override
		public final int compare(final Election o1, final Election o2) {
			final Date date1 = determineDate(o1);
			final Date date2 = determineDate(o2);
			return date1 == null ? date2 == null ? 0 : -1 : date2 == null ? 1 : date1.compareTo(date2);
		}

		/**
		 * Determines the date for the election.
		 * 
		 * @author IanBrown
		 * @param election
		 *            the election.
		 * @return the date.
		 * @since Apr 24, 2012
		 * @version Apr 24, 2012
		 */
		public final Date determineDate(final Election election) {
			final String heldOn = election.getHeldOn();
			Date date = null;
			for (final SimpleDateFormat supportDateFormat : SvidPageAddOn.SUPPORTED_DATE_FORMATS) {
				try {
					date = supportDateFormat.parse(heldOn);
					return date;
				} catch (final ParseException e) {
					date = null;
				}
			}

			return date;
		}
	}

	/**
	 * the marker used to indicate that SVID elections should be filled in for a question field.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	static final String ELECTION_VARIABLE = "{SVID.elections}";

	/**
	 * the variable name for a no-input SVID election field.
	 * 
	 * @author IanBrown
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	static final String NO_INPUT_ELECTION_VARIABLE = "SVID Election";

	/**
	 * the string used when there is no upcoming election.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	static final String NO_UPCOMING_ELECTION = "No upcoming election";

	/**
	 * the date formats supported.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	static final SimpleDateFormat[] SUPPORTED_DATE_FORMATS = //
	{ new SimpleDateFormat("MMMM dd, yyyy"), //
			new SimpleDateFormat("MMM dd, yyyy"), //
			new SimpleDateFormat("dd-MMM-yyyy"), //
			new SimpleDateFormat("dd/MMM/yyyy"), //
			new SimpleDateFormat("MM/dd/yyyy") };

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@Autowired
	private LocalOfficialService localOfficialService;

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	public LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	public void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

	/** {@inheritDoc} */
	@Override
	protected void prepareAddOnVariant(final WizardContext form, final QuestionVariant variant) {
		final Collection<QuestionField> fields = variant.getFields();

		if (fields != null) {
			List<Election> upComingElections = new LinkedList<Election>();

			final WizardResults wizardResults = form.getWizardResults();
			final VotingRegion votingRegion = wizardResults.getVotingRegion();
			if (votingRegion != null) {
				final State state = votingRegion.getState();
				final StateSpecificDirectory svid = getLocalOfficialService().findSvidForState(state);
				if (svid != null) {
					final Collection<?> elections = svid.getElections();
					if (elections != null) {
						upComingElections = findUpComingElections(elections);
					}
				}
			}

			for (final QuestionField field : fields) {
				prepareAddOnField(upComingElections, field);
			}
		}
	}

	/**
	 * Finds the elections in the input collection that are coming up. Because elections have a string for the held on field rather
	 * than a date, this method has to interpret the value. Any value that is identified is compared to the current date and the
	 * election is returned if the current date is before the held on date. Any held on string that cannot be identified is assumed
	 * to be up-coming and the election is returned.
	 * 
	 * @author IanBrown
	 * @param elections
	 *            the collection of elections - note that it is possible to get non-election objects as this is a simple collection.
	 *            We'll ignore those.
	 * @return the list of up-coming elections, sorted by date (unidentified dates are considered last).
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private List<Election> findUpComingElections(final Collection<?> elections) {
		final List<Election> upComingElections = new ArrayList<Election>();
		final ElectionComparator electionComparator = new ElectionComparator();
		final Date now = new Date();
		for (final Object electionObject : elections) {
			if (electionObject instanceof Election) {
				final Election election = (Election) electionObject;
				final Date electionDate = electionComparator.determineDate(election);
				if (electionDate == null || electionDate.after(now)) {
					upComingElections.add(election);
				}
			}
		}
		Collections.sort(upComingElections, electionComparator);
		return upComingElections;
	}

	/**
	 * Prepares an add on field by filling in the SVID election information if the field calls for it.
	 * <p>
	 * There are two possibilities:
	 * <ol>
	 * <li>The field is a radio/select type with a title contains the string <code>ELECTION_VARAIBLE</code>, or</li>
	 * <li>The field is a no input type with a title that starts with the string <code>NO_INPUT_ELECTION_VARIABLE</code>.</li>
	 * </ol>
	 * 
	 * @author IanBrown
	 * @param upComingElections
	 *            the list of upcoming elections.
	 * @param field
	 *            the field.
	 * @since Apr 24, 2012
	 * @version Jul 11, 2012
	 */
	private void prepareAddOnField(final List<Election> upComingElections, final QuestionField field) {
		final String fieldTitle = field.getTitle();
		final int evIdx = fieldTitle.indexOf(ELECTION_VARIABLE);
		if (evIdx >= 0) {
			prepareAddOnFieldWithElectionOptions(upComingElections, field, fieldTitle, evIdx);
		} else if (fieldTitle.startsWith(NO_INPUT_ELECTION_VARIABLE)) {
			prepageAddOnFieldWithElectionText(upComingElections, field, fieldTitle);
		}
	}

	/**
	 * Prepares the add on field (if it is a no-input field) by using the appropriate election as the text.
	 * @author IanBrown
	 * @param upComingElections the up-coming elections.
	 * @param field the field.
	 * @param fieldTitle the title of the field - should end with a numeric index for the election field. If not, the field is skipped.
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	private void prepageAddOnFieldWithElectionText(List<Election> upComingElections, QuestionField field, String fieldTitle) {
		final int length = NO_INPUT_ELECTION_VARIABLE.length();
		if (fieldTitle.trim().length() == length) {
			return;
		}
		String electionIndexString = fieldTitle.substring(length).trim();
		if (!electionIndexString.matches("[0-9]+")) {
			return;
		}
		final int electionIndex = Integer.parseInt(electionIndexString) - 1;
		if (electionIndex < upComingElections.size()) {
			final Election upComingElection = upComingElections.get(electionIndex);
			field.setHelpText(upComingElection.getTitle().trim() + " held on " + upComingElection.getHeldOn().trim());
		}
	}

	/**
	 * Prepares the field (if it is one that takes options) by providing option values for each of the elections.
	 * @author IanBrown
	 * @param upComingElections the up-coming elections.
	 * @param field the field.
	 * @param fieldTitle the title of the field.
	 * @param evIdx the index of the <code>ELECTION_VARIABLE</code> string in the field title.
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	private void prepareAddOnFieldWithElectionOptions(final List<Election> upComingElections, final QuestionField field,
			final String fieldTitle, final int evIdx) {
		final String fieldTitlePrefix = fieldTitle.substring(0, evIdx).trim();
		final int afterEvIdx = evIdx + ELECTION_VARIABLE.length();
		if (afterEvIdx >= fieldTitle.length()) {
			field.setTitle(fieldTitlePrefix);
		} else {
			final String fieldTitleSuffix = fieldTitle.substring(afterEvIdx).trim();
			field.setTitle(fieldTitlePrefix + " " + fieldTitleSuffix);
		}
		final FieldType fieldType = field.getType();
		final String templateName = fieldType.getTemplateName();
		if ((FieldType.TEMPLATE_RADIO.equals(templateName) || FieldType.TEMPLATE_SELECT.equals(templateName))
				&& fieldType.isGenericOptionsAllowed()) {
			field.setGenericOptions(new LinkedList<FieldDictionaryItem>());
			for (final Election upComingElection : upComingElections) {
				final VirtualDictionaryItem electionItem = new VirtualDictionaryItem();
				electionItem.setForField(field);
				electionItem.setValue(upComingElection.getTitle().trim() + " held on " + upComingElection.getHeldOn().trim());
				field.getGenericOptions().add(electionItem);
			}
			if (field.getGenericOptions().isEmpty()) {
				final VirtualDictionaryItem electionItem = new VirtualDictionaryItem();
				electionItem.setForField(field);
				electionItem.setValue(NO_UPCOMING_ELECTION);
				field.getGenericOptions().add(electionItem);
			}
		}
	}

	/**
	 * Retrieves the upcoming elections from the form.
	 * 
	 * @author IanBrown
	 * @param form
	 *            the wizard context form.
	 * @return the list of upcoming elections.
	 * @since Apr 24, 2012
	 * @version Jul 11, 2012
	 */
	@SuppressWarnings("unused")
	private final List<Election> retrieveUpComingElectionsFromForm(final WizardContext form) {
		List<Election> upComingElections = new LinkedList<Election>();

		final WizardResults wizardResults = form.getWizardResults();
		final VotingRegion votingRegion = wizardResults.getVotingRegion();
		if (votingRegion != null) {
			final State state = votingRegion.getState();
			final StateSpecificDirectory svid = getLocalOfficialService().findSvidForState(state);
			if (svid != null) {
				final Collection<?> elections = svid.getElections();
				if (elections != null) {
					upComingElections = findUpComingElections(elections);
				}
			}
		}

		return upComingElections;
	}
}
