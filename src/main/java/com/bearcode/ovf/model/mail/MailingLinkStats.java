package com.bearcode.ovf.model.mail;

/**
 * @author leonid.
 */
public class MailingLinkStats {
    private MailingLinkStatus status;
    private int errorCount;
    private String errorMessage;
    private long rowCount;

    public MailingLinkStatus getStatus() {
        return status;
    }

    public void setStatus( MailingLinkStatus status ) {
        this.status = status;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount( int errorCount ) {
        this.errorCount = errorCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage( String errorMessage ) {
        this.errorMessage = errorMessage;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount( long rowCount ) {
        this.rowCount = rowCount;
    }
}
