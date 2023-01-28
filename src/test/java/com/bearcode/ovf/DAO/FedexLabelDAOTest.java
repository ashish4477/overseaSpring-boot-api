/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.express.FedexLabel;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link FedexLabelDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 18, 2012
 * @version Jul 18, 2012
 */
public final class FedexLabelDAOTest extends BearcodeDAOCheck<FedexLabelDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FedexLabelDAO#getLabel(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetLabel() {
		final long id = 87542l;
		final FedexLabel label = createMock("Label", FedexLabel.class);
		EasyMock.expect(getHibernateTemplate().get(FedexLabel.class, id)).andReturn(label);
		replayAll();

		final FedexLabel actualLabel = getBearcodeDAO().getLabel(id);

		assertSame("The label is returned", label, actualLabel);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FedexLabelDAO#getLabelByNumber(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetLabelByNumber() {
		final String number = "L287";
		final FedexLabel label = createMock("Label", FedexLabel.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FedexLabel.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(label);
		replayAll();

		final FedexLabel actualLabel = getBearcodeDAO().getLabelByNumber(number);

		assertSame("The label is returned", label, actualLabel);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FedexLabelDAO#getLabelsForUser(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetLabelsForUserOverseasUser() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final FedexLabel label = createMock("Label", FedexLabel.class);
		final List<FedexLabel> labels = Arrays.asList(label);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FedexLabel.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(labels);
		replayAll();

		final Collection actualLabels = getBearcodeDAO().getLabelsForUser(user);

		assertSame("The labels are returned", labels, actualLabels);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.FedexLabelDAO#getLabelsForUser(com.bearcode.ovf.model.common.OverseasUser, boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetLabelsForUserOverseasUserBoolean() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final boolean onlyUnexpired = false;
		final FedexLabel label = createMock("Label", FedexLabel.class);
		final List<FedexLabel> labels = Arrays.asList(label);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FedexLabel.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(labels);
		replayAll();

		final Collection actualLabels = getBearcodeDAO().getLabelsForUser(user, onlyUnexpired);

		assertSame("The labels are returned", labels, actualLabels);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.FedexLabelDAO#getLabelsForUserUnexpired(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetLabelsForUserUnexpired() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final FedexLabel label = createMock("Label", FedexLabel.class);
		final List<FedexLabel> labels = Arrays.asList(label);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FedexLabel.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(labels);
		replayAll();

		final Collection actualLabels = getBearcodeDAO().getLabelsForUserUnexpired(user);

		assertSame("The labels are returned", labels, actualLabels);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final FedexLabelDAO createBearcodeDAO() {
		return new FedexLabelDAO();
	}
}
