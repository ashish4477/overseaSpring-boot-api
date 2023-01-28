package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.mail.MailingAddress;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 2, 2007
 * Time: 6:02:44 PM
 */
@Repository
public class MailingAddressDAO extends BearcodeDAO {

    /**
     * Finds all mailing list entries.
     *
     * @return the mailing list entries.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public List<MailingAddress> findAll() {
        return getHibernateTemplate().loadAll( MailingAddress.class );
    }

    public MailingAddress getByEmail( final String name ) {
        final DetachedCriteria criteria = DetachedCriteria.forClass( MailingAddress.class ).add( Restrictions.eq( "email", name ) );
        final List result = getHibernateTemplate().findByCriteria( criteria );
        return result.size() != 0 ? (MailingAddress) result.iterator().next() : null;
    }

    /**
     * Gets a list of mailing list entries for the specified URL.
     *
     * @param url the URL.
     * @return the list of mailing list entries.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public List<MailingAddress> getByUrl( final String url ) {
        final DetachedCriteria criteria = DetachedCriteria.forClass( MailingAddress.class ).add( Restrictions.eq( "url", url ) );
        @SuppressWarnings("unchecked")
        final List<MailingAddress> mailingAddresses = getHibernateTemplate().findByCriteria( criteria );
        return mailingAddresses;
    }

    /**
     * Find addresses which is not connected to any mailing list
     * (excluding
     * @return List of Mailing Address
     */
    public List<MailingAddress> findLostAddresses( String face, String state ) {
        StringBuilder sqlQuery = new StringBuilder(
                "select * from mailing_list_address ma " +
                "left join mailing_list_link ml on ml.mailing_address_id = ma.id " +
                "where ml.id is null "
        );
        if ( face != null && !face.isEmpty() ) {
            sqlQuery.append( "and ma.url = :faceUrl " );
        }
        if ( state != null && !state.isEmpty() ) {
            sqlQuery.append( "and ma.voting_state_name = :stateName " );
        }
        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createSQLQuery( sqlQuery.toString() )
                .addEntity( MailingAddress.class );
        if ( face != null && !face.isEmpty() ) {
            query.setString( "faceUrl", face );
        }
        if ( state != null && !state.isEmpty() ) {
            query.setString( "stateName", state );
        }
        return query.list();

    }

    /**
     * Count number of addresses which is not connected to any mailing list
     * (excluding
     * @return List of Mailing Address
     */
    public long countLostAddresses() {
        String sqlQuery = "select count(ma.id) as address_count from mailing_list_address ma " +
                "left join mailing_list_link ml on ml.mailing_address_id = ma.id " +
                "where ml.id is null ";
        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery( sqlQuery )
                .addScalar( "address_count", LongType.INSTANCE );
        return (Long) query.uniqueResult();

    }

    /**
     * Find SHS's and states of addresses which is not connected to any mailing list
     * (excluding
     * @return List of Mailing Address
     */
    public List findFacesOfLostAddresses( String face ) {
        StringBuilder sqlQuery = new StringBuilder(
                "select ma.url, ma.voting_state_name, count(ma.email) as address_count from mailing_list_address ma " +
                        "left join mailing_list_link ml on ml.mailing_address_id = ma.id " +
                        "where ml.id is null "
        );
        if ( face != null && !face.isEmpty() ) {
            sqlQuery.append( "and ma.url = :faceUrl " );
        }
        sqlQuery.append( "GROUP BY url, voting_state_name" );

        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery( sqlQuery.toString() )
                .addScalar( "url", StringType.INSTANCE )
                .addScalar( "voting_state_name", StringType.INSTANCE )
                .addScalar( "address_count", LongType.INSTANCE );
        if ( face != null && !face.isEmpty() ) {
            query.setString( "faceUrl", face );
        }
        return query.list();

    }

}
