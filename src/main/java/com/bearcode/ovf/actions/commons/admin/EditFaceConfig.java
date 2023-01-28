package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.mail.FaceMailingList;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.service.MailingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 12, 2007
 * Time: 8:28:56 PM
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditFaceConfig.htm")
public class EditFaceConfig extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    public EditFaceConfig() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/FacesConfigEdit.jsp" );
        setPageTitle( "Edit Face Config" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("faceConfig")
    public FaceConfig formBackingObject( @RequestParam(value = "configId", required = false) Long id ) throws Exception {
        FaceConfig faceConfig = null;
        if ( id != null && id != 0 ) {
            faceConfig = getFacesService().findConfigById( id );
        }
        if ( faceConfig == null ) {
            return new FaceConfig();
        }
        return faceConfig;
    }

    @ModelAttribute("faceMailingList")
    public FaceMailingList getFaceMailingList( @RequestParam(value = "configId", required = false) Long id ) throws Exception {
        FaceConfig faceConfig = null;
        if ( id != null && id != 0 ) {
            faceConfig = getFacesService().findConfigById( id );
        }
        if ( faceConfig != null ) {
            return getFacesService().findFaceMailingList( faceConfig );
        }
        return null;
    }

    @ModelAttribute("mailingLists")
    public List<MailingList> getMailingLists() {
        return mailingListService.findAllMailingLists();
    }

    @RequestMapping( method = {RequestMethod.GET, RequestMethod.POST} )
    public String showFaceConfig( HttpServletRequest request, ModelMap model,
                                  @ModelAttribute("faceConfig") FaceConfig faceConfig ) throws Exception {
        if ( faceConfig.getId() != null ) {
            Collection<FaceFlowInstruction> instructions = getFacesService().findInstructions( faceConfig );
            model.addAttribute( "faceInstructions", instructions );
            model.addAttribute( "logo", getFacesService().findLogo( faceConfig ) );
            model.addAttribute( "addingAvailable", (FlowType.values().length - 1) > instructions.size() );
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping( method = RequestMethod.POST, params = {"save","mailingListId"} )
    protected String onSubmit( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("faceConfig") FaceConfig faceConfig,
                               @RequestParam(value = "mailingListId",required = false, defaultValue = "0") Long mailingListId
    ) throws Exception {
        boolean newObject = faceConfig.getId() == null;

        if ( faceConfig.getId() != null && faceConfig.getId() > 0 ) {
            getFacesService().saveFaceMailingList( faceConfig, mailingListId );
        }
        getFacesService().saveConfig( faceConfig );
        if ( newObject ) return showFaceConfig( request, model, faceConfig );
        return "redirect:/admin/FacesConfigsList.htm";
    }

    @RequestMapping( method = RequestMethod.POST, params = "save" )
    protected String onSubmit( HttpServletRequest request, ModelMap model,
                                     @ModelAttribute("faceConfig") FaceConfig faceConfig ) throws Exception {
        boolean newObject = faceConfig.getId() == null;

        getFacesService().saveConfig( faceConfig );
        if ( newObject ) return showFaceConfig( request, model, faceConfig );
        return "redirect:/admin/FacesConfigsList.htm";
    }
}
