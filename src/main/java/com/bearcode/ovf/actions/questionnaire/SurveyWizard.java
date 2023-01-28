package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.QuestionnaireArbiter;
import com.bearcode.ovf.validators.AnswerValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/w")
public class SurveyWizard extends BaseController implements ApplicationContextAware {

    private static final String CANCEL_REDIRECT_URL_LOGGED_IN = "/Login.htm?msg=ravaSaved";
    private static final String CANCEL_REDIRECT_URL_NON_LOGGED_IN = "/home.htm?msg=ravaQuit";
    public static final String BUILD_URL_TEMPLATE = "/w/%s/%d.htm";

    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private QuestionnaireArbiter arbiter;

    @Autowired
    private MailingListService mailingListService;

    private ApplicationContext applicationContext;

    public SurveyWizard() {
        setSectionName( "rava" );
        setSectionCss( "/css/rava.css" );
        setContentBlock( "/WEB-INF/pages/blocks/wizard/WizardPage.jsp" );
    }

    @Override
    public void setApplicationContext( final ApplicationContext applicationContext ) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @InitBinder
    protected void initBinder( final ServletRequestDataBinder binder ) {

        // Need to check the binder target type before assignment the validator
        final Object target = binder.getTarget();
        if ( target instanceof Answer ) {
            binder.setValidator( new AnswerValidator() );
        }
    }


