package com.bearcode.ovf.actions.express;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.OverseasWizardController;
import com.bearcode.ovf.actions.express.forms.ExpressForm;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.express.CountryDescription;
import com.bearcode.ovf.model.express.FedexLabel;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.service.*;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.tools.AuthorizeNetService;
import com.bearcode.ovf.tools.FedexGetLabelService;
import com.bearcode.ovf.tools.QuestionnaireArbiter;
import com.bearcode.ovf.validators.ExpressFormValidator;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 11, 2008
 * Time: 5:37:17 PM
 *
 * @author Leonid Ginzburg
 */
public class ExpressYourVoteWizard extends OverseasWizardController {

    @Autowired
    private FedexService fedexService;
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionFieldService questionFieldService;
    @Autowired
    private OverseasUserService userService;
    @Autowired
    private LocalOfficialService localOfficialService;
    @Autowired
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
    @Autowired
    private FedexGetLabelService fedexGetLabelService;
    @Autowired
    private AuthorizeNetService authorizeNetService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private StateService stateService;
    @Autowired
    private QuestionnaireArbiter arbiter;
    @Autowired
    private EodApiService eodApiService;

    private String[] contentBlocks;
    private Date serviceStart;

    public void setAuthenticationProcessingFilter( UsernamePasswordAuthenticationFilter authenticationProcessingFilter ) {
        this.usernamePasswordAuthenticationFilter = authenticationProcessingFilter;
    }

    public void setEmailService( EmailService emailService ) {
        this.emailService = emailService;
    }

    public void setServiceStart( String strDate ) {
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
        try {
            serviceStart = format.parse( strDate );
        } catch ( ParseException e ) {
            // incorrect date - set to 2010-09-05
            Calendar calendar = Calendar.getInstance();
            calendar.set( 2010, 9, 1 );
            serviceStart = calendar.getTime();
        }
    }

    public void setFedexService( FedexService fedexService ) {
        this.fedexService = fedexService;
    }

    public void setQuestionnaireService( QuestionnaireService questionnaireService ) {
        this.questionnaireService = questionnaireService;
    }

    public void setQuestionFieldService( QuestionFieldService questionFieldService ) {
        this.questionFieldService = questionFieldService;
    }

    public void setUserService( OverseasUserService userService ) {
        this.userService = userService;
    }

    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    public void setFedexGetLabelService( FedexGetLabelService fedexGetLabelService ) {
        this.fedexGetLabelService = fedexGetLabelService;
    }

    public void setAuthorizeNetService( AuthorizeNetService authorizeNetService ) {
        this.authorizeNetService = authorizeNetService;
    }

    public void setContentBlocks( String[] contentBlocks ) {
        this.contentBlocks = contentBlocks;
    }

    public String getContentBlock( HttpServletRequest request, Object object, int page ) {
        if ( contentBlocks != null && contentBlocks.length > page ) {
            return contentBlocks[page];
        }
        return "";
    }


    protected int getPageCount( HttpServletRequest request, Object command ) {
        if ( contentBlocks != null && contentBlocks.length > 0 )
            return contentBlocks.length;
        return 0;
    }

    protected Object formBackingObject( HttpServletRequest request ) throws Exception {
        ExpressForm form = new ExpressForm();
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( user instanceof OverseasUser ) {
            populateFedexForm( form, (OverseasUser) user, true );
        }
        return form;
    }

    protected Map buildReferences( HttpServletRequest request, Object object, Errors errors, int page ) {
        Map<String, Object> ref = new HashMap<String, Object>();
        ExpressForm form = (ExpressForm) object;
        boolean serviceStarted = serviceStart.compareTo( new Date() ) < 0;

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( user instanceof OverseasUser ) {
            ref.put( "loggedUser", user );
        }
        switch ( page ) {
            case 0:
                if ( user instanceof OverseasUser ) {
                    ref.put( "labels", fedexService.findFedexLabelsForUser( (OverseasUser) user ) );
                }
                ref.put( "trackingNumber", MapUtils.getString( request.getParameterMap(), "trackingNumber", "" ) );
                ref.put( "serviceStarted", serviceStarted );
                ref.put( "fcountries", fedexService.findFedexCountries() );
                break;
            case 1:
                CountryDescription country = form.getCountry();
                ref.put( "country", country );
                ref.put( "serviceActive", isCountryActive( country ) );
                break;
            case 2:
                if ( form.getDestinationLeo() != null ) {
                    String stateName = form.getDestinationLeo().getMailing().getState();
                    ref.put( "regions", eodApiService.getRegionsOfState( stateName ) );
                }
                break;
            case 3:
                if ( form.getNameOnCard() == null || form.getNameOnCard().length() == 0 ) {
                    form.setNameOnCard( form.getFirstName() + " " + form.getLastName() );
                }
                FieldType countriestType = questionFieldService.findFieldTypeById( 8 );  // find countries field type
                ref.put( "countriesList", countriestType.getFixedOptions() );
        }
        return ref;
    }

