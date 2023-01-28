package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.questionnaire.PdfFilling;
import com.bearcode.ovf.model.questionnaire.Related;
import com.bearcode.ovf.service.RelatedService;
import com.bearcode.ovf.validators.PdfFillingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 18, 2007
 * Time: 7:12:13 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditInstruction.htm")
public class EditPdfFilling extends BaseController {

    @Autowired
    private RelatedService relatedService;

    @Autowired
    private PdfFillingValidator fillingValidator;

    public EditPdfFilling() {
        setSectionCss( "/css/admin.css" );
        setSectionName( "admin" );
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditPdfFilling.jsp" );
        setPageTitle( "Edit Voter Instruction" );
    }

    public void setRelatedService( RelatedService relatedService ) {
        this.relatedService = relatedService;
    }

    @ModelAttribute("filling")
    public PdfFilling formBackingObject( @RequestParam(value = "id", required = false, defaultValue = "0") Long id ) throws Exception {
        if ( id > 0 ) {
            Related filling = relatedService.findRelated( id );
            if ( filling != null && filling instanceof PdfFilling )
                return (PdfFilling) filling;
        }
        return new PdfFilling();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showPage( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("filling") @Valid PdfFilling filling,
                               BindingResult errors ) {
        boolean newbie = filling.getId() == 0;
        if ( !errors.hasErrors() && request.getParameterMap().containsKey( "save" ) ) {
            relatedService.savePdfFilling( filling );
        }
        if ( request.getParameterMap().containsKey( "delete" ) ) {
            relatedService.deletePdfFilling( filling );
        }
        else if ( newbie || errors.hasErrors() ) {
            return showPage( request, model );
        }
        return "redirect:/admin/InstructionsList.htm";
    }

    @InitBinder
    protected void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof PdfFilling ) {
            binder.setValidator( fillingValidator );
        }
    }
}
