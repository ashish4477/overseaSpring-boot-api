package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by leonid on 26.05.16.
 */
public class LocalElection implements Serializable {
    private static final SimpleDateFormat LONG_FORMAT = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss" );
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
    private static final long serialVersionUID = 7086402315675703862L;

    private long id = 0;
    private String createdAt = "";
    private String updatedAt = "";
    private String title = "";
    private String contactEmail = "";
    private String contactPhone = "";
    private StateOfElection state;
    private ElectionLevel electionLevel;
    private ElectionType electionType;
    private String electionStatus = "";
    private boolean electionDayRegistrationIsAvailable = false;
    private boolean useOverseasDatesAsMilitaryDates = false;
    private String electionDate = "";
    private boolean isPublic = true;
    private String additionalInformation = "";
    private List<ElectionLocation> locations;
    private List<ElectionUrl> urls;
    private List<ElectionDate> dates;
    private String shortUrl = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt( String createdAt ) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt( String updatedAt ) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail( String contactEmail ) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone( String contactPhone ) {
        this.contactPhone = contactPhone;
    }

    public ElectionLevel getElectionLevel() {
        return electionLevel;
    }

    public void setElectionLevel( ElectionLevel electionLevel ) {
        this.electionLevel = electionLevel;
    }

    public ElectionType getElectionType() {
        return electionType;
    }

    public void setElectionType( ElectionType electionType ) {
        this.electionType = electionType;
    }

    public String getElectionStatus() {
        return electionStatus;
    }

    public void setElectionStatus( String electionStatus ) {
        this.electionStatus = electionStatus;
    }

    public boolean isElectionDayRegistrationIsAvailable() {
        return electionDayRegistrationIsAvailable;
    }

    public void setElectionDayRegistrationIsAvailable( boolean electionDayRegistrationIsAvailable ) {
        this.electionDayRegistrationIsAvailable = electionDayRegistrationIsAvailable;
    }

    public String getElectionDate() {
        return electionDate;
    }

    public void setElectionDate( String electionDate ) {
        this.electionDate = electionDate;
    }

    public Date getElectionDateAsDate() {
        Date electionDate = null;
        try {
            electionDate = DATE_FORMAT.parse( this.electionDate );
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return electionDate;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic( boolean isPublic ) {
        this.isPublic = isPublic;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation( String additionalInformation ) {
        this.additionalInformation = additionalInformation;
    }

    public List<ElectionLocation> getLocations() {
        return locations;
    }

    public void setLocations( List<ElectionLocation> locations ) {
        this.locations = locations;
    }

    public List<ElectionUrl> getUrls() {
        return urls;
    }

    public void setUrls( List<ElectionUrl> urls ) {
        this.urls = urls;
    }

    public List<ElectionDate> getDates() {
        return dates;
    }

    public void setDates( List<ElectionDate> dates ) {
        this.dates = dates;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl( String shortUrl ) {
        this.shortUrl = shortUrl;
    }

    public boolean isUseOverseasDatesAsMilitaryDates() {
        return useOverseasDatesAsMilitaryDates;
    }

    public void setUseOverseasDatesAsMilitaryDates( boolean useOverseasDatesAsMilitaryDates ) {
        this.useOverseasDatesAsMilitaryDates = useOverseasDatesAsMilitaryDates;
    }

    public StateOfElection getState() {
        return state;
    }

    public void setState( StateOfElection state ) {
        this.state = state;
    }
}