    protected ModelAndView processFinish( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
        return null;
    }


    protected int getTargetPage( HttpServletRequest request, Object command, Errors errors, int currentPage ) {

        ExpressForm form = (ExpressForm) command;
        /**
         * For countries with free service, skip the CC card input page
         */
        if ( (currentPage == 2 || currentPage == 4) && getTargetPage( request, currentPage ) == 3 ) {

            if ( form.getCountry() != null && form.getCountry().getRate() == 0 ) {
                return currentPage == 2 ? 4 : 2;
            }
        }
        /**
         * For inactive countries, always display page 1
         *
         */
        if ( form.getCountry() != null && !isCountryActive( form.getCountry() ) ) {
            return 1;
        }

        return super.getTargetPage( request, command, errors, currentPage );
    }

    /**
     * Returns a boolean whether the given country is currenly active. Returns true if the country is active, false otherwise.
     *
     * @param country - A CountryDescription instance
     * @return boolean - true if the country is active, false otherwise
     */
    private boolean isCountryActive( CountryDescription country ) {
        Date now = new Date();
        return (country != null &&
                country.getLastDate() != null &&
                now.compareTo( country.getLastDate() ) < 0 &&
                now.compareTo( this.serviceStart ) > 0);
    }

    protected void onBindAndValidate( HttpServletRequest request, Object command, BindException errors, int page ) throws Exception {
        ExpressForm form = (ExpressForm) command;
        switch ( page ) {
            case 0:
                if ( form.getCountryId() != 0 && (form.getCountry() == null || form.getCountryId() != form.getCountry().getId()) ) {
                    form.setCountry( fedexService.findFedexCountry( form.getCountryId() ) );
                }
                break;
            case 1:
                break;
            case 2:
                Long regionId = MapUtils.getLong( request.getParameterMap(), "regionId", 0L );
                if ( regionId != 0 && (form.getDestinationLeo() == null || !regionId.equals( form.getDestinationLeo().getRegion().getId() )) ) {
                    LocalOffice leo = eodApiService.getLocalOffice( regionId.toString() );
                    form.setDestinationLeo( leo );
                }

                /**
                 * Fix up the postal code
                 *
                 **/
                // remove all spaces and dashes
                String fixedPostalCode = form.getPickUp().getZip().replaceAll( "[\\s\\-]+", "" );
                // Special case for NL: remove everything after 1st 4 digits
                if ( form.getCountry().getCountryCode().equalsIgnoreCase( "NL" ) ) {
                    fixedPostalCode = fixedPostalCode.replaceAll( "^(\\d{4}).*$", "$1" );
                }
                form.getPickUp().setZip( fixedPostalCode );
                break;
        }
        ExpressFormValidator validator = (ExpressFormValidator) getValidator();
        validator.validate( form, errors, page );
    }


