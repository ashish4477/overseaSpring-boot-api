package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.*;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import com.bearcode.ovf.webservices.grecaptcha.GReCaptchaService;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Extended {@link BaseController} that supports both the EOD (LEO) and SVID for domestic sites.
 */
@Controller
@SessionAttributes(ICaptchaUse.USE_COUNT)
public class DomesticEodController extends BaseController implements ICaptchaUse {

    private static final String EOD_DOMESTIC_TITLE = "Election Official Directory | Local State Election Offices  & Information for Voters | US Vote";
    private static final String EOD_DOMESTIC_DESCRIPTION = "Find local state election offices for all US counties in every state. Our comprehensive directory will get you the voter information you are looking for.";

    private static final String SVID_DOMESTIC_TITLE = "State Voting Requirements | Voter Information | US Vote";
    private static final String SVID_DOMESTIC_DESCRIPTION = "Voting Requirements & Information for all US counties in every state. Our comprehensive directory will get you local state by state voter information.";

    /**
     * the domestic EOD result JSP.
     */
    static final String DOMESTIC_EOD_RESULT_JSP = "/WEB-INF/pages/blocks/EodDisplayDomestic.jsp";

    /**
     * the domestic EOD start (local) JSP.
     */
    static final String DOMESTIC_EOD_START_JSP = "/WEB-INF/pages/blocks/EodLocalStart.jsp";

    /**
     * the default section CSS.
     */
    static final String DEFAULT_SECTION_CSS = "/css/eod.css";

    /**
     * the default section name.
     */
    static final String DEFAULT_SECTION_NAME = "eod";

    /**
     * the local official service.
     */
    @Autowired
    @Deprecated
    private LocalOfficialService localOfficialService;

    @Autowired
    private LocalElectionsService localElectionsService;

    @Autowired
    private EodApiService eodApiService;

    /**
     * the captcha service.
     */
    @Autowired
    private GReCaptchaService reCaptchaService;

    @Autowired
    private OvfPropertyService propertyService;
    /**
     * Consructs a domestic EOD controller with default values.
     */
    public DomesticEodController() {
        setContentBlock( DOMESTIC_EOD_START_JSP );
        setSuccessContentBlock( DOMESTIC_EOD_RESULT_JSP );
        setSectionCss( DEFAULT_SECTION_CSS );
        setSectionName( DEFAULT_SECTION_NAME );
        setPageTitle( "Information Directory" );
    }

