/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistrationStatus;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link PendingVoterRegistrationDAO}.
 * 
 * @author IanBrown
 * 
 * @since Nov 8, 2012
 * @version Nov 26, 2012
 */
public final class PendingVoterRegistrationDAOTest extends BearcodeDAOCheck<PendingVoterRegistrationDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.PendingVoterRegistrationDAO#findById(Long)}.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindById() {
		final Long id = 98129l;
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		EasyMock.expect(getHibernateTemplate().get(PendingVoterRegistration.class, id)).andReturn(pendingVoterRegistration);
		replayAll();

		final PendingVoterRegistration actualPendingVoterRegistration = getBearcodeDAO().findById(id);

		assertSame("The pending voter registration is returned", pendingVoterRegistration, actualPendingVoterRegistration);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.PendingVoterRegistrationDAO#findById(Long)} for a <code>null</code> identifier.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindById_nullIdentifier() {
		final Long id = null;
		replayAll();

		final PendingVoterRegistration actualPendingVoterRegistration = getBearcodeDAO().findById(id);

		assertNull("No pending voter registration is returned", actualPendingVoterRegistration);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.PendingVoterRegistrationDAO#findForVotingStateAndRegion(java.lang.String, java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Nov 8, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindForVotingStateAndRegionStringString() {
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(PendingVoterRegistration.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(2);
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(pendingVoterRegistration);
		EasyMock.expect(criteria.list()).andReturn(pendingVoterRegistrations);
		replayAll();

		final List<PendingVoterRegistration> actualPendingVoterRegistrations = getBearcodeDAO().findForVotingStateAndRegion(
				votingState, votingRegion);

		assertSame("The pending voter registrations are returned", pendingVoterRegistrations, actualPendingVoterRegistrations);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.PendingVoterRegistrationDAO#findForVotingStateAndRegion(java.lang.String, java.lang.String, javax.xml.types.Duration)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws DatatypeConfigurationException
	 *             if there is a problem creating the timeout.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindForVotingStateAndRegionStringStringDuration() throws DatatypeConfigurationException {
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		final Duration timeout = datatypeFactory.newDuration("P1M");
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(PendingVoterRegistration.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(3);
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(pendingVoterRegistration);
		EasyMock.expect(criteria.list()).andReturn(pendingVoterRegistrations);
		replayAll();

		final List<PendingVoterRegistration> actualPendingVoterRegistrations = getBearcodeDAO().findForVotingStateAndRegion(
				votingState, votingRegion, timeout);

		assertSame("The pending voter registrations are returned", pendingVoterRegistrations, actualPendingVoterRegistrations);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.PendingVoterRegistrationDAO#findStatusById(Long)}.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindStatusById() {
		final Long id = 98129l;
		final PendingVoterRegistrationStatus pendingVoterRegistrationStatus = createMock("PendingVoterRegistrationStatus",
				PendingVoterRegistrationStatus.class);
		EasyMock.expect(getHibernateTemplate().get(PendingVoterRegistrationStatus.class, id)).andReturn(pendingVoterRegistrationStatus);
		replayAll();

		final PendingVoterRegistrationStatus actualPendingVoterRegistrationStatus = getBearcodeDAO().findStatusById(id);

		assertSame("The pending voter registration status is returned", pendingVoterRegistrationStatus,
				actualPendingVoterRegistrationStatus);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.PendingVoterRegistrationDAO#findStatusById(Long)} for a <code>null</code>
	 * identifier.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindStatusById_nullIdentifier() {
		final Long id = null;
		replayAll();

		final PendingVoterRegistrationStatus actualPendingVoterRegistrationStatus = getBearcodeDAO().findStatusById(id);

		assertNull("No pending voter registration status is returned", actualPendingVoterRegistrationStatus);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final PendingVoterRegistrationDAO createBearcodeDAO() {
		return new PendingVoterRegistrationDAO();
	}
}
