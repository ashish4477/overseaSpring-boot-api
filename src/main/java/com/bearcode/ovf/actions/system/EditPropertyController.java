package com.bearcode.ovf.actions.system;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.system.OvfProperty;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("/admin/EditProperty.htm")
public class EditPropertyController extends BaseController {

    @Autowired
    private OvfPropertyService propertyService;

    public EditPropertyController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditOvfProperty.jsp" );
        setSectionCss( "/css/admin.css" );
        setSectionName( "admin" );
        setPageTitle( "Edit System Properties" );
    }

    @ModelAttribute("ovfProperty")
    public OvfProperty getOvfProperty( @RequestParam("propertyName") OvfPropertyNames propertyNames) {
        List<OvfProperty> properties = propertyService.findAllProperties();
        for ( OvfProperty property : properties ) {
            if ( property.getPropertyName() == propertyNames ) {
                return property;
            }
        }
        return new OvfProperty( propertyNames, propertyNames.getDefaultValue() );
    }

    @RequestMapping( method = RequestMethod.GET )
    public String showProperty(HttpServletRequest request, ModelMap model,
                               @ModelAttribute("ovfProperty") OvfProperty property ) {
        if ( property == null || property.getPropertyName() == null ) {
            return "redirect:/admin/PropertiesList.htm";
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping( method = RequestMethod.POST )
    public String submitProperty( HttpServletRequest request, ModelMap model,
                                  @ModelAttribute("ovfProperty") OvfProperty property ) {

        if ( property != null && property.getPropertyName() != null ) {
            if ( !property.getPropertyValue().equals( property.getPropertyName().getDefaultValue() ) || property.getId() != 0 ) {
                propertyService.saveProperty( property );
            }
        }
        return "redirect:/admin/PropertiesList.htm";
    }

    @RequestMapping( method = RequestMethod.HEAD )
    public ResponseEntity<String> unsupportedMethods() {
        return sendMethodNotAllowed();
    }
}
