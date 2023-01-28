package com.bearcode.ovf.actions.system;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.system.OvfProperty;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("/admin")
public class OvfPropertiesController extends BaseController {

    @Autowired
    private OvfPropertyService ovfPropertyService;

    public OvfPropertiesController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/ListOvfProperties.jsp" );
        setSectionCss( "/css/admin.css" );
        setSectionName( "admin" );
        setPageTitle( "List of System Properties" );

    }

    @ModelAttribute("ovfProperties")
    public List<OvfProperty> getActualProperties() {
        Map<OvfPropertyNames,OvfProperty> properties = new HashMap<OvfPropertyNames, OvfProperty>();
        for ( OvfProperty property : ovfPropertyService.findAllProperties() ) {
            properties.put( property.getPropertyName(), property );
        }
        for ( OvfPropertyNames names : OvfPropertyNames.values() ) {
            if ( !properties.containsKey( names ) ) {
                properties.put( names, new OvfProperty( names, names.getDefaultValue() ) );
            }
        }
        List<OvfProperty> result = new LinkedList<OvfProperty>( properties.values() );
        Collections.sort( result );

        return result;
    }

    @RequestMapping("/PropertiesList.htm")
    public String showPropertiesList( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }
}
