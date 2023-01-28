package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.ReportDateEditor;
import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.model.mail.MailingLinkStats;
import com.bearcode.ovf.model.mail.MailingLinkStatus;
import com.bearcode.ovf.service.MailingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("/admin/MailingLinkStatistics.htm")
public class MailingLinkStatistics extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    public MailingLinkStatistics() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/MailingLinkStatistics.jsp" );
        setPageTitle( "Mailing Link Stats" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("stats")
    public List<MailingLinkStats> getStats(@RequestParam(value = "fromDate", required = false) Date fromDate,
                                           ModelMap modelMap ) {
        if ( fromDate == null ) {
            Calendar from = Calendar.getInstance();
            from.add( Calendar.MONTH, -1 );
            fromDate = from.getTime();
        }
        modelMap.addAttribute( "fromTheDate", fromDate );
        return mailingListService.findMailingLinkStatistic( fromDate );
    }

    @ModelAttribute("linkStatuses")
    public MailingLinkStatus[] getMailingLinkStatuses() {
        return MailingLinkStatus.values();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String showStatistics( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping( method = RequestMethod.POST, params = "changeStatus")
    public String changeStatuses( HttpServletRequest request, ModelMap modelMap,
                                  @RequestParam("fromDate") Date fromDate,
                                  @RequestParam("fromStatus") MailingLinkStatus fromStatus,
                                  @RequestParam("toStatus") MailingLinkStatus toStatus,
                                  @RequestParam("count") int limit ) {
        List<MailingLink> links = mailingListService.findMailingLinksForChange( fromDate, fromStatus, limit );
        for ( MailingLink link : links ) {
            link.setStatus( toStatus );
            if ( fromStatus == MailingLinkStatus.ERROR ) {
                link.setErrorCount( 0 );
                link.setErrorMessage( "" );
            }
        }
        mailingListService.saveMailingLink( links );
        modelMap.addAttribute( "stats", getStats( fromDate, modelMap ) );
        return buildModelAndView( request, modelMap );
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        binder.registerCustomEditor( Date.class, new ReportDateEditor() );
    }
}
