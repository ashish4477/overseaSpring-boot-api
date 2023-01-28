package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by leonid on 27.05.16.
 */
public class ElectionDateType implements Serializable {
    public static final String TO_BE_ANNOUNCED = "to be announced";
    public static final String NOT_REQUIRED = "not required";
    public static final String NOT_ALLOWED = "not allowed";
    public static final String AUTOMATIC = "automatic";

    private static final long serialVersionUID = -7325027802350363622L;
    private long id = 0;
    private boolean defaultType = false;
    private String name = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public boolean isDefault() {
        return defaultType;
    }

    public void setDefault( boolean defaultType ) {
        this.defaultType = defaultType;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
