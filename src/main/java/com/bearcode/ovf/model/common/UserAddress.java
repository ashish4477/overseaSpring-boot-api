package com.bearcode.ovf.model.common;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2007
 * Time: 5:09:34 PM
 *
 * @author Leonid Ginzburg
 */
@Embeddable
public class UserAddress extends Address implements Cloneable {
    private static final long serialVersionUID = -8413164899825982621L;
    private String country = "";
    private String county = "";
    private String description = "";
    private AddressType type = AddressType.STREET;

    public static String REMOVED_FIELD_PATTERN = "--REMOVED:";

    //names of field when exporting to pdf
    public static final String FIELD_ADDRESS = "Address";
    public static final String FIELD_CITY = "City";
    public static final String FIELD_STATE = "State";
    public static final String FIELD_COUNTY = "County";
    public static final String FIELD_COUNTRY = "Country";
    public static final String FIELD_ZIP = "Zip Code";
    public static final String FIELD_ZIP5 = "Zip Code5";
    public static final String FIELD_ZIP4 = "Zip Code4";
    public static final String FIELD_POSTAL = "Postal Code";
    public static final String FIELD_DESCRIPTION = "Description";
    public static final String FIELD_FORMATTED = "Formatted Address";
    public static final String FIELD_STREET1 = "Street1";
    public static final String FIELD_STREET2 = "Street2";


    public UserAddress() {
    }

    public UserAddress( AddressType type ) {
        this.type = type;
    }

    // copy ctor
    protected UserAddress( UserAddress userAddress ) {
        this.setType( userAddress.getType() );
        this.setStreet1( userAddress.getStreet1() );
        this.setStreet2( userAddress.getStreet2() );
        this.setCity( userAddress.getCity() );
        this.setState( userAddress.getState() );
        this.setZip( userAddress.getZip() );
        this.setZip4( userAddress.getZip4() );
        this.setCountry( userAddress.getCountry() );
        this.setDescription( userAddress.getDescription() );
        this.setCounty( userAddress.getCounty() );
    }

