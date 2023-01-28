/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.Country;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link CountryDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 18, 2012
 * @version Jul 18, 2012
 */
public final class CountryDAOTest extends BearcodeDAOCheck<CountryDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CountryDAO#getAllCountries()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetAllCountries() {
		final Country country = createMock("Country", Country.class);
		final List<Country> countries = Arrays.asList(country);
		EasyMock.expect(
				getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(-1), EasyMock.eq(-1)))
				.andReturn(countries);
		replayAll();

		final Collection<Country> actualCountries = getBearcodeDAO().getAllCountries();

		assertSame("The countries are returned", countries, actualCountries);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CountryDAO#getByAbbreviation(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetByAbbreviation() {
		final String name = "USA";
		final Country country = createMock("Country", Country.class);
		final List<Country> countries = Arrays.asList(country);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(countries);
		replayAll();

		final Country actualCountry = getBearcodeDAO().getByAbbreviation(name);

		assertSame("The country is returned", country, actualCountry);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CountryDAO#getByAbbreviation(java.lang.String)} for the case where there is no
	 * match.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetByAbbreviation_noMatch() {
		final String name = "USA";
		final List<Country> countries = new LinkedList<Country>();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(countries);
		replayAll();

		final Country actualCountry = getBearcodeDAO().getByAbbreviation(name);

		assertNull("No country is returned", actualCountry);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CountryDAO#getById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetById() {
		final long id = 7625l;
		final Country country = createMock("Country", Country.class);
		EasyMock.expect(getHibernateTemplate().get(Country.class, id)).andReturn(country);
		replayAll();

		final Country actualCountry = getBearcodeDAO().getById(id);

		assertSame("The country is returned", country, actualCountry);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CountryDAO#getByName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetByName() {
		final String name = "United States of America";
		final Country country = createMock("Country", Country.class);
		final List<Country> countries = Arrays.asList(country);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(countries);
		replayAll();

		final Country actualCountry = getBearcodeDAO().getByName(name);

		assertSame("The country is returned", country, actualCountry);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.CountryDAO#getByName(java.lang.String)} for the case where there is no match.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetByName_noMatch() {
		final String name = "United States of America";
		final List<Country> countries = new LinkedList<Country>();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(countries);
		replayAll();

		final Country actualCountry = getBearcodeDAO().getByName(name);

		assertNull("No country is returned", actualCountry);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final CountryDAO createBearcodeDAO() {
		return new CountryDAO();
	}

}
