package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.model.questionnaire.PdfFilling;
import com.bearcode.ovf.model.questionnaire.Related;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 18, 2007
 * Time: 6:16:37 PM
 * @author Leonid Ginzburg
 */
@Repository
public class RelatedDAO extends BearcodeDAO {

    public Related findRelatedById(long id) {
        return getHibernateTemplate().get(Related.class, id);
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PdfFilling> findFillings() {
        DetachedCriteria criteria = DetachedCriteria.forClass( PdfFilling.class );
        return findBy( criteria );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PdfFilling> findFillings(PagingInfo paging) {
        DetachedCriteria criteria = DetachedCriteria.forClass( PdfFilling.class );
        calculateRows( criteria, paging );
        criteria = DetachedCriteria.forClass( PdfFilling.class );
        criteria.addOrder( Order.asc( "inPdfName" ) );

        return findBy( criteria, paging );
    }

}
