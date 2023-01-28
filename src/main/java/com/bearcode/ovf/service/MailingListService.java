package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.*;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.mail.*;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.validators.OverseasUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Jun 20, 2007 Time: 5:47:42 PM
 *
 * @author Leonid Ginzburg
 */
@Service
public class MailingListService {

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private MailingAddressDAO mailingAddressDAO;

    @Autowired
    private MailingListDAO mailingListDAO;

    @Autowired
    private MailingTemplateDAO mailingTemplateDAO;

    @Autowired
    private FaceConfigDAO faceConfigDAO;

    /**
     * Converts the mailing list to CSV.
     *
     * @param mailingAddresses the mailing list.
     * @return the CSV.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public String convertToCSV( final List<MailingAddress> mailingAddresses ) {
        final StringBuilder csv = new StringBuilder( "" );
        String prefix = "";

        for ( final MailingAddress mailingAddress : mailingAddresses ) {
            csv.append( prefix );
            prefix = "\n";
            addMailingAddressEntryToCSV( mailingAddress, csv );
        }

        return csv.toString();
    }

    /**
     * Finds all of the mailing list entries.
     *
     * @return the list of all mailing list entries.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public List<MailingAddress> findAllMailingAddress() {
        return getMailingAddressDAO().findAll();
    }

    public MailingAddress findByEmail( final String str ) {
        return getMailingAddressDAO().getByEmail( str );
    }

    /**
     * Finds the mailing list entries for the specified face.
     *
     * @param face the face.
     * @return the list of mailing list entries.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public List<MailingAddress> findByFace( final FaceConfig face ) {
        return getMailingAddressDAO().getByUrl( face.getUrlPath() );
    }

    //todo: wrong place for the method. move to State Service
    public State findStateByAbbreviation( final String state ) {
        return getStateDAO().getByAbbreviation( state );
    }

    /**
     * Gets the mailing list DAO.
     *
     * @return the mailing list DAO.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public MailingAddressDAO getMailingAddressDAO() {
        return mailingAddressDAO;
    }

    /**
     * Gets the state DAO.
     *
     * @return the state DAO.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public StateDAO getStateDAO() {
        return stateDAO;
    }

    public MailingListDAO getMailingListDAO() {
        return mailingListDAO;
    }

    public void setMailingListDAO( MailingListDAO mailingListDAO ) {
        this.mailingListDAO = mailingListDAO;
    }

    public void saveMailingAddress( final MailingAddress mailingAddress ) {
        getMailingAddressDAO().makePersistent( mailingAddress );
    }

    public synchronized void saveToMailingList( final WizardResults wizardResults ) {
        final FieldType fieldType = wizardResults.getFieldTypeIfSignUp();
        saveToMailingList( wizardResults, fieldType );
    }

    public synchronized void saveToMailingList( final WizardResults wizardResults, final MailingList mailingList ) {
        final String email = validateEmail( wizardResults.getUsername() );
        if ( email != null ) {
            MailingAddress signup = mailingAddressDAO.getByEmail( email );
            if ( signup == null ) {
                signup = new MailingAddress();
                signup.setEmail( email );
            }

            signup.setFirstName( wizardResults.getName().getFirstName() );
            signup.setLastName( wizardResults.getName().getLastName() );
            signup.setBirthYear( wizardResults.getBirthYear() );
            signup.setVoterType( wizardResults.getVoterType() );
            signup.setPhone( wizardResults.getPhone() );

            signup.setUrl( wizardResults.getFaceUrl() );

            final WizardResultAddress votingAddress = wizardResults.getVotingAddress();
            signup.setVotingCity( votingAddress != null ? votingAddress.getCity() : "" );
            signup.setVotingStateName( votingAddress != null ? votingAddress.getState() : "" );
            signup.setVotingPostalCode( votingAddress != null ? votingAddress.getZip() + "-" + votingAddress.getZip4() : "" );
            /*signup.setRegion( wizardResults.getVotingRegion() );
            if ( wizardResults.getVotingRegion() != null ) {
                signup.setState( wizardResults.getVotingRegion().getState() );
            }*/
            signup.setEodRegion( wizardResults.getEodRegionId() );
            signup.setVotingRegionName( wizardResults.getVotingRegionName() );
            signup.setState( stateDAO.getByAbbreviation( wizardResults.getVotingRegionState() ) );

