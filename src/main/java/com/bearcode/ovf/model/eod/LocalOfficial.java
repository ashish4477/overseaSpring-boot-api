package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.VotingRegion;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2007
 * Time: 5:09:23 PM
 *
 * @author Leonid Ginzburg
 */
public class LocalOfficial extends AbstractLocalOfficial {
    public static final int STATUS_READY = 1;
    public static final int STATUS_HIDDEN = 0;
    private static final long serialVersionUID = -208594971687715415L;

    private VotingRegion region;
    private LocalOfficeType localOfficeType = LocalOfficeType.ALL;

    private int status = STATUS_READY;

    private List<AdditionalAddress> additionalAddresses;

    public LocalOfficial() {
        region = null;
        setStatus( STATUS_READY );

        additionalAddresses = new LinkedList<AdditionalAddress>();
    }

    public VotingRegion getRegion() {
        return region;
    }

    public void setRegion( VotingRegion region ) {
        this.region = region;
    }

    public LocalOfficeType getLocalOfficeType() {
        return localOfficeType;
    }

    public void setLocalOfficeType(LocalOfficeType localOfficeType) {
        this.localOfficeType = localOfficeType;
    }

    public LocalOfficial updateFrom( LocalOfficial fromLeo ) {

        if (
                !getPhysical().equals( fromLeo.getPhysical() ) ||
                        !getMailing().equals( fromLeo.getMailing() ) ||
                        !getLeo().equals( fromLeo.getLeo() ) ||
                        !getLovc().equals( fromLeo.getLovc() ) ||
                        !getAddContact().equals( fromLeo.getAddContact() ) ||
                        !getLeoPhone().equals( fromLeo.getLeoPhone() ) ||
                        !getLeoFax().equals( fromLeo.getLeoFax() ) ||
                        !getLeoEmail().equals( fromLeo.getLeoEmail() ) ||
                        !getDsnPhone().equals( fromLeo.getDsnPhone() ) ||
                        !getLovcPhone().equals( fromLeo.getLovcPhone() ) ||
                        !getLovcFax().equals( fromLeo.getLovcFax() ) ||
                        !getLovcEmail().equals( fromLeo.getLovcEmail() ) ||
                        !getAddPhone().equals( fromLeo.getAddPhone() ) ||
                        !getAddEmail().equals( fromLeo.getAddEmail() ) ||
                        !getWebsite().equals( fromLeo.getWebsite() ) ||
                        !getHours().equals( fromLeo.getHours() ) ||
                        !getFurtherInstruction().equals( fromLeo.getFurtherInstruction() )
                ) {
            setUpdated( new Date() );
        }

        getPhysical().updateFrom( fromLeo.getPhysical() );
        getMailing().updateFrom( fromLeo.getMailing() );
        setDsnPhone( fromLeo.getDsnPhone() );
        setGeneralEmail( fromLeo.getGeneralEmail() );

        getLeo().updateFrom( fromLeo.getLeo() );
        getLovc().updateFrom( fromLeo.getLovc() );
        getAddContact().updateFrom( fromLeo.getAddContact() );

        for ( int i = 0; i < fromLeo.getOfficers().size(); i++ ) {
            if ( getOfficers().size() == i ) {
                Officer toAdd = new Officer();
                toAdd.setOrderNumber( i + 1 );
                getOfficers().add( toAdd );
            }
            getOfficers().get(i).updateFrom( fromLeo.getOfficers().get(i) );
        }
        if ( getOfficers().size() > fromLeo.getOfficers().size() ) {
            for ( int i = fromLeo.getOfficers().size(); i < getOfficers().size(); i++ ) {
                getOfficers().get( i ).clear();
            }
        }
        setWebsite( fromLeo.getWebsite() );
        setHours( fromLeo.getHours() );
        setFurtherInstruction( fromLeo.getFurtherInstruction() );
        for ( int j = 0; j < fromLeo.getAdditionalAddresses().size(); j++ ) {
            if ( getAdditionalAddresses().size() == j ) {
                getAdditionalAddresses().add( fromLeo.getAdditionalAddresses().get( j ) );
            } else {
                getAdditionalAddresses().get( j ).updateFrom ( fromLeo.getAdditionalAddresses().get( j ) );
            }
        }
        if ( getAdditionalAddresses().size() > fromLeo.getAdditionalAddresses().size() ) {
            int j = 0;
            for ( Iterator<AdditionalAddress> iterator = getAdditionalAddresses().iterator(); iterator.hasNext(); j++) {
                iterator.next();
                if ( j >= fromLeo.getAdditionalAddresses().size() ) {
                    iterator.remove();
                }
            }
        }
        return this;
    }

    @JsonIgnore   //TODO remove annotation when mobile application will use same model for this class
    public List<AdditionalAddress> getAdditionalAddresses() {
        return additionalAddresses;
    }

    public void setAdditionalAddresses( List<AdditionalAddress> additionalAddresses ) {
        this.additionalAddresses = additionalAddresses;
    }

    public void sortAdditionalAddresses() {
        Collections.sort( additionalAddresses, new Comparator<AdditionalAddress>() {
            @Override
            public int compare( AdditionalAddress addr1, AdditionalAddress addr2 ) {
                return addr1.getType().getName().compareTo( addr2.getType().getName() );
            }
        } );
    }
}
