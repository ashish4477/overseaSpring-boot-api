package com.bearcode.ovf.webservices.sendgrid.model;

import java.util.List;

/**
 * @author leonid.
 */
public class ErrorMessage {
    private String message;
    private List<Integer> errorIndices;

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public List<Integer> getErrorIndices() {
        return errorIndices;
    }

    public void setErrorIndices( List<Integer> errorIndices ) {
        this.errorIndices = errorIndices;
    }
}
