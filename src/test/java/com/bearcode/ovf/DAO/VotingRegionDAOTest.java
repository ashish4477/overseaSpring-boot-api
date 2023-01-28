/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;
import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.County;
import com.bearcode.ovf.model.common.Municipality;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficeType;
import com.bearcode.ovf.model.eod.LocalOfficial;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link VotingRegionDAO}.
 * @author Ian
 *
 */
public final class VotingRegionDAOTest extends BearcodeDAOCheck<VotingRegionDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VotingRegionDAO#getRegionsForState(com.bearcode.ovf.model.common.State)}.
	 */
	@Test
	public final void testGetRegionsForStateState() {
		final State state = createMock("State", State.class);
		final VotingRegion expectedRegion = createMock("Region", VotingRegion.class);
		final List<VotingRegion> expectedRegions = Arrays.asList(expectedRegion);
		final Session currentSession = createMock("CurrentSession", Session.class);
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
		EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(currentSession);
		final Query query = createMock("Query", Query.class);
		EasyMock.expect(currentSession.createQuery(EasyMock.anyObject(String.class))).andReturn(query);
		EasyMock.expect(query.setParameter("state", state)).andReturn(query);
		EasyMock.expect(query.setParameter("type", LocalOfficeType.ALL)).andReturn(query);
		EasyMock.expect(query.list()).andReturn(expectedRegions);
		replayAll();
		
		final List<VotingRegion> actualRegions = getBearcodeDAO().getRegionsForState(state);
		
		assertEquals("The voting regions are returned", expectedRegions, actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VotingRegionDAO#getRegionsForState(com.bearcode.ovf.model.common.State, com.bearcode.ovf.model.eod.LocalOfficeType)}.
	 */
	@Test
	public final void testGetRegionsForStateStateLocalOfficeType() {
		final State state = createMock("State", State.class);
		final LocalOfficeType type = LocalOfficeType.DOMESTIC;
		final VotingRegion expectedRegion = createMock("Region", VotingRegion.class);
		final List<VotingRegion> expectedRegions = Arrays.asList(expectedRegion);
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
		final Session currentSession = createMock("CurrentSession", Session.class);
		EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(currentSession);
		final Query query = createMock("Query", Query.class);
		EasyMock.expect(currentSession.createQuery(EasyMock.anyObject(String.class))).andReturn(query);
		EasyMock.expect(query.setParameter("state", state)).andReturn(query);
		EasyMock.expect(query.setParameter("type", type)).andReturn(query);
		EasyMock.expect(query.list()).andReturn(expectedRegions);
		replayAll();
		
		final List<VotingRegion> actualRegions = getBearcodeDAO().getRegionsForState(state, type);
		
		assertEquals("The voting regions are returned", expectedRegions, actualRegions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VotingRegionDAO#getById(long)}.
	 */
	@Test
	public final void testGetById() {
		final long id = 1928l;
		final VotingRegion expectedRegion = createMock("Region", VotingRegion.class);
		EasyMock.expect(getHibernateTemplate().get(VotingRegion.class, id)).andReturn(expectedRegion);
		replayAll();
		
		final VotingRegion actualRegion = getBearcodeDAO().getById(id);
		
		assertEquals("The voting region is returned", expectedRegion, actualRegion);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VotingRegionDAO#getRegionByName(com.bearcode.ovf.model.common.VotingRegion)}.
	 */
	@Test
	public final void testGetRegionByName() {
		final VotingRegion region = createMock("Region", VotingRegion.class);
		final State state = createMock("State", State.class);
		EasyMock.expect(region.getState()).andReturn(state);
		final String name = "Region Name";
		EasyMock.expect(region.getName()).andReturn(name );
		final VotingRegion expectedRegion = createMock("ExpectedRegion", VotingRegion.class);
		final List<VotingRegion> expectedRegions = Arrays.asList(expectedRegion);
		EasyMock.expect(getHibernateTemplate().findByCriteria(EasyMock.anyObject(DetachedCriteria.class))).andReturn(expectedRegions);
		replayAll();
		
		final VotingRegion actualRegion = getBearcodeDAO().getRegionByName(region);
		
		assertEquals("The voting region is returned", expectedRegion, actualRegion);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VotingRegionDAO#findLeoByRegionId(long)}.
	 */
	@Test
	public final void testFindLeoByRegionId() {
		final long id = 987128l;
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
		final Session currentSession = createMock("CurrentSession", Session.class);
		EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(currentSession);
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(currentSession.createCriteria(LocalOfficial.class)).andReturn(criteria);
		EasyMock.expect(criteria.add(EasyMock.anyObject(Criterion.class))).andReturn(criteria);
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		final LocalOfficial expectedLeo = createMock("LEO", LocalOfficial.class);
		EasyMock.expect(criteria.uniqueResult()).andReturn(expectedLeo);
		final VotingRegion expectedRegion = createMock("Region", VotingRegion.class);
		EasyMock.expect(expectedLeo.getRegion()).andReturn(expectedRegion);
		getHibernateTemplate().initialize(expectedRegion);
		final State expectedState = createMock("State", State.class);
		EasyMock.expect(expectedRegion.getState()).andReturn(expectedState);
		getHibernateTemplate().initialize(expectedState);
		final County expectedCounty = createMock("County", County.class);
		EasyMock.expect(expectedRegion.getCounty()).andReturn(expectedCounty);
		getHibernateTemplate().initialize(expectedCounty);
		final Municipality expectedMunicipality = createMock("Municipality", Municipality.class);
		EasyMock.expect(expectedRegion.getMunicipality()).andReturn(expectedMunicipality);
		getHibernateTemplate().initialize(expectedMunicipality);
		final Address expectedMailing = createMock("Mailing", Address.class);
		EasyMock.expect(expectedLeo.getMailing()).andReturn(expectedMailing);
		getHibernateTemplate().initialize(expectedMailing);
		final Address expectedPhysical = createMock("Physical", Address.class);
		EasyMock.expect(expectedLeo.getPhysical()).andReturn(expectedPhysical);
		getHibernateTemplate().initialize(expectedPhysical);
		replayAll();
		
		final LocalOfficial actualLeo = getBearcodeDAO().findLeoByRegionId(id);
		
		assertEquals("The LEO is returned", expectedLeo, actualLeo);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VotingRegionDAO#findCountyByStateAndName(com.bearcode.ovf.model.common.State, java.lang.String)}.
	 */
	@Test
	public final void testFindCountyByStateAndName() {
		final State state = createMock("State", State.class);
		final String countyName = "County Name";
		final County expectedCounty = createMock("County", County.class);
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
		final Session currentSession = createMock("CurrentSession", Session.class);
		EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(currentSession);
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(currentSession.createCriteria(County.class)).andReturn(criteria);
		final Long stateId = 22839l;
		EasyMock.expect(state.getId()).andReturn(stateId);
		EasyMock.expect(criteria.add(EasyMock.anyObject(Criterion.class))).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(expectedCounty);
		replayAll();
		
		final County actualCounty = getBearcodeDAO().findCountyByStateAndName(state, countyName);
		
		assertEquals("The county is returned", expectedCounty, actualCounty);
		verifyAll();
	}
	
	/**
	 * Test method for {@link com.bearcode.ovf.DAO.VotingRegionDAO#cleanCountiesAndMunicipalities(java.util.Set)}.
	 */
	@Test
	public final void testCleanCountiesAndMunicipalities() {
		final State state = createMock("State", State.class);
		final Set<State> states = new HashSet<State>(Arrays.asList(state));
		EasyMock.expect(getHibernateTemplate().getSessionFactory()).andReturn(getPersistentResource());
		final Session currentSession = createMock("CurrentSession", Session.class);
		EasyMock.expect(getPersistentResource().getCurrentSession()).andReturn(currentSession);
		final Long stateId = 92898l;
		EasyMock.expect(state.getId()).andReturn(stateId);
		final SQLQuery sqlQuery = createMock("SQLQuery", SQLQuery.class);
		EasyMock.expect(currentSession.createSQLQuery(EasyMock.anyObject(String.class))).andReturn(sqlQuery).times(2);
		EasyMock.expect(sqlQuery.setParameterList("state_list", Arrays.asList(stateId))).andReturn(sqlQuery).times(2);
		EasyMock.expect(sqlQuery.executeUpdate()).andReturn(1).times(2);
		replayAll();
		
		getBearcodeDAO().cleanCountiesAndMunicipalities(states);
		
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
    protected final VotingRegionDAO createBearcodeDAO() {
	    final VotingRegionDAO votingRegionDAO = new VotingRegionDAO();
	    return votingRegionDAO;
    }
}
