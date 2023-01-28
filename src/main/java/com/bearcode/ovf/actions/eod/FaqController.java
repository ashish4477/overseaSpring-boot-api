package com.bearcode.ovf.actions.eod;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bearcode.ovf.actions.commons.BaseController;

/**
 * Date: 20.07.01
 * Time: 11:40
 *
 * @author Vernon Grant
 */
@Controller
@RequestMapping("/faq.htm")
public class FaqController extends ViewLeoController {
    
    public FaqController() {
        setContentBlock("/WEB-INF/pages/blocks/Faq.jsp");
        setSectionCss( "/css/eod.css" );
        setSectionName( "faq" );
        setPageTitle( "Faq" );
    }
}
