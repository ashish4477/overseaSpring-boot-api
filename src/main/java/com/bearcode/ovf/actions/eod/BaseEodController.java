package com.bearcode.ovf.actions.eod;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.ElectionView;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.utils.ElectionsFilter;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import com.bearcode.ovf.webservices.grecaptcha.GReCaptchaService;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.LocalElection;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

/**
 * Date: 19.05.14
 * Time: 15:37
 *
 * @author Leonid Ginzburg
 */
//@Component
public class BaseEodController extends BaseController implements ICaptchaUse {

    @Autowired
    @Deprecated
    private LocalOfficialService localOfficialService;

    @Autowired
    private GReCaptchaService reCaptchaService;

    @Autowired
    private LocalElectionsService localElectionsService;

    @Autowired
    private EodApiService eodApiService;

    @Autowired
    private OvfPropertyService propertyService;

    protected boolean showEod = true;
    protected boolean showSvid = true;

    private boolean isRegionAjax = false;


    public BaseEodController() {
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "Election Official Directory" );
    }

    public String getPageUrlAndOther( final HttpServletRequest request,
                                      final ModelMap model ) {
        final String regionLabel = MapUtils.getString(request.getParameterMap(), "regionLabel", "region");
        model.addAttribute( "regionLabel", regionLabel );
        model.addAttribute( "isEod", isShowEod() );
        model.addAttribute( "isSvid", isShowSvid() );
        return request.getServletPath();
    }

    public State getSelectedState(  Long stateId ) {
        if ( stateId != null && stateId > 0 ) {
            return getStateService().findState( stateId );
        }
        return null;
    }

    @Deprecated
    public Collection<VotingRegion> getRegions( Long stateId ) {
        return getStateService().findRegionsForState( stateId );
    }

    public Collection<EodRegion> getEodRegions( String stateShortName ) {
        return getEodApiService().getRegionsOfState( stateShortName );
    }

    public EodRegion getSelectedRegion( final ModelMap model,
                                        final Long stateId,
                                        final Long regionId,
                                        final String regionName ) {
        if ( stateId != null && stateId != 0 ) {
            State state = getStateService().findState( stateId );
            if ( state != null ) {
                return getSelectedRegion( model, state.getAbbr(), regionId, regionName );
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public EodRegion getSelectedRegion( final ModelMap model,
                               final String stateAbbr,
                               final Long regionId,
                               final String regionName ) {

        EodRegion resultRegion = null;
        if ( stateAbbr != null && StringUtils.isNotEmpty( stateAbbr ) ) {
            //if ( isShowSvid() ) {
                final StateVoterInformation svid = localElectionsService.findStateVoterInformation( stateAbbr );
                model.addAttribute( "stateVoterInformation", svid );
                if ( svid != null ) {
                    final List<ElectionView> localElections = localElectionsService.findElectionsOfState( stateAbbr );
                    model.addAttribute( "localElections", localElections );
                    model.addAttribute( "identificationRequirements", LocalElectionsService.getGroupedIdentificationRequirements( svid ) );
                    model.addAttribute( "eligibilityRequirements", LocalElectionsService.getGroupedEligibilityRequirements( svid ) );
                    model.addAttribute( "amIRegistered", LocalElectionsService.getAmIRegisteredUrl( svid ) );
                }

            //}
            if ( isShowEod() ) {
                Collection<EodRegion> regions = (Collection<EodRegion>) model.get( "regions" );
                if ( regions == null ) {
                    regions = this.getEodRegions( stateAbbr );
                }
                if ( regionId != 0 || regionName.length() > 0 ) {
                    // make sure the voting region is in this state
                    for ( final EodRegion region : regions ) {
                        if ( regionId.equals( region.getId() ) ) {
                            final LocalOffice leo = getEodApiService().getLocalOffice( regionId.toString() );
                            model.addAttribute( "leo", leo );
                            resultRegion = region;
                            break;
                        }
                        if ( region.getName().equalsIgnoreCase( regionName ) ) {
                            final LocalOffice leo = getEodApiService().getLocalOffice( region.getId().toString() );
                            model.addAttribute( "leo", leo );
                            resultRegion = region;
                            break;
                        }
                    }
                }
            }
        }
        List<ElectionView> localElections = (List<ElectionView>) model.get( "localElections" );
        if ( localElections != null && localElections.size() > 0 ) {
            // do filter
/*
            ElectionsFilter filter = new ElectionsFilter( resultRegion==null? null : resultRegion.getRegionName() );
            model.addAttribute( "localElections", filter.filterStateWide( localElections ) );
*/
            model.addAttribute( "localElections", localElections );
        }
        return resultRegion;
    }

    public String showPage(final HttpServletRequest request,
                           final ModelMap model,
                           final OverseasUser user,
                           final Integer useCount) {

        FaceConfig faceConfig = getFaceConfig(request);
        if ( faceConfig.isUseCaptcha() && user == null ) {  //anonymous
            if ( useCount == null || useCount <= 0 ) {
                model.addAttribute( SHOW_CAPTHCA, true );
            }
        }
        return buildModelAndView( request, model );
    }

    public String checkCaptcha( final HttpServletRequest request,
                                final ModelMap model,
                                String captchaInput,
                                final HttpSession theSession,
                                final OverseasUser user,
                                Integer useCount ) {
        FaceConfig faceConfig = getFaceConfig(request);
        if ( faceConfig.isUseCaptcha() && user == null ) {
            if ( useCount == null || useCount <= 0 ) {
                //captchaInput = captchaInput.toLowerCase();
                if ( !captchaInput.equals( "" ) && reCaptchaService.verifyCaptcha( captchaInput, faceConfig.getRelativePrefix() ) ) {
                    model.addAttribute( USE_COUNT, number4Captcha );
                    useCount = number4Captcha;
                }
                if ( useCount == null || useCount <= 0 ) {
                    // kind of reject. show first page again
                    return this.showPage(request, model, user, useCount);
                }
            }
        }
        if ( model.get( "selectedRegion" ) != null
                || ( model.get( "selectedState" ) != null && isShowSvid() ) ) {
            LocalOffice leo = (LocalOffice) model.get( "leo" );
            if ( leo != null ) {
                model.addAttribute( "regionCorrectionUri", buildCorrectionsUri( leo.getId(), request ) );
            }
            return buildSuccessModelAndView( request, model );
        }
        else {
            return buildModelAndView( request, model );
        }
    }


    public String showResult(final HttpServletRequest request,
                             final ModelMap model,
                             final OverseasUser user,
                             Integer useCount) {

        FaceConfig faceConfig = getFaceConfig(request);

        if ( model.get( "selectedRegion" ) != null ||
                ( model.get( "selectedState" ) != null && isShowSvid() ) ) {
            // check CAPTHCA if region is selected or state is selected and SVID

            if ( faceConfig.isUseCaptcha() && user == null ) {
                if ( useCount != null && useCount > 0 ) {
                    model.addAttribute( USE_COUNT, --useCount );
                } else {
                    return this.showPage(request, model, user, useCount);
                }
            }
            LocalOffice leo = (LocalOffice) model.get( "leo" );
            if ( leo != null ) {
                model.addAttribute( "regionCorrectionUri", buildCorrectionsUri( leo.getId(), request ) );
            }
            return buildSuccessModelAndView( request, model );
        }
        return this.showPage(request, model, user, useCount);
    }

    @Deprecated
    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    @Deprecated
    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    public void setCaptchaService( GReCaptchaService reCaptchaService ) {
        this.reCaptchaService = reCaptchaService;
    }

    public boolean isShowEod() {
        return showEod;
    }

    public void setShowEod( boolean showEod ) {
        this.showEod = showEod;
    }

    public boolean isShowSvid() {
        return showSvid;
    }

    public void setShowSvid( boolean showSvid ) {
        this.showSvid = showSvid;
    }

    public boolean isRegionAjax() {
        return isRegionAjax;
    }

    public void setRegionAjax( boolean regionAjax ) {
        isRegionAjax = regionAjax;
    }

    public LocalElectionsService getLocalElectionsService() {
        return localElectionsService;
    }

    public EodApiService getEodApiService() {
        return eodApiService;
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

    public GReCaptchaService getCaptchaService() {
        return reCaptchaService;
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
