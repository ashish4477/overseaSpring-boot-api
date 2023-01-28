/**
 * 
 */
package com.bearcode.ovf.model.encryption;

/**
 * Class indicating the status of an encryption key.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Nov 8, 2012
 */
public class EncryptionKeyStatus {

	/**
	 * the identifier for this encryption key status.
	 * 
	 * @author IanBrown
	 * @since Nov 8, 2012
	 * @version Nov 8, 2012
	 */
	private EncryptionKeyStatusId id;

	/**
	 * the status of the key.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private boolean status;

	/**
	 * Gets the date.
	 * 
	 * @author IanBrown
	 * @return the date.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public String getDate() {
		return getId() == null ? null : getId().getDate();
	}

	/**
	 * Gets the id.
	 * 
	 * @author IanBrown
	 * @return the id.
	 * @since Nov 8, 2012
	 * @version Nov 8, 2012
	 */
	public EncryptionKeyStatusId getId() {
		return id;
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
		return getId() == null ? null : getId().getState();
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
		return getId() == null ? null : getId().getVotingRegion();
	}

	/**
	 * Gets the status.
	 * 
	 * @author IanBrown
	 * @return the status.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public boolean isStatus() {
		return status;
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
		if (getId() == null) {
			setId(new EncryptionKeyStatusId());
		}
		getId().setDate(date);
	}

	/**
	 * Sets the id.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the id to set.
	 * @since Nov 8, 2012
	 * @version Nov 8, 2012
	 */
	public void setId(final EncryptionKeyStatusId id) {
		this.id = id;
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
		if (getId() == null) {
			setId(new EncryptionKeyStatusId());
		}
		getId().setState(state);
	}

	/**
	 * Sets the status.
	 * 
	 * @author IanBrown
	 * @param status
	 *            the status to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public void setStatus(final boolean status) {
		this.status = status;
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
		if (getId() == null) {
			setId(new EncryptionKeyStatusId());
		}
		getId().setVotingRegion(votingRegion);
	}
}
