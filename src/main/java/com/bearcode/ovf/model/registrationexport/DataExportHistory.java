package com.bearcode.ovf.model.registrationexport;

import com.bearcode.ovf.model.questionnaire.WizardResults;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by leonid on 22.09.16.
 */
public class DataExportHistory implements Serializable {

    private static final long serialVersionUID = 6155241460190084671L;

    private long id;

    private ExportHistoryStatus status = ExportHistoryStatus.NEW;

    private Date creationDate = new Date();

    private Date lastModification;

    private Date exportDate;

    private WizardResults wizardResults;

    private DataExportConfiguration exportConfig;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public ExportHistoryStatus getStatus() {
        return status;
    }

    public void setStatus( ExportHistoryStatus status ) {
        this.status = status;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification( Date lastModification ) {
        this.lastModification = lastModification;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate( Date exportDate ) {
        this.exportDate = exportDate;
    }

    public WizardResults getWizardResults() {
        return wizardResults;
    }

    public void setWizardResults( WizardResults wizardResults ) {
        this.wizardResults = wizardResults;
    }

    public DataExportConfiguration getExportConfig() {
        return exportConfig;
    }

    public void setExportConfig( DataExportConfiguration exportConfig ) {
        this.exportConfig = exportConfig;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate( Date creationDate ) {
        this.creationDate = creationDate;
    }
}
