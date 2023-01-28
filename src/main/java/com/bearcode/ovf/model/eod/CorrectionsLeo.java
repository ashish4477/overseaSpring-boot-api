package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.OverseasUser;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 6, 2007
 * Time: 6:26:38 PM
 */
public class CorrectionsLeo extends AbstractLocalOfficial {

    public static final String ALL_CORRECT_MESSAGE = "This data record is correct; no updates are needed.";

    public static final int STATUS_SENT = 1;
    public static final int STATUS_ACCEPTED = 2;
    public static final int STATUS_DECLINED = 3;
    private static final long serialVersionUID = 7876607647169328417L;

    private LocalOfficial correctionFor;

    private String message = "";
    private String submitterName = "";
    private String submitterEmail = "";
    private String submitterPhone = "";
    private Date created;
    private OverseasUser editor;
    private Date updated;

    private int allCorrect = 0;

    private List<CorrectionAdditionalAddress> additionalAddresses;

    public CorrectionsLeo() {
        super();
        created = new Date();
        setStatus( STATUS_SENT );

        additionalAddresses = new LinkedList<CorrectionAdditionalAddress>();
    }


    public CorrectionsLeo( LocalOfficial correctionFor ) {
        super();
        created = new Date();
        this.correctionFor = correctionFor;
        setStatus( STATUS_SENT );
        additionalAddresses = new LinkedList<CorrectionAdditionalAddress>();
        populateCorrections();
    }

    public void populateCorrections() {
        if ( correctionFor == null ) return;

        setWebsite( correctionFor.getWebsite() );
        setDsnPhone( correctionFor.getDsnPhone() );
        setGeneralEmail( correctionFor.getGeneralEmail() );

        setFurtherInstruction( correctionFor.getFurtherInstruction() );

        getMailing().updateFrom( correctionFor.getMailing() );
        getPhysical().updateFrom( correctionFor.getPhysical() );
        setHours( correctionFor.getHours() );

//        int size = Math.max( correctionFor.getOfficers().size(), 5 );  // 5 - another magic number
        int size = correctionFor.getOfficers().size() + 1;
        for ( int i = 0; i < size; i++ ) {
            Officer officer =  new Officer();
            if ( i < this.getOfficers().size() ) {
                officer = this.getOfficers().get( i );
            }
            else {
                officer.setOrderNumber( i + 1 );
                this.getOfficers().add( officer );
            }
            if ( i < correctionFor.getOfficers().size() ) {
                officer.updateFrom( correctionFor.getOfficers().get( i ) );
                officer.setOrderNumber( correctionFor.getOfficers().get( i ).getOrderNumber() );
            }
        }
        int addressesSize = correctionFor.getAdditionalAddresses().size() + 1;
        for ( int j = 0; j < addressesSize; j++ ) {
            CorrectionAdditionalAddress correctionAdditionalAddress;
            if ( j < this.getAdditionalAddresses().size() ) {
                correctionAdditionalAddress = getAdditionalAddresses().get( j );
            }
            else {
                correctionAdditionalAddress = new CorrectionAdditionalAddress();
                getAdditionalAddresses().add( correctionAdditionalAddress );
            }
            if ( j < correctionFor.getAdditionalAddresses().size() ) {
                correctionAdditionalAddress.updateFrom( correctionFor.getAdditionalAddresses().get( j ) );
            }
        }
    }

    public LocalOfficial getCorrectionFor() {
        return correctionFor;
    }

    public void setCorrectionFor( LocalOfficial correctionFor ) {
        this.correctionFor = correctionFor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated( Date created ) {
        this.created = created;
    }

    public OverseasUser getEditor() {
        return editor;
    }

    public void setEditor( OverseasUser editor ) {
        this.editor = editor;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated( Date updated ) {
        this.updated = updated;
    }

    public boolean checkEmpty() {
        if ( correctionFor == null ) return true;  // is empty if there is no object for correction
        boolean status = true;
        for ( int i = 0; i < getOfficers().size(); i++ ) {
            if ( i < correctionFor.getOfficers().size() ) {
                status = status && this.getOfficers().get( i ).checkEqual( correctionFor.getOfficers().get( i ) );
            }
            else {
                status = status && this.getOfficers().get( i ).isEmpty();
            }
        }
        for ( int j = 0; j < getAdditionalAddresses().size(); j++ ) {
            if ( j < correctionFor.getAdditionalAddresses().size() ) {
                status = status &&
                        getAdditionalAddresses().get( j ).checkEqual( correctionFor.getAdditionalAddresses().get( j ));
            }
            else {
                status = status && getAdditionalAddresses().get( j ).isEmpty();
            }
        }
        return status &&
                getWebsite().equalsIgnoreCase( correctionFor.getWebsite() ) &&
                getGeneralEmail().equalsIgnoreCase( correctionFor.getGeneralEmail() ) &&
                getDsnPhone().equalsIgnoreCase( correctionFor.getDsnPhone() ) &&
                getFurtherInstruction().equalsIgnoreCase( correctionFor.getFurtherInstruction() ) &&
                getMailing() != null && getMailing().checkEqual( correctionFor.getMailing() ) &&
                getPhysical() != null && getPhysical().checkEqual( correctionFor.getPhysical() ) &&
                getHours().equalsIgnoreCase( correctionFor.getHours() );
    }

    public boolean checkSubmitterEmpty() {
        return getSubmitterName().isEmpty() ||
                getSubmitterEmail().isEmpty() ||
                getSubmitterPhone().isEmpty();
    }


    public String getSubmitterName() {
        return submitterName;
    }


    public void setSubmitterName( String submitterName ) {
        this.submitterName = submitterName;
    }


    public String getSubmitterEmail() {
        return submitterEmail;
    }


    public void setSubmitterEmail( String submitterEmail ) {
        this.submitterEmail = submitterEmail;
    }


    public String getSubmitterPhone() {
        return submitterPhone;
    }


    public void setSubmitterPhone( String submitterPhone ) {
        this.submitterPhone = submitterPhone;
    }

    public int getAllCorrect() {
        if ( checkMessageForAllCorrect() ) {
            allCorrect = 1;
        }
        return allCorrect;
    }

    public void setAllCorrect( int allCorrect ) {
        this.allCorrect = allCorrect;
    }

    public boolean checkMessageForAllCorrect() {
        return message.contains( ALL_CORRECT_MESSAGE );
    }

    public void updateMessageAllCorrect() {
        if ( message.isEmpty() ) {
            message = new String( ALL_CORRECT_MESSAGE );
        }
        else {
            message += "\n" + ALL_CORRECT_MESSAGE;
        }
    }

    public List<CorrectionAdditionalAddress> getAdditionalAddresses() {
        return additionalAddresses;
    }

    public void setAdditionalAddresses( List<CorrectionAdditionalAddress> additionalAddresses ) {
        this.additionalAddresses = additionalAddresses;
    }
}
