package com.bearcode.ovf.webservices.sendgrid.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
public class SendGridLogMessage implements Serializable {
    private static final long serialVersionUID = -4943147846050157134L;

    private long id;

    private String logLevel;

    private Date logDate;

    private String message;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate( Date logDate ) {
        this.logDate = logDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel( String logLevel ) {
        this.logLevel = logLevel;
    }
}