    public String getCountry() {
        return country;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    public void updateFrom( final UserAddress address ) {
        if ( address == null )
            return;
        super.updateFrom( address );
        country = address.getCountry();
        description = address.getDescription();
        county = address.getCounty();
        if ( address.getType() != null && address.getType() != AddressType.UNKNOWN ) {
            type = address.getType();
        }
    }

    @Transient
    @JsonIgnore
    public boolean isEmptySpace() {
        return super.isEmptySpace() &&
                StringUtils.isBlank( country ) &&
                StringUtils.isBlank( county );
    }

    public boolean checkEqual( UserAddress compare ) {
        return super.checkEqual( compare ) &&
                country.equalsIgnoreCase( compare.getCountry() );
    }


    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    public AddressType getType() {
        return this.type;
    }

    public void setType( AddressType type ) {
        this.type = type;
    }

    public String getTypeName() {
        return type.name();
    }

    public void setTypeName( String typeName ) {
        if ( typeName != null )
            this.type = AddressType.valueOf( typeName );
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        if ( description != null ) this.description = description.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty( String county ) {
        if ( county != null ) this.county = county.trim();
    }

    public void makeAnonymous() {
        String removedDate = new Date().toString();
        setAddressTo( REMOVED_FIELD_PATTERN + removedDate + "--" );
        setStreet1( REMOVED_FIELD_PATTERN + removedDate + "--" );
        setStreet2( REMOVED_FIELD_PATTERN + removedDate + "--" );
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        UserAddress userAddress = new UserAddress();
        userAddress.setCountry( this.getCountry() );
        userAddress.setDescription( this.getDescription() );
        userAddress.setCounty( this.getCounty() );
        return userAddress;
    }

    /**
     * Gets the address on a single line, using commas and whitespace as separators.
     * @author IanBrown
     * @return the address line.
     * @since May 25, 2012
     * @version May 25, 2012
     */
    @Transient
    @JsonIgnore
    public String getSingleLineAddress() {
        StringBuffer buffer = new StringBuffer();
        switch ( type ) {
            case STREET:
            case OVERSEAS:
                buffer.append( getFullStreet() )
                		.append(", ")
                        .append( getCity() )
                        .append( (getState().length() > 0 || getZip().length() > 0) ? ", " : "" )
                        .append( getState() )
                        .append( " " )
                        .append( getZip() )
                        .append( getZip4().length() > 0 ? "-" : "" )
                        .append( getZip4() )
                        .append( "," )
                        .append( getCountry()  );
                break;
            case RURAL_ROUTE:
            case DESCRIBED:
                buffer.append( getFullStreet() )
                        .append( " " )
                        .append( getDescription() )
                        .append( ", " )
                        .append( getCity() )
                        .append( (getState().length() > 0 || getZip().length() > 0) ? ", " : "" )
                        .append( getState() )
                        .append( " " )
                        .append( getZip() )
                        .append( getZip4().length() > 0 ? "-" : "" )
                        .append( getZip4() )
                        .append( ", " )
                        .append( getCountry() );
                break;
            case MILITARY:
                buffer.append( getFullStreet() )
                        .append( ", " )
                        .append( getCity() )
                        .append( " " )
                        .append( getState() )
                        .append( " " )
                        .append( getZip() );
                break;
        }
        return buffer.toString();    	
    }
    
    @Transient
    @JsonIgnore
    public String getFormattedAddress() {
        return formatAddressForOutput("<p>", "</p>");
    }

    @Transient
    @JsonIgnore
    public String getMultilineAddress() {
    	return formatAddressForOutput("", "<br />");
    }
    
	/**
	 * Formats the address for output to HTML.
	 * @author IanBrown
	 * @param linePrefix the prefix for a line.
	 * @param linePostfix the postfix for a line.
	 * @return the formatted address.
	 * @since Oct 24, 2012
	 * @version Oct 24, 2012
	 */
	public String formatAddressForOutput(String linePrefix, String linePostfix) {
		StringBuffer buffer = new StringBuffer();
        switch ( type ) {
            case STREET:
            case OVERSEAS:
                buffer.append(linePrefix)
                        .append( StringEscapeUtils.escapeXml( getFullStreet() ) )
                        .append( linePostfix ).append( linePrefix )
                        .append( StringEscapeUtils.escapeXml( getCity() ) )
                        .append( (getState().length() > 0 || getZip().length() > 0) ? ", " : "" )
                        .append( StringEscapeUtils.escapeXml( getState() ) )
                        .append( " " )
                        .append( getZip() )
                        .append( getZip4().length() > 0 ? "-" : "" )
                        .append( getZip4() )
                        .append( linePostfix ).append( linePrefix )
                        .append( StringEscapeUtils.escapeXml( getCountry() ) )
                        .append( linePostfix );
                break;
            case RURAL_ROUTE:
            case DESCRIBED:
                buffer.append( linePrefix )
                        .append( StringEscapeUtils.escapeXml( getFullStreet() ) )
                        .append( " " )
                        .append( StringEscapeUtils.escapeXml( getDescription() ) )
                        .append( linePostfix ).append( linePrefix )
                        .append( StringEscapeUtils.escapeXml( getCity() ) )
                        .append( (getState().length() > 0 || getZip().length() > 0) ? ", " : "" )
                        .append( getState() )
                        .append( " " )
                        .append( getZip() )
                        .append( getZip4().length() > 0 ? "-" : "" )
                        .append( getZip4() )
                        .append( linePostfix ).append( linePrefix )
                        .append( StringEscapeUtils.escapeXml( getCountry() ) )
                        .append( linePostfix );
                break;
            case MILITARY:
                buffer.append( linePrefix )
                        .append( StringEscapeUtils.escapeXml( getFullStreet() ) )
                        .append( linePostfix ).append( linePrefix )
                        .append( StringEscapeUtils.escapeXml( getCity() ) )
                        .append( " " )
                        .append( getState() )
                        .append( " " )
                        .append( getZip() )
                        .append( linePostfix );
                break;
        }
        return buffer.toString();
	}

    public void populateAddressFields( Map<String, String> fields, String prefix, boolean country ) {

        switch ( type ) {
            case STREET:
                fields.put( prefix + FIELD_COUNTY, StringEscapeUtils.escapeXml( getCounty() ) );
            case MILITARY:
            case OVERSEAS:
                fields.put( prefix + FIELD_ADDRESS, StringEscapeUtils.escapeXml( getFullStreet() ) ); //only street should be there
                fields.put( prefix + FIELD_CITY, StringEscapeUtils.escapeXml( getCity() ) );
                fields.put( prefix + FIELD_STATE, getState() );
                if ( country ) {
                    fields.put( prefix + FIELD_POSTAL, getZip() );
                    fields.put( prefix + FIELD_COUNTRY, StringEscapeUtils.escapeXml( getCountry() ) );
                } else {
                    if ( getZip4().length() > 0 ) {
                        fields.put( prefix + FIELD_ZIP, getZip() + "-" + getZip4() );
                    } else {
                        fields.put( prefix + FIELD_ZIP, getZip() );
                    }
                    fields.put( prefix + FIELD_ZIP5, getZip() );
                    fields.put( prefix + FIELD_ZIP4, getZip4() );
                }
                break;
            case RURAL_ROUTE:
            case DESCRIBED:
                fields.put( prefix + FIELD_ADDRESS, "SEE ADDENDUM (page 2)" );
                fields.put( prefix + FIELD_CITY, StringEscapeUtils.escapeXml( getCity() ) );
                fields.put( prefix + FIELD_STATE, getState() );
                if ( country ) {
                    fields.put( prefix + FIELD_POSTAL, getZip() );
                    fields.put( prefix + FIELD_COUNTRY, StringEscapeUtils.escapeXml( getCountry() ) );
                } else {
                    if ( getZip4().length() > 0 ) {
                        fields.put( prefix + FIELD_ZIP, getZip() + "-" + getZip4() );
                    } else {
                        fields.put( prefix + FIELD_ZIP, getZip() );
                    }
                    fields.put( prefix + FIELD_ZIP5, getZip() );
                    fields.put( prefix + FIELD_ZIP4, getZip4() );
                }
                fields.put( prefix + FIELD_DESCRIPTION, StringEscapeUtils.escapeXml( ((getStreet1().length() > 0) ? (getStreet1() + " ") : "") + getDescription() ) );
                break;

        }
        fields.put( prefix + FIELD_FORMATTED, getFormattedAddress() );

        fields.put( prefix + FIELD_STREET1, StringEscapeUtils.escapeXml( getStreet1() ) );
        fields.put( prefix + FIELD_STREET2, StringEscapeUtils.escapeXml( getStreet2() ) );
    }

    public static UserAddress create( UserAddress userAddress ) {
        return new UserAddress( userAddress );
    }

    public void anonymize() {
        this.setStreet1( "" );
        this.setStreet2( "" );
        this.setZip4( "" );
        this.setDescription( "" );
    }
}
