package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Date: 19.06.14
 * Time: 18:19
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping(value = "/us")
@SessionAttributes(ICaptchaUse.USE_COUNT)
public class CleanUrlDomesticEodController extends BaseController implements ICaptchaUse {

    @Autowired
    private DomesticEodController domesticEodController;

    @Autowired
    @Qualifier("viewLeoController")
    private ViewLeoController eodController;

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

    /**
     * Gets the use count.
     *
     * @return the use count.
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    @ModelAttribute(DomesticEodController.USE_COUNT)
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

    @RequestMapping(value = "/election-official-directory/{stateName}", params = {CAPTHCA_PARAM})
    public String checkCaptchaEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   final HttpSession session,
                                   @PathVariable("stateName") String stateName,
                                   @RequestParam(CAPTHCA_PARAM) final String captchaInput,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        return checkCaptchaEod( request, model, session, stateName, null, captchaInput, user, useCount );
    }

    @RequestMapping(value = "/election-official-directory/{stateName}/{regionName}", params = {CAPTHCA_PARAM})
    public String checkCaptchaEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   final HttpSession session,
                                   @PathVariable("stateName") String stateName,
                                   @PathVariable("regionName") String regionName,
                                   @RequestParam(CAPTHCA_PARAM) final String captchaInput,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        State state = stateName != null ? getStateService().findStateByAbbreviationOrName(stateName) : null;
        model.addAttribute("selectedState", state);
        regionName = regionName.replace("%2e", ".");
        EodRegion region = null;
        if ( state != null ) {
            model.addAttribute( "regions", eodController.getEodRegions( state.getAbbr() ));
            if ( regionName != null) {
                region = eodController.getSelectedRegion(model, state.getAbbr(), 0l, regionName);
            }
        }
        return domesticEodController.checkCaptchaEod( request, model, session,
                state == null ? 0l : state.getId(),
                region == null ? 0l : region.getId(),
                captchaInput, user, useCount );
    }

    @RequestMapping(value = "/election-official-directory/{stateName}")
    public String showDomesticEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   @PathVariable("stateName") String stateName,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        return showDomesticEod( request, model, stateName, null, user, useCount );
    }

    @RequestMapping(value = "/election-official-directory/{stateName}/{regionName}")
    public String showDomesticEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   @PathVariable("stateName") String stateName,
                                   @PathVariable("regionName") String regionName,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        State state = stateName != null ? getStateService().findStateByAbbreviationOrName(stateName) : null;
        model.addAttribute("selectedState", state);
        if ( regionName != null ) {
            regionName = regionName.replace("%2e", ".");
        }
        EodRegion region = null;
        if ( state != null ) {
            model.addAttribute( "regions", eodController.getEodRegions( state.getAbbr() ));
            if ( regionName != null) {
                region = eodController.getSelectedRegion(model, state.getAbbr(), 0l, regionName);
            }
        }
        return domesticEodController.showDomesticEodResult( request, model,
                state == null ? 0l : state.getId(),
                region == null ? 0l : region.getId(),
                user, useCount );
    }

    @RequestMapping(value = "/state-voting-information/{stateName}", params = {CAPTHCA_PARAM})
    public String checkCaptchaSvid( final HttpServletRequest request,
                                    final ModelMap model,
                                    final HttpSession session,
                                    @PathVariable("stateName") String stateName,
                                    @RequestParam(CAPTHCA_PARAM) final String captchaInput,
                                    @RequestParam(value = "voting_information", required = false, defaultValue = "0") final String votingInformation,
                                    @RequestParam(value = "location", required = false, defaultValue = "0") final String location,
                                    @ModelAttribute("user") final OverseasUser user,
                                    @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {
        State state = stateName != null ? getStateService().findStateByAbbreviationOrName(stateName) : null;
        model.addAttribute("selectedState", state);
        if ( state != null ) {
            model.addAttribute( "regions", eodController.getEodRegions( state.getAbbr() ));
        }
        return domesticEodController.checkCaptchaSvid( request, model, session,
                state == null ? 0l : state.getId(), 0l,votingInformation,location,
                captchaInput, user, useCount );
    }

    @RequestMapping(value = "/state-voting-information/{stateName}")
    public String showDomesticSvidResult( final HttpServletRequest request,
                                          final ModelMap model,
                                          @PathVariable("stateName") String stateName,
                                          @ModelAttribute("user") final OverseasUser user,
                                          @ModelAttribute(DomesticEodController.USE_COUNT) final Integer useCount ) {

        State state = stateName != null ? getStateService().findStateByAbbreviationOrName(stateName) : null;
        model.addAttribute("selectedState", state);
        if ( state != null ) {
            model.addAttribute( "regions", eodController.getEodRegions( state.getAbbr() ));
        }
        return domesticEodController.showDomesticSvidResult(request, model,
                state == null ? 0l : state.getId(), 0l, "", "",
                user, useCount);
    }

    @ModelAttribute("uocavaOffice")
    public LocalOffice getUocavaRegion( @PathVariable(value = "stateName") String stateName,
                                        HttpServletRequest request, ModelMap model ) {
        State state = getStateService().findStateByAbbreviationOrName( stateName );
        EodRegion uocavaRegion = state != null ? eodController.getEodApiService().findUocavaRegion( state.getAbbr() ) : null;
        LocalOffice uocava = uocavaRegion != null ? eodController.getEodApiService().getLocalOffice( uocavaRegion.getId().toString() ) : null;
        if ( uocava != null ) {
            model.addAttribute( "uocavaCorrectionUri", eodController.buildCorrectionsUri( uocava.getId(), request ) );
        }
        return uocava;
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE)
    public String getSiteKey() {
        return domesticEodController.getSiteKey();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_SKIMM)
    public String getSiteKeySkim(){
        return domesticEodController.getSiteKeySkim();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_VOTE411)
    public String getSiteKeyVote411(){
        return domesticEodController.getSiteKeyVote411();
    }
}
