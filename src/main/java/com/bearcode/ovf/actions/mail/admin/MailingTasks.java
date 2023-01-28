package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.mail.MailingTask;
import com.bearcode.ovf.service.MailingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author leonid.
 */
@Controller
public class MailingTasks extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    public MailingTasks() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/MailingTasks.jsp" );
        setPageTitle( "Mailing Tasks" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("mailingTasks")
    public List<MailingTask> getTasks() {
        return mailingListService.findAllTasks();
    }

    @RequestMapping(value = "/admin/MailingTasks.htm")
    public String showTasks( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }
}
