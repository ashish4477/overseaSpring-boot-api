package com.bearcode.ovf.webservices.eod;

import com.bearcode.ovf.model.eod.Officer;
import com.bearcode.ovf.utils.CacheKeyConstants;
import com.bearcode.ovf.webservices.eod.model.EodOfficer;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import com.bearcode.ovf.webservices.eod.model.StateOfEod;
import com.bearcode.ovf.webservices.util.model.Meta;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by leonid on 12.04.17.
 */
@Component
public class EodApiService  {

    private final Logger logger = LoggerFactory.getLogger( EodApiService.class );

    private static final String UOCAVA_NAME = "Absentee Overseas Voter Election";

    private Gson gson;

    @Autowired
    private EodApiConnector apiConnector;

    public EodApiService() {
        gson = new GsonBuilder()
                .setFieldNamingPolicy( FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES )
                .create();
    }

    @Cacheable( value = CacheKeyConstants.GET_ALL_EOD_STATES )
    public List<StateOfEod> getAllStates() {

        Map<String, String> params = new HashMap<String, String>();
        params.put( "offset", "0" );
        params.put( "limit", "70" );  // get all states.

        JsonObject result = apiConnector.callMethod( EodApiConnector.GET_STATES, params );
        List<StateOfEod> states = null;
        if ( result != null ) {
            JsonElement statesElement = result.get( "objects" );
            states = gson.fromJson( statesElement, new TypeToken<List<StateOfEod>>() {}.getType() );
        }
        return states != null ? states : Collections.<StateOfEod>emptyList();
    }

