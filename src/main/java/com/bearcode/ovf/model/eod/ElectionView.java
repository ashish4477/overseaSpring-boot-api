package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.webservices.localelections.model.ElectionLevel;
import com.bearcode.ovf.webservices.localelections.model.ElectionLevelSortOrder;
import com.bearcode.ovf.webservices.localelections.model.ElectionType;

/**
 * Created by leonid on 01.06.16.
 */
public interface ElectionView extends Comparable<ElectionView> {

    public String getCitizenBallotRequest();
    public String getCitizenBallotReturn();
    public String getCitizenRegistration();

    public String getDomesticBallotRequest();
    public String getDomesticBallotReturn();
    public String getDomesticRegistration();

    public String getMilitaryBallotRequest();
    public String getMilitaryBallotReturn();
    public String getMilitaryRegistration();

    public String getDomesticEarlyVoting();
    public String getAbsenteeVoting();

    public String getDomesticNotes();
    public String getNotes();

    public String getHeldOn();
    public String getTitle();

    public String getStateId();

    public String getStateName();
    public String getStateAbbr();

    public ElectionLevelSortOrder getSortOrder();
    public ElectionLevel getElectionLevel();
    public ElectionType getElectionType();

}
