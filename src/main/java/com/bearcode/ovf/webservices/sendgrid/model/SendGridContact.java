package com.bearcode.ovf.webservices.sendgrid.model;

import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.model.mail.MailingLink;

import java.util.Calendar;
import java.util.Date;

/**
 * @author leonid.
 */
public class SendGridContact {
    private String email;
    private String firstName;
    private String lastName;
    private String name;
    private String campaign;
    private String origin;
    private Date birthyear;
    private String countryCode;
    private String currentCity;
    private long mobilePhone;
    private String userType;
    private String voterType;
    private String votingState;
    private String votingRegion;
    private long votingZipcode;
    private String ansiFipsState;
    private String ansiFipsCountry;
    private String ansiFipsCity;
    private String url;

    public SendGridContact( MailingLink link ) {
        campaign = link.getMailingList().getName();
        origin = "api";
        MailingAddress ma = link.getMailingAddress();
        email = ma.getEmail();
        firstName = ma.getFirstName();
        lastName = ma.getLastName();
        StringBuilder tempName = new StringBuilder( firstName );
        if ( tempName.length() > 0 ) tempName.append( " " );
        tempName.append( lastName );
        name = tempName.toString();
        Calendar date = Calendar.getInstance();
        date.set( (int)ma.getBirthYear(), Calendar.JANUARY, 1 );
        birthyear = date.getTime();
        countryCode = ma.getCurrentCountryName();
        currentCity = ma.getCurrentCity();
        mobilePhone = extractInt( ma.getPhone() );
        userType = "voter";
        try {
            voterType = VoterType.valueOf( ma.getVoterType() ).getTitle();
        } catch (IllegalArgumentException e) {
            voterType = VoterType.UNSPECIFIED.getTitle();
        }
        votingState = ma.getVotingStateName();
        votingRegion = ma.getVotingRegionName();
        votingZipcode = extractInt( ma.getVotingPostalCode() );
        ansiFipsState = ma.getVotingStateName();
        ansiFipsCity = ma.getVotingCity();
        ansiFipsCountry = "";
        url = ma.getUrl();
    }

    private long extractInt( String value ) {
        if ( value == null ) return 0;
        try {
            return Long.parseLong( value.replaceAll( "\\D", "" ) ); // remove non-digits
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public Date getBirthyear() {
        return birthyear;
    }

    public void setBirthyear( Date birthyear ) {
        this.birthyear = birthyear;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode( String countryCode ) {
        this.countryCode = countryCode;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity( String currentCity ) {
        this.currentCity = currentCity;
    }

    public long getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone( long mobilePhone ) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType( String userType ) {
        this.userType = userType;
    }

    public String getVoterType() {
        return voterType;
    }

    public void setVoterType( String voterType ) {
        this.voterType = voterType;
    }

    public String getVotingState() {
        return votingState;
    }

    public void setVotingState( String votingState ) {
        this.votingState = votingState;
    }

    public String getVotingRegion() {
        return votingRegion;
    }

    public void setVotingRegion( String votingRegion ) {
        this.votingRegion = votingRegion;
    }

    public long getVotingZipcode() {
        return votingZipcode;
    }

    public void setVotingZipcode( long votingZipcode ) {
        this.votingZipcode = votingZipcode;
    }

    public String getAnsiFipsState() {
        return ansiFipsState;
    }

    public void setAnsiFipsState( String ansiFipsState ) {
        this.ansiFipsState = ansiFipsState;
    }

    public String getAnsiFipsCountry() {
        return ansiFipsCountry;
    }

    public void setAnsiFipsCountry( String ansiFipsCountry ) {
        this.ansiFipsCountry = ansiFipsCountry;
    }

    public String getAnsiFipsCity() {
        return ansiFipsCity;
    }

    public void setAnsiFipsCity( String ansiFipsCity ) {
        this.ansiFipsCity = ansiFipsCity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign( String campaign ) {
        this.campaign = campaign;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin( String origin ) {
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
