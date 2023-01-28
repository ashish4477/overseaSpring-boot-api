package com.bearcode.ovf.tools;

import com.bearcode.ovf.actions.questionnaire.admin.forms.FaceMigrationContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.migration.FaceConfigFacade;
import com.bearcode.ovf.model.migration.FaceInstructionFacade;
import com.bearcode.ovf.model.migration.FaceLogoFacade;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.utils.SecurityContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Date: 26.12.11
 * Time: 18:39
 *
 * @author Leonid Ginzburg
 */
@Component
public class FaceMigrationDealer {
    @Autowired
    private FacesService facesService;

    private Collection<FaceConfig> facesToPersist = new LinkedList<FaceConfig>();
    private Collection<FaceFlowLogo> logosToPersist = new LinkedList<FaceFlowLogo>();
    private Collection<FaceFlowInstruction> instructionsToPersist = new LinkedList<FaceFlowInstruction>();

    private void clear() {
        facesToPersist.clear();
        logosToPersist.clear();
        instructionsToPersist.clear();
    }

    public void doExport( FaceMigrationContext context ) {
        Collection<FaceConfig> faces = facesService.findAllConfigs();
        for ( FaceConfig face : faces ) {
            FaceConfigFacade facade = new FaceConfigFacade( face );
            FaceFlowLogo logo = facesService.findLogo( face );
            if ( logo != null ) {
                facade.setLogoFacade( new FaceLogoFacade( logo ) );
            }
            Collection<FaceFlowInstruction> instructions = facesService.findInstructions( face );
            for ( FaceFlowInstruction instruction : instructions ) {
                facade.getInstructionFacades().add( new FaceInstructionFacade( instruction ) );
            }
            context.getFaceConfigFacades().add( facade );
        }
    }

    public void doImport( FaceMigrationContext context ) {
        importFaces( context.getFaceConfigFacades() );

        facesService.saveMigration( this );
        clear();
    }

    private void importFaces( Collection<FaceConfigFacade> faceConfigFacades ) {
        Collection<FaceConfig> faces = facesService.findAllConfigs();
        for ( FaceConfigFacade faceConfigFacade : faceConfigFacades ) {
            FaceConfig face = findFace( faceConfigFacade, faces );
            if ( face == null ) {
                face = new FaceConfig();
            }
            faceConfigFacade.exportTo( face );
            facesToPersist.add( face );

            if ( faceConfigFacade.getLogoFacade() != null ) {
                FaceFlowLogo logo = null;
                if ( face.getId() != null && face.getId() > 0 ) {
                    logo = facesService.findLogo( face );
                }
                if ( logo == null ) {
                    logo = new FaceFlowLogo();
                    logo.setFaceConfig( face );
                }
                faceConfigFacade.getLogoFacade().exportTo( logo );
                logo.setUpdatedTime( new Date() );
                logo.setUpdatedBy( SecurityContextHelper.getUser() );
                logosToPersist.add( logo );
            }

            for ( FaceInstructionFacade instructionFacade : faceConfigFacade.getInstructionFacades() ) {
                FaceFlowInstruction instruction = null;
                if ( face.getId() != null && face.getId() > 0 ) {
                    instruction = facesService.findInstructionOfFlow( face, instructionFacade.getFlowType() );
                }
                if ( instruction == null ) {
                    instruction = new FaceFlowInstruction();
                    instruction.setFaceConfig( face );
                }
                instructionFacade.exportTo( instruction );
                instruction.setUpdatedTime( new Date() );
                instruction.setUpdatedBy( SecurityContextHelper.getUser() );
                instructionsToPersist.add( instruction );
            }
        }
    }

    private FaceConfig findFace( FaceConfigFacade faceConfigFacade, Collection<FaceConfig> faces ) {
        for ( FaceConfig face : faces ) {
            if ( face.getRelativePrefix().equals( faceConfigFacade.getRelativePrefix() ) ) {
                return face;
            }
        }
        return null;
    }


    public Collection<FaceConfig> getFacesToPersist() {
        return facesToPersist;
    }

    public void setFacesToPersist( Collection<FaceConfig> facesToPersist ) {
        this.facesToPersist = facesToPersist;
    }

    public Collection<FaceFlowLogo> getLogosToPersist() {
        return logosToPersist;
    }

    public void setLogosToPersist( Collection<FaceFlowLogo> logosToPersist ) {
        this.logosToPersist = logosToPersist;
    }

    public Collection<FaceFlowInstruction> getInstructionsToPersist() {
        return instructionsToPersist;
    }

    public void setInstructionsToPersist( Collection<FaceFlowInstruction> instructionsToPersist ) {
        this.instructionsToPersist = instructionsToPersist;
    }
}
