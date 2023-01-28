/**
 * DeleteShipmentRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fedex.ship.stub;


/**
 * Descriptive data sent to FedEx by a customer in order to delete
 * a package.
 */
public class DeleteShipmentRequest  implements java.io.Serializable {
    /* Descriptive data to be used in authentication of the sender's
     * identity (and right to use FedEx web services). */
    private com.fedex.ship.stub.WebAuthenticationDetail webAuthenticationDetail;

    /* Descriptive data identifying the client submitting the transaction. */
    private com.fedex.ship.stub.ClientDetail clientDetail;

    /* Descriptive data for this customer transaction. The TransactionDetail
     * from the request is echoed back to the caller in the corresponding
     * reply. */
    private com.fedex.ship.stub.TransactionDetail transactionDetail;

    /* Identifies the version/level of a service operation expected
     * by a caller (in each request) and performed by the callee (in each
     * reply). */
    private com.fedex.ship.stub.VersionId version;

    /* The timestamp of the shipment request. */
    private java.util.Calendar shipTimestamp;

    /* Identifies the FedEx tracking number of the package being cancelled. */
    private com.fedex.ship.stub.TrackingId trackingId;

    /* Determines the type of deletion to be performed in relation
     * to package level vs shipment level. */
    private com.fedex.ship.stub.DeletionControlType deletionControl;

    public DeleteShipmentRequest() {
    }

    public DeleteShipmentRequest(
           com.fedex.ship.stub.WebAuthenticationDetail webAuthenticationDetail,
           com.fedex.ship.stub.ClientDetail clientDetail,
           com.fedex.ship.stub.TransactionDetail transactionDetail,
           com.fedex.ship.stub.VersionId version,
           java.util.Calendar shipTimestamp,
           com.fedex.ship.stub.TrackingId trackingId,
           com.fedex.ship.stub.DeletionControlType deletionControl) {
           this.webAuthenticationDetail = webAuthenticationDetail;
           this.clientDetail = clientDetail;
           this.transactionDetail = transactionDetail;
           this.version = version;
           this.shipTimestamp = shipTimestamp;
           this.trackingId = trackingId;
           this.deletionControl = deletionControl;
    }


    /**
     * Gets the webAuthenticationDetail value for this DeleteShipmentRequest.
     * 
     * @return webAuthenticationDetail   * Descriptive data to be used in authentication of the sender's
     * identity (and right to use FedEx web services).
     */
    public com.fedex.ship.stub.WebAuthenticationDetail getWebAuthenticationDetail() {
        return webAuthenticationDetail;
    }


    /**
     * Sets the webAuthenticationDetail value for this DeleteShipmentRequest.
     * 
     * @param webAuthenticationDetail   * Descriptive data to be used in authentication of the sender's
     * identity (and right to use FedEx web services).
     */
    public void setWebAuthenticationDetail(com.fedex.ship.stub.WebAuthenticationDetail webAuthenticationDetail) {
        this.webAuthenticationDetail = webAuthenticationDetail;
    }


    /**
     * Gets the clientDetail value for this DeleteShipmentRequest.
     * 
     * @return clientDetail   * Descriptive data identifying the client submitting the transaction.
     */
    public com.fedex.ship.stub.ClientDetail getClientDetail() {
        return clientDetail;
    }


    /**
     * Sets the clientDetail value for this DeleteShipmentRequest.
     * 
     * @param clientDetail   * Descriptive data identifying the client submitting the transaction.
     */
    public void setClientDetail(com.fedex.ship.stub.ClientDetail clientDetail) {
        this.clientDetail = clientDetail;
    }


    /**
     * Gets the transactionDetail value for this DeleteShipmentRequest.
     * 
     * @return transactionDetail   * Descriptive data for this customer transaction. The TransactionDetail
     * from the request is echoed back to the caller in the corresponding
     * reply.
     */
    public com.fedex.ship.stub.TransactionDetail getTransactionDetail() {
        return transactionDetail;
    }


    /**
     * Sets the transactionDetail value for this DeleteShipmentRequest.
     * 
     * @param transactionDetail   * Descriptive data for this customer transaction. The TransactionDetail
     * from the request is echoed back to the caller in the corresponding
     * reply.
     */
    public void setTransactionDetail(com.fedex.ship.stub.TransactionDetail transactionDetail) {
        this.transactionDetail = transactionDetail;
    }


