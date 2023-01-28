package com.bearcode.ovf.security;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * Overrides default behavior of spring security AuthenticationProcessingFilter when using the
 * spring-security-redirect parameter. Default behavior is that if the initial authentication fails,
 * the value of that parameter is lost and so the browser is redirected to the default redirect URL on
 * successful authentication.
 *
 * This class will instead save the spring-security-redirect parameter value in the http session and redirect
 * the browser to that URL regardless of how many failed authentication attempts occur between the inital
 * submission of that parameter and a succesful authentication request.
 *
 * Created by IntelliJ IDEA.
 * User: dhughes
 * Date: Sep 10, 2010
 * Time: 2:23:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class BCAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter
 {

    public static final String SUCCESS_REDIRECT_URL_KEY = "BC_SUCCESS_REDIRECT_URL_KEY";

    /**
     * If the target URL is not the default, then save it in the session so that it is accessible
     * if/when there is a successful authentication
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException {
        String targetUrl = this.determineTargetUrl(request);
        if(targetUrl != null && !targetUrl.equals(getDefaultTargetUrl())){
            request.getSession().setAttribute(SUCCESS_REDIRECT_URL_KEY,targetUrl);
        }
    }

    /**
     * Remove any saved target URL from the session
     *
     * @param request
     * @param response
     * @param authResult
     * @throws IOException

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException {
        request.getSession().removeAttribute(SUCCESS_REDIRECT_URL_KEY);
    }

    /**
     * Returns the target URL to redirect to upon successful authentication. If the session contains a redirect
     * URL from an initial authentication attempt that included a spring-security-redirect parameter AND the default
     * target URL is returned by super.determineTargetUrl(), then the saved URL is returned.
     *
     * @param request
     * @return

    protected String determineTargetUrl(HttpServletRequest request) {
        String currentTarget = super.determineTargetUrl(request);

        String savedTarget = (String) request.getSession().getAttribute(SUCCESS_REDIRECT_URL_KEY);

        if (StringUtils.hasText(savedTarget) && currentTarget.equals(getDefaultTargetUrl())) {
            try {
                currentTarget = URLDecoder.decode(savedTarget, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("UTF-8 not supported. Shouldn't be possible");
            }
        }
        
        return currentTarget;
   }
	*/
    
}
