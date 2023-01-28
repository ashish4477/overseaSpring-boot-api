package com.bearcode.ovf.actions.eod;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Date: 17.10.11
 * Time: 18:15
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/wi-eod.htm")
public class ViewLeoWisconsin extends ViewLeoController {

    public ViewLeoWisconsin() {
        mainTemplate = "templates/WITemplate";
    }
}
