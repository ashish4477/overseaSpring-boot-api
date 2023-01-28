/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bearcode.ovf.webservices.SmartyStreetService;
import net.sourceforge.jgeocoder.AddressComponent;

import org.springframework.beans.factory.annotation.Autowired;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipState;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.webservices.DistrictLookupService;

/**
 * Extended {@link AbstractStreetSegmentFinder} that uses Melissa data to validate addresses. Note that this implementation simply
 * validates the input address and creates a fake segment to represent it. The city and ZIP code information is read from a simple
 * CSV file.
 * 
 * @author IanBrown
 * 
 * @since Sep 4, 2012
 * @version Oct 9, 2012
 */
public class StreetSegmentFinderMelissa extends AbstractStreetSegmentFinder {

	/**
	 * the string returned if there are no cities.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private static final List<String> EMPTY_CITIES = new LinkedList<String>();

	/**
	 * the cities by state abbreviation and either ZIP code or voting region.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private Map<String, List<String>> cities = null;

	/**
	 * the path to the city file.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private String cityFilePath;

	/**
	 * the district lookup service.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	@Autowired
	private DistrictLookupService districtLookupService;

    @Autowired
    private SmartyStreetService smartyStreetService;

	/**
	 * the faces service to use.
	 * 
	 * @author IanBrown
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	@Autowired
	private FacesService facesService;

	/**
	 * the valet object used to acquire resources.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Sep 5, 2012
	 */
	private StreetSegmentFinderMelissaValet valet = StreetSegmentFinderMelissaValetImpl.getInstance();

