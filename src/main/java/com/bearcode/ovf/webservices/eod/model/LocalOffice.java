package com.bearcode.ovf.webservices.eod.model;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.Person;
import com.bearcode.ovf.model.eod.AbstractLocalOfficial;
import com.bearcode.ovf.model.eod.AdditionalAddress;
import com.bearcode.ovf.model.eod.Officer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonid on 12.04.17.
 */
public class LocalOffice extends AbstractLocalOfficial {

    private static final long serialVersionUID = -8976526191616730393L;

    private String email;
    private String notes;

    private EodRegion eodRegion;


    private List<EodAddress> addresses;

    public LocalOffice() {
        setStatus( 1 );
        setUpdated( new Date() );
        addresses = new LinkedList<EodAddress>();
    }


    @Override
    public String getGeneralEmail() {
        return email;
    }

    @Override
    public String getFurtherInstruction() {
        return notes;
    }


    public List<EodAddress> getAdditionalAddresses() {
        return addresses;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes( String notes ) {
        this.notes = notes;
    }

    public EodRegion getEodRegion() {
        return eodRegion;
    }

    public void setEodRegion( EodRegion eodRegion ) {
        this.eodRegion = eodRegion;
    }

    public EodRegion getRegion() {
        return eodRegion;
    }


    public void assignApiAddresses() {
        if ( addresses.size() == 1 ) {
            EodAddress address = addresses.get( 0 );
            address.getType().setName( "Postal Mail &amp; Express Mail Delivery" );
        }
        else if ( addresses.size() >= 2 ) {
            for ( EodAddress address : addresses ) {
                if ( address.isRegularMail() && !address.isPhysical() ) {
                    address.getType().setName( "Postal Mail" );
                }
                else if ( address.isPhysical() && !address.isRegularMail() ) {
                    address.getType().setName( "Express Mail Delivery" );
                }
                else if ( address.isPhysical() && address.isRegularMail()  ) {
                    address.getType().setName( "Postal Mail &amp; Express Mail Delivery" );
                }
            }
            Collections.sort( addresses );
        }

        for ( EodAddress address : addresses ) {
            address.setMainOfficer( findAddressContact( address ) );
        }
    }

    public boolean isAddressLabelCombined() {
        return addresses.size() > 2;
    }

    public List<EodAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses( List<EodAddress> addresses ) {
        this.addresses = addresses;
    }

    public Officer findPrimary() {
        if ( getOfficers() != null && !getOfficers().isEmpty() ) {
            for ( Officer officer : getOfficers() ) {
                if ( officer instanceof EodOfficer ) {
                    if ( ((EodOfficer)officer).isPrimary() ) {
                        return officer;
                    }
                }
            }
        }
        return null;
    }

    public Officer findSecondary() {
        if ( getOfficers() != null && !getOfficers().isEmpty() ) {
            for ( Officer officer : getOfficers() ) {
                if ( officer instanceof EodOfficer ) {
                    if ( ((EodOfficer)officer).isSecondary() ) {
                        return officer;
                    }
                }
            }
        }
        return null;
    }

    public Person getLeo() {
        Person person = new Person();
        Officer primary = findPrimary();
        if ( primary != null ) {
            person.setFirstName( primary.getFirstName() );
            person.setLastName( primary.getLastName() );
            person.setTitle( primary.getTitle() );
            person.setInitial( primary.getInitial() );
            person.setSuffix( primary.getSuffix() );
        }
        return person;
    }

    public String getLeoPhone() {
        Officer primary = findPrimary();
        return primary != null ? primary.getPhone() : "";
    }

    public String getLeoFax() {
        Officer primary = findPrimary();
        return primary != null ? primary.getFax() : "";
    }

    public String getLeoEmail() {
        Officer primary = findPrimary();
        return primary != null ? primary.getEmail() : "";
    }


    public Person getLovc() {
        Person person = new Person();
        Officer secondary = findSecondary();
        if ( secondary != null ) {
            person.setFirstName( secondary.getFirstName() );
            person.setLastName( secondary.getLastName() );
            person.setTitle( secondary.getTitle() );
            person.setInitial( secondary.getInitial() );
            person.setSuffix( secondary.getSuffix() );
        }
        return person;
    }

    public String getLovcPhone() {
        Officer secondary = findSecondary();
        return secondary != null ? secondary.getPhone() : "";
    }

    public String getLovcFax() {
        Officer secondary = findSecondary();
        return secondary != null ? secondary.getFax() : "";
    }

    public String getLovcEmail() {
        Officer secondary = findSecondary();
        return secondary != null ? secondary.getEmail() : "";
    }

    private Officer findAddressContact( EodAddress appropriateAddress ) {
        Pattern pattern = Pattern.compile( "\\d+$" );
        Matcher matcher = pattern.matcher( appropriateAddress.getPrimaryContactUri() );
        if ( matcher.find() ) {
            String strContactId = matcher.group();
            try {
                Long contactId = Long.parseLong( strContactId );
                for ( Officer contact : getOfficers() ) {
                    if ( contact.getId() == contactId ) {
                        return contact;
                    }
                }
            } catch (NumberFormatException e) {
                // nothing to do
            }
        }
        return null;
    }
}

