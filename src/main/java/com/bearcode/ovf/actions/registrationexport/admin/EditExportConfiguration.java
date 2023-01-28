package com.bearcode.ovf.actions.registrationexport.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.DeliverySchedulePropertyEditor;
import com.bearcode.ovf.editor.ExportLevelPropertyEditor;
import com.bearcode.ovf.editor.FaceConfigPropertyEditor;
import com.bearcode.ovf.forms.AdminExportConfigForm;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DeliverySchedule;
import com.bearcode.ovf.model.registrationexport.ExportLevel;
import com.bearcode.ovf.service.RegistrationExportService;
import com.bearcode.ovf.validators.AdminExportConfigValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by leonid on 08.10.16.
 */
@Controller
@RequestMapping("/admin/EditExportConfiguration.htm")
public class EditExportConfiguration extends BaseController {

    @Autowired
    private RegistrationExportService registrationExportService;

    @Autowired
    private FaceConfigPropertyEditor faceConfigPropertyEditor;

    @Autowired
    private DeliverySchedulePropertyEditor schedulePropertyEditor;

    @Autowired
    private ExportLevelPropertyEditor exportLevelPropertyEditor;

    @Autowired
    private AdminExportConfigValidator adminExportConfigValidator;

    public EditExportConfiguration() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditExportConfiguration.jsp" );
        setPageTitle( "Edit data export configuration" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("exportCofiguration")
    public AdminExportConfigForm getConfiguration( @RequestParam(value = "id", required = false) Long id ) {
        DataExportConfiguration configuration = null;
        if ( id != null && id != 0 ) {
            configuration = registrationExportService.findConfiguration( id );
        }
        if ( configuration == null ) {
            configuration = new DataExportConfiguration();
            configuration.setFaceConfigs( new ArrayList<FaceConfig>() );
        }
        return new AdminExportConfigForm( configuration );
    }

    @ModelAttribute("faceConfigs")
    public Collection<FaceConfig> getFaces() {
        return super.getFacesService().findAllConfigs();
    }

    @ModelAttribute("deliverySchedules")
    public DeliverySchedule[] getDeliveries()  {
        return DeliverySchedule.values();
    }

    @ModelAttribute("exportLevels")
    public ExportLevel[] getExportLevels() {
        return ExportLevel.values();
    }


    @RequestMapping(method = RequestMethod.GET)
    public String showPage( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveConfiguration( HttpServletRequest request, ModelMap modelMap,
            @Valid @ModelAttribute("exportCofiguration") AdminExportConfigForm configuration, BindingResult errors ) {
        if ( !errors.hasErrors() ) {
            configuration.adjustFaceConfigs();
            boolean creation =  configuration.getConfiguration().getId() == 0;
            registrationExportService.saveConfiguration( configuration.getConfiguration() );
            if ( creation ) {
                registrationExportService.makeHistoriesForNewConfig( configuration.getConfiguration() );
            }
        }

        return buildModelAndView( request, modelMap );
    }

    @InitBinder
    public void initBinder( final ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof AdminExportConfigForm ) {

            binder.setValidator( adminExportConfigValidator );
            binder.registerCustomEditor( FaceConfig.class, faceConfigPropertyEditor );
            binder.registerCustomEditor( DeliverySchedule.class, schedulePropertyEditor );
            binder.registerCustomEditor( ExportLevel.class, exportLevelPropertyEditor );
        }
    }
}
