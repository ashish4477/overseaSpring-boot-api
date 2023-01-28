/**
 * 
 */
package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistrationStatus;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.xml.datatype.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Extended {@link BearcodeDAO} to provide reading and writing of {@link PendingVoterRegistration} objects.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Nov 26, 2012
 */
@Repository
public class PendingVoterRegistrationDAO extends BearcodeDAO {

    private static final int MAX_RESULTS = 100000;

    /**
	 * Finds the pending voter registration for the specified identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @return the pending voter registration or <code>null</code>.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	public PendingVoterRegistration findById(final Long id) {
		return id == null ? null : getHibernateTemplate().get(PendingVoterRegistration.class, id);
	}

	/**
	 * Finds the pending voter registrations for the specified state and voting region.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state abbreviation.
	 * @param votingRegion
	 *            the optional voting region name.
	 * @return the list of pending voter registrations.
	 * @since Nov 5, 2012
	 * @version Nov 26, 2012
	 */
    @Deprecated
	public List<PendingVoterRegistration> findForVotingStateAndRegion(final String votingState, final String votingRegion) {
		return findForVotingStateAndRegion(votingState, votingRegion, null);
	}

	/**
	 * Finds the pending voter registrations for the specified state and voting region older than the specified duration.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state.
	 * @param votingRegion
	 *            the voting region.
	 * @param timeout
	 *            the timeout duration.
	 * @return the pending voter registrations.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
    @Deprecated
	@SuppressWarnings({ "unchecked", "cast" })
	public List<PendingVoterRegistration> findForVotingStateAndRegion(final String votingState, final String votingRegion,
			final Duration timeout) {
		final Criteria criteria = getSession().createCriteria(PendingVoterRegistration.class);
		criteria.add(Restrictions.eq("votingState", votingState));
		if (votingRegion != null && !votingRegion.isEmpty()) {
			criteria.add(Restrictions.eq("votingRegion", votingRegion));
		}
		if (timeout != null) {
			final Duration passed = timeout.negate();
			final Calendar calendar = new GregorianCalendar();
			passed.addTo(calendar);
			criteria.add(Restrictions.le("createdDate", calendar.getTime()));
		}
		return (List<PendingVoterRegistration>) criteria.list();
	}

	/**
     * Finds the pending voter registrations for the specified configuration.
     *
     * @author IanBrown
     * @param configuration
     *            the pending voter registration configuration.
     * @return the pending voter registrations.
     * @since Nov 26, 2012
     * @version Nov 26, 2012
     */
    public List<PendingVoterRegistration> findForConfiguration(final PendingVoterRegistrationConfiguration configuration) {
        return findForConfiguration( configuration, null, false );
    }

    /**
     * Finds the pending voter registrations for the specified configuration older than the specified duration.
     *
     * @author IanBrown
     * @param configuration
     *            the pending voter registration configuration.
     * @param timeout
     *            the timeout duration.
     * @return the pending voter registrations.
     * @since Nov 26, 2012
     * @version Nov 26, 2012
     */
    @SuppressWarnings({ "unchecked", "cast" })
    public List<PendingVoterRegistration> findForConfiguration(final PendingVoterRegistrationConfiguration configuration,
                                                               final Duration timeout,
                                                               final boolean all) {
        final Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( PendingVoterRegistration.class );
        if ( configuration.getFacePrefix() != null && !configuration.getFacePrefix().isEmpty() ) {
            criteria.add( Restrictions.eq( "facePrefix", configuration.getFacePrefix() ) );
        }
        final String votingState = configuration.getVotingState();
        if ( votingState != null && !votingState.isEmpty() ) {
            criteria.add(Restrictions.eq("votingState", votingState));
        }
        final String votingRegion = configuration.getVotingRegion();
        if (votingRegion != null && !votingRegion.isEmpty()) {
            criteria.add(Restrictions.eq("votingRegion", votingRegion));
        }
        if (timeout != null) {
            final Duration passed = timeout.negate();
            final Calendar calendar = new GregorianCalendar();
            passed.addTo(calendar);
            criteria.add(Restrictions.le("createdDate", calendar.getTime()));
        }
        criteria.addOrder( Order.asc("createdDate") ); // older first
        if ( !all ) {
            criteria.setMaxResults( MAX_RESULTS );
        }
        return (List<PendingVoterRegistration>) criteria.list();
    }

    /**
	 * Finds the pending voter registration status for the specified identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @return the pending voter registration status or <code>null</code>.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	public PendingVoterRegistrationStatus findStatusById(final Long id) {
		return id == null ? null : getHibernateTemplate().get(PendingVoterRegistrationStatus.class, id);
	}
}
