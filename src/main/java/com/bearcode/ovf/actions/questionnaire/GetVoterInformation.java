package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.editor.VotingRegionPropertyEditor;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.mail.FaceMailingList;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.*;
import com.bearcode.ovf.tools.SendEmailHelper;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.validators.AnswerValidator;
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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/VoterInformation.htm")
public class GetVoterInformation extends BaseController implements AllowedForRedirect {

    @Autowired
    private VotingRegionPropertyEditor votingRegionPropertyEditor;

    @Autowired
    private OverseasUserValidator overseasUserValidator;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired(required = false)
    private VotingPrecinctService votingPrecinctService;

    @Autowired
    private OverseasUserService userService;

    @Autowired
    private SendEmailHelper sendEmailHelper;

    @Autowired
    private EodApiService eodApiService;

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private ZohoService zohoService;

    public GetVoterInformation() {
        setSectionName( "rava" );
        setSectionCss( "/css/rava.css" );
        setContentBlock( "/WEB-INF/pages/blocks/wizard/VoterInformation.jsp" );
        setPageTitle( "Voter Information" );
    }

    @InitBinder
    protected void initBinder( ServletRequestDataBinder binder ) {
        binder.registerCustomEditor( VotingRegion.class, votingRegionPropertyEditor );

        // Need to check the binder target type before assignment the validator
        final Object target = binder.getTarget();
        if ( target instanceof OverseasUser ) {
            binder.setValidator( overseasUserValidator );
        }
    }

    @ModelAttribute("wizardContext")
    public WizardContext getWizardContext( HttpServletRequest request ) {
        //get WizardContext from the session for current flow
        return SessionContextStorage.create( request ).load();
    }


    @ModelAttribute("deception")
    public String modelDeception( HttpServletRequest request, ModelMap model ) {
        final WizardContext modelContext = (WizardContext) model.get( "wizardContext" );
        final WizardContext form = modelContext == null ? getWizardContext( request ) : modelContext;
        // need to cheat model attribute 'user' in case of anonymous filling form.
        // for correct binding user should not be null
        // getUser() could not be overridden because we need in access to wizardContext
        if ( getUser() == null && form != null ) {
            if ( form.getPretenceOfUser() == null ) {
                // anonymous !
                OverseasUser user = new OverseasUser();
                user.populateFromWizardResults( form.getWizardResults() );
                form.setPretenceOfUser( user );
            }
            model.addAttribute( "user", form.getPretenceOfUser() );
        }
        return null;
    }

    @ModelAttribute("phoneNumberTypes")
    public String[] getPhoneNumberTypes() {
        return PhoneNumberType.getStringValues();
    }

