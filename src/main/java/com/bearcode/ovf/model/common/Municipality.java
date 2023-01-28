/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.common;

import javax.persistence.Column;

/**
 * Extended {@link LookupEntity} representing a municipality (village, township,
 * city, etc.).
 * 
 * @author Ian
 * @since 01/16/2015
 * @version 01/16/2015
 */
public class Municipality extends LookupEntity {

	/** the serial version UID for the class. */
	private static final long serialVersionUID = -91279875974537563L;

	/** the county in which this municipality resides. */
	private County county;

	/** the state in which this municipality resides. */
	private State state;

	/** the type of municipality. */
	@Column(name = "municipality_type")
	private String type = "";

	/**
	 * Gets the county in which this municipality resides.
	 * 
	 * @return the county.
	 */
	@BusinessKey
	public County getCounty() {
		return county;
	}

	/**
	 * Gets the state in which this municipality resides.
	 * 
	 * @return the state.
	 */
	@BusinessKey
	public State getState() {
		return state;
	}

	/**
	 * Gets the type of municipality (Village, Township, City, etc.)
	 * 
	 * @return the type of municipality.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the county in which this municipality resides.
	 * 
	 * @param county
	 *            the county to set.
	 */
	public void setCounty(final County county) {
		this.county = county;
	}

	/**
	 * Sets the state in which this municipality resides.
	 * 
	 * @param state
	 *            the state to set.
	 */
	public void setState(final State state) {
		this.state = state;
	}

	/**
	 * Sets the type of municipality (Village, Township, City, etc.)
	 * 
	 * @param type
	 *            the type of municipality.
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Determines if this municipality is equal to the other one.
	 * 
	 * @param other
	 *            the other municipality.
	 * @return <code>true</code> if the municipalities are equal,
	 *         <code>false</code> otherwise.
	 */
	public boolean valueEquals(final Municipality other) {
		return stateEquals(other) && countyEquals(other) && typeEquals(other)
		        && nameEquals(other);
	}

	/**
	 * Determines if the county of this municipality is equal to that of the
	 * other one.
	 * 
	 * @param other
	 *            the other municipality.
	 * @return <code>true</code> if the counties are equal, <code>false</code>
	 *         otherwise.
	 */
	private final boolean countyEquals(final Municipality other) {
		return getCounty() == null ? other.getCounty() == null : getCounty()
		        .valueEquals(other.getCounty());
	}

	/**
	 * Determines if the name of this municipality is equal to that of the other
	 * one.
	 * 
	 * @param other
	 *            the other municipality.
	 * @return <code>true</code> if the names are equal, <code>false</code>
	 *         otherwise.
	 */
	private final boolean nameEquals(final Municipality other) {
		return getName() == null ? other.getName() == null
		        : other.getName() != null
		                && getName().equalsIgnoreCase(other.getName());
	}

	/**
	 * Determines if the state of this municipality is equal to that of the
	 * other one.
	 * 
	 * @param other
	 *            the other municipality.
	 * @return <code>true</code> if the states are equal, <code>false</code>
	 *         otherwise.
	 */
	private final boolean stateEquals(final Municipality other) {
		return getState() == null ? other.getState() == null : getState()
		        .valueEquals(other.getState());
	}

	/**
	 * Determines if the type of this municipality is the same as the other one.
	 * 
	 * @param other
	 *            the other municipality.
	 * @return <code>true</code> if the types are equal, <code>false</code>
	 *         otherwise.
	 */
	private final boolean typeEquals(final Municipality other) {
		return getType() == null ? other.getType() == null
		        : other.getType() != null
		                && getType().equalsIgnoreCase(other.getType());
	}
}