/**
 * 
 */
package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.service.OverseasUserService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link UserExport}.
 * 
 * TODO this class is not really being unit tested as it depends on a number of
 * other classes - some that it creates and some from which it uses static
 * methods. These tests could be more thorough in checking results and in
 * checking the filter settings.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Dec 22, 2011
 */
public final class UserExportTest extends EasyMockSupport {

	/**
	 * the overseas user service.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private OverseasUserService overseasUserService;

	/**
	 * the user export to test.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private UserExport userExport;

	/**
	 * Sets up to test the user export.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Before
	public final void setUpUserExport() {
		setOverseasUserService(createMock("OverseasUserService",
				OverseasUserService.class));
		setUserExport(createUserExport());
        ReflectionTestUtils.setField(getUserExport(),"overseasUserService",getOverseasUserService());
	}

	/**
	 * Tears down the user export after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@After
	public final void tearDownUserExport() {
		setUserExport(null);
		setOverseasUserService(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.UserExport#doExportUsers(Integer, Integer, String)}
	 * for the case where a CSV is requested.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem with the request.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testHandleRequestInternalHttpServletRequestHttpServletResponse_csv()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setParameter("format", "csv");
		final Collection<OverseasUser> users = new ArrayList<OverseasUser>();
		EasyMock.expect(
				getOverseasUserService().findUsers(
						(UserFilterForm) EasyMock.anyObject()))
				.andReturn(users);
		replayAll();

		final ResponseEntity<byte[]> actualResponseEntity = getUserExport()
				.doExportUsers( 0, 65000, "csv" );

		assertNotNull( "Nothing is returned", actualResponseEntity );
		assertEquals("The content type is set to CSV", "text/csv",
				actualResponseEntity.getHeaders().getFirst( "Content-Type" ));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.UserExport#doExportUsers(Integer, Integer, String)}
	 * for the case where an Excel spreadsheet is requested.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem with the request.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testHandleRequestInternalHttpServletRequestHttpServletResponse_excel()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setParameter("format", "xls");
		final Collection<OverseasUser> users = new ArrayList<OverseasUser>();
		EasyMock.expect(
				getOverseasUserService().findUsers(
						(UserFilterForm) EasyMock.anyObject()))
				.andReturn(users);
		replayAll();

		final ResponseEntity<byte[]> actualResponseEntity  = getUserExport()
				.doExportUsers( 0, 65000, "xls" );

        assertNotNull( "Nothing is returned", actualResponseEntity );
		assertEquals("The content type is set to Excel",
				"application/vnd.ms-excel", actualResponseEntity.getHeaders().getFirst( "Content-Type" ));
		verifyAll();
	}


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.UserExport#doExportUsers(Integer, Integer, String)}
	 * for the defaults and one set of users.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem with the request.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testHandleRequestInternalHttpServletRequestHttpServletResponse_oneSetOfUsers()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final OverseasUser user = createUser(1);
		final Collection<OverseasUser> users = Arrays.asList(user);
		EasyMock.expect(
				getOverseasUserService().findUsers(
						(UserFilterForm) EasyMock.anyObject()))
				.andReturn(users);
		EasyMock.expect(
				getOverseasUserService().findUsers(
						(UserFilterForm) EasyMock.anyObject())).andReturn(
				new ArrayList<OverseasUser>());
		replayAll();

        final ResponseEntity<byte[]> actualResponseEntity  = getUserExport()
                .doExportUsers( 0, 65000, "xls" );

        assertNotNull( "Nothing is returned", actualResponseEntity );
        assertEquals("The content type is set to Excel",
                "application/vnd.ms-excel", actualResponseEntity.getHeaders().getFirst( "Content-Type" ));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.UserExport#doExportUsers(Integer, Integer, String)}
	 * for the defaults and two sets of users.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem with the request.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testHandleRequestInternalHttpServletRequestHttpServletResponse_twoSetsOfUsers()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final OverseasUser user1 = createUser(1);
		final Collection<OverseasUser> users1 = Arrays.asList(user1);
		EasyMock.expect(
				getOverseasUserService().findUsers(
						(UserFilterForm) EasyMock.anyObject())).andReturn(
				users1);
		final OverseasUser user2 = createUser(2);
		final Collection<OverseasUser> users2 = Arrays.asList(user2);
		EasyMock.expect(
				getOverseasUserService().findUsers(
						(UserFilterForm) EasyMock.anyObject())).andReturn(
				users2);
		EasyMock.expect(
				getOverseasUserService().findUsers(
						(UserFilterForm) EasyMock.anyObject())).andReturn(
				new ArrayList<OverseasUser>());
		replayAll();

        final ResponseEntity<byte[]> actualResponseEntity  = getUserExport()
                .doExportUsers( 0, 65000, "xls" );

        assertNotNull( "Nothing is returned", actualResponseEntity );
        assertEquals("The content type is set to Excel",
                "application/vnd.ms-excel", actualResponseEntity.getHeaders().getFirst( "Content-Type" ));
		verifyAll();
	}


	/**
	 * Creates a user.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the unique ID for the user.
	 * @return the user.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@SuppressWarnings("unchecked")
	private OverseasUser createUser(final int id) {
		final OverseasUser user = new OverseasUser();
        user.setId( (long) id );
                //createMock("user" + id, OverseasUser.class);

		final Person personName = new Person();
                //createMock("person" + id, Person.class);
		final Person personPrevName = new Person();
                //createMock("personPrev" + id, Person.class);
		final UserAddress votingAddress = new UserAddress();
                //createMock("VotingAddress" + id,
				//UserAddress.class);
        user.setVotingAddress( votingAddress );
		//EasyMock.expect(user.getVotingAddress()).andReturn(votingAddress);
		//votingAddress.populateAddressFields(
		//		(Map<String, String>) EasyMock.anyObject(),
		//		EasyMock.eq("Voting "), EasyMock.eq(false));
		final UserAddress currentAddress = new UserAddress();
                //createMock("CurrentAddress" + id,
				//UserAddress.class);
		//EasyMock.expect(user.getCurrentAddress()).andReturn(currentAddress);
        user.setCurrentAddress( currentAddress );
		//currentAddress.populateAddressFields(
		//		(Map<String, String>) EasyMock.anyObject(),
		//		EasyMock.eq("Current "), EasyMock.eq(true));
		
		final UserAddress prevAddress = new UserAddress();
                //createMock("PreviousAddress"
				//+ id, UserAddress.class);
		//EasyMock.expect(user.getPreviousAddress()).andReturn(
		//		prevAddress);
        user.setPreviousAddress( prevAddress );
		//prevAddress.populateAddressFields(
		//		(Map<String, String>) EasyMock.anyObject(),
		//		EasyMock.eq("Previous "), EasyMock.eq(true));
		
		final UserAddress forwardingAddress = new UserAddress();
                //createMock("ForwardingAddress"
				//+ id, UserAddress.class);
        user.setForwardingAddress( forwardingAddress );
		//EasyMock.expect(user.getForwardingAddress()).andReturn(
		//		forwardingAddress);
		//forwardingAddress.populateAddressFields(
		//		(Map<String, String>) EasyMock.anyObject(),
		//		EasyMock.eq("Forwarding "), EasyMock.eq(true));
		//forwardingAddress.populateAddressFields(
		//		(Map<String, String>) EasyMock.anyObject(),
		//		EasyMock.eq("Mailing "), EasyMock.eq(true));
		final VotingRegion votingRegion = new VotingRegion();
                //createMock("VotingRegion" + id,
				//VotingRegion.class);
        user.setVotingRegion( votingRegion );
		//EasyMock.expect(user.getVotingRegion()).andReturn(votingRegion)
		//		.times(2);
		final String fullName = "Full Name " + id;
        votingRegion.setName( fullName );
		//EasyMock.expect(votingRegion.getName()).andReturn(fullName); // .getFullName()
		final VoterType voterType = VoterType.values()[id
				% VoterType.values().length];
        user.setVoterType( voterType );
		//EasyMock.expect(user.getVoterType()).andReturn(voterType).times(2);
		final VoterHistory voterHistory = VoterHistory.values()[id
				% VoterHistory.values().length];
        user.setVoterHistory( voterHistory );
		//EasyMock.expect(user.getVoterHistory()).andReturn(voterHistory)
		//		.times(2);
		final String phone = "Phone" + id;
        user.setPhone( phone );
		//EasyMock.expect(user.getPhone()).andReturn(phone);
        final String altPhone = "AltPhone" + id;
        user.setAlternatePhone( altPhone );
        //EasyMock.expect(user.getAlternatePhone()).andReturn(altPhone);
		final String username = "Username" + id;
		user.setUsername( username );
        //EasyMock.expect(user.getUsername()).andReturn(username);
		//EasyMock.expect(user.getName()).andReturn(personName).atLeastOnce();
		final String userFullName = "Full Name " + id;
		//EasyMock.expect(personName.getFullName()).andReturn(userFullName);
		final String firstName = "First" + id;
        personName.setFirstName( firstName );
		//EasyMock.expect(personName.getFirstName()).andReturn(firstName);
		final String lastName = "Last" + id;
        personName.setLastName( lastName );
		//EasyMock.expect(personName.getLastName()).andReturn(lastName);
		final String middleName = "Middle" + id;
        personName.setMiddleName( middleName );
		//EasyMock.expect(personName.getMiddleName()).andReturn(middleName);
		final String suffix = "Suffix" + id;
        personName.setSuffix( suffix );
		//EasyMock.expect(personName.getSuffix()).andReturn(suffix);
		final String prefix = "Prefix" + id;
		personName.setTitle( prefix );
		//EasyMock.expect(personName.getTitle()).andReturn(prefix);
		
		final String previousName = "Previous" + id;
		//EasyMock.expect(user.getPreviousName()).andReturn(personPrevName).atLeastOnce();
		//EasyMock.expect(personPrevName.getFullName()).andReturn(previousName);
        personPrevName.setFirstName(previousName + "FirstName");
        personPrevName.setLastName(previousName + "LastName");
        personPrevName.setMiddleName(previousName + "MiddleName");
        personPrevName.setSuffix(previousName + "Suffix");
        personPrevName.setTitle(previousName + "Title");
		//EasyMock.expect(personPrevName.getFirstName()).andReturn(previousName + "FirstName");
		//EasyMock.expect(personPrevName.getLastName()).andReturn(previousName + "LastName");
		//EasyMock.expect(personPrevName.getMiddleName()).andReturn(previousName + "MiddleName");
		//EasyMock.expect(personPrevName.getSuffix()).andReturn(previousName + "Suffix");
		//EasyMock.expect(personPrevName.getTitle()).andReturn(previousName + "Title");
		
		final int birthMonth = 1 + (id % 12);
        user.setBirthMonth( birthMonth );
		//EasyMock.expect(user.getBirthMonth()).andReturn(birthMonth).anyTimes();
		final int birthDate = 1 + (id % 28);
        user.setBirthDate( birthDate );
		//EasyMock.expect(user.getBirthDate()).andReturn(birthDate).anyTimes();
		final int birthYear = 1950 + id;
        user.setBirthYear( birthYear );
		//EasyMock.expect(user.getBirthYear()).andReturn(birthYear).anyTimes();
		final String race = "Race" + id;
        user.setRace( race );
		//EasyMock.expect(user.getRace()).andReturn(race);
		final String ethnicity = "Ethnicity" + id;
        user.setEthnicity( ethnicity );
		//EasyMock.expect(user.getEthnicity()).andReturn(ethnicity);
		final String gender = ((id % 2) == 0) ? "Male" : "Female";
        user.setGender( gender );
		//EasyMock.expect(user.getGender()).andReturn(gender);
		final String ballotPref = "Ballot Preference " + id;
        user.setBallotPref( ballotPref );
		//EasyMock.expect(user.getBallotPref()).andReturn(ballotPref);
		return user;
	}

	/**
	 * Creates a user export.
	 * 
	 * @author IanBrown
	 * @return the user export.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private UserExport createUserExport() {
		return new UserExport();
	}

	/**
	 * Gets the overseas user service.
	 * 
	 * @author IanBrown
	 * @return the overseas user service.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private OverseasUserService getOverseasUserService() {
		return overseasUserService;
	}

	/**
	 * Gets the user export.
	 * 
	 * @author IanBrown
	 * @return the user export.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private UserExport getUserExport() {
		return userExport;
	}

	/**
	 * Sets the overseas user service.
	 * 
	 * @author IanBrown
	 * @param overseasUserService
	 *            the overseas user service to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setOverseasUserService(
			final OverseasUserService overseasUserService) {
		this.overseasUserService = overseasUserService;
	}

	/**
	 * Sets the user export.
	 * 
	 * @author IanBrown
	 * @param userExport
	 *            the user export to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setUserExport(final UserExport userExport) {
		this.userExport = userExport;
	}

}
