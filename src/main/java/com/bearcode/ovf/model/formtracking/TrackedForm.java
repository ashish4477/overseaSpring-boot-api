/**
 * 
 */
package com.bearcode.ovf.model.formtracking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.FlowType;

/**
 * Domain object to assist a voter in tracking his/her form after it is submitted.
 * 
 * @author IanBrown
 * 
 * @since Apr 25, 2012
 * @version Apr 25, 2012
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "tracked_forms")
public class TrackedForm {

	/**
	 * the identifier for the tracked form.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * the first name of the voter.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Column(name = "first_name")
	private String firstName;

	/**
	 * the last name of the voter.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Column(name = "last_name")
	private String lastName;

	/**
	 * the email address of the voter.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Column(name = "email_address")
	private String emailAddress;

	/**
	 * the type of flow.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Column(name = "flow_type")
	@Enumerated(EnumType.STRING)
	private FlowType flowType;

	/**
	 * the face used to create the form.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "face_id")
	private FaceConfig face;

	/**
	 * the last date that an email was sent.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_email_date")
	private Date lastEmailDate;

	/**
	 * the number of email messages that have been sent.
	 * 
	 * @author IanBrown
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	@Column(name = "number_email")
	private int numberOfEmailSent;

	/**
	 * Gets the eMail address.
	 * 
	 * @author IanBrown
	 * @return the eMail address.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Gets the face.
	 * 
	 * @author IanBrown
	 * @return the face.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public FaceConfig getFace() {
		return face;
	}

	/**
	 * Gets the first name.
	 * 
	 * @author IanBrown
	 * @return the first name.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the flow type.
	 * 
	 * @author IanBrown
	 * @return the flow type.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public FlowType getFlowType() {
		return flowType;
	}

	/**
	 * Gets the identifier.
	 * 
	 * @author IanBrown
	 * @return the identifier.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the last eMail date.
	 * 
	 * @author IanBrown
	 * @return the last eMail date.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public Date getLastEmailDate() {
		return lastEmailDate;
	}

	/**
	 * Gets the last name.
	 * 
	 * @author IanBrown
	 * @return the last name.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the number of eMail sent.
	 * 
	 * @author IanBrown
	 * @return the number of eMail sent.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public int getNumberOfEmailSent() {
		return numberOfEmailSent;
	}

	/**
	 * Sets the eMail address.
	 * 
	 * @author IanBrown
	 * @param emailAddress
	 *            the eMail address to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Sets the face.
	 * 
	 * @author IanBrown
	 * @param face
	 *            the face to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setFace(final FaceConfig face) {
		this.face = face;
	}

	/**
	 * Sets the first name.
	 * 
	 * @author IanBrown
	 * @param firstName
	 *            the first name to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the flow type.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setFlowType(final FlowType flowType) {
		this.flowType = flowType;
	}

	/**
	 * Sets the identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the last eMail date.
	 * 
	 * @author IanBrown
	 * @param lastEmailDate
	 *            the last eMail date to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setLastEmailDate(final Date lastEmailDate) {
		this.lastEmailDate = lastEmailDate;
	}

	/**
	 * Sets the last name.
	 * 
	 * @author IanBrown
	 * @param lastName
	 *            the last name to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the numberOfEmailSent.
	 * 
	 * @author IanBrown
	 * @param numberOfEmailSent
	 *            the numberOfEmailSent to set.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public void setNumberOfEmailSent(final int numberOfEmailSent) {
		this.numberOfEmailSent = numberOfEmailSent;
	}
}
