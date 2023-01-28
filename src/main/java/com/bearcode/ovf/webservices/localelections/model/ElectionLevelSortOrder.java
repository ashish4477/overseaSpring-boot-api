package com.bearcode.ovf.webservices.localelections.model;

/**
 * @author leonid.
 */
public enum ElectionLevelSortOrder {
    //FEDERAL ("Federal","Federal",1),
    //STATE ("State","State",2),
    FEDERAL_STATE("Federal and State", "(Federal|State|Territory)", 1),
    OTHER ( "Local","Other",5);

    /**
     * Name for showing on the page
     */
    private String name;

    /**
     * pattern to match ElectionLevel.name
     */
    private String pattern;

    /**
     * sort order
     */
    private int order;

    ElectionLevelSortOrder( String name, String pattern, int order ) {
        this.name = name;
        this.order = order;
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public String getPattern() {
        return pattern;
    }
}
