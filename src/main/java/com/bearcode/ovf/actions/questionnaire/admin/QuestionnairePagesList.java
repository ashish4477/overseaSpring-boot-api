package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.questionnaire.PageType;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author Alexey Polyakov
 *         Date: Aug 14, 2007
 *         Time: 1:36:45 PM
 */
@Controller
@RequestMapping("/admin/QuestionnairePages.htm")
public class QuestionnairePagesList extends BaseController {

    @Autowired
    private QuestionnaireService questionnaireService;

    public QuestionnairePagesList() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/QuestionnairePageList.jsp" );
        setPageTitle( "Questionnaire Pages" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("pages")
    public Collection<QuestionnairePage> getPages() {
        return questionnaireService.findQuestionnairePages();
    }

    @ModelAttribute("formTypes")
    public PageType[] getFormTypes() {
        return PageType.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showPage( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

}
