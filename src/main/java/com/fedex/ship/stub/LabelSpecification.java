/**
 * LabelSpecification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fedex.ship.stub;


/**
 * Description of shipping label to be returned in the reply
 */
public class LabelSpecification  implements java.io.Serializable {
    /* Specifies how to create, organize, and return the document. */
    private com.fedex.ship.stub.ShippingDocumentDispositionDetail[] dispositions;

    /* Specify type of label to be returned */
    private com.fedex.ship.stub.LabelFormatType labelFormatType;

    /* Specifies the image format used for a shipping document. */
    private com.fedex.ship.stub.ShippingDocumentImageType imageType;

    /* For thermal printer lables this indicates the size of the label
     * and the location of the doc tab if present. */
    private com.fedex.ship.stub.LabelStockType labelStockType;

    /* This indicates if the top or bottom of the label comes out
     * of the printer first. */
    private com.fedex.ship.stub.LabelPrintingOrientationType labelPrintingOrientation;

    /* If present, this contact and address information will replace
     * the return address information on the label. */
    private com.fedex.ship.stub.ContactAndAddress printedLabelOrigin;

    /* Allows customer-specified control of label content. */
    private com.fedex.ship.stub.CustomerSpecifiedLabelDetail customerSpecifiedDetail;

    public LabelSpecification() {
    }

    public LabelSpecification(
           com.fedex.ship.stub.ShippingDocumentDispositionDetail[] dispositions,
           com.fedex.ship.stub.LabelFormatType labelFormatType,
           com.fedex.ship.stub.ShippingDocumentImageType imageType,
           com.fedex.ship.stub.LabelStockType labelStockType,
           com.fedex.ship.stub.LabelPrintingOrientationType labelPrintingOrientation,
           com.fedex.ship.stub.ContactAndAddress printedLabelOrigin,
           com.fedex.ship.stub.CustomerSpecifiedLabelDetail customerSpecifiedDetail) {
           this.dispositions = dispositions;
           this.labelFormatType = labelFormatType;
           this.imageType = imageType;
           this.labelStockType = labelStockType;
           this.labelPrintingOrientation = labelPrintingOrientation;
           this.printedLabelOrigin = printedLabelOrigin;
           this.customerSpecifiedDetail = customerSpecifiedDetail;
    }


    /**
     * Gets the dispositions value for this LabelSpecification.
     * 
     * @return dispositions   * Specifies how to create, organize, and return the document.
     */
    public com.fedex.ship.stub.ShippingDocumentDispositionDetail[] getDispositions() {
        return dispositions;
    }


    /**
     * Sets the dispositions value for this LabelSpecification.
     * 
     * @param dispositions   * Specifies how to create, organize, and return the document.
     */
    public void setDispositions(com.fedex.ship.stub.ShippingDocumentDispositionDetail[] dispositions) {
        this.dispositions = dispositions;
    }

    public com.fedex.ship.stub.ShippingDocumentDispositionDetail getDispositions(int i) {
        return this.dispositions[i];
    }

    public void setDispositions(int i, com.fedex.ship.stub.ShippingDocumentDispositionDetail _value) {
        this.dispositions[i] = _value;
    }


    /**
     * Gets the labelFormatType value for this LabelSpecification.
     * 
     * @return labelFormatType   * Specify type of label to be returned
     */
    public com.fedex.ship.stub.LabelFormatType getLabelFormatType() {
        return labelFormatType;
    }


    /**
     * Sets the labelFormatType value for this LabelSpecification.
     * 
     * @param labelFormatType   * Specify type of label to be returned
     */
    public void setLabelFormatType(com.fedex.ship.stub.LabelFormatType labelFormatType) {
        this.labelFormatType = labelFormatType;
    }


    /**
     * Gets the imageType value for this LabelSpecification.
     * 
     * @return imageType   * Specifies the image format used for a shipping document.
     */
    public com.fedex.ship.stub.ShippingDocumentImageType getImageType() {
        return imageType;
    }


    /**
     * Sets the imageType value for this LabelSpecification.
     * 
     * @param imageType   * Specifies the image format used for a shipping document.
     */
    public void setImageType(com.fedex.ship.stub.ShippingDocumentImageType imageType) {
        this.imageType = imageType;
    }


    /**
     * Gets the labelStockType value for this LabelSpecification.
     * 
     * @return labelStockType   * For thermal printer lables this indicates the size of the label
     * and the location of the doc tab if present.
     */
    public com.fedex.ship.stub.LabelStockType getLabelStockType() {
        return labelStockType;
    }


    /**
     * Sets the labelStockType value for this LabelSpecification.
     * 
     * @param labelStockType   * For thermal printer lables this indicates the size of the label
     * and the location of the doc tab if present.
     */
    public void setLabelStockType(com.fedex.ship.stub.LabelStockType labelStockType) {
        this.labelStockType = labelStockType;
    }


