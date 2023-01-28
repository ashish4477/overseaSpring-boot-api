package com.bearcode.ovf.actions.eod;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Date: 17.10.11
 * Time: 18:12
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/svid.htm")
public class ViewSvidController extends ViewLeoController {

    public ViewSvidController() {
        setShowEod( false );
    }
}
