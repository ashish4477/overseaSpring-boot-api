/**
 * 
 */
package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.Election;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import org.easymock.EasyMock;
import org.hibernate.*;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link LocalOfficialDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 18, 2012
 * @version Jul 19, 2012
 */
public final class LocalOfficialDAOTest extends BearcodeDAOCheck<LocalOfficialDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#checkLeoGotChanged()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("deprecation")
	@Test
	public final void testCheckLeoGotChanged() {
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery query = createMock("Query", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(query);
		EasyMock.expect(query.addScalar("updated_count", Hibernate.INTEGER)).andReturn(query);
		EasyMock.expect(query.uniqueResult()).andReturn(1l);
		replayAll();

		final boolean actualResult = getBearcodeDAO().checkLeoGotChanged();

		assertTrue("The LEO got changed", actualResult);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#checkLeoGotChanged()} for the case where the LEO did not change.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("deprecation")
	@Test
	public final void testCheckLeoGotChanged_noChange() {
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery query = createMock("Query", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(query);
		EasyMock.expect(query.addScalar("updated_count", Hibernate.INTEGER)).andReturn(query);
		EasyMock.expect(query.uniqueResult()).andReturn(0);
		replayAll();

		final boolean actualResult = getBearcodeDAO().checkLeoGotChanged();

		assertFalse("The LEO did not change", actualResult);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#checkLeoGotChanged()} for the case where there are no results.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("deprecation")
	@Test
	public final void testCheckLeoGotChanged_noResults() {
		final Session session = addSessionToHibernateTemplate();
		final SQLQuery query = createMock("Query", SQLQuery.class);
		EasyMock.expect(session.createSQLQuery((String) EasyMock.anyObject())).andReturn(query);
		EasyMock.expect(query.addScalar("updated_count", Hibernate.INTEGER)).andReturn(query);
		EasyMock.expect(query.uniqueResult()).andReturn(null);
		replayAll();

		final boolean actualResult = getBearcodeDAO().checkLeoGotChanged();

		assertFalse("The LEO did not change", actualResult);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findAll()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindAll() {
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(LocalOfficial.class)).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(localOfficials);
		replayAll();

		final Collection actualLocalOfficials = getBearcodeDAO().findAll();

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findElection(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindElection() {
		final long electionId = 542l;
		final Election election = createMock("Election", Election.class);
		EasyMock.expect(getHibernateTemplate().get(Election.class, electionId)).andReturn(election);
		replayAll();

		final Election actualElection = getBearcodeDAO().findElection(electionId);

		assertSame("The election is returned", election, actualElection);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findLeoByRegion(com.bearcode.ovf.model.common.VotingRegion)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindLeoByRegion() {
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final LocalOfficial localOfficial = createMock("LEO", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
        final Criteria criteria = createMock("Criteria", Criteria.class);
        final org.hibernate.classic.Session session = createMock("Session", org.hibernate.classic.Session.class );
        EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
        EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(session);
        EasyMock.expect(session.createCriteria(LocalOfficial.class)).andReturn(criteria);
        EasyMock.expect(criteria.add((Criterion)EasyMock.anyObject())).andReturn(criteria);
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
        EasyMock.expect(criteria.uniqueResult()).andReturn(localOfficial);
		replayAll();

		final LocalOfficial actualLocalOfficial = getBearcodeDAO().findLeoByRegion(region);

		assertSame("The local official is returned", localOfficial, actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findLeoByRegion(com.bearcode.ovf.model.common.VotingRegion)} for
	 * the case where there is no match.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindLeoByRegion_noMatch() {
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final List<LocalOfficial> localOfficials = new LinkedList<LocalOfficial>();
        final Criteria criteria = createMock("Criteria", Criteria.class);
        final org.hibernate.classic.Session session = createMock("Session", org.hibernate.classic.Session.class );
        EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
        EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(session);
        EasyMock.expect(session.createCriteria(LocalOfficial.class)).andReturn(criteria);
        EasyMock.expect(criteria.add((Criterion)EasyMock.anyObject())).andReturn(criteria);
        EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
        EasyMock.expect(criteria.uniqueResult()).andReturn(null);
		replayAll();

		getBearcodeDAO().findLeoByRegion(region);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findLeoByState(long, com.bearcode.commons.DAO.PagingInfo)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindLeoByStateLongPagingInfo() {
		final long stateId = 7612l;
		final PagingInfo paging = setUpPaging();
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject()))
				.andReturn(Arrays.asList(1l));
		EasyMock.expect(paging.getFirstResult()).andReturn(0);
		EasyMock.expect(
				getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(0), EasyMock.eq(72)))
				.andReturn(localOfficials);
		replayAll();

		final Collection<LocalOfficial> actualLocalOfficials = getBearcodeDAO().findLeoByState(stateId, paging);

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findLeoByState(long, com.bearcode.commons.DAO.PagingInfo, com.bearcode.ovf.model.common.VotingRegion)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindLeoByStateLongPagingInfoVotingRegion() {
		final long stateId = 7627l;
		final PagingInfo paging = setUpPaging();
		final VotingRegion lookFor = createMock("LookFor", VotingRegion.class);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject()))
				.andReturn(Arrays.asList(1l));
		final String name = "Name";
		EasyMock.expect(lookFor.getName()).andReturn(name);
		paging.setFirstResult(2);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject()))
				.andReturn(Arrays.asList(1l));
		EasyMock.expect(paging.getFirstResult()).andReturn(2);
		paging.setFirstResult(0);
		EasyMock.expect(paging.getFirstResult()).andReturn(0);
		EasyMock.expect(
				getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(0), EasyMock.eq(72)))
				.andReturn(localOfficials);
		replayAll();

		final Collection<LocalOfficial> actualLocalOfficials = getBearcodeDAO().findLeoByState(stateId, paging, lookFor);

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findLeoByState(com.bearcode.ovf.model.common.State)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindLeoByStateState() {
		final State state = createMock("State", State.class);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(LocalOfficial.class)).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("region", "region")).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(localOfficials);
		replayAll();

		final Collection<LocalOfficial> actualLocalOfficials = getBearcodeDAO().findLeoByState(state);

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findStateStatistics(com.bearcode.commons.DAO.PagingInfo)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindStateStatistics() {
		final PagingInfo pagingInfo = setUpPaging();
		EasyMock.expect(getHibernateTemplate().find((String) EasyMock.anyObject())).andReturn(Arrays.asList(1l));
		final String stateName = "State Name";
		final int regionCount = 1;
		final int statusSum = 2;
		final State state = createMock("State", State.class);
		final List<?> statisticsRow = Arrays.asList(stateName, regionCount, statusSum, state);
		final List<List<?>> statistics = Arrays.asList(new List<?>[] { statisticsRow });
		final SessionFactory sessionFactory = createMock("SessionFactory", SessionFactory.class);
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(sessionFactory);
		final Session session = createMock("Session", Session.class);
		EasyMock.expect(sessionFactory.getCurrentSession()).andReturn(session);
		final Query query = createMock("Query", Query.class);
		EasyMock.expect(session.createQuery((String) EasyMock.anyObject())).andReturn(query);
		EasyMock.expect(query.setMaxResults(72)).andReturn(query);
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(0);
		EasyMock.expect(query.setFirstResult(0)).andReturn(query);
		EasyMock.expect(query.list()).andReturn(statistics);
		replayAll();

		final Collection actualStatistics = getBearcodeDAO().findStateStatistics(pagingInfo);

		assertSame("The statistics are returned", statistics, actualStatistics);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findSvidForState(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindSvidForStateLong() {
		final long stateId = 762l;
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(StateSpecificDirectory.class)).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("state", "state")).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(svid);
		replayAll();

		final StateSpecificDirectory actualSvid = getBearcodeDAO().findSvidForState(stateId);

		assertSame("The SVID is returned", svid, actualSvid);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#findSvidForState(com.bearcode.ovf.model.common.State)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindSvidForStateState() {
		final State state = createMock("State", State.class);
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(StateSpecificDirectory.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(svid);
		replayAll();

		final StateSpecificDirectory actualSvid = getBearcodeDAO().findSvidForState(state);

		assertSame("The SVID is returned", svid, actualSvid);
		verifyAll();

	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#getById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetById() {
		final long id = 61762l;
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		EasyMock.expect(getHibernateTemplate().get(LocalOfficial.class, id)).andReturn(localOfficial);
		replayAll();

		final LocalOfficial actualLocalOfficial = getBearcodeDAO().getById(id);

		assertSame("The local official is returned", localOfficial, actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#getSvidById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testGetSvidById() {
		final long svidId = 4512l;
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getHibernateTemplate().get(StateSpecificDirectory.class, svidId)).andReturn(svid);
		replayAll();

		final StateSpecificDirectory actualSvid = getBearcodeDAO().getSvidById(svidId);

		assertSame("The SVID is returned", svid, actualSvid);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#initialize(com.bearcode.ovf.model.eod.LocalOfficial)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testInitialize() {
		final LocalOfficial leo = createMock("LEO", LocalOfficial.class);
		final Address mailingAddress = createMock("MailingAddress", Address.class);
		EasyMock.expect(leo.getMailing()).andReturn(mailingAddress);
		getHibernateTemplate().initialize(mailingAddress);
		final Address physicalAddress = createMock("PhysicalAddress", Address.class);
		EasyMock.expect(leo.getPhysical()).andReturn(physicalAddress);
		getHibernateTemplate().initialize(physicalAddress);
		replayAll();

		getBearcodeDAO().initialize(leo);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#search(java.util.List, java.util.List)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testSearchListOfStringListOfString() {
		final String param = "Param";
		final List<String> params = Arrays.asList(param);
		final String state = "State";
		final List<String> states = Arrays.asList(state);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(LocalOfficial.class)).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("region", "region")).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("region.state", "state")).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(2);
		EasyMock.expect(criteria.list()).andReturn(localOfficials);
		replayAll();

		final Collection actualLocalOfficials = getBearcodeDAO().search(params, states);

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#search(java.util.List, java.util.List)} for the case where no
	 * parameters are provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testSearchListOfStringListOfString_noParams() {
		final List<String> params = new LinkedList<String>();
		final String state = "State";
		final List<String> states = Arrays.asList(state);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(LocalOfficial.class)).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("region", "region")).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("region.state", "state")).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(localOfficials);
		replayAll();

		final Collection actualLocalOfficials = getBearcodeDAO().search(params, states);

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.LocalOfficialDAO#search(java.util.List, java.util.List)} for the case where no
	 * states are provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testSearchListOfStringListOfString_noStates() {
		final String param = "Param";
		final List<String> params = Arrays.asList(param);
		final List<String> states = new LinkedList<String>();
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(LocalOfficial.class)).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("region", "region")).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("region.state", "state")).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(localOfficials);
		replayAll();

		final Collection actualLocalOfficials = getBearcodeDAO().search(params, states);

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.LocalOfficialDAO#search(java.util.List, java.util.List, com.bearcode.commons.DAO.PagingInfo)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 19, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testSearchListOfStringListOfStringPagingInfo() {
		final String param = "Param";
		final List<String> params = Arrays.asList(param);
		final String state = "State";
		final List<String> states = Arrays.asList(state);
		final PagingInfo pagingInfo = setUpPaging();
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final List<LocalOfficial> localOfficials = Arrays.asList(localOfficial);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject()))
				.andReturn(Arrays.asList(1l));
		EasyMock.expect(pagingInfo.getFirstResult()).andReturn(0);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(0), EasyMock.eq(72))).andReturn(localOfficials);
		replayAll();

		final Collection actualLocalOfficials = getBearcodeDAO().search(params, states, pagingInfo);

		assertSame("The local officials are returned", localOfficials, actualLocalOfficials);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final LocalOfficialDAO createBearcodeDAO() {
		return new LocalOfficialDAO();
	}

	/**
	 * Sets up the paging information.
	 * 
	 * @author IanBrown
	 * @return the paging information.
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	private PagingInfo setUpPaging() {
		final PagingInfo paging = createMock("Paging", PagingInfo.class);
		EasyMock.expect(paging.getMaxResults()).andReturn(72).atLeastOnce();
		EasyMock.expect(paging.getFirstResult()).andReturn(1);
		paging.setFirstResult(0);
		paging.setActualRows(1);
		EasyMock.expectLastCall().atLeastOnce();
		final String field = "Field";
		final String[] fields = new String[] { field };
		EasyMock.expect(paging.getOrderFields()).andReturn(fields).anyTimes();
		EasyMock.expect(paging.isAscending()).andReturn(false).anyTimes();
		return paging;
	}
}
