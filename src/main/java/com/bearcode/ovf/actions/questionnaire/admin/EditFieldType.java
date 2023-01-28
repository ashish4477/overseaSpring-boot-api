package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.FieldTypeSingleValue;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.validators.FieldTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Alexey Polyakov
 *         Date: Aug 13, 2007
 *         Time: 7:57:30 PM
 */
@Controller
@RequestMapping("/admin/EditFieldType.htm")
public class EditFieldType extends BaseController {

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private FieldTypeValidator validator;

    public EditFieldType() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditFieldType.jsp" );
        setPageTitle( "Edit field type" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof FieldType ) {
            binder.setValidator( validator );
        }
    }

    @ModelAttribute("fieldType")
    public FieldType formBackingObject( @RequestParam("id") Long id,
                                        @RequestParam(value = "type", required = false, defaultValue = "") String type ) {
        if ( id != null && id != 0 ) {
            return questionFieldService.findFieldTypeById( id );
        } else if ( type.equalsIgnoreCase( "mail-in" ) ) {
            FieldType fieldType = new FieldTypeSingleValue();
            fieldType.setTemplateName( FieldType.TEMPLATE_CHECKBOX_FILLED );
            fieldType.setName( "new Mail-In checkbox" );
            fieldType.setGenericOptionsAllowed( false );
            fieldType.setVerificationPatternApplicable( false );
            return fieldType;
        }
        return null;
    }

    @ModelAttribute("templateNames")
    public List<String> getTemplateNames() {
        return FieldType.getTemplateNames();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit( HttpServletRequest request, ModelMap modelMap,
                            @ModelAttribute("fieldType") @Valid FieldType fieldType,
                            BindingResult errors ) throws Exception {
        if ( !errors.hasErrors() ) {
            questionFieldService.saveFieldType( fieldType );
        }
        return buildModelAndView( request, modelMap );
    }

    @RequestMapping(method = RequestMethod.GET)
    public String buildReferences( HttpServletRequest request, ModelMap model,
                                   @ModelAttribute("fieldType") FieldType fieldType ) {
        if ( fieldType == null ) {
            return "redirect:/admin/FieldTypes.htm";
        }
        return buildModelAndView( request, model );
    }

}
