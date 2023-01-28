/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.candidate.model.OfficeType;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Extended {@link AbstractCandidateFinder} that reads a CSV file. This first implementation reads from two files downloaded from
 * Minnesota that provide just a text string to use to figure out which candidate belongs to which place. This requires some special
 * parsing code - will need a better mapping capability for the real finder. This is just a test implementation.
 * 
 * @author IanBrown
 * 
 * @since Jun 13, 2012
 * @version Jul 30, 2012
 */
@Component
public class CandidateFinderCSV extends AbstractCandidateFinder {

	/**
	 * the descriptive string for a council member.
	 * 
	 * @author IanBrown
	 * @since Jun 15, 2012
	 * @version Jun 15, 2012
	 */
	static final String COUNCIL_MEMBER = "Council Member (%1$s)";

	/**
	 * the descriptive string used to indicate a council member with a sub office (such as a ward).
	 * 
	 * @author IanBrown
	 * @since Jun 15, 2012
	 * @version Jun 15, 2012
	 */
	static final String COUNCIL_MEMBER_SUB_OFFICE = "Council Member %2$s (%1$s)";

	/**
	 * the descriptive string used to specify the county commissioner.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String COUNTY_COMMISSIONER = "County Commissioner District %1$s";

	/**
	 * the descriptive string for a judge on the state court of appeals.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String COURT_OF_APPEALS_JUDGE = "Judge - Court of Appeals %1$s";

	/**
	 * the format used to read dates.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");

	/**
	 * the descriptive string for a judge on a district court.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String DISTRICT_COURT_JUDGE = "Judge - %1$s District Court %2$s";

	/**
	 * the string used to indicate a candidate for president.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	static final String PRESIDENT = "President";

	/**
	 * the descriptive string used to specify a soil and water supervisor.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String SOIL_AND_WATER_SUPERVISOR = "Soil and Water Supervisor District %1$s";

	/**
	 * the prefix string used to flag a special election.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	static final String SPECIAL_ELECTION = "Special Election for";

	/**
	 * the descriptive string used to indicate a candidate for the state house of representatives.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String STATE_REPRESENTATIVE = "State Representative District %1$s";

	/**
	 * the descriptive string used to indicate a candidate for the state senate.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String STATE_SENATOR = "State Senator District %1$s";

	/**
	 * the descriptive string for an associate justice for the state supreme court.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String SUPREME_COURT_ASSOCIATE_JUSTICE = "Associate Justice - Supreme Court %1$s";

	/**
	 * the string for the chief justice of the state supreme court.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String SUPREME_COURT_CHIEF_JUSTICE = "Chief Justice - Supreme Court";

	/**
	 * the descriptive string used to indicate a candidate for the U.S. house of representatives.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 15, 2012
	 */
	static final String US_REPRESENTATIVE = "U.S. Representative District %1$s";

	/**
	 * the string used to indicate a candidate for the U.S. senate.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	static final String US_SENATOR = "U.S. Senator";

	/**
	 * the string used to indicate that a certain number of candidates should be selected.
	 * 
	 * @author IanBrown
	 * @since Jun 15, 2012
	 * @version Jun 15, 2012
	 */
	private final static String ELECT_NUMBER = "(Elect ";

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(CandidateFinderCSV.class);

	/**
	 * the format string used to specify the offices.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	private final static Map<String, OfficeType> OFFICES;

	/**
	 * the string description used to specify the mayor.
	 * 
	 * @author IanBrown
	 * @since Jun 15, 2012
	 * @version Jun 15, 2012
	 */
	static final String MAYOR = "Mayor (%1$s)";

