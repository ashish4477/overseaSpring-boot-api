package com.bearcode.ovf.model.mail;

import com.bearcode.ovf.model.common.FaceConfig;

import java.io.Serializable;

/**
 * @author leonid.
 */
public class FaceMailingList implements Serializable {

    private static final long serialVersionUID = 4418459510348416039L;

    private long id;

    private FaceConfig face;
    private MailingList mailingList;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public FaceConfig getFace() {
        return face;
    }

    public void setFace( FaceConfig face ) {
        this.face = face;
    }

    public MailingList getMailingList() {
        return mailingList;
    }

    public void setMailingList( MailingList mailingList ) {
        this.mailingList = mailingList;
    }
}
