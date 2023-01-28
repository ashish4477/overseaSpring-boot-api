package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dhughes 08/31/2016.
 */
public class VoterType implements Serializable {
    private static final long serialVersionUID = 8206997939738410993L;
    private long id = 0;
    private String kind;
    private String name;

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
}
