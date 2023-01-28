package com.bearcode.ovf.actions.registrationexport.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.service.RegistrationExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by leonid on 11.10.16.
 */
@Controller
public class ExportConfigurationsList extends BaseController {

    @Autowired
    private RegistrationExportService registrationExportService;

    public ExportConfigurationsList() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/ExportConfigurationsList.jsp" );
        setPageTitle( "Export configurations list" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );

    }

    @ModelAttribute("exportConfigs")
    public List<DataExportConfiguration> getConfigs() {
        return registrationExportService.findAllConfigurations();
    }

    @RequestMapping("/admin/ExportConfigurationsList.htm")
    public String showPage( HttpServletRequest request, ModelMap modelMap ) {
        return buildModelAndView( request, modelMap );
    }
}
