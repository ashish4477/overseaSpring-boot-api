package com.bearcode.ovf.utils;

import com.bearcode.ovf.model.eod.ElectionView;
import com.bearcode.ovf.webservices.localelections.model.ElectionLocation;
import com.bearcode.ovf.webservices.localelections.model.LocalElectionDecorator;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author leonid.
 */
public class ElectionsFilter {

    private String regionName;

    public ElectionsFilter() {
    }

    public ElectionsFilter( String regionName ) {
        this.regionName = regionName;
    }

    public Collection<ElectionView> filterStateWide( Collection<ElectionView> elections ) {
        for ( Iterator<ElectionView> iterator = elections.iterator(); iterator.hasNext(); ) {
            ElectionView election = iterator.next();
            if ( election instanceof LocalElectionDecorator ) {
                LocalElectionDecorator localElection = (LocalElectionDecorator) election;
                if ( localElection.getElectionLevel().getName().equalsIgnoreCase( "state" ) ) {
                    continue;
                }
                if ( regionName != null && regionName.length() > 0 ) {
                    boolean locationFound = false;
                    for( ElectionLocation location : localElection.getLocations() ) {
                        if ( regionName.equalsIgnoreCase( location.getName() ) ) {
                            locationFound = true;
                        }
                    }
                    if ( locationFound ) {
                        continue;
                    }
                }

                iterator.remove();
            }
        }
        return elections;
    }
}
