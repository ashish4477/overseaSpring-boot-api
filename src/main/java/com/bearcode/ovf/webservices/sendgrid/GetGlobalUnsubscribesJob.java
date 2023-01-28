package com.bearcode.ovf.webservices.sendgrid;

import com.bearcode.ovf.model.mail.SendGridMark;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.OvfPropertyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

/**
 * @author leonid.
 */
public class GetGlobalUnsubscribesJob extends QuartzJobBean implements StatefulJob {

    //@Autowired
    private MailingListService mailingListService;

    //@Autowired
    private SendGridService sendGridService;

    private OvfPropertyService propertyService;

    private int limit = 500;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit( int limit ) {
        this.limit = limit;
    }

    @Override
    protected void executeInternal( JobExecutionContext context ) throws JobExecutionException {
        if (propertyService.getPropertyAsInt( OvfPropertyNames.SEND_GRID_SEND_UPDATES ) == 0 ||
                !Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED)))
            return;

        SendGridMark lastMark = mailingListService.findLastMark();
        long startTime = lastMark != null ? lastMark.getLastRun().getTime() / 1000l : 0l;
        int offset = 0;

        SendGridMark mark = new SendGridMark();
        mark.setLastRun( new Date() );

        int runSize = 0;
        boolean goodStart = (lastMark != null);
        do {
            List<String> emails = sendGridService.getGlobalUnsubscribes( startTime, limit, offset );
            if ( emails != null ) {
                runSize = emails.size();
                goodStart = true;
            }
            mailingListService.updateUnsubscribes( emails );

            mark.setNumberOfUnsubscribes( mark.getNumberOfUnsubscribes() + runSize );
            if ( runSize < limit ) {
                runSize = 0;
            }
            else {
                offset += runSize;
            }
        } while ( runSize > 0 );
        if ( goodStart ) {
            mailingListService.saveSendGridMark( mark );
        }

    }
}
