package com.bearcode.ovf.actions;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.VoterClassificationType;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.HomeSecondaryContentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 1, 2007
 * Time: 8:50:57 PM
 *
 * @author Leonid Ginzburg
 */

@Controller
public class HomeController extends BaseController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private HomeSecondaryContentHelper secondaryContentHelper;

    public HomeController() {
        setPageTitle( "Home" );
        setContentBlock( "/WEB-INF/pages/blocks/Home.jsp" );
        setSectionCss( "/css/home.css" );
        setSectionName( "home" );
        setShowMetaKeywords( true );
     }

    public void setQuestionnaireService( QuestionnaireService questionnaireService ) {
        this.questionnaireService = questionnaireService;
    }

    @RequestMapping("/home.htm")
    public String showPage( HttpServletRequest request, ModelMap model ) throws Exception {
        FaceConfig config = getFaceConfig( request );
        if ( config.getRelativePrefix().contains( "skimm" ) ) {
            return "redirect:/sviddomestic.htm";
        }
        
        if ( config.getRelativePrefix().contains( "vote411" ) ) {
            return "redirect:/voter-registration-absentee-voting.htm";
        }

        model.addAttribute( "pageObject", questionnaireService.findPageByNumber( 1, FlowType.RAVA.getPageType() ) );
        model.addAttribute( "voterClassificationTypesList", VoterClassificationType.values() );

        if ( config.getRelativePrefix().contains( "widget" ) ) {
            return "redirect:/w/rava.htm";
        }
        String contentUrl = config.getExternalContentUrl();
        if ( contentUrl.trim().length() == 0 ) {
            config = facesService.findDefaultConfig();
            contentUrl = config.getExternalContentUrl();
        }

        String serverPath = (request.getProtocol().toLowerCase().contains("https") ? "https" : "http") + "://" + request.getServerName() + contentUrl;
        String secondary = secondaryContentHelper.getSecondaryContent( serverPath, getHttpClient(), deploymentEnv );
        model.addAttribute( "secondaryContent", secondary );

        return buildModelAndView( request, model );
    }

    @RequestMapping("eoddomot.htm")
    public String eoddomotPage(HttpServletRequest request) {
        return "redirect:https://www.usvotefoundation.org/us-voter-faq";
    }

    @RequestMapping("/eoddomesticus")
    public String eoddomesticusPage(HttpServletRequest request) {
        return "redirect:https://www.usvotefoundation.org/us-voter-faq";
    }

    @RequestMapping("/state-elections/state-election-dates-de")
    public String sedPage(HttpServletRequest request) {
        return "redirect:https://www.usvotefoundation.org/vote/state-elections/state-election-dates-deadlines.htm";
    }

}