    /**
     * Checks the captcha input for the EOD.
     *
     * @param request      the request.
     * @param model        the model map.
     * @param session      the HTTP session.
     * @param stateId      the state identifier.
     * @param regionId     the region identifier.
     * @param captchaInput the captcha input.
     * @param user         the user
     * @param useCount     the use count.
     * @return the resulting model and view.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    @RequestMapping(value = "/eoddomestic.htm", params = {CAPTHCA_PARAM, "submission"})
    public String checkCaptchaEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   final HttpSession session,
                                   @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                   @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                                   @RequestParam(CAPTHCA_PARAM) final String captchaInput,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(USE_COUNT) final Integer useCount ) {
        if ( !checkCaptcha( model, request, captchaInput, user, useCount ) ) {
            return showDomesticEod( request, model, stateId, regionId, user, null );
        }

        return showDomesticEodResult( request, model, stateId, regionId, user,
                ((useCount == null) || (useCount == 0)) ? number4Captcha : useCount );
    }

    /**
     * Checks the captcha input for the SVID.
     *
     * @param request      the request.
     * @param model        the model map.
     * @param session      the HTTP session.
     * @param stateId      the state identifier.
     * @param regionId     the region identifier.
     * @param captchaInput the captcha input.
     * @param user         the user
     * @param useCount     the use count.
     * @return the resulting model and view.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    @RequestMapping(value = "/sviddomestic.htm", params = {CAPTHCA_PARAM, "submission"})
    public String checkCaptchaSvid( final HttpServletRequest request,
                                    final ModelMap model,
                                    final HttpSession session,
                                    @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                    @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                                    @RequestParam(value = "voting_information", required = false, defaultValue = "0") final String votingInformation,
                                    @RequestParam(value = "location", required = false, defaultValue = "0") final String location,
                                    @RequestParam(CAPTHCA_PARAM) final String captchaInput,
                                    @ModelAttribute("user") final OverseasUser user,
                                    @ModelAttribute(USE_COUNT) final Integer useCount ) {
        if ( !checkCaptcha( model, request, captchaInput, user, useCount ) ) {
            return showDomesticSvid( request, model, stateId, regionId, votingInformation, location, user, null );
        }

        return showDomesticSvidResult( request, model, stateId, regionId, votingInformation, location, user,
                ((useCount == null) || (useCount == 0)) ? number4Captcha : useCount );
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
     * Gets the local official service.
     *
     * @return the local official service.
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    @Deprecated
    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
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
     * Gets the regions for the state from the request.
     *
     * @param stateId the state identifier.
     * @return the regions.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    @ModelAttribute("regions")
    public Collection<EodRegion> getRegions(
            @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId ) {
        State state = getStateService().findState( stateId );
        if ( state == null ) {
            return Collections.emptyList();
        }
        return getEodApiService().getRegionsOfState( state.getAbbr() );
    }

    /**
     * Gets the selected state from the request.
     *
     * @param stateId the state identifier from the request.
     * @return the selected state.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    @ModelAttribute("selectedState")
    public State getSelectedState( @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId ) {
        if ( stateId != 0l ) {
            return getStateService().findState( stateId );
        }

        return null;
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

    /**
     * Gets the use count.
     *
     * @return the use count.
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    @ModelAttribute(USE_COUNT)
    public Integer getUseCountAttribute() {
        return number4Captcha;
    }


    /**
     * Sets the local official service.
     *
     * @param localOfficialService the local official service to set.
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    @Deprecated
    public void setLocalOfficialService( final LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    /**
     * Shows the domestic EOD request page.
     *
     * @param request      the request.
     * @param model        the model map.
     * @param stateId      the state identifier.
     * @param regionId     the region identifier.
     * @param user         the user
     * @param useCount     the use count.
     * @return the resulting model and view.
     * @author IanBrown
     * @version Feb 3, 2012
     * @since Jan 17, 2012
     */
    @RequestMapping(value = "/eoddomestic.htm", params = "!submission")
    public String showDomesticEod( final HttpServletRequest request,
                                   final ModelMap model,
                                   @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                   @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                                   @ModelAttribute("user") final OverseasUser user,
                                   @ModelAttribute(USE_COUNT) final Integer useCount ) {
        model.addAttribute( "isEod", true );
        model.addAttribute( "isSvid", true );
        setCustomPageTitle(EOD_DOMESTIC_TITLE);
        setMetaDescription(EOD_DOMESTIC_DESCRIPTION);

        if ( stateId != 0l ) {
            if ( regionId != 0l ) {
                @SuppressWarnings("unchecked")
                final Collection<EodRegion> regions = (Collection<EodRegion>) model.get( "regions" );
                for ( final EodRegion region : regions ) {
                    if ( region.getId() == regionId ) {
                        final LocalOffice leo = getEodApiService().getLocalOffice( regionId.toString() );
                        model.addAttribute( "leo", leo );
                        model.addAttribute( "selectedRegion", region );
                        model.addAttribute( "regionCorrectionUri", buildCorrectionsUri( leo.getId(), request  ) );
                    }
                }
            }
        }

        determineNeedForCaptcha( request, user, useCount, model );

        final String modelAndView = buildModelAndView( request, model );
        return modelAndView;
    }

