package com.bearcode.ovf.model.eod;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author Leonid Ginzburg
 */
public class DeliveryOptions {
	private boolean post;
	private boolean email;
	private boolean fax;
	private boolean tel;

	/**
	 * in-person delivery option.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	private boolean inPerson;

	public boolean checkEmpty(final DeliveryOptions options) {
		return this.inPerson == options.isInPerson() && this.post == options.isPost() && this.fax == options.isFax()
				&& this.email == options.isEmail() && this.tel == options.isTel();
	}

	public boolean isEmail() {
		return email;
	}

	public boolean isFax() {
		return fax;
	}

	/**
	 * Gets the in-person flag.
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if delivery in-person is acceptable, <code>false</code> otherwise.
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	public boolean isInPerson() {
		return inPerson;
	}

	public boolean isPost() {
		return post;
	}

	public boolean isTel() {
		return tel;
	}

	public void setEmail(final boolean email) {
		this.email = email;
	}

	public void setFax(final boolean fax) {
		this.fax = fax;
	}

	/**
	 * Sets the in-person flag.
	 * 
	 * @author IanBrown
	 * @param inPerson
	 *            <code>true</code> if delivery in-person is acceptable, <code>false</code> otherwise.
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	public void setInPerson(final boolean inPerson) {
		this.inPerson = inPerson;
	}

	public void setPost(final boolean post) {
		this.post = post;
	}

	public void setTel(final boolean tel) {
		this.tel = tel;
	}

	public void updateFrom(final DeliveryOptions options) {
		this.inPerson = options.isInPerson();
		this.post = options.isPost();
		this.fax = options.isFax();
		this.email = options.isEmail();
		this.tel = options.isTel();
	}
}
