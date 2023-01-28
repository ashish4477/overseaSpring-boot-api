package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.common.FaceFlowLogo;
import org.apache.commons.codec.binary.Base64;

/**
 * Date: 26.12.11
 * Time: 17:26
 *
 * @author Leonid Ginzburg
 */
public class FaceLogoFacade {
	private String contentType;
    private String logoBase64;

    public FaceLogoFacade() {
    }

    public FaceLogoFacade( FaceFlowLogo logo ) {
        contentType = logo.getContentType();
        logoBase64 = Base64.encodeBase64String( logo.getLogo() );
    }

    public void exportTo( FaceFlowLogo faceFlowLogo ) {
        faceFlowLogo.setLogo( Base64.decodeBase64( logoBase64 ) );
        faceFlowLogo.setContentType( contentType );
    }

    public String getLogoBase64() {
        return logoBase64;
    }

    public void setLogoBase64( String logoBase64 ) {
        this.logoBase64 = logoBase64;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }
}
