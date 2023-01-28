/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.model.common;

import javax.persistence.Column;

/**
 * Extended {@link LookupEntity} representing a county.
 * 
 * @author Ian
 * @since 01/16/2015
 * @version 01/16/2015
 */
public class County extends LookupEntity {

	/** the serial version UID for the class. */
	private static final long serialVersionUID = -1060300697883361502L;

	/** the state in which this county resides. */
	private State state;

	/** the type of county. */
	@Column(name = "county_type")
	private String type = "";

	/**
	 * Gets the state in which this county resides.
	 * 
	 * @return the state.
	 */
	@BusinessKey
	public State getState() {
		return state;
	}

	/**
	 * Gets the type of county (County, Parish, etc.).
	 * 
	 * @return the type of county.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the state in which this county resides.
	 * 
	 * @param state
	 *            the state.
	 */
	public void setState(final State state) {
		this.state = state;
	}

	/**
	 * Sets the type of county (County, Parish, etc.).
	 * 
	 * @param type
	 *            the type of county.
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Are the counties equal?
	 * 
	 * @param other
	 *            the other county.
	 * @return <code>true</code> if the counties are equal, <code>false</code>
	 *         otherwise.
	 */
	public boolean valueEquals(final County other) {
		if (other == null) {
			return false;
		} else if (this == other) {
			return true;
		}

		return stateEquals(other) && typeEquals(other) && nameEquals(other);
	}

	/**
	 * Determines if the name of this county is equal to that of the input
	 * county.
	 * 
	 * @param other
	 *            the other county.
	 * @return <code>true</code> if the names are equal, <code>false</code>
	 *         otherwise.
	 */
	private final boolean nameEquals(final County other) {
		return getName() == null ? other.getName() == null
		        : other.getName() != null
		                && getName().equalsIgnoreCase(other.getName());
	}

	/**
	 * Determines if the state of this county is equal to that of the other
	 * county.
	 * 
	 * @param other
	 *            the other county.
	 * @return <code>true</code> if the counties are equal, <code>false</code>
	 *         otherwise.
	 */
	private final boolean stateEquals(final County other) {
		return getState() == null ? other.getState() == null : getState()
		        .valueEquals(other.getState());
	}

	/**
	 * Determines if the type of this county is equal to that of the input
	 * county.
	 * 
	 * @param other
	 *            the other county.
	 * @return <code>true</code> if the types are equal, <code>false</code>
	 *         otherwise.
	 */
	private final boolean typeEquals(final County other) {
		return getType() == null ? other.getType() == null
		        : other.getType() != null
		                && getType().equalsIgnoreCase(other.getType());
	}
}