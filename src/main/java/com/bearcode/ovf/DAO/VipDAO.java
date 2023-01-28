/**
 * 
 */
package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.vip.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * DAO for dealing with VIP data.
 * 
 * @author IanBrown
 * 
 * @since Jun 22, 2012
 * @version Oct 12, 2012
 */
@Repository
public class VipDAO extends BearcodeDAO {

	/**
	 * deletes the VIP ballots.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_BALLOTS = "DELETE FROM VipBallot";

	/**
	 * deletes the VIP candidates.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_CANDIDATES = "DELETE FROM VipCandidate";

	/**
	 * deletes the VIP contests.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_CONTESTS = "DELETE FROM VipContest";

	/**
	 * deletes the VIP detail addresses.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_DETAIL_ADDRESSES = "DELETE FROM VipDetailAddress";

	/**
	 * deletes the VIP elections.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul, 2012
	 */
	static final String DELETE_VIP_ELECTIONS = "DELETE FROM VipElection";

	/**
	 * deletes the VIP electoral districts.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_ELECTORAL_DISTRICTS = "DELETE FROM VipElectoralDistrict";

	/**
	 * deletes the VIP localities.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_LOCALITIES = "DELETE FROM VipLocality";

	/**
	 * deletes the VIP precincts.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_PRECINCTS = "DELETE FROM VipPrecinct";

	/**
	 * deletes the VIP referenda.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_REFERENDA = "DELETE FROM VipReferendum";

	/**
	 * deletes the VIP states.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_STATES = "DELETE FROM VipState";

	/**
	 * deletes the VIP street segments.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jul 17, 2012
	 */
	static final String DELETE_VIP_STREET_SEGMENTS = "DELETE FROM VipStreetSegment";

	/**
	 * find the cities matching the source, state abbreviation, and county name.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Oct 9, 2012
	 */
	static final String FIND_CITIES_BY_SOURCE_STATE_AND_COUNTY = //
	"SELECT DISTINCT da.city" //
			+ " FROM VipStreetSegment ss" //
			+ " INNER JOIN ss.nonHouseAddress da" //
			+ " INNER JOIN ss.precinct p" //
			+ " INNER JOIN p.locality l" //
			+ " WHERE ss.source = :source" //
			+ "   AND (da.state = :state OR l.state.name = :stateName)" //
			+ "   AND l.name = :votingRegion" //
			+ " ORDER BY da.city";

	/**
	 * finds the cities matching the source, state abbreviation, and ZIP code.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Aug 6, 2012
	 */
	static final String FIND_CITIES_BY_SOURCE_STATE_AND_ZIP = //
	"SELECT DISTINCT da.city" //
			+ " FROM VipStreetSegment ss" //
			+ " INNER JOIN ss.nonHouseAddress da" //
			+ " WHERE ss.source = ?" //
			+ "   AND da.state = ?" //
			+ "   AND da.zip = ?" //
			+ " ORDER BY da.city";

	/**
	 * finds the ZIP codes matching the source and state abbreviation.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Aug 6, 2012
	 */
	static final String FIND_ZIP_CODES = //
	"SELECT DISTINCT da.zip" //
			+ " FROM VipStreetSegment ss" //
			+ " INNER JOIN ss.nonHouseAddress da" //
			+ " WHERE ss.source=?" //
			+ "   AND da.state=?" //
			+ " ORDER BY da.zip";

	/**
	 * the state data access object.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	@Autowired
	private StateDAO stateDAO;

	/**
	 * map of the state names by their abbreviations.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private final Map<String, String> stateNames = new HashMap<String, String>();

	/**
	 * Clears all of the VIP data out of the database.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void clear() {
		final Session session = getSessionFactory().openSession();
		executeQuery(session, DELETE_VIP_BALLOTS);
		executeQuery(session, DELETE_VIP_CANDIDATES);
		executeQuery(session, DELETE_VIP_CONTESTS);
		executeQuery(session, DELETE_VIP_DETAIL_ADDRESSES);
		executeQuery(session, DELETE_VIP_ELECTIONS);
		executeQuery(session, DELETE_VIP_ELECTORAL_DISTRICTS);
		executeQuery(session, DELETE_VIP_LOCALITIES);
		executeQuery(session, DELETE_VIP_PRECINCTS);
		executeQuery(session, DELETE_VIP_REFERENDA);
		executeQuery(session, DELETE_VIP_STATES);
		executeQuery(session, DELETE_VIP_STREET_SEGMENTS);
	}

	/**
	 * Finds the ballot belonging to the specified data source and having the specified ballot VIP identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballotId
	 *            the ballot VIP identifier.
	 * @return the ballot.
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	public VipBallot findBallotBySourceAndVipId(final VipSource source, final long ballotId) {
		return findBySourceAndVipId(source, VipBallot.class, ballotId);
	}

	/**
	 * Finds the ballot response for the specified data source and ballot response identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballotResponseId
	 *            the ballot response identifier.
	 * @return the ballot response.
	 * @since Jun 28, 2012
	 * @version Aug 16, 2012
	 */
	public VipBallotResponse findBallotResponseBySourceAndVipId(final VipSource source, final long ballotResponseId) {
		return findBySourceAndVipId(source, VipBallotResponse.class, ballotResponseId);
	}

