package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.webservices.factcheck.FactCheckService;
import com.bearcode.ovf.webservices.factcheck.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 29.05.14
 * Time: 18:53
 *
 * @author Leonid Ginzburg
 */
@Controller
public class FactCheckArticleController extends BaseController {

    @Autowired
    private FactCheckService factCheckService;

    public FactCheckArticleController() {
        setSectionCss( "/css/rava.css" );
        setSectionName("mva factcheck article");
        setPageTitle("In The News");
        setContentBlock("/WEB-INF/pages/blocks/Article.jsp");
    }

    @ModelAttribute("article")
    public Article getArticle( @RequestParam("id") Long id ) {
        return factCheckService.getArticle(id);
    }

    @RequestMapping("/Article.htm")
    public String showArticle( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    public void setFactCheckService(FactCheckService factCheckService) {
        this.factCheckService = factCheckService;
    }
}
