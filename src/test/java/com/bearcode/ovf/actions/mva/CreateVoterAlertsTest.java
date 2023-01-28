package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.validators.OverseasUserValidator;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public final class CreateVoterAlertsTest extends BaseControllerCheck<CreateVoterAlerts> {

    private OverseasUserService userService;

    private OverseasUserValidator validator;

    @Test
    public final void testVoterAlertView() throws Exception{
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final OverseasUser user = createMock("user", OverseasUser.class);
        final Authentication
                authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, user);
        final ModelMap model = createModelMap(user, request, null, true, false);
        addAttributeToModelMap(model, "user", user);
        replayAll();
        final String actualReferences = getBaseController()
                .showCreateVoterAlertForm( request, null, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);

        verifyAll();
    }

    @Test
    public final void testVoterAlertSave_withNonExistingUser(){
        OverseasUser user = new OverseasUser();
        user.getName().setFirstName("Test User");
        user.setVoterHistory(VoterHistory.DOMESTIC_VOTER);
        UserAddress currentAddress = new UserAddress();
        currentAddress.setCity("Test City");
        currentAddress.setCounty("Test Country");
        currentAddress.setState("Test State");
        user.setUsername("test@gmail.com");
        final MockHttpServletRequest request = new MockHttpServletRequest();

        OverseasUserService userService = createMock("userService", OverseasUserService.class);
        EasyMock.expect(userService.findUserByName(user.getUsername())).andReturn(null);

        final String userRole = UserRole.USER_ROLE_FACE_ADMIN;
        final UserRole role = createMock("Role", UserRole.class);
        final Collection<UserRole> roles = Arrays.asList(role);
        EasyMock.expect(userService.findRolesByName(userRole)).andReturn(roles);
        userService.makeNewUser(user);
        EasyMock.expectLastCall();
        replayAll();

        OverseasUser existingUser = userService.findUserByName("test@gmail.com");
        assertNull("User exist", existingUser);

		Collection<UserRole> userRoles = userService.findRolesByName(userRole);
		assertNotNull("Roles not found", userRoles);
    }

    @Test
    public final void testVoterAlertSave_withExistingUser(){
        OverseasUser user = new OverseasUser();
        user.getName().setFirstName("Test User");
        user.setVoterHistory(VoterHistory.DOMESTIC_VOTER);
        UserAddress currentAddress = new UserAddress();
        currentAddress.setCity("Test City");
        currentAddress.setCounty("Test Country");
        currentAddress.setState("Test State");
        user.setUsername("test@gmail.com");
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final ModelMap model = createModelMap(user, request, null, true, true);
        addAttributeToModelMap(model, "user", user);
        final Authentication
                authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, user);
        OverseasUser expectedUser = new OverseasUser();
        user.setId(1L);
        user.getName().setFirstName("Test User");
        user.setVoterHistory(VoterHistory.DOMESTIC_VOTER);
        UserAddress expectedUserCurrentAddress = new UserAddress();
        expectedUserCurrentAddress.setCity("Test City");
        expectedUserCurrentAddress.setCounty("Test Country");
        expectedUserCurrentAddress.setState("Test State");
        expectedUser.setUsername("test@gmail.com");

        OverseasUserService userService = createMock("userService", OverseasUserService.class);
        EasyMock.expect(userService.findUserByName("test@gmail.com")).andReturn(expectedUser);

        final String userRole = UserRole.USER_ROLE_FACE_ADMIN;
        final UserRole role = createMock("Role", UserRole.class);
        final Collection<UserRole> roles = Arrays.asList(role);
        EasyMock.expect(userService.findRolesByName(userRole)).andReturn(roles);

        replayAll();

        Collection<UserRole> userRoles = userService.findRolesByName(userRole);
        assertNotNull("Roles not found", userRoles);

        OverseasUser existingUser = userService.findUserByName("test@gmail.com");
        assertNotNull("User exist", existingUser);

        final String actualReferences = getBaseController()
                .showCreateVoterAlertForm( request, null, model );
        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);
    }

    @Override
    protected CreateVoterAlerts createBaseController() {
        final CreateVoterAlerts createVoterAlerts = new CreateVoterAlerts();
        ReflectionTestUtils.setField(createVoterAlerts, "userService", userService);
        ReflectionTestUtils.setField(createVoterAlerts, "overseasUserValidator", validator);
        return createVoterAlerts;
    }

    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/CreateVoterAlerts.jsp";
    }

    @Override
    protected String getExpectedPageTitle() {
        return "Create Voter Alerts";
    }

    @Override
    protected String getExpectedSectionCss() {
        return "/css/rava.css";
    }

    @Override
    protected String getExpectedSectionName() {
        return "rava";
    }

    @Override
    protected String getExpectedSuccessContentBlock() {
        return null;
    }

    @Override
    protected void setUpForBaseController() {
        setUserService(createMock("OverseasUserService", OverseasUserService.class));
        setValidator(createMock("OverseasUserValidator", OverseasUserValidator.class));

    }

    @Override
    protected void tearDownForBaseController() {
        setUserService(null);
        setValidator(null);
    }

    public void setUserService(OverseasUserService userService) {
        this.userService = userService;
    }

    public void setValidator(OverseasUserValidator validator) {
        this.validator = validator;
    }
}