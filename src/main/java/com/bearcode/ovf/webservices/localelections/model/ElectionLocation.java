package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by leonid on 26.05.16.
 */
public class ElectionLocation implements Serializable {
    private static final long serialVersionUID = 145079593935364635L;
    private String id = "";
    private String stateId = "";
    private String type = "";
    private String name = "";

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId( String stateId ) {
        this.stateId = stateId;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
