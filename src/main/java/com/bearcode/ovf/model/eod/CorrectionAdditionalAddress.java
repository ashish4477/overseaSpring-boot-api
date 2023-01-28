package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.BusinessKeyObject;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by leonid on 22.05.16.
 */
public class CorrectionAdditionalAddress extends Address {
    private static final long serialVersionUID = -1197488733549622350L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0l;

    @Column(name = "address_type")
    private String addressTypeName = "";

    private String email = "";
    private String website = "";

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getAddressTypeName() {
        return addressTypeName;
    }

    public void setAddressTypeName( String addressTypeName ) {
        this.addressTypeName = addressTypeName;
    }


    public void updateFrom( AdditionalAddress additionalAddress ) {
        addressTypeName = additionalAddress.getType().getName();
        email = additionalAddress.getEmail();
        website = additionalAddress.getWebsite();
        super.updateFrom( additionalAddress.getAddress() );
    }

    public boolean isEmpty() {
        return StringUtils.isBlank( addressTypeName ) &&
                super.isEmptySpace();

    }

    public boolean checkEqual( AdditionalAddress additionalAddress ) {
        return additionalAddress != null && additionalAddress.getType() != null &&
                addressTypeName.equalsIgnoreCase( additionalAddress.getType().getName() ) &&
                email.equalsIgnoreCase( additionalAddress.getEmail() ) &&
                website.equalsIgnoreCase( additionalAddress.getWebsite() ) &&
                super.checkEqual( additionalAddress.getAddress() );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj instanceof AdditionalAddress ) {
            AdditionalAddress address = (AdditionalAddress) obj;
            return this.checkEqual( address );
        }
        return super.equals( obj );
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite( String website ) {
        this.website = website;
    }
}