            final WizardResultAddress currentAddress = wizardResults.getCurrentAddress();
            signup.setCurrentAddress( currentAddress != null ? currentAddress.getStreet1() + " " + currentAddress.getStreet2() : "" );
            signup.setCurrentCity( currentAddress != null ? currentAddress.getCity() : "" );
            signup.setCurrentPostalCode( currentAddress != null ? currentAddress.getZip() + "-" + currentAddress.getZip4() : "" );
            signup.setCurrentCountryName( currentAddress != null ? currentAddress.getCountry() : "" );

            mailingAddressDAO.makePersistent( signup );

            if ( mailingList != null ) {
                MailingLink link = mailingListDAO.findLinkByListAndAddress( mailingList, signup );
                if ( link == null ) {
                    link = new MailingLink();
                    link.setMailingAddress( signup );
                    link.setMailingList( mailingList );
                    link.setStatus( MailingLinkStatus.NEW );
                }
                else {
                    switch ( link.getStatus() ) {
                        case UNSUBSCRIBED:
                            link.setStatus( MailingLinkStatus.RESTORED );
                            break;
                        case ERROR:
                            link.setStatus( MailingLinkStatus.NEW );
                            link.setErrorCount( 0 );
                            break;
                        case SUBSCRIBED:
                            link.setStatus( MailingLinkStatus.UPDATED );
                            break;
                    }
                }
                mailingListDAO.makePersistent( link );
            }
        }
    }

    public synchronized void saveToMailingList( final WizardResults wizardResults, final FieldType fieldType ) {
        if ( fieldType != null ) {
            MailingList mailingList = mailingListDAO.findByFieldType( fieldType );
            saveToMailingList( wizardResults, mailingList );
        }
    }

    public void saveToMailingListIfHasSignup( final WizardResults wizardResults ) {
        saveToMailingList( wizardResults/*, false*/ );
    }


    /**
     * Sets the mailing list DAO.
     *
     * @param mailingAddressDAO the mailing list DAO to set.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public void setMailingAddressDAO( final MailingAddressDAO mailingAddressDAO ) {
        this.mailingAddressDAO = mailingAddressDAO;
    }

    /**
     * Sets the state DAO.
     *
     * @param stateDAO the state DAO to set.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    public void setStateDAO( final StateDAO stateDAO ) {
        this.stateDAO = stateDAO;
    }

    /**
     * Adds the values from the mailing list entry to the CSV builder.
     *
     * @param mailingAddress the mailing list entry.
     * @param csv              the string builder used to create the CSV.
     * @author IanBrown
     * @version Apr 19, 2012
     * @since Apr 19, 2012
     */
    private void addMailingAddressEntryToCSV( final MailingAddress mailingAddress, final StringBuilder csv ) {
        String stateName = "";
        if ( mailingAddress.getState() != null ) {
            stateName = mailingAddress.getState().getAbbr();
        } else if ( !mailingAddress.getVotingStateName().isEmpty() ) {
            stateName = mailingAddress.getVotingStateName();
        } else if ( mailingAddress.getRegion() != null ) {
            stateName = mailingAddress.getRegion().getState().getAbbr();
        }
        csv.append( "\"" ).append( mailingAddress.getEmail() ).append( "\",\"" ).append( mailingAddress.getFirstName() )
                .append( "\",\"" ).append( mailingAddress.getLastName() ).append( "\",\"" )
                .append( mailingAddress.getRegion() == null ? "" : mailingAddress.getRegion().getName() )
                .append( "\",\"" )
                .append( stateName )
                .append( "\"" );
    }

    /**
     * Returns the given string if it looks like a valid email address otherwise returns null
     *
     * @param email tested email
     * @return email validated email or null
     */
    private String validateEmail( final String email ) {

        if ( email != null && email.matches( OverseasUserValidator.USERNAME_PATTERN ) ) {
            return email;
        }
        return null;
    }

    public List<MailingList> findAllMailingLists() {
        return mailingListDAO.findAll();
    }

    public MailingList findMailingList( Long id ) {
        return mailingListDAO.findById( id );
    }

    public void saveMailingList( MailingList mailingList ) {
        mailingListDAO.makePersistent( mailingList );
    }

    public List<MailingLink> findNewMailingLinks() {
        return mailingListDAO.findNewLinks();
    }

    public List<MailingLink> findMailingLinksByStatus( MailingLinkStatus status, int limit ) {
        return mailingListDAO.findLinksByStatus( status, limit );
    }


    public synchronized void updateMailingLink( MailingLink link ) {
        mailingListDAO.makePersistent( link );
    }

    public List<String> findMailingApiKeys() {
        return mailingListDAO.findMailingApiKeys();
    }

    public MailingLink findLinkByCompaignAndEmail( String apiKey, String campaignId, String email ) {
        return mailingListDAO.findLinkByCompaignAndEmail( apiKey, campaignId, email );
    }

    public List<LocalOfficial> findLeoForMailing( MailingList list, int maxResults ) {
        return mailingListDAO.findLeoForMailing( list, maxResults );
    }

    public List<LocalOfficial> findLovcForMailing( MailingList list, int maxResults ) {
        return mailingListDAO.findLovcForMailing( list, maxResults );
    }

    public MailingLink findLinkByListAndAddress( MailingList list, MailingAddress address ) {
        return mailingListDAO.findLinkByListAndAddress( list, address );
    }

    public MailingLink findLinkByListIdAndEmail( Long listId, String email ) {
        return mailingListDAO.findLinkByListAndEmail( listId, email );
    }

    public void saveMigrations( Collection<MailingList> mailingListsToDelete, Collection<MailingList> mailingListsToPersist ) {
        mailingListDAO.removeAllLinks( mailingListsToDelete );
        mailingListDAO.makeAllTransient( mailingListsToDelete );
        mailingListDAO.makeAllPersistent( mailingListsToPersist );
    }

    public Long countLostAddresses() {
        return mailingAddressDAO.countLostAddresses();
    }

    public List<MailingAddress> findLostAddresses( String faceUrl, String state ) {
        return mailingAddressDAO.findLostAddresses( faceUrl, state );
    }

    public Map<String, Map<String,Long>> findFacesOfLostAddresses( String faceUrl ) {
        List result = mailingAddressDAO.findFacesOfLostAddresses( faceUrl );
        String currentFace = null;
        Map<String, Map<String,Long>> faces = new HashMap<String, Map<String, Long>>();
        Map<String,Long> states = null;
        for ( Object row : result ) {
            if ( row instanceof Object[] ) {
                String face = (String) ((Object[])row)[0];
                String state = (String) ((Object[])row)[1];
                Long count = (Long) ((Object[])row)[2];

                if ( !face.equalsIgnoreCase( currentFace ) ) {
                    currentFace = face;
                    states = new HashMap<String, Long>();
                    faces.put( face, states );
                }
                if ( states != null ) {
                    states.put( state, count );
                }
            }
        }
        return faces;
    }

    public void saveMailingLink( List<MailingLink> createdLinks ) {
        mailingListDAO.makeAllPersistent( createdLinks );
    }

    public MailTemplate findTemplate( Long id ) {
        return mailingTemplateDAO.findById( id );
    }

    public List<MailTemplate> findAllTemplates() {
        return mailingTemplateDAO.findAllTemplates();
    }

    public void saveTemplate( MailTemplate template ) {
        mailingTemplateDAO.makePersistent( template );
    }

    public Map<Long, Long> findSubscribersCount() {
        return mailingLinkCount( MailingLinkStatus.SUBSCRIBED, MailingLinkStatus.NEW );
    }

    public Map<Long, Long> findUnsubscribersCount() {
        return mailingLinkCount( MailingLinkStatus.UNSUBSCRIBED );
    }

    public Map<Long, Long> findErrorsCount() {
        return mailingLinkCount( MailingLinkStatus.ERROR );
    }

    private Map<Long, Long> mailingLinkCount( MailingLinkStatus... statuses) {
        List result = mailingListDAO.countLinks( statuses );
        Map<Long, Long> count = new HashMap<Long, Long>();
        for ( Object row : result ) {
            if ( row instanceof Object[] ) {
                Long mailingListId = (Long) ((Object[])row)[0];
                Long number = (Long) ((Object[])row)[1];
                count.put( mailingListId, number );
            }
        }
        return count;
    }

    public void saveMailingTask( MailingTask task ) {
        mailingListDAO.makePersistent( task );
    }

    public List<MailingLink> findMailingLinks( MailingList mailingList, long offset, long pageSize ) {
        return mailingListDAO.findLinks( mailingList, offset, pageSize  );
    }

    public List<MailingTask> findActiveTasks() {
        return mailingListDAO.findTasks();
    }

    public void updateBounces( String... email) {
        List<String> emails = Arrays.<String>asList( email );
        List<MailingLink> links = mailingListDAO.findLinksByEmails( emails );
        for ( MailingLink link : links ) {
            link.setStatus( MailingLinkStatus.ERROR );
        }
        mailingListDAO.makeAllPersistent( links );
    }

    public void updateUnsubscribes( List<String> emails ) {
        if ( emails != null && !emails.isEmpty() ) {
            List<MailingLink> links = mailingListDAO.findLinksByEmails( emails );
            for ( MailingLink link : links ) {
                link.setStatus( MailingLinkStatus.UNSUBSCRIBED );
            }
            mailingListDAO.makeAllPersistent( links );
        }
    }

    public List<MailingTask> findAllTasks() {
        return mailingListDAO.findAllTasks();
    }

    public MailingTask findMailingTaskById( Long taskId ) {
        return mailingListDAO.findMailingTaskById( taskId );
    }

    public void saveSemaphore( MailingSemaphore semaphore ) {
        mailingListDAO.makePersistent( semaphore );
    }

    public MailingSemaphore findSemaphore() {
        return mailingListDAO.findSemaphore();
    }

    public MailingTask findFirstTask() {
        return mailingListDAO.findFirstTask();
    }

    public long coutMailingLinks( MailingList list ) {
        return mailingListDAO.countLinksOfList( list );
    }

    public List<FaceMailingList> findFacesForMailingList( MailingList mailingList ) {
        return mailingListDAO.findFacesForMailingList( mailingList );
    }

    public void saveMailingListFaces( MailingList mailingList, long[] facesId ) {
        List<FaceMailingList> faceMailingLists = mailingListDAO.findFacesForMailingList( mailingList );
        for ( long faceId : facesId ) {
            boolean found = false;
            for ( FaceMailingList faceMailingList : faceMailingLists ) {
                if ( faceMailingList.getFace().getId() == faceId ) {
                    found = true;
                    break;
                }
            }
            if ( !found ) {
                FaceConfig face = faceConfigDAO.findById( faceId );
                if ( face != null ) {
                    FaceMailingList toAssign = mailingListDAO.findMailingListOfFace( face );
                    if ( toAssign == null ) {
                        toAssign = new FaceMailingList();
                        toAssign.setMailingList( mailingList );
                    }
                    toAssign.setFace( face );
                    mailingListDAO.makePersistent( toAssign );
                }
            }
        }
        Arrays.sort( facesId );
        for ( FaceMailingList faceMailingList : faceMailingLists ) {
            if ( Arrays.binarySearch( facesId, faceMailingList.getFace().getId() ) < 0 ) {
                mailingListDAO.makePersistent( faceMailingList );
            }
        }
    }

    public SendGridMark findLastMark() {
        return mailingListDAO.findLastMark();
    }

    public void saveSendGridMark( SendGridMark mark ) {
        mailingListDAO.makePersistent( mark );
    }

    public List<MailingLinkStats> findMailingLinkStatistic( Date fromDate ) {
        return mailingListDAO.findMailingLinkStatistic( fromDate );
    }

    public List<MailingLink> findMailingLinksForChange( Date fromDate, MailingLinkStatus fromStatus, int limit ) {
        return mailingListDAO.findMailingLinksForChange( fromDate, fromStatus, limit );
    }
}
