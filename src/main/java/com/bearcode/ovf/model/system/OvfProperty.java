package com.bearcode.ovf.model.system;

import java.io.Serializable;

/**
 * @author leonid.
 */
public class OvfProperty implements Serializable, Comparable<OvfProperty> {

    private static final long serialVersionUID = -8884516351359199115L;

    private long id;
    private OvfPropertyNames propertyName;
    private String propertyValue;

    public OvfProperty() {
    }

    public OvfProperty( OvfPropertyNames propertyName, String propertyValue ) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public OvfPropertyNames getPropertyName() {
        return propertyName;
    }

    public void setPropertyName( OvfPropertyNames propertyName ) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue( String propertyValue ) {
        this.propertyValue = propertyValue;
    }

    @Override
    public int compareTo( OvfProperty o ) {
        int compare = this.getPropertyName().getGroupName().compareTo( o.getPropertyName().getGroupName());
        if ( compare != 0 ) return compare;
        compare = this.getPropertyName().getPropertyName().compareTo( o.getPropertyName().getPropertyName() );
        return compare != 0 ? compare : this.getPropertyValue().compareTo( o.getPropertyValue() );
    }
}
