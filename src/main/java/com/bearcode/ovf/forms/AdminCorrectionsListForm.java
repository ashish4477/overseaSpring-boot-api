package com.bearcode.ovf.forms;

import com.bearcode.ovf.model.eod.CorrectionsLeo;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 10, 2007
 * Time: 7:04:01 PM
 */
public class AdminCorrectionsListForm extends CommonFormObject {
    private int status = CorrectionsLeo.STATUS_SENT;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
