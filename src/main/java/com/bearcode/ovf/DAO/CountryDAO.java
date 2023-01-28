package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.Country;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2007
 * Time: 4:35:26 PM
 * @author Leonid Ginzburg
 */
@Repository
public class CountryDAO extends BearcodeDAO {

	/**
	 *
	 * @return Collection of all active countries
	 */
    public Collection<Country> getAllCountries() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Country.class)
				.add(Restrictions.ge("active", true))
                .addOrder(Order.asc("name"));
        return findBy( criteria );
    }

    public Country getById( long id ) {
        return (Country) getHibernateTemplate().get( Country.class, id );
    }

    public Country getByName( String name ){
        DetachedCriteria criteria = DetachedCriteria.forClass( Country.class )
                .add( Restrictions.eq( "name", name ));
        List result = getHibernateTemplate().findByCriteria( criteria );
        return result.size() != 0 ?  (Country) result.iterator().next() : null ;
    }

    public Country getByAbbreviation( String name ){
        DetachedCriteria criteria = DetachedCriteria.forClass( Country.class )
                .add( Restrictions.eq( "abbr", name ));
        List result = getHibernateTemplate().findByCriteria( criteria );
        return result.size() != 0 ?  (Country) result.iterator().next() : null ;
    }
}
