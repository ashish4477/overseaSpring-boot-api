package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.AllowedForAddOn;
import com.bearcode.ovf.actions.questionnaire.AllowedForRedirect;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Alexey Polyakov
 *         Date: Aug 14, 2007
 *         Time: 1:37:10 PM
 */
@Controller
@RequestMapping("/admin/EditQuestionnairePage.htm")
public class EditQuestionnairePage extends BaseController implements ApplicationContextAware {

    /**
     * the string used to redirect to the questionnaire pages administration page.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String REDIRECT_ADMIN_QUESTIONNAIRE_PAGES = "redirect:/admin/QuestionnairePages.htm";

    /**
     * the error message produced when the page hierarchy cannot be deleted because it has dependents.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String REJECT_DEPENDENTS_ERROR_MESSAGE = "List of questions contains questions with dependents. Please delete the dependents first.";

    /**
     * the error code produced when the page hierarchy cannot be deleted because it has dependents.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String REJECT_DEPENDENTS_ERROR_CODE = "rava.admin.page.questions_have_dependents";

    /**
     * the default section name.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String DEFAULT_SECTION_NAME = "admin";

    /**
     * the default section CSS.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String DEFAULT_SECTION_CSS = "/css/admin.css";

    /**
     * the default page title.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String DEFAULT_PAGE_TITLE = "Edit Questionnaire Page";

    /**
     * the default content block.
     *
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/admin/EditQuestionnairePage.jsp";

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    private ApplicationContext applicationContext;

    public EditQuestionnairePage() {
        setContentBlock( DEFAULT_CONTENT_BLOCK );
        setPageTitle( DEFAULT_PAGE_TITLE );
        setSectionName( DEFAULT_SECTION_NAME );
        setSectionCss( DEFAULT_SECTION_CSS );
    }

    @ModelAttribute("questionaryPage")
    public QuestionnairePage formBackingObject( ModelMap model,
                                                @RequestParam(value = "id", required = false) Long pageId,
                                                @RequestParam(value = "type", required = false) String typeName,
                                                @RequestParam(value = "classType", required = false, defaultValue = "GENERAL") String classType ) {

        QuestionnairePage thisPage = null;
        if ( pageId != null && pageId != 0 )
            thisPage = questionnaireService.findPageById( pageId );

        if ( thisPage == null ) { // create a new page
            thisPage = new QuestionnairePage( typeName );
            try {
                PageClassType pageClassType = PageClassType.valueOf( classType );
                switch ( pageClassType ) {
                    case ADD_ON:
                        thisPage = new AddOnPage( typeName );
                        break;
                    case EXTERNAL:
                        thisPage = new ExternalPage( typeName );
                        break;
                }
            } catch ( IllegalArgumentException e ) {
                //nothing to do. Page has been already created.
            }
        }
        String pageClass = PageClassType.GENERAL.name();
        if ( thisPage instanceof AddOnPage ) pageClass = PageClassType.ADD_ON.name();
        if ( thisPage instanceof ExternalPage ) pageClass = PageClassType.EXTERNAL.name();
        model.addAttribute( "pageClass", pageClass );
        return thisPage;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String showForm( HttpServletRequest request,
                            @ModelAttribute("questionaryPage") QuestionnairePage thisPage,
                            ModelMap model ) {

        List<QuestionnairePage> pages = questionnaireService.findQuestionnairePages( thisPage.getType() );

        if ( pages.size() > 0 ) {
            int minNumber = pages.get( 0 ).getNumber() - 1;
            int maxNumber = pages.get( pages.size() - 1 ).getNumber() + 1;

            Map<Integer, Collection<Integer>> crossPageConnection = questionnaireService.findCrossPageConnection( thisPage.getType() );

            Collection<Integer> connections = crossPageConnection.get( thisPage.getNumber() );
            if ( connections != null ) {
                for ( Integer pageNum : connections ) {
                    if ( pageNum < thisPage.getNumber() && pageNum > minNumber ) {
                        minNumber = pageNum;
                    }
                    if ( pageNum > thisPage.getNumber() && pageNum < maxNumber ) {
                        maxNumber = pageNum;
                    }
                }
            }

            /*// find biggest number of page that contains question this page depends on
            if(thisPage.getId() != 0 && thisPage.getQuestions() != null ) {
                for (Question question : thisPage.getQuestions()) {
                    if(question.getVariants() != null) {
                        for (QuestionVariant questionVariant : question.getVariants()) {
                            if(questionVariant.getKeys() != null) {
                                for (BasicDependency dependency : questionVariant.getKeys()) {
                                    if ( dependency instanceof QuestionDependency &&  ((QuestionDependency)dependency).getDependsOn().getPage().getNumber() > minNumber) {
                                        minNumber = ((QuestionDependency)dependency).getDependsOn().getPage().getNumber();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // find smallest number of page contains question depends on this page
            for (QuestionnairePage p : pages) {
                if(thisPage.getNumber() > p.getNumber()) continue;
                if ( p.getQuestions() != null ) {
                    for (Question question : p.getQuestions()) {
                        if(question.getVariants() != null) {
                            for (QuestionVariant variant : question.getVariants()) {
                                if(variant.getKeys() != null) {
                                    for (BasicDependency dependency : variant.getKeys()) {
                                        if ( dependency instanceof QuestionDependency &&  ((QuestionDependency)dependency).getDependsOn().getPage().getId() ==  thisPage.getId()) {
                                            if(p.getNumber() < maxNumber)
                                                maxNumber = p.getNumber();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
*/
            for ( Iterator<QuestionnairePage> iterator = pages.iterator(); iterator.hasNext(); ) {
                QuestionnairePage p = iterator.next();
                if ( p.getNumber() < minNumber || p.getNumber() >= maxNumber ) {
                    iterator.remove();
                }
            }

            model.addAttribute( "minNumber", minNumber );
            model.addAttribute( "maxNumber", maxNumber );

            Collection<QuestionVariant> dependentVariants = getQuestionnaireService().findDependentVariants( thisPage );
            model.addAttribute( "dependentVariants", dependentVariants );
        }
        model.addAttribute( "pages", pages );

        if ( thisPage.getId() == 0 ) {
            model.addAttribute( "pageClasses", PageClassType.values() );
        }
        addAvailableBean( model );
        return buildModelAndView( request, model );
    }

    private void addAvailableBean( ModelMap model ) {
        PageClassType classType = PageClassType.valueOf( (String) model.get( "pageClass" ) );
        Collection<String> names = new LinkedHashSet<String>();  // only unique urls
        switch ( classType ) {
            case ADD_ON:
                names.addAll( Arrays.asList( applicationContext.getBeanNamesForType( AllowedForAddOn.class ) ) );
                break;
            case EXTERNAL:
                String beanNames[] = applicationContext.getBeanNamesForType( AllowedForRedirect.class );
                for ( String beanName : beanNames ) {
                    AllowedForRedirect bean = applicationContext.getBean( beanName, AllowedForRedirect.class );
                    RequestMapping mapping = bean.getClass().getAnnotation( RequestMapping.class );
                    String[] prefixes;
                    if ( mapping != null ) {
                        prefixes = mapping.value();
                    } else {
                        prefixes = new String[]{""};
                    }
                    //looks for methods, they could have additional parts of mapped URL
                    for ( Method method : bean.getClass().getDeclaredMethods() ) {
                        mapping = method.getAnnotation( RequestMapping.class );
                        if ( mapping != null ) {
                            if ( Arrays.asList( mapping.method() ).contains( RequestMethod.GET )
                                    && mapping.params().length == 0 && mapping.headers().length == 0 ) {
                                // only plain GET handlers are allowed
                                String[] methodValues = mapping.value();
                                if ( methodValues.length == 0 ) methodValues = new String[]{""};
                                for ( String mappingValue : methodValues ) {
                                    for ( String prefix : prefixes ) {
                                        mappingValue = prefix + mappingValue;
                                        if ( mappingValue.length() > 0 && !mappingValue.matches( ".*\\{\\w+\\}.*" ) ) {
                                            // do not work with URL with path variables. Too hard to be sure about building correct URL
                                            names.add( mappingValue );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }
        model.addAttribute( "additionalBehavior", names );
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String savePage( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("questionaryPage") @Valid QuestionnairePage page, BindingResult errors ) {

        if ( !errors.hasErrors() ) {
            boolean newPage = page.getId() == 0l;
            questionnaireService.savePage( page );
            if ( !newPage ) {
                return REDIRECT_ADMIN_QUESTIONNAIRE_PAGES;
            }
        }
        return showForm( request, page, model );
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete")
    public String deletePage( HttpServletRequest request, ModelMap model,
                              @ModelAttribute("questionaryPage") QuestionnairePage page, BindingResult errors ) {

        final List<Question> questions = page.getQuestions();
        if ( questions == null || questions.isEmpty() ) {
            questionnaireService.deletePage( page );
        } else {
            errors.reject( "rava.admin.page.questions_not_empty", "List of questions isn't empty. Please, delete all questions of the page first." );
            return showForm( request, page, model );
        }
        return REDIRECT_ADMIN_QUESTIONNAIRE_PAGES;
    }

    /**
     * Deletes the page hierarchy.
     *
     * @param request the request.
     * @param model   the model.
     * @param page    the page.
     * @param errors  the errors.
     * @return the response string.
     * @author IanBrown
     * @version May 25, 2012
     * @since May 24, 2012
     */
    @RequestMapping(method = RequestMethod.POST, params = "deleteHierarchy")
    public String deletePageHierarchy( final HttpServletRequest request, final ModelMap model,
                                       @ModelAttribute("questionaryPage") final QuestionnairePage page, final BindingResult errors ) {
        try {
            questionnaireService.deletePageHierarchy( page );
            return REDIRECT_ADMIN_QUESTIONNAIRE_PAGES;

        } catch ( final IllegalStateException e ) {
            errors.reject( REJECT_DEPENDENTS_ERROR_CODE, REJECT_DEPENDENTS_ERROR_MESSAGE );
            return showForm( request, page, model );
        }
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof QuestionnairePage ) {
            binder.setValidator( validator );
        }
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Gets the questionnaire service.
     *
     * @return the questionnaire service.
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    /**
     * Sets the questionnaire service.
     *
     * @param questionnaireService the questionnaire service to set.
     * @author IanBrown
     * @version May 24, 2012
     * @since May 24, 2012
     */
    public void setQuestionnaireService( QuestionnaireService questionnaireService ) {
        this.questionnaireService = questionnaireService;
    }
}
