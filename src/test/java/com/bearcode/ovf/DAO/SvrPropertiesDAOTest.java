/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.svrproperties.SvrProperty;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link SvrPropertiesDAO}.
 * 
 * @author IanBrown
 * 
 * @since Oct 22, 2012
 * @version Oct 22, 2012
 */
public final class SvrPropertiesDAOTest extends BearcodeDAOCheck<SvrPropertiesDAO> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.SvrPropertiesDAO#findProperty(java.lang.String, java.lang.String, java.lang.String)} for the case
	 * where there is no match.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@Test
	public final void testFindProperty_noMatch() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region";
		final String propertyName = "property.name";
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, SvrProperty.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(3);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(new LinkedList<SvrProperty>());
		replayAll();

		final String actualPropertyValue = getBearcodeDAO().findProperty(stateAbbreviation, votingRegionName, propertyName);

		assertNull("No property value is returned", actualPropertyValue);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.SvrPropertiesDAO#findProperty(java.lang.String, java.lang.String, java.lang.String)} for the case
	 * where there is no voting region provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@Test
	public final void testFindProperty_noVotingRegion() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = null;
		final String propertyName = "property.name";
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, SvrProperty.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(3);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		final SvrProperty stateProperty = createMock("StateProperty", SvrProperty.class);
		final List<SvrProperty> svrProperties = Arrays.asList(stateProperty);
		final String statePropertyValue = "State Property Value";
		EasyMock.expect(stateProperty.getPropertyValue()).andReturn(statePropertyValue);
		EasyMock.expect(criteria.list()).andReturn(svrProperties);
		replayAll();

		final String actualPropertyValue = getBearcodeDAO().findProperty(stateAbbreviation, votingRegionName, propertyName);

		assertEquals("The state property value is returned", statePropertyValue, actualPropertyValue);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.SvrPropertiesDAO#findProperty(java.lang.String, java.lang.String, java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@Test
	public final void testFindProperty() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region Name";
		final String propertyName = "property.name";
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = addCriteriaToSession(session, SvrProperty.class);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(3);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		final SvrProperty votingRegionProperty = createMock("VotingRegionProperty", SvrProperty.class);
		final SvrProperty stateProperty = createMock("StateProperty", SvrProperty.class);
		final List<SvrProperty> svrProperties = Arrays.asList(votingRegionProperty, stateProperty);
		final String votingRegionPropertyValue = "Voting Region Property Value";
		EasyMock.expect(votingRegionProperty.getPropertyValue()).andReturn(votingRegionPropertyValue);
		EasyMock.expect(criteria.list()).andReturn(svrProperties);
		replayAll();

		final String actualPropertyValue = getBearcodeDAO().findProperty(stateAbbreviation, votingRegionName, propertyName);

		assertEquals("The voting region property value is returned", votingRegionPropertyValue, actualPropertyValue);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final SvrPropertiesDAO createBearcodeDAO() {
		return new SvrPropertiesDAO();
	}

}
