/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.ovf.DAO.CountryDAO;
import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.DAO.VotingRegionDAO;
import com.bearcode.ovf.model.common.Country;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;

/**
 * Test for {@link StateService}
 * 
 * @author IanBrown
 * 
 * @since Jun 11, 2012
 * @version Jul 28, 2012
 */
public final class StateServiceTest extends EasyMockSupport {

	/**
	 * the state service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private StateService stateService;

	/**
	 * the country DAO to use.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private CountryDAO countryDAO;

	/**
	 * the state DAO to use.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private StateDAO stateDAO;

	/**
	 * the region DAO to use.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private VotingRegionDAO regionDAO;

	/**
	 * Sets up the state service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Before
	public final void setUpStateService() {
		setCountryDAO(createMock("CountryDAO", CountryDAO.class));
		setStateDAO(createMock("StateDAO", StateDAO.class));
		setRegionDAO(createMock("RegionDAO", VotingRegionDAO.class));
		setStateService(createStateService());
		ReflectionTestUtils.setField(getStateService(), "countryDAO", getCountryDAO());
		ReflectionTestUtils.setField(getStateService(), "stateDAO", getStateDAO());
		ReflectionTestUtils.setField(getStateService(), "regionDAO", getRegionDAO());
	}

	/**
	 * Tears down the state service after testing.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@After
	public final void tearDownStateService() {
		setStateService(null);
		setRegionDAO(null);
		setStateDAO(null);
		setCountryDAO(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findAllCountries()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindAllCountries() {
		final Country country = createMock("Country", Country.class);
		final Collection<Country> countries = Arrays.asList(country);
		EasyMock.expect(getCountryDAO().getAllCountries()).andReturn(countries);
		replayAll();

		final Collection<Country> actualCountries = getStateService().findAllCountries();

		assertSame("The countries  are returned", countries, actualCountries);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findAllStates()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindAllStates() {
		final State state = createMock("State", State.class);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateDAO().getAllStates()).andReturn(states);
		replayAll();

		final Collection<State> actualStates = getStateService().findAllStates();

		assertSame("The states are returned", states, actualStates);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findByAbbreviation(String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	@Test
	public final void testFindByAbbreviation() {
		final String abbreviation = "AB";
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(abbreviation)).andReturn(state);
		replayAll();

		final State actualState = getStateService().findByAbbreviation(abbreviation);

		assertSame("The state is returned", state, actualState);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findLeoById(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindLeoById() {
		final long id = 1209l;
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(getRegionDAO().findLeoByRegionId(id)).andReturn(leo);
		replayAll();

		final LocalOfficial actualLeo = getStateService().findLeoById(id);

		assertSame("The LEO is returned", leo, actualLeo);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findLeoById(java.lang.Long)} for the case where there is no
	 * local official with the specified ID.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindLeoById_noLeo() {
		final long id = 1209l;
		EasyMock.expect(getRegionDAO().findLeoByRegionId(id)).andReturn(null);
		replayAll();

		final LocalOfficial actualLeo = getStateService().findLeoById(id);

		assertNull("There is no LEO", actualLeo);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegion(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegion() {
		final long regionId = 9812l;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(getRegionDAO().getById(regionId)).andReturn(region);
		replayAll();

		final VotingRegion actualRegion = getStateService().findRegion(regionId);

		assertSame("The region is returned", region, actualRegion);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegion(java.lang.Long)} for the case where the is no region.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegion_noRegion() {
		final long regionId = 9812l;
		EasyMock.expect(getRegionDAO().getById(regionId)).andReturn(null);
		replayAll();

		final VotingRegion actualRegion = getStateService().findRegion(regionId);

		assertNull("There is no region", actualRegion);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateLong() {
		final long stateId = 981289l;
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final List<VotingRegion> regions = Arrays.asList(region);
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getById(stateId)).andReturn(state);
		EasyMock.expect(getRegionDAO().getRegionsForState(state)).andReturn(regions);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(stateId);

		assertSame("The regions are returned", regions, actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(java.lang.Long)} for the case where there
	 * are no regions.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateLong_noRegions() {
		final long stateId = 981289l;
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getById(stateId)).andReturn(state);
		EasyMock.expect(getRegionDAO().getRegionsForState(state)).andReturn(null);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(stateId);

		assertNull("There are no regions", actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(java.lang.Long)} for the case where there is
	 * no state for the ID.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateLong_noState() {
		final long stateId = 981289l;
		EasyMock.expect(getStateDAO().getById(stateId)).andReturn(null);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(stateId);

		assertTrue("An empty collection is returned", actualRegions.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(com.bearcode.ovf.model.common.State)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateState() {
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final List<VotingRegion> regions = Arrays.asList(region);
		final State state = createMock("State", State.class);
		EasyMock.expect(getRegionDAO().getRegionsForState(state)).andReturn(regions);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(state);

		assertSame("The regions are returned", regions, actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(com.bearcode.ovf.model.common.State)} for
	 * the case where there are no regions.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateState_noRegions() {
		final State state = createMock("State", State.class);
		EasyMock.expect(getRegionDAO().getRegionsForState(state)).andReturn(null);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(state);

		assertNull("There are no regions", actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(com.bearcode.ovf.model.common.State)} for
	 * the case where there is no state.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateState_noState() {
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState((State) null);

		assertTrue("An empty collection is returned", actualRegions.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateString() {
		final String name = "ST";
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final List<VotingRegion> regions = Arrays.asList(region);
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(name)).andReturn(state);
		EasyMock.expect(getRegionDAO().getRegionsForState(state)).andReturn(regions);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(name);

		assertSame("The regions are returned", regions, actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(java.lang.String)} for the case where there
	 * are no regions.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateString_noRegions() {
		final String name = "ST";
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(name)).andReturn(state);
		EasyMock.expect(getRegionDAO().getRegionsForState(state)).andReturn(null);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(name);

		assertNull("There are no regions", actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegionsForState(java.lang.String)} for the case where there
	 * is no state with the specified name.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRegionsForStateString_noState() {
		final String name = "ST";
		EasyMock.expect(getStateDAO().getByAbbreviation(name)).andReturn(null);
		replayAll();

		final Collection<VotingRegion> actualRegions = getStateService().findRegionsForState(name);

		assertTrue("An empty collection is returned", actualRegions.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findState(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindState() {
		final long stateId = 712878l;
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getById(stateId)).andReturn(state);
		replayAll();

		final State actualState = getStateService().findState(stateId);

		assertSame("The state is returned", state, actualState);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findState(long)} for the case where there is no state.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindState_noState() {
		final long stateId = 712878l;
		EasyMock.expect(getStateDAO().getById(stateId)).andReturn(null);
		replayAll();

		final State actualState = getStateService().findState(stateId);

		assertNull("No state is returned", actualState);
		verifyAll();
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.service.StateService#findRegion(State, String)}.
	 * @author IanBrown
	 * @since Aug 3, 2012
	 * @version Aug 3, 2012
	 */
	@Test
	public final void testFindRegion_stateString() {
		final State state = createMock("State", State.class);
		final String votingRegionName = "Voting Region Name";
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(getRegionDAO().getRegionByName((VotingRegion) EasyMock.anyObject())).andReturn(region);
		replayAll();
		
		final VotingRegion actualRegion = getStateService().findRegion(state, votingRegionName);
		
		assertSame("The region is returned", region, actualRegion);
		verifyAll();
	}

	/**
	 * Creates a state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private StateService createStateService() {
		return new StateService();
	}

	/**
	 * Gets the country DAO.
	 * 
	 * @author IanBrown
	 * @return the country DAO.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private CountryDAO getCountryDAO() {
		return countryDAO;
	}

	/**
	 * Gets the region DAO.
	 * 
	 * @author IanBrown
	 * @return the region DAO.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private VotingRegionDAO getRegionDAO() {
		return regionDAO;
	}

	/**
	 * Gets the state DAO.
	 * 
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private StateService getStateService() {
		return stateService;
	}

	/**
	 * Sets the country DAO.
	 * 
	 * @author IanBrown
	 * @param countryDAO
	 *            the country DAO to set.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private void setCountryDAO(final CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	/**
	 * Sets the region DAO.
	 * 
	 * @author IanBrown
	 * @param regionDAO
	 *            the region DAO to set.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private void setRegionDAO(final VotingRegionDAO regionDAO) {
		this.regionDAO = regionDAO;
	}

	/**
	 * Sets the state DAO.
	 * 
	 * @author IanBrown
	 * @param stateDAO
	 *            the state DAO to set.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private void setStateDAO(final StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}
}
