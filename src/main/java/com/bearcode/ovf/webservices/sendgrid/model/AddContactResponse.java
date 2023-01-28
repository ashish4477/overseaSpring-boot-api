package com.bearcode.ovf.webservices.sendgrid.model;

import java.util.List;

/**
 * @author leonid.
 */
/*
* {
* "new_count":4,
* "updated_count":0,
* "error_count":1,
* "error_indices":[0],
* "unmodified_indices":[],
* "persisted_recipients":["bGVvbkBsZW8ucnU=","Ym91bmNlK2pkb2U9cnUucnVAc2ltdWxhdG9yLmFtYXpvbnNlcy5jb20=","Z2luZW9AcnUucnU=","bGdpbnpidXJnQGJlYXItY29kZS5jb20="],
* "errors":[{"message":"Email duplicated in request.","error_indices":[0]}]}
* */
public class AddContactResponse {
    private int newCount;
    private int updatedCount;
    private int errorCount;
    private List<Integer> errorIndices;
    private List<String> persistedRecipients;
    private List<ErrorMessage> errors;

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount( int newCount ) {
        this.newCount = newCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount( int updatedCount ) {
        this.updatedCount = updatedCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount( int errorCount ) {
        this.errorCount = errorCount;
    }

    public List<Integer> getErrorIndices() {
        return errorIndices;
    }

    public void setErrorIndices( List<Integer> errorIndices ) {
        this.errorIndices = errorIndices;
    }

    public List<String> getPersistedRecipients() {
        return persistedRecipients;
    }

    public void setPersistedRecipients( List<String> persistedRecipients ) {
        this.persistedRecipients = persistedRecipients;
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    public void setErrors( List<ErrorMessage> errors ) {
        this.errors = errors;
    }

    public String findErrorMessages( int index ) {
        StringBuilder builder = new StringBuilder();

        for ( ErrorMessage message : errors ) {
            if ( message.getErrorIndices().contains( index ) ) {
                builder.append( message.getMessage() )
                        .append( "; " );
            }
        }
        return builder.toString();
    }
}
