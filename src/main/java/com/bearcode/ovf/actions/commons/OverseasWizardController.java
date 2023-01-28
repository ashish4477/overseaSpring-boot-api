package com.bearcode.ovf.actions.commons;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.utils.SecurityContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Polyakov
 *         Date: Aug 27, 2007
 *         Time: 6:53:47 PM
 * @deprecated
 */
public abstract class OverseasWizardController extends AbstractWizardFormController implements AttributesDictionary {
    private String pageTitle = null;
    private String sectionCss = null;
	private String sectionName = null;
    private String successView = null;
	private boolean includeGoogleAnalytics = true;

	@Autowired
    private FacesService facesService;
 	@Autowired
    private StateService stateService;
    
    protected final String SUBMISSION_PARAMETER_NAME = "submission";
    protected final String LOGIN_REDIRECT_PARAMETER_NAME = "spring-security-redirect";
    
	protected String deploymentEnv = "";

    @SuppressWarnings("unchecked")
    protected Map referenceData(HttpServletRequest request, Object object, Errors errors, int i) throws Exception {
        Map references;
        try {
            references = buildReferences(request, object, errors, i);
        } catch (Exception e) {
            logger.error("",e);
            throw e;
        }
        if (references == null) references = new HashMap();

        // Get user information
        references.put(USER_DETAILS, SecurityContextHelper.getUser() );

        if ( getPageTitle() != null ) {
            references.put(TITLE,  getPageTitle());
        } else {
        }
        
        // add a reference to an external css
        String externalCss = MapUtils.getString( request.getParameterMap(), "css", "" );
        if(externalCss != ""){
        	try {
	        	URL u = new URL(externalCss);       	
	        	request.getSession().setAttribute(EXTERNAL_CSS, u.toString());
        	}
        	catch(MalformedURLException e){
        		request.getSession().removeAttribute(EXTERNAL_CSS);
        	}
        }
        if(request.getSession().getAttribute(EXTERNAL_CSS) != ""){
        	references.put(EXTERNAL_CSS,request.getSession().getAttribute(EXTERNAL_CSS));
        }
        
        references.put( SECTION_CSS, getSectionCss() );
        references.put(SECTION_NAME, getSectionName() );
        references.put( CURRENT_STEP, i);

        String serverPath = request.getServerName()+request.getContextPath();
        FaceConfig config = facesService.findConfig( serverPath );
        references.put(FACE_RELATIVE_PATH, config.getRelativePrefix());

        String contentName = getContentBlock(request, object, i); /*(String) references.get(CONTENT);*/
        //contentName = contentName.replaceAll("WEB-INF", "WEB-INF/"+config.getRelativePrefix() );
        contentName = facesService.getApprovedFileName( contentName, config.getRelativePrefix() );
        references.put(CONTENT, contentName);
        references.put( "states", stateService.findAllStates() );
		references.put("includeGoogleAnalytics", includeGoogleAnalytics);

        return references;
    }

    protected abstract Map buildReferences(HttpServletRequest request, Object object, Errors errors, int page);

    protected boolean isFormSubmission(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("post") || MapUtils.getBoolean( request.getParameterMap(), SUBMISSION_PARAMETER_NAME, false );
    }

    public abstract String getContentBlock(HttpServletRequest request, Object object,int page);

    public void setPage(String page) {
        setPages(new String[]{page});
    }

    protected String getViewName(HttpServletRequest request, Object object, int i) {
        return getPages()[0];
    }

    public String getPageTitle() {
        return pageTitle;
    }
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getSectionCss() {
        return sectionCss;
    }
    public void setSectionCss(String sectionCss) {
        this.sectionCss = sectionCss;
    }

    public String getSectionName() {
        return sectionName;
    }
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }


    public String getSuccessView() {
        return successView;
    }
    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public void setFacesService(FacesService facesService) {
        this.facesService = facesService;
    }

    public FacesService getFacesService() {
        return facesService;
    }

    public StateService getStateService() {
        return stateService;
    }

    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

	public void setDeploymentEnv(String deploymentEnv) {
		this.deploymentEnv = deploymentEnv;
	}

}
