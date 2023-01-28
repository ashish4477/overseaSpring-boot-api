/**
 * ValidatedHazardousCommodityContent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fedex.ship.stub;


/**
 * Documents the kind and quantity of an individual hazardous commodity
 * in a package.
 */
public class ValidatedHazardousCommodityContent  implements java.io.Serializable {
    /* Identifies and describes an individual hazardous commodity. */
    private com.fedex.ship.stub.ValidatedHazardousCommodityDescription description;

    /* Specifies the amount of the commodity in alternate units. */
    private com.fedex.ship.stub.HazardousCommodityQuantityDetail quantity;

    /* Customer-provided specifications for handling individual commodities. */
    private com.fedex.ship.stub.HazardousCommodityOptionDetail options;

    public ValidatedHazardousCommodityContent() {
    }

    public ValidatedHazardousCommodityContent(
           com.fedex.ship.stub.ValidatedHazardousCommodityDescription description,
           com.fedex.ship.stub.HazardousCommodityQuantityDetail quantity,
           com.fedex.ship.stub.HazardousCommodityOptionDetail options) {
           this.description = description;
           this.quantity = quantity;
           this.options = options;
    }


    /**
     * Gets the description value for this ValidatedHazardousCommodityContent.
     * 
     * @return description   * Identifies and describes an individual hazardous commodity.
     */
    public com.fedex.ship.stub.ValidatedHazardousCommodityDescription getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ValidatedHazardousCommodityContent.
     * 
     * @param description   * Identifies and describes an individual hazardous commodity.
     */
    public void setDescription(com.fedex.ship.stub.ValidatedHazardousCommodityDescription description) {
        this.description = description;
    }


    /**
     * Gets the quantity value for this ValidatedHazardousCommodityContent.
     * 
     * @return quantity   * Specifies the amount of the commodity in alternate units.
     */
    public com.fedex.ship.stub.HazardousCommodityQuantityDetail getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity value for this ValidatedHazardousCommodityContent.
     * 
     * @param quantity   * Specifies the amount of the commodity in alternate units.
     */
    public void setQuantity(com.fedex.ship.stub.HazardousCommodityQuantityDetail quantity) {
        this.quantity = quantity;
    }


    /**
     * Gets the options value for this ValidatedHazardousCommodityContent.
     * 
     * @return options   * Customer-provided specifications for handling individual commodities.
     */
    public com.fedex.ship.stub.HazardousCommodityOptionDetail getOptions() {
        return options;
    }


    /**
     * Sets the options value for this ValidatedHazardousCommodityContent.
     * 
     * @param options   * Customer-provided specifications for handling individual commodities.
     */
    public void setOptions(com.fedex.ship.stub.HazardousCommodityOptionDetail options) {
        this.options = options;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ValidatedHazardousCommodityContent)) return false;
        ValidatedHazardousCommodityContent other = (ValidatedHazardousCommodityContent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.quantity==null && other.getQuantity()==null) || 
             (this.quantity!=null &&
              this.quantity.equals(other.getQuantity()))) &&
            ((this.options==null && other.getOptions()==null) || 
             (this.options!=null &&
              this.options.equals(other.getOptions())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getQuantity() != null) {
            _hashCode += getQuantity().hashCode();
        }
        if (getOptions() != null) {
            _hashCode += getOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ValidatedHazardousCommodityContent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ValidatedHazardousCommodityContent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ValidatedHazardousCommodityDescription"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "HazardousCommodityQuantityDetail"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("options");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Options"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "HazardousCommodityOptionDetail"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
