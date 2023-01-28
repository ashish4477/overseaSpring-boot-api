package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dhughes 08/31/2016.
 */
public class EligibilityRequirementsList implements Serializable {
    private static final long serialVersionUID = 8206997939092475610L;
    private long id = 0;
    private long position = 0;
    private VoterType voterType;
    private String header;
    private String headerType;
    private String footer;
    private String footerType;
    private List<RequirementsListItem> items;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }


    public long getPosition(){
        return position;
    }

    public void setPosition(long position){
        this.position = position;
    }

    public VoterType getVoterType(){
        return voterType;
    }

    public void setVoterType(VoterType voterType){
        this.voterType = voterType;
    }

    public String getHeader(){
        return header;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public String getHeaderType(){
        return headerType;
    }

    public void setHeaderType(String headerType){
        this.headerType = headerType;
    }

    public String getFooter(){
        return footer;
    }

    public void setFooter(String footer){
        this.footer = footer;
    }

    public String getFooterType(){
        return footerType;
    }

    public void setFooterType(String footerType){
        this.footerType = footerType;
    }

    public List<RequirementsListItem> getItems(){
        return items;
    }

    public void setItems(List<RequirementsListItem> items){
        this.items = items;
    }
}
