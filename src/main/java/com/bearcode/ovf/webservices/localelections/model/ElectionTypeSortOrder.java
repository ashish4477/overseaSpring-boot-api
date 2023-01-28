package com.bearcode.ovf.webservices.localelections.model;

/**
 * @author leonid.
 */
public enum ElectionTypeSortOrder {
    GENERAL ("General",1),
    PRIMARY ("Primary",2),
    RUNOFF ("Runoff",3),
    SPECIAL ("Special",4),
    OTHER ( "",5),                          //​"Caucus" "Recall"  "State Primary" "Congressional Primary"
    GENERAL_RUNOFF ( "General Runoff",6),
    PRIMARY_RUNOFF ( "Primary Runoff",7);

    private String name;
    private int order;

    ElectionTypeSortOrder( String name, int order ) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }
}
