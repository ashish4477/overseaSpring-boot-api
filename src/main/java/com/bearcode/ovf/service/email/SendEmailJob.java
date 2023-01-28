package com.bearcode.ovf.service.email;

import java.util.List;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bearcode.ovf.model.email.RawEmail;

public class SendEmailJob extends QuartzJobBean implements StatefulJob {
	
	private final static Logger log = LoggerFactory.getLogger(SendEmailJob .class);
	
	private EmailService emailService;

    private OvfPropertyService propertyService;

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	private TaskExecutor sendEmailThreadPool;

    public TaskExecutor getSendEmailThreadPool() {
        return sendEmailThreadPool;
    }

    public void setSendEmailThreadPool( TaskExecutor sendEmailThreadPool ) {
        this.sendEmailThreadPool = sendEmailThreadPool;
    }

    public OvfPropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService( OvfPropertyService propertyService ) {
        this.propertyService = propertyService;
    }

    @Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String tumbler = propertyService.getProperty( OvfPropertyNames.EMAIL_SERVICE_TUMBLER_SWITCH );
        if ( !"1".equals( tumbler ) || !Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED))) return;
    	try {
    		final List<RawEmail> emails = emailService.findUnsentEmails();
        	this.sendEmails(emails);
		} catch (Exception e) {
			log.error("Cannot send emails", e);
		}
	}

	private void sendEmails(final List<RawEmail> emails) {
		for (final RawEmail email : emails) {
			try {
				emailService.sendEmail(email);
				//sendEmailThreadPool.execute( new SendEmailTask( emailService, email ) );
			} catch (Exception e) {
				log.error("Cannot send email", e);
			}
		}
	}
}
