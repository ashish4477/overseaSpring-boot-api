package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by dhughes 08/31/2016.
 */
public class VotingMethod implements Serializable {
    private static final long serialVersionUID = 8206997939456558922L;
    private long id = 0;
    private VotingMethodType votingMethodType;
    private boolean allowed;
    private String additionalInfo;
    private String additionalInfoType;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public VotingMethodType getVotingMethodType(){
        return votingMethodType;
    }

    public void setVotingMethodType(VotingMethodType votingMethodType){
        this.votingMethodType = votingMethodType;
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void setAllowed(boolean allowed){
        this.allowed = allowed;
    }

    public String getAdditionalInfo(){
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo){
        this.additionalInfo = additionalInfo;
    }

    public String getAdditionalInfoType(){
        return additionalInfoType;
    }

    public void setAdditionalInfoType(String additionalInfoType){
        this.additionalInfoType = additionalInfoType;
    }
}
