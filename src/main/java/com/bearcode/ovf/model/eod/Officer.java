package com.bearcode.ovf.model.eod;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

/**
 * Date: 12.11.12
 * Time: 18:15
 *
 * @author Leonid Ginzburg
 */
public class Officer implements Serializable {
    private static final long serialVersionUID = 3689024556812425996L;

    private long id;
    private String officeName = "";
    private String email = "";
    private String phone = "";
    private String fax = "";
    private String title = "";
    private String firstName = "";
    private String initial = "";
    private String lastName = "";
    private String suffix = "";
    private int orderNumber;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName( String officeName ) {
        this.officeName = officeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax( String fax ) {
        this.fax = fax;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial( String initial ) {
        this.initial = initial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix( String suffix ) {
        this.suffix = suffix;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber( int orderNumber ) {
        this.orderNumber = orderNumber;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return StringUtils.isBlank( title ) &&
                StringUtils.isBlank(firstName ) &&
                StringUtils.isBlank(initial ) &&
                StringUtils.isBlank(lastName ) &&
                StringUtils.isBlank(suffix ) &&
                StringUtils.isBlank(email ) &&
                StringUtils.isBlank(phone ) &&
                StringUtils.isBlank(fax );
    }

    public void updateFrom( Officer officer ) {
        officeName = officer.getOfficeName();
        title = officer.getTitle();
        firstName = officer.getFirstName();
        initial = officer.getInitial();
        lastName = officer.getLastName();
        suffix = officer.getSuffix();
        email = officer.getEmail();
        phone = officer.getPhone();
        fax = officer.getFax();
    }

    public void clear() {
        officeName = "";
        title = "";
        firstName = "";
        initial = "";
        lastName = "";
        suffix = "";
        email = "";
        phone = "";
        fax = "";
    }

    public boolean checkEqual( Officer officer ) {
        return officeName.equalsIgnoreCase( officer.getOfficeName() ) &&
                title.equalsIgnoreCase( officer.getTitle() ) &&
                firstName.equalsIgnoreCase( officer.getFirstName() ) &&
                initial.equalsIgnoreCase( officer.getInitial() ) &&
                lastName.equalsIgnoreCase( officer.getLastName() ) &&
                suffix.equalsIgnoreCase( officer.getSuffix() ) &&
                email.equalsIgnoreCase( officer.getEmail() ) &&
                phone.equalsIgnoreCase( officer.getPhone() ) &&
                fax.equalsIgnoreCase( officer.getFax() );
    }
}
