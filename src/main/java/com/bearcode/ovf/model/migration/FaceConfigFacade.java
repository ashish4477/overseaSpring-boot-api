package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.common.FaceConfig;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Date: 26.12.11
 * Time: 17:15
 *
 * @author Leonid Ginzburg
 */
public class FaceConfigFacade {
    private String urlPath = "";
    private String relativePrefix = "";
    private boolean defaultPath = false;
    private boolean scytlIntegration = false;
    private boolean useEnvelope = false;
    private boolean useFaxPage = false;
    private boolean useNotarizationPage = false;
    private boolean useBlankAddendumPage = false;
    private String externalContentUrl = "";

    private FaceLogoFacade logoFacade;

    private Collection<FaceInstructionFacade> instructionFacades = new LinkedList<FaceInstructionFacade>();

    public FaceConfigFacade() {
    }

    public FaceConfigFacade( FaceConfig face ) {
        urlPath = face.getUrlPath();
        relativePrefix = face.getRelativePrefix();
        defaultPath = face.isDefaultPath();
        scytlIntegration = face.isScytlIntegration();
        useEnvelope = face.isUseEnvelope();
        useFaxPage = face.isUseFaxPage();
        useNotarizationPage = face.isUseNotarizationPage();
        useBlankAddendumPage = face.isUseBlankAddendumPage();
        externalContentUrl = face.getExternalContentUrl();
    }

    public void exportTo( FaceConfig face ) {
        if ( face.getId() == null || face.getId().equals( 0L ) ) {
            face.setUrlPath( urlPath );
        }
        face.setRelativePrefix( relativePrefix );
        face.setDefaultPath( defaultPath );
        face.setScytlIntegration( scytlIntegration );
        face.setUseEnvelope( useEnvelope );
        face.setUseFaxPage( useFaxPage );
        face.setUseNotarizationPage( useNotarizationPage );
        face.setUseBlankAddendumPage( useBlankAddendumPage );
        face.setExternalContentUrl( externalContentUrl );
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath( String urlPath ) {
        this.urlPath = urlPath;
    }

    public String getRelativePrefix() {
        return relativePrefix;
    }

    public void setRelativePrefix( String relativePrefix ) {
        this.relativePrefix = relativePrefix;
    }

    public boolean isDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath( boolean defaultPath ) {
        this.defaultPath = defaultPath;
    }

    public boolean isScytlIntegration() {
        return scytlIntegration;
    }

    public void setScytlIntegration( boolean scytlIntegration ) {
        this.scytlIntegration = scytlIntegration;
    }

    public boolean isUseEnvelope() {
        return useEnvelope;
    }

    public void setUseEnvelope( boolean useEnvelope ) {
        this.useEnvelope = useEnvelope;
    }

    public boolean isUseFaxPage() {
        return useFaxPage;
    }

    public void setUseFaxPage( boolean useFaxPage ) {
        this.useFaxPage = useFaxPage;
    }

    public boolean isUseNotarizationPage() {
        return useNotarizationPage;
    }

    public void setUseNotarizationPage( boolean useNotarizationPage ) {
        this.useNotarizationPage = useNotarizationPage;
    }

    public boolean isUseBlankAddendumPage() {
        return useBlankAddendumPage;
    }

    public void setUseBlankAddendumPage( boolean useBlankAddendumPage ) {
        this.useBlankAddendumPage = useBlankAddendumPage;
    }

    public String getExternalContentUrl() {
        return externalContentUrl;
    }

    public void setExternalContentUrl( String externalContentUrl ) {
        this.externalContentUrl = externalContentUrl;
    }

    public FaceLogoFacade getLogoFacade() {
        return logoFacade;
    }

    public void setLogoFacade( FaceLogoFacade logoFacade ) {
        this.logoFacade = logoFacade;
    }

    public Collection<FaceInstructionFacade> getInstructionFacades() {
        return instructionFacades;
    }

    public void setInstructionFacades( Collection<FaceInstructionFacade> instructionFacades ) {
        this.instructionFacades = instructionFacades;
    }
}
