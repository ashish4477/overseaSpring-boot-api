package com.bearcode.ovf.faces;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.service.FacesService;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 6, 2007
 * Time: 5:08:37 PM
 * @author Leonid Ginzburg
 */
public class FacesVew extends JstlView {

    public static final String FACES_CONFIGURATION_SERVICE_NAME = "facesService";


    protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FacesService service = getApplicationContext().getBean( FACES_CONFIGURATION_SERVICE_NAME, FacesService.class );

        if ( service != null ) {
            String url = request.getServerName() + request.getContextPath();
            FaceConfig config = service.findConfig( url );
            String viewUrl = getUrl();
            viewUrl = service.getApprovedFileName( viewUrl, config.getRelativePrefix() );
            return viewUrl;
        }

        return super.prepareForRendering(request, response);
    }
}
