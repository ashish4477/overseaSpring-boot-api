package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
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
import java.util.*;

/**
 * @author Alexey Polyakov
 *         Date: Aug 15, 2007
 *         Time: 5:45:41 PM
 */
@Controller
@RequestMapping("/admin/EditQuestionVariant.htm")
public class EditQuestionVariant extends BaseController {

    /**
     * the error message produced when the question variant hierarchy cannot be deleted because it has dependents.
     */
    static final String REJECT_DEPENDENTS_ERROR_MESSAGE = "The question variant belongs to a question that has dependents. Please delete the dependents first.";

    /**
     * the error code produced when the question variant hierarchy cannot be deleted because it has dependents.
     */
    static final String REJECT_DEPENDENTS_ERROR_CODE = "rava.admin.question_variant.question_variant_has_dependents";

    /**
     * the default section CSS.
     */
    static final String DEFAULT_SECTION_CSS = "/css/admin.css";

    /**
     * the default section name.
     */
    static final String DEFAULT_SECTION_NAME = "admin";

    /**
     * the default page title.
     */
    static final String DEFAULT_PAGE_TITLE = "Edit Question Variant";

    /**
     * the default content block.
     */
    static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/admin/EditQuestionVariant.jsp";

    /**
     * redirection to the admin edit question page.
     */
    static final String REDIRECT_ADMIN_EDIT_QUESTION = "redirect:/admin/EditQuestionGroup.htm";

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    public EditQuestionVariant() {
        setContentBlock( DEFAULT_CONTENT_BLOCK );
        setPageTitle( DEFAULT_PAGE_TITLE );
        setSectionName( DEFAULT_SECTION_NAME );
        setSectionCss( DEFAULT_SECTION_CSS );
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof QuestionVariant ) {
            binder.setValidator( validator );
        }
    }

    public void setQuestionnaireService( QuestionnaireService questionnaireService ) {
        this.questionnaireService = questionnaireService;
    }

    public void setValidator( Validator validator ) {
        this.validator = validator;
    }


    public void setQuestionFieldService( QuestionFieldService questionFieldService ) {
        this.questionFieldService = questionFieldService;
    }

    @ModelAttribute("variant")
    public QuestionVariant formBackingObject( @RequestParam(value = "id", required = false) Long id,
                                              @RequestParam(value = "questionId", required = false) Long questionId ) {
        if ( id != null && id != 0 ) {
            return questionnaireService.findQuestionVariantById( id );
        } else if ( questionId != null && questionId != 0 ) {
            QuestionVariant questionVariant = new QuestionVariant();
            questionVariant.setQuestion( questionnaireService.findQuestionById( questionId ) );
            return questionVariant;
        }
        return null;
    }

    @ModelAttribute("questionsToMove")
    public Collection<Question> getQuestionsToMove( @RequestParam(value = "id", required = false) Long id ) {
        QuestionVariant variant = null;
        if ( id != null && id != 0 ) {
            variant = questionnaireService.findQuestionVariantById( id );
        }
        if ( variant != null && variant.getId() != 0 && variant.getQuestion() != null) {
            return questionnaireService.findQuestionsOfPageType( variant.getQuestion().getPage().getType() );
        }
        return Collections.emptyList();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showVariant(HttpServletRequest request, ModelMap model, @ModelAttribute("variant") QuestionVariant variant) {
        if ( variant == null ) {
            //it's possible if admin click on "Edit question" button during FWAB on "Choose candidate" page (CandidatePageAddon)
            return EditQuestionnairePage.REDIRECT_ADMIN_QUESTIONNAIRE_PAGES;
        }
        if ( variant.getId() != 0 ) {
            final Question question = variant.getQuestion();
            final boolean keyQuestion = questionnaireService.checkUsingInDependencies( question );
            model.addAttribute( "keyQuestion", keyQuestion );
            if ( keyQuestion ) {
                final Collection<QuestionVariant> dependentVariants = new ArrayList<QuestionVariant>();
                final Collection<PdfFilling> dependentInstructions = new ArrayList<PdfFilling>();
                for ( BasicDependency dependency : questionnaireService.findDependents( question ) ) {
                    if ( dependency.getDependent() instanceof PdfFilling ) {
                        dependentInstructions.add( (PdfFilling) dependency.getDependent() );
                    }
                    if ( dependency.getDependent() instanceof QuestionVariant ) {
                        dependentVariants.add( (QuestionVariant) dependency.getDependent() );
                    }
                }
                model.addAttribute( "dependentVariants", dependentVariants );
                model.addAttribute( "dependentInstructions", dependentInstructions );
            }
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String saveVariant( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("variant") @Valid QuestionVariant variant,
                               BindingResult errors ) {
        if ( !errors.hasErrors() ) {
            boolean newVariant = variant.getId() == 0;
            questionnaireService.saveQuestionVariant( variant );
            if ( !newVariant ) {
                return REDIRECT_ADMIN_EDIT_QUESTION + "?id=" + variant.getQuestion().getId();
            }
        }
        return showVariant( request, model, variant );
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete")
    public String deleteVariant( HttpServletRequest request, ModelMap model,
                                 @ModelAttribute("variant") @Valid QuestionVariant variant,
                                 BindingResult errors ) {
        final Collection<QuestionField> fields = variant.getFields();
        if ( fields == null || fields.isEmpty() ) {
            questionnaireService.deleteQuestionVariant( variant );
        } else {
            errors.reject( "rava.admin.variant.fields_not_empty", "List of fields isn't empty. Please, delete all fields first." );
            return showVariant( request, model, variant );
        }

        return REDIRECT_ADMIN_EDIT_QUESTION + "?id=" + variant.getQuestion().getId();
    }

    @RequestMapping(method = RequestMethod.POST, params = "deleteHierarchy")
    public String deleteVariantHierarchy( HttpServletRequest request, ModelMap model,
                                          @ModelAttribute("variant") @Valid QuestionVariant variant, BindingResult errors ) {
        try {
            questionnaireService.deleteQuestionVariantHierarchy( variant );
            return REDIRECT_ADMIN_EDIT_QUESTION + "?id=" + variant.getQuestion().getId();

        } catch ( final IllegalStateException e ) {
            errors.reject( REJECT_DEPENDENTS_ERROR_CODE, REJECT_DEPENDENTS_ERROR_MESSAGE );
            return showVariant( request, model, variant );
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "move")
    public String moveQuestionVariant( HttpServletRequest request, ModelMap model,
                                       @ModelAttribute("variant") @Valid QuestionVariant variant,
                                       BindingResult errors,
                                       @RequestParam("newQuestionId") Long newQuestionId ) {
        if ( errors.hasErrors() ) {
            return showVariant( request, model, variant );
        }
        Question destinationQuestion = questionnaireService.findQuestionById( newQuestionId );
        if ( destinationQuestion == null ) {
            return showVariant( request, model, variant );
        }
        variant.setQuestion( destinationQuestion );
        questionnaireService.saveQuestionVariant( variant );
        return REDIRECT_ADMIN_EDIT_QUESTION + "?id=" + variant.getQuestion().getId();
    }



    @RequestMapping(method = RequestMethod.GET, params = "cloneVariant")
    public String cloneVariant( HttpServletRequest request, ModelMap model,
                                @ModelAttribute("variant") QuestionVariant variant ) {
        final QuestionVariant newVariant = questionnaireService.cloneVariant( variant );
        final Collection<QuestionField> newFields = new ArrayList<QuestionField>( newVariant.getFields() );
        questionnaireService.saveQuestionVariant( newVariant );
        questionnaireService.refresh( newVariant );
        for ( final QuestionField field : newFields ) {
            questionFieldService.saveQuestionField( field );
        }
        questionnaireService.refresh( newVariant );
        model.put( "variant", newVariant );
        return showVariant( request, model, newVariant );
    }

}
