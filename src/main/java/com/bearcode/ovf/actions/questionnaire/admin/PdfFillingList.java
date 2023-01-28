package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.service.RelatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 18, 2007
 * Time: 6:12:15 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/InstructionsList.htm")
public class PdfFillingList extends BaseController {

    @Autowired
    private RelatedService relatedService;

    public PdfFillingList() {
        setSectionCss( "/css/admin.css" );
        setSectionName( "admin" );
        setContentBlock( "/WEB-INF/pages/blocks/admin/PdfFillingList.jsp" );
        setPageTitle( "Voter Instructions" );
    }

    public void setRelatedService( RelatedService relatedService ) {
        this.relatedService = relatedService;
    }

    @ModelAttribute("paging")
    public CommonFormObject getFillings() {
        CommonFormObject form = new CommonFormObject();
        form.setPageSize( 25 );                           // TODO get param from config
        return form;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showPage( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("paging") CommonFormObject form ) {
        model.addAttribute( "fillings", relatedService.findPdfFillings( form ) );
        return buildModelAndView( request, model );
    }
}
