package com.bearcode.ovf.model.common;

/**
 * Created by IntelliJ IDEA.
 * User: dhughes
 * Date: Mar 1, 2011
 * Time: 5:38:48 PM
 */
public enum VoterHistory {
    OVERSEAS_VOTER("I have voted as an overseas voter"),
    DOMESTIC_VOTER("I have voted as a domestic voter"),
    UNIFORMED_SERVICES_VOTER("I have voted as a uniformed services voter"),
    OVERSEAS_AND_DOMESTIC_VOTER("I have voted as a domestic and an overseas voter"),
    FIRST_TIME_VOTER("I am a first time voter"),
    UNIFORMED_SERVICES_AND_DOMESTIC_VOTER("I have voted as a uniformed services & domestic voter"),
    NOT_SURE("I don't know or remember if I've ever voted before.");

	private String strValue;

	VoterHistory(String stringVal){
		this.strValue = stringVal;
	}

	public String getValue(){
		return strValue;
	}

	public String getName(){
		return name();
	}
	
}
