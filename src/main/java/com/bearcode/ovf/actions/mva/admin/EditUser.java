package com.bearcode.ovf.actions.mva.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.validators.AdminUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Oct 24, 2007 Time: 3:25:30 PM
 * 
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditAccount.htm")
public class EditUser extends BaseController {

	@Autowired
	private OverseasUserService userService;

	@Autowired
	private AdminUserValidator validator;

	public EditUser() {
		setSectionName("admin");
		setSectionCss("/css/admin.css");
		setPageTitle("Edit Voter Account");
		setContentBlock("/WEB-INF/pages/blocks/admin/MvaEditUser.jsp");
	}

	@ModelAttribute("faceConfigs")
	public Collection<FaceConfig> getFaceConfigs() {
		return getFacesService().findAllConfigs();
	}

	@ModelAttribute("ovfUser")
	public OverseasUser getOvfUser(@RequestParam(value = "userId", required = false) final Long userId, final ModelMap model)
			throws Exception {
		if (userId != null && userId != 0) {
			final OverseasUser user = userService.findUserById(userId);
			if (user != null) {
				model.addAttribute("pdfLog", userService.findGenerationLog(user));
				return user;
			}
		}
		return new OverseasUser();
	}

	@ModelAttribute("roles")
	public Collection<UserRole> getRoles() {
		return userService.findRoles();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String saveUser(final HttpServletRequest request, final ModelMap model,
			@ModelAttribute("ovfUser") @Valid final OverseasUser ovfUser, final BindingResult errors,
			@RequestParam(value = "selectedRoles", required = false) final Long[] roles,
			@RequestParam(value = "selectedFace", required = false) final Long faceId,
            @RequestParam(value = "newPasswd", required = false) final String newPassword ) {

		if (ovfUser.getId() == 0) { // new user should have a password
			if (newPassword == null || newPassword.trim().length() == 0) {
				errors.rejectValue("password", "mva.password.empty", "Missing password");
			}
		}
		if (newPassword != null && newPassword.length() > 0) {
			if (!errors.hasErrors() && newPassword.length() < 6) {
				errors.rejectValue("password", "mva.password.6_char_min", "Password grete than 200 characters.");
			}
			if (!errors.hasErrors() && newPassword.length() > 200) {
				errors.rejectValue("password", "mva.password.200_char_max", "Password must be at least 6 characters.");
			}
		}

		if (!errors.hasErrors()) {
			if (ovfUser.getRoles() != null) {
				ovfUser.getRoles().clear();
			} else {
				ovfUser.setRoles(new LinkedList<UserRole>());
			}
			if (roles != null && roles.length > 0) {
				for (final long roleId : roles) {
					final UserRole role = userService.findRoleById(roleId);
					ovfUser.getRoles().add(role);
				}
			}
			final UserRole faceAdminRole = userService.findRolesByName(UserRole.USER_ROLE_FACE_ADMIN).iterator().next();
			final UserRole reportingDashboardRole = userService.findRolesByName(UserRole.USER_ROLE_REPORTING_DASHBOARD).iterator()
					.next();
			final UserRole pendingVoterRegistrationsRole = userService
					.findRolesByName(UserRole.USER_ROLE_PENDING_VOTER_REGISTRATIONS).iterator().next();
			if (ovfUser.getRoles().contains(faceAdminRole) || ovfUser.getRoles().contains(reportingDashboardRole)
					|| ovfUser.getRoles().contains(pendingVoterRegistrationsRole)) {
				if (faceId != 0) {
					final FaceConfig face = getFacesService().findConfigById(faceId);
					if (face != null) {
						ovfUser.setAssignedFace(face);
					}
				}
				if (ovfUser.getFaces().size() != 1) {
					// if FACE ADMIN role was assigned to the user - he SHOULD have assigned face.
					// if face was not found - FACE ADMIN role should be removed from roles
					ovfUser.getRoles().remove(faceAdminRole);
					// Similar for pending voter registrations.
					ovfUser.getRoles().remove(pendingVoterRegistrationsRole);
				}
			} else {
				if (ovfUser.getFaces() != null) {
					ovfUser.getFaces().clear();
				}
			}
			if (ovfUser.getRoles().size() == 0) {
				ovfUser.setRoles(userService.findRolesByName(UserRole.USER_ROLE_VOTER));
			}
			if (newPassword != null && newPassword.length() > 0) {
				ovfUser.setPassword(OverseasUser.encrypt(newPassword));
				ovfUser.setScytlPassword(OverseasUser.encryptScytl(newPassword));
			}
			userService.saveUser(ovfUser);
		}

		return buildModelAndView(request, model);
	}

	public void setUserService(final OverseasUserService userService) {
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showPage(final HttpServletRequest request, final ModelMap model) {
		return buildModelAndView(request, model);
	}

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		if (binder.getTarget() instanceof OverseasUser) {
			binder.setValidator(validator);
		}
	}
}