	/**
	 * Finds the candidate bio for the source and candidate VIP identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param candidateVipId
	 *            the candidate VIP identifier.
	 * @return the candidate bio.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	public VipCandidateBio findCandidateBioBySourceAndVipId(final VipSource source, final long candidateVipId) {
		final Criteria criteria = getSession().createCriteria(VipCandidateBio.class);
		criteria.createAlias("candidate", "c").add(Restrictions.eq("c.source", source))
				.add(Restrictions.eq("c.vipId", candidateVipId));
        criteria.setMaxResults(1);
		return (VipCandidateBio) criteria.uniqueResult();
	}

	/**
	 * Finds a VIP candidate for the specified data source and specified candidate identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param candidateId
	 *            the candidate identifier.
	 * @return the candidate.
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	public VipCandidate findCandidateBySourceAndVipId(final VipSource source, final long candidateId) {
		return findBySourceAndVipId(source, VipCandidate.class, candidateId);
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
	 *            the voting region name.
	 * @return the cities.
	 * @since Jul 30, 2012
	 * @version Oct 10, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<String> findCitiesBySourceStateAndVotingRegion(final VipSource source, final String stateAbbreviation,
			final String votingRegionName) {
		final int countyIdx = votingRegionName.indexOf("County");
		if (countyIdx == -1) {
			return new LinkedList<String>();
		}
		final String countyName = votingRegionName.substring(0, countyIdx).trim();
		String stateName = stateNames.get(stateAbbreviation);
		if (stateName == null) {
			final State state = getStateDAO().getByAbbreviation(stateAbbreviation);
			if (state == null) {
				// We'll simply ignore this value.
				stateName = "No such state";
			} else {
				stateName = state.getName();
				stateNames.put(stateAbbreviation, stateName);
			}
		}
		return getHibernateTemplate().findByNamedParam(FIND_CITIES_BY_SOURCE_STATE_AND_COUNTY,
				new String[] { "source", "state", "stateName", "votingRegion" },
				new Object[] { source, stateAbbreviation, stateName, countyName });
	}

	/**
	 * Finds the cities for the source, state abbreviation, and ZIP code.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param zip
	 *            the ZIP code.
	 * @return the cities.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<String> findCitiesBySourceStateAndZip(final VipSource source, final String stateAbbreviation, final String zip) {
		return getHibernateTemplate().find(FIND_CITIES_BY_SOURCE_STATE_AND_ZIP, source, stateAbbreviation, zip);
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
	@SuppressWarnings("unchecked")
	public List<VipContest> findContestsBySourceAndElection(final VipSource source, final VipElection election) {
		final Criteria criteria = getSession().createCriteria(VipContest.class);
		criteria.add(Restrictions.eq("source", source)).add(Restrictions.eq("election", election));
		return criteria.list();
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
	@SuppressWarnings("unchecked")
	public List<VipContest> findContestsForElectoralDistrict(final VipElectoralDistrict electoralDistrict) {
		final Criteria criteria = getSession().createCriteria(VipContest.class);
		criteria.add(Restrictions.eq("electoralDistrict", electoralDistrict));
		criteria.addOrder(Order.asc("ballotPlacement"));
		return criteria.list();
	}

	/**
	 * Finds the custom ballot for the specified data source and custom ballot identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param customBallotId
	 *            the custom ballot identifier.
	 * @return the custom ballot.
	 * @since Jun 28, 2012
	 * @version Aug 16, 2012
	 */
	public VipCustomBallot findCustomBallotBySourceAndVipId(final VipSource source, final long customBallotId) {
		return findBySourceAndVipId(source, VipCustomBallot.class, customBallotId);
	}

