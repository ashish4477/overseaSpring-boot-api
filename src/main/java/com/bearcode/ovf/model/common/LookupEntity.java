package com.bearcode.ovf.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User: Dmitry Sarkisov
 * Date: 13.12.2005
 * Time: 18:00:07
 * Represents a simple entity with ID and Name atributes.
 */
public abstract class LookupEntity extends BusinessKeyObject implements Serializable {

    private static final long serialVersionUID = 3843709785684333702L;
    
	/**
	 * Object primary key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@BusinessKey
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
