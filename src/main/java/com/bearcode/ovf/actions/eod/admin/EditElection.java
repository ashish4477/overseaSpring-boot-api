package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.eod.Election;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.service.LocalOfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/ElectionEdit.htm")
public class EditElection extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    public EditElection() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodEditElection.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/admin/EodEditSuccessPage.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "Edit Election Deadlines" );
    }

    @ModelAttribute("election")
    protected Election formBackingObject( @RequestParam(value = "electionId", required = false, defaultValue = "0") Long electionId,
                                          @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        if ( electionId == 0 ) {
            // create new election
            StateSpecificDirectory svid = getLocalOfficialService().findSvidForState( stateId );
            Election election = new Election();
            election.setStateInfo( svid );
            return election;
        }
        return localOfficialService.findElection( electionId );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("election") Election command, BindingResult errors ) {
        String message = "eod.admin.election.save_success";
        if ( request.getParameterMap().containsKey( "save" ) ) {
            localOfficialService.saveElection( command );
        }
        if ( request.getParameterMap().containsKey( "delete" ) ) {
            localOfficialService.deleteElection( command );
            message = "eod.admin.election.delete_success";
        }
        model.addAttribute( "messageCode", message );
        return buildSuccessModelAndView( request, model );
    }
}
