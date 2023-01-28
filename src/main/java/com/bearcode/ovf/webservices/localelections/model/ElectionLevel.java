package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by leonid on 26.05.16.
 */
public class ElectionLevel implements Serializable {
    private static final long serialVersionUID = 2994474153940011441L;
    private long id = 0;
    private String name = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
