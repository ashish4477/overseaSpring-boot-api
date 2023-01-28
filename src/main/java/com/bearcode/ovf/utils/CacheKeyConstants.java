package com.bearcode.ovf.utils;

/**
 * @author leonid.
 */
public interface CacheKeyConstants {
    // local election API
    String GET_ALL_STATES = "getAllStates";
    String GET_ALL_STATE_VOTER_INFORMATION = "getAllStateVoterInformation";
    String FIND_STATE_VOTER_INFORMATION = "findStateVoterInformation";
    String FIND_ELECTIONS_OF_STATE = "findElectionsOfState";
    String GET_ALL_ELECTIONS = "getAllElections";

    String GET_ALL_EOD_STATES = "getEodAllStates";
    String GET_EOD_REGION_BY_NAME = "getEodRegionByName";
    String GET_EOD_REGION = "getEodRegion";
    String GET_REGIONS_OF_STATE = "getRegionsOfState";
    String GET_REGIONS_OF_STATE_SELECT2 = "getRegionsOfStateSelect2";
    String GET_LOCAL_OFFICE = "getLocalOffice";
    String GET_UOCAVA_REGION = "getUocavaRegion";
}
