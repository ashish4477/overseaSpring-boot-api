package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.service.LocalOfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 4, 2007
 * Time: 4:36:12 PM
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EodStates.htm")
public class StatesListController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;


    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService(LocalOfficialService localOfficialService) {
        this.localOfficialService = localOfficialService;
    }

    public StatesListController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodStateList.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "List of States" );
    }

    @ModelAttribute("paging")
    protected CommonFormObject getPaging() {
        CommonFormObject object = new CommonFormObject();
        object.setPageSize( 25 );                           // TODO get param from config
        return object;   
    }

    @ModelAttribute("statistics")
    public Collection<Map<String,Object>> getStateStatistics( @ModelAttribute("paging") CommonFormObject paging ) {
        return localOfficialService.findStateStatistics( paging );
    }

    @RequestMapping( method = RequestMethod.GET )
    public String showStates( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }
}
