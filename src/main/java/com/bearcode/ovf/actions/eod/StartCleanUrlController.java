package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 21.05.14
 * Time: 20:31
 *
 * @author Leonid Ginzburg
 */
@Controller
@SessionAttributes(ICaptchaUse.USE_COUNT)
public class StartCleanUrlController extends BaseEodController {

    public StartCleanUrlController() {
        setContentBlock( "/WEB-INF/pages/blocks/EodStart.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/EodDisplay.jsp" );
    }

    @ModelAttribute(USE_COUNT)
    public Integer getUseCountAttribute() {
        return number4Captcha;
    }


    @ModelAttribute("pageUrl")
    public String getPageUrlAndOther( final HttpServletRequest request,
                                      final ModelMap model ) {
        model.addAttribute("useCleanUrl", true);
        model.addAttribute("contextPath", request.getContextPath());
        return super.getPageUrlAndOther(request, model);
    }

    @RequestMapping(value = "/state-voting-information")
    public String showSvidPage(final HttpServletRequest request,
                               final ModelMap model,
                               @ModelAttribute("user") final OverseasUser user,
                               @ModelAttribute(USE_COUNT) final Integer useCount,
                               @RequestParam(value = "stateId", required = false) Long stateId ) {
        if ( stateId != null ) {
            State state = getSelectedState( stateId );
            if ( state != null ) {
                return "redirect:/state-voting-information/"+state.getAbbr();
            }
        }
        model.addAttribute( "isEod", false );
        return super.showPage(request, model, user, useCount);
    }

    @RequestMapping(value = "/election-official-directory")
    public String showEodPage(final HttpServletRequest request,
                              final ModelMap model,
                              @ModelAttribute("user") final OverseasUser user,
                              @ModelAttribute(USE_COUNT) final Integer useCount) {

        return super.showPage(request, model, user, useCount);
    }

}
