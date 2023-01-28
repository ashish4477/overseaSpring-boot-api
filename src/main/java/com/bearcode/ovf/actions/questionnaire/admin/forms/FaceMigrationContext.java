package com.bearcode.ovf.actions.questionnaire.admin.forms;

import com.bearcode.ovf.model.migration.FaceConfigFacade;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Date: 26.12.11
 * Time: 18:41
 *
 * @author Leonid Ginzburg
 */
public class FaceMigrationContext {
    private Collection<FaceConfigFacade> faceConfigFacades = new LinkedList<FaceConfigFacade>();

    @Transient
    private Collection<String> messages = new LinkedList<String>();

    public Collection<FaceConfigFacade> getFaceConfigFacades() {
        return faceConfigFacades;
    }

    public void setFaceConfigFacades( Collection<FaceConfigFacade> faceConfigFacades ) {
        this.faceConfigFacades = faceConfigFacades;
    }

    public Collection<String> getMessages() {
        return messages;
    }

    public void setMessages( Collection<String> messages ) {
        this.messages = messages;
    }
}
