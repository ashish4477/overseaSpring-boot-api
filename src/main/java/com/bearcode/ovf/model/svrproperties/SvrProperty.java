/**
 * 
 */
package com.bearcode.ovf.model.svrproperties;

/**
 * State and voting region properties.
 * <p>
 * This model provides per state or per voting region property strings for display.
 * 
 * @author IanBrown
 * 
 * @since Oct 22, 2012
 * @version Oct 22, 2012
 */
public class SvrProperty {

	/**
	 * the identifier.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private Long id;

	/**
	 * the name of the property.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private String propertyName;

	/**
	 * the value for the property.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private String propertyValue;

	/**
	 * the abbreviation for the state.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private String stateAbbreviation;

	/**
	 * the name of the voting region.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private String votingRegionName;

	/**
	 * Gets the id.
	 * 
	 * @author IanBrown
	 * @return the id.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the property name.
	 * 
	 * @author IanBrown
	 * @return the property name.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Gets the property value.
	 * 
	 * @author IanBrown
	 * @return the property value.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * Gets the state abbreviation.
	 * 
	 * @author IanBrown
	 * @return the state abbreviation.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public String getStateAbbreviation() {
		return stateAbbreviation;
	}

	/**
	 * Gets the voting region name.
	 * 
	 * @author IanBrown
	 * @return the voting region name.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public String getVotingRegionName() {
		return votingRegionName;
	}

	/**
	 * Sets the id.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the id to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the property name.
	 * 
	 * @author IanBrown
	 * @param propertyName
	 *            the property name to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Sets the property value.
	 * 
	 * @author IanBrown
	 * @param propertyValue
	 *            the property value to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public void setPropertyValue(final String propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * Sets the state abbreviation.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public void setStateAbbreviation(final String stateAbbreviation) {
		this.stateAbbreviation = stateAbbreviation;
	}

	/**
	 * Sets the voting region name.
	 * 
	 * @author IanBrown
	 * @param votingRegionName
	 *            the voting region name to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public void setVotingRegionName(final String votingRegionName) {
		this.votingRegionName = votingRegionName;
	}
}
