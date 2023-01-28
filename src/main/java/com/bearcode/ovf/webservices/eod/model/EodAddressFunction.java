package com.bearcode.ovf.webservices.eod.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author leonid.
 */
public enum EodAddressFunction {
    DOM_VR("Domestic Voter Registration"),
    DOM_REQ("Domestic Absentee Ballot Request"),
    DOM_RET("Domestic Absentee Ballot Return"),
    OVS_REQ("Overseas Absentee Ballot Request"),
    OVS_RET("Overseas Absentee Ballot Return");

    private String description = "";

    EodAddressFunction( String description ) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Collection<String> descriptions() {
        List<String> descr = new ArrayList<String>();
        for ( EodAddressFunction function : values() ) {
            descr.add( function.description );
        }
        return descr;
    }
}
