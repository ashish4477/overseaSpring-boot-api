package com.bearcode.ovf.webservices.factcheck.model;

import java.io.Serializable;

/**
 * Date: 27.05.14
 * Time: 17:19
 *
 * @author Leonid Ginzburg
 */
public class ArticlesList implements Serializable {
    private static final long serialVersionUID = -3081475372860388011L;

    private FactCheckMeta meta;
    private Article[] objects;

    public FactCheckMeta getMeta() {
        return meta;
    }

    public void setMeta(FactCheckMeta meta) {
        this.meta = meta;
    }

    public Article[] getObjects() {
        return objects;
    }

    public void setObjects(Article[] objects) {
        this.objects = objects;
    }
}
