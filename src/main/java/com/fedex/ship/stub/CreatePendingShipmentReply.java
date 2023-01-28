/**
 * CreatePendingShipmentReply.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fedex.ship.stub;


/**
 * Reply to the Close Request transaction. The Close Reply bring back
 * the ASCII data buffer which will be used to print the Close Manifest.
 * The Manifest is essential at the time of pickup.
 */
public class CreatePendingShipmentReply  implements java.io.Serializable {
    /* Identifies the highest severity encountered when executing
     * the request; in order from high to low: FAILURE, ERROR, WARNING, NOTE,
     * SUCCESS. */
    private com.fedex.ship.stub.NotificationSeverityType highestSeverity;

    /* The descriptive data detailing the status of a sumbitted transaction. */
    private com.fedex.ship.stub.Notification[] notifications;

    /* Descriptive data that governs data payload language/translations.
     * The TransactionDetail from the request is echoed back to the caller
     * in the corresponding reply. */
    private com.fedex.ship.stub.TransactionDetail transactionDetail;

    /* Identifies the version/level of a service operation expected
     * by a caller (in each request) and performed by the callee (in each
     * reply). */
    private com.fedex.ship.stub.VersionId version;

    /* The reply payload. All of the returned information about this
     * shipment/package. */
    private com.fedex.ship.stub.CompletedShipmentDetail completedShipmentDetail;

    public CreatePendingShipmentReply() {
    }

    public CreatePendingShipmentReply(
           com.fedex.ship.stub.NotificationSeverityType highestSeverity,
           com.fedex.ship.stub.Notification[] notifications,
           com.fedex.ship.stub.TransactionDetail transactionDetail,
           com.fedex.ship.stub.VersionId version,
           com.fedex.ship.stub.CompletedShipmentDetail completedShipmentDetail) {
           this.highestSeverity = highestSeverity;
           this.notifications = notifications;
           this.transactionDetail = transactionDetail;
           this.version = version;
           this.completedShipmentDetail = completedShipmentDetail;
    }


    /**
     * Gets the highestSeverity value for this CreatePendingShipmentReply.
     * 
     * @return highestSeverity   * Identifies the highest severity encountered when executing
     * the request; in order from high to low: FAILURE, ERROR, WARNING, NOTE,
     * SUCCESS.
     */
    public com.fedex.ship.stub.NotificationSeverityType getHighestSeverity() {
        return highestSeverity;
    }


    /**
     * Sets the highestSeverity value for this CreatePendingShipmentReply.
     * 
     * @param highestSeverity   * Identifies the highest severity encountered when executing
     * the request; in order from high to low: FAILURE, ERROR, WARNING, NOTE,
     * SUCCESS.
     */
    public void setHighestSeverity(com.fedex.ship.stub.NotificationSeverityType highestSeverity) {
        this.highestSeverity = highestSeverity;
    }


    /**
     * Gets the notifications value for this CreatePendingShipmentReply.
     * 
     * @return notifications   * The descriptive data detailing the status of a sumbitted transaction.
     */
    public com.fedex.ship.stub.Notification[] getNotifications() {
        return notifications;
    }


    /**
     * Sets the notifications value for this CreatePendingShipmentReply.
     * 
     * @param notifications   * The descriptive data detailing the status of a sumbitted transaction.
     */
    public void setNotifications(com.fedex.ship.stub.Notification[] notifications) {
        this.notifications = notifications;
    }

    public com.fedex.ship.stub.Notification getNotifications(int i) {
        return this.notifications[i];
    }

    public void setNotifications(int i, com.fedex.ship.stub.Notification _value) {
        this.notifications[i] = _value;
    }


    /**
     * Gets the transactionDetail value for this CreatePendingShipmentReply.
     * 
     * @return transactionDetail   * Descriptive data that governs data payload language/translations.
     * The TransactionDetail from the request is echoed back to the caller
     * in the corresponding reply.
     */
    public com.fedex.ship.stub.TransactionDetail getTransactionDetail() {
        return transactionDetail;
    }


    /**
     * Sets the transactionDetail value for this CreatePendingShipmentReply.
     * 
     * @param transactionDetail   * Descriptive data that governs data payload language/translations.
     * The TransactionDetail from the request is echoed back to the caller
     * in the corresponding reply.
     */
    public void setTransactionDetail(com.fedex.ship.stub.TransactionDetail transactionDetail) {
        this.transactionDetail = transactionDetail;
    }


    /**
     * Gets the version value for this CreatePendingShipmentReply.
     * 
     * @return version   * Identifies the version/level of a service operation expected
     * by a caller (in each request) and performed by the callee (in each
     * reply).
     */
    public com.fedex.ship.stub.VersionId getVersion() {
        return version;
    }


    /**
     * Sets the version value for this CreatePendingShipmentReply.
     * 
     * @param version   * Identifies the version/level of a service operation expected
     * by a caller (in each request) and performed by the callee (in each
     * reply).
     */
    public void setVersion(com.fedex.ship.stub.VersionId version) {
        this.version = version;
    }


    /**
     * Gets the completedShipmentDetail value for this CreatePendingShipmentReply.
     * 
     * @return completedShipmentDetail   * The reply payload. All of the returned information about this
     * shipment/package.
     */
    public com.fedex.ship.stub.CompletedShipmentDetail getCompletedShipmentDetail() {
        return completedShipmentDetail;
    }


    /**
     * Sets the completedShipmentDetail value for this CreatePendingShipmentReply.
     * 
     * @param completedShipmentDetail   * The reply payload. All of the returned information about this
     * shipment/package.
     */
    public void setCompletedShipmentDetail(com.fedex.ship.stub.CompletedShipmentDetail completedShipmentDetail) {
        this.completedShipmentDetail = completedShipmentDetail;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreatePendingShipmentReply)) return false;
        CreatePendingShipmentReply other = (CreatePendingShipmentReply) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.highestSeverity==null && other.getHighestSeverity()==null) || 
             (this.highestSeverity!=null &&
              this.highestSeverity.equals(other.getHighestSeverity()))) &&
            ((this.notifications==null && other.getNotifications()==null) || 
             (this.notifications!=null &&
              java.util.Arrays.equals(this.notifications, other.getNotifications()))) &&
            ((this.transactionDetail==null && other.getTransactionDetail()==null) || 
             (this.transactionDetail!=null &&
              this.transactionDetail.equals(other.getTransactionDetail()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.completedShipmentDetail==null && other.getCompletedShipmentDetail()==null) || 
             (this.completedShipmentDetail!=null &&
              this.completedShipmentDetail.equals(other.getCompletedShipmentDetail())));
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
        if (getHighestSeverity() != null) {
            _hashCode += getHighestSeverity().hashCode();
        }
        if (getNotifications() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNotifications());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNotifications(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTransactionDetail() != null) {
            _hashCode += getTransactionDetail().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getCompletedShipmentDetail() != null) {
            _hashCode += getCompletedShipmentDetail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreatePendingShipmentReply.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "CreatePendingShipmentReply"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("highestSeverity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "HighestSeverity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "NotificationSeverityType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notifications");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Notifications"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Notification"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "TransactionDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "TransactionDetail"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "VersionId"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completedShipmentDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "CompletedShipmentDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "CompletedShipmentDetail"));
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
