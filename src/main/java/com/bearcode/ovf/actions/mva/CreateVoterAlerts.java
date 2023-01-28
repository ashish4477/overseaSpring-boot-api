package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.ExtendedProfile;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class CreateVoterAlerts extends BaseController {

	@Autowired
	private OverseasUserService userService;

	@Autowired
	private OverseasUserValidator overseasUserValidator;

	@Autowired
	private EodApiService eodApiService;

	public CreateVoterAlerts() {
		setSectionCss( "/css/rava.css" );
		setSectionName( "rava" );
		setContentBlock( "/WEB-INF/pages/blocks/CreateVoterAlerts.jsp" );
		setPageTitle( "Create Voter Alerts" );
	}

	@ModelAttribute("user")
	protected OverseasUser getOvfUser() {
		OverseasUser user = super.getUser();
		if ( user == null ) {
			user = new OverseasUser();
		}
		return user;
	}

	@RequestMapping(value = "/CreateVoterAlert.htm", method = RequestMethod.GET)
	public String showCreateVoterAlertForm(HttpServletRequest request,
										   @ModelAttribute("user") OverseasUser user,
										   ModelMap model) {
		return buildModelAndView( request, model );
	}

	@RequestMapping(value = "/CreateVoterAlert.htm", method = RequestMethod.POST)
	public String postVoterAlert(HttpServletRequest request,
								 @ModelAttribute("user") OverseasUser user,
								 BindingResult result, ModelMap model){
		user.setVoterAlertOnly(true);
		validateUser( request, user, result );
		if (result.hasErrors() ) {
			setContentBlock( "/WEB-INF/pages/blocks/CreateVoterAlerts.jsp" );
			setPageTitle( "Create Voter Alert" );
			return buildModelAndView(request, model); // redirect to form
		}
		user.setRoles(userService.findRolesByName(UserRole.USER_ROLE_VOTER));
		EodRegion region = eodApiService.getRegion(user.getEodRegionId());
		if(region != null){
			user.setEodRegionName(region.getRegionName());
		}
		userService.makeNewUser(user);
		ExtendedProfile extendedProfile = userService.findExtendedProfile(user);
		if(extendedProfile == null){
			extendedProfile = new ExtendedProfile();
			extendedProfile.setUser( user );
		}
		userService.saveExtendedProfile(extendedProfile, user.getVoterType());
		return "redirect:/home.htm";
	}

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		if ( binder.getTarget() instanceof OverseasUser ) {
			binder.setValidator( overseasUserValidator );
		}
	}

	protected void validateUser( HttpServletRequest request,
								 OverseasUser user,
								 BindingResult errors ) {
		overseasUserValidator.validate( user, errors, getUserValidationSkipFields( request ) );
	}
}
