package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 9, 2007
 * Time: 3:53:28 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/RemoveAccount.htm")
public class RemoveVoterAccount extends BaseController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private OverseasUserService userService;

    public RemoveVoterAccount() {
        mainTemplate = "templates/SecondTemplate";
        setPageTitle( "Remove Voter Account" );
        setSectionName( "login" );
        setSectionCss( "/css/login.css" );
        setContentBlock( "/WEB-INF/pages/blocks/RemoveAccount.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/RemoveAccountSuccess.jsp" );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String buildReferences( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit( HttpServletRequest request,
                            ModelMap model,
                            @ModelAttribute("user") OverseasUser user ) throws Exception {

        if ( user != null ) {
            SessionContextStorage.create( request ).deleteAll();

            SecurityContextHolder.getContext().setAuthentication( null );
            //questionnaireService.makeUserAnswersAnonymity( user );
            userService.makeAnonymous( user );
        }

        return buildSuccessModelAndView( request, model );
    }

}
