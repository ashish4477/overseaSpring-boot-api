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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This controller is used for displaying the PDF download link
 * <p/>
 * <p/>
 * Date: 8/19/2020
 * Time: 6:29:36 PM
 *
 * @author Leonid Ginzburg, Daemmon hughes
 */
@Controller
@RequestMapping("/DownloadLinkPage.htm")
public class DownloadLinkPage extends BaseController {
    public DownloadLinkPage() {
        setPageTitle( "Form Download" );
        setSectionCss( "/css/rava.css" );
        setSectionName( "rava Download" );
        setContentBlock( "/WEB-INF/pages/blocks/DownloadLinkPage.jsp" );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String buildReferences( HttpServletRequest request,
                                   ModelMap model,
                                   @RequestParam(value = "generationUUID", required = true) final String uuid ) {
        model.addAttribute( "uuid", uuid );
        return buildModelAndView( request, model );
    }
}
