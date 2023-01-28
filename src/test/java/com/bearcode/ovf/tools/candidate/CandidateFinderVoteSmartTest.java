/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.axis.utils.XMLUtils;
import org.easymock.EasyMock;
import org.junit.Ignore;

import com.bearcode.ovf.actions.questionnaire.StandardContest;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import com.itextpdf.text.xml.XMLUtil;

/**
 * Extended {@link CandidateFinderCheck} test for {@link CandidateFinderVoteSmart}.
 * 
 * @author IanBrown
 * 
 * @since Sep 11, 2012
 * @version Nov 1, 2012
 */
public final class CandidateFinderVoteSmartTest extends CandidateFinderCheck<CandidateFinderVoteSmart> {

	/**
	 * the XML for the end of the candidate additional bio.
	 * 
	 * @author IanBrown
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	private static final Object CANDIDATE_ADDITIONAL_BIO_END = "</addlBio>";

	/**
	 * the XML for the start of candidate additional bio.
	 * 
	 * @author IanBrown
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	private static final Object CANDIDATE_ADDTIONAL_BIO_START = "<addlBio>";

	/**
	 * the XML for the end of the candidate bio.
	 * 
	 * @author IanBrown
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	private static final Object CANDIDATE_BIO_END = "</bio>";

	/**
	 * the XML for the start of the candidate bio.
	 * 
	 * @author IanBrown
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	private static final Object CANDIDATE_BIO_START = "<bio>";

	/**
	 * the XML for the candidate end string.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String CANDIDATE_END = "</candidate>";

	/**
	 * the XML for the candidate identifier value.
	 * 
	 * @author IanBrown
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	private final static String CANDIDATE_ID = "candidateId";

	/**
	 * the candidate name property.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private final static String CANDIDATE_NAME = "ballotName";

	/**
	 * the candidate party property.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private final static String CANDIDATE_PARTY = "electionParties";

	/**
	 * the XML candidate start string.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String CANDIDATE_START = "<candidate>";

	/**
	 * the XML candidates list end string.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String CANDIDATES_LIST_END = "</candidatesList>";

	/**
	 * the XML candidates list start string.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String CANDIDATES_LIST_START = "<candidatesList>";

	/**
	 * the XML for the electoral district name property.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private final static String ELECTION_DISTRICT_NAME = "electionDistrictName";

	/**
	 * the election office.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private final static String ELECTION_OFFICE = "electionOffice";

	/**
	 * the XML for the election special property.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private final static String ELECTION_SPECIAL = "electionSpecial";

	/**
	 * the XML for the election stage property.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private final static String ELECTION_STAGE = "electionStage";

	/**
	 * the election status property.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private final static String ELECTION_STATUS = "electionStatus";

	/**
	 * the XML for the candidate photo.
	 * 
	 * @author IanBrown
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	private static final String PHOTO = "photo";

	/**
	 * a valid ZIP code.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String VALID_ZIP = "55033";

	/**
	 * a valid ZIP+4;
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String VALID_ZIP_4 = "9821";

	/**
	 * the XML header string.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * the XML strings for the candidates.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private List<String> candidates;

	/**
	 * the vote smart service to use.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private VoteSmartService voteSmartService;

	/**
	 * a valid state.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private static final String VALID_STATE = "NH";

	/**
	 * a valid voting region.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private static final String VALID_VOTING_REGION = "Grafton County";

	/**
	 * Creates a contest.
	 * 
	 * @author IanBrown
	 * @param electionStage
	 *            the stage of the election.
	 * @param electionStatus
	 *            the status of the election.
	 * @param electionOffice
	 *            the office for the contest.
	 * @param electionDistrictName
	 *            the name of the election district.
	 * @param candidateName
	 *            the name of the candidate.
	 * @param candidateParty
	 *            the party of the candidate.
	 * @param special
	 *            is this a special election?
	 * @return the contest.
	 * @since Sep 11, 2012
	 * @version Sep 13, 2012
	 */
	public VipContest createContest(final String electionStage, final String electionStatus, final String electionOffice,
			final String electionDistrictName, final String candidateName, final String candidateParty, final boolean special) {
		final VipContest contest = createMock("FederalContest", VipContest.class);
		EasyMock.expect(getValet().acquireContest()).andReturn(contest);
		contest.setOffice(electionOffice);
		contest.setType(electionStage);
		if ("PRIMARY".equalsIgnoreCase(electionStage)) {
			contest.setPrimaryParty(candidateParty);
		}
		contest.setSpecial(special);
		final VipBallot ballot = createMock("FederalBallot", VipBallot.class);
		EasyMock.expect(getValet().acquireBallot()).andReturn(ballot);
		contest.setBallot(ballot);
		EasyMock.expect(contest.getBallot()).andReturn(ballot).atLeastOnce();
		final VipElectoralDistrict electoralDistrict = createElectoralDistrict(electionOffice, electionDistrictName);
		contest.setElectoralDistrict(electoralDistrict);
		addCandidate(electionOffice, electionDistrictName, electionStage, special, electionStatus, candidateName, candidateParty,
				ballot);
		return contest;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean areReferendumsSupported() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected final void completeSetUp(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws IOException {
		final String candidatesString = buildCandidatesXML(zip, zip4, getCandidates());
		EasyMock.expect(getVoteSmartService().retrieveCandidatesByZip(zip, zip4)).andReturn(candidatesString);
	}

	/** {@inheritDoc} */
	@Override
	protected final CandidateFinderVoteSmart createCandidateFinder() {
		final CandidateFinderVoteSmart candidateFinderVoteSmart = new CandidateFinderVoteSmart();
		candidateFinderVoteSmart.setVoteSmartService(getVoteSmartService());
		return candidateFinderVoteSmart;
	}

	/** {@inheritDoc} */
	@Override
	protected final ValidAddress createValidAddress(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(streetSegment).anyTimes();
		final UserAddress validatedAddress = createMock("ValidatedAddress", UserAddress.class);
		EasyMock.expect(validAddress.getValidatedAddress()).andReturn(validatedAddress).anyTimes();
		EasyMock.expect(validatedAddress.getZip()).andReturn(zip).anyTimes();
		EasyMock.expect(validatedAddress.getZip4()).andReturn(zip4).anyTimes();
		return validAddress;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidState() {
		return "IS";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidVotingRegion() {
		return "Invalid voting region";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidState() {
		return VALID_STATE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidVotingRegion() {
		return VALID_VOTING_REGION;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidZip() {
		return VALID_ZIP;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidZip4() {
		return VALID_ZIP_4;
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForCountyContest(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception {
		final String electionStage = "Primary";
		final String electionStatus = "Potential";
		final String electionOffice = "County Commissioner";
		final String electionDistrictName = "1";
		final String candidateName = "County Commissioner Candidate";
		final String candidateParty = "County Party";
		final boolean special = false;
		final VipContest contest = createContest(electionStage, electionStatus, electionOffice, electionDistrictName,
				candidateName, candidateParty, special);
		return Arrays.asList(contest);
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForFederalContest(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception {
		final String electionStage = "General";
		final String electionStatus = "Running";
		final String electionOffice = StandardContest.PRESIDENT.getOffice()[0];
		final String electionDistrictName = "";
		final String candidateName = "Presidential Candidate";
		final String candidateParty = "Presidential Party";
		final boolean special = false;
		final VipContest contest = createContest(electionStage, electionStatus, electionOffice, electionDistrictName,
				candidateName, candidateParty, special);
		return Arrays.asList(contest);
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForLocalContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		final String electionStage = "General";
		final String electionStatus = "Running";
		final String electionOffice = "School Board";
		final String electionDistrictName = "I";
		final String candidateName = "School Board Candidate";
		final String candidateParty = "";
		final boolean special = true;
		final VipContest contest = createContest(electionStage, electionStatus, electionOffice, electionDistrictName,
				candidateName, candidateParty, special);
		return Arrays.asList(contest);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpDataForNoContests(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		// Nothing to do.
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForOtherAddress(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		// All of the data would normally be at VoteSmart and there is nothing to be done at this end to eliminate it from
		// consideration. As such, there is nothing to do here.
		return new LinkedList<VipContest>();
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForStateContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		final String electionStage = "Primary";
		final String electionStatus = "Running";
		final String electionOffice = "State Senate";
		final String electionDistrictName = "A";
		final String candidateName = "State Senate Candidate";
		final String candidateParty = "State Senate Party";
		final boolean special = true;
		final VipContest contest = createContest(electionStage, electionStatus, electionOffice, electionDistrictName,
				candidateName, candidateParty, special);
		return Arrays.asList(contest);
	}

	/** {@inheritDoc} */
	@Override
	protected final VipCandidateBio setUpForCandidateBio() throws Exception {
		final long candidateId = 1234l;
		final String photoUrl = "http://www.candidate.org/photo.jpg";
		final VipCandidate candidate = buildCandidate(candidateId, null, null);
		final VipCandidateBio candidateBio = createMock("CandidateBio", VipCandidateBio.class);
		EasyMock.expect(getValet().acquireCandidateBio()).andReturn(candidateBio);
		candidateBio.setCandidate(candidate);
		EasyMock.expect(candidateBio.getCandidate()).andReturn(candidate).anyTimes();
		candidateBio.setPhotoUrl(photoUrl);
		StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append(CANDIDATE_BIO_START);
		sb.append(CANDIDATE_START);
		// TODO examine other fields to see if there is anything else of interest.
		addXMLProperty(CANDIDATE_ID, Long.toString(candidateId), sb);
		addXMLProperty(PHOTO, photoUrl, sb);
		sb.append(CANDIDATE_END);
		sb.append(CANDIDATE_BIO_END);
		EasyMock.expect(getVoteSmartService().retrieveCandidateBio(candidateId)).andReturn(sb.toString());

		sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append(CANDIDATE_ADDTIONAL_BIO_START);
		// TODO examine real data to see whether we might get something useful.
		sb.append(CANDIDATE_ADDITIONAL_BIO_END);
		EasyMock.expect(getVoteSmartService().retrieveCandidateAdditionalBio(candidateId)).andReturn(sb.toString());
		return candidateBio;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForCandidateFinder() {
		setVoteSmartService(createMock("VoteSmartService", VoteSmartService.class));
		setCandidates(new LinkedList<String>());
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForMissingCandidateBio(final long candidateVipId) {
		// TODO determine what the real message looks like and return that.
		EasyMock.expect(getVoteSmartService().retrieveCandidateBio(candidateVipId)).andReturn(null);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForMissingReferendumDetail(final long referendumVipId) {
		throw new UnsupportedOperationException("Referendums are not supported");
	}

	/** {@inheritDoc} */
	@Override
	protected final VipReferendumDetail setUpForReferendumDetail() {
		throw new UnsupportedOperationException("Referendums are not supported");
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForState() {
		getForStateOrVotingRegion().setStates(Arrays.asList(VALID_STATE));
		getForStateOrVotingRegion().setVotingRegionType("County");
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForVotingRegion() {
		final Map<String, Collection<String>> votingRegions = new HashMap<String, Collection<String>>();
		votingRegions.put(VALID_STATE, Arrays.asList(VALID_VOTING_REGION));
		getForStateOrVotingRegion().setVotingRegions(votingRegions);
		getForStateOrVotingRegion().setVotingRegionType("County");
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForCandidateFinder() {
		setCandidates(null);
		setVoteSmartService(null);
	}

	/**
	 * Creates a candidate record with the specified values.
	 * 
	 * @author IanBrown
	 * @param electionOffice
	 *            the office for which the candidate is running.
	 * @param electoralDistrictName
	 *            the name of the electoral district - may be blank.
	 * @param electionStage
	 *            the stage of the election.
	 * @param special
	 *            is this a special election?
	 * @param electionStatus
	 *            the status of the election.
	 * @param candidateName
	 *            the name of the candidate.
	 * @param candidateParty
	 *            the party of the candidate.
	 * @param ballot
	 *            the ballot.
	 * @since Sep 11, 2012
	 * @version Sep 12, 2012
	 */
	private void addCandidate(final String electionOffice, final String electoralDistrictName, final String electionStage,
			final boolean special, final String electionStatus, final String candidateName, final String candidateParty,
			final VipBallot ballot) {
		final long candidateId = candidateName.hashCode();
		final String candidateString = buildCandidateXML(electionOffice, electoralDistrictName, electionStage, electionStatus,
				special, candidateId, candidateName, candidateParty);
		getCandidates().add(candidateString);
		final VipCandidate candidate = buildCandidate(candidateId, candidateName, candidateParty);
		ballot.addCandidate(candidate);
	}

	/**
	 * Adds an XML property to the string builder.
	 * 
	 * @author IanBrown
	 * @param propertyName
	 *            the name of the property.
	 * @param propertyValue
	 *            the value of the property.
	 * @param sb
	 *            the string builder.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private void addXMLProperty(final String propertyName, final String propertyValue, final StringBuilder sb) {
		if (propertyValue != null && !propertyValue.trim().isEmpty()) {
			sb.append("<").append(propertyName).append(">").append(propertyValue).append("</").append(propertyName).append(">");
		} else {
			sb.append("<").append(propertyName).append("/>");
		}
	}

	/**
	 * Builds a VIP candidate for the input candidate name.
	 * 
	 * @author IanBrown
	 * @param candidateId
	 *            the candidate identifier.
	 * @param candidateName
	 *            the name of the candidate.
	 * @param candidateParty
	 *            the party of the candidate.
	 * @return the VIP candidate.
	 * @since Sep 11, 2012
	 * @version Sep 12, 2012
	 */
	private VipCandidate buildCandidate(final long candidateId, final String candidateName, final String candidateParty) {
		final String candidateMarker = candidateName == null ? "" : candidateName.replace(' ', '_');
		final VipCandidate candidate = createMock("Candidate" + candidateMarker, VipCandidate.class);
		EasyMock.expect(getValet().acquireCandidate()).andReturn(candidate);
		candidate.setVipId(candidateId);
		EasyMock.expect(candidate.getVipId()).andReturn(candidateId).anyTimes();
		if (candidateName != null && !candidateName.isEmpty()) {
			candidate.setName(candidateName);
		}
		if (candidateParty != null && !candidateParty.isEmpty()) {
			candidate.setParty(candidateParty);
		}
		return candidate;
	}

	/**
	 * Builds the XML string for the candidates in the specified ZIP/ZIP+4 area.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the ZIP code.
	 * @param zip4
	 *            the ZIP+4.
	 * @param candidates
	 *            the individual candidate strings.
	 * @return the full candidate string.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private String buildCandidatesXML(final String zip, final String zip4, final List<String> candidates) {
		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER).append(CANDIDATES_LIST_START);
		for (final String candidate : candidates) {
			sb.append(candidate);
		}
		sb.append(CANDIDATES_LIST_END);
		return sb.toString();
	}

	/**
	 * Builds the XML string for the candidate.
	 * 
	 * @author IanBrown
	 * @param electionOffice
	 *            the office for the election.
	 * @param electoralDistrictName
	 *            the name of the electoral district.
	 * @param electionStage
	 *            the stage of the election.
	 * @param electionStatus
	 *            the status of the election.
	 * @param special
	 *            is this a special election?
	 * @param candidateId
	 *            the candidate identifier.
	 * @param candidateName
	 *            the name of the candidate.
	 * @param candidateParty
	 *            the party of the candidate.
	 * @return the XML for the candidate.
	 * @since Sep 11, 2012
	 * @version Nov 1, 2012
	 */
	private String buildCandidateXML(final String electionOffice, final String electoralDistrictName, final String electionStage,
			final String electionStatus, final boolean special, final long candidateId, final String candidateName,
			final String candidateParty) {
		final StringBuilder sb = new StringBuilder();
		sb.append(CANDIDATE_START);
		addXMLProperty(CANDIDATE_ID, Long.toString(candidateId), sb);
		addXMLProperty(ELECTION_STAGE, XMLUtils.xmlEncodeString(electionStage), sb);
		addXMLProperty(ELECTION_STATUS, XMLUtils.xmlEncodeString(electionStatus), sb);
		addXMLProperty(ELECTION_SPECIAL, special ? "t" : "f", sb);
		addXMLProperty(CANDIDATE_NAME, XMLUtils.xmlEncodeString(candidateName), sb);
		addXMLProperty(CANDIDATE_PARTY, XMLUtils.xmlEncodeString(candidateParty), sb);
		addXMLProperty(ELECTION_OFFICE, XMLUtils.xmlEncodeString(electionOffice), sb);
		addXMLProperty(ELECTION_DISTRICT_NAME, electoralDistrictName, sb);
		sb.append(CANDIDATE_END);
		return sb.toString();
	}

	/**
	 * Creates an electoral district.
	 * 
	 * @author IanBrown
	 * @param electionOffice
	 *            the office.
	 * @param electionDistrictName
	 *            the election district name.
	 * @return the electoral district.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private VipElectoralDistrict createElectoralDistrict(final String electionOffice, final String electionDistrictName) {
		final VipElectoralDistrict electoralDistrict = createMock("FederalElectoralDistrict", VipElectoralDistrict.class);
		EasyMock.expect(getValet().acquireElectoralDistrict()).andReturn(electoralDistrict);
		if (electionDistrictName == null || electionDistrictName.isEmpty()) {
			electoralDistrict.setName("State-Wide");
			electoralDistrict.setType("State-Wide");
		} else {
			electoralDistrict.setName(electionDistrictName);
			electoralDistrict.setType(electionOffice);
		}
		return electoralDistrict;
	}

	/**
	 * Gets the candidates.
	 * 
	 * @author IanBrown
	 * @return the candidates.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private List<String> getCandidates() {
		return candidates;
	}

	/**
	 * Gets the VoteSmart service.
	 * 
	 * @author IanBrown
	 * @return the VoteSmart service.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private VoteSmartService getVoteSmartService() {
		return voteSmartService;
	}

	/**
	 * Sets the candidates.
	 * 
	 * @author IanBrown
	 * @param candidates
	 *            the candidates to set.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private void setCandidates(final List<String> candidates) {
		this.candidates = candidates;
	}

	/**
	 * Sets the VoteSmart service.
	 * 
	 * @author IanBrown
	 * @param voteSmartService
	 *            the VoteSmart service to set.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private void setVoteSmartService(final VoteSmartService voteSmartService) {
		this.voteSmartService = voteSmartService;
	}
}