	/** {@inheritDoc} */
	@Override
	public final boolean areRestrictedAddressesRequired() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findCitiesByVotingRegion(final String stateAbbreviation, final String votingRegionName) {
		final String key = (votingRegionName + ", " + stateAbbreviation).toUpperCase();
		return findCitiesForKey(key);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findCitiesByZip(final String stateAbbreviation, final String zip) {
		final String key = (stateAbbreviation + ", " + zip).toUpperCase();
		return findCitiesForKey(key);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findStreetNamesByCity(final String stateAbbreviation, final String city) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/** {@inheritDoc} */
	@Override
	public VipStreetSegment findStreetSegment(final UserAddress address) {
		final String city = address.getCity();
		final String county = address.getCounty();
		final String stateIdentification = address.getState();
		final String zip = address.getZip();

		final String votingRegionName = getVotingRegionType().equalsIgnoreCase("COUNTY") ? address.getCounty() : null;
		if (!isReady(stateIdentification, votingRegionName)) {
			return null;
		}

		final String cityUpperCase = city.toUpperCase();
		if (zip != null && !zip.trim().isEmpty()) {
			if (!findCitiesByZip(stateIdentification, zip).contains(cityUpperCase)) {
				return null;
			}
		} else {
			if (!findCitiesByVotingRegion(stateIdentification, county).contains(cityUpperCase)) {
				return null;
			}
		}

		final String street1 = address.getStreet1();
		final String[] district = getSmartyStreetService().findDistrict(street1, city, stateIdentification, zip);
		if (district == SmartyStreetService.EMPTY) {
			return null;
		}

		final VipStreetSegment streetSegment = getValet().acquireStreetSegment(street1, city, county, stateIdentification, zip, isNeedNormalization());
		final VipState state = getValet().acquireState(stateIdentification);
		final VipLocality locality = getValet().acquireLocality(state, "County", county);
		final VipPrecinct precinct = getValet().acquirePrecinct(locality, district[0]);
		streetSegment.setPrecinct(precinct);
		return streetSegment;
	}

	/** {@inheritDoc} */
	@Override
	public List<VipStreetSegment> findStreetSegments(final String stateAbbreviation, final String zipCode) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findZipCodes(final String stateAbbreviation) {
		loadCities();
		final List<String> zipCodes = new LinkedList<String>();
		final String marker = (stateAbbreviation + ", ").toUpperCase();
		for (final String key : cities.keySet()) {
			if (key.startsWith(marker)) {
				zipCodes.add(key.split(",")[1].trim());
			}
		}
		return zipCodes;
	}

	/**
	 * Gets the city file path.
	 * 
	 * @author IanBrown
	 * @return the city file path.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	public String getCityFilePath() {
		return cityFilePath;
	}

	/**
	 * Gets the district lookup service.
	 * 
	 * @author IanBrown
	 * @return the district lookup service.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	public DistrictLookupService getDistrictLookupService() {
		return districtLookupService;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	public FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	public StreetSegmentFinderMelissaValet getValet() {
		return valet;
	}

	/**
	 * Sets the city file path.
	 * 
	 * @author IanBrown
	 * @param cityFilePath
	 *            the city file path to set.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	public void setCityFilePath(final String cityFilePath) {
		this.cityFilePath = cityFilePath;
	}

	/**
	 * Sets the district lookup service.
	 * 
	 * @author IanBrown
	 * @param districtLookupService
	 *            the district lookup service to set.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	public void setDistrictLookupService(final DistrictLookupService districtLookupService) {
		this.districtLookupService = districtLookupService;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	public void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	public void setValet(final StreetSegmentFinderMelissaValet valet) {
		this.valet = valet;
	}

	/**
	 * Adds the city to the list of cities for the specified key.
	 * 
	 * @author IanBrown
	 * @param key
	 *            the key to find the city.
	 * @param city
	 *            the city.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private void addCity(final String key, final String city) {
		final String upperCaseKey = key.toUpperCase();
		List<String> citiesForKey = cities.get(upperCaseKey);
		if (citiesForKey == null) {
			citiesForKey = new LinkedList<String>();
			cities.put(upperCaseKey, citiesForKey);
		}
		citiesForKey.add(city.toUpperCase());
	}

	/**
	 * Finds the cities matching the specified key.
	 * 
	 * @author IanBrown
	 * @param key
	 *            the key.
	 * @return the cities that match the key.
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private List<String> findCitiesForKey(final String key) {
		loadCities();
		final List<String> votingRegionCities = cities.get(key);
		return votingRegionCities == null ? EMPTY_CITIES : votingRegionCities;
	}

	/**
	 * Loads the cities from the city file.
	 * 
	 * @author IanBrown
	 * @since Sep 4, 2012
	 * @version Oct 9, 2012
	 */
	private void loadCities() {
		synchronized (this) {
			if (cities == null && getCityFilePath() != null) {
				cities = new HashMap<String, List<String>>();
				final InputStream cityStream = getValet().acquireCityStream(getFacesService(), getCityFilePath());
				if (cityStream == null) {
					return;
				}

				final InputStreamReader isr = new InputStreamReader(cityStream);
				final LineNumberReader lnr = new LineNumberReader(isr);
				String line;

				try {
					while ((line = lnr.readLine()) != null) {
						if (line.trim().startsWith("#")) {
							continue;
						}

						parseCity(line);
					}
				} catch (final IOException e) {
					throw new IllegalStateException(getCityFilePath() + " is not a valid city file", e);
				} finally {
					try {
						lnr.close();
					} catch (final IOException e) {
						// We're closing down anyway, so we probably can afford to ignore the close failure.
					}
				}
			}
		}
	}

	/**
	 * Parses a city out of the input line from the city file.
	 * 
	 * @author IanBrown
	 * @param line
	 *            the line. The format is "city,voting region,state,ZIP code".
	 * @since Sep 4, 2012
	 * @version Sep 4, 2012
	 */
	private void parseCity(final String line) {
		final String[] parts = line.split(",");
		if (parts.length != 4) {
			throw new IllegalStateException(line + " from " + getCityFilePath()
					+ " is not a comment and is not city,voting region,state,ZIP code");
		}
		final String city = parts[0];
		final String votingRegion = parts[1];
		final String state = parts[2];
		final String zipCode = parts[3];
		addCity(votingRegion + ", " + state, city);
		addCity(state + ", " + zipCode, city);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean loadDataIfNeeded(String stateIdentification, String votingRegionName) {
		loadCities();

		return cities != null && !cities.isEmpty();
	}

    public SmartyStreetService getSmartyStreetService() {
        return smartyStreetService;
    }

    public void setSmartyStreetService(final SmartyStreetService smartyStreetService) {
        this.smartyStreetService = smartyStreetService;
    }
}
