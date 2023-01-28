package com.bearcode.ovf.model.common;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2007
 * Time: 4:39:12 PM
 * State class. Stores name and abbreviation.
 */
public class State extends LookupEntity {
    private static final long serialVersionUID = 991286755636315445L;
    private String abbr;
    private int fipsCode;    // special code according FIPS standard

    @BusinessKey
    public String getAbbr() {
        return abbr;
    }

    public void setAbbr( String abbr ) {
        this.abbr = abbr;
    }

    @BusinessKey
    public int getFipsCode() {
        return fipsCode;
    }

    public void setFipsCode( int fipsCode ) {
        this.fipsCode = fipsCode;
    }

    public boolean valueEquals( State other ) {
        if ( other == null ) {
            return false;
        } else if ( this == other ) {
            return true;
        }

        return getAbbr() == null ? other.getAbbr() == null : (other.getAbbr() != null) && getAbbr().equalsIgnoreCase( other.getAbbr() );
    }
}
