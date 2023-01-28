package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.State;

import java.io.Serializable;

/**
 * Date: 25.12.13
 * Time: 20:22
 *
 * @author Leonid Ginzburg
 */
public class StateVotingLaws implements Serializable {

    private static final long serialVersionUID = -1869257421427213921L;

    private long id;
    private State state;
    private boolean earlyInPersonVoting = false;
    private boolean noExcuseAbsenteeVoting = false;
    //private boolean absenteeVotingWithExcuse;
    private SameDayRegistrationType sameDayRegistration = SameDayRegistrationType.NO;
    private MailVotingType allMailVoting = MailVotingType.EMPTY;
    private VoterIdType voterId = VoterIdType.EMPTY;
    private String voterIdAdditional = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isEarlyInPersonVoting() {
        return earlyInPersonVoting;
    }

    public void setEarlyInPersonVoting(boolean earlyInPersonVoting) {
        this.earlyInPersonVoting = earlyInPersonVoting;
    }

    public boolean isNoExcuseAbsenteeVoting() {
        return noExcuseAbsenteeVoting;
    }

    public void setNoExcuseAbsenteeVoting(boolean noExcuseAbsenteeVoting) {
        this.noExcuseAbsenteeVoting = noExcuseAbsenteeVoting;
    }

    /*public boolean isAbsenteeVotingWithExcuse() {
        return absenteeVotingWithExcuse;
    }

    public void setAbsenteeVotingWithExcuse(boolean absenteeVotingWithExcuse) {
        this.absenteeVotingWithExcuse = absenteeVotingWithExcuse;
    }*/

    public SameDayRegistrationType getSameDayRegistration() {
        return sameDayRegistration;
    }

    public void setSameDayRegistration(SameDayRegistrationType sameDayRegistration) {
        this.sameDayRegistration = sameDayRegistration;
    }

    public MailVotingType getAllMailVoting() {
        return allMailVoting;
    }

    public void setAllMailVoting(MailVotingType allMailVoting) {
        this.allMailVoting = allMailVoting;
    }

    public VoterIdType getVoterId() {
        return voterId;
    }

    public void setVoterId(VoterIdType voterId) {
        this.voterId = voterId;
    }

    public String getVoterIdAdditional() {
        return voterIdAdditional;
    }

    public void setVoterIdAdditional(String voterIdAdditional) {
        this.voterIdAdditional = voterIdAdditional;
    }
}
