package com.bearcode.ovf.actions.express.forms;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.express.CountryDescription;
import com.bearcode.ovf.model.express.FedexLabel;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 11, 2008
 * Time: 5:38:51 PM
 * @author Leonid Ginzburg
 */
public class ExpressForm {
    private long countryId;
    private CountryDescription country;

    //private String login;
    //private String passw;

    private boolean doLogin;
    private boolean loggedUser = false; 

    private String firstName="";
    private String middleName="";
    private String lastName="";
    private Address pickUp;

    private String notificationEmail="";
    private String notificationEmailConfirm="";
    private String notificationPass="";
    private String notificationPassConfirm="";
    private String notificationPhone="";

    private LocalOffice destinationLeo;

    private String nameOnCard="";
    private String ccNumber="";
    private String cvv="";
    private String ccExpiredMonth="";
    private String ccExpiredYear="";

    private Address billing;
    private String billingCountry = "";

    private String fedexMessage="";
    private FedexLabel fedexLabel;

    private String authorizenetMessage="";

    public ExpressForm() {
        pickUp = new Address();
        billing = new Address();
    }

    public boolean isDoLogin() {
        return doLogin;
    }

    public void setDoLogin(boolean doLogin) {
        this.doLogin = doLogin;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }


    public CountryDescription getCountry() {
        return country;
    }

    public void setCountry(CountryDescription country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getPickUp() {
        return pickUp;
    }

    public void setPickUp(Address pickUp) {
        this.pickUp = pickUp;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public String getNotificationEmailConfirm() {
        return notificationEmailConfirm;
    }

    public void setNotificationEmailConfirm(String notificationEmailConfirm) {
        this.notificationEmailConfirm = notificationEmailConfirm;
    }

    public String getNotificationPass() {
        return notificationPass;
    }

    public void setNotificationPass(String notificationPass) {
        this.notificationPass = notificationPass;
    }

    public String getNotificationPassConfirm() {
        return notificationPassConfirm;
    }

    public void setNotificationPassConfirm(String notificationPassConfirm) {
        this.notificationPassConfirm = notificationPassConfirm;
    }

    public String getNotificationPhone() {
        return notificationPhone;
    }

    public void setNotificationPhone(String notificationPhone) {
        this.notificationPhone = notificationPhone;
    }

    public LocalOffice getDestinationLeo() {
        return destinationLeo;
    }

    public void setDestinationLeo(LocalOffice destinationLeo) {
        this.destinationLeo = destinationLeo;
    }

    /*public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }*/

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCcExpiredMonth() {
        return ccExpiredMonth;
    }

    public void setCcExpiredMonth(String ccExpiredMonth) {
        this.ccExpiredMonth = ccExpiredMonth;
    }

    public String getCcExpiredYear() {
        return ccExpiredYear;
    }

    public void setCcExpiredYear(String ccExpiredYear) {
        this.ccExpiredYear = ccExpiredYear;
    }

    public String getCardExpired() {
        return ccExpiredMonth + ccExpiredYear;
    }

    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;  
    }


    public String getFedexMessage() {
        return fedexMessage;
    }

    public void setFedexMessage(String fedexMessage) {
        this.fedexMessage = fedexMessage;
    }

    public FedexLabel getFedexLabel() {
        return fedexLabel;
    }

    public void setFedexLabel(FedexLabel fedexLabel) {
        this.fedexLabel = fedexLabel;
    }


    public String getAuthorizenetMessage() {
        return authorizenetMessage;
    }

    public void setAuthorizenetMessage(String authorizenetMessage) {
        this.authorizenetMessage = authorizenetMessage;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public boolean isLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(boolean loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Address getBilling() {
        return billing;
    }

    public void setBilling(Address billing) {
        this.billing = billing;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }
}