	/**
	 * Finds the election for the source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the election.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	public VipElection findElectionBySource(final VipSource source) {
		final Criteria criteria = getSession().createCriteria(VipElection.class);
		criteria.add(Restrictions.eq("source", source));
        criteria.setMaxResults(1);
		return (VipElection) criteria.uniqueResult();
	}

	/**
	 * Finds the electoral district belonging to the specified data source and having the specified electoral district identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param electoralDistrictId
	 *            the electoral district identifier.
	 * @return the electoral district.
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	public VipElectoralDistrict findElectoralDistrictBySourceAndVipId(final VipSource source, final long electoralDistrictId) {
		return findBySourceAndVipId(source, VipElectoralDistrict.class, electoralDistrictId);
	}

	/**
	 * Finds the latest source available.
	 * 
	 * @author IanBrown
	 * @return the latest source or <code>null</code>.
	 * @since Jun 27, 2012
	 * @version Jun 29, 2012
	 */
	public VipSource findLatestSource() {
		final Criteria criteria = getSession().createCriteria(VipSource.class);
		criteria.addOrder(Order.desc("dateTime")).add(Restrictions.eq("complete", true)).setMaxResults(1);
		return (VipSource) criteria.uniqueResult();
	}

	/**
	 * Finds the latest source for the states.
	 * 
	 * @author IanBrown
	 * @param states
	 *            the states to match.
	 * @return the latest source.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	public VipSource findLatestSource(final String... states) {
		final Criteria criteria = getSession().createCriteria(VipSource.class, "source");
		final DetachedCriteria stateCriteria = DetachedCriteria.forClass(VipState.class, "state");
		for (final String state : states) {
			stateCriteria.add(Property.forName("state.source.id").eqProperty(Property.forName("source.id"))).add(
					Restrictions.eq("name", state));
			criteria.add(Subqueries.exists(stateCriteria.setProjection(Projections.property("state.id"))));
		}
		criteria.addOrder(Order.desc("dateTime")).add(Restrictions.eq("complete", true)).setMaxResults(1);
		return (VipSource) criteria.uniqueResult();
	}

	/**
	 * Finds the localities of the specified type belonging to the state.
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
	@SuppressWarnings("unchecked")
	public List<VipLocality> findLocalitiesByStateAndType(final VipState state, final String type) {
		final Criteria criteria = getSession().createCriteria(VipLocality.class);
		criteria.add(Restrictions.eq("state", state)).add(Restrictions.eq("type", type).ignoreCase());
		return criteria.list();
	}

	/**
	 * Finds the locality belonging to the specified data source and having the specified locality identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param localityId
	 *            the locality identifier.
	 * @return the locality.
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	public VipLocality findLocalityBySourceAndVipId(final VipSource source, final long localityId) {
		return findBySourceAndVipId(source, VipLocality.class, localityId);
	}

	/**
	 * Finds the precinct for the specified data source and specified precinct identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param precinctId
	 *            the precinct identifier.
	 * @return the precinct.
	 * @since Jun 27, 2012
	 * @version Aug 16, 2012
	 */
	public VipPrecinct findPrecinctBySourceAndVipId(final VipSource source, final long precinctId) {
		return findBySourceAndVipId(source, VipPrecinct.class, precinctId);
	}

	/**
	 * Finds the precincts belonging to the source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the precincts.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<VipPrecinct> findPrecinctsBySource(final VipSource source) {
		final Criteria criteria = getSession().createCriteria(VipPrecinct.class);
		criteria.add(Restrictions.eq("source", source));
		return criteria.list();
	}

	/**
	 * Finds the precinct split for the specified data source and precinct split identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param precinctSplitId
	 *            the precinct split identifier.
	 * @return the precinct split.
	 * @since Jun 29, 2012
	 * @version Aug 16, 2012
	 */
	public VipPrecinctSplit findPrecinctSplitBySourceAndVipId(final VipSource source, final long precinctSplitId) {
		return findBySourceAndVipId(source, VipPrecinctSplit.class, precinctSplitId);
	}

