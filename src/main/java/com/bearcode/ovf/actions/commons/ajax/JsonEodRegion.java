package com.bearcode.ovf.actions.commons.ajax;

import java.io.Serializable;

/**
 * @author leonid.
 */
public class JsonEodRegion implements Serializable {

    private static final long serialVersionUID = -4217181715763597054L;

    private String id;

    private String text;

    private boolean selected;

    public JsonEodRegion( String id, String text, boolean selected ) {
        this.id = id;
        this.text = text;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected( boolean selected ) {
        this.selected = selected;
    }
}
