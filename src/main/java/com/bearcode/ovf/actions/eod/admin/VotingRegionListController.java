package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.AdminVotingRegionListForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 17, 2007
 * Time: 8:08:47 PM
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EodVotingRegions.htm")
public class VotingRegionListController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;


    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService(LocalOfficialService localOfficialService) {
        this.localOfficialService = localOfficialService;
    }

    public VotingRegionListController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodRegionList.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "List of Voting Regions" );
    }

    @ModelAttribute("paging")
    protected AdminVotingRegionListForm getPaging()  {
        AdminVotingRegionListForm object = new AdminVotingRegionListForm();
        object.setPageSize( 25 );                           // TODO get param from config
        return object;
    }

    @ModelAttribute("selectedState")
    protected State getSelectedState( @ModelAttribute("paging") AdminVotingRegionListForm paging ) {
        return getStateService().findState( paging.getStateId() );
    }

    @ModelAttribute("listLeo")
    protected Collection<LocalOfficial> getListLeo(
            @ModelAttribute("paging") AdminVotingRegionListForm paging,
            @RequestParam( value = "lookFor", required = false, defaultValue = "0") Long lookFor
    ) {
        return localOfficialService.findForState( paging.getStateId(), paging, lookFor );
    }

    @RequestMapping( method = RequestMethod.GET )
    public String showVotingRegions( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }
}