	/**
	 * Finds the precinct splits belonging to the source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the precinct splits.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<VipPrecinctSplit> findPrecinctSplitsBySource(final VipSource source) {
		final Criteria criteria = getSession().createCriteria(VipPrecinctSplit.class);
		criteria.add(Restrictions.eq("source", source));
		return criteria.list();
	}

	/**
	 * Finds the referendum belonging to the specified data source and having the specified referndum identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param referendumId
	 *            the referendum identifier.
	 * @return the referendum.
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	public VipReferendum findReferendumBySourceAndVipId(final VipSource source, final long referendumId) {
		return findBySourceAndVipId(source, VipReferendum.class, referendumId);
	}

	/**
	 * Finds the referendum detail for the source and specified referendum VIP identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param referendumVipId
	 *            the referendum VIP identifier.
	 * @return the referendum detail.
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	public VipReferendumDetail findReferendumDetailBySourceAndVipId(final VipSource source, final long referendumVipId) {
		final Criteria criteria = getSession().createCriteria(VipReferendumDetail.class);
		criteria.createAlias("referendum", "r").add(Restrictions.eq("r.source", source))
				.add(Restrictions.eq("r.vipId", referendumVipId));
        criteria.setMaxResults(1);
		return (VipReferendumDetail) criteria.uniqueResult();
	}

	/**
	 * Finds the state for the specified data source and state name (abbreviation).
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param stateName
	 *            the name of the state.
	 * @return the state.
	 * @since Jun 27, 2012
	 * @version Jul 30, 2012
	 */
	public VipState findStateBySourceAndName(final VipSource source, final String stateName) {
		final Criteria criteria = getSession().createCriteria(VipState.class);
		criteria.add(Restrictions.eq("source", source)).add(Restrictions.eq("name", stateName));
        criteria.setMaxResults(1);
		return (VipState) criteria.uniqueResult();
	}

