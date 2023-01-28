package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by dhughes 08/31/2016.
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 820699793908038456L;
    private long id = 0;
    private String kind;
    private String name;
    private String nameFormat;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getKind(){
        return kind;
    }

    public void setKind(String kind){
        this.kind = kind;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getNameFormat(){
        return nameFormat;
    }

    public void setNameFormat(String nameFormat){
        this.nameFormat = nameFormat;
    }
}
