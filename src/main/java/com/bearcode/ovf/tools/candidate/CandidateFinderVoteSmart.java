/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bearcode.ovf.actions.questionnaire.StandardContest;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;

/**
 * Extended {@link AbstractCandidateFinder} that uses VoteSmart to retrieve the candidate information.
 * 
 * @author IanBrown
 * 
 * @since Sep 6, 2012
 * @version Jul 30, 2012
 */
public class CandidateFinderVoteSmart extends AbstractCandidateFinder {

	/**
	 * the logger used for VoteSmart services.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 13, 2012
	 */
	private final static Logger voteSmartLog = LoggerFactory.getLogger("com.bearcode.VoteSmart");

	/**
	 * the VoteSmart service used to get the data.
	 * 
	 * @author IanBrown
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	@Autowired
	private VoteSmartService voteSmartService;

	/**
	 * the map of standard office names from those used by VoteSmart.
	 * 
	 * @author IanBrown
	 * @since Sep 13, 2012
	 * @version Sep 13, 2012
	 */
	private final static Map<String, StandardContest> STANDARD_OFFICES;

	static {
		STANDARD_OFFICES = new HashMap<String, StandardContest>();
		STANDARD_OFFICES.put("PRESIDENT", StandardContest.PRESIDENT);
		STANDARD_OFFICES.put("U.S. HOUSE", StandardContest.REPRESENTATIVE);
		STANDARD_OFFICES.put("U.S. SENATE", StandardContest.SENATOR);
	}

	/**
	 * Acquires the electoral district for the specified office and district name.
	 * 
	 * @author IanBrown
	 * @param electionOffice
	 *            the election office.
	 * @param electionDistrictName
	 *            the election district name.
	 * @param electoralDistrictsById
	 *            the electoral districts by identifier.
	 * @return the electoral district.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	public VipElectoralDistrict acquireElectoralDistrict(final String electionOffice, final String electionDistrictName,
			final Map<String, VipElectoralDistrict> electoralDistrictsById) {
		VipElectoralDistrict electoralDistrict;
		final String electoralDistrictId = electionOffice + " " + electionDistrictName;
		electoralDistrict = electoralDistrictsById.get(electoralDistrictId);
		if (electoralDistrict == null) {
			electoralDistrict = getValet().acquireElectoralDistrict();
			if (electionDistrictName == null || electionDistrictName.isEmpty()
					|| "State-Wide".equalsIgnoreCase(electionDistrictName)) {
				electoralDistrict.setName("State-Wide");
				electoralDistrict.setType("State-Wide");
			} else {
				electoralDistrict.setName(electionDistrictName);
				electoralDistrict.setType(electionOffice);
			}
			electoralDistrictsById.put(electoralDistrictId, electoralDistrict);
		}
		return electoralDistrict;
	}

	/**
	 * Extracts the candidate from the candidate node.
	 * 
	 * @author IanBrown
	 * @param candidateNode
	 *            the candidate node.
	 * @param electionParties
	 *            the election parties.
	 * @return the candidate.
	 * @since Sep 12, 2012
	 * @version Sep 13, 2012
	 */
	public VipCandidate extractCandidate(final ConfigurationNode candidateNode, final String electionParties) {
		final VipCandidate candidate = getValet().acquireCandidate();
		candidate.setVipId(Long.parseLong(retrieveNodeValue(candidateNode, "candidateId")));
		final String ballotName = retrieveNodeValue(candidateNode, "ballotName");
		if (ballotName != null && !ballotName.trim().isEmpty()) {
			candidate.setName(ballotName);
		}
		if (electionParties != null && !electionParties.trim().isEmpty()) {
			candidate.setParty(electionParties);
		}
		return candidate;
	}