	/**
	 * Finds the state belonging to the specified data source and having the specified state identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param stateId
	 *            the state identifier.
	 * @return the state.
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	public VipState findStateBySourceAndVipId(final VipSource source, final long stateId) {
		return findBySourceAndVipId(source, VipState.class, stateId);
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
	@SuppressWarnings("unchecked")
	public List<VipState> findStatesBySource(final VipSource source) {
		final Criteria criteria = getSession().createCriteria(VipState.class);
		criteria.add(Restrictions.eq("source", source));
		return criteria.list();
	}

	/**
	 * Finds the street names for the city.
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
	@SuppressWarnings("unchecked")
	public List<String> findStreetNamesBySourceStateAndCity(final VipSource source, final String stateAbbreviation,
			final String city) {
		final Set<String> streetNames = new TreeSet<String>();
		final Criteria criteria = getSession().createCriteria(VipStreetSegment.class);
		criteria.add(Restrictions.eq("source", source)).createAlias("nonHouseAddress", "nha")
				.add(Restrictions.eq("nha.state", stateAbbreviation)).add(Restrictions.eq("nha.city", city));
		final List<VipStreetSegment> streetSegments = criteria.list();
		for (final VipStreetSegment streetSegment : streetSegments) {
			final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
			final String streetName = nonHouseAddress.toStreet();
			streetNames.add(streetName);
		}
		return new LinkedList<String>(streetNames);
	}

	/**
	 * Finds the street segment matching the VIP source and address.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the VIP source.
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
	 * @return the street segment or <code>null</code> if there is no match.
	 * @since Jul 5, 2012
	 * @version Oct 12, 2012
	 */
	public VipStreetSegment findStreetSegmentForAddress(final VipSource source, final String houseNumberPrefix,
			final int houseNumber, final String houseNumberSuffix, final String streetDirection, final String streetName,
			final String streetSuffix, final String addressDirection, final String city, final String state, final String zip) {
		final Criteria criteria = getSession().createCriteria(VipStreetSegment.class);
		criteria.add(Restrictions.eq("source", source)).add(Restrictions.le("startHouseNumber", houseNumber)).add(Restrictions.ge("endHouseNumber", houseNumber));
		if (houseNumber % 2 == 1) {
			criteria.add(Restrictions.or(Restrictions.ilike("oddEvenBoth", "O%"), Restrictions.ilike("oddEvenBoth", "B%")));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("oddEvenBoth", "E%"), Restrictions.ilike("oddEvenBoth", "B%")));
		}
		final Criteria nonHouseCriteria = criteria.createCriteria("nonHouseAddress");
		if (zip != null && !zip.trim().isEmpty()) {
			nonHouseCriteria.add(Restrictions.eq("zip", zip));
		}
		if (state != null && !state.trim().isEmpty()) {
			nonHouseCriteria.add(Restrictions.eq("state", state));
		}
		if (city != null && !city.trim().isEmpty()) {
			nonHouseCriteria.add(Restrictions.eq("city", city));
		}
		if (houseNumberPrefix != null) {
			nonHouseCriteria.add(Restrictions.ilike("houseNumberPrefix", houseNumberPrefix, MatchMode.EXACT));
		}
		if (houseNumberSuffix != null) {
			nonHouseCriteria.add(Restrictions.ilike("houseNumberSuffix", houseNumberSuffix, MatchMode.EXACT));
		}
		if (streetDirection != null) {
			nonHouseCriteria.add(Restrictions.ilike("streetDirection", streetDirection, MatchMode.EXACT));
		}
        if ( streetName != null ) {
            nonHouseCriteria.add(Restrictions.ilike("streetName", streetName, MatchMode.EXACT));
        }
        if ( streetSuffix != null ) {
            nonHouseCriteria.add( Restrictions.ilike("streetSuffix", streetSuffix, MatchMode.EXACT));
        }
        if (addressDirection != null) {
			nonHouseCriteria.add(Restrictions.ilike("addressDirection", addressDirection, MatchMode.EXACT));
		}
        criteria.setMaxResults(1);
		return (VipStreetSegment) criteria.uniqueResult();
	}

	/**
	 * Finds the street segments for the source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @return the street segments.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<VipStreetSegment> findStreetSegmentsBySource(final VipSource source) {
		final Criteria criteria = getSession().createCriteria(VipStreetSegment.class);
		criteria.add(Restrictions.eq("source", source));
		return criteria.list();
	}

	/**
	 * Finds the street segments for the specified source within the specified ZIP code.
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
	@SuppressWarnings("unchecked")
	public List<VipStreetSegment> findStreetSegmentsBySourceAndZip(final VipSource source, final String zipCode) {
		final Criteria criteria = getSession().createCriteria(VipStreetSegment.class);
		criteria.add(Restrictions.eq("source", source)).createAlias("nonHouseAddress", "nha")
				.add(Restrictions.eq("nha.zip", zipCode)).addOrder(Order.asc("nha.streetName"))
				.addOrder(Order.asc("nha.streetSuffix")).addOrder(Order.asc("nha.streetDirection"))
				.addOrder(Order.asc("nha.addressDirection")).addOrder(Order.asc("startHouseNumber"))
				.addOrder(Order.asc("oddEvenBoth"));
		return criteria.list();
	}

	/**
	 * Finds the ZIP codes for the source that belong to the specified state.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @return the ZIP codes.
	 * @since Jul 17, 2012
	 * @version Jul 30, 2012
	 */
	@SuppressWarnings("unchecked")
	public List<String> findZipCodesBySourceAndZip(final VipSource source, final String stateAbbreviation) {
		return getHibernateTemplate().find(FIND_ZIP_CODES, source, stateAbbreviation);
	}

	/**
	 * Gets the state DAO.
	 * 
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	public StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Sets the state DAO.
	 * 
	 * @author IanBrown
	 * @param stateDAO
	 *            the state DAO to set.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	public void setStateDAO(final StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

	/**
	 * Executes the specified query.
	 * 
	 * @author IanBrown
	 * @param session
	 *            the session.
	 * @param queryString
	 *            the query string.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private void executeQuery(final Session session, final String queryString) {
		final Query query = session.createQuery(queryString);
		query.executeUpdate();
	}

	/**
	 * Finds a VIP object by source and VIP identifier.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param vipClass
	 *            the class of the object to find.
	 * @param vipId
	 *            the VIP identifier.
	 * @return the object found or <code>null</code> if there is no match.
	 * @since Jun 27, 2012
	 * @version Aug 16, 2012
	 */
	@SuppressWarnings("unchecked")
	private <V extends AbstractVip> V findBySourceAndVipId(final VipSource source, final Class<V> vipClass, final long vipId) {
		final Criteria criteria = getSession().createCriteria(vipClass);
		criteria.add(Restrictions.eq("source", source)).add(Restrictions.eq("vipId", vipId));
        criteria.setMaxResults(1);
		return (V) criteria.uniqueResult();
	}
}
