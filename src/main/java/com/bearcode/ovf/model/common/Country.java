package com.bearcode.ovf.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2007
 * Time: 4:39:12 PM
 * State class. Stores name and abbreviation.
 */
@Entity
@Table(name="countries")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Country extends LookupEntity {
    private static final long serialVersionUID = -4046410865819857080L;
    
    @Column
    private String abbreviation;    // special code according FIPS standard
    
	@Column(name = "active", nullable = false, columnDefinition="tinyint(1) NOT NULL")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;    		// special code according FIPS standard

	@BusinessKey
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@BusinessKey
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbr) {
        this.abbreviation = abbr;
    }
}
