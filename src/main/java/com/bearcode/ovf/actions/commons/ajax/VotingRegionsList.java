package com.bearcode.ovf.actions.commons.ajax;

import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: dhughes
 * Date: Apr 21, 2011
 * Time: 11:09:59 PM
 */
@Controller
public class VotingRegionsList {

    @Autowired
    protected StateService stateService;

    @Autowired
    protected EodApiService eodApiService;

    @RequestMapping(value="/ajax/getRegionsHTMLSelect.htm")
    public String getRegionsHTMLSelect(
            @RequestParam(value = "stateId",required = false,defaultValue = "") String state,
            @RequestParam( value = "regionId", required = false, defaultValue = "0") Long regionId,
            ModelMap references ){

        String stateAbbr = null;
        Long stateId = null;
        if ( state.matches("^[A-Z]{2}$") ) {
            stateAbbr = state;
        }
        if ( state.matches("^\\d+$") ) {
            stateId = Long.parseLong(state);
        }

        Collection<EodRegion> regions = Collections.emptyList();
        if ( stateAbbr != null ) {
            regions = eodApiService.getRegionsOfState( stateAbbr );
        } else if ( stateId != null ) {
            State stateObj = stateService.findState( stateId );
            if ( stateObj != null ) {
                regions = eodApiService.getRegionsOfState( stateObj.getAbbr() );
            }
        }
        references.put( "regions", regions );

        if ( regionId != 0 ) {
            // make sure the voting region is in this state
            for ( EodRegion region: regions ) {
                if ( region.getId().equals( regionId ) ) {
                    references.put( "selectedRegion", region );
                    break;
                }
            }
        }
        return "ajax/GetRegionsNoLabel";
    }

/*
    @RequestMapping(value="/ajax/getJsonRegions.htm", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<JsonEodRegion> getRegionsHTMLSelect(
            @RequestParam(value = "stateId",required = false,defaultValue = "") String state,
            @RequestParam( value = "regionId", required = false, defaultValue = "0") Long regionId ) {

        String stateAbbr = null;
        Long stateId = null;
        if ( state.matches("^[A-Z]{2}$") ) {
            stateAbbr = state;
        }
        if ( state.matches("^\\d+$") ) {
            stateId = Long.parseLong(state);
        }
        List<JsonEodRegion> jsonEodRegions =  new ArrayList<JsonEodRegion>();

        Collection<EodRegion> regions = Collections.emptyList();
        if ( stateAbbr != null ) {
            regions = eodApiService.getRegionsOfState( stateAbbr );
        } else if ( stateId != null ) {
            State stateObj = stateService.findState( stateId );
            if ( stateObj != null ) {
                regions = eodApiService.getRegionsOfState( stateObj.getAbbr() );
            }
        }

        // make sure the voting region is in this state ???
        for ( EodRegion region: regions ) {
            jsonEodRegions.add( new JsonEodRegion( region.getId().toString(), region.getName(), region.getId().equals( regionId ) ) );
        }
        return jsonEodRegions;
    }
*/

    @RequestMapping(value="/ajax/getJsonRegions.htm")
    public  ResponseEntity<String> getJsonRegions(
            @RequestParam(value = "stateId",required = false,defaultValue = "") String state,
            @RequestParam( value = "regionId", required = false, defaultValue = "0") Long regionId ) {

        String stateAbbr = null;
        Long stateId = null;
        if ( state.matches("^[A-Z]{2}$") ) {
            stateAbbr = state;
        }
        if ( state.matches("^\\d+$") ) {
            stateId = Long.parseLong(state);
        }
        List<JsonEodRegion> jsonEodRegions =  new ArrayList<JsonEodRegion>();

        Collection<EodRegion> regions = Collections.emptyList();
        if ( stateAbbr != null ) {
            regions = eodApiService.getRegionsOfState( stateAbbr );
        } else if ( stateId != null ) {
            State stateObj = stateService.findState( stateId );
            if ( stateObj != null ) {
                regions = eodApiService.getRegionsOfState( stateObj.getAbbr() );
            }
        }

        // make sure the voting region is in this state ???
        for ( EodRegion region: regions ) {
            jsonEodRegions.add( new JsonEodRegion( region.getId().toString(), region.getName(), region.getId().equals( regionId ) ) );
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        Gson gson = new GsonBuilder().create();
        String result = gson.toJson( jsonEodRegions );
        return new ResponseEntity<String>( result, headers, HttpStatus.OK );
    }

    /* To work with select2 */
    @RequestMapping(value="/ajax/getJsonRegions2.htm")
    public  ResponseEntity<String> getJsonRegions2(
            @RequestParam(value = "stateId",required = false,defaultValue = "") String state,
            @RequestParam( value = "regionId", required = false, defaultValue = "0") Long regionId,
            @RequestParam( value = "q", required = false) String terms,
            @RequestParam( value = "page", required = false, defaultValue = "0") Integer page) {

        String stateAbbr = null;
        Long stateId = null;
        if ( state.matches("^[A-Z]{2}$") ) {
            stateAbbr = state;
        }
        if ( state.matches("^\\d+$") ) {
            stateId = Long.parseLong(state);
        }
        List<JsonEodRegion> jsonEodRegions =  new ArrayList<JsonEodRegion>();
        Map<String, Object> results = null;

        page = page > 0 ? page - 1 : 0;

        if ( stateAbbr != null ) {
            results = eodApiService.getRegionsOfStateForSelect2( stateAbbr, terms, page );
        } else if ( stateId != null ) {
            State stateObj = stateService.findState( stateId );
            if ( stateObj != null ) {
                results = eodApiService.getRegionsOfStateForSelect2( stateObj.getAbbr(), terms, page );
            }
        }
        Collection<EodRegion> regions = (results != null && results.get( "results" ) != null) ? (Collection<EodRegion>)results.get( "results" ) : Collections.<EodRegion>emptyList();
        // make sure the voting region is in this state ???
        for ( EodRegion region: regions ) {
            jsonEodRegions.add( new JsonEodRegion( region.getId().toString(), region.getName(), region.getId().equals( regionId ) ) );
        }

        Map<String, Object> finalResults = new HashMap<String, Object>();
        finalResults.put( "total_count", results != null && results.get( "totals" ) != null ? results.get( "totals" ): 0 );
        finalResults.put( "items", jsonEodRegions );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        Gson gson = new GsonBuilder().create();
        String result = gson.toJson( finalResults );
        return new ResponseEntity<String>( result, headers, HttpStatus.OK );
    }


}
