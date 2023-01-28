package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.questionnaire.PageType;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author Alexey Polyakov
 *         Date: Aug 14, 2007
 *         Time: 1:39:41 PM
 */
@Repository
@SuppressWarnings( "unchecked" )
public class QuestionnairePageDAO extends BearcodeDAO {

    public List<QuestionnairePage> findPages() {
        return getSession().createCriteria(QuestionnairePage.class)
                .addOrder( Order.desc( "type" ) )
                .addOrder(Order.asc("number"))
                .list();
    }

    public List<QuestionnairePage> findPages(PageType type) {
        return getSession().createCriteria(QuestionnairePage.class)
                .add( Restrictions.eq( "type", type ))
                .addOrder(Order.asc("number"))
                .list();
    }

    public QuestionnairePage findById(long id) {
        return (QuestionnairePage) getSession().get(QuestionnairePage.class, id);
    }

    public List<QuestionnairePage> findPagesAfterPage(int number, PageType type ) {
        return getSession().createCriteria(QuestionnairePage.class)
                .add(Restrictions.ge("number", number))
                .add( Restrictions.eq( "type", type ))
                .addOrder(Order.asc("number")).list();
    }

    public int getLastPageNumber() {
        return ((Long)getSession().createCriteria(QuestionnairePage.class)
        		.setProjection(Projections.countDistinct("id"))
        		.uniqueResult())
        		.intValue();
    }

    public int countPages( PageType type) {
        return ((Long)getSession().createCriteria( QuestionnairePage.class )
                .add( Restrictions.eq( "type", type ) )
                .setProjection( Projections.countDistinct("id") )
                .uniqueResult() )
                .intValue();
    }

    public QuestionnairePage findPageByNumber(int page, PageType type ) {
        return (QuestionnairePage) getSession().createCriteria(QuestionnairePage.class)
                .add(Restrictions.eq("number", page))
                .add( Restrictions.eq( "type", type ))
                .uniqueResult();
    }

    public int findLastFilledPage(Collection<Long> fieldIds) {
        DetachedCriteria criteria = DetachedCriteria.forClass( QuestionnairePage.class )
                .createAlias("questions", "questions")
                .createAlias("questions.variants", "variants")
                .createAlias("variants.fields", "fields")
                .add(Restrictions.in("fields.id", fieldIds ))
                .setProjection(Projections.max("stepNumber"));
        Collection<Integer> pages = getHibernateTemplate().findByCriteria(criteria);
        if ( pages != null && pages.size() > 0 )
            return pages.iterator().next();
        else
            return 0;
    }

    public Map<Integer, Collection<Integer>> defineDependencies( PageType type ) {
        Query query = getSession().createQuery(
                "select distinct fromPage.number, toPage.number " +
                "from QuestionDependency qd, QuestionVariant variant " +
                "join qd.dependsOn.page fromPage " +
                "join variant.question.page toPage " +
                "where variant = qd.dependent " +
                        "and fromPage.type = :type " +
                        "and toPage.type = :type" );
        query.setParameter( "type", type );

        Map<Integer,Collection<Integer>> crossPages = new HashMap<Integer, Collection<Integer>>();
        for ( Object o : query.list() ) {
            Object[] row = (Object[]) o;
            Integer fromNumber = (Integer) row[0];
            Integer toNumber = (Integer) row[1];
            putIntoCrossMap( crossPages, fromNumber, toNumber );
            putIntoCrossMap( crossPages, toNumber, fromNumber );
        }
        return crossPages;
    }

    private void putIntoCrossMap( Map<Integer,Collection<Integer>> crossPages, Integer first, Integer second ){
        Collection<Integer> cross = crossPages.get( first );
        if ( cross == null ) {
            cross = new HashSet<Integer>();
            crossPages.put( first, cross );
        }
        cross.add( second );
    }
}
