/**
 * 
 */
package com.bearcode.ovf.model.encryption;

import java.io.Serializable;

/**
 * Composite identifier for an {@link EncryptionKeyStatus}.
 * @author IanBrown
 *
 * @since Nov 8, 2012
 * @version Nov 8, 2012
 */
public class EncryptionKeyStatusId implements Serializable {
	
	/**
	 * the serial version identifier for the class.
	 * @author IanBrown
	 * @since Nov 8, 2012
	 * @version Nov 8, 2012
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the date string.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	private String date;

	/**
	 * the state abbreviation.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	private String state;

	/**
	 * the optional voting region.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	private String votingRegion;

	/**
	 * Gets the date.
	 * 
	 * @author IanBrown
	 * @return the date.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Gets the state.
	 * 
	 * @author IanBrown
	 * @return the state.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public String getState() {
		return state;
	}

	/**
	 * Gets the voting region.
	 * 
	 * @author IanBrown
	 * @return the voting region.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public String getVotingRegion() {
		return votingRegion;
	}

	/**
	 * Sets the date.
	 * 
	 * @author IanBrown
	 * @param date
	 *            the date to set.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * Sets the state.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state to set.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public void setState(final String state) {
		this.state = state;
	}

	/**
	 * Sets the voting region.
	 * 
	 * @author IanBrown
	 * @param votingRegion
	 *            the voting region to set.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public void setVotingRegion(final String votingRegion) {
		this.votingRegion = votingRegion;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EncryptionKeyStatusId)) return false;

        EncryptionKeyStatusId that = (EncryptionKeyStatusId) o;

        if (!date.equals(that.date)) return false;
        if (!state.equals(that.state)) return false;
        if (!votingRegion.equals(that.votingRegion)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + votingRegion.hashCode();
        return result;
    }
}
