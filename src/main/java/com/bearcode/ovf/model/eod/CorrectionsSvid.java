package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.OverseasUser;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
public class CorrectionsSvid extends AbstractStateSpecificDirectory {

    public static final int STATUS_SENT = 1;
    public static final int STATUS_ACCEPTED = 2;
    public static final int STATUS_DECLINED = 3;

    private StateSpecificDirectory correctionFor;

    private String message = "";
    private Date created;
    private OverseasUser editor;
    private int status;

    public CorrectionsSvid() {
        super();
        created = new Date();
        setStatus( STATUS_SENT );
    }

    public CorrectionsSvid( StateSpecificDirectory correctionFor ) {
        super();
        created = new Date();
        setStatus( STATUS_SENT );
        this.correctionFor = correctionFor;
        populateCorrections();
    }

    public StateSpecificDirectory getCorrectionFor() {
        return correctionFor;
    }

    public void setCorrectionFor( StateSpecificDirectory correctionFor ) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus( int status ) {
        this.status = status;
    }

    private void populateCorrections() {
        if ( correctionFor == null ) return;
        getStateContact().updateFrom( correctionFor.getStateContact() );
        setStatePhone( correctionFor.getStatePhone() );
        setStateEmail( correctionFor.getStateEmail() );
        setStateFax( correctionFor.getStateFax() );
        setWebsite( correctionFor.getWebsite() );

        setConfirmationOrStatus( correctionFor.getConfirmationOrStatus() );

        getCitizenRegistration().updateFrom( correctionFor.getCitizenRegistration() );
        getCitizenBallotRequest().updateFrom( correctionFor.getCitizenBallotRequest() );
        getCitizenBlankBallot().updateFrom( correctionFor.getCitizenBlankBallot() );
        getCitizenBallotReturn().updateFrom( correctionFor.getCitizenBallotReturn() );
        getMilitaryRegistration().updateFrom( correctionFor.getMilitaryRegistration() );
        getMilitaryBallotRequest().updateFrom( correctionFor.getMilitaryBallotRequest() );
        getMilitaryBlankBallot().updateFrom( correctionFor.getMilitaryBlankBallot() );
        getMilitaryBallotReturn().updateFrom( correctionFor.getMilitaryBallotReturn() );

        getPhysical().updateFrom( correctionFor.getPhysical() );
        getMailing().updateFrom( correctionFor.getMailing() );
    }

    public boolean checkEmpty() {
        if (
                correctionFor != null
                        && getStateContact() != null && getStateContact().checkEquals( correctionFor.getStateContact() )
                        && getStatePhone().equalsIgnoreCase( correctionFor.getStatePhone() )
                        && getStateEmail().equalsIgnoreCase( correctionFor.getStateEmail() )
                        && getConfirmationOrStatus().equalsIgnoreCase( correctionFor.getConfirmationOrStatus() )
                        //&& getRegistrationDeadline().equals( correctionFor.getRegistrationDeadline() )
                        && getCitizenRegistration().checkEmpty( correctionFor.getCitizenRegistration() )
                        && getCitizenBallotRequest().checkEmpty( correctionFor.getCitizenBallotRequest() )
                        && getCitizenBlankBallot().checkEmpty( correctionFor.getCitizenBlankBallot() )
                        && getCitizenBallotReturn().checkEmpty( correctionFor.getCitizenBallotReturn() )
                        && getMilitaryRegistration().checkEmpty( correctionFor.getMilitaryRegistration() )
                        && getMilitaryBallotRequest().checkEmpty( correctionFor.getMilitaryBallotRequest() )
                        && getMilitaryBlankBallot().checkEmpty( correctionFor.getMilitaryBlankBallot() )
                        && getMilitaryBallotReturn().checkEmpty( correctionFor.getMilitaryBallotReturn() )
                        && getPhysical() != null && getPhysical().checkEqual( correctionFor.getPhysical() )
                        && getMailing() != null && getMailing().checkEqual( correctionFor.getMailing() )
                        && getStateFax().equalsIgnoreCase( correctionFor.getStateFax() )
                        && getWebsite().equalsIgnoreCase( correctionFor.getWebsite() )

                ) {
            return true;
        }
        return false;
    }
}
