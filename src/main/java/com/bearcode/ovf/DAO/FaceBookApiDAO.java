package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.FaceBookApi;
import com.bearcode.ovf.model.common.State;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: Jan 20, 2012
 * Time: 4:35:26 PM
 * @author Daemmon Hughes
 */
@Repository
public class FaceBookApiDAO extends BearcodeDAO {

    public FaceBookApi getByDomain( String domain ){
        DetachedCriteria criteria = DetachedCriteria.forClass( FaceBookApi.class )
                .add( Restrictions.eq( "domain", domain ))
                .add( Restrictions.eq( "active", true ));
        List result = getHibernateTemplate().findByCriteria( criteria );
        return result.size() != 0 ?  (FaceBookApi) result.iterator().next() : null ;
    }
 }
