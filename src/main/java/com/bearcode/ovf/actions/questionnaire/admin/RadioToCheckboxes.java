package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Date: 21.06.12
 * Time: 18:35
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/RadioToCheckboxes.htm")
public class RadioToCheckboxes extends BaseController {

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private QuestionnaireService questionnaireService;

    private static final long CHECKBOX_TYPE_ID = 5;

    public RadioToCheckboxes() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/RadioToCheckboxes.jsp" );
        setPageTitle( "Radio to Checkboxes Conversion" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("checkboxes")
    public Collection<QuestionField> defineTransformedCheckboxes( ModelMap model,
                                                                  @RequestParam("fieldId") Long fieldId ) {
        QuestionField field = null;
        if ( fieldId != null ) {
            field = questionFieldService.findQuestionFieldById( fieldId );
        }
        model.addAttribute( "radioField", field );

        Collection<QuestionField> checkboxes = new LinkedList<QuestionField>();
        FieldType type = questionFieldService.findFieldTypeById( CHECKBOX_TYPE_ID );
        if ( field != null && field.getType().isGenericOptionsAllowed() ) {
            int order = field.getOrder();
            for ( FieldDictionaryItem item : field.getGenericOptions() ) {
                QuestionField checkField = new QuestionField();
                checkField.setQuestion( field.getQuestion() );
                checkField.setTitle( item.getViewValue().replaceFirst( "[a-z\\d][\\.\\)] ", "" ) );
                checkField.setInPdfName( field.getInPdfName() + item.getOutputValue() );
                checkField.setOrder( order );
                checkField.setOldOrder( order );
                checkField.setType( type );
                order++;
                checkboxes.add( checkField );
            }
        }
        return checkboxes;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showCheckboxes( HttpServletRequest request, ModelMap model,
                                  @ModelAttribute("radioField") QuestionField field ) {
        if ( field != null ) {
            return buildModelAndView( request, model );
        }
        return "redirect:/admin/QuestionnairePages.htm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doConversion( @ModelAttribute("radioField") QuestionField field,
                                @ModelAttribute("checkboxes") Collection<QuestionField> checkboxes ) {
        if ( checkboxes.size() > 0 ) {
            questionFieldService.deleteQuestionField( field );
            for ( QuestionField checkbox : checkboxes ) {
                questionFieldService.saveQuestionField( checkbox );
            }
            long qId = checkboxes.iterator().next().getQuestion().getId();
            return "redirect:/admin/EditQuestionVariant.htm?id=" + qId;
        }
        if ( field != null ) {
            return "redirect:/admin/EditQuestionVariant.htm?id=" + field.getQuestion().getId();
        }
        return "redirect:/admin/QuestionnairePages.htm";
    }
}
