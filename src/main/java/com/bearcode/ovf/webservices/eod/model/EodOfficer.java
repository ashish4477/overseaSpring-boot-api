package com.bearcode.ovf.webservices.eod.model;

import com.bearcode.ovf.model.eod.Officer;

/**
 * @author leonid.
 */
public class EodOfficer extends Officer {
    private static final long serialVersionUID = 6156505630351683447L;

    public static final String PRIMARY_TYPE = "primary";
    public static final String SECONDARY_TYPE = "secondary";
    public static final String ADDITIONAL_TYPE = "additional";

    private String officeType;

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType( String officeType ) {
        this.officeType = officeType;
    }

    public void setJobTitle( String jobTitle ) {
        setOfficeName( jobTitle );
    }

    public String getJobTitle() {
        return getOfficeName();
    }

    public boolean isPrimary() {
        return officeType.equalsIgnoreCase( PRIMARY_TYPE );
    }

    public boolean isSecondary() {
        return officeType.equalsIgnoreCase( SECONDARY_TYPE );
    }

    public boolean isAdditional() {
        return officeType.equalsIgnoreCase( ADDITIONAL_TYPE );
    }
}
