/**
 * 
 */
package com.bearcode.ovf.DAO;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.encryption.EncryptionKeyStatus;

/**
 * Extended {@link BearcodeDAO} data access object for {@link EncryptionKeyStatus}.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Nov 6, 2012
 */
@Repository
public class EncryptionKeyStatusDAO extends BearcodeDAO {

	/**
	 * Finds an encryption key status for the specified state, voting region, and date string.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param date
	 *            the date string.
	 * @return the encryption key status or <code>null</code> if there is none.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public EncryptionKeyStatus findByStateVotingRegionAndDate(final String state, final String votingRegion, final String date) {
		final Criteria criteria = getSession().createCriteria(EncryptionKeyStatus.class);
		criteria.add(Restrictions.eq("id.state", state)).add(Restrictions.eq("id.date", date));
		if (votingRegion == null) {
			criteria.add(Restrictions.isNull("id.votingRegion"));
		} else {
			criteria.add(Restrictions.eq("id.votingRegion", votingRegion));
		}
		return (EncryptionKeyStatus) criteria.uniqueResult();
	}
}
