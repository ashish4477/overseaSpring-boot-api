package com.bearcode.ovf.model.email;

import java.util.Date;

/**
 * Keep track of sent emails
 * 
 * @author pavel
 *
 */
public class RawEmailLog {
	
	private long id;
	private Date created;
	private RawEmail rawEmail;
	private RawEmailLogStatus status = RawEmailLogStatus.INITIAL;
    private String error;
    
    	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}

	public RawEmail getRawEmail() {
		return rawEmail;
	}
	public void setRawEmail(RawEmail rawEmail) {
		this.rawEmail = rawEmail;
	}
	public RawEmailLogStatus getStatus() {
		return status;
	}
	public void setStatus(RawEmailLogStatus status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
  
}
