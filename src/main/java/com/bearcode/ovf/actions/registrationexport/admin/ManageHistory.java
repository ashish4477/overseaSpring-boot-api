package com.bearcode.ovf.actions.registrationexport.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DataExportHistory;
import com.bearcode.ovf.model.registrationexport.ExportHistoryStatus;
import com.bearcode.ovf.service.RegistrationExportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leonid on 11.10.16.
 */
@Controller
@RequestMapping("/admin/ManageExportHistory.htm")
public class ManageHistory extends BaseController {

    @Autowired
    private RegistrationExportService registrationExportService;

    public ManageHistory() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/ManageHistory.jsp" );
        setPageTitle( "Manage history" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("exportCofig")
    public DataExportConfiguration getConfig( @RequestParam(value = "id", required = false) Long id) {
        if ( id == null ) {
            return null;
        }
        return registrationExportService.findConfiguration( id );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showPage( HttpServletRequest request, ModelMap modelMap,
                            @ModelAttribute("exportCofig") DataExportConfiguration configuration ) {
        if ( configuration == null ) {
            return "redirect:/admin/ExportConfigurationsList.htm";
        }

        long historyCount = registrationExportService.countHistories( configuration );
        DataExportHistory firstHistory = registrationExportService.findFirstHistory( configuration );
        DataExportHistory lastHistory = registrationExportService.findLastHistory( configuration );
        DataExportHistory lastExported = registrationExportService.findLastHistory( configuration, ExportHistoryStatus.EXPORTED );
        long preparedCount = registrationExportService.countHistories( configuration, ExportHistoryStatus.PREPARED );
        DataExportHistory firstPrepared = registrationExportService.findFirstHistory( configuration, ExportHistoryStatus.PREPARED );

        modelMap.addAttribute( "historyCount", historyCount );
        modelMap.addAttribute( "firstHistory", firstHistory );
        modelMap.addAttribute( "lastHistory", lastHistory );
        modelMap.addAttribute( "lastExported", lastExported );
        modelMap.addAttribute( "preparedCount", preparedCount );
        modelMap.addAttribute( "firstPrepared", firstPrepared );


        return buildModelAndView( request, modelMap );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String setNewExportDate( HttpServletRequest request, ModelMap modelMap,
                                    @ModelAttribute("exportCofig") DataExportConfiguration configuration,
                                    @RequestParam(value = "startExportDate", required = false) String exportString ) {
        if ( configuration == null ) {
            return "redirect:/admin/ExportConfigurationsList.htm";
        }

        if ( StringUtils.isNotBlank( exportString ) ) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date exportDate = dateFormat.parse( exportString );
                registrationExportService.updateHistoriesForExport( configuration, exportDate );
            } catch (ParseException e) {
                // nothing
            }
        }

        return showPage( request, modelMap, configuration );
    }

}
