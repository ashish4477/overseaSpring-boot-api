package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.AdminStatisticsForm;
import com.bearcode.ovf.service.OverseasUserService;
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
 * Date: Nov 23, 2007
 * Time: 5:54:40 PM
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/Statistics.htm")
public class BaseStatistics extends BaseController {

    @Autowired
    private OverseasUserService userService;

    public BaseStatistics() {
        setPageTitle( "Statistics" );
        setContentBlock( "/WEB-INF/pages/blocks/admin/Statistics.jsp" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("statisticsForm")
    public AdminStatisticsForm getFormObject() {
        return new AdminStatisticsForm();
    }

    @RequestMapping( method = {RequestMethod.GET, RequestMethod.POST} )
    public String showStatistics(HttpServletRequest request,
                                  ModelMap model,
                                  @ModelAttribute("statisticsForm") AdminStatisticsForm form) throws Exception {
        model.addAttribute( "usersCount", userService.countUsers() );
        model.addAttribute( "realUsers", userService.countRealUsers() );

        model.addAttribute( "usersByState", userService.countUsersByState( form ) );
        model.addAttribute( "pdfStats", userService.countPdfGenerations( form ) );
        return buildModelAndView( request, model );
    }
}
