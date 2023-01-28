/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.tools.votingprecinct.StreetSegmentFinderCSV.StreetSegmentField;

/**
 * Extended {@link StreetSegmentFinderCheck} test for {@link StreetSegmentFinderCSV}.
 * 
 * @author IanBrown
 * 
 * @since Jun 5, 2012
 * @version Oct 9, 2012
 */
public final class StreetSegmentFinderCSVTest extends StreetSegmentFinderCheck<StreetSegmentFinderCSV> {

	/**
	 * the valid state identification.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private static final String VALID_STATE = "MN";

	/**
	 * the path for the CSV file to read.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	private static final String CSV_FILE_PATH = "src/test/resources/com/bearcode/ovf/tools/votingprecinct/precinctfinder.txt";

	/**
	 * the path for the county file to read.
	 * 
	 * @author IanBrown
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	private static final String COUNTY_FILE_PATH = "src/test/resources/com/bearcode/ovf/tools/votingprecinct/counties.txt";

	/**
	 * are restricted addresses required?
	 * 
	 * @author IanBrown
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	private boolean restrictedAddressesRequired;

	/**
	 * the valid name for the voting region.
	 * 
	 * @author IanBrown
	 * @since Oct 9, 2012
	 * @version Oct 9, 2012
	 */
	private final static String VALID_VOTING_REGION_NAME = "Steele";

	/** {@inheritDoc} */
	@Override
	protected final boolean canHaveInvalidSetup() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean canRestrictedAddressesBeRequired() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createInvalidAddress() {
		final UserAddress address = new UserAddress(AddressType.STREET);
		address.setStreet1("76 STRAND DR");
		address.setCity("OWATONNA");
		address.setState(VALID_STATE);
		address.setZip("55060");
		return address;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createNormalizedAddress() {
		final UserAddress address = new UserAddress(AddressType.STREET);
		address.setStreet1("4800 22ND AVE SW");
		address.setCity("OWATONNA");
		address.setState(VALID_STATE);
		address.setZip("55060");
		return address;
	}

	/** {@inheritDoc} */
	@Override
	protected final StreetSegmentFinderCSV createStreetSegmentFinder() {
		final StreetSegmentFinderCSV streetSegmentFinder = new StreetSegmentFinderCSV();
		return streetSegmentFinder;
	}

	/** {@inheritDoc} */
	@Override
	protected final UserAddress createValidAddress() {
		final UserAddress address = new UserAddress(AddressType.STREET);
		address.setStreet1("4800 22ND Avenue Southwest");
		address.setCity("Owatonna");
		address.setState(VALID_STATE);
		address.setZip("55060");
		return address;
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean isSupportedStreetNames() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidCity() {
		return "Invalid City";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidState() {
		return "IS";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidVotingRegion() {
		return "Unknown County";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectInvalidZipCode() {
		return "87123";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidCity() {
		return "Owatonna";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidState() {
		return VALID_STATE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidVotingRegion() {
		return VALID_VOTING_REGION_NAME + " County";
	}

	/** {@inheritDoc} */
	@Override
	protected final String selectValidZipCode() {
		return "55060";
	}

	/** {@inheritDoc} */
	@Override
	protected final void setRestrictedAddressesRequired(final boolean restrictedAddressesRequired) {
		this.restrictedAddressesRequired = restrictedAddressesRequired;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpInvalidStreetSegmentFinder() {
		// Nothing to do - with nothing set up, the finder will fail.
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpNotReadyStreetSegmentFinder() {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("StreetSegmentsEmpty", ".csv");
		} catch (final IOException e) {
			fail("Failed to create temp file for testing");
		}
		getForStateOrVotingRegion().setCsvFilePath(tempFile.getAbsolutePath());
		getForStateOrVotingRegion().setCountyFilePath(COUNTY_FILE_PATH);
		final Map<String, StreetSegmentField> fieldMap = createFieldMap();
		getForStateOrVotingRegion().setFieldMap(fieldMap);
		getForStateOrVotingRegion().setRestrictedAddressesRequired(isRestrictedAddressesRequired());
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForState() {
		getForStateOrVotingRegion().setStates(Arrays.asList(VALID_STATE));
		getForStateOrVotingRegion().setCsvFilePath(CSV_FILE_PATH);
		getForStateOrVotingRegion().setCountyFilePath(COUNTY_FILE_PATH);
		final Map<String, StreetSegmentField> fieldMap = createFieldMap();
		getForStateOrVotingRegion().setFieldMap(fieldMap);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpReadyForVotingRegion() {
		final Map<String, Collection<String>> votingRegions = new HashMap<String, Collection<String>>();
		votingRegions.put(VALID_STATE, Arrays.asList(VALID_VOTING_REGION_NAME));
		getForStateOrVotingRegion().setVotingRegions(votingRegions);
		getForStateOrVotingRegion().setCsvFilePath(CSV_FILE_PATH);
		getForStateOrVotingRegion().setCountyFilePath(COUNTY_FILE_PATH);
		final Map<String, StreetSegmentField> fieldMap = createFieldMap();
		getForStateOrVotingRegion().setFieldMap(fieldMap);
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpSpecificStreetSegmentFinder() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpValidStreetSegmentFinder() {
		setUpReadyForState();
		getForStateOrVotingRegion().setRestrictedAddressesRequired(isRestrictedAddressesRequired());
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownSpecificStreetSegmentFinder() {
	}

	/**
	 * Creates the field map.
	 * 
	 * @author IanBrown
	 * @return the field map.
	 * @since Jun 5, 2012
	 * @version Jul 31, 2012
	 */
	private Map<String, StreetSegmentField> createFieldMap() {
		final Map<String, StreetSegmentField> fieldMap = new HashMap<String, StreetSegmentField>();
		fieldMap.put("StreetAddr", StreetSegmentField.STREET);
		fieldMap.put("HouseNbrLo", StreetSegmentField.HOUSE_NUMBER_LOW);
		fieldMap.put("HouseNbrHi", StreetSegmentField.HOUSE_NUMBER_HIGH);
		fieldMap.put("OddEven", StreetSegmentField.ODD_EVEN);
		fieldMap.put("PctName", StreetSegmentField.PRECINCT_NAME);
		fieldMap.put("City", StreetSegmentField.CITY);
		fieldMap.put("County", StreetSegmentField.COUNTY);
		fieldMap.put("State", StreetSegmentField.STATE);
		fieldMap.put("Zip", StreetSegmentField.ZIP);
		// fieldMap.put("CongDist", StreetSegmentField.US_DISTRICT);
		// fieldMap.put("SenDist", StreetSegmentField.STATE_SENATORIAL_DISTRICT);
		// fieldMap.put("LegDist", StreetSegmentField.STATE_REPRESENTATIVE_DISTRICT);
		return fieldMap;
	}

	/**
	 * Gets the restrictedAddressesRequired.
	 * 
	 * @author IanBrown
	 * @return the restrictedAddressesRequired.
	 * @since Sep 19, 2012
	 * @version Sep 19, 2012
	 */
	private boolean isRestrictedAddressesRequired() {
		return restrictedAddressesRequired;
	}
}
