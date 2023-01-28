package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.webservices.eod.EodApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by leonid on 25.08.16.
 * An additional page after the download page containing how to return your ballot information
 * and share on facebook button.
 */
@Controller
public class DownloadSuccess extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private EodApiService eodApiService;

    public DownloadSuccess() {
        setPageTitle( "Complete Registration" );
        setSectionCss( "/css/rava.css" );
        setSectionName( "rava DownloadSuccess" );
        setContentBlock( "/WEB-INF/pages/blocks/wizard/DownloadSuccessPage.jsp" );
    }

    /**
     * Use the SurveyWizard session form for this controller as well
     *
     * @param request Http Request
     * @return Current WizardContext or null
     */
    @ModelAttribute("wizardContext")
    protected WizardContext formBackingObject( HttpServletRequest request ) {
        WizardContext wizardContext = SessionContextStorage.create( request ).load();
        if ( wizardContext != null )
            return wizardContext;

        return new WizardContext();
    }

    /**
     * Use the SurveyWizard session form for this controller as well
     *
     * @param request Http Request
     * @return Current WizardContext or null
     */
    @ModelAttribute("leo")
    protected LocalOfficial getVotingRegion( HttpServletRequest request ) {
        WizardContext wizardContext = SessionContextStorage.create( request ).load();
        if ( wizardContext != null && wizardContext.getWizardResults() != null ) {
            VotingRegion region = wizardContext.getWizardResults().getVotingRegion();
            return localOfficialService.findForRegion( region );
        }
        return null;
    }

    @RequestMapping("/DownloadSuccess.htm")
    public String buildReferences( HttpServletRequest request, ModelMap model,
                                   @ModelAttribute("wizardContext") WizardContext wizardContext ) {

        model.addAttribute( "wizardUrl", SurveyWizard.buildUrl( wizardContext ) );

        return buildModelAndView( request, model );
    }

}
