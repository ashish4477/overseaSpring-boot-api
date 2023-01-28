package com.bearcode.ovf.webservices.sendgrid.model;

import java.util.List;

/**
 * @author leonid.
 */
public class GetSuppressionGroupResponse {

    private List<SuppressionGroup> suppressions;

    public List<SuppressionGroup> getSuppressions() {
        return suppressions;
    }

    public void setSuppressions( List<SuppressionGroup> suppressions ) {
        this.suppressions = suppressions;
    }
}
