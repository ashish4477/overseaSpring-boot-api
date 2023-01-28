/**
 * 
 */
package com.bearcode.ovf.actions.commons.ajax;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.service.StateService;

/**
 * Test for {@link VotingRegionsList}.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Dec 22, 2011
 */
public final class VotingRegionsListTest extends EasyMockSupport {

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private StateService stateService;

	/**
	 * Eod API service
	 */
	private EodApiService eodApiService;

	/**
	 * the voting regions list to test.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private VotingRegionsList votingRegionsList;

	/**
	 * Sets up to test the voting regions list.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Before
	public final void setUpVotingRegionsList() {
		setStateService(createMock("StateService", StateService.class));
		setEodApiService( createMock( "EodApiService", EodApiService.class ) );
		setVotingRegionsList( createVotingRegionsList() );
		ReflectionTestUtils.setField( getVotingRegionsList(), "stateService", getStateService() );
        ReflectionTestUtils.setField(getVotingRegionsList(), "eodApiService", getEodApiService() );
	}

	/**
	 * Tears down the voting regions list after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@After
	public final void tearDownVotingRegionsList() {
		setVotingRegionsList(null);
		setStateService(null);
        setEodApiService( null );
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.ajax.VotingRegionsList#getRegionsHTMLSelect(java.lang.String, java.lang.Long, org.springframework.ui.ModelMap)}
	 * for the case where no state or region ID is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetRegionsHTMLSelect_noStateOrRegionId() {
		final String state = "";
		final long regionId = 0l;
		final ModelMap references = createMock("References", ModelMap.class);
		final Collection<VotingRegion> regions = Collections.emptyList();
		addAttributeToReferences(references, "regions", regions);
		replayAll();

		final String actualResult = getVotingRegionsList().getRegionsHTMLSelect(state, regionId, references);

		assertEquals("The result is an ajax page", "ajax/GetRegionsNoLabel", actualResult);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.ajax.VotingRegionsList#getRegionsHTMLSelect(java.lang.String, java.lang.Long, org.springframework.ui.ModelMap)}
	 * for the case where a region ID is provided that does match a region.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetRegionsHTMLSelect_regionIdMatched() {
		final String state = "SA";
		final long regionId = 1l;
		final ModelMap references = createMock("References", ModelMap.class);
		final EodRegion region = createMock("Region", EodRegion.class);
		final List<EodRegion> regions = Arrays.asList(region);
		EasyMock.expect(getEodApiService().getRegionsOfState( state )).andReturn(regions);
		addAttributeToReferences(references, "regions", regions);
		EasyMock.expect(region.getId()).andReturn(1l);
		addAttributeToReferences(references, "selectedRegion", region);
		replayAll();

		final String actualResult = getVotingRegionsList().getRegionsHTMLSelect(state, regionId, references);

		assertEquals("The result is an ajax page", "ajax/GetRegionsNoLabel", actualResult);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.ajax.VotingRegionsList#getRegionsHTMLSelect(java.lang.String, java.lang.Long, org.springframework.ui.ModelMap)}
	 * for the case where a region ID is provided that doesn't match a region.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetRegionsHTMLSelect_regionIdNotMatched() {
		final String state = "SA";
		final long regionId = 1l;
		final ModelMap references = createMock("References", ModelMap.class);
		final EodRegion region = createMock("Region", EodRegion.class);
		final List<EodRegion> regions = Arrays.asList(region);
		EasyMock.expect( getEodApiService().getRegionsOfState( state )).andReturn( regions );
		addAttributeToReferences(references, "regions", regions);
		EasyMock.expect(region.getId()).andReturn(2l);
		replayAll();

		final String actualResult = getVotingRegionsList().getRegionsHTMLSelect(state, regionId, references);

		assertEquals("The result is an ajax page", "ajax/GetRegionsNoLabel", actualResult);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.ajax.VotingRegionsList#getRegionsHTMLSelect(java.lang.String, java.lang.Long, org.springframework.ui.ModelMap)}
	 * for the case where a state abbreviation is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetRegionsHTMLSelect_stateAbbreviation() {
		final String state = "SA";
		final long regionId = 0l;
		final ModelMap references = createMock("References", ModelMap.class);
		final EodRegion region = createMock("Region", EodRegion.class);
		final List<EodRegion> regions = Arrays.asList(region);
		EasyMock.expect( getEodApiService().getRegionsOfState( state )).andReturn( regions );
		addAttributeToReferences(references, "regions", regions);
		replayAll();

		final String actualResult = getVotingRegionsList().getRegionsHTMLSelect(state, regionId, references);

		assertEquals("The result is an ajax page", "ajax/GetRegionsNoLabel", actualResult);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.ajax.VotingRegionsList#getRegionsHTMLSelect(java.lang.String, java.lang.Long, org.springframework.ui.ModelMap)}
	 * for the case where a state ID is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetRegionsHTMLSelect_stateId() {
		final long stateId = 27;
		final State state = new State();
		state.setId( stateId );
		state.setName( "Name" );
		state.setAbbr( "Abbr" );
		final long regionId = 0l;
		final ModelMap references = createMock("References", ModelMap.class);
		final EodRegion region = createMock("Region", EodRegion.class);
		final List<EodRegion> regions = Arrays.asList(region);
		EasyMock.expect( getStateService().findState( stateId ) ).andReturn( state );
		EasyMock.expect( getEodApiService().getRegionsOfState( state.getAbbr() )).andReturn( regions );
		addAttributeToReferences(references, "regions", regions);
		replayAll();

		final String actualResult = getVotingRegionsList().getRegionsHTMLSelect(String.valueOf( stateId ), regionId, references);

		assertEquals("The result is an ajax page", "ajax/GetRegionsNoLabel", actualResult);
		verifyAll();
	}

	/**
	 * Adds an attribute to the references.
	 * 
	 * @author IanBrown
	 * @param references
	 *            the references.
	 * @param attributeName
	 *            the name of the attribute.
	 * @param attributeValue
	 *            the value of the attribute.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void addAttributeToReferences(final ModelMap references, final String attributeName, final Object attributeValue) {
		EasyMock.expect(references.put(attributeName, attributeValue)).andReturn(references);
	}

	/**
	 * Creates a voting regions list.
	 * 
	 * @author IanBrown
	 * @return the voting regions list.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private VotingRegionsList createVotingRegionsList() {
		return new VotingRegionsList();
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private StateService getStateService() {
		return stateService;
	}

	public EodApiService getEodApiService() {
		return eodApiService;
	}

	public void setEodApiService( EodApiService eodApiService ) {
		this.eodApiService = eodApiService;
	}

	/**
	 * Gets the voting regions list.
	 * 
	 * @author IanBrown
	 * @return the voting regions list.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private VotingRegionsList getVotingRegionsList() {
		return votingRegionsList;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}

	/**
	 * Sets the voting regions list.
	 * 
	 * @author IanBrown
	 * @param votingRegionsList
	 *            the voting regions list to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setVotingRegionsList(final VotingRegionsList votingRegionsList) {
		this.votingRegionsList = votingRegionsList;
	}

}
