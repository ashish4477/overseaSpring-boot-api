package com.bearcode.ovf.tools.pendingregistration;

import java.io.Serializable;

/**
 * Date: 20.10.14
 * Time: 23:53
 *
 * @author Leonid Ginzburg
 */
public class DataPreparationStatus implements Serializable {

    private static final long serialVersionUID = 8003273716726484956L;

    public static int CREATED = 0;
    public static int IN_PROGRESS = 1;
    public static int COMPLETED = 2;
    public static int ERROR = 3;

    private String id;
    private int status = CREATED;
    private int percent = 0;
    private String message = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
