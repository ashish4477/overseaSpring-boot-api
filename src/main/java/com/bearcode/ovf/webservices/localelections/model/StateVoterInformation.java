package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dhughes 08/31/2016.
 */
public class StateVoterInformation implements Serializable {
    private static final long serialVersionUID = 8206997939080937461L;
    private long id = 0;
    private StateOfElection state;
    private String eligibilityGeneralInfo;
    private String votingGeneralInfo;
    private String voterIdGeneralInfo;
    private List<EligibilityRequirementsList> eligibilityRequirements; // A list of lists
    private List<IdentificationRequirementsList> identificationRequirements; // A list of lists
    private List<WitnessNotarizationRequirementsList> witnessNotarizationRequirements; // A list of lists
    private List<TransmissionMethodsList> transmissionMethods;
    private List<VotingMethod> votingMethods;
    private List<LookupToolListItem> lookupTools;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public StateOfElection getState(){
        return state;
    }

    public void setState(StateOfElection state){
        this.state = state;
    }

    public String getVotingGeneralInfo(){
        return votingGeneralInfo;
    }

    public void setVotingGeneralInfo(String votingGeneralInfo){
        this.votingGeneralInfo = votingGeneralInfo;
    }

    public String getVoterIdGeneralInfo(){
        return voterIdGeneralInfo;
    }

    public void setVoterIdGeneralInfo(String voterIdGeneralInfo){
        this.voterIdGeneralInfo = voterIdGeneralInfo;
    }

    public String getEligibilityGeneralInfo(){
        return eligibilityGeneralInfo;
    }

    public void setEligibilityGeneralInfo(String eligibilityGeneralInfo){
        this.eligibilityGeneralInfo = eligibilityGeneralInfo;
    }

    public List<EligibilityRequirementsList> getEligibilityRequirements(){
        return eligibilityRequirements;
    }

    public void setEligibilityRequirements(List<EligibilityRequirementsList> eligibilityRequirements){
        this.eligibilityRequirements = eligibilityRequirements;
    }

    public List<IdentificationRequirementsList> getIdentificationRequirements(){
        return identificationRequirements;
    }

    public void setIdentificationRequirements(List<IdentificationRequirementsList> identificationRequirements){
        this.identificationRequirements = identificationRequirements;
    }

    public List<WitnessNotarizationRequirementsList> getWitnessNotarizationRequirements(){
        return witnessNotarizationRequirements;
    }

    public void setWitnessNotarizationRequirements(List<WitnessNotarizationRequirementsList> witnessNotarizationRequirements){
        this.witnessNotarizationRequirements = witnessNotarizationRequirements;
    }

    public List<TransmissionMethodsList> getTransmissionMethods(){
        return transmissionMethods;
    }

    public void setTransmissionMethods(List<TransmissionMethodsList> transmissionMethods){
        this.transmissionMethods = transmissionMethods;
    }

    public List<VotingMethod> getVotingMethods(){
        Collections.sort(votingMethods, new Comparator<VotingMethod>() {
            public int compare(VotingMethod o1, VotingMethod o2) {
                return Double.compare( o1.getVotingMethodType().getId(), o2.getVotingMethodType().getId());
            }
        });
        return votingMethods;
    }

    public void setVotingMethods(List<VotingMethod> votingMethods){
        this.votingMethods = votingMethods;
    }

    public List<LookupToolListItem> getLookupTools(){
        Collections.sort( lookupTools, new Comparator<LookupToolListItem>() {
            @Override
            public int compare( LookupToolListItem o1, LookupToolListItem o2 ) {
                return Double.compare( o1.getLookupTool().getId(), o2.getLookupTool().getId() );
            }
        } );
        return lookupTools;
    }

    public void setLookupTools(List<LookupToolListItem> lookupTools){
        this.lookupTools = lookupTools;
    }

    public List<LookupToolListItem> getValidLookupTools(){
        List<LookupToolListItem> newList = new ArrayList<LookupToolListItem>();
        for (LookupToolListItem item: getLookupTools() ) {
            if (item.getUrl() != null  && item.getUrl().matches("^http.+") ) {
                newList.add(item);
            }
        }
        return newList;
    }

    public List<VotingMethod> getAllowedVotingMethods(){
        List<VotingMethod> newList = new ArrayList<VotingMethod>();
        for ( VotingMethod votingMethod : getVotingMethods() ) {
            if ( votingMethod.isAllowed() ) {
                newList.add(votingMethod);
            }
        }
        return newList;
    }

}
