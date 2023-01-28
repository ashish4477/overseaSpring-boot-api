package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.FacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * Date: 09.07.11
 * Time: 0:29
 *
 * @author Leonid Ginzburg
 */
@Controller
public class FaceLogoUpload {
    @Autowired
    private FacesService facesService;

    @RequestMapping( value = "/admin/FaceLogoUpload.htm", method = RequestMethod.POST )
    public String uploadLogo( @RequestParam("faceLogo") MultipartFile file,
                              @RequestParam("configId") Long configId ) {
        if ( !file.isEmpty() ) {
            FaceConfig config = facesService.findConfigById( configId );
            FaceFlowLogo logo = facesService.findLogo( config );
            if ( logo == null ) {
                logo = new FaceFlowLogo();
            }

            // Get user information
            if ( SecurityContextHolder.getContext().getAuthentication() != null ) {
                Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (userObj instanceof OverseasUser ) {

                    try {
                        logo.setUpdatedBy( (OverseasUser)userObj );
                        logo.setUpdatedTime( new Date() );
                        logo.setFaceConfig( config );
                        logo.setContentType( file.getContentType() );
                        logo.setLogo( file.getBytes() );

                        facesService.saveFaceLogo( logo );
                    } catch ( IOException e ) {
                        //todo logger
                    }
                }
            }

        }
        return String.format( "redirect:/admin/EditFaceConfig.htm?configId=%d", configId );
    }
}
