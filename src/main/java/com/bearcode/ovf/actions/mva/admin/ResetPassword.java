package com.bearcode.ovf.actions.mva.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.utils.CipherAgentUtils;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Date: 25.01.13
 * Time: 21:13
 *
 * @author Leonid Ginzburg
 */
@Controller
public class ResetPassword extends BaseController {

    @Autowired
    private OverseasUserService userService;

    @Autowired
    private OverseasUserValidator overseasUserValidator;

    public ResetPassword() {
        setSectionCss( "/css/rava.css" );
        setSectionName( "rava" );
        setPageTitle( "My Voter Account" );
        setContentBlock( "/WEB-INF/pages/blocks/ResetPassword.jsp" );
    }

    @ModelAttribute("user")
    public OverseasUser decipherUser( HttpServletRequest request,
                                      @RequestParam(value = "n", required = false) String cipherName ) {
        if ( cipherName == null ) return null;

        try {
            FaceConfig config = this.getFaceConfig( request );

            String[] parts = CipherAgentUtils.decodeToken( cipherName, config.getName() ).split( ":" );
            if ( parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0 ) {

                OverseasUser user = userService.findUserByName( parts[0] );

                Long time = Long.parseLong( parts[1], 16 );
                Calendar requested = Calendar.getInstance();
                requested.setTime( new Date( time ) );
                requested.add( Calendar.DAY_OF_YEAR, 2 );
                Calendar now = Calendar.getInstance();
                if ( now.before( requested ) ) {
                    return user;
                }
            }

        } catch ( Exception e ) {
            //logger.error( "Unable to decode authentication key", e );
        }
        return null;
    }

    @Override
    @ModelAttribute("noUser")
    protected OverseasUser getUser() {
        return super.getUser();
    }


    @RequestMapping(value = "/ResetPassword.htm", method = RequestMethod.GET)
    public String showResetPassword( HttpServletRequest request, ModelMap model,
                                        @ModelAttribute("user") OverseasUser user,
                                        BindingResult result
                                     ) {
        Set<String> skip = new HashSet<String>();
        skip.add( "gender" );
        skip.add( "voterType" );
        skip.add( "voterHistory" );
        skip.add( "birthDate" );
        skip.add( "birthMonth" );
        skip.add( "birthYear" );
        skip.add( "password" );

        if ( user != null ) {
            validateUser( request, user, result, skip );
        }
        if ( user == null || result.hasErrors() ) {
            model.addAttribute( "showErrorMsg", true );
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(value = "/ResetPassword.htm", method = RequestMethod.POST)
    public String postPasswordForm( HttpServletRequest request,
                                    ModelMap model,
                                    @ModelAttribute("user") OverseasUser user,
                                    BindingResult result ) {
        Set<String> skip = new HashSet<String>();
        skip.add( "gender" );
        skip.add( "voterType" );
        skip.add( "voterHistory" );
        skip.add( "birthDate" );
        skip.add( "birthMonth" );
        skip.add( "birthYear" );

        boolean validationOk = true;
        //boolean otherErrors = false;
        validateUser( request, user, result, skip );
        if ( result.hasErrors() ) {
            for ( FieldError error : result.getFieldErrors() ) {
                if ( !error.getField().equalsIgnoreCase( "password" )
                        && !error.getField().equalsIgnoreCase( "confirmPassword" ) ) {
                    model.addAttribute( "showErrorMsg", true );
                    //otherErrors = true;
                }
                else {
                    validationOk = false;    // looking only for password fields
                }
            }
        }
        if ( !validationOk ) {
            // re-display form
            return buildModelAndView( request, model );
        }
        String password = user.getPassword();
        user.setPassword( OverseasUser.encrypt( password ) );
        user.setScytlPassword( OverseasUser.encryptScytl( password ) );

        userService.saveUser( user );

        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(
                user, password, user.getAuthorities() );
        SecurityContextHolder.getContext().setAuthentication( t );
        return "redirect:Login.htm?updated=1" /*+ (otherErrors?"&showErrorMsg=1":"")*/;
    }

    @RequestMapping(value = "/ResetPassword.htm", method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }


    @InitBinder
    protected void initBinder( WebDataBinder binder ) {
        if ( binder.getTarget() instanceof OverseasUser ) {
            //binder.registerCustomEditor( VotingRegion.class, votingRegionPropertyEditor );
            binder.setValidator( overseasUserValidator );
        }
    }

    protected void validateUser( HttpServletRequest request, OverseasUser user,
                                 BindingResult errors ) {
        validateUser( request, user, errors, null );
    }

    protected void validateUser( HttpServletRequest request, OverseasUser user,
                                 BindingResult errors, Set<String> fieldsToSkip ) {
        if ( fieldsToSkip == null ) {
            fieldsToSkip = new HashSet<String>();
        }
        fieldsToSkip.addAll( this.getUserValidationSkipFields( request ) );
        overseasUserValidator.validateUser( user, errors, fieldsToSkip );
    }

}
