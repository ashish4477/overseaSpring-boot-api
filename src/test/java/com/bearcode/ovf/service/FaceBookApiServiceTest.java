/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertSame;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.ovf.DAO.FaceBookApiDAO;
import com.bearcode.ovf.model.common.FaceBookApi;

/**
 * Test for {@link com.bearcode.ovf.service.FaceBookApiService}.
 * 
 * @author IanBrown
 * 
 * @since Jul 17, 2012
 * @version Jul 17, 2012
 */
public final class FaceBookApiServiceTest extends EasyMockSupport {

	/**
	 * the facebook API DAO.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FaceBookApiDAO faceBookApiDAO;

	/**
	 * the facebook API service to test.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FaceBookApiService faceBookApiService;

	/**
	 * Sets up to test the facebook API service.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Before
	public final void setUpFaceBookApiService() {
		setFaceBookApiDAO(createMock("FaceBookAPIDAO", FaceBookApiDAO.class));
		setFaceBookApiService(createFaceBookApiService());
		ReflectionTestUtils.setField(getFaceBookApiService(), "faceBookApiDAO", getFaceBookApiDAO());
	}

	/**
	 * Tears down the set up for testing the facebook API service.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@After
	public final void tearDownFaceBookApiService() {
		setFaceBookApiService(null);
		setFaceBookApiDAO(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FaceBookApiService#findForDomain(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindForDomain() {
		final String domain = "Domain";
		final FaceBookApi faceBookApi = createMock("FaceBookAPI", FaceBookApi.class);
		EasyMock.expect(getFaceBookApiDAO().getByDomain(domain)).andReturn(faceBookApi);
		replayAll();

		final FaceBookApi actualFaceBookApi = getFaceBookApiService().findForDomain(domain);

		assertSame("The facebook API is returned", faceBookApi, actualFaceBookApi);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FaceBookApiService#findForSubDomain(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindForSubDomain() {
		final String domain = "Domain";
		final String subDomain = "Sub." + domain;
		final FaceBookApi faceBookApi = createMock("FaceBookAPI", FaceBookApi.class);
		EasyMock.expect(getFaceBookApiDAO().getByDomain(domain)).andReturn(faceBookApi);
		replayAll();

		final FaceBookApi actualFaceBookApi = getFaceBookApiService().findForSubDomain(subDomain);

		assertSame("The facebook API is returned", faceBookApi, actualFaceBookApi);
		verifyAll();
	}

	/**
	 * Creates a facebook API service.
	 * 
	 * @author IanBrown
	 * @return the facebook API service.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FaceBookApiService createFaceBookApiService() {
		return new FaceBookApiService();
	}

	/**
	 * Gets the facebook API DAO.
	 * 
	 * @author IanBrown
	 * @return the facebook API DAO.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FaceBookApiDAO getFaceBookApiDAO() {
		return faceBookApiDAO;
	}

	/**
	 * Gets the facebook API service.
	 * 
	 * @author IanBrown
	 * @return the facebook API service.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FaceBookApiService getFaceBookApiService() {
		return faceBookApiService;
	}

	/**
	 * Sets the facebook API DAO.
	 * 
	 * @author IanBrown
	 * @param faceBookApiDAO
	 *            the facebook API DAO to set.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private void setFaceBookApiDAO(final FaceBookApiDAO faceBookApiDAO) {
		this.faceBookApiDAO = faceBookApiDAO;
	}

	/**
	 * Sets the facebook API service.
	 * 
	 * @author IanBrown
	 * @param faceBookApiService
	 *            the facebook API service to set.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private void setFaceBookApiService(final FaceBookApiService faceBookApiService) {
		this.faceBookApiService = faceBookApiService;
	}

}
