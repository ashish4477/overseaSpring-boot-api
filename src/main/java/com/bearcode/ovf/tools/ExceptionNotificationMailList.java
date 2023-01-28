package com.bearcode.ovf.tools;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by leonid on 24.08.15.
 */
@Component
public class ExceptionNotificationMailList {

    protected List<String> mailList = new LinkedList<String>();

    public List<String> getMailList() {
        return mailList;
    }

    public void setMailList( List<String> mailList ) {
        this.mailList = mailList;
    }
}
