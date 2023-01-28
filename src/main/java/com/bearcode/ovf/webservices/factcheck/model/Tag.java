package com.bearcode.ovf.webservices.factcheck.model;

import java.io.Serializable;

/**
 * Date: 27.05.14
 * Time: 16:40
 *
 * @author Leonid Ginzburg
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = -1123845480103730941L;

    private Long id;
    private String name;
    private String label;
    private String resourceUri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
