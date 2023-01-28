package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by dhughes 08/31/2016.
 */
public class LookupToolListItem implements Serializable {
    private static final long serialVersionUID = 8069979398847297621L;

    private long id = 0;
    private LookupTool lookupTool;
    private String url;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public LookupTool getLookupTool(){
        return lookupTool;
    }

    public void setLookupTool(LookupTool lookupTool){
        this.lookupTool = lookupTool;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getKind(){
        return getLookupTool().getKind();
    }

    public String getName(){
        return getLookupTool().getName();
    }

    public class LookupTool implements Serializable {
        private static final long serialVersionUID = 8069979398847297621L;
        private long id = 0;
        private String kind;
        private String name;

        public long getId(){
            return id;
        }

        public void setId(long id){
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
}
