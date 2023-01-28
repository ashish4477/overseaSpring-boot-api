/**
 * 
 */
package com.bearcode.ovf.tools.candidate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
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
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.VipObject;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Integration test for {@link ElectionService}.
 * 
 * @author IanBrown
 * 
 * @since Jun 13, 2012
 * @version Oct 11, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "../../actions/commons/applicationContext_test.xml", "ElectionService-context.xml" })
@DirtiesContext
public final class ElectionServiceIntegration extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the election service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 13, 2012
	 * @version Jun 13, 2012
	 */
	@Autowired
	private ElectionService electionService;

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * the state DAO.
	 * 
	 * @author IanBrown
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	@Autowired
	private StateDAO stateDAO;
	
	/**
	 * the voting region DAO.
	 * @author IanBrown
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	@Autowired
	private VotingRegionDAO votingRegionDAO;

	/**
	 * Gets the voting region DAO.
	 * @author IanBrown
	 * @return the voting region DAO.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	public VotingRegionDAO getVotingRegionDAO() {
		return votingRegionDAO;
	}

	/**
	 * Sets the voting region DAO.
	 * @author IanBrown
	 * @param votingRegionDAO the voting region DAO to set.
	 * @since Sep 17, 2012
	 * @version Sep 17, 2012
	 */
	public void setVotingRegionDAO(VotingRegionDAO votingRegionDAO) {
		this.votingRegionDAO = votingRegionDAO;
	}

	/**
	 * Creates a valid address from the input values.
	 * 
	 * @author IanBrown
	 * @param street1
	 *            the street address.
	 * @param city
	 *            the city.
	 * @param county
	 *            the county.
	 * @param state
	 *            the state.
	 * @param zip
	 *            the ZIP code.
	 * @return the valid address.
	 * @since Sep 11, 2012
	 * @version Sep 17, 2012
	 */
	public ValidAddress createValidAddress(final String street1, final String city, final String county, final String state,
			final String zip) {
		final WizardResultAddress workingAddress = new WizardResultAddress();
		workingAddress.setStreet1(street1);
		workingAddress.setCity(city);
		workingAddress.setCounty(county);
		workingAddress.setState(state);
		workingAddress.setZip(zip);
		final State votingState = getStateDAO().getByAbbreviation(workingAddress.getState());
		final ValidAddress validAddress;
		if (county == null) {
			validAddress = getVotingPrecinctService().validateAddress(workingAddress, votingState);
		} else {
			final String countyName = county.toUpperCase().endsWith("COUNTY") ? county : county + " County";
			final VotingRegion region = new VotingRegion();
			region.setState(votingState);
			region.setName(countyName);
			final VotingRegion votingRegion = getVotingRegionDAO().getRegionByName(region);
			if (votingRegion == null) {
				validAddress = getVotingPrecinctService().validateAddress(workingAddress, votingState);				
			} else {
				validAddress = getVotingPrecinctService().validateAddress(workingAddress, votingRegion);
			}
		}
		assertNotNull("There is a valid address", validAddress);
		assertNotNull("The valid address has a street segment", validAddress.getStreetSegment());
		return validAddress;
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findCandidateBio(String, String, long)} for VIP data.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem getting the candidate bio.
	 * @since Aug 16, 2012
	 * @version Sep 17, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindCandidateBio_vip() throws Exception {
		loadVipService();
		final ValidAddress validAddress = buildValidVirginiaAddress();
		final long candidateVipId = 90001l;

		final VipCandidateBio actualCandidateBio = getElectionService().findCandidateBio(
				validAddress.getValidatedAddress().getState(), validAddress.getValidatedAddress().getCounty(), candidateVipId);

		assertNotNull("A candidate bio is returned", actualCandidateBio);
		assertEquals("The candidate VIP id is correct", candidateVipId, actualCandidateBio.getCandidate().getVipId().longValue());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findContests(ValidAddress)} for the case where a VIP
	 * file is loaded into the database.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem getting the contests.
	 * @since Jul 3, 2012
	 * @version Aug 16, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindContests_vip() throws Exception {
		loadVipService();
		final ValidAddress validAddress = buildValidVirginiaAddress();

		final Collection<VipContest> actualContests = getElectionService().findContests(validAddress);

		assertNotNull("A collection of contests is returned", actualContests);
		assertFalse("There is at least one contest", actualContests.isEmpty());
	}


	/**
	 * Test method for {@link com.bearcode.ovf.tools.candidate.ElectionService#findReferendumDetail(String, String, long)} for VIP
	 * data.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem getting the candidate bio.
	 * @since Aug 17, 2012
	 * @version Sep 17, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testFindReferendumDetail_vip() throws Exception {
		loadVipService();
		final ValidAddress validAddress = buildValidVirginiaAddress();
		final long referendumVipId = 90011l;

		final VipReferendumDetail actualReferendumDetail = getElectionService().findReferendumDetail(
				validAddress.getValidatedAddress().getState(), validAddress.getValidatedAddress().getCounty(), referendumVipId);

		assertNotNull("A referendum detail is returned", actualReferendumDetail);
		assertEquals("The referendum VIP id is correct", referendumVipId, actualReferendumDetail.getReferendum().getVipId()
				.longValue());
	}

	/**
	 * Builds a valid address for Florida.
	 * 
	 * @author IanBrown
	 * @return the valid address.
	 * @since Aug 16, 2012
	 * @version Sep 17, 2012
	 */
	private ValidAddress buildValidFloridaAddress() {
		final String street1 = "302 N. Wilson St";
		final String city = "Crestview";
		final String county = "Okaloosa County";
		final String state = "FL";
		final String zip = "32536";
		return createValidAddress(street1, city, county, state, zip);
	}

	/**
	 * Builds a valid address for Virginia.
	 * 
	 * @author IanBrown
	 * @return the valid address.
	 * @since Aug 16, 2012
	 * @version Sep 17, 2012
	 */
	private ValidAddress buildValidVirginiaAddress() {
		final String street1 = "2 E Guinevere Dr SE";
		final String city = "Annandale";
		final String county = null;
		final String state = "VA";
		final String zip = "22003";
		return createValidAddress(street1, city, county, state, zip);
	}

	/**
	 * Gets the election service to test.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since Jun 13, 2012
	 * @version Jun 13, 2012
	 */
	private ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the state DAO.
	 * 
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Jul 3, 2012
	 * @version Jul 3, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Loads the VIP service data.
	 * 
	 * @author IanBrown
	 * @throws FileNotFoundException
	 *             if the VIP source file isn't found.
	 * @throws JAXBException
	 *             if there is a problem with the VIP XML.
	 * @throws IOException
	 *             if there is a problem reading the VIP source file.
	 * @since Aug 16, 2012
	 * @version Oct 11, 2012
	 */
	private void loadVipService() throws FileNotFoundException, JAXBException, IOException {
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
