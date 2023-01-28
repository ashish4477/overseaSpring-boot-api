/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.DAO.VipDAO;
import com.bearcode.ovf.model.common.UserAddress;
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
import com.bearcode.ovf.tools.vip.xml.ObjectFactory;
import com.bearcode.ovf.tools.vip.xml.SimpleAddressType;
import com.bearcode.ovf.tools.vip.xml.VipObject;
import com.bearcode.ovf.tools.vip.xml.VipObject.Ballot;
import com.bearcode.ovf.tools.vip.xml.VipObject.Ballot.CandidateId;
import com.bearcode.ovf.tools.vip.xml.VipObject.BallotResponse;
import com.bearcode.ovf.tools.vip.xml.VipObject.Candidate;
import com.bearcode.ovf.tools.vip.xml.VipObject.Contest;
import com.bearcode.ovf.tools.vip.xml.VipObject.CustomBallot;
import com.bearcode.ovf.tools.vip.xml.VipObject.CustomBallot.BallotResponseId;
import com.bearcode.ovf.tools.vip.xml.VipObject.Election;
import com.bearcode.ovf.tools.vip.xml.VipObject.ElectoralDistrict;
import com.bearcode.ovf.tools.vip.xml.VipObject.Locality;
import com.bearcode.ovf.tools.vip.xml.VipObject.Precinct;
import com.bearcode.ovf.tools.vip.xml.VipObject.PrecinctSplit;
import com.bearcode.ovf.tools.vip.xml.VipObject.Referendum;
import com.bearcode.ovf.tools.vip.xml.VipObject.Source;
import com.bearcode.ovf.tools.vip.xml.VipObject.State;
import com.bearcode.ovf.tools.vip.xml.VipObject.StreetSegment;

/**
 * Test for {@link VipService}.
 * 
 * @author IanBrown
 * 
 * @since Jun 25, 2012
 * @version Oct 11, 2012
 */
public final class VipServiceTest extends EasyMockSupport {

