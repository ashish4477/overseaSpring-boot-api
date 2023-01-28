/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.InputStream;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.FacesService;

/**
 * Abstract test for implementations of {@link StreetSegmentFinderMelissaValet}.
 * 
 * @author IanBrown
 * 
 * @param <V>
 *            the type of street segment finder Melissa valet to test.
 * @since Sep 5, 2012
 * @version Oct 5, 2012
 */
public abstract class StreetSegmentFinderMelissaValetCheck<V extends StreetSegmentFinderMelissaValet> extends EasyMockSupport {

	/**
	 * the street segment finder Melissa valet to test.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private V valet;

	/**
	 * Sets up the street segment finder Melissa valet to test.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	@Before
	public final void setUpValet() {
		setUpForValet();
		setValet(createValet());
	}

	/**
	 * Tears down the valet after testing.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	@After
	public final void tearDownValet() {
		setValet(null);
		tearDownForValet();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquireCityStream(StreetSegmentFinder, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	@Test
	public final void testAcquireCityStream() {
		final FacesService facesService = createMock("FaceService", FacesService.class);
		final String cityFilePath = "City File.path";
		final InputStream cityStream = createMock("CityStream", InputStream.class);
		EasyMock.expect(facesService.openResource(cityFilePath)).andReturn(cityStream);
		replayAll();

		final InputStream actualCityStream = getValet().acquireCityStream(facesService, cityFilePath);

		assertSame("The city stream is returned", cityStream, actualCityStream);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquireCityStream(StreetSegmentFinder, java.lang.String)}
	 * for the case where there is no city file path.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAcquireCityStream_noCityFilePath() {
		final FacesService facesService = createMock("FacesService", FacesService.class);
		final String cityFilePath = null;

		getValet().acquireCityStream(facesService, cityFilePath);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquireCityStream(StreetSegmentFinder, java.lang.String)}
	 * for the case where there is faces service.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAcquireCityStream_noFacesService() {
		final FacesService facesService = null;
		final String cityFilePath = "City File Path.cfp";

		getValet().acquireCityStream(facesService, cityFilePath);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquireCityStream(StreetSegmentFinder, java.lang.String)}
	 * for the case where there is no such file.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	@Test
	public final void testAcquireCityStream_noFile() {
		final FacesService facesService = createMock("FaceService", FacesService.class);
		final String cityFilePath = "No Such File.nsf";
		EasyMock.expect(facesService.openResource(cityFilePath)).andReturn(null);
		replayAll();

		final InputStream actualCityStream = getValet().acquireCityStream(facesService, cityFilePath);

		assertNull("No city stream is returned", actualCityStream);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquireLocality(VipState, String, String)}.
	 * 
	 * @author IanBrown
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	@Test
	public final void testAcquireLocality() {
		final VipState state = createMock("State", VipState.class);
		final String type = "County";
		final String name = "Name";
		replayAll();

		final VipLocality actualLocality = getValet().acquireLocality(state, type, name);

		assertNotNull("A locality is acquired", actualLocality);
		assertSame("The state is set", state, actualLocality.getState());
		assertEquals("The type is set", type, actualLocality.getType());
		assertEquals("The name is set", name, actualLocality.getName());
		verifyAll();
	}

	/**
	 * Test method for {@lnk com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquirePrecinct(String)}.
	 * 
	 * @author IanBrown
	 * @since Sep 10, 2012
	 * @version Sep 17, 2012
	 */
	@Test
	public final void testAcquirePrecinct() {
		final VipLocality locality = createMock("Locality", VipLocality.class);
		final String districtName = "District Name";
		replayAll();

		final VipPrecinct actualPrecinct = getValet().acquirePrecinct(locality, districtName);

		assertNotNull("A precinct is acquired", actualPrecinct);
		assertEquals("The locality is set", locality, actualPrecinct.getLocality());
		final List<VipElectoralDistrict> actualElectoralDistricts = actualPrecinct.getElectoralDistricts();
		assertNotNull("There are electoral districts", actualElectoralDistricts);
		assertEquals("There is one electoral district", 1, actualElectoralDistricts.size());
		final VipElectoralDistrict actualElectoralDistrict = actualElectoralDistricts.get(0);
		assertEquals("The district name is set", districtName, actualElectoralDistrict.getName());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquireState(String)}.
	 * 
	 * @author IanBrown
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	@Test
	public final void testAcquireState() {
		final String stateAbbr = "SB";
		replayAll();

		final VipState actualState = getValet().acquireState(stateAbbr);

		assertNotNull("A state is acquired", actualState);
		assertEquals("The state abbreviation is set", stateAbbr, actualState.getName());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderMelissaValet#acquireStreetSegment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Sep 5, 2012
	 * @version Sep 6, 2012
	 */
	@Test
	public final void testAcquireStreetSegment() {
		final int houseNumber = 123;
		final String streetName = "My Street";
		final String street1 = houseNumber + " " + streetName;
		final String city = "My City";
		final String votingRegion = "My County";
		final String state = "NH";
		final String zip = "12345";

		final VipStreetSegment actualStreetSegment = getValet().acquireStreetSegment(street1, city, votingRegion, state, zip, true);

		assertNotNull("A street segment is returned", actualStreetSegment);
		assertEquals("The start house number is set", houseNumber, actualStreetSegment.getStartHouseNumber());
		assertEquals("The end house number is set", houseNumber, actualStreetSegment.getEndHouseNumber());
		final VipDetailAddress actualNonHouseAddress = actualStreetSegment.getNonHouseAddress();
		assertNotNull("There is a non-house address", actualNonHouseAddress);
		assertEquals("The street name is set", "MY", actualNonHouseAddress.getStreetName());
		assertEquals("The street suffix is set", "ST", actualNonHouseAddress.getStreetSuffix());
		assertEquals("The city is set", city.toUpperCase(), actualNonHouseAddress.getCity());
		assertEquals("The state is set", state, actualNonHouseAddress.getState());
		assertEquals("The ZIP is set", zip, actualNonHouseAddress.getZip());
	}

	/**
	 * Creates a valet of the type to test.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	protected abstract V createValet();

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	protected final V getValet() {
		return valet;
	}

	/**
	 * Sets up to test the specific type of valet.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	protected abstract void setUpForValet();

	/**
	 * Tears down the set up for testing the specific type of valet.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	protected abstract void tearDownForValet();

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	private void setValet(final V valet) {
		this.valet = valet;
	}

}
