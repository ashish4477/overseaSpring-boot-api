package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.VotingRegionPropertyEditor;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.eod.ElectionView;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.service.ZohoService;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import com.bearcode.ovf.webservices.votesmart.model.CandidateBio;
import com.bearcode.ovf.webservices.votesmart.model.CandidateZip;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This controller handles user account management URLs
 * <p/>
 * /CreateAccount.htm
 * /UpdateAccount.htm
 * /ChangePassword.htm
 */
@Controller
public class UserAccount extends BaseController {

    @Autowired
    private OverseasUserService userService;

    @Autowired
    private OverseasUserValidator overseasUserValidator;

    @Autowired
    private VotingRegionPropertyEditor votingRegionPropertyEditor;

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private EodApiService eodApiService;

    @Autowired
    private LocalElectionsService localElectionsService;

    @Autowired
    private VoteSmartService voteSmartService;

    @Autowired
    private ZohoService zohoService;

    public UserAccount() {
        setSectionCss( "/css/rava.css" );
        setSectionName( "mva rava" );    /* added rava for legacy styles*/
        setPageTitle( "My Voter Account" );
    }

    @ModelAttribute("phoneNumberTypes")
    public String[] getPhoneNumberTypes() {
        return PhoneNumberType.getStringValues();
    }

    @ModelAttribute("extendedProfile")
    public ExtendedProfile getExtendedProfile() {
        ExtendedProfile extendedProfile = userService.findExtendedProfile( getUser() );
        return extendedProfile != null ? extendedProfile : new ExtendedProfile();
    }

