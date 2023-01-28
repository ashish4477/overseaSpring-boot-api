/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.DAO.RelatedDAO;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.PdfFilling;
import com.bearcode.ovf.model.questionnaire.Related;

/**
 * Test for {@link RelatedService}.
 * 
 * @author IanBrown
 * 
 * @since Jun 11, 2012
 * @version Jun 11, 2012
 */
public final class RelatedServiceTest extends EasyMockSupport {

	/**
	 * the related service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private RelatedService relatedService;

	/**
	 * the related DAO to use.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private RelatedDAO relatedDAO;

	/**
	 * Sets up the related service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Before
	public final void setUpRelatedService() {
		setRelatedDAO(createMock("RelatedDAO", RelatedDAO.class));
		setRelatedService(createRelatedService());
		ReflectionTestUtils.setField(getRelatedService(), "relatedDAO", getRelatedDAO());
	}

	/**
	 * Tears down the related service after testing.
	 * 
	 * @author IanBrown
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@After
	public final void tearDownRelatedService() {
		setRelatedService(null);
		setRelatedDAO(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.RelatedService#deletePdfFilling(com.bearcode.ovf.model.questionnaire.PdfFilling)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testDeletePdfFilling() {
		final PdfFilling filling = createMock("PdfFilling", PdfFilling.class);
		final BasicDependency key = createMock("Key", BasicDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList(key);
		EasyMock.expect(filling.getKeys()).andReturn(keys).atLeastOnce();
		getRelatedDAO().makeAllTransient(keys);
		getRelatedDAO().makeTransient(filling);
		replayAll();

		getRelatedService().deletePdfFilling(filling);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.RelatedService#deletePdfFilling(com.bearcode.ovf.model.questionnaire.PdfFilling)} for the
	 * case where there are no keys.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testDeletePdfFilling_noKeys() {
		final PdfFilling filling = createMock("PdfFilling", PdfFilling.class);
		final Collection<BasicDependency> keys = new ArrayList<BasicDependency>();
		EasyMock.expect(filling.getKeys()).andReturn(keys).atLeastOnce();
		getRelatedDAO().makeTransient(filling);
		replayAll();

		getRelatedService().deletePdfFilling(filling);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.RelatedService#findPdfFillings()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindPdfFillings() {
		final PdfFilling pdfFilling = createMock("PdfFilling", PdfFilling.class);
		final Collection<PdfFilling> pdfFillings = Arrays.asList(pdfFilling);
		EasyMock.expect(getRelatedDAO().findFillings()).andReturn(pdfFillings);
		replayAll();

		final Collection<PdfFilling> actualPdfFillings = getRelatedService().findPdfFillings();

		assertSame("The PDF fillings are returned", pdfFillings, actualPdfFillings);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.RelatedService#findPdfFillings(com.bearcode.ovf.forms.CommonFormObject)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindPdfFillingsCommonFormObject() {
		final CommonFormObject form = createMock("Form", CommonFormObject.class);
		final PdfFilling pdfFilling = createMock("PdfFilling", PdfFilling.class);
		final Collection<PdfFilling> pdfFillings = Arrays.asList(pdfFilling);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(form.createPagingInfo()).andReturn(pagingInfo);
		EasyMock.expect(getRelatedDAO().findFillings(pagingInfo)).andReturn(pdfFillings);
		replayAll();

		final Collection<PdfFilling> actualPdfFillings = getRelatedService().findPdfFillings(form);

		assertSame("The PDF fillings are returned", pdfFillings, actualPdfFillings);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.RelatedService#findRelated(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRelated() {
		final long id = 89982l;
		final Related related = createMock("Related", Related.class);
		EasyMock.expect(getRelatedDAO().findRelatedById(id)).andReturn(related);
		replayAll();

		final Related actualRelated = getRelatedService().findRelated(id);

		assertSame("The related object is returned", related, actualRelated);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.RelatedService#findRelated(long)} for the case where there are no related objects.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testFindRelated_noRelated() {
		final long id = 89982l;
		EasyMock.expect(getRelatedDAO().findRelatedById(id)).andReturn(null);
		replayAll();

		final Related actualRelated = getRelatedService().findRelated(id);

		assertNull("No related object is returned", actualRelated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.RelatedService#savePdfFilling(com.bearcode.ovf.model.questionnaire.PdfFilling)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testSavePdfFilling() {
		PdfFilling filling = createMock("Filling", PdfFilling.class);
		getRelatedDAO().makePersistent(filling);
		replayAll();
		
		getRelatedService().savePdfFilling(filling);
		
		verifyAll();
	}

	/**
	 * Creates a related service.
	 * 
	 * @author IanBrown
	 * @return the related service.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private RelatedService createRelatedService() {
		return new RelatedService();
	}

	/**
	 * Gets the related DAO.
	 * 
	 * @author IanBrown
	 * @return the related DAO.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private RelatedDAO getRelatedDAO() {
		return relatedDAO;
	}

	/**
	 * Gets the related service.
	 * 
	 * @author IanBrown
	 * @return the related service.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private RelatedService getRelatedService() {
		return relatedService;
	}

	/**
	 * Sets the related DAO.
	 * 
	 * @author IanBrown
	 * @param relatedDAO
	 *            the related DAO to set.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private void setRelatedDAO(final RelatedDAO relatedDAO) {
		this.relatedDAO = relatedDAO;
	}

	/**
	 * Sets the related service.
	 * 
	 * @author IanBrown
	 * @param relatedService
	 *            the related service to set.
	 * @since Jun 11, 2012
	 * @version Jun 11, 2012
	 */
	private void setRelatedService(final RelatedService relatedService) {
		this.relatedService = relatedService;
	}
}
