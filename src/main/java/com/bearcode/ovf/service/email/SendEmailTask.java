package com.bearcode.ovf.service.email;

import com.bearcode.ovf.model.email.RawEmail;

public class SendEmailTask implements Runnable {


    private EmailService emailService;

    private RawEmail rawEmail;

    public SendEmailTask (EmailService emailService, RawEmail rawEmail) {
        this.emailService = emailService;
        this.rawEmail = rawEmail;
    }

    public void run() {
        this.emailService.sendEmail(this.rawEmail);
    }

}
