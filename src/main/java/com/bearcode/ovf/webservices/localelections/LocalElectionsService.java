package com.bearcode.ovf.webservices.localelections;

import com.bearcode.ovf.model.eod.ElectionView;
import com.bearcode.ovf.utils.CacheKeyConstants;
import com.bearcode.ovf.webservices.localelections.model.*;
import com.bearcode.ovf.webservices.util.model.Meta;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by leonid on 25.05.16.
 */
@Component
public class LocalElectionsService {

    protected final static Logger log = LoggerFactory.getLogger( LocalElectionsService.class.getName() );
    private class ElectionComparator implements Comparator<ElectionView> {

        @Override
        public int compare( ElectionView o1, ElectionView o2 ) {
            int stageOne = o1.getStateName().compareTo( o2.getStateName() );
            if ( stageOne == 0 ) {
                int stageTwo = o1.getTitle().compareTo( o2.getTitle() );
                if ( stageTwo == 0 &&
                        o1 instanceof LocalElectionDecorator &&
                        o2 instanceof LocalElectionDecorator ) {
                    LocalElectionDecorator elOne = (LocalElectionDecorator) o1;
                    LocalElectionDecorator elTwo = (LocalElectionDecorator) o2;
                    if ( elOne.getElectionDateAsDate() != null
                            && elTwo.getElectionDateAsDate() != null ) {
                        return elOne.getElectionDateAsDate().compareTo( elTwo.getElectionDateAsDate() );
                    }
                }
                return stageTwo;
            }
            return stageOne;
        }
    }

    @Autowired
    private LocalElectionConnector connector;

    private Gson gson;

    public LocalElectionsService() {
        gson = new GsonBuilder()
                .setFieldNamingPolicy( FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES )
                .create();
    }

    public LocalElectionConnector getConnector() {
        return connector;
    }

    public void setConnector( LocalElectionConnector connector ) {
        this.connector = connector;
    }

    @Cacheable( value = CacheKeyConstants.GET_ALL_STATES )
    public List<StateOfElection> getAllStates() {

        Map<String, String> params = new HashMap<String, String>();
        params.put( "offset", "0" );
        params.put( "limit", "70" );  // get all states.

        JsonObject result = connector.callMethod( LocalElectionConnector.GET_STATES, params );
        List<StateOfElection> states = null;
        if ( result != null ) {
            JsonElement statesElement = result.get( "objects" );
            states = gson.fromJson( statesElement, new TypeToken<List<StateOfElection>>() {}.getType() );
        }
        return states != null ? states : Collections.<StateOfElection>emptyList();
    }

    @Cacheable( value =  CacheKeyConstants.GET_ALL_STATE_VOTER_INFORMATION )
    public List<StateVoterInformation> getAllStateVoterInformation() {

        Map<String, String> params = new HashMap<String, String>();
        params.put( "offset", "0" );
        params.put( "limit", "100" );  // get all state voter information.

        JsonObject result = connector.callMethod( LocalElectionConnector.GET_STATE_VOTER_INFORMATION, params );
        List<StateVoterInformation> states = null;
        if ( result != null ) {
            JsonElement stateVoterInformationElements = result.get( "objects" );
            states = gson.fromJson( stateVoterInformationElements, new TypeToken<List<StateVoterInformation>>() {}.getType() );
        }
        
        if (states != null) {
            Collections.sort(states, new Comparator<StateVoterInformation>() {
                public int compare(StateVoterInformation s1, StateVoterInformation s2) {
                    return s1.getState().getName().compareTo(s2.getState().getName());
                }
            });
            return states;
        }
        return Collections.<StateVoterInformation>emptyList();
    }