    /**
     * Shows the result of the domestic EOD page.
     *
     * @param request  the request.
     * @param model    the model.
     * @param stateId  the state identifier.
     * @param regionId the region identifier.
     * @param user     the user.
     * @param useCount the use count.
     * @return the resulting model and view.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 17, 2012
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping(value = "/eoddomestic.htm", params = "submission")
    public String showDomesticEodResult( final HttpServletRequest request,
                                         final ModelMap model,
                                         @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                         @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                                         @ModelAttribute("user") final OverseasUser user,
                                         @ModelAttribute(USE_COUNT) final Integer useCount ) {
        final String modelAndView;
        final FaceConfig faceConfig = getFaceConfig(request);
        setCustomPageTitle(EOD_DOMESTIC_TITLE);
        setMetaDescription(EOD_DOMESTIC_DESCRIPTION);

        if ( faceConfig.isUseCaptcha() && user == null ) {
            if ( (useCount == null) || (useCount == 0) ) {
                return showDomesticEod( request, model, stateId, regionId, user, useCount );
            }

            model.addAttribute( USE_COUNT, useCount - 1 );
        }

        if ( regionId != 0l ) {

            final LocalOffice leo = getEodApiService().getLocalOffice( regionId.toString() );
            if ( leo != null ) {
                model.addAttribute( "leo", leo );
                model.addAttribute( "regionCorrectionUri", buildCorrectionsUri( leo.getId(), request  ) );
                List<EodRegion> regions = (List<EodRegion>)model.get( "regions" );
                for ( EodRegion region : regions ) {
                    if ( region.getId().equals( regionId ) ) {
                        model.addAttribute( "selectedRegion", region );
                    }
                }
                addSvidToModel(stateId,model);

                modelAndView = buildSuccessModelAndView( request, model );
            }
            else {
                return showDomesticEod( request, model, stateId, regionId, user, useCount );
            }
        } else if ( stateId != 0l ) {
            addSvidToModel(stateId,model);

            modelAndView = buildSuccessModelAndView( request, model );

        } else {
            return showDomesticEod( request, model, stateId, regionId, user, useCount );
        }

        model.addAttribute( "isEod", true );
        model.addAttribute( "isSvid", true );
        return modelAndView;
    }

    /**
     * Shows the domestic SVID request page.
     *
     * @param request      the request.
     * @param model        the model map.
     * @param stateId      the state identifier.
     * @param regionId     the region identifier.
     * @param user         the user
     * @param useCount     the use count.
     * @return the resulting model and view.
     * @author IanBrown
     * @version Feb 3, 2012
     * @since Jan 17, 2012
     */
    @RequestMapping(value = "/sviddomestic.htm", params = "!submission")
    public String showDomesticSvid( final HttpServletRequest request,
                                    final ModelMap model,
                                    @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                    @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                                    @RequestParam(value = "voting_information", required = false, defaultValue = "0") final String votingInformation,
                                    @RequestParam(value = "location", required = false, defaultValue = "0") final String location,
                                    @ModelAttribute("user") final OverseasUser user,
                                    @ModelAttribute(USE_COUNT) final Integer useCount ) {
        model.addAttribute( "isEod", false );
        model.addAttribute( "isSvid", true );
        model.addAttribute( "votingInformation", votingInformation );
        model.addAttribute( "location", location );
        model.addAttribute( "stateId", stateId );
        setCustomPageTitle(SVID_DOMESTIC_TITLE);
        setMetaDescription(SVID_DOMESTIC_DESCRIPTION);

        determineNeedForCaptcha( request, user, useCount, model );

        final String modelAndView = buildModelAndView( request, model );
        return modelAndView;
    }

    /**
     * Shows the result of the domestic SVID request page.
     *
     * @param request      the request.
     * @param model        the model map.
     * @param stateId      the state identifier.
     * @param regionId     the region identifier.
     * @param user         the user
     * @param useCount     the use count.
     * @return the resulting model and view.
     * @author IanBrown
     * @version Feb 3, 2012
     * @since Jan 17, 2012
     */
    @RequestMapping(value = "/sviddomestic.htm", params = "submission")
    public String showDomesticSvidResult( final HttpServletRequest request,
                                          final ModelMap model,
                                          @RequestParam(value = "stateId", required = false, defaultValue = "0") final Long stateId,
                                          @RequestParam(value = "regionId", required = false, defaultValue = "0") final Long regionId,
                                          @RequestParam(value = "voting_information", required = false, defaultValue = "0") final String votingInformation,
                                          @RequestParam(value = "location", required = false, defaultValue = "0") final String location,
                                          @ModelAttribute("user") final OverseasUser user,
                                          @ModelAttribute(USE_COUNT) final Integer useCount ) {
        setCustomPageTitle(SVID_DOMESTIC_TITLE);
        setMetaDescription(SVID_DOMESTIC_DESCRIPTION);
        final FaceConfig faceConfig = getFaceConfig(request);

        if ( faceConfig.isUseCaptcha() && user == null ) {
            if ( (useCount == null) || (useCount == 0) ) {
                return showDomesticSvid( request, model, stateId, regionId, votingInformation, location, user, useCount );
            }

            model.addAttribute( USE_COUNT, useCount - 1 );
        }
        if ( stateId != 0l ) {
            model.addAttribute( "isEod", false );
            model.addAttribute( "isSvid", true );
            model.addAttribute( "votingInformation", votingInformation );
            model.addAttribute( "location", location );
            model.addAttribute( "stateId", stateId );
            addSvidToModel(stateId,model);
            final String modelAndView = buildSuccessModelAndView( request, model );
            return modelAndView;
        }

        return showDomesticSvid( request, model, stateId, regionId, votingInformation,location, user, useCount );
    }

