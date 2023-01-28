package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.FieldTypePropertyEditor;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 14, 2007
 * Time: 4:06:29 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditQuestionField.htm")
public class EditQuestionField extends BaseController {

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    @Autowired
    private FieldTypePropertyEditor fieldTypeEditor;

    public EditQuestionField() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditQuestionField.jsp" );
        setPageTitle( "Edit Question" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("field")
    public QuestionField formBackingObject( @RequestParam(value = "fieldId", required = false) Long fieldId,
                                            @RequestParam(value = "variantId", required = false) Long variantId ) {
        if ( fieldId != null && fieldId > 0 ) {
            QuestionField field = questionFieldService.findQuestionFieldById( fieldId );
            if ( field != null ) {
                Hibernate.initialize( field.getQuestion().getFields() );
            }
            return field;
        } else {
            if ( variantId != null && variantId > 0 ) {
                QuestionVariant variant = questionnaireService.findQuestionVariantById( variantId );
                QuestionField field = new QuestionField();
                field.setQuestion( variant );
                return field;
            }
        }
        return null;
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof QuestionField ) {
            binder.setValidator( validator );
            binder.registerCustomEditor( FieldType.class, fieldTypeEditor );
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String showQuestionField( HttpServletRequest request, ModelMap model,
                                     @ModelAttribute("field") QuestionField field ) {
        List<FieldType> fieldTypes = questionFieldService.findFieldTypes();
        boolean usedInDependencies = false;
        if ( field.getType() != null ) {
            usedInDependencies = questionFieldService.checkUsingInDependecies( field );
            if ( usedInDependencies && field.getType().isGenericOptionsAllowed() ) {
                for ( Iterator<FieldType> itType = fieldTypes.iterator(); itType.hasNext(); ) {
                    if ( itType.next().isGenericOptionsAllowed() ) continue;
                    itType.remove();
                }
            }
        }
        model.addAttribute( "fieldTypes", fieldTypes );
        model.addAttribute( "usedInDependecies", usedInDependencies );
        if ( field.getType() != null && field.getType().isDependenciesAllowed() ) {
            model.addAttribute( "dependency", questionFieldService.findFieldDependencyForField( field ) );
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String saveQuestionField( HttpServletRequest request, ModelMap model,
                                     @ModelAttribute("field") @Valid QuestionField field,
                                     BindingResult errors ) {
        if ( !errors.hasErrors() ) {
            if ( questionFieldService.checkUsingInDependecies( field )
                    && !(!field.getGenericOptions().isEmpty()
                    && field.getType().isGenericOptionsAllowed()) ) {
                // if field is used in dependencies and it already has some generic options
                // field type should support generic options feature.
                // so it prevent changing field type from Generic Options type to something else
                errors.rejectValue( "type", "rava.admin.field.cant_change_type", "This question is used in dependecies. You can't change its type." );
            }
            if ( field.getType().isGenericOptionsAllowed() ) {
                updateGenericOptions( field, request, errors );
            }
        }
        if ( !errors.hasErrors() ) {
            boolean newField = field.getId() == 0;
            questionFieldService.saveQuestionField( field );
            if ( !newField ) {
                return "redirect:/admin/EditQuestionVariant.htm?id=" + field.getQuestion().getId();
            }
        }
        return showQuestionField( request, model, field );
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete")
    public String deleteQuestionField( HttpServletRequest request, ModelMap model,
                                       @ModelAttribute("field") QuestionField field,
                                       BindingResult errors ) {

        if ( questionFieldService.checkUsingInDependecies( field ) ) {
            errors.reject( "", "Field can't be deleted" );
        }
        if ( !errors.hasErrors() ) {
            questionFieldService.deleteQuestionField( field );
            return "redirect:/admin/EditQuestionVariant.htm?id=" + field.getQuestion().getId();
        }
        return showQuestionField( request, model, field );
    }

    private void updateGenericOptions( QuestionField field, HttpServletRequest request, BindingResult errors ) {
        String[] entered = MapUtils.getStrings( request.getParameterMap(), "optionIds", null );
        if ( entered != null && field.getGenericOptions() != null ) {
            List<String> existingId = new LinkedList<String>( Arrays.asList( entered ) );
            for ( Iterator<FieldDictionaryItem> itItem = field.getGenericOptions().iterator(); itItem.hasNext(); ) {
                FieldDictionaryItem item = itItem.next();
                String id = String.valueOf( item.getId() );
                if ( existingId.contains( id ) ) {
                    String value = MapUtils.getString( request.getParameterMap(), "gen" + id, "" ).trim();
                    if ( value.length() == 0 ) {
                        //delete item
                        if ( questionFieldService.checkUsingInDependecies( item ) ) {
                            // error
                            errors.rejectValue( "genericOptions", "rava.admin.field.cant_delete_option", "Option is used in dependency and therefore cann't be deleted" );
                        } else {
                            itItem.remove();
                            item.setForField( null );
                        }
                    } else {
                        item.setValue( value );
                    }
                }
            }
        }
        String[] addedOptions = MapUtils.getStrings( request.getParameterMap(), "genOptions", null );
        if ( addedOptions != null ) {
            for ( String newItem : addedOptions ) {
                FieldDictionaryItem added = field.getType().createGenericItem();
                if ( added != null && newItem.trim().length() > 0 ) {
                    added.setForField( field );
                    added.setValue( newItem );
                    field.getGenericOptions().add( added );
                }
            }
        }
    }

}
