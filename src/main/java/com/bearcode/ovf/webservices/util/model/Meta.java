package com.bearcode.ovf.webservices.util.model;

/**
 * Created by leonid on 26.05.16.
 */
public class Meta {

    private int offset = 0;
    private int limit = 0;
    private int totalObjects = 0;
    private String next = "";
    private String previous = "";
    private int totalCount = 0;

    public int getOffset() {
        return offset;
    }

    public void setOffset( int offset ) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit( int limit ) {
        this.limit = limit;
    }

    public int getTotalObjects() {
        return totalCount==0? totalObjects : totalCount;
    }

    public void setTotalObjects( int totalObjects ) {
        this.totalObjects = totalObjects;
    }

    public String getNext() {
        return next;
    }

    public void setNext( String next ) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious( String previous ) {
        this.previous = previous;
    }
}
