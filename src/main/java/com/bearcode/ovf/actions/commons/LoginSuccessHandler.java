package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 27.10.11 Time: 17:20
 * <p/>
 * This handler makes decision where to go after login. Login controller stores page name where user was before login into the
 * session. This handler redirects to that page if the page is not in exclude list.
 *
 * @author Leonid Ginzburg
 */
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String LOGIN_CAME_FROM = "LOGIN_CAME_FROM";

    /**
     * the target URLs by role (overrides the default if set).
     *
     * @author IanBrown
     * @version Mar 27, 2012
     * @since Mar 27, 2012
     */
    private Map<Object, Object> roleTargetUrl;

    private String[] excludePagePatterns = {};

    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * Gets the role target URL map.
     *
     * @return the role target URL map.
     * @author IanBrown
     * @version Mar 27, 2012
     * @since Mar 27, 2012
     */
    public Map<Object, Object> getRoleTargetUrl() {
        return roleTargetUrl;
    }

    @Override
    public void onAuthenticationSuccess( final HttpServletRequest request, final HttpServletResponse response,
                                         final Authentication authentication ) throws IOException, ServletException {
        String referer = defineRequestedUrl( request, response, authentication ); /*(String) request.getSession().getAttribute( LOGIN_CAME_FROM );*/
        if ( referer == null || checkExcludedPage( referer ) ) {
            referer = null;
            if ( getRoleTargetUrl() != null ) {
                final OverseasUser user = (OverseasUser) authentication.getPrincipal();
                for ( int idx = UserRole.ALL_ROLES.length - 1; idx >= 0; --idx ) {
                    final String roleName = UserRole.ALL_ROLES[idx];
                    if ( user.isInRole( roleName ) ) {
                        referer = (String) getRoleTargetUrl().get( roleName );
                        if ( referer != null ) {
                            break;
                        }
                    }
                }
            }

            if ( referer == null ) {
                referer = determineTargetUrl( request, response );
            }
        }
        request.getSession().removeAttribute( LOGIN_CAME_FROM );
        getRedirectStrategy().sendRedirect( request, response, referer );
    }

    public void setExcludePagePatterns( final String[] excludePagePatterns ) {
        this.excludePagePatterns = excludePagePatterns;
    }

    /**
     * Sets the role target URL map.
     *
     * @param roleTargetUrl the role target URL map to set.
     * @author IanBrown
     * @version Mar 27, 2012
     * @since Mar 27, 2012
     */
    public void setRoleTargetUrl( final Map<Object, Object> roleTargetUrl ) {
        this.roleTargetUrl = roleTargetUrl;
    }

    private boolean checkExcludedPage( final String page ) {
        for ( final String pattern : excludePagePatterns ) {
            final Matcher matcher = Pattern.compile( pattern ).matcher( page );
            if ( matcher.find() ) {
                return true;
            }
        }
        return false;
    }

    private String defineRequestedUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            return null;
        }

        if (isAlwaysUseDefaultTargetUrl() || StringUtils.hasText( request.getParameter( getTargetUrlParameter() ) )) {
            requestCache.removeRequest(request, response);
            //super.onAuthenticationSuccess(request, response, authentication);

            return determineTargetUrl( request, response );
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        return targetUrl;

    }
}