	static {
		OFFICES = new LinkedHashMap<String, OfficeType>();
		OFFICES.put(PRESIDENT, OfficeType.PRESIDENT);
		OFFICES.put(US_SENATOR, OfficeType.US_SENATOR);
		OFFICES.put(US_REPRESENTATIVE, OfficeType.US_REPRESENTATIVE);
		OFFICES.put(STATE_SENATOR, OfficeType.STATE_SENATOR);
		OFFICES.put(STATE_REPRESENTATIVE, OfficeType.STATE_REPRESENTATIVE);
		OFFICES.put(COUNTY_COMMISSIONER, OfficeType.COUNTY_COMMISSIONER);
		OFFICES.put(SOIL_AND_WATER_SUPERVISOR, OfficeType.SOIL_AND_WATER_SUPERVISOR);
		OFFICES.put(SUPREME_COURT_CHIEF_JUSTICE, OfficeType.SUPREME_COURT_CHIEF_JUSTICE);
		OFFICES.put(SUPREME_COURT_ASSOCIATE_JUSTICE, OfficeType.SUPREME_COURT_ASSOCIATE_JUSTICE);
		OFFICES.put(COURT_OF_APPEALS_JUDGE, OfficeType.COURT_OF_APPEALS_JUDGE);
		OFFICES.put(DISTRICT_COURT_JUDGE, OfficeType.DISTRICT_COURT_JUDGE);
		OFFICES.put(MAYOR, OfficeType.MAYOR);

		// These two must be in this order to ensure that the more specific one is matched instead of the more general one.
		OFFICES.put(COUNCIL_MEMBER_SUB_OFFICE, OfficeType.COUNCIL_MEMBER);
		OFFICES.put(COUNCIL_MEMBER, OfficeType.COUNCIL_MEMBER);
	}

	/**
	 * the application context.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * the path to the FSC (Federal-State-County) file.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	private String fscFilePath;

	/**
	 * the path to the local file.
	 * 
	 * @author IanBrown
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	private String localFilePath;

	/**
	 * the available contests.
	 * 
	 * @author IanBrown
	 * @since Jul 2, 2012
	 * @version Aug 9, 2012
	 */
	private Map<String, VipContest> availableContests;

	/**
	 * the cnadidate bios.
	 * 
	 * @author IanBrown
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	private final Map<Long, VipCandidateBio> candidateBios = new TreeMap<Long, VipCandidateBio>();

	/**
	 * the last VIP identifier used.
	 * 
	 * @author IanBrown
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	private long vipId = 0l;

	/** {@inheritDoc} */
	@Override
	public VipCandidateBio findCandidateBio(final long candidateVipId) throws Exception {
		loadContests();
		return candidateBios.get(candidateVipId);
	}

	/** {@inheritDoc} */
	@Override
	public List<VipContest> findContests(final ValidAddress validAddress) throws Exception {
		final VipStreetSegment streetSegment = validAddress.getStreetSegment();
		loadContests();

		final List<VipContest> contests = locateContestsForStreetSegment(streetSegment);

		return contests;
	}

