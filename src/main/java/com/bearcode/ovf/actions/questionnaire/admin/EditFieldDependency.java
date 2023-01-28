package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.QuestionPropertyEditor;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 24, 2007
 * Time: 8:19:49 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditFieldDependency.htm")
public class EditFieldDependency extends BaseController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    @Autowired
    private QuestionPropertyEditor questionPropertyEditor;

    public EditFieldDependency() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditFieldDependency.jsp" );
        setPageTitle( "Edit Field Dependency" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @InitBinder
    protected void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof FieldDependency ) {
            binder.setValidator( validator );
            binder.registerCustomEditor( Question.class, questionPropertyEditor );
        }
    }

    @ModelAttribute("dependency")
    public FieldDependency formBackingObject( @RequestParam(value = "dependencyId", required = false) Long dependencyId,
                                              @RequestParam(value = "fieldId", required = false) Long fieldId ) {
        FieldDependency dependency = null;
        if ( dependencyId == null || dependencyId == 0 ) {
            dependency = new FieldDependency();
            if ( fieldId != null && fieldId != 0 ) {
                dependency.setDependent( questionFieldService.findQuestionFieldById( fieldId ) );
            }
        } else {
            dependency = questionFieldService.findFieldDependencyById( dependencyId );
        }
        return dependency;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String handleGet( HttpServletRequest request,
                             ModelMap model,
                             @ModelAttribute("dependency") FieldDependency dependency ) {
        QuestionField dependent = dependency.getDependent();
        Collection<Question> questions = questionnaireService.findQuestionForDependency( dependent.getQuestion().getQuestion() );

        if ( questions == null ) {
            questions = new LinkedList<Question>();
        }
        for ( Iterator<Question> itQuest = questions.iterator(); itQuest.hasNext(); ) {
            QuestionField field = itQuest.next().getKeyField();
            if ( field != null
                    && (field.getType() instanceof FieldTypeFixedDictionary
                    || field.getType() instanceof FieldTypeGenericDictionary) ) {
                continue;
            }
            itQuest.remove();
        }
        model.addAttribute( "questions", questions );
        if ( dependency.getDependsOn() != null ) {
            QuestionField field = dependency.getDependsOn().getKeyField();
            if ( field != null ) {
                model.addAttribute( "conditions", field.getOptions() );
            }
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String saveDependency( HttpServletRequest request,
                                  ModelMap model,
                                  @ModelAttribute("dependency") @Valid FieldDependency dependency,
                                  BindingResult errors ) {
        if ( !errors.hasErrors() ) {
            questionFieldService.saveDependency( dependency );
            return "redirect:/admin/EditQuestionField.htm?fieldId=" + dependency.getDependent().getId();
        }
        return handleGet( request, model, dependency );
    }


    @RequestMapping(method = RequestMethod.POST, params = "delete")
    protected String deleteDependency( @ModelAttribute("dependency") FieldDependency dependency ) {
        questionFieldService.deleteDependency( dependency );
        return "redirect:/admin/EditQuestionField.htm?fieldId=" + dependency.getDependent().getId();
    }
}
