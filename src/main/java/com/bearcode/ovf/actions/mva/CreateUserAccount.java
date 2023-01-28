package com.bearcode.ovf.actions.mva;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.SurveyWizard;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.editor.VotingRegionPropertyEditor;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.tools.SendEmailHelper;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

/**
 * Date: 13.10.11
 * Time: 17:40
 *
 * @author Leonid Ginzburg
 */
@Controller
public class CreateUserAccount extends BaseController {
    public static final String CREATE_USER_FROM_RAVA_FORM = "CREATE_USER_FROM_RAVA_FORM";

    @Autowired
    private OverseasUserService userService;

    @Autowired
    private OverseasUserValidator overseasUserValidator;

    @Autowired
    private VotingRegionPropertyEditor votingRegionPropertyEditor;

    @Autowired
    private EodApiService eodApiService;

    @Autowired
    private SendEmailHelper sendEmailHelper;

    public CreateUserAccount() {
        setSectionCss( "/css/rava.css" );
        setSectionName( "rava" );
    }

    @ModelAttribute("phoneNumberTypes")
    public String[] getPhoneNumberTypes() {
        return PhoneNumberType.getStringValues();
    }

    @RequestMapping(value = "/CreateAccount.htm", method = RequestMethod.GET)
    public String showCreateForm( HttpServletRequest request, ModelMap model,
                                  @ModelAttribute("user") OverseasUser user ) {
        if ( user != null && user.getVotingAddress() != null && StringUtils.isNotEmpty( user.getVotingAddress().getState() )) {
            model.addAttribute( "regions", eodApiService.getRegionsOfState( user.getVotingAddress().getState() ) );
        }
        if ( user != null && StringUtils.isNotEmpty( user.getEodRegionId() )) {
            model.addAttribute( "selectedRegion", eodApiService.getRegion( user.getEodRegionId() ) );
        }

        setContentBlock( "/WEB-INF/pages/blocks/CreateAccount.jsp" );
        setPageTitle( "Create Account" );
        return buildModelAndView( request, model );
    }

    @RequestMapping(value = "/CreatePassword.htm", method = RequestMethod.GET)
    public String showCreatePasswordForm( HttpServletRequest request, ModelMap model,
                                          @ModelAttribute("user") OverseasUser user ) {
        // if no form, then just show the regular create account screen instead
        WizardContext wizardContext = SessionContextStorage.create( request ).load();
        if ( wizardContext == null ) {
            return showCreateForm( request, model, user );
        }
        String username = wizardContext.getWizardResults().getUsername();
        OverseasUser existed = userService.findUserByName( username );
        if ( existed != null ) {
            model.addAttribute( "existed", true );
            userService.evict( existed );
        }

        model.addAttribute( "wizardContext", wizardContext );
        setContentBlock( "/WEB-INF/pages/blocks/CreatePassword.jsp" );
        setPageTitle( "Create Password" );
        request.getSession().setAttribute( CREATE_USER_FROM_RAVA_FORM, true );

        return buildModelAndView( request, model );
    }