    @ModelAttribute("faceMailingList")
    public FaceMailingList gerFaceMailingList( HttpServletRequest request ) {
        FaceConfig faceConfig = getFaceConfig( request );
        return getFacesService().findFaceMailingList( faceConfig );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String handleGetVoterInformation( HttpServletRequest request, ModelMap model,
                                             @RequestParam(value = "votingAddress.state", required = false) String votingAddressState,
                                             @RequestParam(value = "votingAddress.zip", required = false) String votingAddressZip,
                                             @ModelAttribute("wizardContext") WizardContext wizardContext) {
        if ( wizardContext == null ) {
            return "redirect:/home.htm";
        }
        if ( wizardContext.isFlowFinished() ) { //restart of the process
            wizardContext.setFlowFinished( false );
            model.addAttribute( "secondRunDetected", true );
        }
        final OverseasUser pretenceOfUser = wizardContext.getPretenceOfUser();
        if ( (votingAddressState != null) && !votingAddressState.trim().isEmpty() ) {
            wizardContext.getWizardResults().getVotingAddress().setState( votingAddressState );
            if ( pretenceOfUser != null ) {
                try {
                    UserAddress votingAddress = pretenceOfUser.getVotingAddress();
                    if ( votingAddress == null ) {
                        votingAddress = new UserAddress( AddressType.STREET );
                        pretenceOfUser.setVotingAddress( votingAddress );
                    }
                    votingAddress.setState( votingAddressState );
                } catch ( final IllegalArgumentException e ) {
                }
            }
        }
        if ( (votingAddressZip != null) && !votingAddressZip.trim().isEmpty() ) {
            wizardContext.getWizardResults().getVotingAddress().setZip( votingAddressZip );
            if ( pretenceOfUser != null ) {
                try {
                    UserAddress votingAddress = pretenceOfUser.getVotingAddress();
                    if ( votingAddress == null ) {
                        votingAddress = new UserAddress( AddressType.STREET );
                        pretenceOfUser.setVotingAddress( votingAddress );
                    }
                    votingAddress.setZip( votingAddressZip );
                } catch ( final IllegalArgumentException e ) {
                }
            }
        }
        if ( pretenceOfUser != null ) {
            if ( pretenceOfUser.getVotingAddress() != null && StringUtils.isNotEmpty( pretenceOfUser.getVotingAddress().getState() )) {
                model.addAttribute( "regions", eodApiService.getRegionsOfState( pretenceOfUser.getVotingAddress().getState() ) );
            }
        }
        String eodRegionId = wizardContext.getWizardResults().getEodRegionId();
        if ( StringUtils.isEmpty( eodRegionId ) && pretenceOfUser != null ) {
            eodRegionId = pretenceOfUser.getEodRegionId();
        }
        if ( StringUtils.isNotEmpty( eodRegionId ) ) {
            model.addAttribute( "selectedRegion", eodApiService.getRegion( eodRegionId ) );
        }

        adduserValidationFieldsToSkip( request, model );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handlePostVoterInformation( HttpServletRequest request,
                                              ModelMap model,
                                              @RequestParam(value = "noSavedUsers", required = false) Boolean noSavedUsers,
                                              @ModelAttribute("user") OverseasUser user,
                                              BindingResult errors,
                                              @ModelAttribute("wizardContext") WizardContext wizardContext ) {

        if ( wizardContext == null ) {
            return "redirect:/errors.htm?errorCode=503";
        }
        List<String> skipFields = FormValidationRulesType.getSkipFieldsForWizardFlow(wizardContext.getFlowType());
        logger.debug("skipFields: [{}]", skipFields);
        errors = validateUserFields( user, errors, new HashSet<>(skipFields));
        final WizardResults wizardResults = wizardContext.getWizardResults();
        if ( !errors.hasErrors() ) {
            wizardResults.populateFromUser( user ); // copy result to WizardContext

            if ( StringUtils.isNotEmpty( user.getEodRegionId() ) ) {
                EodRegion region = eodApiService.getRegion( user.getEodRegionId() );
                if ( region != null ) {
                    wizardResults.setVotingRegionName( region.getName() );
                }
            }
            wizardResults.setVotingRegionState( user.getVotingAddress().getState() );

            wizardContext.processSaveResults( questionnaireService );

            //mailing opt-in
            FaceConfig face = getFaceConfig( request );
            validateAnswers(request, wizardContext);
            boolean optIn = MapUtils.getBoolean( request.getParameterMap(), "optIn", false );
            if ( optIn ) {
                FaceMailingList faceMailingList = getFacesService().findFaceMailingList( face );
                if ( faceMailingList != null ) {
                    mailingListService.saveToMailingList( wizardResults, faceMailingList.getMailingList() );
                }
                if(user.getId() > 0){
                    zohoService.voterAlertOptInToZoho(wizardResults.getUsername());
                } else {
                    wizardResults.setOptIn(true);
                }
            }
            if ( getUser() == null && (noSavedUsers == null || !noSavedUsers) ) {
                if ( face.isAutoCreateAccount() ) {
                    // special case. some faces could want auto create account
                    autoCreateNewUser( wizardContext, face );
                    // new session gets started here
                    SessionContextStorage.create( request, wizardContext ).save();
                } else if (face.getRelativePrefix().contains("skimm") || face.getRelativePrefix().contains("vote411")){
                    // for skimm flow
                    // return "redirect:/CreateAccount.htm";
                } else {
                    // special case: if there is not a logged in user,
                    // give them the opportunity to choose
                    // a password and create an account
                    return "redirect:/CreatePassword.htm";
                }
            }
            // OK, going back
            String redirect = SurveyWizard.getContinueUrl( request );
            return "redirect:" + redirect;
        }
        return handleGetVoterInformation( request, model, null, null, wizardContext );
    }

    protected void validateAnswers( final HttpServletRequest request, final WizardContext formObject){

        final long[] fieldIds = MapUtils.getLongs( request.getParameterMap(), "fields", new long[]{} );
        final AnswerValidator validator = new AnswerValidator();

        for ( final long fieldId : fieldIds ) {
            try {
                Answer answer = formObject.getAnswerByFieldId( fieldId );
                    final QuestionField field = questionFieldService.findQuestionFieldById( fieldId );

                    // we should to extract a "fresh" field for prevent "Lazy"
                    // exception from throwing while going forward and back on
                    // pages
                    if ( answer == null ) {
                        if ( field != null ) {
                            answer = field.createAnswer();
                        } else {
                            continue; // field was not found - mailformed URL
                        }
                    } else {
                        answer.setField( field );
                    }
                answer.parseValue( request.getParameterMap() );
                formObject.putAnswer( answer );
            } catch ( final Exception e ) {
                // it could be ObjectNotFound exception - url passed
                // inconsistent field ID.
                logger.error( "Answer couldn't be parsed", e );
            }
        }
    }

    private void autoCreateNewUser( WizardContext form, FaceConfig face ) {
        // check this user against the database
        OverseasUser user = userService.findUserByName( form.getWizardResults().getUsername() );
        // if user already exists password will be reset and create new account message will be sent anyway
        if ( user == null ) {
            user = new OverseasUser();
        }
        user.populateFromWizardResults( form.getWizardResults() );

        user.setRoles(userService.findRolesByName(UserRole.USER_ROLE_VOTER));

        String password = OverseasUser.generatePassword();
        user.setPassword( password );
        userService.makeNewUser(user);

        try {
            sendEmailHelper.sendNewAccountThank( user, face );
        } catch (EmailException e) {
            logger.error("Create a New Account: Cannot send \"thank you\" email", e);
        }

        // assign current WizardContext to new user
        form.getWizardResults().setUser( user );

        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(
                user, password, user.getAuthorities() );
        SecurityContextHolder.getContext().setAuthentication( t );
    }

    protected BindingResult validateUserFields( OverseasUser user, BindingResult errors, Set<String> skip ) {

        if ( skip == null ) {
            skip = new HashSet<String>( 1 );
        }
        // we don't need to validate password because we are not updating the
        // user, just the WizardContext
        skip.add( "password" );
        skip.add( "gender" );

        overseasUserValidator.validateUser( user, errors, skip );
        return errors;
    }


    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }

}
