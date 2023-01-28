package com.bearcode.ovf.model.email;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * Raw Email
 * 
 * @author pavel
 *
 */
public class RawEmail {

    public static enum Priority {
        LOWEST(10),
        LOW(20),
        NORMAL(30),
        HIGH(40),
        HIGHEST(50);

        private final int priority;

        Priority(final int priority) {
            this.priority = priority;
        }

        public int asInt() {
            return priority;
        }
    }


    private long id;

    private Date created;
    private Date updated;
    private Date retryTime;

    private String from;
    private String to;
    private String replyTo;
    private String subject;
    private String templatePath;
    private String body;

    private int attempt = 0;
    private int priority = Priority.NORMAL.asInt();
    private RawEmailLogStatus status = RawEmailLogStatus.INITIAL;
    private String error;

    private Collection<RawEmailLog> logs = new HashSet<RawEmailLog>();

    private boolean bcc;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getRetryTime() { return retryTime; }
    public void setRetryTime(Date retryTime) { this.retryTime = retryTime; }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public String getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplatePath() {
        return templatePath;
    }
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public int getAttempt() {
        return attempt;
    }
    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setPriorityLevel( Priority level ) {
        this.priority = level.asInt();
    }
    public RawEmailLogStatus getStatus() {
        return status;
    }
    public void setStatus(RawEmailLogStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    public Collection<RawEmailLog> getLogs() {
        return logs;
    }
    public void setLogs(Collection<RawEmailLog> logs) {
        this.logs = logs;
    }
    public boolean isBcc() {
        return bcc;
    }
    public void setBcc(boolean bcc) {
        this.bcc = bcc;
    }
}
