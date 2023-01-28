/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import net.sourceforge.jgeocoder.AddressComponent;
import net.sourceforge.jgeocoder.us.AddressParser;
import net.sourceforge.jgeocoder.us.AddressStandardizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.AbstractVip;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipStreetSegment;

/**
 * Extended {@link AbstractStreetSegmentFinder} that loads the street segments from a simple CSV (comma-separated-value) file.
 * 
 * @author IanBrown
 * 
 * @since Jun 5, 2012
 * @version Oct 9, 2012
 */
@Component
public class StreetSegmentFinderCSV extends AbstractStreetSegmentFinder {

	/**
	 * Enumeration of the fields for street segments.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	public enum StreetSegmentField {

		/**
		 * the city (town, etc.) containing the street segment.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		CITY,

		/**
		 * the county identifier for the street segment.
		 * 
		 * @author IanBrown
		 * @since Jul 31, 2012
		 * @version Jul 31, 2012
		 */
		COUNTY,

		/**
		 * the high house number for the street segment.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		HOUSE_NUMBER_HIGH,

		/**
		 * the low house number for the street segment.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		HOUSE_NUMBER_LOW,

		/**
		 * the odd/even house numbers indicator.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		ODD_EVEN,

		/**
		 * the name of the precinct containing the street segment.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		PRECINCT_NAME,

		/**
		 * the state containing the street segment.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		STATE,

		/**
		 * the name of the street.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		STREET,

		/**
		 * the ZIP code containing the street segment.
		 * 
		 * @author IanBrown
		 * @since Jun 5, 2012
		 * @version Jun 5, 2012
		 */
		ZIP
	}

	/**
	 * the application context.
	 * 
	 * @author IanBrown
	 * @since Jun 6, 2012
	 * @version Jun 6, 2012
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * the names of the counties by their identifiers.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private Map<Integer, String> counties;

	/**
	 * the county file path.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private String countyFilePath;

	/**
	 * the path to the CSV file.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	private String csvFilePath;

	/**
	 * the field mapping object.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	private Map<String, StreetSegmentField> fieldMap;

	/**
	 * are restricted addresses required?
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	private boolean restrictedAddressesRequired;

	/**
	 * the street segments known to the finder, by ZIP code and street name.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jun 29, 2012
	 */
	private Map<String, Map<String, Collection<VipStreetSegment>>> streetSegments;

	/** {@inheritDoc} */
	@Override
	public boolean areRestrictedAddressesRequired() {
		return isRestrictedAddressesRequired();
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findCitiesByVotingRegion(final String stateAbbreviation, final String votingRegionName) {
		synchronized (this) {
			if (streetSegments == null) {
				loadStreetSegments();
			}
		}

		final int countyIdx = votingRegionName.indexOf("County");
		if (countyIdx == -1) {
			return new LinkedList<String>();
		}
		final String countyName = votingRegionName.substring(0, countyIdx).trim();
		final List<String> cities = new LinkedList<String>();
		for (final Map.Entry<String, Map<String, Collection<VipStreetSegment>>> zipStreetSegmentsEntry : streetSegments.entrySet()) {
			for (final Collection<VipStreetSegment> streetStreetSegments : zipStreetSegmentsEntry.getValue().values()) {
				for (final VipStreetSegment streetSegment : streetStreetSegments) {
					final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
					if (stateAbbreviation.equalsIgnoreCase(nonHouseAddress.getState())) {
						final VipPrecinct precinct = streetSegment.getPrecinct();
						final VipLocality locality = precinct.getLocality();
						if (countyName.equalsIgnoreCase(locality.getName())) {
							final String city = nonHouseAddress.getCity();
							if (!cities.contains(city)) {
								cities.add(city);
							}
						}
					}
				}
			}
		}

		Collections.sort(cities);
		return cities;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findCitiesByZip(final String stateAbbreviation, final String zip) {
		synchronized (this) {
			if (streetSegments == null) {
				loadStreetSegments();
			}
		}

		final List<String> cities = new LinkedList<String>();
		final Map<String, Collection<VipStreetSegment>> zipStreetSegments = streetSegments.get(zip);
		if (zipStreetSegments != null) {
			for (final Collection<VipStreetSegment> streetStreetSegments : zipStreetSegments.values()) {
				for (final VipStreetSegment streetSegment : streetStreetSegments) {
					final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
					if (stateAbbreviation.equalsIgnoreCase(nonHouseAddress.getState())) {
						final String city = nonHouseAddress.getCity();
						if (!cities.contains(city)) {
							cities.add(city);
						}
					}
				}
			}
		}

		Collections.sort(cities);
		return cities;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findStreetNamesByCity(final String stateAbbreviation, final String city) {
		synchronized (this) {
			if (streetSegments == null) {
				loadStreetSegments();
			}
		}

		final Set<String> matchingStreetNames = new TreeSet<String>();
		for (final Map.Entry<String, Map<String, Collection<VipStreetSegment>>> zipStreetSegmentsEntry : streetSegments.entrySet()) {
			for (final Collection<VipStreetSegment> streetStreetSegments : zipStreetSegmentsEntry.getValue().values()) {
				for (final VipStreetSegment streetSegment : streetStreetSegments) {
					final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
					if (stateAbbreviation.equalsIgnoreCase(nonHouseAddress.getState())
							&& city.equalsIgnoreCase(nonHouseAddress.getCity())) {
						matchingStreetNames.add(nonHouseAddress.toStreet());
					}
				}
			}
		}

		final List<String> streetNames = new LinkedList<String>(matchingStreetNames);
		Collections.sort(streetNames);
		return streetNames;
	}

	/** {@inheritDoc} */
	@Override
	public VipStreetSegment findStreetSegment(final UserAddress address) {
		synchronized (this) {
			if (streetSegments == null) {
				loadStreetSegments();
			}
		}

		final Map<AddressComponent, String> normalizedAddress = normalizeAddress(address, isNeedNormalization());
		if (normalizedAddress == null) {
			return null;
		}

		final String stateIdentification = normalizedAddress.get(AddressComponent.STATE);
		final String votingRegionName = getVotingRegionType().equalsIgnoreCase("COUNTY") ? address.getCounty() : null;
		if (!isReady(stateIdentification, votingRegionName)) {
			return null;
		}

		final String zip = normalizedAddress.get(AddressComponent.ZIP);
		Collection<VipStreetSegment> streetStreetSegments = null;
		if (zip != null && !zip.trim().isEmpty()) {
			final Map<String, Collection<VipStreetSegment>> zipStreetSegments = streetSegments.get(zip);
			if (zipStreetSegments == null) {
				return null;
			}
			streetStreetSegments = findStreetSegmentsInZip(normalizedAddress, zipStreetSegments);
		} else {
			for (final Map<String, Collection<VipStreetSegment>> zipStreetSegments : streetSegments.values()) {
				final Collection<VipStreetSegment> streetSegmentsFromZip = findStreetSegmentsInZip(normalizedAddress,
						zipStreetSegments);
				if (streetSegmentsFromZip != null && !streetSegmentsFromZip.isEmpty()) {
					streetStreetSegments = streetSegmentsFromZip;
					break;
				}
			}
		}
		if (streetStreetSegments == null) {
			return null;
		}

		return findHouseNumber(streetStreetSegments, normalizedAddress.get(AddressComponent.NUMBER));
	}

	/** {@inheritDoc} */
	@Override
	public final List<VipStreetSegment> findStreetSegments(final String stateAbbreviation, final String zipCode) {
		synchronized (this) {
			if (streetSegments == null) {
				loadStreetSegments();
			}
		}

		final List<VipStreetSegment> matchingStreetSegments = new LinkedList<VipStreetSegment>();
		final Map<String, Collection<VipStreetSegment>> zipStreetSegments = streetSegments.get(zipCode);
		if (zipStreetSegments != null) {
			for (final Collection<VipStreetSegment> streetStreetSegments : zipStreetSegments.values()) {
				matchingStreetSegments.addAll(streetStreetSegments);
			}
		}

		return matchingStreetSegments;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findZipCodes(final String stateAbbreviation) {
		synchronized (this) {
			if (streetSegments == null) {
				loadStreetSegments();
			}
		}

		final List<String> matchingZipCodes = new LinkedList<String>();
		for (final Map.Entry<String, Map<String, Collection<VipStreetSegment>>> zipStreetSegmentsEntry : streetSegments.entrySet()) {
			final String zip = zipStreetSegmentsEntry.getKey();
			streets: for (final Collection<VipStreetSegment> streetStreetSegments : zipStreetSegmentsEntry.getValue().values()) {
				for (final VipStreetSegment streetSegment : streetStreetSegments) {
					if (stateAbbreviation.equalsIgnoreCase(streetSegment.getNonHouseAddress().getState())) {
						matchingZipCodes.add(zip);
						break streets;
					}
				}
			}
		}

		Collections.sort(matchingZipCodes);
		return matchingZipCodes;
	}

	/**
	 * Gets the county file path.
	 * 
	 * @author IanBrown
	 * @return the county file path.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public String getCountyFilePath() {
		return countyFilePath;
	}

	/**
	 * Gets the CSV file path.
	 * 
	 * @author IanBrown
	 * @return the CSV file path.
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	public String getCsvFilePath() {
		return csvFilePath;
	}

	/**
	 * Gets the field map.
	 * 
	 * @author IanBrown
	 * @return the field map.
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	public Map<String, StreetSegmentField> getFieldMap() {
		return fieldMap;
	}

	/**
	 * Gets the restricted addresses required.
	 * 
	 * @author IanBrown
	 * @return the restricted addresses required.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	public boolean isRestrictedAddressesRequired() {
		return restrictedAddressesRequired;
	}

	/**
	 * Sets the county file path.
	 * 
	 * @author IanBrown
	 * @param countyFilePath
	 *            the county file path to set.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	public void setCountyFilePath(final String countyFilePath) {
		this.countyFilePath = countyFilePath;
	}

	/**
	 * Sets the CSV file path.
	 * 
	 * @author IanBrown
	 * @param csvFilePath
	 *            the CSV file path to set.
	 * @since Jun 5, 2012
	 * @version Jun 14, 2012
	 */
	public void setCsvFilePath(final String csvFilePath) {
		this.csvFilePath = csvFilePath;
		streetSegments = null;
	}

	/**
	 * Sets the field map.
	 * 
	 * @author IanBrown
	 * @param fieldMap
	 *            the field map to set.
	 * @since Jun 5, 2012
	 * @version Jun 14, 2012
	 */
	public void setFieldMap(final Map<String, StreetSegmentField> fieldMap) {
		this.fieldMap = fieldMap;
		streetSegments = null;
	}

	/**
	 * Sets the restricted addresses required.
	 * 
	 * @author IanBrown
	 * @param restrictedAddressesRequired
	 *            the restricted addresses required to set.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	public void setRestrictedAddressesRequired(final boolean restrictedAddressesRequired) {
		this.restrictedAddressesRequired = restrictedAddressesRequired;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean loadDataIfNeeded(final String stateIdentification, final String votingRegionName) {
		synchronized (this) {
			if (streetSegments == null) {
				loadStreetSegments();
			}
		}

		return streetSegments != null && !streetSegments.isEmpty();
	}

	/**
	 * Adds the street segment to the new street segments.
	 * 
	 * @author IanBrown
	 * @param streetSegment
	 *            the street segment.
	 * @param newStreetSegments
	 *            the new street segments by ZIP and street.
	 * @since Jun 5, 2012
	 * @version Jun 29, 2012
	 */
	private void addStreetSegment(final VipStreetSegment streetSegment,
			final Map<String, Map<String, Collection<VipStreetSegment>>> newStreetSegments) {
		final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
		final String zip = nonHouseAddress.getZip();
		Map<String, Collection<VipStreetSegment>> zipStreetSegments = newStreetSegments.get(zip);
		if (zipStreetSegments == null) {
			zipStreetSegments = new HashMap<String, Collection<VipStreetSegment>>();
			newStreetSegments.put(zip, zipStreetSegments);
		}

		final String street = nonHouseAddress.toStreet();
		Collection<VipStreetSegment> streetStreetSegments = zipStreetSegments.get(street);
		if (streetStreetSegments == null) {
			streetStreetSegments = new LinkedList<VipStreetSegment>();
			zipStreetSegments.put(street, streetStreetSegments);
		}

		streetStreetSegments.add(streetSegment);
	}

	/**
	 * Adds the value for the specified field to the street segment.
	 * 
	 * @author IanBrown
	 * @param field
	 *            the field.
	 * @param value
	 *            the value.
	 * @param newPrecincts
	 *            the map of new precincts by name.
	 * @param newCounties
	 *            the map of new counties by identifier.
	 * @param related
	 *            the objects related to the street segment.
	 * @param streetSegment
	 *            the street segment to update.
	 * @return the value if it should be worked later or <code>null</code> if it can be ignored.
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	private String addValueToStreetSegment(final StreetSegmentField field, final String value,
			final Map<String, VipPrecinct> newPrecincts, final Map<Integer, VipLocality> newCounties,
			final Map<StreetSegmentField, AbstractVip> related, final VipStreetSegment streetSegment) {
		final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
		String street = null;
		switch (field) {
		case CITY:
			nonHouseAddress.setCity(value);
			break;

		case COUNTY:
			final int countyId = Integer.parseInt(value);
			VipLocality county = newCounties.get(countyId);
			if (county == null) {
				county = new VipLocality();
				final String countyName = counties.get(countyId);
				county.setType("County");
				county.setName(countyName);
				newCounties.put(countyId, county);
			}
			related.put(field, county);
			break;

		case HOUSE_NUMBER_HIGH:
			streetSegment.setEndHouseNumber(Integer.parseInt(value));
			break;

		case HOUSE_NUMBER_LOW:
			streetSegment.setStartHouseNumber(Integer.parseInt(value));
			break;

		case ODD_EVEN:
			streetSegment.setOddEvenBoth(value);
			break;

		case PRECINCT_NAME:
			VipPrecinct precinct = newPrecincts.get(value);
			if (precinct == null) {
				precinct = new VipPrecinct();
				precinct.setName(value);
				newPrecincts.put(value, precinct);
			}
			streetSegment.setPrecinct(precinct);
			related.put(field, precinct);
			break;

		case STATE:
			nonHouseAddress.setState(value);
			break;

		case STREET:
			street = value;
			break;

		case ZIP:
			nonHouseAddress.setZip(value);
			break;
		}

		return street;
	}

	/**
	 * Determines the names of the fields from the input line.
	 * 
	 * @author IanBrown
	 * @param line
	 *            the line to parse.
	 * @return the field names in order.
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	private List<String> determineFieldNames(final String line) {
		final List<String> fieldNames = splitLine(line);
		return fieldNames;
	}

	/**
	 * Finds the file specified by the file path.
	 * 
	 * @author IanBrown
	 * @param filePath
	 *            the file path.
	 * @return the file found.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private File findFile(final String filePath) {
		File file = null;
		if (applicationContext != null) {
			final Resource csvResource = applicationContext.getResource("WEB-INF/classes/" + filePath);
			try {
				final File resourceFile = csvResource.getFile();
				if (resourceFile.canRead()) {
					file = resourceFile;
				}
			} catch (final IOException e) {
			}
		}

		if (file == null) {
			final URL url = ClassLoader.getSystemResource(filePath);
			if (url == null) {
				file = new File(filePath);
			} else {
				file = new File(url.getPath().substring(1));
			}
			if (!file.canRead()) {
				throw new IllegalStateException("No readable file at " + filePath + " can be found");
			}
		}

		return file;
	}

	/**
	 * Finds the house number from the normalized adderss in the street segments.
	 * 
	 * @author IanBrown
	 * @param streetStreetSegments
	 *            the street segments for the street.
	 * @param houseNumberString
	 *            the string representation of the house number.
	 * @return the street segment for the house number or <code>null</code>.
	 * @since Jun 5, 2012
	 * @version Jun 29, 2012
	 */
	private VipStreetSegment findHouseNumber(final Collection<VipStreetSegment> streetStreetSegments, final String houseNumberString) {
		final int houseNumber = parseHouseNumberString(houseNumberString);
		final int value = houseNumber % 2 == 0 ? 2 : 1;

		if (streetStreetSegments != null) {
			for (final VipStreetSegment streetSegment : streetStreetSegments) {
				final String oddEvenBoth = streetSegment.getOddEvenBoth().toUpperCase();
				final int oebValue = "O".equals(oddEvenBoth) ? 1 : "E".equals(oddEvenBoth) ? 2 : 3;
				if ((value & oebValue) == value) {
					if (streetSegment.getStartHouseNumber() <= houseNumber && houseNumber <= streetSegment.getEndHouseNumber()) {
						return streetSegment;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Finds the street segments matching the normalized address within the specified ZIP street segments.
	 * 
	 * @author IanBrown
	 * @param normalizedAddress
	 *            the normalized address.
	 * @param zipStreetSegments
	 *            the street segments for the ZIP code.
	 * @return the matching street segments.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	private Collection<VipStreetSegment> findStreetSegmentsInZip(final Map<AddressComponent, String> normalizedAddress,
			final Map<String, Collection<VipStreetSegment>> zipStreetSegments) {
		final Map<AddressComponent, String> workingAddress = new HashMap<AddressComponent, String>(normalizedAddress);
		String street = extractStreet(workingAddress);
		Collection<VipStreetSegment> streetStreetSegments = zipStreetSegments.get(street);
		if (streetStreetSegments == null) {
			if (renormalizeAddress(workingAddress)) {
				// If there is an alternative for the normalized address (i.e., the data has a different expectation than the
				// normalizer).
				street = extractStreet(normalizedAddress);
				streetStreetSegments = zipStreetSegments.get(street);
				if (streetStreetSegments == null) {
					return null;
				}
			}
		}
		return streetStreetSegments;
	}

	/**
	 * Loads the county file.
	 * 
	 * @author IanBrown
	 * @param countyFile
	 *            the county file.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private void loadCountyFile(final File countyFile) {
		try {
			final LineNumberReader lnr = new LineNumberReader(new FileReader(countyFile));
			try {
				loadCountyReader(lnr);

			} finally {
				lnr.close();
			}

		} catch (final IOException e) {
			throw new IllegalStateException(countyFile.getAbsolutePath() + " cannot be read", e);
		}
	}

	/**
	 * Loads the counties from the input line reader.
	 * 
	 * @author IanBrown
	 * @param lnr
	 *            the line number reader.
	 * @throws IOException
	 *             if there is a problem reading the counties.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private void loadCountyReader(final LineNumberReader lnr) throws IOException {
		final Map<Integer, String> newCounties = new HashMap<Integer, String>();
		String line;

		while ((line = lnr.readLine()) != null) {
			if (line.trim().isEmpty()) {
				continue;
			}

			final String[] parts = line.split(",");
			final int countyId = Integer.parseInt(parts[0].trim());
			final String countyName = parts[1].trim();
			newCounties.put(countyId, countyName);
		}

		counties = newCounties;
	}

	/**
	 * Loads the data from the specified CSV file.
	 * 
	 * @author IanBrown
	 * @param csvFile
	 *            the CSV file.
	 * @since Jun 5, 2012
	 * @version Jun 6, 2012
	 */
	private void loadCsvFile(final File csvFile) {
		try {
			final LineNumberReader lnr = new LineNumberReader(new FileReader(csvFile));
			try {
				loadCsvReader(lnr);

			} finally {
				lnr.close();
			}

		} catch (final IOException e) {
			throw new IllegalStateException(csvFile.getAbsolutePath() + " cannot be read", e);
		}
	}

	/**
	 * Loads the street segments from the input reader.
	 * 
	 * @author IanBrown
	 * @param lnr
	 *            the reader.
	 * @throws IOException
	 *             if there is a problem reading the file.
	 * @since Jun 6, 2012
	 * @version Jul 31, 2012
	 */
	private void loadCsvReader(final LineNumberReader lnr) throws IOException {
		final Map<String, Map<String, Collection<VipStreetSegment>>> newStreetSegments = new HashMap<String, Map<String, Collection<VipStreetSegment>>>();
		final Map<Integer, VipLocality> newCounties = new HashMap<Integer, VipLocality>();
		final Map<String, VipPrecinct> newPrecincts = new HashMap<String, VipPrecinct>();
		String line = null;
		while ((line = lnr.readLine()) != null) {
			if (!line.trim().isEmpty()) {
				break;
			}
		}
		if (line == null) {
			return;
		}

		final List<String> fieldNames = determineFieldNames(line);

		while ((line = lnr.readLine()) != null) {
			if (line.trim().isEmpty()) {
				continue;
			}

			parseLine(fieldNames, line, newStreetSegments, newPrecincts, newCounties);
		}

		streetSegments = newStreetSegments;
	}

	/**
	 * Loads the street segments from the CSV file.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Sep 26, 2012
	 */
	private void loadStreetSegments() {
		if (getCountyFilePath() == null) {
			return;
		} else if (getCsvFilePath() == null) {
			return;
		}

		final File countyFile = findFile(getCountyFilePath());
		final File csvFile = findFile(getCsvFilePath());
		loadCountyFile(countyFile);
		loadCsvFile(csvFile);
	}

	/**
	 * Pulls out the house number value from the input string.
	 * 
	 * @author IanBrown
	 * @param houseNumberString
	 *            the house number string.
	 * @return the house number.
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	private int parseHouseNumberString(final String houseNumberString) {
		// For now, assume it is numeric.
		return Integer.parseInt(houseNumberString);
	}

	/**
	 * Parses the line and creates a street segment if possible.
	 * 
	 * @author IanBrown
	 * @param fieldNames
	 *            the names of the fields.
	 * @param line
	 *            the line to parse.
	 * @param newStreetSegments
	 *            the new street segments by ZIP and street.
	 * @param newPrecincts
	 *            the new precincts by name.
	 * @param newCounties
	 *            the new counties by identifier.
	 * @since Jun 5, 2012
	 * @version Jun 29, 2012
	 */
	private void parseLine(final List<String> fieldNames, final String line,
			final Map<String, Map<String, Collection<VipStreetSegment>>> newStreetSegments,
			final Map<String, VipPrecinct> newPrecincts, final Map<Integer, VipLocality> newCounties) {
		final List<String> fields = splitLine(line);
		if (fields.size() != fieldNames.size()) {
			throw new IllegalStateException(getCsvFilePath() + " contains inconsist data at\n" + line);
		}

		final VipStreetSegment streetSegment = new VipStreetSegment();
		streetSegment.setNonHouseAddress(new VipDetailAddress());
		String street = null;
		final Map<StreetSegmentField, AbstractVip> related = new HashMap<StreetSegmentField, AbstractVip>();
		for (int idx = 0; idx < fieldNames.size(); ++idx) {
			final String fieldName = fieldNames.get(idx);
			final StreetSegmentField field = getFieldMap().get(fieldName);
			if (field == null) {
				continue;
			}

			final String value = fields.get(idx);
			final String workValue = addValueToStreetSegment(field, value, newPrecincts, newCounties, related, streetSegment);
			if (workValue != null) {
				street = workValue;
			}
		}
		((VipPrecinct) related.get(StreetSegmentField.PRECINCT_NAME)).setLocality((VipLocality) related
				.get(StreetSegmentField.COUNTY));

		if (street != null) {
			final VipDetailAddress nonHouseAddress = streetSegment.getNonHouseAddress();
			final String lineAddress = "1 " + street + ", " + nonHouseAddress.getCity() + ", " + nonHouseAddress.getState() + " "
					+ nonHouseAddress.getZip();
			final Map<AddressComponent, String> parsedAddress = AddressParser.parseAddress(lineAddress);
			if (parsedAddress == null) {
				return;
			}
			final Map<AddressComponent, String> normalizedAddress = AddressStandardizer.normalizeParsedAddress(parsedAddress);
			if (normalizedAddress == null) {
				return;
			}
			nonHouseAddress.setStreetDirection(normalizedAddress.get(AddressComponent.PREDIR));
			nonHouseAddress.setStreetName(normalizedAddress.get(AddressComponent.STREET));
			nonHouseAddress.setStreetSuffix(normalizedAddress.get(AddressComponent.TYPE));
			nonHouseAddress.setAddressDirection(normalizedAddress.get(AddressComponent.POSTDIR));
			addStreetSegment(streetSegment, newStreetSegments);
		}
	}

	/**
	 * Renormalize the input address by replacing the standard forms of the normalized address with alternatives.
	 * 
	 * @author IanBrown
	 * @param normalizedAddress
	 *            the normalized address.
	 * @return <code>true</code> if the address was changed, <code>false</code> if no changes were made.
	 * @since Jun 4, 2012
	 * @version Jun 4, 2012
	 */
	private boolean renormalizeAddress(final Map<AddressComponent, String> normalizedAddress) {
		final String streetName = normalizedAddress.get(AddressComponent.STREET);
		final int idx = streetName.indexOf("SAINT");
		if (idx > -1) {
			final StringBuilder newStreetName = new StringBuilder("");
			if (idx > 0) {
				newStreetName.append(streetName.substring(0, idx));
			}
			newStreetName.append("ST");
			if (idx + 5 < streetName.length()) {
				newStreetName.append(streetName.substring(idx + 5));
			}
			normalizedAddress.put(AddressComponent.STREET, newStreetName.toString());
			return true;
		}

		return false;
	}

	/**
	 * Splits the line. The line is assumed to have the form "value"[,...].
	 * 
	 * @author IanBrown
	 * @param line
	 *            the line to split.
	 * @return the parts of the line (quotes are removed).
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	private List<String> splitLine(final String line) {
		final StringTokenizer strtok = new StringTokenizer(line, ",\"", true);
		final List<String> values = new LinkedList<String>();
		if (strtok.hasMoreTokens()) {

			while (strtok.hasMoreTokens()) {
				String token = strtok.nextToken(",\"");
				if ("\"".equals(token)) {
					token = strtok.nextToken("\"");
					values.add(token);
					token = strtok.nextToken();
				} else if (token.equals(",")) {
					// Skip commas.
				} else {
					values.add(token);
				}
			}
		}
		return values;
	}
}
