package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.admin.forms.QuestionnaireViewForm;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.DependentRoot;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Date: 24.11.11
 * Time: 14:56
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/QuestionnaireView.htm")
@SessionAttributes("viewForm")
public class QuestionnaireView extends BaseController {

    @Autowired
    private QuestionnaireService questionnaireService;

    public QuestionnaireView() {
        mainTemplate = "templates/ContentOnlyTemplate";
        setContentBlock( "/WEB-INF/pages/blocks/admin/QuestionnaireView.jsp" );
        setPageTitle( "Questionnaire Overview" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("faceConfigs")
    public Collection<FaceConfig> getFaces() {
        return getFacesService().findAllConfigs();
    }

    @ModelAttribute("pages")
    public Collection<QuestionnairePage> getQuestionnaire( ModelMap model ) {
        QuestionnaireViewForm viewForm = new QuestionnaireViewForm( getFacesService().findDefaultConfig() );
        model.addAttribute( "viewForm", viewForm );
        Collection<QuestionnairePage> pages = questionnaireService.findQuestionnairePages( viewForm.getFormType() );
        WizardContext form = viewForm.getWizardContext();
        form.setCurrentFace( getFacesService().findConfigById( viewForm.getFaceConfigId() ) );
        HashMap<Long, Collection<QuestionVariant>> dependencies = new HashMap<Long, Collection<QuestionVariant>>();
        for ( Iterator<QuestionnairePage> pageIt = pages.iterator(); pageIt.hasNext(); ) {
            QuestionnairePage page = pageIt.next();
            adjustPage( page, form, pages, dependencies );

            if ( page instanceof AddOnPage || page instanceof ExternalPage ) {
                continue;  // do not remove special pages
            }
            if ( page.isEmpty() ) {
                pageIt.remove();
            }
        }
        model.addAttribute( "dependencies", dependencies );
        return pages;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String showQuestionnaire( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }


    // have to repeat arbiter function here because it needs to change dependency checking

    /**
     * Adjust page to the answers according to the dependencies.
     * Remove questions that should not be asked.
     *
     * @param page          Page with all questions and variants as it stored in the DB.
     * @param wizardContext virtual answers
     * @param pages         List of all pages. Only previous pages have been processed
     * @param dependencies  map to be shown on the interface
     */
    public void adjustPage( QuestionnairePage page, WizardContext wizardContext, Collection<QuestionnairePage> pages, HashMap<Long, Collection<QuestionVariant>> dependencies ) {
        page = questionnaireService.findPageById( page.getId() );  // refresh page - re-attach page to session to prevent LazyInit..Exception
        for ( Iterator<Question> itQuestion = page.getQuestions().iterator(); itQuestion.hasNext(); ) {
            Question question = itQuestion.next();
            adjustQuestion( question, wizardContext, pages, dependencies );
            if ( question.getVariants().size() == 0 ) {
                itQuestion.remove();
            }
        }
    }

    /**
     * Remove all variants that doesn't correspond to the current answers
     *
     * @param question      question which variants need to be adjusted
     * @param wizardContext virtual answers
     * @param pages         List of all pages. Only previous pages have been processed
     * @param dependencies  map to be shown on the interface
     */
    private void adjustQuestion( Question question, WizardContext wizardContext, Collection<QuestionnairePage> pages, HashMap<Long, Collection<QuestionVariant>> dependencies ) {
        QuestionVariant defaultVariant = null;
        for ( Iterator<QuestionVariant> itVariant = question.getVariants().iterator(); itVariant.hasNext(); ) {
            QuestionVariant variant = itVariant.next();
            if ( variant.isDefault() ) {
                if ( defaultVariant == null ) {
                    defaultVariant = variant;
                    continue;
                } else {
                    itVariant.remove(); // remove all other default variants.
                    continue;
                }
            }
            if ( !checkDependency( variant, wizardContext, pages, dependencies ) ) {
                itVariant.remove();
            }
        }
        if ( question.getVariants().size() > 1 && defaultVariant != null ) {
            question.getVariants().remove( defaultVariant );
        }
    }

    /**
     * Check if the question variant satisfies to the answers and the dependencies
     *
     * @param dependent     Checking object
     * @param wizardContext Answers
     * @param pages         List of all pages. Only previous pages have been processed
     * @param dependencies  map to be shown on the interface
     * @return True if object satisfies
     */
    private boolean checkDependency( QuestionVariant dependent, WizardContext wizardContext, Collection<QuestionnairePage> pages, HashMap<Long, Collection<QuestionVariant>> dependencies ) {
        boolean result = true;
        DependentRoot grouping = null;
        boolean groupStart = false;
        boolean resultForGroup = true;
        for ( BasicDependency key : dependent.getKeys() ) {
            // cheking current dependency
            boolean currentCheck;
            if ( key instanceof QuestionDependency ) {
                currentCheck = checkQuestion( (QuestionDependency) key, pages );
                if ( currentCheck ) {
                    addDependencyDescription( dependencies, ((QuestionDependency) key).getDependsOn().getKeyField().getId(), dependent );
                    //dependencies.put( ((QuestionDependency) key).getDependsOn().getKeyField().getId(), dependent );
                }
            } else {
                currentCheck = key.checkDependency( wizardContext );
            }

            if ( !key.checkGroup( grouping ) ) {
                grouping = key.getDependsOn();
                groupStart = true;
            }
            if ( groupStart ) {
                groupStart = false;

                // result always is TRUE at this point. If it is FALSE we return from this function immediately
                result = resultForGroup;
                if ( !result ) {
                    break;
                }

                resultForGroup = currentCheck;
            } else {
                resultForGroup = resultForGroup || currentCheck;  //??
            }

        }
        return result && resultForGroup;
    }

    private boolean checkQuestion( QuestionDependency key, Collection<QuestionnairePage> pages ) {
        for ( QuestionnairePage prevPage : pages ) {
            if ( prevPage.getId() == ((QuestionVariant) key.getDependent()).getQuestion().getPage().getId() )
                break;  // look for previous pages only
            for ( Question prevQuestion : prevPage.getQuestions() ) {
                if ( prevQuestion.getId() == key.getDependsOn().getId() ) return true;
            }
        }
        return false;
    }

    private void addDependencyDescription( HashMap<Long, Collection<QuestionVariant>> dependencies, long id, QuestionVariant dependent ) {
        Collection<QuestionVariant> variants = dependencies.get( id );
        if ( variants == null ) {
            variants = new LinkedList<QuestionVariant>();
            dependencies.put( id, variants );
        }
        if ( !variants.contains( dependent ) ) {
            variants.add( dependent );
        }
    }

}
