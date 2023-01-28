package com.bearcode.ovf.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bearcode.ovf.DAO.MailingListDAO;
import com.bearcode.ovf.model.mail.FaceMailingList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bearcode.ovf.DAO.FaceConfigDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.tools.FaceMigrationDealer;
import com.bearcode.ovf.utils.ResourceUtils;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Nov 6, 2007 Time: 8:20:59 PM
 *
 * @author Leonid Ginzburg
 */
@Service
public class FacesService {

    @Autowired
    private FaceConfigDAO faceConfigDAO;

    @Autowired
    private MailingListDAO mailingListDAO;

    private final Map<String, FaceConfig> cachePathToFaceConfig;

    private final Map<String, FaceConfig> cachePrefixToFaceConfig;

    private FaceConfig cacheDefaultConfig;

    public FacesService() {
        cachePathToFaceConfig = new HashMap<String, FaceConfig>();
        cachePrefixToFaceConfig = new HashMap<String, FaceConfig>();
    }

    public Collection<FaceConfig> findAllConfigs() {
        return faceConfigDAO.findConfigs();
    }

    public FaceConfig findConfig( final String path ) {
        FaceConfig config = cachePathToFaceConfig.get( path );
        /*
        if ( config != null ) {
            return config; // cache hit
        }
        */
        config = faceConfigDAO.findConfigByPath( path );
        if ( config == null ) {
            config = new FaceConfig();
        }

        cachePathToFaceConfig.put( path, config );
        return config;
    }

    public FaceConfig findConfigById( final long id ) {
        return faceConfigDAO.findById( id );
    }

    public FaceConfig findConfigByPrefix( final String prefix ) {
        FaceConfig config = cachePrefixToFaceConfig.get( prefix );
        if ( config != null ) {
            return config; // Cache hit.
        }

        config = faceConfigDAO.findConfigByPrefix( prefix );
        if ( config == null ) {
            config = new FaceConfig();
        }

        cachePrefixToFaceConfig.put( prefix, config );
        return config;
    }

    public FaceConfig findDefaultConfig() {
        if ( cacheDefaultConfig == null ) {
            cacheDefaultConfig = faceConfigDAO.getDefaultConfig();
        }
        return cacheDefaultConfig;
    }

    public FaceFlowInstruction findInstructionById( final Long id ) {
        return faceConfigDAO.findInstructionById( id );
    }

    public FaceFlowInstruction findInstructionOfFlow( final FaceConfig config, final FlowType flowType ) {
        FaceFlowInstruction instruction = faceConfigDAO.findInstructionOfFlow( config, flowType );

        if ( instruction == null ) {
            final FaceConfig defaultConfig = faceConfigDAO.getDefaultConfig();
            instruction = faceConfigDAO.findInstructionOfFlow( defaultConfig, flowType );
        }
        return instruction;
    }

    public Collection<FaceFlowInstruction> findInstructions() {
        return faceConfigDAO.findAllInstructions();
    }

    public Collection<FaceFlowInstruction> findInstructions( final FaceConfig faceConfig ) {
        return faceConfigDAO.findInstructions( faceConfig );
    }

    /**
     * Get FaceFlowLogo for a face.
     *
     * @param faceConfig The face
     * @return returns null if logo is not specified
     */
    public FaceFlowLogo findLogo( final FaceConfig faceConfig ) {
        return faceConfigDAO.findLogo( faceConfig );
    }

    /**
     * Get FaceFlowLogo for a face
     *
     * @param faceConfig
     * @return returns logo for default face if logo is not specified for the given face
     */
    public FaceFlowLogo findLogoOfFace( final FaceConfig faceConfig ) {
        FaceFlowLogo logo = faceConfigDAO.findLogo( faceConfig );
        if ( logo == null ) {
            final FaceConfig defaultConfig = faceConfigDAO.getDefaultConfig();
            logo = faceConfigDAO.findLogo( defaultConfig );
        }
        return logo;
    }

	public String getApprovedFileName(final String rawPath, final String relativePath) {
		// try to find resource using current face
		String path = resExists(rawPath, relativePath);
		if (path != null)
			return path;

		// try to find resource using default face
		final FaceConfig config = this.findDefaultConfig();
		path = resExists(rawPath, config.getRelativePrefix());
		return path != null ? path : "";
	}

	private String resExists(final String rawPath, final String relativePath) {
		final String rel = rawPath.replaceAll("WEB-INF", "WEB-INF/" + relativePath);
		final String tryRel = rel.replaceAll( "/WEB-INF", ".." );
		return ResourceUtils.exists(tryRel) ? rel : null;
	}

	/**
	 * Opens the resource for the specified path.
	 * @author IanBrown
	 * @param resourcePath the resource path.
	 * @return the resource input stream.
	 * @since Sep 6, 2012
	 * @version Sep 6, 2012
	 */
	public InputStream openResource(final String resourcePath) {
		final String path = resourcePath.replaceAll("/WEB-INF", "..");
		return ResourceUtils.openResource(path);
	}
	
    public void invalidateCache() {
        cachePathToFaceConfig.clear();
        cachePrefixToFaceConfig.clear();
        cacheDefaultConfig = null;
    }

    public void saveConfig( final FaceConfig config ) {
        if ( config.isDefaultPath() ) {
            final FaceConfig oldDefault = faceConfigDAO.getDefaultConfig();
            if ( !oldDefault.getId().equals( config.getId() ) ) {
                oldDefault.setDefaultPath( false );
                faceConfigDAO.makePersistent( oldDefault );
            }
        }
        faceConfigDAO.makePersistent( config );
        invalidateCache();
    }

    public void saveFaceLogo( final FaceFlowLogo logo ) {
        faceConfigDAO.makePersistent( logo );
    }

    public void saveInstruction( final FaceFlowInstruction instruction, final OverseasUser user ) {
        instruction.setUpdatedBy( user );
        instruction.setUpdatedTime( new Date() );
        faceConfigDAO.makePersistent( instruction );
    }

    public void saveMigration( final FaceMigrationDealer faceMigrationDealer ) {
        faceConfigDAO.makeAllPersistent( faceMigrationDealer.getFacesToPersist() );
        faceConfigDAO.makeAllPersistent( faceMigrationDealer.getInstructionsToPersist() );
        faceConfigDAO.makeAllPersistent( faceMigrationDealer.getLogosToPersist() );
    }

    public FaceMailingList findFaceMailingList( FaceConfig faceConfig ) {
        return faceConfigDAO.findFaceMailingList( faceConfig );
    }

    public void saveFaceMailingList( FaceConfig faceConfig, Long mailingListId ) {
        FaceMailingList faceMailingList = faceConfigDAO.findFaceMailingList( faceConfig );

        if ( mailingListId != 0 ) {
            if ( faceMailingList == null ) {
                faceMailingList = new FaceMailingList();
                faceMailingList.setFace( faceConfig );
            }
            faceMailingList.setMailingList( mailingListDAO.findById( mailingListId ) );
            if ( faceMailingList.getMailingList() != null ) {
                mailingListDAO.makePersistent( faceMailingList );
            }
        }
        else if ( faceMailingList != null ) {
            mailingListDAO.makeTransient( faceMailingList );
        }
    }
}