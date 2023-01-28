package com.bearcode.ovf.model.express;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2008
 * Time: 7:03:27 PM
 * @author Leonid Ginzburg
 */
public class CountryDescription {
    private long id;
    private String name;
    private boolean pickup;
    private boolean dropoff;
    private String servicePhone;
    private String fedexUrl;
    private String serviceUrl;
    private String dropoffUrl;
    private String tcUrl;
    private double rate;
    private String currencyCode;
    private String localCurrencyName;
    private double exchangeRate;
    private String deliveryTime;
    private String accountNumber;
    private Date lastDate;
    private String group;
    private String zipPattern;
    private String countryCode;
    private String lastDateText;
    private String comment;
    private double gmtOffset;

    public double getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(double gmtOffset) {
        this.gmtOffset = gmtOffset;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLastDateText() {
        return lastDateText;
    }

    public void setLastDateText(String lastDateText) {
        this.lastDateText = lastDateText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public boolean isDropoff() {
        return dropoff;
    }

    public void setDropoff(boolean dropoff) {
        this.dropoff = dropoff;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getFedexUrl() {
        return fedexUrl;
    }

    public void setFedexUrl(String fedexUrl) {
        this.fedexUrl = fedexUrl;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        if ( serviceUrl.indexOf("http://") < 0 ) {
            this.serviceUrl = "http://" + serviceUrl;
        } else {
            this.serviceUrl = serviceUrl;
        }
    }

    public String getDropoffUrl() {
        return dropoffUrl;
    }

    public void setDropoffUrl(String dropoffUrl) {
        if ( dropoffUrl.indexOf("http://") < 0 ) {
            this.dropoffUrl = "http://" + dropoffUrl;
        }
        else {
            this.dropoffUrl = dropoffUrl;
        }
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        if ( rate != null )
            this.rate = rate;
        else
            this.rate = 0;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLocalCurrencyName() {
        return localCurrencyName;
    }

    public void setLocalCurrencyName(String localCurrencyName) {
        this.localCurrencyName = localCurrencyName;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        if ( exchangeRate != null )
            this.exchangeRate = exchangeRate;
        else
            this.exchangeRate = 0;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public Date getLastDateLocal(){ 
       long time = getLastDate().getTime() + (long)(getGmtOffset() * 3600000); //multiply by milliseconds in an hour
       return new Date(time);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

	public String getTcUrl() {
		return tcUrl;
	}

	public void setTcUrl(String tcUrl) {
		this.tcUrl = tcUrl;
	}


    public String getZipPattern() {
        return zipPattern;
    }

    public void setZipPattern(String zipPattern) {
        this.zipPattern = zipPattern;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
