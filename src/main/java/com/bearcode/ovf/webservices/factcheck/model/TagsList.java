package com.bearcode.ovf.webservices.factcheck.model;

import java.io.Serializable;

/**
 * Date: 27.05.14
 * Time: 17:18
 *
 * @author Leonid Ginzburg
 */
public class TagsList implements Serializable {
    private static final long serialVersionUID = 2072423466189793348L;

    private FactCheckMeta meta;
    private Tag[] objects;

    public FactCheckMeta getMeta() {
        return meta;
    }

    public void setMeta(FactCheckMeta meta) {
        this.meta = meta;
    }

    public Tag[] getObjects() {
        return objects;
    }

    public void setObjects(Tag[] objects) {
        this.objects = objects;
    }
}
