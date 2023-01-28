/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.vip.AbstractVip;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipBallotResponse;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipCustomBallot;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendum;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipSource;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link VipDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jun 25, 2012
 * @version Oct 11, 2012
 */
public final class VipDAOTest extends BearcodeDAOCheck<VipDAO> {

	/**
	 * the state DAO.
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private StateDAO stateDAO;
	
	/**
	 * Gets the state DAO.
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Sets the state DAO.
	 * @author IanBrown
	 * @param stateDAO the state DAO to set.
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private void setStateDAO(StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#clear()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@Test
	public final void testClear() {
		final Session session = createMock("Session", Session.class);
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
		EasyMock.expect(getPersistentResource().openSession()).andReturn(session);
		setUpQuery(session, "DeleteVIPBallots", VipDAO.DELETE_VIP_BALLOTS);
		setUpQuery(session, "DeleteVIPCandidates", VipDAO.DELETE_VIP_CANDIDATES);
		setUpQuery(session, "DeleteVIPContests", VipDAO.DELETE_VIP_CONTESTS);
		setUpQuery(session, "DeleteVIPDetailAddresses", VipDAO.DELETE_VIP_DETAIL_ADDRESSES);
		setUpQuery(session, "DeleteVIPElections", VipDAO.DELETE_VIP_ELECTIONS);
		setUpQuery(session, "DeleteVIPElectoralDistricts", VipDAO.DELETE_VIP_ELECTORAL_DISTRICTS);
		setUpQuery(session, "DeleteVIPLocalities", VipDAO.DELETE_VIP_LOCALITIES);
		setUpQuery(session, "DeleteVIPPrecincts", VipDAO.DELETE_VIP_PRECINCTS);
		setUpQuery(session, "DeleteVIPReferenda", VipDAO.DELETE_VIP_REFERENDA);
		setUpQuery(session, "DeleteVIPStates", VipDAO.DELETE_VIP_STATES);
		setUpQuery(session, "DeleteVIPStreetSegments", VipDAO.DELETE_VIP_STREET_SEGMENTS);
		replayAll();

		getBearcodeDAO().clear();

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findBallotBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindBallotBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 8267l;
		final VipBallot ballot = createMock("Ballot", VipBallot.class);
		setUpFindBySourceAndId(VipBallot.class, ballot);
		replayAll();

		final VipBallot actualBallot = getBearcodeDAO().findBallotBySourceAndVipId(source, id);

		assertSame("The ballot is returned", ballot, actualBallot);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findBallotResponseBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindBallotResponseBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 8267l;
		final VipBallotResponse ballotResponse = createMock("Ballot", VipBallotResponse.class);
		setUpFindBySourceAndId(VipBallotResponse.class, ballotResponse);
		replayAll();

		final VipBallotResponse actualBallotResponse = getBearcodeDAO().findBallotResponseBySourceAndVipId(source, id);

		assertSame("The ballot response is returned", ballotResponse, actualBallotResponse);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findCandidateBioBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindCandidateBioBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long candidateVipId = 8728l;
		final VipCandidateBio candidateBio = createMock("CandidateBio", VipCandidateBio.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipCandidateBio.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.createAlias("candidate", "c")).andReturn(criteria);
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.uniqueResult()).andReturn(candidateBio);
		replayAll();

		final VipCandidateBio actualCandidateBio = getBearcodeDAO().findCandidateBioBySourceAndVipId(source, candidateVipId);

		assertSame("The candidate bio is returned", candidateBio, actualCandidateBio);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findCandidateBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindCandidateBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 12879l;
		final VipCandidate candidate = createMock("Candidate", VipCandidate.class);
		setUpFindBySourceAndId(VipCandidate.class, candidate);
		replayAll();

		final VipCandidate actualCandidate = getBearcodeDAO().findCandidateBySourceAndVipId(source, id);

		assertSame("The candidate is returned", candidate, actualCandidate);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findCitiesBySourceStateAndVotingRegion(VipSource, String, String)} for a
	 * county voting region.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	public final void testFindCitiesBySourceStateAndVotingRegion_county() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String countyName = "Name";
		final String votingRegionName = countyName + " County";
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(stateAbbreviation)).andReturn(state);
		final String stateName = "State Name";
		EasyMock.expect(state.getName()).andReturn(stateName);
		EasyMock.expect(
				getHibernateTemplate().findByNamedParam(EasyMock.eq(VipDAO.FIND_CITIES_BY_SOURCE_STATE_AND_COUNTY), EasyMock.aryEq(new String[] { "source", "state", "stateName", "votingRegion" }), EasyMock.aryEq(new Object[] { source, stateAbbreviation, stateName, countyName })))
				.andReturn(cities);
		replayAll();

		final List<String> actualCities = getBearcodeDAO().findCitiesBySourceStateAndVotingRegion(source, stateAbbreviation,
				votingRegionName);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findCitiesBySourceStateAndZip(VipSource, String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	public final void testFindCitiesBySourceStateAndZip() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String zip = "19375";
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(getHibernateTemplate().find(VipDAO.FIND_CITIES_BY_SOURCE_STATE_AND_ZIP, source, stateAbbreviation, zip))
				.andReturn(cities);
		replayAll();

		final List<String> actualCities = getBearcodeDAO().findCitiesBySourceStateAndZip(source, stateAbbreviation, zip);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findContestsBySourceAndElection(VipSource, VipElection)}.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	@Test
	public final void testFindContestsBySourceAndElection() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipElection election = createMock("Election", VipElection.class);
		final VipContest contest = createMock("Contest", VipContest.class);
		final List<VipContest> contests = Arrays.asList(contest);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipContest.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(contests);
		replayAll();

		final List<VipContest> actualContests = getBearcodeDAO().findContestsBySourceAndElection(source, election);

		assertSame("The contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findContestsForElectoralDistrict(VipElectoralDistrict)}.
	 * 
	 * @author IanBrown
	 * @since Jul 9, 2012
	 * @version Oct 11, 2012
	 */
	@Test
	public final void testFindContestsForElectoralDistrict() {
		final VipElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", VipElectoralDistrict.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipContest.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		final VipContest contest = createMock("Contest", VipContest.class);
		final List<VipContest> contests = Arrays.asList(contest);
		EasyMock.expect(criteria.list()).andReturn(contests);
		replayAll();

		final List<VipContest> actualContests = getBearcodeDAO().findContestsForElectoralDistrict(electoralDistrict);

		assertSame("The contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findCustomBallotBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindCustomBallotBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 8267l;
		final VipCustomBallot customBallot = createMock("Ballot", VipCustomBallot.class);
		setUpFindBySourceAndId(VipCustomBallot.class, customBallot);
		replayAll();

		final VipCustomBallot actualCustomBallot = getBearcodeDAO().findCustomBallotBySourceAndVipId(source, id);

		assertSame("The custom ballot is returned", customBallot, actualCustomBallot);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findElectionBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	@Test
	public final void testFindElectionBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipElection election = createMock("Election", VipElection.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipElection.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.uniqueResult()).andReturn(election);
		replayAll();

		final VipElection actualElection = getBearcodeDAO().findElectionBySource(source);

		assertSame("The election is returned", election, actualElection);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findElectoralDistrictBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindElectoralDistrictBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 2425l;
		final VipElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", VipElectoralDistrict.class);
		setUpFindBySourceAndId(VipElectoralDistrict.class, electoralDistrict);
		replayAll();

		final VipElectoralDistrict actualElectoralDistrict = getBearcodeDAO().findElectoralDistrictBySourceAndVipId(source, id);

		assertSame("The electoral district is returned", electoralDistrict, actualElectoralDistrict);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findLatestSource()}.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Jun 29, 2012
	 */
	@Test
	public final void testFindLatestSource() {
		final VipSource latestSource = createMock("LatestSource", VipSource.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipSource.class);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(latestSource);
		replayAll();

		final VipSource actualLatestSource = getBearcodeDAO().findLatestSource();

		assertSame("The latest source is returned", latestSource, actualLatestSource);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findLatestSource(String...)}.
	 * 
	 * @author IanBrown
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	@Test
	public final void testFindLatestSourceStrings() {
		final String state1 = "S1";
		final String state2 = "S2";
		final VipSource latestSource = createMock("LatestSource", VipSource.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipSource.class, "source");
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(latestSource);
		replayAll();

		final VipSource actualLatestSource = getBearcodeDAO().findLatestSource(state1, state2);

		assertSame("The latest source is returned", latestSource, actualLatestSource);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findLocalitiesByStateAndType(VipState, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 27, 2012
	 * @version Jul 27, 2012
	 */
	@Test
	public final void testFindLocalitiesByStateAndType() {
		final VipState state = createMock("State", VipState.class);
		final String type = "type";
		final VipLocality locality = createMock("Locality", VipLocality.class);
		final List<VipLocality> localities = Arrays.asList(locality);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipLocality.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(localities);
		replayAll();

		final List<VipLocality> actualLocalities = getBearcodeDAO().findLocalitiesByStateAndType(state, type);

		assertSame("The localities are returned", localities, actualLocalities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findLocalityBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindLocalityBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 6152l;
		final VipLocality locality = createMock("Locality", VipLocality.class);
		setUpFindBySourceAndId(VipLocality.class, locality);
		replayAll();

		final VipLocality actualLocality = getBearcodeDAO().findLocalityBySourceAndVipId(source, id);

		assertSame("The locality is returned", locality, actualLocality);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findPrecinctBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindPrecinctBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 8267l;
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		setUpFindBySourceAndId(VipPrecinct.class, precinct);
		replayAll();

		final VipPrecinct actualPrecinct = getBearcodeDAO().findPrecinctBySourceAndVipId(source, id);

		assertSame("The precinct is returned", precinct, actualPrecinct);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findPrecinctsBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	@Test
	public final void testFindPrecinctsBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipPrecinct precinct = createMock("Precinct", VipPrecinct.class);
		final List<VipPrecinct> precincts = Arrays.asList(precinct);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipPrecinct.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(precincts);
		replayAll();

		final List<VipPrecinct> actualPrecincts = getBearcodeDAO().findPrecinctsBySource(source);

		assertSame("The precincts are returned", precincts, actualPrecincts);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findPrecinctSplitsBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	@Test
	public final void testFindPrecinctSplitsBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipPrecinctSplit precinctSplit = createMock("PrecinctSplit", VipPrecinctSplit.class);
		final List<VipPrecinctSplit> precinctSplits = Arrays.asList(precinctSplit);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipPrecinctSplit.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(precinctSplits);
		replayAll();

		final List<VipPrecinctSplit> actualPrecinctSplits = getBearcodeDAO().findPrecinctSplitsBySource(source);

		assertSame("The precincts are returned", precinctSplits, actualPrecinctSplits);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findPrecinctSplitsBySourceAndId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindPrecinctSplitsBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 8267l;
		final VipPrecinctSplit precinctSplit = createMock("PrecinctSplit", VipPrecinctSplit.class);
		setUpFindBySourceAndId(VipPrecinctSplit.class, precinctSplit);
		replayAll();

		final VipPrecinctSplit actualPrecinctSplit = getBearcodeDAO().findPrecinctSplitBySourceAndVipId(source, id);

		assertSame("The precinct split is returned", precinctSplit, actualPrecinctSplit);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findReferendumBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindReferendumBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 12879l;
		final VipReferendum referendum = createMock("Referendum", VipReferendum.class);
		setUpFindBySourceAndId(VipReferendum.class, referendum);
		replayAll();

		final VipReferendum actualReferendum = getBearcodeDAO().findReferendumBySourceAndVipId(source, id);

		assertSame("The referendum is returned", referendum, actualReferendum);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findReferendumDetailBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	@Test
	public final void testFindReferendumDetailBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long referendumVipId = 8728l;
		final VipReferendumDetail referendumDetail = createMock("ReferendumDetail", VipReferendumDetail.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipReferendumDetail.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.createAlias("referendum", "r")).andReturn(criteria);
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.uniqueResult()).andReturn(referendumDetail);
		replayAll();

		final VipReferendumDetail actualReferendumDetail = getBearcodeDAO().findReferendumDetailBySourceAndVipId(source,
				referendumVipId);

		assertSame("The referendum detail is returned", referendumDetail, actualReferendumDetail);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findStateBySourceAndName(VipSource, String)}.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Jun 27, 2012
	 */
	@Test
	public final void testFindStateBySourceAndName() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateName = "SN";
		final VipState state = createMock("State", VipState.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipState.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.uniqueResult()).andReturn(state);
		replayAll();

		final VipState actualState = getBearcodeDAO().findStateBySourceAndName(source, stateName);

		assertSame("The state is returned", state, actualState);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findStateBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindStateBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long id = 982l;
		final VipState state = createMock("State", VipState.class);
		setUpFindBySourceAndId(VipState.class, state);
		replayAll();

		final VipState actualState = getBearcodeDAO().findStateBySourceAndVipId(source, id);

		assertSame("The state is returned", state, actualState);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findStatesBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	@Test
	public final void testFindStatesBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipState state = createMock("State", VipState.class);
		final List<VipState> states = Arrays.asList(state);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipState.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(states);
		replayAll();

		final List<VipState> actualStates = getBearcodeDAO().findStatesBySource(source);

		assertSame("The states are returned", states, actualStates);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findStreetNamesBySourceStateAndCity(VipSource, String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	public final void testFindStreetNamesBySourceStateAndCity() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String city = "City";
		final String streetName = "Street Name";
		final List<String> streetNames = Arrays.asList(streetName);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final List<VipStreetSegment> streetSegments = Arrays.asList(streetSegment);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipStreetSegment.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.createAlias("nonHouseAddress", "nha")).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(streetSegments);
		final VipDetailAddress nonHouseAddress = createMock("NonHouseAddress", VipDetailAddress.class);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress);
		EasyMock.expect(nonHouseAddress.toStreet()).andReturn(streetName);
		replayAll();

		final List<String> actualStreetNames = getBearcodeDAO()
				.findStreetNamesBySourceStateAndCity(source, stateAbbreviation, city);

		assertEquals("The street names are returned", streetNames, actualStreetNames);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.VipDAO#findStreetSegmentForAddress(VipSource, String, int, String, String, String, String, String, String, String, String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jul 5, 2012
	 * @version Aug 31, 2012
	 */
	@Test
	public final void testFindStreetSegmentForAddress() {
		final VipSource vipSource = createMock("VipSource", VipSource.class);
		final int houseNumber = 83;
		final String houseNumberSuffix = "1/2";
		final String streetDirection = "S";
		final String streetName = "JOHNSON";
		final String streetSuffix = "DR";
		final String addressDirection = "N";
		final String city = "CITY";
		final String state = "ST";
		final String zip = "13579";
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipStreetSegment.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		final Criteria nonHouseCriteria = createMock("NonHouseCriteria", Criteria.class);
		EasyMock.expect(criteria.createCriteria("nonHouseAddress")).andReturn(nonHouseCriteria);
		EasyMock.expect(nonHouseCriteria.add((Criterion) EasyMock.anyObject())).andReturn(nonHouseCriteria).atLeastOnce();
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.uniqueResult()).andReturn(streetSegment);
		replayAll();

		final VipStreetSegment actualStreetSegment = getBearcodeDAO().findStreetSegmentForAddress(vipSource, null, houseNumber,
				houseNumberSuffix, streetDirection, streetName, streetSuffix, addressDirection, city, state, zip);

		assertSame("The street segment is returned", streetSegment, actualStreetSegment);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findStreetSegmentsBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	@Test
	public final void testFindStreetSegmentsBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final List<VipStreetSegment> streetSegments = Arrays.asList(streetSegment);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipStreetSegment.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(streetSegments);
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getBearcodeDAO().findStreetSegmentsBySource(source);

		assertSame("The street segments are returned", streetSegments, actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findStreetSegmentsBySourceAndZip(VipSource, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindStreetSegmentsForSourceAndZip() {
		final VipSource source = createMock("Source", VipSource.class);
		final String zipCode = "13579";
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final List<VipStreetSegment> streetSegments = Arrays.asList(streetSegment);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, VipStreetSegment.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.createAlias("nonHouseAddress", "nha")).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(streetSegments);
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getBearcodeDAO().findStreetSegmentsBySourceAndZip(source, zipCode);

		assertSame("The street segments are returned", streetSegments, actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VipDAO#findZipCodesBySourceAndState(VipSource, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 30, 2012
	 */
	@Test
	public final void testFindZipCodesForSourceAndState() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String zipCode = "97531";
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(getHibernateTemplate().find(VipDAO.FIND_ZIP_CODES, source, stateAbbreviation)).andReturn(zipCodes);
		replayAll();

		final List<String> actualZipCodes = getBearcodeDAO().findZipCodesBySourceAndZip(source, stateAbbreviation);

		assertSame("The ZIP codes are returned", zipCodes, actualZipCodes);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final VipDAO createBearcodeDAO() {
		final VipDAO vipDAO = new VipDAO();
		setStateDAO(createMock("StateDAO", StateDAO.class));
		vipDAO.setStateDAO(getStateDAO());
		return vipDAO;
	}

	/**
	 * Sets up to perform a find by source and VIP identifier.
	 * 
	 * @author IanBrown
	 * @param vipClass
	 *            the class of VIP object to find.
	 * @param vipObject
	 *            the VIP object to find.
	 * @since Jun 27, 2012
	 * @version Jun 27, 2012
	 */
	private <V extends AbstractVip> void setUpFindBySourceAndId(final Class<V> vipClass, final V vipObject) {
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, vipClass);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.uniqueResult()).andReturn(vipObject);
	}

	/**
	 * Sets up a query.
	 * 
	 * @author IanBrown
	 * @param session
	 *            the session.
	 * @param queryName
	 *            the name of the query.
	 * @param queryString
	 *            the string for the query.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private void setUpQuery(final Session session, final String queryName, final String queryString) {
		final Query query = createMock(queryName, Query.class);
		EasyMock.expect(session.createQuery(queryString)).andReturn(query);
		EasyMock.expect(query.executeUpdate()).andReturn(queryString.hashCode());
	}
}
