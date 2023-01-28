package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.AdminCorrectionsListForm;
import com.bearcode.ovf.service.LocalOfficialService;
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
 * Date: Jul 11, 2007
 * Time: 4:27:57 PM
 *
 * @author Leonid Ginzburg
 *         <p/>
 *         Not used yet. There is no correction for SVID.
 */
@Controller
@RequestMapping("/admin/EodCorrectionsList.htm")
public class CorrectionsListController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    public CorrectionsListController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodCorrectionsList.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "List of corrections" );
    }

    @ModelAttribute("paging")
    protected AdminCorrectionsListForm formBackingObject() {
        AdminCorrectionsListForm object = new AdminCorrectionsListForm();
        object.setPageSize( 25 );                           // TODO get param from config
        return object;
    }


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String buildReferences( HttpServletRequest request, ModelMap model,
                                   @ModelAttribute("paging") AdminCorrectionsListForm command ) {
        model.put( "correctionsList", localOfficialService.findCorrections( command ) );
        return buildModelAndView( request, model );
    }
}
