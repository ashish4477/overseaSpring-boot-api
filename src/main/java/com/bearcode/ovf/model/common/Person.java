package com.bearcode.ovf.model.common;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Jun 18, 2007 Time: 5:00:16 PM Person's description: title, name, etc.
 */
public class Person extends BusinessKeyObject implements Serializable {
    private static final long serialVersionUID = -8200206582050524027L;
    private long id = 0;
    private String title = "";
    private String firstName = "";
    private String initial = "";
    private String lastName = "";
    private String suffix = "";

    public static final String REMOVED_FIELD_PATTERN = "--REMOVED:%s--";

    public long getId() {
        return id;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    @BusinessKey
    public String getTitle() {
        return title;
    }

    public void setTitle( final String title ) {
        if ( title != null ) {
            this.title = title.trim();
        }
    }

    @BusinessKey
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( final String firstName ) {
        if ( firstName != null ) {
            this.firstName = firstName.trim();
        }
    }

    @BusinessKey
    public String getInitial() {
        return initial;
    }

    public void setInitial( final String initial ) {
        if ( initial != null ) {
            this.initial = initial.trim();
        }
    }

    @BusinessKey
    public String getLastName() {
        return lastName;
    }

    public void setLastName( final String lastName ) {
        if ( lastName != null ) {
            this.lastName = lastName.trim();
        }
    }

    @BusinessKey
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix( final String suffix ) {
        if ( suffix != null ) {
            this.suffix = suffix.trim();
        }
    }

    @Transient
    public String getMiddleName() {
        return initial;
    }

    public void setMiddleName( final String middleName ) {
        if ( middleName != null ) {
            initial = middleName.trim();
        }
    }

    public void updateFrom( final Person person ) {
        if ( person != null ) {
            title = person.getTitle();
            firstName = person.getFirstName();
            initial = person.getInitial();
            lastName = person.getLastName();
            suffix = person.getSuffix();
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return title.length() == 0 &&
                firstName.length() == 0 &&
                initial.length() == 0 &&
                lastName.length() == 0 &&
                suffix.length() == 0;
    }

    public boolean checkEquals( final Person person ) {
        return isEquals( this, person );
    }

    public void makeAnonymous() {
        final String removedDate = new Date().toString();
        firstName = String.format( REMOVED_FIELD_PATTERN, removedDate );
        lastName = String.format( REMOVED_FIELD_PATTERN, removedDate );
        initial = String.format( REMOVED_FIELD_PATTERN, removedDate );
        suffix = "";
        title = "";
    }

    @JsonIgnore
    public String getFullName() {
        final StringBuilder buffer = new StringBuilder();
        String prefix = "";
        if (lastName != null && !lastName.isEmpty()) {
        	buffer.append(prefix).append(lastName);
        	prefix = ", ";
        }
        if (firstName != null && !firstName.isEmpty()) {
        	buffer.append(prefix).append(firstName);
        	prefix = " ";
        }
        if (initial != null && !initial.isEmpty()) {
        	buffer.append(prefix).append(initial);
        	prefix = " ";
        }
        return buffer.toString();
    }
}
