/**
 * 
 */
package com.bearcode.ovf.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bearcode.ovf.DAO.VipDAO;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.AbstractVip;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipBallotCandidate;
import com.bearcode.ovf.model.vip.VipBallotResponse;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipCustomBallot;
import com.bearcode.ovf.model.vip.VipCustomBallotResponse;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendum;
import com.bearcode.ovf.model.vip.VipReferendumBallotResponse;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipSource;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.vip.xml.DetailAddressType;
import com.bearcode.ovf.tools.vip.xml.SimpleAddressType;
import com.bearcode.ovf.tools.vip.xml.VipObject;
import com.bearcode.ovf.tools.vip.xml.VipObject.Ballot;
import com.bearcode.ovf.tools.vip.xml.VipObject.Ballot.CandidateId;
import com.bearcode.ovf.tools.vip.xml.VipObject.BallotLineResult;
import com.bearcode.ovf.tools.vip.xml.VipObject.BallotResponse;
import com.bearcode.ovf.tools.vip.xml.VipObject.Candidate;
import com.bearcode.ovf.tools.vip.xml.VipObject.Contest;
import com.bearcode.ovf.tools.vip.xml.VipObject.ContestResult;
import com.bearcode.ovf.tools.vip.xml.VipObject.CustomBallot;
import com.bearcode.ovf.tools.vip.xml.VipObject.CustomBallot.BallotResponseId;
import com.bearcode.ovf.tools.vip.xml.VipObject.EarlyVoteSite;
import com.bearcode.ovf.tools.vip.xml.VipObject.Election;
import com.bearcode.ovf.tools.vip.xml.VipObject.ElectionAdministration;
import com.bearcode.ovf.tools.vip.xml.VipObject.ElectionOfficial;
import com.bearcode.ovf.tools.vip.xml.VipObject.ElectionRules;
import com.bearcode.ovf.tools.vip.xml.VipObject.ElectoralDistrict;
import com.bearcode.ovf.tools.vip.xml.VipObject.Locality;
import com.bearcode.ovf.tools.vip.xml.VipObject.PollingLocation;
import com.bearcode.ovf.tools.vip.xml.VipObject.Precinct;
import com.bearcode.ovf.tools.vip.xml.VipObject.PrecinctSplit;
import com.bearcode.ovf.tools.vip.xml.VipObject.Referendum;
import com.bearcode.ovf.tools.vip.xml.VipObject.Source;
import com.bearcode.ovf.tools.vip.xml.VipObject.State;
import com.bearcode.ovf.tools.vip.xml.VipObject.StreetSegment;
import com.bearcode.ovf.tools.vip.xml.VipObject.VoterIdentification;
import com.bearcode.ovf.tools.vip.xml.VipObject.VoterIdRules;

/**
 * Service for dealing with VIP data.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version May 6, 2013
 */
@Service
public class VipService {

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Jun 26, 2012
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(VipService.class);

	/**
	 * the VIP DAO.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@Autowired
	private VipDAO vipDAO;

	/**
	 * Clears out the VIP data from the database.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void clear() {
		getVipDAO().clear();
	}

	/**
	 * Converts the input VIP object into its database format.
	 * 
	 * @author IanBrown
	 * @param vipObject
	 *            the VIP object.
	 * @param lastModified
	 *            the last modified date for the source data - this may be different than the date in the data, which is generally
	 *            the election date.
	 * @since Jun 22, 2012
	 * @version May 6, 2013
	 */
	public void convert(final VipObject vipObject, final Date lastModified) {
		final List<Object> elements = vipObject.getSourceOrElectionOrState();
		if (elements == null || elements.isEmpty()) {
			throw new IllegalArgumentException("The VipObject must contain elements");
		}

		final Map<Long, AbstractVip> vipObjects = new HashMap<Long, AbstractVip>();
		final VipSource source = extractSource(elements);
		final Collection<VipState> states = extractStates(source, elements, vipObjects);
		final String[] stateNames = extractStateNames(states);
		final VipSource latestSource = findLatestSource(stateNames);
		final long latestSourceTime = latestSource == null || latestSource.getDateTime() == null ? 0l : latestSource.getDateTime().getTime() / 1000l;
		final long sourceTime = source.getDateTime().getTime() / 1000l;
		final long latestSourceModified = latestSource == null || latestSource.getLastModified() == null ? 0l : latestSource.getLastModified().getTime() / 1000l;
		final long sourceModified = lastModified.getTime() / 1000l;
		if (latestSource != null && latestSource.getName().equals(source.getName())
				&& latestSource.getSourceId().equals(source.getSourceId()) && latestSource.getVipId().equals(source.getVipId())
				&& (latestSourceTime > sourceTime || latestSourceTime == sourceTime && latestSourceModified >= sourceModified)) {
			// The source is either unchanged or older than the current one.
			return;
		}
		source.setLastModified(lastModified);
		getVipDAO().makePersistent(source);
		if (!states.isEmpty()) {
			getVipDAO().makeAllPersistent(states);
		}
		final VipElection election = extractElection(source, elements, vipObjects);

		// final Map<String, Integer> elementTypes = initiateElementsCount(elements);
		// int idx = 0;
		for (final Object element : elements) {
			// idx = addElementToCount(elementTypes, idx, element);

			if (element instanceof Source || element instanceof Election || element instanceof State) {
				continue;
			}

			if (element instanceof Ballot) {
				convertBallot(source, (Ballot) element, elements, vipObjects);

			} else if (element instanceof BallotResponse) {
				convertBallotResponse(source, (BallotResponse) element, vipObjects);

			} else if (element instanceof Candidate) {
				convertCandidate(source, (Candidate) element, vipObjects);

			} else if (element instanceof Contest) {
				convertContest(source, election, (Contest) element, vipObjects);

			} else if (element instanceof CustomBallot) {
				convertCustomBallot(source, (CustomBallot) element, vipObjects);

			} else if (element instanceof ElectoralDistrict) {
				convertElectoralDistrict(source, (ElectoralDistrict) element, vipObjects);

			} else if (element instanceof Locality) {
				convertLocality(source, (Locality) element, vipObjects);

			} else if (element instanceof Precinct) {
				convertPrecinct(source, (Precinct) element, vipObjects);

			} else if (element instanceof PrecinctSplit) {
				convertPrecinctSplit(source, (PrecinctSplit) element, vipObjects);

			} else if (element instanceof Referendum) {
				convertReferendum(source, (Referendum) element, vipObjects);

			} else if (element instanceof StreetSegment) {
				convertStreetSegment(source, (StreetSegment) element, vipObjects);

			} else if (element instanceof BallotLineResult || element instanceof ContestResult || element instanceof EarlyVoteSite
					|| element instanceof ElectionAdministration || element instanceof PollingLocation
					|| element instanceof ElectionOfficial
					|| element instanceof VoterIdentification
					|| element instanceof VoterIdRules
					|| element instanceof ElectionRules) {
				// TODO
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(element + " not yet supported");
				}

			} else {
				// TODO
				throw new UnsupportedOperationException(element + " not implemented yet");
			}
		}

