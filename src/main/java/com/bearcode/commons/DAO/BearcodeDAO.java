package com.bearcode.commons.DAO;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2007
 * Time: 4:10:27 PM
 * @author Leonid Ginzburg
 */
public class BearcodeDAO  extends HibernateDaoSupport {

    public BearcodeDAO() {
    }

    @Autowired
    public void setPersistentResource( SessionFactory sessionFactory ) {
        super.setSessionFactory( sessionFactory );
    }

    public void makePersistent(Object object) throws DataAccessException {
        try {
            getHibernateTemplate().saveOrUpdate(object);
        } catch (HibernateException e) {
            throw getHibernateTemplate().convertHibernateAccessException(e);
        }
    }

    public void makeTransient(Object object) throws DataAccessException {
        try {
            getHibernateTemplate().delete(object);
        } catch (HibernateException e) {
            throw getHibernateTemplate().convertHibernateAccessException(e);
        }
    }

    public void makeAllTransient(Collection objects) throws DataAccessException {
        try {
            getHibernateTemplate().deleteAll(objects);
        } catch (HibernateException e) {
            throw getHibernateTemplate().convertHibernateAccessException(e);
        }
    }

    public void makeAllPersistent(Collection objects) throws DataAccessException {
        try {
            if(!objects.isEmpty()){
                getHibernateTemplate().saveOrUpdateAll(objects);
            }
        } catch (HibernateException e) {
            throw getHibernateTemplate().convertHibernateAccessException(e);
        }
    }

    public Object merge(Object object) throws DataAccessException {
        try {
            return getHibernateTemplate().merge(object);
        } catch (HibernateException e) {
            throw getHibernateTemplate().convertHibernateAccessException(e);
        }
    }

    /**
     * Adjust paging info according actual number of rows.
     * FirstResult should be greater than 0 and lesser than number of rows.
     * @param pagingInfo Adjusting info.
     * @param rows Actual number of rows.
     */
    protected void adjustPagingInfo(PagingInfo pagingInfo, Long rows) {
        if ( pagingInfo != null && pagingInfo.getMaxResults() > 0 ) {
            int firstResult = pagingInfo.getFirstResult();
            int maxResults = pagingInfo.getMaxResults();
            if ( firstResult < 0 )
                pagingInfo.setFirstResult( 0 );
            if ( firstResult >= rows ) {
                firstResult = (int) (Math.floor(rows / maxResults) * maxResults);
                if (firstResult >= rows) firstResult -= maxResults;
                pagingInfo.setFirstResult(firstResult);
            }
            pagingInfo.setActualRows(rows);
        }
    }

    /**
     * Change the given criteria so that it return number of rows in the query and execute it.
     * Adjust PagingInfo according result.
     * @param criteria Criteria for searching elements.
     * @param pagingInfo Paging info - how many elements are on a page and what's page number to search.
     * @return Number of rows in the query.
     */
    public Long calculateRows( DetachedCriteria criteria, PagingInfo pagingInfo) {
        try {
            Long rows = (Long) getHibernateTemplate()
                    .findByCriteria(criteria.setProjection( Projections.distinct( Projections.rowCount()) )).iterator().next();
            adjustPagingInfo(pagingInfo, rows );
            return rows;
        } catch (HibernateException e) {
            throw getHibernateTemplate().convertHibernateAccessException(e);
        }
    }

    /**
     * Execute the given criteria and apply paging info.
     * @param criteria Criteria for searching elements.
     * @param pagingInfo Paging info - how many elements are on a page and what's page number to search.
     * @return List of elements of the given page
     */
    public List findBy( DetachedCriteria criteria, PagingInfo pagingInfo) {
        try {

            int firstResult = -1;
            int maxResults = -1;
            if (pagingInfo != null) {
                String[] orderFields = pagingInfo.getOrderFields();
                boolean ascending = pagingInfo.isAscending();
                firstResult = pagingInfo.getFirstResult();
                maxResults = pagingInfo.getMaxResults();
                if (orderFields != null) {
                    for (String orderField : orderFields) {
                        if (ascending) {
                            criteria.addOrder(Order.asc(orderField));
                        } else {
                            criteria.addOrder(Order.desc(orderField));
                        }
                    }
                }
            }
            return  getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
        } catch (HibernateException e) {
            throw getHibernateTemplate().convertHibernateAccessException(e);
        }
    }

    public Collection findBy( DetachedCriteria criteria) {
        return findBy(criteria, null);
    }

    public void refresh(Object object) {
        getHibernateTemplate().refresh(object);
    }

    public void flush() {
        getHibernateTemplate().flush();
    }

    public void evict( Object obj ) {
        getHibernateTemplate().evict( obj );
    }


}
