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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
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
import com.bearcode.ovf.tools.vip.xml.VipObject;

/**
 * Integration test for {@link VipService}.
 * 
 * @author IanBrown
 * 
 * @since Jul 16, 2012
 * @version Oct 11, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "../actions/commons/applicationContext_test.xml", "VipService-context.xml" })
@DirtiesContext
public final class VipServiceIntegration extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the VIP service to use.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * Sets up the VIP service to test.
	 * 
	 * @author IanBrown
	 * @throws JAXBException
	 *             if there is a problem parsing the XML.
	 * @throws IOException
	 *             if there is a problem reading the VIP data.
	 * @since Jul 16, 2012
	 * @version Oct 11, 2012
	 */
	@Before
	public final void setUpVipService() throws JAXBException, IOException {
		final String source = "src/test/resources/com/bearcode/ovf/tools/vip/vip.xml";
		final File sourceFile = new File(source);
		final FileInputStream fis = new FileInputStream(sourceFile);
		final JAXBContext context = JAXBContext.newInstance(VipObject.class.getPackage().getName());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final VipObject vipObject = (VipObject) unmarshaller.unmarshal(fis);
		fis.close();
		getVipService().convert(vipObject, new Date());
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
		final VipSource source = findLatestSource();
		final long candidateVipId = 90002l;

		final VipCandidateBio actualCandidateBio = getVipService().findCandidateBioBySourceAndVipId(source, candidateVipId);

		assertNotNull("A candidate bio is returned", actualCandidateBio);
		final VipCandidate actualCandidate = actualCandidateBio.getCandidate();
		assertEquals("The VIP identifier of the candidate is correct", candidateVipId, actualCandidate.getVipId().longValue());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findCitiesBySourceStateAndVotingRegion(VipSource, String, String)}
	 * for a county region.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindCitiesBySourceStateAndVotingRegion_county() {
		final VipSource source = findLatestSource();
		final String stateAbbreviation = "OH";
		final String countyName = "Allen";

		final List<String> actualCities = getVipService().findCitiesBySourceStateAndVotingRegion(source, stateAbbreviation,
				countyName + " County");

		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There are cities", actualCities.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findCitiesBySourceStateAndZip(VipSource, String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	public final void testFindCitiesBySourceStateAndZip() {
		final VipSource source = findLatestSource();
		final String stateAbbreviation = "VA";
		final String zip = "22003";

		final List<String> actualCities = getVipService().findCitiesBySourceStateAndZip(source, stateAbbreviation, zip);

		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There are cities", actualCities.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.VipService#findContestsBySourceAndElection(com.bearcode.ovf.model.vip.VipSource, com.bearcode.ovf.model.vip.VipElection)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindContestsBySourceAndElection() {
		final VipSource source = findLatestSource();
		final VipElection election = findElectionBySource(source);

		final List<VipContest> actualContests = getVipService().findContestsBySourceAndElection(source, election);

		assertNotNull("A list of contests is returned", actualContests);
		assertFalse("There are contests", actualContests.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.VipService#findContestsForElectoralDistrict(com.bearcode.ovf.model.vip.VipElectoralDistrict)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindContestsForElectoralDistrict() {
		final VipSource source = findLatestSource();
		final List<VipPrecinct> precincts = findPrecinctsBySource(source);
		final VipPrecinct precinct = precincts.get(0);
		final VipElectoralDistrict electoralDistrict = precinct.getElectoralDistricts().iterator().next();

		final List<VipContest> actualContests = getVipService().findContestsForElectoralDistrict(electoralDistrict);

		assertNotNull("There is a list of contests", actualContests);
		assertFalse("There are contests", actualContests.isEmpty());
		for (final VipContest actualContest : actualContests) {
			assertSame("The contest source is set", source, actualContest.getSource());
			assertSame("The contest electoral district is set", electoralDistrict, actualContest.getElectoralDistrict());
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findElectionBySource(com.bearcode.ovf.model.vip.VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindElectionBySource() {
		final VipSource source = findLatestSource();

		final VipElection actualElection = findElectionBySource(source);

		assertNotNull("An election is returned", actualElection);
		assertSame("The election source is set", source, actualElection.getSource());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findLatestSource()}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindLatestSource() {
		final VipSource actualLatestSource = findLatestSource();

		assertNotNull("There is a latest source", actualLatestSource);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findLatestSource(String...)}.
	 * 
	 * @author IanBrown
	 * @since Sep 18, 2012
	 * @version Sep 18, 2012
	 */
	@Test
	public final void testFindLatestSourceStrings() {
		final String state = "Ohio";

		final VipSource actualLatestSource = getVipService().findLatestSource(state);

		assertNotNull("There is a latest source", actualLatestSource);
		final VipState actualState = getVipService().findStateBySourceAndName(actualLatestSource, state);
		assertNotNull("The source has the correct state", actualState);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.VipService#findLocalitiesByStateAndType(com.bearcode.ovf.model.vip.VipState, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 27, 2012
	 * @version Jul 27, 2012
	 */
	@Test
	public final void testFindLocalitiesByStateAndType() {
		final VipSource source = findLatestSource();
		final VipState state = findStateBySourceAndName(source, "Ohio");
		final String type = "county";

		final List<VipLocality> actualLocalities = findLocalitiesByStateAndType(state, type);

		assertNotNull("There is a list of localities", actualLocalities);
		assertFalse("There are localities", actualLocalities.isEmpty());
		for (final VipLocality actualLocality : actualLocalities) {
			assertSame("The locality state is set", state, actualLocality.getState());
			assertEquals("The locality type is set", type, actualLocality.getType());
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findPrecinctsBySource(com.bearcode.ovf.model.vip.VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindPrecinctsBySource() {
		final VipSource source = findLatestSource();

		final List<VipPrecinct> actualPrecincts = findPrecinctsBySource(source);

		assertNotNull("There is a list of precincts", actualPrecincts);
		assertFalse("There are precincts", actualPrecincts.isEmpty());
		for (final VipPrecinct actualPrecinct : actualPrecincts) {
			assertSame("The precinct source is set", source, actualPrecinct.getSource());
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findPrecinctSplitsBySource(com.bearcode.ovf.model.vip.VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindPrecinctSplitsBySource() {
		final VipSource source = findLatestSource();

		final List<VipPrecinctSplit> actualPrecinctSplits = getVipService().findPrecinctSplitsBySource(source);

		assertNotNull("There is a list of precinct splits", actualPrecinctSplits);
		assertFalse("There are precinct splits", actualPrecinctSplits.isEmpty());
		for (final VipPrecinctSplit actualPrecinctSplit : actualPrecinctSplits) {
			assertSame("The precinct split source is set", source, actualPrecinctSplit.getSource());
		}
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
		final VipSource source = findLatestSource();
		final long referendumVipId = 90011l;

		final VipReferendumDetail actualReferendumDetail = getVipService().findReferendumDetailBySourceAndVipId(source,
				referendumVipId);

		assertNotNull("A referendum detail is returned", actualReferendumDetail);
		final VipReferendum actualReferendum = actualReferendumDetail.getReferendum();
		assertEquals("The VIP identifier of the referendum is correct", referendumVipId, actualReferendum.getVipId().longValue());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.VipService#findStateBySourceAndName(com.bearcode.ovf.model.vip.VipSource, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 27, 2012
	 * @version Jul 27, 2012
	 */
	@Test
	public final void testFindStateBySourceAndName() {
		final VipSource source = findLatestSource();
		final String name = "Ohio";

		final VipState actualState = findStateBySourceAndName(source, name);

		assertNotNull("A state is returned", actualState);
		assertSame("The state source is set", source, actualState.getSource());
		assertEquals("The state name is set", name, actualState.getName());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStatesBySource(VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	@Test
	public final void testFindStatesBySource() {
		final VipSource source = findLatestSource();

		final List<VipState> actualStates = findStatesBySource(source);

		assertNotNull("There is a list of states", actualStates);
		assertFalse("There are states", actualStates.isEmpty());
		for (final VipState actualState : actualStates) {
			assertSame("The state source is set", source, actualState.getSource());
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStreetNamesBySourceStateAndCity(VipSource, String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	public final void testFindStreetNamesBySourceStateAndCity() {
		final VipSource source = findLatestSource();
		final String stateAbbreviation = "VA";
		final String city = "Annandale";

		final List<String> actualStreetNames = getVipService().findStreetNamesBySourceStateAndCity(source, stateAbbreviation, city);

		assertNotNull("A list of street names is returned", actualStreetNames);
		assertFalse("There are street names", actualStreetNames.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.VipService#findStreetSegmentForAddress(com.bearcode.ovf.model.vip.VipSource, String, int, String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, String, String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Aug 31, 2012
	 */
	@Test
	public final void testFindStreetSegmentForAddress() {
		final VipSource source = findLatestSource();
		final int houseNumber = 2;
		final String houseNumberSuffix = "A";
		final String streetName = "Main";
		final String streetSuffix = "Dr";
		final String city = null;
		final String state = null;
		final String zip = "22003";

		final VipStreetSegment actualStreetSegment = getVipService().findStreetSegmentForAddress(source, null, houseNumber,
				houseNumberSuffix, null, streetName, streetSuffix, null, city, state, zip);

		assertNotNull("A street segment is returned", actualStreetSegment);
		assertTrue("The house number is in range",
				actualStreetSegment.getStartHouseNumber() <= houseNumber && actualStreetSegment.getEndHouseNumber() >= houseNumber);
		assertTrue("The house number is on the correct side",
				"both".equals(actualStreetSegment.getOddEvenBoth()) || "even".equals(actualStreetSegment.getOddEvenBoth()));
		final VipDetailAddress actualNonHouseAddress = actualStreetSegment.getNonHouseAddress();
		assertNotNull("There is a non-house address", actualNonHouseAddress);
		assertEquals("The house number suffix is correct", houseNumberSuffix, actualNonHouseAddress.getHouseNumberSuffix());
		assertNull("There is no street direction", actualNonHouseAddress.getStreetDirection());
		assertEquals("The street name is correct", streetName, actualNonHouseAddress.getStreetName());
		assertEquals("The street suffix is correct", streetSuffix, actualNonHouseAddress.getStreetSuffix());
		assertNull("There is no address direction", actualNonHouseAddress.getAddressDirection());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.VipService#findStreetSegmentsBySource(com.bearcode.ovf.model.vip.VipSource)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindStreetSegmentsBySource() {
		final VipSource source = findLatestSource();

		final List<VipStreetSegment> actualStreetSegments = getVipService().findStreetSegmentsBySource(source);

		assertNotNull("There is a list of street segments", actualStreetSegments);
		assertFalse("There are street segments", actualStreetSegments.isEmpty());
		for (final VipStreetSegment actualStreetSegment : actualStreetSegments) {
			assertSame("The street segment source is set", source, actualStreetSegment.getSource());
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.VipService#findStreetSegmentsBySourceAndZip(com.bearcode.ovf.model.vip.VipSource, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindStreetSegmentsBySourceAndZip() {
		final VipSource source = findLatestSource();
		final String zipCode = "22003";

		final List<VipStreetSegment> actualStreetSegments = getVipService().findStreetSegmentsBySourceAndZip(source, zipCode);

		assertNotNull("There is a list of street segments", actualStreetSegments);
		assertFalse("There are street segments", actualStreetSegments.isEmpty());
		for (final VipStreetSegment actualStreetSegment : actualStreetSegments) {
			assertSame("The street segment source is set", source, actualStreetSegment.getSource());
			assertEquals("The street segment ZIP code is set", zipCode, actualStreetSegment.getNonHouseAddress().getZip());
		}
	}

	/**
	 * Finds the election for the specified source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the election.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	private VipElection findElectionBySource(final VipSource source) {
		return getVipService().findElectionBySource(source);
	}

	/**
	 * Finds the latest source.
	 * 
	 * @author IanBrown
	 * @return the latest source.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	private VipSource findLatestSource() {
		return getVipService().findLatestSource();
	}

	/**
	 * Finds the localities for the state and type.
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
	private List<VipLocality> findLocalitiesByStateAndType(final VipState state, final String type) {
		return getVipService().findLocalitiesByStateAndType(state, type);
	}

	/**
	 * Finds the precincts for the specified source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the precincts.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	private List<VipPrecinct> findPrecinctsBySource(final VipSource source) {
		return getVipService().findPrecinctsBySource(source);
	}

	/**
	 * Finds the state with the specified name belonging to the specified source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param name
	 *            the name of the state.
	 * @return the state.
	 * @since Jul 27, 2012
	 * @version Jul 27, 2012
	 */
	private VipState findStateBySourceAndName(final VipSource source, final String name) {
		return getVipService().findStateBySourceAndName(source, name);
	}

	/**
	 * Finds the states belonging to the specified source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the states.
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	private List<VipState> findStatesBySource(final VipSource source) {
		return getVipService().findStatesBySource(source);
	}

	/**
	 * Gets the VIP service to test.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}
}
