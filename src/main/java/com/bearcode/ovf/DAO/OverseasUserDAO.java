package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.ExtendedProfile;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 5, 2007
 * Time: 9:55:37 PM
 *
 * @author Leonid Ginzburg
 */
@Repository
public class OverseasUserDAO extends BearcodeDAO implements UserDetailsService/*, IVoterDAO*/ {

    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException, DataAccessException {
        return loadUserByUsername( username, true );
    }

    /**
     * Returns an OverseasUser instance whith the given username. If ignoreDisabled is true,
     * records where the password is set to OverseasUser.DISABLED_PASSWORD will be ignored.
     * <p/>
     * If the user is note found, this method throws a UsernameNotFoundException.
     *
     * @param username
     * @param ignoreDisabled
     * @return
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     */
    public UserDetails loadUserByUsername( String username, boolean ignoreDisabled ) throws UsernameNotFoundException, DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass( OverseasUser.class )
                .add( Restrictions.eq( "username", username ) );
        if ( ignoreDisabled ) {
            criteria.add( Restrictions.ne( "password", OverseasUser.DISABLED_PASSWORD ) );
        }
        List users = getHibernateTemplate().findByCriteria( criteria );
        if ( users == null || users.size() == 0 ) {
            throw new UsernameNotFoundException( "User " + username + " not found" );
        }

