package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.OverseasUser;
import com.google.gson.annotations.Expose;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 06.10.14
 * Time: 21:55
 *
 * @author Leonid Ginzburg
 */
public class PdfFormTrack implements Serializable {
    private static final long serialVersionUID = 7969824038540643684L;

    public static final int CREATED = 0;
    public static final int IN_PROCESS = 1;
    public static final int GENERATED = 2;
    public static final int ERROR = 3;
    public static final int TRACK_NOT_FOUND = 4;

    @Expose
    private long id;

    @Expose
    private Date created = new Date();

    private String formFileName = "";

    @Expose
    private int status = CREATED;

    private String hash = "";

    private OverseasUser user;

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

    public String getFormFileName() {
        return formFileName;
    }

    public void setFormFileName(String formFileName) {
        this.formFileName = formFileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public OverseasUser getUser() {
        return user;
    }

    public void setUser(OverseasUser user) {
        this.user = user;
    }
}
