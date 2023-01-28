package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonid on 27.05.16.
 */
public class ElectionDate implements Serializable, Comparable<ElectionDate> {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat( "hh:mm:ss" );
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
    private static final long serialVersionUID = -2090140917244287357L;

    private long id = 0;
    private String date;
    private String time;
    private String kind = "";
    private ElectionDateType dateType;
    private String timeZone = "";
    private String timeZoneOffset = "";
    private String dateHumanReadable = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind( String kind ) {
        this.kind = kind;
    }

    public ElectionDateType getDateType() {
        return dateType;
    }

    public void setDateType( ElectionDateType dateType ) {
        this.dateType = dateType;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone( String timeZone ) {
        this.timeZone = timeZone;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset( String timeZoneOffset ) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public String getDateHumanReadable() {
        return dateHumanReadable;
    }

    public void setDateHumanReadable( String dateHumanReadable ) {
        this.dateHumanReadable = dateHumanReadable;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date ) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime( String time ) {
        this.time = time;
    }

    public ElectionDateKind getDateKind() {
        return ElectionDateKind.valueOf( kind );
    }

    public String getReadableWithMarkup() {
        if ( this.getDateType().getName().equalsIgnoreCase( ElectionDateType.AUTOMATIC ) ||
                this.getDateType().getName().equalsIgnoreCase( ElectionDateType.NOT_ALLOWED ) ||
                this.getDateType().getName().equalsIgnoreCase( ElectionDateType.NOT_REQUIRED ) ||
                this.getDateType().getName().equalsIgnoreCase( ElectionDateType.TO_BE_ANNOUNCED ) ) {
            return dateHumanReadable;
        }
        String markup = dateHumanReadable;
        Pattern pattern = Pattern.compile( "(\\w.*)(\\w{3} \\w{3} \\d{1,2}).*" );
        Matcher matcher = pattern.matcher( markup );
        if ( matcher.matches() ) {
            String toBold = matcher.group(1);
            markup = markup.replace( toBold, "<strong>" + toBold + "</strong>" );
        }
        pattern = Pattern.compile( "\\d((A|P)M)" );
        matcher = pattern.matcher( markup );
        if ( matcher.find() ) {
            String toLower = matcher.group( 1 );
            markup = markup.replace( toLower, toLower.toLowerCase() );
        }
        return markup;
    }

    public Date getDateAsDate() {
        if ( date != null ) {
            Pattern pattern = Pattern.compile( "\\d{4}-\\d{2}-\\d{2}" );
            Matcher matcher = pattern.matcher( date );
            if ( matcher.find() ) {
                Date date = null;
                try {
                    date = DATE_FORMAT.parse( matcher.group(0) );
                } catch (Exception e) {
                }
                return date;
            }
        }
        return null;
    }


    @Override
    public int compareTo( ElectionDate o ) {
        if ( o == null ) {
            return -1;
        }
        if ( dateType.getId() == 1 && o.getDateType().getId() != 1 ) {
            return -1;
        }
        else if ( dateType.getId() != 1 && o.getDateType().getId() == 1  ) {
            return 1;
        }
        Date dateOne = getDateAsDate();
        Date dateTwo = o.getDateAsDate();
        if ( dateOne == null && dateTwo == null ) {
            return 0;
        }
        if ( dateOne == null ) {
            return 1;
        }
        if ( dateTwo == null ) {
            return -1;
        }
        return dateOne.compareTo( dateTwo );
    }
}
