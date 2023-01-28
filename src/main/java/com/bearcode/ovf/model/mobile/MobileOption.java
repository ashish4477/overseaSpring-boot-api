/**
 * 
 */
package com.bearcode.ovf.model.mobile;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;

/**
 * An option for answering a mobile question.
 * 
 * @author IanBrown
 * 
 * @since Apr 12, 2012
 * @version Jul 12, 2012
 */
@JsonIgnoreProperties({ "item" })
public class MobileOption {

	/**
	 * the identifier for the option.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private Long id;

	/**
	 * the item for the mobile option.
	 * 
	 * @author IanBrown
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	private FieldDictionaryItem item;

	/**
	 * the value of the option.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private String value;

	/**
	 * Gets the identifier.
	 * 
	 * @author IanBrown
	 * @return the identifier.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the item.
	 * 
	 * @author IanBrown
	 * @return the item.
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	public FieldDictionaryItem getItem() {
		return item;
	}

	/**
	 * Gets the value.
	 * 
	 * @author IanBrown
	 * @return the value.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the item.
	 * 
	 * @author IanBrown
	 * @param item
	 *            the item to set.
	 * @since Jul 11, 2012
	 * @version Jul 11, 2012
	 */
	public void setItem(final FieldDictionaryItem item) {
		this.item = item;
	}

	/**
	 * Sets the value.
	 * 
	 * @author IanBrown
	 * @param value
	 *            the value to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder((getValue() == null) ? "" : getValue());
		if (getId() != null) {
			sb.append("[#").append(getId()).append("]");
		}
		return sb.toString();
	}
}
