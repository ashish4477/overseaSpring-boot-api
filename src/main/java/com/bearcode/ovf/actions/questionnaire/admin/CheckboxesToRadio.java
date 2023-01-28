package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.apache.commons.lang.StringUtils;
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
 * Date: 15.06.12
 * Time: 22:14
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/CheckboxesToRadio.htm")
public class CheckboxesToRadio extends BaseController {
    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private QuestionnaireService questionnaireService;

    private static final long RADIO_TYPE_ID = 3;

    public CheckboxesToRadio() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/CheckboxesToRadio.jsp" );
        setPageTitle( "Checkboxes to Radio Conversion" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("fieldsToTransform")
    public Collection<QuestionField> defineTransformedFields( HttpServletRequest request,
                                                              ModelMap model,
                                                              @RequestParam(value = "variantId") Long variantId ) {
        QuestionVariant variant = questionnaireService.findQuestionVariantById( variantId );
        model.addAttribute( "selectedVariant", variant );
        Collection<QuestionField> fields = new LinkedList<QuestionField>();
        String prefix = null;
        for ( QuestionField field : variant.getFields() ) {
            if ( field.getType().getTemplateName().equals( FieldType.TEMPLATE_CHECKBOX ) ) {
                if ( fields.size() == 0 ) {
                    fields.add( field );
                    prefix = field.getInPdfName();
                    continue;
                } else {
                    if ( field.getInPdfName().startsWith( prefix ) ) {
                        fields.add( field );
                        continue;
                    } else {
                        String pretence = StringUtils.getCommonPrefix( new String[]{prefix, field.getInPdfName()} );
                        if ( pretence.length() > 2 ) {
                            fields.add( field );
                            prefix = pretence;
                            continue;
                        }
                    }
                }
            }
            if ( fields.size() > 1 ) {
                break;
            } else {
                fields.clear();
            }
        }
        model.addAttribute( "variablePrefix", prefix );
        return fields;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showFieldsToTransform( HttpServletRequest request, ModelMap model,
                                         @ModelAttribute("selectedVariant") QuestionVariant variant ) {
        if ( questionnaireService.checkUsingInDependencies( variant.getQuestion() ) ) {
            return "redirect:/admin/EditQuestionVariant.htm?id=" + variant.getId();
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doTransformation( HttpServletRequest request, ModelMap model,
                                    @ModelAttribute("selectedVariant") QuestionVariant variant,
                                    @ModelAttribute("fieldsToTransform") Collection<QuestionField> fields,
                                    @ModelAttribute("variablePrefix") String prefix ) {
        if ( fields.size() > 1 ) {
            QuestionField firstQuestion = fields.iterator().next();
            FieldType type = questionFieldService.findFieldTypeById( RADIO_TYPE_ID );
            firstQuestion.setType( type );   // new type
            char orderChar = 'a';
            for ( QuestionField field : fields ) {      // create options
                FieldDictionaryItem item = type.createGenericItem();
                StringBuffer itemValue = new StringBuffer();
                itemValue.append( orderChar++ ).append( ") " )
                        .append( field.getTitle() )
                        .append( "=" )
                        .append( StringUtils.difference( prefix, field.getInPdfName() ) );
                item.setValue( itemValue.toString() );
                item.setForField( firstQuestion );
                firstQuestion.getGenericOptions().add( item );
            }
            firstQuestion.setTitle( "Please select from the following options" );  // new title
            firstQuestion.setInPdfName( prefix );                   // new variable name
            firstQuestion.setOldOrder( firstQuestion.getOrder() );  // don't move the question field

            questionFieldService.saveQuestionField( firstQuestion );  // save first
            for ( QuestionField field : fields ) {                    // and delete other
                if ( field.getId() != firstQuestion.getId() ) {
                    questionFieldService.deleteQuestionField( field );
                }
            }
            return "redirect:/admin/EditQuestionField.htm?fieldId=" + firstQuestion.getId();
        }

        return "redirect:/admin/EditQuestionVariant.htm?id=" + variant.getId();
    }

}
