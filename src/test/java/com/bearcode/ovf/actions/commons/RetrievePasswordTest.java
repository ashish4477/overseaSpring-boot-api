/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.bearcode.ovf.forms.UserAccountForm;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.Person;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.validators.RemindPasswordValidator;

/**
 * Extended {@link BaseControllerCheck} test for
 * {@link RetrievePassword}.
 * 
 * @author IanBrown
 * 
 * @since Dec 21, 2011
 * @version Jan 3, 2012
 */
public final class RetrievePasswordTest extends BaseControllerCheck<RetrievePassword> {

	/**
	 * Sets up an EasyMock argument matcher for a mail command.
	 * 
	 * @author IanBrown
	 * @param expectedEmail
	 *            the expected mail command.
	 * @return always returns <code>null</code>.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private final static Email eqEmail(final Email expectedEmail) {
		EasyMock.reportMatcher(new IArgumentMatcher() {

			@Override
			public final void appendTo(final StringBuffer buffer) {
				buffer.append("eqMailCommand To: " + expectedEmail.getToAsString() + " Template: "
						+ expectedEmail.getTemplate() + " Model: " + expectedEmail.getModel() + " BCC: "
						+ expectedEmail.isBcc());
			}

			@Override
			public final boolean matches(final Object argument) {
				if (!(argument instanceof Email)) {
					return false;
				}

				final Email actualEmail = (Email) argument;
				return actualEmail.getToAsString().equals(expectedEmail.getToAsString())
						&& actualEmail.getTemplate().equals(expectedEmail.getTemplate())
						&& checkModel(expectedEmail.getModel(), actualEmail.getModel())
						&& (actualEmail.isBcc() == expectedEmail.isBcc());
			}

			/**
			 * Checks to see that the actual model matches the expected one. The
			 * code checks everything except the password directly. If the
			 * expected password is "***ANY***", then any password matches,
			 * otherwise an exact match is required.
			 * 
			 * @author IanBrown
			 * @param expectedModel
			 *            the expected model.
			 * @param actualModel
			 *            the actual model.
			 * @return <code>true</code> if the two match, <code>false</code>
			 *         otherwise.
			 * @since Dec 21, 2011
			 * @version Dec 21, 2011
			 */
			@SuppressWarnings("rawtypes")
			private boolean checkModel(final Map expectedModel, final Map actualModel) {
				for (final Object key : expectedModel.keySet()) {
					if ("newPass".equals(key)) {
						final Object expectedPassword = expectedModel.get(key);
						if (!"***ANY***".equals(expectedPassword)) {
							if (!actualModel.get(key).equals(expectedPassword)) {
								return false;
							}
						}
					} else {
						if (!actualModel.get(key).equals(actualModel.get(key))) {
							return false;
						}
					}
				}
				return true;
			}

		});

		return null;
	}

	/**
	 * the user service.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private OverseasUserService userService;

	/**
	 * the email service.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private EmailService emailService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#getUserForm(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetUserForm() {
		final String username = "User Name";
		replayAll();

		final UserAccountForm actualUserForm = getBaseController().getUserForm(username);

		assertNotNull("A user form is returned", actualUserForm);
		assertEquals("The username is set", username, actualUserForm.getUsername());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * for the case where the target is not a user account form.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testInitBinder_notUserAccountForm() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		final Object target = createMock("Target", Object.class);
		EasyMock.expect(binder.getTarget()).andReturn(target);
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#initBinder(org.springframework.web.bind.ServletRequestDataBinder)}
	 * for the case where the target is a user account form.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testInitBinder_userAccountForm() {
		final ServletRequestDataBinder binder = createMock("Binder", ServletRequestDataBinder.class);
		final UserAccountForm target = createMock("Target", UserAccountForm.class);
		EasyMock.expect(binder.getTarget()).andReturn(target);
		binder.setValidator((RemindPasswordValidator) EasyMock.anyObject());
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.UserAccountForm, org.springframework.validation.BindingResult)}
	 * for the case where there is no such user.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the form.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testOnSubmit_noSuchUser() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final UserAccountForm refreshPass = createMock("RefreshPass", UserAccountForm.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final String username = "Username";
		EasyMock.expect(refreshPass.getUsername()).andReturn(username).atLeastOnce();
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(null);
		errors.rejectValue("username", "", "User with email " + username + " could not be found.");
		replayAll();

		final String actualModelAndView = getBaseController().onSubmit(request, model, refreshPass, errors);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.UserAccountForm, org.springframework.validation.BindingResult)}
	 * for the case where there is a user, but there were previous errors.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the form.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testOnSubmit_previousErrors() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final UserAccountForm refreshPass = createMock("RefreshPass", UserAccountForm.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final String username = "Username";
		EasyMock.expect(refreshPass.getUsername()).andReturn(username).atLeastOnce();
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect(getUserService().findUserByName(username)).andReturn(user);
		EasyMock.expect(errors.hasErrors()).andReturn(true);
		errors.rejectValue("username", "", "User with email " + username + " could not be found.");
		replayAll();

		final String actualModelAndView = getBaseController().onSubmit(request, model, refreshPass, errors);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.UserAccountForm, org.springframework.validation.BindingResult)}
	 * for the case where there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the form.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testOnSubmit_user() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap( null, request, null, true, true );
		final UserAccountForm refreshPass = createMock( "RefreshPass", UserAccountForm.class );
		final BindingResult errors = createMock( "Errors", BindingResult.class );
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, null );
		final String username = "Username";
		EasyMock.expect(refreshPass.getUsername()).andReturn( username ).atLeastOnce();
		final OverseasUser user = createMock("User", OverseasUser.class);
		EasyMock.expect( getUserService().findUserByName( username ) ).andReturn( user );
		EasyMock.expect( errors.hasErrors() ).andReturn( false );
		EasyMock.expect(user.getUsername()).andReturn(username).times( 2 );
		setUpSendEmail( user, username, "***ANY***", "RelativePrefix/" );
		//user.setPassword((String) EasyMock.anyObject());
		//user.setScytlPassword((String) EasyMock.anyObject());
		//getUserService().saveUser(user);
		replayAll();

		final String actualModelAndView = getBaseController().onSubmit(request, model, refreshPass, errors);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#sendEmail(com.bearcode.ovf.model.common.OverseasUser, java.lang.String, com.bearcode.ovf.model.common.FaceConfig)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws EmailException
	 *             if there is a problem sending the email.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSendEmail() throws EmailException {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final String newPass = "New Password";
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final String username = "Username";
		EasyMock.expect(user.getUsername()).andReturn(username).times( 2 );
		final String relativePrefix = "RelativePrefix/";
		EasyMock.expect( config.getName() ).andReturn( "faces/ovf" ).anyTimes();
		EasyMock.expect( config.getRelativePrefix() ).andReturn( relativePrefix );
		EasyMock.expect( config.getUrlPath() ).andReturn( "Url_path" ).anyTimes();
		setUpSendEmail(user, username, newPass, relativePrefix);
		replayAll();

		getBaseController().sendEmail(user, newPass, config);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#setEmailService(com.bearcode.ovf.service.email.EmailService)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetEmailService() {
		assertSame("The email service is set", getEmailService(), ReflectionTestUtils.getField(getBaseController(), "emailService"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#setUserService(com.bearcode.ovf.service.OverseasUserService)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetUserService() {
		assertSame("The user service is set", getUserService(), ReflectionTestUtils.getField(getBaseController(), "userService"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.RetrievePassword#showPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testShowPage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualModelAndView = getBaseController().showPage(request, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final RetrievePassword createBaseController() {
		final RetrievePassword retrievePassword = new RetrievePassword();
		retrievePassword.setEmailService(getEmailService());
		retrievePassword.setUserService(getUserService());
		return retrievePassword;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/RemindPassword.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Remind Password";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return "/css/login.css";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return "login";
	}

	/** {@inheritDoc} */
	@Override
	protected String getExpectedSuccessContentBlock() {
		return "/WEB-INF/pages/blocks/RemindPasswordSuccess.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setEmailService(createMock("EmailService", EmailService.class));
		setUserService(createMock("UserService", OverseasUserService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setUserService(null);
		setEmailService(null);
	}

	/**
	 * Gets the email service.
	 * 
	 * @author IanBrown
	 * @return the email service.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private EmailService getEmailService() {
		return emailService;
	}

	/**
	 * Gets the user service.
	 * 
	 * @author IanBrown
	 * @return the user service.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private OverseasUserService getUserService() {
		return userService;
	}

	/**
	 * Sets the email service.
	 * 
	 * @author IanBrown
	 * @param emailService
	 *            the email service to set.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Sets up to send email for a new password for the user.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @param username
	 *            the username.
	 * @param newPass
	 *            the new password.
	 * @param relativePrefix
	 *            the relative prefix.
	 * @throws EmailException
	 *             if there is a problem setting up the email.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setUpSendEmail(final OverseasUser user, final String username, final String newPass, final String relativePrefix)
			throws EmailException {
		final String approvedFilename = "Approved Filename";
		EasyMock.expect(getFacesService().getApprovedFileName(EmailTemplates.XML_CHANGE_PASSWORD, relativePrefix)).andReturn(
				approvedFilename);
		final String firstName = "First Name";
		final Person name = createMock("Name", Person.class);
		EasyMock.expect(user.getName()).andReturn(name).atLeastOnce();
		EasyMock.expect(name.getFirstName()).andReturn(firstName);
		final Email email = createMock("Email", Email.class);
		EasyMock.expect(email.getToAsString()).andReturn(username).atLeastOnce();
		EasyMock.expect(email.getTemplate()).andReturn(approvedFilename).atLeastOnce();
		final Map model = new HashMap();
		EasyMock.expect(email.getModel()).andReturn(model).atLeastOnce();
		model.put("firstName", firstName);
		model.put("newPass", newPass);
		EasyMock.expect(email.isBcc()).andReturn(false).atLeastOnce();
		getEmailService().queue(eqEmail(email));
	}

	/**
	 * Sets the user service.
	 * 
	 * @author IanBrown
	 * @param userService
	 *            the user service to set.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private void setUserService(final OverseasUserService userService) {
		this.userService = userService;
	}
}
