/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

import com.bearcode.ovf.model.eod.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.DAO.CorrectionsDAO;
import com.bearcode.ovf.DAO.LocalOfficialDAO;
import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.DAO.VotingRegionDAO;
import com.bearcode.ovf.forms.AdminCorrectionsListForm;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.County;
import com.bearcode.ovf.model.common.Municipality;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;

/**
 * Test for {@link LocalOfficialService}.
 * 
 * @author Ian
 * 
 */
public final class LocalOfficialServiceTest extends EasyMockSupport {

	private CorrectionsDAO correctionsDAO;

	private LocalOfficialDAO localOfficialDAO;

	private LocalOfficialService localOfficialService;

	private VotingRegionDAO regionDAO;

	private StateDAO stateDAO;

	/**
	 * Sets up the local official service.
	 */
	@Before
	public final void setup() {
		
		// Override a couple of methods that are sometimes called by other methods. This allows us to optionally mock these methods
		// in tests on the calling method. The methods can still be tested independently.
		localOfficialService = new LocalOfficialService() {
			private final Map<String, Pair<Object[], Object>> methodsToMock = null;
			
			@Override
            public void saveLocalOfficial(final LocalOfficial leo) {
				if (methodsToMock != null) {
	            	final Pair<Object[], Object> expectedParams = methodsToMock.get("saveLocalOfficial");
	            	if (expectedParams != null) {
	            		final Object[] expectedArgs = expectedParams.getKey();
	            		assertEquals("The correct number of arguments were provided for saveLocalOfficial", expectedArgs.length, 1);
	            		assertEquals("The LEO is correct", expectedArgs[0], leo);
	            		return;
	            	}
				}
				
				super.saveLocalOfficial(leo);			
			}
			
			@Override
            public void saveVotingRegion(final VotingRegion region) {
				if (methodsToMock != null) {
	            	final Pair<Object[], Object> expectedParams = methodsToMock.get("saveVotingRegion");
	            	if (expectedParams != null) {
	            		final Object[] expectedArgs = expectedParams.getKey();
	            		assertEquals("The correct number of arguments were provided for saveVotingRegion", expectedArgs.length, 1);
	            		assertEquals("The voting region is correct", expectedArgs[0], region);
	            		return;
	            	}
				}
				
				super.saveVotingRegion(region);
			}
		};
		regionDAO = createMock("RegionDAO", VotingRegionDAO.class);
		ReflectionTestUtils.setField(localOfficialService, "regionDAO",
		        regionDAO);
		stateDAO = createMock("StateDAO", StateDAO.class);
		ReflectionTestUtils
		        .setField(localOfficialService, "stateDAO", stateDAO);
		localOfficialDAO = createMock("LocalOfficialDAO",
		        LocalOfficialDAO.class);
		ReflectionTestUtils.setField(localOfficialService, "localOfficialDAO",
		        localOfficialDAO);
		correctionsDAO = createMock("CorrectionsDAO", CorrectionsDAO.class);
		ReflectionTestUtils.setField(localOfficialService, "correctionsDAO",
		        correctionsDAO);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#checkLeoGotUpdated()}
	 * .
	 */
	@Test
	public final void testCheckLeoGotUpdated() {
		final boolean leoGotUpdated = true;
		EasyMock.expect(localOfficialDAO.checkLeoGotChanged()).andReturn(leoGotUpdated);
		replayAll();
		
		final boolean actualLeoGotUpdated = localOfficialService.checkLeoGotUpdated();
		
		assertEquals("The LEO got updated", leoGotUpdated, actualLeoGotUpdated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#countStateWithNoExcuse()}
	 * .
	 */
	@Test
	public final void testCountStateWithNoExcuse() {
		final long count = 24l;
		EasyMock.expect(localOfficialDAO.countStateWithNoExcuse()).andReturn(count);
		replayAll();
		
		final long actualCount = localOfficialService.countStateWithNoExcuse();
		
		assertEquals("The number of states with no excuse is returned", count, actualCount);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#deleteAllRegionsOfState(com.bearcode.ovf.model.common.State)}
	 * .
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testDeleteAllRegionsOfState() {
		final State state = createMock("State", State.class);
		final List<VotingRegion> regions = createMock("Regions", List.class);
		EasyMock.expect(regionDAO.getRegionsForState(state)).andReturn(regions);
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		final Collection<LocalOfficial> localOfficials = Arrays.asList(leo);
		EasyMock.expect(localOfficialDAO.findLeoByState(state)).andReturn(localOfficials);
		final Address mailing = createMock("Mailing", Address.class);
		EasyMock.expect(leo.getMailing()).andReturn(mailing).atLeastOnce();
		final Address physical = createMock("Physical", Address.class);;
		EasyMock.expect(leo.getPhysical()).andReturn(physical).atLeastOnce();
		final Collection<CorrectionsLeo> corrections = createMock("Corrections", Collection.class);
		EasyMock.expect(correctionsDAO.findForRegions(localOfficials)).andReturn(corrections);
		correctionsDAO.makeAllTransient(corrections);
		localOfficialDAO.makeAllTransient(Arrays.asList(mailing, physical));
		localOfficialDAO.makeAllTransient(localOfficials);
		regionDAO.makeAllTransient(regions);
		replayAll();
		
		localOfficialService.deleteAllRegionsOfState(state);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#deleteElection(com.bearcode.ovf.model.eod.Election)}
	 * .
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
	public final void testDeleteElection() {
		final Election election = createMock("Election", Election.class);
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(election.getStateInfo()).andReturn(svid);
		svid.setUpdated(EasyMock.anyObject(Date.class));
		final Collection elections = new ArrayList<Election>(Arrays.asList(election));
		EasyMock.expect(svid.getElections()).andReturn(elections);
		localOfficialDAO.makeTransient(election);
		localOfficialDAO.makePersistent(svid);
		replayAll();

		localOfficialService.deleteElection(election);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findAll()}.
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindAll() {
		final Collection all = createMock("All", Collection.class);
		EasyMock.expect(localOfficialDAO.findAll()).andReturn(all);
		replayAll();

		final Collection actualAll = localOfficialService.findAll();

		assertEquals("The collection of all local officials is returned", all,
		        actualAll);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findAllElections()}.
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testFindAllElections() {
		final Collection<Election> allElections = createMock("Elections", Collection.class);
		EasyMock.expect(localOfficialDAO.findAllElections()).andReturn(allElections);
		replayAll();
		
		final Collection<Election> actualAllElections = localOfficialService.findAllElections();
		
		assertEquals("The elections collection is returned", allElections, actualAllElections);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findAllStateVotingLaws()}
	 * .
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testFindAllStateVotingLaws() {
		final Collection<StateVotingLaws> allStateVotingLaws = createMock("AllStateVotingLaws", Collection.class);
		EasyMock.expect(localOfficialDAO.findAllStateVotingLaws()).andReturn(allStateVotingLaws);
		replayAll();
		
		final Collection<StateVotingLaws> actualAllStateVotingLaws = localOfficialService.findAllStateVotingLaws();
		
		assertEquals("The collection of all state voting laws is returned", allStateVotingLaws, actualAllStateVotingLaws);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findById(long)}.
	 */
	@Test
	public final void testFindById() {
		final long id = 928l;
		final LocalOfficial localOfficial = createMock("LocalOfficial",
		        LocalOfficial.class);
		EasyMock.expect(localOfficialDAO.getById(id)).andReturn(localOfficial);
		localOfficial.sortAdditionalAddresses();
		replayAll();

		final LocalOfficial actualLocalOfficial = localOfficialService
		        .findById(id);

		assertEquals("The local official is returned", localOfficial,
		        actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findCorrections(com.bearcode.ovf.forms.AdminCorrectionsListForm)}
	 * .
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindCorrections() {
		final AdminCorrectionsListForm form = createMock(
		        "AdminCorrectionsListForm", AdminCorrectionsListForm.class);
		final int status = 10;
		EasyMock.expect(form.getStatus()).andReturn(status);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(form.createPagingInfo()).andReturn(pagingInfo);
		final Collection corrections = createMock("Corrections",
		        Collection.class);
		EasyMock.expect(
		        correctionsDAO.findCorrectionByStatus(status, pagingInfo))
		        .andReturn(corrections);
		replayAll();

		final Collection actualCorrections = localOfficialService
		        .findCorrections(form);

		assertEquals("The corrections are returned", corrections,
		        actualCorrections);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findCorrectionsById(long)}
	 * .
	 */
	@Test
	public final void testFindCorrectionsById() {
		final long correctionId = 8782l;
		final CorrectionsLeo corrections = createMock("Corrections", CorrectionsLeo.class);
		EasyMock.expect(correctionsDAO.getById(correctionId)).andReturn(corrections);
		replayAll();

		final CorrectionsLeo actualCorrections = localOfficialService.findCorrectionsById(correctionId);
		
		assertEquals("The corrections are returned", corrections, actualCorrections);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findElection(long)}.
	 */
	@Test
	public final void testFindElection() {
		final long electionId = 7612l;
		final Election election = createMock("Election", Election.class);
		EasyMock.expect(localOfficialDAO.findElection(electionId)).andReturn(election);
		replayAll();
		
		final Election actualElection = localOfficialService.findElection(electionId);
		
		assertEquals("The election is returned", election, actualElection);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForRegion(long)}
	 * .
	 */
	@Test
	public final void testFindForRegionLong() {
		final long regionId = 87l;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(regionDAO.getById(regionId)).andReturn(region);
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(localOfficialDAO.findLeoByRegion(region)).andReturn(leo);
		leo.sortAdditionalAddresses();
		replayAll();
		
		final LocalOfficial actualLeo = localOfficialService.findForRegion(regionId);
		
		assertEquals("The LEO is returned", leo, actualLeo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForRegion(com.bearcode.ovf.model.common.VotingRegion)}
	 * .
	 */
	@Test
	public final void testFindForRegionVotingRegion() {
		final VotingRegion region = createMock("VotingRegion", VotingRegion.class);
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(localOfficialDAO.findLeoByRegion(region)).andReturn(leo);
		leo.sortAdditionalAddresses();
		replayAll();
		
		final LocalOfficial actualLeo = localOfficialService.findForRegion(region);
		
		assertEquals("The leo is returned", leo, actualLeo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForSearch(java.util.List, java.util.List)}
	 * .
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
	public final void testFindForSearchListOfStringListOfString() {
		final List<String> params = createMock("Params", List.class);
		final List<String> states = createMock("States", List.class);
		final Collection localOfficials = createMock("LocalOfficials", Collection.class);
		EasyMock.expect(localOfficialDAO.search(params, states)).andReturn(localOfficials);
		replayAll();
		
		final Collection actualLocalOfficials = localOfficialService.findForSearch(params, states);
		
		assertEquals("The collection of local officials is returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForSearch(java.util.List, java.util.List, com.bearcode.commons.DAO.PagingInfo)}
	 * .
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testFindForSearchListOfStringListOfStringPagingInfo() {
		final List<String> params = createMock("Params", List.class);
		final List<String> states = createMock("States", List.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		final Collection localOfficials = createMock("LocalOfficials", Collection.class);
		EasyMock.expect(localOfficialDAO.search(params, states, pagingInfo)).andReturn(localOfficials);
		replayAll();

		final Collection actualLocalOfficials = localOfficialService.findForSearch(params, states, pagingInfo);

		assertEquals("The local officials collection is returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForState(long)}.
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testFindForStateLong() {
		final long stateId = 2l;
		final State state = createMock("State", State.class);
		EasyMock.expect(stateDAO.getById(stateId)).andReturn(state);
		final Collection<LocalOfficial> localOfficials = createMock("LocalOfficials", Collection.class);
		EasyMock.expect(localOfficialDAO.findLeoByState(state)).andReturn(localOfficials);
		replayAll();
		
		final Collection<LocalOfficial> actualLocalOfficials = localOfficialService.findForState(stateId);
		
		assertEquals("The collection of local officials is returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForState(long, com.bearcode.ovf.forms.CommonFormObject)}
	 * .
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testFindForStateLongCommonFormObject() {
		final long stateId = 87126l;
		final CommonFormObject form = createMock("Form", CommonFormObject.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(form.createPagingInfo()).andReturn(pagingInfo);
		final Collection<LocalOfficial> localOfficials = createMock("LocalOfficials", Collection.class);
		EasyMock.expect(localOfficialDAO.findLeoByState(stateId, pagingInfo)).andReturn(localOfficials);
		replayAll();
		
		final Collection<LocalOfficial> actualLocalOfficials = localOfficialService.findForState(stateId, form);
		
		assertEquals("The collection of local officials is returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForState(long, com.bearcode.ovf.forms.CommonFormObject, java.lang.Long)}
	 * .
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testFindForStateLongCommonFormObjectLong() {
		final long stateId = 23l;
		final CommonFormObject form = createMock("Form", CommonFormObject.class);
		final Long lookFor = 2l;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(regionDAO.getById(lookFor)).andReturn(region);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(form.createPagingInfo()).andReturn(pagingInfo);
		final Collection<LocalOfficial> localOfficials = createMock("LocalOfficials", Collection.class);
		EasyMock.expect(localOfficialDAO.findLeoByState(stateId, pagingInfo, region)).andReturn(localOfficials);
		EasyMock.expect(form.getPagingInfo()).andReturn(pagingInfo).atLeastOnce();
		final int firstResult = 1;
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(firstResult).atLeastOnce();
		final int pageSize = 24;
		EasyMock.expect(form.getPageSize()).andReturn(pageSize).atLeastOnce();
		form.setPage(EasyMock.anyInt());
		replayAll();
		
		final Collection<LocalOfficial> actualLocalOfficials = localOfficialService.findForState(stateId, form, lookFor);
		
		assertEquals("The collection of local officials is returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findForState(com.bearcode.ovf.model.common.State)}
	 * .
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testFindForStateState() {
		final State state = createMock("State", State.class);
		final Collection<LocalOfficial> localOfficials = createMock("LocalOfficials", Collection.class);
		EasyMock.expect(localOfficialDAO.findLeoByState(state)).andReturn(localOfficials);
		replayAll();
		
		final Collection<LocalOfficial> actualLocalOfficials = localOfficialService.findForState(state);
		
		assertEquals("The collection of local officials is returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findStateStatistics(com.bearcode.ovf.forms.CommonFormObject)}
	 * .
	 */
	@SuppressWarnings("unchecked")
    @Test
	public final void testFindStateStatistics() {
		final CommonFormObject form = createMock("Form", CommonFormObject.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(form.createPagingInfo()).andReturn(pagingInfo);
		final Integer total = 10;
		final Integer approved = 8;
		final State state = createMock("State", State.class);
		final Object[] row = new Object[] { "Ignore", total, approved, state };
		final Object[][] rows = new Object[][] { row };
		EasyMock.expect(localOfficialDAO.findStateStatistics(pagingInfo)).andReturn(Arrays.asList(rows));
		replayAll();

		final Collection<Map<String, Object>> actualStateStatistics = localOfficialService.findStateStatistics(form);
		
		final Map<String, Object> stateStatistic = new HashMap<String, Object>();
		stateStatistic.put("total", total);
		stateStatistic.put("approved", approved);
		stateStatistic.put("state", state);
        final Collection<Map<String, Object>> expectedStateStatistics = Arrays.asList(stateStatistic);
		assertEquals("The collection of state statistics is returned", expectedStateStatistics, actualStateStatistics);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findSvid(long)}.
	 */
	@Test
	public final void testFindSvid() {
		final long svidId = 5557l;
		final StateSpecificDirectory svid = createMock("SVID",StateSpecificDirectory.class);
		EasyMock.expect(localOfficialDAO.getSvidById(svidId)).andReturn(svid);
		replayAll();

		final StateSpecificDirectory actualSvid = localOfficialService.findSvid(svidId);

		assertEquals("The SVID is returned", svid, actualSvid);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findSvidForState(long)}
	 * .
	 */
	@Test
	public final void testFindSvidForStateLong() {
		final long stateId = 3l;
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(localOfficialDAO.findSvidForState(stateId)).andReturn(svid);
		replayAll();
		
		final StateSpecificDirectory actualSvid = localOfficialService.findSvidForState(stateId);
		
		assertEquals("The SVID is returned", svid, actualSvid);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findSvidForState(com.bearcode.ovf.model.common.State)}
	 * .
	 */
	@Test
	public final void testFindSvidForStateState() {
		final State state = createMock("State", State.class);
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(localOfficialDAO.findSvidForState(state)).andReturn(svid);
		replayAll();
	
		final StateSpecificDirectory actualSvid = localOfficialService.findSvidForState(state);
		
		assertEquals("The SVID is returned", svid, actualSvid);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findVotingLaws(com.bearcode.ovf.model.common.State)}
	 * .
	 */
	@Test
	public final void testFindVotingLaws() {
		final State state = createMock("State", State.class);
		final StateVotingLaws votingLaws = createMock("VotingLaws", StateVotingLaws.class);
		EasyMock.expect(localOfficialDAO.findVotingLawsByState(state)).andReturn(votingLaws);
		replayAll();
		
		final StateVotingLaws actualVotingLaws = localOfficialService.findVotingLaws(state);
		
		assertEquals("The voting laws are returned", votingLaws, actualVotingLaws);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#findVotingLawsByStateAbbr(java.lang.String)}
	 * .
	 */
	@Test
	public final void testFindVotingLawsByStateAbbr() {
		final String stateAbbr = "SA";
		final StateVotingLaws votingLaws = createMock("votingLaws", StateVotingLaws.class);
		EasyMock.expect(localOfficialDAO.findVotingLawsByStateAbbreviation(stateAbbr)).andReturn(votingLaws);
		replayAll();
		
		final StateVotingLaws actualVotingLaws = localOfficialService.findVotingLawsByStateAbbr(stateAbbr);
		
		assertEquals("The voting laws are returned", votingLaws, actualVotingLaws);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#initializeLeo(com.bearcode.ovf.model.eod.LocalOfficial)}
	 * .
	 */
	@Test
	public final void testInitializeLeo() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		localOfficialDAO.initialize(leo);
		replayAll();
		
		localOfficialService.initializeLeo(leo);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#makeCorrections(com.bearcode.ovf.model.eod.CorrectionsLeo)}
	 * .
	 */
    @Test
	public final void testMakeCorrectionsCorrectionsLeo() {
		final CorrectionsLeo command = createMock("Command", CorrectionsLeo.class);
		final Officer officer = createMock("Officer", Officer.class);
		final List<Officer> officers = new ArrayList<Officer>(Arrays.asList(officer));
		EasyMock.expect(command.getOfficers()).andReturn(officers);
		EasyMock.expect(officer.isEmpty()).andReturn(true);
		EasyMock.expect( command.getAdditionalAddresses() ).andReturn( Collections.<CorrectionAdditionalAddress>emptyList() );
		correctionsDAO.makePersistent(command);
		replayAll();
		
		localOfficialService.makeCorrections(command);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#makeCorrections(com.bearcode.ovf.model.eod.CorrectionsSvid)}
	 * .
	 */
	@Test
	public final void testMakeCorrectionsCorrectionsSvid() {
		final CorrectionsSvid command = createMock("Command", CorrectionsSvid.class);
		localOfficialDAO.makePersistent(command);
		replayAll();
		
		localOfficialService.makeCorrections(command);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveAllLocalOfficial(java.util.Collection)}
	 * for the case where the local official to be saved already exists.
	 */
	@Test
	public final void testSaveAllLocalOfficial_existingLeo() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		final Collection<LocalOfficial> eod = Arrays.asList(leo);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(leo.getRegion()).andReturn(region).atLeastOnce();
		region.makeConsistent();
		final VotingRegion existingRegion = createMock("ExistingRegion", VotingRegion.class);
		EasyMock.expect(regionDAO.getRegionByName(region)).andReturn(existingRegion);
		final State state = createMock("State", State.class);
		EasyMock.expect(existingRegion.getState()).andReturn(state);
		final LocalOfficial existingLeo = createMock("ExistingLEO", LocalOfficial.class);
		EasyMock.expect(localOfficialDAO.findLeoByRegion(existingRegion)).andReturn(existingLeo);
		EasyMock.expect(existingRegion.updateFrom(region)).andReturn(existingRegion);
		EasyMock.expect(existingLeo.updateFrom(leo)).andReturn(existingLeo);
		final Map<String, Pair<Object[], Object>> methodsToMock = new HashMap<String, Pair<Object[], Object>>();
		methodsToMock.put("saveVotingRegion", new ImmutablePair<Object[], Object>(new Object[]{existingRegion}, null));
		existingLeo.setRegion( existingRegion );
		methodsToMock.put("saveLocalOfficial", new ImmutablePair<Object[], Object>(new Object[]{existingLeo}, null));
		ReflectionTestUtils.setField(localOfficialService, "methodsToMock", methodsToMock);
		regionDAO.cleanCountiesAndMunicipalities(new HashSet<State>(Arrays.asList(state)));
		replayAll();
		
		localOfficialService.saveAllLocalOfficial(eod);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveAllLocalOfficial(java.util.Collection)}
	 * for the case where the local official to be saved does not already exist.
	 */
	@Test
	public final void testSaveAllLocalOfficial_newLeo() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		final Collection<LocalOfficial> eod = Arrays.asList(leo);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(leo.getRegion()).andReturn(region).atLeastOnce();
		region.makeConsistent();
		final State state = createMock("State", State.class);
		EasyMock.expect(region.getState()).andReturn(state).atLeastOnce();
		EasyMock.expect(regionDAO.getRegionByName(region)).andReturn(null);
		regionDAO.cleanCountiesAndMunicipalities(new HashSet<State>(Arrays.asList(state)));
		final Map<String, Pair<Object[], Object>> methodsToMock = new HashMap<String, Pair<Object[], Object>>();
		methodsToMock.put("saveVotingRegion", new ImmutablePair<Object[], Object>(new Object[]{region}, null));
		leo.setRegion(region);
		methodsToMock.put("saveLocalOfficial", new ImmutablePair<Object[], Object>(new Object[]{leo}, null));
		ReflectionTestUtils.setField(localOfficialService, "methodsToMock", methodsToMock);
		replayAll();
		
		localOfficialService.saveAllLocalOfficial(eod);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveCorrections(com.bearcode.ovf.model.eod.CorrectionsLeo)}
	 * .
	 */
	@Test
	public final void testSaveCorrections() {
		final CorrectionsLeo correctionsLeo = createMock("CorrectionsLeo", CorrectionsLeo.class);
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(correctionsLeo.getCorrectionFor()).andReturn(leo).atLeastOnce();
		leo.setUpdated(EasyMock.anyObject(Date.class));
		EasyMock.expectLastCall().atLeastOnce();
		final Officer officer = createMock("Officer", Officer.class);
		final List<Officer> officers = new ArrayList<Officer>(Arrays.asList(officer));
		EasyMock.expect(leo.getOfficers()).andReturn(officers);
		EasyMock.expect( officer.isEmpty() ).andReturn( true );
		EasyMock.expect( officer.getId() ).andReturn( 0l ).anyTimes();
		EasyMock.expect( localOfficialDAO.findAdditionalAddressTypes() ).andReturn( Collections.<AdditionalAddressType>emptyList() ).anyTimes();
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).anyTimes();
		localOfficialDAO.makePersistent( leo );
		correctionsLeo.setStatus(EasyMock.anyInt());
		correctionsDAO.makePersistent(correctionsLeo);
		replayAll();
		
		localOfficialService.saveCorrections(correctionsLeo);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveElection(com.bearcode.ovf.model.eod.Election)}
	 * .
	 */
	@Test
	public final void testSaveElection() {
		final Election election = createMock("Election", Election.class);
		localOfficialDAO.makePersistent(election);
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(election.getStateInfo()).andReturn(svid);
		svid.setUpdated(EasyMock.anyObject(Date.class));
		localOfficialDAO.makePersistent(svid);
		replayAll();
		
		localOfficialService.saveElection(election);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveLocalOfficial(com.bearcode.ovf.model.eod.LocalOfficial)}
	 * for a LEO with an empty officer.
	 */
	@Test
	public final void testSaveLocalOfficial_emptyOfficer() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		leo.setUpdated(EasyMock.anyObject(Date.class));
		final Officer officer = createMock("Officer", Officer.class);
		final List<Officer> officers = new ArrayList<Officer>(Arrays.asList(officer));
		EasyMock.expect(leo.getOfficers()).andReturn(officers);
		EasyMock.expect(officer.isEmpty()).andReturn(true);
		EasyMock.expect( officer.getId() ).andReturn( 0l ).anyTimes();
		EasyMock.expect( localOfficialDAO.findAdditionalAddressTypes() ).andReturn( Collections.<AdditionalAddressType>emptyList() ).anyTimes();
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).anyTimes();
		localOfficialDAO.makePersistent(leo);
		replayAll();
		
		localOfficialService.saveLocalOfficial(leo);
		
		assertTrue("There are no officers left", officers.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveLocalOfficial(com.bearcode.ovf.model.eod.LocalOfficial)}
	 * for a LEO without any officers.
	 */
	@Test
	public final void testSaveLocalOfficial_noOfficers() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		leo.setUpdated(EasyMock.anyObject(Date.class));
		final List<Officer> officers = new ArrayList<Officer>();
		EasyMock.expect(leo.getOfficers()).andReturn(officers);
		EasyMock.expect( localOfficialDAO.findAdditionalAddressTypes() ).andReturn( Collections.<AdditionalAddressType>emptyList() ).anyTimes();
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).anyTimes();
		localOfficialDAO.makePersistent(leo);
		replayAll();
		
		localOfficialService.saveLocalOfficial(leo);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveLocalOfficial(com.bearcode.ovf.model.eod.LocalOfficial)}
	 * for a LEO with an officer with an ID.
	 */
	@Test
	public final void testSaveLocalOfficial_officerWithId() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		leo.setUpdated(EasyMock.anyObject(Date.class));
		final Officer officer = createMock("Officer", Officer.class);
		final List<Officer> officers = new ArrayList<Officer>(Arrays.asList(officer));
		EasyMock.expect(leo.getOfficers()).andReturn(officers);
		EasyMock.expect(officer.isEmpty()).andReturn(false);
		officer.setOrderNumber( 1 );
		EasyMock.expect(officer.getId()).andReturn( 2l );
		EasyMock.expect( localOfficialDAO.findAdditionalAddressTypes() ).andReturn( Collections.<AdditionalAddressType>emptyList() ).anyTimes();
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).anyTimes();
		localOfficialDAO.makePersistent(leo);
		replayAll();
		
		localOfficialService.saveLocalOfficial(leo);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveLocalOfficial(com.bearcode.ovf.model.eod.LocalOfficial)}
	 * for a LEO with an officer without an ID.
	 */
	@Test
	public final void testSaveLocalOfficial_officerWithoutId() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		leo.setUpdated(EasyMock.anyObject(Date.class));
		final Officer officer = createMock("Officer", Officer.class);
		final List<Officer> officers = new ArrayList<Officer>(Arrays.asList(officer));
		EasyMock.expect(leo.getOfficers()).andReturn(officers);
		EasyMock.expect(officer.isEmpty()).andReturn(false);
		officer.setOrderNumber( 1 );
		EasyMock.expect(officer.getId()).andReturn( 0l );
		EasyMock.expect( localOfficialDAO.findAdditionalAddressTypes() ).andReturn( Collections.<AdditionalAddressType>emptyList() ).anyTimes();
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).anyTimes();
		localOfficialDAO.makePersistent(officer);
		localOfficialDAO.makePersistent(leo);
		replayAll();
		
		localOfficialService.saveLocalOfficial(leo);
		
		verifyAll();
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveSvid(com.bearcode.ovf.model.eod.StateSpecificDirectory)}
	 * .
	 */
	@Test
	public final void testSaveSvid() {
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		svid.setUpdated(EasyMock.anyObject(Date.class));
		localOfficialDAO.makePersistent(svid);
		replayAll();
		
		localOfficialService.saveSvid(svid);
		
		verifyAll();
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveVotingLaws(com.bearcode.ovf.model.eod.StateVotingLaws)}
	 * .
	 */
	@Test
	public final void testSaveVotingLaws() {
		final StateVotingLaws votingLaws = createMock("VotingLaws", StateVotingLaws.class);
		localOfficialDAO.makePersistent(votingLaws);
		replayAll();
		
		localOfficialService.saveVotingLaws(votingLaws);

		verifyAll();
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveVotingRegion(VotingRegion)}
	 * for a region w/ a county.
	 */
	@Test
	public final void testSaveVotingRegion_county() {
		final VotingRegion region = createMock("VotingRegion", VotingRegion.class);
		final County county = createMock("County", County.class);
		EasyMock.expect(region.getCounty()).andReturn(county);
		EasyMock.expect(county.getId()).andReturn(null);
		final State state = createMock("State", State.class);
		EasyMock.expect(county.getState()).andReturn(state);
		final String countyName = "County";
		EasyMock.expect(county.getName()).andReturn(countyName);
		EasyMock.expect(regionDAO.findCountyByStateAndName(state, countyName)).andReturn(null);
		regionDAO.makePersistent(county);
		EasyMock.expect(region.getMunicipality()).andReturn(null);
		regionDAO.makePersistent(region);
		replayAll();
		
		localOfficialService.saveVotingRegion(region);
		
		verifyAll();
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveVotingRegion(VotingRegion)}
	 * for a region w/ a county and a municipality that belongs to that county.
	 */
	@Test
	public final void testSaveVotingRegion_countyMunicipality() {
		final VotingRegion region = createMock("VotingRegion", VotingRegion.class);
		final County county = createMock("County", County.class);
		EasyMock.expect(region.getCounty()).andReturn(county);
		final Long countyId = 238992l;
		EasyMock.expect(county.getId()).andReturn(countyId);
		final Municipality municipality = createMock("Municipality", Municipality.class);
		EasyMock.expect(region.getMunicipality()).andReturn(municipality);
		EasyMock.expect(municipality.getCounty()).andReturn(county);
		regionDAO.makePersistent(municipality);
		regionDAO.makePersistent(region);
		replayAll();
		
		localOfficialService.saveVotingRegion(region);
		
		verifyAll();
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveVotingRegion(VotingRegion)}
	 * for a region w/ a county and a new municipality.
	 */
	@Test
	public final void testSaveVotingRegion_countyNewMunicipality() {
		final VotingRegion region = createMock("VotingRegion", VotingRegion.class);
		final County county = createMock("County", County.class);
		EasyMock.expect(region.getCounty()).andReturn(county);
		final Long countyId = 390812l;
		EasyMock.expect(county.getId()).andReturn(countyId);
		final Municipality municipality = createMock("Municipality", Municipality.class);
		EasyMock.expect(region.getMunicipality()).andReturn(municipality);
		EasyMock.expect(municipality.getCounty()).andReturn(null);
		municipality.setCounty(county);
		regionDAO.makePersistent(municipality);
		regionDAO.makePersistent(region);
		replayAll();
		
		localOfficialService.saveVotingRegion(region);
		
		verifyAll();
	}
	
	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveVotingRegion(VotingRegion)}
	 * for a region w/o a municipality or county.
	 */
	@Test
	public final void testSaveVotingRegion_justRegion() {
		final VotingRegion region = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(region.getCounty()).andReturn(null);
		EasyMock.expect(region.getMunicipality()).andReturn(null);
		regionDAO.makePersistent(region);
		replayAll();
		
		localOfficialService.saveVotingRegion(region);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#saveVotingRegion(VotingRegion)}
	 * for a region w/ a municipality.
	 */
	@Test
	public final void testSaveVotingRegion_municipality() {
		final VotingRegion region = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(region.getCounty()).andReturn(null);
		final Municipality municipality = createMock("Municipality", Municipality.class);
		EasyMock.expect(region.getMunicipality()).andReturn(municipality);
		regionDAO.makePersistent(municipality);
		regionDAO.makePersistent(region);
		replayAll();
		
		localOfficialService.saveVotingRegion(region);
		
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.LocalOfficialService#updateDeclinedCorrections(com.bearcode.ovf.model.eod.CorrectionsLeo)}
	 * .
	 */
    @Test
	public final void testUpdateDeclinedCorrections() {
		final CorrectionsLeo correctionsLeo = createMock("CorrectionsLeo", CorrectionsLeo.class);
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(correctionsLeo.getCorrectionFor()).andReturn(leo);
		final Officer officer = createMock("Officer", Officer.class);
		final List<Officer> officers = new ArrayList<Officer>(Arrays.asList(officer));
		EasyMock.expect(leo.getOfficers()).andReturn(officers);
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() );
		EasyMock.expect(officer.isEmpty()).andReturn(true);
		correctionsLeo.setStatus(EasyMock.anyInt());
		correctionsDAO.makePersistent(correctionsLeo);
		replayAll();
		
		localOfficialService.updateDeclinedCorrections(correctionsLeo);
		
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.LocalOfficialService#updateLocalOfficial(LocalOfficial)} for a new voting region.
	 */
	@Test
	public final void testUpdateLocalOfficial_newVotingRegion() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(leo.getRegion()).andReturn(region).atLeastOnce();
		region.makeConsistent();
		EasyMock.expect(regionDAO.getRegionByName(region)).andReturn(null);
		EasyMock.expect(region.getCounty()).andReturn(null);
		EasyMock.expect(region.getMunicipality()).andReturn(null);
		regionDAO.makePersistent(region);
		leo.setRegion(region);
		leo.setUpdated(EasyMock.anyObject(Date.class));
		final List<Officer> officers = new ArrayList<Officer>();
		EasyMock.expect(leo.getOfficers()).andReturn(officers);
		EasyMock.expect( localOfficialDAO.findAdditionalAddressTypes() ).andReturn( Collections.<AdditionalAddressType>emptyList() ).anyTimes();
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).anyTimes();
		localOfficialDAO.makePersistent(leo);
		final State state = createMock("State", State.class);
		EasyMock.expect(region.getState()).andReturn(state);
		final Set<State> states = new HashSet<State>(Arrays.asList(state));
		regionDAO.cleanCountiesAndMunicipalities(states);
		replayAll();

		localOfficialService.updateLocalOfficial(leo);
		
		verifyAll();
	}
}