    @RequestMapping(value ="/MyVotingInformation.htm", method = RequestMethod.GET)
    public String showVotingAddressAndInformation(HttpServletRequest request,
                                                  ModelMap model,
                                                  @ModelAttribute("user") OverseasUser user){
        setContentBlock( "/WEB-INF/pages/blocks/ViewVotingInformation.jsp" );
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 1);
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value ="/MyDemocracyProfile.htm", method = RequestMethod.GET)
    public String showDemocracyProfile(HttpServletRequest request,
                                       ModelMap model,
                                       @ModelAttribute("user") OverseasUser user){
        setContentBlock( "/WEB-INF/pages/blocks/ViewDemocracyProfile.jsp" );
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 2);
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value ="/ManageSubscription.htm", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String showManageSubscription(HttpServletRequest request,
                                         ModelMap model,
                                         @ModelAttribute("user") OverseasUser user){
        setContentBlock( "/WEB-INF/pages/blocks/ViewManageSubscription.jsp" );
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 3);
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value ="/ManageSubscription.htm", method = RequestMethod.POST)
    public String saveManageSubscription(HttpServletRequest request,
                                         ModelMap model,
                                         @ModelAttribute("user") OverseasUser user){
        OverseasUser existingUser = userService.findUserById(user.getId());
        if(existingUser.isEmailOptOut()!=user.isEmailOptOut()){
            existingUser.setEmailOptOut(user.isEmailOptOut());
            userService.saveUser( existingUser );
            zohoService.updateContactToZoho( existingUser );
        } else {
            model.addAttribute("showLeftMenu", true);
            return buildModelAndView( request, model );
        }
        return "redirect:MyVotingInformation.htm";
    }

    @RequestMapping(value ="/UpcomingElections.htm", method = RequestMethod.GET)
    public String showUpcomingElections(HttpServletRequest request,
                                        ModelMap model,
                                        @ModelAttribute("user") OverseasUser user){
        String regionId = user.getEodRegionId();
        String stateName = user.getVotingAddress() != null ? user.getVotingAddress().getState() : null;

        if ( StringUtils.isNotEmpty( regionId ) ) {
            final LocalOffice leo = eodApiService.getLocalOffice( regionId, true );
            if ( leo != null ) {
                final State state = getStateService().findByAbbreviation( stateName!=null ? stateName : leo.getEodRegion().getStateAbbr() );

                model.addAttribute( "leo", leo );
                model.addAttribute( "selectedRegion", leo.getEodRegion() );
                model.addAttribute( "selectedState", state );

//                final StateSpecificDirectory svid = getLocalOfficialService().findSvidForState( state );
//                model.addAttribute( "svid", svid );
                StateVoterInformation stateVoterInformation = localElectionsService.findStateVoterInformation( stateName );
                model.addAttribute( "stateVoterInformation", stateVoterInformation );
                if ( stateVoterInformation != null ) {
                    model.addAttribute( "identificationRequirements", LocalElectionsService.getGroupedIdentificationRequirements(stateVoterInformation) );
                    model.addAttribute( "eligibilityRequirements", LocalElectionsService.getGroupedEligibilityRequirements(stateVoterInformation) );
                }
                List<ElectionView> localElections = localElectionsService.findElectionsOfState( stateName );
                model.addAttribute( "localElections", localElections );
            }
        }
        setContentBlock( "/WEB-INF/pages/blocks/ViewUpcomingElection.jsp" );
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 4);
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value ="/QuickTakeToVoting.htm", method = RequestMethod.GET)
    public String showQuickTakeToVoting(HttpServletRequest request,
                                        ModelMap model,
                                        @ModelAttribute("user") OverseasUser user){
        return "redirect:/MyDates.htm";
    }

    @RequestMapping(value ="/AmIRegistered.htm", method = RequestMethod.GET)
    public String showAmIRegisterPage(HttpServletRequest request,
                                      ModelMap model,
                                      @ModelAttribute("user") OverseasUser user){
        setContentBlock( "/WEB-INF/pages/blocks/ViewAmIRegister.jsp" );
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 5);
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value ="/VoterRegisterBallotRequest.htm", method = RequestMethod.GET)
    public String showVoterRegistrationBallotRequestPage(HttpServletRequest request,
                                                         ModelMap model,
                                                         @ModelAttribute("user") OverseasUser user){
        setContentBlock( "/WEB-INF/pages/blocks/ViewVoterRegisterBallotRequest.jsp" );
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 6);
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value ="/MyStateVotingTools.htm", method = RequestMethod.GET)
    public String showMyStateVotingToolsPage(HttpServletRequest request,
                                             ModelMap model,
                                             @ModelAttribute("user") OverseasUser user){
        setContentBlock( "/WEB-INF/pages/blocks/ViewMyStateVotingTools.jsp" );
        model.addAttribute("showLeftMenu", true);
        if ( user != null ) {
            if ( user.getVotingAddress() != null && StringUtils.isNotEmpty( user.getVotingAddress().getState() )) {
                //model.addAttribute( "leo", localOfficialService.findForRegion( user.getVotingRegion() ) );
                model.addAttribute( "svid", localElectionsService.findStateVoterInformation( user.getVotingAddress().getState() ) );
                model.addAttribute( "selectedState", getStateService().findByAbbreviation( user.getVotingAddress().getState() ) );
            }
            if ( StringUtils.isNotEmpty( user.getEodRegionId() )) {
                model.addAttribute( "leo", eodApiService.getLocalOffice( user.getEodRegionId(), true ) );
            }
            else if ( user.getVotingRegion() != null ) {
                // old method; keep for compatibility
                model.addAttribute( "leo", localOfficialService.findForRegion( user.getVotingRegion() ) );

            }
            model.addAttribute("menuId", 8);
            //user membership history
            model.addAttribute( "membershipStat", userService.findUserMembershipStat( user ) );
        }
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value ="/ElectionOfficeAndContacts.htm", method = RequestMethod.GET)
    public String showElectionOfficeAndContactsPage(HttpServletRequest request,
                                                    final ModelMap model,
                                                    @ModelAttribute("user") OverseasUser user){
        if ( user != null ) {
            if ( user.getVotingAddress() != null && StringUtils.isNotEmpty( user.getVotingAddress().getState() )) {
                //model.addAttribute( "leo", localOfficialService.findForRegion( user.getVotingRegion() ) );
                model.addAttribute( "svid", localElectionsService.findStateVoterInformation( user.getVotingAddress().getState() ) );
                model.addAttribute( "selectedState", getStateService().findByAbbreviation( user.getVotingAddress().getState() ) );
            }
            if ( StringUtils.isNotEmpty( user.getEodRegionId() )) {
                model.addAttribute( "leo", eodApiService.getLocalOffice( user.getEodRegionId(), true ) );
            }
            else if ( user.getVotingRegion() != null ) {
                // old method; keep for compatibility
                model.addAttribute( "leo", localOfficialService.findForRegion( user.getVotingRegion() ) );

            }

            //user membership history
            model.addAttribute( "membershipStat", userService.findUserMembershipStat( user ) );
        }
        setContentBlock( "/WEB-INF/pages/blocks/ViewElectionOfficeAnadContracts.jsp" );
        model.addAttribute("showLeftMenu", true);
        model.addAttribute("menuId", 9);
        String target = buildModelAndView( request, model );
        return target;
    }

    @RequestMapping(value = "/UpdateAccount.htm", method = RequestMethod.GET)
    public String showUpdateForm( HttpServletRequest request,
                                  ModelMap model,
                                  @ModelAttribute("user") OverseasUser user ) {

        if ( user != null && user.getVotingAddress() != null && StringUtils.isNotEmpty( user.getVotingAddress().getState() )) {
            model.addAttribute( "regions", eodApiService.getRegionsOfState( user.getVotingAddress().getState() ) );
        }
        if ( user != null ) {
            if ( StringUtils.isNotEmpty( user.getEodRegionId() ) && !user.getEodRegionId().equals( "0" ) ) {
                model.addAttribute( "selectedRegion", eodApiService.getRegion( user.getEodRegionId() ) );
            }
            else if ( user.getVotingRegion() != null ){
                // empty new EodRegion
                EodRegion suggestedRegion = eodApiService.findRegionByName( user.getVotingRegion().getState().getAbbr(), user.getVotingRegion().getName() );
                if ( suggestedRegion != null ) {
                    user.setEodRegionId( suggestedRegion.getId().toString() );
                    model.addAttribute( "selectedRegion", suggestedRegion );
                }
            }
        }

        setContentBlock( "/WEB-INF/pages/blocks/UpdateAccount.jsp" );
        model.addAttribute("showLeftMenu", false);
        adduserValidationFieldsToSkip( request, model );
        return buildModelAndView( request, model );
    }

    @RequestMapping(value = "/UpdateAccount.htm", method = RequestMethod.POST)
    public String postUpdateForm( HttpServletRequest request, ModelMap model, @ModelAttribute("user") OverseasUser user,
                                  BindingResult result ) {
        List<String> skipFields = FormValidationRulesType.getSkipFields(FormValidationRulesType.VOTER_ACCOUNT_UPDATE);
        overseasUserValidator.validate( user, result, new HashSet<>(skipFields) );
        if ( result.hasErrors() ) {
            // re-display form
            HashMap<String,String> toSkip = new HashMap<>();
            for (String field: skipFields){
                toSkip.put(field, field);
            }
            model.addAttribute("userValidationFieldsToSkip",toSkip);
            return buildModelAndView( request, model );
        }
        userService.updateUser( user );
        zohoService.updateContactToZoho( user );

        return "redirect:MyVotingInformation.htm";
    }

    @RequestMapping(value = "/ChangePassword.htm", method = RequestMethod.GET)
    public String showPasswordForm( HttpServletRequest request, ModelMap model ) {

        setContentBlock( "/WEB-INF/pages/blocks/ChangePassword.jsp" );
        return buildModelAndView( request, model );
    }

    @RequestMapping(value = "/ChangePassword.htm", method = RequestMethod.POST)
    public String postPasswordForm( HttpServletRequest request,
                                    ModelMap model,
                                    @ModelAttribute("user") OverseasUser user,
                                    BindingResult result ) {

        overseasUserValidator.validateUserFields( user, result, new HashSet<>(Arrays.asList("password", "confirmPassword")) );
        if ( result.hasErrors() ) {
            boolean hasPasswordErrors = false;
            for ( FieldError error : result.getFieldErrors() ) {
                if (error.getField().equalsIgnoreCase( "password" )
                        || error.getField().equalsIgnoreCase( "confirmPassword" ) ) {
                    model.addAttribute( "showErrorMsg", true );
                    hasPasswordErrors = true;
                }
            }
            if (hasPasswordErrors) {
                // re-display form
                return buildModelAndView(request, model);
            }
        }
        String password = user.getPassword();
        user.setPassword( OverseasUser.encrypt( password ) );
        user.setScytlPassword( OverseasUser.encryptScytl( password ) );

        userService.updateUser( user );
        return "redirect:MyVotingInformation.htm";
    }

    @InitBinder
    protected void initBinder( WebDataBinder binder ) {
        if ( binder.getTarget() instanceof OverseasUser ) {
            binder.registerCustomEditor( VotingRegion.class, votingRegionPropertyEditor );
            binder.setValidator( overseasUserValidator );
        }
    }

    @RequestMapping(value = "/MyProfile.htm", method = RequestMethod.GET)
    public String showProfile( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("extendedProfile") ExtendedProfile extendedProfile ) {
        setContentBlock( "/WEB-INF/pages/blocks/MyProfile.jsp" );
        if ( request.getParameter( "showErrorMsg" ) != null ) {
            model.addAttribute( "showErrorMsg", true );
        }
        if ( extendedProfile != null && extendedProfile.getUser() != null) {
            //user membership history
            model.addAttribute( "membershipStat", userService.findUserMembershipStat( extendedProfile.getUser() ) );
        }

        model.addAttribute("showLeftMenu", false);
        String target = buildModelAndView( request, model );
        model.addAttribute( SECTION_NAME, getSectionName() + " profile");
        return target;
    }

    @RequestMapping(value = "/MyProfile.htm", method = RequestMethod.POST)
    public String saveProfile( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("extendedProfile") ExtendedProfile extendedProfile) {
        VoterType updatedVoterType = VoterType.UNSPECIFIED;
        if (!Objects.isNull(extendedProfile) && !Objects.isNull(extendedProfile.getUser())) {
            updatedVoterType = extendedProfile.getUser().getVoterType();
        }
        extendedProfile.getUser().getVoterType();
        OverseasUser user = (OverseasUser) model.get("user");
        extendedProfile.setUser( user );
        userService.saveExtendedProfile( extendedProfile, updatedVoterType);
        return "redirect:MyDemocracyProfile.htm";

    }

    @RequestMapping(value = "/MyReps.htm", method = RequestMethod.GET)
    public String showReps( HttpServletRequest request,
                            ModelMap model,
                            @ModelAttribute("user") OverseasUser user ) {

        setContentBlock( "/WEB-INF/pages/blocks/MyReps.jsp" );
        if ( user != null ) {
            //user membership history
            model.addAttribute( "membershipStat", userService.findUserMembershipStat( user ) );
        }
        if ( user != null && user.getVotingAddress() != null && user.getVotingAddress().getZip() != null && !user.getVotingAddress().getZip().isEmpty() ) {
            List<CandidateZip> candidates = voteSmartService.getOfficialsByZip( user.getVotingAddress().getZip(), user.getVotingAddress().getZip4() );
            List<CandidateBio> congressional = new LinkedList<CandidateBio>();
            for ( CandidateZip candidate : candidates ) {
                if ( candidate.getElectionOfficeId().equals("5") || candidate.getElectionOfficeId().equals("6") ) {
                    CandidateBio bio = voteSmartService.getCandidateBio( candidate.getCandidateId() ); // US. Senate and US.House
                    if ( bio != null ) congressional.add( bio );
                }
            }
            model.addAttribute("officials", congressional);
            model.addAttribute( "votingState", getStateService().findStateByAbbreviationOrName( user.getVotingAddress().getState() ) );
        }
        String target = buildModelAndView( request, model );
        model.addAttribute( SECTION_NAME, getSectionName() + " my-reps");
        model.addAttribute("menuId", 10);
        model.addAttribute("showLeftMenu", true);
        return target;
    }

    @RequestMapping(value = {"/UpdateAccount.htm","/ChangePassword.htm","/MyProfile.htm","/MyKeyContacts.htm","/MyReps.htm", "/ManageSubscription.htm"}, method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }
}