	/**
	 * the VIP DAO.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipDAO vipDAO;

	/**
	 * the VIP service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipService vipService;

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStreetNamesBySourceStateAndCity(VipSource, String, String)} .
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	public final void findStreetNamesBySourceStateAndCity() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String city = "City";
		final String streetName = "Street Name";
		final List<String> streetNames = Arrays.asList(streetName);
		EasyMock.expect(getVipDAO().findStreetNamesBySourceStateAndCity(source, stateAbbreviation, city)).andReturn(streetNames);
		replayAll();

		final List<String> actualStreetNames = getVipService().findStreetNamesBySourceStateAndCity(source, stateAbbreviation, city);

		assertSame("The street names are returned", streetNames, actualStreetNames);
		verifyAll();
	}

	/**
	 * Sets up to test the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@Before
	public final void setUpVipService() {
		setVipDAO(createMock("VipDAO", VipDAO.class));
		setVipService(createVipService());
		getVipService().setVipDAO(getVipDAO());
	}

	/**
	 * Tears down the VIP service after testing.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@After
	public final void tearDownVipService() {
		setVipService(null);
		setVipDAO(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#clear()}.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@Test
	public final void testClear() {
		getVipDAO().clear();
		replayAll();

		getVipService().clear();

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a ballot.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_ballot() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 18782l;
		final Election election = createElection(electionId, stateId, vipState);
		final long electoralDistrictId = 7862l;
		final long ballotId = 5552l;
		final int ballotPlacement = 2;
		final VipBallot vipBallot = null;
		final Contest contest = createContest(electionId, electoralDistrictId, false, null, true, null, null, null, ballotPlacement,
				ballotId, vipBallot);
		final long candidateId = 762l;
		final long referendumId = 2323l;
		final long customBallotId = 6725l;
		final boolean writeIn = true;
		final int sortOrder = 3;
		final Ballot ballot = createBallot(ballotId, vipBallot, candidateId, sortOrder, null, referendumId, null, customBallotId,
				null, writeIn);
		final Candidate candidate = createCandidate(candidateId, sortOrder, null, null);
		final List<Object> vipElements = Arrays.asList(source, state, election, contest, ballot, candidate);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a ballot response.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_ballotResponse() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 762l;
		final Election election = createElection(electionId, stateId, vipState);
		final long electoralDistrictId = 7862l;
		final long ballotId = 5552l;
		final VipBallot vipBallot = null;
		final Contest contest = createContest(electionId, electoralDistrictId, false, null, false, null, null, null, null,
				ballotId, vipBallot);
		final Long customBallotId = 652l;
		final VipCustomBallot vipCustomBallot = null;
		final Ballot ballot = createBallot(ballotId, vipBallot, null, null, null, null, null, customBallotId, vipCustomBallot, false);
		final long ballotResponseId = 42623l;
		final VipBallotResponse vipBallotResponse = null;
		final CustomBallot customBallot = createCustomBallot(customBallotId, vipCustomBallot, ballotResponseId, vipBallotResponse);
		final BallotResponse ballotResponse = createBallotResponse(ballotResponseId, vipBallotResponse);
		final List<Object> vipElements = Arrays.asList(source, state, election, contest, ballot, customBallot, ballotResponse);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a candidate.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_candidate() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 762l;
		final Election election = createElection(electionId, stateId, vipState);
		final long electoralDistrictId = 7862l;
		final long ballotId = 5552l;
		final VipBallot vipBallot = null;
		final Contest contest = createContest(electionId, electoralDistrictId, true, "Party Time", true, "Office", 2, 3, null,
				ballotId, vipBallot);
		final long candidateId = 872l;
		final VipCandidate vipCandidate = null;
		final VipCandidateBio vipCandidateBio = null;
		final Ballot ballot = createBallot(ballotId, vipBallot, candidateId, null, vipCandidate, null, null, null, null, true);
		final Candidate candidate = createCandidate(candidateId, null, vipCandidate, vipCandidateBio);
		final List<Object> vipElements = Arrays.asList(source, state, election, contest, ballot, candidate);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a contest.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_contest() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 762l;
		final Election election = createElection(electionId, stateId, vipState);
		final long electoralDistrictId = 7862l;
		final long ballotId = 5552l;
		final Contest contest = createContest(electionId, electoralDistrictId, false, null, false, null, null, null, null, ballotId, null);
		final List<Object> vipElements = Arrays.asList(source, state, election, contest);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a custom ballot.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_customBallot() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 762l;
		final Election election = createElection(electionId, stateId, vipState);
		final long electoralDistrictId = 7862l;
		final long ballotId = 5552l;
		final VipBallot vipBallot = null;
		final Contest contest = createContest(electionId, electoralDistrictId, false, null, false, null, null, null, null,
				ballotId, vipBallot);
		final Long customBallotId = 652l;
		final VipCustomBallot vipCustomBallot = null;
		final Ballot ballot = createBallot(ballotId, vipBallot, null, null, null, null, null, customBallotId, vipCustomBallot, false);
		final long ballotResponseId = 725542l;
		final CustomBallot customBallot = createCustomBallot(customBallotId, vipCustomBallot, ballotResponseId, null);
		final List<Object> vipElements = Arrays.asList(source, state, election, contest, ballot, customBallot);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with an electoral district.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_electoralDistrict() {
		final long stateId = 23l;
		final String stateName = "SN";
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final VipState vipState = null;
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 123l;
		final Election election = createElection(electionId, stateId, vipState);
		final long localityId = 42l;
		final Locality locality = createLocality(localityId, stateId, vipState);
		final long electoralDistrictId = 298l;
		final VipElectoralDistrict vipElectoralDistrict = null;
		final long precinctId = 762l;
		final Precinct precinct = createPrecinct(precinctId, null, localityId, electoralDistrictId, vipElectoralDistrict);
		final ElectoralDistrict electoralDistrict = createElectoralDistrict(electoralDistrictId, vipElectoralDistrict);
		final List<Object> vipElements = Arrays.asList(source, election, state, locality, precinct, electoralDistrict);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with just a source and an
	 * election.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Sep 20, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_emptyElection() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 277l;
		final Election election = createElection(electionId, stateId, vipState);
		final List<Object> vipElements = Arrays.asList(source, state, election);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a locality.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_locality() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 762l;
		final Election election = createElection(electionId, stateId, vipState);
		final long localityId = 872l;
		final Locality locality = createLocality(localityId, stateId, vipState);
		final List<Object> vipElements = Arrays.asList(source, election, state, locality);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with no elements.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testConvert_noElements() {
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final List<Object> vipElements = new ArrayList<Object>();
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		replayAll();

		getVipService().convert(vipObject, new Date());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for the case where there is no source.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testConvert_noSource() {
		EasyMock.expect(getVipDAO().findLatestSource()).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Object vipElement = createMock("VipElement", Object.class);
		final List<Object> vipElements = Arrays.asList(vipElement);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		replayAll();

		getVipService().convert(vipObject, new Date());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a source is the same as
	 * or older than the existing one.
	 * 
	 * @author IanBrown
	 * @since Aug 21, 2012
	 * @version Oct 11, 2012
	 */
	@Test
	public final void testConvert_olderSource() {
		final VipSource latestSource = createMock("LatestSource", VipSource.class);
		EasyMock.expect(getVipDAO().findLatestSource()).andReturn(latestSource);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final long sourceId = 872l;
		final long vipId = 987287l;
		final String sourceName = "Source Name";
		final Source source = createSource(sourceId, vipId, sourceName, false);
		EasyMock.expect(latestSource.getName()).andReturn(sourceName);
		EasyMock.expect(latestSource.getSourceId()).andReturn(sourceId);
		EasyMock.expect(latestSource.getVipId()).andReturn(vipId);
		EasyMock.expect(latestSource.getDateTime()).andReturn(new Date()).atLeastOnce();
		EasyMock.expect(latestSource.getLastModified()).andReturn(new Date()).atLeastOnce();
		final List<Object> vipElements = Arrays.asList((Object) source);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		replayAll();

		getVipService().convert(vipObject, new Date(0l));

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a precinct.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_precinct() {
		final long stateId = 23l;
		final String stateName = "SN";
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final VipState vipState = null;
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 123l;
		final Election election = createElection(electionId, stateId, vipState);
		final long localityId = 42l;
		final Locality locality = createLocality(localityId, stateId, vipState);
		final long electoralDistrictId = 298l;
		final long precinctId = 52l;
		final Precinct precinct = createPrecinct(precinctId, null, localityId, electoralDistrictId, null);
		final List<Object> vipElements = Arrays.asList(source, election, state, locality, precinct);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a precinct split.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_precinctSplit() {
		final long stateId = 23l;
		final String stateName = "SN";
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final VipState vipState = null;
		final State state = createState(stateId, stateName, vipState);
		final long electionId = 123l;
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final Election election = createElection(electionId, stateId, vipState);
		final long localityId = 42l;
		final Locality locality = createLocality(localityId, stateId, vipState);
		final long electoralDistrictId = 298l;
		final VipElectoralDistrict vipElectoralDistrict = null;
		final long precinctId = 52l;
		final VipPrecinct vipPrecinct = null;
		final Precinct precinct = createPrecinct(precinctId, vipPrecinct, localityId, electoralDistrictId, vipElectoralDistrict);
		final long precinctSplitId = 236l;
		final PrecinctSplit precinctSplit = createPrecinctSplit(precinctSplitId, null, precinctId, vipPrecinct,
				electoralDistrictId, vipElectoralDistrict);
		final List<Object> vipElements = Arrays.asList(source, election, state, locality, precinct, precinctSplit);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a referendum.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_referendum() {
		final long stateId = 23l;
		final String stateName = "SN";
		final VipState vipState = null;
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 762l;
		final Election election = createElection(electionId, stateId, vipState);
		final long electoralDistrictId = 7862l;
		final long ballotId = 5552l;
		final VipBallot vipBallot = null;
		final Contest contest = createContest(electionId, electoralDistrictId, false, null, false, null, null, null, null,
				ballotId, vipBallot);
		final long referendumId = 982l;
		final VipReferendum vipReferendum = null;
		final Ballot ballot = createBallot(ballotId, vipBallot, null, null, null, referendumId, vipReferendum, null, null, false);
		final long ballotResponseId = 8672l;
		final VipReferendumDetail vipReferendumDetail = null;
		final Referendum referendum = createReferendum(referendumId, vipReferendum, ballotResponseId, null, vipReferendumDetail);
		final List<Object> vipElements = Arrays.asList(source, state, election, contest, ballot, referendum);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with just a source.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testConvert_sourceOnly() {
		EasyMock.expect(getVipDAO().findLatestSource()).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final List<Object> vipElements = Arrays.asList((Object) source);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		replayAll();

		getVipService().convert(vipObject, new Date());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a state.
	 * 
	 * @author IanBrown
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_state() {
		final long stateId = 23l;
		final String stateName = "SN";
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final VipState vipState = null;
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 762l;
		final Election election = createElection(electionId, stateId, vipState);
		final List<Object> vipElements = Arrays.asList(source, election, state);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a street segment.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_streetSegment() {
		final long stateId = 23l;
		final String stateName = "SN";
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final VipState vipState = null;
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 123l;
		final Election election = createElection(electionId, stateId, vipState);
		final long localityId = 42l;
		final Locality locality = createLocality(localityId, stateId, vipState);
		final long electoralDistrictId = 298l;
		final long precinctId = 73216l;
		final VipPrecinct vipPrecinct = null;
		final Precinct precinct = createPrecinct(precinctId, vipPrecinct, localityId, electoralDistrictId, null);
		final long streetSegmentId = 7126l;
		final StreetSegment streetSegment = createStreetSegment(streetSegmentId, stateName, precinctId, vipPrecinct, null, null);
		final List<Object> vipElements = Arrays.asList(source, election, state, locality, precinct, streetSegment);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#convert(VipObject, Date)} for a VIP object with a street segment for a
	 * precinct split.
	 * 
	 * @author IanBrown
	 * @since Jun 27, 2012
	 * @version Oct 11, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testConvert_streetSegmentPrecinctSplit() {
		final long stateId = 23l;
		final String stateName = "SN";
		EasyMock.expect(getVipDAO().findLatestSource(stateName)).andReturn(null);
		final VipObject vipObject = createMock("VipObject", VipObject.class);
		final Source source = createSource(0l, 9726l, "Source Name", true);
		final VipState vipState = null;
		final State state = createState(stateId, stateName, vipState);
		getVipDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {

			@Override
			public final void makeAllPersistent(final Collection objects) {
				assertEquals("There is one object in the collection", 1, objects.size());
				final Object object = objects.iterator().next();
				assertTrue("The object is a VipState", object instanceof VipState);
				final VipState vipState = (VipState) object;
				assertEquals("The state identifier is set", stateId, vipState.getVipId().longValue());
				assertEquals("The state name is set", stateName, vipState.getName());
			}
		});
		final long electionId = 123l;
		final Election election = createElection(electionId, stateId, vipState);
		final long localityId = 42l;
		final Locality locality = createLocality(localityId, stateId, vipState);
		final long electoralDistrictId = 298l;
		final long precinctId = 73216l;
		final VipPrecinct vipPrecinct = null;
		final Precinct precinct = createPrecinct(precinctId, vipPrecinct, localityId, electoralDistrictId, null);
		final long precinctSplitId = 6512l;
		final VipPrecinctSplit vipPrecinctSplit = null;
		final PrecinctSplit precinctSplit = createPrecinctSplit(precinctSplitId, vipPrecinctSplit, precinctId, vipPrecinct,
				electoralDistrictId, null);
		final long streetSegmentId = 7126l;
		final StreetSegment streetSegment = createStreetSegment(streetSegmentId, stateName, precinctId, vipPrecinct,
				precinctSplitId, vipPrecinctSplit);
		final List<Object> vipElements = Arrays.asList(source, election, state, locality, precinct, precinctSplit, streetSegment);
		EasyMock.expect(vipObject.getSourceOrElectionOrState()).andReturn(vipElements).atLeastOnce();
		setUpSourceComplete();
		replayAll();

		getVipService().convert(vipObject, new Date());

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findCandidateBioBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	public final void testFindCandidateBioBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long candidateVipId = 98298l;
		final VipCandidateBio candidateBio = createMock("CandidateBio", VipCandidateBio.class);
		EasyMock.expect(getVipDAO().findCandidateBioBySourceAndVipId(source, candidateVipId)).andReturn(candidateBio);
		replayAll();

		final VipCandidateBio actualCandidateBio = getVipService().findCandidateBioBySourceAndVipId(source, candidateVipId);

		assertSame("The candidate bio is returned", candidateBio, actualCandidateBio);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findCitiesBySourceStateAndVotingRegion(VipSource, String, String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	@Test
	public final void testFindCitiesBySourceStateAndVotingRegion() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region Name";
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(getVipDAO().findCitiesBySourceStateAndVotingRegion(source, stateAbbreviation, votingRegionName)).andReturn(
				cities);
		replayAll();

		final List<String> actualCities = getVipService().findCitiesBySourceStateAndVotingRegion(source, stateAbbreviation,
				votingRegionName);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findCitiesBySourceStateAndZip(VipSource, String, String)} .
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	public final void testFindCitiesBySourceStateAndZip() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String zip = "97531";
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(getVipDAO().findCitiesBySourceStateAndZip(source, stateAbbreviation, zip)).andReturn(cities);
		replayAll();

		final List<String> actualCities = getVipService().findCitiesBySourceStateAndZip(source, stateAbbreviation, zip);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findContestsBySourceAndElection(VipSource, VipElection)}.
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
		EasyMock.expect(getVipDAO().findContestsBySourceAndElection(source, election)).andReturn(contests);
		replayAll();

		final List<VipContest> actualContests = getVipService().findContestsBySourceAndElection(source, election);

		assertSame("The contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findContestsForElectoralDistrict(VipElectoralDistrict)}.
	 * 
	 * @author IanBrown
	 * @since Jul 9, 2012
	 * @version Jul 9, 2012
	 */
	@Test
	public final void testFindContestsForElectoralDistrict() {
		final VipElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", VipElectoralDistrict.class);
		final VipContest contest = createMock("Contest", VipContest.class);
		final List<VipContest> contests = Arrays.asList(contest);
		EasyMock.expect(getVipDAO().findContestsForElectoralDistrict(electoralDistrict)).andReturn(contests);
		replayAll();

		final List<VipContest> actualContests = getVipService().findContestsForElectoralDistrict(electoralDistrict);

		assertSame("The contests are returned", contests, actualContests);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findElectionBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	@Test
	public final void testFindElectionBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipElection election = createMock("Election", VipElection.class);
		EasyMock.expect(getVipDAO().findElectionBySource(source)).andReturn(election);
		replayAll();

		final VipElection actualElection = getVipDAO().findElectionBySource(source);

		assertSame("The election is returned", election, actualElection);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findLatestSource()}.
	 * 
	 * @author IanBrown
	 * @since Jun 28, 2012
	 * @version Sep 17, 2012
	 */
	@Test
	public final void testFindLatestSource() {
		final VipSource latestSource = createMock("LatestSource", VipSource.class);
		EasyMock.expect(getVipDAO().findLatestSource()).andReturn(latestSource);
		replayAll();

		final VipSource actualLatestSource = getVipService().findLatestSource();

		assertSame("The latest source is returned", latestSource, actualLatestSource);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findLatestSource(String...)}.
	 * 
	 * @author IanBrown
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	@Test
	public final void testFindLatestSourceStrings() {
		final String state = "ST";
		final VipSource latestSource = createMock("LatestSource", VipSource.class);
		EasyMock.expect(getVipDAO().findLatestSource(state)).andReturn(latestSource);
		replayAll();

		final VipSource actualLatestSource = getVipService().findLatestSource(state);

		assertSame("The latest source is returned", latestSource, actualLatestSource);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findLocalitiesByStateAndType(VipState, String)}.
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
		EasyMock.expect(getVipDAO().findLocalitiesByStateAndType(state, type)).andReturn(localities);
		replayAll();

		final List<VipLocality> actualLocalities = getVipService().findLocalitiesByStateAndType(state, type);

		assertSame("The localities are returned", localities, actualLocalities);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findPrecinctsBySource(VipSource)}.
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
		EasyMock.expect(getVipDAO().findPrecinctsBySource(source)).andReturn(precincts);
		replayAll();

		final List<VipPrecinct> actualPrecincts = getVipService().findPrecinctsBySource(source);

		assertSame("The precincts are returned", precincts, actualPrecincts);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findPrecinctSplitsBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	@Test
	public final void testFindPrecinctSplitsBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipPrecinctSplit precinctSplit = createMock("Precinct", VipPrecinctSplit.class);
		final List<VipPrecinctSplit> precinctSplits = Arrays.asList(precinctSplit);
		EasyMock.expect(getVipDAO().findPrecinctSplitsBySource(source)).andReturn(precinctSplits);
		replayAll();

		final List<VipPrecinctSplit> actualPrecinctSplits = getVipService().findPrecinctSplitsBySource(source);

		assertSame("The precinct splits are returned", precinctSplits, actualPrecinctSplits);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findReferendumDetailBySourceAndVipId(VipSource, long)}.
	 * 
	 * @author IanBrown
	 * @since Aug 17, 2012
	 * @version Aug 17, 2012
	 */
	@Test
	public final void testFindReferendumDetailBySourceAndVipId() {
		final VipSource source = createMock("Source", VipSource.class);
		final long referendumVipId = 42176l;
		final VipReferendumDetail referendumDetail = createMock("ReferendumDetail", VipReferendumDetail.class);
		EasyMock.expect(getVipDAO().findReferendumDetailBySourceAndVipId(source, referendumVipId)).andReturn(referendumDetail);
		replayAll();

		final VipReferendumDetail actualReferendumDetail = getVipService().findReferendumDetailBySourceAndVipId(source,
				referendumVipId);

		assertSame("The referendum detail is returned", referendumDetail, actualReferendumDetail);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStatesBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	@Test
	public final void testFindStateBySource() {
		final VipSource source = createMock("Source", VipSource.class);
		final VipState state = createMock("State", VipState.class);
		final List<VipState> states = Arrays.asList(state);
		EasyMock.expect(getVipDAO().findStatesBySource(source)).andReturn(states);
		replayAll();

		final List<VipState> actualStates = getVipService().findStatesBySource(source);

		assertSame("The states are returned", states, actualStates);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStateBySourceAndName(VipSource, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 27, 2012
	 * @version Jul 27, 2012
	 */
	@Test
	public final void testFindStateBySourceAndName() {
		final VipSource source = createMock("Source", VipSource.class);
		final String name = "State Name";
		final VipState state = createMock("State", VipState.class);
		EasyMock.expect(getVipDAO().findStateBySourceAndName(source, name)).andReturn(state);
		replayAll();

		final VipState actualState = getVipService().findStateBySourceAndName(source, name);

		assertSame("The state is returned", state, actualState);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.VipService#findStreetSegmentForAddress(VipSource, String, int, String, String, String, String, String, String, String, String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jul 5, 2012
	 * @version Aug 31, 2012
	 */
	@Test
	public final void testFindStreetSegmentForAddress() {
		final VipSource vipSource = createMock("VipSource", VipSource.class);
		final int houseNumber = 27;
		final String houseNumberSuffix = "1/2";
		final String streetDirection = "N";
		final String streetName = "ALISON";
		final String streetSuffix = "AVE";
		final String addressDirection = "SW";
		final String city = "CITY";
		final String state = "ST";
		final String zip = "54321";
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		EasyMock.expect(
				getVipDAO().findStreetSegmentForAddress(vipSource, null, houseNumber, houseNumberSuffix, streetDirection,
						streetName, streetSuffix, addressDirection, city, state, zip)).andReturn(streetSegment);
		replayAll();

		final VipStreetSegment actualStreetSegment = getVipService().findStreetSegmentForAddress(vipSource, null, houseNumber,
				houseNumberSuffix, streetDirection, streetName, streetSuffix, addressDirection, city, state, zip);

		assertSame("The street segment is returned", streetSegment, actualStreetSegment);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStreetSegmentsBySource(VipSource)}.
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
		EasyMock.expect(getVipDAO().findStreetSegmentsBySource(source)).andReturn(streetSegments);
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getVipService().findStreetSegmentsBySource(source);

		assertSame("The street segments are returned", streetSegments, actualStreetSegments);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findZipCodesBySourceAndState(VipSource, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindStreetSegmentsForSourceAndState() {
		final VipSource source = createMock("Source", VipSource.class);
		final String stateAbbreviation = "SA";
		final String zipCode = "54321";
		final List<String> zipCodes = Arrays.asList(zipCode);
		EasyMock.expect(getVipDAO().findZipCodesBySourceAndZip(source, stateAbbreviation)).andReturn(zipCodes);
		replayAll();

		final List<String> actualZipCodes = getVipService().findZipCodesBySourceAndState(source, stateAbbreviation);

		assertSame("The ZIP codes are returned", zipCodes, actualZipCodes);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStreetSegmentsBySourceAndZip(VipSource, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindStreetSegmentsForSourceAndZip() {
		final VipSource source = createMock("Source", VipSource.class);
		final String zipCode = "54321";
		final VipStreetSegment streetSegment = createMock("StreetSegment", VipStreetSegment.class);
		final List<VipStreetSegment> streetSegments = Arrays.asList(streetSegment);
		EasyMock.expect(getVipDAO().findStreetSegmentsBySourceAndZip(source, zipCode)).andReturn(streetSegments);
		replayAll();

		final List<VipStreetSegment> actualStreetSegments = getVipService().findStreetSegmentsBySourceAndZip(source, zipCode);

		assertSame("The street segments are returned", streetSegments, actualStreetSegments);
		verifyAll();
	}

	/**
	 * Creates a VIP ballot to convert.
	 * 
	 * @author IanBrown
	 * @param ballotId
	 *            the identifier of the ballot.
	 * @param vipBallot
	 *            the VIP ballot.
	 * @param candidateId
	 *            the candidate identifier.
	 * @param sortOrder the sort order for the candidate.
	 * @param vipCandidate
	 *            the VIP candidate.
	 * @param referendumId
	 *            the referendum identifier.
	 * @param vipReferendum
	 *            the VIP referendum.
	 * @param customBallotId
	 *            the custom ballot identifier.
	 * @param vipCustomBallot
	 *            the VIP custom ballot.
	 * @param writeIn
	 *            are write-ins allowed?
	 * @return the ballot.
	 * @since Jun 26, 2012
	 * @version Sep 20, 2012
	 */
	@SuppressWarnings("unchecked")
	private Ballot createBallot(final long ballotId, final VipBallot vipBallot, final Long candidateId,
			final Integer sortOrder, final VipCandidate vipCandidate, final Long referendumId, final VipReferendum vipReferendum,
			final Long customBallotId, final VipCustomBallot vipCustomBallot, final boolean writeIn) {
		final Ballot ballot = createMock("Ballot", Ballot.class);
		EasyMock.expect(ballot.getId()).andReturn(BigInteger.valueOf(ballotId));

		if (referendumId == null) {
			EasyMock.expect(ballot.getReferendumId()).andReturn(null);
		} else {
			EasyMock.expect(ballot.getReferendumId()).andReturn(BigInteger.valueOf(referendumId));
			if (vipReferendum == null) {
				getVipDAO().makePersistent(EasyMock.anyObject());
				EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
					@Override
					public final void makePersistent(final Object object) {
						final VipReferendum referendum = (VipReferendum) object;
						assertEquals("The VIP id of the referendum is set", referendumId, referendum.getVipId());
						assertNotNull("The referendum source is set", referendum.getSource());
					}
				});
			}
			if (vipBallot != null) {
				vipBallot.setReferendum((VipReferendum) EasyMock.anyObject());
			}
		}

		if (customBallotId == null) {
			EasyMock.expect(ballot.getCustomBallotId()).andReturn(null);
		} else {
			EasyMock.expect(ballot.getCustomBallotId()).andReturn(BigInteger.valueOf(customBallotId));
			if (vipCustomBallot == null) {
				getVipDAO().makePersistent(EasyMock.anyObject());
				EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
					@Override
					public final void makePersistent(final Object object) {
						final VipCustomBallot customBallot = (VipCustomBallot) object;
						assertEquals("The VIP identifier of the custom ballot is set", customBallotId, customBallot.getVipId());
						assertNotNull("The source of the custom ballot is set", customBallot.getSource());
					}
				});
			}
			if (vipBallot != null) {
				vipBallot.setCustomBallot((VipCustomBallot) EasyMock.anyObject());
			}
		}

		if (candidateId == null) {
			EasyMock.expect(ballot.getCandidateId()).andReturn(new ArrayList<CandidateId>());
		} else {
			final CandidateId candidateIdO = createMock("CandidateIdO", CandidateId.class);
			final List<CandidateId> candidateIds = Arrays.asList(candidateIdO);
			EasyMock.expect(ballot.getCandidateId()).andReturn(candidateIds);
			if (sortOrder == null) {
				EasyMock.expect(candidateIdO.getSortOrder()).andReturn(BigInteger.valueOf(1l));
			} else {
				EasyMock.expect(candidateIdO.getSortOrder()).andReturn(null);
			}
			EasyMock.expect(candidateIdO.getValue()).andReturn(BigInteger.valueOf(candidateId));
			if (vipCandidate == null) {
				getVipDAO().makePersistent(EasyMock.anyObject());
				EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
					@Override
					public final void makePersistent(final Object object) {
						final VipCandidate candidate = (VipCandidate) object;
						assertEquals("The VIP identifier of the candidate is set", candidateId, candidate.getVipId());
						assertNotNull("The source of the candidate is set", candidate.getSource());
					}
				});
			}
			if (vipBallot != null) {
				vipBallot.setCandidates((List<VipBallotCandidate>) EasyMock.anyObject());
				EasyMock.expectLastCall().andDelegateTo(new VipBallot() {
					@Override
					public final void setCandidates(final List<VipBallotCandidate> candidates) {
						assertEquals("There is one candidate", 1, candidates.size());
						for (final VipBallotCandidate candidate : candidates) {
							assertEquals("The sort order for the candidate is set", sortOrder == null ? Integer.valueOf(1) : sortOrder, candidate.getSortOrder());
						}
					}
				});
			}
		}

		EasyMock.expect(ballot.getWriteIn()).andReturn(writeIn ? "yes" : null);
		if (vipBallot != null) {
			vipBallot.setWriteIn(writeIn);
		}

		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipBallot ballot = (VipBallot) object;
				if (vipBallot == null) {
					assertEquals("The VIP id of the ballot is set", ballotId, ballot.getVipId().longValue());
					assertNotNull("The source of the ballot is set", ballot.getSource());
					if (referendumId == null) {
						assertNull("The ballot referendum is not set", ballot.getReferendum());
					} else {
						assertEquals("The ballot referendum is set", referendumId, ballot.getReferendum().getVipId());
					}
					if (candidateId == null) {
						assertNull("There are no ballot candidates", ballot.getCandidates());
					} else {
						final List<VipBallotCandidate> candidates = ballot.getCandidates();
						assertNotNull("The ballot candidates are set", candidates);
						assertEquals("There is one candidate", 1, candidates.size());
						final VipBallotCandidate ballotCandidate = candidates.get(0);
						assertEquals("The sort order is set", sortOrder == null ? Integer.valueOf(1) : sortOrder, ballotCandidate.getSortOrder());
						final VipCandidate candidate = ballotCandidate.getCandidate();
						assertEquals("The candidate is set", candidateId, candidate.getVipId());
					}
					if (customBallotId == null) {
						assertNull("The custom ballot is not set", ballot.getCustomBallot());
					} else {
						assertEquals("The custom ballot is set", customBallotId, ballot.getCustomBallot().getVipId());
					}
					assertEquals("The write-in flag is set", writeIn, ballot.isWriteIn());
				} else {
					assertSame("The ballot is saved", vipBallot, ballot);
				}
			}
		});
		return ballot;
	}

	/**
	 * Creates a ballot response to be converted.
	 * 
	 * @author IanBrown
	 * @param ballotResponseId
	 *            the ballot response identifier.
	 * @param vipBallotResponse
	 *            the VIP ballot response.
	 * @return the ballot response.
	 * @since Jun 28, 2012
	 * @version Sep 20, 2012
	 */
	private BallotResponse createBallotResponse(final long ballotResponseId, final VipBallotResponse vipBallotResponse) {
		final BallotResponse ballotResponse = createMock("BallotResponse", BallotResponse.class);
		EasyMock.expect(ballotResponse.getId()).andReturn(BigInteger.valueOf(ballotResponseId));
		final String text = "Text";
		EasyMock.expect(ballotResponse.getText()).andReturn(text);
		if (vipBallotResponse != null) {
			vipBallotResponse.setText(text);
		}
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipBallotResponse ballotResponse = (VipBallotResponse) object;
				if (vipBallotResponse == null) {
					assertEquals("The ballot response VIP id is set", ballotResponseId, ballotResponse.getVipId().longValue());
					assertNotNull("The ballot response source is set", ballotResponse.getSource());
					assertEquals("The ballot source text is set", text, ballotResponse.getText());
				} else {
					assertSame("The VIP ballot response save", vipBallotResponse, ballotResponse);
				}
			}
		});
		return ballotResponse;
	}

