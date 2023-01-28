/**
 * 
 */
package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import org.easymock.EasyMock;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link OverseasUserDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jun 7, 2012
 * @version Jun 7, 2012
 */
public final class OverseasUserDAOTest extends BearcodeDAOCheck<OverseasUserDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#countGenerations(java.util.Date, java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountGenerations() {
		final Date from = new Date();
		final Date to = new Date();
		final Object[] generation = { 1, 1, FlowType.FWAB };
		final List<?> generations = Arrays.asList(new Object[] { generation });
		final Session session = addSessionToHibernateTemplate();
		final Query namedQuery = addNamedQueryToSession( session, "generationStats", from, to );
		EasyMock.expect(namedQuery.list()).andReturn(generations);
		replayAll();

		final Collection<?> actualGenerations = getBearcodeDAO().countGenerations( from, to );

		assertSame( "The generations are returned", generations, actualGenerations );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#countRealUsers()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountRealUsers() {
		final long realUsers = 210;
		final List<?> realUsersList = Arrays.asList(realUsers);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(realUsersList);
		replayAll();

		final int actualRealUsers = getBearcodeDAO().countRealUsers();

		assertEquals("The real users are returned", realUsers, actualRealUsers);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#countUsers()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountUsers() {
		final long users = 284;
		final List<?> usersList = Arrays.asList( users );
		EasyMock.expect(getHibernateTemplate().findByCriteria( (DetachedCriteria) EasyMock.anyObject() )).andReturn(usersList);
		replayAll();

		final int actualUsers = getBearcodeDAO().countUsers();

		assertEquals( "The users are returned", users, actualUsers );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#countUsersByState(java.util.Date, java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountUsersByState() {
		final Date from = new Date();
		final Date to = new Date();
		final State state = createMock("State", State.class);
		final long value = 12l;
		final Object[] stateUsers = { state, value };
		final List<?> usersByState = Arrays.asList( new Object[]{stateUsers} );
		final Session session = addSessionToHibernateTemplate();
		final Query namedQuery = addNamedQueryToSession( session, "byStateStats", from, to );
		EasyMock.expect(namedQuery.list()).andReturn(usersByState);
		replayAll();

		final Collection<?> actualUsersByState = getBearcodeDAO().countUsersByState( from, to );

		assertSame( "The users by state are returned", usersByState, actualUsersByState );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#countUsers(com.bearcode.ovf.forms.UserFilterForm)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountUsersUserFilterForm() {
		final long users = 123l;
		final UserFilterForm filter = createMock("Filter", UserFilterForm.class);
		setUpFilter(filter);
		final long roleId = 9828l;
		EasyMock.expect(filter.getRoleId()).andReturn(roleId).atLeastOnce();
		final List<?> usersCollection = Arrays.asList( users );
		EasyMock.expect(getHibernateTemplate().findByCriteria( (DetachedCriteria) EasyMock.anyObject() )).andReturn(usersCollection);
		replayAll();

		final int actualUsers = getBearcodeDAO().countUsers( filter );

		assertEquals( "The users are returned", users, actualUsers );
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#findById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindById() {
		final long userId = 9892l;
		final OverseasUser user = createMock( "User", OverseasUser.class );
		EasyMock.expect(getHibernateTemplate().get( OverseasUser.class, userId )).andReturn(user);
		replayAll();

		final OverseasUser actualUser = getBearcodeDAO().findById( userId );

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#findGenerationLog(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindGenerationLog() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final WizardResults generation = createMock("Generation", WizardResults.class);
		final List<?> generationLog = Arrays.asList( generation );
		EasyMock.expect(getHibernateTemplate().findByCriteria( (DetachedCriteria) EasyMock.anyObject() )).andReturn(generationLog);
		replayAll();

		final Collection<?> actualGenerationLog = getBearcodeDAO().findGenerationLog( user );

		assertSame("The generation log is returned", generationLog, actualGenerationLog);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#findRoleById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindRoleById() {
		final long roleId = 238781l;
		final UserRole role = createMock( "Role", UserRole.class );
		EasyMock.expect(getHibernateTemplate().get( UserRole.class, roleId )).andReturn(role);
		replayAll();

		final UserRole actualRole = getBearcodeDAO().findRoleById( roleId );

		assertSame("The role is returned", role, actualRole);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#findRoles()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindRoles() {
		final UserRole role = createMock("Role", UserRole.class);
		final List<UserRole> roles = Arrays.asList(role);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(roles);
		replayAll();

		final Collection<UserRole> actualRoles = getBearcodeDAO().findRoles();

		assertSame("The roles are returned", roles, actualRoles);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#findRolesByName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindRolesByName() {
		final String name = UserRole.USER_ROLE_USER_EXPORT;
		final UserRole role = createMock("Role", UserRole.class);
		final List<UserRole> roles = Arrays.asList(role);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(roles);
		replayAll();

		final Collection<UserRole> actualRoles = getBearcodeDAO().findRolesByName(name);

		assertSame("The roles are returned", roles, actualRoles);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.OverseasUserDAO#findUsers(com.bearcode.ovf.forms.UserFilterForm, com.bearcode.commons.DAO.PagingInfo)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindUsers() {
		final UserFilterForm filter = createMock("Filter", UserFilterForm.class);
		final PagingInfo paging = createMock("Paging", PagingInfo.class);
		setUpFilter(filter);
		final long roleId = 9828l;
		EasyMock.expect(filter.getRoleId()).andReturn(roleId).atLeastOnce();
		final int firstResult = 3;
		EasyMock.expect(paging.getFirstResult()).andReturn(firstResult).atLeastOnce();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final List<OverseasUser> users = Arrays.asList(user);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(
				Arrays.asList((long) users.size()));
		final int maxResults = 4;
		EasyMock.expect(paging.getMaxResults()).andReturn(maxResults).atLeastOnce();
		paging.setFirstResult(0);
		paging.setActualRows(1);
		paging.setFirstResult(firstResult);
		final String orderField = "id";
		final String[] orderFields = new String[] { orderField };
		EasyMock.expect(paging.getOrderFields()).andReturn(orderFields);
		EasyMock.expect(paging.isAscending()).andReturn(false);
		EasyMock.expect(
				getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject(), EasyMock.eq(firstResult),
						EasyMock.eq(maxResults))).andReturn(users);
		replayAll();

		final Collection<OverseasUser> actualUsers = getBearcodeDAO().findUsers(filter, paging);

		assertSame("The users are returned", users, actualUsers);
		verifyAll();
	}


	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#loadUserByUsernameMd5(java.lang.String, boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testLoadUserByUsernameMd5() {
		final String username = "Username";
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String usernameMd5 = md5Encoder.encodePassword(username, null);
		final boolean ignoreDisabled = true;
		final OverseasUser user = createMock("User", OverseasUser.class);
		final Session session = addSessionToHibernateTemplate();
		final Query query = addQueryToSession(session, (String) EasyMock.anyObject());
		EasyMock.expect( query.setString( "nameMD5", usernameMd5 ) ).andReturn( query );
		EasyMock.expect( query.setString( EasyMock.anyObject( String.class ), EasyMock.anyObject( String.class ) ) ).andReturn( query );
		EasyMock.expect( query.setMaxResults( 1 ) ).andReturn( query );
		EasyMock.expect(query.uniqueResult()).andReturn( user );
		replayAll();

		final UserDetails actualUser = getBearcodeDAO().loadUserByUsernameMd5(usernameMd5, ignoreDisabled);

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#loadUserByUsername(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testLoadUserByUsernameString() {
		final String username = "Username";
		final UserDetails user = createMock("User", UserDetails.class);
		final List<?> users = Arrays.asList(user);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(users);
		replayAll();

		final UserDetails actualUser = getBearcodeDAO().loadUserByUsername(username);

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.OverseasUserDAO#loadUserByUsername(java.lang.String, boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testLoadUserByUsernameStringBoolean() {
		final String username = "Username";
		final boolean ignoreDisabled = false;
		final UserDetails user = createMock("User", UserDetails.class);
		final List<?> users = Arrays.asList(user);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(users);
		replayAll();

		final UserDetails actualUser = getBearcodeDAO().loadUserByUsername(username, ignoreDisabled);

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final OverseasUserDAO createBearcodeDAO() {
		return new OverseasUserDAO();
	}

	/**
	 * Adds a named query for the date range to the session.
	 * 
	 * @author IanBrown
	 * @param session
	 *            the session.
	 * @param name
	 *            the name of the query.
	 * @param from
	 *            the from date.
	 * @param to
	 *            the to date.
	 * @return the named query.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private Query addNamedQueryToSession(final Session session, final String name, final Date from, final Date to) {
		final Query namedQuery = createMock("NamedQuery", Query.class);
		EasyMock.expect(session.getNamedQuery(name)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setDate("fromDate", from)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setDate("toDate", to)).andReturn(namedQuery);
		return namedQuery;
	}

	/**
	 * Sets up the user filter form.
	 * 
	 * @author IanBrown
	 * @param filter
	 *            the filter.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private void setUpFilter(final UserFilterForm filter) {
		final String login = "Login";
		EasyMock.expect(filter.getLogin()).andReturn(login).atLeastOnce();
		final String firstName = "First";
		EasyMock.expect(filter.getFirstName()).andReturn(firstName).atLeastOnce();
		final String lastName = "Last";
		EasyMock.expect(filter.getLastName()).andReturn(lastName).atLeastOnce();
		final Date minLastUpdated = new Date();
		EasyMock.expect(filter.getMinLastUpdated()).andReturn(minLastUpdated).atLeastOnce();
		final Date maxLastUpdated = new Date();
		EasyMock.expect(filter.getMaxLastUpdated()).andReturn(maxLastUpdated).atLeastOnce();
	}
}
