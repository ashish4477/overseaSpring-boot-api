package com.bearcode.ovf.model.mail;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 30, 2008
 * Time: 4:32:41 PM
 * @author Leonid Ginzburg
 */
public class MailTemplate implements Serializable {

    private static final long serialVersionUID = 8219687807595944649L;
    private long id;

    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String name ="";

    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String subject="";

    private String from="";

    private String replyTo="";

    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String bodyTemplate="";

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }
}