    /**
     * Gets the labelPrintingOrientation value for this LabelSpecification.
     * 
     * @return labelPrintingOrientation   * This indicates if the top or bottom of the label comes out
     * of the printer first.
     */
    public com.fedex.ship.stub.LabelPrintingOrientationType getLabelPrintingOrientation() {
        return labelPrintingOrientation;
    }


    /**
     * Sets the labelPrintingOrientation value for this LabelSpecification.
     * 
     * @param labelPrintingOrientation   * This indicates if the top or bottom of the label comes out
     * of the printer first.
     */
    public void setLabelPrintingOrientation(com.fedex.ship.stub.LabelPrintingOrientationType labelPrintingOrientation) {
        this.labelPrintingOrientation = labelPrintingOrientation;
    }


    /**
     * Gets the printedLabelOrigin value for this LabelSpecification.
     * 
     * @return printedLabelOrigin   * If present, this contact and address information will replace
     * the return address information on the label.
     */
    public com.fedex.ship.stub.ContactAndAddress getPrintedLabelOrigin() {
        return printedLabelOrigin;
    }


    /**
     * Sets the printedLabelOrigin value for this LabelSpecification.
     * 
     * @param printedLabelOrigin   * If present, this contact and address information will replace
     * the return address information on the label.
     */
    public void setPrintedLabelOrigin(com.fedex.ship.stub.ContactAndAddress printedLabelOrigin) {
        this.printedLabelOrigin = printedLabelOrigin;
    }


    /**
     * Gets the customerSpecifiedDetail value for this LabelSpecification.
     * 
     * @return customerSpecifiedDetail   * Allows customer-specified control of label content.
     */
    public com.fedex.ship.stub.CustomerSpecifiedLabelDetail getCustomerSpecifiedDetail() {
        return customerSpecifiedDetail;
    }


    /**
     * Sets the customerSpecifiedDetail value for this LabelSpecification.
     * 
     * @param customerSpecifiedDetail   * Allows customer-specified control of label content.
     */
    public void setCustomerSpecifiedDetail(com.fedex.ship.stub.CustomerSpecifiedLabelDetail customerSpecifiedDetail) {
        this.customerSpecifiedDetail = customerSpecifiedDetail;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LabelSpecification)) return false;
        LabelSpecification other = (LabelSpecification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dispositions==null && other.getDispositions()==null) || 
             (this.dispositions!=null &&
              java.util.Arrays.equals(this.dispositions, other.getDispositions()))) &&
            ((this.labelFormatType==null && other.getLabelFormatType()==null) || 
             (this.labelFormatType!=null &&
              this.labelFormatType.equals(other.getLabelFormatType()))) &&
            ((this.imageType==null && other.getImageType()==null) || 
             (this.imageType!=null &&
              this.imageType.equals(other.getImageType()))) &&
            ((this.labelStockType==null && other.getLabelStockType()==null) || 
             (this.labelStockType!=null &&
              this.labelStockType.equals(other.getLabelStockType()))) &&
            ((this.labelPrintingOrientation==null && other.getLabelPrintingOrientation()==null) || 
             (this.labelPrintingOrientation!=null &&
              this.labelPrintingOrientation.equals(other.getLabelPrintingOrientation()))) &&
            ((this.printedLabelOrigin==null && other.getPrintedLabelOrigin()==null) || 
             (this.printedLabelOrigin!=null &&
              this.printedLabelOrigin.equals(other.getPrintedLabelOrigin()))) &&
            ((this.customerSpecifiedDetail==null && other.getCustomerSpecifiedDetail()==null) || 
             (this.customerSpecifiedDetail!=null &&
              this.customerSpecifiedDetail.equals(other.getCustomerSpecifiedDetail())));
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
        if (getDispositions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDispositions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDispositions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLabelFormatType() != null) {
            _hashCode += getLabelFormatType().hashCode();
        }
        if (getImageType() != null) {
            _hashCode += getImageType().hashCode();
        }
        if (getLabelStockType() != null) {
            _hashCode += getLabelStockType().hashCode();
        }
        if (getLabelPrintingOrientation() != null) {
            _hashCode += getLabelPrintingOrientation().hashCode();
        }
        if (getPrintedLabelOrigin() != null) {
            _hashCode += getPrintedLabelOrigin().hashCode();
        }
        if (getCustomerSpecifiedDetail() != null) {
            _hashCode += getCustomerSpecifiedDetail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LabelSpecification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "LabelSpecification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispositions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "Dispositions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ShippingDocumentDispositionDetail"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("labelFormatType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "LabelFormatType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "LabelFormatType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ImageType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ShippingDocumentImageType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("labelStockType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "LabelStockType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "LabelStockType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("labelPrintingOrientation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "LabelPrintingOrientation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "LabelPrintingOrientationType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("printedLabelOrigin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "PrintedLabelOrigin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "ContactAndAddress"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customerSpecifiedDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "CustomerSpecifiedDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fedex.com/ws/ship/v12", "CustomerSpecifiedLabelDetail"));
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
