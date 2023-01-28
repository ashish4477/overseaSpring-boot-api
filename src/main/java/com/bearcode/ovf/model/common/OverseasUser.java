package com.bearcode.ovf.model.common;

import com.bearcode.ovf.model.questionnaire.WizardResults;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 5, 2007
 * Time: 9:17:16 PM
 *
 * @author Leonid Ginzburg
 */
public class OverseasUser implements UserDetails, Serializable {

    private static final long serialVersionUID = -6717210205587894363L;
    private long id;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String confirmPassword;
    @JsonIgnore
    private String scytlPassword;
    private boolean enabled = true;  //TODO: change it! Make a field in DB
    private boolean scytlIntegration = false;
    private boolean facebookIntegration = false;
    private Date lastUpdated = new Date();
    private Date created = new Date();

    private Person name = new Person();
    private Person previousName = new Person();

    private String alternateEmail = "";
    private String phone = "";
    private String alternatePhone = "";
    private String phoneType = "";
    private String alternatePhoneType = "";

    private Collection<UserRole> roles;
    private Collection<FaceConfig> faces;

    private UserAddress currentAddress;
    private UserAddress votingAddress;
    private UserAddress forwardingAddress;
    private UserAddress previousAddress;

    @Deprecated
    private VotingRegion votingRegion;

    /**
     * region ID in external EOD-API service
     */
    private String eodRegionId;
    private String eodRegionName;

    private VoterType voterType;
    private VoterClassificationType voterClassificationType;
    private VoterHistory voterHistory;
    private String ballotPref;

    private int birthYear;
    private int birthMonth;
    private int birthDate;

    private String race;
    private String ethnicity;
    private String gender;
    private String party;

    private String faceName = "";
    private boolean optIn;

    public static final String REMOVED_FIELD_PATTERN = "--REMOVED:";
    public static final String DISABLED_PASSWORD = "no password";
    private boolean emailOptOut;
    private boolean voterAlertOnly;


