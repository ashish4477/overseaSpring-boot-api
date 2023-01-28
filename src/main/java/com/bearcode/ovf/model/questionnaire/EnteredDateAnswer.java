package com.bearcode.ovf.model.questionnaire;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * @author Alexey Polyakov
 *         Date: Sep 21, 2007
 *         Time: 4:18:13 PM
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("DATE")
public class EnteredDateAnswer extends Answer {
    private static final long serialVersionUID = 322346556101480328L;

    private Date value;

    private static final DateFormat INPUT_FORMAT = new SimpleDateFormat( "MM/dd/yyyy" );
    private static final DateFormat SHORT_FORMAT = new SimpleDateFormat( "MM/dd/yyyy" );
    private static final DateFormat LONG_FORMAT = new SimpleDateFormat( "d MMMM yyyy" );

    public void setValue( String value ) {
        try {
            if ( value != null && value.lastIndexOf( "/" ) > 0 ) {
                this.value = INPUT_FORMAT.parse( value );
            } else {
                this.value = new Date( Long.parseLong( value ) );
            }
        } catch ( ParseException e ) {
            this.value = null;
        } catch ( NumberFormatException e ) {
            this.value = null;
        }
    }

    @Column(name = "entered_value")
    public String getValue() {
        if ( value == null )
            return "";
        return INPUT_FORMAT.format( value );
    }


    public void output( Map<String, String> model, boolean doEscapeXml ) {
        if ( getField().getInPdfName().length() > 0 ) {
            model.put( getField().getInPdfName(), getDate( SHORT_FORMAT ) );
            model.put( getField().getInPdfName() + "_long", getDate( LONG_FORMAT ) );
        }
    }

    public Date getDate() {
        return value;
    }

    public String getDate( DateFormat format ) {
        if ( value == null ) return "";
        return format.format( value );
    }

    @Override
    public Answer createClone() {
        return new EnteredDateAnswer();
    }

}
