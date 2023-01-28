package com.bearcode.ovf.faces;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.service.FacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date: 08.07.11
 * Time: 23:01
 *
 * @author Leonid Ginzburg
 */
@Controller
public class FaceLogoController {
    @Autowired
    private FacesService facesService;

    @RequestMapping(value = "/faceLogo/getLogo.do", method = RequestMethod.GET )
    public ModelAndView handleRequestInternal( HttpServletRequest request, HttpServletResponse response,
                                               @RequestParam(value = "configId",required = false,defaultValue = "0") Long configId) throws Exception {
        response.setHeader( "Cache-Control", "private" );
        long currentTime = System.currentTimeMillis();
        long tenMinutes = 20 * 60 * 1000; // In milliseconds
        response.setDateHeader( "Expires", currentTime + tenMinutes );


        FaceConfig config;
        FaceFlowLogo logo;
        if ( configId == 0 ) {
            // Config not defined - find logo and get default logo if first one not found
            String serverPath = request.getServerName() + request.getContextPath();

            config = facesService.findConfig( serverPath );
            logo = facesService.findLogoOfFace( config );
        } else {
            config = facesService.findConfigById( configId );
            logo = facesService.findLogo( config );
        }
        if ( logo != null ) {
            response.setHeader( "Content-Type", logo.getContentType() );
            response.getOutputStream().write( logo.getLogo() );
            response.getOutputStream().flush();
        }

        return null;
    }
}
