package com.bearcode.ovf.actions.mail;

import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.model.mail.MailingLinkStatus;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.service.MailingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Date: 16.07.12
 * Time: 19:17
 *
 * @author Leonid Ginzburg
 */
@Component
public class SubscribeEodContacts {

    private final Logger logger = LoggerFactory.getLogger( SubscribeEodContacts.class );

    @Autowired
    private MailingListService mailingListService;

    /**
     * Id of MailingList
     * Id is to be set using overrideProperties file
     * todo - set up this using OvfProperties
     */
    private long mailingListId = 0;

    /**
     * Number of rows to be proceeded
     * Intended to reduce loading on the process at first run
     */
    private int numberOfRows = 50;

    /**
     * Creates records in MailingLink and MailingAddress tables for emails from
     * LocalOfficial which emails are not present in MailingAddress
     */
    public void updateMails() {
        try {

            if ( mailingListId == 0 ) return;

            MailingList list = mailingListService.findMailingList( mailingListId );
            if ( list == null ) return;

            List<LocalOfficial> localOfficials = mailingListService.findLeoForMailing( list, numberOfRows );

            for ( LocalOfficial leo : localOfficials ) {
                MailingAddress signup = new MailingAddress();
                signup.setEmail( leo.getLeoEmail() );

                signup.setFirstName( leo.getLeo().getFirstName() );
                signup.setLastName( leo.getLeo().getLastName() );
                signup.setBirthYear( 0 );
                signup.setVoterType( "" );
                signup.setPhone( leo.getLeoPhone() );
                signup.setUrl( "" );
                signup.setVotingCity( leo.getPhysical().getCity() );
                signup.setVotingStateName( leo.getPhysical().getState() );
                signup.setVotingPostalCode( leo.getPhysical().getZip() );
                signup.setRegion( leo.getRegion() );

                signup.setCurrentAddress( "" );
                signup.setCurrentCity( "" );
                signup.setCurrentPostalCode( "" );
                signup.setCurrentCountryName( "" );

                mailingListService.saveMailingAddress( signup );

                MailingLink link = mailingListService.findLinkByListAndAddress( list, signup );
                if ( link == null ) {
                    link = new MailingLink();
                    link.setMailingAddress( signup );
                    link.setMailingList( list );
                    link.setStatus( MailingLinkStatus.NEW );
                } else if ( link.getStatus() == MailingLinkStatus.UNSUBSCRIBED ) {
                    link.setStatus( MailingLinkStatus.NEW );
                }
                mailingListService.updateMailingLink( link );
            }
            localOfficials = mailingListService.findLovcForMailing( list, numberOfRows );

            for ( LocalOfficial leo : localOfficials ) {
                MailingAddress signup = new MailingAddress();
                signup.setEmail( leo.getLovcEmail() );

                signup.setFirstName( leo.getLovc().getFirstName() );
                signup.setLastName( leo.getLovc().getLastName() );
                signup.setBirthYear( 0 );
                signup.setVoterType( "" );
                signup.setPhone( leo.getLovcPhone() );
                signup.setUrl( "" );
                signup.setVotingCity( leo.getPhysical().getCity() );
                signup.setVotingStateName( leo.getPhysical().getState() );
                signup.setVotingPostalCode( leo.getPhysical().getZip() );
                signup.setRegion( leo.getRegion() );

                signup.setCurrentAddress( "" );
                signup.setCurrentCity( "" );
                signup.setCurrentPostalCode( "" );
                signup.setCurrentCountryName( "" );

                mailingListService.saveMailingAddress( signup );

                MailingLink link = mailingListService.findLinkByListAndAddress( list, signup );
                if ( link == null ) {
                    link = new MailingLink();
                    link.setMailingAddress( signup );
                    link.setMailingList( list );
                    link.setStatus( MailingLinkStatus.NEW );
                } else if ( link.getStatus() == MailingLinkStatus.UNSUBSCRIBED ) {
                    link.setStatus( MailingLinkStatus.NEW );
                }
                mailingListService.updateMailingLink( link );
            }
        } catch ( Exception e ) {
            logger.error( "", e );
        }
    }

    public MailingListService getMailingListService() {
        return mailingListService;
    }

    public void setMailingListService( MailingListService mailingListService ) {
        this.mailingListService = mailingListService;
    }

    public long getMailingListId() {
        return mailingListId;
    }

    public void setMailingListId( long mailingListId ) {
        this.mailingListId = mailingListId;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows( int numberOfRows ) {
        this.numberOfRows = numberOfRows;
    }
}
