package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 01.02.12
 * Time: 19:13
 *
 * @author Leonid Ginzburg
 */
@Controller
public class StaticPages extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(StaticPages.class);

    @RequestMapping("/RavaLogin.htm")
    public String wizardLoginPage( HttpServletRequest request, ModelMap model ) {
        setContentBlock( "/WEB-INF/pages/blocks/RavaLogin.jsp" );
        setSectionName( "rava" );
        setSectionCss( "/css/rava.css" );
        setPageTitle( "My Voter Account Login" );
        final WizardContext wizardContext = SessionContextStorage.create(request).load(true);
        if (wizardContext != null) {
        	model.addAttribute("votingAddress", wizardContext.getWizardResults().getVotingAddress());
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping("/AboutMVA.htm")
    public String aboutMvaPage( HttpServletRequest request, ModelMap model ) {
        setContentBlock( "/WEB-INF/pages/blocks/StaticAboutMVA.jsp" );
        setSectionName( "home" );
        setSectionCss( "/css/home.css" );
        setPageTitle( "About My Voter Account" );
        return buildModelAndView( request, model );
    }

    @RequestMapping("/PrivacyPolicy.htm")
    public String privacyPolicyPage( HttpServletRequest request, ModelMap model ) {
        setContentBlock( "/WEB-INF/pages/blocks/StaticPolicies.jsp" );
        setSectionName( "static" );
        setSectionCss( "/css/home.css" );
        setPageTitle( "Privacy Policy" );
        return buildModelAndView( request, model );
    }

    @RequestMapping("/TermsOfUse.htm")
    public String termsOfUsePage( HttpServletRequest request, ModelMap model ) {
        setContentBlock( "/WEB-INF/pages/blocks/StaticTermOfUse.jsp" );
        setSectionName( "static" );
        setSectionCss( "/css/home.css" );
        setPageTitle( "Terms Of Use" );
        return buildModelAndView( request, model );
    }

    @RequestMapping("/Contact.htm")
    public String contactPage( HttpServletRequest request, ModelMap model ) {
        setContentBlock( "/WEB-INF/pages/blocks/Contact.jsp" );
        setSectionName( "static" );
        setSectionCss( "/css/home.css" );
        setPageTitle( "Contact" );
        return buildModelAndView( request, model );
    }

    @RequestMapping("/FwabStart.htm")
    public String fwabStartPage( HttpServletRequest request, ModelMap model ) {
        setContentBlock( "/WEB-INF/pages/blocks/FwabStart.jsp" );
        setSectionName( "rava" );
        setSectionCss( "/css/rava.css" );
        setPageTitle( "Federal Write-in Absentee Ballot (FWAB)" );
        boolean oldShowMeta = isShowMetaKeywords();
        setShowMetaKeywords( true );
        model.addAttribute( "queryString", request.getQueryString() );
        String viewName = buildModelAndView( request, model );
        setShowMetaKeywords( oldShowMeta );
        return viewName;
    }

    @RequestMapping("/errors.htm")
    public String errorsPage( HttpServletRequest request, ModelMap model,
                              @RequestParam(value = "errorCode", required = false) String errCode) {
        //logger.warn("requestURL: {}", request.getRequestURL());
        setContentBlock( "/WEB-INF/pages/blocks/Errors.jsp" );
        setSectionName( "" );
        setSectionCss( "" );
        setPageTitle( "Error" );
        model.addAttribute( "errorCode", errCode == null ? "500" : errCode );
        return buildModelAndView( request, model );
    }

    /**
     * Controller for monitoring system working
     *
     * @param request Http request
     * @param model   page model map
     * @return view name
     */
    @RequestMapping("/monitor.htm")
    public String monitor( HttpServletRequest request, ModelMap model ) {
        setPageTitle( "Monitor" );
        prepareModel( request, model );
        return "templates/MonitorPage";
    }

    @RequestMapping("/Cookies.htm")
    public String cookiesPage( HttpServletRequest request, ModelMap model ) {
        setContentBlock( "/WEB-INF/pages/blocks/StaticCookies.jsp" );
        setSectionName( "static" );
        setSectionCss( "/css/home.css" );
        setPageTitle( "Cookies" );
        return buildModelAndView( request, model );
    }
}