    public OverseasUser() {
    }

    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        for ( UserRole r : roles ) {
            auths.add( r );
        }
        return auths;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username.trim();
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }


    public Collection<FaceConfig> getFaces() {
        return faces;
    }

    public void setFaces( Collection<FaceConfig> faces ) {
        this.faces = faces;
    }

    public FaceConfig getAssignedFace() {
        if ( faces != null && faces.size() > 0 ) {
            return faces.iterator().next();
        }
        return null;
    }

    public void setAssignedFace( FaceConfig faceConfig ) {
        if ( faces == null ) {
            faces = new LinkedList<FaceConfig>();
        } else {
            faces.clear();
        }
        faces.add( faceConfig );
    }

    /**
     * Should implement this method for compatibility with Asegi.
     *
     * @return true because OVF doesn't have such functionality
     */
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Should implement this method for compatibility with Asegi.
     *
     * @return true because OVF doesn't have such functionality
     */
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Should implement this method for compatibility with Asegi.
     *
     * @return true because OVF doesn't have such functionality
     */
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }


    public Collection<UserRole> getRoles() {
        return roles;
    }

    public void setRoles( Collection<UserRole> roles ) {
        this.roles = roles;
    }

    public boolean isScytlIntegration() {
        return scytlIntegration;
    }

    public void setScytlIntegration( boolean scytlIntegration ) {
        this.scytlIntegration = scytlIntegration;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail( String alternateEmail ) {
        this.alternateEmail = alternateEmail.trim();
    }

    /**
     * Make a user account into an anonymous account, no longer accessible by the user
     */
    public void makeAnonymous() {
        if ( username == null ) {
            username = generatePassword(); // get a random string
        }
        username += Math.random() + (String) new Date().toString();
        try {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            md.update( username.getBytes() );
            BigInteger hash = new BigInteger( 1, md.digest() );
            username = hash.toString( 16 );
        } catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e );
        }
        Date removed = new Date();
        String removedDate = removed.toString();
        password = DISABLED_PASSWORD;
        scytlPassword = DISABLED_PASSWORD;
        name.makeAnonymous();
        previousName.makeAnonymous();

        phone = alternatePhone = alternateEmail = "";
        lastUpdated = removed;
        if ( votingAddress != null ) {
            votingAddress.makeAnonymous();
        }
        if ( currentAddress != null ) {
            currentAddress.makeAnonymous();
        }
        if ( forwardingAddress != null ) {
            forwardingAddress.makeAnonymous();
        }
        if ( forwardingAddress != null ) {
            forwardingAddress.makeAnonymous();
        }
        if ( previousAddress != null ) {
            previousAddress.makeAnonymous();
        }
    }

    public void populateFromWizardResults( WizardResults wizardResults ) {
        if ( wizardResults == null )
            return;

        this.username = wizardResults.getUsername().trim();
        this.name.updateFrom( wizardResults.getName() );
        this.previousName.updateFrom( wizardResults.getPreviousName() );

        this.phone = wizardResults.getPhone().trim();
        this.alternateEmail = wizardResults.getAlternateEmail().trim();
        this.alternatePhone = wizardResults.getAlternatePhone().trim();
        this.phoneType = wizardResults.getPhoneType().trim();
        this.alternatePhoneType = wizardResults.getAlternatePhoneType().trim();

/*
        VotingRegion region = wizardResults.getVotingRegion();
        if ( region != null ) {
            this.votingRegion = region;
        }
*/
        this.eodRegionId = wizardResults.getEodRegionId();

        String type = wizardResults.getVoterType();
        if ( type != null && type.length() > 0 ) {
            this.voterType = VoterType.valueOf( type );
        }

        String classificationType = wizardResults.getVoterClassificationType();
        if ( classificationType != null && classificationType.length() > 0 ) {
            this.voterClassificationType = VoterClassificationType.valueOf( classificationType );
        }

        String history = wizardResults.getVoterHistory();
        if ( history != null && history.length() > 0 ) {
            this.voterHistory = VoterHistory.valueOf( history );
        }

        this.ballotPref = wizardResults.getBallotPref();

        this.birthYear = wizardResults.getBirthYear();
        this.birthMonth = wizardResults.getBirthMonth();
        this.birthDate = wizardResults.getBirthDate();
        this.race = wizardResults.getRace();
        this.ethnicity = wizardResults.getEthnicity();
        this.gender = wizardResults.getGender();
        this.emailOptOut = wizardResults.isOptIn();

        votingAddress = populateAddress( votingAddress, wizardResults.getVotingAddress(), AddressType.STREET );
        currentAddress = populateAddress( currentAddress, wizardResults.getCurrentAddress(), AddressType.OVERSEAS );
        forwardingAddress = populateAddress( forwardingAddress, wizardResults.getForwardingAddress(), AddressType.OVERSEAS );
        previousAddress = populateAddress( previousAddress, wizardResults.getPreviousAddress(), AddressType.STREET );

    }

    private UserAddress populateAddress( UserAddress address, final UserAddress from, AddressType type ) {
        if ( address == null ) {
            address = new UserAddress( type );
        }
        address.updateFrom( from );
        if ( address.isEmptySpace() ) {
            // if empty, no need to save it
            address = null;
        }
        return address;
    }

    public static String encrypt( String password ) {
        try {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            md.update( password.getBytes() );
            BigInteger hash = new BigInteger( 1, md.digest() );
            String passwordHash = hash.toString( 16 );
            while ( passwordHash.length() < 32 ) passwordHash = "0" + passwordHash;
            return passwordHash;
        } catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e );
        }
    }

    public static String encryptScytl( final String password ) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        return passwordEncryptor.encryptPassword( password );
    }

    public static String generatePassword() {
        StringBuffer password = new StringBuffer();
        final int PASSWORD_LENGHT = 6;

        for ( int i = 0; i < PASSWORD_LENGHT; ) {
            char c = (char) (Math.random() * 255);
            if ( (c >= 'A' && c <= 'Z' && c != 'I' && c != 'O') || (c >= 'a' && c <= 'z' && c != 'l' && c != 'o') || (c >= '2' && c <= '9') ) {
                password.append( c );
                i++;
            }
        }
        if ( !password.toString().matches( ".*[0-9].*[0-9].*" ) ) {
            int[] ind = new int[2];
            ind[1] = 1 + (int) (Math.random() * PASSWORD_LENGHT - 2);
            ind[0] = (int) (Math.random() * (ind[1] - 1));
            for ( int i = 0; i < 2; i++ ) {
                char c = 0;
                while ( !(c >= '2' && c <= '9') ) {
                    c = (char) (Math.random() * 255);
                }
                password.setCharAt( ind[i], c );
            }
        }
        if ( !password.toString().matches( ".*[a-zA-Z].*[a-zA-Z].*" ) ) {
            int[] ind = new int[2];
            ind[1] = 1 + (int) (Math.random() * PASSWORD_LENGHT - 2);
            ind[0] = (int) (Math.random() * (ind[1] - 1));
            for ( int i = 0; i < 2; i++ ) {
                char c = 0;
                while ( !((c >= 'A' && c <= 'Z' && c != 'I' && c != 'O') || (c >= 'a' && c <= 'z' && c != 'l' && c != 'o')) ) {
                    c = (char) (Math.random() * 255);
                }
                password.setCharAt( ind[i], c );
            }
        }
        return password.toString();
    }

    public boolean isInRole( String shortRoleName ) {
        for ( UserRole role : roles ) {
            if ( role.getAuthority().equalsIgnoreCase( "role_" + shortRoleName ) )
                return true;
        }
        return false;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated ) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated( Date created ) {
        this.created = created;
    }

    @JsonIgnore
    public String getScytlPassword() {
        return scytlPassword;
    }

    public void setScytlPassword( String scytlPassword ) {
        this.scytlPassword = scytlPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone.trim();
    }

    public UserAddress getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress( UserAddress currentAddress ) {
        this.currentAddress = currentAddress;
    }

    public UserAddress getVotingAddress() {
        return votingAddress;
    }

    public void setVotingAddress( UserAddress votingAddress ) {
        this.votingAddress = votingAddress;
    }

    public UserAddress getForwardingAddress() {
        return forwardingAddress;
    }

    public void setForwardingAddress( UserAddress forwardingAddress ) {
        this.forwardingAddress = forwardingAddress;
    }

    public VoterType getVoterType() {
        return voterType;
    }

    public void setVoterType( VoterType voterType ) {
        this.voterType = voterType;
    }

    public VoterClassificationType getVoterClassificationType() {
        return voterClassificationType;
    }

    public void setVoterClassificationType( VoterClassificationType voterClassificationType ) {
        this.voterClassificationType = voterClassificationType;
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

    @Deprecated
    public VotingRegion getVotingRegion() {
        return votingRegion;
    }

    @Deprecated
    public void setVotingRegion( VotingRegion votingRegion ) {
        this.votingRegion = votingRegion;
    }

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName( String faceName ) {
        this.faceName = faceName;
    }

    @JsonIgnore
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword( String confirmPassword ) {
        this.confirmPassword = confirmPassword;
    }

    public VoterHistory getVoterHistory() {
        return voterHistory;
    }

    public void setVoterHistory( VoterHistory voterHistory ) {
        this.voterHistory = voterHistory;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate( int birthDate ) {
        this.birthDate = birthDate;
    }

    public Person getPreviousName() {
        return previousName;
    }

    public void setPreviousName( Person previousName ) {
        this.previousName = previousName;
    }

    public String getParty() {
        return party;
    }

    public void setParty( String party ) {
        this.party = party;
    }

    public boolean isFacebookIntegration() {
        return facebookIntegration;
    }

    public void setFacebookIntegration( boolean facebookIntegration ) {
        this.facebookIntegration = facebookIntegration;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone( String alternatePhone ) {
        this.alternatePhone = alternatePhone.trim();
    }

    public UserAddress getPreviousAddress() {
        return previousAddress;
    }

    public void setPreviousAddress( UserAddress previousAddress ) {
        this.previousAddress = previousAddress;
    }

    public Person getName() {
        return name;
    }

    public void setName( Person name ) {
        this.name = name;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType( String phoneType ) {
        this.phoneType = phoneType;
    }

    public String getAlternatePhoneType() {
        return alternatePhoneType;
    }

    public void setAlternatePhoneType( String alternatePhoneType ) {
        this.alternatePhoneType = alternatePhoneType;
    }

    public String getEodRegionId() {
        return eodRegionId;
    }

    public void setEodRegionId( String eodRegionId ) {
        this.eodRegionId = eodRegionId;
    }

    public boolean isOptIn() {
        return optIn;
    }

    public void setOptIn(boolean optIn) {
        this.optIn = optIn;
    }

    public boolean isEmailOptOut() {
        return emailOptOut;
    }

    public void setEmailOptOut(boolean emailOptOut) {
        this.emailOptOut = emailOptOut;
    }

    public boolean isVoterAlertOnly() {
        return voterAlertOnly;
    }

    public void setVoterAlertOnly(boolean voterAlertOnly) {
        this.voterAlertOnly = voterAlertOnly;
    }

    public String getEodRegionName() {
        return eodRegionName;
    }

    public void setEodRegionName(String eodRegionName) {
        this.eodRegionName = eodRegionName;
    }
}
