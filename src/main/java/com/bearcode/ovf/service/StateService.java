package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.CountryDAO;
import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.DAO.VotingRegionDAO;
import com.bearcode.ovf.model.common.Country;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficeType;
import com.bearcode.ovf.model.eod.LocalOfficial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2007
 * Time: 5:47:42 PM
 * @author Leonid Ginzburg
 */
@Service
public class StateService {

    @Autowired
	private CountryDAO countryDAO;

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private VotingRegionDAO regionDAO;

    public Collection<State> findAllStates() {
        return stateDAO.getAllStates();
    }

    public State findState( long stateId ) {
        return stateDAO.getById( stateId );
    }

    @Deprecated
    public Collection<VotingRegion> findRegionsForState( State state ) {
    	if (state == null)
    		return Collections.emptyList();
    	
        return regionDAO.getRegionsForState( state );
    }

    @Deprecated
    public Collection<VotingRegion> findRegionsForState( Long stateId ) {
    	if (stateId == null) 
    		return Collections.emptyList();
    	
        State state = stateDAO.getById( stateId );
        return findRegionsForState( state );
    }

    @Deprecated
    public Collection<VotingRegion> findRegionsForState( String abbr ) {
        State state = stateDAO.getByAbbreviation(abbr);
        return findRegionsForState( state );
    }

    public Collection<Country> findAllCountries() {
        return countryDAO.getAllCountries();
    }

    @Deprecated
    public VotingRegion findRegion( Long regionId ) {
    	if (regionId == null) return null;
        return regionDAO.getById( regionId );
    }

    @Deprecated
    public LocalOfficial findLeoById(Long id) {
    	 if (id == null) return null;
        return regionDAO.findLeoByRegionId( id );
    }

	/**
	 * Finds the state with the specified abbreviation.
	 * @author IanBrown
	 * @param abbreviation the state abbreviation.
	 * @return the state.
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	public State findByAbbreviation(final String abbreviation) {
		return stateDAO.getByAbbreviation(abbreviation);
	}

	/**
	 * Finds the voting region with the specified name belonging to the selected state.
	 * @author IanBrown
	 * @param state the state.
	 * @param votingRegionName the name of the voting region.
	 * @return the voting region.
	 * @since Aug 3, 2012
	 * @version Aug 3, 2012
	 */
    @Deprecated
	public VotingRegion findRegion(State state, String votingRegionName) {
		final VotingRegion region = new VotingRegion();
		region.setState(state);
		region.setName(votingRegionName);
		return regionDAO.getRegionByName(region);
	}

    public State findStateByAbbreviationOrName( String stateName ) {
        State state = stateDAO.getByAbbreviation( stateName );
        if ( state != null ) {
            return state;
        }
        String fullName = stateName.replaceAll( "[-_+]", " ");
        return stateDAO.getByName( fullName );
    }

    @Deprecated
    public LocalOfficial findUocavaOfficeForState(State state) {
        List<VotingRegion> officials = regionDAO.getRegionsForState( state, LocalOfficeType.UOCAVA );
        if ( officials != null && officials.size() != 0 ) {
            return regionDAO.findLeoByRegionId( officials.get(0).getId() );
        }
        return null;
    }

    @Deprecated
    public LocalOfficial findUocavaOfficeForState( Long stateId ) {
        State state = stateDAO.getById( stateId );
        if ( state != null ) {
            List<VotingRegion> officials = regionDAO.getRegionsForState( state, LocalOfficeType.UOCAVA );
            if ( officials != null && officials.size() != 0 ) {
                return regionDAO.findLeoByRegionId( officials.get(0).getId() );
            }
        }
        return null;
    }

    @Deprecated
    public VotingRegion findUocavaRegionForState( Long stateId ) {
        State state = stateDAO.getById( stateId );
        if ( state != null ) {
            List<VotingRegion> officials = regionDAO.getRegionsForState( state, LocalOfficeType.UOCAVA );
            if ( officials != null && officials.size() != 0 ) {
                return officials.get(0);
            }
        }
        return null;
    }
}
