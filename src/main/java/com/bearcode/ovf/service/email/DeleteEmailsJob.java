package com.bearcode.ovf.service.email;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DeleteEmailsJob extends QuartzJobBean implements StatefulJob {
	
	private final static Logger log = LoggerFactory.getLogger(DeleteEmailsJob.class);

    private EmailService emailService;

    private OvfPropertyService propertyService;

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(final EmailService emailService) {
        this.emailService = emailService;
    }

    public OvfPropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService(OvfPropertyService propertyService) {
        this.propertyService = propertyService;
    }

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED))) {
            emailService.deleteOldEmails();
            emailService.deleteOldErrorEmails();
        }
    }

}
