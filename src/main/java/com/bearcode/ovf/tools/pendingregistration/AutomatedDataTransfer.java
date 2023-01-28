package com.bearcode.ovf.tools.pendingregistration;

import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.service.PendingVoterRegistrationService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by leonid on 31.10.14.
 */
@Component
public class AutomatedDataTransfer implements ApplicationContextAware {
    protected Logger logger = LoggerFactory.getLogger( AutomatedDataTransfer.class );


    @Autowired
    private OnlineDataTransferExecutor onlineDataTransferExecutor;

    @Autowired
    private OnlineDataTransferCache onlineDataTransferCache;

    @Autowired
    private PendingVoterRegistrationService pendingVoterRegistrationService;

    @Autowired
    private EmailService emailService;

    private List<String> mailList = new LinkedList<String>();

    private ApplicationContext applicationContext;

    public void setMailList( String mailListStr ) {
        String[] shopList = mailListStr.split(",");
        for ( String elem : shopList ) {
            if ( elem.trim().matches( OverseasUserValidator.USERNAME_PATTERN) ) {
                mailList.add( elem.trim() );
            }
        }
    }

    public void doHourlyDeliveries() {
        doDelivery(PendingVoterRegistrationConfiguration.DeliverySchedule.HOURLY);
    }

    public void doDailyDeliveries() {
        doDelivery(PendingVoterRegistrationConfiguration.DeliverySchedule.DAILY);
    }

    public void doWeeklyDeliveries() {
        doDelivery(PendingVoterRegistrationConfiguration.DeliverySchedule.WEEKLY);
    }

    public void doMonthlyDeliveries() {
        doDelivery(PendingVoterRegistrationConfiguration.DeliverySchedule.MONTHLY);
    }

    private void doDelivery(PendingVoterRegistrationConfiguration.DeliverySchedule deliverySechdule) {
        logger.info( "Initiating doDelivery for {} DeliverySchedule.", deliverySechdule );

        for ( PendingVoterRegistrationConfiguration configuration : pendingVoterRegistrationService.getPendingVoterRegistrationConfigurations() ) {
            if ( !configuration.isEnabled() ) {
                continue;
            }
            if ( !configuration.getDeliverySchedule().equals(deliverySechdule) ) {
                continue;
            }
            if ( !configuration.canSftp() ) {
                logger.error( "Missing sftp settings for \"{}\".", configuration.getFacePrefix() );
                continue;
            }
            try {
                List<PendingVoterRegistration> registrations = pendingVoterRegistrationService.findForConfiguration( configuration );
                if ( registrations.size() > 0 ) {
                    final int records = registrations.size();
                    logger.info( "Delivering {} registrations for \"{}\".", registrations.size(), configuration.getFacePrefix() );
                    final String id = onlineDataTransferCache.createStatusId();
                    onlineDataTransferExecutor.createDataTransferCsv(configuration, id, null, true);
                    DataPreparationStatus status;
                    do {
                        Thread.sleep(1000l);
                        status = onlineDataTransferCache.createStatus(id);
                    } while (status.getStatus() < DataPreparationStatus.COMPLETED );
                    if ( status.getStatus() == DataPreparationStatus.COMPLETED ) {
                        if ( writeCsvToFtp( id, configuration ) ) {
                            pendingVoterRegistrationService.makeComplete( null, registrations );
                        }
                    }
                    else {
                        sendEmailNotification( "CSV preparation process failed with an error. " + status.getMessage(), null );
                    }
                    logger.info( "Finished Automated Delivery for \"{}\". {} records were processed.", configuration.getFacePrefix(), records );
                } else {
                    logger.info( "No registrations to deliver for \"{}\".", configuration.getFacePrefix() );
                }
            } catch (Exception e) {
                logger.error( "Couldn't create and transfer csv data.", e );
                sendEmailNotification( "Exception was thrown.", e );
            }
        }
    }

