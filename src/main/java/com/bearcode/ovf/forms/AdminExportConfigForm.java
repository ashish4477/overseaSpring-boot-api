package com.bearcode.ovf.forms;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DeliverySchedule;
import com.bearcode.ovf.model.registrationexport.ExportLevel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by leonid on 11.10.16.
 */
public class AdminExportConfigForm {

    private DataExportConfiguration configuration;

    private List<FaceConfig> faceConfigs;

    public AdminExportConfigForm( DataExportConfiguration configuration ) {
        this.configuration = configuration;
        faceConfigs = new ArrayList<FaceConfig>();
        faceConfigs.addAll( configuration.getFaceConfigs() );
    }

    public List<FaceConfig> getFaceConfigs() {
        return faceConfigs;
    }

    public void setFaceConfigs( List<FaceConfig> faceConfigs ) {
        this.faceConfigs = faceConfigs;
    }

    public DataExportConfiguration getConfiguration() {
        return configuration;
    }

    public long getId() {
        return configuration.getId();
    }

    public void setId( long id ) {
    }

    public VotingRegion getVotingRegion() {
        return configuration.getVotingRegion();
    }

    public void setVotingRegion( VotingRegion votingRegion ) {
        configuration.setVotingRegion( votingRegion );
    }

    public String getZipPassword() {
        return configuration.getZipPassword();
    }

    public void setZipPassword( String zipPassword ) {
        configuration.setZipPassword( zipPassword );
    }

    public ExportLevel getExportAnswersLevel() {
        return configuration.getExportAnswersLevel();
    }

    public void setExportAnswersLevel( ExportLevel exportAnswersLevel ) {
        configuration.setExportAnswersLevel( exportAnswersLevel );
    }

    public String getServerAddress() {
        return configuration.getServerAddress();
    }

    public void setServerAddress( String serverAddress ) {
        configuration.setServerAddress( serverAddress );
    }

    public String getServerPort() {
        return configuration.getServerPort();
    }

    public void setServerPort( String serverPort ) {
        configuration.setServerPort( serverPort );
    }

    public String getSftpUserName() {
        return configuration.getSftpUserName();
    }

    public void setSftpUserName( String sftpUserName ) {
        configuration.setSftpUserName( sftpUserName );
    }

    public String getSftpPassword() {
        return configuration.getSftpPassword();
    }

    public void setSftpPassword( String sftpPassword ) {
        configuration.setSftpPassword( sftpPassword );
    }

    public String getSftpPassphrase() {
        return configuration.getSftpPassphrase();
    }

    public void setSftpPassphrase( String sftpPassphrase ) {
        configuration.setSftpPassphrase( sftpPassphrase );
    }

    public String getSftpPrivateKey() {
        return configuration.getSftpPrivateKey();
    }

    public void setSftpPrivateKey( String sftpPrivateKey ) {
        configuration.setSftpPrivateKey( sftpPrivateKey );
    }

    public State getState() {
        return configuration.getState();
    }

    public void setState( State state ) {
        configuration.setState( state );
    }

    public DeliverySchedule getDeliverySchedule() {
        return configuration.getDeliverySchedule();
    }

    public void setDeliverySchedule( DeliverySchedule deliverySchedule ) {
        configuration.setDeliverySchedule( deliverySchedule );
    }

    public boolean isEnabled() {
        return configuration.isEnabled();
    }

    public void setEnabled( boolean enabled ) {
        configuration.setEnabled( enabled );
    }

    public String getSftpDir() {
        return configuration.getSftpDir();
    }

    public void setSftpDir( String sftpDir ) {
        configuration.setSftpDir( sftpDir );
    }

    public void adjustFaceConfigs() {
        for ( FaceConfig newConfig : this.faceConfigs ) {
            if ( !configuration.getFaceConfigs().contains( newConfig ) ) {
                configuration.getFaceConfigs().add( newConfig );
            }
        }
        for ( Iterator<FaceConfig> configIterator = configuration.getFaceConfigs().iterator(); configIterator.hasNext(); ) {
            FaceConfig oldConfig = configIterator.next();
            if ( !this.faceConfigs.contains( oldConfig ) ) {
                configIterator.remove();
            }
        }
    }
}