    @RequestMapping(value = "CreateAccount.htm", method = RequestMethod.POST)
    public String postCreateForm( HttpServletRequest request,
                                  @ModelAttribute("user") OverseasUser user,
                                  BindingResult result, ModelMap model ) {
        if (!isUserCreatingByWizard( request, user )) {
            validateUser(user, result);
        }
        if ( result.hasErrors() ) {
            setContentBlock( "/WEB-INF/pages/blocks/CreateAccount.jsp" );
            setPageTitle( "Create Account" );
            if ( user != null && StringUtils.isNotEmpty( user.getEodRegionId() )) {
                model.addAttribute( "selectedRegion", eodApiService.getRegion( user.getEodRegionId() ) );
            }
            return buildModelAndView(request, model); // redirect to form
        }
        user.setRoles(userService.findRolesByName(UserRole.USER_ROLE_VOTER));
        EodRegion region = eodApiService.getRegion(user.getEodRegionId());
        if(region != null){
            user.setEodRegionName(region.getRegionName());
        }
        String password = user.getPassword();
        userService.makeNewUser(user);
        ExtendedProfile extendedProfile = userService.findExtendedProfile(user);
        if(extendedProfile == null){
            extendedProfile = new ExtendedProfile();
            extendedProfile.setUser( user );
        }
        userService.saveExtendedProfile(extendedProfile, user.getVoterType());
        
        try {
            sendEmailHelper.sendNewAccountThank( user, getFaceConfig( request ) );
        } catch (EmailException e) {
            logger.error("Create a New Account: Cannot send \"thank you\" email", e);
        }

        // TODO: don't always do automatic login
        boolean doLogin = true;

        // determine if we are supposed to login after creating the account
        if ( doLogin ) {

            // assign current WizardContext to new user
            WizardContext wizardContext = SessionContextStorage.create( request ).load( false );
            if ( wizardContext != null ) {
                wizardContext.getWizardResults().setUser( user );
            } else {
                wizardContext = new WizardContext();
            }

            UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(
                    user, password, user.getAuthorities() );
            SecurityContextHolder.getContext().setAuthentication( t );
            // new session gets started here
            SessionContextStorage.create( request, wizardContext ).save();
        }

        String redirect = getRedirect( request );
        if ( redirect == null || redirect.isEmpty() ) {
            redirect = doLogin ? "MyVotingInformation.htm" : "Login.htm";
        }

        return "redirect:" + redirect;
    }

    private String getRedirect( HttpServletRequest request ) {
        // see if we need to go back to the SurveyWizard controller
        String redirect = SurveyWizard.getContinueUrl( request );
        // override if there was a specific redirect in the request
        String reqRedirect = MapUtils.getString( request.getParameterMap(), "redirect", null );
        if ( reqRedirect != null ) {
            redirect = reqRedirect;
        }
        return redirect;
    }

    @Override
    @ModelAttribute("user")
    protected OverseasUser getUser() {
        OverseasUser user = super.getUser();
        if ( user == null ) {
            user = new OverseasUser();
        }
        return user;
    }

    /*
    *  If user is creating during Wizard process the object has to be populated from WizardContext
     */
    protected boolean isUserCreatingByWizard(HttpServletRequest request, OverseasUser user ) {
        Boolean isUserCreatedByWizard = (Boolean) request.getSession().getAttribute( CREATE_USER_FROM_RAVA_FORM );
        if ( isUserCreatedByWizard != null && isUserCreatedByWizard ) {
            WizardContext wizardContext = SessionContextStorage.create( request ).load();
            if ( wizardContext != null ) {
                user.populateFromWizardResults( wizardContext.getWizardResults() );
                /*
                 When Create Account submitted from Wizard we the field VoterHistory (VoterType) should be
                 populated manually since it's not required on the Wizard form, but is required on the Create Account form.
                 */
                if (wizardContext.getFlowType() == FlowType.DOMESTIC_REGISTRATION || wizardContext.getFlowType() == FlowType.DOMESTIC_ABSENTEE) {
                    user.setVoterType(VoterType.DOMESTIC_VOTER);
                    user.setVoterHistory(VoterHistory.DOMESTIC_VOTER);
                }
            }
            request.getSession().removeAttribute( CREATE_USER_FROM_RAVA_FORM );
            return true;
        }
        return false;
    }

    @InitBinder
    protected void initBinder( WebDataBinder binder ) {
        if ( binder.getTarget() instanceof OverseasUser ) {
            binder.registerCustomEditor( VotingRegion.class, votingRegionPropertyEditor );
            binder.setValidator( overseasUserValidator );
        }
    }

    protected void validateUser(OverseasUser user, BindingResult errors ) {
        List<String> skipFields = FormValidationRulesType.getSkipFields(FormValidationRulesType.VOTER_ACCOUNT_CREATE);
        overseasUserValidator.validate( user, errors, new HashSet<>(skipFields));
    }

    @RequestMapping(value = "/CreateAccount.htm", method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }

    @RequestMapping(value = "/CreatePassword.htm", method = {RequestMethod.HEAD, RequestMethod.POST} )
    public ResponseEntity<String> answerToPostHeadRequest() {
        return sendMethodNotAllowed();
    }

}
