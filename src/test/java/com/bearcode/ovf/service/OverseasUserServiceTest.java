/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.DAO.OverseasUserDAO;
import com.bearcode.ovf.forms.AdminStatisticsForm;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;

/**
 * Unit test for {@link OverseasUserService}.
 * 
 * @author IanBrown
 * 
 * @since Jun 7, 2012
 * @version Jun 7, 2012
 */
public final class OverseasUserServiceTest extends EasyMockSupport {

	/**
	 * the overseas user service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private OverseasUserService overseasUserService;

	/**
	 * the overseas user DAO to use.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private OverseasUserDAO overseasUserDAO;

	/**
	 * the overseas zoho integration.
	 *
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private ZohoService zohoService;


	/**
	 * Sets up the overseas user service to test.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Before
	public final void setUpOverseasUserService() {
		setOverseasUserDAO(createMock("OverseasUserDAO", OverseasUserDAO.class));
		setOverseasUserService(new OverseasUserService());
		setZohoService(createMock("ZohoService", ZohoService.class));
		ReflectionTestUtils.setField(getOverseasUserService(), "userDAO", getOverseasUserDAO());
		ReflectionTestUtils.setField(getOverseasUserService(), "zohoService", getZohoService());
	}

	/**
	 * Tears down the overseas user service after testing.
	 * 
	 * @author IanBrown
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@After
	public final void tearDownOverseasUserService() {
		setOverseasUserService(null);
		setOverseasUserDAO(null);
		setZohoService(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.OverseasUserService#countPdfGenerations(com.bearcode.ovf.forms.AdminStatisticsForm)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountPdfGenerations() {
		final AdminStatisticsForm form = createMock("Form", AdminStatisticsForm.class);
		final Date startRangeDate = new Date();
		EasyMock.expect(form.getStartRangeDate()).andReturn(startRangeDate);
		final Date endRangeDate = new Date();
		EasyMock.expect(form.getEndRangeDate()).andReturn(endRangeDate);
		final Object[] pdfGeneration = { 1, 1, FlowType.FWAB };
		final Collection<?> pdfGenerations = Arrays.asList(new Object[] { pdfGeneration });
		EasyMock.expect(getOverseasUserDAO().countGenerations(startRangeDate, endRangeDate)).andReturn(pdfGenerations);
		replayAll();

		final Collection<?> actualPdfGenerations = getOverseasUserService().countPdfGenerations(form);

		assertSame("The PDF generations are returned", pdfGenerations, actualPdfGenerations);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#countRealUsers()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountRealUsers() {
		final int realUsers = 27;
		EasyMock.expect(getOverseasUserDAO().countRealUsers()).andReturn(realUsers);
		replayAll();

		final int actualRealUsers = getOverseasUserService().countRealUsers();

		assertEquals("The number of real users is returned", realUsers, actualRealUsers);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#countUsers()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountUsers() {
		final int users = 82;
		EasyMock.expect(getOverseasUserDAO().countUsers()).andReturn(users);
		replayAll();

		final int actualUsers = getOverseasUserService().countUsers();

		assertEquals("The number of users is returned", users, actualUsers);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.OverseasUserService#countUsersByState(com.bearcode.ovf.forms.AdminStatisticsForm)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountUsersByState() {
		final Map<State, Long> usersByState = new HashMap<State, Long>();
		final State state = createMock("State", State.class);
		final long value = 12l;
		usersByState.put(state, value);
		final AdminStatisticsForm form = createMock("Form", AdminStatisticsForm.class);
		final Date startRangeDate = new Date();
		EasyMock.expect(form.getStartRangeDate()).andReturn(startRangeDate);
		final Date endRangeDate = new Date();
		EasyMock.expect(form.getEndRangeDate()).andReturn(endRangeDate);
		final Object[] stateUsers = { state, value };
		final Collection<?> usersByStateCollection = Arrays.asList(new Object[] { stateUsers });
		EasyMock.expect(getOverseasUserDAO().countUsersByState(startRangeDate, endRangeDate)).andReturn(usersByStateCollection);
		replayAll();

		final Map<State, Long> actualUsersByState = getOverseasUserService().countUsersByState(form);

		assertEquals("The users by state map is returned", usersByState, actualUsersByState);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#countUsers(com.bearcode.ovf.forms.UserFilterForm)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testCountUsersUserFilterForm() {
		final int users = 62;
		final UserFilterForm filter = createMock("Filter", UserFilterForm.class);
		EasyMock.expect(getOverseasUserDAO().countUsers(filter)).andReturn(users);
		replayAll();

		final int actualUsers = getOverseasUserService().countUsers(filter);

		assertEquals("The number of users is returned", users, actualUsers);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#deleteUser(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testDeleteUser() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getOverseasUserDAO().merge(user)).andReturn(user);
		getOverseasUserDAO().evict(user);
		getOverseasUserDAO().makeTransient(user);
		replayAll();

		getOverseasUserService().deleteUser(user);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#evict(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testEvict() {
		final OverseasUser existingUser = createMock("ExistingUser", OverseasUser.class);
		getOverseasUserDAO().evict(existingUser);
		replayAll();

		getOverseasUserService().evict(existingUser);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.OverseasUserService#findGenerationLog(com.bearcode.ovf.model.common.OverseasUser)}.
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
		final Collection<?> generationLog = Arrays.asList(generation);
		EasyMock.expect(getOverseasUserDAO().findGenerationLog(user)).andReturn(generationLog);
		replayAll();

		final Collection<?> actualGenerationLog = getOverseasUserService().findGenerationLog(user);

		assertSame("The generation log is returned", generationLog, actualGenerationLog);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#findRoleById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindRoleById() {
		final long roleId = 76l;
		final UserRole role = createMock("Role", UserRole.class);
		EasyMock.expect(getOverseasUserDAO().findRoleById(roleId)).andReturn(role);
		replayAll();

		final UserRole actualRole = getOverseasUserService().findRoleById(roleId);

		assertSame("The role is returned", role, actualRole);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#findRoles()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindRoles() {
		final UserRole role = createMock("Role", UserRole.class);
		final Collection<UserRole> roles = Arrays.asList(role);
		EasyMock.expect(getOverseasUserDAO().findRoles()).andReturn(roles);
		replayAll();

		final Collection<UserRole> actualRoles = getOverseasUserService().findRoles();

		assertSame("The roles are returned", roles, actualRoles);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#findRolesByName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindRolesByName() {
		final String userRole = UserRole.USER_ROLE_FACE_ADMIN;
		final UserRole role = createMock("Role", UserRole.class);
		final Collection<UserRole> roles = Arrays.asList(role);
		EasyMock.expect(getOverseasUserDAO().findRolesByName(userRole)).andReturn(roles);
		replayAll();

		final Collection<UserRole> actualRoles = getOverseasUserService().findRolesByName(userRole);

		assertSame("The roles are returned", roles, actualRoles);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#findUserById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindUserById() {
		final long userId = 981237l;
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getOverseasUserDAO().findById(userId)).andReturn(user);
		replayAll();

		final OverseasUser actualUser = getOverseasUserService().findUserById(userId);

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#findUserByName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindUserByName() {
		final String email = "email@somewhere.com";
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getOverseasUserDAO().loadUserByUsername(email, false)).andReturn(user);
		replayAll();

		final OverseasUser actualUser = getOverseasUserService().findUserByName(email);

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#findUserByNameMd5(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindUserByNameMd5() {
		final String email = "email@somewhere.com";
		final PasswordEncoder md5Encoder = new Md5PasswordEncoder();
		final String emailMd5 = md5Encoder.encodePassword(email, null);
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getOverseasUserDAO().loadUserByUsernameMd5(emailMd5, false)).andReturn(user);
		replayAll();

		final OverseasUser actualUser = getOverseasUserService().findUserByNameMd5(emailMd5);

		assertSame("The user is returned", user, actualUser);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#findUsers(com.bearcode.ovf.forms.UserFilterForm)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testFindUsers() {
		final UserFilterForm filter = createMock("Filter", UserFilterForm.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(filter.createPagingInfo()).andReturn(pagingInfo);
		final OverseasUser user = createMock("User", OverseasUser.class);
		final Collection<OverseasUser> users = Arrays.asList(user);
		EasyMock.expect(getOverseasUserDAO().findUsers(filter, pagingInfo)).andReturn(users);
		replayAll();

		final Collection<OverseasUser> actualUsers = getOverseasUserService().findUsers(filter);

		assertSame("The users are returned", users, actualUsers);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#getUserDAO()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testGetUserDAO() {
		replayAll();

		final OverseasUserDAO actualUserDAO = getOverseasUserService().getUserDAO();

		assertSame("The overseas user DAO is returned", getOverseasUserDAO(), actualUserDAO);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.OverseasUserService#makeAnonymous(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testMakeAnonymous() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		user.makeAnonymous();
		final String username = "Username";
		EasyMock.expect(user.getUsername()).andReturn(username);
		final UserDetails user2 = createMock("User2", OverseasUser.class);
		EasyMock.expect(getOverseasUserDAO().loadUserByUsername(username, false)).andReturn(user2);
		user.makeAnonymous();
		final String username2 = "Username2";
		EasyMock.expect(user.getUsername()).andReturn(username2);
		EasyMock.expect(getOverseasUserDAO().loadUserByUsername(username2, false)).andReturn(null);
		user.setLastUpdated((Date) EasyMock.anyObject());
		getOverseasUserDAO().makePersistent(user);
		replayAll();

		getOverseasUserService().makeAnonymous(user);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#makeNewUser(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testMakeNewUser() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final String password = "Password";
		EasyMock.expect(user.getPassword()).andReturn(password);
		user.setPassword((String) EasyMock.anyObject());
		user.setScytlPassword((String) EasyMock.anyObject());
		setUpForSave(user);
		getZohoService().sendContactToZoho(user);
		EasyMock.expectLastCall();
		replayAll();
		getOverseasUserService().makeNewUser(user);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.OverseasUserService#saveUser(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	@Test
	public final void testSaveUser() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final UserAddress currentAddress = createMock("CurrentAddress", UserAddress.class);
		EasyMock.expect(user.getCurrentAddress()).andReturn(currentAddress);
		EasyMock.expect(currentAddress.isEmptySpace()).andReturn(false);
		final UserAddress votingAddress = createMock("VotingAddress", UserAddress.class);
		EasyMock.expect(user.getVotingAddress()).andReturn(votingAddress);
		EasyMock.expect(votingAddress.isEmptySpace()).andReturn(false);
		final UserAddress forwardingAddress = createMock("ForwardingAddress", UserAddress.class);
		EasyMock.expect(user.getForwardingAddress()).andReturn(forwardingAddress);
		EasyMock.expect(forwardingAddress.isEmptySpace()).andReturn(false);
		final UserAddress previousAddress = createMock("PreviousAddress", UserAddress.class);
		EasyMock.expect(user.getPreviousAddress()).andReturn(previousAddress);
		EasyMock.expect(previousAddress.isEmptySpace()).andReturn(false);
		user.setLastUpdated((Date) EasyMock.anyObject());
		getOverseasUserDAO().makePersistent(user);
		replayAll();

		getOverseasUserService().saveUser(user);

		verifyAll();
	}

	/**
	 * Sets up the user to be saved.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	protected void setUpForSave(final OverseasUser user) {
		final UserAddress currentAddress = createMock("CurrentAddress", UserAddress.class);
		EasyMock.expect(user.getCurrentAddress()).andReturn(currentAddress);
		EasyMock.expect(currentAddress.isEmptySpace()).andReturn(false);
		final UserAddress votingAddress = createMock("VotingAddress", UserAddress.class);
		EasyMock.expect(user.getVotingAddress()).andReturn(votingAddress);
		EasyMock.expect(votingAddress.isEmptySpace()).andReturn(false);
		final UserAddress forwardingAddress = createMock("ForwardingAddress", UserAddress.class);
		EasyMock.expect(user.getForwardingAddress()).andReturn(forwardingAddress);
		EasyMock.expect(forwardingAddress.isEmptySpace()).andReturn(false);
		final UserAddress previousAddress = createMock("PreviousAddress", UserAddress.class);
		EasyMock.expect(user.getPreviousAddress()).andReturn(previousAddress);
		EasyMock.expect(previousAddress.isEmptySpace()).andReturn(false);
		user.setLastUpdated((Date) EasyMock.anyObject());
		getOverseasUserDAO().makePersistent(user);
	}

	/**
	 * Gets the overseas user DAO.
	 * 
	 * @author IanBrown
	 * @return the overseas user DAO.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private OverseasUserDAO getOverseasUserDAO() {
		return overseasUserDAO;
	}

	/**
	 * Gets the overseas user service.
	 * 
	 * @author IanBrown
	 * @return the overseas user service.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private OverseasUserService getOverseasUserService() {
		return overseasUserService;
	}

	/**
	 * Sets the overseas user DAO.
	 * 
	 * @author IanBrown
	 * @param overseasUserDAO
	 *            the overseas user DAO to set.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private void setOverseasUserDAO(final OverseasUserDAO overseasUserDAO) {
		this.overseasUserDAO = overseasUserDAO;
	}

	/**
	 * Sets the overseas user service.
	 * 
	 * @author IanBrown
	 * @param overseasUserService
	 *            the overseas user service to set.
	 * @since Jun 7, 2012
	 * @version Jun 7, 2012
	 */
	private void setOverseasUserService(final OverseasUserService overseasUserService) {
		this.overseasUserService = overseasUserService;
	}

	/**
	 * Sets the overseas zoho integration service.
	 *
	 * @author Shahriar Miraj
	 * @param zohoService
	 *            the overseas user service to set.
	 * @since Nov 7, 2021
	 * @version Nov 7, 2021
	 */


	public ZohoService getZohoService() {
		return zohoService;
	}

	public void setZohoService(ZohoService zohoService) {
		this.zohoService = zohoService;
	}
}
