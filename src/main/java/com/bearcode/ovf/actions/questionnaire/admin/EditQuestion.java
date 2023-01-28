package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.questionnaire.*;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexey Polyakov
 *         Date: Aug 14, 2007
 *         Time: 5:33:15 PM
 *         <p/>
 *         object name - Question
 *         name on view - Question Group
 */
@Controller
@RequestMapping("/admin/EditQuestionGroup.htm")
public class EditQuestion extends BaseController {

    /**
     * the error message produced when the question hierarchy cannot be deleted because it has dependents.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String REJECT_DEPENDENTS_ERROR_MESSAGE = "The question has dependents. Please delete the dependents first.";

    /**
     * the error code produced when the question hierarchy cannot be deleted because it has dependents.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String REJECT_DEPENDENTS_ERROR_CODE = "rava.admin.question.question_has_dependents";

    /**
     * the default section CSS.
     *
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    static final String DEFAULT_SECTION_CSS = "/css/admin.css";

    /**
     * the default section name.
     *
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    static final String DEFAULT_SECTION_NAME = "admin";

    /**
     * the default page title.
     *
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    static final String DEFAULT_PAGE_TITLE = "Edit Question Group";

    /**
     * the default content block.
     *
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/admin/EditQuestion.jsp";

    /**
     * redirection to the admin edit questionnaire page.
     *
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    static final String REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE = "redirect:/admin/EditQuestionnairePage.htm";

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    public EditQuestion() {
        setContentBlock( DEFAULT_CONTENT_BLOCK );
        setPageTitle( DEFAULT_PAGE_TITLE );
        setSectionName( DEFAULT_SECTION_NAME );
        setSectionCss( DEFAULT_SECTION_CSS );
    }

    @ModelAttribute("question")
    public Question formBackingObject( @RequestParam(value = "id", required = false) Long id,
                                       @RequestParam(value = "pageId", required = false) Long pageId ) {
        if ( id != null && id != 0 ) {
            return questionnaireService.findQuestionById( id );
        } else if ( pageId != null && pageId != 0 ) {
            Question question = new Question();
            question.setPage( questionnaireService.findPageById( pageId ) );
            return question;
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showQuestion( HttpServletRequest request, ModelMap model,
                                @ModelAttribute("question") Question question ) {
        if ( question == null ) {
            //it's possible if admin click on "Edit question" button during FWAB on "Choose candidate" page (CandidatePageAddon)
            return EditQuestionnairePage.REDIRECT_ADMIN_QUESTIONNAIRE_PAGES;
        }
        if ( question.getId() != 0 ) {
            boolean hasDependents = questionnaireService.checkUsingInDependencies( question );
            model.addAttribute( "keyQuestion", hasDependents );
            if ( hasDependents ) {
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
            definePagesForMoving( model, question );
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String saveQuestion( HttpServletRequest request, ModelMap model,
                                @ModelAttribute("question") @Valid Question question,
                                BindingResult errors ) {
        if ( !errors.hasErrors() ) {
            boolean newQuestion = question.getId() == 0;

            questionnaireService.saveQuestion( question );
            if ( !newQuestion ) {
                return REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + question.getPage().getId();
            }
        }
        return showQuestion( request, model, question );
    }

    @RequestMapping(method = RequestMethod.POST, params = "move")
    public String moveQuestion( HttpServletRequest request, ModelMap model,
                                @ModelAttribute("question") @Valid Question question,
                                BindingResult errors,
                                @RequestParam("newPageId") Long newPageId ) {
        if ( errors.hasErrors() ) {
            return showQuestion( request, model, question );
        }
        QuestionnairePage destinationPage = questionnaireService.findPageById( newPageId );
        int lastNumber = 1;
        final List<Question> destinationQuestions = destinationPage.getQuestions();
        if ( destinationQuestions != null && destinationQuestions.size() > 0 ) {
            Question lastQuestion = destinationQuestions.get( destinationQuestions.size() - 1 );
            lastNumber = lastQuestion.getOrder();
        }
        question.setPage( destinationPage );
        question.setOrder( lastNumber + 1 );
        question.setOldOrder( lastNumber + 1 );
        questionnaireService.saveQuestion( question );

        return REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + newPageId;
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete")
    public String deleteQuestion( HttpServletRequest request, ModelMap model,
                                  @ModelAttribute("question") Question question, BindingResult errors ) {

        if ( question.getId() != 0 && (question.getVariants() == null || question.getVariants().isEmpty()) ) {
            if ( !questionnaireService.checkUsingInDependencies( question ) ) {
                questionnaireService.deleteQuestion( question );
            } else {
                errors.reject( REJECT_DEPENDENTS_ERROR_CODE, REJECT_DEPENDENTS_ERROR_MESSAGE );
                return showQuestion( request, model, question );
            }
        } else {
            errors.reject( "rava.admin.question.variants_not_empty", "List of variants isn't empty. Please, delete all variants of the question first." );
            return showQuestion( request, model, question );
        }

        return REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + question.getPage().getId();
    }

    /**
     * Deletes the question and its hierarchy.
     *
     * @param request  the request.
     * @param model    the model.
     * @param question the question to delete.
     * @param errors   the resulting errors (if any).
     * @return the response.
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    @RequestMapping(method = RequestMethod.POST, params = "deleteHierarchy")
    public String deleteQuestionHierarchy( final HttpServletRequest request, final ModelMap model,
                                           @ModelAttribute("question") final Question question, final BindingResult errors ) {
        try {
            questionnaireService.deleteQuestionHierarchy( question );
            return REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + question.getPage().getId();

        } catch ( final IllegalStateException e ) {
            errors.reject( REJECT_DEPENDENTS_ERROR_CODE, REJECT_DEPENDENTS_ERROR_MESSAGE );
            return showQuestion( request, model, question );
        }
    }

    protected void definePagesForMoving( ModelMap model, Question question ) {
        QuestionnairePage currentPage = question.getPage();
        List<QuestionnairePage> pages = questionnaireService.findQuestionnairePages( currentPage.getType() );

        if ( pages.size() > 0 ) {
            int minNumber = pages.get( 0 ).getNumber() - 1;
            int maxNumber = pages.get( pages.size() - 1 ).getNumber() + 1;


            // find biggest number of page that contains question this question depends on
            if ( currentPage.getId() != 0 ) {
                //for ( Question question : currentPage.getQuestions() ) {
                if ( question.getVariants() != null ) {
                    for ( QuestionVariant questionVariant : question.getVariants() ) {
                        if ( questionVariant.getKeys() != null ) {
                            for ( BasicDependency dependency : questionVariant.getKeys() ) {
                                if ( dependency instanceof QuestionDependency && ((QuestionDependency) dependency).getDependsOn().getPage().getNumber() > minNumber ) {
                                    minNumber = ((QuestionDependency) dependency).getDependsOn().getPage().getNumber();
                                }
                            }
                        }
                    }
                }
                //}
            }
            // find smallest number of page contains question depends on this page
            for ( QuestionnairePage p : pages ) {
                if ( currentPage.getNumber() > p.getNumber() ) continue;
                for ( Question farQuestion : p.getQuestions() ) {
                    if ( farQuestion.getVariants() != null ) {
                        for ( QuestionVariant variant : farQuestion.getVariants() ) {
                            if ( variant.getKeys() != null ) {
                                for ( BasicDependency dependency : variant.getKeys() ) {

                                    if ( dependency instanceof QuestionDependency && ((QuestionDependency) dependency).getDependsOn().getId() == question.getId() ) {
                                        if ( p.getNumber() < maxNumber ) maxNumber = p.getNumber();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for ( Iterator<QuestionnairePage> iterator = pages.iterator(); iterator.hasNext(); ) {
                QuestionnairePage p = iterator.next();
                if ( p.getNumber() < minNumber || p.getNumber() >= maxNumber ) {
                    iterator.remove();
                }
            }

            model.addAttribute( "minNumber", minNumber );
            model.addAttribute( "maxNumber", maxNumber );
        }
        model.addAttribute( "pages", pages );

    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof Question ) {
            binder.setValidator( validator );
        }
    }

    /**
     * Gets the questionnaire service.
     *
     * @return the questionnaire service.
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    /**
     * Sets the questionnaire service.
     *
     * @param questionnaireService the questionnaire service to set.
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    public void setQuestionnaireService( QuestionnaireService questionnaireService ) {
        this.questionnaireService = questionnaireService;
    }

    /**
     * Gets the validator.
     *
     * @return the validator.
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Sets the validator.
     *
     * @param validator the validator to set.
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    public void setValidator( Validator validator ) {
        this.validator = validator;
    }
}
