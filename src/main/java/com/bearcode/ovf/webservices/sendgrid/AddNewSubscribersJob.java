package com.bearcode.ovf.webservices.sendgrid;

import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.model.mail.MailingLinkStatus;
import com.bearcode.ovf.model.system.OvfProperty;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.webservices.sendgrid.model.AddContactResponse;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author leonid.
 */
//@Component
public class AddNewSubscribersJob extends QuartzJobBean implements StatefulJob {
    private final Logger logger = LoggerFactory.getLogger( AddNewSubscribersJob.class );

    //@Autowired
    private MailingListService mailingListService;

    //@Autowired
    private SendGridService sendGridService;

    private int maxErrors = 5;

    private OvfPropertyService propertyService;

    public void addNewContacts() {
        List<MailingLink> links = mailingListService.findMailingLinksByStatus( MailingLinkStatus.NEW, 500 );
        if ( links != null && !links.isEmpty() ) {
            updateContacts( links );
        }
    }


    private void updateContacts( List<MailingLink> links ) {
        if ( links != null && !links.isEmpty() ) {
            AddContactResponse response = sendGridService.sendNewContacts( links );

            for ( int i = 0; i < links.size(); i++ ) {
                MailingLink link = links.get( i );
                if ( response != null ) {
                    if ( response.getErrorIndices().contains( i ) ) {
                        link.setErrorCount( maxErrors );
                        link.setStatus( MailingLinkStatus.DUPLICATED );
                        link.setErrorMessage( response.findErrorMessages( i ) );
                    }
                    else {
                        link.setStatus( MailingLinkStatus.SUBSCRIBED );
                        link.setLastUpdated( new Date() );
                    }
                } else {  // can't connect SendGrid
                    link.setErrorCount( link.getErrorCount() + 1 );
                    if ( link.getErrorCount() == maxErrors ) {
                        link.setStatus( MailingLinkStatus.ERROR );
                    }
                }
                mailingListService.updateMailingLink( link );
            }
        }
    }



    public void deleteGlobalUnsubscribes() {
        List<MailingLink> links = mailingListService.findMailingLinksByStatus( MailingLinkStatus.RESTORED, 500 );

        if ( links != null && !links.isEmpty() ) {
            sendGridService.restoreGlobalUnsubscribes( links );
            updateContacts( links );
        }
    }

    private void checkGroupUpdateSubscribers() {
        List<MailingLink> links = mailingListService.findMailingLinksByStatus( MailingLinkStatus.UPDATED, 500 );

        if ( links != null && !links.isEmpty() ) {
            sendGridService.checkDeleteGroupUnsubscribes( links );
            updateContacts( links );
        }
    }

    public MailingListService getMailingListService() {
        return mailingListService;
    }

    public void setMailingListService( MailingListService mailingListService ) {
        this.mailingListService = mailingListService;
    }

    public SendGridService getSendGridService() {
        return sendGridService;
    }

    public void setSendGridService( SendGridService sendGridService ) {
        this.sendGridService = sendGridService;
    }

    public void setPropertyService( OvfPropertyService propertyService ) {
        this.propertyService = propertyService;
    }

    @Override
    protected void executeInternal( final JobExecutionContext context ) throws JobExecutionException {
        if (propertyService.getPropertyAsInt( OvfPropertyNames.SEND_GRID_SEND_UPDATES ) == 0
                || !Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED)))
            return;

        addNewContacts();

        try {
            TimeUnit.SECONDS.sleep( 2 ); // add timeout 2 sec to not exceed SendGrid max load
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        checkGroupUpdateSubscribers();

        try {
            TimeUnit.SECONDS.sleep( 2 );
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        deleteGlobalUnsubscribes();
    }
}
