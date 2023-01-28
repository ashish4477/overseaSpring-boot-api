package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.questionnaire.PdfFormTrack;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Date: 06.10.14
 * Time: 22:15
 *
 * @author Leonid Ginzburg
 */
@Repository
public class PdfFormTrackDAO extends BearcodeDAO {

    public PdfFormTrack findById( long id ) {
        return getHibernateTemplate().get( PdfFormTrack.class, id );
    }

    @SuppressWarnings("unchecked")
    public List<PdfFormTrack> findByHash( String hash ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( PdfFormTrack.class )
                .add( Restrictions.eq( "hash",hash ) );
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<PdfFormTrack> findOldTracks(Date expiredDate ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( PdfFormTrack.class )
                .add( Restrictions.le( "created", expiredDate ));
        return criteria.list();
    }
}
