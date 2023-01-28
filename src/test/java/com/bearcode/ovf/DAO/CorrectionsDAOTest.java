/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.model.eod.CorrectionsLeo;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link CorrectionsDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 18, 2012
 * @version Jul 18, 2012
 */
public final class CorrectionsDAOTest extends BearcodeDAOCheck<CorrectionsDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CorrectionsDAO#findCorrectionByStatus(int, com.bearcode.commons.DAO.PagingInfo)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindCorrectionByStatus() {
		final int status = CorrectionsLeo.STATUS_ACCEPTED;
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		final CorrectionsLeo correction = createMock("Correction", CorrectionsLeo.class);
		final List<CorrectionsLeo> corrections = Arrays.asList(correction);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject()))
				.andReturn(Arrays.asList(1l));
		EasyMock.expect(pagingInfo.getMaxResults()).andReturn(10).atLeastOnce();
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(1);
		pagingInfo.setFirstResult(0);
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(0);
		pagingInfo.setActualRows(1);
		final String[] orderFields = new String[] { "field" };
		EasyMock.expect(pagingInfo.getOrderFields()).andReturn(orderFields);
		EasyMock.expect(pagingInfo.isAscending()).andReturn(true);
		EasyMock.expect(
				getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(0), EasyMock.eq(10)))
				.andReturn(corrections);
		replayAll();

		final Collection actualCorrections = getBearcodeDAO().findCorrectionByStatus(status, pagingInfo);

		assertSame("The corrections are returned", corrections, actualCorrections);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CorrectionsDAO#findNewCorrections()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindNewCorrections() {
		final CorrectionsLeo newCorrection = createMock("NewCorrection", CorrectionsLeo.class);
		final List<CorrectionsLeo> newCorrections = Arrays.asList(newCorrection);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(newCorrections);
		replayAll();

		final Collection<CorrectionsLeo> actualNewCorrections = getBearcodeDAO().findNewCorrections();

		assertSame("The new corrections are returned", newCorrections, actualNewCorrections);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CorrectionsDAO#getById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetById() {
		final long id = 276l;
		final CorrectionsLeo correction = createMock("Correction", CorrectionsLeo.class);
		EasyMock.expect(getHibernateTemplate().get(CorrectionsLeo.class, id)).andReturn(correction);
		replayAll();
		
		final CorrectionsLeo actualCorrection = getBearcodeDAO().getById(id);
		
		assertSame("The correction is returned", correction, actualCorrection);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final CorrectionsDAO createBearcodeDAO() {
		return new CorrectionsDAO();
	}
}