		source.setComplete(true);
		getVipDAO().makePersistent(source);

		// terminateElementsCount(elementTypes);
	}

	/**
	 * Finds the candidate bio for the source and VIP identification of the candidate.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param candidateVipId
	 *            the VIP identifier for the candidate.
	 * @return the candidate bio.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	public VipCandidateBio findCandidateBioBySourceAndVipId(final VipSource source, final long candidateVipId) {
		return getVipDAO().findCandidateBioBySourceAndVipId(source, candidateVipId);
	}

	/**
	 * Finds the cities for the source, state abbreviation, and voting region name.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @return the cities.
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	public List<String> findCitiesBySourceStateAndVotingRegion(final VipSource source, final String stateAbbreviation,
			final String votingRegionName) {
		return getVipDAO().findCitiesBySourceStateAndVotingRegion(source, stateAbbreviation, votingRegionName);
	}

	/**
	 * Finds the cities for the source, state abbreviation, and ZIP code.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param zip
	 *            the ZIP code.
	 * @return the cities.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public List<String> findCitiesBySourceStateAndZip(final VipSource source, final String stateAbbreviation, final String zip) {
		return getVipDAO().findCitiesBySourceStateAndZip(source, stateAbbreviation, zip);
	}

	/**
	 * Finds the contests for the source and election.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param election
	 *            the election.
	 * @return the contests.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public List<VipContest> findContestsBySourceAndElection(final VipSource source, final VipElection election) {
		return getVipDAO().findContestsBySourceAndElection(source, election);
	}

	/**
	 * Finds the contests for the specified electoral district.
	 * 
	 * @author IanBrown
	 * @param electoralDistrict
	 *            the electoral district.
	 * @return the contests.
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	public List<VipContest> findContestsForElectoralDistrict(final VipElectoralDistrict electoralDistrict) {
		return getVipDAO().findContestsForElectoralDistrict(electoralDistrict);
	}

	/**
	 * Finds the election for the source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @return the election.
	 * @since Jun 25, 2012
	 * @version Jun 28, 2012
	 */
	public VipElection findElectionBySource(final VipSource source) {
		return getVipDAO().findElectionBySource(source);
	}

	/**
	 * Finds the latest source of data.
	 * 
	 * @author IanBrown
	 * @return the source of data.
	 * @since Jun 26, 2012
	 * @version Jun 27, 2012
	 */
	public VipSource findLatestSource() {
		return getVipDAO().findLatestSource();
	}

	/**
	 * Finds the latest source of data for a particular set of states.
	 * 
	 * @author IanBrown
	 * @param states
	 *            the states.
	 * @return the source of data.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	public VipSource findLatestSource(final String... states) {
		if (states == null || states.length == 0) {
			return getVipDAO().findLatestSource();
		}

		return getVipDAO().findLatestSource(states);
	}

	/**
	 * Finds the localities of the specified type for the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param type
	 *            the type of localities.
	 * @return the localities.
	 * @since Jul 27, 2012
	 * @version Jul 27, 2012
	 */
	public List<VipLocality> findLocalitiesByStateAndType(final VipState state, final String type) {
		return getVipDAO().findLocalitiesByStateAndType(state, type);
	}

	/**
	 * Finds the precincts for the specified source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the precincts.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public List<VipPrecinct> findPrecinctsBySource(final VipSource source) {
		return getVipDAO().findPrecinctsBySource(source);
	}

	/**
	 * Finds the precinct splits for the specified data source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of data.
	 * @return the precinct splits.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public List<VipPrecinctSplit> findPrecinctSplitsBySource(final VipSource source) {
		return getVipDAO().findPrecinctSplitsBySource(source);
	}

	/**
	 * Finds the referendum detail for the source and specified VIP identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param referendumVipId
	 *            the VIP identifier for the referendum.
	 * @return the referendum detail.
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	public VipReferendumDetail findReferendumDetailBySourceAndVipId(final VipSource source, final long referendumVipId) {
		return getVipDAO().findReferendumDetailBySourceAndVipId(source, referendumVipId);
	}

	/**
	 * Finds the state by source and name.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @param name
	 *            the name.
	 * @return the state.
	 * @since Jul 27, 2012
	 * @version Jul 27, 2012
	 */
	public VipState findStateBySourceAndName(final VipSource source, final String name) {
		return getVipDAO().findStateBySourceAndName(source, name);
	}

	/**
	 * Finds the states belonging to the specified source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @return the states.
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	public List<VipState> findStatesBySource(final VipSource source) {
		return getVipDAO().findStatesBySource(source);
	}

	/**
	 * Finds the names of the streets in the city.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param city
	 *            the name of the city.
	 * @return the street names.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public List<String> findStreetNamesBySourceStateAndCity(final VipSource source, final String stateAbbreviation,
			final String city) {
		return getVipDAO().findStreetNamesBySourceStateAndCity(source, stateAbbreviation, city);
	}

	/**
	 * Finds the street segment for the specified address.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data to retrieve.
	 * @param houseNumberPrefix
	 *            the prefix for the house number.
	 * @param houseNumber
	 *            the house number.
	 * @param houseNumberSuffix
	 *            the suffix for the house number.
	 * @param streetDirection
	 *            the direction of the street (may be <code>null</code>).
	 * @param streetName
	 *            the name of the street.
	 * @param streetSuffix
	 *            the suffix for the street.
	 * @param addressDirection
	 *            the direction of the addresses (may be <code>null</code>).
	 * @param city
	 *            the city (may be <code>null</code>).
	 * @param state
	 *            the state (may be <code>null</code>).
	 * @param zip
	 *            the ZIP code (may be <code>null</code>).
	 * @return the matching street segment or <code>null</code>.
	 * @since Jul 5, 2012
	 * @version Sep 17, 2012
	 */
	public VipStreetSegment findStreetSegmentForAddress(final VipSource source, final String houseNumberPrefix,
			final int houseNumber, final String houseNumberSuffix, final String streetDirection, final String streetName,
			final String streetSuffix, final String addressDirection, final String city, final String state, final String zip) {
		return getVipDAO().findStreetSegmentForAddress(source, null, houseNumber, houseNumberSuffix, streetDirection, streetName,
				streetSuffix, addressDirection, city, state, zip);
	}

	/**
	 * Finds the street segments for the specified data source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @return the street segments.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	public List<VipStreetSegment> findStreetSegmentsBySource(final VipSource source) {
		return getVipDAO().findStreetSegmentsBySource(source);
	}

	/**
	 * Finds the street segments belonging to the source within the specified ZIP code.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @param zipCode
	 *            the ZIP code.
	 * @return the street segments.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	public List<VipStreetSegment> findStreetSegmentsBySourceAndZip(final VipSource source, final String zipCode) {
		return getVipDAO().findStreetSegmentsBySourceAndZip(source, zipCode);
	}

	/**
	 * Finds the ZIP codes for the source belonging to the specified state.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @return the ZIP codes.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	public List<String> findZipCodesBySourceAndState(final VipSource source, final String stateAbbreviation) {
		return getVipDAO().findZipCodesBySourceAndZip(source, stateAbbreviation);
	}

	/**
	 * Gets the VIP DAO.
	 * 
	 * @author IanBrown
	 * @return the VIP DAO.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public VipDAO getVipDAO() {
		return vipDAO;
	}

	/**
	 * Sets the VIP DAO.
	 * 
	 * @author IanBrown
	 * @param vipDAO
	 *            the VIP DAO to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setVipDAO(final VipDAO vipDAO) {
		this.vipDAO = vipDAO;
	}

	/**
	 * Adds an element to the count of elements by type.
	 * 
	 * @author IanBrown
	 * @param elementTypes
	 *            the counts of elements by type.
	 * @param idx
	 *            the index of the previous element.
	 * @param element
	 *            the element.
	 * @return the index of the current element.
	 * @since Sep 20, 2012
	 * @version Sep 20, 2012
	 */
	private int addElementToCount(final Map<String, Integer> elementTypes, final int idx, final Object element) {
		final int currentIdx = idx + 1;
		final String className = element.getClass().getSimpleName();
		final Integer current = elementTypes.get(className);
		if (current == null) {
			elementTypes.put(className, 1);
		} else {
			elementTypes.put(className, current.intValue() + 1);
		}
		if (idx % 1000 == 0) {
			LOGGER.info(System.currentTimeMillis() + " Converting element #" + currentIdx + ":\n" + elementTypes);
		}
		return currentIdx;
	}

	/**
	 * Converts the input VIP ballot to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballot
	 *            the VIP ballot.
	 * @param elements the elements.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 25, 2012
	 * @version Oct 24, 2012
	 */
	private void convertBallot(final VipSource source, final Ballot ballot, List<Object> elements, final Map<Long, AbstractVip> vipObjects) {
		final long ballotId = ballot.getId().longValue();
		VipBallot vipBallot = (VipBallot) vipObjects.get(ballotId);
		if (vipBallot == null) {
			vipBallot = new VipBallot();
			vipBallot.setVipId(ballotId);
			vipBallot.setSource(source);
			getVipDAO().makePersistent(vipBallot);
			vipObjects.put(ballotId, vipBallot);
		}

		final BigInteger referendumIdBI = ballot.getReferendumId();
		if (referendumIdBI != null) {
			final long referendumId = referendumIdBI.longValue();
			VipReferendum vipReferendum = (VipReferendum) vipObjects.get(referendumId);
			if (vipReferendum == null) {
				vipReferendum = new VipReferendum();
				vipReferendum.setTitle("No title");
				vipReferendum.setVipId(referendumId);
				vipReferendum.setSource(source);
				getVipDAO().makePersistent(vipReferendum);
				vipObjects.put(referendumId, vipReferendum);
			}

			vipBallot.setReferendum(vipReferendum);
		}

		final BigInteger customBallotIdBI = ballot.getCustomBallotId();
		if (customBallotIdBI != null) {
			final long customBallotId = customBallotIdBI.longValue();
			VipCustomBallot vipCustomBallot = (VipCustomBallot) vipObjects.get(customBallotId);
			if (vipCustomBallot == null) {
				vipCustomBallot = new VipCustomBallot();
				vipCustomBallot.setVipId(customBallotId);
				vipCustomBallot.setSource(source);
				getVipDAO().makePersistent(vipCustomBallot);
				vipObjects.put(customBallotId, vipCustomBallot);
			}

			vipBallot.setCustomBallot(vipCustomBallot);
		}

		final List<CandidateId> candidateIds = ballot.getCandidateId();
		if (candidateIds != null && !candidateIds.isEmpty()) {
			convertCandidateIds(source, candidateIds, vipBallot, elements, vipObjects);
		}

		vipBallot.setWriteIn(convertYesNoEnum(ballot.getWriteIn()));

		getVipDAO().makePersistent(vipBallot);
	}

	/**
	 * Converts the VIP ballot response to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballotResponse
	 *            the ballot response.
	 * @param vipObjects
	 *            the map of VIP objects seen.
	 * @since Jun 28, 2012
	 * @version Sep 20, 2012
	 */
	private void convertBallotResponse(final VipSource source, final BallotResponse ballotResponse,
			final Map<Long, AbstractVip> vipObjects) {
		final long ballotResponseId = ballotResponse.getId().longValue();
		VipBallotResponse vipBallotResponse = (VipBallotResponse) vipObjects.get(ballotResponseId);
		if (vipBallotResponse == null) {
			vipBallotResponse = new VipBallotResponse();
			vipBallotResponse.setVipId(ballotResponseId);
			vipBallotResponse.setSource(source);
			vipObjects.put(ballotResponseId, vipBallotResponse);
		}
		vipBallotResponse.setText(ballotResponse.getText());
		getVipDAO().makePersistent(vipBallotResponse);
	}

	/**
	 * Converts the big integer value to an integer.
	 * 
	 * @author IanBrown
	 * @param bigInteger
	 *            the big integer.
	 * @return the integer.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private Integer convertBigInteger(final BigInteger bigInteger) {
		return bigInteger == null ? null : bigInteger.intValue();
	}

	/**
	 * Converts the input candidate to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param candidate
	 *            the candidate.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 27, 2012
	 * @version May 8, 2013
	 */
	private void convertCandidate(final VipSource source, final Candidate candidate, final Map<Long, AbstractVip> vipObjects) {
		final long candidateId = candidate.getId().longValue();
		VipCandidate vipCandidate = (VipCandidate) vipObjects.get(candidateId);
		if (vipCandidate == null) {
			vipCandidate = new VipCandidate();
			vipCandidate.setVipId(candidateId);
			vipCandidate.setSource(source);
			vipObjects.put(candidateId, vipCandidate);
		}

		vipCandidate.setName(candidate.getName());
		vipCandidate.setParty(candidate.getParty());
		vipCandidate.setIncumbent("YES".equalsIgnoreCase(candidate.getIncumbent()));
		getVipDAO().makePersistent(vipCandidate);

		final VipCandidateBio vipCandidateBio = new VipCandidateBio();
		vipCandidateBio.setCandidate(vipCandidate);
		vipCandidateBio.setBiography(candidate.getBiography());
		vipCandidateBio.setCandidateUrl(candidate.getCandidateUrl());
		vipCandidateBio.setEmail(candidate.getEmail());
		vipCandidateBio.setFiledMailingAddress(convertSimpleAddressType(candidate.getFiledMailingAddress()));
		vipCandidateBio.setPhone(candidate.getPhone());
		vipCandidateBio.setPhotoUrl(candidate.getPhotoUrl());
		getVipDAO().makePersistent(vipCandidateBio);
	}

	/**
	 * Converts the input candidate identifiers to ballot candidate references.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param candidateIds
	 *            the candidate identifiers.
	 * @param ballot
	 *            the VIP ballot being built.
	 * @param elements the elements.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 25, 2012
	 * @version Aug 29, 2012
	 */
	private void convertCandidateIds(final VipSource source, final List<CandidateId> candidateIds, final VipBallot ballot,
			List<Object> elements, final Map<Long, AbstractVip> vipObjects) {
		final LinkedList<VipBallotCandidate> candidates = new LinkedList<VipBallotCandidate>();
		ballot.setCandidates(candidates);
		for (final CandidateId candidateId : candidateIds) {
			final long vipCandidateId = candidateId.getValue().longValue();
			VipCandidate vipCandidate = (VipCandidate) vipObjects.get(vipCandidateId);
			if (vipCandidate == null) {
				vipCandidate = new VipCandidate();
				vipCandidate.setVipId(vipCandidateId);
				vipCandidate.setSource(source);
				getVipDAO().makePersistent(vipCandidate);
				vipObjects.put(vipCandidateId, vipCandidate);
			}

			final VipBallotCandidate vipBallotCandidate = new VipBallotCandidate();
			vipBallotCandidate.setBallot(ballot);
			vipBallotCandidate.setCandidate(vipCandidate);
			int sortOrder = ballot.getCandidates().size() + 1;
			final BigInteger ballotSortOrder = candidateId.getSortOrder();
			if (ballotSortOrder == null) {
				for (final Object element : elements) {
					if (element instanceof Candidate) {
						final Candidate candidate = (Candidate) element;
						if (candidate.getId().longValue() == vipCandidateId) {
							final BigInteger candidateSortOrder = candidate.getSortOrder();
							if (candidateSortOrder != null) {
								sortOrder = candidateSortOrder.intValue();
							}
							break;
						}
					}
				}
			} else {
				sortOrder = ballotSortOrder.intValue();
			}

			vipBallotCandidate.setSortOrder(sortOrder);
			candidates.add(vipBallotCandidate);
		}
		
		Collections.sort(candidates, new Comparator<VipBallotCandidate>() {

			@Override
			public int compare(VipBallotCandidate o1, VipBallotCandidate o2) {
				return o1.getSortOrder() - o2.getSortOrder();
			}
		});
	}

	/**
	 * Converts the VIP contest to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param election
	 *            the election.
	 * @param contest
	 *            the contest.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	private void convertContest(final VipSource source, final VipElection election, final Contest contest,
			final Map<Long, AbstractVip> vipObjects) {
		final long electionId = contest.getElectionId().longValue();
		if (electionId != election.getVipId()) {
			throw new IllegalStateException("The election identifier (" + contest.getElectionId().longValue()
					+ ") for the contest (" + contest.getId().longValue() + ") does not match the one for the election ("
					+ election.getVipId() + ")");
		}

		final long electoralDistrictId = contest.getElectoralDistrictId().longValue();
		VipElectoralDistrict vipElectoralDistrict = (VipElectoralDistrict) vipObjects.get(electoralDistrictId);
		if (vipElectoralDistrict == null) {
			vipElectoralDistrict = new VipElectoralDistrict();
			vipElectoralDistrict.setVipId(electoralDistrictId);
			vipElectoralDistrict.setSource(source);
			getVipDAO().makePersistent(vipElectoralDistrict);
			vipObjects.put(electoralDistrictId, vipElectoralDistrict);
		}

		final long ballotId = contest.getBallotId().longValue();
		VipBallot vipBallot = (VipBallot) vipObjects.get(ballotId);
		if (vipBallot == null) {
			vipBallot = new VipBallot();
			vipBallot.setVipId(ballotId);
			vipBallot.setSource(source);
			getVipDAO().makePersistent(vipBallot);
			vipObjects.put(ballotId, vipBallot);
		}

		final VipContest vipContest = new VipContest();
		vipContest.setVipId(contest.getId().longValue());
		vipContest.setSource(source);
		final BigInteger ballotPlacement = contest.getBallotPlacement();
		vipContest.setBallotPlacement(ballotPlacement == null ? null : ballotPlacement.intValue());
		vipContest.setBallot(vipBallot);
		vipContest.setElection(election);
		vipContest.setElectoralDistrict(vipElectoralDistrict);
		vipContest.setType(contest.getType());
		vipContest.setPartisan(convertYesNoEnum(contest.getPartisan()));
		vipContest.setPrimaryParty(contest.getPrimaryParty());
		vipContest.setSpecial(convertYesNoEnum(contest.getSpecial()));
		final String office = contest.getOffice();
		vipContest.setOffice(office);
		vipContest.setNumberElected(convertBigInteger(contest.getNumberElected()));
		final BigInteger numberVotingFor = contest.getNumberVotingFor();
		if (numberVotingFor == null && office != null && Pattern.matches(".+ (Elect \\d+)", office)) {
			final int startIdx = office.indexOf("(Elect ");
			final int endIdx = office.indexOf(")", startIdx);
			vipContest.setNumberVotingFor(Integer.parseInt(office.substring(startIdx + "(Elect ".length(), endIdx)));
		} else {
			vipContest.setNumberVotingFor(convertBigInteger(numberVotingFor));
		}
		getVipDAO().makePersistent(vipContest);
	}

	/**
	 * Converts the VIP custom ballot to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param customBallot
	 *            the custom ballot.
	 * @param vipObjects
	 *            the map of VIP objects seen.
	 * @since Jun 28, 2012
	 * @version Sep 20, 2012
	 */
	private void convertCustomBallot(final VipSource source, final CustomBallot customBallot,
			final Map<Long, AbstractVip> vipObjects) {
		final long customBallotId = customBallot.getId().longValue();
		VipCustomBallot vipCustomBallot = (VipCustomBallot) vipObjects.get(customBallotId);
		if (vipCustomBallot == null) {
			vipCustomBallot = new VipCustomBallot();
			vipCustomBallot.setVipId(customBallotId);
			vipCustomBallot.setSource(source);
			vipObjects.put(customBallotId, vipCustomBallot);
		}

		final List<Object> elements = customBallot.getHeadingOrBallotResponseId();
		final List<VipCustomBallotResponse> customBallotResponses = new LinkedList<VipCustomBallotResponse>();
		for (final Object element : elements) {
			if (element instanceof String) {
				vipCustomBallot.setHeading((String) element);
			} else {
				final BallotResponseId ballotResponseIdV = (BallotResponseId) element;
				final BigInteger sortOrderBI = ballotResponseIdV.getSortOrder();
				final int sortOrder = sortOrderBI == null ? customBallotResponses.size() + 1 : sortOrderBI.intValue();
				final long ballotResponseId = ballotResponseIdV.getValue().longValue();
				VipBallotResponse vipBallotResponse = (VipBallotResponse) vipObjects.get(ballotResponseId);
				if (vipBallotResponse == null) {
					vipBallotResponse = new VipBallotResponse();
					vipBallotResponse.setVipId(ballotResponseId);
					vipBallotResponse.setSource(source);
					getVipDAO().makePersistent(vipBallotResponse);
					vipObjects.put(ballotResponseId, vipBallotResponse);
				}
				final VipCustomBallotResponse vipCustomBallotResponse = new VipCustomBallotResponse();
				vipCustomBallotResponse.setSortOrder(sortOrder);
				vipCustomBallotResponse.setCustomBallot(vipCustomBallot);
				vipCustomBallotResponse.setBallotResponse(vipBallotResponse);
				customBallotResponses.add(vipCustomBallotResponse);
			}
		}
		vipCustomBallot.setBallotResponses(customBallotResponses);
		getVipDAO().makePersistent(vipCustomBallot);
	}

	/**
	 * Converts the input VIP detail address to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param detailAddress
	 *            the detail address.
	 * @return the database VIP detail address.
	 * @since Jun 27, 2012
	 * @version Aug 10, 2012
	 */
	private VipDetailAddress convertDetailAddress(final VipSource source, final DetailAddressType detailAddress) {
		final VipDetailAddress vipDetailAddress = new VipDetailAddress();
		vipDetailAddress.setAddressDirection(detailAddress.getAddressDirection());
		vipDetailAddress.setCity(detailAddress.getCity());
		vipDetailAddress.setHouseNumberPrefix(detailAddress.getHouseNumberPrefix());
		vipDetailAddress.setHouseNumberSuffix(detailAddress.getHouseNumberSuffix());
		vipDetailAddress.setState(detailAddress.getState());
		vipDetailAddress.setStreetDirection(detailAddress.getStreetDirection());
		vipDetailAddress.setStreetName(detailAddress.getStreetName());
		vipDetailAddress.setStreetSuffix(detailAddress.getStreetSuffix());
		vipDetailAddress.setZip(detailAddress.getZip());
		getVipDAO().makePersistent(vipDetailAddress);
		return vipDetailAddress;
	}

	/**
	 * Converts the input VIP electoral district to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param electoralDistrict
	 *            the VIP electoral district.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 26, 2012
	 * @version Aug 29, 2012
	 */
	private void convertElectoralDistrict(final VipSource source, final ElectoralDistrict electoralDistrict,
			final Map<Long, AbstractVip> vipObjects) {
		final long electoralDistrictId = electoralDistrict.getId().longValue();
		VipElectoralDistrict vipElectoralDistrict = (VipElectoralDistrict) vipObjects.get(electoralDistrictId);
		if (vipElectoralDistrict == null) {
			vipElectoralDistrict = new VipElectoralDistrict();
			vipElectoralDistrict.setVipId(electoralDistrictId);
			vipElectoralDistrict.setSource(source);
			vipObjects.put(electoralDistrictId, vipElectoralDistrict);
		}

		vipElectoralDistrict.setName(electoralDistrict.getName());
		final BigInteger number = electoralDistrict.getNumber();
		if (number != null) {
			vipElectoralDistrict.setNumber(number.intValue());
		}
		vipElectoralDistrict.setType(electoralDistrict.getType());
		getVipDAO().makePersistent(vipElectoralDistrict);
	}

	/**
	 * Converts the input VIP locality to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param locality
	 *            the locality.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 26, 2012
	 * @version Aug 29, 2012
	 */
	private void convertLocality(final VipSource source, final Locality locality, final Map<Long, AbstractVip> vipObjects) {
		final long stateId = locality.getStateId().longValue();
		final VipState vipState = (VipState) vipObjects.get(stateId);
		if (vipState == null) {
			throw new IllegalStateException("There should be a VIP state for " + stateId);
		}

		final long localityId = locality.getId().longValue();
		VipLocality vipLocality = (VipLocality) vipObjects.get(localityId);
		if (vipLocality == null) {
			vipLocality = new VipLocality();
			vipLocality.setVipId(localityId);
			vipLocality.setSource(source);
		}
		vipLocality.setState(vipState);
		vipLocality.setName(locality.getName());
		vipLocality.setType(locality.getType());
		getVipDAO().makePersistent(vipLocality);
		vipObjects.put(localityId, vipLocality);
	}

	/**
	 * Converts the input VIP precinct to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param precinct
	 *            the precinct.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 26, 2012
	 * @version Aug 29, 2012
	 */
	private void convertPrecinct(final VipSource source, final Precinct precinct, final Map<Long, AbstractVip> vipObjects) {
		final long precinctId = precinct.getId().longValue();
		VipPrecinct vipPrecinct = (VipPrecinct) vipObjects.get(precinctId);
		if (vipPrecinct == null) {
			vipPrecinct = new VipPrecinct();
			vipPrecinct.setVipId(precinctId);
			vipPrecinct.setSource(source);
			vipObjects.put(precinctId, vipPrecinct);
		}
		final long localityId = precinct.getLocalityId().longValue();
		final VipLocality vipLocality = (VipLocality) vipObjects.get(localityId);
		if (vipLocality == null) {
			throw new IllegalStateException("A locality should exist for VIP ID " + localityId);
		}

		vipPrecinct.setLocality(vipLocality);
		final List<BigInteger> electoralDistrictIds = precinct.getElectoralDistrictId();
		if (electoralDistrictIds != null && !electoralDistrictIds.isEmpty()) {
			final List<VipElectoralDistrict> vipElectoralDistricts = new LinkedList<VipElectoralDistrict>();
			for (final BigInteger electoralDistrictIdBI : electoralDistrictIds) {
				final long electoralDistrictId = electoralDistrictIdBI.longValue();
				VipElectoralDistrict vipElectoralDistrict = (VipElectoralDistrict) vipObjects.get(electoralDistrictId);
				if (vipElectoralDistrict == null) {
					vipElectoralDistrict = new VipElectoralDistrict();
					vipElectoralDistrict.setVipId(electoralDistrictId);
					vipElectoralDistrict.setSource(source);
					getVipDAO().makePersistent(vipElectoralDistrict);
					vipObjects.put(electoralDistrictId, vipElectoralDistrict);
				}
				vipElectoralDistricts.add(vipElectoralDistrict);
			}
			vipPrecinct.setElectoralDistricts(vipElectoralDistricts);
		}
		vipPrecinct.setName(precinct.getName());
		vipPrecinct.setNumber(precinct.getNumber());
		vipPrecinct.setWard(precinct.getWard());
		getVipDAO().makePersistent(vipPrecinct);
	}

	/**
	 * Converts the VIP precinct split to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param precinctSplit
	 *            the precinct split.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 29, 2012
	 * @version Aug 29, 2012
	 */
	private void convertPrecinctSplit(final VipSource source, final PrecinctSplit precinctSplit,
			final Map<Long, AbstractVip> vipObjects) {
		final long precinctSplitId = precinctSplit.getId().longValue();
		VipPrecinctSplit vipPrecinctSplit = (VipPrecinctSplit) vipObjects.get(precinctSplitId);
		if (vipPrecinctSplit == null) {
			vipPrecinctSplit = new VipPrecinctSplit();
			vipPrecinctSplit.setVipId(precinctSplitId);
			vipPrecinctSplit.setSource(source);
			vipObjects.put(precinctSplitId, vipPrecinctSplit);
		}
		final long precinctId = precinctSplit.getPrecinctId().longValue();
		VipPrecinct vipPrecinct = (VipPrecinct) vipObjects.get(precinctId);
		if (vipPrecinct == null) {
			vipPrecinct = new VipPrecinct();
			vipPrecinct.setVipId(precinctId);
			vipPrecinct.setSource(source);
			getVipDAO().makePersistent(vipPrecinct);
			vipObjects.put(precinctId, vipPrecinct);
		}
		vipPrecinctSplit.setPrecinct(vipPrecinct);
		vipPrecinctSplit.setName(precinctSplit.getName());
		final List<BigInteger> electoralDistrictIds = precinctSplit.getElectoralDistrictId();
		final List<VipElectoralDistrict> vipElectoralDistricts = new LinkedList<VipElectoralDistrict>();
		for (final BigInteger electoralDistrictIdBI : electoralDistrictIds) {
			final long electoralDistrictId = electoralDistrictIdBI.longValue();
			VipElectoralDistrict vipElectoralDistrict = (VipElectoralDistrict) vipObjects.get(electoralDistrictId);
			if (vipElectoralDistrict == null) {
				vipElectoralDistrict = new VipElectoralDistrict();
				vipElectoralDistrict.setVipId(electoralDistrictId);
				vipElectoralDistrict.setSource(source);
				getVipDAO().makePersistent(vipElectoralDistrict);
				vipObjects.put(electoralDistrictId, vipElectoralDistrict);
			}
			vipElectoralDistricts.add(vipElectoralDistrict);
		}
		vipPrecinctSplit.setElectoralDistricts(vipElectoralDistricts);
		getVipDAO().makePersistent(vipPrecinctSplit);
	}

	/**
	 * Converts the input VIP referendum to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param referendum
	 *            the referendum.
	 * @param vipObjects
	 *            the map of VIP objects seen.
	 * @since Jun 27, 2012
	 * @version Oct 24, 2012
	 */
	private void convertReferendum(final VipSource source, final Referendum referendum, final Map<Long, AbstractVip> vipObjects) {
		final long referendumId = referendum.getId().longValue();
		VipReferendum vipReferendum = (VipReferendum) vipObjects.get(referendumId);
		if (vipReferendum == null) {
			vipReferendum = new VipReferendum();
			vipReferendum.setTitle("No title");
			vipReferendum.setSource(source);
			vipReferendum.setVipId(referendumId);
			getVipDAO().makePersistent(vipReferendum);
			vipObjects.put(referendumId, vipReferendum);
		}

		final VipReferendumDetail vipReferendumDetail = new VipReferendumDetail();
		vipReferendumDetail.setReferendum(vipReferendum);
		final List<JAXBElement<?>> elements = referendum.getTitleOrSubtitleOrBrief();
		if (elements != null && !elements.isEmpty()) {
			final List<VipReferendumBallotResponse> referendumBallotResponses = new LinkedList<VipReferendumBallotResponse>();
			for (final JAXBElement<?> element : elements) {
				final QName elementName = element.getName();
				final String localPart = elementName.getLocalPart();
				final String value = element.getValue() instanceof String ? (String) element.getValue() : null;
				if ("brief".equalsIgnoreCase(localPart)) {
					vipReferendum.setBrief(value);
				} else if ("subtitle".equalsIgnoreCase(element.getName().getLocalPart())) {
					vipReferendum.setSubTitle(value);
				} else if ("text".equalsIgnoreCase(element.getName().getLocalPart())) {
					vipReferendum.setText(value);
				} else if ("title".equalsIgnoreCase(element.getName().getLocalPart())) {
					vipReferendum.setTitle(value);
				} else if ("ballot_response_id".equals(element.getName().getLocalPart())) {
					final Referendum.BallotResponseId ballotResponseIdV = (Referendum.BallotResponseId) element.getValue();
					final BigInteger sortOrderBI = ballotResponseIdV.getSortOrder();
					final int sortOrder = sortOrderBI == null ? referendumBallotResponses.size() + 1 : sortOrderBI.intValue();
					final long ballotResponseId = ballotResponseIdV.getValue().longValue();
					VipBallotResponse vipBallotResponse = (VipBallotResponse) vipObjects.get(ballotResponseId);
					if (vipBallotResponse == null) {
						vipBallotResponse = new VipBallotResponse();
						vipBallotResponse.setVipId(ballotResponseId);
						vipBallotResponse.setSource(source);
						getVipDAO().makePersistent(vipBallotResponse);
						vipObjects.put(ballotResponseId, vipBallotResponse);
					}
					final VipReferendumBallotResponse vipReferendumBallotResponse = new VipReferendumBallotResponse();
					vipReferendumBallotResponse.setSortOrder(sortOrder);
					vipReferendumBallotResponse.setReferendum(vipReferendum);
					vipReferendumBallotResponse.setBallotResponse(vipBallotResponse);
					referendumBallotResponses.add(vipReferendumBallotResponse);
				} else if ("con_statement".equalsIgnoreCase(localPart)) {
					vipReferendumDetail.setConStatement(value);
				} else if ("pro_statement".equalsIgnoreCase(localPart)) {
					vipReferendumDetail.setProStatement(value);
				} else if ("passage_threshold".equalsIgnoreCase(localPart)) {
					vipReferendumDetail.setPassageThreshold(value);
				} else if ("effect_of_abstain".equalsIgnoreCase(localPart)) {
					vipReferendumDetail.setEffectOfAbstain(value);
				}
			}
			vipReferendum.setBallotResponses(referendumBallotResponses);
		}

		getVipDAO().makePersistent(vipReferendum);
		getVipDAO().makePersistent(vipReferendumDetail);
	}

	/**
	 * Converts a simple address type to a user address.
	 * 
	 * @author IanBrown
	 * @param simpleAddress
	 *            the simple address type.
	 * @return the user address.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	private UserAddress convertSimpleAddressType(final SimpleAddressType simpleAddress) {
		if (simpleAddress == null) {
			return null;
		}

		final UserAddress userAddress = new UserAddress();
		userAddress.setStreet1(simpleAddress.getLine1());
		userAddress.setStreet2(simpleAddress.getLine2());
		userAddress.setCity(simpleAddress.getCity());
		userAddress.setState(simpleAddress.getState());
		userAddress.setZip(simpleAddress.getZip());
		return userAddress;
	}

	/**
	 * Converts the input VIP state to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param state
	 *            the state.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @param states
	 *            the collection of states.
	 * @since Jun 26, 2012
	 * @version Sep 19, 2012
	 */
	private void convertState(final VipSource source, final State state, final Map<Long, AbstractVip> vipObjects,
			final Collection<VipState> states) {
		final long stateId = state.getId().longValue();
		VipState vipState = (VipState) vipObjects.get(stateId);
		if (vipState == null) {
			vipState = new VipState();
			vipState.setVipId(stateId);
			vipState.setSource(source);
			vipObjects.put(stateId, vipState);
		}

		String stateName = state.getName();
		if (stateName.toUpperCase().startsWith("STATE OF ")) {
			stateName = stateName.substring("STATE OF ".length());
		}
		vipState.setName(stateName);
		states.add(vipState);
	}

	/**
	 * Converts the input VIP street segment to its database format.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param streetSegment
	 *            the street segment.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @since Jun 27, 2012
	 * @version Aug 29, 2012
	 */
	private void convertStreetSegment(final VipSource source, final StreetSegment streetSegment,
			final Map<Long, AbstractVip> vipObjects) {
		final VipStreetSegment vipStreetSegment = new VipStreetSegment();
		vipStreetSegment.setVipId(streetSegment.getId().longValue());
		vipStreetSegment.setSource(source);
		vipStreetSegment.setEndHouseNumber(streetSegment.getEndHouseNumber().intValue());
		vipStreetSegment.setNonHouseAddress(convertDetailAddress(source, streetSegment.getNonHouseAddress()));
		vipStreetSegment.setOddEvenBoth(streetSegment.getOddEvenBoth());
		final long precinctId = streetSegment.getPrecinctId().longValue();
		VipPrecinct vipPrecinct = (VipPrecinct) vipObjects.get(precinctId);
		if (vipPrecinct == null) {
			vipPrecinct = new VipPrecinct();
			vipPrecinct.setVipId(precinctId);
			vipPrecinct.setSource(source);
			getVipDAO().makePersistent(vipPrecinct);
			vipObjects.put(precinctId, vipPrecinct);
		}
		vipStreetSegment.setPrecinct(vipPrecinct);
		final BigInteger precinctSplitIdBI = streetSegment.getPrecinctSplitId();
		if (precinctSplitIdBI != null) {
			final long precinctSplitId = precinctSplitIdBI.longValue();
			VipPrecinctSplit vipPrecinctSplit = (VipPrecinctSplit) vipObjects.get(precinctSplitId);
			if (vipPrecinctSplit == null) {
				vipPrecinctSplit = new VipPrecinctSplit();
				vipPrecinctSplit.setVipId(precinctSplitId);
				vipPrecinctSplit.setSource(source);
				getVipDAO().makePersistent(vipPrecinctSplit);
				vipObjects.put(precinctSplitId, vipPrecinctSplit);
			}
			vipStreetSegment.setPrecinctSplit(vipPrecinctSplit);
		}
		vipStreetSegment.setStartHouseNumber(streetSegment.getStartHouseNumber().intValue());
		getVipDAO().makePersistent(vipStreetSegment);
	}

	/**
	 * Converts an XML YesNoEnum object to a boolean.
	 * 
	 * @author IanBrown
	 * @param yesNoEnum
	 *            the XML YesNoEnum object - a string value.
	 * @return <code>true</code> if the input was "yes", <code>false</code> otherwise.
	 * @since Aug 10, 2012
	 * @version Aug 10, 2012
	 */
	private boolean convertYesNoEnum(final String yesNoEnum) {
		return "yes".equalsIgnoreCase(yesNoEnum);
	}

	/**
	 * Extracts the election from the elements.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the VIP source.
	 * @param elements
	 *            the elements.
	 * @param vipObjects
	 *            the VIP objects seen.
	 * @return the election in database format.
	 * @since Jun 26, 2012
	 * @version Aug 29, 2012
	 */
	private VipElection extractElection(final VipSource source, final List<Object> elements, final Map<Long, AbstractVip> vipObjects) {
		Election election = null;
		for (final Object element : elements) {
			if (element instanceof Election) {
				election = (Election) element;
				break;
			}
		}

		if (election == null) {
			throw new IllegalArgumentException("The VipObject must contain an election");
		}

		final VipElection vipElection = new VipElection();
		vipElection.setVipId(election.getId().longValue());
		vipElection.setSource(source);
		vipElection.setDate(election.getDate().toGregorianCalendar().getTime());
		final long stateId = election.getStateId().longValue();
		VipState vipState = (VipState) vipObjects.get(stateId);
		if (vipState == null) {
			vipState = new VipState();
			vipState.setVipId(stateId);
			vipState.setSource(source);
			getVipDAO().makePersistent(vipState);
			vipObjects.put(stateId, vipState);
		}
		vipElection.setState(vipState);
		vipElection.setType(election.getElectionType());
		getVipDAO().makePersistent(vipElection);
		return vipElection;
	}

	/**
	 * Extracts the source from the elements.
	 * 
	 * @author IanBrown
	 * @param elements
	 *            the elements.
	 * @return the source in database format.
	 * @since Jun 26, 2012
	 * @version Aug 21, 2012
	 */
	private VipSource extractSource(final List<Object> elements) {
		Source source = null;
		for (final Object element : elements) {
			if (element instanceof Source) {
				source = (Source) element;
				break;
			}
		}

		if (source == null) {
			throw new IllegalArgumentException("The VipObject must contain a source for the data");
		}

		final VipSource vipSource = new VipSource();
		vipSource.setVipId(source.getVipId().longValue());
		vipSource.setSourceId(source.getId().longValue());
		vipSource.setName(source.getName());
		vipSource.setDateTime(source.getDatetime().toGregorianCalendar().getTime());
		return vipSource;
	}

	/**
	 * Extracts the names of the states.
	 * 
	 * @author IanBrown
	 * @param states
	 *            the states.
	 * @return the names of the states.
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private String[] extractStateNames(final Collection<VipState> states) {
		final String[] stateNames = new String[states.size()];
		int idx = 0;
		for (final VipState state : states) {
			stateNames[idx] = state.getName();
			++idx;
		}
		return stateNames;
	}

	/**
	 * Extracts the states from the elements.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @param elements
	 *            the elements.
	 * @param vipObjects
	 *            the map of VIP objects by VIP identifier.
	 * @return the states.
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	private Collection<VipState> extractStates(final VipSource source, final List<Object> elements,
			final Map<Long, AbstractVip> vipObjects) {
		final Collection<VipState> states = new LinkedList<VipState>();
		for (final Object element : elements) {
			if (element instanceof State) {
				convertState(source, (State) element, vipObjects, states);
			}
		}
		return states;
	}

	/**
	 * Initiates the counting of elements by type.
	 * 
	 * @author IanBrown
	 * @param elements
	 *            the elements to count.
	 * @return the counts of elements by type.
	 * @since Sep 20, 2012
	 * @version Sep 20, 2012
	 */
	private Map<String, Integer> initiateElementsCount(final List<Object> elements) {
		LOGGER.info(System.currentTimeMillis() + " Converting " + elements.size() + " elements");
		final Map<String, Integer> elementTypes = new java.util.TreeMap<String, Integer>();
		return elementTypes;
	}

	/**
	 * Finishes up counting the elements seen.
	 * 
	 * @author IanBrown
	 * @param elementTypes
	 *            the counts for the types of elements.
	 * @since Sep 20, 2012
	 * @version Sep 20, 2012
	 */
	private void terminateElementsCount(final Map<String, Integer> elementTypes) {
		LOGGER.info(System.currentTimeMillis() + " Converted all elements. Breakdown: " + elementTypes);
	}
}
