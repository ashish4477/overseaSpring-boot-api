package com.bearcode.ovf.actions.commons;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.model.common.FaceBookApi;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.FaceBookApiService;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.utils.SecurityContextHelper;
import com.bearcode.ovf.utils.TlsContextHelper;
import com.google.common.collect.Sets;
import org.apache.commons.httpclient.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 22, 2007
 * Time: 7:23:36 PM
 */
public abstract class BaseController implements AttributesDictionary {

    protected Logger logger = LoggerFactory.getLogger( BaseController.class );
    private String contentBlock = null;
    private String successContentBlock = null;
    private String pageTitle = null;
    private String customPageTitle = null;
    private String metaDescription = null;
    private String sectionCss = null;
    private String sectionName = null;
    private boolean showMetaKeywords = false;

    @Autowired
    protected StateService stateService;

    @Autowired
    protected FacesService facesService;

    @Autowired
    protected FaceBookApiService faceBookApiService;

    protected String mainTemplate = "templates/MainTemplate";

    protected String deploymentEnv = "";

    protected final static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat( "yyyy" );

    /**
     * Builds model map and returns main template name as view name.
     * Puts all common and necessary data into the map.
     * Transform content block name according to faces configuration
     *
     * @param request Http Request
     * @param model   Model map
     * @return view name
     */
    protected String buildModelAndView( HttpServletRequest request, ModelMap model ) {

        if ( getContentBlock() != null ) {
            model.addAttribute( CONTENT, getContentBlock() );
        }
        prepareModel( request, model );
        return mainTemplate;
    }

    protected void prepareModel( HttpServletRequest request, ModelMap model ) {
        // Get user information
        model.addAttribute( "userDetails", getUser() );

        // add a reference to an external css
        String externalCss = MapUtils.getString( request.getParameterMap(), "css", "" );
        if ( !externalCss.equals( "" ) ) {
            try {
                URL u = new URL( externalCss );
                request.getSession().setAttribute( EXTERNAL_CSS, u.toString() );
            } catch ( MalformedURLException e ) {
                request.getSession().removeAttribute( EXTERNAL_CSS );
            }
        }
        if ( request.getSession().getAttribute( EXTERNAL_CSS ) != "" ) {
            model.addAttribute( EXTERNAL_CSS, request.getSession().getAttribute( EXTERNAL_CSS ) );
        }

        if ( getPageTitle() != null ) {
            model.addAttribute( TITLE, getPageTitle() );
        }
        if ( getCustomPageTitle() != null ) {
            model.addAttribute( CUSTOM_TITLE, getCustomPageTitle() );
        }
        if ( getMetaDescription() != null ) {
            model.addAttribute( META_DESCRIPTION, getMetaDescription() );
        }
        model.addAttribute( SECTION_CSS, getSectionCss() );
        model.addAttribute( SECTION_NAME, getSectionName() );
        model.addAttribute( SHOW_META_KEYWORDS, isShowMetaKeywords() );
        model.addAttribute( "states", stateService.findAllStates() );
        model.addAttribute( "countries", stateService.findAllCountries() );

        FaceConfig config = getFaceConfig( request );
        model.addAttribute( FACE_RELATIVE_PATH, config.getRelativePrefix() );

        FaceBookApi fbApi = getFaceBookApi( request );
        if ( fbApi != null ) {
            model.addAttribute( "facebookAppId", fbApi.getAppKey() );
        }

        String contentName = (String) model.get( CONTENT );
        if ( contentName != null ) {
            contentName = facesService.getApprovedFileName( contentName, config.getRelativePrefix() );
            model.addAttribute( CONTENT, contentName );
        }

        model.addAttribute( "yearNumber", YEAR_FORMAT.format( new Date() ) );
    }

    /**
     * Builds model map and returns main template name as view name for success page.
     * Puts all common and necessary data into the map.
     * Replace content block name with success content block name.
     * Transform success content block name according to faces configuration
     *
     * @param request Http Request
     * @param model   Model map
     * @return view name
     */
    protected String buildSuccessModelAndView( HttpServletRequest request, ModelMap model ) {
        if ( getSuccessContentBlock() != null ) {
            model.addAttribute( CONTENT, getSuccessContentBlock() );
        } else if ( getContentBlock() != null ) {
            model.addAttribute( CONTENT, getContentBlock() );
        }
        prepareModel( request, model );
        return mainTemplate;
    }

