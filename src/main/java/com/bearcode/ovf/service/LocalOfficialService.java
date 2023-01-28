package com.bearcode.ovf.service;

import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.DAO.CorrectionsDAO;
import com.bearcode.ovf.DAO.LocalOfficialDAO;
import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.DAO.VotingRegionDAO;
import com.bearcode.ovf.forms.AdminCorrectionsListForm;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.County;
import com.bearcode.ovf.model.common.Municipality;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2007
 * Time: 6:03:40 PM
 *
 * @author Leonid Ginzburg
 */
@SuppressWarnings("unchecked")
@Service
public class LocalOfficialService {

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private VotingRegionDAO regionDAO;

    @Autowired
    private LocalOfficialDAO localOfficialDAO;

    @Autowired
    private CorrectionsDAO correctionsDAO;


    public LocalOfficial findById( long id ) {
        LocalOfficial leo = localOfficialDAO.getById( id );
        if ( leo != null ) {
            leo.sortAdditionalAddresses();
        }
        return leo;
    }

    public Collection<LocalOfficial> findForState( State state ) {
        return localOfficialDAO.findLeoByState( state );
    }

    public Collection<LocalOfficial> findForState( long stateId ) {
        State state = stateDAO.getById( stateId );
        return findForState( state );
    }

    public Collection<LocalOfficial> findForState( long stateId, CommonFormObject form ) {
        return localOfficialDAO.findLeoByState( stateId, form.createPagingInfo() );
    }

    public Collection<LocalOfficial> findForState( long stateId, CommonFormObject form, Long lookFor ) {
        VotingRegion region = null;
        if ( lookFor != null ) {
            region = regionDAO.getById( lookFor );
        }
        Collection<LocalOfficial> result = localOfficialDAO.findLeoByState( stateId, form.createPagingInfo(), region );
        //set actual page num
        if ( form.getPagingInfo().getFirstResult() > 0 && form.getPageSize() > 0 ) {
            form.setPage( (int) Math.floor( (double) form.getPagingInfo().getFirstResult() / form.getPageSize() ) );
        }
        return result;
    }

    public LocalOfficial findForRegion( VotingRegion region ) {
        LocalOfficial leo = localOfficialDAO.findLeoByRegion( region );
        if ( leo != null ) leo.sortAdditionalAddresses();
        return leo;
    }

    public LocalOfficial findForRegion( long regionId ) {
        VotingRegion region = regionDAO.getById( regionId );
        return findForRegion( region );
    }

    /**
     * Save all LEOs from the list. If region already exists, update existing information,
     * save a new info otherwise.
     *
     * @param eod List of LEOs to store.
     */
    public void saveAllLocalOfficial( Collection<LocalOfficial> eod ) {
    	final Set<State> states = new HashSet<State>();
        for (final LocalOfficial leo : eod ) {
            updateIndividualLocalOfficial(leo, states);
        }
        
        regionDAO.cleanCountiesAndMunicipalities( states );
    }

    /**
     * Updates a single local official.
     * @param leo the local official.
     */
    public void updateLocalOfficial(final LocalOfficial leo) {
    	final Set<State> states = new HashSet<State>();
    	updateIndividualLocalOfficial( leo, states );
        regionDAO.cleanCountiesAndMunicipalities( states );
    }
    
    /**
     * Updates an individual local official.
     * @param leo the local official.
     * @param states the set of states updated (may change on output).
     */
	private void updateIndividualLocalOfficial(final LocalOfficial leo, final Set<State> states) {
    	leo.getRegion().makeConsistent();
	    final VotingRegion region = regionDAO.getRegionByName( leo.getRegion() );
	    VotingRegion updatedRegion = leo.getRegion();
	    LocalOfficial updatedLeo = leo;
	    if ( region != null ) {
	    	updatedRegion = region.updateFrom( leo.getRegion() );
	        final LocalOfficial existingLeo = localOfficialDAO.findLeoByRegion( updatedRegion );
	        if (existingLeo != null) {
	        	updatedLeo = existingLeo.updateFrom( leo );
	        }
	    }
	    
	    saveVotingRegion( updatedRegion );
	    updatedLeo.setRegion( updatedRegion );
	    saveLocalOfficial( updatedLeo );
	    states.add( updatedRegion.getState() );
    }

