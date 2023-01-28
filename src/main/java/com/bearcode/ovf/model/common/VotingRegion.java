package com.bearcode.ovf.model.common;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Joiner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2007
 * Time: 4:56:03 PM
 * Voting region
 */
public class VotingRegion extends LookupEntity {
    private static final long serialVersionUID = -8462792746433958108L;
    
    private State state;
    
    private County county;
    
    private Municipality municipality;
    
    @BusinessKey
    public State getState() {
        return state;
    }

    public void setState(final State state ) {
        this.state = state;
    }
    
    @BusinessKey
    public Municipality getMunicipality() {
    	return municipality;
    }
    
    public void setMunicipality(final Municipality municipality) {
    	this.municipality = municipality;
    }
    
    @BusinessKey
    public County getCounty() {
    	return county;
    }
    
    public void setCounty(final County county) {
    	this.county = county;
    }

    @JsonIgnore
    public String getType() {
    	if (getName() != null) {
    		final Matcher matcher = Pattern.compile( ".*(County|City|Town|Township|Borough).*" ).matcher( getName() );
    		if ( matcher.find() ) {
    			return matcher.group( 1 ).toLowerCase();
    		}
    	}
    	
        return "region";
    }

	@JsonIgnore
    public String getFullName() {
		return Joiner.on(", ").useForNull("").join(getName(), getState() != null ? getState().getAbbr() : null);
    }

    public boolean valueEquals( final VotingRegion other ) {
        if ( other == null ) {
            return false;
        } else if ( this == other ) {
            return true;
        }
        
        return stateEquals(other) && typeEquals(other) && nameEquals(other);
    }
    
    private boolean nameEquals(final VotingRegion other) {
    	return getName() == null ? other.getName() == null : getName().equalsIgnoreCase(other.getName());
    }
    
    private boolean stateEquals(final VotingRegion other) {
    	return getState() == null ? other.getState() == null : getState().valueEquals(other.getState());
    }
    
    private boolean typeEquals(final VotingRegion other) {
    	return getType() == null ? other.getType() == null : getType().equalsIgnoreCase(other.getType());
    }

	public VotingRegion updateFrom(final VotingRegion newRegion) {
		final County updatedCounty = updateCountyFrom(newRegion.getCounty());
		final Municipality updatedMunicipality = updateMunicipalityFrom(updatedCounty, newRegion.getMunicipality());
		
		setCounty(updatedCounty);
		setMunicipality(updatedMunicipality);
		
		return this;
    }

	private Municipality updateMunicipalityFrom(final County updatedCounty, Municipality newMunicipality) {
		Municipality updatedMunicipality = getMunicipality();
		
	    if (getMunicipality() == null) {
	    	if (newMunicipality != null) {
				updatedMunicipality = buildUpdatedMunicipality(updatedCounty, newMunicipality);
	    	}
		} else {
			if (newMunicipality == null) {
				updatedMunicipality = null;
			} else {
				if (!getMunicipality().getName().equals(newMunicipality.getName()) ||
						!getMunicipality().getType().equals(newMunicipality.getType()) ||
						(updatedCounty != getCounty())) {
					updatedMunicipality = buildUpdatedMunicipality(updatedCounty, newMunicipality);
				}
			}
		}

	    return updatedMunicipality;
    }

	private Municipality buildUpdatedMunicipality(final County updatedCounty, Municipality newMunicipality) {
	    final Municipality updatedMunicipality = new Municipality();
	    updatedMunicipality.setState(getState());
	    updatedMunicipality.setCounty(updatedCounty);
	    updatedMunicipality.setName(newMunicipality.getName());
	    updatedMunicipality.setType(newMunicipality.getType());
	    return updatedMunicipality;
    }

	private County updateCountyFrom(final County newCounty) {
		County updatedCounty = getCounty();
		if (getCounty() == null) {
			if (newCounty != null) {
				updatedCounty = buildUpdatedCounty(newCounty);
			}
		} else {
			if (newCounty == null) {
				updatedCounty = null;
			} else {
				if (!getCounty().getName().equals(newCounty.getName()) ||
						!getCounty().getType().equals(newCounty.getType())) {
					updatedCounty = buildUpdatedCounty(newCounty);
				}
			}
		}
	    return updatedCounty;
    }

	private County buildUpdatedCounty(final County newCounty) {
	    final County updatedCounty = new County();
	    updatedCounty.setState(getState());
	    updatedCounty.setName(newCounty.getName());
	    updatedCounty.setType(newCounty.getType());
	    return updatedCounty;
    }

	/**
	 * Ensures that the region is consistent - namely, that the county and municipality (if they exist)
	 * properly point at the state.
	 */
	public void makeConsistent() {
	    if ((getCounty() != null) && (getCounty().getState() == null)) {
	    	getCounty().setState(getState());
	    }
	    if ((getMunicipality() != null) && (getMunicipality().getState() == null)) {
	    	getMunicipality().setState(getState());
	    }
    }
}