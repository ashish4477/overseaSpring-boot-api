package com.bearcode.ovf.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2007
 * Time: 5:09:34 PM
 *
 * @author Leonid Ginzburg
 */

@Entity
@Table(name = "addresses")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Address extends BusinessKeyObject implements Serializable {
	private static final long serialVersionUID = -6405969852170577220L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "address_to")
	private String addressTo = "";

	@Column
	private String street1 = "";

	@Column
	private String street2 = "";

	@Column
	private String city = "";

	@Column
	private String state = "";

	@Column
	private String zip = "";

	@Column
	private String zip4 = "";

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    @BusinessKey
    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo( String addressTo ) {
        this.addressTo = addressTo;
    }

	@BusinessKey
    public String getStreet1() {
        return street1.trim();
    }

    public void setStreet1( String street1 ) {
        if ( street1 != null ) this.street1 = street1.trim();
    }

    @BusinessKey
    public String getStreet2() {
        return street2;
    }

    public void setStreet2( String street2 ) {
        if ( street2 != null ) this.street2 = street2.trim();
    }

    @JsonIgnore
    public String getFullStreet() {
        final StringBuilder sb = new StringBuilder( (street1 == null) ? "" : street1.trim() );
        final String s2 = (street2 == null) ? "" : street2.trim();
        if ( s2.isEmpty() ) {
            return sb.toString();
		}

		if (sb.length() > 0) {
			sb.append(", ");
		}
		sb.append(s2);

		return sb.toString();
	}

    @BusinessKey
    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        if ( city != null ) this.city = city.trim();
    }

	@BusinessKey
	public String getState() {
		return state;
	}

    public void setState( String state ) {
        if ( state != null ) this.state = state.trim();
    }

    @BusinessKey
    public String getZip() {
        return zip;
    }

    public void setZip( String zip ) {
        if ( zip != null ) this.zip = zip.trim();
    }

	@BusinessKey
	public String getZip4() {
		return zip4;
	}

    public void setZip4( String zip4 ) {
        if ( zip4 != null ) this.zip4 = zip4.trim();
    }

    public void updateFrom( Address address ) {
        addressTo = address.getAddressTo();
        street1 = address.getStreet1();
        street2 = address.getStreet2();
        city = address.getCity();
        state = address.getState();
        zip = address.getZip();
        zip4 = address.getZip4();
    }

    @JsonIgnore
    public boolean isEmptySpace() {
        return StringUtils.isBlank( addressTo ) &&
                StringUtils.isBlank( street1 ) &&
                StringUtils.isBlank( street2 ) &&
                StringUtils.isBlank( city ) &&
                StringUtils.isBlank( state ) &&
                StringUtils.isBlank( zip ) &&
                StringUtils.isBlank( zip4 );
    }

    public boolean checkEqual( Address compare ) {
        return compare != null &&
                addressTo.equalsIgnoreCase( compare.getAddressTo() ) &&
                street1.equalsIgnoreCase( compare.getStreet1() ) &&
                street2.equalsIgnoreCase( compare.getStreet2() ) &&
                city.equalsIgnoreCase( compare.getCity() ) &&
                state.equalsIgnoreCase( compare.getState() ) &&
                zip.equalsIgnoreCase( compare.getZip() ) &&
                zip4.equalsIgnoreCase( compare.getZip4() );
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof Address && isEquals( this, obj );
    }
}
