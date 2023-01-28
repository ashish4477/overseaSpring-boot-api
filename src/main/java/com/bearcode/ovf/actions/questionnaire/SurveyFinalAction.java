package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.PdfFormTrack;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DataExportHistory;
import com.bearcode.ovf.model.registrationexport.ExportHistoryStatus;
import com.bearcode.ovf.service.RegistrationExportService;
import com.bearcode.ovf.tools.pdf.CreatePdfExecutor;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This controller is used for displaying the PDF download link
 * <p/>
 * <p/>
 * Date: 7/9/2010
 * Time: 6:29:36 PM
 *
 * @author Leonid Ginzburg, Daemmon hughes
 */
@Controller
@RequestMapping("/Download.htm")
public class SurveyFinalAction extends BaseController {

    private int downloadExpiration = 300; //seconds that the download link is valid

    @Autowired
    private CreatePdfExecutor createPdfExecutor;

    @Autowired
    private RegistrationExportService registrationExportService;

    public SurveyFinalAction() {
        setPageTitle( "Form Download" );
        setSectionCss( "/css/rava.css" );
        setSectionName( "rava Download" );
        setContentBlock( "/WEB-INF/pages/blocks/wizard/DownloadLinkPage.jsp" );
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

    @RequestMapping(method = RequestMethod.GET)
    public String buildReferences( HttpServletRequest request,
                                   ModelMap model,
                                   @ModelAttribute("wizardContext") WizardContext wizardContext ) {

        model.addAttribute( "downloadExpiration", downloadExpiration );
        wizardContext.setFlowFinished( true );
        if ( wizardContext.isFormCompleted() ) {
            model.addAttribute( "formValid", true );
            model.addAttribute( "wizardUrl", SurveyWizard.buildUrl( wizardContext ) );
            PdfFormTrack track = createPdfExecutor.getFormTrack( wizardContext );
            model.addAttribute( "generationId", track.getId() );
            if(wizardContext.getWizardResults().getUuid() == null){
                wizardContext.getWizardResults().setUuid(UUID.randomUUID().toString());
            }
            model.addAttribute( "generationUUID", wizardContext.getWizardResults().getUuid());
            createPdfExecutor.createPdfFormFile( track.getId(), wizardContext );

            createExportHistory( wizardContext );
        }
        return buildModelAndView( request, model );
    }

    /**
     * A "submit" request will invalidate the session form. We use javascript on the form view to create a submit request after
     * downloadExpiration seconds or on a DOM page unload event.
     *
     * @param request       Http Request
     * @param model         Model map
     * @param wizardContext Current wizard context
     * @return Name of view
     */
    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit( HttpServletRequest request,
                               ModelMap model,
                               @ModelAttribute("wizardContext") WizardContext wizardContext ) {

        // clear the session
        SessionContextStorage.create( request, wizardContext ).delete();

        model.addAttribute( "expired", true );
        model.addAttribute( "formValid", false );
        return buildSuccessModelAndView( request, model );
    }

    public int getDownloadExpiration() {
        return downloadExpiration;
    }

    public void setDownloadExpiration( int downloadExpiration ) {
        this.downloadExpiration = downloadExpiration;
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<String> dummyHeadRequest() {
        return sendMethodNotAllowed();
    }


    private void createExportHistory( WizardContext wizardContext ) {
        if (wizardContext.getFlowType() != FlowType.RAVA) {
            return;
        }
        List<DataExportConfiguration> exportConfigs = registrationExportService.findConfigurationsForFace( wizardContext.getCurrentFace() );
        for ( DataExportConfiguration exportConfiguration : exportConfigs ) {
            DataExportHistory history = new DataExportHistory();
            history.setStatus( ExportHistoryStatus.PREPARED );
            history.setExportConfig( exportConfiguration );
            history.setWizardResults( wizardContext.getWizardResults() );
            registrationExportService.saveExportHistory( history );
        }
    }

    @ModelAttribute("hideScript")
    public boolean getHideScript() {
        return true;
    }
}