	/**
     * Saves the voting region and its referenced county and municipality.
     * @param region the voting region to save.
     */
	public void saveVotingRegion(final VotingRegion region) {
		County county = region.getCounty();
		if ((county != null) && (county.getId() == null)) {
			final County existingCounty = regionDAO.findCountyByStateAndName( county.getState(), county.getName() );
			if (existingCounty != null) {
				county = existingCounty;
			} else {
				regionDAO.makePersistent(county);
			}
		}
		
		final Municipality municipality = region.getMunicipality();
		if (municipality != null) {
			if (county != null) {
				if (municipality.getCounty() != county) {
					municipality.setCounty(county);
				}
			}
			regionDAO.makePersistent(municipality);
		}
		regionDAO.makePersistent( region );
	}

	public void saveLocalOfficial( LocalOfficial leo ) {
        leo.setUpdated( new Date() );
        int i = 1;
        for( Iterator<Officer> it = leo.getOfficers().iterator(); it.hasNext(); ) {
            Officer officer = it.next();
            if ( officer.isEmpty() ) {
                it.remove();
                if ( officer.getId() != 0 ) {
                    localOfficialDAO.makeTransient( officer );
                }
            }
            else {
                officer.setOrderNumber( i++ );
                if ( officer.getId() == 0 ) {
                    localOfficialDAO.makePersistent( officer );
                }
            }
        }

        for ( Iterator<AdditionalAddress> addressIterator = leo.getAdditionalAddresses().iterator(); addressIterator.hasNext(); ) {
            AdditionalAddress address = addressIterator.next();
            if ( address.getType() == null || StringUtils.isEmpty( address.getType().getName() ) ) {
                addressIterator.remove();
            }
        }
        localOfficialDAO.makePersistent( leo );
    }

    public void deleteAllRegionsOfState( State state ) {
        Collection<VotingRegion> regions = regionDAO.getRegionsForState( state );
        Collection<LocalOfficial> localOfficials = localOfficialDAO.findLeoByState( state );
        Collection<Address> addresses = new LinkedList<Address>();
        for ( LocalOfficial leo : localOfficials ) {
            if ( leo.getMailing() != null ) {
                addresses.add( leo.getMailing() );
            }
            if ( leo.getPhysical() != null ) {
                addresses.add( leo.getPhysical() );
            }
        }
        Collection<CorrectionsLeo> corrections = correctionsDAO.findForRegions( localOfficials );

        //remove them all
        correctionsDAO.makeAllTransient( corrections );
        localOfficialDAO.makeAllTransient( addresses );
        localOfficialDAO.makeAllTransient( localOfficials );
        regionDAO.makeAllTransient( regions );
    }

    public Collection<Map<String, Object>> findStateStatistics( CommonFormObject form ) {
        Collection result = new LinkedList();
        for ( Object rowObj : localOfficialDAO.findStateStatistics( form.createPagingInfo() ) ) {
            if ( rowObj instanceof Object[] ) {
                Object[] row = (Object[]) rowObj;
                Map rowMap = new HashMap();
                rowMap.put( "total", row[1] );
                rowMap.put( "approved", row[2] );
                rowMap.put( "state", row[3] );
                result.add( rowMap );
            }
        }
        return result;
    }

    public void makeCorrections( CorrectionsLeo command ) {
        for( Iterator<Officer> it = command.getOfficers().iterator(); it.hasNext(); ) {
            Officer officer = it.next();
            if ( officer.isEmpty() ) {
                it.remove();
            }
            else {
                if ( officer.getId() == 0 ) {
                    correctionsDAO.makePersistent( officer );
                }
            }
        }
        for ( Iterator<CorrectionAdditionalAddress> addressIterator = command.getAdditionalAddresses().iterator(); addressIterator.hasNext(); ) {
            CorrectionAdditionalAddress address = addressIterator.next();
            if ( address.isEmpty() ) {
                addressIterator.remove();
            }
        }
        correctionsDAO.makePersistent( command );
    }

    public Collection findCorrections( AdminCorrectionsListForm form ) {

        return correctionsDAO.findCorrectionByStatus( form.getStatus(), form.createPagingInfo() );
    }

    public CorrectionsLeo findCorrectionsById( long correctionId ) {
        return correctionsDAO.getById( correctionId );
    }

