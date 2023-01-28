package com.bearcode.ovf.webservices.eod.model;

import java.io.Serializable;

/**
 * Created by leonid on 13.04.17.
 */
public class StateOfEod implements Serializable {
    private static final long serialVersionUID = 4432097207664200596L;
    private String id = "";
    private String name = "";
    private String abbr = "";

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getShortName() {
        return abbr;
    }

    public void setShortName( String shortName ) {
        this.abbr = shortName;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr( String shortName ) {
        this.abbr = shortName;
    }
}


