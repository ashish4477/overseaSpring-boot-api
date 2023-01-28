package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.MailingListDAO;
import com.bearcode.ovf.webservices.sendgrid.model.SendGridLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leonid.
 */
@Service
public class SendGridLogService {

    @Autowired
    private MailingListDAO mailingListDAO;

    public void saveLog( SendGridLogMessage logMessage ) {
        mailingListDAO.makePersistent( logMessage );
    }

    public List<SendGridLogMessage> findLogMessages() {
        return mailingListDAO.findSendGridMessages();
    }
}
