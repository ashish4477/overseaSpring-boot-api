package com.bearcode.ovf.model.mail;

import com.bearcode.ovf.model.questionnaire.FieldType;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Date: 04.07.12
 * Time: 22:33
 *
 * MailingList aka Mailing Campaign
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "mailingList")
public class MailingList implements Serializable {
    private static final long serialVersionUID = 3051609907975581941L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String name = "";

    @Column(name = "getresponseAPIKey")
    //@NotBlank(message = "{rava.field.is_empty}")
    private String apiKey = "";

    @Column(name = "getresponseCompainID")
    //@NotBlank(message = "{rava.field.is_empty}")
    private String campaignId = "";

    @ManyToOne
    @JoinColumn(name = "fieldTypeId")
    private FieldType fieldType;

    private String from;

    private String replyTo;

    private String signature;

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

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType( FieldType fieldType ) {
        this.fieldType = fieldType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom( String from ) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo( String replyTo ) {
        this.replyTo = replyTo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature( String signature ) {
        this.signature = signature;
    }
}
