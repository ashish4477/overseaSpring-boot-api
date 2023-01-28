package com.bearcode.ovf.webservices.eod.model;

import com.bearcode.ovf.model.common.LookupEntity;

/**
 * Created by leonid on 12.04.17.
 */
public class EodRegion extends LookupEntity {
    private static final long serialVersionUID = -8382788956028599692L;

    private String county;
    private String countyName;
    private String municipality;
    private String municipalityName;
    private String municipalityType;
    private String stateName;
    private String stateAbbr;
    private String regionName;

    public String getCounty() {
        return county;
    }

    public void setCounty( String county ) {
        this.county = county;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName( String countyName ) {
        this.countyName = countyName;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality( String municipality ) {
        this.municipality = municipality;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName( String municipalityName ) {
        this.municipalityName = municipalityName;
    }

    public String getMunicipalityType() {
        return municipalityType;
    }

    public void setMunicipalityType( String municipalityType ) {
        this.municipalityType = municipalityType;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName( String stateName ) {
        this.stateName = stateName;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public void setStateAbbr( String stateAbbr ) {
        this.stateAbbr = stateAbbr;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName( String regionName ) {
        this.regionName = regionName;
    }

    @Override
    public String getName() {
        return regionName;
    }

}
