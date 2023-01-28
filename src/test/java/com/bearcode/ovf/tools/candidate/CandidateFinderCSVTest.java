/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.candidate.model.OfficeType;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link CandidateFinderCheck} test for {@link CandidateFinderCSV}.
 * 
 * @author IanBrown
 * 
 * @since Jul 2, 2012
 * @version Oct 10, 2012
 */
public final class CandidateFinderCSVTest extends CandidateFinderCheck<CandidateFinderCSV> {

	/**
	 * the valid state.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private static final String VALID_STATE = "MN";

	/**
	 * the name of the city.
	 * 
	 * @author IanBrown
	 * @return
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private final static String CITY_NAME = "City";

	/**
	 * the name of the county.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private final static String COUNTY_NAME = "County";

	/**
	 * the ward.
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private final static String WARD = "Ward 1";

	/**
	 * the state district.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private static final String STATE_DISTRICT = "23";

	/**
	 * the US district.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private static final String US_DISTRICT = "1";

	/**
	 * the valid ZIP code.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private static final String VALID_ZIP = "23212";

	/**
	 * the valid voting region.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private static final String VALID_VOTING_REGION = "Valid Voting Region";

	/**
	 * the current VIP identifier.
	 * 
	 * @author IanBrown
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	private long vipId;

	/** {@inheritDoc} */
	@Override
	protected final boolean areReferendumsSupported() {
		// I don't have referendum data and right now don't see the point to trying to implement it for the CSV files. Might address
		// again in the future.
		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected final void completeSetUp(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		// Nothing more to do.
	}

	/** {@inheritDoc} */
	@Override
	protected final CandidateFinderCSV createCandidateFinder() {
		final CandidateFinderCSV candidateFinder = new CandidateFinderCSV();
		vipId = 0l;
		return candidateFinder;
	}

	/** {@inheritDoc} */
	@Override
	protected final ValidAddress createValidAddress(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(validAddress.getStreetSegment()).andReturn(createStreetSegment(zip, zip4, electoralDistricts)).anyTimes();
		return validAddress;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidState() {
		return "MA";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidVotingRegion() {
		return "Invalid Voting Region";
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
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForCountyContest(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception {
		final List<VipContest> contests = new LinkedList<VipContest>();
		final File fscFile = File.createTempFile("FscElections", ".csv");
		getForStateOrVotingRegion().setFscFilePath(fscFile.getAbsolutePath());
		final PrintWriter pw = new PrintWriter(new FileWriter(fscFile));
		try {
			addCandidate(pw, electoralDistricts, true, contests, "CountyCommissioner", OfficeType.COUNTY_COMMISSIONER,
					CandidateFinderCSV.COUNTY_COMMISSIONER, COUNTY_NAME);
		} finally {
			pw.close();
		}
		return contests;
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForFederalContest(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) throws Exception {
		final List<VipContest> contests = new LinkedList<VipContest>();
		final File fscFile = File.createTempFile("FscElections", ".csv");
		getForStateOrVotingRegion().setFscFilePath(fscFile.getAbsolutePath());
		final PrintWriter pw = new PrintWriter(new FileWriter(fscFile));
		try {
			addCandidate(pw, electoralDistricts, true, contests, "USRepresentative", OfficeType.US_REPRESENTATIVE,
					CandidateFinderCSV.US_REPRESENTATIVE, US_DISTRICT);
		} finally {
			pw.close();
		}
		return contests;
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForLocalContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		final List<VipContest> contests = new LinkedList<VipContest>();
		final File localFile = File.createTempFile("LocalElections", ".csv");
		getForStateOrVotingRegion().setLocalFilePath(localFile.getAbsolutePath());
		final PrintWriter pw = new PrintWriter(new FileWriter(localFile));
		try {
			addCandidate(pw, electoralDistricts, true, contests, "CouncilMember", OfficeType.COUNCIL_MEMBER,
					CandidateFinderCSV.COUNCIL_MEMBER_SUB_OFFICE, CITY_NAME, WARD);
		} finally {
			pw.close();
		}
		return contests;
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
		final List<VipContest> contests = new LinkedList<VipContest>();
		final File localFile = File.createTempFile("LocalElections", ".csv");
		getForStateOrVotingRegion().setLocalFilePath(localFile.getAbsolutePath());
		final PrintWriter pw = new PrintWriter(new FileWriter(localFile));
		try {
			addCandidate(pw, electoralDistricts, false, contests, "CouncilMember", OfficeType.COUNCIL_MEMBER,
					CandidateFinderCSV.COUNCIL_MEMBER_SUB_OFFICE, CITY_NAME + "A", WARD + "1");
		} finally {
			pw.close();
		}
		return contests;
	}

	/** {@inheritDoc} */
	@Override
	protected final List<VipContest> setUpDataForStateContest(final Map<String, VipElectoralDistrict> electoralDistricts)
			throws Exception {
		final List<VipContest> contests = new LinkedList<VipContest>();
		final File fscFile = File.createTempFile("FscElections", ".csv");
		getForStateOrVotingRegion().setFscFilePath(fscFile.getAbsolutePath());
		final PrintWriter pw = new PrintWriter(new FileWriter(fscFile));
		try {
			addCandidate(pw, electoralDistricts, true, contests, "StateRepresentative", OfficeType.STATE_REPRESENTATIVE,
					CandidateFinderCSV.STATE_REPRESENTATIVE, STATE_DISTRICT);
		} finally {
			pw.close();
		}
		return contests;
	}

	/** {@inheritDoc} */
	@Override
	protected final VipCandidateBio setUpForCandidateBio() throws Exception {
		final List<VipContest> contests = new LinkedList<VipContest>();
		final File localFile = File.createTempFile("LocalElections", ".csv");
		getForStateOrVotingRegion().setLocalFilePath(localFile.getAbsolutePath());
		final PrintWriter pw = new PrintWriter(new FileWriter(localFile));
		try {
			final Map<String, VipElectoralDistrict> electoralDistricts = new LinkedHashMap<String, VipElectoralDistrict>();
			final VipCandidateBio candidateBio = addCandidate(pw, electoralDistricts, true, contests, "CouncilMember",
					OfficeType.COUNCIL_MEMBER, CandidateFinderCSV.COUNCIL_MEMBER_SUB_OFFICE, CITY_NAME, WARD);
			return candidateBio;
		} finally {
			pw.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForCandidateFinder() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForMissingCandidateBio(final long candidateVipId) {
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForMissingReferendumDetail(final long referendumVipId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/** {@inheritDoc} */
	@Override
	protected final VipReferendumDetail setUpForReferendumDetail() {
		throw new UnsupportedOperationException("Not implemented yet");
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
	}

	/**
	 * Adds a candidate with the specified name.
	 * 
	 * @author IanBrown
	 * @param pw
	 *            the print writer.
	 * @param electoralDistricts
	 *            the electoral districts.
	 * @param expectCandidate
	 *            is the candidate expected?
	 * @param contests
	 *            the contests.
	 * @param name
	 *            the candidate's name.
	 * @param officeType
	 *            the type of office.
	 * @param officeFormat
	 *            the format string for the office.
	 * @param officeValues
	 *            the values for the office.
	 * @return the candidate bio.
	 * @since Jul 2, 2012
	 * @version Aug 17, 2012
	 */
	private VipCandidateBio addCandidate(final PrintWriter pw, final Map<String, VipElectoralDistrict> electoralDistricts,
			final boolean expectCandidate, final Collection<VipContest> contests, final String name, final OfficeType officeType,
			final String officeFormat, final Object... officeValues) {
		final String officeName = String.format(officeFormat, officeValues);
		final String party = officeName.substring(0, 1);
		final String street1 = Math.abs(officeName.hashCode()) % 100 + " " + officeName + " Street";
		final String city = "City of " + officeName;
		final String state = officeName.substring(0, 2);
		String zip = Integer.toString(Math.abs(officeName.hashCode()) % 100000);
		while (zip.length() < 5) {
			zip = "0" + zip;
		}
		final String phone = Integer.toString(officeName.hashCode());
		final String url = "http://www." + officeName.replace(' ', '_') + ".org";
		final String email = "email@" + officeName.replace(' ', '_') + ".org";
		pw.append(";").append(name).append(";;").append(officeName).append(";;").append(party).append(";").append(street1)
				.append(";").append(city).append(";").append(state).append(";").append(zip).append(";;;;;").append(phone)
				.append(";").append(url).append(";").append(email).append(";;;");
		final VipContest contest = createMock("Contest" + name, VipContest.class);
		EasyMock.expect(getValet().acquireContest()).andReturn(contest);
		contest.setVipId(++vipId);
		contest.setOffice(officeType.toString());
		EasyMock.expect(contest.getOffice()).andReturn(officeType.toString()).anyTimes();
		final VipBallot ballot = createMock("Ballot" + name, VipBallot.class);
		EasyMock.expect(getValet().acquireBallot()).andReturn(ballot);
		ballot.setVipId(++vipId);
		contest.setBallot(ballot);
		EasyMock.expect(contest.getBallot()).andReturn(ballot).anyTimes();
		final String edName;
		if (officeValues.length == 1) {
			edName = officeValues[0].toString();
		} else {
			edName = officeValues[0].toString() + " " + officeValues[1].toString();
		}
		VipElectoralDistrict electoralDistrict;
		if ((electoralDistrict = electoralDistricts.get(name)) == null) {
			electoralDistrict = createMock("ElectoralDistrict" + name, VipElectoralDistrict.class);
			electoralDistrict.setName(edName);
			electoralDistricts.put(name, electoralDistrict);
			EasyMock.expect(getValet().acquireElectoralDistrict()).andReturn(electoralDistrict);
			electoralDistrict.setVipId(++vipId);
		}
		EasyMock.expect(electoralDistrict.getName()).andReturn(edName).anyTimes();
		contest.setElectoralDistrict(electoralDistrict);
		EasyMock.expect(contest.getElectoralDistrict()).andReturn(electoralDistrict).anyTimes();
		final VipCandidate candidate = createMock("Candidate" + name, VipCandidate.class);
		EasyMock.expect(getValet().acquireCandidate()).andReturn(candidate);
		candidate.setVipId(++vipId);
		EasyMock.expect(candidate.getVipId()).andReturn(vipId - 1).anyTimes();
		candidate.setName(name);
		candidate.setParty(party);
		final VipCandidateBio candidateBio = createMock("CandidateBio" + name, VipCandidateBio.class);
		EasyMock.expect(getValet().acquireCandidateBio()).andReturn(candidateBio);
		candidateBio.setCandidate(candidate);
		EasyMock.expect(candidateBio.getCandidate()).andReturn(candidate).anyTimes();
		candidateBio.setCandidateUrl(url);
		candidateBio.setEmail(email);
		final UserAddress filedMailingAddress = createMock("FiledMailingAddress", UserAddress.class);
		EasyMock.expect(getValet().acquireUserAddress()).andReturn(filedMailingAddress);
		filedMailingAddress.setStreet1(street1);
		filedMailingAddress.setCity(city);
		filedMailingAddress.setState(state);
		filedMailingAddress.setZip(zip);
		candidateBio.setFiledMailingAddress(filedMailingAddress);
		candidateBio.setPhone(phone);
		ballot.addCandidate(candidate);
		if (expectCandidate) {
			contests.add(contest);
		}
		return candidateBio;
	}

	/**
	 * Creates a precinct.
	 * 
	 * @author IanBrown
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the precinct.
	 * @since Jul 2, 2012
	 * @version Aug 9, 2012
	 */
	private VipPrecinct createPrecinct(final Map<String, VipElectoralDistrict> electoralDistricts) {
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		final List<VipElectoralDistrict> precinctElectoralDistricts = new LinkedList<VipElectoralDistrict>();
		final VipElectoralDistrict countyDistrict = createMock("CountyDistrict", VipElectoralDistrict.class);
		EasyMock.expect(countyDistrict.getName()).andReturn(COUNTY_NAME).anyTimes();
		precinctElectoralDistricts.add(countyDistrict);
		electoralDistricts.put(COUNTY_NAME, countyDistrict);
		final VipElectoralDistrict stateDistrict = createMock("StateDistrict", VipElectoralDistrict.class);
		EasyMock.expect(stateDistrict.getName()).andReturn(STATE_DISTRICT).anyTimes();
		precinctElectoralDistricts.add(stateDistrict);
		electoralDistricts.put(STATE_DISTRICT, stateDistrict);
		final VipElectoralDistrict usDistrict = createMock("USDistrict", VipElectoralDistrict.class);
		EasyMock.expect(usDistrict.getName()).andReturn(US_DISTRICT).anyTimes();
		precinctElectoralDistricts.add(usDistrict);
		electoralDistricts.put(US_DISTRICT, usDistrict);
		EasyMock.expect(precinct.getElectoralDistricts()).andReturn(precinctElectoralDistricts).anyTimes();
		return precinct;
	}

	/**
	 * Creates a precinct split.
	 * 
	 * @author IanBrown
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the precinct split.
	 * @since Jul 2, 2012
	 * @version Aug 9, 2012
	 */
	private VipPrecinctSplit createPrecinctSplit(final Map<String, VipElectoralDistrict> electoralDistricts) {
		final VipPrecinctSplit precinctSplit = createMock("PrecinctSplit", VipPrecinctSplit.class);
		final List<VipElectoralDistrict> precinctSplitElectoralDistricts = new LinkedList<VipElectoralDistrict>();
		EasyMock.expect(precinctSplit.getElectoralDistricts()).andReturn(precinctSplitElectoralDistricts).anyTimes();
		final VipElectoralDistrict wardDistrict = createMock("WardDistrict", VipElectoralDistrict.class);
		final String wardName = CITY_NAME + " " + WARD;
		EasyMock.expect(wardDistrict.getName()).andReturn(wardName).anyTimes();
		precinctSplitElectoralDistricts.add(wardDistrict);
		electoralDistricts.put(wardName, wardDistrict);
		return precinctSplit;
	}

	/**
	 * Creates a street segment.
	 * 
	 * @author IanBrown
	 * @param zip
	 *            the ZIP code.
	 * @param zip4
	 *            the ZIP+4.
	 * @param electoralDistricts
	 *            the electoral districts by name.
	 * @return the street segment.
	 * @since Jul 2, 2012
	 * @version Sep 11 2012
	 */
	private VipStreetSegment createStreetSegment(final String zip, final String zip4,
			final Map<String, VipElectoralDistrict> electoralDistricts) {
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(streetSegment.getPrecinct()).andReturn(createPrecinct(electoralDistricts)).anyTimes();
		EasyMock.expect(streetSegment.getPrecinctSplit()).andReturn(createPrecinctSplit(electoralDistricts)).anyTimes();
		return streetSegment;
	}
}
