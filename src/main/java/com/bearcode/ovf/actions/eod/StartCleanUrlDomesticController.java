package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.LocalOfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by leonid on 19.02.16.
 */
@Controller
@SessionAttributes(ICaptchaUse.USE_COUNT)
public class StartCleanUrlDomesticController extends BaseController implements ICaptchaUse{

    /**
     * the domestic EOD result JSP.
     *
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    static final String DOMESTIC_EOD_RESULT_JSP = "/WEB-INF/pages/blocks/EodDisplayDomestic.jsp";

    /**
     * the domestic EOD start JSP.
     *
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    static final String DOMESTIC_EOD_START_JSP = "/WEB-INF/pages/blocks/EodStart.jsp";

    /**
     * the default section CSS.
     *
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    static final String DEFAULT_SECTION_CSS = "/css/eod.css";

    /**
     * the default section name.
     *
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    static final String DEFAULT_SECTION_NAME = "eod";

    @Autowired
    private DomesticEodController domesticEodController;

    /**
     * Consructs a domestic EOD controller with default values.
     *
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    public StartCleanUrlDomesticController() {
        setContentBlock( DOMESTIC_EOD_START_JSP );
        setSuccessContentBlock( DOMESTIC_EOD_RESULT_JSP );
        setSectionCss( DEFAULT_SECTION_CSS );
        setSectionName( DEFAULT_SECTION_NAME );
        setPageTitle( "Information Directory" );
    }



    @ModelAttribute(USE_COUNT)
    public Integer getUseCountAttribute() {
        return number4Captcha;
    }

    @ModelAttribute("useCleanUrl")
    public boolean getUseCleanUrl() {
        return true;
    }

    @ModelAttribute("contextPath")
    public String getContextPath( final HttpServletRequest request ) {
        return request.getContextPath();
    }

    /**
     * Is this a domestic page?
     *
     * @return always <code>true</code>.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 18, 2012
     */
    @ModelAttribute("isDomestic")
    public boolean getDomestic() {
        return true;
    }

    /**
     * Gets the page URL from the request.
     *
     * @param request the request.
     * @return the page URL.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    @ModelAttribute("pageUrl")
    public String getPageUrl( final HttpServletRequest request ) {
        return request.getServletPath();
    }

    /**
     * Gets the region label from the request.
     *
     * @param regionLabel the region label from the request.
     * @return the region label.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    @ModelAttribute("regionLabel")
    public String getRegionLabel(
            @RequestParam(value = "regionLabel", required = false, defaultValue = "region") final String regionLabel ) {
        return regionLabel;
    }

    @RequestMapping(value = "/us/election-official-directory", params = {CAPTHCA_PARAM})
    public String checkCaptchaEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   final HttpSession session,
                                   @RequestParam(CAPTHCA_PARAM) final String captchaInput,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        return domesticEodController.checkCaptchaEod( request, model, session, 0l, 0l, captchaInput, user, useCount );
    }

    @RequestMapping(value = "/us/election-official-directory")
    public String showDomesticEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        if ( stateId != 0l ) {
            State state = getStateService().findState( stateId );
            if ( state != null ) {
                return "redirect:/us/election-official-directory/"+state.getAbbr();
            }
        }
        return domesticEodController.showDomesticEodResult( request, model, stateId, 0l, user, useCount );
    }

    @RequestMapping(value = "/us/state-voting-information")
    public String showDomesticSvid( final HttpServletRequest request,
                                    final ModelMap model,
                                    @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                    @ModelAttribute("user") final OverseasUser user,
                                    @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        if ( stateId != 0l ) {
            State state = getStateService().findState( stateId );
            if ( state != null ) {
                return "redirect:/us/state-voting-information/"+state.getAbbr();
            }
        }
        return domesticEodController.showDomesticSvidResult( request, model, 0l, 0l, "", "", user, useCount );
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE)
    public String getSiteKey() {
        return domesticEodController.getSiteKey();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_SKIMM)
    public String getSiteKeySkim() {
        return domesticEodController.getSiteKeySkim();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_VOTE411)
    public String getSiteKeyVote411() {
        return domesticEodController.getSiteKeyVote411();
    }
}
