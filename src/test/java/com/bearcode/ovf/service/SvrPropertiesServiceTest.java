/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.DAO.SvrPropertiesDAO;

/**
 * Test for {@link SvrPropertiesService}.
 * 
 * @author IanBrown
 * 
 * @since Oct 22, 2012
 * @version Oct 22, 2012
 */
public final class SvrPropertiesServiceTest extends EasyMockSupport {

	/**
	 * the SVR properties service to test.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private SvrPropertiesService svrPropertiesService;

	/**
	 * the SVR properties DAO.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private SvrPropertiesDAO svrPropertiesDAO;

	/**
	 * Sets up the SVR properties service.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@Before
	public final void setUpSvrPropertiesService() {
		setSvrPropertiesDAO(createMock("SvrPropertiesDAO", SvrPropertiesDAO.class));
		setSvrPropertiesService(createSvrPropertiesService());
		getSvrPropertiesService().setSvrPropertiesDAO(getSvrPropertiesDAO());
	}

	/**
	 * Tears down the SVR properties service after testing.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@After
	public final void tearDownSvrPropertiesService() {
		setSvrPropertiesService(null);
		setSvrPropertiesDAO(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.SvrPropertiesService#findProperty(java.lang.String, java.lang.String, java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@Test
	public final void testFindProperty() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region";
		final String propertyName = "property.name";
		final String propertyValue = "Property Value";
		EasyMock.expect(getSvrPropertiesDAO().findProperty(stateAbbreviation, votingRegionName, propertyName)).andReturn(
				propertyValue);
		replayAll();

		final String actualPropertyValue = getSvrPropertiesService()
				.findProperty(stateAbbreviation, votingRegionName, propertyName);

		assertEquals("The property value is returned", propertyValue, actualPropertyValue);
		verifyAll();
	}

	/**
	 * Creates an SVR properties service.
	 * 
	 * @author IanBrown
	 * @return the SVR properties service.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private SvrPropertiesService createSvrPropertiesService() {
		return new SvrPropertiesService();
	}

	/**
	 * Gets the svrPropertiesDAO.
	 * 
	 * @author IanBrown
	 * @return the svrPropertiesDAO.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private SvrPropertiesDAO getSvrPropertiesDAO() {
		return svrPropertiesDAO;
	}

	/**
	 * Gets the svrPropertiesService.
	 * 
	 * @author IanBrown
	 * @return the svrPropertiesService.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private SvrPropertiesService getSvrPropertiesService() {
		return svrPropertiesService;
	}

	/**
	 * Sets the svrPropertiesDAO.
	 * 
	 * @author IanBrown
	 * @param svrPropertiesDAO
	 *            the svrPropertiesDAO to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private void setSvrPropertiesDAO(final SvrPropertiesDAO svrPropertiesDAO) {
		this.svrPropertiesDAO = svrPropertiesDAO;
	}

	/**
	 * Sets the svrPropertiesService.
	 * 
	 * @author IanBrown
	 * @param svrPropertiesService
	 *            the svrPropertiesService to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private void setSvrPropertiesService(final SvrPropertiesService svrPropertiesService) {
		this.svrPropertiesService = svrPropertiesService;
	}

}
