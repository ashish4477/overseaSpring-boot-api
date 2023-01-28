package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.admin.forms.DependencyGroupForm;
import com.bearcode.ovf.editor.QuestionPropertyEditor;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.service.RelatedService;
import com.bearcode.ovf.utils.UserInfoFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Date: 08.12.11
 * Time: 18:54
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditDependencyGroup.htm")
public class EditDependencyGroup extends BaseController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private RelatedService relatedService;

/*
    @Autowired
    private UserInfoFields userInfoFields;
*/

    @Autowired
    private QuestionPropertyEditor questionPropertyEditor;

    public EditDependencyGroup() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditDependencyGroup.jsp" );
        setPageTitle( "Edit Dependency" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );

    }

    @InitBinder
    protected void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof DependencyGroupForm ) {
            binder.registerCustomEditor( Question.class, questionPropertyEditor );
        }
    }

    /**
     * Build form object web interface works with
     *
     * @param dependentId Id of dependent object (QuestionVariant or PdfFilling)
     * @return Form object
     */
    @ModelAttribute("dependencyGroup")
    public DependencyGroupForm getDependencyGroup( @RequestParam("dependentId") Long dependentId ) {
        DependencyGroupForm form = new DependencyGroupForm();
        form.setDependent( relatedService.findRelated( dependentId ) );
        return form;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String showDependencyGroup( HttpServletRequest request,
                                       ModelMap model,
                                       @ModelAttribute("dependencyGroup") DependencyGroupForm form ) {
        //finalize binding
        form.setDependencies( questionnaireService.findDependenciesOfType( form.getType(), form.getDependent(), form.getDependsOn(), form.getFieldName() ) );
        form.populateConditions();

        List<String> availableChoices = new LinkedList<String>();
        Related dependent = form.getDependent();
        if ( form.getType() != null ) {
            switch ( form.getType() ) {
                case QUESTION:
                    Collection<Question> questions = null;
                    if ( form.getDependsOn() == null ) {
                        if ( dependent instanceof QuestionVariant ) {
                            questions = questionnaireService.findQuestionForDependency( ((QuestionVariant) dependent).getQuestion() );
                        }
                        if ( dependent instanceof PdfFilling ) {
                            questions = questionnaireService.findQuestionForDependency();
                        }
                        for ( Iterator<Question> itq = questions.iterator(); itq.hasNext(); ) {
                            FieldType fieldType = itq.next().getKeyField().getType();
                            if ( !fieldType.isGenericOptionsAllowed() &&
                                    !fieldType.getTemplateName().contains( "checkbox" ) ) {
                                itq.remove();
                            }
                        }
                        model.addAttribute( "questions", questions );
                    } else {
                        QuestionField field = form.getDependsOn().getKeyField();
                        if ( field.getType().isGenericOptionsAllowed() ) {
                            for ( FieldDictionaryItem item : field.getOptions() ) {
                                availableChoices.add( item.getValue() );
                            }
                        } else {
                            availableChoices.add( "true" );
                            availableChoices.add( "false" );
                        }
                    }
                    break;
                case USER:
                    if ( form.getFieldName() == null ) {
                        model.addAttribute( "userFields", UserInfoFields.getInstance().getDependencyFields() );
                    } else {
                        availableChoices.addAll( UserInfoFields.getInstance().getDependencyOptions( form.getFieldName() ) );
                    }
                    break;
                case FACE:
                    for ( FaceConfig face : getFacesService().findAllConfigs() ) {
                        availableChoices.add( face.getName() );
                    }
                    break;
                case FLOW:
                    for ( FlowType flow : FlowType.values() ) {
                        availableChoices.add( flow.name() );
                    }
            }
        }
        if ( dependent instanceof QuestionVariant ) {
            model.addAttribute( "subHeader", "Variant: " + ((QuestionVariant) dependent).getTitle() + " of " + ((QuestionVariant) dependent).getQuestion().getName() );
            model.addAttribute( "goBackUrl", "/admin/EditQuestionVariant.htm" );
        } else if ( dependent instanceof PdfFilling ) {
            model.addAttribute( "subHeader", "Voter Instruction: " + ((PdfFilling) dependent).getName() );
            model.addAttribute( "goBackUrl", "/admin/EditInstruction.htm" );
        }
        Collections.sort( availableChoices );
        model.addAttribute( "choices", availableChoices );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String saveDependenciesGroup( HttpServletRequest request,
                                         ModelMap model,
                                         @ModelAttribute("dependencyGroup") DependencyGroupForm form ) {
        //finalize binding
        form.setDependencies( questionnaireService.findDependenciesOfType( form.getType(), form.getDependent(), form.getDependsOn(), form.getFieldName() ) );
        if ( form.getSelectedCondition() == null ) {
            form.setSelectedCondition( Collections.<String>emptyList() );
        }

        // clear dependencies and selected conditions lists - remove all correspondent values
        // dependencies - is what we have in the DB
        // selected conditions - is what we want to have
        for ( Iterator<BasicDependency> it = form.getDependencies().iterator(); it.hasNext(); ) {
            BasicDependency key = it.next();
            if ( form.getSelectedCondition().contains( key.getConditionName() ) ) {
                form.getSelectedCondition().remove( key.getConditionName() );
                it.remove();
            }
        }
        // now dependencies list contains keys to delete
        // and selected list contains keys to create
        if ( !form.getDependencies().isEmpty() ) {
            questionnaireService.deleteDependencies( form.getDependencies() );
        }
        for ( String condition : form.getSelectedCondition() ) {
            BasicDependency dependency = null;
            switch ( form.getType() ) {
                case QUESTION:
                    QuestionField field = form.getDependsOn().getKeyField();

                    if ( field.getType().isGenericOptionsAllowed() ) {
                        FieldDictionaryItem selectedItem = null;
                        for ( FieldDictionaryItem item : form.getDependsOn().getKeyField().getOptions() ) {
                            if ( item.getValue().equals( condition ) ) {
                                selectedItem = item;
                                break;
                            }
                        }
                        if ( selectedItem != null ) {
                            dependency = new QuestionDependency( form.getDependent(), form.getDependsOn(), selectedItem );
                        }
                    } else {
                        dependency = new QuestionCheckboxDependency( form.getDependent(), form.getDependsOn(), condition );
                    }
                    break;
                case USER:
                    dependency = new UserFieldDependency( form.getDependent(), form.getFieldName(), condition );
                    break;
                case FLOW:
                    try {
                        dependency = new FlowDependency( form.getDependent(), FlowType.valueOf( condition ) );
                    } catch ( IllegalArgumentException e ) {
                        logger.error( "Trying to create FlowDependency for undefined flow name" );
                    }
                    break;
                case FACE:
                    FaceConfig selected = null;
                    for ( FaceConfig face : getFacesService().findAllConfigs() ) {
                        if ( face.getName().equals( condition ) ) {
                            selected = face;
                            break;
                        }
                    }
                    if ( selected != null ) {
                        dependency = new FaceDependency( form.getDependent(), selected );
                    }
                    break;
            }
            if ( dependency != null ) {
                questionnaireService.saveDependency( dependency );
            }
        }

        if ( form.getDependent() instanceof PdfFilling ) {
            return "redirect:/admin/EditInstruction.htm?id=" + form.getDependent().getId();
        }
        return "redirect:/admin/EditQuestionVariant.htm?id=" + form.getDependent().getId();
    }

}
