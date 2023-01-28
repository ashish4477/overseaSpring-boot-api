package com.bearcode.ovf.actions;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.service.LocalOfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 27.11.12
 * Time: 19:13
 *
 * @author Leonid Ginzburg
 */
@Controller
public class CountyCorner extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    public CountyCorner() {
        setPageTitle( "County Corner" );
        setContentBlock( "/WEB-INF/pages/blocks/CountyCorner.jsp" );
        setSectionCss( "/css/home.css" );
        setSectionName( "countyCorner" );
        setShowMetaKeywords( true );
    }

    @ModelAttribute("leo")
    public LocalOfficial findLocalOfficial( @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                               @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                               @RequestParam(value = "regionName", required = false, defaultValue = "") final String regionName ) {
        LocalOfficial leo = null;
        if ( regionId != 0 ) {
            leo = localOfficialService.findForRegion( regionId );
        } else if ( stateId != 0 && regionName.length() > 0 ) {
            for ( VotingRegion region : getStateService().findRegionsForState( stateId ) ) {
                if ( region.getName().equalsIgnoreCase( regionName ) ) {
                    leo = localOfficialService.findForRegion( region );
                    break;
                }
            }
        }
        return leo;
    }

    @ModelAttribute("svid")
    public StateSpecificDirectory findStateSpecificDirectory( @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId ) {
        StateSpecificDirectory svid = null;
        if ( stateId != 0 ) {
            svid = localOfficialService.findSvidForState( stateId );
        }
        return svid;
    }

    @RequestMapping("/countyCorner.htm")
    public String showForm( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

}