    private boolean writeCsvToFtp( String id, PendingVoterRegistrationConfiguration configuration ) throws IOException {
        byte[] csv = onlineDataTransferCache.getCsvData(id);
        boolean done = false;

        final String privateKeyName = configuration.getPrivateKeyName();
        final String serverAddress = configuration.getServerAddress();
        final String serverPort = configuration.getServerPort();
        final String sftpUser = configuration.getSftpUser();
        final String password = configuration.getPassword();
        final String passphrase = configuration.getPassphrase();
        final String sftpDir = configuration.getSftpDir();

        if (csv != null && csv.length > 0) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            final String csvFileName = format.format(new Date());
            final File temp = writeCsvIntoTempFile( csv, csvFileName );

            StandardFileSystemManager manager = new StandardFileSystemManager();

            try {
                //Initializes the file manager
                manager.init();

                //Setup our SFTP configuration
                FileSystemOptions opts = new FileSystemOptions();
                SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
                        opts, "no");
                SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
                SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

                if ( privateKeyName != null && !privateKeyName.isEmpty() ) {
                    // get private key file
                    File privateKey = applicationContext.getResource( privateKeyName ).getFile();
                    SftpFileSystemConfigBuilder.getInstance().setIdentities( opts, new File[] { privateKey } );
                }
                if ( passphrase != null && !passphrase.isEmpty()) {
                    SftpFileSystemConfigBuilder.getInstance().setUserInfo( opts, new UserInfo() {
                        @Override
                        public String getPassphrase() {
                            return passphrase;
                        }

                        @Override
                        public String getPassword() {
                            return null;
                        }

                        @Override
                        public boolean promptPassword( String s ) {
                            return false;
                        }

                        @Override
                        public boolean promptPassphrase( String s ) {
                            return false;
                        }

                        @Override
                        public boolean promptYesNo( String s ) {
                            return false;
                        }

                        @Override
                        public void showMessage( String s ) {

                        }
                    } );
                }

                //Create the SFTP URI using the host name, port, user id, remote path and file name
                String sftpUri = buildSftpUri( sftpUser, password, serverAddress, serverPort, csvFileName, sftpDir );

                // Create local file object
                FileObject localFile = manager.resolveFile(temp.getAbsolutePath());

                // Create remote file object
                FileObject remoteFile = manager.resolveFile(sftpUri, opts);

                // Copy local file to sftp server
                remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
                done = true;
            } finally {
                manager.close();
                temp.delete();
            }

        }

        return done;
    }

    private File writeCsvIntoTempFile( final byte[] csv, final String fileName ) throws IOException {
        // create local file
        final File temp;
        FileWriter writer = null;
        try {
            temp = File.createTempFile( fileName, ".csv" );
            writer = new FileWriter( temp );
            IOUtils.write( csv, writer, "UTF-8" );
        } finally {
            if ( writer != null ) {
                IOUtils.closeQuietly( writer );
            }
        }
        return temp;
    }

    private void sendEmailNotification( final String reason, final Exception e) {
        for (String toEmail : mailList ) {
            try {
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter( writer );
                e.printStackTrace( pw );
                pw.close();
                String trace = writer.toString().replaceAll( "\\n", "<br/>" ).replaceAll( "\\t", "&nbsp;&nbsp;&nbsp;&nbsp; " );
                final Email email = Email.builder()
                        .template( EmailTemplates.XML_AUTOMATED_DELIVERY_FAILURE )
                        .to( toEmail )
                        .model( "reason", reason )
                        .model( "exception", e )
                        .model( "exceptionTrace", trace )
                        .model( "priority", RawEmail.Priority.LOWEST )
                        .build();
                emailService.queue(email);
            } catch (Exception ex) {
                logger.error("Delivery failure message was not sent due to {}.", ex.getMessage());
            }
        }
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private String buildSftpUri( String username, String password, String server, String port, String fileName, String sftpDir ) {
        StringBuilder builder = new StringBuilder();
        builder.append( "sftp://" )
                .append( username );
        if ( password != null && !password.isEmpty() ) {
            builder.append( ":" ).append( password );
        }
        builder.append( "@" ).append( server );
        if ( port != null && port.matches( "\\d+" )  ) {
            builder.append( ":" ).append( port );
        }
        if(sftpDir != null & sftpDir != ""){
            builder.append("/").append(sftpDir);
        }
        builder.append( "/" ).append( fileName ).append( ".csv" );
        return builder.toString();
    }
}
