package com.bearcode.ovf.actions.mail;

import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.mail.*;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.utils.CipherAgentException;
import com.bearcode.ovf.utils.CipherAgentUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author leonid.
 */
@Component
public class SendNewsletters extends QuartzJobBean implements StatefulJob {
    private final Logger logger = LoggerFactory.getLogger( SendNewsletters.class );

    private final static long PAGE_SIZE = 2000;

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OvfPropertyService propertyService;

    public OvfPropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService(OvfPropertyService propertyService) {
        this.propertyService = propertyService;
    }


    /**
     * preiodic job - looking for open tasks, getting links and send emails
     */
    public void runNewsletters() {
        if (!Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED))) {
            return;
        }
        SemaphoreState semaphoreState = checkSemaphore();

        if ( !semaphoreState.isGreen() ) {
            MailingSemaphore semaphore = semaphoreState.getSemaphore();

            MailingTask task = null;
            try {
                task = mailingListService.findFirstTask();

                if ( task != null ) {
                    long offset = semaphore.getOffset();
                    final MailingList list = task.getMailingList();
                    final MailTemplate template = task.getTemplate();
                    if ( semaphore.getRecordCount() == 0 ) {
                        long recordsCount = mailingListService.coutMailingLinks( list );
                        semaphore.setRecordCount( recordsCount );
                    }

                    List<MailingLink> links = mailingListService.findMailingLinks( list, offset, PAGE_SIZE );
                    for ( MailingLink link : links ) {
                        try {
                            String token = CipherAgentUtils.createToken( "unsubscribe", String.format( "%d:%s", list.getId(), link.getMailingAddress().getEmail() ) );
                            final Email email = Email.builder()
                                    .templateBody( template.getBodyTemplate() + list.getSignature() )
                                    .to( link.getMailingAddress().getEmail() )
                                    .model( "priority", RawEmail.Priority.LOW)
                                    .model( "contact", link.getMailingAddress() )
                                    .model( "unsubscribeKey", token )
                                    .model( "year", Calendar.getInstance().get( Calendar.YEAR ) )
                                    .bcc( false )
                                    .from( StringUtils.isEmpty( template.getFrom() ) ? list.getFrom() : template.getFrom() )
                                    .replyTo( StringUtils.isEmpty( template.getReplyTo() ) ? list.getReplyTo() : template.getReplyTo() )
                                    .subject( StringUtils.isEmpty( task.getSubject() ) ? template.getSubject() : task.getSubject() )
                                    .build();
                            emailService.queue( email );
                        } catch (EmailException e) {
                            logger.error( String.format( "Can't send email to \"%s\"", link.getMailingAddress().getEmail() ), e );
                        } catch (CipherAgentException e) {
                            logger.error( String.format( "Can't encode token for %s", link.getMailingAddress().getEmail() ), e );
                        }

                    }
                    semaphore.setOffset( offset + PAGE_SIZE );

                    if ( semaphore.getOffset() >= semaphore.getRecordCount() ) {
                        task.setStatus( MailingTask.STATUS_FINISHED );
                        mailingListService.saveMailingTask( task );
                        semaphore.setRecordCount( 0 );
                        semaphore.setOffset( 0 );
                    }
                }
            }
            catch (Exception e) {
                semaphore.setRecordCount( 0 );
                if ( task != null ) {
                    task.setStatus( MailingTask.STATUS_ERROR );
                    mailingListService.saveMailingTask( task );
                }
                logger.error( "Error while running mailing task.", e );
            }
            finally {
                semaphore.setBusyStatus( false );
                mailingListService.saveSemaphore( semaphore );
            }

        }

    }


    @Override
    protected void executeInternal( JobExecutionContext context ) throws JobExecutionException {
        runNewsletters();
    }


    public MailingListService getMailingListService() {
        return mailingListService;
    }

    public void setMailingListService( MailingListService mailingListService ) {
        this.mailingListService = mailingListService;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService( EmailService emailService ) {
        this.emailService = emailService;
    }

    public class SemaphoreState {
        private MailingSemaphore semaphore;
        private boolean green;

        public SemaphoreState( MailingSemaphore semaphore, boolean state ) {
            this.semaphore = semaphore;
            this.green = state;
        }

        public MailingSemaphore getSemaphore() {
            return semaphore;
        }

        public boolean isGreen() {
            return green;
        }
    }

    public synchronized SemaphoreState checkSemaphore() {
        MailingSemaphore semaphore = mailingListService.findSemaphore();
        boolean state = ! semaphore.isBusyStatus();
        if ( state ) {
            semaphore.setBusyStatus( true );
            mailingListService.saveSemaphore( semaphore );
        }
        return new SemaphoreState( semaphore, state );
    }
}
