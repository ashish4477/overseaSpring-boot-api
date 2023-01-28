package com.bearcode.ovf.model.mail;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
public class MailingTask implements Serializable {

    public static final int STATUS_NEW = 0;
    public static final int STATUS_READY = 1;
    public static final int STATUS_STARTED = 2;
    public static final int STATUS_FINISHED = 3;
    public static final int STATUS_ERROR = 4;

    private static final long serialVersionUID = -2348759182872887859L;
    private long id;

    @NotNull
    private MailingList mailingList;

    @NotNull
    private MailTemplate template;

    @NotNull
    private Date startOn;
    private int status;
    private String subject;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public MailingList getMailingList() {
        return mailingList;
    }

    public void setMailingList( MailingList mailingList ) {
        this.mailingList = mailingList;
    }

    public MailTemplate getTemplate() {
        return template;
    }

    public void setTemplate( MailTemplate template ) {
        this.template = template;
    }

    public Date getStartOn() {
        return startOn;
    }

    public void setStartOn( Date startOn ) {
        this.startOn = startOn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus( int status ) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject( String subject ) {
        this.subject = subject;
    }

    public String getStatusName() {
        switch ( status ) {
            case STATUS_NEW:
                return "New";
            case STATUS_READY:
                return "Ready";
            case STATUS_FINISHED:
                return "Finished";
            case STATUS_STARTED:
                return "Started";
            case STATUS_ERROR:
                return "Error";
        }
        return "";
    }
}
