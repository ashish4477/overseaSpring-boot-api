package com.bearcode.ovf.actions.mva;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceBookApi;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import com.restfb.DefaultFacebookClient;
import com.restfb.types.User;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * This controller handles user account management URLs
 * <p/>
 * /CreateAccount.htm
 * /UpdateAccount.htm
 * /ChangePassword.htm
 */
@Controller
@SessionAttributes("user")
public class FacebookIntegration extends BaseController {

    @Autowired
    private OverseasUserService userService;

    private final static String FB_PARAMETER_ACCESS_TOKEN = "access_token=";
    private final static String FB_AUTH_TOKEN_URL = "https://graph.facebook.com/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s";
    private final static String FB_ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token" +
            "?client_id=%s" +
            "&client_secret=%s" +
            "&code=%s" +
            "&redirect_uri=%s";


    public final static int AUTH_ERROR_NEED_FB_INTEGRATION = 1;

    @RequestMapping(value = "/FacebookLogin.htm", method = RequestMethod.GET)
    public String doFacebookLogin( HttpServletRequest request, ModelMap model ) {

        FaceBookApi fbApi = getFaceBookApi( request );
        String fbApiKey = "";
        if ( fbApi != null ) {
            fbApiKey = fbApi.getAppKey();
        }
        String facebookOAuthUrl = String.format( FB_AUTH_TOKEN_URL, fbApiKey, buildFullUrl( request, "/FacebookLoginResult.htm" ), "email" );
        model.addAttribute( "facebookOAuthUrl", facebookOAuthUrl );
        setContentBlock( "/WEB-INF/pages/blocks/FacebookLogin.jsp" );
        return buildModelAndView( request, model );
    }

    /**
     * Intended to receive requests from the Facebook OAuth system
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/FacebookLoginResult.htm", method = RequestMethod.GET)
    public String doFacebookLoginResult( HttpServletRequest request, ModelMap model ) {

        String fbErrorReason = null;
        String fbErrorDesc = null;
// handle error codes in the request
        String fbCode = MapUtils.getString( request.getParameterMap(), "error", "" );

        if ( fbCode.length() > 0 ) {
            fbErrorDesc = MapUtils.getString( request.getParameterMap(), "error_description", "" );
            fbErrorReason = MapUtils.getString( request.getParameterMap(), "error_reason", "" );
            if ( fbErrorDesc.length() == 0 ) {
                fbErrorDesc = "unknown";
            }
            if ( fbErrorReason.length() == 0 ) {
                fbErrorReason = "unknown";
            }
            return returnError( request, model, fbErrorReason, fbErrorDesc );
        }

        fbCode = MapUtils.getString( request.getParameterMap(), "code", "" );
        if ( fbCode.length() == 0 ) {
            return returnError( request, model, "missing code", "no code parameter was received" );
        }

        // Try to get the auth token
        String redirectURL = buildFullUrl( request, "/FacebookLoginResult.htm" );
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout( 10000 );
        String responseString = null;
        try {
            String facebookAppId = "";
            String facebookAppSecret = "";
            FaceBookApi fbApi = getFaceBookApi( request );
            if ( fbApi != null ) {
                facebookAppId = fbApi.getAppKey();
                facebookAppSecret = fbApi.getAppSecret();
            }

            GetMethod method = new GetMethod( String.format( FB_ACCESS_TOKEN_URL,
                    facebookAppId, facebookAppSecret,
                    URLEncoder.encode( fbCode, "utf-8" ),
                    URLEncoder.encode( redirectURL, "utf-8" ) ) );
            httpClient.executeMethod( method );
            responseString = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
            method.releaseConnection();
        } catch ( HttpException e ) {
            logger.error( "could not access FB_ACCESS_TOKEN_URL " + FB_ACCESS_TOKEN_URL + " with code " + fbCode, e );
        } catch ( IOException e ) {
            logger.error( "could not access FB_ACCESS_TOKEN_URL " + FB_ACCESS_TOKEN_URL + " with code " + fbCode, e );
        }

        // handle error getting auth token
        if ( responseString == null || !responseString.startsWith( FB_PARAMETER_ACCESS_TOKEN ) ) {
            return returnError( request, model, "no auth token", "could not retrieve an auth token from Facebook" );
        }

        User user = null;
        try {
            String authToken = responseString.substring( FB_PARAMETER_ACCESS_TOKEN.length() );
            authToken = authToken.replaceAll( "&.*", "" );
            DefaultFacebookClient fbClient = new DefaultFacebookClient( authToken );
            user = fbClient.fetchObject( "me", User.class );
        } catch ( Exception e ) {
            logger.error( "could not retrieve user information from Facebook", e );
        }

        if ( user == null ) {
            return returnError( request, model, "no facebook user", "could not retrieve user information from Facebook" );
        }

        String fbEmail = user.getEmail();
        OverseasUser existingUser = userService.findUserByName( fbEmail );
        if ( existingUser != null ) {
            // if a user with this email exists but is not facebook enabled,
            // we need to make the user verify that they allow FB login
            if ( !existingUser.isFacebookIntegration() ) {
                model.addAttribute( "email", fbEmail );
                return returnError( request, model, AUTH_ERROR_NEED_FB_INTEGRATION );
            } else {
                String origHash = existingUser.getPassword();
                String tempPass = OverseasUser.generatePassword();
                existingUser.setPassword( tempPass );
                UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken( existingUser, tempPass, existingUser.getAuthorities() );
                SecurityContextHolder.getContext().setAuthentication( t );
                existingUser.setPassword( origHash );
                return "redirect:/Login.htm";
            }
        } else {
            return "redirect:/CreateAccount.htm";
        }
    }

    private String returnError( HttpServletRequest request, ModelMap model, String error, String errorDesc ) {

        model.addAttribute( "errorDesc", errorDesc );
        model.addAttribute( "errorReason", error );
        setContentBlock( "/WEB-INF/pages/blocks/FacebookLoginError.jsp" );
        return buildModelAndView( request, model );
    }

    private String returnError( HttpServletRequest request, ModelMap model, int errorCode ) {

        model.addAttribute( "errorCode", errorCode );
        setContentBlock( "/WEB-INF/pages/blocks/FacebookLoginError.jsp" );
        return buildModelAndView( request, model );
    }


}
