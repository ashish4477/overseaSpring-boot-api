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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.express.CountryDescription;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link FedexCountryDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 18, 2012
 * @version Jul 18, 2012
 */
public final class FedexCountryDAOTest extends BearcodeDAOCheck<FedexCountryDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FedexCountryDAO#getActiveFedexCountries()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetActiveFedexCountries() {
		final CountryDescription activeFedexCountry = createMock("ActiveFedexCountry", CountryDescription.class);
		final List<CountryDescription> activeFedexCountries = Arrays.asList(activeFedexCountry);
		EasyMock.expect(
				getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(-1), EasyMock.eq(-1)))
				.andReturn(activeFedexCountries);
		replayAll();

		final Collection<CountryDescription> actualActiveFedexCountries = getBearcodeDAO().getActiveFedexCountries();

		assertSame("The active FedEd countries are returned", activeFedexCountries, actualActiveFedexCountries);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FedexCountryDAO#getFedexCountries()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetFedexCountries() {
		final CountryDescription fedexCountry = createMock("FedexCountry", CountryDescription.class);
		final List<CountryDescription> fedexCountries = Arrays.asList(fedexCountry);
		EasyMock.expect(
				getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(-1), EasyMock.eq(-1)))
				.andReturn(fedexCountries);
		replayAll();

		final Collection<CountryDescription> actualFedexCountries = getBearcodeDAO().getFedexCountries();

		assertSame("The FedEx countries are returned", fedexCountries, actualFedexCountries);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FedexCountryDAO#getFedexCountry(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetFedexCountry() {
		final long id = 3467l;
		final CountryDescription fedexCountry = createMock("FedexCountry", CountryDescription.class);
		EasyMock.expect(getHibernateTemplate().get(CountryDescription.class, id)).andReturn(fedexCountry);
		replayAll();

		final CountryDescription actualFedexCountry = getBearcodeDAO().getFedexCountry(id);

		assertSame("The FedEx country is returned", fedexCountry, actualFedexCountry);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FedexCountryDAO#getFedexCountryByName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetFedexCountryByName() {
		final String countryName = "Country Name";
		final CountryDescription fedexCountry = createMock("FedexCountry", CountryDescription.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(CountryDescription.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(fedexCountry);
		replayAll();
		
		final CountryDescription actualFedexCountry = getBearcodeDAO().getFedexCountryByName(countryName);
		
		assertSame("The FedEx country is returned", fedexCountry, actualFedexCountry);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final FedexCountryDAO createBearcodeDAO() {
		return new FedexCountryDAO();
	}
}
