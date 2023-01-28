/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.vip.*;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {@link ContestPageAddOn} that provides a state-level
 * contests page.
 * 
 * @author IanBrown
 * 
 * @since Aug 6, 2012
 * @version Sep 30, 2013
 */
@Component
public class StateContestsPageAddOn extends AbstractAllowedForAddOn implements
        ContestPageAddOn {

	/**
	 * the formatter used to produce the election dates.
	 */
	private final static SimpleDateFormat ELECTION_DATE_FORMATTER = new SimpleDateFormat("MMMM dd, yyyy");
	
	/**
	 * the title for the field describing the contests.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Oct 23, 2012
	 */
	static final String CONTESTS_FIELD_TITLE = "";

	/**
	 * the name of the group containing the candidates.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	static final String CONTESTS_GROUP_NAME = "contests";

	/**
	 * the title of the group containing the candidates.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	static final String CONTESTS_GROUP_TITLE = "Note: you are not voting interactively online.";

	/**
	 * the default text for the contests.
	 * 
	 * @author IanBrown
	 * @since Oct 23, 2012
	 * @version Oct 23, 2012
	 */
	static final String[] CONTESTS_TEXT = {
	        "<p>You must print and send in your ballot at the end of this procedure.</p>",
	        "<p>The candidate and question choices you make will be deleted after you download your write-in ballot and close the window.</p>" };

	/**
	 * the name of the property to provide the text for the contests.
	 * 
	 * @author IanBrown
	 * @since Oct 23, 2012
	 * @version Oct 23, 2012
	 */
	static final String CONTESTS_TEXT_PROPERTY = "fwab.contests.text";

	/**
	 * the name of the property specifying the title for the contests.
	 * 
	 * @author IanBrown
	 * @since Oct 23, 2012
	 * @version Oct 23, 2012
	 */
	static final String CONTESTS_TITLE_PROPERTY = "fwab.contests.title";

	/**
	 * the title for the variant showing the contests.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Oct 23, 2012
	 */
	static final String CONTESTS_VARIANT_TITLE = "";

	/**
	 * the title for a custom ballot.
	 * 
	 * @author IanBrown
	 * @since Aug 9, 2012
	 * @version Aug 9, 2012
	 */
	static final String CUSTOM_TITLE = "Custom ballot";

	/**
	 * the title for the header field.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Oct 22, 2012
	 */
	static final String HEADER_FIELD_TITLE = "";

	/**
	 * the name of the group containing the headers.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	static final String HEADER_GROUP_NAME = "header";
	
	/** the name of the group containing the election information. */
	static final String ELECTION_GROUP_NAME = "election";

	/**
	 * the title of the group containing the header.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Oct 12, 2012
	 */
	static final String HEADER_GROUP_TITLE = "Choose your candidates and vote on ballot questions:";

	/**
	 * text for the header.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	static final String[] HEADER_TEXT = {
	        "<p>You may use this write-in ballot to vote in federal, state and local races by choosing a candidate and by voting yes or no on local ballot questions.<p/>",
	        "<p>To find links to additional information about the candidates and questions appearing on your ballot, go to <a href=\"http://myballotmn.sos.state.mn.us/Default.aspx\">http://myballotmn.sos.state.mn.us/Default.aspx</a>.</p>" };

	/**
	 * the header text property name.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	static final String HEADER_TEXT_PROPERTY = "fwab.header.text";

	/**
	 * the property for the header title.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	static final String HEADER_TITLE_PROPERTY = "fwab.header.title";

	/**
	 * the title for the variant containing the header.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	static final String HEADER_VARIANT_TITLE = "";

	/**
	 * the prefix for the title of the question field used when there is no
	 * contest for the office.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 9, 2012
	 */
	static final String NO_OFFICE_CONTEST_TITLE = "There is no contest for the office of ";

	/**
	 * the default string used to format the regional districts.
	 * 
	 * @author IanBrown
	 * @since Oct 23, 2012
	 * @version Oct 23, 2012
	 */
	static final String REGIONAL_DISTRICT_FORMAT = "DISTRICT: {0} {1}";

	/**
	 * the name of the property to supply the district format for a regional
	 * district.
	 * 
	 * @author IanBrown
	 * @since Oct 23, 2012
	 * @version Oct 23, 2012
	 */
	static final String REGIONAL_DISTRICT_PROPERTY = "fwab.regional.district";

	/**
	 * the standard group name for a custom ballot.
	 * 
	 * @author IanBrown
	 * @since Aug 9, 2012
	 * @version Aug 9, 2012
	 */
	static final String STANDARD_CUSTOM_GROUP_NAME = "fwab_custom_ballot";

	/**
	 * the standard name for the question group for an office contest.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 9, 2012
	 */
	static final String STANDARD_OFFICE_GROUP_NAME = "fwab_choose_candidate";

	/**
	 * the standard group name for a referendum.
	 * 
	 * @author IanBrown
	 * @since Aug 9, 2012
	 * @version Aug 9, 2012
	 */
	static final String STANDARD_REFERENDUM_GROUP_NAME = "fwab_referendum";

	/**
	 * the default format string for the statewide district.
	 * 
	 * @author IanBrown
	 * @since Oct 23, 2012
	 * @version Oct 23, 2012
	 */
	static final String STATEWIDE_DISTRICT_FORMAT = "";

	/**
	 * the name of the property to supply the district format for the statewide
	 * district.
	 * 
	 * @author IanBrown
	 * @since Oct 23, 2012
	 * @version Oct 23, 2012
	 */
	static final String STATEWIDE_DISTRICT_PROPERTY = "fwab.statewide.district";

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private final static Logger LOGGER = LoggerFactory
	        .getLogger(StateContestsPageAddOn.class);

	/**
	 * should the district be shown?
	 * 
	 * @author IanBrown
	 * @since Oct 29, 2012
	 * @version Oct 29, 2012
	 */
	static final boolean SHOW_DISTRICT = false;

	/** the title for the election variant. */
	static final String ELECTION_VARIANT_TITLE = "";

	/**
	 * Builds the title for an office contest.
	 * 
	 * @author IanBrown
	 * @param office
	 *            the office.
	 * @param special
	 *            the special election flag.
	 * @param type
	 *            the type of election.
	 * @param district
	 *            the district (may be null or blank).
	 * @param elect
	 *            the number to be elected (may be null or blank).
	 * @return the title.
	 * @since Sep 13, 2012
	 * @version May 7, 2013
	 */
	static String buildOfficeTitle(final String office, final boolean special,
	        final String type, final String district, final String elect) {
		final StringBuilder sb = new StringBuilder();
		boolean addElection = false;
		String prefix = " ";
		if (special) {
			sb.append("SPECIAL");
			addElection = true;
			prefix = " ";
		}

		if (!"GENERAL".equalsIgnoreCase(type)) {
			sb.append(prefix).append(type.toUpperCase());
			addElection = true;
		}

		if (addElection) {
			sb.append(" ELECTION<br>");
		}
		sb.append(office.toUpperCase());
		if (district != null && !district.isEmpty()) {
			sb.append("<br>").append(district.toUpperCase());
		}
		if (elect != null && !elect.isEmpty()) {
			sb.append("<br>").append(elect);
		}

		return sb.toString();
	}

	/**
	 * the candidate page add on.
	 * 
	 * @author IanBrown
	 * @since Aug 27, 2012
	 * @version Aug 27, 2012
	 */
	@Autowired
	private CandidatePageAddon candidatePageAddon;

	/**
	 * the election service.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	@Autowired
	private ElectionService electionService;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Constructs a state contests page add on.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	public StateContestsPageAddOn() {
		setValet(PageAddOnValetImpl.getInstance());
	}

	/**
	 * Gets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @return the candidate page add on.
	 * @since Aug 27, 2012
	 * @version Aug 27, 2012
	 */
	public CandidatePageAddon getCandidatePageAddon() {
		return candidatePageAddon;
	}

	/**
	 * Gets the election service.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	public ElectionService getElectionService() {
		return electionService;
	}

	/** {@inheritDoc} */
	@Override
	public Long getFirstFieldId(final QuestionnairePage currentPage) {
		return firstFieldId + currentPage.getStepNumber() * 1000;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	public VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/** {@inheritDoc} */
	@Override
	public void prepareAddOnPage(final WizardContext form,
	        final QuestionnairePage currentPage) {

		// fill page if this is FWAB flow. Leave it intact otherwise, it will be
		// skipped
		if (form.getFlowType() == FlowType.FWAB) {
			final WizardResults wizardResults = form.getWizardResults();

			final String stateIdentification = wizardResults.getVotingRegionState();
			final String votingRegionName = wizardResults.getVotingRegionName();
			if (getVotingPrecinctService().isReady(stateIdentification,
			        votingRegionName)
			        && getElectionService().isReady(stateIdentification,
			                votingRegionName)) {
				final WizardResultAddress votingAddress = wizardResults
				        .getVotingAddress();
				final ValidAddress validAddress = getVotingPrecinctService()
				        .validateAddress(votingAddress,  stateIdentification  );
				if (validAddress != null) {
					try {
						final List<String> contestOrder = getElectionService()
						        .contestOrder(stateIdentification,
										votingRegionName);
						buildPageForAddress(form, currentPage, validAddress,
						        contestOrder);
						return;
					} catch (final RuntimeException e) {
						// Propagate run-time exceptions normally.
						throw e;
					} catch (final Exception e) {
						// Log and ignore other exceptions - the page will not
						// be displayed.
						LOGGER.warn("Unable to build state contests page for "
						        + validAddress.getValidatedAddress()
						                .getSingleLineAddress());
					}
				}
			}

			// For addresses that are not considered "valid", we'll try to use
			// the standard candidate page.
			getCandidatePageAddon().prepareAddOnPage(form, currentPage);
		}
	}

	/**
	 * Sets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @param candidatePageAddon
	 *            the candidate page add on to set.
	 * @since Aug 27, 2012
	 * @version Aug 27, 2012
	 */
	public void setCandidatePageAddon(
	        final CandidatePageAddon candidatePageAddon) {
		this.candidatePageAddon = candidatePageAddon;
	}

	/**
	 * Sets the election service.
	 * 
	 * @author IanBrown
	 * @param electionService
	 *            the election service to set.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	public void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	public void setVotingPrecinctService(
	        final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Builds a string for the district based on the format desired by the
	 * state.
	 * <p>
	 * TODO this code needs to handle the district number. In addition, it needs
	 * to provide for cases where there is just a district name and cases where
	 * there is just a district number.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param districtType
	 *            the type of district.
	 * @param districtName
	 *            the name of the district.
	 * @param districtNumber
	 *            the number of the district.
	 * @return the string for the district.
	 * @since Oct 23, 2012
	 * @version Oct 29, 2012
	 */
	final String buildDistrictString(final String stateAbbreviation,
	        final String votingRegionName, final String districtType,
	        final String districtName, final Integer districtNumber) {
		if (!SHOW_DISTRICT) {
			return null;
		}

		final String districtFormat;
		if (districtType.equalsIgnoreCase("STATEWIDE")) {
			districtFormat = singlelineProperty(stateAbbreviation,
			        votingRegionName, STATEWIDE_DISTRICT_PROPERTY,
			        STATEWIDE_DISTRICT_FORMAT);
		} else {
			districtFormat = singlelineProperty(stateAbbreviation,
			        votingRegionName, REGIONAL_DISTRICT_PROPERTY,
			        REGIONAL_DISTRICT_FORMAT);
		}

		final String districtTypeString = MessageFormat.format(districtFormat,
		        districtType, districtName);
		return districtTypeString;
	}

	/**
	 * Adds an additional group for the input contest.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard contest.
	 * @param currentPage
	 *            the current page.
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param softId
	 *            the soft identifier.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio button field type.
	 * @param multiChoiceFieldType
	 *            the multiple selection field type.
	 * @param contest
	 *            the contest.
	 * @param partisanParty the partisan party.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param additionalContestIdx
	 *            the additional contest index.
	 * @return the new soft identifier.
	 * @since Oct 4, 2012
	 * @version Oct 1, 2013
	 */
	private long addAdditionalGroupForContest(
	        final WizardContext wizardContext,
	        final QuestionnairePage currentPage,
	        final String stateAbbreviation, final String votingRegionName,
	        final long softId, final FieldType noInputFieldType,
	        final FieldType radioFieldType,
	        final FieldType multiChoiceFieldType, final VipContest contest,
	        String partisanParty, String noPartyName, final int additionalContestIdx) {
		long id = softId;
		final VipBallot ballot = contest.getBallot();
		if (ballot.getReferendum() != null) {
			id = addGroupForReferendum(wizardContext, currentPage,
			        stateAbbreviation, votingRegionName, noInputFieldType,
			        radioFieldType, multiChoiceFieldType, id,
			        additionalContestIdx, contest);
		} else if (ballot.getCustomBallot() != null) {
			id = addContestForCustomBallot(wizardContext, currentPage,
			        noInputFieldType, radioFieldType, multiChoiceFieldType, id,
			        additionalContestIdx, contest);
		} else if (ballot.getCandidates() != null
		        && !ballot.getCandidates().isEmpty()) {
			id = addGroupForOffice(wizardContext, currentPage,
			        stateAbbreviation, votingRegionName, noInputFieldType,
			        radioFieldType, multiChoiceFieldType, id,
			        contest, partisanParty, noPartyName, additionalContestIdx);
		}

		return id;
	}

	/**
	 * Adds a contest for a custom ballot.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param currentPage
	 *            the current page.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple selection field type.
	 * @param softId
	 *            the soft identifier.
	 * @param additionalContestIdx
	 *            the additional contest index.
	 * @param contest
	 *            the custom ballot contest.
	 * @return the new soft identifier.
	 * @since Oct 5, 2012
	 * @version Oct 1, 2013
	 */
	private long addContestForCustomBallot(final WizardContext wizardContext,
	        final QuestionnairePage currentPage,
	        final FieldType noInputFieldType, final FieldType radioFieldType,
	        final FieldType multiChoiceFieldType, final long softId,
	        final int additionalContestIdx, final VipContest contest) {
		final String contestFor = createTitleForCustomBallot(contest);
		final String name = STANDARD_CUSTOM_GROUP_NAME;
		final String title = CUSTOM_TITLE;
		final String noContestText = "";
		final String contestText = "";
		final String inPdfName = ADDITIONAL_CANDIDATE_PREFIX
		        + additionalContestIdx;
		return createGroupForContest(wizardContext, currentPage,
		        noInputFieldType, radioFieldType, multiChoiceFieldType, softId,
		        contestFor, false, contest, name, title, noContestText,
		        contestText, inPdfName, null, null, null);
	}

	/**
	 * Adds a group for a contest for an office.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param currentPage
	 *            the current page.
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple selection field type.
	 * @param softId
	 *            the soft identifier.
	 * @param officeContest
	 *            the office contest.
	 * @param partisanParty the name of the partisan party.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param additionalContestIdx
	 *            the index for the additional contest identifiers.
	 * @return the new soft identifier.
	 * @since Oct 4, 2012
	 * @version Oct 1, 2013
	 */
	private long addGroupForOffice(final WizardContext wizardContext,
	        final QuestionnairePage currentPage,
	        final String stateAbbreviation, final String votingRegionName,
	        final FieldType noInputFieldType, final FieldType radioFieldType,
	        final FieldType multiChoiceFieldType, final long softId,
	        final VipContest officeContest, String partisanParty, String noPartyName, final Integer additionalContestIdx) {
		final String office = officeContest.getOffice();
		final boolean special = officeContest.isSpecial();
		final String type = officeContest.getType();
		final VipElectoralDistrict electoralDistrict = officeContest
		        .getElectoralDistrict();
		final String district = buildDistrictString(stateAbbreviation,
		        votingRegionName, electoralDistrict.getType(),
		        electoralDistrict.getName(), electoralDistrict.getNumber());
		final Integer numberVotingFor = officeContest.getNumberVotingFor();
		final String elect = numberVotingFor == null || numberVotingFor == 1 ? "VOTE FOR ONE"
		        : "VOTE FOR UP TO " + numberVotingFor;
		final String name = STANDARD_OFFICE_GROUP_NAME;
		final String title = buildOfficeTitle(office, special, type, district,
		        elect);
		return createGroupForOffice(wizardContext, currentPage,
		        noInputFieldType, radioFieldType, multiChoiceFieldType, softId,
		        additionalContestIdx, office, false, officeContest, name,
		        title, null, partisanParty, noPartyName);
	}

	/**
	 * Adds a contest for a referendum.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param currentPage
	 *            the current page.
	 * @param state
	 *            the abbreviation of the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple selection field type.
	 * @param softId
	 *            the soft identifier.
	 * @param additionalContestIdx
	 *            the additional contest index.
	 * @param contest
	 *            the contest.
	 * @return the new soft identifier.
	 * @since Oct 5, 2012
	 * @version Oct 1, 2013
	 */
	private long addGroupForReferendum(final WizardContext wizardContext,
	        final QuestionnairePage currentPage, final String state,
	        final String votingRegionName, final FieldType noInputFieldType,
	        final FieldType radioFieldType,
	        final FieldType multiChoiceFieldType, final long softId,
	        final int additionalContestIdx, final VipContest contest) {
		final String name = STANDARD_REFERENDUM_GROUP_NAME;
		final VipReferendum referendum = contest.getBallot().getReferendum();
		final String brief = referendum.getBrief();
		final String text = referendum.getText();
		final String contestFor = brief == null ? text : brief;
		final String title = buildTitleForReferendum(referendum);
		final String noContestText = "";
		final String contestText = brief == null ? null : text;
		final String inPdfName = ADDITIONAL_CANDIDATE_PREFIX
		        + additionalContestIdx;
		// final VipReferendumDetail referendumDetail =
		// getElectionService().findReferendumDetail(state, votingRegionName,
		// referendum.getVipId());
		final String additionalInfo = null; // referendumDetail.getEffectOfAbstain();
		return createGroupForContest(wizardContext, currentPage,
		        noInputFieldType, radioFieldType, multiChoiceFieldType, softId,
		        contestFor, false, contest, name, title, noContestText,
		        contestText, inPdfName, null, null, additionalInfo);
	}

	/**
	 * Adds a standard group for the specified type of contest.
	 * 
	 * @author IanBrown
	 * @param standardContest
	 *            the type of contest.
	 * @param wizardContext
	 *            the wizard context.
	 * @param currentPage
	 *            the current page.
	 * @param softId
	 *            the soft identifier.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio button field type.
	 * @param multiChoiceFieldType
	 *            the multiple selection field type.
	 * @param contests
	 *            the contests.
	 * @param partisanParty
	 *            the name of the party in partisan elections.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @return the new soft identifier.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2013
	 */
	private long addGroupForStandardOffice(
	        final StandardContest standardContest,
	        final WizardContext wizardContext,
	        final QuestionnairePage currentPage, final long softId,
	        final FieldType noInputFieldType, final FieldType radioFieldType,
	        final FieldType multiChoiceFieldType,
	        final List<VipContest> contests, final String partisanParty, final String noPartyName) {
		long id = softId;
		final String standardTitle = standardContest.getOffice()[0]
		        .toUpperCase();
		final List<VipContest> matchingContests = findContests(contests,
		        partisanParty, noPartyName, standardContest);
		if (matchingContests == null) {
			final String name = standardContest.getAlternativeGroupName();
			final String title = standardTitle;
			id = createGroupForOffice(wizardContext, currentPage,
			        noInputFieldType, radioFieldType, multiChoiceFieldType, id,
			        null, title, true, null, name, title,
			        standardContest.getInPdfName(), partisanParty, noPartyName);
		} else {
			final String name = STANDARD_OFFICE_GROUP_NAME;
			for (final VipContest contest : matchingContests) {
				final String contestOffice = contest.getOffice().toUpperCase();
				final String title = buildOfficeTitle(
				        contestOffice,
				        contest.isSpecial(),
				        contest.getType(),
				        null,
				        "VOTE FOR ONE"
				                + (standardContest == StandardContest.PRESIDENT ? " TEAM"
				                        : ""));
				id = createGroupForOffice(wizardContext, currentPage,
				        noInputFieldType, radioFieldType, multiChoiceFieldType,
				        id, null, contestOffice, true, contest, name, title,
				        standardContest.getInPdfName(), partisanParty, noPartyName);
			}
			contests.removeAll(matchingContests);
		}
		return id;
	}

	/**
	 * Builds the contests question group.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param softId
	 *            the soft identifier.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @return the new soft identifier.
	 * @since Aug 7, 2012
	 * @version Oct 22, 2012
	 */
	private long buildContestsGroup(final QuestionnairePage currentPage,
	        final String stateAbbreviation, final String votingRegionName,
	        final long softId, final FieldType noInputFieldType) {
		long id = softId;
		final String contestsHeaderTitle = singlelineProperty(
		        stateAbbreviation, votingRegionName, CONTESTS_TITLE_PROPERTY,
		        CONTESTS_GROUP_TITLE);
		final Question contestsGroup = getValet().createGroup(currentPage, id,
		        CONTESTS_GROUP_NAME, contestsHeaderTitle);
		++id;
		final QuestionVariant contestsVariant = getValet().createVariant(
		        contestsGroup, id, CONTESTS_VARIANT_TITLE);
		++id;
		final String contestsText = multilineProperty(stateAbbreviation,
		        votingRegionName, CONTESTS_TEXT_PROPERTY, CONTESTS_TEXT);
		getValet().createField(contestsVariant, id, noInputFieldType,
		        CONTESTS_FIELD_TITLE, contestsText);
		++id;
		return id;
	}

	/**
	 * Builds the header question group.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *            the current page.
	 * @param softId
	 *            the soft identifier.
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @return the new soft identifier.
	 * @since Aug 7, 2012
	 * @version Jul 30, 2013
	 */
	private long buildHeaderGroup(final QuestionnairePage currentPage,
	        final long softId, final String stateAbbreviation,
	        final String votingRegionName, final FieldType noInputFieldType) {
		long id = softId;
		final String headerTitle = singlelineProperty(stateAbbreviation,
		 votingRegionName, HEADER_TITLE_PROPERTY,
		 HEADER_GROUP_TITLE);
		final Question headerGroup = getValet().createGroup(currentPage, id,
		        HEADER_GROUP_NAME, headerTitle);
		++id;
		final QuestionVariant headerVariant = getValet().createVariant(
		        headerGroup, id, HEADER_VARIANT_TITLE);
		++id;
		final String headerText = multilineProperty(stateAbbreviation,
		        votingRegionName, HEADER_TEXT_PROPERTY, HEADER_TEXT);
		getValet().createField(headerVariant, id, noInputFieldType,
		        HEADER_FIELD_TITLE, headerText);
		++id;
		return id;
	}

	/**
	 * Creates the header title from the input election.
	 * @param stateAbbreviation the abbreviation for the state.
	 * @param votingRegionName the name of the voting region.
	 * @param election
	 *            the election.
	 * @return the header title.
	 */
	private String createHeaderTitleFromElection(String stateAbbreviation, String votingRegionName, final VipElection election) {
		final String electionType;
		if ("STG".equals(election.getType())) {
			electionType = "STATE GENERAL ELECTION BALLOT";
		} else {
			electionType = "STATE PARTISAN PRIMARY BALLOT";
		}
		final String location = votingRegionName + ", " + election.getState().getName();
		final String electionDate = ELECTION_DATE_FORMATTER.format(election.getDate());
		return electionType + "<br/>" + location.toUpperCase() + "<br/>" + electionDate.toUpperCase();
	}

	/**
	 * Builds the page of contests for the input valid address.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param currentPage
	 *            the current page.
	 * @param validAddress
	 *            the valid address.
	 * @param contestOrder
	 *            the order in which the contests (state and local) should
	 *            appear.
	 * @throws Exception
	 *             if there is a problem finding the contests to work.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2013
	 */
	private void buildPageForAddress(final WizardContext wizardContext,
	        final QuestionnairePage currentPage,
	        final ValidAddress validAddress, final List<String> contestOrder)
	        throws Exception {
		final FieldType noInputFieldType = getQuestionFieldService()
		        .findFieldTypeByTemplate( FieldType.TEMPLATE_NOT_INPUT );
		final FieldType radioFieldType = getQuestionFieldService()
		        .findFieldTypeByTemplate( FieldType.TEMPLATE_RADIO );
		final FieldType multiChoiceFieldType = getValet()
		        .acquireMultipleCheckboxesFieldType();
		long softId = getFirstFieldId( currentPage );
		final WizardResults wizardResults = wizardContext.getWizardResults();
		final String partisanParty = findPartisanParty( wizardResults
				.getAnswers() );
		final String stateAbbreviation = wizardResults.getVotingRegionState();
		final String votingRegionName = wizardResults.getVotingRegionName();
		final String noPartyName = PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY;
		softId = buildElectionGroup(currentPage, stateAbbreviation, votingRegionName, softId, noInputFieldType);
		softId = buildHeaderGroup(currentPage, softId, stateAbbreviation,
		        votingRegionName, noInputFieldType);
		softId = buildContestsGroup(currentPage, stateAbbreviation,
		        votingRegionName, softId, noInputFieldType);
		final List<VipContest> contests = getElectionService().findContests(
		        validAddress);
		final StandardContest[] standardContests = StandardContest
		        .orderedValues();
		for (final StandardContest standardContest : standardContests) {
			softId = addGroupForStandardOffice(standardContest, wizardContext,
			        currentPage, softId, noInputFieldType, radioFieldType,
			        multiChoiceFieldType, contests, partisanParty, noPartyName);
		}

		if (!contests.isEmpty()) {
            final VoterType voterType;
            try {
                voterType = VoterType.valueOf(wizardResults.getVoterType());
            } catch (IllegalArgumentException e) {
                return;  // voter type undefined
            }

            if (voterType != VoterType.OVERSEAS_VOTER) {
				final List<VipContest> orderedContests = ElectionService
				        .orderContests(contests, contestOrder);
				int additionalContestIdx = 1;
				for (final VipContest contest : orderedContests) {
					String contestPartisanParty = contest.isPartisan() ? contest.getPartisanParty() : null;
					if ((contestPartisanParty != null)
					        && !contestPartisanParty.isEmpty()
					        && !contestPartisanParty.equals(partisanParty)) {
						// Skip partisan contests for a different party.
						contests.remove(partisanParty);
						continue;
					}

					final long startingId = softId;
					softId = addAdditionalGroupForContest(wizardContext,
					        currentPage, stateAbbreviation, votingRegionName,
					        softId, noInputFieldType, radioFieldType,
					        multiChoiceFieldType, contest, partisanParty, noPartyName, additionalContestIdx);
					additionalContestIdx += Math.max(softId - 2l, startingId)
					        - startingId;
				}
				contests.removeAll(orderedContests);
			}
		}
	}

	/**
	 * Builds the election group.
	 * @param currentPage the current page.
	 * @param stateAbbreviation the state abbreviation.
	 * @param votingRegionName the name of the voting region.
	 * @param softId the soft identifier.
	 * @param noInputFieldType the no-input field type.
	 * @return the new soft identifier.
	 */
	private long buildElectionGroup(QuestionnairePage currentPage,
            String stateAbbreviation, String votingRegionName, long softId,
            FieldType noInputFieldType) {
		final VipElection election = getElectionService().findElection(
		        stateAbbreviation, votingRegionName);
		final String electionTitle = createHeaderTitleFromElection(stateAbbreviation, votingRegionName, election);
		long id = softId;
		final Question electionGroup = getValet().createGroup(currentPage, id,
		        ELECTION_GROUP_NAME, electionTitle);
		++id;
		@SuppressWarnings("unused")
        final QuestionVariant electionVariant = getValet().createVariant(
		        electionGroup, id, ELECTION_VARIANT_TITLE);
		++id;
		return id;
    }

	/**
	 * Finds the name of the party for whom the voter wishes to vote in partisan
	 * elections.
	 * 
	 * @param answers
	 *            the answers provided by the user.
	 * @return the name of the party or <code>null</code> if none is found.
	 */
	private String findPartisanParty(Collection<Answer> answers) {
		for (final Answer answer : answers) {
			if (PartisanPartyAddOn.PARTISAN_FIELD_TITLE.equals(answer
			        .getField().getTitle())) {
				return answer.getValue();
			}
		}

		return null;
	}

	/**
	 * Builds a title for the input referendum contest.
	 * 
	 * @author IanBrown
	 * @param referendum
	 *            the referendum.
	 * @return the title.
	 * @since Aug 9, 2012
	 * @version Oct 22, 2012
	 */
	private String buildTitleForReferendum(final VipReferendum referendum) {
		final String[] parts = referendum.getTitle().split("\\n");
		final StringBuilder titleBuilder = new StringBuilder();
		String prefix = "";
		for (final String part : parts) {
			titleBuilder.append(prefix).append(part);
			prefix = "<br/>";
		}
		final String subTitle = referendum.getSubTitle();
		if (subTitle != null) {
			titleBuilder.append("<br/>").append(subTitle);
		}
		return titleBuilder.toString().toUpperCase();
	}

	/**
	 * Creates a group for the input contest.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param currentPage
	 *            thec current page.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple selection field type.
	 * @param softId
	 *            the soft identifier.
	 * @param contestFor
	 *            the name of the reason for the contest (such as the name of
	 *            the office).
	 * @param standard
	 *            is this a standard contest?
	 * @param contest
	 *            the contest.
	 * @param name
	 *            the name of the group.
	 * @param title
	 *            the title of the group.
	 * @param noContestText
	 *            the text for the no contest case.
	 * @param contestText
	 *            the text for the contest case.
	 * @param inPdfName
	 *            the name for the PDF variable.
	 * @param partisanParty the name of the partisan party - used by Minnesota to select candidates in primaries.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param additionalInfo
	 *            additional information to be displayed as a no-input field.
	 * @return the new soft identifier.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	private long createGroupForContest(final WizardContext wizardContext,
	        final QuestionnairePage currentPage,
	        final FieldType noInputFieldType, final FieldType radioFieldType,
	        final FieldType multiChoiceFieldType, final long softId,
	        final String contestFor, final boolean standard,
	        final VipContest contest, final String name, final String title,
	        final String noContestText, final String contestText,
	        final String inPdfName, String partisanParty, String noPartyName, final String additionalInfo) {
		long id = softId;
		final Question group = getValet().createGroup(currentPage, id, name,
		        title);
		++id;
		final QuestionVariant variant = getValet().createVariant(group, id,
		        name);
		++id;
		if (contest == null) {
			getValet().createField(variant, id, noInputFieldType, contestFor,
			        noContestText);
			++id;
		} else {
			final Integer numberVotingFor = contest.getNumberVotingFor();
			Answer answer = wizardContext.getAnswerByFieldId(id);
			final QuestionField field;
			if (answer == null) {
				final FieldType fieldType = numberVotingFor == null
				        || numberVotingFor == 1 ? radioFieldType
				        : multiChoiceFieldType;
				field = getValet().createField(variant, id, fieldType,
				        contestFor, contestText);
				answer = field.createAnswer();
				wizardContext.putAnswer(answer);
			} else {
				field = answer.getField();
				field.setQuestion(variant);
				variant.getFields().add(field);
			}
			if (numberVotingFor == null || numberVotingFor == 1) {
				++id;
			} else {
				field.setVerificationPattern(numberVotingFor.toString());
				id += numberVotingFor;
			}
			field.setRequired(false);
			field.setSecurity(true);
			field.setInPdfName(inPdfName);
			final Collection<FieldDictionaryItem> options = getValet()
			        .createOptions(standard, contest, partisanParty, noPartyName);
			field.setGenericOptions(options);
			if (additionalInfo != null) {
				getValet().createField(variant, id, noInputFieldType,
				        additionalInfo, null);
				++id;
			}
		}
		return id;
	}

	/**
	 * Creates a group for a contest for an office.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param currentPage
	 *            the current page.
	 * @param noInputFieldType
	 *            the no input field type.
	 * @param radioFieldType
	 *            the radio field type.
	 * @param multiChoiceFieldType
	 *            the multiple choice field type.
	 * @param softId
	 *            the soft identifier.
	 * @param additionalContestIdx
	 *            the index for an additional contest.
	 * @param office
	 *            the name of the office.
	 * @param standard
	 *            is this a standard contest?
	 * @param contest
	 *            the contest.
	 * @param name
	 *            the name of the question.
	 * @param title
	 *            the title for the question.
	 * @param standardInPdfName
	 *            the standard PDF name for the office.
	 * @param partisanParty the name of the party for partisan contests - also used to choose candidates from primary contests for Minnesota.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @return the new soft identifier.
	 * @since Aug 9, 2012
	 * @version Oct 1, 2013
	 */
	private long createGroupForOffice(final WizardContext wizardContext,
	        final QuestionnairePage currentPage,
	        final FieldType noInputFieldType, final FieldType radioFieldType,
	        final FieldType multiChoiceFieldType, final long softId,
	        final Integer additionalContestIdx, final String office,
	        final boolean standard, final VipContest contest,
	        final String name, final String title, final String standardInPdfName, 
	        final String partisanParty, final String noPartyName) {
		final String noContestText;
		final String contestText;
		final String inPdfName;
		if (contest == null) {
			noContestText = NO_OFFICE_CONTEST_TITLE + office;
			contestText = null;
			inPdfName = standardInPdfName;
		} else {
			noContestText = null;
			final String contestType = contest.getType();
			final String textType;
			final String pdfType = "";  // if standard - do not add "_primary" or "_sprcial" to pdf names
                                        // if non standard - use 'ufAdditionalCandidateNN' for names
			if ("GENERAL".equalsIgnoreCase(contestType) ) {
				textType = "";
			} else {
				textType = contestType + " ";
			}
            inPdfName = additionalContestIdx == null ? standardInPdfName
                    + pdfType : ADDITIONAL_CANDIDATE_PREFIX
                    + additionalContestIdx;
			if (contest.isSpecial() ) {
				contestText = office + " (special " + textType + "election)";
			} else {
				contestText = office
				        + (textType.isEmpty() ? "" : " (" + textType
				                + "election)");
			}
        }
		return createGroupForContest(wizardContext, currentPage,
		        noInputFieldType, radioFieldType, multiChoiceFieldType, softId,
		        "Candidate", standard, contest, name, title, noContestText,
		        contestText, inPdfName, partisanParty, noPartyName, null);
	}

	/**
	 * Creates a title for the custom ballot contest.
	 * 
	 * @author IanBrown
	 * @param customBallotContest
	 *            the custom ballot contest.
	 * @return the title.
	 * @since Aug 9, 2012
	 * @version Aug 9, 2012
	 */
	private String createTitleForCustomBallot(
	        final VipContest customBallotContest) {
		final VipBallot ballot = customBallotContest.getBallot();
		final VipCustomBallot customBallot = ballot.getCustomBallot();
		return customBallot.getHeading();
	}

	/**
	 * Finds the contest(s) for the specified standard contest.
	 * 
	 * @author IanBrown
	 * @param contests
	 *            the contests.
	 * @param partisanParty
	 *            the name of the party for partisan elections.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @param standardContest
	 *            the standard contest.
	 * @return the contest(s).
	 * @since Aug 7, 2012
	 * @version Oct 1, 2013
	 */
	private List<VipContest> findContests(final List<VipContest> contests,
	        String partisanParty, String noPartyName, final StandardContest standardContest) {
		final List<VipContest> matchingContests = new LinkedList<VipContest>();
		final List<VipContest> inputContests = new ArrayList<VipContest>(
		        contests);
		for (final VipContest contest : inputContests) {
			String contestPartisanParty = contest.isPartisan() ? contest.getPartisanParty() : null;
			if ((contestPartisanParty != null)
			        && !contestPartisanParty.isEmpty()
			        && !contestPartisanParty.equals(partisanParty)) {
				// Skip partisan contests for other parties.
				contests.remove(contest);
				continue;
			}

			final VipBallot ballot = contest.getBallot();
			final List<VipBallotCandidate> candidates = ballot.getCandidates();
			if (candidates == null || candidates.isEmpty()) {
				continue;
			}
			if ("PRIMARY".equalsIgnoreCase(contest.getType()) && (partisanParty != null)) {
				// Special case: for primary elections in Minnesota, we only want to provide the candidates of the partisan party.
				int primaryCandidates = 0;
				for (final VipBallotCandidate candidate : candidates) {
					String partyName = candidate.getCandidate().getParty();
					if (partisanParty.equalsIgnoreCase(partyName) || ((noPartyName != null) && noPartyName.equalsIgnoreCase(partyName))) {
						++primaryCandidates;
					}
				}
				if (primaryCandidates == 0) {
					// Skip primary contests with no candidates in the selected party.
					contests.remove(contest);
					continue;
				}
			}
			if (standardContest.matches(contest.getOffice())) {
				contests.remove(contest);
				matchingContests.add(contest);
			}
		}

		return matchingContests.isEmpty() ? null : matchingContests;
	}
}
