package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by leonid on 26.05.16.
 */
public class StateOfElection implements Serializable {
    private static final long serialVersionUID = 8206997939080302604L;
    private String id = "";
    private String name = "";
    private String shortName = "";
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
        return shortName;
    }

    public void setShortName( String shortName ) {
        this.shortName = shortName;
    }

    public String getAbbr() {
        return shortName;
    }

    public void setAbbr( String shortName ) {
        this.shortName = shortName;
    }
}
