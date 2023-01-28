package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.service.MailingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Date: 05.07.12
 * Time: 22:11
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/MailingLists.htm")
public class MailingLists extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    public MailingLists() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/MailingLists.jsp" );
        setPageTitle( "Mailing List" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("mailingLists")
    public List<MailingList> getMailingLists() {
        return mailingListService.findAllMailingLists();
    }

    @ModelAttribute("subscribersCount")
    public Map<Long,Long> getSubscribersCount() {
        return mailingListService.findSubscribersCount();
    }

    @ModelAttribute("unsubscribersCount")
    public Map<Long,Long> getUnsubscribersCount() {
        return mailingListService.findUnsubscribersCount();
    }

    @ModelAttribute("errorsCount")
    public Map<Long,Long> getErrorsCount() {
        return mailingListService.findErrorsCount();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public String showLists( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }
}
