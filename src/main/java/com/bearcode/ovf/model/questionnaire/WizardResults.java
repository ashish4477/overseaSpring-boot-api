package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Nov 15, 2007 Time: 4:49:56 PM
 *
 * @author Leonid Ginzburg
 */
public class WizardResults {

    private long id;
    private Date creationDate;
    private Date lastChangedDate;
    private FlowType flowType;
    private String faceUrl = "";
    private String currentPageTitle = "";
    private OverseasUser user;

    private String username = "";

    private WizardResultPerson name = new WizardResultPerson();
    private WizardResultPerson previousName = new WizardResultPerson();

    private String phone = "";
    private String alternateEmail = "";
    private String alternatePhone = "";
    private String phoneType = "";
    private String alternatePhoneType = "";

    private WizardResultAddress currentAddress = new WizardResultAddress( AddressType.OVERSEAS );
    private WizardResultAddress votingAddress = new WizardResultAddress( AddressType.STREET );
    private WizardResultAddress forwardingAddress = new WizardResultAddress( AddressType.OVERSEAS );
    private WizardResultAddress previousAddress = new WizardResultAddress( AddressType.STREET );

    @Deprecated
    private VotingRegion votingRegion;
    /**
     * region ID in external EOD-API service
     */
    private String eodRegionId = "";

    // Keep voting region details separately in case the VotingRegion is updated or deleted
    private String votingRegionName = "";
    private String votingRegionState = "";

    private String voterType = "";
    private String voterClassificationType = "";
    private String voterHistory = "";
    private String ballotPref = "";

    private int birthDate;
    private int birthYear;
    private int birthMonth;

    private String race = "";
    private String ethnicity = "";
    private String gender = "";
    private String party = "";

    private boolean downloaded;
    private boolean emailSent;

    private boolean reportable = true;

    private boolean mobile = false;
    private String mobileDeviceType = "";
    private boolean optIn;
    private String url;
    private String uuid;
    
    public static String REMOVED_ANSWER_PATTERN = "--REMOVED:";

    /**
     * Number of pages in current flow. This is session dependent storage for the number to avoid multiple SQL questions in
     * SurveyWizard
     */
    private int pageCount;

    private final Map<Long, Answer> cachedAnswers = new LinkedHashMap<Long, Answer>();

    public WizardResults( final FlowType flowType ) {
        this.flowType = flowType;
        lastChangedDate = creationDate = new Date();
    }

    /**
     * Required by Hibernate for hydration
     */
    protected WizardResults() {
    }

    /**
     * Truncates fields and answers that may have personally identifiable information
     *
     * @return Collection of anonymized Answers instances
     */
    public Collection<Answer> anonymize() {

        username = phone = alternateEmail = alternatePhone = "";

        name.makeAnonymous();
        previousName.makeAnonymous();

        if ( votingAddress != null ) {
            votingAddress.anonymize();
        }
        if ( currentAddress != null ) {
            currentAddress.anonymize();
        }
        if ( forwardingAddress != null ) {
            forwardingAddress.anonymize();
        }
        if ( previousAddress != null ) {
            previousAddress.anonymize();
        }

        return anonymizeAnswers();
    }

    /**
     * Used together with anonymize(). Working with users with no account we don't want to save personally identifiable information,
     * but we need it for completing the form. We have to copy this info into temporary object and restore it after anonymized
     * WizardResults get saved. We do not need to copy all object fields, it's enough to copy only those truncated in anonymize().
     *
     * @param orig results to temporary store
     */
    public void copyFromTemporary( final WizardResults orig ) {
        name.updateFrom( orig.getName() );
        previousName.updateFrom( orig.getPreviousName() );

        setUsername( orig.username );
        setPhone( orig.phone );
        setAlternateEmail( orig.alternateEmail );
        setAlternatePhone( orig.alternatePhone );

        if ( currentAddress == null ) {
            currentAddress = WizardResultAddress.create( orig.currentAddress );
        } else {
            currentAddress.updateFrom( orig.currentAddress );
        }
        if ( votingAddress == null ) {
            votingAddress = WizardResultAddress.create( orig.votingAddress );
        } else {
            votingAddress.updateFrom( orig.votingAddress );
        }
        if ( forwardingAddress == null ) {
            forwardingAddress = WizardResultAddress.create( orig.forwardingAddress );
        } else {
            forwardingAddress.updateFrom( orig.forwardingAddress );
        }
        if ( previousAddress == null ) {
            previousAddress = WizardResultAddress.create( orig.previousAddress );
        } else {
            previousAddress.updateFrom( orig.previousAddress );
        }

        for ( final Answer herAnswer : orig.cachedAnswers.values() ) {
            final Answer myAnswer = cachedAnswers.get( herAnswer.getField().getId() );
            if ( myAnswer == null ) {
                // I've not got it, clone and store
                final Answer ca = herAnswer.clone();
                ca.setWizardResults( this );
                cachedAnswers.put( ca.getField().getId(), ca );
            } else {
                // I've got it, just copy value if it's needed
                if ( !myAnswer.getValue().equals( herAnswer.getValue() ) ) {
                    myAnswer.update( herAnswer );  // using a.setValue( b.getValue() ) is not correct
                }
            }
        }
    }