    // common entry point
    @RequestMapping(value = "/{flow}.htm", method = RequestMethod.GET)
    public String handleFlowStartPage( final HttpServletRequest request,
                                       @PathVariable final String flow,
                                       @RequestParam(value = "voterType", required = false) final String voterTypeStr,
                                       @RequestParam(value = "voterClassificationType", required = false) final String voterClassificationTypeStr,
                                       @RequestParam(value = "vrState", required = false) final String votingRegionState,
                                       @RequestParam(value = "vrName", required = false) final String votingRegionName,
                                       @ModelAttribute("wizardContext") final WizardContext wizardContext ) {
        if ( wizardContext == null ) {
            return "redirect:/errors.htm?errorCode=404";
        }

        // switch current flow name stored in the session to the given flow
        SessionContextStorage.create( request ).activate( getFlowType( flow ) );

        final WizardResults wizardResults = wizardContext.getWizardResults();
        final OverseasUser pretenceOfUser = wizardContext.getPretenceOfUser();

        /*
           * TODO allow passing arbitrary wizard session parameters instead of having to code for each case
           */
        if ( voterTypeStr != null ) {
            wizardResults.setVoterType( voterTypeStr );
            try {
                VoterType voterType = VoterType.valueOf( voterTypeStr );
                if ( pretenceOfUser != null ) {
                    pretenceOfUser.setVoterType( voterType );
                }
            } catch ( final IllegalArgumentException e ) {
                // incorrect voter type. forget it.
                wizardResults.setVoterType( "" );
            }
        }

        if (StringUtils.isNotEmpty(voterClassificationTypeStr)) {
            wizardResults.setVoterClassificationType(voterClassificationTypeStr);
            try {
                VoterClassificationType voterClassificationType = VoterClassificationType.valueOf( voterClassificationTypeStr);
                if ( pretenceOfUser != null ) {
                    pretenceOfUser.setVoterClassificationType( voterClassificationType );
                }
            } catch ( final IllegalArgumentException e ) {
                // incorrect voter type. forget it.
                wizardResults.setVoterClassificationType( "" );
            }
        }

        if ( votingRegionState != null && !votingRegionState.trim().isEmpty() ) {
            final State votingState = getStateService().findByAbbreviation( votingRegionState );
            if ( votingState != null ) {
                final WizardResultAddress votingAddress = wizardResults.getVotingAddress();
                final String abbr = votingState.getAbbr();
                if ( !abbr.equalsIgnoreCase( votingAddress.getState() ) ) {
                    votingAddress.setState( abbr );
                    votingAddress.setStreet1( null );
                    votingAddress.setStreet2( null );
                    votingAddress.setCounty( null );
                    votingAddress.setCity( null );
                    votingAddress.setZip( null );
                    wizardResults.setVotingRegion( null );
                }
/*
                final VotingRegion votingRegion = (votingRegionName == null || votingRegionName.trim().isEmpty()) ? null
                        : getStateService().findRegion( votingState, votingRegionName );
                if ( votingRegion != null ) {
                    final VotingRegion wizardVotingRegion = wizardResults.getVotingRegion();
                    if ( wizardVotingRegion == null ) {
                        wizardResults.setVotingRegion( votingRegion );
                    } else if ( !votingRegion.valueEquals( wizardVotingRegion ) ) {
                        wizardResults.setVotingRegion( votingRegion );
                        votingAddress.setState( abbr );
                        votingAddress.setStreet1( null );
                        votingAddress.setStreet2( null );
                        votingAddress.setCounty( null );
                        votingAddress.setCity( null );
                        votingAddress.setZip( null );
                    }
                }
                if ( pretenceOfUser != null ) {
                    try {
                        pretenceOfUser.getVotingAddress().setState( abbr );
                        if ( votingRegion != null ) {
                            pretenceOfUser.setVotingRegion( votingRegion );
                        }
                    } catch ( final IllegalArgumentException e ) {
                        // Incorrect state identifier, forget it.
                    }
                }
*/
            }
        }

        final String redirect = wizardContext.createFlowPageUrl( flow, 1 );
        FaceConfig faceConfig = getFaceConfig( request );
        if ( getUser() == null && faceConfig.isLoginAllowed() ) {
            return "redirect:/RavaLogin.htm?redirect=" + redirect;
        }

        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/{flow}/{page}.htm", method = RequestMethod.GET)
    public String handleFlowPageGetMethod( final HttpServletRequest request,
                                           @PathVariable final String flow,
                                           @PathVariable final int page,
                                           @RequestParam(value = "vrState", required = false) final String votingRegionState,
                                           @RequestParam(value = "vrName", required = false) final String votingRegionName,
                                           @ModelAttribute("wizardContext") final WizardContext wizardContext,
                                           @ModelAttribute("user") final OverseasUser user,
                                           final ModelMap model ) throws Exception {

        if ( wizardContext == null ) {
            return "redirect:/errors.htm?errorCode=503";
        }
        if ( (votingRegionState != null) && !votingRegionState.trim().isEmpty() ) {
            final State votingState = getStateService().findByAbbreviation( votingRegionState );
            final WizardResults wizardResults = wizardContext.getWizardResults();
            wizardResults.getVotingAddress().setState( votingState.getAbbr() );
            final VotingRegion votingRegion = (votingRegionName == null || votingRegionName.trim().isEmpty()) ? null
                    : getStateService().findRegion( votingState, votingRegionName );
            if ( votingRegion != null ) {
                wizardResults.setVotingRegion( votingRegion );
            }
            final OverseasUser pretenceOfUser = wizardContext.getPretenceOfUser();
            if ( pretenceOfUser != null ) {
                try {
                    pretenceOfUser.getVotingAddress().setState( votingState.getAbbr() );
                    if ( votingRegion != null ) {
                        pretenceOfUser.setVotingRegion( votingRegion );
                    }
                } catch ( final IllegalArgumentException e ) {
                    // Incorrect state identifier, forget it.
                }
            }
        }

        boolean goForward = true;
        if ( request.getParameter( "back" ) != null ) {
            goForward = false;
        }

        final int currentPage = preparePage( wizardContext, page, goForward );
        // complete check - if wizard reaches last page that contains no questions
        if ( wizardContext.getCurrentPage().isEmpty() && wizardContext.isFormCompleted() ) {
            // NB: at this point check for empty page is sufficient.
            // it's impossible to be here with empty page and not be at the end of wizard.
            return doFinish( request, wizardContext );
        }
        if ( page != currentPage ) {
            // if we skip some pages we want this will be reflected in the url
            return "redirect:" + buildUrl( wizardContext, currentPage );
        }
        return showPage( request, model, wizardContext );
    }

    @RequestMapping(value = "/{flow}/{page}.htm", method = RequestMethod.POST)
    public String handleFlowPagePostMethod( final HttpServletRequest request,
                                            @PathVariable final String flow,
                                            @PathVariable final int page,
                                            @ModelAttribute("wizardContext") final WizardContext wizardContext,
                                            BindingResult errors,
                                            final ModelMap model ) throws Exception {

        if ( wizardContext == null ) {
            return "redirect:/errors.htm?errorCode=503";
        }

        int currentPage = 0;
        if ( wizardContext.getCurrentPage() != null ) {
            currentPage = wizardContext.getCurrentPage().getNumber();
        }

        // Only accept POSTs to the current page
        if ( page != currentPage ) {
            return "redirect:" + buildUrl( wizardContext, page );
        }

        // do validation and population of form object
        errors = validateAnswers( request, wizardContext, errors );
        if ( errors != null && errors.hasErrors() ) {
            // there was a validation error, redisplay the page
            return showPage( request, model, wizardContext );
        }
        // save here to keep every step of form filling (#3700)
        saveWizardContext( wizardContext );

        //do immediate save to mailing list RM:5311
        doImmediateMailingList( request, wizardContext );

        if ( request.getParameter( "finish" ) != null ) {
            return doFinish( request, wizardContext );
        }

        final String redirect = buildUrl( wizardContext, page + 1 ); // next page
        return "redirect:" + redirect;
    }

    protected String showPage( final HttpServletRequest request, final ModelMap model, final WizardContext wizardContext )
            throws Exception {

        final QuestionnairePage pageObject = wizardContext.getCurrentPage();

        // handle redirect page.
        if ( pageObject instanceof ExternalPage ) {
            setContinuePage( request, pageObject.getNumber() + 1 );
            final String externalLink = ((ExternalPage) pageObject).getExternalLink();
            return "redirect:" + externalLink;
        }

        final int page = pageObject != null ? pageObject.getNumber() : 0;
        if ( page == 0 ) {
            // flag used to specify what HTML form to display - user or wizard
            model.put( "firstPage", true );
        }

        setPageTitle( pageObject != null ? pageObject.getTitle() : "Wizard" );
        if ( wizardContext.isFormCompleted() ) {
            model.put( "lastPage", true );
        }
        model.put( "formUrl", buildUrl( wizardContext, page ) );
        if ( page > 0 ) {
            model.put( "backUrl", buildUrl( wizardContext, page - 1 ) + "?back=1" );
            model.put( "cancelUrl", buildUrl( wizardContext, page ) + "?cancel=1" );
        }

        return buildModelAndView( request, model );
    }

    /**
     * Overrides default behavior: will initialize a new form if one does not already exist in the session
     *
     * @param request Http Request
     * @param flow    Flow type
     * @return Form object
     */
    @ModelAttribute("wizardContext")
    public WizardContext getWizardContext( final HttpServletRequest request, @PathVariable final String flow ) {
        final FlowType flowType = getFlowType( flow );
        if( flowType == null ) return null;
        WizardContext wizardContext = SessionContextStorage.create( request ).load( flowType, true );
        if ( wizardContext == null ) {
            if ( !request.getMethod().equalsIgnoreCase( "post" ) ) {
                wizardContext = createWizard( request, flowType );
                wizardContext.setWizardUrlTemplate( BUILD_URL_TEMPLATE );
            }
        }
        return wizardContext;
    }

    protected WizardContext createWizard( final HttpServletRequest request, final FlowType flowType ) {
        // create PdfAnswers instance
        final WizardResults wizardResults = new WizardResults( flowType );
        final WizardContext wizardContext = new WizardContext( wizardResults );

        // set face and populate any presets
        final FaceConfig config = getFaceConfig( request );
        wizardContext.setCurrentFace( config );
        wizardResults.populateFromFaceConfig( config );

        // set user and populate any presets
        wizardResults.setUser( getUser() );
        // wizardResults.populateFromUser( getUser() ); incorporated into 'setUser' method

        // set page count
        final int cnt = questionnaireService.countPages( flowType.getPageType() );
        wizardContext.setPageCount( cnt );

        // save to database
        wizardContext.processSaveResults( questionnaireService );

        // save to session
        SessionContextStorage.create( request, wizardContext ).save();

        return wizardContext;
    }

    protected FlowType getFlowType( final String flow ) {
        try {
            return FlowType.valueOf( flow.toUpperCase() );
        } catch ( final Exception e ) {
            return null;
        }
    }

    public static String buildUrl( final WizardContext form, final int... pages ) {
        if ( form == null || form.getFlowType() == null ) {
            // When there is form or no flow type, we aren't actually running the wizard. Return null.
            return null;
        }

        assert pages.length == 0 || pages.length == 1 : "pages";

        int page = 0;
        if ( pages.length == 1 ) {
            page = pages[0];
        } else if ( form != null && form.getCurrentPage() != null ) {
            page = form.getCurrentPage().getNumber();
        }

        return form.createFlowPageUrl( form.getFlowType().toString(), page );
    }

    private int preparePage( final WizardContext wizardContext, int page, final boolean goForward ) {
        if ( page < 1 ) {
            page = 1;
        }
        if ( page > wizardContext.getPageCount() ) {
            page = wizardContext.getPageCount();
        }

        QuestionnairePage currentPage = wizardContext.getCurrentPage();
        Integer currentPageNumber = null;
        if ( currentPage != null ) {
            // We cannot skip ahead from the current page
            currentPageNumber = currentPage.getNumber();
            if ( page > currentPageNumber ) {
                page = currentPageNumber + 1;
            }
        } else {
            page = 1;
        }

        do {
            currentPage = questionnaireService.findPageByNumber( page, wizardContext.getFlowType().getPageType() );
            if ( currentPage == null ) {  // current page should not be null. if so there is abuse of wizard. drive them to the start.
                page = 1;
                currentPage = questionnaireService.findPageByNumber( page, wizardContext.getFlowType().getPageType() );
            }
            if ( currentPageNumber == null ) {
                currentPageNumber = currentPage.getNumber();
            }

            // Special cases for additional pages
            if ( currentPage instanceof AddOnPage ) {
                // let Add-On fill the page with questions by its own way
                final AllowedForAddOn addOnBean = applicationContext.getBean( ((AddOnPage) currentPage).getBeanName(),
                        AllowedForAddOn.class );
                if ( addOnBean != null ) {
                    addOnBean.prepareAddOnPage( wizardContext, currentPage );
                }
            }

            if ( currentPage instanceof ExternalPage ) {
                // there is no need to check and prepare this page
                // return from here, redirect will be formed somewhere else
                wizardContext.setCurrentPage( currentPage );
                return currentPage.getNumber();
            }

            arbiter.adjustPage( currentPage, wizardContext ); // clear questions
            arbiter.checkAnswer( currentPage, wizardContext.getAnswersAsMap() ); // clear answer

            wizardContext.setCurrentPage( currentPage );
            page += (goForward) ? 1 : -1;

        } while ( currentPage.isEmpty() && page <= wizardContext.getPageCount() );

        arbiter.applyFieldDependencies( currentPage, questionFieldService.findFieldDependencies(), wizardContext.getAnswersAsMap() );
        return currentPage.getNumber();
    }

    /**
     * Persists form info and redirects client to configured URLs. If there is no logged-in user, form is anonymized before
     * persisting.
     *
     * @param request Servlet request
     * @param form    Wizard command form object
     * @return View name with redirect instruction
     * @throws Exception in case of data inconsistent
     */
    @RequestMapping(value = "/{page}.htm", method = {RequestMethod.GET, RequestMethod.POST}, params = "cancel")
    public String doCancel( final HttpServletRequest request, @ModelAttribute("wizardContext") final WizardContext form )
            throws Exception {

        String redirectUrl = CANCEL_REDIRECT_URL_LOGGED_IN;
        if ( saveWizardContext( form ) == null ) {
            redirectUrl = CANCEL_REDIRECT_URL_NON_LOGGED_IN;
        }

        SessionContextStorage.create( request, form ).delete();

        return "redirect:" + redirectUrl;
    }

    protected String doFinish( final HttpServletRequest request, final WizardContext wizardContext ) throws Exception {

        // OverseasUser user = saveWizardContext( wizardContext );

        // save the wizardContext in the session so the controllers can access it.
        SessionContextStorage.create( request, wizardContext ).save();

        // add to mailing list if appropriate
        // 03/01/2016 - we are doing immediate saving to mailing list
        //mailingListService.saveToMailingListIfHasSignup( wizardContext.getWizardResults() );

        // initial decision on where to go after processing
        String redirect = "/home.htm";
        if ( wizardContext.isFormCompleted() ) {
            redirect = "/Download.htm?flow=" + wizardContext.getFlowType().name();
        }
        return "redirect:" + redirect;
    }

    protected BindingResult validateAnswers( final HttpServletRequest request, final WizardContext formObject,
                                             final BindingResult binder ) throws Exception {

        final long[] fieldIds = MapUtils.getLongs( request.getParameterMap(), "fields", new long[]{} );
        final AnswerValidator validator = new AnswerValidator();

        final boolean withinAddOn = formObject.getFlowType() == FlowType.FWAB && isAddOnFields( fieldIds );
        for ( final long fieldId : fieldIds ) {
            try {
                Answer answer = formObject.getAnswerByFieldId( fieldId );
                if ( !withinAddOn ) {
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
                }
                answer.parseValue( request.getParameterMap() );
                formObject.putAnswer( answer );

                validator.validateAnswer( answer, binder );
            } catch ( final Exception e ) {
                // it could be ObjectNotFound exception - url passed
                // inconsistent field ID.
                logger.error( "Answer couldn't be parsed", e );
            }
        }
        return binder;
    }

    private boolean isAddOnFields( final long[] fieldIds ) {
        boolean is = false;
        for ( final long fieldId : fieldIds ) {
            if ( fieldId >= AllowedForAddOn.firstFieldId ) {
                is = true;
                break;
            }
        }
        return is;
    }

    /**
     * Checks that the session user matches the form user and throws an exception if they do not. Persists the form.
     *
     * @param wizardContext Wizard Context
     * @return OverseasUser if there is one, null otherwise
     * @throws Exception if the session user and form user do not match
     */
    protected OverseasUser saveWizardContext( final WizardContext wizardContext ) throws Exception {

        final OverseasUser user = wizardContext.getWizardResults().getUser();
        final OverseasUser sessionUser = getUser();
        if ( user != sessionUser ) {
            final String msg = "Session user " + sessionUser + " and form user " + user + " do not match";
            logger.error( msg );
            throw new IllegalArgumentException( msg );
        }
        wizardContext.processSaveResults( questionnaireService );
        return user;
    }

    protected static void setContinuePage( final HttpServletRequest request, final int page ) {
        request.getSession().setAttribute( SurveyWizard.class.getName() + ".startPage", String.valueOf( page ) );
    }

    protected static int getContinuePage( final HttpServletRequest request ) {
        try {
            final String pageStr = (String) request.getSession().getAttribute( SurveyWizard.class.getName() + ".startPage" );
            if ( pageStr == null ) {
                return -1;
            }

            return Integer.parseInt( pageStr );
        } catch ( final NumberFormatException e ) {
            return -1;
        }
    }

    /**
     * Returns a url to the last page that was set by setContinuePage(). Resets session so that after this method is called,
     * subsequent calls to it will return null until setContinuePage() is set again.
     *
     * @param request
     * @return String
     */
    public static String getContinueUrl( final HttpServletRequest request ) {
        String url;
        final WizardContext ctx = SessionContextStorage.create( request ).load();
        if ( getContinuePage( request ) >= 0 ) {
            url = SurveyWizard.buildUrl( ctx, getContinuePage( request ) );
        } else {
            url = SurveyWizard.buildUrl( ctx, 0 );
        }
        setContinuePage( request, -1 );
        return url;
    }


    @RequestMapping(value = "/{flow}/{page}.htm", method = RequestMethod.HEAD)
    public ResponseEntity<String> dummyHeadRequest() {
        return sendMethodNotAllowed();
    }

    private void doImmediateMailingList( final HttpServletRequest request, final WizardContext wizardContext ) {
        final long[] fieldIds = MapUtils.getLongs( request.getParameterMap(), "fields", new long[]{} );
        final boolean withinAddOn = wizardContext.getFlowType() == FlowType.FWAB && isAddOnFields( fieldIds );
        for ( final long fieldId : fieldIds ) {
                Answer answer = wizardContext.getAnswerByFieldId( fieldId );
                if ( answer != null && !withinAddOn ) {
                    if ( answer.getField().getType().isMailingListSignUp() &&
                            answer.getValue().equalsIgnoreCase( "true" ) ) {
                        mailingListService.saveToMailingList( wizardContext.getWizardResults(), answer.getField().getType() );
                    }
                }
        }


    }
}
