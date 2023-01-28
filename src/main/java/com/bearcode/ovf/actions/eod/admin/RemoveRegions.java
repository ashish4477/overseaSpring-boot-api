package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Date: 13.09.12
 * Time: 19:29
 *
 * @author Leonid Ginzburg
 */
@Controller
public class RemoveRegions {

    @Autowired
    LocalOfficialService localOfficialService;

    @Autowired
    StateService stateService;

    @RequestMapping(value = "/admin/RemoveRegions.htm", method = RequestMethod.POST)
    public String removeRegions( @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        State state = stateService.findState( stateId );
        if ( state != null ) {
            localOfficialService.deleteAllRegionsOfState( state );
        }
        return "redirect:/admin/EodStates.htm";
    }
}