	/**
	 * Creates a candidate to be converted.
	 * 
	 * @author IanBrown
	 * @param candidateId
	 *            the candidate identifier.
	 * @param sortOrder the sort order for the candidate.
	 * @param vipCandidate
	 *            the VIP candidate.
	 * @param vipCandidateBio
	 *            the VIP candidate bio.
	 * @return the candidate.
	 * @since Jun 27, 2012
	 * @version May 8, 2013
	 */
	private Candidate createCandidate(final long candidateId, final Integer sortOrder, final VipCandidate vipCandidate, final VipCandidateBio vipCandidateBio) {
		final Candidate candidate = createMock("Candidate", Candidate.class);
		EasyMock.expect(candidate.getId()).andReturn(BigInteger.valueOf(candidateId)).atLeastOnce();
		EasyMock.expect(candidate.getSortOrder()).andReturn(sortOrder == null ? null : BigInteger.valueOf(sortOrder.longValue())).anyTimes();
		final String candidateName = "Candidate Name";
		EasyMock.expect(candidate.getName()).andReturn(candidateName);
		final String candidateParty = "Candidate Party";
		EasyMock.expect(candidate.getParty()).andReturn(candidateParty);
		if (vipCandidate != null) {
			vipCandidate.setName(candidateName);
			vipCandidate.setParty(candidateParty);
		}
		EasyMock.expect(candidate.getIncumbent()).andReturn("").anyTimes();
		final String biography = "Candidate Biography";
		EasyMock.expect(candidate.getBiography()).andReturn(biography);
		final String candidateUrl = "http://candidate.url";
		EasyMock.expect(candidate.getCandidateUrl()).andReturn(candidateUrl);
		final String email = "email@candidate.gov";
		EasyMock.expect(candidate.getEmail()).andReturn(email);
		final SimpleAddressType filedMailingAddress = createSimpleAddressType("Filed");
		final String phone = "1234567890";
		EasyMock.expect(candidate.getPhone()).andReturn(phone);
		final String photoUrl = "http://candidate.url/photo";
		EasyMock.expect(candidate.getPhotoUrl()).andReturn(photoUrl);
		EasyMock.expect(candidate.getFiledMailingAddress()).andReturn(filedMailingAddress);
		if (vipCandidateBio != null) {
			vipCandidateBio.setBiography(biography);
			vipCandidateBio.setCandidateUrl(candidateUrl);
			vipCandidateBio.setEmail(email);
			vipCandidateBio.setFiledMailingAddress((UserAddress) EasyMock.anyObject());
			vipCandidateBio.setPhone(phone);
			vipCandidateBio.setPhotoUrl(photoUrl);
			EasyMock.expectLastCall().andDelegateTo(new VipCandidateBio() {
				@Override
				public final void setFiledMailingAddress(final UserAddress actualFiledMailingAddress) {
					assertNotNull("A filed mailing address is provided", actualFiledMailingAddress);
					assertEquals("The street1 line is set", filedMailingAddress.getLine1(), actualFiledMailingAddress.getStreet1());
					assertEquals("The street2 line is set", filedMailingAddress.getLine2(), actualFiledMailingAddress.getStreet2());
					assertEquals("The city is set", filedMailingAddress.getCity(), actualFiledMailingAddress.getCity());
					assertEquals("The state is set", filedMailingAddress.getState(), actualFiledMailingAddress.getState());
					assertEquals("The ZIP is set", filedMailingAddress.getZip(), actualFiledMailingAddress.getZip());
				}
			});
		}

		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipCandidate candidate = (VipCandidate) object;
				if (vipCandidate == null) {
					assertEquals("The candidate VIP identifier is set", candidateId, candidate.getVipId().longValue());
					assertNotNull("The candidate source is set", candidate.getSource());
					assertEquals("The candidate name is set", candidateName, candidate.getName());
					assertEquals("The candidate party is set", candidateParty, candidate.getParty());
				} else {
					assertSame("The VIP candidate is saved", vipCandidate, candidate);
				}
			}
		});

		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipCandidateBio candidateBio = (VipCandidateBio) object;
				if (vipCandidateBio == null) {
					assertEquals("The candidate VIP identifier is set", candidateId, candidateBio.getCandidate().getVipId()
							.longValue());
					assertEquals("The candidate biography is set", biography, candidateBio.getBiography());
					assertEquals("The candidate URL is set", candidateUrl, candidateBio.getCandidateUrl());
					assertEquals("The candidate email is set", email, candidateBio.getEmail());
					final UserAddress actualFiledMailingAddress = candidateBio.getFiledMailingAddress();
					assertNotNull("A filed mailing address is provided", actualFiledMailingAddress);
					assertEquals("The street1 line is set", filedMailingAddress.getLine1(), actualFiledMailingAddress.getStreet1());
					assertEquals("The street2 line is set", filedMailingAddress.getLine2(), actualFiledMailingAddress.getStreet2());
					assertEquals("The city is set", filedMailingAddress.getCity(), actualFiledMailingAddress.getCity());
					assertEquals("The state is set", filedMailingAddress.getState(), actualFiledMailingAddress.getState());
					assertEquals("The ZIP is set", filedMailingAddress.getZip(), actualFiledMailingAddress.getZip());
					assertEquals("The phone is set", phone, candidateBio.getPhone());
					assertEquals("The photo URL is set", photoUrl, candidateBio.getPhotoUrl());
				} else {
					assertSame("The VIP candidate bio is saved", vipCandidateBio, candidateBio);
				}
			}
		});

		return candidate;
	}

	/**
	 * Creates a contest to be converted.
	 * 
	 * @author IanBrown
	 * @param electionId
	 *            the identifier for the election.
	 * @param electoralDistrictId
	 *            the electoral district identifier.
	 * @param partisan
	 *            is this a partisan election?
	 * @param primaryParty
	 *            the primary party.
	 * @param special
	 *            is this a special election?
	 * @param office
	 *            the office.
	 * @param numberElected
	 *            the number to be elected.
	 * @param numberVotingFor
	 *            the number to vote for.
	 * @param ballotPlacement the placement of this contest on the ballot.
	 * @param ballotId
	 *            the ballot identifier.
	 * @param vipBallot
	 *            the VIP ballot or <code>null</code>.
	 * @return the contest.
	 * @since Jun 26, 2012
	 * @version Oct 11, 2012
	 */
	private Contest createContest(final long electionId, final long electoralDistrictId, final boolean partisan,
			final String primaryParty, final boolean special, final String office, final Integer numberElected,
			final Integer numberVotingFor, final Integer ballotPlacement, final long ballotId, final VipBallot vipBallot) {
		final Contest contest = createMock("Contest", Contest.class);
		EasyMock.expect(contest.getElectionId()).andReturn(BigInteger.valueOf(electionId));
		EasyMock.expect(contest.getElectoralDistrictId()).andReturn(BigInteger.valueOf(electoralDistrictId));
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipElectoralDistrict electoralDistrict = (VipElectoralDistrict) object;
				assertEquals("The electoral district VIP id is set", electoralDistrictId, electoralDistrict.getVipId().longValue());
				assertNotNull("The electoral district source is set", electoralDistrict.getSource());
			}
		});
		EasyMock.expect(contest.getPartisan()).andReturn(partisan ? "yes" : null);
		EasyMock.expect(contest.getPrimaryParty()).andReturn(primaryParty);
		EasyMock.expect(contest.getSpecial()).andReturn(special ? "yes" : null);
		EasyMock.expect(contest.getOffice()).andReturn(office);
		EasyMock.expect(contest.getNumberElected()).andReturn(
				numberElected == null ? null : BigInteger.valueOf(numberElected.longValue()));
		EasyMock.expect(contest.getNumberVotingFor()).andReturn(
				numberVotingFor == null ? null : BigInteger.valueOf(numberVotingFor.longValue()));
		EasyMock.expect(contest.getBallotPlacement()).andReturn(ballotPlacement == null ? null : BigInteger.valueOf(ballotPlacement.longValue()));
		EasyMock.expect(contest.getBallotId()).andReturn(BigInteger.valueOf(ballotId));
		if (vipBallot == null) {
			getVipDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
				@Override
				public final void makePersistent(final Object object) {
					final VipBallot ballot = (VipBallot) object;
					assertEquals("The ballot VIP id is set", ballotId, ballot.getVipId().longValue());
					assertNotNull("The ballot source is set", ballot.getSource());
				}
			});
		}
		final long vipId = 872l;
		EasyMock.expect(contest.getId()).andReturn(BigInteger.valueOf(vipId));
		final String contestType = "Contest Type";
		EasyMock.expect(contest.getType()).andReturn(contestType);
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipContest contest = (VipContest) object;
				assertEquals("The VIP id is set", vipId, contest.getVipId().longValue());
				assertNotNull("The source is set", contest.getSource());
				assertEquals("The election ID is set", electionId, contest.getElection().getVipId().longValue());
				assertEquals("The partisan flag is set", partisan, contest.isPartisan());
				assertEquals("The primary party is set", primaryParty, contest.getPrimaryParty());
				assertEquals("The special flag is set", special, contest.isSpecial());
				assertEquals("The office is set", office, contest.getOffice());
				assertEquals("The number elected is set", numberElected, contest.getNumberElected());
				assertEquals("The number voting for is set", numberVotingFor, contest.getNumberVotingFor());
				assertEquals("The ballot placement is set", ballotPlacement, contest.getBallotPlacement());
				if (vipBallot == null) {
					assertEquals("The ballot ID is set", ballotId, contest.getBallot().getVipId().longValue());
				} else {
					assertSame("The ballot is set", vipBallot, contest.getBallot());
				}
				assertEquals("The electoral district ID is set", electoralDistrictId, contest.getElectoralDistrict().getVipId()
						.longValue());
				assertEquals("The contest type is set", contestType, contest.getType());
			}
		});
		return contest;
	}

	/**
	 * Creates a custom ballot to convert.
	 * 
	 * @author IanBrown
	 * @param customBallotId
	 *            the custom ballot identifier.
	 * @param vipCustomBallot
	 *            the VIP custom ballot.
	 * @param ballotResponseId
	 *            the ballot response identifier.
	 * @param vipBallotResponse
	 *            the VIP ballot response.
	 * @return the custom ballot.
	 * @since Jun 28, 2012
	 * @version Sep 20, 2012
	 */
	@SuppressWarnings("unchecked")
	private CustomBallot createCustomBallot(final long customBallotId, final VipCustomBallot vipCustomBallot,
			final long ballotResponseId, final VipBallotResponse vipBallotResponse) {
		final CustomBallot customBallot = createMock("CustomBallot", CustomBallot.class);
		EasyMock.expect(customBallot.getId()).andReturn(BigInteger.valueOf(customBallotId));
		if (vipCustomBallot != null) {
			EasyMock.expect(vipCustomBallot.getVipId()).andReturn(customBallotId).anyTimes();
		}
		final String heading = "Heading";
		final BallotResponseId ballotResponseIdO = createMock("BallotResponseId", BallotResponseId.class);
		final List<Object> elements = Arrays.asList(heading, ballotResponseIdO);
		final int sortOrder = 1;
		EasyMock.expect(ballotResponseIdO.getSortOrder()).andReturn(BigInteger.valueOf(sortOrder));
		EasyMock.expect(ballotResponseIdO.getValue()).andReturn(BigInteger.valueOf(ballotResponseId));
		EasyMock.expect(customBallot.getHeadingOrBallotResponseId()).andReturn(elements);
		if (vipBallotResponse == null) {
			getVipDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
				@Override
				public final void makePersistent(final Object object) {
					final VipBallotResponse ballotResponse = (VipBallotResponse) object;
					assertEquals("The ballot response VIP id is set", ballotResponseId, ballotResponse.getVipId().longValue());
					assertNotNull("The ballot response source is set", ballotResponse.getSource());
				}
			});
		}
		if (vipCustomBallot != null) {
			vipCustomBallot.setHeading(heading);
			vipCustomBallot.setBallotResponses((List<VipCustomBallotResponse>) EasyMock.anyObject());
		}
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipCustomBallot customBallot = (VipCustomBallot) object;
				if (vipCustomBallot == null) {
					assertEquals("The custom ballot VIP id is set", customBallotId, customBallot.getVipId().longValue());
					assertNotNull("The custom ballot source is set", customBallot.getSource());
					assertEquals("The custom ballot heading is set", heading, customBallot.getHeading());
					assertEquals("There is one custom ballot response", 1, customBallot.getBallotResponses().size());
				} else {
					assertSame("The VIP custom ballot is saved", vipCustomBallot, customBallot);
				}
			}
		});
		return customBallot;
	}

	/**
	 * Creates an election object to convert.
	 * 
	 * @author IanBrown
	 * @param electionId
	 *            the election identifier.
	 * @param stateId
	 *            the state identifier.
	 * @param vipState
	 *            the state (if one exists).
	 * @return the election object.
	 * @since Jun 26, 2012
	 * @version Sep 18, 2012
	 */
	private Election createElection(final long electionId, final long stateId, final VipState vipState) {
		final Election election = createMock("Election", Election.class);
		EasyMock.expect(election.getId()).andReturn(BigInteger.valueOf(electionId));
		final XMLGregorianCalendar date = createMock("date", XMLGregorianCalendar.class);
		EasyMock.expect(election.getDate()).andReturn(date);
		final GregorianCalendar gregorianCalendar = new GregorianCalendar();
		EasyMock.expect(date.toGregorianCalendar()).andReturn(gregorianCalendar);
		EasyMock.expect(election.getStateId()).andReturn(BigInteger.valueOf(stateId));
		final String electionType = "Election Type";
		EasyMock.expect(election.getElectionType()).andReturn(electionType);
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipElection election = (VipElection) object;
				assertEquals("The VIP ID is set", electionId, election.getVipId().longValue());
				assertNotNull("The source is set", election.getSource());
				assertEquals("The date is set", gregorianCalendar.getTime(), election.getDate());
				if (vipState == null) {
					assertEquals("The state ID is set", stateId, election.getState().getVipId().longValue());
				} else {
					assertSame("The state is set", vipState, election.getState());
				}
				assertEquals("The election type is set", electionType, election.getType());
			}
		});
		return election;
	}

	/**
	 * Creates an electoral district to convert.
	 * 
	 * @author IanBrown
	 * @param electoralDistrictId
	 *            the identifier for the electoral district.
	 * @param vipElectoralDistrict
	 *            the VIP electoral district.
	 * @return the electoral district.
	 * @since Jun 26, 2012
	 * @version Aug 29, 2012
	 */
	private ElectoralDistrict createElectoralDistrict(final long electoralDistrictId,
			final VipElectoralDistrict vipElectoralDistrict) {
		final ElectoralDistrict electoralDistrict = createMock("ElectoralDistrict", ElectoralDistrict.class);
		EasyMock.expect(electoralDistrict.getId()).andReturn(BigInteger.valueOf(electoralDistrictId));
		final String electoralDistrictName = "Electoral District Name";
		EasyMock.expect(electoralDistrict.getName()).andReturn(electoralDistrictName);
		final int electoralDistrictNumber = 872;
		EasyMock.expect(electoralDistrict.getNumber()).andReturn(BigInteger.valueOf(electoralDistrictNumber));
		final String electoralDistrictType = "Electoral District Type";
		EasyMock.expect(electoralDistrict.getType()).andReturn(electoralDistrictType);
		if (vipElectoralDistrict != null) {
			vipElectoralDistrict.setName(electoralDistrictName);
			vipElectoralDistrict.setNumber(electoralDistrictNumber);
			vipElectoralDistrict.setType(electoralDistrictType);
		}
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipElectoralDistrict electoralDistrict = (VipElectoralDistrict) object;
				if (vipElectoralDistrict == null) {
					assertEquals("The VIP id of the electoral district is set", electoralDistrictId, electoralDistrict.getVipId()
							.longValue());
					assertNotNull("The source of the electoral district is set", electoralDistrict.getSource());
					assertEquals("The name of the electoral district is set", electoralDistrictName, electoralDistrict.getName());
					assertEquals("The number of electoral district is set", electoralDistrictNumber, electoralDistrict.getNumber()
							.intValue());
					assertEquals("The type of the electoral district is set", electoralDistrictType, electoralDistrict.getType());
				} else {
					assertSame("The VIP electoral district is saved", vipElectoralDistrict, electoralDistrict);
				}
			}
		});
		return electoralDistrict;
	}

	/**
	 * Creates a locality for the state.
	 * 
	 * @author IanBrown
	 * @param localityId
	 *            the locality identifier.
	 * @param stateId
	 *            the state identifier.
	 * @param vipState
	 *            the VIP state.
	 * @return the locality.
	 * @since Jun 26, 2012
	 * @version Aug 29, 2012
	 */
	private Locality createLocality(final long localityId, final long stateId, final VipState vipState) {
		final Locality locality = createMock("Locality", Locality.class);
		EasyMock.expect(locality.getId()).andReturn(BigInteger.valueOf(localityId));
		EasyMock.expect(locality.getStateId()).andReturn(BigInteger.valueOf(stateId));
		final String localityName = "Locality Name";
		EasyMock.expect(locality.getName()).andReturn(localityName);
		final String localityType = "Locality Type";
		EasyMock.expect(locality.getType()).andReturn(localityType);
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipLocality locality = (VipLocality) object;
				assertEquals("The VIP id is set", localityId, locality.getVipId().longValue());
				assertNotNull("The source is set", locality.getSource());
				assertNotNull("The state is set", locality.getState());
				assertEquals("The locality name is set", localityName, locality.getName());
				assertEquals("The type is set", localityType, locality.getType());
			}
		});
		return locality;
	}

	/**
	 * Creates a non-house address.
	 * 
	 * @author IanBrown
	 * @param stateName
	 *            the name of the state.
	 * @return the non-house address.
	 * @since Jun 27, 2012
	 * @version Aug 10, 2012
	 */
	private DetailAddressType createNonHouseAddress(final String stateName) {
		final DetailAddressType nonHouseAddress = createMock("NonHouseAddress", DetailAddressType.class);
		final String addressDirection = "Address Direction";
		EasyMock.expect(nonHouseAddress.getAddressDirection()).andReturn(addressDirection);
		final String city = "City";
		EasyMock.expect(nonHouseAddress.getCity()).andReturn(city);
		EasyMock.expect(nonHouseAddress.getState()).andReturn(stateName);
		final String houseNumberPrefix = "A";
		EasyMock.expect(nonHouseAddress.getHouseNumberPrefix()).andReturn(houseNumberPrefix);
		final String houseNumberSuffix = "1/2";
		EasyMock.expect(nonHouseAddress.getHouseNumberSuffix()).andReturn(houseNumberSuffix);
		final String streetDirection = "Street Direction";
		EasyMock.expect(nonHouseAddress.getStreetDirection()).andReturn(streetDirection);
		final String streetName = "Street Name";
		EasyMock.expect(nonHouseAddress.getStreetName()).andReturn(streetName);
		final String streetSuffix = "Street Suffix";
		EasyMock.expect(nonHouseAddress.getStreetSuffix()).andReturn(streetSuffix);
		final String zip = "12345";
		EasyMock.expect(nonHouseAddress.getZip()).andReturn(zip);
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipDetailAddress nonHouseAddress = (VipDetailAddress) object;
				assertEquals("The address direction is set", addressDirection, nonHouseAddress.getAddressDirection());
				assertEquals("The address city is set", city, nonHouseAddress.getCity());
				assertSame("The address state is set", stateName, nonHouseAddress.getState());
				assertEquals("The house number prefix is set", houseNumberPrefix, nonHouseAddress.getHouseNumberPrefix());
				assertEquals("The house number suffix is set", houseNumberSuffix, nonHouseAddress.getHouseNumberSuffix());
				assertEquals("The address street direction is set", streetDirection, nonHouseAddress.getStreetDirection());
				assertEquals("The address street name is set", streetName, nonHouseAddress.getStreetName());
				assertEquals("The address street suffix is set", streetSuffix, nonHouseAddress.getStreetSuffix());
				assertEquals("The address ZIP is set", zip, nonHouseAddress.getZip());
			}
		});
		return nonHouseAddress;
	}

	/**
	 * Creates a precinct to be converted.
	 * 
	 * @author IanBrown
	 * @param precinctId
	 *            the precinct identifier.
	 * @param vipPrecinct
	 *            the VIP precinct.
	 * @param localityId
	 *            the locality identifier.
	 * @param electoralDistrictId
	 *            the electoral district identifier.
	 * @param vipElectoralDistrict
	 *            the electoral district (or <code>null</code>).
	 * @return the precinct.
	 * @since Jun 26, 2012
	 * @version Aug 29, 2012
	 */
	@SuppressWarnings("unchecked")
	private Precinct createPrecinct(final long precinctId, final VipPrecinct vipPrecinct, final long localityId,
			final long electoralDistrictId, final VipElectoralDistrict vipElectoralDistrict) {
		final Precinct precinct = createMock("Precinct", Precinct.class);
		EasyMock.expect(precinct.getId()).andReturn(BigInteger.valueOf(precinctId));
		EasyMock.expect(precinct.getLocalityId()).andReturn(BigInteger.valueOf(localityId));
		final String precinctName = "Precinct Name";
		EasyMock.expect(precinct.getName()).andReturn(precinctName);
		final String precinctNumber = "PC1";
		EasyMock.expect(precinct.getNumber()).andReturn(precinctNumber);
		final String ward = "Ward";
		EasyMock.expect(precinct.getWard()).andReturn(ward);
		final List<BigInteger> electoralDistrictIds = Arrays.asList(BigInteger.valueOf(electoralDistrictId));
		EasyMock.expect(precinct.getElectoralDistrictId()).andReturn(electoralDistrictIds);
		if (vipElectoralDistrict == null) {
			getVipDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
				@Override
				public final void makePersistent(final Object object) {
					final VipElectoralDistrict electoralDistrict = (VipElectoralDistrict) object;
					assertEquals("The VIP identifier is set", electoralDistrictId, electoralDistrict.getVipId().longValue());
					assertNotNull("The source is set", electoralDistrict.getSource());
				}
			});
		}

		if (vipPrecinct != null) {
			vipPrecinct.setElectoralDistricts((List<VipElectoralDistrict>) EasyMock.anyObject());
			vipPrecinct.setLocality((VipLocality) EasyMock.anyObject());
			vipPrecinct.setName(precinctName);
			vipPrecinct.setNumber(precinctNumber);
			vipPrecinct.setWard(ward);
		}
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipPrecinct precinct = (VipPrecinct) object;
				if (vipPrecinct == null) {
					assertEquals("The VIP id is set", precinctId, precinct.getVipId().longValue());
					assertNotNull("The source is set", precinct.getSource());
					assertNotNull("The locality is set", precinct.getLocality());
					final List<VipElectoralDistrict> electoralDistricts = precinct.getElectoralDistricts();
					assertEquals("There is one electoral district", 1, electoralDistricts.size());
					final VipElectoralDistrict electoralDistrict = electoralDistricts.iterator().next();
					if (vipElectoralDistrict == null) {
						assertEquals("The electoral district VIP id is set", electoralDistrictId, electoralDistrict.getVipId()
								.longValue());
						assertNotNull("The electoral district source is set", electoralDistrict.getSource());
					} else {
						assertSame("The electoral district is set", vipElectoralDistrict, electoralDistrict);
					}
					assertEquals("The name is set", precinctName, precinct.getName());
					assertEquals("The number is set", precinctNumber, precinct.getNumber());
					assertEquals("The ward is set", ward, precinct.getWard());
				} else {
					assertSame("The VIP precinct is saved", vipPrecinct, precinct);
				}
			}
		});
		return precinct;
	}

	/**
	 * Creates a precinct split to convert.
	 * 
	 * @author IanBrown
	 * @param precinctSplitId
	 *            the precinct split identifier.
	 * @param vipPrecinctSplit
	 *            the VIP precinct split.
	 * @param precinctId
	 *            the precinct identifier.
	 * @param vipPrecinct
	 *            the VIP precinct.
	 * @param electoralDistrictId
	 *            the electoral district identifier.
	 * @param vipElectoralDistrict
	 *            the VIP electoral district.
	 * @return the precinct split.
	 * @since Jun 29, 2012
	 * @version Aug 29, 2012
	 */
	@SuppressWarnings("unchecked")
	private PrecinctSplit createPrecinctSplit(final long precinctSplitId, final VipPrecinctSplit vipPrecinctSplit,
			final long precinctId, final VipPrecinct vipPrecinct, final long electoralDistrictId,
			final VipElectoralDistrict vipElectoralDistrict) {
		final PrecinctSplit precinctSplit = createMock("PrecinctSplit", PrecinctSplit.class);
		EasyMock.expect(precinctSplit.getId()).andReturn(BigInteger.valueOf(precinctSplitId));
		EasyMock.expect(precinctSplit.getPrecinctId()).andReturn(BigInteger.valueOf(precinctId));
		final String precinctSplitName = "Precinct Split Name";
		EasyMock.expect(precinctSplit.getName()).andReturn(precinctSplitName);
		final List<BigInteger> electoralDistrictIds = Arrays.asList(BigInteger.valueOf(electoralDistrictId));
		EasyMock.expect(precinctSplit.getElectoralDistrictId()).andReturn(electoralDistrictIds);
		if (vipPrecinctSplit != null) {
			vipPrecinctSplit.setName(precinctSplitName);
			if (vipPrecinct == null) {
				vipPrecinctSplit.setPrecinct((VipPrecinct) EasyMock.anyObject());
			} else {
				vipPrecinctSplit.setPrecinct(vipPrecinct);
			}
			vipPrecinctSplit.setElectoralDistricts((List<VipElectoralDistrict>) EasyMock.anyObject());
		}
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipPrecinctSplit precinctSplit = (VipPrecinctSplit) object;
				if (vipPrecinctSplit == null) {
					assertEquals("The precinct split VIP id is set", precinctSplitId, precinctSplit.getVipId().longValue());
					assertNotNull("The precinct split source is set", precinctSplit.getSource());
					if (vipPrecinct == null) {
						assertNotNull("The precinct split precinct is set", precinctSplit.getPrecinct());
					} else {
						assertSame("The precinct split precinct is the VIP precinct", vipPrecinct, precinctSplit.getPrecinct());
					}
					assertEquals("The precinct split name is set", precinctSplitName, precinctSplit.getName());
					final List<VipElectoralDistrict> electoralDistricts = precinctSplit.getElectoralDistricts();
					assertEquals("There is one electoral district for the precinct split", 1, electoralDistricts.size());
					if (vipElectoralDistrict != null) {
						assertTrue("The VIP electoral district is in the precinct split",
								electoralDistricts.contains(vipElectoralDistrict));
					}
				} else {
					assertSame("The VIP precinct split is saved", vipPrecinctSplit, precinctSplit);
				}
			}
		});
		return precinctSplit;
	}

	/**
	 * Creates a referendum to convert.
	 * 
	 * @author IanBrown
	 * @param referendumId
	 *            the referendum identifier.
	 * @param vipReferendum
	 *            the VIP referendum.
	 * @param ballotResponseId
	 *            the ballot response identifier.
	 * @param vipBallotResponse
	 *            the VIP ballot response.
	 * @param vipReferendumDetail
	 *            the VIP referendum detail.
	 * @return the referendum.
	 * @since Jun 27, 2012
	 * @version Sep 20, 2012
	 */
	@SuppressWarnings("unchecked")
	private Referendum createReferendum(final long referendumId, final VipReferendum vipReferendum, final long ballotResponseId,
			final VipBallotResponse vipBallotResponse, final VipReferendumDetail vipReferendumDetail) {
		final Referendum referendum = createMock("Referendum", Referendum.class);
		EasyMock.expect(referendum.getId()).andReturn(BigInteger.valueOf(referendumId));
		if (vipReferendum != null) {
			EasyMock.expect(vipReferendum.getVipId()).andReturn(referendumId).anyTimes();
		}
		final ObjectFactory objectFactory = new ObjectFactory();
		final List<JAXBElement<?>> elements = new LinkedList<JAXBElement<?>>();
		EasyMock.expect(referendum.getTitleOrSubtitleOrBrief()).andReturn(elements);
		final String brief = "Brief";
		elements.add(objectFactory.createVipObjectReferendumBrief(brief));
		final String subTitle = "Sub Title";
		elements.add(objectFactory.createVipObjectReferendumSubtitle(subTitle));
		final String text = "Text";
		elements.add(objectFactory.createVipObjectReferendumText(text));
		final String title = "Title";
		elements.add(objectFactory.createVipObjectReferendumTitle(title));
		if (vipReferendum != null) {
			vipReferendum.setBrief(brief);
			vipReferendum.setSubTitle(subTitle);
			vipReferendum.setText(text);
			vipReferendum.setTitle(title);
			vipReferendum.setBallotResponses((List<VipReferendumBallotResponse>) EasyMock.anyObject());
		}

		final Referendum.BallotResponseId ballotResponseIdO = createMock("BallotResponseId", Referendum.BallotResponseId.class);
		elements.add(objectFactory.createVipObjectReferendumBallotResponseId(ballotResponseIdO));
		final int sortOrder = 1;
		EasyMock.expect(ballotResponseIdO.getSortOrder()).andReturn(BigInteger.valueOf(sortOrder));
		EasyMock.expect(ballotResponseIdO.getValue()).andReturn(BigInteger.valueOf(ballotResponseId));
		if (vipBallotResponse == null) {
			getVipDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
				@Override
				public final void makePersistent(final Object object) {
					final VipBallotResponse ballotResponse = (VipBallotResponse) object;
					assertEquals("The ballot response VIP id is set", ballotResponseId, ballotResponse.getVipId().longValue());
					assertNotNull("The ballot response source is set", ballotResponse.getSource());
				}
			});
		}

		final String conStatement = "Con Statement";
		elements.add(objectFactory.createVipObjectReferendumConStatement(conStatement));
		final String effectOfAbstain = "Effect of Abstain";
		elements.add(objectFactory.createVipObjectReferendumEffectOfAbstain(effectOfAbstain));
		final String passageThreshold = "50%";
		elements.add(objectFactory.createVipObjectReferendumPassageThreshold(passageThreshold));
		final String proStatement = "Pro Statement";
		elements.add(objectFactory.createVipObjectReferendumProStatement(proStatement));
		if (vipReferendumDetail != null) {
			vipReferendumDetail.setConStatement(conStatement);
			vipReferendumDetail.setEffectOfAbstain(effectOfAbstain);
			vipReferendumDetail.setPassageThreshold(passageThreshold);
			vipReferendumDetail.setProStatement(proStatement);
		}

		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipReferendum referendum = (VipReferendum) object;
				if (vipReferendum == null) {
					assertEquals("The referendum VIP ID is set", referendumId, referendum.getVipId().longValue());
					assertNotNull("The referendum source is set", referendum.getSource());
					assertEquals("The referendum brief is set", brief, referendum.getBrief());
					assertEquals("The referendum sub-title is set", subTitle, referendum.getSubTitle());
					assertEquals("The referendum text is set", text, referendum.getText());
					assertEquals("The referendum title is set", title, referendum.getTitle());
					final List<VipReferendumBallotResponse> ballotResponses = referendum.getBallotResponses();
					assertEquals("There is one ballot response", 1, ballotResponses.size());
					if (vipBallotResponse != null) {
						assertTrue("The VIP ballot response is in the referendum", ballotResponses.contains(vipBallotResponse));
					}
				} else {
					assertSame("The VIP referendum is saved", vipReferendum, referendum);
				}
			}
		});

		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipReferendumDetail referendumDetail = (VipReferendumDetail) object;
				if (vipReferendumDetail == null) {
					assertNotNull("The referendum is set", referendumDetail.getReferendum());
					assertEquals("The con statement is set", conStatement, referendumDetail.getConStatement());
					assertEquals("The effect of abstain is set", effectOfAbstain, referendumDetail.getEffectOfAbstain());
					assertEquals("The passage threshold is set", passageThreshold, referendumDetail.getPassageThreshold());
					assertEquals("The pro statement is set", proStatement, referendumDetail.getProStatement());
				}
			}
		});

		return referendum;
	}

	/**
	 * Creates a simple address type for the specified string.
	 * 
	 * @author IanBrown
	 * @param string
	 *            the string describing the address type.
	 * @return the simple address type.
	 * @since Aug 16, 2012
	 * @version Aug 16, 2012
	 */
	private SimpleAddressType createSimpleAddressType(final String string) {
		final SimpleAddressType simpleAddressType = createMock(string, SimpleAddressType.class);
		EasyMock.expect(simpleAddressType.getLine1()).andReturn("Line 1").anyTimes();
		EasyMock.expect(simpleAddressType.getLine2()).andReturn("Line 2").anyTimes();
		EasyMock.expect(simpleAddressType.getCity()).andReturn("City").anyTimes();
		EasyMock.expect(simpleAddressType.getState()).andReturn("ST").anyTimes();
		EasyMock.expect(simpleAddressType.getZip()).andReturn("28460").anyTimes();
		return simpleAddressType;
	}

	/**
	 * Creates a source object to convert.
	 * 
	 * @author IanBrown
	 * @param sourceId
	 *            the source specific identifier.
	 * @param vipId
	 *            the VIP identifier.
	 * @param sourceName
	 *            the name of the source.
	 * @param saveSource
	 *            should the source be saved?
	 * @return the source.
	 * @since Jun 26, 2012
	 * @version Aug 21, 2012
	 */
	private Source createSource(final long sourceId, final long vipId, final String sourceName, final boolean saveSource) {
		final Source source = createMock("Source", Source.class);
		EasyMock.expect(source.getId()).andReturn(BigInteger.valueOf(sourceId));
		EasyMock.expect(source.getVipId()).andReturn(BigInteger.valueOf(vipId));
		EasyMock.expect(source.getName()).andReturn(sourceName);
		final XMLGregorianCalendar dateTime = createMock("dateTime", XMLGregorianCalendar.class);
		EasyMock.expect(source.getDatetime()).andReturn(dateTime).atLeastOnce();
		final GregorianCalendar gregorianCalendar = new GregorianCalendar();
		EasyMock.expect(dateTime.toGregorianCalendar()).andReturn(gregorianCalendar);
		if (saveSource) {
			getVipDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
				@Override
				public final void makePersistent(final Object object) {
					final VipSource source = (VipSource) object;
					assertEquals("The VIP ID is set", vipId, source.getVipId().longValue());
					assertEquals("The source ID is set", sourceId, source.getSourceId().longValue());
					assertEquals("The name is set", sourceName, source.getName());
					assertEquals("The date/time is set", gregorianCalendar.getTime(), source.getDateTime());
					assertFalse("The source is not complete", source.isComplete());
				}
			});
		}
		return source;
	}

	/**
	 * Creates a state object to convert.
	 * 
	 * @author IanBrown
	 * @param stateId
	 *            the state identifier.
	 * @param stateName
	 *            the name of the state.
	 * @param vipState
	 *            the VIP state object.
	 * @return the state object.
	 * @since Jun 26, 2012
	 * @version Sep 18, 2012
	 */
	private State createState(final long stateId, final String stateName, final VipState vipState) {
		final State state = createMock("State", State.class);
		EasyMock.expect(state.getId()).andReturn(BigInteger.valueOf(stateId));
		EasyMock.expect(state.getName()).andReturn(stateName);
		if (vipState != null) {
			vipState.setName(stateName);
		}
		return state;
	}

	/**
	 * Creates a street segment to convert.
	 * 
	 * @author IanBrown
	 * @param streetSegmentId
	 *            the street segment identifier.
	 * @param stateName
	 *            the name of the state.
	 * @param precinctId
	 *            the precinct identifier.
	 * @param vipPrecinct
	 *            the VIP precinct.
	 * @param precinctSplitId
	 *            the precinct split identifier.
	 * @param vipPrecinctSplit
	 *            the VIP precinct split.
	 * @return the street segment.
	 * @since Jun 27, 2012
	 * @version Aug 29, 2012
	 */
	private StreetSegment createStreetSegment(final long streetSegmentId, final String stateName, final long precinctId,
			final VipPrecinct vipPrecinct, final Long precinctSplitId, final VipPrecinctSplit vipPrecinctSplit) {
		final StreetSegment streetSegment = createMock("StreetSegment", StreetSegment.class);
		EasyMock.expect(streetSegment.getId()).andReturn(BigInteger.valueOf(streetSegmentId));
		final int endHouseNumber = 3000;
		EasyMock.expect(streetSegment.getEndHouseNumber()).andReturn(BigInteger.valueOf(endHouseNumber));
		final DetailAddressType nonHouseAddress = createNonHouseAddress(stateName);
		EasyMock.expect(streetSegment.getNonHouseAddress()).andReturn(nonHouseAddress);
		final String oddEvenBoth = "OEB";
		EasyMock.expect(streetSegment.getOddEvenBoth()).andReturn(oddEvenBoth);
		EasyMock.expect(streetSegment.getPrecinctId()).andReturn(BigInteger.valueOf(precinctId));
		if (precinctSplitId == null) {
			EasyMock.expect(streetSegment.getPrecinctSplitId()).andReturn(null);
		} else {
			EasyMock.expect(streetSegment.getPrecinctSplitId()).andReturn(BigInteger.valueOf(precinctSplitId.longValue()));
		}
		final int startHouseNumber = 1;
		EasyMock.expect(streetSegment.getStartHouseNumber()).andReturn(BigInteger.valueOf(startHouseNumber));
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipStreetSegment streetSegment = (VipStreetSegment) object;
				assertEquals("The street segment VIP ID is set", streetSegmentId, streetSegment.getVipId().longValue());
				assertNotNull("The street segment source is set", streetSegment.getSource());
				assertEquals("The street segment end house number is set", endHouseNumber, streetSegment.getEndHouseNumber());
				assertNotNull("The street segment non-house address is set", streetSegment.getNonHouseAddress());
				assertEquals("The street segment side is set", oddEvenBoth, streetSegment.getOddEvenBoth());
				if (vipPrecinct == null) {
					assertNotNull("The street segment precinct is set", streetSegment.getPrecinct());
				} else {
					assertSame("The street segment precinct is set", vipPrecinct, streetSegment.getPrecinct());
				}
				if (precinctSplitId != null) {
					if (vipPrecinctSplit == null) {
						assertNotNull("The street segment precinct split is set", streetSegment.getPrecinctSplit());
					} else {
						assertSame("The street segment precinct split is set", vipPrecinctSplit, streetSegment.getPrecinctSplit());
					}
				}
				assertEquals("The street segment start house number is set", startHouseNumber, streetSegment.getStartHouseNumber());
			}
		});
		return streetSegment;
	}

	/**
	 * Creates a VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipService createVipService() {
		return new VipService();
	}

	/**
	 * Gets the VIP DAO.
	 * 
	 * @author IanBrown
	 * @return the VIP DAO.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipDAO getVipDAO() {
		return vipDAO;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Sets up to ensure that the source is completed.
	 * 
	 * @author IanBrown
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private void setUpSourceComplete() {
		getVipDAO().makePersistent(EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new VipDAO() {
			@Override
			public final void makePersistent(final Object object) {
				final VipSource source = (VipSource) object;
				assertTrue("The source is complete", source.isComplete());
			}
		});
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
	private void setVipDAO(final VipDAO vipDAO) {
		this.vipDAO = vipDAO;
	}

	/**
	 * Sets the VIP service.
	 * 
	 * @author IanBrown
	 * @param vipService
	 *            the VIP service to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private void setVipService(final VipService vipService) {
		this.vipService = vipService;
	}

}
