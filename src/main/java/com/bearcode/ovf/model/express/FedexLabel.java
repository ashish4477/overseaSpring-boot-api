package com.bearcode.ovf.model.express;

import com.bearcode.ovf.model.common.OverseasUser;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 7, 2008
 * Time: 5:00:04 PM
 * @author Leonid Ginzburg
 */
public class FedexLabel {
    private long id;
    private String trackingNumber;
    private String fileName;
    private Date created;
    private String message;
    private boolean paymentStatus;
    private String transaction="";
    private OverseasUser owner;
    
    public static final long EXPIRE_PERIOD = 12L*60L*60L*1000L; // 12 hours

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public OverseasUser getOwner() {
        return owner;
    }

    public void setOwner(OverseasUser owner) {
        this.owner = owner;
    }
    
    public boolean isExpired(){
    	return getCreated().getTime() + EXPIRE_PERIOD < new Date().getTime();
    }
}
