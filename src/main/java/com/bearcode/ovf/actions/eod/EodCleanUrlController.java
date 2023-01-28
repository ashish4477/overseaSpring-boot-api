package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * Date: 19.05.14
 * Time: 21:33
 *
 * @author Leonid Ginzburg
 */
@Controller
@SessionAttributes(ICaptchaUse.USE_COUNT)
@RequestMapping(value = "/election-official-directory/{stateName}")
public class EodCleanUrlController extends BaseEodController  {

    public EodCleanUrlController() {
        setContentBlock( "/WEB-INF/pages/blocks/EodStart.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/EodDisplay.jsp" );
        //setShowSvid( false );
    }

    @ModelAttribute(USE_COUNT)
    public Integer getUseCountAttribute() {
        return number4Captcha;
    }


    @ModelAttribute("pageUrl")
    public String getPageUrlAndOther( final HttpServletRequest request,
                                      final ModelMap model ) {
        model.addAttribute( "useCleanUrl", true );
        model.addAttribute( "contextPath", request.getContextPath() );
        return super.getPageUrlAndOther( request, model );
    }

    @ModelAttribute("selectedState")
    public State getSelectedState( @PathVariable(value = "stateName") String stateName ) {
        return getStateService().findStateByAbbreviationOrName( stateName );
    }

    @ModelAttribute("regions")
    public Collection<EodRegion> getRegions( @PathVariable(value = "stateName") String stateName ) {
        State state = getStateService().findStateByAbbreviationOrName( stateName );
        return state != null ? getEodRegions( state.getAbbr() ) : null;
    }

    @ModelAttribute("uocavaOffice")
    public LocalOffice getUocavaRegion( final HttpServletRequest request,
                                        @PathVariable(value = "stateName") String stateName,
                                        final ModelMap model) {
        State state = getStateService().findStateByAbbreviationOrName( stateName );
        EodRegion uocavaRegion = state != null ? getEodApiService().findUocavaRegion( state.getAbbr() ) : null;
        LocalOffice uocava = uocavaRegion != null ? getEodApiService().getLocalOffice( uocavaRegion.getId().toString() ) : null;
        if ( uocava != null ) {
            model.addAttribute( "uocavaCorrectionUri", buildCorrectionsUri( uocava.getId(), request ) );
        }
        return uocava;
    }

    @RequestMapping("")
    public String showEodPage(final HttpServletRequest request,
                              final ModelMap model,
                              @ModelAttribute("user") final OverseasUser user,
                              @ModelAttribute(USE_COUNT) final Integer useCount,
                              @ModelAttribute("selectedState") final State state) {
        getSelectedRegion( model, state != null ? state.getId() : 0l, 0l, "" );
        return super.showResult(request, model, user, useCount);
    }

    @RequestMapping(value = "/{regionName}")
    public String showEodAndRegionPage( final HttpServletRequest request,
                               final ModelMap model,
                               @ModelAttribute("user") final OverseasUser user,
                               @ModelAttribute(USE_COUNT) final Integer useCount,
                               @ModelAttribute("selectedState") final State state,
                               @PathVariable("regionName") String regionName ) {
        regionName = regionName.replace("%2e", ".");
        if ( state != null ) {
            /*EodRegion region = getSelectedRegion( model, state.getAbbr(), 0l, regionName );
            if ( region != null ) {
                model.addAttribute("selectedRegion", region);
                model.addAttribute( "regionCorrectionUri", buildCorrectionsUri( region.getId(), request ) );
            }*/
            prepareModelAndCorrectionUrl( request, model, state.getAbbr(), regionName );
        }
        return super.showResult(request, model, user, useCount);
    }

    @RequestMapping(params = {CAPTHCA_PARAM})
    public String checkCaptcha( final HttpServletRequest request,
                                final ModelMap model,
                                @RequestParam(CAPTHCA_PARAM) String captchaInput,
                                final HttpSession theSession,
                                @ModelAttribute("user") final OverseasUser user,
                                @ModelAttribute(USE_COUNT) Integer useCount ) {
        return super.checkCaptcha( request, model, captchaInput, theSession, user, useCount );
    }

    @RequestMapping(value = "/{regionName}", params = {CAPTHCA_PARAM})
    public String checkCaptchaForRegion( final HttpServletRequest request,
                                final ModelMap model,
                                @RequestParam(CAPTHCA_PARAM) String captchaInput,
                                final HttpSession theSession,
                                @ModelAttribute("user") final OverseasUser user,
                                @ModelAttribute(USE_COUNT) Integer useCount,
                                @ModelAttribute("selectedState") final State state,
                                @PathVariable("regionName") String regionName ) {
        regionName = regionName.replace("%2e", ".");
        if ( state != null ) {
            /*EodRegion region = getSelectedRegion( model, state.getAbbr(), 0l, regionName );
            if ( region != null ) {
                model.addAttribute("selectedRegion", region);
                model.addAttribute( "regionCorrectionUri", buildCorrectionsUri( region.getId(), request ) );
            }*/
            prepareModelAndCorrectionUrl( request, model, state.getAbbr(), regionName );
        }
        return super.checkCaptcha( request, model, captchaInput, theSession, user, useCount);
    }

    private void prepareModelAndCorrectionUrl( final HttpServletRequest request,
                                               final ModelMap model, String stateAbbr, String regionName ) {
        EodRegion region = getSelectedRegion( model, stateAbbr, 0l, regionName );
        if ( region != null ) {
            model.addAttribute("selectedRegion", region);
            LocalOffice leo = (LocalOffice) model.get( "leo" );
            if ( leo != null ) {
                model.addAttribute( "regionCorrectionUri", buildCorrectionsUri( leo.getId(), request ) );
            }
        }

    }

}
