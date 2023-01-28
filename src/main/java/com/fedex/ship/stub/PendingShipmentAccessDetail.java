/**
 * PendingShipmentAccessDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fedex.ship.stub;


/**
 * This information describes how and when a pending shipment may
 * be accessed for completion.
 */
public class PendingShipmentAccessDetail  implements java.io.Serializable {
    /* Only for pending shipment type of "EMAIL" */
    private java.lang.String emailLabelUrl;

    /* Only for pending shipment type of "EMAIL" */
    private java.lang.String userId;

    /* Only for pending shipment type of "EMAIL" */
    private java.lang.String password;

    /* This element is currently not supported and is for the future
     * use. */
    private java.util.Calendar expirationTimestamp;

    public PendingShipmentAccessDetail() {
    }

    public PendingShipmentAccessDetail(
           java.lang.String emailLabelUrl,
           java.lang.String userId,
           java.lang.String password,
           java.util.Calendar expirationTimestamp) {
           this.emailLabelUrl = emailLabelUrl;
           this.userId = userId;
           this.password = password;
           this.expirationTimestamp = expirationTimestamp;
    }


    /**
     * Gets the emailLabelUrl value for this PendingShipmentAccessDetail.
     * 
     * @return emailLabelUrl   * Only for pending shipment type of "EMAIL"
     */
    public java.lang.String getEmailLabelUrl() {
        return emailLabelUrl;
    }


    /**
     * Sets the emailLabelUrl value for this PendingShipmentAccessDetail.
     * 
     * @param emailLabelUrl   * Only for pending shipment type of "EMAIL"
     */
    public void setEmailLabelUrl(java.lang.String emailLabelUrl) {
        this.emailLabelUrl = emailLabelUrl;
    }


    /**
     * Gets the userId value for this PendingShipmentAccessDetail.
     * 
     * @return userId   * Only for pending shipment type of "EMAIL"
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this PendingShipmentAccessDetail.
     * 
     * @param userId   * Only for pending shipment type of "EMAIL"
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the password value for this PendingShipmentAccessDetail.
     * 
     * @return password   * Only for pending shipment type of "EMAIL"
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this PendingShipmentAccessDetail.
     * 
     * @param password   * Only for pending shipment type of "EMAIL"
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the expirationTimestamp value for this PendingShipmentAccessDetail.
     * 
     * @return expirationTimestamp   * This element is currently not supported and is for the future
     * use.
     */
    public java.util.Calendar getExpirationTimestamp() {
        return expirationTimestamp;
    }


    /**
     * Sets the expirationTimestamp value for this PendingShipmentAccessDetail.
     * 
     * @param expirationTimestamp   * This element is currently not supported and is for the future
     * use.
     */
    public void setExpirationTimestamp(java.util.Calendar expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PendingShipmentAccessDetail)) return false;
        PendingShipmentAccessDetail other = (PendingShipmentAccessDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.emailLabelUrl==null && other.getEmailLabelUrl()==null) || 
             (this.emailLabelUrl!=null &&
              this.emailLabelUrl.equals(other.getEmailLabelUrl()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.expirationTimestamp==null && other.getExpirationTimestamp()==null) || 
             (this.expirationTimestamp!=null &&
              this.expirationTimestamp.equals(other.getExpirationTimestamp())));
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
        if (getEmailLabelUrl() != null) {
            _hashCode += getEmailLabelUrl().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getExpirationTimestamp() != null) {
            _hashCode += getExpirationTimestamp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PendingShipmentAccessDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "PendingShipmentAccessDetail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emailLabelUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "EmailLabelUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "UserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationTimestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ExpirationTimestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
