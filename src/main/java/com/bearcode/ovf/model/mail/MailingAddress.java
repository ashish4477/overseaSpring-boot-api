package com.bearcode.ovf.model.mail;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VoterClassificationType;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 30, 2008
 * Time: 4:32:41 PM
 *
 * @author Leonid Ginzburg
 */
public class MailingAddress implements Serializable {
    private static final long serialVersionUID = 3617351962199718338L;
    private long id = 0;
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String votingCity = "";
    private State state;
    private String votingStateName = "";
    private VotingRegion region;
    private String eodRegion = "";
    private String votingRegionName = "";
    private String votingPostalCode = "";
    private long birthYear;
    private String voterType = "";
    private String voterClassificationType = "";

    private String currentAddress = "";
    private String currentCity = "";
    private String currentPostalCode = "";
    private long currentCountry;
    private String currentCountryName = "";

    private String phone = "";
    private String url = "";


    public String getVotingPostalCode() {
        return votingPostalCode;
    }

    public void setVotingPostalCode( String votingPostalCode ) {
        this.votingPostalCode = votingPostalCode;
    }

    public long getBirthYear() {
        return birthYear;
    }

    public void setBirthYear( long birthYear ) {
        this.birthYear = birthYear;
    }

    public String getVoterType() {
        return voterType;
    }

    public void setVoterType( String voterType ) {
        this.voterType = voterType;
    }

    public String getVoterClassificationType() {
        return voterClassificationType;
    }

    public void setVoterClassificationType(String voterClassificationType) {
        this.voterClassificationType = voterClassificationType;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress( String currentAddress ) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentPostalCode() {
        return currentPostalCode;
    }

    public void setCurrentPostalCode( String currentPostalCode ) {
        this.currentPostalCode = currentPostalCode;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity( String currentCity ) {
        this.currentCity = currentCity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public String getVotingCity() {
        return votingCity;
    }

    public void setVotingCity( String votingCity ) {
        this.votingCity = votingCity;
    }

    public String getVotingStateName() {
        return votingStateName;
    }

    public void setVotingStateName( String votingStateName ) {
        this.votingStateName = votingStateName;
    }

    public String getCurrentCountryName() {
        return currentCountryName;
    }

    public void setCurrentCountryName( String currentCountryName ) {
        this.currentCountryName = currentCountryName;
    }

    public long getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry( long currentCountry ) {
        this.currentCountry = currentCountry;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public VotingRegion getRegion() {
        return region;
    }

    public void setRegion( VotingRegion region ) {
        this.region = region;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public State getState() {
        return state;
    }

    public void setState( State state ) {
        this.state = state;
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

    public String getEodRegion() {
        return eodRegion;
    }

    public void setEodRegion( String eodRegion ) {
        this.eodRegion = eodRegion;
    }

    public String getVotingRegionName() {
        return votingRegionName;
    }

    public void setVotingRegionName( String votingRegionName ) {
        this.votingRegionName = votingRegionName;
    }

    public List<Map<String,String>> prepareGetResponseParams() {
        List<Map<String,String>> customs = new LinkedList<Map<String, String>>();
        /*
        OVF:
user_type -- [new] this should be a choice of: Voter, Press, Org
voter_type
voting _county
voting_state_name

USVOTE:
user_type -- [new] this should be a choice of: Voter, Press, Org
voter_type
VotingState
Country
         */
        Map<String,String> paramUserType = new HashMap<String, String>();
        paramUserType.put( "name", "user_type" );
        paramUserType.put( "content", "Voter" );
        customs.add( paramUserType );

        try {
            VoterType type = VoterType.valueOf( voterType );
            Map<String,String> paramVoterType = new HashMap<String, String>();
            paramVoterType.put( "name" ,"voter_type" );
            paramVoterType.put( "content", type.getTitle() );
            customs.add( paramVoterType );
        } catch (IllegalArgumentException e) {
            //
        }

        // Voter Classification
        if (voterClassificationType != null) {
            Map<String, String> paramVoterClassification = new HashMap<>();
            paramVoterClassification.put("name", "user_classification_type");
            paramVoterClassification.put("content", "VoterClassification");
            customs.add(paramVoterClassification);

            try {
                VoterClassificationType vcType = VoterClassificationType.valueOf(voterClassificationType);
                Map<String, String> paramVoterClassificationType = new HashMap<>();
                paramVoterClassificationType.put("name", "voter_classification_type");
                paramVoterClassificationType.put("content", vcType.getValue());
                customs.add(paramVoterClassificationType);
            } catch (IllegalArgumentException e) {
                //
            }
        }

        if ( region != null ) {
            Map<String,String> paramVotingRegion = new HashMap<>();
            paramVotingRegion.put( "name", "voting_region" );
            paramVotingRegion.put( "content", region.getName() );
            customs.add( paramVotingRegion );

            if ( region.getCounty() != null ) {
                Map<String,String> paramVotingCounty = new HashMap<String, String>();
                paramVotingCounty.put( "name", "voting_county" );
                paramVotingCounty.put( "content", region.getCounty().getName() );
                customs.add( paramVotingCounty );
            }
        }

        if ( votingStateName != null && !votingStateName.isEmpty() ) {
            Map<String,String> paramState = new HashMap<String, String>();
            paramState.put( "name" ,"voting_state_name" );
            paramState.put( "content", votingStateName );
            customs.add( paramState );

            Map<String,String> paramUSVoteState = new HashMap<String, String>();
            paramUSVoteState.put( "name" ,"VotingState" );
            paramUSVoteState.put( "content", votingStateName );
            customs.add( paramUSVoteState );
        }

        if ( currentCountryName != null && !currentCountryName.isEmpty() ) {
            Map<String,String> paramCountry = new HashMap<String, String>();
            paramCountry.put( "name" ,"Country" );
            paramCountry.put( "content", currentCountryName );
            customs.add( paramCountry );
        }

        return customs;
    }
}
