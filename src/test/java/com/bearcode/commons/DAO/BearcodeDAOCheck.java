/**
 * 
 */
package com.bearcode.commons.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bearcode.ovf.model.common.OverseasUser;

/**
 * Abstract test for implementations of {@link BearcodeDAO}.
 * 
 * @author IanBrown
 * 
 * @param <D>
 *            the type of bear code DAO to test.
 * @since Dec 12, 2011
 * @version Sep 18, 2012
 */
public abstract class BearcodeDAOCheck<D extends BearcodeDAO> extends EasyMockSupport {

	/**
	 * the original security context.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	private SecurityContext originalSecurityContext;

	/**
	 * the security context.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	private SecurityContext securityContext;

	/**
	 * the bearcode DAO to test.
	 * 
	 * @author IanBrown
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private D bearcodeDAO;

	/**
	 * the Hibernate template to use.
	 * 
	 * @author IanBrown
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private HibernateTemplate hibernateTemplate;

	/**
	 * the persistent resource.
	 * 
	 * @author IanBrown
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private SessionFactory persistentResource;

	/**
	 * Sets up the report DAO to test.
	 * 
	 * @author IanBrown
	 * @since Dec 12, 2011
	 * @version Feb 8, 2012
	 */
	@Before
	public final void setUpReportDAO() {
		setSecurityContext(createMock("SecurityContext", SecurityContext.class));
		setPersistentResource(createMock("PersistentResource", SessionFactory.class));
		setHibernateTemplate(createMock("HibernateTemplate", HibernateTemplate.class));
		setBearcodeDAO(createBearcodeDAO());
		setOriginalSecurityContext(SecurityContextHolder.getContext());
		SecurityContextHolder.setContext(getSecurityContext());
		bearcodeDAO.setPersistentResource(getPersistentResource());
		bearcodeDAO.setHibernateTemplate(getHibernateTemplate());
	}

