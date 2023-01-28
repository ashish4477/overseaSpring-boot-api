package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.service.QuestionFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alexey Polyakov
 *         Date: Aug 13, 2007
 *         Time: 4:33:35 PM
 */
@Controller
public class FieldTypeList extends BaseController {

    @Autowired
    private QuestionFieldService questionFieldService;

    public FieldTypeList() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/FieldTypeList.jsp" );
        setPageTitle( "Field types" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @RequestMapping("/admin/FieldTypes.htm")
    public String buildReferences( HttpServletRequest request, ModelMap model ) throws Exception {
        model.addAttribute( "fieldTypes", questionFieldService.findFieldTypes() );
        return buildModelAndView( request, model );
    }

}