    /**
     * Gets the version value for this DeleteShipmentRequest.
     * 
     * @return version   * Identifies the version/level of a service operation expected
     * by a caller (in each request) and performed by the callee (in each
     * reply).
     */
    public com.fedex.ship.stub.VersionId getVersion() {
        return version;
    }


    /**
     * Sets the version value for this DeleteShipmentRequest.
     * 
     * @param version   * Identifies the version/level of a service operation expected
     * by a caller (in each request) and performed by the callee (in each
     * reply).
     */
    public void setVersion(com.fedex.ship.stub.VersionId version) {
        this.version = version;
    }


    /**
     * Gets the shipTimestamp value for this DeleteShipmentRequest.
     * 
     * @return shipTimestamp   * The timestamp of the shipment request.
     */
    public java.util.Calendar getShipTimestamp() {
        return shipTimestamp;
    }


    /**
     * Sets the shipTimestamp value for this DeleteShipmentRequest.
     * 
     * @param shipTimestamp   * The timestamp of the shipment request.
     */
    public void setShipTimestamp(java.util.Calendar shipTimestamp) {
        this.shipTimestamp = shipTimestamp;
    }


    /**
     * Gets the trackingId value for this DeleteShipmentRequest.
     * 
     * @return trackingId   * Identifies the FedEx tracking number of the package being cancelled.
     */
    public com.fedex.ship.stub.TrackingId getTrackingId() {
        return trackingId;
    }


    /**
     * Sets the trackingId value for this DeleteShipmentRequest.
     * 
     * @param trackingId   * Identifies the FedEx tracking number of the package being cancelled.
     */
    public void setTrackingId(com.fedex.ship.stub.TrackingId trackingId) {
        this.trackingId = trackingId;
    }


    /**
     * Gets the deletionControl value for this DeleteShipmentRequest.
     * 
     * @return deletionControl   * Determines the type of deletion to be performed in relation
     * to package level vs shipment level.
     */
    public com.fedex.ship.stub.DeletionControlType getDeletionControl() {
        return deletionControl;
    }


    /**
     * Sets the deletionControl value for this DeleteShipmentRequest.
     * 
     * @param deletionControl   * Determines the type of deletion to be performed in relation
     * to package level vs shipment level.
     */
    public void setDeletionControl(com.fedex.ship.stub.DeletionControlType deletionControl) {
        this.deletionControl = deletionControl;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeleteShipmentRequest)) return false;
        DeleteShipmentRequest other = (DeleteShipmentRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.webAuthenticationDetail==null && other.getWebAuthenticationDetail()==null) || 
             (this.webAuthenticationDetail!=null &&
              this.webAuthenticationDetail.equals(other.getWebAuthenticationDetail()))) &&
            ((this.clientDetail==null && other.getClientDetail()==null) || 
             (this.clientDetail!=null &&
              this.clientDetail.equals(other.getClientDetail()))) &&
            ((this.transactionDetail==null && other.getTransactionDetail()==null) || 
             (this.transactionDetail!=null &&
              this.transactionDetail.equals(other.getTransactionDetail()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.shipTimestamp==null && other.getShipTimestamp()==null) || 
             (this.shipTimestamp!=null &&
              this.shipTimestamp.equals(other.getShipTimestamp()))) &&
            ((this.trackingId==null && other.getTrackingId()==null) || 
             (this.trackingId!=null &&
              this.trackingId.equals(other.getTrackingId()))) &&
            ((this.deletionControl==null && other.getDeletionControl()==null) || 
             (this.deletionControl!=null &&
              this.deletionControl.equals(other.getDeletionControl())));
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
        if (getWebAuthenticationDetail() != null) {
            _hashCode += getWebAuthenticationDetail().hashCode();
        }
        if (getClientDetail() != null) {
            _hashCode += getClientDetail().hashCode();
        }
        if (getTransactionDetail() != null) {
            _hashCode += getTransactionDetail().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getShipTimestamp() != null) {
            _hashCode += getShipTimestamp().hashCode();
        }
        if (getTrackingId() != null) {
            _hashCode += getTrackingId().hashCode();
        }
        if (getDeletionControl() != null) {
            _hashCode += getDeletionControl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeleteShipmentRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "DeleteShipmentRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("webAuthenticationDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "WebAuthenticationDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "WebAuthenticationDetail"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ClientDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ClientDetail"));
        elemField.setNillable(false);
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
        elemField.setFieldName("shipTimestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ShipTimestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trackingId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "TrackingId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "TrackingId"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deletionControl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "DeletionControl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "DeletionControlType"));
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
