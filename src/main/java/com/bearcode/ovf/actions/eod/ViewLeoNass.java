package com.bearcode.ovf.actions.eod;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Date: 17.10.11
 * Time: 18:06
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/nass.htm")
public class ViewLeoNass extends ViewLeoController {

    public ViewLeoNass() {
    	showSvid = false;
        mainTemplate = "templates/NASSTemplate";
        setShowSvid( false );
    }
}
