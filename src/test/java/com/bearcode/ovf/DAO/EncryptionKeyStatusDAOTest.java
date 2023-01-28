/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertSame;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.encryption.EncryptionKeyStatus;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link EncryptionKeyStatusDAO}.
 * 
 * @author IanBrown
 * 
 * @since Nov 8, 2012
 * @version Nov 8, 2012
 */
public final class EncryptionKeyStatusDAOTest extends BearcodeDAOCheck<EncryptionKeyStatusDAO> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.EncryptionKeyStatusDAO#findByStateVotingRegionAndDate(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Nov 8, 2012
	 * @version Nov 8, 2012
	 */
	@Test
	public final void testFindByStateVotingRegionAndDate() {
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final String date = "11/08/2012";
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(EncryptionKeyStatus.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(3);
		final EncryptionKeyStatus encryptionKeyStatus = createMock("EncryptionKeyStatus", EncryptionKeyStatus.class);
		EasyMock.expect(criteria.uniqueResult()).andReturn(encryptionKeyStatus);
		replayAll();

		final EncryptionKeyStatus actualEncryptionKeyStatus = getBearcodeDAO().findByStateVotingRegionAndDate(state, votingRegion,
				date);

		assertSame("The encryption key status is returned", encryptionKeyStatus, actualEncryptionKeyStatus);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EncryptionKeyStatusDAO createBearcodeDAO() {
		return new EncryptionKeyStatusDAO();
	}
}
