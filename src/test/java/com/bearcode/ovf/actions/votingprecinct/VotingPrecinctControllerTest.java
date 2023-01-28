/**
 * 
 */
package com.bearcode.ovf.actions.votingprecinct;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.bearcode.ovf.actions.commons.AbstractControllerCheck;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;

/**
 * Extended {@link AbstractControllerCheck} test for {@link VotingPrecinctController}.
 * 
 * @author IanBrown
 * 
 * @since Jul 28, 2012
 * @version Jul 31, 2012
 */
public final class VotingPrecinctControllerTest extends AbstractControllerCheck<VotingPrecinctController> {

	/**
	 * the voting precinct service to use.
	 * 
	 * @author IanBrown
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.votingprecinct.VotingPrecinctController#citiesByVotingRegion(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	@Test
	public final void testCitiesByVotingRegion() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting County";
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(getVotingPrecinctService().findCitiesByVotingRegion(stateAbbreviation, votingRegionName)).andReturn(cities);
		replayAll();

		final List<String> actualCities = getController().citiesByVotingRegion(stateAbbreviation, votingRegionName);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.votingprecinct.VotingPrecinctController#citiesByZip(java.lang.String, java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	@Test
	public final void testCitiesByZip() {
		final String stateAbbreviation = "SA";
		final String zip = "13579";
		final String city = "City";
		final List<String> cities = Arrays.asList(city);
		EasyMock.expect(getVotingPrecinctService().findCitiesByZip(stateAbbreviation, zip)).andReturn(cities);
		replayAll();

		final List<String> actualCities = getController().citiesByZip(stateAbbreviation, zip);

		assertSame("The cities are returned", cities, actualCities);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.votingprecinct.VotingPrecinctController#streetNamesByCity(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 28, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	public final void testStreetNamesByCity() {
		final String stateAbbreviation = "SA";
		final String city = "City";
		final String streetName = "Street Name";
		final List<String> streetNames = Arrays.asList(streetName);
		EasyMock.expect(getVotingPrecinctService().findStreetNamesByCity(stateAbbreviation, city)).andReturn(streetNames);
		replayAll();

		final List<String> actualStreetNames = getController().streetNamesByCity(stateAbbreviation, city);

		assertSame("The street names are returned", streetNames, actualStreetNames);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final VotingPrecinctController createController() {
		final VotingPrecinctController votingPrecinctController = new VotingPrecinctController();
		votingPrecinctController.setVotingPrecinctService(getVotingPrecinctService());
		return votingPrecinctController;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForController() {
		setVotingPrecinctService(createMock("VotingPrecinctService", VotingPrecinctService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForController() {
		setVotingPrecinctService(null);
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	private void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}
}
