package com.bearcode.ovf.tools.registrationexport;

import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DataExportHistory;
import com.bearcode.ovf.model.registrationexport.DeliverySchedule;
import com.bearcode.ovf.service.RegistrationExportService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import com.bearcode.ovf.tools.pendingregistration.DataPreparationStatus;
import com.bearcode.ovf.tools.pendingregistration.OnlineDataTransferCache;
import com.bearcode.ovf.validators.OverseasUserValidator;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by leonid on 28.09.16.
 */
@Component
public class AutomatedDataExporter  implements ApplicationContextAware {
    protected Logger logger = LoggerFactory.getLogger( AutomatedDataExporter.class );

    @Autowired
    private AsyncDataExportPreparator asyncDataExportPreparator;

    @Autowired
    private OnlineDataTransferCache onlineDataTransferCache;

    @Autowired
    private RegistrationExportService registrationExportService;

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
        doDelivery( DeliverySchedule.HOURLY);
    }

    public void doDailyDeliveries() {
        doDelivery(DeliverySchedule.DAILY);
    }

    public void doWeeklyDeliveries() {
        doDelivery(DeliverySchedule.WEEKLY);
    }

    public void doMonthlyDeliveries() {
        doDelivery( DeliverySchedule.MONTHLY );
    }

    private void doDelivery(DeliverySchedule deliverySechdule) {
        logger.info( "Initiating doDelivery for {} DeliverySchedule.", deliverySechdule );

        List<DataExportConfiguration> configs = registrationExportService.findConfigurations( deliverySechdule );

        for ( DataExportConfiguration configuration : configs ) {
            if ( !configuration.canSftp() ) {
                logger.error( "Missing sftp settings for \"{}\".", configuration.getName() );
                continue;
            }
            try {
                int numberOfTry = 1;
                while ( numberOfTry > 0 ) {
                    List<DataExportHistory> histories = registrationExportService.findHistoriesForExport( configuration );
                    if ( histories.size() > 0 ) {
                        final int records = histories.size();
                        logger.info( "Delivering {} registrations for \"{}\".", histories.size(), configuration.getName() );
                        final String id = onlineDataTransferCache.createStatusId();
                        asyncDataExportPreparator.createDataTransferCsvFromWizard( configuration, histories, id );
                        DataPreparationStatus status;
                        do {
                            Thread.sleep( 1000l );
                            status = onlineDataTransferCache.createStatus( id );
                        } while ( status.getStatus() < DataPreparationStatus.COMPLETED );
                        if ( status.getStatus() == DataPreparationStatus.COMPLETED ) {
                            if ( writeCsvToFtp( id, configuration, numberOfTry ) ) {
                                registrationExportService.makeComplete( histories );
                            }
                        } else {
                            sendEmailNotification( "CSV preparation process failed with an error. " + status.getMessage(), null );
                        }
                        logger.info( "Finished Automated Delivery for \"{}\". {} records were processed.", configuration.getName(), records );
                        numberOfTry++;
                    } else {
                        if ( numberOfTry == 1 ) {
                            logger.info( "No registrations to deliver for \"{}\".", configuration.getName() );
                        }
                        numberOfTry = 0;  //stop repeating
                    }
                }
            } catch (Exception e) {
                logger.error( "Couldn't create and transfer csv data.", e );
                sendEmailNotification( "Exception was thrown.", e );
            }
        }
    }

    private boolean writeCsvToFtp( String id, DataExportConfiguration configuration, int numberOfPart ) throws IOException {
        byte[] csv = onlineDataTransferCache.getCsvData(id);
        boolean done = false;

        final String privateKeyName = configuration.getSftpPrivateKey();
        final String passphrase = configuration.getSftpPassphrase();

        if (csv != null && csv.length > 0) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String csvFileName = format.format(new Date());
            if ( numberOfPart > 1 ) {
                csvFileName = String.format( "%s_part_%d", csvFileName, numberOfPart );
            }
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
                String sftpUri = configuration.buildSftpUri( csvFileName );

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

}
