package com.bearcode.ovf.actions.commons;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.utils.SecurityContextHelper;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 22, 2007
 * Time: 7:23:36 PM
 * @deprecated
 */
public abstract class OverseasFormController extends SimpleFormController implements AttributesDictionary {

	private String contentBlock = null;
    private String successContentBlock = null;
    private String pageTitle = null;
    private String sectionCss = null;
	private String sectionName = null;
	private boolean showMetaKeywords = false;

    private StateService stateService;
    private FacesService facesService;
    //protected Logger log = LoggerFactory.getLogger( getClass() );

	protected String deploymentEnv = "";

    @SuppressWarnings("unchecked")
    protected Map referenceData(HttpServletRequest request, Object object, Errors errors) throws Exception {

        Map references;
        try {
            references = buildReferences(request, object, errors);
        } catch (Exception e) {
            logger.error("",e);
            throw e;
        }
        if (references == null) references = new HashMap();

        // Get user information
       	references.put("userDetails", SecurityContextHelper.getUser() );
        if ( getContentBlock() != null ) {
            references.put(CONTENT, getContentBlock() );
        }
        if ( getPageTitle() != null ) {
            references.put(TITLE,  getPageTitle());
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

        references.put(SECTION_CSS, getSectionCss() );
        references.put(SECTION_NAME, getSectionName() );
        references.put(SHOW_META_KEYWORDS, isShowMetaKeywords() );
        references.put( "states", stateService.findAllStates() );

        String serverPath = request.getServerName()+request.getContextPath();
        FaceConfig config = facesService.findConfig( serverPath );
        references.put(FACE_RELATIVE_PATH, config.getRelativePrefix());

        String contentName = (String) references.get(CONTENT);
        if ( contentName != null ) {
            //contentName = contentName.replaceAll("WEB-INF", "WEB-INF/"+config.getRelativePrefix() );
            contentName = facesService.getApprovedFileName( contentName, config.getRelativePrefix() );
            references.put(CONTENT, contentName);
        }
        return references;
    }

    abstract public Map buildReferences(HttpServletRequest request, Object object, Errors errors) throws Exception ;

    /**
     * This fuction prevent exception throwing in case when command isn't defined
     * @param request current HTTP request
     * @return the backing object
     * @throws Exception in case of invalid state or arguments
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        if(getCommandName()==null || getCommandName().length()<1) {
            setCommandName("dummy");
        }
        if(getCommandClass()==null) {
            setCommandClass(StringBuffer.class);
        }
        return super.formBackingObject(request);
    }

    @SuppressWarnings("unchecked")
    protected ModelAndView buildSuccessModelAndView(HttpServletRequest request, Object command, BindException errors, String modelName, Object modelObject ) throws Exception {
        Map model = referenceData(request, command, errors );
        String relative = (String) model.get(FACE_RELATIVE_PATH);
        String successContent = facesService.getApprovedFileName( getSuccessContentBlock(), relative );
        model.put(CONTENT, successContent /*getSuccessContentBlock().replaceAll("WEB-INF", "WEB-INF/"+relative )*/ );
        model.put( getCommandName(), command );
        model.put( modelName, modelObject );
        return new ModelAndView( getFormView(), model );
    }

    @SuppressWarnings("unchecked")
    protected ModelAndView buildSuccessModelAndView(HttpServletRequest request, Object command, BindException errors, Map model ) throws Exception {
        Map inernalModel = referenceData(request, command, errors );
        String relative = (String) inernalModel.get(FACE_RELATIVE_PATH);
        String successContent = facesService.getApprovedFileName( getSuccessContentBlock(), relative );
        inernalModel.put(CONTENT, successContent /*getSuccessContentBlock().replaceAll("WEB-INF", "WEB-INF/"+relative )*/ );
        inernalModel.put( getCommandName(), command );
        inernalModel.putAll( model );
        return new ModelAndView( getFormView(), inernalModel );
    }

    protected boolean isFormSubmission(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("post") || MapUtils.getBoolean( request.getParameterMap(), "submission", false );
    }


    public String getContentBlock() {
        return contentBlock;
    }

    public void setContentBlock(String contentBlock) {
        this.contentBlock = contentBlock;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public StateService getStateService() {
        return stateService;
    }

    public void setStateService(StateService stateService) {
        this.stateService = stateService;
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


    public String getSuccessContentBlock() {
        return successContentBlock;
    }

    public void setSuccessContentBlock(String successContentBlock) {
        this.successContentBlock = successContentBlock;
    }

    public void setFacesService(FacesService facesService) {
        this.facesService = facesService;
    }

    public FacesService getFacesService() {
        return facesService;
    }

	public boolean isShowMetaKeywords() {
		return showMetaKeywords;
	}

	public void setShowMetaKeywords(boolean showMetaKeywords) {
		this.showMetaKeywords = showMetaKeywords;
	}

	public void setDeploymentEnv(String deploymentEnv) {
		this.deploymentEnv = deploymentEnv;
	}
}
