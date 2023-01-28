package com.bearcode.ovf.model.registrationexport;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leonid on 22.09.16.
 *
 * Configuration class for Data export
 */
public class DataExportConfiguration implements Serializable {

    private static final long serialVersionUID = -6797097770952586796L;

    private long id;

    private List<FaceConfig> faceConfigs;

    /**
     * the optional state tied with the config
     */
    private State state;

    /**
     * the optional voting region tied with the config
     */
    private VotingRegion votingRegion;

    /**
     * the password for the ZIP file.
     *
     */
    private String zipPassword;

    /**
     * Level of answers to export : export all, only face dependents or state dependents
     * */

    private ExportLevel exportAnswersLevel = ExportLevel.EXPORT_FACE;

    /**
     * SFTP server name or address
     */
    private String serverAddress;

    /**
     * SFTP server port
     */
    private String serverPort;

    /**
     * SFTP directory
     */
    private String sftpDir;

    /**
     * SFTP user name
     */
    private String sftpUserName;

    /**
     * SFTP password
     */
    private String sftpPassword;

    /**
     * SFTP passphrase
     */
    private String sftpPassphrase;

    /**
     * SFTP private key file resource name
     */
    private String sftpPrivateKey;

    /**
     * Schedule of export
     */
    private DeliverySchedule deliverySchedule = DeliverySchedule.NONE;

    private boolean enabled = false;


    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public VotingRegion getVotingRegion() {
        return votingRegion;
    }

    public void setVotingRegion( VotingRegion votingRegion ) {
        this.votingRegion = votingRegion;
    }

    public String getZipPassword() {
        return zipPassword;
    }

    public void setZipPassword( String zipPassword ) {
        this.zipPassword = zipPassword;
    }

    public ExportLevel getExportAnswersLevel() {
        return exportAnswersLevel;
    }

    public void setExportAnswersLevel( ExportLevel exportAnswersLevel ) {
        this.exportAnswersLevel = exportAnswersLevel;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress( String serverAddress ) {
        this.serverAddress = serverAddress;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort( String serverPort ) {
        this.serverPort = serverPort;
    }

    public String getSftpUserName() {
        return sftpUserName;
    }

    public void setSftpUserName( String sftpUserName ) {
        this.sftpUserName = sftpUserName;
    }

    public String getSftpPassword() {
        return sftpPassword;
    }

    public void setSftpPassword( String sftpPassword ) {
        this.sftpPassword = sftpPassword;
    }

    public String getSftpPassphrase() {
        return sftpPassphrase;
    }

    public void setSftpPassphrase( String sftpPassphrase ) {
        this.sftpPassphrase = sftpPassphrase;
    }

    public String getSftpPrivateKey() {
        return sftpPrivateKey;
    }

    public void setSftpPrivateKey( String sftpPrivateKey ) {
        this.sftpPrivateKey = sftpPrivateKey;
    }

    public State getState() {
        return state;
    }

    public void setState( State state ) {
        this.state = state;
    }

    public DeliverySchedule getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule( DeliverySchedule deliverySchedule ) {
        this.deliverySchedule = deliverySchedule;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public String getSftpDir() {
        return sftpDir;
    }

    public void setSftpDir( String sftpDir ) {
        this.sftpDir = sftpDir;
    }

    public List<FaceConfig> getFaceConfigs() {
        return faceConfigs;
    }

    public void setFaceConfigs( List<FaceConfig> faceConfigs ) {
        this.faceConfigs = faceConfigs;
    }

    public boolean canSftp( ) {
        return StringUtils.isNotBlank( serverAddress )
                && StringUtils.isNotBlank( sftpUserName )
                && ( StringUtils.isNotBlank( sftpPrivateKey )  || StringUtils.isNotBlank( sftpPassword ) );
    }

    public String buildSftpUri( String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append( "sftp://" )
                .append( sftpUserName );
        if ( StringUtils.isNotBlank( sftpPassword ) ) {
            builder.append( ":" ).append( sftpPassword );
        }
        builder.append( "@" ).append( serverAddress );
        if ( StringUtils.isNotBlank( serverPort ) && serverPort.matches( "\\d+" ) ) {
            builder.append( ":" ).append( serverPort );
        }
        if ( StringUtils.isNotBlank( sftpDir ) ) {
            builder.append( "/" ).append( sftpDir );
        }
        builder.append( "/" ).append( fileName ).append( ".csv" );
        return builder.toString();
    }

    public Object getName() {
        StringBuilder name = new StringBuilder();
        if ( faceConfigs != null && faceConfigs.size() > 0 ) {
            name.append( "SHS " );
            for (  int i =0; i < faceConfigs.size(); i++ ) {
                FaceConfig faceConfig = faceConfigs.get( i );
                if ( i > 0 ) {
                    name.append( "+" );
                }
                name.append( faceConfig.getName() );
            }
        }
        if ( state != null ) {
            name.append( " State " ).append( state.getAbbr() );
        }
        if ( votingRegion != null ) {
            name.append( " Voting Region " ).append( votingRegion.getName() );
        }
        return name.toString();
    }

}
