/**
 * 
 */
package com.bearcode.ovf.tools.pendingregistration;

import org.springframework.stereotype.Component;

import javax.xml.datatype.Duration;

/**
 * Configuration class for pending voter registrations - provides information for one SHS.
 * 
 * @author IanBrown
 * 
 * @since Nov 13, 2012
 * @version Nov 28, 2012
 */
@Component
public class PendingVoterRegistrationConfiguration {

    public static final int EXPORT_ALL = 0;
    public static final int EXPORT_FACE = 1;
    public static final int EXPORT_STATE = 2;

    public enum DeliverySchedule {NONE, HOURLY, DAILY, WEEKLY, MONTHLY};

    /**
	 * is this configuration enabled?
	 * 
	 * @author IanBrown
	 * @since Nov 28, 2012
	 * @version Nov 28, 2012
	 */
	private boolean enabled;

	/**
	 * the face prefix for the SHS.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private String facePrefix;

	/**
	 * must the data come from the SHS?
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private boolean requireShs;

	/**
	 * the timeout duration.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private Duration timeout;

	/**
	 * the name of the optional voting region.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private String votingRegion;

	/**
	 * the abbreviation for the voting state.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private String votingState;

	/**
	 * the password for the ZIP file.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	private String zipPassword;

    /**
     * Level of answers to export : export all, only face dependents or state dependents
     * */

    private int exportAnswersLevel = EXPORT_ALL;

    /**
     * Allow automated delivery for this config.
     * sftp server should be available, its address and credentials should be in properties
     */
    private DeliverySchedule deliverySchedule = DeliverySchedule.NONE;

    private String privateKeyName;
    private  String serverAddress;
    private  String serverPort;
    private  String sftpUser;
    private  String password;
    private  String passphrase;
    private  String sftpDir;

    public String getPrivateKeyName(){
        return privateKeyName;
    }

    public void setPrivateKeyName(String privateKeyName){
        this.privateKeyName = privateKeyName;
    }

    public String getServerAddress(){
        return serverAddress;
    }

    public void setServerAddress(String serverAddress){
        this.serverAddress = serverAddress;
    }

    public String getServerPort(){
        return serverPort;
    }

    public void setServerPort(String serverPort){
        this.serverPort = serverPort;
    }

    public String getSftpUser(){
        return sftpUser;
    }

    public void setSftpUser(String sftpUser){
        this.sftpUser = sftpUser;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassphrase(){
        return passphrase;
    }

    public void setPassphrase(String passphrase){
        this.passphrase = passphrase;
    }

    public String getSftpDir(){
        return sftpDir;
    }

    public void setSftpDir(String sftpDir){
        this.sftpDir = sftpDir;
    }

	/**
	 * Gets the face prefix.
	 * 
	 * @author IanBrown
	 * @return the face prefix.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public String getFacePrefix() {
		return facePrefix;
	}

	/**
	 * Gets the timeout.
	 * 
	 * @author IanBrown
	 * @return the timeout.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	public Duration getTimeout() {
		return timeout;
	}

	/**
	 * Gets the voting region.
	 * 
	 * @author IanBrown
	 * @return the voting region.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public String getVotingRegion() {
		return votingRegion;
	}

	/**
	 * Gets the voting state.
	 * 
	 * @author IanBrown
	 * @return the voting state.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public String getVotingState() {
		return votingState;
	}

	/**
	 * Gets the ZIP password.
	 * 
	 * @author IanBrown
	 * @return the ZIP password.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public String getZipPassword() {
		return zipPassword;
	}

	/**
	 * Gets the enabled.
	 * 
	 * @author IanBrown
	 * @return the enabled.
	 * @since Nov 28, 2012
	 * @version Nov 28, 2012
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Gets the require SHS.
	 * 
	 * @author IanBrown
	 * @return the require SHS.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public boolean isRequireShs() {
		return requireShs;
	}

	/**
	 * Sets the enabled.
	 * 
	 * @author IanBrown
	 * @param enabled
	 *            the enabled to set.
	 * @since Nov 28, 2012
	 * @version Nov 28, 2012
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Sets the face prefix.
	 * 
	 * @author IanBrown
	 * @param facePrefix
	 *            the face prefix to set.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public void setFacePrefix(final String facePrefix) {
		this.facePrefix = facePrefix;
	}

	/**
	 * Sets the require SHS.
	 * 
	 * @author IanBrown
	 * @param requireShs
	 *            the require SHS to set.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public void setRequireShs(final boolean requireShs) {
		this.requireShs = requireShs;
	}

	/**
	 * Sets the timeout.
	 * 
	 * @author IanBrown
	 * @param timeout
	 *            the timeout to set.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	public void setTimeout(final Duration timeout) {
		this.timeout = timeout;
	}

	/**
	 * Sets the voting region.
	 * 
	 * @author IanBrown
	 * @param votingRegion
	 *            the voting region to set.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public void setVotingRegion(final String votingRegion) {
		this.votingRegion = votingRegion;
	}

	/**
	 * Sets the voting state.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state to set.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public void setVotingState(final String votingState) {
		this.votingState = votingState;
	}

	/**
	 * Sets the ZIP password.
	 * 
	 * @author IanBrown
	 * @param zipPassword
	 *            the ZIP password to set.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public void setZipPassword(final String zipPassword) {
		this.zipPassword = zipPassword;
	}


    public int getExportAnswersLevel() {
        return exportAnswersLevel;
    }

    public void setExportAnswersLevel(int exportAnswersLevel) {
        this.exportAnswersLevel = exportAnswersLevel;
    }

    public DeliverySchedule getDeliverySchedule(){
        return deliverySchedule;
    }

    public void setDeliverySchedule(DeliverySchedule deliverySchedule){
        this.deliverySchedule = deliverySchedule;
    }


    public boolean isAutomatedDelivery() {
        return deliverySchedule != DeliverySchedule.NONE;
    }

    public void setExportLevel( final String exportLevel ) {
        if ( exportLevel.equalsIgnoreCase("FACE") ) {
            exportAnswersLevel = EXPORT_FACE;
        }
        else if ( exportLevel.equalsIgnoreCase("STATE") ) {
            exportAnswersLevel = EXPORT_STATE;
        }
        else {
            exportAnswersLevel = EXPORT_ALL;
        }
    }

    public boolean canSftp( ) {
        return serverAddress != null
                && sftpUser != null
                && ( privateKeyName != null  || password != null );
    }

}
