package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dhughes 08/31/2016.
 */
public class TransmissionMethodsListItem implements Serializable {
    private static final long serialVersionUID = 8206997939092475610L;
    private long id = 0;
    private DocumentType documentType;
    private DocumentTransmissionMethod documentTransmissionMethod;
    private boolean allowed;
    private String additionalInfo;
    private String additionalInfoType;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public DocumentType getDocumentType(){
        return documentType;
    }

    public void setDocumentType(DocumentType documentType){
        this.documentType = documentType;
    }

    public DocumentTransmissionMethod getDocumentTransmissionMethod(){
        return documentTransmissionMethod;
    }

    public void setDocumentTransmissionMethod(DocumentTransmissionMethod documentTransmissionMethod){
        this.documentTransmissionMethod = documentTransmissionMethod;
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void setAllowed(boolean allowed){
        this.allowed = allowed;
    }

    public String getAdditionalInfo(){
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo){
        this.additionalInfo = additionalInfo;
    }

    public String getAdditionalInfoType(){
        return additionalInfoType;
    }

    public void setAdditionalInfoType(String additionalInfoType){
        this.additionalInfoType = additionalInfoType;
    }
}
