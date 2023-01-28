package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;

@Controller
@SessionAttributes(ICaptchaUse.USE_COUNT)
@RequestMapping(value = "/eod.htm")
public class ViewLeoController extends BaseEodController {


    public ViewLeoController() {
        setContentBlock( "/WEB-INF/pages/blocks/EodLocalStart.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/EodDisplay.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "Election Official Directory" );
    }

    @ModelAttribute(USE_COUNT)
    public Integer getUseCountAttribute() {
        return number4Captcha;
    }

    @ModelAttribute("pageUrl")
    public String getPageUrlAndOther( final HttpServletRequest request,
                                      final ModelMap model ) {
        return super.getPageUrlAndOther( request, model );
    }

    @ModelAttribute("selectedState")
    public State getSelectedState( @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        return super.getSelectedState( stateId );
    }

    //@ModelAttribute("regions")
    public Collection<VotingRegion> getRegions( @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        return getStateService().findRegionsForState( stateId );
    }

    @ModelAttribute("regions")
    public Collection<EodRegion> getEodRegions( @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        State state = getStateService().findState( stateId );
        if ( state == null ) {
            return Collections.emptyList();
        }
        return super.getEodRegions( state.getAbbr() );
    }

    @ModelAttribute("uocavaOffice")
    public LocalOffice getUocavaRegion( final HttpServletRequest request,
                                        @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId,
                                        final ModelMap model) {
        if ( stateId != null && stateId != 0 ) {
            State state = getStateService().findState( stateId );
            EodRegion uocavaRegion = state != null ? getEodApiService().findUocavaRegion( state.getAbbr() ) : null;
            LocalOffice uocava = uocavaRegion != null ? getEodApiService().getLocalOffice( uocavaRegion.getId().toString() ) : null;
            if ( uocava != null ) {
                model.addAttribute( "uocavaCorrectionUri", buildCorrectionsUri( uocava.getId(), request ) );
            }
            return uocava;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("selectedRegion")
    public EodRegion getSelectedRegion( final ModelMap model,
                               @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                               @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                               @RequestParam(value = "regionName", required = false, defaultValue = "") final String regionName ) {

        return super.getSelectedRegion( model, stateId, regionId, regionName );
    }


/*
    @ModelAttribute("regionCorrectionUri")
    public String getRegionCorrectionUrl( final HttpServletRequest request,
            @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId ) {
        if ( regionId != null ) {
            return buildCorrectionsUri( regionId, request );
        }
        return "";
    }
*/

    @RequestMapping(params ="!submission")
    public String showEodPage( final HttpServletRequest request,
                               final ModelMap model,
                               @ModelAttribute("user") final OverseasUser user,
                               @ModelAttribute(USE_COUNT) final Integer useCount ) {

        return super.showPage(request, model, user, useCount);
    }

    @RequestMapping(params = {CAPTHCA_PARAM, "submission"})
    public String checkCaptcha( final HttpServletRequest request,
                                final ModelMap model,
                                @RequestParam(CAPTHCA_PARAM) String captchaInput,
                                final HttpSession theSession,
                                @ModelAttribute("user") final OverseasUser user,
                                @ModelAttribute(USE_COUNT) Integer useCount ) {
        return super.checkCaptcha( request, model, captchaInput, theSession, user, useCount);
    }


    @RequestMapping(params = "submission")
    public String showEodResult(final HttpServletRequest request,
                                final ModelMap model,
                                @ModelAttribute("user") final OverseasUser user,
                                @ModelAttribute(USE_COUNT) Integer useCount) {
        return super.showResult(request, model, user, useCount);
    }

}
