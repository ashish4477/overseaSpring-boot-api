package com.bearcode.ovf.actions.questionnaire.admin.forms;

import com.bearcode.ovf.model.migration.FieldTypeFacade;
import com.bearcode.ovf.model.migration.FillingFacade;
import com.bearcode.ovf.model.migration.MailingListFacade;
import com.bearcode.ovf.model.migration.PageFacade;
import com.bearcode.ovf.model.questionnaire.PageType;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Date: 20.12.11
 * Time: 16:27
 *
 * @author Leonid Ginzburg
 */
public class MigrationContext {
    private String programVersion = "";
    private PageType pageType;
    private int version;

    private Collection<FieldTypeFacade> fieldTypes;
    private Collection<PageFacade> pages;
    private Collection<FillingFacade> fillings;
    private Collection<MailingListFacade> mailingLists;

    @Transient
    private Collection<String> messages;

    public MigrationContext() {
        fieldTypes = new LinkedList<FieldTypeFacade>();
        pages = new LinkedList<PageFacade>();
        fillings = new LinkedList<FillingFacade>();
        mailingLists = new LinkedList<MailingListFacade>();

        messages = new LinkedList<String>();
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType( PageType pageType ) {
        this.pageType = pageType;
    }

    public Collection<PageFacade> getPages() {
        return pages;
    }

    public void setPages( Collection<PageFacade> pages ) {
        this.pages = pages;
    }

    public Collection<String> getMessages() {
        return messages;
    }

    public void setMessages( Collection<String> messages ) {
        this.messages = messages;
    }

    public Collection<FillingFacade> getFillings() {
        return fillings;
    }

    public void setFillings( Collection<FillingFacade> fillings ) {
        this.fillings = fillings;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion( int version ) {
        this.version = version;
    }

    public Collection<FieldTypeFacade> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes( Collection<FieldTypeFacade> fieldTypes ) {
        this.fieldTypes = fieldTypes;
    }

    public Collection<MailingListFacade> getMailingLists() {
        return mailingLists;
    }

    public void setMailingLists( Collection<MailingListFacade> mailingLists ) {
        this.mailingLists = mailingLists;
    }

    public String getProgramVersion() {
        return programVersion;
    }

    public void setProgramVersion( String programVersion ) {
        this.programVersion = programVersion;
    }
}
