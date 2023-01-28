/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.DAO.VotingRegionDAO;
import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.VipObject;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Integration test for {@link VotingPrecinctService}.
 * 
 * @author IanBrown
 * 
 * @since Jun 4, 2012
 * @version Oct 11, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "../../actions/commons/applicationContext_test.xml", "VotingPrecinctService-context.xml" })
@DirtiesContext
public final class VotingPrecinctServiceIntegration extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the state DAO to use.
	 * 
	 * @author IanBrown
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	@Autowired
	private StateDAO stateDAO;

	/**
	 * the VIP service to use.
	 * 
	 * @author IanBrown
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * the voting precinct service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * the voting region DAO.
	 * 
	 * @author IanBrown
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	@Autowired
	private VotingRegionDAO votingRegionDAO;

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findCitiesByVotingRegion(String, String)}
	 * for VIP data.
	 * 
	 * @author IanBrown
	 * @throws JAXBException
	 *             if there is a problem reading the XML file.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Jul 28, 2012
	 * @version Oct 10, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindCitiesByVotingRegion_vip() throws JAXBException, IOException {
		loadVipData();
		final String stateAbbreviation = "OH";
		final String votingRegionName = "Adams County";

		final List<String> actualCities = getVotingPrecinctService().findCitiesByVotingRegion(stateAbbreviation, votingRegionName);

		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There is at least one city", actualCities.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findCitiesByZip(String, String)} for VIP
	 * data.
	 * 
	 * @author IanBrown
	 * @throws JAXBException
	 *             if there is a problem reading the XML file.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindCitiesByZip_vip() throws JAXBException, IOException {
		loadVipData();
		final String stateAbbreviation = "OH";
		final String zip = "43321";

		final List<String> actualCities = getVotingPrecinctService().findCitiesByZip(stateAbbreviation, zip);

		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There is at least one city", actualCities.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findStreetNamesByCity(String, String)} for
	 * VIP data.
	 * 
	 * @author IanBrown
	 * @throws JAXBException
	 *             if there is a problem reading the XML file.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Jul 31, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindStreetNamesByCity_vip() throws JAXBException, IOException {
		loadVipData();
		final String stateAbbreviation = "VA";
		final String city = "Annandale";

		final List<String> actualStreetNames = getVotingPrecinctService().findStreetNamesByCity(stateAbbreviation, city);

		assertNotNull("A list of street names is returned", actualStreetNames);
		assertFalse("There is at least one street name", actualStreetNames.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findStreetSegments(String, State)} ()} for a VIP state.
	 * 
	 * @author IanBrown
	 * 
	 * @throws JAXBException
	 *             if there is a problem interpreting the VIP XML file.
	 * @throws IOException
	 *             if there is a problem reading the VIP XML file.
	 * @since Jul 16, 2012
	 * @version Jul 28, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindStreetSegments_vip() throws JAXBException, IOException {
		loadVipData();
		final String stateAbbreviation = "OH";
		final String zipCode = "43321";
		final State votingState = getStateDAO().getByAbbreviation(stateAbbreviation);

		final List<VipStreetSegment> actualStreetSegments = getVotingPrecinctService().findStreetSegments(zipCode, votingState);

		assertNotNull("A list of street segments is returned", actualStreetSegments);
		assertFalse("There is at least one street segment", actualStreetSegments.isEmpty());
		for (final VipStreetSegment actualStreetSegment : actualStreetSegments) {
			final VipDetailAddress actualNonHouseAddress = actualStreetSegment.getNonHouseAddress();
			assertEquals("The state is correct", stateAbbreviation, actualNonHouseAddress.getState());
			assertEquals("The ZIP code is correct", zipCode, actualNonHouseAddress.getZip());
		}
	}


	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#findZipCodes()} for a VIP state.
	 * 
	 * @author IanBrown
	 * 
	 * @throws JAXBException
	 *             if there is a problem interpreting the VIP XML file.
	 * @throws IOException
	 *             if there is a problem reading the VIP XML file.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindZipCodes_vip() throws JAXBException, IOException {
		loadVipData();
		final String stateAbbreviation = "VA";
		final State votingState = getStateDAO().getByAbbreviation(stateAbbreviation);

		final List<String> actualZipCodes = getVotingPrecinctService().findZipCodes(votingState);

		assertNotNull("There is a list of ZIP codes", actualZipCodes);
		assertFalse("There is at least one ZIP code in the list", actualZipCodes.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#fixAddress(UserAddress, VipStreetSegment)}
	 * for a valid VIP address.
	 * 
	 * @author IanBrown
	 * 
	 * @throws JAXBException
	 *             if there is a problem interpreting the VIP XML file.
	 * @throws IOException
	 *             if there is a problem reading the VIP XML file.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFixAddress_vip() throws JAXBException, IOException {
		loadVipData();
		final WizardResultAddress workingAddress = new WizardResultAddress();
		workingAddress.setStreet1("9 Main Drive");
		workingAddress.setCity("Annandale");
		workingAddress.setState("VA");
		final State votingState = getStateDAO().getByAbbreviation(workingAddress.getState());
		final ValidAddress validAddress = getVotingPrecinctService().validateAddress(workingAddress, votingState);

		getVotingPrecinctService().fixAddress(workingAddress, validAddress.getStreetSegment());

		assertEquals("The street1 value is fixed", "9 MAIN DR", workingAddress.getStreet1().toUpperCase());
		assertEquals("The city value is fixed", "ANNANDALE", workingAddress.getCity().toUpperCase());
		assertEquals("The county is set", "ADAMS COUNTY", workingAddress.getCounty().toUpperCase());
		assertEquals("The ZIP is set", "22003", workingAddress.getZip());
	}


	/**
	 * Test method for {@link com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService#validateAddress(UserAddress, State)} for a
	 * valid VIP address.
	 * 
	 * @author IanBrown
	 * 
	 * @throws JAXBException
	 *             if there is a problem interpreting the VIP XML file.
	 * @throws IOException
	 *             if there is a problem reading the VIP XML file.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testValidateAddress_validVip() throws JAXBException, IOException {
		loadVipData();
		final WizardResultAddress workingAddress = new WizardResultAddress();
		workingAddress.setStreet1("9 Main Dr");
		workingAddress.setCity("Annandale");
		workingAddress.setState("VA");
		workingAddress.setZip("22003");
		final State votingState = getStateDAO().getByAbbreviation(workingAddress.getState());

		final ValidAddress actualValidAddress = getVotingPrecinctService().validateAddress(workingAddress, votingState);

		assertNotNull("There is a valid address", actualValidAddress);
		assertNotNull("The valid address has a street segment", actualValidAddress.getStreetSegment());
	}

	/**
	 * Gets the state DAO.
	 * 
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	private StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 6, 2012
	 * @version Jul 6, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Jun 5, 2012
	 * @version Jun 5, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Gets the voting region DAO.
	 * 
	 * @author IanBrown
	 * @return the voting region DAO.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	private VotingRegionDAO getVotingRegionDAO() {
		return votingRegionDAO;
	}

	/**
	 * Loads the VIP data.
	 * 
	 * @author IanBrown
	 * @throws FileNotFoundException
	 *             if the XML file cannot be found.
	 * @throws JAXBException
	 *             if the XML file cannot be read.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Jul 28, 2012
	 * @version Oct 11, 2012
	 */
	private void loadVipData() throws FileNotFoundException, JAXBException, IOException {
		final String source = "src/test/resources/com/bearcode/ovf/tools/vip/vip.xml";
		final File sourceFile = new File(source);
		final FileInputStream fis = new FileInputStream(sourceFile);
		final JAXBContext context = JAXBContext.newInstance(VipObject.class.getPackage().getName());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final VipObject vipObject = (VipObject) unmarshaller.unmarshal(fis);
		fis.close();
		getVipService().convert(vipObject, new Date());
	}
}
