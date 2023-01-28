package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.webservices.localelections.model.ElectionLevel;
import com.bearcode.ovf.webservices.localelections.model.ElectionLevelSortOrder;
import com.bearcode.ovf.webservices.localelections.model.ElectionType;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
public class Election implements ElectionView {
    private long id;
    private StateSpecificDirectory stateInfo;

    /**
     * the type of election.
     *
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    private TypeOfElection typeOfElection;

    private String title;
    private String heldOn;
    private String citizenRegistration;
    private String citizenBallotRequest;
    private String citizenBallotReturn;
    private String militaryRegistration;
    private String militaryBallotRequest;
    private String militaryBallotReturn;

    /**
     * the due date for a domestic registration.
     *
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    private String domesticRegistration;

    /**
     * the due date for a domestic ballot request.
     *
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    private String domesticBallotRequest;

    /**
     * the due date for a domestic ballot return.
     *
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    private String domesticBallotReturn;

    /**
     * early voting for domestic voters.
     *
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 18, 2012
     */
    private String domesticEarlyVoting;

    /**
     * the notes for domestic voters.
     *
     * @author IanBrown
     * @version Jan 25, 2012
     * @since Jan 25, 2012
     */
    private String domesticNotes;

    private String notes;
    private int order;

    public String getCitizenBallotRequest() {
        return citizenBallotRequest;
    }

    public String getCitizenBallotReturn() {
        return citizenBallotReturn;
    }

    public String getCitizenRegistration() {
        return citizenRegistration;
    }

    /**
     * Gets the due date for a domestic ballot request.
     *
     * @return the domestic ballot request.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public String getDomesticBallotRequest() {
        return domesticBallotRequest;
    }

    /**
     * Gets the due date for a domestic ballot return.
     *
     * @return the domestic ballot return.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public String getDomesticBallotReturn() {
        return domesticBallotReturn;
    }

    /**
     * Gets the domestic early voting.
     *
     * @return the domestic early voting.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 18, 2012
     */
    public String getDomesticEarlyVoting() {
        return domesticEarlyVoting;
    }

    /**
     * Gets the domestic notes.
     *
     * @return the domestic notes.
     * @author IanBrown
     * @version Jan 25, 2012
     * @since Jan 25, 2012
     */
    public String getDomesticNotes() {
        return domesticNotes;
    }

    /**
     * Gets the due date for a domestic registration.
     *
     * @return the domestic registration.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public String getDomesticRegistration() {
        return domesticRegistration;
    }

    public String getHeldOn() {
        return heldOn;
    }

    public long getId() {
        return id;
    }

    public String getMilitaryBallotRequest() {
        return militaryBallotRequest;
    }

    public String getMilitaryBallotReturn() {
        return militaryBallotReturn;
    }

    public String getMilitaryRegistration() {
        return militaryRegistration;
    }

    public String getNotes() {
        return notes;
    }

    public int getOrder() {
        return order;
    }

    @JsonIgnore
    public StateSpecificDirectory getStateInfo() {
        return stateInfo;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getStateId() {
        return String.valueOf( stateInfo.getState().getId() );
    }

    @Override
    public String getStateName() {
        return stateInfo.getState().getName();
    }

    /**
     * Gets the type of election.
     *
     * @return the type of election.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public TypeOfElection getTypeOfElection() {
        return typeOfElection;
    }

    public void setCitizenBallotRequest( final String citizenBallotRequest ) {
        this.citizenBallotRequest = citizenBallotRequest;
    }

    public void setCitizenBallotReturn( final String citizenBallotReturn ) {
        this.citizenBallotReturn = citizenBallotReturn;
    }

    public void setCitizenRegistration( final String citizenRegistration ) {
        this.citizenRegistration = citizenRegistration;
    }

    /**
     * Sets the due date for a domestic ballot request.
     *
     * @param domesticBallotRequest the domestic ballot request to set.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public void setDomesticBallotRequest( final String domesticBallotRequest ) {
        this.domesticBallotRequest = domesticBallotRequest;
    }

    /**
     * Sets the due date for a domestic ballot return.
     *
     * @param domesticBallotReturn the domestic ballot return to set.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public void setDomesticBallotReturn( final String domesticBallotReturn ) {
        this.domesticBallotReturn = domesticBallotReturn;
    }

    /**
     * Sets the domestic early voting.
     *
     * @param domesticEarlyVoting the domestic early voting to set.
     * @author IanBrown
     * @version Jan 18, 2012
     * @since Jan 18, 2012
     */
    public void setDomesticEarlyVoting( final String domesticEarlyVoting ) {
        this.domesticEarlyVoting = domesticEarlyVoting;
    }

    /**
     * Sets the domestic notes.
     *
     * @param domesticNotes the domestic notes to set.
     * @author IanBrown
     * @version Jan 25, 2012
     * @since Jan 25, 2012
     */
    public void setDomesticNotes( final String domesticNotes ) {
        this.domesticNotes = domesticNotes;
    }

    /**
     * Sets the due date for a domestic registration.
     *
     * @param domesticRegistration the domestic registration to set.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public void setDomesticRegistration( final String domesticRegistration ) {
        this.domesticRegistration = domesticRegistration;
    }

    public void setHeldOn( final String heldOn ) {
        this.heldOn = heldOn;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    public void setMilitaryBallotRequest( final String militaryBallotRequest ) {
        this.militaryBallotRequest = militaryBallotRequest;
    }

    public void setMilitaryBallotReturn( final String militaryBallotReturn ) {
        this.militaryBallotReturn = militaryBallotReturn;
    }

    public void setMilitaryRegistration( final String militaryRegistration ) {
        this.militaryRegistration = militaryRegistration;
    }

    public void setNotes( final String notes ) {
        this.notes = notes;
    }

    public void setOrder( final int order ) {
        this.order = order;
    }

    public void setStateInfo( final StateSpecificDirectory stateInfo ) {
        this.stateInfo = stateInfo;
    }

    public void setTitle( final String title ) {
        this.title = title;
    }

    /**
     * Sets the type of election.
     *
     * @param typeOfElection the type of election to set.
     * @author IanBrown
     * @version Jan 13, 2012
     * @since Jan 13, 2012
     */
    public void setTypeOfElection( final TypeOfElection typeOfElection ) {
        this.typeOfElection = typeOfElection;
    }

    @Override
    public String getStateAbbr() {
        return stateInfo.getState().getAbbr();
    }

    @Override
    public String getAbsenteeVoting() {
        return "";
    }

    @Override
    public int compareTo( ElectionView o ) {
        return 0;  // elections are not using any more
    }

    @Override
    public ElectionLevelSortOrder getSortOrder() {
        return ElectionLevelSortOrder.OTHER;
    }

    @Override
    public ElectionLevel getElectionLevel() {
        return new ElectionLevel();
    }

    @Override
    public ElectionType getElectionType() {
        return new ElectionType();
    }
}
