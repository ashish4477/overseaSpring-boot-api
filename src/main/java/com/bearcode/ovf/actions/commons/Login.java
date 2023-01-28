package com.bearcode.ovf.actions.commons;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 19.10.11 Time: 22:14
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/Login.htm")
public class Login extends BaseController {

    public Login() {
        mainTemplate = "templates/SecondTemplate";
        setPageTitle( "My Voter Account Login" );
        setContentBlock( "/WEB-INF/pages/blocks/Login.jsp" );
        setSectionName( "login" );
        setSectionCss( "/css/login.css" );
    }

    @RequestMapping
    public String showPage( final HttpServletRequest request, final ModelMap model ) {
        model.addAttribute( "j_username", request.getParameter( "j_username" ) );
        final String header = request.getHeader( "referer" );
        if ( header != null ) {
            final String[] referer = header.split( request.getContextPath() );
            if ( referer.length > 1 && request.getSession().getAttribute( LoginSuccessHandler.LOGIN_CAME_FROM ) == null ) {
                request.getSession().setAttribute( LoginSuccessHandler.LOGIN_CAME_FROM, referer[1] );
            }
        }
        return buildModelAndView( request, model );
    }
}
