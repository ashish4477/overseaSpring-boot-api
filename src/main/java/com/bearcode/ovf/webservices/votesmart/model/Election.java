package com.bearcode.ovf.webservices.votesmart.model;

public class Election {
	private String officeId;
	private String office;
	private String officeType;
	private String electionParties;
	private String district;
	private String status;
	private String ballotName;
	
	public Election(String id, String name, String type, String parties, String district, String status, String ballotName) {
		this.officeId = id;
		this.office = name;
		this.officeType = type;
		this.electionParties = parties;
		this.district = district;
		this.status = status;
		this.ballotName = ballotName;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getOfficeType() {
		return officeType;
	}
	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}
	public String getParties() {
		return electionParties;
	}
	public void setParties(String parties) {
		this.electionParties = parties;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBallotName() {
		return ballotName;
	}
	public void setBallotName(String ballotName) {
		this.ballotName = ballotName;
	}
}
