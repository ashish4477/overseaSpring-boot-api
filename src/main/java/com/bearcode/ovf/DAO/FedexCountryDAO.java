package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.express.CountryDescription;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2008
 * Time: 7:24:20 PM
 * @author Leonid Ginzburg
 */
@Repository
public class FedexCountryDAO  extends BearcodeDAO {

    public Collection<CountryDescription> getFedexCountries() {
        DetachedCriteria criteria = DetachedCriteria.forClass( CountryDescription.class )
                 .addOrder(Order.asc("name"));
        return findBy( criteria );
    }

    public Collection<CountryDescription> getActiveFedexCountries() {
        DetachedCriteria criteria = DetachedCriteria.forClass( CountryDescription.class )
                 .add(Restrictions.gt("lastDate", new Date()))
        		 .addOrder(Order.asc("name"));
        return findBy( criteria );
    }

    public CountryDescription getFedexCountry( long id ) {
        return (CountryDescription) getHibernateTemplate().get( CountryDescription.class, id );
    }

    public CountryDescription getFedexCountryByName(String countryName) {
        Criteria criteria = getSession().createCriteria( CountryDescription.class )
                 .add(Restrictions.like("name", countryName))
                .setMaxResults(1);
        return (CountryDescription) criteria.uniqueResult();
    }
}
