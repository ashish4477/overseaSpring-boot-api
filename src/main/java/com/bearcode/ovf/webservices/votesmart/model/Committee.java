package com.bearcode.ovf.webservices.votesmart.model;

public class Committee {
	private String committeeId;
	private String committeeName;
	
	public Committee(String id, String name) {
		this.committeeId = id;
		this.committeeName = name;
	}
	public String getCommitteeId() {
		return committeeId;
	}
	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}
	public String getCommitteeName() {
		return committeeName;
	}
	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}
}
