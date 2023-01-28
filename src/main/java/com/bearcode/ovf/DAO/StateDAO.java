package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.State;
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
public class StateDAO extends BearcodeDAO {

    public Collection getAllStates() {
        DetachedCriteria criteria = DetachedCriteria.forClass(State.class)
                .addOrder(Order.asc("name"));
        return findBy( criteria );
    }

    public State getById( long id ) {
        return (State) getHibernateTemplate().get( State.class, id );
    }

    public State getByName( String name ){
        DetachedCriteria criteria = DetachedCriteria.forClass( State.class )
                .add( Restrictions.like( "name", name ));
        List result = getHibernateTemplate().findByCriteria( criteria );
        return result.size() != 0 ?  (State) result.iterator().next() : null ;
    }

    public State getByAbbreviation( String name ){
        DetachedCriteria criteria = DetachedCriteria.forClass( State.class )
                .add( Restrictions.eq( "abbr", name ));
        List result = getHibernateTemplate().findByCriteria( criteria );
        return result.size() != 0 ?  (State) result.iterator().next() : null ;
    }
}