    protected void postProcessPage( HttpServletRequest request, Object command, Errors errors, int page ) throws Exception {
        ExpressForm form = (ExpressForm) command;
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ( page == 2 ) {

            if ( errors.hasErrors() )
                return;

            if ( currentUser == null || !(currentUser instanceof OverseasUser) ) {

                if ( form.isDoLogin() ) {
                    // try logging in
                    try {
                        Authentication authentication = usernamePasswordAuthenticationFilter.attemptAuthentication( request, null );
                        SecurityContextHolder.getContext().setAuthentication( authentication );
                    } catch ( AuthenticationException e ) {
                        errors.rejectValue( "doLogin", "", "Incorrect email address or password" );
                    }

                    currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if ( currentUser instanceof OverseasUser ) {
                        populateFedexForm( form, (OverseasUser) currentUser );
                    }
                    return;
                }

                // check if user submitted email and password for creating a new account
                if ( !form.isLoggedUser() ) {
                    // email and password should be checked by validator
                    OverseasUser oldUser = userService.findUserByName( form.getNotificationEmail() );
                    if ( oldUser != null ) {
                        errors.rejectValue( "fedexLabel", "", "An account with that email address already exists. Access the account using the \"Retrieve data from My Voter Account\" section below." );
                        return;
                    }

                    OverseasUser newUser = new OverseasUser();
                    newUser.setUsername( form.getNotificationEmail() );
                    newUser.setPassword( form.getNotificationPass() );
                    newUser.setScytlPassword( form.getNotificationPass() );
                    newUser.getName().setFirstName( form.getFirstName() );
                    newUser.getName().setLastName( form.getLastName() );
                    newUser.setRoles( userService.findRolesByName( UserRole.USER_ROLE_VOTER ) );
                    UserAddress currentAddress = new UserAddress( AddressType.OVERSEAS );
                    currentAddress.updateFrom( form.getPickUp() );
                    currentAddress.setCountry( form.getCountry().getName() );
                    newUser.setCurrentAddress( currentAddress );
                    userService.makeNewUser( newUser );
                    currentUser = userService.findUserByName(newUser.getUsername());

                    // log in
                    UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken( newUser, form.getNotificationPass(), newUser.getAuthorities() );
                    SecurityContextHolder.getContext().setAuthentication( t );

                    // send email
                    String serverPath = request.getServerName() + request.getContextPath();
                    FaceConfig config = getFacesService().findConfig( serverPath );
                    
                    try {
                        final String template = getFacesService().getApprovedFileName(EmailTemplates.XML_EYV_THANK_YOU, config.getRelativePrefix());
						final Email email = Email.builder()
                        		.template( template )
                        		.to( newUser.getUsername() )
                        		.model( "firstName", newUser.getName().getFirstName() )
                                .model( "priority", RawEmail.Priority.HIGH )
                        		.build();
                        emailService.queue(email);
                    } catch ( EmailException e ) {
                        logger.error( "Can't queue email.", e );
                    }
                }
            }

            boolean labelReady = false;
            if ( form.getFedexLabel() == null || form.getFedexLabel().getId() == 0 ) {
                labelReady = fedexGetLabelService.getFedexLabel( form );
            } else {
                labelReady = true;
            }
            if ( labelReady ) {
                FedexLabel label = form.getFedexLabel();
                label.setPaymentStatus( false );
                label.setCreated( new Date() );
                if ( currentUser instanceof OverseasUser ) {
                    label.setOwner( (OverseasUser) currentUser );
                }
                fedexService.saveFedexLabel( label );
            } else {
                errors.rejectValue( "fedexLabel", "", form.getFedexMessage() );
            }
        }

        if ( page <= 3 && getTargetPage( request, command, errors, page ) == 4 ) {
            if ( errors.hasErrors() )
                return;

            if ( form.getFedexLabel() != null ) {
                FedexLabel label = form.getFedexLabel();
                if ( form.getCountry().getRate() > 0 ) {
                    if ( authorizeNetService.doPayment( form, form.getCountry().getRate(), request.getRemoteAddr() ) ) {
                        label.setPaymentStatus( true );
                    } else {
                        errors.rejectValue( "fedexLabel", "", form.getAuthorizenetMessage() );
                        label.setPaymentStatus( false );
                    }
                    label.setMessage( form.getAuthorizenetMessage() );
                } else {
                    label.setPaymentStatus( true );
                    label.setMessage( "Free" );
                }
                fedexService.saveFedexLabel( label );
            } else {
                errors.rejectValue( "fedexLabel", "", form.getFedexMessage() );
            }
        }
    }

    private void populateFedexForm( ExpressForm form, OverseasUser user ) {
        populateFedexForm( form, user, false );
    }

    private void populateFedexForm( ExpressForm form, OverseasUser user, boolean findCountry ) {
        UserAddress overseasAddress = user.getCurrentAddress();

        if ( overseasAddress != null ) {
            form.getPickUp().setStreet1( overseasAddress.getStreet1() );
            form.getPickUp().setStreet2( overseasAddress.getStreet2() );
            form.getPickUp().setCity( overseasAddress.getCity() );
            form.getPickUp().setState( overseasAddress.getState() );
            form.getPickUp().setZip( overseasAddress.getZip() );
            if ( findCountry ) {
                String countryName = overseasAddress.getCountry();
                if ( countryName != null && countryName.length() > 0 ) {
                    CountryDescription countryDescription = fedexService.findFedexCountryByName( countryName );
                    if ( countryDescription != null ) {
                        form.setCountry( countryDescription );
                        form.setCountryId( countryDescription.getId() );
                    }
                }
            }
        }
        form.setNotificationEmail( user.getUsername() );

        form.setFirstName( user.getName().getFirstName() );
        form.setLastName( user.getName().getLastName() );
        form.setNotificationPhone( user.getPhone() );


        form.setLoggedUser( true );

        // find voting region
        if ( StringUtils.isNotEmpty( user.getEodRegionId() ) ) {
            LocalOffice leo = eodApiService.getLocalOffice( user.getEodRegionId(), true );
            if ( leo != null ) {
                form.setDestinationLeo( leo );
            }
        }
    }
}
