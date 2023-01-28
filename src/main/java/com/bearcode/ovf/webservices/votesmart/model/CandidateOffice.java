package com.bearcode.ovf.webservices.votesmart.model;

import java.util.List;

public class CandidateOffice {
	private String parties;
	private String title;
	private String shortTitle;
	private String name;
	private String type;
	private String status;
	private String firstElect;
	private String lastElect;
	private String nextElect;
	private String termStart;
	private String termEnd;
	private String district;
	private String stateId;
	private List<Committee> committees;
	private List<Election> elections;
	
	public String getParties() {
		return parties;
	}
	public void setParties(String parties) {
		this.parties = parties;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFirstElect() {
		return firstElect;
	}
	public void setFirstElect(String firstElect) {
		this.firstElect = firstElect;
	}
	public String getLastElect() {
		return lastElect;
	}
	public void setLastElect(String lastElect) {
		this.lastElect = lastElect;
	}
	public String getNextElect() {
		return nextElect;
	}
	public void setNextElect(String nextElect) {
		this.nextElect = nextElect;
	}
	public String getTermStart() {
		return termStart;
	}
	public void setTermStart(String termStart) {
		this.termStart = termStart;
	}
	public String getTermEnd() {
		return termEnd;
	}
	public void setTermEnd(String termEnd) {
		this.termEnd = termEnd;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	public List<Committee> getCommittees() {
		return committees;
	}
	public void setCommittees(List<Committee> committees) {
		this.committees = committees;
	}
	public List<Election> getElections() {
		return elections;
	}
	public void setElections(List<Election> elections) {
		this.elections = elections;
	}
}
