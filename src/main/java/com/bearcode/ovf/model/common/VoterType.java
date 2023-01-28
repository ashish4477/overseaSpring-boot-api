package com.bearcode.ovf.model.common;

public enum VoterType {
	DOMESTIC_VOTER("US-based Domestic Voter", "Domestic"),
	OVERSEAS_VOTER("Overseas Voter", "Overseas"),
	MILITARY_VOTER("Military Voter", "Military"),
	UNSPECIFIED("Unspecified", "Unspecified");

	private final String title;
	private final String zohoValue;

	VoterType(String title, String zohoValue){
		this.title = title;
		this.zohoValue = zohoValue;
	}

	public String getTitle(){
		return title;
	}

	public String getZohoValue() {
		return zohoValue;
	}

	public String getName(){
		return name();
	}
	
}