    public ResponseEntity<String> sendMethodNotAllowed() {
        HttpHeaders headers =  new HttpHeaders();
        Set<HttpMethod> allowed = new HashSet<HttpMethod>( 2 );
        Collections.addAll( allowed, HttpMethod.GET, HttpMethod.POST  );
        headers.setAllow( allowed );
        return new ResponseEntity<String>(headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    public String getContentBlock() {
        return contentBlock;
    }

    public void setContentBlock( String contentBlock ) {
        this.contentBlock = contentBlock;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle( String pageTitle ) {
        this.pageTitle = pageTitle;
    }

    public String getCustomPageTitle() {
        return customPageTitle;
    }

    public void setCustomPageTitle(String customPageTitle) {
        this.customPageTitle = customPageTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getSectionCss() {
        return sectionCss;
    }

    public void setSectionCss( String sectionCss ) {
        this.sectionCss = sectionCss;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName( String sectionName ) {
        this.sectionName = sectionName;
    }


    public String getSuccessContentBlock() {
        return successContentBlock;
    }

    public void setSuccessContentBlock( String successContentBlock ) {
        this.successContentBlock = successContentBlock;
    }

    public FacesService getFacesService() {
        return facesService;
    }

    public StateService getStateService() {
        return stateService;
    }

    public boolean isShowMetaKeywords() {
        return showMetaKeywords;
    }

    public void setShowMetaKeywords( boolean showMetaKeywords ) {
        this.showMetaKeywords = showMetaKeywords;
    }

    public void setDeploymentEnv( String deploymentEnv ) {
        this.deploymentEnv = deploymentEnv;
    }

    protected FaceBookApi getFaceBookApi( HttpServletRequest request ) {
        return faceBookApiService.findForSubDomain( request.getServerName() );
    }

    /**
     * Returns the logged-in user or null
     *
     * @return currently logged in user
     */
    @ModelAttribute("user")
    protected OverseasUser getUser() {
        return SecurityContextHelper.getUser();
    }

    protected FaceConfig getFaceConfig( HttpServletRequest request ) {
        return facesService.findConfig( request.getServerName() + request.getContextPath() );
    }

    protected String buildFullUrl( HttpServletRequest request, String uri ) {
        return (request.getLocalPort() == 80 ? "http" : "https") +
                "://" +
                request.getServerName() + request.getContextPath() +
                ((uri.startsWith( "/" )) ? uri : "/" + uri);
    }

    /**
     * Utility method for instantiating a HttpClient that can handle https requests
     * in all deployment environments
     *
     * @return HttpClient
     */
    protected HttpClient getHttpClient() {

        /*
              On the staging server we are using the production SSL cert, which is invalid for the
              staging URLs (*.staging.overseasvotefoundation.org), so we need to use a custom TrustManager
              to unconditionally accept the cert. We could perhaps get around this by adding the cert to
              the staging trusted cert store, but then we have to update it whenever a new cert is installed.
          */
        if ( !deploymentEnv.equalsIgnoreCase( "production" ) ) {
            TlsContextHelper.useCustomTrustManager();
        }
        return new HttpClient();
    }


    /**
     * Returns a set of OverseasUser field names to skip during validation
     *
     * @param request
     * @return Set<String> fieldNamesToSkip
     */
    protected Set<String> getUserValidationSkipFields( HttpServletRequest request ) {
        FaceConfig faceConfig = getFaceConfig( request );
        String[] skipArr = faceConfig.getUserValidationSkipFields().split( "," );
        Set<String> skip = new HashSet<String>( skipArr.length );
        for ( String s : skipArr ) {
            skip.add( s.replaceAll( "/\\s+/", "" ) );
        }
        return skip;
    }

    protected void adduserValidationFieldsToSkip(HttpServletRequest request, ModelMap model){

        Set<String> fieldsToSkip = getUserValidationSkipFields(request);
        // We use a Map here because of jstl lameness in checking the contents of a list
        HashMap<String,String> toSkip = new HashMap<String,String>();
        for(String field: fieldsToSkip){
            toSkip.put(field, field);
        }
        model.addAttribute("userValidationFieldsToSkip",toSkip);
    }


}
