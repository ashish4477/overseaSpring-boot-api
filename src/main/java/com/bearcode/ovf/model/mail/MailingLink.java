package com.bearcode.ovf.model.mail;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Date: 05.07.12
 * Time: 15:35
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "mailingList")
public class MailingLink implements Serializable {

    private static final long serialVersionUID = -7737953012833117281L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "mailingAddressId")
    private MailingAddress mailingAddress;

    @ManyToOne
    @JoinColumn(name = "mailingListId")
    private MailingList mailingList;

    private Date lastUpdated;

    private MailingLinkStatus status;

    private int errorCount = 0;

    private String errorMessage = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public MailingAddress getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress( MailingAddress mailingAddress ) {
        this.mailingAddress = mailingAddress;
    }

    public MailingList getMailingList() {
        return mailingList;
    }

    public void setMailingList( MailingList mailingList ) {
        this.mailingList = mailingList;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated ) {
        this.lastUpdated = lastUpdated;
    }

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
}
