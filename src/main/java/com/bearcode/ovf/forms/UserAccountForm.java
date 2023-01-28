package com.bearcode.ovf.forms;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 9, 2007
 * Time: 6:24:10 PM
 *
 * @author Leonid Ginzburg
 */
public class UserAccountForm {
    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String newPass = "";
    private String confirmPass = "";
    private boolean correct = true;
    private boolean doLogin = false;
    private String redirect;

    private String phone = "";
    private Address currentAddress;
    private Address votingAddress;
    private Address forwardingAddress;

    private VoterType voterType;
    @Deprecated
    private VotingRegion votingRegion;
    private String eodRegionId = "";
    private String ballotPref = "";

    private int birthYear;
    private int birthMonth;

    private String race = "";
    private String ethnicity = "";
    private String gender = "";

    public UserAccountForm() {
    }

    public UserAccountForm( OverseasUser user ) {
        username = user.getUsername();
        firstName = user.getName().getFirstName();
        lastName = user.getName().getLastName();
        phone = user.getPhone();

        currentAddress = user.getCurrentAddress();
        votingAddress = user.getVotingAddress();
        forwardingAddress = user.getForwardingAddress();

        voterType = user.getVoterType();
        //votingRegion = user.getVotingRegion();
        eodRegionId = user.getEodRegionId();
        ballotPref = user.getBallotPref();

        birthYear = user.getBirthYear();
        birthMonth = user.getBirthMonth();

        race = user.getRace();
        ethnicity = user.getEthnicity();
        gender = user.getGender();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress( Address currentAddress ) {
        this.currentAddress = currentAddress;
    }

    public Address getVotingAddress() {
        return votingAddress;
    }

    public void setVotingAddress( Address votingAddress ) {
        this.votingAddress = votingAddress;
    }

    public Address getForwardingAddress() {
        return forwardingAddress;
    }

    public void setForwardingAddress( Address forwardingAddress ) {
        this.forwardingAddress = forwardingAddress;
    }

    public VoterType getVoterType() {
        return voterType;
    }

    public void setVoterType( VoterType voterType ) {
        this.voterType = voterType;
    }

    public String getBallotPref() {
        return ballotPref;
    }

    public void setBallotPref( String ballotPref ) {
        this.ballotPref = ballotPref;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear( int birthYear ) {
        this.birthYear = birthYear;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth( int birthMonth ) {
        this.birthMonth = birthMonth;
    }

    public String getRace() {
        return race;
    }

    public void setRace( String race ) {
        this.race = race;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity( String ethnicity ) {
        this.ethnicity = ethnicity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender( String gender ) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass( String newPass ) {
        this.newPass = newPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass( String confirmPass ) {
        this.confirmPass = confirmPass;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect( boolean correct ) {
        this.correct = correct;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect( String redirect ) {
        this.redirect = redirect;
    }

    public boolean isDoLogin() {
        return doLogin;
    }

    public void setDoLogin( boolean doLogin ) {
        this.doLogin = doLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    @Deprecated
    public VotingRegion getVotingRegion() {
        return votingRegion;
    }

    @Deprecated
    public void setVotingRegion( VotingRegion votingRegion ) {
        this.votingRegion = votingRegion;
    }

    public String getEodRegionId() {
        return eodRegionId;
    }

    public void setEodRegionId( String eodRegionId ) {
        this.eodRegionId = eodRegionId;
    }
}