	/** {@inheritDoc} */
	@Override
	public VipCandidateBio findCandidateBio(final long candidateVipId) throws Exception {
		VipCandidateBio candidateBio = null;
		final String candidateBioString = getVoteSmartService().retrieveCandidateBio(candidateVipId);
		if (candidateBioString != null) { // TODO determine what the real string looks like when there is nothing.
			XMLConfiguration xmlReader = new XMLConfiguration();
			xmlReader.setDelimiterParsingDisabled(true);
			InputStream is = IOUtils.toInputStream(candidateBioString);
			xmlReader.load(is);

			final String error = xmlReader.getString("errorMessage", "");
			if (error != null && error.length() > 0) {
				voteSmartLog.error("Error occured for candidateId=" + candidateVipId + ", error=" + error);
				return null;
			}

			final VipCandidate candidate = getValet().acquireCandidate();
			candidate.setVipId(candidateVipId);
			candidateBio = getValet().acquireCandidateBio();
			candidateBio.setCandidate(candidate);
			candidateBio.setPhotoUrl(xmlReader.getString("candidate.photo", null));
			// TODO

			final String candidateAdditionalBioString = getVoteSmartService().retrieveCandidateAdditionalBio(candidateVipId);
			xmlReader = new XMLConfiguration();
			xmlReader.setDelimiterParsingDisabled(true);
			is = IOUtils.toInputStream(candidateAdditionalBioString);
			xmlReader.load(is);

			// TODO
		}

		return candidateBio;
	}

	/** {@inheritDoc} */
	@Override
	public List<VipContest> findContests(final ValidAddress validAddress) throws Exception {
		final UserAddress validatedAddress = validAddress.getValidatedAddress();
		final String zip = validatedAddress.getZip();
		final String zip4 = validatedAddress.getZip4();
		final String candidatesList = getVoteSmartService().retrieveCandidatesByZip(zip, zip4);
		final Map<String, VipContest> contestsById = new TreeMap<String, VipContest>();
		final Map<String, VipElectoralDistrict> electoralDistrictsById = new TreeMap<String, VipElectoralDistrict>();
		parseCandidates(candidatesList, contestsById, electoralDistrictsById);
		return new ArrayList<VipContest>(contestsById.values());
	}

	/** {@inheritDoc} */
	@Override
	public VipReferendumDetail findReferendumDetail(final long referendumVipId) {
		throw new UnsupportedOperationException("Referendums are not supported");
	}

	/**
	 * Gets the VoteSmart service.
	 * 
	 * @author IanBrown
	 * @return the VoteSmart service.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	public VoteSmartService getVoteSmartService() {
		return voteSmartService;
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
	public void setVoteSmartService(final VoteSmartService voteSmartService) {
		this.voteSmartService = voteSmartService;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean loadDataIfNeeded(final String stateIdentification, final String votingRegionName) {
		return true;
	}

	/**
	 * Builds a candidates string for the input candidates list. This is either the input list string or a truncated version
	 * thereof.
	 * 
	 * @author IanBrown
	 * @param candidatesList
	 *            the candidates list.
	 * @return the candidates string.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private String buildCandidatesString(final String candidatesList) {
		final String candidatesString;
		if (candidatesList.length() <= 128) {
			candidatesString = candidatesList;
		} else {
			candidatesString = candidatesList.substring(0, 128) + "...";
		}
		return candidatesString;
	}

	/**
	 * Parses the input candidate.
	 * 
	 * @author IanBrown
	 * @param candidateNode
	 *            the node containing the candidate information.
	 * @param contestsById
	 *            the contests by identifier.
	 * @param electoralDistrictsById
	 *            the electoral districts by identifier.
	 * @since Sep 11, 2012
	 * @version Sep 12, 2012
	 */
	private void parseCandidate(final ConfigurationNode candidateNode, final Map<String, VipContest> contestsById,
			final Map<String, VipElectoralDistrict> electoralDistrictsById) {
		final String electionStatus = retrieveNodeValue(candidateNode, "electionStatus");
		if (!"RUNNING".equalsIgnoreCase(electionStatus) && !"POTENTIAL".equalsIgnoreCase(electionStatus)) {
			return;
		}

		final String electionStage = retrieveNodeValue(candidateNode, "electionStage");
		final String special = retrieveNodeValue(candidateNode, "electionSpecial").toUpperCase();
		final boolean specialFlag = special.startsWith("T");
		final String electionOffice = standardizeOffice(retrieveNodeValue(candidateNode, "electionOffice"));
		final String electionDistrictName = retrieveNodeValue(candidateNode, "electionDistrictName");
		final String electionParties = retrieveNodeValue(candidateNode, "electionParties");
		final StringBuilder contestSB = new StringBuilder();
		String prefix = "";
		if (specialFlag) {
			contestSB.append(prefix).append("Special");
			prefix = " ";
		}
		if (!"GENERAL".equalsIgnoreCase(electionStage)) {
			contestSB.append(prefix).append(electionStage);
			prefix = " ";
			if ("PRIMARY".equalsIgnoreCase(electionStage) && electionParties != null) {
				contestSB.append(prefix).append(electionParties);
			}
		}

		final VipElectoralDistrict electoralDistrict = acquireElectoralDistrict(electionOffice, electionDistrictName,
				electoralDistrictsById);

		contestSB.append(prefix).append(electionOffice);
		final String contestId = contestSB.toString();
		VipContest contest = contestsById.get(contestId);
		if (contest == null) {
			contest = getValet().acquireContest();
			contest.setOffice(electionOffice);
			contest.setType(electionStage);
			if ("PRIMARY".equalsIgnoreCase(electionStage)) {
				contest.setPrimaryParty(electionParties);
			}
			contest.setSpecial(specialFlag);
			contest.setElectoralDistrict(electoralDistrict);
			contestsById.put(contestId, contest);
			contest.setBallot(getValet().acquireBallot());
		}
		final VipBallot ballot = contest.getBallot();
		final VipCandidate candidate = extractCandidate(candidateNode, electionParties);
		ballot.addCandidate(candidate);
	}

