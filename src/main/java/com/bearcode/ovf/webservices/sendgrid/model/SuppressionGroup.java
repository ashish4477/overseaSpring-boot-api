package com.bearcode.ovf.webservices.sendgrid.model;

/**
 * @author leonid.
 */

/*
*       "description": "Optional description.",
      "id": 1,
      "is_default": true,
      "name": "Weekly News",
      "suppressed": true
*/
public class SuppressionGroup {
    private long id;
    private String name;
    private boolean suppressed;
    private String description;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public boolean isSuppressed() {
        return suppressed;
    }

    public void setSuppressed( boolean suppressed ) {
        this.suppressed = suppressed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }
}