    public void saveCorrections( CorrectionsLeo correctionsLeo ) {
        correctionsLeo.getCorrectionFor().setUpdated( new Date() );
        //localOfficialDAO.makePersistent( correctionsLeo.getCorrectionFor() );
        saveLocalOfficial( correctionsLeo.getCorrectionFor() );
        correctionsLeo.setStatus( CorrectionsLeo.STATUS_ACCEPTED );
        correctionsDAO.makePersistent( correctionsLeo );
    }

    public void updateDeclinedCorrections( CorrectionsLeo correctionsLeo ) {
        LocalOfficial leo = correctionsLeo.getCorrectionFor();
        for( Iterator<Officer> it = leo.getOfficers().iterator(); it.hasNext(); ) {
            Officer officer = it.next();
            if ( officer.isEmpty() ) {
                it.remove();
            }
        }
        for ( Iterator<AdditionalAddress> addressIt = leo.getAdditionalAddresses().iterator(); addressIt.hasNext(); ) {
            AdditionalAddress address = addressIt.next();
            if ( address.checkEmpty() ) {
                addressIt.remove();
            }
        }
        correctionsLeo.setStatus( CorrectionsLeo.STATUS_DECLINED );
        correctionsDAO.makePersistent( correctionsLeo );
    }

    public Collection findAll() {
        return localOfficialDAO.findAll();
    }

    public boolean checkLeoGotUpdated() {
        return localOfficialDAO.checkLeoGotChanged();
    }

    public void initializeLeo( LocalOfficial leo ) {
        localOfficialDAO.initialize( leo );
    }

    public StateSpecificDirectory findSvidForState( State state ) {
        return localOfficialDAO.findSvidForState( state );
    }

    public void saveSvid( StateSpecificDirectory svid ) {
        svid.setUpdated( new Date() );
        localOfficialDAO.makePersistent( svid );
    }

    public StateSpecificDirectory findSvidForState( long stateId ) {
        return localOfficialDAO.findSvidForState( stateId );
    }

    public StateSpecificDirectory findSvid( long svidId ) {
        return localOfficialDAO.getSvidById( svidId );
    }

    public void makeCorrections( CorrectionsSvid command ) {
        localOfficialDAO.makePersistent( command );
    }

    public Election findElection( long electionId ) {
        return localOfficialDAO.findElection( electionId );
    }

    public void saveElection( Election election ) {
        localOfficialDAO.makePersistent( election );
        StateSpecificDirectory svid = election.getStateInfo();  // update date
        svid.setUpdated( new Date() );
        localOfficialDAO.makePersistent( svid );
    }

    public Collection findForSearch( List<String> params, List<String> states ) {
        return localOfficialDAO.search( params, states );
    }

    public Collection findForSearch( List<String> params, List<String> states, PagingInfo pagingInfo ) {
        return localOfficialDAO.search( params, states, pagingInfo );
    }

    public void deleteElection( Election election ) {
        StateSpecificDirectory svid = election.getStateInfo();  // update date
        svid.setUpdated( new Date() );
        svid.getElections().remove( election );
        localOfficialDAO.makeTransient( election );
        localOfficialDAO.makePersistent( svid );
    }

    public StateVotingLaws findVotingLaws( State state ) {
        return localOfficialDAO.findVotingLawsByState( state );
    }

    public void saveVotingLaws( StateVotingLaws votingLaws ) {
        localOfficialDAO.makePersistent( votingLaws );
    }

    public StateVotingLaws findVotingLawsByStateAbbr( String stateAbbr ) {
        return localOfficialDAO.findVotingLawsByStateAbbreviation( stateAbbr );
    }

    public Collection<StateVotingLaws> findAllStateVotingLaws() {
        return localOfficialDAO.findAllStateVotingLaws();
    }

    public long countStateWithNoExcuse() {
        return localOfficialDAO.countStateWithNoExcuse();
    }

    public Collection<Election> findAllElections() {
        return localOfficialDAO.findAllElections();
    }

    public Collection<AdditionalAddressType> findAdditionalAddressTypes() {
        return localOfficialDAO.findAdditionalAddressTypes();
    }

    public AdditionalAddressType findAdditionalAddressType( long id ) {
        return localOfficialDAO.findAdditionalAddressType( id );
    }
}