	/** {@inheritDoc} */
	@Override
	public VipReferendumDetail findReferendumDetail(final long referendumVipId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Gets the FSC (Federal-State-County) file path.
	 * 
	 * @author IanBrown
	 * @return the FSC file path.
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	public String getFscFilePath() {
		return fscFilePath;
	}

	/**
	 * Gets the local file path.
	 * 
	 * @author IanBrown
	 * @return the local file path.
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	public String getLocalFilePath() {
		return localFilePath;
	}

	/**
	 * Sets the FSC (Federal-State-County) file path.
	 * 
	 * @author IanBrown
	 * @param fscFilePath
	 *            the FSC file path to set.
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	public void setFscFilePath(final String fscFilePath) {
		this.fscFilePath = fscFilePath;
	}

	/**
	 * Sets the local file path.
	 * 
	 * @author IanBrown
	 * @param localFilePath
	 *            the local file path to set.
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	public void setLocalFilePath(final String localFilePath) {
		this.localFilePath = localFilePath;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean loadDataIfNeeded(final String stateIdentification, final String votingRegionName) {
		try {
			loadContests();
			return true;
		} catch (final IOException e) {
			LOGGER.warn("Failed to read data", e);
			return false;
		}
	}

	/**
	 * Finds the contests for the specified electoral district.
	 * 
	 * @author IanBrown
	 * @param electoralDistrict
	 *            the electoral districts.
	 * @param contests
	 *            the contests found.
	 * @since Jul 3, 2012
	 * @version Aug 9, 2012
	 */
	private void findContestsForElectoralDistrict(final VipElectoralDistrict electoralDistrict, final List<VipContest> contests) {
		for (final VipContest contest : getAvailableContests().values()) {
			if (contest.getElectoralDistrict().getName().equals(electoralDistrict.getName())) {
				contests.add(contest);
			}
		}
	}

	/**
	 * Gets the available contests.
	 * 
	 * @author IanBrown
	 * @return the available contests by office.
	 * @since Jul 2, 2012
	 * @version Jul 3, 2012
	 */
	private Map<String, VipContest> getAvailableContests() {
		return availableContests;
	}

	/**
	 * Loads the contests from the data files if they have no previously been loaded.
	 * 
	 * @author IanBrown
	 * @throws IOException
	 *             if a file cannot be read.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private void loadContests() throws IOException {
		if (getAvailableContests() == null) {
			setAvailableContests(new LinkedHashMap<String, VipContest>());
			if (getFscFilePath() != null) {
				loadFscFile();
			}

			if (getLocalFilePath() != null) {
				loadLocalFile();
			}
		}
	}

	/**
	 * Loads the input file into the available contests.
	 * 
	 * @author IanBrown
	 * @param file
	 *            the file.
	 * @throws IOException
	 *             if the file cannot be read.
	 * @since Jul 2, 2012
	 * @version Aug 17, 2012
	 */
	private void loadFile(final File file) throws IOException {
		final LineNumberReader lnr = new LineNumberReader(new FileReader(file));
		try {
			String line;

			while ((line = lnr.readLine()) != null) {
				if (line.startsWith("#") || line.startsWith("//")) {
					continue;
				}
				final String[] parts = line.split(";");
				final String[] office = parseOffice(parts[3]);
				if (office.length == 0) {
					// TODO
					continue;
				}
				VipContest contest = getAvailableContests().get(parts[3]);
				final VipBallot ballot;
				if (contest == null) {
					contest = getValet().acquireContest();
					contest.setVipId(++vipId);
					ballot = getValet().acquireBallot();
					ballot.setVipId(++vipId);
					contest.setOffice(office[0]);
					contest.setBallot(ballot);
					final VipElectoralDistrict electoralDistrict = getValet().acquireElectoralDistrict();
					electoralDistrict.setVipId(++vipId);
					if (office.length == 1) {
						electoralDistrict.setName(office[0]);
					} else if (office.length == 2) {
						electoralDistrict.setName(office[1]);
					} else {
						electoralDistrict.setName(office[1] + " " + office[2]);
					}
					contest.setElectoralDistrict(electoralDistrict);
				} else {
					ballot = contest.getBallot();
				}
				final VipCandidate candidate = getValet().acquireCandidate();
				candidate.setVipId(++vipId);
				candidate.setName(parts[1]);
				candidate.setParty(parts[5]);
				final VipCandidateBio candidateBio = getValet().acquireCandidateBio();
				candidateBio.setCandidate(candidate);
				candidateBio.setCandidateUrl(parts[15]);
				candidateBio.setEmail(parts[16]);
				final UserAddress filedMailingAddress = getValet().acquireUserAddress();
				filedMailingAddress.setStreet1(parts[6]);
				filedMailingAddress.setCity(parts[7]);
				filedMailingAddress.setState(parts[8]);
				if (parts[9].length() >= 5) {
					filedMailingAddress.setZip(parts[9].substring(0, 5));
				}
				candidateBio.setFiledMailingAddress(filedMailingAddress);
				candidateBio.setPhone(parts[14]);
				ballot.addCandidate(candidate);
				candidateBios.put(candidate.getVipId(), candidateBio);
				getAvailableContests().put(parts[3], contest);
			}
		} finally {
			lnr.close();
		}
	}

	/**
	 * Loads the contests from the federal-state-county file.
	 * 
	 * @author IanBrown
	 * @throws IOException
	 *             if there is a problem loading the FSC file.
	 * @since Jul 2, 2012
	 * @version Jul 3, 2012
	 */
	private void loadFscFile() throws IOException {
		final File fscFile = locateFile(getFscFilePath());
		loadFile(fscFile);
	}

	/**
	 * Loads the local file into the available contests.
	 * 
	 * @author IanBrown
	 * @throws IOException
	 *             if the file cannot be read.
	 * @since Jul 2, 2012
	 * @version Jul 2, 2012
	 */
	private void loadLocalFile() throws IOException {
		final File localFile = locateFile(getLocalFilePath());
		loadFile(localFile);
	}

	/**
	 * Finds the contests that match the street segment.
	 * 
	 * @author IanBrown
	 * @param streetSegment
	 *            the street segment.
	 * @return the contests that match.
	 * @since Jul 2, 2012
	 * @version Aug 9, 2012
	 */
	private List<VipContest> locateContestsForStreetSegment(final VipStreetSegment streetSegment) {
		final List<VipContest> contests = new LinkedList<VipContest>();
		final VipPrecinct precinct = streetSegment.getPrecinct();
		final List<VipElectoralDistrict> precinctElectoralDistricts = precinct.getElectoralDistricts();
		for (final VipElectoralDistrict electoralDistrict : precinctElectoralDistricts) {
			findContestsForElectoralDistrict(electoralDistrict, contests);
		}
		final VipPrecinctSplit precinctSplit = streetSegment.getPrecinctSplit();
		if (precinctSplit != null) {
			final List<VipElectoralDistrict> precinctSplitElectoralDistricts = precinctSplit.getElectoralDistricts();
			for (final VipElectoralDistrict electoralDistrict : precinctSplitElectoralDistricts) {
				findContestsForElectoralDistrict(electoralDistrict, contests);
			}
		}
		return contests;
	}

	/**
	 * Locates the file for the specified path.
	 * 
	 * @author IanBrown
	 * @param filePath
	 *            the file path.
	 * @return the file.
	 * @since Jun 14, 2012
	 * @version Jun 14, 2012
	 */
	private File locateFile(final String filePath) {
		File file = null;
		if (applicationContext != null) {
			final Resource csvResource = applicationContext.getResource("WEB-INF/classes/" + filePath);
			try {
				final File resourceFile = csvResource.getFile();
				if (resourceFile.canRead()) {
					file = resourceFile;
				}
			} catch (final IOException e) {
			}
		}
		if (file == null) {
			final URL url = ClassLoader.getSystemResource(filePath);
			if (url == null) {
				file = new File(filePath);
			} else {
				file = new File(url.getPath().substring(1));
			}
			if (!file.canRead()) {
				throw new IllegalStateException("No readable CSV file " + filePath + " can be found");
			}
		}

		return file;
	}

	/**
	 * Parses the office of the candidate.
	 * 
	 * @author IanBrown
	 * @param string
	 *            the string representing the office of the candidate.
	 * @return the parsed office.
	 * @since Jun 14, 2012
	 * @version Jul 2, 2012
	 */
	private String[] parseOffice(final String string) {
		final List<String> officeParts = new LinkedList<String>();
		String parseString;
		if (string.startsWith(SPECIAL_ELECTION)) {
			parseString = string.substring(SPECIAL_ELECTION.length()).trim();
			// TODO should we flag special elections?
		} else if (string.contains(ELECT_NUMBER)) {
			final int idx = string.indexOf(ELECT_NUMBER);
			final int endIdx = string.indexOf(")", idx) + 1;
			parseString = string.substring(0, idx).trim()
					+ (endIdx == string.length() ? "" : " " + string.substring(endIdx).trim());
		} else {
			parseString = string;
		}
		for (final Map.Entry<String, OfficeType> office : OFFICES.entrySet()) {
			final String formatString = office.getKey();
			final OfficeType officeType = office.getValue();

			officeParts.clear();
			if (tryOfficeFormat(parseString, formatString, officeParts)) {
				officeParts.add(0, officeType.toString());
				break;
			}
		}

		return officeParts.toArray(new String[officeParts.size()]);
	}

	/**
	 * Returns the remaining tokens from the tokenizer.
	 * 
	 * @author IanBrown
	 * @param parseTokenizer
	 *            the parse tokenizer.
	 * @return the remaining tokens.
	 * @since Jun 15, 2012
	 * @version Jun 15, 2012
	 */
	private String remainingTokens(final StringTokenizer parseTokenizer) {
		final StringBuilder sb = new StringBuilder("");
		while (parseTokenizer.hasMoreTokens()) {
			sb.append(parseTokenizer.nextToken());
		}
		return sb.toString().trim();
	}

	/**
	 * Sets the available contests.
	 * 
	 * @author IanBrown
	 * @param availableContests
	 *            the available contests to set.
	 * @since Jul 2, 2012
	 * @version Jul 3, 2012
	 */
	private void setAvailableContests(final Map<String, VipContest> availableContests) {
		this.availableContests = availableContests;
	}

	/**
	 * Tries the specified office format string to see if it matches the parse string.
	 * 
	 * @author IanBrown
	 * @param parseString
	 *            the string to parse.
	 * @param formatString
	 *            the format string to use.
	 * @param officeParts
	 *            the list of office parts.
	 * @return <code>true</code> if the format string matched, <code>false</code> if it did not.
	 * @since Jun 15, 2012
	 * @version Jun 29, 2012
	 */
	private boolean tryOfficeFormat(final String parseString, final String formatString, final List<String> officeParts) {
		final StringTokenizer parseTokenizer = new StringTokenizer(parseString, " ()", true);
		final StringTokenizer formatTokenizer = new StringTokenizer(formatString, " ()", true);
		String officeId = null;
		String subOfficeId = null;
		boolean inOfficeId = false;
		boolean inSubOfficeId = false;

		while (parseTokenizer.hasMoreTokens() && formatTokenizer.hasMoreTokens()) {
			String formatToken = formatTokenizer.nextToken();
			String parseToken = parseTokenizer.nextToken();

			if ((formatToken.equals("(") || formatToken.equals(")")) && formatToken.equals(parseToken)) {
				inOfficeId = inSubOfficeId = false;
				continue;
			}

			if (!inOfficeId && !inSubOfficeId) {
				if (formatToken.equals("%1$s")) {
					inOfficeId = true;
					officeId = parseToken;
				} else if (formatToken.equals("%2$s")) {
					inSubOfficeId = true;
					subOfficeId = parseToken;
				} else if (!formatToken.equals(parseToken)) {
					return false;
				}
			} else {
				while (formatToken.equals(" ") && formatTokenizer.hasMoreTokens()) {
					formatToken = formatTokenizer.nextToken();
				}

				if (formatToken.equals(" ")) {
					if (inOfficeId) {
						officeId += parseToken + remainingTokens(parseTokenizer);
					} else if (inSubOfficeId) {
						subOfficeId += parseToken + remainingTokens(parseTokenizer);
					}
					break;
				}

				if (parseToken.equals(formatToken)) {
					inOfficeId = inSubOfficeId = false;
				} else {
					if (!inOfficeId && !inSubOfficeId) {
						return false;
					}

					while (!parseToken.equals(formatToken)) {
						if (inOfficeId) {
							officeId += parseToken;
						} else {
							subOfficeId += parseToken;
						}
						if (parseTokenizer.hasMoreTokens()) {
							parseToken = parseTokenizer.nextToken();
						} else {
							return false;
						}
					}

					inOfficeId = inSubOfficeId = false;
				}
			}
		}

		final boolean matched = !parseTokenizer.hasMoreTokens() && !formatTokenizer.hasMoreTokens();
		if (matched) {
			if (officeId != null) {
				officeParts.add(officeId.trim());
				if (subOfficeId != null) {
					officeParts.add(subOfficeId.trim());
				}
			}
		}
		return matched;
	}
	
	/** {@inheritDoc} */
	@Override
    public VipElection findElection(String stateAbbreviation,
            String votingRegionName) {
	    // TODO Auto-generated method stub
	    return null;
    }

}