    /**
     * @return Temporary object
     * @see WizardResults#copyFromTemporary
     */
    public WizardResults createTemporary() {
        final WizardResults temporary = new WizardResults( flowType );
        temporary.copyFromTemporary( this );
        return temporary;

    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public Collection<Answer> getAnswers() {
        return Collections.unmodifiableCollection( cachedAnswers.values() );
    }

    public Map<Long, Answer> getAnswersAsMap() {
        return cachedAnswers;
    }

    public String getBallotPref() {
        return ballotPref;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public WizardResultAddress getCurrentAddress() {
        return currentAddress;
    }

    public String getCurrentPageTitle() {
        return currentPageTitle;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public FlowType getFlowType() {
        return flowType;
    }

    public WizardResultAddress getForwardingAddress() {
        return forwardingAddress;
    }

    public String getGender() {
        return gender;
    }

    public long getId() {
        return id;
    }

    public Date getLastChangedDate() {
        return lastChangedDate;
    }

    /**
     * Gets the mobile device type.
     *
     * @return the mobile device type.
     * @author IanBrown
     * @version Apr 20, 2012
     * @since Apr 20, 2012
     */
    public String getMobileDeviceType() {
        return mobileDeviceType;
    }

    public WizardResultPerson getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phone;
    }

    public WizardResultAddress getPreviousAddress() {
        return previousAddress;
    }

    public WizardResultPerson getPreviousName() {
        return previousName;
    }

    public String getRace() {
        return race;
    }

    public OverseasUser getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getVoterHistory() {
        return voterHistory;
    }

    public String getVoterType() {
        return voterType;
    }

    public String getVoterClassificationType() {
        return voterClassificationType;
    }

    public WizardResultAddress getVotingAddress() {
        return votingAddress;
    }

    @Deprecated
    public VotingRegion getVotingRegion() {
        return votingRegion;
    }

    public String getVotingRegionName() {
        return votingRegionName;
    }

    public String getVotingRegionState() {
        return votingRegionState;
    }

    public boolean hasMailingListSignUp() {
        for ( final Answer answer : cachedAnswers.values() ) {
            if ( answer.getField().getType().isMailingListSignUp() &&
                    answer.getValue().equalsIgnoreCase( "true" ) ) {
                return true;
            }
        }
        return false;
    }

    public FieldType getFieldTypeIfSignUp() {
        for ( final Answer answer : cachedAnswers.values() ) {
            if ( answer.getField().getType().isMailingListSignUp() &&
                    answer.getValue().equalsIgnoreCase( "true" ) ) {
                return answer.getField().getType();
            }
        }
        return null;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    /**
     * Gets the mobile flag.
     *
     * @return the mobile flag.
     * @author IanBrown
     * @version Apr 20, 2012
     * @since Apr 20, 2012
     */
    public boolean isMobile() {
        return mobile;
    }

    /**
     * Gets the reportable flag.
     *
     * @return <code>true</code> if this record is reportable, <code>false</code> if it is not.
     * @author IanBrown
     * @version Apr 4, 2012
     * @since Apr 4, 2012
     */
    public boolean isReportable() {
        return reportable;
    }

    /**
     * Populates String properties from FaceConfig presets. Silently ignores any non-setter methods, non-existing setter methods and
     * setters that do not take a String parameter.
     *
     * @param config - a FaceConfig instance
     */
    public void populateFromFaceConfig( final FaceConfig config ) {

        final Map<String, String> presets = config.getPresetPdfAnswersFields();
        for ( final String fullName : presets.keySet() ) {
            Object currObject = this;
            for ( final String methodName : fullName.split( "\\." ) ) {
                if ( methodName.startsWith( "set" ) ) {
                    try {
                        final Class[] params = new Class[1];
                        params[0] = String.class;
                        final Method m = currObject.getClass().getMethod( methodName, params );
                        m.invoke( currObject, presets.get( fullName ) );
                    } catch ( final NoSuchMethodException e ) {
                        // ignore
                    } catch ( final InvocationTargetException e ) {
                        // ignore
                    } catch ( final IllegalAccessException e ) {
                        // ignore
                    }
                } else {
                    try { // invoke get method
                        final Class[] params = new Class[0];
                        final Method m = currObject.getClass().getMethod( methodName, params );
                        currObject = m.invoke( currObject );
                    } catch ( final NoSuchMethodException e ) {
                        // ignore
                    } catch ( final InvocationTargetException e ) {
                        // ignore
                    } catch ( final IllegalAccessException e ) {
                        // ignore
                    }
                }
            }
        }
    }

    public void populateFromUser( final OverseasUser user ) {
        if ( user == null ) {
            return;
        }

        this.username = user.getUsername();

        this.name.updateFrom( user.getName() );
        this.previousName.updateFrom( user.getPreviousName() );

        this.phone = user.getPhone();
        this.alternateEmail = user.getAlternateEmail();
        this.alternatePhone = user.getAlternatePhone();
        this.phoneType = user.getPhoneType();
        this.alternatePhoneType = user.getAlternatePhoneType();

         /*final VotingRegion region = user.getVotingRegion();
         if ( region != null ) {
             this.votingRegion = region;
             this.votingRegionName = votingRegion.getName();
             this.votingRegionState = votingRegion.getState().getAbbr();
         }*/
        this.eodRegionId = user.getEodRegionId();

        final VoterType type = user.getVoterType();
        if ( type != null ) {
            this.voterType = type.getName();
        }
        final VoterClassificationType voterClassificationType = user.getVoterClassificationType();
        if ( voterClassificationType != null ) {
            this.voterClassificationType = voterClassificationType.getName();
        }
        final VoterHistory history = user.getVoterHistory();
        if ( history != null ) {
            this.voterHistory = history.getName();
        }
        this.ballotPref = user.getBallotPref();

        this.birthDate = user.getBirthDate();
        this.birthYear = user.getBirthYear();
        this.birthMonth = user.getBirthMonth();
        this.race = user.getRace();
        this.ethnicity = user.getEthnicity();
        this.gender = user.getGender();
        this.party = user.getParty();
        this.optIn = user.isOptIn();

        final UserAddress voting = user.getVotingAddress();
        if ( voting != null ) {
            this.votingAddress = WizardResultAddress.create( voting );
        }

        final UserAddress current = user.getCurrentAddress();
        if ( current != null ) {
            this.currentAddress = WizardResultAddress.create( current );
        }

        final UserAddress forwarding = user.getForwardingAddress();
        if ( forwarding != null ) {
            this.forwardingAddress = WizardResultAddress.create( forwarding );
        }

        final UserAddress previous = user.getPreviousAddress();
        if ( previous != null ) {
            this.previousAddress = WizardResultAddress.create( previous );
        }
    }

    public void putAnswer( final Answer answer ) {
        if ( answer.getWizardResults() != null && answer.getWizardResults() != this ) {
            throw new IllegalArgumentException( "Trying to add Answer that already belongs to another WizardResults instance" );
        }
        answer.setWizardResults( this );
        cachedAnswers.put( answer.getField().getId(), answer.clone() );
    }

    public void setAlternateEmail( final String alternateEmail ) {
        this.alternateEmail = alternateEmail;
    }

    public void setAlternatePhone( final String alternatePhone ) {
        this.alternatePhone = alternatePhone;
    }

    public void setAnswers( final Collection<Answer> answers ) {
        cachedAnswers.clear();
        for ( final Answer answer : answers ) {
            final long fieldId = answer.getField().getId();
            if ( !cachedAnswers.containsKey( fieldId ) ) {
                cachedAnswers.put( fieldId, answer );
            } else {
                // throw new IllegalArgumentException( "Duplicated answer for answer with field.id=" + fieldId );
            }
        }
    }

    public void setBallotPref( final String ballotPref ) {
        this.ballotPref = ballotPref;
    }

    public void setBirthDate( final int birthDate ) {
        this.birthDate = birthDate;
    }

    public void setBirthMonth( final int birthMonth ) {
        this.birthMonth = birthMonth;
    }

    public void setBirthYear( final int birthYear ) {
        this.birthYear = birthYear;
    }

    public void setCreationDate( final Date creationDate ) {
        this.creationDate = creationDate;
    }

    public void setCurrentAddress( final WizardResultAddress currentAddress ) {
        this.currentAddress = currentAddress;
    }

    public void setCurrentPageTitle( final String currentPageTitle ) {
        this.currentPageTitle = currentPageTitle;
    }

    public void setDownloaded( final boolean downloaded ) {
        this.downloaded = downloaded;
    }

    public void setEmailSent( final boolean emailSent ) {
        this.emailSent = emailSent;
    }

    public void setEthnicity( final String ethnicity ) {
        this.ethnicity = ethnicity;
    }

    public void setFaceUrl( final String faceUrl ) {
        this.faceUrl = faceUrl;
    }

    public void setFlowType( final FlowType flowType ) {
        this.flowType = flowType;
    }

    public void setForwardingAddress( final WizardResultAddress forwardingAddress ) {
        this.forwardingAddress = forwardingAddress;
    }

    public void setGender( final String gender ) {
        this.gender = gender;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    public void setLastChangedDate( final Date date ) {
        this.lastChangedDate = date;
    }

    /**
     * Sets the mobile flag.
     *
     * @param mobile the mobile flag to set.
     * @author IanBrown
     * @version Apr 20, 2012
     * @since Apr 20, 2012
     */
    public void setMobile( final boolean mobile ) {
        this.mobile = mobile;
    }

    /**
     * Sets the mobile device type.
     *
     * @param mobileDeviceType the mobile device type to set.
     * @author IanBrown
     * @version Apr 20, 2012
     * @since Apr 20, 2012
     */
    public void setMobileDeviceType( final String mobileDeviceType ) {
        this.mobileDeviceType = mobileDeviceType;
    }

    public void setName( final WizardResultPerson name ) {
        this.name = name;
    }

    public void setParty( final String party ) {
        this.party = party;
    }

    public void setPhone( final String phone ) {
        this.phone = phone;
    }

    public void setPreviousAddress( final WizardResultAddress previousAddress ) {
        this.previousAddress = previousAddress;
    }

    public void setPreviousName( final WizardResultPerson previousName ) {
        this.previousName = previousName;
    }

    public void setRace( final String race ) {
        this.race = race;
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

    /**
     * Sets the reportable flag.
     *
     * @param reportable <code>true</code> if this record is reportable, <code>false</code> if it is not.
     * @author IanBrown
     * @version Apr 4, 2012
     * @since Apr 4, 2012
     */
    public void setReportable( final boolean reportable ) {
        this.reportable = reportable;
    }

    public void setUser( final OverseasUser user ) {
        this.user = user;

        populateFromUser( user );
    }

    public void setUsername( final String username ) {
        this.username = username;
    }

    public void setVoterHistory( final String voterHistory ) {
        this.voterHistory = voterHistory;
    }

    public void setVoterType( final String voterType ) {
        this.voterType = voterType;
    }

    public void setVoterClassificationType( final String voterClassificationType ) {
        this.voterClassificationType = voterClassificationType;
    }

    public void setVotingAddress( final WizardResultAddress votingAddress ) {
        this.votingAddress = votingAddress;
    }

    @Deprecated
    public void setVotingRegion( final VotingRegion votingRegion ) {
        this.votingRegion = votingRegion;
    }

    public void setVotingRegionName( final String votingRegionName ) {
        this.votingRegionName = votingRegionName;
    }

    public void setVotingRegionState( final String votingRegionState ) {
        this.votingRegionState = votingRegionState;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return Collection of anonymized Answers instances
     */
    private Collection<Answer> anonymizeAnswers() {
        final Collection<Answer> changedAnswers = new ArrayList<Answer>();
        final String removedDate = new Date().toString();

        for ( final Answer answer : cachedAnswers.values() ) {
            if ( !(answer instanceof EnteredAnswer) || answer.getValue() == null || answer.getValue().length() == 0 ) {
                continue;
            }

            final String templateName = answer.getField().getType().getTemplateName();
            if ( templateName.equals( FieldType.TEMPLATE_TEXT ) || templateName.equals( FieldType.TEMPLATE_TEXTAREA )
                    || templateName.equals( FieldType.TEMPLATE_TEXT_CONFIRM ) ) {

                answer.setValue( REMOVED_ANSWER_PATTERN + removedDate + "--" );
                changedAnswers.add( answer );
            }
        }
        return changedAnswers;

    }

    public boolean checkReportable() {
        return !(user != null && user.isInRole( UserRole.USER_ROLE_APPLICATION_TESTER )) &&
                !(username != null && (
                        username.equalsIgnoreCase( "test@bear-code.com" )
                                || username.equalsIgnoreCase( "test@overseasvotefoundation.org" )
                                || username.equalsIgnoreCase( "test@usvotefoundation.org" )
                )) &&
                !((flowType == FlowType.FWAB || flowType == FlowType.RAVA) &&
                        (voterType == null || voterType.trim().length() == 0)
                );
    }
}
