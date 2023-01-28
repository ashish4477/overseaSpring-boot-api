package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.mail.MailingList;

/**
 * Date: 04.10.12
 * Time: 19:05
 *
 * @author Leonid Ginzburg
 */
public class MailingListFacade extends AbleToMigrate {
    private long id;
    private String name;
    private String apiKey;
    private String campaignId;
    private String from = "";
    private String replyTo = "";
    private String signature = "";

    private long fieldTypeId;
    private long fieldTypeMigrationId;

    public MailingListFacade() {
    }

    public MailingListFacade( MailingList mailingList ) {
        id = mailingList.getId();
        name = mailingList.getName();
        apiKey = mailingList.getApiKey();
        campaignId = mailingList.getCampaignId();
        fieldTypeId = mailingList.getFieldType() != null ? mailingList.getFieldType().getId() : 0L;
        from = mailingList.getFrom();
        replyTo = mailingList.getReplyTo();
        signature = mailingList.getSignature();
    }

    public MailingList createMailingList() {
        MailingList mailingList = new MailingList();
        exportTo( mailingList );
        return mailingList;
    }

    public void exportTo( MailingList mailingList ) {
        mailingList.setName( name );
        mailingList.setApiKey( apiKey );
        mailingList.setCampaignId( campaignId );
        mailingList.setFrom( from );
        mailingList.setReplyTo( replyTo );
        mailingList.setSignature( signature );
    }

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey( String apiKey ) {
        this.apiKey = apiKey;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId( String campaignId ) {
        this.campaignId = campaignId;
    }

    public long getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId( long fieldTypeId ) {
        this.fieldTypeId = fieldTypeId;
    }

    public long getFieldTypeMigrationId() {
        return fieldTypeMigrationId;
    }

    public void setFieldTypeMigrationId( long fieldTypeMigrationId ) {
        this.fieldTypeMigrationId = fieldTypeMigrationId;
    }

    @Override
    public String getBaseClassName() {
        return MailingList.class.getSimpleName();
    }
}
