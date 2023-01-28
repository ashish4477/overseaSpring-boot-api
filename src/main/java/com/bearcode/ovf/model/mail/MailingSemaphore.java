package com.bearcode.ovf.model.mail;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
public class MailingSemaphore implements Serializable {

    private static final long serialVersionUID = 446437902640110609L;
    private long id;

    private boolean busyStatus = false;

    private long recordCount;

    private long offset;


    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public boolean isBusyStatus() {
        return busyStatus;
    }

    public void setBusyStatus( boolean busyStatus ) {
        this.busyStatus = busyStatus;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount( long recordCount ) {
        this.recordCount = recordCount;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset( long offset ) {
        this.offset = offset;
    }
}
