package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;

/**
 * Created by dhughes 08/31/2016.
 */
public class RequirementsListItem implements Serializable {
    private static final long serialVersionUID = 8069979392758392004L;

    private RequirementsListItem.Item item;
    private long position = 0;

    public Item getItem(){
        return item;
    }

    public void setItem(Item item){
        this.item = item;
    }

    public long getPosition(){
        return position;
    }

    public void setPosition(long position){
        this.position = position;
    }

    public String getKind(){
        return item.getKind();
    }

    public String getName(){
        return item.getName();
    }

    private class Item implements Serializable {
        private static final long serialVersionUID = 8085579392758394532L;
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
