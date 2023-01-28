package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.AdminInstructionsForm;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.questionnaire.FlowType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 09.07.11
 * Time: 19:41
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditFaceFlowInstruction.htm")
public class EditFaceInstruction extends BaseController {

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    public EditFaceInstruction() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/FaceInstructionEdit.jsp" );
    }

    @InitBinder
    protected void initBinder( DataBinder binder ) {
        binder.setValidator( validator );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showFaceFlowInstruction(
            @ModelAttribute("instruction") AdminInstructionsForm instruction,
            HttpServletRequest request,
            ModelMap model ) {
        instruction.textToShow();
        List<String> types = new LinkedList<String>();
        Collection<FaceFlowInstruction> instructions = facesService.findInstructions( instruction.getFaceConfig() );
        Collection<String> usedTypes = new LinkedList<String>();
        for ( FaceFlowInstruction ins : instructions ) {
            usedTypes.add( ins.getFlowTypeName() );
        }
        for ( FlowType type : FlowType.values() ) {
            if (!usedTypes.contains( type.toString() ) )
                types.add( type.toString() );
        }
        model.addAttribute( "types", types );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateInstruction(
            HttpServletRequest request,
            @ModelAttribute("instruction") @Valid AdminInstructionsForm instruction,
            BindingResult errors,
            ModelMap model
    ) {
        if ( !errors.hasErrors() ) {
            instruction.textToSave();
            getFacesService().saveInstruction( instruction.getInstruction(), getUser() );
        }
        return buildModelAndView( request, model );
    }

    @ModelAttribute("instruction")
    public AdminInstructionsForm getInstruction( @RequestParam(value = "id", required = false) Long id,
                                                 @RequestParam(value = "configId", required = false) Long configId ) {
        FaceFlowInstruction instruction = null;
        if ( id != null && id != 0 ) {
            instruction = getFacesService().findInstructionById( id );
        } else {
            instruction = new FaceFlowInstruction();
            if ( configId != null ) {
                FaceConfig config = getFacesService().findConfigById( configId );
                instruction.setFaceConfig( config );
            }
        }
        return new AdminInstructionsForm( instruction );
    }
}