    @SuppressWarnings( "unchecked" )
    @Cacheable( value = CacheKeyConstants.GET_REGIONS_OF_STATE, key = "'region_of_state:'+#stateShortName", unless = "#result==null" )
    public List<EodRegion> getRegionsOfState( String stateShortName ) {
        List<StateOfEod> states = getAllStates();
        String stateId = "";
        for ( StateOfEod stateOfElection : states ) {
            if ( stateOfElection.getShortName().equalsIgnoreCase( stateShortName )) {
                stateId = stateOfElection.getId();
                break;
            }
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put( "state", stateId );
        Meta meta = new Meta();

        List<EodRegion> elections = new ArrayList<EodRegion>();
        do {
            JsonObject oneCall = apiConnector.callMethod( EodApiConnector.GET_REGIONS, params );
            if ( oneCall != null ) {
                meta = gson.fromJson( oneCall.get( "meta" ), Meta.class );
                JsonElement statesElement = oneCall.get( "objects" );
                elections.addAll( (Collection<? extends EodRegion>) gson.fromJson( statesElement, new TypeToken<List<EodRegion>>() {}.getType() ) );

                //params.clear();
                params.put( "offset", String.valueOf( meta.getOffset() + meta.getLimit() ) );
                params.put( "limit", String.valueOf( meta.getTotalObjects() ) );  // get everything in two calls
            }
            else {
                break;
            }

        } while ( meta != null && meta.getOffset() + meta.getLimit() < meta.getTotalObjects() );

        return elections;

    }

    @SuppressWarnings( "unchecked" )
    @Cacheable( value = CacheKeyConstants.GET_REGIONS_OF_STATE_SELECT2, key = "'region2:'+#stateShortName+':page:'+#page",
            condition = "#term==null" )
    public Map<String, Object> getRegionsOfStateForSelect2( String stateShortName, String term, Integer page ) {
        final int pageSize = 50;
        Map<String, String> params = new HashMap<String, String>();
        params.put( "state_abbr", stateShortName );
        params.put( "offset", String.valueOf( page * pageSize ) );
        params.put( "limit", String.valueOf( pageSize ) );
        if ( term != null && term.trim().length() > 0 ) {
            params.put( "region_name__icontains", term.trim() );
        }
        Meta meta = null;

        Map<String, Object> results = new HashMap<String, Object>();

        List<EodRegion> elections = new ArrayList<EodRegion>();
        JsonObject oneCall = apiConnector.callMethod( EodApiConnector.GET_REGIONS, params );
        if ( oneCall != null ) {
            meta = gson.fromJson( oneCall.get( "meta" ), Meta.class );
            JsonElement statesElement = oneCall.get( "objects" );
            elections.addAll( (Collection<? extends EodRegion>) gson.fromJson( statesElement, new TypeToken<List<EodRegion>>() {}.getType() ) );

            //params.clear();
            params.put( "offset", String.valueOf( meta.getOffset() + meta.getLimit() ) );
            params.put( "limit", String.valueOf( meta.getTotalObjects() ) );  // get everything in two calls
        }
        results.put( "totals", meta != null ? meta.getTotalObjects() : 0 );
        results.put( "results", elections );
        return results;

    }

    /**
     * Load local office info from EOD API service
     * @param regionId office ID
     * @return Local Office
     */
    public LocalOffice getLocalOffice( String regionId ) {
        return getLocalOffice( regionId, true );
    }

    /**
     * Load local office and region info from EOD API service
     * @param regionId office (region) ID
     * @param loadRegion load region flag
     * @return Local office
     */
    @Cacheable( value = CacheKeyConstants.GET_LOCAL_OFFICE, key = "'office:'+#regionId", unless = "#result==null" )
    public LocalOffice getLocalOffice( String regionId, boolean loadRegion ) {
        LocalOffice localOffice = null;
        if (  regionId != null && !regionId.isEmpty() && regionId.matches( "[1-9]\\d*" ) ) {
            Map<String, String> params = new HashMap<String, String>();
            params.put( "region", regionId );
            JsonObject result = apiConnector.callMethod( EodApiConnector.GET_OFFICES, params );
            if ( result != null ) {
                JsonArray jsonObjects = result.getAsJsonArray( "objects" );
                if ( jsonObjects != null && jsonObjects.size() > 0 ) {
                    JsonObject jsonOffice = jsonObjects.get( 0 ).getAsJsonObject();
                    jsonOffice.remove( "officers" );
                    try {
                        localOffice = gson.fromJson(jsonOffice, LocalOffice.class);
                    } catch (JsonSyntaxException e) {
                        logger.debug(e.getMessage());
                    }

                    if ( localOffice != null ) {
                        if ( localOffice.getOfficers().isEmpty() ) { // no officers were loaded, make separate call
                            params.clear();
                            params.put( "office", String.valueOf( localOffice.getId() ) );
                            List<Officer> officers = new ArrayList<>();
                            JsonObject officersObject = apiConnector.callMethod( EodApiConnector.GET_OFFICIALS, params );
                            if ( officersObject != null ) {
                                officers = gson.fromJson( officersObject.get( "objects" ), new TypeToken<List<EodOfficer>>(){}.getType() );
                            }
                            localOffice.setOfficers( officers );
                        }

                        if ( loadRegion ) {
                            EodRegion region = null;
                            params.clear();
                            JsonObject regionResult = apiConnector.callMethod( EodApiConnector.GET_REGIONS, regionId, params );
                            if ( regionResult != null ) {
                                region = gson.fromJson( regionResult.toString(), EodRegion.class );
                                localOffice.setEodRegion( region );
                            }
                        }
                        localOffice.assignApiAddresses();
                    }
                }
            }
        }

        return localOffice;
    }

    @Cacheable( value = CacheKeyConstants.GET_EOD_REGION, key = "'region:'+#eodRegionId", unless = "#result==null" )
    public EodRegion getRegion( String eodRegionId ) {
        EodRegion region = null;
        if ( eodRegionId != null && !eodRegionId.isEmpty() && eodRegionId.matches( "[1-9]\\d*" ) ) {
            Map<String, String> params = new HashMap<String, String>();
            JsonObject regionResult = apiConnector.callMethod( EodApiConnector.GET_REGIONS, eodRegionId, params );
            if ( regionResult != null ) {
                region = gson.fromJson( regionResult.toString(), EodRegion.class );
            }
        }
        return region;
    }

    @SuppressWarnings( "unchecked" )
    @Cacheable( value = CacheKeyConstants.GET_EOD_REGION_BY_NAME, key = "'state:'+#stateShortName+':name:'+#regionName", unless = "#result==null" )
    public EodRegion findRegionByName( String stateShortName, String regionName ) {
        Map<String, String> params = new HashMap<String, String>();
        params.put( "state_abbr", stateShortName );
        params.put( "region_name", regionName );
        Meta meta = new Meta();

        List<EodRegion> regions = new ArrayList<EodRegion>();
        JsonObject oneCall = apiConnector.callMethod( EodApiConnector.GET_REGIONS, params );
        if ( oneCall != null ) {
            meta = gson.fromJson( oneCall.get( "meta" ), Meta.class );
            JsonElement statesElement = oneCall.get( "objects" );
            regions.addAll( (Collection<? extends EodRegion>) gson.fromJson( statesElement, new TypeToken<List<EodRegion>>() {
            }.getType() ) );

            //params.clear();
            params.put( "offset", String.valueOf( meta.getOffset() + meta.getLimit() ) );
            params.put( "limit", String.valueOf( meta.getLimit() ) );
        }

        return regions.size() == 1 ? regions.get( 0 ) : null;

    }

    @SuppressWarnings( "unchecked" )
    @Cacheable( value = CacheKeyConstants.GET_UOCAVA_REGION, key = "'uocavaState:'+#stateShortName", unless = "#result==null" )
    public EodRegion findUocavaRegion( String stateShortName ) {
        Map<String, String> params = new HashMap<String, String>();
        params.put( "state_abbr", stateShortName );
        params.put( "region_name__istartswith", UOCAVA_NAME );
        Meta meta = new Meta();

        List<EodRegion> regions = new ArrayList<EodRegion>();
        JsonObject oneCall = apiConnector.callMethod( EodApiConnector.GET_REGIONS, params );
        if ( oneCall != null ) {
            meta = gson.fromJson( oneCall.get( "meta" ), Meta.class );
            JsonElement statesElement = oneCall.get( "objects" );
            regions.addAll( (Collection<? extends EodRegion>) gson.fromJson( statesElement, new TypeToken<List<EodRegion>>() {
            }.getType() ) );

            //params.clear();
            params.put( "offset", String.valueOf( meta.getOffset() + meta.getLimit() ) );
            params.put( "limit", String.valueOf( meta.getLimit() ) );
        }

        return regions.size() == 1 ? regions.get( 0 ) : null;

    }


}
