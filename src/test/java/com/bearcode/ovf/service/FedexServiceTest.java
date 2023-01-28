/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.ovf.DAO.FedexCountryDAO;
import com.bearcode.ovf.DAO.FedexLabelDAO;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.express.CountryDescription;
import com.bearcode.ovf.model.express.FedexLabel;

/**
 * Test for {@link com.bearcode.ovf.service.FedexService}.
 * 
 * @author IanBrown
 * 
 * @since Jul 17, 2012
 * @version Jul 17, 2012
 */
public final class FedexServiceTest extends EasyMockSupport {

	/**
	 * the FedEx country DAO.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FedexCountryDAO fedexCountryDAO;

	/**
	 * the FedEx label DAO.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FedexLabelDAO fedexLabelDAO;

	/**
	 * the FedEx service.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FedexService fedexService;

	/**
	 * Sets up the FedEx service to test.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Before
	public final void setUpFedexService() {
		setFedexCountryDAO(createMock("FedExCountryDAO", FedexCountryDAO.class));
		setFedexLabelDAO(createMock("FedExLabelDAO", FedexLabelDAO.class));
		setFedexService(createFedexService());
		ReflectionTestUtils.setField(getFedexService(), "fedexCountryDAO", getFedexCountryDAO());
		ReflectionTestUtils.setField(getFedexService(), "fedexLabelDAO", getFedexLabelDAO());
	}

	/**
	 * Tears down the FedEx service after testing.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@After
	public final void tearDownFedExService() {
		setFedexService(null);
		setFedexLabelDAO(null);
		setFedexCountryDAO(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FedexService#findActiveFedexCountries()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindActiveFedexCountries() {
		final CountryDescription activeFedexCountry = createMock("ActiveFedexCountry", CountryDescription.class);
		final Collection<CountryDescription> activeFedexCountries = Arrays.asList(activeFedexCountry);
		EasyMock.expect(getFedexCountryDAO().getActiveFedexCountries()).andReturn(activeFedexCountries);
		replayAll();

		final Collection<CountryDescription> actualActiveFedexCountries = getFedexService().findActiveFedexCountries();

		assertSame("The active FedEx countries are returned", activeFedexCountries, actualActiveFedexCountries);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FedexService#findFedexCountries()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindFedexCountries() {
		final CountryDescription fedexCountry = createMock("FedexCountry", CountryDescription.class);
		final Collection<CountryDescription> fedexCountries = Arrays.asList(fedexCountry);
		EasyMock.expect(getFedexCountryDAO().getFedexCountries()).andReturn(fedexCountries);
		replayAll();

		final Collection<CountryDescription> actualFedexCountries = getFedexService().findFedexCountries();

		assertSame("The FedEx countries are returned", fedexCountries, actualFedexCountries);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FedexService#findFedexCountry(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindFedexCountry() {
		final long id = 76252l;
		final CountryDescription fedexCountry = createMock("FedExCountry", CountryDescription.class);
		EasyMock.expect(getFedexCountryDAO().getFedexCountry(id)).andReturn(fedexCountry);
		replayAll();

		final CountryDescription actualFedexCountry = getFedexService().findFedexCountry(id);

		assertSame("The FedEx country is returned", fedexCountry, actualFedexCountry);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FedexService#findFedexCountryByName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindFedexCountryByName() {
		final String countryName = "Country Name";
		final CountryDescription fedexCountry = createMock("FedExCountry", CountryDescription.class);
		EasyMock.expect(getFedexCountryDAO().getFedexCountryByName(countryName)).andReturn(fedexCountry);
		replayAll();

		final CountryDescription actualFedexCountry = getFedexService().findFedexCountryByName(countryName);

		assertSame("The FedEx country is returned", fedexCountry, actualFedexCountry);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FedexService#findFedexLabel(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindFedexLabel() {
		final long id = 235123l;
		final FedexLabel fedexLabel = createMock("FedExLabel", FedexLabel.class);
		EasyMock.expect(getFedexLabelDAO().getLabel(id)).andReturn(fedexLabel);
		replayAll();

		final FedexLabel actualFedexLabel = getFedexService().findFedexLabel(id);

		assertSame("The FedEx label is returned", fedexLabel, actualFedexLabel);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FedexService#findFedexLabelByNumber(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindFedexLabelByNumber() {
		final String number = "N1234";
		final FedexLabel fedexLabel = createMock("FedExLabel", FedexLabel.class);
		EasyMock.expect(getFedexLabelDAO().getLabelByNumber(number)).andReturn(fedexLabel);
		replayAll();

		final FedexLabel actualFedexLabel = getFedexService().findFedexLabelByNumber(number);

		assertSame("The FedEx label is returned", fedexLabel, actualFedexLabel);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FedexService#findFedexLabelsForUser(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindFedexLabelsForUser() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final FedexLabel fedexLabel = createMock("FedExLabel", FedexLabel.class);
		final Collection fedexLabels = Arrays.asList(fedexLabel);
		EasyMock.expect(getFedexLabelDAO().getLabelsForUser(user)).andReturn(fedexLabels);
		replayAll();

		final Collection actualFedexLabels = getFedexService().findFedexLabelsForUser(user);

		assertSame("The FedEx labels are returned", fedexLabels, actualFedexLabels);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FedexService#findFedexLabelsForUserUnexpired(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindFedexLabelsForUserUnexpired() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final FedexLabel fedexLabel = createMock("FedExLabel", FedexLabel.class);
		final Collection fedexLabels = Arrays.asList(fedexLabel);
		EasyMock.expect(getFedexLabelDAO().getLabelsForUserUnexpired(user)).andReturn(fedexLabels);
		replayAll();

		final Collection actualFedexLabels = getFedexService().findFedexLabelsForUserUnexpired(user);

		assertSame("The FedEx labels are returned", fedexLabels, actualFedexLabels);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FedexService#saveFedexCountry(com.bearcode.ovf.model.express.CountryDescription)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveFedexCountry() {
		final CountryDescription country = createMock("Country", CountryDescription.class);
		getFedexCountryDAO().makePersistent(country);
		replayAll();

		getFedexService().saveFedexCountry(country);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FedexService#saveFedexLabel(com.bearcode.ovf.model.express.FedexLabel)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveFedexLabel() {
		final FedexLabel label = createMock("Label", FedexLabel.class);
		getFedexLabelDAO().makePersistent(label);
		replayAll();

		getFedexService().saveFedexLabel(label);

		verifyAll();
	}

	/**
	 * Create a FedEx service.
	 * 
	 * @author IanBrown
	 * @return the FedEx service.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FedexService createFedexService() {
		return new FedexService();
	}

	/**
	 * Gets the FedEx country DAO.
	 * 
	 * @author IanBrown
	 * @return the FedEx country DAO.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FedexCountryDAO getFedexCountryDAO() {
		return fedexCountryDAO;
	}

	/**
	 * Gets the FedEx label DAO.
	 * 
	 * @author IanBrown
	 * @return the FedEx label DAO.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FedexLabelDAO getFedexLabelDAO() {
		return fedexLabelDAO;
	}

	/**
	 * Gets the FedEx service.
	 * 
	 * @author IanBrown
	 * @return the FedEx service.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FedexService getFedexService() {
		return fedexService;
	}

	/**
	 * Sets the FedEx country DAO.
	 * 
	 * @author IanBrown
	 * @param fedexCountryDAO
	 *            the FedEx country DAO to set.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private void setFedexCountryDAO(final FedexCountryDAO fedexCountryDAO) {
		this.fedexCountryDAO = fedexCountryDAO;
	}

	/**
	 * Sets the FedEx label DAO.
	 * 
	 * @author IanBrown
	 * @param fedexLabelDAO
	 *            the FedEx label DAO to set.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private void setFedexLabelDAO(final FedexLabelDAO fedexLabelDAO) {
		this.fedexLabelDAO = fedexLabelDAO;
	}

	/**
	 * Sets the FedEx service.
	 * 
	 * @author IanBrown
	 * @param fedexService
	 *            the FedEx service to set.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private void setFedexService(final FedexService fedexService) {
		this.fedexService = fedexService;
	}
}
