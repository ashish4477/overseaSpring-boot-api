package com.bearcode.ovf.actions.eod;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Date: 17.10.11
 * Time: 18:20
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping({"/eodStandalone.htm","/MN-ElectionOfficialDirectory"})
public class ViewLeoStandalone extends ViewLeoController {

    public ViewLeoStandalone() {
        mainTemplate = "templates/ContentOnlyTemplate";
        setSectionCss( "/css/eod-standalone.css" );
        setShowSvid( false );
    }
}
