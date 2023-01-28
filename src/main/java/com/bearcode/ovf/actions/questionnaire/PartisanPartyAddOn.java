/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.vip.VipBallotCandidate;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link AllowedForAddOn} to ask the voter what party they wish to vote for in
 * partisan races.
 * 
 * @author Ian Brown
 */
@Component
public class PartisanPartyAddOn extends AbstractAllowedForAddOn implements
    AllowedForAddOn {

	/**
	 * the title for the partisan field.
	 */
	static final String PARTISAN_FIELD_TITLE = "Partisan Party";

	/**
	 * the logger for the class.
	 */
	private static Logger LOGGER = LoggerFactory
	    .getLogger(PartisanPartyAddOn.class);

	/**
	 * the title for the header field.
	 */
	static final String HEADER_FIELD_TITLE = "";

	/**
	 * the name of the group containing the headers.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	static final String HEADER_GROUP_NAME = "header";

	/**
	 * the default title for the header group.
	 */
	static final String HEADER_GROUP_TITLE = "Partisan Elections";

	/**
	 * the default header text.
	 */
	static final String[] HEADER_TEXT = { "<p>You may vote in both partisan and non-partisan races.</p>" };

	/**
	 * the SVR property that provides the text for the partisan header.
	 */
	static final String HEADER_TEXT_PROPERTY = "partisan.header.text";

	/**
	 * the SVR property that provides the title for the partisan headers.
	 */
	static final String HEADER_TITLE_PROPERTY = "partisan.header.title";

	/**
	 * the title for the variant containing the header.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	static final String HEADER_VARIANT_TITLE = "";

	/**
	 * the name of the partisan field.
	 */
	static final String PARTISAN_FIELD_NAME = "partisanParty";

	/**
	 * the partisan group name.
	 */
	static final String PARTISAN_GROUP_NAME = "partisan";

	/**
	 * the title for the partisan group.
	 */
	static final String PARTISAN_GROUP_TITLE = "Please select a party for the partisan races:";

	/**
	 * the text for the partisan group.
	 */
	static final String[] PARTISAN_TEXT = {
	    "<p>For partisan races, you are allowed to vote for only one party.</p>",
	    "<p>You will be allowed to vote in all non-partisan races, regardless of the party of the candidates.</p>" };

	/**
	 * the property for the partisan text.
	 */
	static final String PARTISAN_TEXT_PROPERTY = "partisan.text";

	/**
	 * the SVR property that provides the title for the partisan group.
	 */
	static final String PARTISAN_TITLE_PROPERTY = "partisan.title";

	/**
	 * the title for the partisan variant.
	 */
	static final String PARTISAN_VARIANT_TITLE = "";

	/**
	 * the SVR property to get the string presented to the user for the case where no property is selected.
	 */
	public static final String NO_PARTY_PROPERTY = "partisan.no_party";

	/**
	 * the default string used when no party is selected.
	 */
	public static final String DEFAULT_NO_PARTY_NAME = "Non-Partisan Contests Only";

	/** the default party name for a non-partisan party. */
	public static final String DEFAULT_NON_PARTISAN_PARTY = "NONPARTISAN";

	/**
	 * the election service.
	 */
	@Autowired
	private ElectionService electionService;

	/**
	 * the voting precinct service.
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;


	@Autowired
	private StateService stateService;

	/**
	 * Constructs a
	 */
	public PartisanPartyAddOn() {
		setValet(PageAddOnValetImpl.getInstance());
	}

	/** {@inheritDoc} */
	@Override
	public Long getFirstFieldId(final QuestionnairePage currentPage) {
		return firstFieldId + currentPage.getStepNumber() * 1000;
	}

	/** {@inheritDoc} */
	@Override
	public void prepareAddOnPage(final WizardContext form,
	    final QuestionnairePage currentPage) {

		// Fill page if this is FWAB flow. Leave it intact otherwise, it will be
		// skipped.
		if (form.getFlowType() == FlowType.FWAB) {
			final WizardResults wizardResults = form.getWizardResults();
			final VotingRegion votingRegion = wizardResults.getVotingRegion();
			final State votingState;
			final String stateAbbreviation;
			final String votingRegionName;
			if ( votingRegion != null ) {
				votingState = votingRegion.getState();

				stateAbbreviation = votingState.getAbbr();
				votingRegionName = votingRegion.getName();
			}
			else {
				stateAbbreviation = wizardResults.getVotingRegionState();
				votingRegionName = wizardResults.getVotingRegionName();

				votingState = stateService.findByAbbreviation( stateAbbreviation );
			}
			if (getVotingPrecinctService().isReady(stateAbbreviation,
			    votingRegionName)
			    && getElectionService().isReady(stateAbbreviation, votingRegionName)) {
				final WizardResultAddress votingAddress = wizardResults
				    .getVotingAddress();
				final ValidAddress validAddress = getVotingPrecinctService()
				    .validateAddress(votingAddress, votingState);
				if (validAddress != null) {
					try {
						buildPageForAddress(form, currentPage, validAddress,
						    stateAbbreviation, votingRegionName);
						return;
					} catch (final RuntimeException e) {
						// Propagate run-time exceptions normally.
						throw e;
					} catch (final Exception e) {
						// Log and ignore other exceptions - the page will not
						// be displayed.
						LOGGER.warn("Unable to build state contests page for "
						    + validAddress.getValidatedAddress().getSingleLineAddress());
					}
				}
			}
		}
	}

	/**
	 * Builds the header group.
	 * 
	 * @param currentPage
	 *          the current page.
	 * @param softId
	 *          the current identifier.
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param noInputFieldType
	 *          the no-input field type.
	 * @return the new identifier.
	 */
	private long buildHeaderGroup(final QuestionnairePage currentPage, final long softId,
	    final String stateAbbreviation, final String votingRegionName,
	    final FieldType noInputFieldType) {
		long id = softId;
		final String headerTitle = singlelineProperty(stateAbbreviation,
		    votingRegionName, HEADER_TITLE_PROPERTY, HEADER_GROUP_TITLE);
		final Question headerGroup = getValet().createGroup(currentPage, id,
		    HEADER_GROUP_NAME, headerTitle);
		++id;
		final QuestionVariant headerVariant = getValet().createVariant(headerGroup,
		    id, HEADER_VARIANT_TITLE);
		++id;
		final String headerText = multilineProperty(stateAbbreviation,
		    votingRegionName, HEADER_TEXT_PROPERTY, HEADER_TEXT);
		getValet().createField(headerVariant, id, noInputFieldType,
		    HEADER_FIELD_TITLE, headerText);
		++id;
		return id;
	}

	/**
	 * Builds the page for the specified address.
	 * 
	 * @param form
	 *          the wizard context form.
	 * @param currentPage
	 *          the current page.
	 * @param validAddress
	 *          the validated address.
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @throws Exception
	 *           if there is a problem finding the contests.
	 */
	private void buildPageForAddress(final WizardContext form,
	    final QuestionnairePage currentPage, final ValidAddress validAddress,
	    final String stateAbbreviation, final String votingRegionName) throws Exception {
		final List<VipContest> contests = getElectionService().findContests(
		    validAddress);

		// Leave page empty if there are no contests.
		if ((contests != null) && !contests.isEmpty()) {
			final List<VipContest> partisanContests = getElectionService().findPartisanContests(contests);

			if ((partisanContests != null) && !partisanContests.isEmpty()) {
				buildPageForPartisanContests(form, currentPage, stateAbbreviation,
				    votingRegionName, partisanContests);
			}
		}
	}

	/**
	 * Builds the page for the partisan contests.
	 * 
	 * @param form
	 *          the wizard context.
	 * @param currentPage
	 *          the current page.
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param partisanContests
	 *          the partisan contests.
	 */
	private void buildPageForPartisanContests(final WizardContext form,
	    final QuestionnairePage currentPage, final String stateAbbreviation,
	    final String votingRegionName, final List<VipContest> partisanContests) {
		long softId = getFirstFieldId(currentPage);
		final FieldType noInputFieldType = getQuestionFieldService()
		    .findFieldTypeByTemplate(FieldType.TEMPLATE_NOT_INPUT);
		final FieldType radioFieldType = getQuestionFieldService()
		    .findFieldTypeByTemplate(FieldType.TEMPLATE_RADIO);
		softId = buildHeaderGroup(currentPage, softId, stateAbbreviation,
		    votingRegionName, noInputFieldType);
		softId = buildPartisanGroup(currentPage, form, softId, stateAbbreviation,
		    votingRegionName, radioFieldType, partisanContests);
	}

	/**
	 * Builds the group showing the partisan parties.
	 * 
	 * @param currentPage
	 *          the current page.
	 * @param form
	 *          the wizard context form.
	 * @param softId
	 *          the current identifier.
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param radioFieldType
	 *          the radio button field type.
	 * @param partisanContests
	 *          the partisan contests.
	 * @return the new identifier.
	 */
	private long buildPartisanGroup(final QuestionnairePage currentPage,
	    final WizardContext form, final long softId, final String stateAbbreviation,
	    final String votingRegionName, final FieldType radioFieldType,
	    final List<VipContest> partisanContests) {
		long id = softId;
		final String partisanGroupTitle = singlelineProperty(stateAbbreviation,
		    votingRegionName, PartisanPartyAddOn.PARTISAN_TITLE_PROPERTY,
		    PartisanPartyAddOn.PARTISAN_GROUP_TITLE);
		final Question partisanGroup = getValet().createGroup(currentPage, id,
		    PartisanPartyAddOn.PARTISAN_GROUP_NAME, partisanGroupTitle);
		++id;
		final QuestionVariant partisanVariant = getValet().createVariant(
		    partisanGroup, id, PARTISAN_VARIANT_TITLE);
		++id;
		multilineProperty(stateAbbreviation, votingRegionName,
		    PartisanPartyAddOn.PARTISAN_TEXT_PROPERTY,
		    PartisanPartyAddOn.PARTISAN_TEXT);
		final String noPartyName = singlelineProperty(stateAbbreviation, votingRegionName, NO_PARTY_PROPERTY, DEFAULT_NO_PARTY_NAME);
		Answer answer = form.getAnswerByFieldId(id);
		final QuestionField partisanField;
		if (answer == null) {
			partisanField = getValet().createField(partisanVariant, id,
			    radioFieldType, PartisanPartyAddOn.PARTISAN_FIELD_TITLE, "");
			answer = partisanField.createAnswer();
			form.putAnswer(answer);
		} else {
			partisanField = answer.getField();
			partisanField.setQuestion(partisanVariant);
			partisanVariant.getFields().add(partisanField);
		}
		++id;
		partisanField.setRequired(false);
		partisanField.setSecurity(true);
		final List<String> partyNames = findPartisanPartyNames(partisanContests, noPartyName);
		final String[] optionValues = partyNames.toArray(new String[partyNames
		    .size()]);
		final Collection<FieldDictionaryItem> options = getValet().createOptions(
		    optionValues);
		partisanField.setGenericOptions(options);
		
		return id;
	}

	/**
	 * Finds the list of unique party names for the partisan contests.
	 * @param partisanContests
	 *          the partisan contests.
	 * @param noPartyName the string displayed for no party selection.
	 * @return the list of unique party names.
	 */
	private List<String> findPartisanPartyNames(final List<VipContest> partisanContests, final String noPartyName) {
		final List<String> partyNames = new LinkedList<String>();
		for (final VipContest partisanContest : partisanContests) {
			String partyName = partisanContest.getPartisanParty();
			if (partyName != null) {
				if (!partyNames.contains(partyName)) {
					partyNames.add(partyName);
				}
			} else if ("PRIMARY".equalsIgnoreCase(partisanContest.getType())) {
				for (final VipBallotCandidate candidate : partisanContest.getBallot().getCandidates()) {
					partyName = candidate.getCandidate().getParty();
					if (!PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY.equalsIgnoreCase(partyName) && !partyNames.contains(partyName)) {
						partyNames.add(partyName);
					}
				}
			}
		}
		partyNames.add(noPartyName);
		return partyNames;
	}

	/**
	 * Gets the election service.
	 * 
	 * @return the electionService.
	 */
	private ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @return the votingPrecinctService.
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}
}