	/**
	 * Tears down the set up for testing the report DAO.
	 * 
	 * @author IanBrown
	 * @since Dec 12, 2011
	 * @version Feb 8, 2012
	 */
	@After
	public final void tearDownReportDAO() {
		setBearcodeDAO( null );
		setHibernateTemplate( null );
		setPersistentResource( null );
		SecurityContextHolder.setContext(getOriginalSecurityContext());
		setSecurityContext( null );
		setOriginalSecurityContext( null );
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.BearcodeDAO#calculateRows(org.hibernate.criterion.DetachedCriteria, com.bearcode.commons.DAO.PagingInfo)}
	 * for the case where paging info is provided, and the first result is larger than the number of rows.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	@SuppressWarnings("rawtypes")
	public final void testCalculateRows_largeFirstResult() {
		final DetachedCriteria criteria = createMock("Criteria", DetachedCriteria.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(criteria.setProjection((Projection) EasyMock.anyObject())).andReturn(criteria);
		final long rows = 12l;
		final List results = Arrays.asList(rows);
		EasyMock.expect(getHibernateTemplate().findByCriteria(criteria)).andReturn(results);
		EasyMock.expect(pagingInfo.getMaxResults()).andReturn((int) rows).times(2);
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn( (int) rows + 1 );
		pagingInfo.setFirstResult( 0 );
		pagingInfo.setActualRows( rows );
		replayAll();

		final long actualRows = getBearcodeDAO().calculateRows(criteria, pagingInfo);

		assertEquals( "The number of rows is returned", rows, actualRows );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.BearcodeDAO#calculateRows(org.hibernate.criterion.DetachedCriteria, com.bearcode.commons.DAO.PagingInfo)}
	 * for the case where paging info is provided, and the first result is negative.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	@SuppressWarnings("rawtypes")
	public final void testCalculateRows_negativeFirstResult() {
		final DetachedCriteria criteria = createMock("Criteria", DetachedCriteria.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(criteria.setProjection((Projection) EasyMock.anyObject())).andReturn(criteria);
		final long rows = 12l;
		final List results = Arrays.asList(rows);
		EasyMock.expect(getHibernateTemplate().findByCriteria(criteria)).andReturn(results);
		EasyMock.expect(pagingInfo.getMaxResults()).andReturn((int) rows).times(2);
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(-1);
		pagingInfo.setFirstResult(0);
		pagingInfo.setActualRows(rows);
		replayAll();

		final long actualRows = getBearcodeDAO().calculateRows(criteria, pagingInfo);

		assertEquals("The number of rows is returned", rows, actualRows);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.BearcodeDAO#calculateRows(org.hibernate.criterion.DetachedCriteria, com.bearcode.commons.DAO.PagingInfo)}
	 * for the case where no paging info is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	@SuppressWarnings("rawtypes")
	public final void testCalculateRows_noPagingInfo() {
		final DetachedCriteria criteria = createMock("Criteria", DetachedCriteria.class);
		EasyMock.expect(criteria.setProjection( (Projection) EasyMock.anyObject() )).andReturn(criteria);
		final long rows = 12l;
		final List results = Arrays.asList(rows);
		EasyMock.expect(getHibernateTemplate().findByCriteria(criteria)).andReturn( results );
		replayAll();

		final long actualRows = getBearcodeDAO().calculateRows(criteria, null);

		assertEquals("The number of rows is returned", rows, actualRows);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#evict(java.lang.Object)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testEvict() {
		final Object obj = createMock( "Obj", Object.class );
		getHibernateTemplate().evict( obj );
		replayAll();

		getBearcodeDAO().evict( obj );

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#findBy(org.hibernate.criterion.DetachedCriteria)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindByDetachedCriteria() {
		final DetachedCriteria criteria = createMock("Criteria", DetachedCriteria.class);
		final List results = new ArrayList();
		EasyMock.expect( getHibernateTemplate().findByCriteria( criteria, -1, -1 ) ).andReturn( results );
		replayAll();

		final Collection actualResults = getBearcodeDAO().findBy(criteria);

		assertSame( "The resutls are returned", results, actualResults );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.BearcodeDAO#findBy(org.hibernate.criterion.DetachedCriteria, com.bearcode.commons.DAO.PagingInfo)}
	 * for the case where there the fields are to ascend.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindByDetachedCriteriaPagingInfo_ascending() {
		final DetachedCriteria criteria = createMock("Criteria", DetachedCriteria.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		final String field1 = "field1";
		final String field2 = "field2";
		final String[] fields = { field1, field2 };
		EasyMock.expect(pagingInfo.getOrderFields()).andReturn(fields);
		EasyMock.expect(pagingInfo.isAscending()).andReturn(true);
		final int firstResult = 23;
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(firstResult);
		final int maxResults = 978;
		EasyMock.expect(pagingInfo.getMaxResults()).andReturn(maxResults);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		final List results = new ArrayList();
		EasyMock.expect(getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults)).andReturn(results);
		replayAll();

		final Collection actualResults = getBearcodeDAO().findBy(criteria, pagingInfo);

		assertSame("The resutls are returned", results, actualResults);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.BearcodeDAO#findBy(org.hibernate.criterion.DetachedCriteria, com.bearcode.commons.DAO.PagingInfo)}
	 * for the case where there the fields are to descend.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindByDetachedCriteriaPagingInfo_descending() {
		final DetachedCriteria criteria = createMock("Criteria", DetachedCriteria.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		final String field1 = "field1";
		final String field2 = "field2";
		final String[] fields = { field1, field2 };
		EasyMock.expect(pagingInfo.getOrderFields()).andReturn(fields);
		EasyMock.expect(pagingInfo.isAscending()).andReturn(false);
		final int firstResult = 23;
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(firstResult);
		final int maxResults = 978;
		EasyMock.expect(pagingInfo.getMaxResults()).andReturn(maxResults);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		final List results = new ArrayList();
		EasyMock.expect(getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults)).andReturn(results);
		replayAll();

		final Collection actualResults = getBearcodeDAO().findBy(criteria, pagingInfo);

		assertSame("The resutls are returned", results, actualResults);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.BearcodeDAO#findBy(org.hibernate.criterion.DetachedCriteria, com.bearcode.commons.DAO.PagingInfo)}
	 * for the case where there is no order for the fields.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindByDetachedCriteriaPagingInfo_noOrderFields() {
		final DetachedCriteria criteria = createMock("Criteria", DetachedCriteria.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(pagingInfo.getOrderFields()).andReturn(null);
		EasyMock.expect(pagingInfo.isAscending()).andReturn(false);
		final int firstResult = 3;
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(firstResult);
		final int maxResults = 10;
		EasyMock.expect(pagingInfo.getMaxResults()).andReturn(maxResults);
		final List results = new ArrayList();
		EasyMock.expect(getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults)).andReturn(results);
		replayAll();

		final Collection actualResults = getBearcodeDAO().findBy(criteria, pagingInfo);

		assertSame("The resutls are returned", results, actualResults);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#flush()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testFlush() {
		getHibernateTemplate().flush();
		replayAll();

		getBearcodeDAO().flush();

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#makeAllPersistent(java.util.Collection)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testMakeAllPersistent() {
		final Object object = createMock("Object", Object.class);
		final Collection objects = Arrays.asList(object);
		getHibernateTemplate().saveOrUpdateAll( objects );
		replayAll();

		getBearcodeDAO().makeAllPersistent( objects );

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#makeAllTransient(java.util.Collection)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testMakeAllTransient() {
		final Object object = createMock("Object", Object.class);
		final Collection objects = Arrays.asList(object);
		getHibernateTemplate().deleteAll( objects );
		replayAll();

		getBearcodeDAO().makeAllTransient( objects );

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#makePersistent(java.lang.Object)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testMakePersistent() {
		final Object object = createMock( "Object", Object.class );
		getHibernateTemplate().saveOrUpdate( object );
		replayAll();

		getBearcodeDAO().makePersistent( object );

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#makeTransient(java.lang.Object)} .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testMakeTransient() {
		final Object object = createMock("Object", Object.class);
		getHibernateTemplate().delete( object );
		replayAll();

		getBearcodeDAO().makeTransient( object );

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#merge(java.lang.Object)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testMerge() {
		final Object object = createMock("Object", Object.class);
		final Object mergedObject = createMock("MergedObject", Object.class);
		EasyMock.expect( getHibernateTemplate().merge( object ) ).andReturn( mergedObject );
		replayAll();

		final Object actualMergedObject = getBearcodeDAO().merge(object);

		assertSame( "The merged object is returned", mergedObject, actualMergedObject );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.BearcodeDAO#refresh(java.lang.Object)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testRefresh() {
		final Object object = createMock("Object", Object.class);
		getHibernateTemplate().refresh( object );
		replayAll();

		getBearcodeDAO().refresh( object );

		verifyAll();
	}

	/**
	 * Adds authentication to the security context.
	 * 
	 * @author IanBrown
	 * @return the authentication.
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	protected final Authentication addAuthenticationToSecurityContext() {
		final Authentication authentication = createMock("Authentication", Authentication.class);
		EasyMock.expect(getSecurityContext().getAuthentication()).andReturn(authentication).anyTimes();
		return authentication;
	}

	/**
	 * Adds criteria to the session.
	 * 
	 * @author IanBrown
	 * @param <T>
	 *            the type of class to work on.
	 * @param session
	 *            the session.
	 * @param criteriaClass
	 *            the class of object to work on.
	 * @return the criteria.
	 * @since Dec 12, 2011
	 * @version Jun 26, 2012
	 */
	protected final <T> Criteria addCriteriaToSession(final Session session, final Class<T> criteriaClass) {
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(criteriaClass)).andReturn(criteria);
		EasyMock.expect(criteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY )).andReturn(criteria).anyTimes();
		return criteria;
	}

	/**
	 * Adds criteria with a name to the session.
	 * 
	 * @author IanBrown
	 * @param <T>
	 *            the type of class to work on.
	 * @param session
	 *            the session.
	 * @param criteriaClass
	 *            the class of object to work on.
	 * @param name
	 *            the name to give the criteria.
	 * @return the criteria.
	 * @since Sep 18, 2011
	 * @version Sep 18, 2012
	 */
	protected final <T> Criteria addCriteriaToSession(final Session session, final Class<T> criteriaClass, final String name) {
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(criteriaClass, name)).andReturn(criteria);
		EasyMock.expect(criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)).andReturn(criteria).anyTimes();
		return criteria;
	}

	/**
	 * Adds the overseas user to the authentication.
	 * 
	 * @author IanBrown
	 * @param authentication
	 *            the authentication.
	 * @param user
	 *            the overseas user.
	 * @return the overseas user.
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	protected final OverseasUser addOverseasUserToAuthentication(final Authentication authentication, final OverseasUser user) {
		EasyMock.expect(authentication.getPrincipal()).andReturn(user).anyTimes();
		return user;
	}

	/**
	 * Adds results to a SQL query.
	 * 
	 * @author IanBrown
	 * @param sqlQuery
	 *            the SQL query.
	 * @return the results.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	protected final List addResultsToSQLQuery(final SQLQuery sqlQuery) {
		final List sqlResults = new ArrayList();
		EasyMock.expect( sqlQuery.list() ).andReturn( sqlResults );
		return sqlResults;
	}

	/**
	 * Adds a session to the Hibernate template.
	 * 
	 * @author IanBrown
	 * @return the session.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final Session addSessionToHibernateTemplate() {
		EasyMock.expect(getHibernateTemplate().isAllowCreate()).andReturn( true ).anyTimes();
		EasyMock.expect( getHibernateTemplate().getSessionFactory() ).andReturn( getPersistentResource() );
		EasyMock.expect(getHibernateTemplate().getEntityInterceptor()).andReturn( null ).anyTimes();
		EasyMock.expect(getHibernateTemplate().getJdbcExceptionTranslator()).andReturn( null ).anyTimes();
		final Session session = createMock("Session", Session.class);
		EasyMock.expect(getPersistentResource().openSession()).andReturn(session).anyTimes();
		EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(session).anyTimes();
		EasyMock.expect(session.getSessionFactory()).andReturn( getPersistentResource() ).anyTimes();
		return session;
	}

	/**
	 * Adds a session to the Hibernate template.
	 *
	 * @author IanBrown
	 * @return the session.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final Session addAnotherSessionToHibernateTemplate() {
		EasyMock.expect(getHibernateTemplate().isAllowCreate()).andReturn( true ).anyTimes();
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource()).anyTimes();
		EasyMock.expect(getHibernateTemplate().getEntityInterceptor()).andReturn(null).anyTimes();
		EasyMock.expect(getHibernateTemplate().getJdbcExceptionTranslator()).andReturn(null).anyTimes();
		final Session session = createMock("Session", Session.class);
		EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(session).anyTimes();
		EasyMock.expect(session.getSessionFactory()).andReturn(getPersistentResource()).anyTimes();
		return session;
	}

	/**
	 * Adds a SQL query for the query string to the session.
	 * 
	 * @author IanBrown
	 * @param session
	 *            the session.
	 * @param queryString
	 *            the query string.
	 * @return the SQL query.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final SQLQuery addSQLQueryToSession(final Session session, final String queryString) {
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect( session.createSQLQuery( queryString ) ).andReturn( sqlQuery );
		return sqlQuery;
	}

	/**
	 * Adds a Hibernate Query for the query string to the session.
	 *
	 * @author IanBrown
	 * @param session
	 *            the session.
	 * @param queryString
	 *            the query string.
	 * @return the SQL query.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final Query addQueryToSession(final Session session, final String queryString) {
		final Query query = createMock("Query", Query.class);
		EasyMock.expect(session.createQuery(queryString)).andReturn(query);
		return query;
	}

	/**
	 * Creates a bear code DAO of the type to test.
	 * 
	 * @author IanBrown
	 * @return the bear code DAO.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected abstract D createBearcodeDAO();

	/**
	 * Gets the bear code data access object.
	 * 
	 * @author IanBrown
	 * @return the report DAO.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final D getBearcodeDAO() {
		return bearcodeDAO;
	}

	/**
	 * Gets the hibernate template.
	 * 
	 * @author IanBrown
	 * @return the hibernate template.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	protected final HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Gets the persistent resource.
	 * 
	 * @author IanBrown
	 * @return the persistent resource.
	 * @since Dec 12, 2011
	 * @version Jun 25, 2012
	 */
	protected final SessionFactory getPersistentResource() {
		return persistentResource;
	}

	/**
	 * Gets the security context.
	 * 
	 * @author IanBrown
	 * @return the security context.
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	protected final SecurityContext getSecurityContext() {
		return securityContext;
	}

	/**
	 * Gets the original security context.
	 * 
	 * @author IanBrown
	 * @return the original security context.
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	private SecurityContext getOriginalSecurityContext() {
		return originalSecurityContext;
	}

	/**
	 * Sets the bear code data access object.
	 * 
	 * @author IanBrown
	 * @param bearcodeDAO
	 *            the bear code DAO to set.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private void setBearcodeDAO(final D bearcodeDAO) {
		this.bearcodeDAO = bearcodeDAO;
	}

	/**
	 * Sets the hibernate template.
	 * 
	 * @author IanBrown
	 * @param hibernateTemplate
	 *            the hibernate template to set.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private void setHibernateTemplate(final HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * Sets the original security context.
	 * 
	 * @author IanBrown
	 * @param originalSecurityContext
	 *            the original security context to set.
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	private void setOriginalSecurityContext(final SecurityContext originalSecurityContext) {
		this.originalSecurityContext = originalSecurityContext;
	}

	/**
	 * Sets the persistent resource.
	 * 
	 * @author IanBrown
	 * @param persistentResource
	 *            the persistent resource to set.
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	private void setPersistentResource(final SessionFactory persistentResource) {
		this.persistentResource = persistentResource;
	}

	/**
	 * Sets the security context.
	 * 
	 * @author IanBrown
	 * @param securityContext
	 *            the security context to set.
	 * @since Dec 20, 2011
	 * @version Feb 8, 2012
	 */
	private void setSecurityContext(final SecurityContext securityContext) {
		this.securityContext = securityContext;
	}
}
