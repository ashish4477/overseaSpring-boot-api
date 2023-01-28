package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.express.FedexLabel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 14, 2008
 * Time: 5:26:09 PM
 * @author Leonid Ginzburg
 */
@Repository
public class FedexLabelDAO extends BearcodeDAO {

    public FedexLabel getLabel( long id ) {
        return (FedexLabel) getHibernateTemplate().get( FedexLabel.class, id );
    }

    public FedexLabel getLabelByNumber( String number ) {
        Criteria criteria = getSession().createCriteria( FedexLabel.class )
                .add( Restrictions.eq("trackingNumber", number) )
                .setMaxResults(1);
        return (FedexLabel) criteria.uniqueResult();
    }

    public Collection getLabelsForUser(OverseasUser user) {
        return getLabelsForUser(user, false);       
    }

    public Collection getLabelsForUserUnexpired(OverseasUser user) {
        return getLabelsForUser(user, true);       
    }

    protected Collection getLabelsForUser(OverseasUser user, boolean onlyUnexpired) {
        Criteria criteria = getSession().createCriteria( FedexLabel.class )
                .add( Restrictions.eq("owner", user) );
        if(onlyUnexpired){
        	long minTime = new Date().getTime() - FedexLabel.EXPIRE_PERIOD;
        	criteria.add(Expression.gt("created", new Date(minTime)));
        }
        return criteria.list();       
    }
}
