package com.bearcode.ovf.service;

import com.bearcode.ovf.AbstractApplicationTest;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.VoterHistory;
import com.bearcode.ovf.model.common.VoterType;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Integration test for {@link OverseasUserService}.
 * @author PLarkin
 */
public class OverseasUserServiceIntegration extends AbstractApplicationTest {

	@Test
	@Ignore
	// TODO Need to rework this test.
	public void testAnonymizeUser() {

		final Map<String, String> vals = defaulUserVals();

		// create the new user
		final OverseasUser user = buildUser();
		overseasUserService.makeAnonymous(user);

		// find the new user
		final OverseasUser userNotFound = overseasUserService.findUserByName(vals.get("userName"));
		assertNull(userNotFound);
		final OverseasUser userFound = overseasUserService.findUserByName(user.getUsername());
		assertNotNull(userFound);
		assertEquals("Password is not inactive", OverseasUser.DISABLED_PASSWORD, userFound.getPassword());
		assertFalse("Username is not anonymous", userFound.getUsername().equals(vals.get("userName")));
		assertTrue("Firstname is not anonymous", userFound.getName().getFirstName().matches(OverseasUser.REMOVED_FIELD_PATTERN + ".+"));
		assertTrue("Lastname is not anonymous", userFound.getName().getLastName().matches(OverseasUser.REMOVED_FIELD_PATTERN + ".+"));
		assertTrue("Middlename is not anonymous", userFound.getName().getMiddleName().matches(OverseasUser.REMOVED_FIELD_PATTERN + ".+"));
		assertTrue("Previousname is not anonymous", userFound.getPreviousName().getFirstName().matches(OverseasUser.REMOVED_FIELD_PATTERN + ".+"));
		assertTrue("suffix is not anonymous", userFound.getName().getSuffix().matches(""));

		// delete the new user
		overseasUserService.deleteUser(userFound);
	}

	//@Test
	//public void testBogus() {
		// otherwise if all tests are disabled there is an exception
	//}

	@Test
	@Ignore
	// TODO Need to rework this test.
	public void testCreateAndDeleteUser() {

		final Map<String, String> vals = defaulUserVals();

		// create the new user
		final OverseasUser origUser = buildUser();
		overseasUserService.makeNewUser(origUser);

		// find the new user
		final OverseasUser userFound = overseasUserService.findUserByName(vals.get("userName"));
		assertNotNull(userFound);
		assertEquals(origUser.getUsername(), userFound.getUsername());
		assertEquals(origUser.getName().getFirstName(), userFound.getName().getFirstName());
		assertEquals(origUser.getName().getMiddleName(), userFound.getName().getMiddleName());
		assertEquals(origUser.getName().getLastName(), userFound.getName().getLastName());
		assertEquals(origUser.getName().getSuffix(), userFound.getName().getSuffix());
		assertEquals(origUser.getPreviousName().getFirstName(), userFound.getPreviousName().getFirstName());
		assertEquals(origUser.getBirthMonth(), userFound.getBirthMonth());
		assertEquals(origUser.getBirthYear(), userFound.getBirthYear());
		assertEquals(origUser.getVoterType(), userFound.getVoterType());
		assertEquals(origUser.getVoterHistory(), userFound.getVoterHistory());

		// delete the new user
		overseasUserService.deleteUser(userFound);
	}

	@Test
	@Ignore
	// TODO Need to rework this test.
	public void testFindUser() {
		final OverseasUser user = overseasUserService.findUserById(1);
		assertNotNull("Could not find user 1", user);
		assertEquals("User 1 firstname did not match", "joe", user.getName().getFirstName());
	}

	private OverseasUser buildUser() {

		final Map<String, String> vals = defaulUserVals();

		final OverseasUser user = new OverseasUser();
		user.setUsername(vals.get("userName"));
		user.getName().setFirstName(vals.get("firstName"));
		user.getName().setMiddleName(vals.get("middleName"));
		user.getName().setLastName(vals.get("lastName"));
		user.getName().setSuffix(vals.get("suffix"));
		user.getPreviousName().setFirstName(vals.get("previousName"));
		user.setBirthMonth(Integer.parseInt(vals.get("birthMonth")));
		user.setBirthYear(Integer.parseInt(vals.get("birthYear")));
		user.setVoterType(VoterType.OVERSEAS_VOTER);
		user.setVoterHistory(VoterHistory.DOMESTIC_VOTER);
		return user;
	}

	private Map<String, String> defaulUserVals() {

		final Map<String, String> vals = new HashMap<String, String>(0);
		vals.put("userName", "test@example.com");
		vals.put("firstName", "testy");
		vals.put("middleName", "test");
		vals.put("lastName", "tester");
		vals.put("suffix", "junior");
		vals.put("previousName", "My Previous Name");
		vals.put("birthMonth", "12");
		vals.put("birthYear", "1950");
		return vals;
	}

}
