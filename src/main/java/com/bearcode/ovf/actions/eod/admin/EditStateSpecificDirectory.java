package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.validators.StateSpecificDirectoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/SvidEdit.htm")
public class EditStateSpecificDirectory extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private StateSpecificDirectoryValidator validator;

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService(LocalOfficialService localOfficialService) {
        this.localOfficialService = localOfficialService;
    }

    public EditStateSpecificDirectory() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodEditSsvid.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/admin/EodEditSuccessPage.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "Edit State Voter Information" );
    }

    @ModelAttribute("svid")
    protected StateSpecificDirectory formBackingObject(@RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        return localOfficialService.findSvidForState( stateId );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(HttpServletRequest request, ModelMap model,
                                 @ModelAttribute("svid") @Valid StateSpecificDirectory command, BindingResult errors) {
        if ( request.getParameterMap().containsKey("save") && !errors.hasErrors() ) {
            localOfficialService.saveSvid( command );
            model.addAttribute( "messageCode", "eod.admin.svid.save_success" );
            return buildSuccessModelAndView( request, model );
        }
        return buildModelAndView( request, model );
    }

    @InitBinder
    protected void initBinder( DataBinder binder ) {
        if ( binder.getTarget() instanceof StateSpecificDirectory ) {
            binder.setValidator( validator );
        }
    }
}