	/**
	 * Parses the candidates from the input candidates list.
	 * 
	 * @author IanBrown
	 * @param candidatesList
	 *            the candidates list.
	 * @param contestsById
	 *            the contests by identifier.
	 * @param electoralDistrictsById
	 *            the electoral districts by identifier.
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private void parseCandidates(final String candidatesList, final Map<String, VipContest> contestsById,
			final Map<String, VipElectoralDistrict> electoralDistrictsById) {
		if (candidatesList == null) {
			return;
		}

		final XMLConfiguration xmlReader = new XMLConfiguration();
		xmlReader.setDelimiterParsingDisabled(true);
		final InputStream is = IOUtils.toInputStream(candidatesList);
		try {
			xmlReader.load(is);
			final String error = xmlReader.getString("errorMessage", "");
			if (error != null && !error.trim().isEmpty()) {
				final String candidatesString = buildCandidatesString(candidatesList);
				voteSmartLog.error("Error parsing candidates list, error = " + error);
				return;
			}

			final ConfigurationNode rootNode = xmlReader.getRootNode();
			if (rootNode.getChildrenCount() == 0) {
				return;
			}

			final List<?> candidateChildren = rootNode.getChildren("candidate");
			for (final Object candidateChild : candidateChildren) {
				final ConfigurationNode candidateNode = (ConfigurationNode) candidateChild;
				parseCandidate(candidateNode, contestsById, electoralDistrictsById);
			}
		} catch (final ConfigurationException e) {
			final String candidatesString = buildCandidatesString(candidatesList);
			voteSmartLog.error("Error parsing " + candidatesString, e);
		}
	}

	/**
	 * Retrieves the single node value with the specified name within the candidate node.
	 * 
	 * @author IanBrown
	 * @param candidateNode
	 *            the candidate node.
	 * @param nodeName
	 *            the name of the node.
	 * @return the value for the node..
	 * @since Sep 11, 2012
	 * @version Sep 11, 2012
	 */
	private String retrieveNodeValue(final ConfigurationNode candidateNode, final String nodeName) {
		final List<?> nodes = candidateNode.getChildren(nodeName);
		String nodeValue = "";
		if (nodes.size() == 1) {
			final ConfigurationNode nodeObject = (ConfigurationNode) nodes.get(0);
			nodeValue = (String) nodeObject.getValue();
		}
		return nodeValue;
	}

	/**
	 * Standardizes the input office string.
	 * 
	 * @author IanBrown
	 * @param office
	 *            the office.
	 * @return the standardized office.
	 * @since Sep 13, 2012
	 * @version Sep 20, 2012
	 */
	private String standardizeOffice(final String office) {
		final StandardContest standardOffice = STANDARD_OFFICES.get(office.toUpperCase());
		return standardOffice == null ? office : standardOffice.getOffice()[0];
	}

	/** {@inheritDoc} */
	@Override
    public VipElection findElection(String stateAbbreviation,
            String votingRegionName) {
	    // TODO Auto-generated method stub
	    return null;
    }
}
