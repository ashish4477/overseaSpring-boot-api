package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.model.eod.CorrectionsLeo;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 6, 2007
 * Time: 10:06:08 PM
 *
 * @author Leonid Ginzburg
 */
@Repository
@SuppressWarnings("unchecked")
public class CorrectionsDAO extends BearcodeDAO {

    public Collection<CorrectionsLeo> findNewCorrections() {
        DetachedCriteria criteria = DetachedCriteria.forClass( CorrectionsLeo.class )
                .add( Restrictions.eq( "status", CorrectionsLeo.STATUS_SENT ) )
                .addOrder( Order.desc( "created" ) );
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public Collection findCorrectionByStatus( int status, PagingInfo pagingInfo ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( CorrectionsLeo.class )
                .add( Restrictions.eq( "status", status ) )
                .addOrder( Order.desc( "created" ) );
        calculateRows( criteria, pagingInfo );
        criteria = DetachedCriteria.forClass( CorrectionsLeo.class )  // re-create criteria
                .add( Restrictions.eq( "status", status ) )
                .addOrder( Order.desc( "created" ) );
        return findBy( criteria, pagingInfo );
    }

    public CorrectionsLeo getById( long correctionId ) {
        return (CorrectionsLeo) getHibernateTemplate().get( CorrectionsLeo.class, correctionId );
    }

    public Collection<CorrectionsLeo> findForRegions( Collection<LocalOfficial> localOfficials ) {
        Criteria criteria = getSession().createCriteria( CorrectionsLeo.class )
                .add( Restrictions.in( "correctionFor", localOfficials ) );
        return criteria.list();
    }

    public void makePersistent( Officer object ) throws DataAccessException {
        try {
            getHibernateTemplate().saveOrUpdate( "CorrectionOfficer", object );
        } catch (HibernateException e) {
            throw convertHibernateAccessException(e);
        }
    }
}
