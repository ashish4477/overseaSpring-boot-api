package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.BusinessKeyObject;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by leonid on 13.05.16.
 */
public class AdditionalAddress extends BusinessKeyObject implements Serializable {

    private static final long serialVersionUID = -6532552165117465642L;
    private long id = 0;

    private AdditionalAddressType type;
    private Address address;
    private String email = "";
    private String website = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public AdditionalAddressType getType() {
        return type;
    }

    public void setType( AdditionalAddressType type ) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress( Address address ) {
        this.address = address;
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

    @Override
    public boolean equals( Object obj ) {
        if ( obj instanceof CorrectionAdditionalAddress ) {
            CorrectionAdditionalAddress correction = (CorrectionAdditionalAddress) obj;
            return correction.checkEqual( this );
        }
        return super.equals( obj );
    }

    public boolean checkEmpty() {
        return type == null || ( StringUtils.isBlank( type.getName() ) && type.getId() == null );
    }

    public void updateFrom( AdditionalAddress updateFrom ) {
        if ( type == null ) {
            type = new AdditionalAddressType();
        }
        type.setName( updateFrom.getType().getName() );
        if ( address == null ) {
            address = new CorrectionAdditionalAddress();
        }
        address.updateFrom( updateFrom.getAddress() );
        email = updateFrom.getEmail();
        website = updateFrom.getWebsite();
    }
}