    @Cacheable( value = CacheKeyConstants.FIND_ELECTIONS_OF_STATE, key = "'elections_of_state:'+#stateShortName")
    public List<ElectionView> findElectionsOfState( String stateShortName ) {
        List<StateOfElection> states = getAllStates();
        String stateId = "";
        for ( StateOfElection stateOfElection : states ) {
            if ( stateOfElection.getShortName().equalsIgnoreCase( stateShortName )) {
                stateId = stateOfElection.getId();
                break;
            }
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put( "state_id", stateId );

        Meta meta = new Meta();
        List<ElectionView> elections = new ArrayList<ElectionView>();
        do {
            JsonObject oneCall = connector.callMethod( LocalElectionConnector.GET_ELECTIONS, params );
            if ( oneCall != null ) {
                meta = gson.fromJson( oneCall.get( "meta" ), Meta.class );
                JsonElement statesElement = oneCall.get( "objects" );
                elections.addAll( gson.<List<? extends ElectionView>>fromJson( statesElement, new TypeToken<List<LocalElectionDecorator>>() {}.getType() ) );

                params.clear();
                params.put( "state_id", stateId );
                params.put( "offset", String.valueOf( meta.getOffset() + meta.getLimit() ) );
                params.put( "limit", String.valueOf( meta.getLimit() ) );
            }

        } while ( meta != null && meta.getOffset() + meta.getLimit() < meta.getTotalObjects() );
            removeArchivedElections( elections );
            Collections.sort( elections/*, new ElectionComparator()*/ );
        return elections;
    }

     @Cacheable( value = CacheKeyConstants.FIND_STATE_VOTER_INFORMATION, key = "'svid_of_state:'+#stateShortName")
     public StateVoterInformation findStateVoterInformation( String stateShortName ) {
         List<StateOfElection> states = getAllStates();
         String stateId = "";
         for ( StateOfElection stateOfElection : states ) {
             if ( stateOfElection.getShortName().equalsIgnoreCase( stateShortName )) {
                 stateId = stateOfElection.getId();
                 break;
             }
         }
         Map<String, String> params = new HashMap<String, String>();
         params.put( "state_id", stateId );

         JsonObject result = connector.callMethod( LocalElectionConnector.GET_STATE_VOTER_INFORMATION, params );
         List<StateVoterInformation> statesInfo = null;
         if ( result != null ) {
             JsonElement stateVoterInformationElements = result.get( "objects" );
             statesInfo = gson.fromJson( stateVoterInformationElements, new TypeToken<List<StateVoterInformation>>() {}.getType() );
         }
         return statesInfo != null && statesInfo.size() > 0 ? statesInfo.get( 0 ) : null;
     }

    @Cacheable( value = CacheKeyConstants.GET_ALL_ELECTIONS )
    public List<ElectionView> getAllElections() {
        Meta meta = new Meta();
        Map<String, String> params = new HashMap<String, String>();
        List<ElectionView> elections = new ArrayList<ElectionView>();
        do {
           JsonObject oneCall = connector.callMethod( LocalElectionConnector.GET_ELECTIONS, params );
            if ( oneCall != null ) {
                meta = gson.fromJson( oneCall.get( "meta" ), Meta.class );
                JsonElement statesElement = oneCall.get( "objects" );
                elections.addAll( gson.<List<? extends ElectionView>>fromJson( statesElement, new TypeToken<List<LocalElectionDecorator>>() {}.getType() ) );

                params.clear();
                params.put( "offset", String.valueOf( meta.getOffset() + meta.getLimit() ) );
                params.put( "limit", String.valueOf( meta.getLimit() ) );
            }

        } while ( meta != null && meta.getOffset() + meta.getLimit() < meta.getTotalObjects() );
        removeArchivedElections( elections );
        Collections.sort( elections/*, new ElectionComparator()*/ );
        return elections;
    }

    private void removeArchivedElections( List<ElectionView> elections ) {
        for ( Iterator<ElectionView> iterator = elections.iterator(); iterator.hasNext(); ) {
            ElectionView election = iterator.next();
            if ( election instanceof LocalElectionDecorator ) {
                LocalElectionDecorator decorator = (LocalElectionDecorator) election;
                if ( !LocalElectionDecorator.VIEW_STATUS.equalsIgnoreCase( decorator.getElectionStatus() ) ) {
                    iterator.remove();
                }
            }
        }
    }

    public static Map getGroupedIdentificationRequirements(StateVoterInformation stateVoterInformation){
        final HashMap<String, Integer> categoryNameIds = new HashMap<String, Integer>();
        categoryNameIds.put("Voter Registration", 1); 
        categoryNameIds.put("Voting In-Person", 2);
        categoryNameIds.put("Voting Military", 3);
        categoryNameIds.put("Voting Overseas", 4);
        categoryNameIds.put("Vote-by-Mail / Absentee", 5);
        Map<String,List<IdentificationRequirementsList>> ret = new TreeMap<String,List<IdentificationRequirementsList>>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return categoryNameIds.get(s1).compareTo(categoryNameIds.get(s2));
            }
        });
        if(stateVoterInformation != null){
            String[] expectedKeys = new String[]{"Voter Registration","Voting In-Person","Voting Military","Voting Overseas","Vote-by-Mail / Absentee"};
            for ( String expectedKey : expectedKeys ) {
                ret.put( expectedKey, new ArrayList<IdentificationRequirementsList>() );
            }
            if(stateVoterInformation.getIdentificationRequirements() != null){
                for ( IdentificationRequirementsList list : stateVoterInformation.getIdentificationRequirements() ) {
                    String key = list.getCategory().getName();
                    if ( ret.get(key) != null) {
                        ret.get(key).add(list);
                    }
                }
            }
        }
        return ret;
    }

    public static Map getGroupedEligibilityRequirements(StateVoterInformation stateVoterInformation){
        final HashMap<String, Integer> typeNameIds = new HashMap<String, Integer>();
        typeNameIds.put("Domestic Voter", 1); 
        typeNameIds.put("Overseas Voter", 2);
        typeNameIds.put("Military Voter", 3);
        typeNameIds.put("Student Eligibility", 4);
        Map<String,List<EligibilityRequirementsList>> ret = new TreeMap<String,List<EligibilityRequirementsList>>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return typeNameIds.get(s1).compareTo(typeNameIds.get(s2));
            }
        });
        if(stateVoterInformation != null){
            String[] expectedKeys = new String[]{"Domestic Voter","Military Voter","Overseas Voter","Student Eligibility"};
            for ( String expectedKey : expectedKeys ) {
                ret.put( expectedKey, new ArrayList<EligibilityRequirementsList>() );
            }
            if(stateVoterInformation.getEligibilityRequirements() != null){
                for ( EligibilityRequirementsList list : stateVoterInformation.getEligibilityRequirements() ) {
                    String key = list.getVoterType().getName();
                    if ( ret.get(key) != null) {
                        ret.get(key).add(list);
                    }
                }
            }
        }
        return ret;
    }

    public static String getAmIRegisteredUrl( StateVoterInformation stateVoterInformation ) {
        if ( stateVoterInformation != null && stateVoterInformation.getValidLookupTools() != null ) {
            for ( LookupToolListItem item : stateVoterInformation.getValidLookupTools() ) {
                if ( item.getName().equals( "Am I Registered?" ) ) {
                    return item.getUrl();
                }
            }
        }
        return "none on record";
    }

}
