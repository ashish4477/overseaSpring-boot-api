package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.service.SendGridLogService;
import com.bearcode.ovf.webservices.sendgrid.model.SendGridLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("/admin/SendGridLog.htm")
public class SendGridLog extends BaseController {

    @Autowired
    private SendGridLogService logService;

    public SendGridLog() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/SendGridLog.jsp" );
        setPageTitle( "Send Grid Log Messages" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("logMessages")
    public List<SendGridLogMessage> getMessages() {
        return logService.findLogMessages();
    }

    @RequestMapping( method = {RequestMethod.POST, RequestMethod.GET})
    public String showPage( HttpServletRequest request, ModelMap modelMap ) {
        return buildModelAndView( request, modelMap );
    }
}