    /**
     * Check the captcha input.
     *
     * @param model        the model.
     * @param request      the request.
     * @param captchaInput the captcha input.
     * @param user         the user.
     * @param useCount     the use count.
     * @return <code>true</code> if the captcha input is good (or there is a user), <code>false</code> otherwise.
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    private boolean checkCaptcha( final ModelMap model,
                                  final HttpServletRequest request,
                                  final String captchaInput,
                                  final OverseasUser user,
                                  final Integer useCount ) {
        final HttpSession session = request.getSession();
        final FaceConfig faceConfig = getFaceConfig( request );
        if ( faceConfig.isUseCaptcha() && (user == null) && ((useCount == null) || (useCount <= 0)) ) {
            //final String lowerCaptchaInput = captchaInput.toLowerCase();
            if ( !captchaInput.isEmpty() && reCaptchaService.verifyCaptcha( captchaInput, faceConfig.getRelativePrefix()) ) {
                model.addAttribute( USE_COUNT, number4Captcha );
                return true;
            }


            return false;
        }

        return true;
    }

    public LocalElectionsService getLocalElectionsService() {
        return localElectionsService;
    }

    public void setLocalElectionsService( LocalElectionsService localElectionsService ) {
        this.localElectionsService = localElectionsService;
    }

    /**
     * Determines if there is a need to perform the captcha - a <code>null</code> user and either a <code>null</code> or
     * non-positive use count.
     *
     * @param user     the user or <code>null</code>.
     * @param useCount the current use count or <code>null</code>.
     * @param model    the model - updated if it is determined there is a need to perform the captcha.
     * @author IanBrown
     * @version Jan 17, 2012
     * @since Jan 17, 2012
     */
    private void determineNeedForCaptcha( final HttpServletRequest request, final OverseasUser user, final Integer useCount, final ModelMap model ) {
        final FaceConfig faceConfig = getFaceConfig(request);
        if ( faceConfig.isUseCaptcha() && (user == null) && ((useCount == null) || (useCount <= 0)) ) {
            model.addAttribute( SHOW_CAPTHCA, true );
        }

    }

    private void addSvidToModel(Long stateId, final ModelMap model) {

        State state = getStateService().findState( stateId );
        if(state != null){
            final StateVoterInformation stateVoterInformation = localElectionsService.findStateVoterInformation(state.getAbbr());
            if(stateVoterInformation != null){
                model.addAttribute( "stateVoterInformation", stateVoterInformation );
                model.addAttribute( "identificationRequirements", LocalElectionsService.getGroupedIdentificationRequirements(stateVoterInformation) );
                model.addAttribute( "eligibilityRequirements", LocalElectionsService.getGroupedEligibilityRequirements( stateVoterInformation ) );
                model.addAttribute( "localElections", localElectionsService.findElectionsOfState( stateVoterInformation.getState().getShortName() ) );
                model.addAttribute( "amIRegistered", LocalElectionsService.getAmIRegisteredUrl( stateVoterInformation ) );
            }
        }
    }

    public EodApiService getEodApiService() {
        return eodApiService;
    }

    public void setEodApiService( EodApiService eodApiService ) {
        this.eodApiService = eodApiService;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected String buildSuccessModelAndView( HttpServletRequest request, ModelMap model ) {
        List<ElectionView> localElections = (List<ElectionView>) model.get( "localElections" );
        if ( localElections != null && localElections.size() > 0 ) {
            EodRegion region = (EodRegion) model.get( "selectedRegion" );
            // do filter
/*
            ElectionsFilter filter = new ElectionsFilter( region==null? null : region.getRegionName() );
            model.addAttribute( "localElections", filter.filterStateWide( localElections ) );
*/
            model.addAttribute( "localElections", localElections );
        }
        return super.buildSuccessModelAndView( request, model );
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE)
    public String getSiteKey() {
        return reCaptchaService.getGoogleSiteKey();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_SKIMM)
    public String getSiteKeySkim() {
        return reCaptchaService.getGoogleSiteKeyForSkimm();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_VOTE411)
    public String getSiteKeyVote411() {
        return reCaptchaService.getGoogleSiteKeyForVote411();
    }

    public GReCaptchaService getReCaptchaService() {
        return reCaptchaService;
    }

    public void setReCaptchaService( GReCaptchaService reCaptchaService ) {
        this.reCaptchaService = reCaptchaService;
    }

    public String getCorrectionsUri() {
        return propertyService.getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_TEMPLATE );
    }

    public String getCorrectionsIdPattern() {
        return propertyService.getProperty( OvfPropertyNames.EOD_API_CORRECTION_URL_PARAM );
    }

    public String buildCorrectionsUri( Long id, final HttpServletRequest request ) {
        String uri = getCorrectionsUri().replace( getCorrectionsIdPattern(), String.valueOf( id ) );
        if ( uri.startsWith( "http" ) ) {
            return uri;
        }

        return (!uri.contains( request.getContextPath().substring( 1 ) ) ? request.getContextPath() : "") + (uri.startsWith( "/" ) ? "" : "/") + uri;
    }
}
