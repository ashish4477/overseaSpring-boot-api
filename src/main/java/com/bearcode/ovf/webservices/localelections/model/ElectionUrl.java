package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by leonid on 27.05.16.
 */
public class ElectionUrl implements Serializable {
    private static final long serialVersionUID = -6112257415415841641L;
    private long id = 0;
    private String url = "";
    private String name = "";
    private ElectionUrlType urlType;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public ElectionUrlType getUrlType() {
        return urlType;
    }

    public void setUrlType( ElectionUrlType type ) {
        this.urlType = type;
    }
}