        return (UserDetails) users.iterator().next();
    }

    public Collection<UserRole> findRolesByName( String name ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( UserRole.class )
                .add( Restrictions.eq( "roleName", name ) );
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public Collection<UserRole> findRoles() {
        DetachedCriteria criteria = DetachedCriteria.forClass( UserRole.class );
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public Collection<OverseasUser> findUsers( UserFilterForm filter, PagingInfo paging ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( OverseasUser.class );
        prepareUserFilterCriteria( criteria, filter );
        criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );
        /*
              TODO:
              CalculateRows has a bug/feature in that it adjusts
              the pager so that it points to the last valid page
              if given a page number beyond the last page with actual results

              We need this findUsers() method to return empty Collection for pages that
              have no results

              Workaround:
          */
        int origPage = paging.getFirstResult();
        calculateRows( criteria, paging );
        paging.setFirstResult( origPage );
        // end workaround

        criteria = DetachedCriteria.forClass( OverseasUser.class );
        prepareUserFilterCriteria( criteria, filter );
        criteria.addOrder( Order.asc( "username" ) );
        criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );
        return findBy( criteria, paging );
    }

    private void prepareUserFilterCriteria( DetachedCriteria criteria, UserFilterForm filter ) {
        criteria.add( Restrictions.ne( "password", "no password" ) );
        if ( filter.getLogin().length() > 0 )
            criteria.add( Restrictions.like( "username", filter.getLogin() + "%" ) );
        if ( filter.getFirstName().length() > 0 || filter.getLastName().length() > 0 ) {
            criteria.createAlias( "name", "name" );
        }
        if ( filter.getFirstName().length() > 0 )
            criteria.add( Restrictions.like( "name.firstName", filter.getFirstName() + "%" ) );
        if ( filter.getLastName().length() > 0 )
            criteria.add( Restrictions.like( "name.lastName", filter.getLastName() + "%" ) );
        criteria.add( Restrictions.ge( "lastUpdated", filter.getMinLastUpdated() ) );
        criteria.add( Restrictions.le( "lastUpdated", filter.getMaxLastUpdated() ) );
        if ( filter.getRoleId() != 0 ) {
            criteria.createAlias( "roles", "roles" )
                    .add( Restrictions.eq( "roles.id", filter.getRoleId() ) );
        }
    }

    public OverseasUser findById( long userId ) {
        return (OverseasUser) getHibernateTemplate().get( OverseasUser.class, userId );
    }

    public UserRole findRoleById( long roleId ) {
        return (UserRole) getHibernateTemplate().get( UserRole.class, roleId );
    }

    public int countUsers() {
        DetachedCriteria criteria = DetachedCriteria.forClass( OverseasUser.class )
                .setProjection( Projections.rowCount() );
        Long rows = (Long) getHibernateTemplate().findByCriteria( criteria ).iterator().next();
        if ( rows != null ) {
            return rows.intValue();
        }
        return 0;
    }

    public int countRealUsers() {
        DetachedCriteria criteria = DetachedCriteria.forClass( OverseasUser.class )
                .add( Restrictions.like( "username", "%@%" ) )
                .setProjection( Projections.rowCount() );
        Long rows = (Long) getHibernateTemplate().findByCriteria( criteria ).iterator().next();
        if ( rows != null ) {
            return rows.intValue();
        }
        return 0;
    }

    public int countUsers( UserFilterForm filter ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( OverseasUser.class );
        prepareUserFilterCriteria( criteria, filter );
        criteria.setProjection( Projections.rowCount() );
        Long rows = (Long) getHibernateTemplate().findByCriteria( criteria ).iterator().next();
        if ( rows != null ) {
            return rows.intValue();
        }
        return 0;
    }

    public Collection countUsersByState( Date from, Date to ) {
        return getSession().getNamedQuery( "byStateStats" )
                .setDate( "fromDate", from == null ? new Date( 0 ) : from )
                .setDate( "toDate", to == null ? new Date() : to )
                .list();
    }

    // Is this right place?  Count number of PDF downloads for both RAVA and FWAB
    public Collection countGenerations( Date from, Date to ) {
        return getSession().getNamedQuery( "generationStats" )
                .setDate( "fromDate", from == null ? new Date( 0 ) : from )
                .setDate( "toDate", to == null ? new Date() : to )
                .list();
    }

    public Collection findGenerationLog( OverseasUser user ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( WizardResults.class )
                .add( Restrictions.eq( "user", user ) )
                .addOrder( Order.desc( "creationDate" ) );
        criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );
        return getHibernateTemplate().findByCriteria( criteria );
    }

	/**
	 * Loads the user with the MD5'd username.
	 * 
	 * @author IanBrown
	 * @param usernameMd5
	 *            the MD5 username.
	 * @param ignoreDisabled
	 *            ignore disabled accounts?
	 * @return the user details found or <code>null</code> if nothing matched.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@SuppressWarnings("cast")
	public UserDetails loadUserByUsernameMd5(final String usernameMd5, final boolean ignoreDisabled) {
/*
		final StringBuilder queryStringBuilder = new StringBuilder("SELECT id FROM users WHERE");
		queryStringBuilder.append(" md5(username) = '").append(usernameMd5).append("'");
		if (ignoreDisabled) {
			queryStringBuilder.append(" AND password != '").append(OverseasUser.DISABLED_PASSWORD).append("'");
		}

		final String queryString = queryStringBuilder.toString();
		final SQLQuery query = getSession().createSQLQuery(queryString);
		final List<?> userIds = query.list();
		return (UserDetails) ((userIds == null || userIds.isEmpty()) ? null : findById(((Number) userIds.get(0)).longValue()));
*/

        Query query = getSession().createQuery( "from OverseasUser user where md5(user.username) = :nameMD5 and user.password != :passwd" );
        query.setString( "nameMD5", usernameMd5 )
        .setString( "passwd", ignoreDisabled ? OverseasUser.DISABLED_PASSWORD : "" )
        .setMaxResults( 1 );
        return (UserDetails) query.uniqueResult();
	}

    public List loadUserMembershipStats( OverseasUser user, List<Integer> searchFor ) {
        Query query = getSession().createQuery( "select distinct year(res.creationDate) from WizardResults res " +
                "where res.downloaded = true and res.user = :user " +
                "and year(res.creationDate) in :years" )
                .setEntity( "user", user )
                .setParameterList( "years", searchFor );
        return query.list();
    }

    public ExtendedProfile findExtendedProfile(OverseasUser user) {
        Criteria criteria = getSession().createCriteria( ExtendedProfile.class )
                .add( Restrictions.eq( "user", user ) )
                .setMaxResults(1);
        return (ExtendedProfile) criteria.uniqueResult();
    }
}
