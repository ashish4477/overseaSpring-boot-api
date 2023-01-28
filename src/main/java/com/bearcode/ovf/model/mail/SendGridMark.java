package com.bearcode.ovf.model.mail;

import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
public class SendGridMark implements Serializable {
    private long id;
    private Date lastRun;
    private int numberOfUnsubscribes;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun( Date lastRun ) {
        this.lastRun = lastRun;
    }

    public int getNumberOfUnsubscribes() {
        return numberOfUnsubscribes;
    }

    public void setNumberOfUnsubscribes( int numberOfUnsubscribes ) {
        this.numberOfUnsubscribes = numberOfUnsubscribes;
    }
}
